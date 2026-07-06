import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/AppTextField.dart';
import 'package:s2toperational/Modules/common_widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/widgets/selection_bottom_sheet.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/controller/call_to_doctor_controller.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/d2d_camp_mapped_doctor_list_response.dart';

class CallToDoctorScreen extends StatelessWidget {
  final int regdId;
  final int campId;
  final String healthScreentype;

  const CallToDoctorScreen({
    super.key,
    required this.regdId,
    required this.campId,
    required this.healthScreentype,
  });

  // ── Color info dialog ─────────────────────────────────────────────────────

  void _showInfoDialog(BuildContext context) {
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: Text(
          "Color Indicators",
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w600,
            fontSize: 16.sp,
          ),
        ),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            _colorInfoRow(color: const Color(0xffC8E6C9), label: "Doctor Assigned / Physical Exam Done"),
            _colorInfoRow(color: Colors.white, label: "Pending / Not Assigned"),
          ],
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text("OK"),
          ),
        ],
      ),
    );
  }

  Widget _colorInfoRow({required Color color, required String label}) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 6.h),
      child: Row(
        children: [
          Container(
            width: 24.w,
            height: 24.h,
            decoration: BoxDecoration(
              color: color,
              border: Border.all(color: Colors.grey.shade400),
              borderRadius: BorderRadius.circular(4),
            ),
          ),
          SizedBox(width: 12.w),
          Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 13.sp,
              color: kTextColor,
            ),
          ),
        ],
      ),
    );
  }

  // ── Row background ────────────────────────────────────────────────────────

  Color _rowColor(String? peStatus, String? doctorMappedStatus) {
    if (peStatus == "1" || doctorMappedStatus == "1") return const Color(0xffC8E6C9); // green : assigned / PE done
    return Colors.white;                                                                // white : not yet assigned
  }

  // ── Doctor bottom sheet ───────────────────────────────────────────────────

  void _showDoctorPicker(
    BuildContext context,
    CallToDoctorController ctrl,
  ) {
    ctrl.fetchDoctorList(
      onLoaded: () {
        showModalBottomSheet(
          context: context,
          isScrollControlled: true,
          shape: const RoundedRectangleBorder(
            borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
          ),
          builder: (_) => SelectionBottomSheet<D2DCampMappedDoctorOutput, int>(
            title: "Choose Doctor",
            items: ctrl.doctorList,
            valueFor: (item) => item.userId ?? 0,
            labelFor: (item) => item.fullName ?? "",
            selectedValue: ctrl.selectedDoctor?.userId,
            height: 460.h,
            padding: EdgeInsets.fromLTRB(16.w, 16.h, 16.w, 24.h),
            showSearch: true,
            onItemTap: (item) {
              ctrl.selectDoctor(item);
              Navigator.pop(context);
            },
            itemBuilderWithIndex: (ctx, item, isSelected, index, total) {
              return Container(
                padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 12.h),
                decoration: BoxDecoration(
                  color: isSelected
                      ? kPrimaryColor.withValues(alpha: 0.08)
                      : Colors.transparent,
                  border: Border(
                    bottom: BorderSide(
                      color: index < total - 1
                          ? Colors.grey.shade200
                          : Colors.transparent,
                    ),
                  ),
                ),
                child: Row(
                  children: [
                    Expanded(
                      child: Text(
                        item.fullName ?? "",
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: 14.sp,
                          fontWeight: isSelected
                              ? FontWeight.w600
                              : FontWeight.w400,
                          color: isSelected ? kPrimaryColor : kTextColor,
                        ),
                      ),
                    ),
                    if (isSelected)
                      Icon(
                        Icons.check_circle_rounded,
                        color: kPrimaryColor,
                        size: 18.sp,
                      ),
                  ],
                ),
              );
            },
          ),
        );
      },
    );
  }

  // ── Call type bottom sheet ────────────────────────────────────────────────

  void _showCallTypePicker(BuildContext context, CallToDoctorController ctrl) {
    final options = [
      {'label': 'Audio', 'value': 1},
      {'label': 'Video', 'value': 2},
    ];
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder: (_) =>
          SelectionBottomSheet<Map<String, dynamic>, int>(
            title: "Call Type",
            items: options,
            valueFor: (item) => item['value'] as int,
            labelFor: (item) => item['label'] as String,
            selectedValue: ctrl.callType == 0 ? null : ctrl.callType,
            height: 220.h,
            padding: EdgeInsets.fromLTRB(16.w, 16.h, 16.w, 24.h),
            onItemTap: (item) {
              ctrl.selectCallType(item['value'] as int);
              Navigator.pop(context);
            },
            itemBuilderWithIndex: (ctx, item, isSelected, index, total) {
              return Container(
                padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 14.h),
                decoration: BoxDecoration(
                  color: isSelected
                      ? kPrimaryColor.withValues(alpha: 0.08)
                      : Colors.transparent,
                  border: Border(
                    bottom: BorderSide(
                      color: index < total - 1
                          ? Colors.grey.shade200
                          : Colors.transparent,
                    ),
                  ),
                ),
                child: Row(
                  children: [
                    Icon(
                      item['value'] == 1
                          ? Icons.mic_rounded
                          : Icons.videocam_rounded,
                      color: isSelected ? kPrimaryColor : dropDownTitleHeader,
                      size: 20.sp,
                    ),
                    SizedBox(width: 12.w),
                    Expanded(
                      child: Text(
                        item['label'] as String,
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: 14.sp,
                          fontWeight: isSelected
                              ? FontWeight.w600
                              : FontWeight.w400,
                          color: isSelected ? kPrimaryColor : kTextColor,
                        ),
                      ),
                    ),
                    if (isSelected)
                      Icon(
                        Icons.check_circle_rounded,
                        color: kPrimaryColor,
                        size: 18.sp,
                      ),
                  ],
                ),
              );
            },
          ),
    );
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<CallToDoctorController>(
      init: CallToDoctorController(
        regdId: regdId,
        campId: campId,
        healthScreentype: healthScreentype,
      ),
      dispose: (_) => Get.delete<CallToDoctorController>(),
      builder: (ctrl) {
        return NetworkWrapper(
          child: Scaffold(
            appBar: mAppBar(
              scTitle: "Call To Doctor",
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () => Navigator.pop(context),
              showActions: true,
              actions: [
                IconButton(
                  icon: const Icon(Icons.info_outline, color: kWhiteColor),
                  onPressed: () => _showInfoDialog(context),
                ),
              ],
            ),
            body: ctrl.isLoading
                ? const CommonSkeletonPatientList()
                : SingleChildScrollView(
                    padding: EdgeInsets.fromLTRB(16.w, 12.h, 16.w, 24.h),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        // ── Table ───────────────────────────────────────────
                        if (ctrl.beneficiaryList.isEmpty)
                          Padding(
                            padding: EdgeInsets.symmetric(vertical: 24.h),
                            child: Center(
                              child: Text(
                                "No beneficiaries found",
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  fontSize: 14.sp,
                                  color: dropDownTitleHeader,
                                ),
                              ),
                            ),
                          )
                        else ...[
                          _TableHeader(),
                          ListView.builder(
                            shrinkWrap: true,
                            physics: const NeverScrollableScrollPhysics(),
                            itemCount: ctrl.beneficiaryList.length,
                            itemBuilder: (context, index) {
                              final obj = ctrl.beneficiaryList[index];
                              final isGreen = obj.peStatus == "1" || obj.doctorMappedStatus == "1";
                              return _BeneficiaryRow(
                                index: index,
                                name: obj.englishName ?? "",
                                type: obj.type ?? "W",
                                assignedDoctor: obj.doctorName ?? "-",
                                isChecked: ctrl.checkedMap[index] ?? false,
                                bgColor: _rowColor(
                                  obj.peStatus,
                                  obj.doctorMappedStatus,
                                ),
                                showCheckbox: !isGreen,
                                onToggle: isGreen ? null : () => ctrl.toggleCheck(index),
                              );
                            },
                          ),
                        ],

                        SizedBox(height: 20.h),
                        const Divider(height: 1),
                        SizedBox(height: 16.h),

                        // ── Choose Doctor ───────────────────────────────────
                        Text(
                          "Choose Doctor",
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontSize: 13.sp,
                            fontWeight: FontWeight.w600,
                            color: kTextColor,
                          ),
                        ),
                        SizedBox(height: 6.h),
                        AppTextField(
                          controller: ctrl.doctorTextCtrl,
                          hint: "Tap to choose doctor",
                          hintStyle: TextStyle(
                            fontSize: 14.sp,
                            fontFamily: FontConstants.interFonts,
                            color: dropDownTitleHeader,
                          ),
                          fieldRadius: 10,
                          readOnly: true,
                          onTap: () => _showDoctorPicker(context, ctrl),
                          suffixIcon: Icon(
                            Icons.keyboard_arrow_down_rounded,
                            color: dropDownTitleHeader,
                            size: 20.sp,
                          ),
                        ),

                        SizedBox(height: 14.h),

                        // ── Call Type ───────────────────────────────────────
                        Text(
                          "Call Type",
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontSize: 13.sp,
                            fontWeight: FontWeight.w600,
                            color: kTextColor,
                          ),
                        ),
                        SizedBox(height: 6.h),
                        AppTextField(
                          controller: ctrl.callTypeTextCtrl,
                          hint: "Tap to select call type",
                          hintStyle: TextStyle(
                            fontSize: 14.sp,
                            fontFamily: FontConstants.interFonts,
                            color: dropDownTitleHeader,
                          ),
                          fieldRadius: 10,
                          readOnly: true,
                          onTap: () => _showCallTypePicker(context, ctrl),
                          suffixIcon: Icon(
                            Icons.keyboard_arrow_down_rounded,
                            color: dropDownTitleHeader,
                            size: 20.sp,
                          ),
                        ),

                        SizedBox(height: 20.h),

                        // ── Assign Doctor button ────────────────────────────
                        SizedBox(
                          width: double.infinity,
                          height: 46.h,
                          child: ElevatedButton(
                            onPressed: ctrl.isSubmitting
                                ? null
                                : () => ctrl.submitAssignment(context),
                            style: ElevatedButton.styleFrom(
                              backgroundColor: kPrimaryColor,
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(10),
                              ),
                            ),
                            child: ctrl.isSubmitting
                                ? const SizedBox(
                                    width: 22,
                                    height: 22,
                                    child: CircularProgressIndicator(
                                      color: kWhiteColor,
                                      strokeWidth: 2.5,
                                    ),
                                  )
                                : Text(
                                    "Assign Doctor",
                                    style: TextStyle(
                                      fontFamily: FontConstants.interFonts,
                                      fontSize: 15.sp,
                                      fontWeight: FontWeight.w600,
                                      color: kWhiteColor,
                                    ),
                                  ),
                          ),
                        ),
                      ],
                    ),
                  ),
          ),
        );
      },
    );
  }
}

