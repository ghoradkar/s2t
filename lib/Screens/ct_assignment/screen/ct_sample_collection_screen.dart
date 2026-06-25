// ignore_for_file: file_names, use_build_context_synchronously

import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ChooseDocumentManager/ChooseDocumentManager.dart';
import 'package:s2toperational/Modules/Json_Class/AssignmentRemarksResponse/AssignmentRemarksResponse.dart';
import 'package:s2toperational/Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/selection_bottom_sheet.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/face_detection_screen.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';
import '../controller/ct_sample_collection_controller.dart';
import '../model/beneficiary_details_for_assign_teamid_details_response.dart';
import '../widget/patient_sample_collection_header_widget.dart';
import '../widget/test_details_widget.dart';
import '../widget/tube_details_widget.dart';

class CTSampleCollectionScreen extends StatelessWidget {
  const CTSampleCollectionScreen({
    super.key,
    required this.beneficiaryDetails,
    this.isAppointmentFlow = false,
  });

  final BeneficiaryDetailsforAssignTeamidOutput? beneficiaryDetails;
  final bool isAppointmentFlow;

  @override
  Widget build(BuildContext context) {
    Get.put(CTSampleCollectionController(
      beneficiaryDetails: beneficiaryDetails,
      isAppointmentFlow: isAppointmentFlow,
    ));
    SizeConfig().init(context);
    return GetBuilder<CTSampleCollectionController>(
      builder: (ctrl) {
        if (ctrl.shouldShowAppointmentPendingAlert) {
          ctrl.shouldShowAppointmentPendingAlert = false;
          WidgetsBinding.instance.addPostFrameCallback(
            (_) => _showAppointmentPendingAlert(context),
          );
        }
        return NetworkWrapper(
          child: Scaffold(
            appBar: mAppBar(
              scTitle: 'Sample Collection',
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () => Navigator.pop(context),
            ),
            body: SingleChildScrollView(
              padding: const EdgeInsets.all(8),
              child: Column(
                children: [
                  PatientSampleCollectionHeaderWidget(
                    fullName:
                        beneficiaryDetails?.beneficiaryName ??
                        ctrl.testDetailslist?.beneficiaryName ??
                        '',
                    gender: ctrl.testDetailslist?.gender ?? '',
                    age: ctrl.testDetailslist?.patAge?.toString() ?? '',
                  ),
                  const SizedBox(height: 10),
                  TestDetailsWidget(list: ctrl.list),
                  const SizedBox(height: 10),
                  TubeDetailsWidget(
                    list: ctrl.tubesDetailslist,
                    countControllers: ctrl.tubeCountControllers,
                  ),
                  if (isAppointmentFlow) ...[
                    const SizedBox(height: 12),
                    _buildSampleCollectionSection(context, ctrl),
                    const SizedBox(height: 20),
                  ],
                ],
              ),
            ),
          ),
        );
      },
    );
  }

