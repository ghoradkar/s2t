// ignore_for_file: avoid_print

import 'dart:async';
import 'dart:io';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/medicine_delivery/view/FaceDetectionScreen.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/medicine_delivery/view/FrontCameraScreen.dart';
import 'package:s2toperational/Modules/Json_Class/PostCampBeneficiaryListResponse/PostCampBeneficiaryListResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:path_provider/path_provider.dart';
import 'package:path/path.dart' as p;

// ── Simple inline models ──────────────────────────────────────────────────────

class DeliveryStatusItem {
  final int id;
  final String name;

  DeliveryStatusItem({required this.id, required this.name});

  factory DeliveryStatusItem.fromJson(Map<String, dynamic> json) =>
      DeliveryStatusItem(
        id: json['DeliveryStatusRemarkID'] ?? 0,
        name: json['StatusRemark'] ?? '',
      );
}

class DeliveryRemarkItem {
  final int id;
  final String name;

  DeliveryRemarkItem({required this.id, required this.name});

  factory DeliveryRemarkItem.fromJson(Map<String, dynamic> json) =>
      DeliveryRemarkItem(
        id: json['DeliveryRemarkID'] ?? 0,
        name: json['DeliveryRemark'] ?? '',
      );
}

// ── Mode enum ─────────────────────────────────────────────────────────────────

enum AckMode { pending, delivered, notAvailable, denied }

// ── Controller ────────────────────────────────────────────────────────────────

class MedicineDeliveryAcknowledgementController extends GetxController {
  final PostCampBeneficiaryOutput beneficiary;

  MedicineDeliveryAcknowledgementController({required this.beneficiary});

  final APIManager _api = APIManager();

  // ── Session ───────────────────────────────────────────────────────────────

  String _userId = '';
  String _orgId = '0';

  // ── Mode ──────────────────────────────────────────────────────────────────

  late final AckMode mode;

  // ── SMS Gateway ───────────────────────────────────────────────────────────

  /// 0 = SMS Striker (smsId "2"), 1 = SMS 91 (smsId "1")
  final RxInt selectedSmsVendorIndex = 0.obs;

  /// Whether the SMS Vendor panel is expanded (native: collapsible section)
  final RxBool smsVendorExpanded = true.obs;

  final List<String> smsVendorOptions = const ['SMS Striker', 'SMS 91'];

  String get selectedSmsVendorName =>
      smsVendorOptions[selectedSmsVendorIndex.value];

  String get smsId => selectedSmsVendorIndex.value == 0 ? '2' : '1';

  void selectSmsVendor(int index) => selectedSmsVendorIndex.value = index;

  // ── Mobile numbers (display only) ────────────────────────────────────────

  String get workerMobileDisplay => beneficiary.workersMob ?? '';

  String get alternateMobileDisplay => beneficiary.alternateMobNo ?? '';

  // ── OTP target number (from API picker) ───────────────────────────────────

  final RxString otpTargetNumber = "8830378568".obs;

  // final RxString otpTargetNumber = ''.obs;
  final RxList<String> mobileNumbersFromApi = <String>[].obs;
  final RxBool isFetchingMobileNumbers = false.obs;

  void fetchMobileNumbers() {
    final regdNo = beneficiary.beneficiryNumber ?? '';
    if (regdNo.isEmpty) {
      ToastManager.toast('Beneficiary number not available');
      return;
    }
    isFetchingMobileNumbers.value = true;
    _api.getMobileNumbersForDeliveryAPI(regdNo, (response, error, success) {
      isFetchingMobileNumbers.value = false;
      if (success && response != null) {
        final output = response['output'] as List? ?? [];
        mobileNumbersFromApi.value =
            output
                .map<String>((e) => (e['MobileNo'] ?? '').toString())
                .where((n) => n.isNotEmpty)
                .toList();
        if (mobileNumbersFromApi.isEmpty) {
          ToastManager.toast('No numbers found');
        }
      } else {
        ToastManager.toast(error.isNotEmpty ? error : 'Failed to load numbers');
      }
    });
  }

  void selectOtpTargetNumber(String number) {
    otpTargetNumber.value = number;
  }

  // ── Face Detection ────────────────────────────────────────────────────────

  /// Whether the face detection toggle section should be shown
  /// (native: shown when faceDetectionCompulsory == "0")
  final RxBool showFaceDetectionSection = false.obs;

  /// true = skip face detection, false = use face detection (default)
  final RxBool skipFaceDetection = false.obs;

