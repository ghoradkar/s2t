// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/Json_Class/TeamDetailsListResponse/TeamDetailsListResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppDropdownTextfield.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';
import '../controller/patient_status_controller.dart';

class PatientStatusScreen extends StatelessWidget {
  const PatientStatusScreen({
    super.key,
    required this.campId,
    required this.dISTLGDCODE,
    required this.cAMPTYPE,
    required this.campTypeDescription,
  });

  final int campId;
  final int dISTLGDCODE;
  final int? cAMPTYPE;
  final String? campTypeDescription;

  @override
  Widget build(BuildContext context) {
    Get.put(PatientStatusController(
      campId: campId,
      dISTLGDCODE: dISTLGDCODE,
      cAMPTYPE: cAMPTYPE,
      campTypeDescription: campTypeDescription,
    ));
    return GetBuilder<PatientStatusController>(
      builder: (ctrl) => NetworkWrapper(
        child: Expanded(
          child: Column(
            children: [
              SizedBox(height: 16.h),
              ctrl.isShowTeamDropDown
                  ? AppDropdownTextfield(
                    icon: icTeamIconn,
                    titleHeaderString: 'Team',
                    valueString: ctrl.teamName,
                    isDisabled: false,
                    onTap: () => _openTeamSheet(context, ctrl),
                  )
                  : const SizedBox.shrink(),
              ctrl.isShowTeamDropDown ? SizedBox(height: 8.h) : const SizedBox.shrink(),
              AppTextField(
                onChange: ctrl.searchPatients,
                controller: ctrl.searchController,
                readOnly: false,
                textInputType: TextInputType.number,
                inputStyle: TextStyle(fontSize: 14.sp * 1.33, color: Colors.black),
                label: RichText(
                  text: TextSpan(
                    text: 'Name or Registration No.',
                    style: TextStyle(
                      color: kLabelTextColor,
                      fontSize: 14.sp * 1.33,
                      fontFamily: FontConstants.interFonts,
                    ),
                  ),
                ),
                labelStyle: TextStyle(fontSize: 14.sp * 1.33),
                suffixIcon: const Icon(Icons.search),
              ),
              SizedBox(height: 8.h),
              Expanded(
                child: ctrl.isLoading
                    ? const CommonSkeletonPatientList()
                    : ctrl.searchPatientStatusDetailsList.isNotEmpty
                    ? ListView.builder(
                      itemCount: ctrl.searchPatientStatusDetailsList.length,
                      itemBuilder: (context, index) {
                        final item = ctrl.searchPatientStatusDetailsList[index];
                        return Padding(
                          padding: EdgeInsets.fromLTRB(0, 0, 0, 12.h),
                          child: Container(
                            padding: EdgeInsets.symmetric(vertical: 8.h, horizontal: 8.w),
                            width: SizeConfig.screenWidth,
                            decoration: BoxDecoration(
                              color: Colors.white,
                              boxShadow: [
                                BoxShadow(color: Colors.black.withValues(alpha: 0.15), blurRadius: 10),
                              ],
                              borderRadius: BorderRadius.circular(10),
                            ),
                            child: Column(
                              children: [
                                Row(
                                  children: [
                                    SizedBox(width: 20.w, height: 20.h, child: Image.asset(icUserIcon)),
                                    SizedBox(width: 6.w),
                                    Expanded(
                                      child: RichText(
                                        text: TextSpan(
                                          text: '${item.patientName ?? ''} : ',
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily: FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: 14.sp,
                                          ),
                                          children: [
                                            TextSpan(
                                              text: '${item.regdNo ?? ''}',
                                              style: TextStyle(
                                                color: dropDownTitleHeader,
                                                fontFamily: FontConstants.interFonts,
                                                fontWeight: FontWeight.w400,
                                                fontSize: 14.sp,
                                              ),
                                            ),
                                          ],
                                        ),
                                        softWrap: true,
                                      ),
                                    ),
                                  ],
                                ),
                                SizedBox(height: 6.h),
                                _statusHeaderRow(),
                                SizedBox(height: 6.h),
                                _statusIconRow(ctrl, item),
                              ],
                            ),
                          ),
                        );
                      },
                    )
                    : NoDataFound(),
              ),
            ],
          ).paddingSymmetric(horizontal: 4.w),
        ),
      ),
    );
  }

  Widget _statusHeaderRow() {
    return Container(
      width: double.infinity,
      height: 30,
      decoration: BoxDecoration(
        color: campCalenderBorder,
        borderRadius: const BorderRadius.only(topLeft: Radius.circular(5), topRight: Radius.circular(5)),
        border: Border.all(width: 1, color: campCalenderBorder),
      ),
      child: Row(
        children: [
          _headerCell('Basic', left: true),
          const SizedBox(width: 1),
          _headerCell('PE'),
          const SizedBox(width: 1),
          _headerCell('LFT'),
          const SizedBox(width: 1),
          _headerCell('VST'),
          const SizedBox(width: 1),
          _headerCell('AST'),
          const SizedBox(width: 1),
          _headerCell('SC', right: true),
        ],
      ),
    );
  }

  Widget _headerCell(String label, {bool left = false, bool right = false}) {
    return Expanded(
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: left ? const Radius.circular(5) : Radius.zero,
            topRight: right ? const Radius.circular(5) : Radius.zero,
          ),
        ),
        child: Center(
          child: Text(
            label,
            style: TextStyle(
              color: uploadBillTitleColor,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w400,
              fontSize: 14,
            ),
          ),
        ),
      ),
    );
  }

  Widget _statusIconRow(PatientStatusController ctrl, dynamic item) {
    return Container(
      width: double.infinity,
      height: 30,
      decoration: BoxDecoration(
        color: campCalenderBorder,
        border: Border.all(width: 1, color: campCalenderBorder),
      ),
      child: Row(
        children: [
          _iconCell(ctrl.checkPatientStatus(item.basicDetails ?? '')),
          const SizedBox(width: 1),
          _iconCell(ctrl.checkPatientStatus(item.physicalExamination ?? '')),
          const SizedBox(width: 1),
          _iconCell(ctrl.checkPatientStatus(item.lungFunctioinTest ?? '')),
          const SizedBox(width: 1),
          _iconCell(ctrl.checkPatientStatus(item.visionScreening ?? '')),
          const SizedBox(width: 1),
          _iconCell(ctrl.checkPatientStatus(item.audioScreeningTest ?? '')),
          const SizedBox(width: 1),
          _iconCell(ctrl.checkPatientStatus(item.barcode ?? '')),
        ],
      ),
    );
  }

  Widget _iconCell(String assetPath) {
    return Expanded(
      child: Container(
        color: Colors.white,
        child: Center(
          child: SizedBox(
            width: 20,
            height: 20,
            child: Image.asset(assetPath),
          ),
        ),
      ),
    );
  }

  Future<void> _openTeamSheet(BuildContext context, PatientStatusController ctrl) async {
    final teams = await ctrl.fetchCampWiseTeams();
    if (teams.isEmpty || !context.mounted) return;
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
          onApplyTap: (p0) => ctrl.selectTeam(p0 as TeamDetailsOutput),
        ),
      ),
    ).whenComplete(() => ctrl.update());
  }
}
