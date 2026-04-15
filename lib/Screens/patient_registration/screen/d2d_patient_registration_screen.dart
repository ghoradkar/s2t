// ignore_for_file: file_names

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/selection_bottom_sheet.dart';
import 'package:s2toperational/Screens/calling_modules/models/relation_model.dart';
import 'package:s2toperational/Screens/patient_registration/controller/d2d_patient_registration_controller.dart';
import 'package:s2toperational/Screens/patient_registration/screen/abha_creation_screen.dart';
import 'package:s2toperational/Screens/patient_registration/screen/abha_demographic_creation_screen.dart';

/// Forces every character to uppercase as the user types.
class _UpperCaseFormatter extends TextInputFormatter {
  const _UpperCaseFormatter();

  @override
  TextEditingValue formatEditUpdate(
    TextEditingValue oldValue,
    TextEditingValue newValue,
  ) => newValue.copyWith(text: newValue.text.toUpperCase());
}

const _kUpper = [_UpperCaseFormatter()];

// Hardcoded marital status list — IDs match the GetMaritalMaster API response
// (native fetches dynamically; Flutter hardcodes these verified values)
// API returns: 1=Married, 2=Unmarried, 3=Divorced, 4=Widowed
const _kMaritalStatus = [
  ('1', 'Married'),
  ('2', 'Unmarried'),
  ('3', 'Divorced'),
  ('4', 'Widowed'),
];

class D2DPatientRegistrationScreen extends StatefulWidget {
  const D2DPatientRegistrationScreen({super.key});

  @override
  State<D2DPatientRegistrationScreen> createState() =>
      _D2DPatientRegistrationScreenState();
}

