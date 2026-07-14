import 'dart:io';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import 'package:s2toperational/patient_registration/repository/regular_patient_registration_repository.dart';
import 'package:s2toperational/acknowledgement/model/acknowledgement_patient_list_response.dart';

enum RationCardType { manual, digital }

class RationCardAcknowledgementController extends GetxController {
  final AcknowledgementPatientOutput patient;
  final int campId;

  RationCardAcknowledgementController({
    required this.patient,
    required this.campId,
  });

  final _repo = RegularPatientRegistrationRepository();
  final _picker = ImagePicker();

  final Rx<RationCardType> rationCardType = RationCardType.manual.obs;
  final RxList<File> capturedPhotos = <File>[].obs;
  final RxBool isLoading = false.obs;

  int empCode = 0;

  @override
  void onInit() {
    super.onInit();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
  }

  int get maxPhotos => rationCardType.value == RationCardType.manual ? 2 : 1;

  void setRationCardType(RationCardType type) {
    rationCardType.value = type;
    capturedPhotos.clear();
  }

  Future<void> capturePhoto() async {
    if (capturedPhotos.length >= maxPhotos) {
      final label = rationCardType.value == RationCardType.manual
          ? 'Manual requires exactly 2 photos'
          : 'Digital requires exactly 1 photo';
      ToastManager.toast(label);
      return;
    }

    var status = await Permission.camera.status;
    if (status.isDenied) status = await Permission.camera.request();
    if (!status.isGranted) {
      ToastManager.toast('Camera permission denied');
      return;
    }

    final picked = await _picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked != null) {
      capturedPhotos.add(File(picked.path));
    }
  }

  void deletePhoto(int index) {
    if (index >= 0 && index < capturedPhotos.length) {
      capturedPhotos.removeAt(index);
    }
  }

  Future<void> uploadRationCard() async {
    if (capturedPhotos.length < maxPhotos) {
      final required = maxPhotos;
      final label =
          rationCardType.value == RationCardType.manual ? 'Manual' : 'Digital';
      ToastManager.toast(
          '$label Ration Card requires $required photo${required > 1 ? 's' : ''}');
      return;
    }

    isLoading.value = true;
    bool allSuccess = true;

    for (int i = 0; i < capturedPhotos.length; i++) {
      final result = await _repo.insertRationCardDetails(
        regdId: patient.regdId?.toString() ?? '',
        empCode: empCode.toString(),
        bocwDependentId: patient.bocwIdDepend?.toString() ?? '0',
        rationCardNo: patient.rationCardNo ?? '',
        photoFile: capturedPhotos[i],
        rcId: i.toString(),
      );

      if (result == null ||
          result['status']?.toString().toLowerCase() != 'success') {
        allSuccess = false;
        final msg = result?['message']?.toString() ??
            'Failed to upload photo ${i + 1}';
        ToastManager.toast(msg);
        break;
      }
    }

    isLoading.value = false;

    if (allSuccess) {
      ToastManager.toast('Ration Card uploaded successfully');
      Get.back();
    }
  }

  String get genderLabel {
    switch (patient.gender?.toUpperCase()) {
      case 'M':
        return 'Male';
      case 'F':
        return 'Female';
      default:
        return patient.gender ?? '-';
    }
  }
}
