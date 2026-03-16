// ignore_for_file: must_be_immutable

import 'dart:io';
import 'dart:typed_data';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/medicine_delivery/controller/MedicineDeliveryAcknowledgementController.dart';
import 'package:s2toperational/Modules/Json_Class/PostCampBeneficiaryListResponse/PostCampBeneficiaryListResponse.dart';

class MedicineDeliveryAcknowledgementScreen extends StatefulWidget {
  final PostCampBeneficiaryOutput beneficiary;

  const MedicineDeliveryAcknowledgementScreen({
    super.key,
    required this.beneficiary,
  });

  @override
  State<MedicineDeliveryAcknowledgementScreen> createState() =>
      _MedicineDeliveryAcknowledgementScreenState();
}

class _MedicineDeliveryAcknowledgementScreenState
    extends State<MedicineDeliveryAcknowledgementScreen> {
  late final MedicineDeliveryAcknowledgementController controller;

  @override
  void initState() {
    super.initState();
    controller = Get.put(
      MedicineDeliveryAcknowledgementController(
        beneficiary: widget.beneficiary,
      ),
    );
  }

  @override
  void dispose() {
    Get.delete<MedicineDeliveryAcknowledgementController>();
    super.dispose();
  }

  // ── Native color palette ───────────────────────────────────────────────────
  // static const _purple = Color(0xFF423897);
  static const _green = Color(0xFF4CAF50);
  static const _blue = Color(0xFF1565C0);
  static const _red = Color(0xFFD32F2F);

  // ── Build ─────────────────────────────────────────────────────────────────

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey.shade100,
      appBar: mAppBar(
        scTitle: 'Medicine Delivery Acknowledgment',
        showActions: false,
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: Obx(
        () =>
            controller.isSubmitting.value
                ? const Center(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      CircularProgressIndicator(color: kPrimaryColor),
                      SizedBox(height: 16),
                      Text('Submitting...'),
                    ],
                  ),
                )
                : SingleChildScrollView(
                  padding: EdgeInsets.symmetric(
                    horizontal: 12.w,
                    vertical: 12.h,
                  ),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      // ── 1. Patient Info Card ──────────────────────────────
                      _buildPatientInfoCard(),
                      SizedBox(height: 12.h),

                      if (controller.mode == AckMode.pending) ...[
                        _buildPendingContent(),
                      ] else if (controller.mode == AckMode.notAvailable) ...[
                        _buildPendingContent(),
                      ] else ...[
                        _buildViewOnlyContent(),
                      ],
                      SizedBox(height: 30.h),
                    ],
                  ),
                ),
      ),
    );
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PATIENT INFO — native CardView with gradient name header
  // ═══════════════════════════════════════════════════════════════════════════

  Widget _buildPatientInfoCard() {
    final b = widget.beneficiary;
    return Card(
      margin: EdgeInsets.zero,
      elevation: 3,
      clipBehavior: Clip.antiAlias,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          _gradientHeader(
            title: b.patientName ?? 'Patient Details',
            icon: Icons.person_rounded,
          ),

          // Fields
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 8.h),
            child: Column(
              children: [
                _nativeField(hint: 'Address', value: b.beneficiryAddress),
                SizedBox(height: 8.h),
                Row(
                  children: [
                    Expanded(
                      child: _nativeField(
                        hint: 'Beneficiary ID',
                        value: b.beneficiryNumber,
                      ),
                    ),
                    SizedBox(width: 8.w),
                    Expanded(
                      child: _nativeField(
                        hint: 'Prescription No.',
                        value: b.treatmentId?.toString(),
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 8.h),
                Row(
                  children: [
                    Expanded(
                      child: _nativeField(
                        hint: 'Pincode',
                        value: b.pincode?.toString(),
                      ),
                    ),
                    SizedBox(width: 8.w),
                    Expanded(
                      child: _nativeField(
                        hint: 'Delivery Challan No.',
                        value: b.deliveryChallanId,
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PENDING MODE CONTENT — full form
  // ═══════════════════════════════════════════════════════════════════════════

  Widget _buildPendingContent() {
    return Obx(() {
      final statusId = controller.selectedStatus.value?.id;
      final isDelivered = controller.isStatusDelivered;
      final isNonDelivery = controller.isStatusNonDelivery;

      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // ── Delivery Status dropdown (always visible in pending) ──────────
          _buildDeliveryStatusDropdown(),

          // ── Remark (status 2/3 only) ─────────────────────────────────────
          if (isNonDelivery) ...[
            SizedBox(height: 12.h),
            _buildRemarkDropdown(),
            if (controller.showOtherRemarkField) ...[
              SizedBox(height: 12.h),
              _buildOtherDescriptionField(),
            ],
          ],

          // ── SMS Vendor (status 1 only) ────────────────────────────────────
          if (isDelivered) ...[
            SizedBox(height: 12.h),
            _buildSmsVendorSection(),
          ],

          // ── OTP Verification (status 1 or Beneficiary Not Available) ─────
          if (controller.isStatusOtpAllowed) ...[
            SizedBox(height: 12.h),
            _buildOtpSection(),
          ],

          // ── Beneficiary Photo (shown when status selected) ────────────────
          if (statusId != null) ...[
            SizedBox(height: 12.h),
            _buildBeneficiaryPhotoSection(),
          ],

          // ── Submit button (non-delivery statuses) ─────────────────────────
          if (isNonDelivery) ...[
            SizedBox(height: 12.h),
            AppButtonWithIcon(
              title: 'Submit',
              onTap: () => controller.submitPhotoOnly(context),
              mWidth: double.infinity,
              mHeight: 48,
              borderRadius: 8,
            ),
          ],

          // ── Delivery Challan (status 1 only) ─────────────────────────────
          if (isDelivered) ...[
            SizedBox(height: 12.h),
            _buildDeliveryChallanSection(),
          ],

          // ── Consent Form (status 1 only) ──────────────────────────────────
          if (isDelivered) ...[
            SizedBox(height: 12.h),
            _buildConsentFormSection(),
          ],

          // ── Deliver Medicines button (status 1, after OTP verified) ───────
          if (isDelivered) ...[
            Obx(() {
              if (!controller.isOtpVerified.value)
                return const SizedBox.shrink();
              return Column(
                children: [
                  SizedBox(height: 12.h),
                  AppButtonWithIcon(
                    title: 'Deliver Medicines',
                    onTap: () => controller.submitDeliveryAck(context),
                    mWidth: double.infinity,
                    mHeight: 48,
                    borderRadius: 8,
                  ),
                ],
              );
            }),
          ],

          // Status message (native: bottom of screen for re-attempt)
          if (controller.mode == AckMode.notAvailable)
            _statusMessage('Need To Re-Attempt Medicines Delivery.', _blue),
        ],
      );
    });
  }

  // ── Delivery Status dropdown ───────────────────────────────────────────────
  // Native: plain label + outlined textview with arrow dropdown

  Widget _buildDeliveryStatusDropdown() {
    return _sectionCard(
      child: Obx(
        () => AppTextField(
          controller: TextEditingController(
            text: controller.selectedStatus.value?.name ?? '',
          ),
          readOnly: true,
          onTap: () => _showStatusPicker(),
          hint: 'Deliver Medicine To Beneficiary',
          label: CommonText(
            text: 'Delivery Status',
            fontSize: 12.sp,
            fontWeight: FontWeight.normal,
            textColor: kBlackColor,
            textAlign: TextAlign.start,
          ),
          hintStyle: TextStyle(
            fontSize: 12.sp,
            fontWeight: FontWeight.w400,
            fontFamily: FontConstants.interFonts,
          ),
          fieldRadius: 10,
          suffixIcon: const Icon(Icons.keyboard_arrow_down),
        ),
      ),
    );
  }

  // ── Remark dropdown ───────────────────────────────────────────────────────

  Widget _buildRemarkDropdown() {
    return _sectionCard(
      child: Obx(
        () => AppTextField(
          controller: TextEditingController(
            text: controller.selectedRemark.value?.name ?? '',
          ),
          readOnly: true,
          onTap:
              controller.remarkList.isEmpty ? null : () => _showRemarkPicker(),
          hint: 'Select Remark',
          label: CommonText(
            text: 'Select Remark',
            fontSize: 12.sp,
            fontWeight: FontWeight.normal,
            textColor: kBlackColor,
            textAlign: TextAlign.start,
          ),
          hintStyle: TextStyle(
            fontSize: 12.sp,
            fontWeight: FontWeight.w400,
            fontFamily: FontConstants.interFonts,
          ),
          fieldRadius: 10,
          suffixIcon:
              controller.remarkList.isEmpty
                  ? const SizedBox(
                    width: 18,
                    height: 18,
                    child: CircularProgressIndicator(
                      strokeWidth: 2,
                      color: kPrimaryColor,
                    ),
                  )
                  : const Icon(Icons.keyboard_arrow_down),
        ),
      ),
    );
  }

  // ── Other Description field ───────────────────────────────────────────────

  Widget _buildOtherDescriptionField() {
    return _sectionCard(
      child: AppTextField(
        controller: controller.otherRemarkController,
        readOnly: false,
        maxLines: 2,
        hint: 'Enter Other Description',
        label: CommonText(
          text: 'Other Description',
          fontSize: 12.sp,
          fontWeight: FontWeight.normal,
          textColor: kBlackColor,
          textAlign: TextAlign.start,
        ),
        hintStyle: TextStyle(
          fontSize: 12.sp,
          fontWeight: FontWeight.w400,
          fontFamily: FontConstants.interFonts,
        ),
        fieldRadius: 10,
      ),
    );
  }

  // ── SMS Vendor section ────────────────────────────────────────────────────
  // Native: gradient header "SMS Vendor" (tappable collapse), then radio group

  Widget _buildSmsVendorSection() {
    return Obx(() {
      final expanded = controller.smsVendorExpanded.value;
      return Card(
        margin: EdgeInsets.zero,
        elevation: 2,
        clipBehavior: Clip.antiAlias,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Column(
          children: [
            GestureDetector(
              onTap: () => controller.smsVendorExpanded.value = !expanded,
              child: _gradientHeader(
                title: 'SMS Vendor',
                icon: Icons.sms_outlined,
                trailing: Icon(
                  expanded ? Icons.arrow_drop_up : Icons.arrow_drop_down,
                  color: Colors.black54,
                  size: 24,
                ),
              ),
            ),
            if (expanded) ...[
              Padding(
                padding: EdgeInsets.all(12.w),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'If OTP not received on beneficiary\'s mobile, use Option 2 to send OTP',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: responsiveFont(13),
                        fontWeight: FontWeight.w600,
                        color: Colors.black87,
                      ),
                    ),
                    SizedBox(height: 12.h),
                    Obx(
                      () => Row(
                        children: [
                          Expanded(
                            child: _nativeRadioTile(
                              label: 'Option 1',
                              value: 0,
                              groupValue:
                                  controller.selectedSmsVendorIndex.value,
                              onChanged: controller.selectSmsVendor,
                            ),
                          ),
                          Expanded(
                            child: _nativeRadioTile(
                              label: 'Option 2',
                              value: 1,
                              groupValue:
                                  controller.selectedSmsVendorIndex.value,
                              onChanged: controller.selectSmsVendor,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ],
        ),
      );
    });
  }

  // ── OTP Section ───────────────────────────────────────────────────────────
  // Native: CardView "OTP Verification" + select number dropdown + worker/alt + send OTP btn

  Widget _buildOtpSection() {
    return Card(
      margin: EdgeInsets.zero,
      elevation: 2,
      clipBehavior: Clip.antiAlias,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Column(
        children: [
          _gradientHeader(
            title: 'OTP Verification',
            icon: Icons.lock_outline_rounded,
          ),

          Padding(
            padding: EdgeInsets.all(12.w),
            child: Column(
              children: [
                // Select Number row
                Row(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Expanded(
                      child: Obx(
                        () => AppTextField(
                          controller: TextEditingController(
                            text: controller.otpTargetNumber.value,
                          ),
                          readOnly: true,
                          onTap: _fetchAndShowOtpNumberPicker,
                          hint: 'Select Number',
                          label: CommonText(
                            text: 'Select Number To Send OTP',
                            fontSize: 12.sp,
                            fontWeight: FontWeight.normal,
                            textColor: kBlackColor,
                            textAlign: TextAlign.start,
                          ),
                          hintStyle: TextStyle(
                            fontSize: 12.sp,
                            fontWeight: FontWeight.w400,
                            fontFamily: FontConstants.interFonts,
                          ),
                          fieldRadius: 10,
                          suffixIcon:
                              controller.isFetchingMobileNumbers.value
                                  ? const SizedBox(
                                    width: 18,
                                    height: 18,
                                    child: CircularProgressIndicator(
                                      strokeWidth: 2,
                                      color: kPrimaryColor,
                                    ),
                                  )
                                  : const Icon(Icons.keyboard_arrow_down),
                        ),
                      ),
                    ),
                    SizedBox(width: 8.w),
                    // Styled call button
                    Obx(() {
                      final number = controller.otpTargetNumber.value;
                      return GestureDetector(
                        onTap: () async {
                          if (number.isEmpty) {
                            showDialog(
                              context: context,
                              builder:
                                  (_) => AlertDialog(
                                    title: const Text('Alert'),
                                    content: const Text('Please select number'),
                                    actions: [
                                      TextButton(
                                        onPressed: () => Navigator.pop(context),
                                        child: const Text('OK'),
                                      ),
                                    ],
                                  ),
                            );
                            return;
                          }
                          final uri = Uri(scheme: 'tel', path: number);
                          if (await canLaunchUrl(uri)) {
                            await launchUrl(uri);
                          }
                        },
                        child: Container(
                          width: 48.w,
                          height: 48.h,
                          decoration: BoxDecoration(
                            color:
                                number.isEmpty
                                    ? Colors.grey.shade100
                                    : kPrimaryColor.withValues(alpha: 0.1),
                            borderRadius: BorderRadius.circular(10),
                            border: Border.all(
                              color:
                                  number.isEmpty
                                      ? Colors.grey.shade300
                                      : kPrimaryColor,
                              width: 1.5,
                            ),
                          ),
                          child: Icon(
                            Icons.call_outlined,
                            color:
                                number.isEmpty
                                    ? Colors.grey.shade400
                                    : kPrimaryColor,
                            size: 22,
                          ),
                        ),
                      );
                    }),
                  ],
                ),

                // OTP Verified state or Send/Verify OTP (only for delivered)
                if (controller.isStatusDelivered)
                  Obx(() {
                    if (controller.isOtpVerified.value) {
                      return _buildOtpVerifiedRow();
                    }
                    return Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        SizedBox(height: 12.h),
                        // Send OTP button
                        Obx(() {
                          if (controller.isOtpSending.value) {
                            return const Center(
                              child: SizedBox(
                                width: 32,
                                height: 32,
                                child: CircularProgressIndicator(
                                  strokeWidth: 2,
                                  color: kPrimaryColor,
                                ),
                              ),
                            );
                          }
                          final otpSent =
                              controller.otpTimerText.value.isNotEmpty;
                          return AppButtonWithIcon(
                            title: 'Send OTP',
                            onTap:
                                otpSent ? () {} : () => controller.sendOtp(context),
                            mWidth: double.infinity,
                            mHeight: 48,
                            borderRadius: 8,
                            buttonColor: otpSent ? Colors.grey.shade400 : null,
                          );
                        }),

                        // Timer
                        Obx(() {
                          if (controller.otpTimerText.value.isNotEmpty) {
                            return Padding(
                              padding: EdgeInsets.only(top: 6.h),
                              child: Text(
                                controller.otpTimerText.value,
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: _red,
                                  fontFamily: FontConstants.interFonts,
                                  fontSize: responsiveFont(12),
                                  fontWeight: FontWeight.w500,
                                ),
                              ),
                            );
                          }
                          return const SizedBox.shrink();
                        }),

                        // OTP input + verify (only while timer is running)
                        Obx(() {
                          if (controller.otpTimerText.value.isNotEmpty) {
                            return _buildOtpInputRow();
                          }
                          return const SizedBox.shrink();
                        }),
                      ],
                    );
                  }),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildOtpVerifiedRow() {
    return Container(
      margin: EdgeInsets.only(top: 12.h),
      padding: EdgeInsets.symmetric(vertical: 12.h),
      decoration: BoxDecoration(
        color: _green.withValues(alpha: 0.08),
        border: Border.all(color: _green.withValues(alpha: 0.5)),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Icon(Icons.check_circle_rounded, color: _green, size: 22),
          SizedBox(width: 8.w),
          Text(
            'OTP Verified',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: responsiveFont(14),
              fontWeight: FontWeight.bold,
              color: _green,
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildOtpInputRow() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: [
        SizedBox(height: 12.h),
        AppTextField(
          controller: controller.otpController,
          readOnly: false,
          textInputType: TextInputType.number,
          maxLength: 6,
          hint: 'Enter OTP',
          label: CommonText(
            text: 'Enter OTP',
            fontSize: 12.sp,
            fontWeight: FontWeight.normal,
            textColor: kBlackColor,
            textAlign: TextAlign.start,
          ),
          hintStyle: TextStyle(
            fontSize: 12.sp,
            fontWeight: FontWeight.w400,
            fontFamily: FontConstants.interFonts,
          ),
          fieldRadius: 10,
        ),
        SizedBox(height: 12.h),
        Obx(
          () =>
              controller.isOtpVerifying.value
                  ? const Center(
                    child: SizedBox(
                      width: 32,
                      height: 32,
                      child: CircularProgressIndicator(
                        strokeWidth: 2,
                        color: _green,
                      ),
                    ),
                  )
                  : AppButtonWithIcon(
                    title: 'Verify OTP',
                    onTap: () => controller.verifyOtp(context),
                    mWidth: double.infinity,
                    mHeight: 48,
                    borderRadius: 8,
                    buttonColor: _green,
                    icon: const SizedBox.shrink(),
                  ),
        ),
      ],
    );
  }

  // ── Beneficiary Photo section ──────────────────────────────────────────────
  // Native: gradient header + skip face detection switch + 80x88 camera image + label
  // Submit button (for status 2/3) is inside this section

  Widget _buildBeneficiaryPhotoSection() {
    return Obx(() {
      final file = controller.beneficiaryPhotoFile.value;
      final b = widget.beneficiary;

      // For notAvailable re-attempt: show previously submitted photo as fallback
      final prevLocalFile = file == null ? _getLocalFile(b.beneficiaryPhotoPath) : null;
      final prevUrl = (file == null && prevLocalFile == null)
          ? _buildImageUrl(rawPath: b.beneficiaryPhotoPath, folder: 'BeneficiaryPhoto')
          : null;

      return Card(
        margin: EdgeInsets.zero,
        elevation: 2,
        clipBehavior: Clip.antiAlias,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Column(
          children: [
            _gradientHeader(
              title: 'Beneficiary / Receiver Photo',
              icon: Icons.camera_alt_outlined,
            ),

            // Skip Face Detection switch
            if (controller.showFaceDetectionSection.value)
              Obx(
                () => Container(
                  color: Colors.white,
                  padding: EdgeInsets.symmetric(
                    horizontal: 12.w,
                    vertical: 4.h,
                  ),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        'Skip Face Detection',
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: responsiveFont(14),
                          color: Colors.black87,
                        ),
                      ),
                      Switch(
                        value: controller.skipFaceDetection.value,
                        inactiveThumbColor: kTextColor,
                        inactiveTrackColor: kTextColor.withValues(alpha: 0.4),
                        activeThumbColor: kPrimaryColor,
                        activeTrackColor: kPrimaryColor.withValues(alpha: 0.4),
                        onChanged:
                            (val) => controller.skipFaceDetection.value = val,
                      ),
                    ],
                  ),
                ),
              ),

            _buildPhotoCaptureArea(
              file: file,
              label: 'Beneficiary / Receiver Photo',
              onTap: () => controller.captureBeneficiaryPhoto(context),
              fallbackLocalFile: prevLocalFile,
              fallbackUrl: prevUrl,
            ),
          ],
        ),
      );
    });
  }

  // ── Delivery Challan section ──────────────────────────────────────────────
  // Native: gradient header "Delivery Challan" + 80x88 camera image + label

  Widget _buildDeliveryChallanSection() {
    return Obx(() {
      final file = controller.deliveryChallanFile.value;

      return Card(
        margin: EdgeInsets.zero,
        elevation: 2,
        clipBehavior: Clip.antiAlias,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Column(
          children: [
            _gradientHeader(
              title: 'Delivery Challan',
              icon: Icons.receipt_long_outlined,
            ),
            _buildPhotoCaptureArea(
              file: file,
              label: 'Capture Delivery Challan',
              onTap: () => controller.captureDeliveryChallan(context),
            ),
          ],
        ),
      );
    });
  }

  // Future<void> _scanDeliveryChallan() async {
  //   String? res = await SimpleBarcodeScanner.scanBarcode(
  //     context,
  //     barcodeAppBar: const BarcodeAppBar(
  //       appBarTitle: 'Scan Bar Code',
  //       centerTitle: false,
  //       enableBackButton: false,
  //       backButtonIcon: Icon(Icons.arrow_back_ios),
  //     ),
  //     isShowFlashIcon: false,
  //     delayMillis: 2000,
  //     cameraFace: CameraFace.back,
  //   );
  //   if (res == null || res == '-1') return;
  //   controller.deliveryChallanNoController.text = res;
  // }

  // ── Consent Form section ──────────────────────────────────────────────────
  // Native: gradient header "Consent Form" + camera image + label
  //         + status messages + btnSubmitDeliveryAck

  Widget _buildConsentFormSection() {
    return Obx(() {
      final file = controller.consentFormFile.value;

      return Card(
        margin: EdgeInsets.zero,
        elevation: 2,
        clipBehavior: Clip.antiAlias,
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
        child: Column(
          children: [
            _gradientHeader(
              title: 'Consent Form',
              icon: Icons.description_outlined,
            ),
            _buildPhotoCaptureArea(
              file: file,
              label: 'Capture Consent Form',
              onTap: () => controller.captureConsentForm(context),
            ),
          ],
        ),
      );
    });
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // VIEW-ONLY CONTENT (already delivered / not available / denied)
  // ═══════════════════════════════════════════════════════════════════════════

  Widget _buildViewOnlyContent() {
    final b = widget.beneficiary;

    final isDelivered = controller.mode == AckMode.delivered;
    final isNotAvailable = controller.mode == AckMode.notAvailable;
    final isDenied = controller.mode == AckMode.denied;
    final beneficiaryLocalFile = _getLocalFile(b.beneficiaryPhotoPath);
    final beneficiaryUrl = beneficiaryLocalFile != null
        ? null
        : _buildImageUrl(rawPath: b.beneficiaryPhotoPath, folder: 'BeneficiaryPhoto');

    final consentLocalFile = _getLocalFile(b.consentFormPhotoPath);
    final consentUrl = consentLocalFile != null
        ? null
        : _buildImageUrl(rawPath: b.consentFormPhotoPath, folder: 'ConsentForm');

    final challanLocalFile = _getLocalFile(b.deliveryChallanPhotoPath);
    final challanUrl = challanLocalFile != null
        ? null
        : _buildImageUrl(rawPath: b.deliveryChallanPhotoPath, folder: 'DeliveryChallan');

    return Column(
      children: [
        // ── Delivery Status (disabled, view-only) ────────────────────────────
        _sectionCard(
          child: AppTextField(
            controller: TextEditingController(text: b.deliveryStatusName ?? ''),
            readOnly: true,
            hint: 'Deliver Medicine To Beneficiary',
            label: CommonText(
              text: 'Delivery Status',
              fontSize: 12.sp,
              fontWeight: FontWeight.normal,
              textColor: kBlackColor,
              textAlign: TextAlign.start,
            ),
            hintStyle: TextStyle(
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              fontFamily: FontConstants.interFonts,
            ),
            fieldRadius: 10,
            suffixIcon: const Icon(Icons.keyboard_arrow_down),
          ),
        ),

        // Remark for not-available / denied
        if (isNotAvailable || isDenied) ...[
          SizedBox(height: 12.h),
          _sectionCard(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _nativeField(hint: 'Remark', value: b.deliveryRemark),
                if (b.otherRemark != null && b.otherRemark!.isNotEmpty) ...[
                  SizedBox(height: 8.h),
                  _nativeField(hint: 'Other Description', value: b.otherRemark),
                ],
              ],
            ),
          ),
        ],

        // OTP Verification (view-only for denied)
        if (isDenied) ...[SizedBox(height: 12.h), _buildOtpSection()],

        // OTP Verification (view-only for delivered)
        if (isDelivered) ...[
          SizedBox(height: 12.h),
          Card(
            margin: EdgeInsets.zero,
            elevation: 2,
            clipBehavior: Clip.antiAlias,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
            child: Column(
              children: [
                _gradientHeader(
                  title: 'OTP Verification',
                  icon: Icons.lock_outline_rounded,
                ),
                Padding(
                  padding: EdgeInsets.all(12.w),
                  child: Column(
                    children: [
                      Row(
                        crossAxisAlignment: CrossAxisAlignment.end,
                        children: [
                          Expanded(
                            child: AppTextField(
                              controller: TextEditingController(
                                text: b.mobileNo ?? '',
                              ),
                              readOnly: true,
                              hint: 'Select Number',
                              label: CommonText(
                                text: 'Select Number',
                                fontSize: 12.sp,
                                fontWeight: FontWeight.normal,
                                textColor: kBlackColor,
                                textAlign: TextAlign.start,
                              ),
                              hintStyle: TextStyle(
                                fontSize: 12.sp,
                                fontWeight: FontWeight.w400,
                                fontFamily: FontConstants.interFonts,
                              ),
                              fieldRadius: 10,
                              suffixIcon: const Icon(Icons.keyboard_arrow_down),
                            ),
                          ),
                          SizedBox(width: 8.w),
                          Builder(
                            builder: (ctx) {
                              final number = b.mobileNo ?? '';
                              return GestureDetector(
                                onTap: () async {
                                  if (number.isEmpty) return;
                                  final uri = Uri(scheme: 'tel', path: number);
                                  if (await canLaunchUrl(uri)) {
                                    await launchUrl(uri);
                                  }
                                },
                                child: Container(
                                  width: 48.w,
                                  height: 48.h,
                                  decoration: BoxDecoration(
                                    color:
                                        number.isEmpty
                                            ? Colors.grey.shade100
                                            : kPrimaryColor.withValues(
                                              alpha: 0.1,
                                            ),
                                    borderRadius: BorderRadius.circular(10),
                                    border: Border.all(
                                      color:
                                          number.isEmpty
                                              ? Colors.grey.shade300
                                              : kPrimaryColor,
                                      width: 1.5,
                                    ),
                                  ),
                                  child: Icon(
                                    Icons.call_outlined,
                                    color:
                                        number.isEmpty
                                            ? Colors.grey.shade400
                                            : kPrimaryColor,
                                    size: 22,
                                  ),
                                ),
                              );
                            },
                          ),
                        ],
                      ),
                      _buildOtpVerifiedRow(),
                    ],
                  ),
                ),
              ],
            ),
          ),
        ],

        // Beneficiary Photo
        SizedBox(height: 12.h),
        _buildViewPhotoCard(
          title: isDelivered ? 'View Beneficiary Photo' : 'Beneficiary / Receiver Photo',
          url: beneficiaryUrl,
          localFile: beneficiaryLocalFile,
          label: isDelivered ? 'View Beneficiary Photo' : 'Beneficiary Photo',
          labelColor: isDelivered ? _blue : Colors.black87,
          icon: Icons.camera_alt_outlined,
        ),

        // Delivery Challan (view-only for delivered)
        if (isDelivered) ...[
          SizedBox(height: 12.h),
          _buildViewPhotoCard(
            title: 'View Delivery Challan',
            url: challanUrl,
            localFile: challanLocalFile,
            label: 'View Delivery Challan',
            labelColor: _blue,
            icon: Icons.receipt_long_outlined,
          ),
        ],

        // Consent Form (view-only for delivered)
        if (isDelivered) ...[
          SizedBox(height: 12.h),
          _buildViewPhotoCard(
            title: 'View Consent Form',
            url: consentUrl,
            localFile: consentLocalFile,
            label: 'View Consent Form',
            labelColor: _blue,
            icon: Icons.description_outlined,
          ),
        ],

        // Status messages
        if (isDelivered) _statusMessage('Medicines Already Delivered', _green),
        if (isNotAvailable)
          _statusMessage('Need To Re-Attempt Medicines Delivery.', _blue),
        if (isDenied) _statusMessage('Medicine Denied By Beneficiary.', _red),
      ],
    );
  }

  // ── View photo card (view-only mode) ─────────────────────────────────────
  Widget _buildViewPhotoCard({
    required String title,
    required String? url,
    required String label,
    required Color labelColor,
    IconData? icon,
    File? localFile,
  }) {
    final hasNetwork = url != null && url.isNotEmpty;
    final hasLocal = localFile != null;
    final hasPhoto = hasNetwork || hasLocal;
    return Card(
      margin: EdgeInsets.zero,
      elevation: 2,
      clipBehavior: Clip.antiAlias,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Column(
        children: [
          _gradientHeader(title: title, icon: icon),
          GestureDetector(
            onTap: hasPhoto
                ? () => _openFullScreen(
                    context,
                    file: hasLocal ? localFile : null,
                    url: hasNetwork ? url : null,
                    title: label,
                  )
                : null,
            child: Container(
              width: double.infinity,
              margin: EdgeInsets.all(12.w),
              padding: EdgeInsets.symmetric(vertical: 10.h, horizontal: 12.w),
              decoration: BoxDecoration(
                color: hasPhoto
                    ? labelColor.withValues(alpha: 0.04)
                    : Colors.grey.shade50,
                border: Border.all(
                  color: hasPhoto
                      ? labelColor.withValues(alpha: 0.3)
                      : Colors.grey.shade300,
                  width: 1.5,
                ),
                borderRadius: BorderRadius.circular(8),
              ),
              child: Row(
                children: [
                  // Thumbnail or camera icon
                  ClipRRect(
                    borderRadius: BorderRadius.circular(6),
                    child: hasLocal
                        ? Image.file(
                            localFile,
                            width: 52,
                            height: 52,
                            fit: BoxFit.cover,
                          )
                        : hasNetwork
                            ? _SslSafeImage(
                                url: url!,
                                width: 52,
                                height: 52,
                                fit: BoxFit.cover,
                              )
                            : Image.asset(icCameraIcon, width: 36, height: 36),
                  ),
                  SizedBox(width: 12.w),
                  // Label
                  Expanded(
                    child: Text(
                      label,
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: responsiveFont(13),
                        color: labelColor,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ),
                  // View icon
                  if (hasPhoto)
                    Container(
                      padding: const EdgeInsets.all(7),
                      decoration: BoxDecoration(
                        color: labelColor.withValues(alpha: 0.1),
                        borderRadius: BorderRadius.circular(7),
                      ),
                      child: Icon(
                        Icons.open_in_new_rounded,
                        color: labelColor,
                        size: 18,
                      ),
                    ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  String? _normalizePath(String? raw) {
    if (raw == null) return null;
    final trimmed = raw.trim();
    if (trimmed.isEmpty) return null;
    final lower = trimmed.toLowerCase();
    if (lower == 'na' || lower == 'null') return null;
    return trimmed;
  }

  String? _buildImageUrl({required String? rawPath, required String folder}) {
    final path = _normalizePath(rawPath);
    if (path == null) return null;

    final trimmed = path.trim();
    if (trimmed.startsWith('http://') || trimmed.startsWith('https://')) {
      return trimmed;
    }

    final base = controller.imageBaseUrl.trim();
    final baseNoSlash =
        base.endsWith('/') ? base.substring(0, base.length - 1) : base;

    // If the stored value is a full local device path (e.g. /storage/emulated/0/...),
    // extract just the filename and build the server URL — matches native behaviour:
    // domain + "/MedicineDelivery/BeneficiaryPhoto/" + filename
    if (trimmed.startsWith('/storage/') || trimmed.startsWith('/data/')) {
      final filename = trimmed.split('/').last;
      if (filename.isEmpty) return null;
      return '$baseNoSlash/MedicineDelivery/$folder/${Uri.encodeComponent(filename)}';
    }

    final cleaned = trimmed
        .replaceAll('\\', '/')
        .replaceAll(RegExp(r'^/+'), '');
    final lower = cleaned.toLowerCase();

    // If path already includes MedicineDelivery/... keep it and just prefix base URL.
    final mdIndex = lower.indexOf('medicinedelivery/');
    if (mdIndex >= 0) {
      final sub = cleaned.substring(mdIndex);
      return Uri.encodeFull('$baseNoSlash/$sub');
    }

    // If path already starts with the folder (e.g. BeneficiaryPhoto/xyz.jpg)
    final folderLower = folder.toLowerCase();
    if (lower.startsWith('$folderLower/')) {
      return Uri.encodeFull('$baseNoSlash/MedicineDelivery/$cleaned');
    }

    // Default: assume filename only — same as native:
    // domain + "/MedicineDelivery/<folder>/" + filename
    return '$baseNoSlash/MedicineDelivery/$folder/${Uri.encodeComponent(cleaned)}';
  }

  /// Returns a [File] if [rawPath] is an absolute local device path that exists,
  /// otherwise null (falls through to network URL logic).
  File? _getLocalFile(String? rawPath) {
    if (rawPath == null || rawPath.trim().isEmpty) return null;
    final trimmed = rawPath.trim();
    // Local paths on Android start with /storage/ or /data/
    if (trimmed.startsWith('/storage/') || trimmed.startsWith('/data/')) {
      final f = File(trimmed);
      return f.existsSync() ? f : null;
    }
    return null;
  }

  Widget _statusMessage(String text, Color color) {
    return Container(
      width: double.infinity,
      margin: EdgeInsets.only(top: 12.h),
      padding: EdgeInsets.symmetric(vertical: 14.h, horizontal: 16.w),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.08),
        border: Border.all(color: color.withValues(alpha: 0.4)),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(
            color == _green
                ? Icons.check_circle_outline_rounded
                : color == _red
                ? Icons.cancel_outlined
                : Icons.info_outline_rounded,
            color: color,
            size: 20,
          ),
          SizedBox(width: 8.w),
          Flexible(
            child: Text(
              text,
              textAlign: TextAlign.center,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: responsiveFont(14),
                fontWeight: FontWeight.w600,
                color: color,
              ),
            ),
          ),
        ],
      ),
    );
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // PICKERS (bottom sheets)
  // ═══════════════════════════════════════════════════════════════════════════

  void _showStatusPicker() {
    final items = controller.statusList;
    if (items.isEmpty) return;
    final initialIndex =
        controller.selectedStatus.value == null
            ? -1
            : items.indexWhere(
              (e) => e.id == controller.selectedStatus.value!.id,
            );
    _showAckDropdownBottomSheet(
      title: 'Select Delivery Status',
      labels: items.map((e) => e.name).toList(),
      initialIndex: initialIndex,
      onSelect: (index) => controller.selectStatus(items[index]),
    );
  }

  void _showRemarkPicker() {
    final items = controller.remarkList;
    if (items.isEmpty) return;
    final initialIndex =
        controller.selectedRemark.value == null
            ? -1
            : items.indexWhere(
              (e) => e.id == controller.selectedRemark.value!.id,
            );
    _showAckDropdownBottomSheet(
      title: 'Select Remark',
      labels: items.map((e) => e.name).toList(),
      initialIndex: initialIndex,
      onSelect: (index) => controller.selectRemark(items[index]),
    );
  }

  void _fetchAndShowOtpNumberPicker() {
    if (controller.mobileNumbersFromApi.isNotEmpty) {
      _showOtpNumberPicker();
      return;
    }
    controller.fetchMobileNumbers();
    ever(controller.mobileNumbersFromApi, (list) {
      if ((list as List).isNotEmpty) _showOtpNumberPicker();
    });
  }

  void _showOtpNumberPicker() {
    final numbers = controller.mobileNumbersFromApi;
    if (numbers.isEmpty) return;
    final initialIndex = numbers.indexOf(controller.otpTargetNumber.value);
    _showAckDropdownBottomSheet(
      title: 'Select Number',
      labels: numbers.toList(),
      initialIndex: initialIndex,
      onSelect: (index) => controller.selectOtpTargetNumber(numbers[index]),
    );
  }

  void _showAckDropdownBottomSheet({
    required String title,
    required List<String> labels,
    required int initialIndex,
    required Function(int) onSelect,
  }) {
    if (labels.isEmpty) return;
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext ctx) {
        int selectedIndex = initialIndex;
        return StatefulBuilder(
          builder: (context, setSheetState) {
            return Container(
              width: double.infinity,
              height: MediaQuery.of(context).size.width * 1.33,
              decoration: const BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(20),
                  topRight: Radius.circular(20),
                ),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Title bar
                  Container(
                    width: double.infinity,
                    padding: EdgeInsets.fromLTRB(0, 12.h, 0, 12.h),
                    decoration: const BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(20),
                        topRight: Radius.circular(20),
                      ),
                    ),
                    child: Center(
                      child: Text(
                        title,
                        style: TextStyle(
                          fontWeight: FontWeight.normal,
                          color: kBlackColor,
                          fontFamily: FontConstants.interFonts,
                          fontSize: 18.sp,
                        ),
                      ),
                    ),
                  ),

                  // Radio list
                  Expanded(
                    child: ListView.builder(
                      itemCount: labels.length,
                      itemBuilder: (ctx, index) {
                        return Padding(
                          padding: EdgeInsets.fromLTRB(8.w, 0, 8.w, 4.h),
                          child: GestureDetector(
                            onTap:
                                () =>
                                    setSheetState(() => selectedIndex = index),
                            child: Container(
                              width: double.infinity,
                              color: Colors.transparent,
                              padding: EdgeInsets.fromLTRB(8.w, 4.h, 8.w, 4.h),
                              child: Row(
                                children: [
                                  SizedBox(
                                    width: 30.w,
                                    height: 30.h,
                                    child: Image.asset(
                                      selectedIndex == index
                                          ? icRadioSelected
                                          : icUnRadioSelected,
                                    ),
                                  ),
                                  SizedBox(width: 10.w),
                                  Expanded(
                                    child: Text(
                                      labels[index],
                                      style: TextStyle(
                                        fontWeight: FontWeight.normal,
                                        color: kBlackColor,
                                        fontFamily: FontConstants.interFonts,
                                        fontSize: 16.sp,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        );
                      },
                    ),
                  ),

                  // Back / Select buttons
                  Padding(
                    padding: EdgeInsets.fromLTRB(
                      16.w,
                      4.h,
                      16.w,
                      MediaQuery.of(context).viewPadding.bottom + 16.h,
                    ),
                    child: Row(
                      children: [
                        Expanded(
                          child: AppActiveButton(
                            buttontitle: 'Back',
                            isCancel: true,
                            onTap: () => Navigator.pop(context),
                          ),
                        ),
                        SizedBox(width: 16.w),
                        Expanded(
                          child: AppActiveButton(
                            buttontitle: 'Select',
                            onTap: () {
                              if (selectedIndex != -1) {
                                Navigator.pop(context);
                                onSelect(selectedIndex);
                              }
                            },
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            );
          },
        );
      },
    );
  }

  // ── Full-screen viewer ────────────────────────────────────────────────────

  void _openFullScreen(
    BuildContext context, {
    File? file,
    String? url,
    required String title,
  }) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder:
            (_) => _FullScreenImageViewer(title: title, file: file, url: url),
      ),
    );
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // SHARED UI PRIMITIVES (matching native style)
  // ═══════════════════════════════════════════════════════════════════════════

  // ── Section card (plain, no header) ──────────────────────────────────────
  Widget _sectionCard({required Widget child}) {
    return Card(
      margin: EdgeInsets.zero,
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(padding: EdgeInsets.all(12.w), child: child),
    );
  }

  // ── Photo capture area (compact horizontal layout) ────────────────────────
  // [fallbackLocalFile] / [fallbackUrl] show a previously submitted photo
  // when no new capture has been made yet (notAvailable re-attempt).
  Widget _buildPhotoCaptureArea({
    required File? file,
    required String label,
    required VoidCallback onTap,
    File? fallbackLocalFile,
    String? fallbackUrl,
  }) {
    final captured = file != null;
    final hasFallbackLocal = !captured && fallbackLocalFile != null;
    final hasFallbackUrl = !captured && !hasFallbackLocal && fallbackUrl != null && fallbackUrl.isNotEmpty;
    final hasAnyPhoto = captured || hasFallbackLocal || hasFallbackUrl;

    return GestureDetector(
      onTap: onTap,
      child: Container(
        width: double.infinity,
        margin: EdgeInsets.all(12.w),
        padding: EdgeInsets.symmetric(vertical: 10.h, horizontal: 12.w),
        decoration: BoxDecoration(
          color: hasAnyPhoto ? kPrimaryColor.withValues(alpha: 0.03) : Colors.grey.shade50,
          border: Border.all(
            color: hasAnyPhoto ? kPrimaryColor.withValues(alpha: 0.35) : Colors.grey.shade300,
            width: 1.5,
          ),
          borderRadius: BorderRadius.circular(8),
        ),
        child: Row(
          children: [
            // Thumbnail or camera icon
            ClipRRect(
              borderRadius: BorderRadius.circular(6),
              child: captured
                  ? Image.file(file, width: 52, height: 52, fit: BoxFit.cover)
                  : hasFallbackLocal
                      ? Image.file(fallbackLocalFile!, width: 52, height: 52, fit: BoxFit.cover)
                      : hasFallbackUrl
                          ? _SslSafeImage(url: fallbackUrl!, width: 52, height: 52, fit: BoxFit.cover)
                          : Image.asset(icCameraIcon, width: 36, height: 36),
            ),
            SizedBox(width: 12.w),
            // Label
            Expanded(
              child: Text(
                label,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: responsiveFont(13),
                  color: hasAnyPhoto ? kPrimaryColor : Colors.grey.shade600,
                  fontWeight: hasAnyPhoto ? FontWeight.w600 : FontWeight.normal,
                ),
              ),
            ),
            // Action button
            Container(
              padding: const EdgeInsets.all(7),
              decoration: BoxDecoration(
                color: hasAnyPhoto ? kPrimaryColor : Colors.grey.shade200,
                borderRadius: BorderRadius.circular(7),
              ),
              child: Icon(
                hasAnyPhoto ? Icons.camera_alt_rounded : Icons.camera_alt_outlined,
                color: hasAnyPhoto ? Colors.white : Colors.grey.shade600,
                size: 18,
              ),
            ),
          ],
        ),
      ),
    );
  }

  /// Section header — solid icon badge, white background, subtle divider.
  Widget _gradientHeader({
    required String title,
    Widget? trailing,
    IconData? icon,
  }) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(vertical: 12.h, horizontal: 14.w),
      decoration: BoxDecoration(
        color: Colors.white,
        border: Border(
          bottom: BorderSide(color: Colors.grey.shade200, width: 1),
        ),
      ),
      child: Row(
        children: [
          if (icon != null) ...[
            Container(
              width: 36.w,
              height: 36.h,
              decoration: BoxDecoration(
                color: kPrimaryColor,
                borderRadius: BorderRadius.circular(8),
              ),
              child: Icon(icon, color: Colors.white, size: 18),
            ),
            SizedBox(width: 10.w),
          ],
          Expanded(
            child: Text(
              title,
              style: TextStyle(
                color: Colors.black87,
                fontFamily: FontConstants.interFonts,
                fontSize: responsiveFont(14),
                fontWeight: FontWeight.w700,
                letterSpacing: 0.2,
              ),
            ),
          ),
          if (trailing != null) trailing,
        ],
      ),
    );
  }

  /// Read-only text field — matches native MaterialEditText style (hint + value)
  Widget _nativeField({required String hint, String? value}) {
    final display = (value == null || value.isEmpty) ? '' : value;
    return Padding(
      padding: const EdgeInsets.only(bottom: 4),
      child: AppTextField(
        controller: TextEditingController(text: display),
        readOnly: true,
        hint: hint,
        label: CommonText(
          text: hint,
          fontSize: 12.sp,
          fontWeight: FontWeight.normal,
          textColor: kBlackColor,
          textAlign: TextAlign.start,
        ),
        hintStyle: TextStyle(
          fontSize: 12.sp,
          fontWeight: FontWeight.w400,
          fontFamily: FontConstants.interFonts,
        ),
        fieldRadius: 10,
      ),
    );
  }

  /// RadioButton row — matches native RadioButton style
  Widget _nativeRadioTile({
    required String label,
    required int value,
    required int groupValue,
    required void Function(int) onChanged,
  }) {
    final selected = value == groupValue;
    return GestureDetector(
      onTap: () => onChanged(value),
      child: Row(
        children: [
          Container(
            width: 20,
            height: 20,
            margin: const EdgeInsets.symmetric(horizontal: 8),
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              border: Border.all(
                color: selected ? kPrimaryColor : Colors.grey,
                width: 2,
              ),
            ),
            child:
                selected
                    ? Center(
                      child: Container(
                        width: 10,
                        height: 10,
                        decoration: const BoxDecoration(
                          color: kPrimaryColor,
                          shape: BoxShape.circle,
                        ),
                      ),
                    )
                    : null,
          ),
          Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: responsiveFont(14),
              color: Colors.black,
            ),
          ),
        ],
      ),
    );
  }
}

// ═══════════════════════════════════════════════════════════════════════════
// Full-screen image viewer
// ═══════════════════════════════════════════════════════════════════════════

class _FullScreenImageViewer extends StatelessWidget {
  final String title;
  final File? file;
  final String? url;

  const _FullScreenImageViewer({required this.title, this.file, this.url})
    : assert(file != null || url != null);

  // static const _purple = Color(0xFF423897);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      appBar: AppBar(
        backgroundColor: Colors.black,
        foregroundColor: Colors.white,
        title: Text(
          title,
          style: const TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.w600,
            fontSize: 15,
          ),
        ),
        leading: IconButton(
          icon: const Icon(
            Icons.arrow_back_ios_new_rounded,
            color: Colors.white,
          ),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: Center(
        child: InteractiveViewer(
          minScale: 0.5,
          maxScale: 4.0,
          child:
              file != null
                  ? Image.file(
                    file!,
                    fit: BoxFit.contain,
                    errorBuilder: (ctx, e, s) => _errorWidget(),
                  )
                  : _SslSafeImage(url: url!, fit: BoxFit.contain),
        ),
      ),
    );
  }

  Widget _errorWidget() => const Column(
    mainAxisSize: MainAxisSize.min,
    children: [
      Icon(Icons.broken_image_rounded, color: Colors.white54, size: 64),
      SizedBox(height: 12),
      Text(
        'Image not available',
        style: TextStyle(color: Colors.white54, fontSize: 14),
      ),
    ],
  );
}

// ─────────────────────────────────────────────────────────────────────────────
// SSL-safe network image widget
// Uses the same IOClient (SSL-bypass) as all API calls, then renders bytes
// via Image.memory — avoids Image.network() which can fail on self-signed certs.
// ─────────────────────────────────────────────────────────────────────────────

class _SslSafeImage extends StatefulWidget {
  final String url;
  final double? width;
  final double? height;
  final BoxFit fit;

  const _SslSafeImage({
    required this.url,
    this.width,
    this.height,
    this.fit = BoxFit.cover,
  });

  @override
  State<_SslSafeImage> createState() => _SslSafeImageState();
}

class _SslSafeImageState extends State<_SslSafeImage> {
  late Future<Uint8List?> _future;

  @override
  void initState() {
    super.initState();
    _future = _fetchBytes();
  }

  Future<Uint8List?> _fetchBytes() async {
    try {
      final client = APIManager().getInstanceOfIoClient();
      final response = await client.get(Uri.parse(widget.url));
      if (response.statusCode == 200) return response.bodyBytes;
    } catch (e) {
      debugPrint('_SslSafeImage fetch error: $e  url=${widget.url}');
    }
    return null;
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder<Uint8List?>(
      future: _future,
      builder: (context, snap) {
        if (snap.connectionState != ConnectionState.done) {
          return SizedBox(
            width: widget.width,
            height: widget.height,
            child: const Center(
              child: CircularProgressIndicator(
                strokeWidth: 2,
                color: kPrimaryColor,
              ),
            ),
          );
        }
        final bytes = snap.data;
        if (bytes == null || bytes.isEmpty) {
          return Image.asset(
            icCameraIcon,
            width: widget.width,
            height: widget.height,
            fit: widget.fit,
          );
        }
        return Image.memory(
          bytes,
          width: widget.width,
          height: widget.height,
          fit: widget.fit,
          errorBuilder:
              (ctx, e, s) => Image.asset(
                icCameraIcon,
                width: widget.width,
                height: widget.height,
              ),
        );
      },
    );
  }
}
