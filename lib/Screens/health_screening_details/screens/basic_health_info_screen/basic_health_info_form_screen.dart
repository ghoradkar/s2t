import 'dart:math' as math;

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/selection_bottom_sheet.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/basic_health_info_form_controller.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';
import 'package:s2toperational/Screens/health_screening_details/screens/basic_health_info_screen/ble_device_list_screen.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/glucose_device_controller.dart';
import 'package:s2toperational/Screens/health_screening_details/screens/basic_health_info_screen/glucose_device_screen.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/smart_scale_device_controller.dart';
import 'package:s2toperational/Screens/health_screening_details/screens/basic_health_info_screen/smart_scale_device_screen.dart';

class BasicHealthInfoFormScreen extends StatelessWidget {
  final int regdId;
  final int campTypeID;
  final UserAttendancesUsingSitedetailsIDOutput? patientItem;

  const BasicHealthInfoFormScreen({
    super.key,
    required this.regdId,
    required this.campTypeID,
    this.patientItem,
  });

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<BasicHealthInfoFormController>(
      init: BasicHealthInfoFormController(
        regdId: regdId,
        campTypeID: campTypeID,
        patientItem: patientItem,
      ),
      dispose: (_) => Get.delete<BasicHealthInfoFormController>(),
      builder:
          (ctrl) => NetworkWrapper(
            child: Scaffold(
              backgroundColor: kBackground,
              appBar: mAppBar(
                scTitle: 'Basic Health Information',
                leadingIcon: iconBackArrow,
                onLeadingIconClick: () => Navigator.pop(context),
              ),
              body: SafeArea(
                child: SingleChildScrollView(
                  padding: EdgeInsets.only(
                    left: 14.w,
                    right: 14.w,
                    bottom: MediaQuery.of(context).viewPadding.bottom + 24.h,
                  ),
                  child: Column(
                    children: [
                      SizedBox(height: 12.h),
                      _PatientDetailsSection(ctrl: ctrl),
                      _BasicHealthInfoSection(ctrl: ctrl),
                      _BloodSugarSection(ctrl: ctrl),
                      _BloodPressureSection(ctrl: ctrl),
                      _DeviceInfoSection(ctrl: ctrl),
                      SizedBox(height: 24.h),
                      SizedBox(
                        width: double.infinity,
                        child: AppActiveButton(
                          buttontitle: 'Save',
                          onTap: ctrl.onSave,
                        ),
                      ),
                      SizedBox(height: 20.h),
                    ],
                  ),
                ),
              ),
            ),
          ),
    );
  }
}

// ── Expandable Card ──────────────────────────────────────────────────────────

class _ExpandableCard extends StatefulWidget {
  final String title;
  final Widget content;
  final IconData? icon;

  const _ExpandableCard({
    required this.title,
    required this.content,
    this.icon,
  });

  @override
  State<_ExpandableCard> createState() => _ExpandableCardState();
}

