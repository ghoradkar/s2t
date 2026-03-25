// ignore_for_file: must_be_immutable
import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/widgets/CommonSkeletonList.dart';
import '../../../../Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/health_screening_details/controllers/camp_for_health_screening_controller.dart';
import '../health_screening_details_screen/health_screening_details_screen.dart';
import 'camp_for_health_screening_row/camp_for_health_screening_row.dart';

class CampForHealthScreeningScreen extends StatelessWidget {
  final int testID;

  const CampForHealthScreeningScreen({super.key, required this.testID});

  @override
  Widget build(BuildContext context) {
    final controller = Get.put(CampForHealthScreeningController());
    SizeConfig().init(context);

    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Select Camp',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            AppTextField(
              controller: controller.dateController,
              readOnly: true,
              onTap: () => _selectCampDate(context, controller),
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
                    icInitiatedBy,
                    height: 24.h,
                    width: 24.w,
                    fit: BoxFit.contain,
                  ),
                ),
              ),
            ),
            const SizedBox(height: 8),
            AppTextField(
              textInputType: TextInputType.number,
              controller: controller.searchController,
              readOnly: false,
              onChange: (value) => controller.filterBySearch(value),
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
            const SizedBox(height: 8),
            Expanded(
              child: Obx(
                () => controller.isLoading.value
                    ? const CommonSkeletonList()
                    : ListView.builder(
                        itemCount: controller.searchList.length,
                        itemBuilder: (context, index) {
                          final camp = controller.searchList[index];
                          return CampForHealthScreeningRow(
                            reMappingCampOutput: camp,
                            onSelectTap: () =>
                                controller.onCampSelected(camp, () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (_) => HealthScreeningDetailsScreen(
                                    testID: testID,
                                    teamid: 0,
                                    districtID:
                                        controller.selectedDistLgdCode,
                                    districtName:
                                        controller.selectedDistName,
                                    campID: controller.selectedCampId,
                                    dISTLGDCODE:
                                        controller.selectedDistLgdCode,
                                    campType: controller.selectedCampType,
                                    campDate:
                                        controller.selectedCampDate.value,
                                    surveyCoordinatorName: '',
                                    campTypeDescription: '',
                                  ),
                                ),
                              ).then((_) => controller.fetchCamps());
                            }),
                          );
                        },
                      ),
              ),
            ),
          ],
        ).paddingSymmetric(vertical: 10, horizontal: 10),
      ),
    );
  }

  Future<void> _selectCampDate(
    BuildContext context,
    CampForHealthScreeningController controller,
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