import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/selection_bottom_sheet.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/visual_screening_controller.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';
import 'package:s2toperational/Screens/health_screening_details/models/visual_screening_test_model.dart';

class VisualScreeningTestScreen extends StatefulWidget {
  final int campId;
  final UserAttendancesUsingSitedetailsIDOutput patient;

  const VisualScreeningTestScreen({
    super.key,
    required this.campId,
    required this.patient,
  });

  @override
  State<VisualScreeningTestScreen> createState() =>
      _VisualScreeningTestScreenState();
}

class _VisualScreeningTestScreenState extends State<VisualScreeningTestScreen> {
  late final VisualScreeningController controller;

  final List<Worker> _workers = [];

  // TextEditingControllers — synced from Rx via workers
  final _blindnessCtrl = TextEditingController();
  final _injuryLeftCtrl = TextEditingController();
  final _injuryRightCtrl = TextEditingController();
  final _snellenLeftCtrl = TextEditingController();
  final _snellenRightCtrl = TextEditingController();
  final _snellenLeftRemarkCtrl = TextEditingController();
  final _snellenRightRemarkCtrl = TextEditingController();
  final _jaegarLeftCtrl = TextEditingController();
  final _jaegarRightCtrl = TextEditingController();
  final _leftRemarkCtrl = TextEditingController();
  final _rightRemarkCtrl = TextEditingController();

  @override
  void initState() {
    super.initState();
    controller = Get.put(VisualScreeningController());
    controller.init(patientData: widget.patient, campID: widget.campId);

    _workers.addAll([
      ever(controller.blindnessName, (v) => _blindnessCtrl.text = v),
      ever(controller.injuryLeftName, (v) => _injuryLeftCtrl.text = v),
      ever(controller.injuryRightName, (v) => _injuryRightCtrl.text = v),
      ever(controller.snellenLeft, (v) => _snellenLeftCtrl.text = v),
      ever(controller.snellenRight, (v) => _snellenRightCtrl.text = v),
      ever(
        controller.snellenLeftRemark,
        (v) => _snellenLeftRemarkCtrl.text = v,
      ),
      ever(
        controller.snellenRightRemark,
        (v) => _snellenRightRemarkCtrl.text = v,
      ),
      ever(controller.jaegarLeft, (v) => _jaegarLeftCtrl.text = v),
      ever(controller.jaegarRight, (v) => _jaegarRightCtrl.text = v),
      ever(controller.leftRemark, (v) => _leftRemarkCtrl.text = v),
      ever(controller.rightRemark, (v) => _rightRemarkCtrl.text = v),
    ]);
  }

  @override
  void dispose() {
    for (final w in _workers) {
      w.dispose();
    }
    _blindnessCtrl.dispose();
    _injuryLeftCtrl.dispose();
    _injuryRightCtrl.dispose();
    _snellenLeftCtrl.dispose();
    _snellenRightCtrl.dispose();
    _snellenLeftRemarkCtrl.dispose();
    _snellenRightRemarkCtrl.dispose();
    _jaegarLeftCtrl.dispose();
    _jaegarRightCtrl.dispose();
    _leftRemarkCtrl.dispose();
    _rightRemarkCtrl.dispose();
    Get.delete<VisualScreeningController>();
    super.dispose();
  }