class _ExpandableCardState extends State<_ExpandableCard>
    with SingleTickerProviderStateMixin {
  bool _expanded = true;
  late final AnimationController _animCtrl;
  late final Animation<double> _rotateAnim;

  @override
  void initState() {
    super.initState();
    _animCtrl = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 200),
      value: 1,
    );
    _rotateAnim = Tween<double>(
      begin: 0,
      end: 0.5,
    ).animate(CurvedAnimation(parent: _animCtrl, curve: Curves.easeInOut));
  }

  @override
  void dispose() {
    _animCtrl.dispose();
    super.dispose();
  }

  void _toggle() {
    setState(() => _expanded = !_expanded);
    _expanded ? _animCtrl.forward() : _animCtrl.reverse();
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.only(top: 12.h),
      child: Container(
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
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Header
            GestureDetector(
              onTap: _toggle,
              child: Container(
                width: double.infinity,
                padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 12.h),
                decoration: BoxDecoration(
                  color: kPrimaryColor,
                  borderRadius:
                      _expanded
                          ? BorderRadius.only(
                            topLeft: Radius.circular(12.r),
                            topRight: Radius.circular(12.r),
                          )
                          : BorderRadius.circular(12.r),
                ),
                child: Row(
                  children: [
                    if (widget.icon != null) ...[
                      Icon(
                        widget.icon,
                        color: kWhiteColor.withValues(alpha: 0.85),
                        size: 18.r,
                      ),
                      SizedBox(width: 8.w),
                    ],
                    Expanded(
                      child: Text(
                        widget.title,
                        style: TextStyle(
                          color: kWhiteColor,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w600,
                          fontSize: 14.sp,
                          letterSpacing: 0.3,
                        ),
                      ),
                    ),
                    AnimatedBuilder(
                      animation: _rotateAnim,
                      builder:
                          (_, child) => Transform.rotate(
                            angle: _rotateAnim.value * math.pi,
                            child: child,
                          ),
                      child: Icon(
                        Icons.keyboard_arrow_down,
                        color: kWhiteColor,
                        size: 22.r,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            // Content
            AnimatedCrossFade(
              duration: const Duration(milliseconds: 200),
              crossFadeState:
                  _expanded
                      ? CrossFadeState.showFirst
                      : CrossFadeState.showSecond,
              firstChild: Padding(
                padding: EdgeInsets.fromLTRB(14.w, 14.h, 14.w, 14.h),
                child: widget.content,
              ),
              secondChild: const SizedBox.shrink(),
            ),
          ],
        ),
      ),
    );
  }
}

// ── Shared helpers ───────────────────────────────────────────────────────────

Widget _label(String text) => RichText(
  text: TextSpan(
    text: text,
    style: TextStyle(
      color: kBlackColor,
      fontSize: 14 * 1.2,
      fontFamily: FontConstants.interFonts,
      fontWeight: FontWeight.w600,
    ),
  ),
);

Widget _fieldLabel(String text) => Padding(
  padding: EdgeInsets.only(bottom: 6.h),
  child: Text(
    text,
    style: TextStyle(
      fontFamily: FontConstants.interFonts,
      fontSize: 14.sp,
      fontWeight: FontWeight.w600,
      color: kTextColor,
    ),
  ),
);

Widget _gap([double? h]) => SizedBox(height: (h ?? 12).h);

// ── Chip-style Option Selector ───────────────────────────────────────────────

class _ChipSelector extends StatelessWidget {
  final String heading;
  final List<String> options;
  final int selectedIndex;
  final ValueChanged<int> onChanged;

  const _ChipSelector({
    required this.heading,
    required this.options,
    required this.selectedIndex,
    required this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        if (heading.isNotEmpty) _fieldLabel(heading),
        Wrap(
          spacing: 8.w,
          runSpacing: 6.h,
          children: List.generate(options.length, (i) {
            final selected = selectedIndex == i;
            return GestureDetector(
              onTap: () => onChanged(i),
              child: AnimatedContainer(
                duration: const Duration(milliseconds: 160),
                padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 7.h),
                decoration: BoxDecoration(
                  color: selected ? kPrimaryColor : kWhiteColor,
                  borderRadius: BorderRadius.circular(20.r),
                  border: Border.all(
                    color:
                        selected
                            ? kPrimaryColor
                            : kTextColor.withValues(alpha: 0.5),
                    width: selected ? 1.5 : 1,
                  ),
                  boxShadow:
                      selected
                          ? [
                            BoxShadow(
                              color: kPrimaryColor.withValues(alpha: 0.25),
                              blurRadius: 4,
                              offset: const Offset(0, 2),
                            ),
                          ]
                          : [],
                ),
                child: Text(
                  options[i],
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 14.sp,
                    fontWeight: selected ? FontWeight.w600 : FontWeight.w400,
                    color: selected ? kWhiteColor : kTextColor,
                  ),
                ),
              ),
            );
          }),
        ),
      ],
    );
  }
}

// ── Month / Year row (shown when habit is Yes) ───────────────────────────────

Widget _monthYearRow(
  TextEditingController monthCtrl,
  TextEditingController yearCtrl,
) {
  return Container(
    // padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
    decoration: BoxDecoration(
      // color: kPrimaryColor.withValues(alpha: 0.04),
      borderRadius: BorderRadius.circular(8.r),
      // border: Border.all(color: kPrimaryColor.withValues(alpha: 0.15)),
    ),
    child: Row(
      children: [
        Expanded(
          child: AppTextField(
            controller: monthCtrl,
            readOnly: false,
            onTap: () {},
            textInputType: TextInputType.number,
            inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            label: _label('Since Month'),
            prefixIcon: Icon(
              Icons.calendar_today_outlined,
              color: kPrimaryColor,
              size: 18.r,
            ).paddingOnly(left: 6.w),
          ),
        ),
        SizedBox(width: 10.w),
        Expanded(
          child: AppTextField(
            controller: yearCtrl,
            readOnly: false,
            onTap: () {},
            textInputType: TextInputType.number,
            inputFormatters: [FilteringTextInputFormatter.digitsOnly],
            label: _label('Since Year'),
            prefixIcon: Icon(
              Icons.calendar_month_outlined,
              color: kPrimaryColor,
              size: 18.r,
            ).paddingOnly(left: 6.w),
          ),
        ),
      ],
    ),
  );
}

