import 'package:flutter/scheduler.dart';
import 'package:get/get.dart';

import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../models/camp_wise_invoice_model.dart';
import '../models/month_wise_invoice_model.dart';
import '../models/verification_remark_model.dart';
import '../../../Screens/d2d_physical_examination/model/YearsResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../repository/invoice_repository.dart';

class RaiseInvoiceController extends GetxController {
  RaiseInvoiceController({required this.invoiceObj});

  final MonthWiseInvoiceOutput invoiceObj;
  final _repo = InvoiceRepository();

  final Rxn<YearsOutput> selectedServiceType = Rxn<YearsOutput>();
  final RxList<CampWiseInvoiceDetailsOutput> campList =
      <CampWiseInvoiceDetailsOutput>[].obs;
  final RxBool isLoading = false.obs;

  final RxDouble totalBeneficiaries = 0.0.obs;
  final RxDouble totalIndividualBillable = 0.0.obs;
  final RxDouble totalIndividualRejected = 0.0.obs;
  final RxDouble totalIndividualBillableWorker = 0.0.obs;
  final RxDouble totalIndividualBillableDependent = 0.0.obs;
  final RxDouble totalIndividualPenaltyAmount = 0.0.obs;

  late final int empCode;
  late final int desigId;
  late final int subOrgId;
  late final String bMobile;
  late final bool isDoctor;

  String otpNumber = "";

  bool get canRaiseInvoice =>
      !(invoiceObj.invoiceApprovedStatus == 1 ||
        invoiceObj.invoiceApprovedStatus == 2);

  bool get canSendForVerification =>
      !(invoiceObj.sendForVerification == 1 ||
        invoiceObj.invoiceApprovedStatus == 1 ||
        invoiceObj.invoiceApprovedStatus == 2);

