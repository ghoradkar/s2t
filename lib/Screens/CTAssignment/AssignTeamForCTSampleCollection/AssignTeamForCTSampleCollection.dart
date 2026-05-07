// ignore_for_file: file_names, must_be_immutable

import 'dart:io';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ChooseDocumentManager/ChooseDocumentManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';
import '../../../Modules/DispatchGroup/DispatchGroup.dart';
import '../../../Modules/Json_Class/AssignmentRemarksResponse/AssignmentRemarksResponse.dart';
import '../../../Modules/Json_Class/BeneficiaryDetailsforAssignTeamidDetailsResponse/BeneficiaryDetailsforAssignTeamidDetailsResponse.dart';
import '../../../Modules/Json_Class/ConfirmatoryTestsScreeningResponse/ConfirmatoryTestsScreeningResponse.dart';
import '../../../Modules/Json_Class/ConfirmatoryTestsScreeningTubeResponse/ConfirmatoryTestsScreeningTubeResponse.dart';
import '../../../Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/widgets/AppActiveButton.dart';
import '../../../Modules/widgets/AppTextField.dart';
import '../../../Modules/widgets/CommonText.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/selection_bottom_sheet.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/medicine_delivery/view/FaceDetectionScreen.dart';
import 'PatientSampleCollectionHeaderView/PatientSampleCollectionHeaderView.dart';
import 'TestDetailsView/TestDetailsView.dart';
import 'TubeDetailsView/TubeDetailsView.dart';

class AssignTeamForCTSampleCollection extends StatefulWidget {
  AssignTeamForCTSampleCollection({
    super.key,
    required this.beneficiaryDetails,
    this.isAppointmentFlow = false,
  });

  BeneficiaryDetailsforAssignTeamidOutput? beneficiaryDetails;
  final bool isAppointmentFlow;

  @override
  State<AssignTeamForCTSampleCollection> createState() =>
      _AssignTeamForCTSampleCollectionState();
}

