import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';
import 'package:s2toperational/Modules/Json_Class/UserAttendancesUsingSitedetailsIDResponse/UserAttendancesUsingSitedetailsIDResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/health_screening_details/repository/health_screening_repository.dart';

class UrineSampleCollectionController extends GetxController {
  final UserAttendancesUsingSitedetailsIDOutput patientItem;
  final int campId;

  UrineSampleCollectionController({
    required this.patientItem,
    required this.campId,
  });

  final HealthScreeningRepository _repo = HealthScreeningRepository();

  final TextEditingController sampleCountCtrl = TextEditingController();
  final TextEditingController barcodeCtrl = TextEditingController();
  final TextEditingController remarkCtrl = TextEditingController();

  // 0 = none selected, 1 = collected, 2 = not collected
  final RxInt sampleStatus = 0.obs;
  final RxBool isSubmitting = false.obs;

  bool get showRemarks => sampleStatus.value == 2;

  bool get showSubmit => sampleStatus.value != 0;

  bool get barcodeLocked =>
      (patientItem.barcode1 ?? '').trim().isNotEmpty &&
      (patientItem.barcode1 ?? '').trim().toLowerCase() != 'null';

  @override
  void onInit() {
    super.onInit();
    sampleCountCtrl.text = '1';
    if (barcodeLocked) {
      barcodeCtrl.text = patientItem.barcode1!.trim();
    }
  }

  @override
  void onClose() {
    sampleCountCtrl.dispose();
    barcodeCtrl.dispose();
    remarkCtrl.dispose();
    super.onClose();
  }

  void onStatusChanged(int? value) {
    if (value != null) sampleStatus.value = value;
  }

  Future<void> scanBarcode(BuildContext context) async {
    final res = await SimpleBarcodeScanner.scanBarcode(
      context,
      barcodeAppBar: const BarcodeAppBar(
        appBarTitle: 'Scan Barcode',
        centerTitle: false,
        enableBackButton: true,
        backButtonIcon: Icon(Icons.arrow_back_ios),
      ),
      isShowFlashIcon: true,
      delayMillis: 2000,
      cameraFace: CameraFace.back,
    );
    if (res == null || res == '-1') return;
    barcodeCtrl.text = res;
  }

  Future<void> validateAndSubmit(BuildContext context) async {
    final sampleCount = sampleCountCtrl.text.trim();
    final barcode = barcodeCtrl.text.trim();
    final remark = remarkCtrl.text.trim();

    if (sampleCount.isEmpty) {
      ToastManager.toast('Please enter sample count.');
      return;
    }
    if (barcode.isEmpty) {
      ToastManager.toast('Please enter barcode.');
      return;
    }
    if (sampleStatus.value == 0) {
      ToastManager.toast('Please select sample status.');
      return;
    }
    if (sampleStatus.value == 2 && remark.isEmpty) {
      ToastManager.toast(
        'Please enter reason, for not collecting urine sample.',
      );
      return;
    }

    final label =
        sampleStatus.value == 1
            ? 'you have Collected Sample'
            : 'you have Not Collected Sample';

    if (!context.mounted) return;
    ToastManager().showConfirmationDialog(
      context: Get.context!,
      message: 'Are you sure, $label?',
      didSelectYes: (bool p1) {
        if (p1 == true) {
          Get.back();

          _submit(context, barcode, remark);
        } else if (p1 == false) {
          Get.back();
        }
      },
    );
    // showDialog(
    //   context: context,
    //   builder: (ctx) => AlertDialog(
    //     title: const Text('Confirm'),
    //     content: Text('Are you sure, $label?'),
    //     actions: [
    //       TextButton(
    //         onPressed: () => Navigator.pop(ctx),
    //         child: const Text('No'),
    //       ),
    //       TextButton(
    //         onPressed: () {
    //           Navigator.pop(ctx);
    //           _submit(context, barcode, remark);
    //         },
    //         child: const Text('Yes'),
    //       ),
    //     ],
    //   ),
    // );
  }

  Future<void> _submit(
    BuildContext context,
    String barcode,
    String remark,
  ) async {
    final userId =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    isSubmitting.value = true;
    ToastManager.showLoader();

    final result = await _repo.submitUrineSampleCollection(
      regdId: patientItem.regdId ?? 0,
      campId: campId,
      barcode1: barcode,
      sampleReceiveFlag: sampleStatus.value,
      createdBy: userId,
      remark: sampleStatus.value == 2 ? remark : '',
    );

    ToastManager.hideLoader();
    isSubmitting.value = false;

    if (result == null) {
      ToastManager.toast('Server not responding. Please try again.');
      return;
    }

    final status = (result['status'] ?? '').toString();
    final message = (result['message'] ?? '').toString();

    if (status.toLowerCase() == 'success') {
      if (!context.mounted) return;
      ToastManager().showSuccessOkayDialog(
        context: context,
        title: "Success",
        message: "Sample collection details submitted successfully.",
        onTap: () {
          Get.back();
          Get.back();
        },
      );
      // showDialog(
      //   context: context,
      //   barrierDismissible: false,
      //   builder:
      //       (ctx) => AlertDialog(
      //         title: const Text('Success'),
      //         content: const Text(
      //           'Sample collection details submitted successfully.',
      //         ),
      //         actions: [
      //           TextButton(
      //             onPressed: () {
      //               Navigator.pop(ctx);
      //               Navigator.pop(context);
      //             },
      //             child: const Text('OK'),
      //           ),
      //         ],
      //       ),
      // );
    } else {
      ToastManager.toast(message.isNotEmpty ? message : 'Submission failed.');
    }
  }
}