  bool get isDoctorUser => isDoctor;

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    desigId = user?.dESGID ?? 0;
    subOrgId = user?.subOrgId ?? 0;
    bMobile = user?.bMobile ?? "";
    isDoctor = desigId == 34;
    SchedulerBinding.instance.addPostFrameCallback((_) {
      fetchCampWiseInvoiceDetails();
    });
  }

  List<YearsOutput> getServiceTypeOptions() {
    if (isDoctor) {
      return [
        YearsOutput(yearID: 1, yearName: "REGULAR"),
        YearsOutput(yearID: 3, yearName: "DOOR TO DOOR"),
      ];
    }
    return [
      YearsOutput(yearID: 1, yearName: "REGULAR"),
      YearsOutput(yearID: 3, yearName: "DOOR TO DOOR"),
      YearsOutput(yearID: 6, yearName: "MMU Camp"),
    ];
  }

  void updateServiceType(YearsOutput serviceType) {
    selectedServiceType.value = serviceType;
    fetchCampWiseInvoiceDetails();
  }

  Future<void> fetchCampWiseInvoiceDetails() async {
    isLoading.value = true;
    final userInvoiceId =
        isDoctor ? "0" : (invoiceObj.userInviceID?.toString() ?? "0");
    final params = {
      "UserInviceID": userInvoiceId,
      "InvoiceYear": invoiceObj.invoiceYear ?? "0",
      "InvoiceMonth": invoiceObj.month?.toString() ?? "0",
      "UserID": empCode.toString(),
      "CampType": selectedServiceType.value?.yearID.toString() ?? "0",
      "LoginUserID": empCode.toString(),
    };

    try {
      campList.value = await _repo.fetchCampDetails(
        isDoctor: isDoctor,
        params: params,
      );
    } catch (e) {
      campList.clear();
      ToastManager.toast(e.toString());
    } finally {
      _recalculateTotals();
      isLoading.value = false;
    }
  }

  Future<List<VerificationRemarkOutput>> fetchVerificationRemarks() async {
    ToastManager.showLoader();
    try {
      return await _repo.fetchVerificationRemarks();
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<String?> requestOtp(int paymentStatusId) async {
    otpNumber = FormatterManager.generateRandomDigits(5);
    final Map<String, String> params = isDoctor
        ? {
            "UserID": empCode.toString(),
            "InvoiceYear": invoiceObj.invoiceYear ?? "0",
            "InvoiceMonth": invoiceObj.month?.toString() ?? "0",
            "OTP": otpNumber,
            "CreatedBy": empCode.toString(),
            "MOBNO": bMobile,
            "SubOrgID": subOrgId.toString(),
          }
        : {
            "PaymentStatusID": paymentStatusId.toString(),
            "UserID": empCode.toString(),
            "UserInviceID": invoiceObj.userInviceID?.toString() ?? "0",
            "OTP": otpNumber,
            "CreatedBy": empCode.toString(),
            "MOBNO": bMobile,
            "SubOrgID": subOrgId.toString(),
          };

    try {
      await _repo.requestOtp(isDoctor: isDoctor, params: params);
      ToastManager.toast("OTP sent successfully on $bMobile");
      return otpNumber;
    } catch (e) {
      ToastManager.toast(e.toString());
      return null;
    }
  }

  Future<void> submitRaiseInvoice(String otp) async {
    ToastManager.showLoader();
    final Map<String, String> params = isDoctor
        ? {
            "PaymentStatusID": "2",
            "UserID": empCode.toString(),
            "InvoiceYear": invoiceObj.invoiceYear ?? "0",
            "InvoiceMonth": invoiceObj.month?.toString() ?? "0",
            "OTP": otp,
            "VerificationRemarkID": "0",
            "CreatedBy": empCode.toString(),
            "OtherRemark": "",
            "UserInviceID": invoiceObj.userInviceID?.toString() ?? "0",
          }
        : {
            "PaymentStatusID": "2",
            "UserID": empCode.toString(),
            "UserInviceID": invoiceObj.userInviceID?.toString() ?? "0",
            "OTP": otp,
            "VerificationRemarkID": "0",
            "OtherRemark": "",
            "CreatedBy": empCode.toString(),
          };

    try {
      await _repo.submitInvoiceStatus(isDoctor: isDoctor, params: params);
      ToastManager.hideLoader();
      ToastManager.toast("Invoice Raised Successfully");
      final navigator = Get.key.currentState;
      if (navigator != null && navigator.canPop()) Get.back();
    } catch (e) {
      ToastManager.hideLoader();
      ToastManager.toast(e.toString());
    }
  }

  Future<void> submitSendForVerification(
    String otp,
    int remarkId,
    String otherRemark,
  ) async {
    ToastManager.showLoader();
    final Map<String, String> params = isDoctor
        ? {
            "PaymentStatusID": "3",
            "UserID": empCode.toString(),
            "InvoiceYear": invoiceObj.invoiceYear ?? "0",
            "InvoiceMonth": invoiceObj.month?.toString() ?? "0",
            "OTP": otp,
            "VerificationRemarkID": remarkId.toString(),
            "CreatedBy": empCode.toString(),
            "OtherRemark": otherRemark,
            "UserInviceID": invoiceObj.userInviceID?.toString() ?? "0",
          }
        : {
            "PaymentStatusID": "3",
            "UserID": empCode.toString(),
            "UserInviceID": invoiceObj.userInviceID?.toString() ?? "0",
            "OTP": otp,
            "VerificationRemarkID": remarkId.toString(),
            "OtherRemark": otherRemark,
            "CreatedBy": empCode.toString(),
          };

    try {
      await _repo.submitInvoiceStatus(isDoctor: isDoctor, params: params);
      ToastManager.hideLoader();
      ToastManager.toast("Send For Verification successfully");
      final navigator = Get.key.currentState;
      if (navigator != null && navigator.canPop()) Get.back();
    } catch (e) {
      ToastManager.hideLoader();
      ToastManager.toast(e.toString());
    }
  }

  void _recalculateTotals() {
    totalBeneficiaries.value = campList.fold(
      0.0,
      (sum, item) => sum + (item.totalBeneficiaries ?? 0.0),
    );
    totalIndividualBillable.value = campList.fold(
      0.0,
      (sum, item) => sum + (item.totalIndividualBillable ?? 0.0),
    );
    totalIndividualRejected.value = campList.fold(
      0.0,
      (sum, item) => sum + (item.totalIndividualRejected ?? 0.0),
    );
    totalIndividualBillableWorker.value = campList.fold(
      0.0,
      (sum, item) => sum + _workerCount(item),
    );
    totalIndividualBillableDependent.value = campList.fold(
      0.0,
      (sum, item) => sum + _dependentCount(item),
    );
    totalIndividualPenaltyAmount.value = campList.fold(
      0.0,
      (sum, item) => sum + (item.individualPenaltyAmount ?? 0.0),
    );
  }

  double _workerCount(CampWiseInvoiceDetailsOutput item) {
    return isDoctor
        ? (item.billablesSameDay ?? 0.0)
        : (item.individualBillableWorker ?? 0.0);
  }

  double _dependentCount(CampWiseInvoiceDetailsOutput item) {
    return isDoctor
        ? (item.billablesAnotherDay ?? 0.0)
        : (item.individualBillableDependent ?? 0.0);
  }
}