// ── BT Search Button ─────────────────────────────────────────────────────────

Widget _btSearchButton({
  required String label,
  required VoidCallback onPressed,
}) {
  return SizedBox(
    width: double.infinity,
    child: OutlinedButton.icon(
      onPressed: onPressed,
      icon: Icon(Icons.bluetooth_searching, size: 17.r, color: kPrimaryColor),
      label: Text(
        label,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 14.sp,
          fontWeight: FontWeight.w600,
          color: kPrimaryColor,
        ),
      ),
      style: OutlinedButton.styleFrom(
        side: BorderSide(color: kPrimaryColor, width: 1.5),
        padding: EdgeInsets.symmetric(vertical: 10.h),
        backgroundColor: kPrimaryColor.withValues(alpha: 0.04),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8.r)),
      ),
    ),
  );
}

// ── Section divider inside card ──────────────────────────────────────────────

Widget _sectionDivider(String title) {
  return Padding(
    padding: EdgeInsets.only(bottom: 10.h, top: 4.h),
    child: Row(
      children: [
        Container(
          width: 3.w,
          height: 16.h,
          decoration: BoxDecoration(
            color: kPrimaryColor,
            borderRadius: BorderRadius.circular(2),
          ),
        ),
        SizedBox(width: 7.w),
        Text(
          title,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 14.sp,
            fontWeight: FontWeight.w700,
            color: kTextColor,
            letterSpacing: 0.2,
          ),
        ),
      ],
    ),
  );
}

// ── Section 1: Patient Details ────────────────────────────────────────────────

class _PatientDetailsSection extends StatelessWidget {
  final BasicHealthInfoFormController ctrl;