  void _fetchFaceDetectionFlag() {
    _api.getFaceDetectionFlagAPI(_userId, (response, error, success) {
      if (success && response != null) {
        final output = response['output'] as List? ?? [];
        if (output.isNotEmpty) {
          final compulsory =
              output.first['IsFaceDetetctionEnabled']?.toString() ?? '1';
          // Show toggle only when faceDetectionCompulsory == "0"
          showFaceDetectionSection.value = compulsory == '0';
        }
      }
    });
  }

  // ── OTP ───────────────────────────────────────────────────────────────────

  final RxBool isOtpSending = false.obs;
  final RxBool isOtpVerifying = false.obs;
  final RxBool isOtpVerified = false.obs;
  final RxString otpTimerText = ''.obs;
  final RxBool canResendOtp = false.obs;

  final TextEditingController otpController = TextEditingController();

  // final TextEditingController deliveryChallanNoController =
  //     TextEditingController();
  String _generatedOtp = '';

  Timer? _otpTimer;

  sendOtp(BuildContext context) {
    final mobile = otpTargetNumber.value;
    if (mobile.isEmpty) {
      // ToastManager.toast('Please select a number to send OTP');
      ToastManager.showAlertDialog(
        context,
        'Please select a number to send OTP',
        () {
          Navigator.pop(context);
        },
      );
      return;
    }

    final rnd = Random();
    _generatedOtp =
        ((1 + rnd.nextInt(2)) * 10000 + rnd.nextInt(10000)).toString();

    final params = {
      'MOBNO': mobile,
      'OTP': _generatedOtp,
      'RegdId': beneficiary.regdId?.toString() ?? '0',
      'CreatedBy': _userId,
      'ReportDeliveredBY': _userId,
      'SubOrgID': _orgId,
      'Option': smsId,
    };

    isOtpSending.value = true;
    _api.sendOTPForMedicineDeliveryAPI(params, (response, error, success) {
      isOtpSending.value = false;
      if (success) {
        ToastManager.toast('OTP sent on $mobile');
        _startOtpTimer();
      } else {
        ToastManager.toast(error.isNotEmpty ? error : 'Failed to send OTP');
      }
    });
  }

  void _startOtpTimer() {
    canResendOtp.value = false;
    int seconds = 120;
    otpTimerText.value = 'Resend OTP in: ${seconds}s';
    _otpTimer?.cancel();
    _otpTimer = Timer.periodic(const Duration(seconds: 1), (t) {
      seconds--;
      if (seconds <= 0) {
        t.cancel();
        otpTimerText.value = '';
        canResendOtp.value = true;
      } else {
        otpTimerText.value = 'Resend OTP in: ${seconds}s';
      }
    });
  }

  void verifyOtp(BuildContext context) {
    final enteredOtp = otpController.text.trim();
    if (enteredOtp.isEmpty) {
      ToastManager.toast('Please enter OTP');
      return;
    }

    final mobile = otpTargetNumber.value;
    final params = {'MOBNO': mobile, 'OTP': enteredOtp};

    isOtpVerifying.value = true;
    _api.verifyOTPForMedicineDeliveryAPI(params, (response, error, success) {
      isOtpVerifying.value = false;
      if (success) {
        _otpTimer?.cancel();
        otpTimerText.value = '';
        canResendOtp.value = false;
        // Show success dialog (native: AlertDialog "OTP Verification successfully")
        // Set isOtpVerified after user taps OK — same as native's onPositiveButton callback.
        // showDialog(
        //   context: context,
        //   barrierDismissible: false,
        //   builder:
        //       (_) => AlertDialog(
        //         title: const Text('Success'),
        //         content: const Text('OTP Verification successfully'),
        //         actions: [
        //           TextButton(
        //             onPressed: () {
        //               Navigator.of(context).pop();
        //               isOtpVerified.value = true;
        //             },
        //             child: const Text('OK'),
        //           ),
        //         ],
        //       ),
        // );

        ToastManager().showSuccessOkayDialog(
          context: context,
          title: 'Success',
          message: 'OTP Verification successfully',
          onTap: () {
            Navigator.of(context).pop();
            isOtpVerified.value = true;
          },
        );
      } else {
        ToastManager.toast(error.isNotEmpty ? error : 'Invalid OTP');
      }
    });
  }

  // ── Photos ────────────────────────────────────────────────────────────────

  final Rx<File?> beneficiaryPhotoFile = Rx<File?>(null);
  final Rx<File?> consentFormFile = Rx<File?>(null);
  final Rx<File?> deliveryChallanFile = Rx<File?>(null);

