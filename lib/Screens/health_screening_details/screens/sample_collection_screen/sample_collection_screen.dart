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
import 'package:s2toperational/Screens/health_screening_details/controllers/sample_collection_controller.dart';

class SampleCollectionScreen extends StatelessWidget {
  final UserAttendancesUsingSitedetailsIDOutput patientItem;
  final int campId;

  const SampleCollectionScreen({
    super.key,
    required this.patientItem,
    required this.campId,
  });

  @override
  Widget build(BuildContext context) {
    return GetBuilder<SampleCollectionController>(
      init: SampleCollectionController(
        patientItem: patientItem,
        campId: campId,
      ),
      dispose: (_) => Get.delete<SampleCollectionController>(),
      builder: (ctrl) => Scaffold(
        backgroundColor: kBackground,
        appBar: mAppBar(
          scTitle: 'Sample Collection',
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
                  () => SizedBox(
                    width: double.infinity,
                    child: AppActiveButton(
                      buttontitle: ctrl.isSubmitting.value
                          ? 'Submitting...'
                          : 'Submit',
                      onTap: ctrl.isSubmitting.value
                          ? () {}
                          : () => ctrl.validateAndSubmit(context),
                    ),
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
  final SampleCollectionController ctrl;
  final BuildContext context;
  const _TestCard({required this.ctrl, required this.context});

  @override
  Widget build(BuildContext context) {
    return _SectionCard(
      title: 'Test',
      icon: Icons.science_outlined,
      child: Column(
        children: [
          AppTextField(
            controller: ctrl.barcode1Ctrl,
            readOnly: ctrl.barcode1Locked,
            label: CommonText(
              text: 'Barcode',
              fontSize: 14.sp * 1.3,
              fontWeight: FontWeight.w600,
              textColor: kLabelTextColor,
              textAlign: TextAlign.left,
            ),
            hint: ctrl.barcode1Locked ? '' : 'Enter or scan barcode',
            suffixIcon: ctrl.barcode1Locked
                ? null
                : GestureDetector(
                    onTap: () => ctrl.scanBarcode(context),
                    child: Icon(Icons.qr_code_scanner_rounded,
                        size: 22.r, color: kPrimaryColor),
                  ),
          ),
          SizedBox(height: 12.h),
          AppTextField(
            controller: ctrl.dateCtrl,
            readOnly: true,
            onTap: () => ctrl.pickDate(context),
            label: CommonText(
              text: 'Sample Collection Date',
              fontSize: 14.sp * 1.3,
              fontWeight: FontWeight.w600,
              textColor: kLabelTextColor,
              textAlign: TextAlign.left,
            ),
            hint: 'Tap to select date',
            suffixIcon:
                Icon(Icons.calendar_today_outlined, size: 18.r, color: kPrimaryColor),
          ),
          SizedBox(height: 12.h),
          AppTextField(
            controller: ctrl.timeCtrl,
            readOnly: true,
            onTap: () => ctrl.pickTime(context),
            label: CommonText(
              text: 'Sample Collection Time',
              fontSize: 14.sp * 1.3,
              fontWeight: FontWeight.w600,
              textColor: kLabelTextColor,
              textAlign: TextAlign.left,
            ),
            hint: 'Tap to select time',
            suffixIcon:
                Icon(Icons.access_time_rounded, size: 18.r, color: kPrimaryColor),
          ),
          SizedBox(height: 12.h),
          _thinDivider(),
          SizedBox(height: 8.h),
          Obx(
            () => Row(
              children: [
                Icon(Icons.local_hospital_outlined,
                    size: 20.r, color: kPrimaryColor),
                SizedBox(width: 10.w),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      CommonText(
                        text: 'To Disha LIS',
                        fontSize: 14.sp,
                        fontWeight: FontWeight.w600,
                        textColor: kTextColor,
                        textAlign: TextAlign.left,
                      ),
                      CommonText(
                        text: 'All Test(except RT-PCR)',
                        fontSize: 12.sp,
                        fontWeight: FontWeight.w400,
                        textColor: kLabelTextColor,
                        textAlign: TextAlign.left,
                      ),
                    ],
                  ),
                ),
                Switch(
                  value: ctrl.toDishaLIS.value,
                  onChanged: (v) => ctrl.toDishaLIS.value = v,
                  activeColor: kPrimaryColor,
                  activeTrackColor: kPrimaryColor.withValues(alpha: 0.4),
                  inactiveThumbColor: Colors.grey.shade500,
                  inactiveTrackColor: Colors.grey.shade300,
                ),
              ],
            ),
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
