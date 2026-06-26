// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import '../models/campId_list_response.dart';
import 'package:s2toperational/Screens/camp_creation/models/camp_type_response.dart';
import 'package:s2toperational/Screens/camp_creation/models/district_response.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/camp_readiness_form/controllers/camp_readiness_form_controller.dart';
import 'package:s2toperational/Screens/camp_readiness_form/models/camp_readiness_form_list_response.dart';
import 'package:s2toperational/Screens/camp_readiness_form/widgets/camp_readiness_form_row.dart';

class CampReadinessFormScreen extends StatelessWidget {
  const CampReadinessFormScreen({super.key});

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<CampReadinessFormController>(
      init: CampReadinessFormController(),
      dispose: (_) => Get.delete<CampReadinessFormController>(),
      builder: (ctrl) {
        return KeyboardDismissOnTap(
          child: Scaffold(
            appBar: mAppBar(
              scTitle: 'Camp Readiness Form',
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () {
                Get.back();
              },
            ),
            body: Container(
              decoration: const BoxDecoration(color: Colors.transparent),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  Container(
                    width: MediaQuery.of(context).size.width,
                    decoration: const BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.all(Radius.circular(10)),
                    ),
                    padding: const EdgeInsets.all(8),
                    child: Column(
                      children: [
                        Row(
                          mainAxisAlignment: MainAxisAlignment.start,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Expanded(
                              child: AppTextField(
                                readOnly: true,
                                controller: TextEditingController(
                                  text: ctrl.campDate,
                                ),
                                inputStyle: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  fontSize: 12,
                                ),
                                label: RichText(
                                  text: TextSpan(
                                    text: 'Camp Date*',
                                    style: TextStyle(
                                      fontFamily: FontConstants.interFonts,
                                      color: kLabelTextColor,
                                      fontSize: responsiveFont(14),
                                      fontWeight: FontWeight.w400,
                                    ),
                                  ),
                                ),
                                labelStyle: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w400,
                                  fontSize: responsiveFont(14),
                                ),
                                prefixIcon: SizedBox(
                                  width: 20,
                                  height: 20,
                                  child: Image.asset(icCalendarMonth),
                                ),
                              ),
                            ),
                            const SizedBox(width: 8),
                            Expanded(
                              child: AppTextField(
                                readOnly: true,
                                onTap: () async {
                                  final list = await ctrl.fetchCampType();
                                  if (list.isNotEmpty && context.mounted) {
                                    _showDropDown(
                                      context,
                                      'Camp Type',
                                      list,
                                      DropDownTypeMenu.CampType,
                                      ctrl,
                                    );
                                  } else if (list.isEmpty) {
                                    ToastManager.toast('No camp types available');
                                  }
                                },
                                controller: TextEditingController(
                                  text: ctrl.selectedCampType
                                          ?.campTypeDescription ??
                                      '',
                                ),
                                inputStyle: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  fontSize: 12,
                                ),
                                label: RichText(
                                  text: TextSpan(
                                    text: 'Camp Type*',
                                    style: TextStyle(
                                      fontFamily: FontConstants.interFonts,
                                      color: kLabelTextColor,
                                      fontSize: responsiveFont(14),
                                      fontWeight: FontWeight.w400,
                                    ),
                                  ),
                                ),
                                labelStyle: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w400,
                                  fontSize: responsiveFont(14),
                                ),
                                prefixIcon: SizedBox(
                                  width: 20,
                                  height: 20,
                                  child: Image.asset(icnTent),
                                ),
                                suffixIcon: const Icon(Icons.keyboard_arrow_down),
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 8),
                        AppTextField(
                          readOnly: true,
                          onTap: () async {
                            final list = await ctrl.fetchDistrict();
                            if (list.isNotEmpty && context.mounted) {
                              _showDropDown(
                                context,
                                'Select District',
                                list,
                                DropDownTypeMenu.District,
                                ctrl,
                              );
                            }
                          },
                          controller: TextEditingController(
                            text: ctrl.selectedDistrict?.dISTNAME ?? '',
                          ),
                          inputStyle: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontSize: 12,
                          ),
                          label: RichText(
                            text: TextSpan(
                              text: 'District*',
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: kLabelTextColor,
                                fontSize: responsiveFont(14),
                                fontWeight: FontWeight.w400,
                              ),
                            ),
                          ),
                          labelStyle: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: responsiveFont(14),
                          ),
                          prefixIcon: SizedBox(
                            width: 20,
                            height: 20,
                            child: Image.asset(icMapPin),
                          ),
                          suffixIcon: const Icon(Icons.keyboard_arrow_down),
                        ),
                        const SizedBox(height: 8),
                        AppTextField(
                          readOnly: true,
                          onTap: () async {
                            final list = await ctrl.fetchCampList();
                            if (list.isNotEmpty && context.mounted) {
                              _showDropDown(
                                context,
                                'Camp ID',
                                list,
                                DropDownTypeMenu.CampReadinessCampID,
                                ctrl,
                              );
                            }
                          },
                          controller: TextEditingController(
                            text: ctrl.selectedCampID?.campId.toString() ?? '',
                          ),
                          inputStyle: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontSize: 12,
                          ),
                          label: RichText(
                            text: TextSpan(
                              text: 'Camp ID*',
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: kLabelTextColor,
                                fontSize: responsiveFont(14),
                                fontWeight: FontWeight.w400,
                              ),
                            ),
                          ),
                          labelStyle: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: responsiveFont(14),
                          ),
                          prefixIcon: SizedBox(
                            width: 20,
                            height: 20,
                            child: Image.asset(icHashIcon),
                          ),
                          suffixIcon: const Icon(Icons.keyboard_arrow_down),
                        ),
                        if (ctrl.showTeam) ...[
                          const SizedBox(height: 8),
                          AppTextField(
                            readOnly: true,
                            controller: TextEditingController(
                              text: ctrl.teamName,
                            ),
                            inputStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontSize: 12,
                            ),
                            label: RichText(
                              text: TextSpan(
                                text: 'Team Id*',
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color: kLabelTextColor,
                                  fontSize: responsiveFont(14),
                                  fontWeight: FontWeight.w400,
                                ),
                              ),
                            ),
                            labelStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: responsiveFont(14),
                            ),
                            prefixIcon: SizedBox(
                              width: 20,
                              height: 20,
                              child: Image.asset(icTeamIconn),
                            ),
                            suffixIcon: const Icon(Icons.keyboard_arrow_down),
                          ),
                        ],
                      ],
                    ),
                  ),
                  const SizedBox(height: 8),
                  Expanded(
                    child: ListView.builder(
                      itemCount: ctrl.campReadinessList.length,
                      itemBuilder: (context, index) {
                        final CampReadinessFormOutput object =
                            ctrl.campReadinessList[index];
                        return CampReadinessFormRow(
                          index: index,
                          object: object,
                          isFormSubmitted: ctrl.isFormSubmitted,
                        );
                      },
                    ),
                  ),
                  Container(
                    width: MediaQuery.of(context).size.width,
                    color: Colors.white,
                    padding: EdgeInsets.only(
                      left: 8,
                      right: 8,
                      top: 8,
                      bottom: 8 + MediaQuery.viewPaddingOf(context).bottom,
                    ),
                    child: ctrl.showSubmitButton
                        ? Row(
                            children: [
                              Expanded(
                                child: AppActiveButton(
                                  buttontitle: 'Cancel',
                                  isCancel: true,
                                  onTap: () {
                                    Get.back();
                                  },
                                ),
                              ),
                              const SizedBox(width: 12),
                              Expanded(
                                child: AppActiveButton(
                                  buttontitle: 'Submit',
                                  onTap: () {
                                    ctrl.validations();
                                  },
                                ),
                              ),
                            ],
                          )
                        : Text(
                            'Camp readiness form already submitted',
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: Colors.green,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w600,
                              fontSize: responsiveFont(16),
                            ),
                          ),
                  ),
                ],
              ),
            ).paddingSymmetric(vertical: 4.h, horizontal: 8.w),
          ),
        );
      },
    );
  }

  void _showDropDown(
    BuildContext context,
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
    CampReadinessFormController ctrl,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.33,
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
              if (dropDownType == DropDownTypeMenu.District) {
                ctrl.onDistrictSelected(selected as DistrictOutput?);
              } else if (dropDownType == DropDownTypeMenu.CampType) {
                ctrl.onCampTypeSelected(selected as CampTypeOutput?);
              } else if (dropDownType == DropDownTypeMenu.CampReadinessCampID) {
                ctrl.onCampIdSelected(selected as CampIdOutput?);
              }
            },
          ),
        );
      },
    );
  }
}