class _AssignTeamForCTSampleCollectionState
    extends State<AssignTeamForCTSampleCollection> {
  int empCode = 0;
  ConfirmatoryTestsScreeningOutput? testDetailslist;
  List<ConfirmatoryTestsScreeningTubeOutput> tubesDetailslist = [];
  APIManager apiManager = APIManager();
  List<ConfirmatoryTestsScreeningOutput> list = [];

  List<AssignmentRemarksOutput> remarksList = [];
  AssignmentRemarksOutput? selectedRemark;

  int _selectedVendor = 1;
  String? _selectedMobileNumber;
  bool _isSmsVendorExpanded = false;

  // OTP flow
  bool _isOtpSent = false;
  bool _isOtpVerified = false;
  String _generatedOtp = '';
  final TextEditingController _otpController = TextEditingController();

  // Sample collection fields
  final TextEditingController _barcodeController = TextEditingController();
  final TextEditingController _sampleCountController = TextEditingController();
  String? _selectedLabName;

  // Lab picker
  List<LandingLabCampCreationOutput> _labList = [];
  int? _selectedLabCode;

  // Tube count controllers (editable)
  List<TextEditingController> _tubeCountControllers = [];

  // Display controllers for dropdown AppTextFields
  final TextEditingController _remarkController = TextEditingController();
  final TextEditingController _mobileController = TextEditingController();
  final TextEditingController _labController = TextEditingController();

  // Photo capture
  bool _skipFaceDetection = false;
  File? _patientPhotoFile;
  File? _consentPhotoFile;

  DispatchGroup dispatchGroup = DispatchGroup();

  final DateFormat _apiFormat = DateFormat('yyyy/MM/dd');

  @override
  void initState() {
    super.initState();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    dispatchGroup.enter();
    dispatchGroup.enter();
    dispatchGroup.enter();
    ToastManager.showLoader();
    getTestInfo();
    getTestInfoCount();
    getAssignmentRemarks();

    dispatchGroup.notify(() {
      dispatchGroup.reset();
      debugPrint("All APIs Completed!");
      ToastManager.hideLoader();

      _mobileController.text = _activeMobile;

      // Auto-select remark based on the ArId passed from appointment confirmation
      final passedArId = widget.beneficiaryDetails?.arId;
      if (selectedRemark == null &&
          passedArId != null &&
          remarksList.isNotEmpty) {
        final matches = remarksList.where((r) => r.arId == passedArId);
        if (matches.isNotEmpty && mounted) {
          setState(() {
            selectedRemark = matches.first;
            _remarkController.text = selectedRemark?.assignmentRemarks ?? '';
          });
        }
      }

      if (mounted && !_isAppointmentConfirmed && !_isSampleCollected) {
        WidgetsBinding.instance.addPostFrameCallback((_) {
          if (mounted) _showAppointmentPendingAlert();
        });
      }
    });
  }

  @override
  void dispose() {
    _otpController.dispose();
    _barcodeController.dispose();
    _sampleCountController.dispose();
    _remarkController.dispose();
    _mobileController.dispose();
    _labController.dispose();
    for (final c in _tubeCountControllers) {
      c.dispose();
    }
    super.dispose();
  }

  void getTestInfo() {
    final today = _apiFormat.format(DateTime.now());
    Map<String, String> params = {
      "USERID": empCode.toString(),
      "DISTLGDCODE": "0",
      "AREA": "0",
      "Type": widget.isAppointmentFlow ? "4" : "9",
      "REDNO": widget.beneficiaryDetails?.regdno ?? "",
      "T2T_Order_Id": (widget.beneficiaryDetails?.t2tOrderId ?? 0).toString(),
      "FROMDATE": "2024/01/01",
      "TODATE": today,
    };
    apiManager.getTestInfoAPI(params, apiTestInfoCallBack);
  }

  void getTestInfoCount() {
    final today = _apiFormat.format(DateTime.now());
    Map<String, String> params = {
      "USERID": empCode.toString(),
      "DISTLGDCODE": "0",
      "AREA": "0",
      "Type": widget.isAppointmentFlow ? "5" : "10",
      "REDNO": widget.beneficiaryDetails?.regdno ?? "",
      "T2T_Order_Id": (widget.beneficiaryDetails?.t2tOrderId ?? 0).toString(),
      "FROMDATE": "2024/01/01",
      "TODATE": today,
    };
    apiManager.getTestInfoCount(params, apiTestInfoCountCallBack);
  }

  void getAssignmentRemarks() {
    Map<String, String> params = {"USERID": empCode.toString(), "Type": "4"};
    apiManager.getAssignmentRemarksAPI(params, apiAssignmentRemarksCallBack);
  }

  void apiTestInfoCallBack(
    ConfirmatoryTestsScreeningResponse? response,
    String errorMessage,
    bool success,
  ) async {
    dispatchGroup.leave();
    if (success) {
      testDetailslist = response?.output?.first;
      list = response?.output ?? [];
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void apiTestInfoCountCallBack(
    ConfirmatoryTestsScreeningTubeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    dispatchGroup.leave();
    if (success) {
      tubesDetailslist = response?.output ?? [];
      for (final c in _tubeCountControllers) {
        c.dispose();
      }
      _tubeCountControllers =
          tubesDetailslist
              .map((t) => TextEditingController(text: '${t.tubCount ?? 0}'))
              .toList();
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void apiAssignmentRemarksCallBack(
    AssignmentRemarksResponse? response,
    String errorMessage,
    bool success,
  ) async {
    dispatchGroup.leave();
    if (success) {
      remarksList = response?.output ?? [];
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  bool get _isAppointmentConfirmed =>
      (testDetailslist?.isAppointmentDone ?? 'N').toUpperCase() == 'Y';

  bool get _isSampleCollected {
    final val =
        testDetailslist?.sampleCollection ??
        widget.beneficiaryDetails?.sampleCollection ??
        'N';
    return val.toUpperCase() == 'Y';
  }

  String get _activeMobile =>
      _selectedMobileNumber ??
      testDetailslist?.workersMob ??
      testDetailslist?.mobileNo ??
      '';

  void _showMobileNumberPicker() {
    final workerMob =
        testDetailslist?.workersMob ?? testDetailslist?.mobileNo ?? '';
    final alternateMob = testDetailslist?.alternateMobNo ?? '';
    final options = <String>[
      if (workerMob.isNotEmpty) workerMob,
      if (alternateMob.isNotEmpty) alternateMob,
      '9371023232', // test number
    ];
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<String, String>(
            title: 'Select Number To Send OTP',
            items: options,
            selectedValue: _selectedMobileNumber ?? _activeMobile,
            valueFor: (item) => item,
            labelFor: (item) => item,
            height: 300,
            padding: EdgeInsets.only(
              top: responsiveHeight(28),
              left: responsiveHeight(35),
              right: responsiveHeight(35),
              bottom: responsiveHeight(60),
            ),
            titleTextStyle: TextStyle(
              fontSize: responsiveFont(16),
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w600,
            ),
            titleBottomSpacing: responsiveHeight(20),
            selectedBackgroundColor: kPrimaryColor.withValues(alpha: 0.1),
            itemTextStyle: TextStyle(
              fontSize: responsiveFont(13),
              fontFamily: FontConstants.interFonts,
              color: kBlackColor,
            ),
            onItemTap: (mob) {
              setState(() {
                _selectedMobileNumber = mob;
                _mobileController.text = mob;
              });
              Navigator.pop(context);
            },
          ),
    );
  }

  void _showRemarkPicker() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => StatefulBuilder(
            builder:
                (ctx, sheetState) =>
                    SelectionBottomSheet<AssignmentRemarksOutput, int>(
                      title: 'CT Assignment Remark',
                      items: remarksList,
                      selectedValue: selectedRemark?.arId,
                      valueFor: (item) => item.arId ?? 0,
                      labelFor: (item) => item.assignmentRemarks ?? 'N/A',
                      height: 380,
                      padding: EdgeInsets.only(
                        top: responsiveHeight(28),
                        left: responsiveHeight(35),
                        right: responsiveHeight(35),
                        bottom: responsiveHeight(60),
                      ),
                      titleTextStyle: TextStyle(
                        fontSize: responsiveFont(16),
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w600,
                      ),
                      titleBottomSpacing: responsiveHeight(20),
                      selectedBackgroundColor: kPrimaryColor.withValues(
                        alpha: 0.1,
                      ),
                      itemTextStyle: TextStyle(
                        fontSize: responsiveFont(13),
                        fontFamily: FontConstants.interFonts,
                        color: kBlackColor,
                      ),
                      onItemTap: (item) {
                        setState(() {
                          selectedRemark = item;
                          _remarkController.text = item.assignmentRemarks ?? '';
                        });
                        Navigator.pop(context);
                        _onRemarkSelected(item);
                      },
                    ),
          ),
    );
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Sample Collection",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: SingleChildScrollView(
          padding: const EdgeInsets.all(8),
          child: Column(
            children: [
              PatientSampleCollectionHeaderView(
                fullName:
                    widget.beneficiaryDetails?.beneficiaryName ??
                    testDetailslist?.beneficiaryName ??
                    "",
                gender: testDetailslist?.gender ?? "",
                age: testDetailslist?.patAge?.toString() ?? "",
              ),
              const SizedBox(height: 10),
              TestDetailsView(list: list),
              const SizedBox(height: 10),
              TubeDetailsView(
                list: tubesDetailslist,
                countControllers: _tubeCountControllers,
              ),
              const SizedBox(height: 12),
              _buildSampleCollectionSection(),
              const SizedBox(height: 20),
            ],
          ),
        ),
      ),
    );
  }

  // true when remark 1 (collect sample) — show barcode/lab/photo sections
  bool get _isCollectionRemark =>
      selectedRemark == null || (selectedRemark?.arId ?? 1) == 1;

  void _showAppointmentPendingAlert() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder:
          (_) => AlertDialog(
            title: Text(
              'Alert',
              style: TextStyle(fontFamily: FontConstants.interFonts),
            ),
            content: Text(
              'Please confirm appointment date',
              style: TextStyle(fontFamily: FontConstants.interFonts),
            ),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(context),
                child: const Text('OK'),
              ),
            ],
          ),
    );
  }

  void _onRemarkSelected(AssignmentRemarksOutput remark) {
    final arId = remark.arId ?? 0;
    if (arId == 4) {
      showDialog(
        context: context,
        barrierDismissible: false,
        builder:
            (_) => AlertDialog(
              title: Row(
                children: [
                  Icon(Icons.warning_amber_rounded, color: Colors.red),
                  const SizedBox(width: 8),
                  const Text('Alert'),
                ],
              ),
              content: const Text(
                'This beneficiary will not be available for CT screening.\n'
                'Please verify before proceeding with the submission.',
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('OK'),
                ),
              ],
            ),
      );
    } else if (arId == 6) {
      showDialog(
        context: context,
        barrierDismissible: false,
        builder:
            (_) => AlertDialog(
              title: Row(
                children: [
                  Icon(Icons.warning_amber_rounded, color: Colors.red),
                  const SizedBox(width: 8),
                  const Text('Alert'),
                ],
              ),
              content: const Text(
                'This beneficiary can be re-attempted for CT screening.',
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('OK'),
                ),
              ],
            ),
      );
    }
  }

  void getLabList() {
    final distCode =
        (testDetailslist?.dISTLGDCODE ??
                widget.beneficiaryDetails?.dISTLGDCODE ??
                0)
            .toString();
    Map<String, String> params = {"DISTLGDCODE": distCode};
    ToastManager.showLoader();
    apiManager.getT2TLabDetailsAPI(params, _labListCallBack);
  }

  void _sendCTOTP() {
    final mobile = _activeMobile;
    if (mobile.isEmpty) {
      ToastManager.toast('Please select a number first');
      return;
    }
    _generatedOtp = (10000 + Random().nextInt(90000)).toString();
    final subOrgId =
        DataProvider()
            .getParsedUserData()
            ?.output
            ?.first
            .subOrgId
            ?.toString() ??
        '0';
    final t2tOrderId =
        (testDetailslist?.t2tOrderId ??
                widget.beneficiaryDetails?.t2tOrderId ??
                0)
            .toString();
    final params = {
      'MOBNO': mobile,
      'OTP': _generatedOtp,
      'RegdId': t2tOrderId,
      'CreatedBy': empCode.toString(),
      'MsgID': '1',
      'SubOrgID': subOrgId,
      'Option': _selectedVendor.toString(),
    };
    ToastManager.showLoader();
    apiManager.sendOTPForCTSampleCollectionAPI(params, (
      response,
      error,
      success,
    ) {
      ToastManager.hideLoader();
      if (success) {
        setState(() => _isOtpSent = true);
        ToastManager.toast('OTP sent to $mobile');
      } else {
        ToastManager.toast(error.isNotEmpty ? error : 'Failed to send OTP');
      }
    });
  }

  void _verifyCTOTP() {
    final entered = _otpController.text.trim();
    if (entered.length < 4) {
      ToastManager.toast('Please enter valid OTP');
      return;
    }
    final mobile = _activeMobile;
    ToastManager.showLoader();
    apiManager.verifyOTPForCTSampleCollectionAPI(
      {'MobNo': mobile, 'Otp': entered},
      (response, error, success) {
        ToastManager.hideLoader();
        if (success) {
          setState(() => _isOtpVerified = true);
          ToastManager.toast('OTP verified successfully');
        } else {
          ToastManager.toast(
            error.isNotEmpty ? error : 'OTP verification failed',
          );
        }
      },
    );
  }

  void _labListCallBack(
    LandingLabCampCreationResponse? response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (success) {
      setState(() => _labList = response?.output ?? []);
      if (_labList.isNotEmpty) _showLabPicker();
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void _showLabPicker() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<LandingLabCampCreationOutput, int>(
            title: 'Select Lab',
            items: _labList,
            selectedValue: _selectedLabCode,
            valueFor: (lab) => lab.labCode ?? 0,
            labelFor: (lab) => lab.labName ?? '',
            height: 420,
            padding: EdgeInsets.only(
              top: responsiveHeight(28),
              left: responsiveHeight(35),
              right: responsiveHeight(35),
              bottom: responsiveHeight(60),
            ),
            titleTextStyle: TextStyle(
              fontSize: responsiveFont(16),
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w600,
            ),
            titleBottomSpacing: responsiveHeight(20),
            selectedBackgroundColor: kPrimaryColor.withValues(alpha: 0.1),
            itemTextStyle: TextStyle(
              fontSize: responsiveFont(13),
              fontFamily: FontConstants.interFonts,
              color: kBlackColor,
            ),
            onItemTap: (lab) {
              setState(() {
                _selectedLabName = lab.labName;
                _selectedLabCode = lab.labCode;
                _labController.text = lab.labName ?? '';
              });
              Navigator.pop(context);
            },
          ),
    );
  }

  Future<void> _scanBarcode() async {
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
    if (res != null && res != '-1') {
      setState(() => _barcodeController.text = res);
    }
  }

  Future<void> _capturePatientPhoto() async {
    if (_skipFaceDetection) {
      final result = await ChooseDocumentManager.pickFile(
        FileSourceType.camera,
      );
      if (result != null) {
        setState(() => _patientPhotoFile = result.file);
      }
    } else {
      final File? photo = await Navigator.push<File?>(
        context,
        MaterialPageRoute(builder: (_) => const FaceDetectionScreen()),
      );
      if (photo != null) {
        setState(() => _patientPhotoFile = photo);
      }
    }
  }

  Future<void> _captureConsentPhoto() async {
    final result = await ChooseDocumentManager.pickFile(FileSourceType.camera);
    if (result != null) {
      setState(() => _consentPhotoFile = result.file);
    }
  }

  Future<void> _submitSampleCollection() async {
    if (_isAppointmentConfirmed && !_isOtpVerified && _isCollectionRemark) {
      ToastManager.toast('Please verify OTP first');
      return;
    }

    final regNo =
        testDetailslist?.regdNo ?? widget.beneficiaryDetails?.regdno ?? '';
    final t2tOrderId =
        (testDetailslist?.t2tOrderId ??
                widget.beneficiaryDetails?.t2tOrderId ??
                0)
            .toString();
    final treatmentId = (testDetailslist?.treatmentID ?? 0).toString();

    if (_isCollectionRemark) {
      if (_selectedLabCode == null) {
        ToastManager.toast('Please select lab');
        return;
      }
      if (_barcodeController.text.trim().isEmpty) {
        ToastManager.toast('Please enter barcode');
        return;
      }
      if (_sampleCountController.text.trim().isEmpty) {
        ToastManager.toast('Please enter sample count');
        return;
      }
      final totalTubeCount = _tubeCountControllers.fold<int>(
        0,
        (sum, c) => sum + (int.tryParse(c.text.trim()) ?? 0),
      );
      final enteredCount =
          int.tryParse(_sampleCountController.text.trim()) ?? -1;
      if (enteredCount != totalTubeCount) {
        ToastManager.toast(
          'Sample count ($enteredCount) must match total tube count ($totalTubeCount)',
        );
        return;
      }
      if (_patientPhotoFile == null) {
        ToastManager.toast('Please capture beneficiary photo');
        return;
      }
      if (_consentPhotoFile == null) {
        ToastManager.toast('Please capture consent form & photo ID');
        return;
      }

      final fields = <String, String>{
        'Regdno': regNo,
        'Barcode': _barcodeController.text.trim(),
        'Labcode': (_selectedLabCode ?? 0).toString(),
        'Samplecount': _sampleCountController.text.trim(),
        'USERID': empCode.toString(),
        'ArId': (selectedRemark?.arId ?? 1).toString(),
        'T2T_Order_Id': t2tOrderId,
        'TreatmentID': treatmentId,
      };
      ToastManager.showLoader();
      apiManager.insertT2TBarcodeCollectionWithConsentAPI(
        fields,
        _patientPhotoFile?.path,
        _consentPhotoFile?.path,
        _onSubmitCallback,
      );
    } else {
      final params = <String, String>{
        'Regdno': regNo,
        'Barcode': '',
        'Labcode': '0',
        'Samplecount': '0',
        'USERID': empCode.toString(),
        'T2T_Order_Id': t2tOrderId,
        'TreatmentID': treatmentId,
        'ArId': (selectedRemark?.arId ?? 0).toString(),
      };
      ToastManager.showLoader();
      apiManager.insertT2TBarcodeCollectionAPI(params, _onSubmitCallback);
    }
  }

  void _onSubmitCallback(dynamic response, String errorMessage, bool success) {
    ToastManager.hideLoader();
    if (success) {
      showDialog(
        context: context,
        barrierDismissible: false,
        builder:
            (_) => AlertDialog(
              title: const Text('Success'),
              content: const Text('Details Submitted Successfully'),
              actions: [
                TextButton(
                  onPressed: () {
                    Navigator.pop(context); // close dialog
                    Navigator.pop(context, true); // pop screen, signal success
                  },
                  child: const Text('OK'),
                ),
              ],
            ),
      );
    } else {
      ToastManager.toast(
        errorMessage.isNotEmpty ? errorMessage : 'Submission failed',
      );
    }
  }

  Widget _buildSampleCollectionSection() {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.all(16.w),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            offset: const Offset(0, 1),
            color: Colors.black.withValues(alpha: 0.1),
            blurRadius: 6,
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // CT Assignment Remark
          AppTextField(
            controller: _remarkController,
            readOnly: true,
            onTap: remarksList.isEmpty ? null : _showRemarkPicker,
            label: CommonText(
              text: 'CT Assignment Remark *',
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              textColor: kTextColor,
              textAlign: TextAlign.start,
            ),
            hint: 'Select Remark',
            hintStyle: TextStyle(
              fontSize: 12.sp,
              fontFamily: FontConstants.interFonts,
            ),
            suffixIcon: Icon(Icons.arrow_drop_down, color: kPrimaryColor),
            fieldRadius: 8,
          ),
          SizedBox(height: 12.h),

          // ── OTP & SMS Vendor combined card ─────────────────────────
          Container(
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(10),
              border: Border.all(color: Colors.grey.shade200),
              boxShadow: [
                BoxShadow(
                  offset: const Offset(0, 1),
                  color: Colors.black.withValues(alpha: 0.06),
                  blurRadius: 6,
                ),
              ],
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Card header
                Container(
                  width: double.infinity,
                  padding: EdgeInsets.symmetric(
                    horizontal: 14.w,
                    vertical: 10.h,
                  ),
                  decoration: BoxDecoration(
                    color: kPrimaryColor,
                    borderRadius: const BorderRadius.vertical(
                      top: Radius.circular(10),
                    ),
                  ),
                  child: Row(
                    children: [
                      Icon(
                        Icons.phone_android_rounded,
                        color: Colors.white,
                        size: 16.sp,
                      ),
                      SizedBox(width: 8.w),
                      CommonText(
                        text: 'OTP Verification',
                        fontSize: 13.sp,
                        fontWeight: FontWeight.w700,
                        textColor: Colors.white,
                        textAlign: TextAlign.start,
                      ),
                    ],
                  ),
                ),

                Padding(
                  padding: EdgeInsets.all(14.w),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // Select Number
                      AppTextField(
                        controller: _mobileController,
                        readOnly: true,
                        onTap: _showMobileNumberPicker,
                        label: CommonText(
                          text: 'Select Number To Send OTP',
                          fontSize: 12.sp,
                          fontWeight: FontWeight.w400,
                          textColor: kTextColor,
                          textAlign: TextAlign.start,
                        ),
                        hint: 'Select Number',
                        hintStyle: TextStyle(
                          fontSize: 12.sp,
                          fontFamily: FontConstants.interFonts,
                        ),
                        suffixIcon: Icon(
                          Icons.arrow_drop_down,
                          color: kPrimaryColor,
                        ),
                        fieldRadius: 8,
                      ),
                      SizedBox(height: 12.h),

                      // SMS Vendor collapsible
                      GestureDetector(
                        onTap:
                            () => setState(
                              () =>
                                  _isSmsVendorExpanded = !_isSmsVendorExpanded,
                            ),
                        child: Container(
                          padding: EdgeInsets.symmetric(
                            horizontal: 12.w,
                            vertical: 10.h,
                          ),
                          decoration: BoxDecoration(
                            color: kPurpleFaint,
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Row(
                            children: [
                              Icon(
                                Icons.settings_outlined,
                                size: 14.sp,
                                color: kPrimaryColor,
                              ),
                              SizedBox(width: 6.w),
                              Expanded(
                                child: CommonText(
                                  text: 'SMS Vendor Settings',
                                  fontSize: 12.sp,
                                  fontWeight: FontWeight.w600,
                                  textColor: kPrimaryColor,
                                  textAlign: TextAlign.start,
                                ),
                              ),
                              Icon(
                                _isSmsVendorExpanded
                                    ? Icons.keyboard_arrow_up
                                    : Icons.keyboard_arrow_down,
                                color: kPrimaryColor,
                                size: 18.sp,
                              ),
                            ],
                          ),
                        ),
                      ),
                      if (_isSmsVendorExpanded) ...[
                        SizedBox(height: 10.h),
                        Container(
                          padding: EdgeInsets.all(10.w),
                          decoration: BoxDecoration(
                            color: Colors.blue.shade50,
                            borderRadius: BorderRadius.circular(8),
                            border: Border.all(color: Colors.blue.shade100),
                          ),
                          child: Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Icon(
                                Icons.info_outline,
                                size: 14.sp,
                                color: Colors.blue.shade700,
                              ),
                              SizedBox(width: 6.w),
                              Expanded(
                                child: CommonText(
                                  text:
                                      'जर लाभार्थ्याच्या मोबाईलवर OTP प्राप्त झाला नसेल तर option 2 वापरून OTP पाठवा',
                                  fontSize: 11.sp,
                                  fontWeight: FontWeight.w400,
                                  textColor: Colors.blue.shade800,
                                  textAlign: TextAlign.start,
                                ),
                              ),
                            ],
                          ),
                        ),
                        SizedBox(height: 10.h),
                        Row(
                          children: [
                            _radioOption(
                              label: 'Option 1',
                              value: 1,
                              groupValue: _selectedVendor,
                              onChanged:
                                  (v) => setState(() => _selectedVendor = v!),
                            ),
                            SizedBox(width: 24.w),
                            _radioOption(
                              label: 'Option 2',
                              value: 2,
                              groupValue: _selectedVendor,
                              onChanged:
                                  (v) => setState(() => _selectedVendor = v!),
                            ),
                          ],
                        ),
                      ],

                      // Send OTP + Enter OTP
                      if (_isAppointmentConfirmed &&
                          !_isOtpVerified &&
                          !_isSampleCollected &&
                          _isCollectionRemark) ...[
                        SizedBox(height: 14.h),
                        SizedBox(
                          width: double.infinity,
                          height: 46.h,
                          child: AppActiveButton(
                            buttontitle: 'Send OTP',
                            onTap: _activeMobile.isEmpty ? () {} : _sendCTOTP,
                          ),
                        ),
                        if (_isOtpSent) ...[
                          SizedBox(height: 12.h),
                          Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Expanded(
                                child: AppTextField(
                                  controller: _otpController,
                                  textInputType: TextInputType.number,
                                  maxLength: 6,
                                  label: CommonText(
                                    text: 'Enter OTP',
                                    fontSize: 12.sp,
                                    fontWeight: FontWeight.w400,
                                    textColor: kTextColor,
                                    textAlign: TextAlign.start,
                                  ),
                                  hint: 'Enter OTP',
                                  hintStyle: TextStyle(
                                    fontSize: 12.sp,
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                  fieldRadius: 8,
                                ),
                              ),
                              SizedBox(width: 8.w),
                              SizedBox(
                                width: 110.w,
                                height: 56.h,
                                child: AppActiveButton(
                                  buttontitle: 'Verify',
                                  onTap: _verifyCTOTP,
                                ),
                              ),
                            ],
                          ),
                        ],
                      ],

                      // OTP Verified badge
                      if (_isOtpVerified) ...[
                        SizedBox(height: 12.h),
                        Container(
                          padding: EdgeInsets.symmetric(
                            horizontal: 12.w,
                            vertical: 8.h,
                          ),
                          decoration: BoxDecoration(
                            color: Colors.green.shade50,
                            borderRadius: BorderRadius.circular(8),
                            border: Border.all(color: Colors.green.shade200),
                          ),
                          child: Row(
                            children: [
                              Icon(
                                Icons.check_circle,
                                color: Colors.green.shade600,
                                size: 16.sp,
                              ),
                              SizedBox(width: 8.w),
                              CommonText(
                                text: 'OTP Verified Successfully',
                                fontSize: 12.sp,
                                fontWeight: FontWeight.w600,
                                textColor: Colors.green.shade700,
                                textAlign: TextAlign.start,
                              ),
                            ],
                          ),
                        ),
                      ],
                    ],
                  ),
                ),
              ],
            ),
          ),
          SizedBox(height: 14.h),

          // Barcode, Lab, Sample Count, Photos
          if (_isAppointmentConfirmed &&
              _isCollectionRemark &&
              !_isSampleCollected) ...[
            AppTextField(
              controller: _barcodeController,
              maxLength: 14,
              textInputType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              label: CommonText(
                text: 'Barcode',
                fontSize: 12.sp,
                fontWeight: FontWeight.w400,
                textColor: kTextColor,
                textAlign: TextAlign.start,
              ),
              hint: 'Enter or scan barcode',
              hintStyle: TextStyle(
                fontSize: 12.sp,
                fontFamily: FontConstants.interFonts,
              ),
              suffixIcon: GestureDetector(
                onTap: _scanBarcode,
                child: Padding(
                  padding: const EdgeInsets.all(10),
                  child: Image.asset(
                    icBarcodeIcon,
                    width: 24,
                    height: 24,
                    errorBuilder:
                        (ctx, e, st) =>
                            Icon(Icons.qr_code_scanner, color: kPrimaryColor),
                  ),
                ),
              ),
              fieldRadius: 8,
            ),
            SizedBox(height: 12.h),

            AppTextField(
              controller: _labController,
              readOnly: true,
              onTap:
                  () => _labList.isNotEmpty ? _showLabPicker() : getLabList(),
              label: CommonText(
                text: 'Select Lab',
                fontSize: 12.sp,
                fontWeight: FontWeight.w400,
                textColor: kTextColor,
                textAlign: TextAlign.start,
              ),
              hint: 'Select Lab',
              hintStyle: TextStyle(
                fontSize: 12.sp,
                fontFamily: FontConstants.interFonts,
              ),
              suffixIcon: Icon(Icons.arrow_drop_down, color: kPrimaryColor),
              fieldRadius: 8,
            ),
            SizedBox(height: 12.h),

            AppTextField(
              controller: _sampleCountController,
              textInputType: TextInputType.number,
              maxLength: 3,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              label: CommonText(
                text: 'Sample Count',
                fontSize: 12.sp,
                fontWeight: FontWeight.w400,
                textColor: kTextColor,
                textAlign: TextAlign.start,
              ),
              hint: 'Enter sample count',
              hintStyle: TextStyle(
                fontSize: 12.sp,
                fontFamily: FontConstants.interFonts,
              ),
              fieldRadius: 8,
            ),
            SizedBox(height: 16.h),

            // Beneficiary Photo + Consent Form side by side
            _buildPhotosRow(),
            SizedBox(height: 20.h),
          ],

          // Submit button
          if (_isAppointmentConfirmed && !_isSampleCollected) ...[
            SizedBox(
              width: double.infinity,
              height: 46.h,
              child: AppActiveButton(
                buttontitle: 'Submit',
                onTap: _submitSampleCollection,
              ),
            ),
          ],

          // Sample already collected
          if (_isSampleCollected) ...[
            _buildSampleAlreadyCollectedCard(),
            SizedBox(height: 12.h),
          ],

          // Appointment pending warning
          if (!_isAppointmentConfirmed) ...[
            _buildAppointmentPendingCard(),
            SizedBox(height: 12.h),
          ],
        ],
      ),
    );
  }

  Widget _buildPhotosRow() {
    return IntrinsicHeight(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Expanded(
            child: _buildSinglePhotoCard(
              title: 'Beneficiary Photo',
              icon: Icons.person_outline,
              photoFile: _patientPhotoFile,
              onCapture: _capturePatientPhoto,
              captureLabel: 'Tap to capture *',
              showSkipToggle: true,
            ),
          ),
          SizedBox(width: 10.w),
          Expanded(
            child: _buildSinglePhotoCard(
              title: 'Consent & Photo ID',
              icon: Icons.description_outlined,
              photoFile: _consentPhotoFile,
              onCapture: _captureConsentPhoto,
              captureLabel: 'Tap to capture *',
              hintText: 'संमतीपत्र आणि ओळखपत्र एकत्र फोटो',
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSinglePhotoCard({
    required String title,
    required IconData icon,
    required File? photoFile,
    required VoidCallback onCapture,
    required String captureLabel,
    bool showSkipToggle = false,
    String? hintText,
  }) {
    final bool captured = photoFile != null;
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(
          color: captured ? Colors.green.shade200 : Colors.grey.shade200,
        ),
        boxShadow: [
          BoxShadow(
            offset: const Offset(0, 1),
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 6,
          ),
        ],
      ),
      child: Column(
        children: [
          // Header
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 9.h),
            decoration: BoxDecoration(
              color: captured ? Colors.green.shade600 : kPrimaryColor,
              borderRadius: const BorderRadius.vertical(
                top: Radius.circular(10),
              ),
            ),
            child: Row(
              children: [
                Icon(
                  captured ? Icons.check_circle_outline : icon,
                  color: Colors.white,
                  size: 14.sp,
                ),
                SizedBox(width: 6.w),
                Expanded(
                  child: CommonText(
                    text: title,
                    fontSize: 11.sp,
                    fontWeight: FontWeight.w600,
                    textColor: Colors.white,
                    textAlign: TextAlign.start,
                    maxLine: 1,
                    overflow: TextOverflow.ellipsis,
                  ),
                ),
              ],
            ),
          ),

          // Body
          Expanded(
            child: Padding(
              padding: EdgeInsets.symmetric(vertical: 14.h, horizontal: 10.w),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  // Photo thumbnail / placeholder — always square rounded
                  GestureDetector(
                    onTap: onCapture,
                    child:
                        captured
                            ? Stack(
                              alignment: Alignment.bottomRight,
                              children: [
                                ClipRRect(
                                  borderRadius: BorderRadius.circular(8),
                                  child: Image.file(
                                    photoFile,
                                    width: 80.w,
                                    height: 80.w,
                                    fit: BoxFit.cover,
                                  ),
                                ),
                                Container(
                                  width: 22.w,
                                  height: 22.w,
                                  decoration: BoxDecoration(
                                    color: kPrimaryColor,
                                    shape: BoxShape.circle,
                                    border: Border.all(
                                      color: Colors.white,
                                      width: 1.5,
                                    ),
                                  ),
                                  child: Icon(
                                    Icons.edit,
                                    color: Colors.white,
                                    size: 11.sp,
                                  ),
                                ),
                              ],
                            )
                            : Container(
                              width: 80.w,
                              height: 80.w,
                              decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(8),
                                color: kPurpleFaint,
                                border: Border.all(
                                  color: kPrimaryColor.withValues(alpha: 0.3),
                                  width: 1.5,
                                ),
                              ),
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                children: [
                                  Icon(
                                    Icons.add_a_photo_outlined,
                                    size: 24.sp,
                                    color: kPrimaryColor,
                                  ),
                                  SizedBox(height: 3.h),
                                  CommonText(
                                    text: 'Tap to add',
                                    fontSize: 8.sp,
                                    fontWeight: FontWeight.w500,
                                    textColor: kPrimaryColor,
                                    textAlign: TextAlign.center,
                                  ),
                                ],
                              ),
                            ),
                  ),

                  SizedBox(height: 8.h),
                  CommonText(
                    text: captureLabel,
                    fontSize: 10.sp,
                    fontWeight: FontWeight.w500,
                    textColor: captured ? Colors.green.shade600 : kTextColor,
                    textAlign: TextAlign.center,
                  ),

                  // Hint text below capture label (consent card only)
                  if (hintText != null) ...[
                    SizedBox(height: 5.h),
                    CommonText(
                      text: hintText,
                      fontSize: 9.sp,
                      fontWeight: FontWeight.w400,
                      textColor: Colors.blue.shade700,
                      textAlign: TextAlign.center,
                    ),
                  ],

                  // Skip Face Detection toggle (beneficiary card only)
                  if (showSkipToggle) ...[
                    SizedBox(height: 6.h),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        CommonText(
                          text: 'Skip Face\nDetection',
                          fontSize: 9.sp,
                          fontWeight: FontWeight.w400,
                          textColor: kTextColor,
                          textAlign: TextAlign.center,
                        ),
                        Transform.scale(
                          scale: 0.7,
                          child: Switch(
                            value: _skipFaceDetection,
                            onChanged:
                                (v) => setState(() => _skipFaceDetection = v),
                            activeThumbColor: Colors.green,
                            inactiveThumbColor: kBlackColor.withValues(
                              alpha: 0.4,
                            ),
                            inactiveTrackColor: kBlackColor.withValues(
                              alpha: 0.2,
                            ),
                            activeTrackColor: Colors.green.withValues(
                              alpha: 0.3,
                            ),
                          ),
                        ),
                      ],
                    ),
                  ],
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSampleAlreadyCollectedCard() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 12),
      decoration: BoxDecoration(
        color: Colors.green[50],
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: Colors.green.shade300),
      ),
      child: Row(
        children: [
          Icon(Icons.check_circle_outline, color: Colors.green[700], size: 18),
          const SizedBox(width: 8),
          Expanded(
            child: Text(
              'Sample Already Collected',
              style: TextStyle(
                fontSize: responsiveFont(12),
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                color: Colors.green[800],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildAppointmentPendingCard() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 12),
      decoration: BoxDecoration(
        color: Colors.orange[50],
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: Colors.orange.shade300),
      ),
      child: Row(
        children: [
          Icon(Icons.info_outline, color: Colors.orange[700], size: 18),
          const SizedBox(width: 8),
          Expanded(
            child: Text(
              'Appointment Confirmation Pending',
              style: TextStyle(
                fontSize: responsiveFont(12),
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                color: Colors.orange[800],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _radioOption({
    required String label,
    required int value,
    required int groupValue,
    required ValueChanged<int?> onChanged,
  }) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Radio<int>(
          value: value,
          groupValue: groupValue,
          onChanged: onChanged,
          activeColor: kPrimaryColor,
          materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
          visualDensity: VisualDensity.compact,
        ),
        Text(
          label,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: responsiveFont(12),
            color: kBlackColor,
          ),
        ),
      ],
    );
  }
}