  Widget _buildSampleCollectionSection(BuildContext context, CTSampleCollectionController ctrl) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.all(16.w),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(offset: const Offset(0, 1), color: Colors.black.withValues(alpha: 0.1), blurRadius: 6),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          AppTextField(
            controller: ctrl.remarkController,
            readOnly: true,
            onTap: ctrl.remarksList.isEmpty ? null : () => _showRemarkPicker(context, ctrl),
            label: CommonText(text: 'CT Assignment Remark *', fontSize: 12.sp, fontWeight: FontWeight.w400, textColor: kTextColor, textAlign: TextAlign.start),
            hint: 'Select Remark',
            hintStyle: TextStyle(fontSize: 12.sp, fontFamily: FontConstants.interFonts),
            suffixIcon: Icon(Icons.arrow_drop_down, color: kPrimaryColor),
            fieldRadius: 8,
          ),
          SizedBox(height: 12.h),
          _buildOtpCard(context, ctrl),
          SizedBox(height: 14.h),
          if (ctrl.isAppointmentConfirmed && ctrl.isCollectionRemark && !ctrl.isSampleCollected) ...[
            AppTextField(
              controller: ctrl.barcodeController,
              maxLength: 14,
              textInputType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              label: CommonText(text: 'Barcode', fontSize: 12.sp, fontWeight: FontWeight.w400, textColor: kTextColor, textAlign: TextAlign.start),
              hint: 'Enter or scan barcode',
              hintStyle: TextStyle(fontSize: 12.sp, fontFamily: FontConstants.interFonts),
              suffixIcon: GestureDetector(
                onTap: () => _scanBarcode(ctrl),
                child: Padding(
                  padding: const EdgeInsets.all(10),
                  child: Image.asset(icBarcodeIcon, width: 24, height: 24,
                      errorBuilder: (ctx, e, st) => Icon(Icons.qr_code_scanner, color: kPrimaryColor)),
                ),
              ),
              fieldRadius: 8,
            ),
            SizedBox(height: 12.h),
            AppTextField(
              controller: ctrl.labController,
              readOnly: true,
              onTap: () => ctrl.labList.isNotEmpty ? _showLabPicker(context, ctrl, ctrl.labList) : _fetchAndShowLabPicker(context, ctrl),
              label: CommonText(text: 'Select Lab', fontSize: 12.sp, fontWeight: FontWeight.w400, textColor: kTextColor, textAlign: TextAlign.start),
              hint: 'Select Lab',
              hintStyle: TextStyle(fontSize: 12.sp, fontFamily: FontConstants.interFonts),
              suffixIcon: Icon(Icons.arrow_drop_down, color: kPrimaryColor),
              fieldRadius: 8,
            ),
            SizedBox(height: 12.h),
            AppTextField(
              controller: ctrl.sampleCountController,
              textInputType: TextInputType.number,
              maxLength: 3,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              label: CommonText(text: 'Sample Count', fontSize: 12.sp, fontWeight: FontWeight.w400, textColor: kTextColor, textAlign: TextAlign.start),
              hint: 'Enter sample count',
              hintStyle: TextStyle(fontSize: 12.sp, fontFamily: FontConstants.interFonts),
              fieldRadius: 8,
            ),
            SizedBox(height: 16.h),
            _buildPhotosRow(context, ctrl),
            SizedBox(height: 20.h),
          ],
          if (ctrl.isAppointmentConfirmed && !ctrl.isSampleCollected) ...[
            SizedBox(
              width: double.infinity,
              height: 46.h,
              child: AppActiveButton(
                buttontitle: 'Submit',
                onTap: () => _handleSubmit(context, ctrl),
              ),
            ),
          ],
          if (ctrl.isSampleCollected) _buildStatusCard('Sample Already Collected', Colors.green, Icons.check_circle_outline),
          if (!ctrl.isAppointmentConfirmed) _buildStatusCard('Appointment Confirmation Pending', Colors.orange, Icons.info_outline),
        ],
      ),
    );
  }

  Widget _buildOtpCard(BuildContext context, CTSampleCollectionController ctrl) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.grey.shade200),
        boxShadow: [BoxShadow(offset: const Offset(0, 1), color: Colors.black.withValues(alpha: 0.06), blurRadius: 6)],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
            decoration: BoxDecoration(color: kPrimaryColor, borderRadius: const BorderRadius.vertical(top: Radius.circular(10))),
            child: Row(
              children: [
                Icon(Icons.phone_android_rounded, color: Colors.white, size: 16.sp),
                SizedBox(width: 8.w),
                CommonText(text: 'OTP Verification', fontSize: 13.sp, fontWeight: FontWeight.w700, textColor: Colors.white, textAlign: TextAlign.start),
              ],
            ),
          ),
          Padding(
            padding: EdgeInsets.all(14.w),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                AppTextField(
                  controller: ctrl.mobileController,
                  readOnly: true,
                  onTap: () => _showMobileNumberPicker(context, ctrl),
                  label: CommonText(text: 'Select Number To Send OTP', fontSize: 12.sp, fontWeight: FontWeight.w400, textColor: kTextColor, textAlign: TextAlign.start),
                  hint: 'Select Number',
                  hintStyle: TextStyle(fontSize: 12.sp, fontFamily: FontConstants.interFonts),
                  suffixIcon: Icon(Icons.arrow_drop_down, color: kPrimaryColor),
                  fieldRadius: 8,
                ),
                SizedBox(height: 12.h),
                GestureDetector(
                  onTap: ctrl.toggleSmsVendorExpanded,
                  child: Container(
                    padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
                    decoration: BoxDecoration(color: kPurpleFaint, borderRadius: BorderRadius.circular(8)),
                    child: Row(
                      children: [
                        Icon(Icons.settings_outlined, size: 14.sp, color: kPrimaryColor),
                        SizedBox(width: 6.w),
                        Expanded(child: CommonText(text: 'SMS Vendor Settings', fontSize: 12.sp, fontWeight: FontWeight.w600, textColor: kPrimaryColor, textAlign: TextAlign.start)),
                        Icon(ctrl.isSmsVendorExpanded ? Icons.keyboard_arrow_up : Icons.keyboard_arrow_down, color: kPrimaryColor, size: 18.sp),
                      ],
                    ),
                  ),
                ),
                if (ctrl.isSmsVendorExpanded) ...[
                  SizedBox(height: 10.h),
                  Container(
                    padding: EdgeInsets.all(10.w),
                    decoration: BoxDecoration(color: Colors.blue.shade50, borderRadius: BorderRadius.circular(8), border: Border.all(color: Colors.blue.shade100)),
                    child: Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Icon(Icons.info_outline, size: 14.sp, color: Colors.blue.shade700),
                        SizedBox(width: 6.w),
                        Expanded(child: CommonText(text: 'जर लाभार्थ्याच्या मोबाईलवर OTP प्राप्त झाला नसेल तर option 2 वापरून OTP पाठवा', fontSize: 11.sp, fontWeight: FontWeight.w400, textColor: Colors.blue.shade800, textAlign: TextAlign.start)),
                      ],
                    ),
                  ),
                  SizedBox(height: 10.h),
                  Row(
                    children: [
                      _radioOption('Option 1', 1, ctrl),
                      SizedBox(width: 24.w),
                      _radioOption('Option 2', 2, ctrl),
                    ],
                  ),
                ],
                if (ctrl.isAppointmentConfirmed && !ctrl.isOtpVerified && !ctrl.isSampleCollected && ctrl.isCollectionRemark) ...[
                  SizedBox(height: 14.h),
                  SizedBox(
                    width: double.infinity,
                    height: 46.h,
                    child: AppActiveButton(
                      buttontitle: 'Send OTP',
                      onTap: ctrl.activeMobile.isEmpty ? () {} : ctrl.sendOTP,
                    ),
                  ),
                  if (ctrl.isOtpSent) ...[
                    SizedBox(height: 12.h),
                    Row(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Expanded(
                          child: AppTextField(
                            controller: ctrl.otpController,
                            textInputType: TextInputType.number,
                            maxLength: 6,
                            label: CommonText(text: 'Enter OTP', fontSize: 12.sp, fontWeight: FontWeight.w400, textColor: kTextColor, textAlign: TextAlign.start),
                            hint: 'Enter OTP',
                            hintStyle: TextStyle(fontSize: 12.sp, fontFamily: FontConstants.interFonts),
                            fieldRadius: 8,
                          ),
                        ),
                        SizedBox(width: 8.w),
                        SizedBox(
                          width: 110.w,
                          height: 56.h,
                          child: AppActiveButton(buttontitle: 'Verify', onTap: ctrl.verifyOTP),
                        ),
                      ],
                    ),
                  ],
                ],
                if (ctrl.isOtpVerified) ...[
                  SizedBox(height: 12.h),
                  Container(
                    padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 8.h),
                    decoration: BoxDecoration(color: Colors.green.shade50, borderRadius: BorderRadius.circular(8), border: Border.all(color: Colors.green.shade200)),
                    child: Row(
                      children: [
                        Icon(Icons.check_circle, color: Colors.green.shade600, size: 16.sp),
                        SizedBox(width: 8.w),
                        CommonText(text: 'OTP Verified Successfully', fontSize: 12.sp, fontWeight: FontWeight.w600, textColor: Colors.green.shade700, textAlign: TextAlign.start),
                      ],
                    ),
                  ),
                ],
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildPhotosRow(BuildContext context, CTSampleCollectionController ctrl) {
    return IntrinsicHeight(
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.stretch,
        children: [
          Expanded(
            child: _buildSinglePhotoCard(
              context: context,
              ctrl: ctrl,
              title: 'Beneficiary Photo',
              icon: Icons.person_outline,
              photoFile: ctrl.patientPhotoFile,
              onCapture: () => _capturePatientPhoto(context, ctrl),
              captureLabel: 'Tap to capture *',
              showSkipToggle: true,
            ),
          ),
          SizedBox(width: 10.w),
          Expanded(
            child: _buildSinglePhotoCard(
              context: context,
              ctrl: ctrl,
              title: 'Consent & Photo ID',
              icon: Icons.description_outlined,
              photoFile: ctrl.consentPhotoFile,
              onCapture: () => _captureConsentPhoto(ctrl),
              captureLabel: 'Tap to capture *',
              hintText: 'संमतीपत्र आणि ओळखपत्र एकत्र फोटो',
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildSinglePhotoCard({
    required BuildContext context,
    required CTSampleCollectionController ctrl,
    required String title,
    required IconData icon,
    required File? photoFile,
    required VoidCallback onCapture,
    required String captureLabel,
    bool showSkipToggle = false,
    String? hintText,
  }) {
    final captured = photoFile != null;
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: captured ? Colors.green.shade200 : Colors.grey.shade200),
        boxShadow: [BoxShadow(offset: const Offset(0, 1), color: Colors.black.withValues(alpha: 0.06), blurRadius: 6)],
      ),
      child: Column(
        children: [
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 9.h),
            decoration: BoxDecoration(
              color: captured ? Colors.green.shade600 : kPrimaryColor,
              borderRadius: const BorderRadius.vertical(top: Radius.circular(10)),
            ),
            child: Row(
              children: [
                Icon(captured ? Icons.check_circle_outline : icon, color: Colors.white, size: 14.sp),
                SizedBox(width: 6.w),
                Expanded(child: CommonText(text: title, fontSize: 11.sp, fontWeight: FontWeight.w600, textColor: Colors.white, textAlign: TextAlign.start, maxLine: 1, overflow: TextOverflow.ellipsis)),
              ],
            ),
          ),
          Expanded(
            child: Padding(
              padding: EdgeInsets.symmetric(vertical: 14.h, horizontal: 10.w),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  GestureDetector(
                    onTap: onCapture,
                    child: captured
                        ? Stack(
                            alignment: Alignment.bottomRight,
                            children: [
                              ClipRRect(borderRadius: BorderRadius.circular(8), child: Image.file(photoFile, width: 80.w, height: 80.w, fit: BoxFit.cover)),
                              Container(
                                width: 22.w, height: 22.w,
                                decoration: BoxDecoration(color: kPrimaryColor, shape: BoxShape.circle, border: Border.all(color: Colors.white, width: 1.5)),
                                child: Icon(Icons.edit, color: Colors.white, size: 11.sp),
                              ),
                            ],
                          )
                        : Container(
                            width: 80.w, height: 80.w,
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(8),
                              color: kPurpleFaint,
                              border: Border.all(color: kPrimaryColor.withValues(alpha: 0.3), width: 1.5),
                            ),
                            child: Column(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Icon(Icons.add_a_photo_outlined, size: 24.sp, color: kPrimaryColor),
                                SizedBox(height: 3.h),
                                CommonText(text: 'Tap to add', fontSize: 8.sp, fontWeight: FontWeight.w500, textColor: kPrimaryColor, textAlign: TextAlign.center),
                              ],
                            ),
                          ),
                  ),
                  SizedBox(height: 8.h),
                  CommonText(text: captureLabel, fontSize: 10.sp, fontWeight: FontWeight.w500, textColor: captured ? Colors.green.shade600 : kTextColor, textAlign: TextAlign.center),
                  if (hintText != null) ...[
                    SizedBox(height: 5.h),
                    CommonText(text: hintText, fontSize: 9.sp, fontWeight: FontWeight.w400, textColor: Colors.blue.shade700, textAlign: TextAlign.center),
                  ],
                  if (showSkipToggle) ...[
                    SizedBox(height: 6.h),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        CommonText(text: 'Skip Face\nDetection', fontSize: 9.sp, fontWeight: FontWeight.w400, textColor: kTextColor, textAlign: TextAlign.center),
                        Transform.scale(
                          scale: 0.7,
                          child: Switch(
                            value: ctrl.skipFaceDetection,
                            onChanged: ctrl.toggleSkipFaceDetection,
                            activeThumbColor: Colors.green,
                            inactiveThumbColor: kBlackColor.withValues(alpha: 0.4),
                            inactiveTrackColor: kBlackColor.withValues(alpha: 0.2),
                            activeTrackColor: Colors.green.withValues(alpha: 0.3),
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

  Widget _buildStatusCard(String text, MaterialColor color, IconData icon) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.symmetric(vertical: 10, horizontal: 12),
      decoration: BoxDecoration(color: color[50], borderRadius: BorderRadius.circular(8), border: Border.all(color: color.shade300)),
      child: Row(
        children: [
          Icon(icon, color: color[700], size: 18),
          const SizedBox(width: 8),
          Expanded(child: Text(text, style: TextStyle(fontSize: responsiveFont(12), fontFamily: FontConstants.interFonts, fontWeight: FontWeight.w600, color: color[800]))),
        ],
      ),
    );
  }

  Widget _radioOption(String label, int value, CTSampleCollectionController ctrl) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Radio<int>(
          value: value,
          groupValue: ctrl.selectedVendor,
          onChanged: (v) => ctrl.setVendor(v!),
          activeColor: kPrimaryColor,
          materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
          visualDensity: VisualDensity.compact,
        ),
        Text(label, style: TextStyle(fontFamily: FontConstants.interFonts, fontSize: responsiveFont(12), color: kBlackColor)),
      ],
    );
  }

  void _showRemarkPicker(BuildContext context, CTSampleCollectionController ctrl) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(16))),
      builder: (_) => SelectionBottomSheet<AssignmentRemarksOutput, int>(
        title: 'CT Assignment Remark',
        items: ctrl.remarksList,
        selectedValue: ctrl.selectedRemark?.arId,
        valueFor: (item) => item.arId ?? 0,
        labelFor: (item) => item.assignmentRemarks ?? 'N/A',
        height: 380,
        padding: EdgeInsets.only(top: responsiveHeight(28), left: responsiveHeight(35), right: responsiveHeight(35), bottom: responsiveHeight(60)),
        titleTextStyle: TextStyle(fontSize: responsiveFont(16), fontFamily: FontConstants.interFonts, fontWeight: FontWeight.w600),
        titleBottomSpacing: responsiveHeight(20),
        selectedBackgroundColor: kPrimaryColor.withValues(alpha: 0.1),
        itemTextStyle: TextStyle(fontSize: responsiveFont(13), fontFamily: FontConstants.interFonts, color: kBlackColor),
        onItemTap: (item) {
          ctrl.setRemark(item);
          Navigator.pop(context);
          _onRemarkSelected(context, item);
        },
      ),
    );
  }

  void _onRemarkSelected(BuildContext context, AssignmentRemarksOutput remark) {
    final arId = remark.arId ?? 0;
    String? message;
    if (arId == 4) {
      message = 'This beneficiary will not be available for CT screening.\nPlease verify before proceeding with the submission.';
    } else if (arId == 6) {
      message = 'This beneficiary can be re-attempted for CT screening.';
    }
    if (message != null) {
      showDialog(
        context: context,
        barrierDismissible: false,
        builder: (_) => AlertDialog(
          title: Row(children: [const Icon(Icons.warning_amber_rounded, color: Colors.red), const SizedBox(width: 8), const Text('Alert')]),
          content: Text(message!),
          actions: [TextButton(onPressed: () => Navigator.pop(context), child: const Text('OK'))],
        ),
      );
    }
  }

  void _showMobileNumberPicker(BuildContext context, CTSampleCollectionController ctrl) {
    final workerMob = ctrl.testDetailslist?.workersMob ?? ctrl.testDetailslist?.mobileNo ?? '';
    final alternateMob = ctrl.testDetailslist?.alternateMobNo ?? '';
    final options = <String>[
      if (workerMob.isNotEmpty) workerMob,
      if (alternateMob.isNotEmpty) alternateMob,
      '9371023232',
    ];
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(16))),
      builder: (_) => SelectionBottomSheet<String, String>(
        title: 'Select Number To Send OTP',
        items: options,
        selectedValue: ctrl.selectedMobileNumber ?? ctrl.activeMobile,
        valueFor: (item) => item,
        labelFor: (item) => item,
        height: 300,
        padding: EdgeInsets.only(top: responsiveHeight(28), left: responsiveHeight(35), right: responsiveHeight(35), bottom: responsiveHeight(60)),
        titleTextStyle: TextStyle(fontSize: responsiveFont(16), fontFamily: FontConstants.interFonts, fontWeight: FontWeight.w600),
        titleBottomSpacing: responsiveHeight(20),
        selectedBackgroundColor: kPrimaryColor.withValues(alpha: 0.1),
        itemTextStyle: TextStyle(fontSize: responsiveFont(13), fontFamily: FontConstants.interFonts, color: kBlackColor),
        onItemTap: (mob) {
          ctrl.setMobileNumber(mob);
          Navigator.pop(context);
        },
      ),
    );
  }

  Future<void> _fetchAndShowLabPicker(BuildContext context, CTSampleCollectionController ctrl) async {
    final labs = await ctrl.fetchLabList();
    if (labs.isNotEmpty) _showLabPicker(context, ctrl, labs);
  }

  void _showLabPicker(BuildContext context, CTSampleCollectionController ctrl, List<LandingLabCampCreationOutput> labs) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(16))),
      builder: (_) => SelectionBottomSheet<LandingLabCampCreationOutput, int>(
        title: 'Select Lab',
        items: labs,
        selectedValue: ctrl.selectedLabCode,
        valueFor: (lab) => lab.labCode ?? 0,
        labelFor: (lab) => lab.labName ?? '',
        height: 420,
        padding: EdgeInsets.only(top: responsiveHeight(28), left: responsiveHeight(35), right: responsiveHeight(35), bottom: responsiveHeight(60)),
        titleTextStyle: TextStyle(fontSize: responsiveFont(16), fontFamily: FontConstants.interFonts, fontWeight: FontWeight.w600),
        titleBottomSpacing: responsiveHeight(20),
        selectedBackgroundColor: kPrimaryColor.withValues(alpha: 0.1),
        itemTextStyle: TextStyle(fontSize: responsiveFont(13), fontFamily: FontConstants.interFonts, color: kBlackColor),
        onItemTap: (lab) {
          ctrl.setLabSelection(lab);
          Navigator.pop(context);
        },
      ),
    );
  }

  Future<void> _scanBarcode(CTSampleCollectionController ctrl) async {
    final res = await SimpleBarcodeScanner.scanBarcode(
      // ignore: use_build_context_synchronously
      Get.context!,
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
      ctrl.barcodeController.text = res;
      ctrl.update();
    }
  }

  Future<void> _capturePatientPhoto(BuildContext context, CTSampleCollectionController ctrl) async {
    if (ctrl.skipFaceDetection) {
      final result = await ChooseDocumentManager.pickFile(FileSourceType.camera);
      if (result != null) ctrl.setPatientPhoto(result.file);
    } else {
      final File? photo = await Navigator.push<File?>(context, MaterialPageRoute(builder: (_) => const FaceDetectionScreen()));
      if (photo != null) ctrl.setPatientPhoto(photo);
    }
  }

  Future<void> _captureConsentPhoto(CTSampleCollectionController ctrl) async {
    final result = await ChooseDocumentManager.pickFile(FileSourceType.camera);
    if (result != null) ctrl.setConsentPhoto(result.file);
  }

  Future<void> _handleSubmit(BuildContext context, CTSampleCollectionController ctrl) async {
    final ok = await ctrl.submitSampleCollection();
    if (ok) {
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
                Navigator.pop(context, true);
              },
              child: const Text('OK'),
            ),
          ],
        ),
      );
    }
  }

  void _showAppointmentPendingAlert(BuildContext context) {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (_) => AlertDialog(
        title: Text('Alert', style: TextStyle(fontFamily: FontConstants.interFonts)),
        content: Text('Please confirm appointment date', style: TextStyle(fontFamily: FontConstants.interFonts)),
        actions: [TextButton(onPressed: () => Navigator.pop(context), child: const Text('OK'))],
      ),
    );
  }
}