  Future<File> _copyToExternalCache(File src, String suffix) async {
    final ext = p.extension(src.path);
    final safeExt = ext.isNotEmpty ? ext : '.jpg';
    final ts = DateTime.now().millisecondsSinceEpoch;
    final fileName = 'cache${ts}_$suffix$safeExt';
    final extCaches = await getExternalCacheDirectories();
    final extCache =
        (extCaches != null && extCaches.isNotEmpty) ? extCaches.first : null;
    final targetDir = extCache ?? await getTemporaryDirectory();
    final target = File(p.join(targetDir.path, fileName));
    return src.copy(target.path);
  }

  Future<void> captureBeneficiaryPhoto(BuildContext context) async {
    if (!skipFaceDetection.value) {
      // Skip Face Detection is OFF → use face detection camera (native: FaceDetectionActivity)
      final file = await Navigator.of(context).push<File>(
        MaterialPageRoute(builder: (_) => const FaceDetectionScreen()),
      );
      if (file != null) {
        beneficiaryPhotoFile.value = await _copyToExternalCache(file, 'BF');
      }
    } else {
      // Skip Face Detection is ON → front camera without blink check
      final file = await Navigator.of(context).push<File>(
        MaterialPageRoute(builder: (_) => const FrontCameraScreen()),
      );
      if (file != null) {
        beneficiaryPhotoFile.value = await _copyToExternalCache(file, 'BF');
      }
    }
  }

  Future<void> captureConsentForm(BuildContext context) async {
    final file = await Navigator.of(
      context,
    ).push<File>(MaterialPageRoute(builder: (_) => const FrontCameraScreen()));
    if (file != null) {
      consentFormFile.value = await _copyToExternalCache(file, 'CF');
    }
  }

  Future<void> captureDeliveryChallan(BuildContext context) async {
    final file = await Navigator.of(
      context,
    ).push<File>(MaterialPageRoute(builder: (_) => const FrontCameraScreen()));
    if (file != null) {
      deliveryChallanFile.value = await _copyToExternalCache(file, 'DC');
    }
  }

  // ── Status / Remark ───────────────────────────────────────────────────────

  final RxList<DeliveryStatusItem> statusList = <DeliveryStatusItem>[].obs;
  final RxList<DeliveryRemarkItem> remarkList = <DeliveryRemarkItem>[].obs;

  final Rx<DeliveryStatusItem?> selectedStatus = Rx<DeliveryStatusItem?>(null);
  final Rx<DeliveryRemarkItem?> selectedRemark = Rx<DeliveryRemarkItem?>(null);

  final TextEditingController otherRemarkController = TextEditingController();

  bool get showOtherRemarkField =>
      selectedRemark.value?.name.toLowerCase().contains('other') == true;

  /// For pending mode: whether to show SMS/OTP/photo sections
  bool get isStatusDelivered => selectedStatus.value?.id == 1;

  /// For pending mode: whether to show OTP section
  bool get isStatusOtpAllowed => selectedStatus.value != null;

  /// For pending mode: whether to show remark section
  bool get isStatusNonDelivery =>
      selectedStatus.value?.id == 2 || selectedStatus.value?.id == 3;

  // ── Submit loading ────────────────────────────────────────────────────────

  final RxBool isSubmitting = false.obs;

  // ── Image base URL ────────────────────────────────────────────────────────

  String get imageBaseUrl => APIManager.kMediaBaseURL;

  // ── Init ──────────────────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    _userId = user?.empCode?.toString() ?? '0';
    _orgId = user?.subOrgId?.toString() ?? '0';

