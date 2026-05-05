// ignore_for_file: file_names, must_be_immutable

import 'dart:io';
import 'dart:math';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
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
import '../../../Modules/widgets/S2TAppBar.dart';
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
    });
  }

  @override
  void dispose() {
    _otpController.dispose();
    _barcodeController.dispose();
    _sampleCountController.dispose();
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
      _tubeCountControllers = tubesDetailslist
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
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(12)),
      ),
      builder:
          (_) => Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Padding(
                padding: const EdgeInsets.all(12),
                child: Text(
                  'Select Number To Send OTP',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w700,
                    fontSize: responsiveFont(14),
                    color: kBlackColor,
                  ),
                ),
              ),
              const Divider(height: 1),
              ...options.map(
                (mob) => ListTile(
                  title: Text(
                    mob,
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(13),
                      color: kBlackColor,
                    ),
                  ),
                  onTap: () {
                    setState(() => _selectedMobileNumber = mob);
                    Navigator.pop(context);
                  },
                ),
              ),
            ],
          ),
    );
  }

  void _showRemarkPicker() {
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(12)),
      ),
      builder:
          (_) => Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Padding(
                padding: const EdgeInsets.all(12),
                child: Text(
                  'Select CT Assignment Remark',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w700,
                    fontSize: responsiveFont(14),
                    color: kBlackColor,
                  ),
                ),
              ),
              const Divider(height: 1),
              Flexible(
                child: ListView.builder(
                  shrinkWrap: true,
                  itemCount: remarksList.length,
                  itemBuilder: (_, i) {
                    final r = remarksList[i];
                    return ListTile(
                      title: Text(
                        r.assignmentRemarks ?? '',
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: responsiveFont(13),
                          color: kBlackColor,
                        ),
                      ),
                      onTap: () {
                        setState(() => selectedRemark = r);
                        Navigator.pop(context);
                        _onRemarkSelected(r);
                      },
                    );
                  },
                ),
              ),
            ],
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

  void _onRemarkSelected(AssignmentRemarksOutput remark) {
    final arId = remark.arId ?? 0;
    if (arId == 4) {
      showDialog(
        context: context,
        barrierDismissible: false,
        builder: (_) => AlertDialog(
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
        builder: (_) => AlertDialog(
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
    final distCode = (testDetailslist?.dISTLGDCODE ??
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
        DataProvider().getParsedUserData()?.output?.first.subOrgId?.toString() ??
        '0';
    final t2tOrderId = (testDetailslist?.t2tOrderId ??
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
    apiManager.sendOTPForCTSampleCollectionAPI(params, (response, error, success) {
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
          ToastManager.toast(error.isNotEmpty ? error : 'OTP verification failed');
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
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(12)),
      ),
      builder: (_) {
        return DraggableScrollableSheet(
          expand: false,
          initialChildSize: 0.5,
          maxChildSize: 0.85,
          builder:
              (_, scrollCtrl) => Column(
                children: [
                  Padding(
                    padding: const EdgeInsets.all(12),
                    child: Text(
                      'Select Lab',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w700,
                        fontSize: responsiveFont(14),
                        color: kBlackColor,
                      ),
                    ),
                  ),
                  const Divider(height: 1),
                  Expanded(
                    child: ListView.builder(
                      controller: scrollCtrl,
                      itemCount: _labList.length,
                      itemBuilder: (_, i) {
                        final lab = _labList[i];
                        return ListTile(
                          title: Text(
                            lab.labName ?? '',
                            style: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontSize: responsiveFont(13),
                              color: kBlackColor,
                            ),
                          ),
                          onTap: () {
                            setState(() {
                              _selectedLabName = lab.labName;
                              _selectedLabCode = lab.labCode;
                            });
                            Navigator.pop(context);
                          },
                        );
                      },
                    ),
                  ),
                ],
              ),
        );
      },
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
    final result = await ChooseDocumentManager.pickFile(FileSourceType.camera);
    if (result != null) {
      setState(() => _patientPhotoFile = result.file);
    }
  }

  Future<void> _captureConsentPhoto() async {
    final result = await ChooseDocumentManager.pickFile(FileSourceType.camera);
    if (result != null) {
      setState(() => _consentPhotoFile = result.file);
    }
  }

  Future<void> _submitSampleCollection() async {
    if (_isAppointmentConfirmed && !_isOtpVerified) {
      ToastManager.toast('Please verify OTP first');
      return;
    }

    final regNo =
        testDetailslist?.regdNo ?? widget.beneficiaryDetails?.regdno ?? '';
    final t2tOrderId = (testDetailslist?.t2tOrderId ??
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
        builder: (_) => AlertDialog(
          title: const Text('Success'),
          content: const Text('Details Submitted Successfully'),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context);
                Navigator.pop(context);
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
      padding: const EdgeInsets.all(12),
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
          // CT Assignment Remark dropdown
          Text(
            'CT Assignment Remark*',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w600,
              fontSize: responsiveFont(13),
              color: kBlackColor,
            ),
          ),
          const SizedBox(height: 6),
          GestureDetector(
            onTap: (remarksList.isEmpty || _isOtpVerified) ? null : _showRemarkPicker,
            child: Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
              decoration: BoxDecoration(
                color: _isOtpVerified ? Colors.grey.shade100 : Colors.white,
                border: Border.all(color: Colors.grey.shade300),
                borderRadius: BorderRadius.circular(6),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(
                      selectedRemark?.assignmentRemarks ?? 'Select Remark',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: responsiveFont(13),
                        color:
                            selectedRemark == null ? Colors.grey : kBlackColor,
                      ),
                    ),
                  ),
                  Icon(Icons.arrow_drop_down, color: Colors.grey.shade600),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),

          // SMS Vendor collapsible header (always visible, matching native tv_vendor)
          GestureDetector(
            onTap:
                () => setState(
                  () => _isSmsVendorExpanded = !_isSmsVendorExpanded,
                ),
            child: Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
              decoration: BoxDecoration(
                color: Colors.grey.shade100,
                borderRadius: BorderRadius.circular(6),
                border: Border.all(color: Colors.grey.shade300),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(
                      'SMS Vendor',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w600,
                        fontSize: responsiveFont(13),
                        color: kBlackColor,
                      ),
                    ),
                  ),
                  Icon(
                    _isSmsVendorExpanded
                        ? Icons.keyboard_arrow_up
                        : Icons.keyboard_arrow_down,
                    color: Colors.grey.shade600,
                  ),
                ],
              ),
            ),
          ),

          // SMS Vendor content (collapsed/expanded)
          if (_isSmsVendorExpanded) ...[
            const SizedBox(height: 10),
            Container(
              padding: const EdgeInsets.all(10),
              decoration: BoxDecoration(
                color: Colors.blue[50],
                borderRadius: BorderRadius.circular(6),
                border: Border.all(color: Colors.blue.shade200),
              ),
              child: Text(
                'जर लाभार्थ्याच्या मोबाईलवर OTP प्राप्त झाला नसेल तर option 2 वापरून OTP पाठवा',
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: responsiveFont(12),
                  color: Colors.blue[900],
                ),
              ),
            ),
            const SizedBox(height: 8),
            Row(
              children: [
                _radioOption(
                  label: 'Option 1',
                  value: 1,
                  groupValue: _selectedVendor,
                  onChanged: (v) => setState(() => _selectedVendor = v!),
                ),
                const SizedBox(width: 20),
                _radioOption(
                  label: 'Option 2',
                  value: 2,
                  groupValue: _selectedVendor,
                  onChanged: (v) => setState(() => _selectedVendor = v!),
                ),
              ],
            ),
          ],
          const SizedBox(height: 12),

          // OTP Verification header — only when appointment is confirmed
          if (_isAppointmentConfirmed) ...[
            Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(vertical: 6, horizontal: 12),
              decoration: BoxDecoration(
                color: kPrimaryColor,
                borderRadius: BorderRadius.circular(6),
              ),
              child: Text(
                'OTP Verification',
                style: TextStyle(
                  color: Colors.white,
                  fontFamily: FontConstants.interFonts,
                  fontWeight: FontWeight.w700,
                  fontSize: responsiveFont(13),
                ),
              ),
            ),
            const SizedBox(height: 10),
          ],

          // Select Number To Send OTP dropdown — always visible (matches native ll_main_mobile)
          Text(
            'Select Number To Send OTP',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: responsiveFont(12),
              color: kBlackColor,
            ),
          ),
          const SizedBox(height: 4),
          GestureDetector(
            onTap: _showMobileNumberPicker,
            child: Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 10),
              decoration: BoxDecoration(
                color: Colors.white,
                border: Border.all(color: Colors.grey.shade300),
                borderRadius: BorderRadius.circular(6),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(
                      _activeMobile.isNotEmpty
                          ? _activeMobile
                          : 'Select Number',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: responsiveFont(13),
                        color:
                            _selectedMobileNumber == null
                                ? Colors.grey
                                : kBlackColor,
                      ),
                    ),
                  ),
                  Icon(Icons.arrow_drop_down, color: Colors.grey.shade600),
                ],
              ),
            ),
          ),
          const SizedBox(height: 12),

          // Send OTP button — hidden after OTP is verified
          if (_isAppointmentConfirmed && !_isOtpVerified) ...[
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: _activeMobile.isEmpty ? null : _sendCTOTP,
                style: ElevatedButton.styleFrom(
                  backgroundColor: kPrimaryColor,
                  padding: const EdgeInsets.symmetric(vertical: 12),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(8),
                  ),
                ),
                child: Text(
                  'Send OTP',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w600,
                    fontSize: responsiveFont(14),
                    color: Colors.white,
                  ),
                ),
              ),
            ),

            // Enter OTP section — shown after OTP is sent
            if (_isOtpSent) ...[
              const SizedBox(height: 12),
              Text(
                'Enter OTP',
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: responsiveFont(12),
                  color: kBlackColor,
                ),
              ),
              const SizedBox(height: 4),
              Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: _otpController,
                      keyboardType: TextInputType.number,
                      maxLength: 6,
                      decoration: InputDecoration(
                        counterText: '',
                        hintText: 'Enter OTP',
                        hintStyle: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: responsiveFont(13),
                          color: Colors.grey,
                        ),
                        contentPadding: const EdgeInsets.symmetric(
                          horizontal: 12,
                          vertical: 10,
                        ),
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(6),
                          borderSide: BorderSide(color: Colors.grey.shade300),
                        ),
                        enabledBorder: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(6),
                          borderSide: BorderSide(color: Colors.grey.shade300),
                        ),
                      ),
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: responsiveFont(13),
                      ),
                    ),
                  ),
                  const SizedBox(width: 8),
                  ElevatedButton(
                    onPressed: _verifyCTOTP,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: kPrimaryColor,
                      padding: const EdgeInsets.symmetric(
                        horizontal: 16,
                        vertical: 12,
                      ),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                    ),
                    child: Text(
                      'Verify OTP',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w600,
                        fontSize: responsiveFont(13),
                        color: Colors.white,
                      ),
                    ),
                  ),
                ],
              ),
            ],
            const SizedBox(height: 12),
          ],

          // Barcode, lab, sample count, photos — visible after OTP verification for remark 1
          if (_isAppointmentConfirmed && _isOtpVerified && _isCollectionRemark) ...[

            // Barcode field
            Text(
              'Barcode',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: responsiveFont(12),
                color: kBlackColor,
              ),
            ),
            const SizedBox(height: 4),
            TextField(
              controller: _barcodeController,
              maxLength: 14,
              keyboardType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              decoration: InputDecoration(
                counterText: '',
                hintText: 'Enter or scan barcode',
                hintStyle: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: responsiveFont(13),
                  color: Colors.grey,
                ),
                contentPadding: const EdgeInsets.symmetric(
                  horizontal: 12,
                  vertical: 10,
                ),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(6),
                  borderSide: BorderSide(color: Colors.grey.shade300),
                ),
                enabledBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(6),
                  borderSide: BorderSide(color: Colors.grey.shade300),
                ),
                suffixIcon: GestureDetector(
                  onTap: _scanBarcode,
                  child: Padding(
                    padding: const EdgeInsets.all(10),
                    child: Image.asset(
                      icBarcodeIcon,
                      width: 24,
                      height: 24,
                      errorBuilder: (ctx, e, st) =>
                          Icon(Icons.qr_code_scanner, color: kPrimaryColor),
                    ),
                  ),
                ),
              ),
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: responsiveFont(13),
              ),
            ),
            const SizedBox(height: 12),

            // Select Lab field
            Text(
              'Select Lab',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: responsiveFont(12),
                color: kBlackColor,
              ),
            ),
            const SizedBox(height: 4),
            GestureDetector(
              onTap: () {
                if (_labList.isNotEmpty) {
                  _showLabPicker();
                } else {
                  getLabList();
                }
              },
              child: Container(
                width: double.infinity,
                padding: const EdgeInsets.symmetric(
                  horizontal: 12,
                  vertical: 10,
                ),
                decoration: BoxDecoration(
                  color: Colors.white,
                  border: Border.all(color: Colors.grey.shade300),
                  borderRadius: BorderRadius.circular(6),
                ),
                child: Row(
                  children: [
                    Expanded(
                      child: Text(
                        _selectedLabName ?? 'Select Lab',
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: responsiveFont(13),
                          color:
                              _selectedLabName == null
                                  ? Colors.grey
                                  : kBlackColor,
                        ),
                      ),
                    ),
                    Icon(Icons.arrow_drop_down, color: Colors.grey.shade600),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 12),

            // Sample Count field
            Text(
              'Sample Count',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: responsiveFont(12),
                color: kBlackColor,
              ),
            ),
            const SizedBox(height: 4),
            TextField(
              controller: _sampleCountController,
              keyboardType: TextInputType.number,
              maxLength: 3,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              decoration: InputDecoration(
                counterText: '',
                hintText: 'Enter sample count',
                hintStyle: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: responsiveFont(13),
                  color: Colors.grey,
                ),
                contentPadding: const EdgeInsets.symmetric(
                  horizontal: 12,
                  vertical: 10,
                ),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(6),
                  borderSide: BorderSide(color: Colors.grey.shade300),
                ),
                enabledBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(6),
                  borderSide: BorderSide(color: Colors.grey.shade300),
                ),
              ),
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: responsiveFont(13),
              ),
            ),
            const SizedBox(height: 16),

            // Beneficiary Photo section
            Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 12),
              decoration: BoxDecoration(
                color: const Color(0xFF424242),
                borderRadius: const BorderRadius.vertical(
                  top: Radius.circular(6),
                ),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(
                      'Beneficiary Photo',
                      style: TextStyle(
                        color: Colors.white,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w600,
                        fontSize: responsiveFont(13),
                      ),
                    ),
                  ),
                  Text(
                    'Skip Face Detection',
                    style: TextStyle(
                      color: Colors.white,
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(11),
                    ),
                  ),
                  Transform.scale(
                    scale: 0.8,
                    child: Switch(
                      value: _skipFaceDetection,
                      onChanged: (v) => setState(() => _skipFaceDetection = v),
                      activeThumbColor: Colors.green,
                    ),
                  ),
                ],
              ),
            ),
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.grey.shade50,
                border: Border.all(color: Colors.grey.shade300),
                borderRadius: const BorderRadius.vertical(
                  bottom: Radius.circular(6),
                ),
              ),
              child: Column(
                children: [
                  GestureDetector(
                    onTap: _capturePatientPhoto,
                    child:
                        _patientPhotoFile != null
                            ? ClipOval(
                              child: Image.file(
                                _patientPhotoFile!,
                                width: 80,
                                height: 80,
                                fit: BoxFit.cover,
                              ),
                            )
                            : Container(
                              width: 80,
                              height: 80,
                              decoration: BoxDecoration(
                                shape: BoxShape.circle,
                                color: Colors.grey.shade200,
                                border: Border.all(color: Colors.grey.shade400),
                              ),
                              child: Icon(
                                Icons.camera_alt,
                                size: 36,
                                color: Colors.grey.shade500,
                              ),
                            ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    'Capture Beneficiary Photo*',
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(12),
                      color: kBlackColor,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 16),

            // Consent Form & Photo ID section
            Container(
              width: double.infinity,
              padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 12),
              decoration: BoxDecoration(
                color: const Color(0xFF424242),
                borderRadius: const BorderRadius.vertical(
                  top: Radius.circular(6),
                ),
              ),
              child: Text(
                'Consent Form & Photo ID',
                style: TextStyle(
                  color: Colors.white,
                  fontFamily: FontConstants.interFonts,
                  fontWeight: FontWeight.w600,
                  fontSize: responsiveFont(13),
                ),
              ),
            ),
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: Colors.grey.shade50,
                border: Border.all(color: Colors.grey.shade300),
                borderRadius: const BorderRadius.vertical(
                  bottom: Radius.circular(6),
                ),
              ),
              child: Column(
                children: [
                  Text(
                    'संमतीपत्र आणि लाभार्थ्याच्या ओळखपत्राचा एकत्र फोटो घेणे आवश्यक आहे.',
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(12),
                      color: Colors.blue.shade800,
                    ),
                  ),
                  const SizedBox(height: 12),
                  GestureDetector(
                    onTap: _captureConsentPhoto,
                    child:
                        _consentPhotoFile != null
                            ? ClipRRect(
                              borderRadius: BorderRadius.circular(8),
                              child: Image.file(
                                _consentPhotoFile!,
                                width: 80,
                                height: 80,
                                fit: BoxFit.cover,
                              ),
                            )
                            : Container(
                              width: 80,
                              height: 80,
                              decoration: BoxDecoration(
                                borderRadius: BorderRadius.circular(8),
                                color: Colors.grey.shade200,
                                border: Border.all(color: Colors.grey.shade400),
                              ),
                              child: Icon(
                                Icons.camera_alt,
                                size: 36,
                                color: Colors.grey.shade500,
                              ),
                            ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    'Capture Consent Form & Photo ID*',
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(12),
                      color: kBlackColor,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 20),

          ], // end _isCollectionRemark

          // Submit button — shown after OTP verified
          if (_isAppointmentConfirmed && _isOtpVerified) ...[
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: () => _submitSampleCollection(),
                style: ElevatedButton.styleFrom(
                  backgroundColor: kPrimaryColor,
                  padding: const EdgeInsets.symmetric(vertical: 14),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(8),
                  ),
                ),
                child: Text(
                  'Submit',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w700,
                    fontSize: responsiveFont(15),
                    color: Colors.white,
                  ),
                ),
              ),
            ),
          ],

          // Appointment pending warning
          if (!_isAppointmentConfirmed) ...[
            _buildAppointmentPendingCard(),
            const SizedBox(height: 12),
          ],
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
