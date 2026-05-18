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
import 'package:s2toperational/Screens/acknowledgement/controllers/acknowledgement_patient_list_controller.dart';
import 'package:s2toperational/Screens/acknowledgement/screens/acknowledgement_confirmation_screen.dart';
import 'package:s2toperational/Screens/acknowledgement/widgets/acknowledgement_patient_row.dart';

class AcknowledgementPatientListScreenNew extends StatelessWidget {
  final int campId;
  final int siteDetailId;
  final String districtName;

  const AcknowledgementPatientListScreenNew({
    super.key,
    required this.campId,
    required this.siteDetailId,
    required this.districtName,
  });

  @override
  Widget build(BuildContext context) {
    final controller = Get.put(
      AcknowledgementPatientListController(
        campId: campId,
        siteDetailId: siteDetailId,
        districtName: districtName,
      ),
      tag: 'ack_patient_$campId',
    );

    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Patient List',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: Column(
          children: [
            AppTextField(
              controller: controller.searchController,
              readOnly: false,
              onChange: controller.filterBySearch,
              hint: 'Search Name / Registration No.',
              label: CommonText(
                text: 'Search Name / Registration No.',
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
                    : controller.searchList.isEmpty
                        ? Center(
                            child: CommonText(
                              text: 'No patients found',
                              fontSize: 14.sp,
                              fontWeight: FontWeight.w500,
                              textColor: kBlackColor,
                              textAlign: TextAlign.center,
                            ),
                          )
                        : ListView.builder(
                            itemCount: controller.searchList.length,
                            itemBuilder: (context, index) {
                              final patient = controller.searchList[index];
                              return AcknowledgementPatientRow(
                                patient: patient,
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder: (_) =>
                                          AcknowledgementConfirmationScreen(
                                        patient: patient,
                                        campId: campId,
                                      ),
                                    ),
                                  ).then(
                                    (_) => controller.fetchPatients(),
                                  );
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
}