// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/utilities/enums.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/app_active_button.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/common_widgets/drop_down_list_screen/drop_down_list_screen.dart';

import '../controller/d2d_team_controller.dart';

class D2DTeamFilterView extends StatelessWidget {
  const D2DTeamFilterView({super.key});

  @override
  Widget build(BuildContext context) {
    final controller = Get.find<D2DTeamController>();
    return GetBuilder<D2DTeamController>(
      builder: (_) {
        return Padding(
          padding: const EdgeInsets.fromLTRB(20, 12, 20, 12),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                width: MediaQuery.of(context).size.width,
                decoration: const BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.only(
                    topLeft: Radius.circular(20),
                    topRight: Radius.circular(20),
                  ),
                ),
                child: Center(
                  child: Text(
                    "Filters",
                    style: TextStyle(
                      fontWeight: FontWeight.normal,
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(18),
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 8),
              AppTextField(
                controller: TextEditingController(
                  text: controller.selectedDistrict?.dISTNAME ?? "",
                ),
                readOnly: true,
                onTap: () => _showDistrictPicker(context, controller),
                hint: 'District',
                label: CommonText(
                  text: 'District',
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
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icMapPin,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                suffixIcon: const Icon(Icons.keyboard_arrow_down),
              ),
              const SizedBox(height: 8),
              AppTextField(
                controller: TextEditingController(
                  text: controller.selectedLab?.labName ?? "",
                ),
                readOnly: true,
                onTap: () => _showLabPicker(context, controller),
                hint: 'Lab',
                label: CommonText(
                  text: 'Lab',
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
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icLandingLab,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                suffixIcon: const Icon(Icons.keyboard_arrow_down),
              ),
              const SizedBox(height: 28),
              Padding(
                padding: const EdgeInsets.fromLTRB(16, 4, 16, 20),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Expanded(
                      child: AppActiveButton(
                        buttontitle: "Back",
                        isCancel: true,
                        onTap: () => Navigator.pop(context),
                      ),
                    ),
                    const SizedBox(width: 16),
                    Expanded(
                      child: AppActiveButton(
                        buttontitle: "Apply",
                        onTap: () {
                          Navigator.pop(context);
                          controller.applyFilter();
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
  }

  void _showDistrictPicker(
    BuildContext context,
    D2DTeamController controller,
  ) async {
    final list = await controller.fetchDistrictList();
    if (list == null || !context.mounted) return;
    _showDropdown(
      context,
      "District",
      list,
      DropDownTypeMenu.BindDistrict,
      controller,
    );
  }

  void _showLabPicker(
    BuildContext context,
    D2DTeamController controller,
  ) async {
    final list = await controller.fetchLabList();
    if (list == null || !context.mounted) return;
    _showDropdown(
      context,
      "Select Lab",
      list,
      DropDownTypeMenu.TalukaPacketRecive,
      controller,
    );
  }

  void _showDropdown(
    BuildContext context,
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
    D2DTeamController controller,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext ctx) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(ctx).size.width * 1.33,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: DropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: (selected) {
              if (dropDownType == DropDownTypeMenu.BindDistrict) {
                controller.setSelectedDistrict(selected);
              } else if (dropDownType == DropDownTypeMenu.TalukaPacketRecive) {
                controller.setSelectedLab(selected);
              }
            },
          ),
        );
      },
    );
  }
}