  // ── Build ─────────────────────────────────────────────────────────────────

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Visual Screening Test',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.fromLTRB(14.w, 14.h, 14.w, 24.h),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _patientCard(),
            SizedBox(height: 12.h),
            _testTypeRow(),
            SizedBox(height: 12.h),
            _blindnessCard(context),
            SizedBox(height: 12.h),
            _injuryCard(context),
            SizedBox(height: 12.h),
            _snellenCard(context),
            SizedBox(height: 12.h),
            _nearVisionCard(context),
            SizedBox(height: 12.h),
            _remarksCard(),
            SizedBox(height: 12.h),
            _spectaclesCard(),
            SizedBox(height: 16.h),
            Obx(() {
              final saving = controller.isSaving.value;
              return AppActiveButton(
                buttontitle: saving ? 'Saving…' : 'Save',
                onTap: () {
                  if (!saving) controller.save(context);
                },
              );
            }),
          ],
        ),
      ),
    );
  }

  // ── Patient card ──────────────────────────────────────────────────────────

  Widget _patientCard() {
    final p = widget.patient;
    return _card(
      title: 'Patient Details',
      icon: Icons.person_outlined,
      child: Column(
        children: [
          AppTextField(
            controller: TextEditingController(text: p.englishName ?? ''),
            readOnly: true,
            label: const Text('Name of the Beneficiary'),
          ),
          SizedBox(height: 10.h),
          Row(
            children: [
              Expanded(
                child: AppTextField(
                  controller: TextEditingController(text: p.gender ?? ''),
                  readOnly: true,
                  label: const Text('Gender'),
                ),
              ),
              SizedBox(width: 10.w),
              Expanded(
                child: AppTextField(
                  controller: TextEditingController(
                    text: p.age?.toString() ?? '',
                  ),
                  readOnly: true,
                  label: const Text('Age'),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  // ── Test type selector (Snellen Chart always active, Machine always disabled) ─

  Widget _testTypeRow() {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(10.r),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 12.h),
      child: Row(
        children: [
          _testTypeOption(label: 'Snellen Chart', selected: true),
          SizedBox(width: 24.w),
          _testTypeOption(label: 'Machine', selected: false, disabled: true),
        ],
      ),
    );
  }

  Widget _testTypeOption({
    required String label,
    required bool selected,
    bool disabled = false,
  }) {
    final activeColor = disabled ? kLabelTextColor : kPrimaryColor;
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          width: 18.r,
          height: 18.r,
          decoration: BoxDecoration(
            shape: BoxShape.circle,
            border: Border.all(
              color: selected ? activeColor : kTextFieldBorder,
              width: 2,
            ),
          ),
          child:
              selected
                  ? Center(
                    child: Container(
                      width: 9.r,
                      height: 9.r,
                      decoration: BoxDecoration(
                        shape: BoxShape.circle,
                        color: activeColor,
                      ),
                    ),
                  )
                  : null,
        ),
        SizedBox(width: 6.w),
        CommonText(
          text: label,
          fontSize: 13.sp,
          fontWeight: FontWeight.w500,
          textColor: disabled ? kLabelTextColor : kTextColor,
          textAlign: TextAlign.left,
        ),
      ],
    );
  }

  // ── Visually Impaired (Blindness) card ────────────────────────────────────

  Widget _blindnessCard(BuildContext context) {
    return _card(
      title: 'Visually Impaired (Blindness)',
      icon: Icons.visibility_off_outlined,
      child: AppTextField(
        controller: _blindnessCtrl,
        readOnly: true,
        label: const Text('Select Visually Impaired *'),
        suffixIcon: const Icon(Icons.arrow_drop_down),
        onTap: () => _showBlindnessPicker(context),
      ),
    );
  }

  void _showBlindnessPicker(BuildContext context) {
    final current = controller.blindnessId.value;
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<VisualOption, String>(
            title: 'Select Visually Impaired',
            items: VisualScreeningData.blindnessOptions,
            valueFor: (item) => item.id,
            labelFor: (item) => item.name,
            selectedValue: current.isEmpty ? null : current,
            height: 300.h,
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 16.h),
            onItemTap: (item) {
              Navigator.pop(context);
              controller.onBlindnessChanged(item.id, item.name);
              if (item.id == '1' || item.id == '2' || item.id == '3') {
                _showBlindnessAlert(context);
              }
            },
          ),
    );
  }

  void _showBlindnessAlert(BuildContext context) {
    showDialog(
      context: Get.context!,
      builder: (_) => ToastManager.commonAlert(
        Get.context!,
        "assets/icons/blind.png",
        "",
        'तुम्ही अंध पर्याय निवडला आहे.\nपर्याय बरोबर असल्याची खात्री करा\nअन्यथा योग्य पर्याय निवडा',
        () {
          Get.back();
        },
        () {
          Get.back();
          controller.resetBlindness();
          Future.delayed(const Duration(milliseconds: 300), () {
            if (mounted) _showBlindnessPicker(Get.context!);
          });
        },
        "Ok",
        "No",
      ),
    );
  }

  // ── Evidence of Disease or Injury card ───────────────────────────────────

  Widget _injuryCard(BuildContext context) {
    return Obx(() {
      final bid = controller.blindnessId.value;
      final leftBlind = bid == '2' || bid == '3';
      final rightBlind = bid == '1' || bid == '3';
      return _card(
        title: 'Evidence of Disease or Injury',
        icon: Icons.local_hospital_outlined,
        child: Row(
          children: [
            Expanded(
              child: AppTextField(
                controller: _injuryLeftCtrl,
                readOnly: true,
                label: const Text('Left'),
                suffixIcon:
                    leftBlind ? null : const Icon(Icons.arrow_drop_down),
                onTap:
                    leftBlind
                        ? null
                        : () => _showInjuryPicker(context, isLeft: true),
              ),
            ),
            SizedBox(width: 10.w),
            Expanded(
              child: AppTextField(
                controller: _injuryRightCtrl,
                readOnly: true,
                label: const Text('Right'),
                suffixIcon:
                    rightBlind ? null : const Icon(Icons.arrow_drop_down),
                onTap:
                    rightBlind
                        ? null
                        : () => _showInjuryPicker(context, isLeft: false),
              ),
            ),
          ],
        ),
      );
    });
  }

  void _showInjuryPicker(BuildContext context, {required bool isLeft}) {
    final current =
        isLeft ? controller.injuryLeftId.value : controller.injuryRightId.value;
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<VisualOption, String>(
            title:
                isLeft
                    ? 'Select Left Eye Injury/Disease'
                    : 'Select Right Eye Injury/Disease',
            items: VisualScreeningData.injuryOptions,
            valueFor: (item) => item.id,
            labelFor: (item) => item.name,
            selectedValue: current.isEmpty ? null : current,
            height: 380.h,
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 16.h),
            onItemTap: (item) {
              Navigator.pop(context);
              if (isLeft) {
                controller.onInjuryLeftChanged(item.id, item.name);
              } else {
                controller.onInjuryRightChanged(item.id, item.name);
              }
            },
          ),
    );
  }

  // ── Snellen Chart card ────────────────────────────────────────────────────

  Widget _snellenCard(BuildContext context) {
    return Obx(() {
      final bid = controller.blindnessId.value;
      final leftBlind = bid == '2' || bid == '3';
      final rightBlind = bid == '1' || bid == '3';
      return _card(
        title: 'Snellen Chart',
        icon: Icons.remove_red_eye_outlined,
        child: Column(
          children: [
            Row(
              children: [
                Expanded(
                  child: AppTextField(
                    controller: _snellenLeftCtrl,
                    readOnly: true,
                    label: const Text('Left'),
                    suffixIcon:
                        leftBlind ? null : const Icon(Icons.arrow_drop_down),
                    onTap:
                        leftBlind
                            ? null
                            : () => _showSnellenPicker(context, isLeft: true),
                  ),
                ),
                SizedBox(width: 10.w),
                Expanded(
                  child: AppTextField(
                    controller: _snellenRightCtrl,
                    readOnly: true,
                    label: const Text('Right'),
                    suffixIcon:
                        rightBlind ? null : const Icon(Icons.arrow_drop_down),
                    onTap:
                        rightBlind
                            ? null
                            : () => _showSnellenPicker(context, isLeft: false),
                  ),
                ),
              ],
            ),
            SizedBox(height: 10.h),
            Row(
              children: [
                Expanded(
                  child: AppTextField(
                    controller: _snellenLeftRemarkCtrl,
                    readOnly: true,
                    label: const Text('Left Eye Remark'),
                  ),
                ),
                SizedBox(width: 10.w),
                Expanded(
                  child: AppTextField(
                    controller: _snellenRightRemarkCtrl,
                    readOnly: true,
                    label: const Text('Right Eye Remark'),
                  ),
                ),
              ],
            ),
          ],
        ),
      );
    });
  }

  void _showSnellenPicker(BuildContext context, {required bool isLeft}) {
    final current =
        isLeft ? controller.snellenLeft.value : controller.snellenRight.value;
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<VisualOption, String>(
            title:
                isLeft
                    ? 'Select Left Eye Snellen Chart'
                    : 'Select Right Eye Snellen Chart',
            items: VisualScreeningData.snellenOptions,
            valueFor: (item) => item.name,
            labelFor: (item) => item.name,
            selectedValue: current.isEmpty ? null : current,
            height: 380.h,
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 16.h),
            onItemTap: (item) {
              Navigator.pop(context);
              if (isLeft) {
                controller.onSnellenLeftChanged(item.name);
              } else {
                controller.onSnellenRightChanged(item.name);
              }
            },
          ),
    );
  }

  // ── Near Vision Chart card ────────────────────────────────────────────────

  Widget _nearVisionCard(BuildContext context) {
    return Obx(() {
      final bid = controller.blindnessId.value;
      final leftBlind = bid == '2' || bid == '3';
      final rightBlind = bid == '1' || bid == '3';
      return _card(
        title: 'Near Vision Chart',
        icon: Icons.center_focus_strong_outlined,
        child: Row(
          children: [
            Expanded(
              child: AppTextField(
                controller: _jaegarLeftCtrl,
                readOnly: true,
                label: const Text('Left'),
                suffixIcon:
                    leftBlind ? null : const Icon(Icons.arrow_drop_down),
                onTap:
                    leftBlind
                        ? null
                        : () => _showJaegarPicker(context, isLeft: true),
              ),
            ),
            SizedBox(width: 10.w),
            Expanded(
              child: AppTextField(
                controller: _jaegarRightCtrl,
                readOnly: true,
                label: const Text('Right'),
                suffixIcon:
                    rightBlind ? null : const Icon(Icons.arrow_drop_down),
                onTap:
                    rightBlind
                        ? null
                        : () => _showJaegarPicker(context, isLeft: false),
              ),
            ),
          ],
        ),
      );
    });
  }

  void _showJaegarPicker(BuildContext context, {required bool isLeft}) {
    final current =
        isLeft ? controller.jaegarLeft.value : controller.jaegarRight.value;
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<VisualOption, String>(
            title:
                isLeft
                    ? 'Select Left Eye Jaegar Chart'
                    : 'Select Right Eye Jaegar Chart',
            items: VisualScreeningData.jaegarOptions,
            valueFor: (item) => item.name,
            labelFor: (item) => item.name,
            selectedValue: current.isEmpty ? null : current,
            height: 350.h,
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 16.h),
            onItemTap: (item) {
              Navigator.pop(context);
              if (isLeft) {
                controller.onJaegarLeftChanged(item.name);
              } else {
                controller.onJaegarRightChanged(item.name);
              }
            },
          ),
    );
  }

  // ── Overall Remarks card ──────────────────────────────────────────────────

  Widget _remarksCard() {
    return _card(
      title: 'Remarks',
      icon: Icons.comment_outlined,
      child: Row(
        children: [
          Expanded(
            child: AppTextField(
              controller: _leftRemarkCtrl,
              readOnly: true,
              label: const Text('Left Eye Remark'),
            ),
          ),
          SizedBox(width: 10.w),
          Expanded(
            child: AppTextField(
              controller: _rightRemarkCtrl,
              readOnly: true,
              label: const Text('Right Eye Remark'),
            ),
          ),
        ],
      ),
    );
  }

  // ── Wearing Spectacle card ────────────────────────────────────────────────

  Widget _spectaclesCard() {
    return _card(
      title: 'Wearing Spectacle ?',
      icon: Icons.remove_red_eye_outlined,
      child: Obx(
        () => Row(
          children: [
            CommonText(
              text: 'Wearing spectacle ?',
              fontSize: 13.sp,
              fontWeight: FontWeight.w500,
              textColor: kTextColor,
              textAlign: TextAlign.left,
            ),
            const Spacer(),
            _radioOption(
              label: 'Yes',
              selected: controller.wearsGlasses.value,
              onTap: () => controller.onWearsGlassesChanged(true),
            ),
            SizedBox(width: 16.w),
            _radioOption(
              label: 'No',
              selected: !controller.wearsGlasses.value,
              onTap: () => controller.onWearsGlassesChanged(false),
            ),
          ],
        ),
      ),
    );
  }

  Widget _radioOption({
    required String label,
    required bool selected,
    required VoidCallback onTap,
  }) {
    return GestureDetector(
      onTap: onTap,
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Container(
            width: 18.r,
            height: 18.r,
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
                        width: 9.r,
                        height: 9.r,
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          color: kPrimaryColor,
                        ),
                      ),
                    )
                    : null,
          ),
          SizedBox(width: 5.w),
          CommonText(
            text: label,
            fontSize: 13.sp,
            fontWeight: FontWeight.w500,
            textColor: kTextColor,
            textAlign: TextAlign.left,
          ),
        ],
      ),
    );
  }

  // ── Shared helpers ────────────────────────────────────────────────────────

  Widget _card({
    required String title,
    required IconData icon,
    required Widget child,
  }) {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(12.r),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 14.h),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [_sectionHeader(title, icon), SizedBox(height: 12.h), child],
      ),
    );
  }

  Widget _sectionHeader(String title, IconData icon) {
    return Row(
      children: [
        Container(
          width: 3.w,
          height: 18.h,
          decoration: BoxDecoration(
            color: kPrimaryColor,
            borderRadius: BorderRadius.circular(2.r),
          ),
        ),
        SizedBox(width: 8.w),
        Icon(icon, size: 15.r, color: kPrimaryColor),
        SizedBox(width: 5.w),
        Expanded(
          child: CommonText(
            text: title,
            fontSize: 13.sp,
            fontWeight: FontWeight.w700,
            textColor: kPrimaryColor,
            textAlign: TextAlign.left,
          ),
        ),
      ],
    );
  }
}
