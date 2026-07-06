// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/camp_details/model/team_details_list_response.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/AppTextField.dart';
import 'package:s2toperational/Modules/common_widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/common_widgets/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Modules/utilities/enums.dart';
import 'package:s2toperational/Screens/calling_modules/widgets/network_wrapper.dart';
import '../controller/screening_test_camp_details_controller.dart';
import '../widget/camp_calendar_camp_details_widget.dart';
import '../widget/screening_details_view.dart';

class ScreeningTestCampDetailsScreen extends StatelessWidget {
  const ScreeningTestCampDetailsScreen({
    super.key,
    required this.campId,
    required this.dISTLGDCODE,
    required this.campDate,
    required this.surveyCoordinatorName,
    required this.dISTNAME,
    required this.mOBNO,
    required this.isHealthScreeing,
    this.cAMPTYPE,
    this.campTypeDescription,
  });

  final int campId;
  final int dISTLGDCODE;
  final String campDate;
  final String surveyCoordinatorName;
  final String dISTNAME;
  final String mOBNO;
  final bool isHealthScreeing;
  final int? cAMPTYPE;
  final String? campTypeDescription;

  @override
  Widget build(BuildContext context) {
    Get.put(ScreeningTestCampDetailsController(
      campId: campId,
      dISTLGDCODE: dISTLGDCODE,
      campDate: campDate,
      surveyCoordinatorName: surveyCoordinatorName,
      dISTNAME: dISTNAME,
      mOBNO: mOBNO,
      isHealthScreeing: isHealthScreeing,
      cAMPTYPE: cAMPTYPE,
      campTypeDescription: campTypeDescription,
    ));
    return GetBuilder<ScreeningTestCampDetailsController>(
      builder: (ctrl) => NetworkWrapper(
        child: Expanded(
          child: SingleChildScrollView(
            child: Column(
              children: [
                const SizedBox(height: 10),
                ctrl.isShowTeamDropDown
                    ? AppTextField(
                      controller: TextEditingController(text: ctrl.teamNumber),
                      readOnly: true,
                      onTap: () async {
                        ToastManager.showLoader();
                        final teams = await ctrl.fetchCampWiseTeams();
                        ToastManager.hideLoader();
                        if (teams.isNotEmpty && context.mounted) {
                          _showTeamSheet(context, teams, ctrl);
                        }
                      },
                      inputStyle: TextStyle(fontFamily: FontConstants.interFonts, fontSize: 14),
                      label: RichText(
                        text: TextSpan(
                          text: 'Team',
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kLabelTextColor,
                            fontSize: responsiveFont(14),
                            fontWeight: FontWeight.w400,
                          ),
                          children: [
                            TextSpan(
                              text: ' *',
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: Colors.red,
                                fontSize: responsiveFont(14),
                                fontWeight: FontWeight.w400,
                              ),
                            ),
                          ],
                        ),
                      ),
                      labelStyle: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w400,
                        fontSize: responsiveFont(14),
                      ),
                      prefixIcon: SizedBox(width: 22, height: 22, child: Image.asset(icTeamIconn)),
                      suffixIcon: const Icon(Icons.keyboard_arrow_down_outlined),
                    )
                    : const SizedBox.shrink(),
                ctrl.isShowTeamDropDown ? const SizedBox(height: 4) : const SizedBox.shrink(),
                isHealthScreeing
                    ? const SizedBox.shrink()
                    : CampCalenderCampDetails(
                      campId: campId,
                      dISTNAME: dISTNAME,
                      surveyCoordinatorName: surveyCoordinatorName,
                      mOBNO: mOBNO,
                    ),
                ctrl.isLoading
                    ? const CommonSkeletonScreeningDetailsTable().paddingOnly(top: 6)
                    : ScreeningDetailsView(campDetailOutput: ctrl.campDetailOutput),
                const SizedBox(height: 10),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void _showTeamSheet(BuildContext context, List<TeamDetailsOutput> teams, ScreeningTestCampDetailsController ctrl) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (_) => Container(
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
          titleString: 'Select Team',
          dropDownList: teams,
          dropDownMenu: DropDownTypeMenu.CampDetailsTeam,
          onApplyTap: (p0) {
            ctrl.selectTeam(p0 as TeamDetailsOutput);
          },
        ),
      ),
    ).whenComplete(() => ctrl.update());
  }
}