// ── Table header ──────────────────────────────────────────────────────────────

class _TableHeader extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: kPrimaryColor.withValues(alpha: 0.1),
        borderRadius: const BorderRadius.vertical(top: Radius.circular(8)),
      ),
      padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 10.h),
      child: Row(
        children: [
          SizedBox(width: 36.w), // checkbox column
          Expanded(
            flex: 3,
            child: Text(
              "Name",
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w700,
                fontSize: 13.sp,
                color: kPrimaryColor,
              ),
            ),
          ),
          SizedBox(
            width: 46.w,
            child: Text(
              "Type",
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w700,
                fontSize: 13.sp,
                color: kPrimaryColor,
              ),
            ),
          ),
          Expanded(
            flex: 3,
            child: Text(
              "Assigned Doctor",
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w700,
                fontSize: 13.sp,
                color: kPrimaryColor,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

// ── Beneficiary row ───────────────────────────────────────────────────────────

class _BeneficiaryRow extends StatelessWidget {
  final int index;
  final String name;
  final String type;
  final String assignedDoctor;
  final bool isChecked;
  final Color bgColor;
  final bool showCheckbox;
  final VoidCallback? onToggle;

  const _BeneficiaryRow({
    required this.index,
    required this.name,
    required this.type,
    required this.assignedDoctor,
    required this.isChecked,
    required this.bgColor,
    required this.showCheckbox,
    required this.onToggle,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onToggle,
      child: Container(
        color: bgColor,
        padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 10.h),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SizedBox(
              width: 36.w,
              child: showCheckbox
                  ? Checkbox(
                      value: isChecked,
                      activeColor: kPrimaryColor,
                      onChanged: (_) => onToggle?.call(),
                      materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
                      visualDensity: const VisualDensity(
                        horizontal: -4,
                        vertical: -4,
                      ),
                    )
                  : const SizedBox.shrink(),
            ),
            Expanded(
              flex: 3,
              child: Text(
                name.toUpperCase(),
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w500,
                  color: kTextColor,
                ),
              ),
            ),
            SizedBox(
              width: 46.w,
              child: Text(
                type,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  color: dropDownTitleHeader,
                ),
              ),
            ),
            Expanded(
              flex: 3,
              child: Text(
                assignedDoctor,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  color: dropDownTitleHeader,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
