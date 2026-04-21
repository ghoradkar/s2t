import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Modules/Json_Class/UserAttendancesUsingSitedetailsIDResponse/UserAttendancesUsingSitedetailsIDResponse.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/urine_sample_collection_controller.dart';

class UrineSampleCollectionScreen extends StatelessWidget {
  final UserAttendancesUsingSitedetailsIDOutput patientItem;
  final int campId;

  const UrineSampleCollectionScreen({
    super.key,
    required this.patientItem,
    required this.campId,
  });

  @override
  Widget build(BuildContext context) {
    return GetBuilder<UrineSampleCollectionController>(
      init: UrineSampleCollectionController(
        patientItem: patientItem,
        campId: campId,
      ),
      dispose: (_) => Get.delete<UrineSampleCollectionController>(),
      builder: (ctrl) => Scaffold(
        backgroundColor: kBackground,
        appBar: mAppBar(
          scTitle: 'Urine Sample Collection',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: SafeArea(
          child: SingleChildScrollView(
            padding: EdgeInsets.fromLTRB(16.w, 14.h, 16.w, 32.h),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _PatientDetailsCard(patientItem: patientItem),
                SizedBox(height: 14.h),
                _TestCard(ctrl: ctrl, context: context),
                SizedBox(height: 24.h),
                Obx(
                  () => ctrl.showSubmit
                      ? SizedBox(
                          width: double.infinity,
                          child: AppActiveButton(
                            buttontitle: ctrl.isSubmitting.value
                                ? 'Submitting...'
                                : 'Submit',
                            onTap: ctrl.isSubmitting.value
                                ? () {}
                                : () => ctrl.validateAndSubmit(context),
                          ),
                        )
                      : const SizedBox.shrink(),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

// ── Patient Details Card ──────────────────────────────────────────────────────

class _PatientDetailsCard extends StatelessWidget {
  final UserAttendancesUsingSitedetailsIDOutput patientItem;
  const _PatientDetailsCard({required this.patientItem});

  @override
  Widget build(BuildContext context) {
    final p = patientItem;
    return _SectionCard(
      title: 'Patient Details',
      icon: Icons.person_outline_rounded,
      child: Column(
        children: [
          _infoRow(Icons.badge_outlined, 'Name',
              (p.englishName ?? '—').toUpperCase()),
          _thinDivider(),
          _infoRow(Icons.wc_outlined, 'Gender', _genderLabel(p.gender)),
          _thinDivider(),
          _infoRow(Icons.cake_outlined, 'Age',
              p.age != null ? '${p.age} yrs' : '—'),
          _thinDivider(),
          _infoRow(Icons.height_outlined, 'Height',
              p.heightCMs != null ? '${p.heightCMs} cm' : '—'),
          _thinDivider(),
          _infoRow(Icons.monitor_weight_outlined, 'Weight',
              p.weightKGs != null ? '${p.weightKGs} kg' : '—'),
        ],
      ),
    );
  }

  String _genderLabel(String? g) {
    if (g == null) return '—';
    if (g.toUpperCase() == 'M') return 'Male';
    if (g.toUpperCase() == 'F') return 'Female';
    if (g.toUpperCase() == 'O') return 'Other';
    return g;
  }
}

// ── Test Card ─────────────────────────────────────────────────────────────────

class _TestCard extends StatelessWidget {
  final UrineSampleCollectionController ctrl;
  final BuildContext context;
  const _TestCard({required this.ctrl, required this.context});

  @override
  Widget build(BuildContext context) {
    return _SectionCard(
      title: 'Urine Sample',
      icon: Icons.science_outlined,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          AppTextField(
            controller: ctrl.sampleCountCtrl,
            readOnly: true,
            label: CommonText(
              text: 'Sample Count',
              fontSize: 14.sp * 1.3,
              fontWeight: FontWeight.w600,
              textColor: kLabelTextColor,
              textAlign: TextAlign.left,
            ),
            hint: '',
          ),
          SizedBox(height: 12.h),
          AppTextField(
            controller: ctrl.barcodeCtrl,
            readOnly: true,
            label: CommonText(
              text: 'Barcode',
              fontSize: 14.sp * 1.3,
              fontWeight: FontWeight.w600,
              textColor: kLabelTextColor,
              textAlign: TextAlign.left,
            ),
            hint: ctrl.barcodeLocked ? '' : 'Scan barcode',
            suffixIcon: ctrl.barcodeLocked
                ? null
                : GestureDetector(
                    onTap: () => ctrl.scanBarcode(context),
                    child: Icon(Icons.qr_code_scanner_rounded,
                        size: 22.r, color: kPrimaryColor),
                  ),
          ),
          SizedBox(height: 16.h),
          _thinDivider(),
          SizedBox(height: 12.h),
          CommonText(
            text: 'Sample Status',
            fontSize: 14.sp,
            fontWeight: FontWeight.w600,
            textColor: kTextColor,
            textAlign: TextAlign.left,
          ),
          SizedBox(height: 8.h),
          Obx(
            () => Column(
              children: [
                _RadioOption(
                  label: 'Sample Collected',
                  value: 1,
                  groupValue: ctrl.sampleStatus.value,
                  onChanged: ctrl.onStatusChanged,
                ),
                _RadioOption(
                  label: 'Sample Not Collected',
                  value: 2,
                  groupValue: ctrl.sampleStatus.value,
                  onChanged: ctrl.onStatusChanged,
                ),
                if (ctrl.showRemarks) ...[
                  SizedBox(height: 12.h),
                  AppTextField(
                    controller: ctrl.remarkCtrl,
                    maxLines: 3,
                    label: CommonText(
                      text: 'Reason',
                      fontSize: 14.sp * 1.3,
                      fontWeight: FontWeight.w600,
                      textColor: kLabelTextColor,
                      textAlign: TextAlign.left,
                    ),
                    hint: 'Enter reason for not collecting urine sample (min 10 chars)',
                  ),
                ],
              ],
            ),
          ),
        ],
      ),
    );
  }
}

// ── Radio Option ──────────────────────────────────────────────────────────────

class _RadioOption extends StatelessWidget {
  final String label;
  final int value;
  final int groupValue;
  final ValueChanged<int?> onChanged;

  const _RadioOption({
    required this.label,
    required this.value,
    required this.groupValue,
    required this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => onChanged(value),
      child: Row(
        children: [
          Radio<int>(
            value: value,
            groupValue: groupValue,
            onChanged: onChanged,
            activeColor: kPrimaryColor,
          ),
          CommonText(
            text: label,
            fontSize: 14.sp,
            fontWeight: FontWeight.w500,
            textColor: kTextColor,
            textAlign: TextAlign.left,
          ),
        ],
      ),
    );
  }
}

// ── Shared Section Card ───────────────────────────────────────────────────────

class _SectionCard extends StatelessWidget {
  final String title;
  final IconData icon;
  final Widget child;
  const _SectionCard(
      {required this.title, required this.icon, required this.child});

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(14.r),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 10,
            offset: const Offset(0, 3),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 11.h),
            decoration: BoxDecoration(
              color: kPrimaryColor,
              borderRadius: BorderRadius.only(
                topLeft: Radius.circular(14.r),
                topRight: Radius.circular(14.r),
              ),
            ),
            child: Row(
              children: [
                Icon(icon,
                    color: Colors.white.withValues(alpha: 0.9), size: 21.r),
                SizedBox(width: 8.w),
                CommonText(
                  text: title,
                  fontSize: 14.sp,
                  fontWeight: FontWeight.w600,
                  textColor: Colors.white,
                  textAlign: TextAlign.left,
                ),
              ],
            ),
          ),
          Padding(
            padding: EdgeInsets.all(14.w),
            child: child,
          ),
        ],
      ),
    );
  }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

Widget _infoRow(IconData icon, String label, String value) {
  return Padding(
    padding: EdgeInsets.symmetric(vertical: 8.h),
    child: Row(
      children: [
        Icon(icon, size: 18.r, color: kPrimaryColor.withValues(alpha: 0.7)),
        SizedBox(width: 10.w),
        SizedBox(
          width: 80.w,
          child: CommonText(
            text: label,
            fontSize: 14.sp,
            fontWeight: FontWeight.w600,
            textColor: kLabelTextColor,
            textAlign: TextAlign.left,
          ),
        ),
        Expanded(
          child: CommonText(
            text: value.isEmpty ? '—' : value,
            fontSize: 14.sp,
            fontWeight: FontWeight.w500,
            textColor: kTextColor,
            textAlign: TextAlign.left,
            maxLine: 1,
            overflow: TextOverflow.ellipsis,
          ),
        ),
      ],
    ),
  );
}

Widget _thinDivider() =>
    Divider(height: 1, thickness: 0.8, color: Colors.grey.shade100);
