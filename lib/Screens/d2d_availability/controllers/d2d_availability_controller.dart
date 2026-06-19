import 'package:get/get.dart';

import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../models/get_doc_list_d2d_response.dart';
import '../repository/d2d_availability_repository.dart';

class D2DAvailabilityController extends GetxController {
  final D2DAvailabilityRepository _repository = D2DAvailabilityRepository();

  final RxInt selectedStatus = 0.obs;
  final RxBool isUpdating = false.obs;
  final RxBool isLoading = false.obs;

  late final int empCode;

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    fetchCurrentStatus();
  }

  Future<void> fetchCurrentStatus() async {
    isLoading.value = true;
    ToastManager.showLoader();

    final response = await _repository.fetchDoctorStatus(empCode);
    ToastManager.hideLoader();
    isLoading.value = false;

    if (response == null) {
      ToastManager.toast("Failed to fetch availability status");
      selectedStatus.value = 0;
      return;
    }

    int status = 0;
    final output = response.output ?? <GetDocListD2DOutput>[];
    for (final item in output) {
      if (item.userId == empCode) {
        status = item.docStatus ?? 0;
        break;
      }
    }
    selectedStatus.value = status == 1 ? 1 : 0;
  }

  Future<void> updateStatus(int status) async {
    if (isUpdating.value || selectedStatus.value == status) return;
    isUpdating.value = true;
    ToastManager.showLoader();

    final response = await _repository.updateDoctorStatus(empCode, status);
    ToastManager.hideLoader();
    isUpdating.value = false;

    if (response != null && response.status?.toLowerCase() == 'success') {
      selectedStatus.value = status;
      final message = response.message ?? '';
      if (message.isNotEmpty) ToastManager.toast(message);
    } else {
      ToastManager.toast(response?.message ?? 'Unable to update status');
    }
  }
}