  const _PatientDetailsSection({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return _ExpandableCard(
      title: 'Patient Details',
      icon: Icons.person_outline,
      content: Column(
        children: [
          AppTextField(
            controller: ctrl.beneficiaryNameCtrl,
            readOnly: true,
            onTap: () {},
            label: _label('Beneficiary Name'),
            prefixIcon: Image.asset(
              icInitiatedBy,
              color: kPrimaryColor,
              width: 20.w,
              height: 20.h,
            ).paddingOnly(left: 6.w),
          ),
          _gap(),
          Row(
            children: [
              Expanded(
                child: AppTextField(
                  controller: ctrl.beneficiaryNoCtrl,
                  readOnly: true,
                  onTap: () {},
                  label: _label('Beneficiary No.'),
                  prefixIcon: Image.asset(
                    icHashIcon,
                    color: kPrimaryColor,
                    width: 20.w,
                    height: 20.h,
                  ).paddingOnly(left: 6.w),
                ),
              ),
              SizedBox(width: 10.w),
              Expanded(
                child: AppTextField(
                  controller: ctrl.mobileNoCtrl,
                  readOnly: true,
                  onTap: () {},
                  label: _label('Mobile No.'),
                  prefixIcon: Image.asset(
                    iconMobile,
                    color: kPrimaryColor,
                    width: 20.w,
                    height: 20.h,
                  ).paddingOnly(left: 6.w),
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

// ── Section 2: Basic Health Information ──────────────────────────────────────

class _BasicHealthInfoSection extends StatelessWidget {
  final BasicHealthInfoFormController ctrl;

  const _BasicHealthInfoSection({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return _ExpandableCard(
      title: 'Basic Health Information',
      icon: Icons.monitor_heart_outlined,
      content: GetBuilder<BasicHealthInfoFormController>(
        builder: (c) {
          final bool editable = !c.isLive;
          return Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // ── Vitals ──────────────────────────────────────
              _sectionDivider('Vitals'),
              Row(
                children: [
                  Expanded(
                    child: AppTextField(
                      controller: c.genderCtrl,
                      readOnly: true,
                      onTap: () {},
                      label: _label('Gender'),
                      prefixIcon: Image.asset(
                        icGenderIcon,
                        color: kPrimaryColor,
                        width: 20.w,
                        height: 20.h,
                      ).paddingOnly(left: 6.w),
                    ),
                  ),
                  SizedBox(width: 10.w),
                  Expanded(
                    child: AppTextField(
                      controller: c.ageCtrl,
                      readOnly: true,
                      onTap: () {},
                      label: _label('Age'),
                      prefixIcon: Image.asset(
                        icCalendarMonth,
                        color: kPrimaryColor,
                        width: 20.w,
                        height: 20.h,
                      ).paddingOnly(left: 6.w),
                    ),
                  ),
                ],
              ),
              _gap(),

              AppTextField(
                controller: TextEditingController(
                  text: c.selectedBloodGroup ?? '',
                ),
                readOnly: true,
                onTap: () => _showBloodGroupPicker(context, c),
                label: _label('Select Blood Group'),
                prefixIcon: Image.asset(
                  icBloodGroup,
                  color: kPrimaryColor,
                  width: 20.w,
                  height: 20.h,
                ).paddingOnly(left: 6.w),
                suffixIcon: Icon(
                  Icons.keyboard_arrow_down,
                  color: kLabelTextColor,
                  size: 20.r,
                ),
              ),
              _gap(),

              Row(
                children: [
                  Expanded(
                    child: AppTextField(
                      controller: c.heightCtrl,
                      readOnly: !editable,
                      onTap: () {},
                      onChange: (_) => c.recalculateBMI(),
                      textInputType: const TextInputType.numberWithOptions(
                        decimal: true,
                      ),
                      inputFormatters: [
                        FilteringTextInputFormatter.allow(
                          RegExp(r'^\d*\.?\d*'),
                        ),
                      ],
                      label: _label('Height (cms)'),
                      prefixIcon: Image.asset(
                        icHeightIcon,
                        color: kPrimaryColor,
                        width: 20.w,
                        height: 20.h,
                      ).paddingOnly(left: 6.w),
                    ),
                  ),
                  SizedBox(width: 10.w),
                  Expanded(
                    child: AppTextField(
                      controller: c.weightCtrl,
                      readOnly: !editable,
                      onTap: () {},
                      onChange: (_) => c.recalculateBMI(),
                      textInputType: const TextInputType.numberWithOptions(
                        decimal: true,
                      ),
                      inputFormatters: [
                        FilteringTextInputFormatter.allow(
                          RegExp(r'^\d*\.?\d*'),
                        ),
                      ],
                      label: _label('Weight (kgs)'),
                      prefixIcon: Image.asset(
                        icWeightIcon,
                        color: kPrimaryColor,
                        width: 20.w,
                        height: 20.h,
                      ).paddingOnly(left: 6.w),
                    ),
                  ),
                ],
              ),
              _gap(8),

              _btSearchButton(
                label: 'Search Weight Machine',
                onPressed: () async {
                  if (c.heightCtrl.text.trim().isEmpty) {
                    ToastManager.toast('Please enter height');
                    return;
                  }
                  if (!await c.isBluetoothOn()) {
                    ToastManager.toast('Please enable Bluetooth first');
                    return;
                  }
                  final result = await Navigator.push<SmartScaleResult?>(
                    context,
                    MaterialPageRoute(
                      builder:
                          (_) => SmartScaleDeviceScreen(
                            heightCm:
                                double.tryParse(c.heightCtrl.text.trim()) ?? 0,
                            patientItem: c.patientItem,
                          ),
                    ),
                  );
                  if (result != null && context.mounted) {
                    c.applyWeightData(
                      weight: result.weight,
                      bmi: result.bmi,
                      deviceNameStr: result.deviceNameStr,
                    );
                  }
                },
              ),
              _gap(),

              Row(
                children: [
                  Expanded(
                    child: AppTextField(
                      controller: c.bmiCtrl,
                      readOnly: true,
                      onTap: () {},
                      label: _label('BMI'),
                      prefixIcon: Image.asset(
                        icBMI,
                        color: kPrimaryColor,
                        width: 20.w,
                        height: 20.h,
                      ).paddingOnly(left: 6.w),
                    ),
                  ),
                  SizedBox(width: 10.w),
                  Expanded(child: _BmiStatusBadge(index: c.bmiStatusIndex)),
                ],
              ),
              _gap(),

              _ChipSelector(
                heading: 'BMI Status',
                options: const ['Underweight', 'Normal', 'Overweight'],
                selectedIndex: c.bmiStatusIndex,
                onChanged: (i) {
                  c.bmiStatusIndex = i;
                  c.update();
                },
              ),
              _gap(14),

              // ── Fasting ──────────────────────────────────────
              _sectionDivider('Fasting'),
              _ChipSelector(
                heading: 'Fasting Duration',
                options: const ['< 12 hrs', '> 12 hrs'],
                selectedIndex: c.fastingIndex,
                onChanged: (i) {
                  c.fastingIndex = i;
                  if (i != 0) c.fastingHrsInputCtrl.clear();
                  c.update();
                },
              ),
              if (c.fastingIndex == 0) ...[
                _gap(8),
                AppTextField(
                  controller: c.fastingHrsInputCtrl,
                  readOnly: false,
                  onTap: () {},
                  textInputType: const TextInputType.numberWithOptions(
                    decimal: true,
                  ),
                  inputFormatters: [
                    FilteringTextInputFormatter.allow(RegExp(r'^\d*\.?\d*')),
                  ],
                  label: _label('Enter Hours'),
                  prefixIcon: Icon(
                    Icons.access_time_outlined,
                    color: kPrimaryColor,
                    size: 18.r,
                  ).paddingOnly(left: 6.w),
                ),
              ],
              _gap(14),

              // ── Personal ──────────────────────────────────────
              _sectionDivider('Personal'),
              _ChipSelector(
                heading: 'Marital Status',
                options: const ['Married', 'Unmarried'],
                selectedIndex: c.maritalStatusIndex,
                onChanged: (i) {
                  c.maritalStatusIndex = i;
                  if (i != 0) c.childrenCtrl.clear();
                  c.update();
                },
              ),
              if (c.maritalStatusIndex == 0) ...[
                _gap(8),
                AppTextField(
                  controller: c.childrenCtrl,
                  readOnly: false,
                  onTap: () {},
                  textInputType: TextInputType.number,
                  inputFormatters: [FilteringTextInputFormatter.digitsOnly],
                  label: _label('Number of Children (if any)'),
                  prefixIcon: Image.asset(
                    iconPerson,
                    color: kPrimaryColor,
                    width: 20.w,
                    height: 20.h,
                  ).paddingOnly(left: 6.w),
                ),
              ],
              _gap(10),
              _ChipSelector(
                heading: 'Family Planning Operation',
                options: const ['No', 'Yes'],
                selectedIndex: c.familyPlanningIndex,
                onChanged: (i) {
                  c.familyPlanningIndex = i;
                  c.update();
                },
              ),
              _gap(14),

              // ── Habits ──────────────────────────────────────
              _sectionDivider('Habits'),
              _HabitRow(
                label: 'Smoking',
                selectedIndex: c.smokingIndex,
                onChanged: (i) {
                  c.smokingIndex = i;
                  if (i != 1) {
                    c.smokingMonthCtrl.clear();
                    c.smokingYearCtrl.clear();
                  }
                  c.update();
                },
                showSince: c.smokingIndex == 1,
                monthCtrl: c.smokingMonthCtrl,
                yearCtrl: c.smokingYearCtrl,
              ),
              _gap(10),
              _HabitRow(
                label: 'Alcohol',
                selectedIndex: c.alcoholIndex,
                onChanged: (i) {
                  c.alcoholIndex = i;
                  if (i != 1) {
                    c.alcoholMonthCtrl.clear();
                    c.alcoholYearCtrl.clear();
                  }
                  c.update();
                },
                showSince: c.alcoholIndex == 1,
                monthCtrl: c.alcoholMonthCtrl,
                yearCtrl: c.alcoholYearCtrl,
              ),
              _gap(10),
              _HabitRow(
                label: 'Tobacco',
                selectedIndex: c.tobaccoIndex,
                onChanged: (i) {
                  c.tobaccoIndex = i;
                  if (i != 1) {
                    c.tobaccoMonthCtrl.clear();
                    c.tobaccoYearCtrl.clear();
                  }
                  c.update();
                },
                showSince: c.tobaccoIndex == 1,
                monthCtrl: c.tobaccoMonthCtrl,
                yearCtrl: c.tobaccoYearCtrl,
              ),
              _gap(10),
              _HabitRow(
                label: 'Drugs',
                selectedIndex: c.drugsIndex,
                onChanged: (i) {
                  c.drugsIndex = i;
                  if (i != 1) {
                    c.drugsMonthCtrl.clear();
                    c.drugsYearCtrl.clear();
                  }
                  c.update();
                },
                showSince: c.drugsIndex == 1,
                monthCtrl: c.drugsMonthCtrl,
                yearCtrl: c.drugsYearCtrl,
              ),
            ],
          );
        },
      ),
    );
  }

  void _showBloodGroupPicker(
    BuildContext context,
    BasicHealthInfoFormController c,
  ) {
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => SelectionBottomSheet<String, String>(
            title: 'Select Blood Group',
            items: BasicHealthInfoFormController.bloodGroups,
            valueFor: (item) => item,
            labelFor: (item) => item,
            selectedValue: c.selectedBloodGroup,
            height: 420.h,
            padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 16.h),
            onItemTap: (item) {
              c.selectedBloodGroup = item;
              c.update();
              Navigator.pop(context);
            },
          ),
    );
  }
}

// ── BMI Status Badge ─────────────────────────────────────────────────────────

class _BmiStatusBadge extends StatelessWidget {
  final int index;

  const _BmiStatusBadge({required this.index});

  @override
  Widget build(BuildContext context) {
    const labels = ['Underweight', 'Normal', 'Overweight'];
    final colors = [
      Colors.orange.shade600,
      Colors.green.shade600,
      Colors.red.shade500,
    ];
    final bgColors = [
      Colors.orange.shade50,
      Colors.green.shade50,
      Colors.red.shade50,
    ];
    final icons = [
      Icons.arrow_downward,
      Icons.check_circle_outline,
      Icons.arrow_upward,
    ];

    if (index < 0 || index >= labels.length) return const SizedBox.shrink();

    return Container(
      padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
      decoration: BoxDecoration(
        color: bgColors[index],
        borderRadius: BorderRadius.circular(8.r),
        border: Border.all(color: colors[index].withValues(alpha: 0.4)),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Icon(icons[index], color: colors[index], size: 16.r),
          SizedBox(width: 5.w),
          Text(
            labels[index],
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 12.sp,
              fontWeight: FontWeight.w700,
              color: colors[index],
            ),
          ),
        ],
      ),
    );
  }
}

// ── Habit Row ────────────────────────────────────────────────────────────────

class _HabitRow extends StatelessWidget {
  final String label;
  final int selectedIndex;
  final ValueChanged<int> onChanged;
  final bool showSince;
  final TextEditingController monthCtrl;
  final TextEditingController yearCtrl;

  const _HabitRow({
    required this.label,
    required this.selectedIndex,
    required this.onChanged,
    required this.showSince,
    required this.monthCtrl,
    required this.yearCtrl,
  });

  @override
  Widget build(BuildContext context) {
    final isNo = selectedIndex == 0;
    return AnimatedContainer(
      duration: const Duration(milliseconds: 200),
      padding: EdgeInsets.symmetric(horizontal: 0.w, vertical: 10.h),
      decoration: BoxDecoration(
        color: isNo ? Colors.grey.shade100 : Colors.transparent,
        borderRadius: BorderRadius.circular(8.r),
        // border: Border.all(color: kTextColor.withValues(alpha: 0.1))
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Expanded(
                child: Text(
                  label,
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w600,
                    color: isNo ? kLabelTextColor : kTextColor,
                  ),
                ).paddingOnly(left: 6.w),
              ),
              _CompactChipSelector(
                options: const ['No', 'Yes'],
                selectedIndex: selectedIndex,
                onChanged: onChanged,
              ),
            ],
          ),
          if (showSince) ...[_gap(8), _monthYearRow(monthCtrl, yearCtrl)],
        ],
      ),
    );
  }
}

// ── Compact inline chip selector (for habit rows) ────────────────────────────

class _CompactChipSelector extends StatelessWidget {
  final List<String> options;
  final int selectedIndex;
  final ValueChanged<int> onChanged;

