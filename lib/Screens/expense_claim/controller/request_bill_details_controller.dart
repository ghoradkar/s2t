// ignore_for_file: avoid_print

import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import '../model/advances_request_details_show_response.dart';
import '../repository/expense_claim_repository.dart';

class RequestBillDetailsController extends GetxController {
  final _repo = ExpenseClaimRepository();

  int campId = 0;
  AdvancesRequestDetailsShowOutput? advancesRequestDetails;

  @override
  void onInit() {
    super.onInit();
    campId = Get.arguments as int? ?? 0;
    loadDetails();
  }

  Future<void> loadDetails() async {
    ToastManager.showLoader();
    try {
      final response = await _repo.fetchBillDetails({'campid': campId.toString()});
      advancesRequestDetails = response.output?.firstOrNull;
    } catch (e) {
      ToastManager.toast(e.toString());
    } finally {
      ToastManager.hideLoader();
      update();
    }
  }
}