    _resolveMode();
    if (mode == AckMode.pending || mode == AckMode.notAvailable) {
      final preferredStatusId =
          mode == AckMode.notAvailable
              ? beneficiary.deliveryStatusRemarkId
              : null;
      final preferredRemarkId =
          mode == AckMode.notAvailable ? beneficiary.deliveryRemarkId : null;
      _fetchStatusList(
        preferredStatusId: preferredStatusId,
        preferredRemarkId: preferredRemarkId,
      );
      if (mode == AckMode.notAvailable) {
        otherRemarkController.text = beneficiary.otherRemark ?? '';
      }
      _fetchFaceDetectionFlag();
    }
    // final dc = beneficiary.deliveryChallanId?.toString() ?? '';
    // if (dc.isNotEmpty && dc != '0') {
    //   deliveryChallanNoController.text = dc;
    // }
  }

  @override
  void onClose() {
    otpController.dispose();
    otherRemarkController.dispose();
    // deliveryChallanNoController.dispose();
    _otpTimer?.cancel();
    super.onClose();
  }

  // ── Mode resolution ───────────────────────────────────────────────────────

  void _resolveMode() {
    final statusId = beneficiary.deliveryStatusRemarkId ?? 0;
    switch (statusId) {
      case 1:
        mode = AckMode.delivered;
      case 2:
        mode = AckMode.notAvailable;
      case 3:
        mode = AckMode.denied;
      default:
        mode = AckMode.pending;
    }
  }

  // ── Status / Remark fetch ─────────────────────────────────────────────────

  void _fetchStatusList({int? preferredStatusId, int? preferredRemarkId}) {
    _api.getMedicineDeliveryStatusListAPI((response, error, success) {
      if (success && response != null) {
        final output = response['output'] as List? ?? [];
        statusList.value =
            output
                .map(
                  (e) => DeliveryStatusItem.fromJson(e as Map<String, dynamic>),
                )
                .toList();

        // Native default: deliveryStatusId = "1" (Deliver Medicine To Beneficiary)
        // Pre-select status ID 1 so the full form is visible from the start.
        if (statusList.isNotEmpty && selectedStatus.value == null) {
          final preferredMatches =
              preferredStatusId == null
                  ? const Iterable<DeliveryStatusItem>.empty()
                  : statusList.where((s) => s.id == preferredStatusId);
          final defaultMatches = statusList.where((s) => s.id == 1);
          final defaultStatus =
              preferredMatches.isNotEmpty
                  ? preferredMatches.first
                  : (defaultMatches.isNotEmpty
                      ? defaultMatches.first
                      : statusList.first);
          selectedStatus.value = defaultStatus;
          fetchRemarkList(
            defaultStatus.id,
            preferredRemarkId: preferredRemarkId,
          );
        }
      }
    });
  }

  void fetchRemarkList(int statusId, {int? preferredRemarkId}) {
    remarkList.clear();
    selectedRemark.value = null;
    _api.getMedicineDeliveryRemarkListAPI(
      {'DeliveryStatusRemarkID': statusId.toString()},
      (response, error, success) {
        if (success && response != null) {
          final output = response['output'] as List? ?? [];
          remarkList.value =
              output
                  .map(
                    (e) =>
                        DeliveryRemarkItem.fromJson(e as Map<String, dynamic>),
                  )
                  .toList();
          if (preferredRemarkId != null && selectedRemark.value == null) {
            final matches = remarkList.where((r) => r.id == preferredRemarkId);
            if (matches.isNotEmpty) {
              selectedRemark.value = matches.first;
            }
          }
        }
      },
    );
  }

  void selectStatus(DeliveryStatusItem item) {
    selectedStatus.value = item;
    fetchRemarkList(item.id);
    // Reset OTP/mobile state when status changes
    if (!isStatusOtpAllowed) {
      isOtpVerified.value = false;
      otpTargetNumber.value = '';
      otpController.clear();
      _otpTimer?.cancel();
      otpTimerText.value = '';
      canResendOtp.value = false;
    }
  }

  void selectRemark(DeliveryRemarkItem item) {
    selectedRemark.value = item;
  }

  // ── Submit ────────────────────────────────────────────────────────────────

  void submitDeliveryAck(BuildContext context) {
    // final dcNo = deliveryChallanNoController.text.trim();
    // if (dcNo.isEmpty || dcNo == '0') {
    //   ToastManager.toast('Please scan or enter delivery challan number');
    //   return;
    // }
    if (beneficiaryPhotoFile.value == null) {
      // ToastManager.toast('Please capture beneficiary photo');
      ToastManager.showAlertDialog(
        context,
        'Please capture beneficiary photo',
        () {
          Navigator.pop(context);
        },
      );
      return;
    }
    if (consentFormFile.value == null) {
      // ToastManager.toast('Please capture consent form');
      ToastManager.showAlertDialog(context, 'Please capture consent form', () {
        Navigator.pop(context);
      });
      return;
    }
    if (deliveryChallanFile.value == null) {
      // ToastManager.toast('Please capture delivery challan');
      ToastManager.showAlertDialog(
        context,
        'Please capture delivery challan',
        () {
          Navigator.pop(context);
        },
      );
      return;
    }

    _showConfirmDialog(
      context,
      () => _doSubmit(context, deliveryStatusId: '1'),
    );
  }

  void submitPhotoOnly(BuildContext context) {
    if (selectedStatus.value == null) {
      // ToastManager.toast('Please select delivery status');
      ToastManager.showAlertDialog(
        context,
        'Please select delivery status',
        () {
          Navigator.pop(context);
        },
      );
      return;
    }
    if (selectedRemark.value == null && isStatusNonDelivery) {
      ToastManager.showAlertDialog(context, 'Please select remark', () {
        Navigator.pop(context);
      });
      // ToastManager.toast('Please select remark');
      return;
    }
    if (showOtherRemarkField && otherRemarkController.text.trim().isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Please enter other description',
        () {
          Navigator.pop(context);
        },
      );
      // ToastManager.toast('Please enter other description');
      return;
    }
    _doSubmit(context, deliveryStatusId: selectedStatus.value!.id.toString());
  }

  void _doSubmit(BuildContext context, {required String deliveryStatusId}) {
    isSubmitting.value = true;
    // final dcNo = deliveryChallanNoController.text.trim();

    final fields = {
      'RegdID': beneficiary.regdId?.toString() ?? '0',
      'userId': _userId,
      'TreatmentID': beneficiary.treatmentId?.toString() ?? '0',
      'OTPVerified': isOtpVerified.value ? '1' : '0',
      'Delivarystatus': 'Y',
      'MedicalDelivaryID': beneficiary.medicalDelivaryId?.toString() ?? '0',
      'Dc_invoice_no': (beneficiary.deliveryChallanId ?? '0'),
      'DeliveryStatusRemarkID': deliveryStatusId,
      'DeliveryRemarkID': selectedRemark.value?.id.toString() ?? '0',
      'Remark': otherRemarkController.text.trim(),
    };

    _api.submitMedicineDeliveryAckAPI(
      fields: fields,
      beneficiaryPhotoFile: beneficiaryPhotoFile.value,
      consentFormFile: consentFormFile.value,
      deliveryChallanFile: deliveryChallanFile.value,
      callback: (response, error, success) {
        isSubmitting.value = false;
        if (success) {
          String message;
          switch (deliveryStatusId) {
            case '1':
              message = 'Medicines Delivered Successfully';
            case '2':
              message = 'Medicines Not Delivered, Need To Re-Attempt';
            case '3':
              message = 'Medicines Denied Successfully';
            default:
              message = 'Submitted Successfully';
          }

          ToastManager().showSuccessOkayDialog(
            context: context,
            title: "Success",
            message: message,
            onTap: () {
              Navigator.of(context).pop(); // dismiss dialog
              Get.back(result: true);
            },
          );

          // ToastManager.showAlertDialog(context, message, () {
          //   Navigator.of(context).pop(); // dismiss dialog
          //   Get.back(result: true); // navigate back to list
          // });
        } else {
          ToastManager.showAlertDialog(
            context,
            error.isNotEmpty ? error : 'Submission failed. Please try again.',
            () {
              Navigator.pop(context);
            },
          );
        }
      },
    );
  }

  void _showConfirmDialog(BuildContext context, VoidCallback onConfirm) {
    ToastManager().showConfirmationDialog(
      context: context,
      message: 'Are you sure you want to submit delivery acknowledgement?',
      didSelectYes: (bool p1) {
        if (p1 == true) {
          Navigator.pop(context);
          onConfirm();
        } else if (p1 == false) {
          Navigator.pop(context);
        }
      },
    );

    // showDialog(
    //   context: context,
    //   builder:
    //       (_) => AlertDialog(
    //         shape: RoundedRectangleBorder(
    //           borderRadius: BorderRadius.circular(16),
    //         ),
    //         title: const Text('Confirm Submission'),
    //         content: const Text(
    //           'Are you sure you want to submit delivery acknowledgement?',
    //         ),
    //         actions: [
    //           TextButton(
    //             onPressed: () => Navigator.pop(context),
    //             child: const Text('No'),
    //           ),
    //           ElevatedButton(
    //             style: ElevatedButton.styleFrom(
    //               backgroundColor: const Color(0xFF423897),
    //               shape: RoundedRectangleBorder(
    //                 borderRadius: BorderRadius.circular(8),
    //               ),
    //             ),
    //             onPressed: () {
    //               Navigator.pop(context);
    //               onConfirm();
    //             },
    //             child: const Text('Yes', style: TextStyle(color: Colors.white)),
    //           ),
    //         ],
    //       ),
    // );
  }
}