  const _CompactChipSelector({
    required this.options,
    required this.selectedIndex,
    required this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(20.r),
        border: Border.all(color: kTextColor.withValues(alpha: 0.2)),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: List.generate(options.length, (i) {
          final selected = selectedIndex == i;
          return GestureDetector(
            onTap: () => onChanged(i),
            child: AnimatedContainer(
              duration: const Duration(milliseconds: 150),
              padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 6.h),
              decoration: BoxDecoration(
                color: selected ? kPrimaryColor : Colors.transparent,
                borderRadius: BorderRadius.circular(20.r),
              ),
              child: Text(
                options[i],
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 12.sp,
                  fontWeight: selected ? FontWeight.w700 : FontWeight.w400,
                  color: selected ? kWhiteColor : kTextColor,
                ),
              ),
            ),
          );
        }),
      ),
    ).paddingOnly(right: 6.w);
  }
}

// ── Section 3: Blood Sugar ────────────────────────────────────────────────────

class _BloodSugarSection extends StatelessWidget {
  final BasicHealthInfoFormController ctrl;

  const _BloodSugarSection({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    final bool editable = !ctrl.isLive;
    return _ExpandableCard(
      title: 'Blood Sugar',
      icon: Icons.water_drop_outlined,
      content: Column(
        children: [
          AppTextField(
            controller: ctrl.bloodSugarRCtrl,
            readOnly: !editable,
            onTap: () {},
            textInputType: const TextInputType.numberWithOptions(decimal: true),
            inputFormatters: [
              FilteringTextInputFormatter.allow(RegExp(r'^\d*\.?\d*')),
            ],
            label: _label('Blood Sugar (R) mg/dL'),
            prefixIcon: Image.asset(
              icBloodGroup,
              color: kPrimaryColor,
              width: 20.w,
              height: 20.h,
            ).paddingOnly(left: 6.w),
          ),
          _gap(10),
          _btSearchButton(
            label: 'Search Sugar Device',
            onPressed: () async {
              if (!await ctrl.isBluetoothOn()) {
                ToastManager.toast('Please enable Bluetooth first');
                return;
              }
              final result = await Navigator.push<GlucoseResult?>(
                context,
                MaterialPageRoute(
                  builder: (_) => GlucoseDeviceScreen(
                    patientItem: ctrl.patientItem,
                  ),
                ),
              );
              if (result != null && context.mounted) {
                ctrl.applySugarData(
                  glucose: result.glucose,
                  deviceNameStr: result.deviceNameStr,
                );
              }
            },
          ),
        ],
      ),
    );
  }
}

// ── Section 4: Blood Pressure ─────────────────────────────────────────────────

class _BloodPressureSection extends StatelessWidget {
  final BasicHealthInfoFormController ctrl;

