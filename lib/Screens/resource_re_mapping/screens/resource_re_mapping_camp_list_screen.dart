import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import '../controllers/resource_re_mapping_controller.dart';
import '../models/ResourceReMappingCampResponse.dart';
import '../widgets/resource_re_mapping_camp_list_row.dart';

class ResourceReMappingCampListScreen extends StatelessWidget {
  const ResourceReMappingCampListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<ResourceReMappingController>(
      init: ResourceReMappingController(),
      dispose: (_) => Get.delete<ResourceReMappingController>(),
      builder: (ctrl) {
        return KeyboardDismissOnTap(
          child: Scaffold(
            appBar: mAppBar(
              scTitle: 'Select Camp',
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () {
                Get.back();
              },
            ),
            body: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              children: [
                SizedBox(
                  width: SizeConfig.screenWidth,
                  child: Row(
                    children: [
                      Expanded(
                        child: AppTextField(
                          controller: TextEditingController(
                            text: ctrl.selectedCampDate,
                          ),
                          readOnly: true,
                          onChange: (value) {},
                          onTap: () {
                            ctrl.selectDate();
                          },
                          hint: 'Camp Date*',
                          label: CommonText(
                            text: 'Camp Date*',
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
                                icCalendarMonth,
                                height: 24.h,
                                width: 24.w,
                                fit: BoxFit.contain,
                              ),
                            ),
                          ),
                        ),
                      ),
                      const SizedBox(width: 8),
                      Expanded(
                        child: AppTextField(
                          controller: TextEditingController(),
                          readOnly: false,
                          onChange: (value) {
                            ctrl.filterSearch(value);
                          },
                          hint: 'Search Camp ID',
                          label: CommonText(
                            text: 'Search Camp ID',
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
                                icSearch,
                                height: 24.h,
                                width: 24.w,
                                fit: BoxFit.contain,
                              ),
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 12),
                Expanded(
                  child: ListView.builder(
                    shrinkWrap: true,
                    itemCount: ctrl.searchCampList.length,
                    itemBuilder: (context, index) {
                      final ResourceReMappingCampOutput camp =
                          ctrl.searchCampList[index];
                      return ResourceReMappingCampListRow(
                        reMappingCampOutput: camp,
                        onSelectTap: () {
                          ctrl.onCampRowTap(camp);
                        },
                      );
                    },
                  ),
                ),
              ],
            ).paddingSymmetric(vertical: 8.h, horizontal: 10.h),
          ),
        );
      },
    );
  }
}
