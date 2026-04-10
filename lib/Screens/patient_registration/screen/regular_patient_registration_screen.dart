// ignore_for_file: file_names

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/patient_registration/controller/regular_patient_registration_controller.dart';

class RegularPatientRegistrationScreen extends StatefulWidget {
  const RegularPatientRegistrationScreen({super.key});

  @override
  State<RegularPatientRegistrationScreen> createState() =>
      _RegularPatientRegistrationScreenState();
}

class _RegularPatientRegistrationScreenState
    extends State<RegularPatientRegistrationScreen> {
  late final RegularPatientRegistrationController c;

  @override
  void initState() {
    super.initState();
    c = Get.find<RegularPatientRegistrationController>();
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
            SizedBox(height: 12.h),
            _sectionTitle(Icons.badge_rounded, 'Worker Information'),
            SizedBox(height: 8.h),
            AppTextField(
              controller: c.tecWorkerRegNo,
              label: _label('Beneficiary Reg. No *'),
              hint: '12-digit number',
              textInputType: TextInputType.number,
              maxLength: 12,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              onChange: c.onWorkerRegNoChanged,
              suffixIcon: Obx(
                () =>
                    c.isLoadingBeneficiary.value
                        ? const SizedBox(
                          width: 18,
                          height: 18,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        )
                        : const Icon(Icons.search),
              ).paddingOnly(right: 8.w),
            ),
            SizedBox(height: 12.h),
            AppTextField(
              controller: c.tecFullName,
              label: _label('English Name *'),
              textCapitalization: TextCapitalization.characters,
              inputFormatters: [UpperCaseTextFormatter()],
            ),
            SizedBox(height: 16.h),
            _sectionTitle(Icons.person_rounded, 'Personal Details'),
            SizedBox(height: 8.h),
            Obx(
              () => Row(
                children: [
                  _selectionChip(
                    label: 'Mr.',
                    selected: c.selectedTitle.value == 'Mr.',
                    onTap: () => c.selectedTitle.value = 'Mr.',
                  ),
                  SizedBox(width: 8.w),
                  _selectionChip(
                    label: 'Mrs.',
                    selected: c.selectedTitle.value == 'Mrs.',
                    onTap: () => c.selectedTitle.value = 'Mrs.',
                  ),
                  SizedBox(width: 8.w),
                  _selectionChip(
                    label: 'Ms.',
                    selected: c.selectedTitle.value == 'Ms.',
                    onTap: () => c.selectedTitle.value = 'Ms.',
                  ),
                ],
              ),
            ),
            SizedBox(height: 12.h),
            Obx(
              () => Row(
                children: [
                  _selectionChip(
                    label: 'Male',
                    icon: Icons.male,
                    selected: c.selectedGender.value == 'M',
                    onTap: () => c.selectedGender.value = 'M',
                  ),
                  SizedBox(width: 8.w),
                  _selectionChip(
                    label: 'Female',
                    icon: Icons.female,
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
                    prefixIcon: const Icon(
                      Icons.cake_rounded,
                      color: kPrimaryColor,
                      size: 18,
                    ).paddingOnly(left: 6.w),
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
              textInputType: TextInputType.phone,
              maxLength: 10,
              prefixIcon: const Icon(
                Icons.phone_rounded,
                color: kPrimaryColor,
                size: 18,
              ).paddingOnly(left: 6.w),
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            ),
            SizedBox(height: 12.h),
            AppTextField(
              controller: c.tecAadhaarNo,
              label: _label('Aadhaar Number *'),
              textInputType: TextInputType.number,
              maxLength: 12,
              prefixIcon: const Icon(
                Icons.credit_card_rounded,
                color: kPrimaryColor,
                size: 18,
              ).paddingOnly(left: 6.w),
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            ),
            SizedBox(height: 16.h),
            _sectionTitle(Icons.home_rounded, 'Address Details'),
            SizedBox(height: 8.h),
            AppTextField(
              controller: c.tecPermAddr,
              label: _label('Permanent Address'),
              maxLines: 2,
              minLines: 2,
              prefixIcon: const Icon(
                Icons.home_work_rounded,
                color: kPrimaryColor,
                size: 18,
              ).paddingOnly(left: 6.w),
            ),
            SizedBox(height: 12.h),
            AppTextField(
              controller: c.tecLocalAddr,
              label: _label('Local Address *'),
              maxLines: 2,
              minLines: 2,
              prefixIcon: const Icon(
                Icons.location_city_rounded,
                color: kPrimaryColor,
                size: 18,
              ).paddingOnly(left: 6.w),
            ),
            SizedBox(height: 12.h),
            AppTextField(
              controller: c.tecPincode,
              label: _label('Pin Code *'),
              textInputType: TextInputType.number,
              maxLength: 6,
              prefixIcon: const Icon(
                Icons.pin_drop_rounded,
                color: kPrimaryColor,
                size: 18,
              ).paddingOnly(left: 6.w),
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            ),
            SizedBox(height: 16.h),
            _sectionTitle(Icons.credit_card_rounded, 'Health Card Details'),
            SizedBox(height: 8.h),
            AppTextField(
              controller: c.tecCardRegDate,
              label: _label('Card Registration Date'),
              readOnly: true,
              prefixIcon: const Icon(
                Icons.event_rounded,
                color: kPrimaryColor,
                size: 18,
              ).paddingOnly(left: 6.w),
              onTap: () => _pickDate(context, c.tecCardRegDate, c.onCardRegDateChanged),
            ),
            SizedBox(height: 12.h),
            AppTextField(
              controller: c.tecCardExpiry,
              label: _label('Card Expiry Date'),
              readOnly: true,
              prefixIcon: const Icon(
                Icons.event_busy_rounded,
                color: kPrimaryColor,
                size: 18,
              ).paddingOnly(left: 6.w),
              onTap:
                  () => _pickDate(context, c.tecCardExpiry, c.onCardExpiryChanged),
            ),
            SizedBox(height: 12.h),
            Obx(() {
              if (!c.showRenewal.value) return const SizedBox.shrink();
              return Column(
                crossAxisAlignment: CrossAxisAlignment.start,
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
                    label: _label('Card Renewal Date *'),
                    readOnly: true,
                    prefixIcon: const Icon(
                      Icons.autorenew_rounded,
                      color: kPrimaryColor,
                      size: 18,
                    ).paddingOnly(left: 6.w),
                    onTap: () => _pickDate(context, c.tecRenewalDate, (_) {}),
                  ),
                ],
              );
            }),
            SizedBox(height: 16.h),
            _sectionTitle(Icons.photo_camera_rounded, 'Photo Upload'),
            SizedBox(height: 6.h),
            CommonText(
              text: 'Upload clear photos of the following documents',
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              textColor: kLabelTextColor,
              textAlign: TextAlign.left,
            ),
            SizedBox(height: 10.h),
            Obx(
              () => Row(
                children: [
                  Expanded(
                    child: _PhotoTile(
                      label: 'Patient Photo',
                      icon: Icons.person_pin,
                      localPath: c.patientPhotoPath.value,
                      url: c.patientPhotoUrl.value,
                      onTap: c.pickPatientPhoto,
                    ),
                  ),
                  SizedBox(width: 8.w),
                  Expanded(
                    child: _PhotoTile(
                      label: 'Health Card',
                      icon: Icons.credit_card,
                      localPath: c.healthCardPhotoPath.value,
                      url: c.healthCardPhotoUrl.value,
                      onTap: c.pickHealthCardPhoto,
                    ),
                  ),
                  SizedBox(width: 8.w),
                  if (c.showRenewal.value)
                    Expanded(
                      child: _PhotoTile(
                        label: 'Renewal Slip',
                        icon: Icons.autorenew,
                        localPath: c.renewalFormPath.value,
                        url: c.renewalPhotoUrl.value,
                        onTap: c.pickRenewalFormPhoto,
                      ),
                    )
                  else
                    const Expanded(child: SizedBox.shrink()),
                ],
              ),
            ),
            SizedBox(height: 18.h),
            AppButtonWithIcon(
              title: 'Register Patient',
              mWidth: double.infinity,
              mHeight: 52,
              icon: const Icon(Icons.check_circle_rounded, color: Colors.white),
              onTap: () {
                c.showConfirmationDialog(context);
              },
            ),
            SizedBox(height: 20.h),
          ],
        ),
      ),
    );
  }

  Widget _campBanner() {
    return Container(
      padding: EdgeInsets.all(12.w),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          colors: [
            kPrimaryColor.withValues(alpha: 0.12),
            kPrimaryColor.withValues(alpha: 0.02),
          ],
        ),
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: kPrimaryColor.withValues(alpha: 0.2)),
      ),
      child: Row(
        children: [
          const Icon(Icons.location_on, color: kPrimaryColor),
          SizedBox(width: 8.w),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              CommonText(
                text: 'Selected Camp',
                fontSize: 11.sp,
                fontWeight: FontWeight.w500,
                textColor: kLabelTextColor,
                textAlign: TextAlign.left,
              ),
              CommonText(
                text: c.navCampName,
                fontSize: 13.sp,
                fontWeight: FontWeight.w600,
                textColor: kPrimaryColor,
                textAlign: TextAlign.left,
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _sectionTitle(IconData icon, String text) {
    return Row(
      children: [
        Container(
          padding: const EdgeInsets.all(6),
          decoration: BoxDecoration(
            color: kPrimaryColor.withValues(alpha: 0.1),
            borderRadius: BorderRadius.circular(8),
          ),
          child: Icon(icon, color: kPrimaryColor, size: 16),
        ),
        SizedBox(width: 8.w),
        Text(
          text,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w600,
            fontSize: 13.sp,
            color: kPrimaryColor,
          ),
        ),
      ],
    );
  }

  Widget _label(String text) => Text(
        text,
        style: TextStyle(
          color: kLabelTextColor,
          fontSize: 14.sp,
          fontFamily: FontConstants.interFonts,
        ),
      );

  Widget _selectionChip({
    required String label,
    IconData? icon,
    required bool selected,
    required VoidCallback onTap,
  }) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 8.h),
        decoration: BoxDecoration(
          color: selected ? kPrimaryColor : kWhiteColor,
          borderRadius: BorderRadius.circular(20),
          border: Border.all(color: kTextFieldBorder),
        ),
        child: Row(
          children: [
            if (icon != null) ...[
              Icon(icon, size: 16, color: selected ? kWhiteColor : kTextColor),
              SizedBox(width: 4.w),
            ],
            Text(
              label,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 12.sp,
                fontWeight: FontWeight.w600,
                color: selected ? kWhiteColor : kTextColor,
              ),
            ),
          ],
        ),
      ),
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
  final String url;
  final VoidCallback onTap;

  const _PhotoTile({
    required this.label,
    required this.icon,
    required this.localPath,
    required this.url,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    Widget content;
    if (localPath.isNotEmpty) {
      content = Image.file(File(localPath), fit: BoxFit.cover);
    } else if (url.isNotEmpty) {
      content = Image.network(url, fit: BoxFit.cover);
    } else {
      content = Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(icon, size: 28, color: kLabelTextColor),
          SizedBox(height: 6.h),
          Text(
            label,
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
          borderRadius: BorderRadius.circular(10),
          border: Border.all(color: kTextFieldBorder),
        ),
        clipBehavior: Clip.antiAlias,
        child: content,
      ),
    );
  }
}