  const _BloodPressureSection({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    final bool editable = !ctrl.isLive;
    return _ExpandableCard(
      title: 'Blood Pressure',
      icon: Icons.favorite_outline,
      content: Row(
        children: [
          Expanded(
            child: AppTextField(
              controller: ctrl.systolicCtrl,
              readOnly: !editable,
              onTap: () {},
              textInputType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              label: _label('Systolic (mmHg)'),
              prefixIcon: Image.asset(
                icBloodGroup,
                color: kPrimaryColor,
                width: 20.w,
                height: 20.h,
              ).paddingOnly(left: 6.w),
            ),
          ),
          SizedBox(width: 10.w),
          Expanded(
            child: AppTextField(
              controller: ctrl.diastolicCtrl,
              readOnly: !editable,
              onTap: () {},
              textInputType: TextInputType.number,
              inputFormatters: [FilteringTextInputFormatter.digitsOnly],
              label: _label('Diastolic (mmHg)'),
              prefixIcon: Image.asset(
                icBloodGroup,
                color: kPrimaryColor,
                width: 20.w,
                height: 20.h,
              ).paddingOnly(left: 6.w),
            ),
          ),
        ],
      ).paddingOnly(top: 4.h),
    );
  }
}

// ── Section 5: Device Information ─────────────────────────────────────────────

class _DeviceInfoSection extends StatelessWidget {
  final BasicHealthInfoFormController ctrl;

