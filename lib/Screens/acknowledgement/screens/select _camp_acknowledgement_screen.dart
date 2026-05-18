import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/acknowledgement/controllers/acknowledgement_camp_list_controller.dart';
import 'package:s2toperational/Screens/acknowledgement/screens/acknowledgement_patient_list_screen.dart';
import 'package:s2toperational/Screens/acknowledgement/widgets/acknowledgement_camp_row.dart';

class AcknowledgementCampListScreenNew extends StatelessWidget {
  const AcknowledgementCampListScreenNew({super.key});

  @override
  Widget build(BuildContext context) {
    final controller = Get.put(AcknowledgementCampListController());

    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Select Camp',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: Column(
          children: [
            AppTextField(
              controller: controller.dateController,
              readOnly: true,
              onTap: () => _selectDate(context, controller),
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
            const SizedBox(height: 8),
            AppTextField(
              controller: controller.searchController,
              readOnly: false,
              onChange: controller.filterBySearch,
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
              textInputType: TextInputType.number,
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
                    : controller.searchList.isEmpty
                        ? Center(
                            child: CommonText(
                              text: 'No camps found for selected date',
                              fontSize: 14.sp,
                              fontWeight: FontWeight.w500,
                              textColor: kBlackColor,
                              textAlign: TextAlign.center,
                            ),
                          )
                        : ListView.builder(
                            itemCount: controller.searchList.length,
                            itemBuilder: (context, index) {
                              final camp = controller.searchList[index];
                              return AcknowledgementNewCampRow(
                                camp: camp,
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder: (_) =>
                                          AcknowledgementPatientListScreenNew(
                                        campId: camp.campId ?? 0,
                                        siteDetailId:
                                            camp.siteDetailId ?? 0,
                                        districtName:
                                            camp.dISTNAME ?? '',
                                      ),
                                    ),
                                  ).then((_) => controller.fetchCamps());
                                },
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

  Future<void> _selectDate(
    BuildContext context,
    AcknowledgementCampListController controller,
  ) async {
    final picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) controller.onDateChanged(picked);
  }
}