class _D2DPatientRegistrationScreenState
    extends State<D2DPatientRegistrationScreen> {
  late final D2DPatientRegistrationController c;

  @override
  void initState() {
    super.initState();
    c = Get.find<D2DPatientRegistrationController>();
  }

  // ── Derived helpers (read inside Obx) ────────────────────────────────────

  bool get _isNo => !c.isDependent.value;

  bool get _isYes => c.isDependent.value;

  bool get _hasData => c.hasApiData.value;

  /// True after a successful ABHA-creation fill — locks most form fields.
  bool get _isLocked => c.abhaFormLocked.value;

  // Show local/current/landmark/district/taluka section when: isDependent OR hasApiData
  bool get _showExtAddr => _isYes || _hasData;

  // Renewal date + post office: only when isDependent=No AND hasApiData (Scenario 2)
  bool get _showRenewalRow => _isNo && _hasData;

  // ── Date picker helper ────────────────────────────────────────────────────

  Future<void> _pickDate(
    BuildContext context,
    TextEditingController tec,
    void Function(String) onPicked, {
    bool readOnly = false,
  }) async {
    if (readOnly) return;
    final now = DateTime.now();
    final picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: DateTime(1900),
      lastDate: now,
    );
    if (picked != null) {
      final formatted = FormatterManager.formatDateToString(picked);
      tec.text = formatted;
      onPicked(formatted);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Patient Registration',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 14.h),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // _campBanner(),
            // SizedBox(height: 14.h),

            // ── Registration Type ─────────────────────────────────────────
            _sectionLabel('Registration Type'),
            SizedBox(height: 8.h),
            Obx(
              () => Row(
                children: [
                  _radioChip(
                    label: 'Without ABHA',
                    selected: c.registrationType.value == 'without_abha',
                    onTap: () => c.onRegistrationTypeChanged('without_abha'),
                  ),
                  SizedBox(width: 10.w),
                  _radioChip(
                    label: 'With ABHA',
                    selected: c.registrationType.value == 'with_abha',
                    onTap: () => c.onRegistrationTypeChanged('with_abha'),
                  ),
                ],
              ),
            ),
            SizedBox(height: 14.h),

            // ── Is Dependent ──────────────────────────────────────────────
            _sectionLabel('Is Dependent'),
            SizedBox(height: 8.h),
            Obx(
              () => Row(
                children: [
                  _radioChip(
                    label: 'Yes',
                    selected: c.isDependent.value,
                    onTap: () => c.onDependentToggled(true),
                  ),
                  SizedBox(width: 10.w),
                  _radioChip(
                    label: 'No',
                    selected: !c.isDependent.value,
                    onTap: () => c.onDependentToggled(false),
                  ),
                ],
              ),
            ),
            SizedBox(height: 14.h),

            // ── The entire form reacts to isDependent × hasApiData ─────────
            Obx(() => _buildForm(context)),
          ],
        ),
      ),
    );
  }

  // ══════════════════════════════════════════════════════════════════════════
  // MAIN FORM — all 4 scenarios driven by _isNo/_isYes/_hasData
  // ══════════════════════════════════════════════════════════════════════════

  Widget _buildForm(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        // ── 1. Beneficiary Reg No ────────────────────────────────────────
        _sectionLabel('Beneficiary Info'),
        SizedBox(height: 8.h),
        Row(
          children: [
            Expanded(
              child: AppTextField(
                controller: c.tecWorkerRegNo,
                label: _label('Beneficiary Reg. No *'),
                hint: '12-digit number',
                textInputType: TextInputType.number,
                maxLength: 12,
                // Disabled after API data returned
                readOnly: _hasData,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                onChange: _hasData ? null : c.onWorkerRegNoChanged,
                suffixIcon:
                    c.isLoadingBeneficiary.value
                        ? const SizedBox(
                          width: 18,
                          height: 18,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        ).paddingOnly(right: 8.w)
                        : const Icon(Icons.search),
              ),
            ),
            SizedBox(width: 8.w),
            // Barcode scanner — hidden when reg no is locked
            if (!_hasData)
              GestureDetector(
                onTap: () async {
                  final res = await SimpleBarcodeScanner.scanBarcode(
                    context,
                    barcodeAppBar: const BarcodeAppBar(
                      appBarTitle: 'Scan Bar Code',
                      centerTitle: false,
                      enableBackButton: false,
                      backButtonIcon: Icon(Icons.arrow_back_ios),
                    ),
                    isShowFlashIcon: false,
                    delayMillis: 2000,
                    cameraFace: CameraFace.back,
                  );
                  if (res == null || res == '-1') return;
                  c.tecWorkerRegNo.text = res;
                  c.onWorkerRegNoChanged(res);
                },
                child: Container(
                  width: 44.w,
                  height: 44.w,
                  decoration: BoxDecoration(
                    color: kPrimaryColor,
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: const Icon(
                    Icons.qr_code_scanner_rounded,
                    color: Colors.white,
                    size: 22,
                  ),
                ),
              ),
          ],
        ),
        SizedBox(height: 12.h),

        // ── 1a. ABHA section (with_abha only) — matches native position ──
        if (c.registrationType.value == 'with_abha') ...[
          _sectionLabel('ABHA'),
          SizedBox(height: 8.h),

          // ── Don't have ABHA No? ────────────────────────────────────────
          Container(
            width: double.infinity,
            padding: EdgeInsets.all(12.w),
            decoration: BoxDecoration(
              color: kWhiteColor,
              borderRadius: BorderRadius.circular(10),
              border: Border.all(color: kPrimaryColor.withValues(alpha: 0.2)),
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Center(
                  child: CommonText(
                    text: "Don't have ABHA No?",
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w600,
                    textColor: kLabelTextColor,
                    textAlign: TextAlign.center,
                  ),
                ),
                SizedBox(height: 8.h),
                // Controls disabled until Beneficiary Reg No API returns data
                AbsorbPointer(
                  absorbing: !_hasData,
                  child: Opacity(
                    opacity: _hasData ? 1.0 : 0.5,
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Row(
                          children: [
                            _radioChip(
                              label: 'Using Demographic',
                              selected: c.abhaCreateMode.value == 'demographic',
                              onTap:
                                  () => c.abhaCreateMode.value = 'demographic',
                            ),
                            SizedBox(width: 8.w),
                            _radioChip(
                              label: 'Using Aadhaar OTP',
                              selected: c.abhaCreateMode.value == 'aadhaar_otp',
                              onTap:
                                  () => c.abhaCreateMode.value = 'aadhaar_otp',
                            ),
                          ],
                        ),
                        SizedBox(height: 10.h),
                        AppButtonWithIcon(
                          title: 'Create ABHA',
                          mHeight: 40,
                          mWidth: double.infinity,
                          onTap: _isLocked ? null : () {
                            if (_isYes && c.selectedRelation.value == null) {
                              ToastManager.toast('Please select relation');
                              return;
                            }
                            if (c.abhaCreateMode.value == 'demographic') {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => AbhaDemographicCreationScreen(
                                    campId: c.navCampId,
                                    siteId: c.navSiteId,
                                    distLgdCode: c.navDistLgd,
                                    district: c.navCampLocation,
                                    campType: c.navCampType,
                                    empCode: c.empCode,
                                  ),
                                ),
                              );
                            } else {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => AbhaCreationScreen(
                                    campId: c.navCampId,
                                    siteId: c.navSiteId,
                                    distLgdCode: c.navDistLgd,
                                    district: c.navCampLocation,
                                    campType: c.navCampType,
                                    empCode: c.empCode,
                                    initialMobile: '',
                                  ),
                                ),
                              );
                            }
                          },
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),
          // SizedBox(height: 12.h),
          //
          // // ── Divider ────────────────────────────────────────────────────
          // Divider(thickness: 1, color: Colors.grey.shade300),
          SizedBox(height: 8.h),

          // ── Search ABHA Details by ─────────────────────────────────────
          Container(
            width: double.infinity,
            padding: EdgeInsets.all(12.w),
            decoration: BoxDecoration(
              color: kWhiteColor,
              borderRadius: BorderRadius.circular(10),
              border: Border.all(color: kPrimaryColor.withValues(alpha: 0.2)),
            ),
            child: Column(
              children: [
                Center(
                  child: CommonText(
                    text: 'Search ABHA Details by',
                    fontSize: 13.sp,
                    fontWeight: FontWeight.w600,
                    textColor: kBlackColor,
                    textAlign: TextAlign.center,
                  ),
                ),
                SizedBox(height: 8.h),

                // Disabled until API data loads, OR after ABHA-creation fill
                AbsorbPointer(
                  absorbing: !_hasData || _isLocked,
                  child: Opacity(
                    opacity: (_hasData && !_isLocked) ? 1.0 : 0.5,
                    child: Column(
                      children: [
                        // ── Find ABHA / Verify ABHA toggle ─────────────────────────────
                        Container(
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(8),
                            border: Border.all(
                              color: kPrimaryColor.withValues(alpha: 0.3),
                            ),
                          ),
                          child: Row(
                            children: [
                              Expanded(
                                child: GestureDetector(
                                  onTap: () => c.abhaSearchMode.value = 'find',
                                  child: Container(
                                    padding: EdgeInsets.symmetric(
                                      vertical: 10.h,
                                    ),
                                    decoration: BoxDecoration(
                                      color:
                                          c.abhaSearchMode.value == 'find'
                                              ? kPrimaryColor
                                              : kWhiteColor,
                                      borderRadius: const BorderRadius.only(
                                        topLeft: Radius.circular(7),
                                        bottomLeft: Radius.circular(7),
                                      ),
                                    ),
                                    child: Center(
                                      child: CommonText(
                                        text: 'Find ABHA',
                                        fontSize: 13.sp,
                                        fontWeight: FontWeight.w500,
                                        textColor:
                                            c.abhaSearchMode.value == 'find'
                                                ? kWhiteColor
                                                : kTextColor,
                                        textAlign: TextAlign.center,
                                      ),
                                    ),
                                  ),
                                ),
                              ),
                              Expanded(
                                child: GestureDetector(
                                  onTap:
                                      () => c.abhaSearchMode.value = 'verify',
                                  child: Container(
                                    padding: EdgeInsets.symmetric(
                                      vertical: 10.h,
                                    ),
                                    decoration: BoxDecoration(
                                      color:
                                          c.abhaSearchMode.value == 'verify'
                                              ? kPrimaryColor
                                              : kWhiteColor,
                                      borderRadius: const BorderRadius.only(
                                        topRight: Radius.circular(7),
                                        bottomRight: Radius.circular(7),
                                      ),
                                    ),
                                    child: Center(
                                      child: CommonText(
                                        text: 'Verify ABHA',
                                        fontSize: 13.sp,
                                        fontWeight: FontWeight.w500,
                                        textColor:
                                            c.abhaSearchMode.value == 'verify'
                                                ? kWhiteColor
                                                : kTextColor,
                                        textAlign: TextAlign.center,
                                      ),
                                    ),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                        SizedBox(height: 10.h),

                        // ── ABHA Number + Address (Verify ABHA only) ───────────────────
                        if (c.abhaSearchMode.value == 'verify') ...[
                          AppTextField(
                            controller: c.tecAbhaNumber,
                            label: _label('ABHA Number'),
                            hint: '14-digit ABHA number',
                            textInputType: TextInputType.number,
                            maxLength: 14,
                            readOnly: c.abhaVerified.value,
                            inputFormatters: [
                              FilteringTextInputFormatter.digitsOnly,
                            ],
                            prefixIcon: const Icon(
                              Icons.health_and_safety_rounded,
                              color: kPrimaryColor,
                              size: 18,
                            ).paddingOnly(left: 6.w),
                          ),
                          SizedBox(height: 6.h),
                          Center(
                            child: CommonText(
                              text: 'OR',
                              fontSize: 12.sp,
                              fontWeight: FontWeight.w500,
                              textColor: kLabelTextColor,
                              textAlign: TextAlign.center,
                            ),
                          ),
                          SizedBox(height: 6.h),
                          AppTextField(
                            controller: c.tecAbhaAddress,
                            label: _label('ABHA Address'),
                            hint: 'yourname@abdm',
                            readOnly: c.abhaVerified.value,
                            prefixIcon: const Icon(
                              Icons.alternate_email_rounded,
                              color: kPrimaryColor,
                              size: 18,
                            ).paddingOnly(left: 6.w),
                          ),
                          SizedBox(height: 10.h),
                        ],

                        // ── Using Mobile / Using Aadhaar radio ─────────────────────────
                        if (!c.abhaVerified.value) ...[
                          Row(
                            children: [
                              _radioChip(
                                label: 'Using Mobile',
                                selected: c.abhaValidateMode.value == 'mobile',
                                onTap:
                                    () => c.abhaValidateMode.value = 'mobile',
                              ),
                              SizedBox(width: 8.w),
                              _radioChip(
                                label: 'Using Aadhaar',
                                selected: c.abhaValidateMode.value == 'aadhaar',
                                onTap:
                                    () => c.abhaValidateMode.value = 'aadhaar',
                              ),
                            ],
                          ),
                          SizedBox(height: 8.h),

                          if (c.abhaValidateMode.value == 'mobile')
                            AppTextField(
                              controller: c.tecAbhaLinkedMobile,
                              label: _label('ABHA Linked Mobile'),
                              hint: 'Enter ABHA linked mobile',
                              textInputType: TextInputType.phone,
                              maxLength: 10,
                              inputFormatters: [
                                FilteringTextInputFormatter.digitsOnly,
                              ],
                              prefixIcon: const Icon(
                                Icons.phone_android_rounded,
                                color: kPrimaryColor,
                                size: 18,
                              ).paddingOnly(left: 6.w),
                            ),

                          if (c.abhaValidateMode.value == 'aadhaar')
                            AppTextField(
                              controller: c.tecAbhaAadhaar,
                              label: _label('Aadhaar Number'),
                              hint: 'Enter Aadhaar number',
                              textInputType: TextInputType.number,
                              maxLength: 12,
                              inputFormatters: [
                                FilteringTextInputFormatter.digitsOnly,
                              ],
                              prefixIcon: const Icon(
                                Icons.credit_card_rounded,
                                color: kPrimaryColor,
                                size: 18,
                              ).paddingOnly(left: 6.w),
                            ),

                          SizedBox(height: 10.h),

                          // ── Clear + Search ABHA buttons ────────────────────────────
                          Row(
                            children: [
                              Expanded(
                                child: OutlinedButton(
                                  onPressed: c.clearAbhaSearch,
                                  style: OutlinedButton.styleFrom(
                                    side: const BorderSide(
                                      color: kPrimaryColor,
                                    ),
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(8),
                                    ),
                                    padding: EdgeInsets.symmetric(
                                      vertical: 10.h,
                                    ),
                                  ),
                                  child: CommonText(
                                    text: 'Clear',
                                    fontSize: 13.sp,
                                    fontWeight: FontWeight.w500,
                                    textColor: kPrimaryColor,
                                    textAlign: TextAlign.center,
                                  ),
                                ),
                              ),
                              SizedBox(width: 12.w),
                              Expanded(
                                child: OutlinedButton(
                                  onPressed:
                                      c.abhaOtpTimer.value > 0 &&
                                              c.abhaOtpSent.value
                                          ? null
                                          : c.sendAbhaOtp,
                                  style: OutlinedButton.styleFrom(
                                    side: const BorderSide(
                                      color: kPrimaryColor,
                                    ),
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(8),
                                    ),
                                    padding: EdgeInsets.symmetric(
                                      vertical: 10.h,
                                    ),
                                  ),
                                  child: CommonText(
                                    text:
                                        c.abhaOtpSent.value
                                            ? 'Resend OTP'
                                            : 'Search ABHA',
                                    fontSize: 13.sp,
                                    fontWeight: FontWeight.w500,
                                    textColor: kPrimaryColor,
                                    textAlign: TextAlign.center,
                                  ),
                                ),
                              ),
                            ],
                          ),

                          // ── OTP section ────────────────────────────────────────────
                          if (c.abhaOtpSent.value) ...[
                            SizedBox(height: 8.h),
                            if (c.abhaOtpTimer.value > 0)
                              Padding(
                                padding: EdgeInsets.only(bottom: 6.h),
                                child: CommonText(
                                  text:
                                      'Resend OTP in ${c.abhaOtpTimer.value}s',
                                  fontSize: 12.sp,
                                  fontWeight: FontWeight.w500,
                                  textColor: kLabelTextColor,
                                  textAlign: TextAlign.start,
                                ),
                              ),
                            AppTextField(
                              controller: c.tecAbhaOtp,
                              label: _label('Enter OTP *'),
                              hint: '6-digit OTP',
                              textInputType: TextInputType.number,
                              maxLength: 6,
                              inputFormatters: [
                                FilteringTextInputFormatter.digitsOnly,
                              ],
                            ),
                            SizedBox(height: 8.h),
                            AppButtonWithIcon(
                              title: 'Verify OTP',
                              mHeight: 40,
                              mWidth: double.infinity,
                              onTap: c.verifyAbhaOtp,
                            ),
                            SizedBox(height: 6.h),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                CommonText(
                                  text: 'OTP Attempts: ',
                                  fontSize: 12.sp,
                                  fontWeight: FontWeight.w400,
                                  textColor: kTextColor,
                                  textAlign: TextAlign.center,
                                ),
                                CommonText(
                                  text: '${c.abhaOtpAttempts.value}',
                                  fontSize: 12.sp,
                                  fontWeight: FontWeight.w600,
                                  textColor: kBlackColor,
                                  textAlign: TextAlign.center,
                                ),
                              ],
                            ),
                          ],
                        ],

                        SizedBox(height: 14.h),
                      ], // Column children
                    ), // Column
                  ), // Opacity
                ), // AbsorbPointer

                        // ── Verified banner (outside AbsorbPointer so Clear stays tappable) ──
                        if (c.abhaVerified.value) ...[
                          SizedBox(height: 10.h),
                          Row(
                            children: [
                              Expanded(child: _verifiedBanner('ABHA verified')),
                              if (_isLocked) ...[
                                SizedBox(width: 8.w),
                                OutlinedButton(
                                  onPressed: c.clearAfterAbhaFill,
                                  style: OutlinedButton.styleFrom(
                                    side: const BorderSide(color: kPrimaryColor),
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(8),
                                    ),
                                    padding: EdgeInsets.symmetric(
                                      vertical: 10.h,
                                      horizontal: 14.w,
                                    ),
                                  ),
                                  child: CommonText(
                                    text: 'Clear',
                                    fontSize: 13.sp,
                                    fontWeight: FontWeight.w500,
                                    textColor: kPrimaryColor,
                                    textAlign: TextAlign.center,
                                  ),
                                ),
                              ],
                            ],
                          ),
                        ],
              ],
            ),
          ),
        ],

        // ── 2. Worker info cards (Scenario 4: Yes+Data) ─────────────────
        if (_isYes && _hasData) ...[
          _infoCard(
            label: 'Worker Name',
            value: c.workerNameDisplay.value,
            icon: Icons.person,
          ),
          SizedBox(height: 8.h),
          Row(
            children: [
              Expanded(
                child: _infoCard(
                  label: 'Worker Age',
                  value: c.workerAgeDisplay.value,
                  icon: Icons.cake,
                ),
              ),
              SizedBox(width: 8.w),
              Expanded(
                child: _infoCard(
                  label: 'Worker Gender',
                  value: c.workerGenderDisplay.value,
                  icon: Icons.wc,
                ),
              ),
            ],
          ),
          SizedBox(height: 12.h),
        ],

        // ── 3. Worker gender dropdown (Scenarios 3 & 4: isDependent=Yes) ─
        if (_isYes) ...[
          _sectionLabel('Worker Gender'),
          SizedBox(height: 6.h),
          _dropdownField(
            hint: 'Click below to change worker\'s gender',
            value:
                c.workerGenderByPhlebo.value.isEmpty
                    ? null
                    : c.workerGenderByPhlebo.value,
            items: const ['Male', 'Female', 'Other'],
            onSelected: (v) => c.workerGenderByPhlebo.value = v,
          ),
          SizedBox(height: 12.h),

          // ── 4. Worker marital status (Scenarios 3 & 4) ────────────────
          _sectionLabel('Worker Marital Status'),
          SizedBox(height: 6.h),
          _dropdownField(
            hint: 'Select worker\'s marital status',
            value:
                c.selectedWorkerMaritalStatusName.value.isEmpty
                    ? null
                    : c.selectedWorkerMaritalStatusName.value,
            items: _kMaritalStatus.map((e) => e.$2).toList(),
            onSelected: (name) {
              final match = _kMaritalStatus.firstWhere((e) => e.$2 == name);
              c.onWorkerMaritalStatusChanged(match.$1, match.$2);
            },
          ),
          SizedBox(height: 12.h),
        ],
        SizedBox(height: 12.h),

        // ── 5. Relation with Worker ──────────────────────────────────────
        _sectionLabel('Relation with Worker'),
        SizedBox(height: 6.h),
        if (_isNo)
          // isDependent=No → always "Self", disabled
          AppTextField(
            controller: TextEditingController(text: 'Self'),
            label: _label('Relation with Worker'),
            readOnly: true,
          )
        else
          // isDependent=Yes → picker
          GestureDetector(
            onTap: () => _showRelationPicker(context),
            child: AbsorbPointer(
              child: AppTextField(
                controller: TextEditingController(
                  text: c.selectedRelation.value?.relName ?? '',
                ),
                label: _label('Relation with Worker *'),
                readOnly: true,
                suffixIcon: const Icon(Icons.arrow_drop_down),
              ),
            ),
          ),
        SizedBox(height: 12.h),

        // ── 6. Name fields ───────────────────────────────────────────────
        _sectionLabel('Name'),
        SizedBox(height: 8.h),
        // First + Middle row
        Row(
          children: [
            Expanded(
              child: AppTextField(
                controller: c.tecFirstName,
                label: _label('First Name *'),
                readOnly: (_isNo && _hasData) || _isLocked,
                textCapitalization: TextCapitalization.characters,
                inputFormatters: _kUpper,
                onChange: (_) => c.onNamePartsChanged(),
              ),
            ),
            SizedBox(width: 8.w),
            Expanded(
              child: AppTextField(
                controller: c.tecMiddleName,
                label: _label('Middle Name *'),
                readOnly: (_isNo && _hasData) || _isLocked,
                textCapitalization: TextCapitalization.characters,
                inputFormatters: _kUpper,
                onChange: (_) => c.onNamePartsChanged(),
              ),
            ),
          ],
        ),
        SizedBox(height: 8.h),
        // Last name — disabled when API returned (Scenarios 2 & 4)
        AppTextField(
          controller: c.tecLastName,
          label: _label('Last Name *'),
          readOnly: _hasData || _isLocked,
          textCapitalization: TextCapitalization.characters,
          inputFormatters: _kUpper,
          onChange: (_) => c.onNamePartsChanged(),
        ),
        SizedBox(height: 8.h),
        // Full name — always disabled (auto-composed)
        AppTextField(
          controller: c.tecFullName,
          label: _label('Full Name'),
          readOnly: true,
        ),
        SizedBox(height: 14.h),

        // ── 7. Contact Number + Verify OTP ──────────────────────────────
        _sectionLabel('Contact'),
        SizedBox(height: 8.h),
        AppTextField(
          controller: c.tecMobileNo,
          label: _label('Contact Number *'),
          maxLength: 10,
          textInputType: TextInputType.phone,
          // Disabled after OTP verified OR after ABHA-creation fill
          readOnly: c.mobileOtpVerified.value || c.altMobileOtpVerified.value || _isLocked,
          inputFormatters: [FilteringTextInputFormatter.digitsOnly],
          prefixIcon: const Icon(
            Icons.phone_rounded,
            color: kPrimaryColor,
            size: 18,
          ).paddingOnly(left: 6.w),
        ),
        SizedBox(height: 8.h),
        // Show Verify Contact Number only when checkbox NOT checked (native behaviour)
        if (c.registrationType.value == 'without_abha' &&
            !c.isNumberNotBelongsToBeneficiary.value) ...[
          if (c.mobileOtpVerified.value)
            _verifiedBanner('Contact number verified')
          else ...[
            AppButtonWithIcon(
              title: 'Verify Contact Number',
              mHeight: 40,
              mWidth: double.infinity,
              onTap: () => _showWhatsAppDialog(context, isAlternate: false),
            ),
            if (c.mobileOtpSent.value) ...[
              SizedBox(height: 8.h),
              AppTextField(
                controller: c.tecMobileOtp,
                label: _label('Enter OTP *'),
                hint: '5-digit OTP',
                textInputType: TextInputType.number,
                maxLength: 5,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              ),
              SizedBox(height: 8.h),
              AppButtonWithIcon(
                title: 'Verify OTP',
                mHeight: 40,
                mWidth: double.infinity,
                onTap: () => c.verifyMobileOtp(c.tecMobileOtp.text.trim()),
              ),
            ],
          ],
        ],
        SizedBox(height: 10.h),

        // ── 8. "This Number not belongs to beneficiary" checkbox ─────────
        // Disabled once any OTP is verified
        GestureDetector(
          onTap:
              c.mobileOtpVerified.value || c.altMobileOtpVerified.value || _isLocked
                  ? null
                  : () {
                    c.isNumberNotBelongsToBeneficiary.value =
                        !c.isNumberNotBelongsToBeneficiary.value;
                    if (!c.isNumberNotBelongsToBeneficiary.value) {
                      c.tecAltMobileNo.clear();
                      c.tecAltMobileOtp.clear();
                      c.altMobileOtpSent.value = false;
                      c.altMobileOtpVerified.value = false;
                    }
                  },
          child: Opacity(
            opacity:
                (c.mobileOtpVerified.value || c.altMobileOtpVerified.value || _isLocked)
                    ? 0.45
                    : 1.0,
            child: Row(
              children: [
                Checkbox(
                  value: c.isNumberNotBelongsToBeneficiary.value,
                  activeColor: kPrimaryColor,
                  onChanged:
                      (c.mobileOtpVerified.value ||
                              c.altMobileOtpVerified.value ||
                              _isLocked)
                          ? null
                          : (v) {
                            c.isNumberNotBelongsToBeneficiary.value =
                                v ?? false;
                            if (!(v ?? false)) {
                              c.tecAltMobileNo.clear();
                              c.tecAltMobileOtp.clear();
                              c.altMobileOtpSent.value = false;
                              c.altMobileOtpVerified.value = false;
                            }
                          },
                ),
                Text(
                  'This Number not belongs to beneficiary',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 13.sp,
                    color: kTextColor,
                  ),
                ),
              ],
            ),
          ),
        ),

        // Alternate mobile section — visible when checkbox checked
        if (c.isNumberNotBelongsToBeneficiary.value) ...[
          SizedBox(height: 8.h),
          AppTextField(
            controller: c.tecAltMobileNo,
            label: _label('Alternate Mobile Number *'),
            maxLength: 10,
            textInputType: TextInputType.phone,
            // Disabled after either contact or alternate OTP is verified
            readOnly: c.mobileOtpVerified.value || c.altMobileOtpVerified.value,
            inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            prefixIcon: const Icon(
              Icons.phone_android_rounded,
              color: kPrimaryColor,
              size: 18,
            ).paddingOnly(left: 6.w),
          ),
          SizedBox(height: 8.h),
          if (c.altMobileOtpVerified.value)
            _verifiedBanner('Alternate number verified')
          else ...[
            AppButtonWithIcon(
              title: 'Verify Alternate Number',
              mHeight: 40,
              mWidth: double.infinity,
              onTap: () => _showWhatsAppDialog(context, isAlternate: true),
            ),
            if (c.altMobileOtpSent.value) ...[
              SizedBox(height: 8.h),
              AppTextField(
                controller: c.tecAltMobileOtp,
                label: _label('Enter OTP *'),
                hint: '5-digit OTP',
                textInputType: TextInputType.number,
                maxLength: 5,
                inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              ),
              SizedBox(height: 8.h),
              AppButtonWithIcon(
                title: 'Verify OTP',
                mHeight: 40,
                mWidth: double.infinity,
                onTap: () => c.verifyAltMobileOtp(),
              ),
            ],
          ],
          SizedBox(height: 10.h),
          // "Alternate mobile belongs to" radios
          Text(
            'Alternate mobile number belongs to:',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 12.sp,
              fontWeight: FontWeight.w500,
              color: kTextColor,
            ),
          ),
          SizedBox(height: 6.h),
          Row(
            children: [
              _radioChip(
                label: 'Self',
                selected: c.altMobileBelongsTo.value == '1',
                onTap: () => c.altMobileBelongsTo.value = '1',
              ),
              SizedBox(width: 6.w),
              _radioChip(
                label: 'Spouse',
                selected: c.altMobileBelongsTo.value == '2',
                onTap: () => c.altMobileBelongsTo.value = '2',
              ),
              SizedBox(width: 6.w),
              _radioChip(
                label: 'Child',
                selected: c.altMobileBelongsTo.value == '3',
                onTap: () => c.altMobileBelongsTo.value = '3',
              ),
            ],
          ),
        ],
        SizedBox(height: 14.h),

        // ── 9b. Identity card (isDependent=Yes: Scenarios 3 & 4) ────────
        if (_isYes) ...[
          _sectionLabel('Identity Card'),
          SizedBox(height: 6.h),
          GestureDetector(
            onTap: () => _showIdentityPicker(context),
            child: AbsorbPointer(
              child: AppTextField(
                controller: TextEditingController(
                  text: c.selectedIdentityName.value,
                ),
                label: _label('Identity Card Type *'),
                readOnly: true,
                suffixIcon:
                    c.isLoadingIdentity.value
                        ? const SizedBox(
                          width: 16,
                          height: 16,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        ).paddingOnly(right: 8.w)
                        : const Icon(Icons.arrow_drop_down),
              ),
            ),
          ),
          SizedBox(height: 12.h),
        ],

        // ── 10. Aadhaar / Identity Number (without_abha only) ───────────
        // Label and input behaviour change when a non-Aadhaar identity card
        // is selected from the identity card picker above.
        // Hidden for with_abha — ABHA itself serves as identity.
        if (c.registrationType.value == 'without_abha') ...[
          Builder(
            builder: (_) {
              final isAadhaar = c.isAadhaarMode;
              final sectionTitle =
                  isAadhaar ? 'Aadhaar' : c.selectedIdentityName.value;
              final fieldLabel =
                  isAadhaar
                      ? 'Aadhaar Number *'
                      : '${c.selectedIdentityName.value} Number *';
              return Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _sectionLabel(sectionTitle),
                  SizedBox(height: 6.h),
                  AppTextField(
                    controller: c.tecAadhaarNo,
                    label: _label(fieldLabel),
                    textInputType:
                        isAadhaar ? TextInputType.number : TextInputType.text,
                    maxLength: c.identityMaxLength,
                    // isDependent=No → disabled; isDependent=Yes → enabled
                    readOnly: _isNo,
                    inputFormatters:
                        isAadhaar
                            ? [
                              FilteringTextInputFormatter.digitsOnly,
                              LengthLimitingTextInputFormatter(12),
                            ]
                            : [
                              ..._kUpper,
                              LengthLimitingTextInputFormatter(
                                c.identityMaxLength,
                              ),
                            ],
                    textCapitalization:
                        isAadhaar
                            ? TextCapitalization.none
                            : TextCapitalization.characters,
                    prefixIcon: const Icon(
                      Icons.credit_card_rounded,
                      color: kPrimaryColor,
                      size: 18,
                    ).paddingOnly(left: 6.w),
                  ),
                ],
              );
            },
          ),
          SizedBox(height: 14.h),
        ],

        // ── 11. DOB + Age ────────────────────────────────────────────────
        _sectionLabel('Date of Birth'),
        SizedBox(height: 6.h),
        Row(
          children: [
            Expanded(
              child: AppTextField(
                controller: c.tecDob,
                label: _label('DOB (YYYY/MM/DD) *'),
                hint: 'yyyy/mm/dd',
                // isDependent=No → always locked (pre-filled from API)
                // isDependent=Yes → editable via date picker (relation required first)
                // abhaFormLocked → locked after ABHA-creation fill
                readOnly: _isNo || _isLocked,
                onTap:
                    (_isNo || _isLocked)
                        ? null
                        : () {
                          if (_isYes && c.selectedRelation.value == null) {
                            ToastManager.showAlertDialog(
                              context,
                              'Please select relation first',
                              () =>
                                  Navigator.of(
                                    context,
                                    rootNavigator: true,
                                  ).pop(),
                            );
                            return;
                          }
                          _pickDate(context, c.tecDob, c.onDobChanged);
                        },
                prefixIcon: const Icon(
                  Icons.cake_rounded,
                  color: kPrimaryColor,
                  size: 18,
                ).paddingOnly(left: 6.w),
              ),
            ),
            SizedBox(width: 10.w),
            SizedBox(
              width: 80.w,
              child: AppTextField(
                controller: c.tecAge,
                label: _label('Age'),
                readOnly: true, // always auto-calculated
              ),
            ),
          ],
        ),
        SizedBox(height: 14.h),

        // ── 12. Gender ───────────────────────────────────────────────────
        _sectionLabel('Gender'),
        SizedBox(height: 6.h),
        Obx(() {
          final locked = c.isGenderLockedByRelation.value || _isLocked;
          final gender = c.selectedGender.value;
          return Row(
            children: [
              _radioChip(
                label: 'Male',
                selected: gender == 'M',
                onTap: () => c.selectedGender.value = 'M',
                enabled: !locked || gender == 'M',
              ),
              SizedBox(width: 8.w),
              _radioChip(
                label: 'Female',
                selected: gender == 'F',
                onTap: () => c.selectedGender.value = 'F',
                enabled: !locked || gender == 'F',
              ),
              SizedBox(width: 8.w),
              _radioChip(
                label: 'Other',
                selected: gender == 'O',
                onTap: () => c.selectedGender.value = 'O',
                enabled: !locked,
              ),
            ],
          );
        }),
        SizedBox(height: 14.h),

        // ── 13. Permanent Address ────────────────────────────────────────
        _sectionLabel('Address'),
        SizedBox(height: 6.h),
        AppTextField(
          controller: c.tecPermAddr,
          label: _label('Permanent Address'),
          maxLines: 2,
          minLines: 2,
          // isDependent=No → disabled; isDependent=Yes → enabled
          readOnly: _isNo,
          textCapitalization: TextCapitalization.characters,
          inputFormatters: _kUpper,
          prefixIcon: const Icon(
            Icons.home_work_rounded,
            color: kPrimaryColor,
            size: 18,
          ).paddingOnly(left: 6.w),
        ),
        SizedBox(height: 12.h),

        // ── 14. Local / Current / Landmark / District / Taluka ───────────
        //    Visible when: isDependent=Yes OR hasApiData (Scenarios 2,3,4)
        if (_showExtAddr) ...[
          AppTextField(
            controller: c.tecLocalAddr,
            label: _label('Local Address *'),
            maxLines: 2,
            minLines: 2,
            textCapitalization: TextCapitalization.characters,
            inputFormatters: _kUpper,
            prefixIcon: const Icon(
              Icons.location_city_rounded,
              color: kPrimaryColor,
              size: 18,
            ).paddingOnly(left: 6.w),
          ),
          SizedBox(height: 12.h),
          AppTextField(
            controller: c.tecCurrentAddr,
            label: _label('Current Address *'),
            maxLines: 2,
            minLines: 2,
            textCapitalization: TextCapitalization.characters,
            inputFormatters: _kUpper,
            prefixIcon: const Icon(
              Icons.my_location_rounded,
              color: kPrimaryColor,
              size: 18,
            ).paddingOnly(left: 6.w),
          ),
          SizedBox(height: 12.h),
          AppTextField(
            controller: c.tecLandmark,
            label: _label('Landmark'),
            textCapitalization: TextCapitalization.characters,
            inputFormatters: _kUpper,
            prefixIcon: const Icon(
              Icons.place_rounded,
              color: kPrimaryColor,
              size: 18,
            ).paddingOnly(left: 6.w),
          ),
          SizedBox(height: 12.h),
        ],

        // ── 15. Renewal Date (Scenario 2 only: No+Data) ─────────────────
        if (_showRenewalRow) ...[
          AppTextField(
            controller: c.tecRenewalDate,
            label: _label('Renewal Date'),
            readOnly: true, // pre-filled from API, disabled
            prefixIcon: const Icon(
              Icons.autorenew_rounded,
              color: kPrimaryColor,
              size: 18,
            ).paddingOnly(left: 6.w),
          ),
          SizedBox(height: 12.h),
        ],

        // ── 16. District + Taluka ────────────────────────────────────────
        if (_showExtAddr) ...[
          Row(
            children: [
              Expanded(
                child: AppTextField(
                  controller: c.tecDistrict,
                  label: _label('District'),
                  readOnly: true,
                  onTap:
                      (_hasData && !c.isDistrictLocked.value && !_isLocked)
                          ? () => _showDistrictPicker(context)
                          : null,
                  prefixIcon: const Icon(
                    Icons.map_rounded,
                    color: kPrimaryColor,
                    size: 18,
                  ).paddingOnly(left: 6.w),
                  suffixIcon:
                      (_hasData && !c.isDistrictLocked.value && !_isLocked)
                          ? const Icon(
                            Icons.arrow_drop_down,
                            color: kPrimaryColor,
                          )
                          : null,
                ),
              ),
              SizedBox(width: 8.w),
              Expanded(
                child: AppTextField(
                  controller: c.tecTaluka,
                  label: _label('Taluka'),
                  readOnly: true,
                  onTap:
                      (_hasData && !c.isTalukaLocked.value)
                          ? () => _showTalukaPicker(context)
                          : null,
                  prefixIcon: const Icon(
                    Icons.location_on_rounded,
                    color: kPrimaryColor,
                    size: 18,
                  ).paddingOnly(left: 6.w),
                  suffixIcon:
                      (_hasData && !c.isTalukaLocked.value)
                          ? const Icon(
                            Icons.arrow_drop_down,
                            color: kPrimaryColor,
                          )
                          : null,
                ),
              ),
            ],
          ),
          SizedBox(height: 12.h),
        ],

        // ── 17. Post Office (Scenario 2 only: No+Data, disabled) ────────
        if (_showRenewalRow) ...[
          AppTextField(
            controller: c.tecPostOffice,
            label: _label('Post Office'),
            readOnly: true, // pre-filled from API, disabled
            prefixIcon: const Icon(
              Icons.local_post_office_rounded,
              color: kPrimaryColor,
              size: 18,
            ).paddingOnly(left: 6.w),
          ),
          SizedBox(height: 12.h),
        ],

        // ── 18. Pin Code ─────────────────────────────────────────────────
        AppTextField(
          controller: c.tecPincode,
          label: _label('Pin Code *'),
          textInputType: TextInputType.number,
          maxLength: 6,
          inputFormatters: [FilteringTextInputFormatter.digitsOnly],
          prefixIcon: const Icon(
            Icons.pin_drop_rounded,
            color: kPrimaryColor,
            size: 18,
          ).paddingOnly(left: 6.w),
        ),
        SizedBox(height: 14.h),

        // ── 19. Skip Face Detection switch ──────────────────────────────
        _sectionLabel('Face Detection'),
        SizedBox(height: 6.h),
        Container(
          padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
          decoration: BoxDecoration(
            color: kWhiteColor,
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: kTextFieldBorder),
          ),
          child: Row(
            children: [
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Text(
                    //   'Face Detection',
                    //   style: TextStyle(
                    //     fontSize: 13.sp,
                    //     color: kTextColor,
                    //     fontFamily: FontConstants.interFonts,
                    //     fontWeight: FontWeight.w500,
                    //   ),
                    // ),
                    CommonText(
                      text: 'Face Detection',
                      fontSize: 14.sp,
                      fontWeight: FontWeight.w500,
                      textColor: kTextColor,
                      textAlign: TextAlign.start,
                    ),

                    // Text(
                    //   'Turn ON to enable face detection; patient photo required',
                    //   style: TextStyle(
                    //     fontSize: 11.sp,
                    //     color: kLabelTextColor,
                    //     fontFamily: FontConstants.interFonts,
                    //   ),
                    // ),
                    CommonText(
                      text:
                          'Turn ON to enable face detection; patient photo required',
                      fontSize: 12.sp,
                      fontWeight: FontWeight.normal,
                      textColor: kLabelTextColor,
                      textAlign: TextAlign.start,
                    ),
                  ],
                ),
              ),
              Switch(
                inactiveThumbColor: kTextColor,
                inactiveTrackColor: kTextColor.withValues(alpha: 0.3),
                value: c.isFaceDetection.value,
                activeThumbColor: kPrimaryColor,
                onChanged: (v) => c.isFaceDetection.value = v,
              ),
            ],
          ),
        ),
        SizedBox(height: 14.h),

        // ── 20. Photo Upload ─────────────────────────────────────────────
        _sectionLabel('Photo Upload'),
        SizedBox(height: 8.h),
        Row(
          children: [
            Expanded(
              child: _PhotoTile(
                label: 'Beneficiary Photo',
                icon: Icons.person_pin,
                localPath: c.patientPhotoPath.value,
                required: c.isFaceDetection.value,
                onTap: c.pickPatientPhoto,
              ),
            ),
            SizedBox(width: 8.w),
            Expanded(
              child: _PhotoTile(
                // isDependent=Yes → "Identity Card"; No → "Beneficiary Card"
                label: _isYes ? 'Identity Card' : 'Beneficiary Card',
                icon: Icons.credit_card,
                localPath: c.healthCardPhotoPath.value,
                onTap: c.pickHealthCardPhoto,
              ),
            ),
          ],
        ),
        // Renewal slip photo — commented out (not visible in native, re-enable when confirmed)
        // if (c.showRenewal.value) ...[
        //   SizedBox(height: 10.h),
        //   Row(children: [
        //     Expanded(child: _PhotoTile(label: 'Renewal Slip', icon: Icons.autorenew,
        //       localPath: c.renewalFormPath.value, onTap: c.pickRenewalFormPhoto)),
        //     const SizedBox(width: 8),
        //     const Expanded(child: SizedBox.shrink()),
        //   ]),
        // ],
        SizedBox(height: 20.h),

        // ── Register Patient button ──────────────────────────────────────
        // without_abha → visible after mobile OTP verified
        // with_abha    → visible after ABHA verified
        if ((c.registrationType.value == 'without_abha' &&
                (c.mobileOtpVerified.value || c.altMobileOtpVerified.value)) ||
            (c.registrationType.value == 'with_abha' &&
                c.abhaVerified.value)) ...[
          AppButtonWithIcon(
            title: 'Register Patient',
            mWidth: double.infinity,
            mHeight: 52,
            icon: const Icon(Icons.check_circle_rounded, color: Colors.white),
            onTap: () {
              ToastManager().showConfirmationDialog(
                context: Get.context!,
                message:
                    "Please confirm the beneficiary's details before submitting",
                didSelectYes: (bool p1) {
                  if (p1 == true) {
                    Navigator.pop(context);
                    c.submitRegistration(context);
                  } else if (p1 == false) {
                    Navigator.pop(context);
                  }
                },
              );
            },
            // onTap: () => c.showConfirmationDialog(context),
          ),
          SizedBox(height: 20.h),
        ],
      ],
    );
  }

  Widget _sectionLabel(String text) {
    return Row(
      children: [
        Container(
          width: 3,
          height: 16,
          decoration: BoxDecoration(
            color: kPrimaryColor,
            borderRadius: BorderRadius.circular(2),
          ),
        ),
        SizedBox(width: 6.w),
        // Text(
        //   text,
        //   style: TextStyle(
        //     fontFamily: FontConstants.interFonts,
        //     fontWeight: FontWeight.w600,
        //     fontSize: 13.sp,
        //     color: kTextColor,
        //   ),
        // ),
        CommonText(
          text: text,
          fontSize: 16.sp,
          fontWeight: FontWeight.w600,
          textColor: kTextColor,
          textAlign: TextAlign.start,
        ),
      ],
    );
  }

  Widget _label(String text) => CommonText(
    text: text,
    fontSize: 14.sp * 1.2,
    fontWeight: FontWeight.w600,
    textColor: kLabelTextColor,
    textAlign: TextAlign.start,
  );

  //     Text(
  //   text,
  //   style: TextStyle(
  //     color: kLabelTextColor,
  //     fontSize: 14.sp,
  //     fontFamily: FontConstants.interFonts,
  //   ),
  // );

  /// Read-only info card (used for worker name/age/gender when Yes+Data)
  Widget _infoCard({
    required String label,
    required String value,
    required IconData icon,
  }) {
    return Container(
      padding: EdgeInsets.all(10.w),
      decoration: BoxDecoration(
        color: kPrimaryColor.withValues(alpha: 0.05),
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: kPrimaryColor.withValues(alpha: 0.2)),
      ),
      child: Row(
        children: [
          Icon(icon, color: kPrimaryColor, size: 18),
          SizedBox(width: 8.w),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              CommonText(
                text: label,
                fontSize: 12.sp,
                fontWeight: FontWeight.w600,
                textColor: kLabelTextColor,
                textAlign: TextAlign.start,
              ),
              // Text(
              //   label,
              //   style: TextStyle(
              //     fontFamily: FontConstants.interFonts,
              //     fontSize: 11.sp,
              //     color: kLabelTextColor,
              //   ),
              // ),
              // Text(
              //   value.isEmpty ? '—' : value,
              //   style: TextStyle(
              //     fontFamily: FontConstants.interFonts,
              //     fontSize: 13.sp,
              //     fontWeight: FontWeight.w600,
              //     color: kTextColor,
              //   ),
              // ),
              CommonText(
                text: value.isEmpty ? '—' : value,
                fontSize: 14.sp,
                fontWeight: FontWeight.w600,
                textColor: kTextColor,
                textAlign: TextAlign.start,
              ),
            ],
          ),
        ],
      ),
    );
  }

  /// Native-style dropdown using a bottom-sheet picker
  Widget _dropdownField({
    required String hint,
    required String? value,
    required List<String> items,
    required void Function(String) onSelected,
  }) {
    return GestureDetector(
      onTap:
          () => _showStringPicker(
            context,
            items,
            onSelected,
            title: hint,
            selectedValue: value,
          ),
      child: AbsorbPointer(
        child: AppTextField(
          controller: TextEditingController(text: value ?? ''),
          label: _label(hint),
          readOnly: true,
          suffixIcon: const Icon(Icons.arrow_drop_down),
        ),
      ),
    );
  }

  void _showStringPicker(
    BuildContext context,
    List<String> items,
    void Function(String) onSelected, {
    String title = '',
    String? selectedValue,
  }) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder:
          (_) => SelectionBottomSheet<String, String>(
            title: title,
            items: items,
            selectedValue: selectedValue,
            valueFor: (item) => item,
            labelFor: (item) => item,
            onItemTap: (item) {
              Navigator.pop(context);
              onSelected(item);
            },
            height: 280.h,
            padding: EdgeInsets.fromLTRB(16.w, 16.h, 16.w, 0),
            showRadio: true,
          ),
    );
  }

  /// Shows WhatsApp Number Availability dialog before sending OTP.
  /// [isAlternate] = true → verifying alternate number; false → contact number.
  void _showWhatsAppDialog(BuildContext context, {required bool isAlternate}) {
    // Validate the relevant number first
    final mobileNo = c.tecMobileNo.text.trim();
    final altNo = c.tecAltMobileNo.text.trim();

    if (isAlternate) {
      if (altNo.isEmpty) {
        ToastManager.toast('Please enter alternate mobile number');
        return;
      }
      if (altNo.length != 10) {
        ToastManager.toast('Please enter valid 10 digit mobile number');
        return;
      }
    } else {
      if (mobileNo.isEmpty) {
        ToastManager.toast('Please enter mobile number');
        return;
      }
      if (mobileNo.length != 10) {
        ToastManager.toast('Please enter valid 10 digit mobile number');
        return;
      }
    }

    // Track selection: 0=none, 1=registered, 2=alternate, 3=not available
    String selectedMode = '0';

    showDialog(
      context: context,
      barrierDismissible: true,
      builder: (dialogContext) {
        return StatefulBuilder(
          builder: (ctx, setDialogState) {
            return Dialog(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(16),
              ),
              child: Padding(
                padding: EdgeInsets.all(16.w),
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Title row with close button
                    Row(
                      children: [
                        Expanded(
                          child:
                          // Text(
                          //   'WhatsApp Number Availability',
                          //   style: TextStyle(
                          //     fontFamily: FontConstants.interFonts,
                          //     fontSize: 15.sp,
                          //     fontWeight: FontWeight.w600,
                          //     color: kTextColor,
                          //   ),
                          // ),
                          CommonText(
                            text: 'WhatsApp Number Availability',
                            fontSize: 14.sp,
                            fontWeight: FontWeight.w600,
                            textColor: kTextColor,
                            textAlign: TextAlign.start,
                          ),
                        ),
                        GestureDetector(
                          onTap: () => Navigator.of(dialogContext).pop(),
                          child: const Icon(Icons.close, size: 20),
                        ),
                      ],
                    ),
                    SizedBox(height: 14.h),

                    // Option 1 — Registered mobile number
                    _dialogRadioRow(
                      label: 'Registered Mob No: ',
                      boldSuffix: mobileNo,
                      selected: selectedMode == '1',
                      onTap: () => setDialogState(() => selectedMode = '1'),
                    ),
                    SizedBox(height: 10.h),

                    // Option 2 — Alternate mobile number (disabled if empty)
                    _dialogRadioRow(
                      label: 'Alternate Mob No: ',
                      boldSuffix: altNo.isEmpty ? '—' : altNo,
                      selected: selectedMode == '2',
                      enabled: altNo.isNotEmpty,
                      onTap:
                          altNo.isEmpty
                              ? null
                              : () => setDialogState(() => selectedMode = '2'),
                    ),
                    SizedBox(height: 10.h),

                    // Option 3 — Not available on both
                    _dialogRadioRow(
                      label: 'WhatsApp Not Available On Both Number',
                      selected: selectedMode == '3',
                      onTap: () => setDialogState(() => selectedMode = '3'),
                    ),
                    SizedBox(height: 18.h),

                    // Submit button
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        style: ElevatedButton.styleFrom(
                          backgroundColor: kPrimaryColor,
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(8),
                          ),
                          padding: EdgeInsets.symmetric(vertical: 12.h),
                        ),
                        onPressed: () {
                          if (selectedMode == '0') {
                            ToastManager.toast('Please select an option');
                            return;
                          }
                          Navigator.of(dialogContext).pop();
                          // Save WhatsApp mode and send OTP
                          c.whatsAppMode.value = selectedMode;
                          if (isAlternate) {
                            c.sendAltMobileOtp();
                          } else {
                            c.sendMobileOtp();
                          }
                        },
                        child: Text(
                          'Submit',
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontSize: 14.sp,
                            fontWeight: FontWeight.w600,
                            color: Colors.white,
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            );
          },
        );
      },
    );
  }

  Widget _dialogRadioRow({
    required String label,
    String? boldSuffix,
    required bool selected,
    bool enabled = true,
    VoidCallback? onTap,
  }) {
    return GestureDetector(
      onTap: enabled ? onTap : null,
      child: Row(
        children: [
          // Custom radio circle — avoids deprecated Radio widget API
          Container(
            width: 20,
            height: 20,
            margin: EdgeInsets.only(right: 8.w),
            decoration: BoxDecoration(
              shape: BoxShape.circle,
              border: Border.all(
                color: selected ? kPrimaryColor : kTextFieldBorder,
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
                          shape: BoxShape.circle,
                          color: kPrimaryColor,
                        ),
                      ),
                    )
                    : null,
          ),
          Expanded(
            child: RichText(
              text: TextSpan(
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 14.sp,
                  color: enabled ? kTextColor : kLabelTextColor,
                ),
                children: [
                  TextSpan(text: label),
                  if (boldSuffix != null)
                    TextSpan(
                      text: boldSuffix,
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontFamily: FontConstants.interFonts,
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

  Future<void> _showIdentityPicker(BuildContext context) async {
    if (c.isLoadingIdentity.value) return;
    // Fetch list if not yet loaded
    await c.fetchIdentityList();
    if (!context.mounted) return;
    if (c.identityList.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Could not load identity card types. Please try again.',
        () => Navigator.of(context, rootNavigator: true).pop(),
      );
      return;
    }
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder:
          (_) => SelectionBottomSheet(
            title: 'Select Identity Card',
            items: c.identityList,
            selectedValue: c.selectedIdentityId.value,
            valueFor: (item) => item.docId?.toString() ?? '0',
            labelFor: (item) => item.documentName ?? '--',
            onItemTap: (item) {
              Navigator.pop(context);
              c.onIdentitySelected(
                item.docId?.toString() ?? '0',
                item.documentName ?? '',
              );
            },
            height: 300.h,
            padding: EdgeInsets.fromLTRB(16.w, 16.h, 16.w, 0),
            showRadio: true,
          ),
    );
  }

  Future<void> _showDistrictPicker(BuildContext context) async {
    await c.fetchRegDistrictList();
    if (!context.mounted) return;
    if (c.regDistrictList.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Could not load district list. Please try again.',
        () => Navigator.of(context, rootNavigator: true).pop(),
      );
      return;
    }
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder:
          (_) => SelectionBottomSheet(
            title: 'Select District',
            items: c.regDistrictList,
            selectedValue: c.tecDistrict.text,
            valueFor: (item) => item.distName ?? '',
            labelFor: (item) => item.distName ?? '--',
            onItemTap: (item) {
              Navigator.pop(context);
              c.selectRegDistrict(item);
            },
            height: 350.h,
            padding: EdgeInsets.fromLTRB(16.w, 16.h, 16.w, 0),
            showRadio: true,
          ),
    );
  }

  Future<void> _showTalukaPicker(BuildContext context) async {
    await c.fetchRegTalukaList();
    if (!context.mounted) return;
    if (c.regTalukaList.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Could not load taluka list. Please select district first.',
        () => Navigator.of(context, rootNavigator: true).pop(),
      );
      return;
    }
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder:
          (_) => SelectionBottomSheet(
            title: 'Select Taluka',
            items: c.regTalukaList,
            selectedValue: c.tecTaluka.text,
            valueFor: (item) => item.tALNAME ?? '',
            labelFor: (item) => item.tALNAME ?? '--',
            onItemTap: (item) {
              Navigator.pop(context);
              c.selectRegTaluka(item);
            },
            height: 350.h,
            padding: EdgeInsets.fromLTRB(16.w, 16.h, 16.w, 0),
            showRadio: true,
          ),
    );
  }

  void _showRelationPicker(BuildContext context) {
    // When isDependent=Yes, worker gender and marital status must be chosen first.
    // Note: workerGenderByPhlebo is the worker's gender (drives relation list),
    // not selectedGender which is the dependent's gender.
    if (c.isDependent.value &&
        (c.workerGenderByPhlebo.value.isEmpty ||
            c.selectedWorkerMaritalStatusId.value == '0')) {
      ToastManager.showAlertDialog(
        context,
        'Please select marital status and gender first',
        () => Navigator.of(context, rootNavigator: true).pop(),
      );
      return;
    }
    if (c.relationList.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Please select marital status and gender first',
        () => Navigator.of(context, rootNavigator: true).pop(),
      );
      return;
    }
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder:
          (_) => SelectionBottomSheet<RelationOutput, int?>(
            title: 'Select Relation',
            items: c.relationList,
            selectedValue: c.selectedRelation.value?.relId,
            valueFor: (item) => item.relId,
            labelFor: (item) => item.relName ?? '--',
            onItemTap: (item) {
              Navigator.pop(context);
              c.onRelationSelected(item);
            },
            height: 300.h,
            padding: EdgeInsets.fromLTRB(16.w, 16.h, 16.w, 0),
            showRadio: true,
          ),
    );
  }

  Widget _verifiedBanner(String message) {
    return Container(
      padding: EdgeInsets.all(8.w),
      decoration: BoxDecoration(
        color: Colors.green.withValues(alpha: 0.06),
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: Colors.green),
      ),
      child: Row(
        children: [
          const Icon(Icons.check_circle, color: Colors.green, size: 18),
          SizedBox(width: 6.w),
          Text(
            message,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 13.sp,
              fontWeight: FontWeight.w600,
              color: Colors.green,
            ),
          ),
        ],
      ),
    );
  }

  Widget _radioChip({
    required String label,
    required bool selected,
    VoidCallback? onTap,
    bool enabled = true,
  }) {
    final effectiveColor = (enabled && onTap != null) ? kPrimaryColor : kLabelTextColor;
    return Expanded(
      child: GestureDetector(
        onTap: enabled ? onTap : null,
        child: Opacity(
          opacity: enabled ? 1.0 : 0.4,
          child: Container(
            padding: EdgeInsets.symmetric(vertical: 9.h),
            decoration: BoxDecoration(
              color:
                  selected
                      ? effectiveColor.withValues(alpha: 0.08)
                      : kWhiteColor,
              borderRadius: BorderRadius.circular(8),
              border: Border.all(
                color: selected ? effectiveColor : kTextFieldBorder,
              ),
            ),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Icon(
                  selected
                      ? Icons.radio_button_checked
                      : Icons.radio_button_unchecked,
                  size: 16,
                  color: selected ? effectiveColor : kLabelTextColor,
                ),
                SizedBox(width: 5.w),
                Text(
                  label,
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 13.sp,
                    fontWeight: selected ? FontWeight.w600 : FontWeight.w400,
                    color: selected ? effectiveColor : kTextColor,
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

// ── Photo Tile ──────────────────────────────────────────────────────────────

class _PhotoTile extends StatelessWidget {
  final String label;
  final IconData icon;
  final String localPath;
  final String? url;
  final bool required;
  final VoidCallback onTap;

  const _PhotoTile({
    required this.label,
    required this.icon,
    required this.localPath,
    required this.onTap,
    this.url,
    this.required = false,
  });

  @override
  Widget build(BuildContext context) {
    final hasImage = localPath.isNotEmpty || (url?.isNotEmpty ?? false);
    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: 110,
        decoration: BoxDecoration(
          color: kWhiteColor,
          borderRadius: BorderRadius.circular(8),
          border: Border.all(
            color: required && !hasImage ? Colors.red : kTextFieldBorder,
          ),
        ),
        child:
            hasImage
                ? ClipRRect(
                  borderRadius: BorderRadius.circular(7),
                  child:
                      localPath.isNotEmpty
                          ? Image.file(File(localPath), fit: BoxFit.cover)
                          : Image.network(url!, fit: BoxFit.cover),
                )
                : Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(icon, color: kPrimaryColor, size: 28),
                    SizedBox(height: 6.h),
                    // Text(
                    //   label,
                    //   textAlign: TextAlign.center,
                    //   style: TextStyle(
                    //     fontFamily: FontConstants.interFonts,
                    //     fontSize: 11.sp,
                    //     color: kLabelTextColor,
                    //   ),
                    // ),
                    CommonText(
                      text: label,
                      fontSize: 12.sp,
                      fontWeight: FontWeight.normal,
                      textColor: kLabelTextColor,
                      textAlign: TextAlign.center,
                    ),
                    if (required) ...[
                      SizedBox(height: 2.h),
                      Text(
                        '(Required)',
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: 10.sp,
                          color: Colors.red,
                        ),
                      ),
                    ],
                  ],
                ),
      ),
    );
  }
}