  const _DeviceInfoSection({required this.ctrl});

  @override
  Widget build(BuildContext context) {
    return _ExpandableCard(
      title: 'Device Information',
      icon: Icons.devices_outlined,
      content: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Status / Error chips
          Obx(() {
            final status = ctrl.deviceStatus.value;
            final error = ctrl.deviceError.value;
            final connected =
                status.contains('Connected') || status.contains(':');
            return Column(
              children: [
                _DeviceStatusTile(
                  icon:
                      connected
                          ? Icons.bluetooth_connected
                          : Icons.bluetooth_disabled,
                  label: 'Status',
                  value: status.isEmpty ? 'No devices connected' : status,
                  color: connected ? Colors.green.shade700 : kLabelTextColor,
                  bgColor: connected ? Colors.green.shade50 : kBackground,
                ),
                if (error.isNotEmpty) ...[
                  _gap(6),
                  _DeviceStatusTile(
                    icon: Icons.error_outline,
                    label: 'Error',
                    value: error,
                    color: Colors.red.shade700,
                    bgColor: Colors.red.shade50,
                  ),
                ],
              ],
            );
          }),
          _gap(14),

          // SCAN + TRANSFER buttons
          Row(
            children: [
              Expanded(
                child: OutlinedButton.icon(
                  onPressed: () async {
                    if (!await ctrl.isBluetoothOn()) {
                      ToastManager.toast('Please enable Bluetooth first');
                      return;
                    }
                    if (!context.mounted) return;
                    final device = await Navigator.push<BluetoothDevice?>(
                      context,
                      MaterialPageRoute(
                        builder:
                            (_) => const BleDeviceListScreen(
                              title: 'Device Pairing',
                            ),
                      ),
                    );
                    if (device != null && context.mounted) {
                      await ctrl.connectGeneralDevice(device);
                    }
                  },
                  icon: Icon(
                    Icons.bluetooth_searching,
                    size: 17.r,
                    color: kPrimaryColor,
                  ),
                  label: Text(
                    'SCAN',
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: 14.sp,
                      fontWeight: FontWeight.w700,
                      color: kPrimaryColor,
                    ),
                  ),
                  style: OutlinedButton.styleFrom(
                    side: BorderSide(color: kPrimaryColor, width: 1.5),
                    padding: EdgeInsets.symmetric(vertical: 11.h),
                    backgroundColor: kPrimaryColor.withValues(alpha: 0.04),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(8.r),
                    ),
                  ),
                ),
              ),
              SizedBox(width: 10.w),
              Expanded(
                child: Obx(
                  () => ElevatedButton.icon(
                    onPressed:
                        ctrl.isTransferring.value ? null : ctrl.transferData,
                    icon:
                        ctrl.isTransferring.value
                            ? SizedBox(
                              width: 15.r,
                              height: 15.r,
                              child: const CircularProgressIndicator(
                                strokeWidth: 2,
                                color: Colors.white,
                              ),
                            )
                            : Icon(Icons.sync, size: 17.r),
                    label: Text(
                      ctrl.isTransferring.value
                          ? 'Transferring...'
                          : 'TRANSFER',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: 14.sp,
                        fontWeight: FontWeight.w700,
                      ),
                    ),
                    style: ElevatedButton.styleFrom(
                      backgroundColor: kPrimaryColor,
                      foregroundColor: kWhiteColor,
                      padding: EdgeInsets.symmetric(vertical: 11.h),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8.r),
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),

          // if (!ctrl.isLive) ...[
          //   _gap(10),
          //   Container(
          //     width: double.infinity,
          //     padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 8.h),
          //     decoration: BoxDecoration(
          //       color: Colors.amber.shade50,
          //       borderRadius: BorderRadius.circular(6.r),
          //       border: Border.all(color: Colors.amber.shade300),
          //     ),
          //     child: Row(
          //       children: [
          //         Icon(
          //           Icons.info_outline,
          //           size: 14.r,
          //           color: Colors.amber.shade800,
          //         ),
          //         SizedBox(width: 6.w),
          //         Expanded(
          //           child: Text(
          //             'Beta mode: manual entry allowed without device.',
          //             style: TextStyle(
          //               fontFamily: FontConstants.interFonts,
          //               fontSize: 11.sp,
          //               color: Colors.amber.shade800,
          //             ),
          //           ),
          //         ),
          //       ],
          //     ),
          //   ),
          // ],
        ],
      ),
    );
  }
}

// ── Device Status Tile ────────────────────────────────────────────────────────

class _DeviceStatusTile extends StatelessWidget {
  final IconData icon;
  final String label;
  final String value;
  final Color color;
  final Color bgColor;

  const _DeviceStatusTile({
    required this.icon,
    required this.label,
    required this.value,
    required this.color,
    required this.bgColor,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 9.h),
      decoration: BoxDecoration(
        color: bgColor,
        borderRadius: BorderRadius.circular(8.r),
        border: Border.all(color: color.withValues(alpha: 0.3)),
      ),
      child: Row(
        children: [
          Icon(icon, size: 16.r, color: color),
          SizedBox(width: 8.w),
          Text(
            '$label: ',
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 12.sp,
              fontWeight: FontWeight.w600,
              color: kTextColor,
            ),
          ),
          Expanded(
            child: Text(
              value,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 12.sp,
                color: color,
                fontWeight: FontWeight.w500,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
