import 'dart:async';

import 'package:get/get.dart';

import '../../../Modules/APIManager/APIManager.dart';
import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/CampWiseInvoiceDetailsResponse/CampWiseInvoiceDetailsResponse.dart';
import '../../../Modules/Json_Class/MonthWiseInvoiceResponse/MonthWiseInvoiceResponse.dart';
import '../../../Modules/Json_Class/VerificationRemarkResponse/VerificationRemarkResponse.dart';
import '../../../Modules/Json_Class/YearsResponse/YearsResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/utilities/DataProvider.dart';

class RaiseInvoiceController extends GetxController {
  RaiseInvoiceController({required this.invoiceObj});

  final MonthWiseInvoiceOutput invoiceObj;
  final APIManager apiManager = APIManager();

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
    fetchCampWiseInvoiceDetails();
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
    Map<String, String> params = {
      "UserInviceID": userInvoiceId,
      "InvoiceYear": invoiceObj.invoiceYear ?? "0",
      "InvoiceMonth": invoiceObj.month?.toString() ?? "0",
      "UserID": empCode.toString(),
      "CampType": selectedServiceType.value?.yearID.toString() ?? "0",
      "LoginUserID": empCode.toString(),
    };

    if (isDoctor) {
      apiManager.getCampWiseDoctorInvoiceDetailsAPI(
        params,
        _onCampWiseInvoiceResponse,
      );
    } else {
      apiManager.getCampWiseInvoiceDetailsAPI(
        params,
        _onCampWiseInvoiceResponse,
      );
    }
  }

  void _onCampWiseInvoiceResponse(
    CampWiseInvoiceDetailsResponse? response,
    String errorMessage,
    bool success,
  ) {
    if (success) {
      campList.value = response?.output ?? <CampWiseInvoiceDetailsOutput>[];
      _recalculateTotals();
    } else {
      campList.clear();
      _recalculateTotals();
      ToastManager.toast(errorMessage);
    }
    isLoading.value = false;
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

  Future<List<VerificationRemarkOutput>> fetchVerificationRemarks() {
    final completer = Completer<List<VerificationRemarkOutput>>();
    ToastManager.showLoader();
    apiManager.getPaymentVerificationRemarkAPI((
      VerificationRemarkResponse? response,
      String errorMessage,
      bool success,
    ) {
      ToastManager.hideLoader();
      if (success) {
        completer.complete(response?.output ?? <VerificationRemarkOutput>[]);
      } else {
        ToastManager.toast(errorMessage);
        completer.complete(<VerificationRemarkOutput>[]);
      }
    });
    return completer.future;
  }

  Future<String?> requestOtp(int paymentStatusId) {
    final completer = Completer<String?>();
    otpNumber = FormatterManager.generateRandomDigits(5);
    if (isDoctor) {
      Map<String, String> param = {
        "UserID": empCode.toString(),
        "InvoiceYear": invoiceObj.invoiceYear ?? "0",
        "InvoiceMonth": invoiceObj.month?.toString() ?? "0",
        "OTP": otpNumber,
        "CreatedBy": empCode.toString(),
        // "MOBNO": "8830378568",
        // "MOBNO": "9673974373",
        "MOBNO": bMobile,
        "SubOrgID": subOrgId.toString(),
      };
      apiManager.getDoctorInvoiceOTPDetailsAPI(param, (
        dynamic response,
        String errorMessage,
        bool success,
      ) {
        if (success) {
          ToastManager.toast("OTP sent successfully on $bMobile");
          completer.complete(otpNumber);
        } else {
          ToastManager.toast(errorMessage);
          completer.complete(null);
        }
      });
    } else {
      Map<String, String> param = {
        "PaymentStatusID": paymentStatusId.toString(),
        "UserID": empCode.toString(),
        "UserInviceID": invoiceObj.userInviceID?.toString() ?? "0",
        "OTP": otpNumber,
        "CreatedBy": empCode.toString(),
        // "MOBNO": "9673974373",
        // "MOBNO": "8830378568",
        "MOBNO": bMobile,
        "SubOrgID": subOrgId.toString(),
      };
      apiManager.getOTPForMedicineDeliveryOrgAPI(param, (
        dynamic response,
        String errorMessage,
        bool success,
      ) {
        if (success) {
          ToastManager.toast("OTP sent successfully on $bMobile");
          completer.complete(otpNumber);
        } else {
          ToastManager.toast(errorMessage);
          completer.complete(null);
        }
      });
    }
    return completer.future;
  }

  Future<void> submitRaiseInvoice(String otp) async {
    ToastManager.showLoader();
    if (isDoctor) {
      Map<String, String> param = {
        "PaymentStatusID": "2",
        "UserID": empCode.toString(),
        "InvoiceYear": invoiceObj.invoiceYear ?? "0",
        "InvoiceMonth": invoiceObj.month?.toString() ?? "0",
        "OTP": otp,
        "VerificationRemarkID": "0",
        "CreatedBy": empCode.toString(),
        "OtherRemark": "",
        "UserInviceID": invoiceObj.userInviceID?.toString() ?? "0",
      };
      apiManager.insertDoctorInvoicePaymentStatusAPI(
        param,
        _onRaiseInvoiceResponse,
      );
    } else {
      Map<String, String> param = {
        "PaymentStatusID": "2",
        "UserID": empCode.toString(),
        "UserInviceID": invoiceObj.userInviceID?.toString() ?? "0",
        "OTP": otp,
        "VerificationRemarkID": "0",
        "OtherRemark": "",
        "CreatedBy": empCode.toString(),
      };
      apiManager.insertInvoicePaymentStatusAPI(
        param,
        _onRaiseInvoiceResponse,
      );
    }
  }

  Future<void> submitSendForVerification(
    String otp,
    int remarkId,
    String otherRemark,
  ) async {
    ToastManager.showLoader();
    if (isDoctor) {
      Map<String, String> param = {
        "PaymentStatusID": "3",
        "UserID": empCode.toString(),
        "InvoiceYear": invoiceObj.invoiceYear ?? "0",
        "InvoiceMonth": invoiceObj.month?.toString() ?? "0",
        "OTP": otp,
        "VerificationRemarkID": remarkId.toString(),
        "CreatedBy": empCode.toString(),
        "OtherRemark": otherRemark,
        "UserInviceID": invoiceObj.userInviceID?.toString() ?? "0",
      };
      apiManager.insertDoctorInvoicePaymentStatusAPI(
        param,
        _onSendForVerificationResponse,
      );
    } else {
      Map<String, String> param = {
        "PaymentStatusID": "3",
        "UserID": empCode.toString(),
        "UserInviceID": invoiceObj.userInviceID?.toString() ?? "0",
        "OTP": otp,
        "VerificationRemarkID": remarkId.toString(),
        "OtherRemark": otherRemark,
        "CreatedBy": empCode.toString(),
      };
      apiManager.insertInvoicePaymentStatusAPI(
        param,
        _onSendForVerificationResponse,
      );
    }
  }

  void _onRaiseInvoiceResponse(
    CampWiseInvoiceDetailsResponse? response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (success) {
      ToastManager.toast("Invoice Raised Successfully");
      final navigator = Get.key.currentState;
      if (navigator != null && navigator.canPop()) {
        Get.back();
      }
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void _onSendForVerificationResponse(
    CampWiseInvoiceDetailsResponse? response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (success) {
      ToastManager.toast("Send For Verification successfully");
      final navigator = Get.key.currentState;
      if (navigator != null && navigator.canPop()) {
        Get.back();
      }
    } else {
      ToastManager.toast(errorMessage);
    }
  }
}
