// ignore_for_file: must_be_immutable
import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/camp_for_health_screening_d2d_controller.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../../Modules/widgets/S2TAppBar.dart';
import '../health_screening_details_screen/health_screening_details_screen.dart';
import 'camp_for_health_screening_d2d_row/camp_for_health_screening_d2d_row.dart';

class CampForHealthScreeningD2DScreen extends StatelessWidget {
  final int testID;

  const CampForHealthScreeningD2DScreen({super.key, required this.testID});

  @override
  Widget build(BuildContext context) {
    final controller = Get.put(CampForHealthScreeningD2DController());
    SizeConfig().init(context);

    return NetworkWrapper(
      child: KeyboardDismissOnTap(
        child: Scaffold(
          appBar: mAppBar(
            scTitle: 'Select Camp',
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Navigator.pop(context),
          ),
          body: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              Row(
                children: [
                  Expanded(
                    child: AppTextField(
                      controller: controller.districtController,
                      // readOnly: !controller.isUserInteractionEnabled,
                      readOnly: true,
                      onTap: () => controller.fetchDistrictList(),
                      hint: 'District',
                      label: CommonText(
                        text: 'District',
                        fontSize: 14.sp * 1.33,
                        fontWeight: FontWeight.normal,
                        textColor: kBlackColor,
                        textAlign: TextAlign.start,
                      ),
                      hintStyle: TextStyle(
                        fontSize: 14.sp * 1.33,
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
                      suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: AppTextField(
                      controller: controller.dateController,
                      readOnly: true,
                      onTap: () => _selectCampDate(context, controller),
                      hint: 'Camp Date*',
                      label: CommonText(
                        text: 'Camp Date*',
                        fontSize: 14.sp * 1.33,
                        fontWeight: FontWeight.normal,
                        textColor: kBlackColor,
                        textAlign: TextAlign.start,
                      ),
                      hintStyle: TextStyle(
                        fontSize: 14.sp * 1.33,
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
                ],
              ).paddingOnly(top: 10, left: 4, right: 4),
              const SizedBox(height: 8),
              Expanded(
                child: Obx(
                  () =>
                      controller.isLoading.value
                          ? const CommonSkeletonList()
                          : controller.campList.isEmpty
                          ? NoDataFound().paddingOnly(left: 4.w, right: 4.w)
                          : ListView.builder(
                            itemCount: controller.campList.length,
                            itemBuilder: (context, index) {
                              final obj = controller.campList[index];
                              return CampForHealthScreeningD2DRow(
                                campDetailsonLabForDoorToDoorOutput: obj,
                                onSelectTap:
                                    () => controller.onCampSelected(obj, () {
                                      Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                          builder:
                                              (
                                                _,
                                              ) => HealthScreeningDetailsScreen(
                                                testID: testID,
                                                teamid: 0,
                                                districtID:
                                                    obj.dISTLGDCODE ?? 0,
                                                districtName:
                                                    obj.dISTNAME ?? '',
                                                campID: obj.campId ?? 0,
                                                dISTLGDCODE:
                                                    obj.dISTLGDCODE ?? 0,
                                                campType: obj.campType ?? 0,
                                                campDate:
                                                    controller
                                                        .selectedCampDate
                                                        .value,
                                                surveyCoordinatorName: '',
                                                campTypeDescription:
                                                    obj.campTypeDescription ??
                                                    '',
                                              ),
                                        ),
                                      );
                                    }),
                              );
                            },
                          ),
                ),
              ),
            ],
          ).paddingSymmetric(horizontal: 8),
        ),
      ),
    );
  }

  Future<void> _selectCampDate(
    BuildContext context,
    CampForHealthScreeningD2DController controller,
  ) async {
    final picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      controller.onDateChanged(picked);
    }
  }
}
