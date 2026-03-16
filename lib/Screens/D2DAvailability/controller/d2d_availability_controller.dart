import 'package:get/get.dart';

import '../../../Modules/APIManager/APIManager.dart';
import '../../../Modules/Json_Class/D2DPhysicalExamDetailsResponse/InsertDetailsResponse.dart';
import '../../../Modules/Json_Class/GetDocListD2DResponse/GetDocListD2DResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/utilities/DataProvider.dart';

class D2DAvailabilityController extends GetxController {
  final APIManager apiManager = APIManager();
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

  void fetchCurrentStatus() {
    isLoading.value = true;
    ToastManager.showLoader();
    apiManager.getDoctorListCSCCampAvailableAPI({
      "DocUserid": empCode.toString(),
    }, _onFetchStatus);
  }

  void updateStatus(int status) {
    if (isUpdating.value || selectedStatus.value == status) {
      return;
    }
    isUpdating.value = true;
    ToastManager.showLoader();
    apiManager.insertCscDoctorAvailabilityStatusAPI({
      "DocUserid": empCode.toString(),
      "DocStatus": status.toString(),
      "Createdby": empCode.toString(),
    }, (InsertDetailsResponse? response, String errorMessage, bool success) {
      ToastManager.hideLoader();
      isUpdating.value = false;
      if (success) {
        selectedStatus.value = status;
        final message = response?.message ?? "";
        if (message.isNotEmpty) {
          ToastManager.toast(message);
        }
      } else {
        ToastManager.toast(
          errorMessage.isNotEmpty
              ? errorMessage
              : (response?.message ?? "Unable to update status"),
        );
      }
    });
  }

  void _onFetchStatus(
    GetDocListD2DResponse? response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();
    isLoading.value = false;
    if (!success || response == null) {
      ToastManager.toast(errorMessage);
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
}
