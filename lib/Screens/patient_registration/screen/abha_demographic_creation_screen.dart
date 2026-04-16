// ignore_for_file: file_names, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/selection_bottom_sheet.dart';
import 'package:s2toperational/Screens/patient_registration/controller/abha_demographic_creation_controller.dart';

class AbhaDemographicCreationScreen extends StatefulWidget {
  final String campId;
  final String siteId;
  final String distLgdCode;
  final String district;
  final String campType;
  final int empCode;

  const AbhaDemographicCreationScreen({
    super.key,
    required this.campId,
    required this.siteId,
    required this.distLgdCode,
    required this.district,
    required this.campType,
    required this.empCode,
  });

  @override
  State<AbhaDemographicCreationScreen> createState() =>
      _AbhaDemographicCreationScreenState();
}

class _AbhaDemographicCreationScreenState
    extends State<AbhaDemographicCreationScreen> {
  late final AbhaDemographicCreationController ctrl;

  @override
  void initState() {
    super.initState();
    Get.delete<AbhaDemographicCreationController>(force: true);
    ctrl = Get.put(AbhaDemographicCreationController()
      ..campId = widget.campId
      ..siteId = widget.siteId
      ..distLgdCode = widget.distLgdCode
      ..district = widget.district
      ..campType = widget.campType
      ..empCode = widget.empCode);
  }

  @override
  void dispose() {
    Get.delete<AbhaDemographicCreationController>(force: true);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey.shade100,
      appBar: mAppBar(
        scTitle: 'ABHA Creation',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: Obx(() {
        if (!ctrl.sessionReady.value && ctrl.sessionError.value.isNotEmpty) {
          return _buildSessionError();
        }
        if (ctrl.sessionLoading.value) return const SizedBox.shrink();
        return SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildLogosCard(),
              Padding(
                padding: EdgeInsets.fromLTRB(12.w, 12.h, 12.w, 20.h),
                child: _buildFormPhase(context),
              ),
            ],
          ),
        );
      }),
    );
  }

  // ── Session error ─────────────────────────────────────────────────

  Widget _buildSessionError() {
    return Center(
      child: Padding(
        padding: EdgeInsets.all(24.w),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(Icons.error_outline, color: Colors.red.shade400, size: 48.sp),
            SizedBox(height: 12.h),
            CommonText(
              text: ctrl.sessionError.value,
              fontSize: 13.sp,
              fontWeight: FontWeight.w400,
              textColor: Colors.red.shade700,
              textAlign: TextAlign.center,
            ),
            SizedBox(height: 20.h),
            OutlinedButton(
              onPressed: ctrl.initSession,
              style: OutlinedButton.styleFrom(
                foregroundColor: kPrimaryColor,
                side: const BorderSide(color: kPrimaryColor, width: 1.5),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(6)),
                padding: EdgeInsets.symmetric(horizontal: 28.w, vertical: 12.h),
              ),
              child: CommonText(
                text: 'Retry',
                fontSize: 15.sp,
                fontWeight: FontWeight.w600,
                textColor: kPrimaryColor,
                textAlign: TextAlign.center,
              ),
            ),
          ],
        ),
      ),
    );
  }

  // ── Logos card ────────────────────────────────────────────────────

  Widget _buildLogosCard() {
    return Card(
      margin: EdgeInsets.all(10.w),
      elevation: 1,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(5)),
      child: Row(
        children: [
          Expanded(
            child: Image.asset(
              'assets/images/nha_logo.png',
              height: 80.h,
              fit: BoxFit.contain,
            ),
          ),
          Expanded(
            child: Image.asset(
              'assets/images/abha_logo.png',
              height: 80.h,
              fit: BoxFit.contain,
            ),
          ),
        ],
      ),
    );
  }

  // ── Demographic form phase ────────────────────────────────────────

  Widget _buildFormPhase(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [

        // Aadhaar Number
        AppTextField(
          controller: ctrl.aadhaarCtrl,
          label: const Text('Aadhaar Number *'),
          hint: 'Enter Aadhaar Number',
          textInputType: TextInputType.number,
          maxLength: 12,
          inputFormatters: [FilteringTextInputFormatter.digitsOnly],
        ),
        SizedBox(height: 12.h),

        // Name
        AppTextField(
          controller: ctrl.nameCtrl,
          label: const Text('Name *'),
          hint: 'Enter Name',
          textInputType: TextInputType.name,
          textCapitalization: TextCapitalization.words,
        ),
        SizedBox(height: 12.h),

        // DOB
        AppTextField(
          controller: ctrl.dobCtrl,
          label: const Text('Date of Birth *'),
          hint: 'yyyy/mm/dd',
          readOnly: true,
          onTap: () => ctrl.pickDob(context),
        ),
        SizedBox(height: 12.h),

        // Gender
        _sectionLabel('Gender *'),
        SizedBox(height: 6.h),
        Obx(() => Row(
              children: [
                _genderChip('Male', 'M'),
                SizedBox(width: 8.w),
                _genderChip('Female', 'F'),
                SizedBox(width: 8.w),
                _genderChip('Other', 'O'),
              ],
            )),
        SizedBox(height: 12.h),

        // Address
        AppTextField(
          controller: ctrl.addressCtrl,
          label: const Text('Address *'),
          hint: 'Enter Address',
          maxLines: 2,
          minLines: 2,
          textCapitalization: TextCapitalization.sentences,
        ),
        SizedBox(height: 12.h),

        // State picker
        Obx(() => GestureDetector(
              onTap: () => _showStatePicker(context),
              child: AbsorbPointer(
                child: AppTextField(
                  controller: TextEditingController(
                      text: ctrl.selectedState.value),
                  label: const Text('Select State *'),
                  readOnly: true,
                  suffixIcon: const Icon(Icons.arrow_drop_down,
                      color: kPrimaryColor),
                ),
              ),
            )),
        SizedBox(height: 12.h),

        // District picker
        GestureDetector(
          onTap: () => _showDistrictPicker(context),
          child: AbsorbPointer(
            child: AppTextField(
              controller: ctrl.districtCtrl,
              label: const Text('Select District *'),
              readOnly: true,
              suffixIcon: const Icon(Icons.arrow_drop_down,
                  color: kPrimaryColor),
            ),
          ),
        ),
        SizedBox(height: 12.h),

        // Pincode
        AppTextField(
          controller: ctrl.pincodeCtrl,
          label: const Text('Pincode *'),
          hint: 'Enter Pincode',
          textInputType: TextInputType.number,
          maxLength: 6,
          inputFormatters: [FilteringTextInputFormatter.digitsOnly],
        ),
        SizedBox(height: 12.h),

        // Mobile Number
        AppTextField(
          controller: ctrl.mobileCtrl,
          label: const Text('Mobile Number *'),
          hint: 'Enter Mobile Number',
          textInputType: TextInputType.phone,
          maxLength: 10,
          inputFormatters: [FilteringTextInputFormatter.digitsOnly],
        ),
        SizedBox(height: 16.h),

        // Terms & Conditions
        _buildTermsSection(),
        SizedBox(height: 16.h),

        // Create ABHA button
        AppButtonWithIcon(
          title: 'Create ABHA',
          mHeight: 48,
          mWidth: double.infinity,
          onTap: ctrl.sessionReady.value ? ctrl.onCreateAbha : () {},
        ),
      ],
    );
  }

  // ── Terms & Conditions ────────────────────────────────────────────

  Widget _buildTermsSection() {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        border: Border.all(color: Colors.grey.shade400),
        borderRadius: BorderRadius.circular(6),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 8.h),
            child: CommonText(
              text: 'Terms and Conditions',
              fontSize: 14.sp,
              fontWeight: FontWeight.bold,
              textColor: Colors.black87,
              textAlign: TextAlign.start,
            ),
          ),
          Divider(height: 1, color: Colors.grey.shade300),
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 6.h),
            child: CommonText(
              text: 'I hereby declare that',
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              textColor: Colors.black87,
              textAlign: TextAlign.start,
            ),
          ),
          Obx(() => _consentTile(
                value: ctrl.cbAadhaar.value,
                text: AbhaDemographicCreationController.consentAadhaar,
                onChanged: (v) => ctrl.cbAadhaar.value = v ?? false,
              )),
          Obx(() => _consentTile(
                value: ctrl.cbAbhaLink.value,
                text: AbhaDemographicCreationController.consentAbhaLink,
                onChanged: (v) => ctrl.cbAbhaLink.value = v ?? false,
              )),
          Obx(() => _consentTile(
                value: ctrl.cbHealthRecord.value,
                text: AbhaDemographicCreationController.consentHealthRecords,
                onChanged: (v) => ctrl.cbHealthRecord.value = v ?? false,
              )),
          Obx(() => _consentTile(
                value: ctrl.cbAnon.value,
                text: AbhaDemographicCreationController.consentAnonymization,
                onChanged: (v) => ctrl.cbAnon.value = v ?? false,
              )),
          Obx(() => _consentTile(
                value: ctrl.cbAnon1.value,
                text: AbhaDemographicCreationController.consentAnonymization1,
                indent: true,
                onChanged: (v) => ctrl.cbAnon1.value = v ?? false,
              )),
          Obx(() => _consentTile(
                value: ctrl.cbAnon2.value,
                text: AbhaDemographicCreationController.consentAnonymization2,
                indent: true,
                onChanged: (v) => ctrl.cbAnon2.value = v ?? false,
              )),
          Divider(height: 1, color: Colors.grey.shade300),
          Obx(() => _consentTile(
                value: ctrl.allConsents,
                text: 'Accept All',
                onChanged: (v) => ctrl.onAcceptAllChanged(v ?? false),
                bold: true,
              )),
        ],
      ),
    );
  }

  Widget _consentTile({
    required bool value,
    required String text,
    required ValueChanged<bool?> onChanged,
    bool indent = false,
    bool bold = false,
  }) {
    return Padding(
      padding: EdgeInsets.only(left: indent ? 20.w : 0, top: 4.h, bottom: 4.h),
      child: Container(
        decoration: BoxDecoration(
          border: Border(
              top: BorderSide(color: Colors.grey.shade300, width: 0.5)),
        ),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Checkbox(
              value: value,
              onChanged: onChanged,
              activeColor: kPrimaryColor,
              materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
              visualDensity: VisualDensity.compact,
            ),
            Expanded(
              child: Padding(
                padding: EdgeInsets.only(top: 10.h, right: 8.w, bottom: 6.h),
                child: CommonText(
                  text: text,
                  fontSize: bold ? 14.sp : 14.sp,
                  fontWeight: bold ? FontWeight.w600 : FontWeight.w400,
                  textColor: Colors.black87,
                  textAlign: TextAlign.start,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  // ── Helpers ───────────────────────────────────────────────────────

  Widget _sectionLabel(String text) {
    return CommonText(
      text: text,
      fontSize: 13.sp,
      fontWeight: FontWeight.w600,
      textColor: Colors.black87,
      textAlign: TextAlign.start,
    );
  }

  Widget _genderChip(String label, String value) {
    final selected = ctrl.selectedGender.value == value;
    return GestureDetector(
      onTap: () => ctrl.selectedGender.value = value,
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 8.h),
        decoration: BoxDecoration(
          color: selected ? kPrimaryColor : Colors.white,
          borderRadius: BorderRadius.circular(20.r),
          border: Border.all(
            color: selected ? kPrimaryColor : Colors.grey.shade400,
          ),
        ),
        child: CommonText(
          text: label,
          fontSize: 13.sp,
          fontWeight: FontWeight.w600,
          textColor: selected ? Colors.white : Colors.black87,
          textAlign: TextAlign.center,
        ),
      ),
    );
  }

  void _showStatePicker(BuildContext context) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: kWhiteColor,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (_) => SelectionBottomSheet<String, String>(
        title: 'Select State',
        items: AbhaDemographicCreationController.kIndianStates,
        selectedValue: ctrl.selectedState.value,
        valueFor: (item) => item,
        labelFor: (item) => item,
        onItemTap: (item) {
          Navigator.pop(context);
          ctrl.selectedState.value = item;
          ctrl.districtCtrl.clear();
          ctrl.selectedDistrictCode = '';
        },
        height: 350.h,
        padding: EdgeInsets.fromLTRB(16.w, 16.h, 16.w, 0),
        showRadio: true,
      ),
    );
  }

  Future<void> _showDistrictPicker(BuildContext context) async {
    ToastManager.showLoader();
    await ctrl.fetchDistrictList();
    ToastManager.hideLoader();
    if (!context.mounted) return;
    if (ctrl.districtList.isEmpty) {
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
      builder: (_) => SelectionBottomSheet(
        title: 'Select District',
        items: ctrl.districtList,
        selectedValue: ctrl.districtCtrl.text,
        valueFor: (item) => item.distName ?? '',
        labelFor: (item) => item.distName ?? '--',
        onItemTap: (item) {
          Navigator.pop(context);
          ctrl.districtCtrl.text = item.distName ?? '';
          ctrl.selectedDistrictCode = item.distLgdCode ?? '';
        },
        height: 350.h,
        padding: EdgeInsets.fromLTRB(16.w, 16.h, 16.w, 0),
        showRadio: true,
      ),
    );
  }
}