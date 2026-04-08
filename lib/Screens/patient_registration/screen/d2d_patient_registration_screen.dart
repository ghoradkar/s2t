
// ignore_for_file: file_names

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/patient_registration/controller/d2d_patient_registration_controller.dart';

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
            _campBanner(),
            SizedBox(height: 14.h),

            // ── Registration Type ──────────────────────────────────────────
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

            // ── Is Dependent ───────────────────────────────────────────────
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

            // ── Worker Info ────────────────────────────────────────────────
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
                    inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                    onChange: c.onWorkerRegNoChanged,
                    suffixIcon: Obx(
                      () => c.isLoadingBeneficiary.value
                          ? const SizedBox(
                              width: 18,
                              height: 18,
                              child: CircularProgressIndicator(strokeWidth: 2),
                            )
                          : const Icon(Icons.search),
                    ).paddingOnly(right: 8.w),
                  ),
                ),
                SizedBox(width: 8.w),
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
            Obx(() {
              if (!c.isDependent.value) {
                return AppTextField(
                  controller: c.tecFullName,
                  label: _label('English Name *'),
                  hint: 'Auto-filled',
                );
              }
              return Column(
                children: [
                  Row(
                    children: [
                      Expanded(
                        child: AppTextField(
                          controller: c.tecFirstName,
                          label: _label('First Name *'),
                          onChange: (_) => c.onNamePartsChanged(),
                        ),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: AppTextField(
                          controller: c.tecMiddleName,
                          label: _label('Middle Name'),
                          onChange: (_) => c.onNamePartsChanged(),
                        ),
                      ),
                    ],
                  ),
                  SizedBox(height: 8.h),
                  AppTextField(
                    controller: c.tecLastName,
                    label: _label('Last Name *'),
                    onChange: (_) => c.onNamePartsChanged(),
                  ),
                ],
              );
            }),
            SizedBox(height: 14.h),

            // ── ABHA Section ───────────────────────────────────────────────
            Obx(() {
              if (c.registrationType.value != 'with_abha') {
                return const SizedBox.shrink();
              }
              return Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _sectionLabel('ABHA Details'),
                  SizedBox(height: 8.h),
                  AppTextField(
                    controller: c.tecAbhaNumber,
                    label: _label('ABHA Number'),
                    hint: 'XX-XXXX-XXXX-XXXX',
                  ),
                  SizedBox(height: 8.h),
                  AppTextField(
                    controller: c.tecAbhaAddress,
                    label: _label('ABHA Address'),
                    hint: 'name@abdm',
                  ),
                  SizedBox(height: 12.h),
                  Obx(() {
                    if (c.abhaVerified.value) {
                      return Container(
                        padding: EdgeInsets.all(8.w),
                        decoration: BoxDecoration(
                          color: Colors.green.withValues(alpha: 0.06),
                          borderRadius: BorderRadius.circular(8),
                          border: Border.all(color: Colors.green),
                        ),
                        child: Row(
                          children: [
                            const Icon(Icons.check_circle, color: Colors.green),
                            SizedBox(width: 6.w),
                            CommonText(
                              text: 'ABHA Verified',
                              fontSize: 13.sp,
                              fontWeight: FontWeight.w600,
                              textColor: Colors.green,
                              textAlign: TextAlign.left,
                            ),
                          ],
                        ),
                      );
                    }
                    return Column(
                      children: [
                        AppButtonWithIcon(
                          title: 'Send OTP to Verify',
                          mHeight: 44,
                          mWidth: double.infinity,
                          onTap: () => c.sendAbhaOtp(),
                        ),
                        if (c.abhaOtpSent.value) ...[
                          SizedBox(height: 10.h),
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
                          SizedBox(height: 6.h),
                          Row(
                            children: [
                              Obx(
                                () => Text(
                                  'Resend in ${c.abhaOtpTimer.value}s',
                                  style: TextStyle(
                                    fontSize: 12.sp,
                                    color: kLabelTextColor,
                                  ),
                                ),
                              ),
                              const Spacer(),
                              Obx(
                                () => TextButton(
                                  onPressed: c.abhaOtpTimer.value == 0 &&
                                          c.abhaResendCount < 3
                                      ? () => c.sendAbhaOtp()
                                      : null,
                                  child: const Text('Resend'),
                                ),
                              ),
                            ],
                          ),
                          AppButtonWithIcon(
                            title: 'Verify OTP',
                            mHeight: 44,
                            mWidth: double.infinity,
                            onTap: () => c.verifyAbhaOtp(),
                          ),
                        ],
                      ],
                    );
                  }),
                  SizedBox(height: 14.h),
                ],
              );
            }),

            // ── Personal Details ───────────────────────────────────────────
            _sectionLabel('Personal Details'),
            SizedBox(height: 8.h),
            _labelText('Title *'),
            SizedBox(height: 6.h),
            Obx(
              () => Row(
                children: [
                  _radioChip(
                    label: 'Mr.',
                    selected: c.selectedTitle.value == 'Mr.',
                    onTap: () => c.selectedTitle.value = 'Mr.',
                  ),
                  SizedBox(width: 8.w),
                  _radioChip(
                    label: 'Mrs.',
                    selected: c.selectedTitle.value == 'Mrs.',
                    onTap: () => c.selectedTitle.value = 'Mrs.',
                  ),
                  SizedBox(width: 8.w),
                  _radioChip(
                    label: 'Ms.',
                    selected: c.selectedTitle.value == 'Ms.',
                    onTap: () => c.selectedTitle.value = 'Ms.',
                  ),
                ],
              ),
            ),
            SizedBox(height: 12.h),
            _labelText('Gender *'),
            SizedBox(height: 6.h),
            Obx(
              () => Row(
                children: [
                  _radioChip(
                    label: 'Male',
                    selected: c.selectedGender.value == 'M',
                    onTap: () => c.selectedGender.value = 'M',
                  ),
                  SizedBox(width: 8.w),
                  _radioChip(
                    label: 'Female',
                    selected: c.selectedGender.value == 'F',
                    onTap: () => c.selectedGender.value = 'F',
                  ),
                ],
              ),
            ),
            SizedBox(height: 12.h),
            Row(
              children: [
                Expanded(
                  child: AppTextField(
                    controller: c.tecDob,
                    label: _label('DOB (YYYY/MM/DD) *'),
                    readOnly: true,
                    hint: 'yyyy/mm/dd',
                    onTap: () => _pickDate(context, c.tecDob, c.onDobChanged),
                  ),
                ),
                SizedBox(width: 10.w),
                SizedBox(
                  width: 80.w,
                  child: AppTextField(
                    controller: c.tecAge,
                    label: _label('Age'),
                    readOnly: true,
                  ),
                ),
              ],
            ),
            SizedBox(height: 12.h),
            AppTextField(
              controller: c.tecMobileNo,
              label: _label('Contact Number *'),
              maxLength: 10,
              textInputType: TextInputType.phone,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            ),
            SizedBox(height: 12.h),
            Obx(() {
              if (c.registrationType.value != 'without_abha') {
                return const SizedBox.shrink();
              }
              if (c.mobileOtpVerified.value) {
                return Container(
                  padding: EdgeInsets.all(8.w),
                  decoration: BoxDecoration(
                    color: Colors.green.withValues(alpha: 0.06),
                    borderRadius: BorderRadius.circular(8),
                    border: Border.all(color: Colors.green),
                  ),
                  child: Row(
                    children: [
                      const Icon(Icons.check_circle, color: Colors.green),
                      SizedBox(width: 6.w),
                      CommonText(
                        text: 'Mobile Verified',
                        fontSize: 13.sp,
                        fontWeight: FontWeight.w600,
                        textColor: Colors.green,
                        textAlign: TextAlign.left,
                      ),
                    ],
                  ),
                );
              }
              return Column(
                children: [
                  AppButtonWithIcon(
                    title: 'Verify Mobile Number',
                    mHeight: 40,
                    mWidth: double.infinity,
                    onTap: () => c.sendMobileOtp(),
                  ),
                  if (c.mobileOtpSent.value) ...[
                    SizedBox(height: 10.h),
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
                      onTap: () =>
                          c.verifyMobileOtp(c.tecMobileOtp.text.trim()),
                    ),
                  ],
                ],
              );
            }),
            SizedBox(height: 12.h),
            AppTextField(
              controller: c.tecAadhaarNo,
              label: _label('Aadhaar Number *'),
              textInputType: TextInputType.number,
              maxLength: 12,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            ),
            SizedBox(height: 12.h),
            Obx(() {
              if (!c.isDependent.value) return const SizedBox.shrink();
              return Column(
                children: [
                  AppTextField(
                    controller: c.tecEducation,
                    label: _label('Education / Qualification'),
                  ),
                  SizedBox(height: 12.h),
                  AppTextField(
                    controller: TextEditingController(
                      text: c.selectedRelation.value?.relName ?? '',
                    ),
                    label: _label('Relation with Worker *'),
                    readOnly: true,
                    suffixIcon: const Icon(Icons.arrow_drop_down),
                    onTap: () => _showRelationPicker(context),
                  ),
                  SizedBox(height: 12.h),
                ],
              );
            }),
            _labelText('Mobile number belongs to:'),
            SizedBox(height: 6.h),
            Obx(
              () => Row(
                children: [
                  _radioChip(
                    label: 'Self',
                    selected: c.selfMobNoMode.value == '1',
                    onTap: () => c.selfMobNoMode.value = '1',
                  ),
                  SizedBox(width: 6.w),
                  _radioChip(
                    label: 'Spouse',
                    selected: c.selfMobNoMode.value == '2',
                    onTap: () => c.selfMobNoMode.value = '2',
                  ),
                  SizedBox(width: 6.w),
                  _radioChip(
                    label: 'Child',
                    selected: c.selfMobNoMode.value == '3',
                    onTap: () => c.selfMobNoMode.value = '3',
                  ),
                ],
              ),
            ),
            SizedBox(height: 14.h),

            // ── Address Details ────────────────────────────────────────────
            _sectionLabel('Address Details'),
            SizedBox(height: 8.h),
            AppTextField(
              controller: c.tecPermAddr,
              label: _label('Permanent Address'),
              maxLines: 2,
              minLines: 2,
            ),
            SizedBox(height: 12.h),
            AppTextField(
              controller: c.tecLocalAddr,
              label: _label('Local Address *'),
              maxLines: 2,
              minLines: 2,
            ),
            SizedBox(height: 12.h),
            Obx(() {
              if (!c.isDependent.value) return const SizedBox.shrink();
              return Column(
                children: [
                  AppTextField(
                    controller: c.tecCurrentAddr,
                    label: _label('Current Address *'),
                    maxLines: 2,
                    minLines: 2,
                  ),
                  SizedBox(height: 12.h),
                  AppTextField(
                    controller: c.tecLandmark,
                    label: _label('Landmark *'),
                  ),
                  SizedBox(height: 12.h),
                  Row(
                    children: [
                      Expanded(
                        child: AppTextField(
                          controller: c.tecTaluka,
                          label: _label('Taluka *'),
                        ),
                      ),
                      SizedBox(width: 8.w),
                      Expanded(
                        child: AppTextField(
                          controller: c.tecDistrict,
                          label: _label('District *'),
                        ),
                      ),
                    ],
                  ),
                  SizedBox(height: 12.h),
                  AppTextField(
                    controller: c.tecPostOffice,
                    label: _label('Post Office'),
                  ),
                  SizedBox(height: 12.h),
                ],
              );
            }),
            AppTextField(
              controller: c.tecPincode,
              label: _label('Pin Code *'),
              maxLength: 6,
              textInputType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            ),
            SizedBox(height: 14.h),

            // ── Health Card Details ────────────────────────────────────────
            _sectionLabel('Health Card Details'),
            SizedBox(height: 8.h),
            AppTextField(
              controller: c.tecCardExpiry,
              label: _label('Card Expiry Date'),
              readOnly: true,
              onTap: () =>
                  _pickDate(context, c.tecCardExpiry, c.onCardExpiryChanged),
            ),
            SizedBox(height: 12.h),
            Obx(() {
              if (!c.showRenewal.value) return const SizedBox.shrink();
              return Column(
                children: [
                  Container(
                    padding: EdgeInsets.all(8.w),
                    decoration: BoxDecoration(
                      color: Colors.orange.withValues(alpha: 0.1),
                      borderRadius: BorderRadius.circular(8),
                      border: Border.all(color: Colors.orange),
                    ),
                    child: Row(
                      children: [
                        const Icon(Icons.info_outline, color: Colors.orange),
                        SizedBox(width: 6.w),
                        CommonText(
                          text: 'Card expired. Renewal required.',
                          fontSize: 12.sp,
                          fontWeight: FontWeight.w500,
                          textColor: Colors.orange,
                          textAlign: TextAlign.left,
                        ),
                      ],
                    ),
                  ),
                  SizedBox(height: 8.h),
                  AppTextField(
                    controller: c.tecRenewalDate,
                    label: _label('Renewal Date *'),
                    readOnly: true,
                    onTap: () => _pickDate(context, c.tecRenewalDate, (_) {}),
                  ),
                  SizedBox(height: 12.h),
                ],
              );
            }),
            SizedBox(height: 14.h),

            // ── Face Detection ─────────────────────────────────────────────
            _sectionLabel('Face Detection'),
            SizedBox(height: 6.h),
            Row(
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Enable face detection',
                        style: TextStyle(
                          fontSize: 13.sp,
                          color: kTextColor,
                          fontFamily: FontConstants.interFonts,
                        ),
                      ),
                      Text(
                        'Turn off to skip; patient photo becomes optional',
                        style: TextStyle(
                          fontSize: 11.sp,
                          color: kLabelTextColor,
                          fontFamily: FontConstants.interFonts,
                        ),
                      ),
                    ],
                  ),
                ),
                Obx(
                  () => Switch(
                    value: c.isFaceDetection.value,
                    activeThumbColor: kPrimaryColor,
                    onChanged: (v) => c.isFaceDetection.value = v,
                  ),
                ),
              ],
            ),
            SizedBox(height: 14.h),

            // ── Photo Upload ───────────────────────────────────────────────
            _sectionLabel('Photo Upload'),
            SizedBox(height: 8.h),
            Obx(
              () => Row(
                children: [
                  Expanded(
                    child: _PhotoTile(
                      label: c.isDependent.value ? 'Identity Card' : 'Health Card',
                      icon: Icons.credit_card,
                      localPath: c.healthCardPhotoPath.value,
                      onTap: c.pickHealthCardPhoto,
                    ),
                  ),
                  SizedBox(width: 8.w),
                  Expanded(
                    child: _PhotoTile(
                      label: 'Patient Photo',
                      icon: Icons.person_pin,
                      localPath: c.patientPhotoPath.value,
                      onTap: c.pickPatientPhoto,
                      required: c.isFaceDetection.value,
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(height: 10.h),
            Obx(
              () => Row(
                children: [
                  Expanded(
                    child: _PhotoTile(
                      label: 'HIV Concern Letter',
                      icon: Icons.description,
                      localPath: c.hivLetterPath.value,
                      onTap: c.pickHivLetterPhoto,
                    ),
                  ),
                  SizedBox(width: 8.w),
                  if (c.showRenewal.value)
                    Expanded(
                      child: _PhotoTile(
                        label: 'Renewal Slip',
                        icon: Icons.autorenew,
                        localPath: c.renewalFormPath.value,
                        onTap: c.pickRenewalFormPhoto,
                      ),
                    )
                  else
                    const Expanded(child: SizedBox.shrink()),
                ],
              ),
            ),
            SizedBox(height: 20.h),
            AppButtonWithIcon(
              title: 'Register Patient',
              mWidth: double.infinity,
              mHeight: 52,
              onTap: () => c.showConfirmationDialog(context),
            ),
            SizedBox(height: 20.h),
          ],
        ),
      ),
    );
  }

  // ── Helpers ────────────────────────────────────────────────────────────────

  Widget _campBanner() {
    return Container(
      padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
      decoration: BoxDecoration(
        color: kPrimaryColor.withValues(alpha: 0.08),
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: kPrimaryColor.withValues(alpha: 0.25)),
      ),
      child: Row(
        children: [
          const Icon(Icons.location_on, color: kPrimaryColor, size: 18),
          SizedBox(width: 8.w),
          Expanded(
            child: Text(
              c.navCampLocation.isEmpty ? 'Selected Camp' : c.navCampLocation,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 13.sp,
                fontWeight: FontWeight.w600,
                color: kPrimaryColor,
              ),
            ),
          ),
        ],
      ),
    );
  }

  /// Simple bold section label with a coloured left indicator
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
        Text(
          text,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w600,
            fontSize: 13.sp,
            color: kTextColor,
          ),
        ),
      ],
    );
  }

  Widget _labelText(String text) => Text(
        text,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 12.sp,
          fontWeight: FontWeight.w500,
          color: kTextColor,
        ),
      );

  Widget _label(String text) => Text(
        text,
        style: TextStyle(
          color: kLabelTextColor,
          fontSize: 14.sp,
          fontFamily: FontConstants.interFonts,
        ),
      );

  /// Simple outlined radio-style chip (no icon, keeps it close to native RadioButton feel)
  Widget _radioChip({
    required String label,
    required bool selected,
    required VoidCallback onTap,
  }) {
    return Expanded(
      child: GestureDetector(
        onTap: onTap,
        child: Container(
          padding: EdgeInsets.symmetric(vertical: 9.h),
          decoration: BoxDecoration(
            color: selected
                ? kPrimaryColor.withValues(alpha: 0.08)
                : kWhiteColor,
            borderRadius: BorderRadius.circular(8),
            border: Border.all(
              color: selected ? kPrimaryColor : kTextFieldBorder,
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
                color: selected ? kPrimaryColor : kLabelTextColor,
              ),
              SizedBox(width: 5.w),
              Text(
                label,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 13.sp,
                  fontWeight: selected ? FontWeight.w600 : FontWeight.w400,
                  color: selected ? kPrimaryColor : kTextColor,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _showRelationPicker(BuildContext context) {
    if (c.relationList.isEmpty) return;
    showModalBottomSheet(
      context: context,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (_) {
        return Container(
          constraints: BoxConstraints(
            maxHeight: MediaQuery.of(context).size.height * 0.55,
          ),
          child: ListView.separated(
            itemCount: c.relationList.length,
            separatorBuilder: (context, index) => const Divider(height: 1),
            itemBuilder: (_, i) {
              final item = c.relationList[i];
              return ListTile(
                title: Text(
                  item.relName ?? '--',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 14.sp,
                    color: kTextColor,
                  ),
                ),
                trailing:
                    const Icon(Icons.chevron_right, color: kPrimaryColor),
                onTap: () {
                  Navigator.pop(context);
                  c.selectedRelation.value = item;
                },
              );
            },
          ),
        );
      },
    );
  }

  Future<void> _pickDate(
    BuildContext context,
    TextEditingController controller,
    Function(String) onPicked,
  ) async {
    final now = DateTime.now();
    final picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: DateTime(1900),
      lastDate: now,
    );
    if (picked != null) {
      final text = FormatterManager.formatDateToString(picked);
      controller.text = text;
      onPicked(text);
    }
  }
}

class _PhotoTile extends StatelessWidget {
  final String label;
  final IconData icon;
  final String localPath;
  final VoidCallback onTap;
  final bool required;

  const _PhotoTile({
    required this.label,
    required this.icon,
    required this.localPath,
    required this.onTap,
    this.required = false,
  });

  @override
  Widget build(BuildContext context) {
    Widget content;
    if (localPath.isNotEmpty) {
      content = Image.file(File(localPath), fit: BoxFit.cover);
    } else {
      content = Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(icon, size: 28, color: kLabelTextColor),
          SizedBox(height: 6.h),
          Text(
            required ? '$label *' : label,
            textAlign: TextAlign.center,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 11.sp,
              color: kLabelTextColor,
            ),
          ),
        ],
      );
    }

    return GestureDetector(
      onTap: onTap,
      child: Container(
        height: 90.h,
        decoration: BoxDecoration(
          color: kWhiteColor,
          borderRadius: BorderRadius.circular(8),
          border: Border.all(color: kTextFieldBorder),
        ),
        clipBehavior: Clip.antiAlias,
        child: content,
      ),
    );
  }
}
