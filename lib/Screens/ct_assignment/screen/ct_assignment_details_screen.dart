// ignore_for_file: file_names, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/utilities/enums.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/common_widgets/AppTextField.dart';
import 'package:s2toperational/Modules/common_widgets/CommonText.dart';
import 'package:s2toperational/Modules/common_widgets/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Modules/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/widgets/network_wrapper.dart';
import '../controller/ct_assignment_details_controller.dart';
import '../controller/ct_sample_collection_controller.dart';
import '../model/t2t_ct_beneficiary_details_response.dart';
import '../screen/ct_sample_collection_screen.dart';
import '../widget/rejected_beneficiary_team_view.dart';

class CTAssignmentDetailsScreen extends StatelessWidget {
  const CTAssignmentDetailsScreen({super.key, required this.selectedCT});

  final T2TCTBeneficiaryDetailsOutput selectedCT;

  @override
  Widget build(BuildContext context) {
    Get.put(CTAssignmentDetailsController(selectedCT: selectedCT));
    SizeConfig().init(context);
    return GetBuilder<CTAssignmentDetailsController>(
      builder: (ctrl) => NetworkWrapper(
        child: Scaffold(
          appBar: mAppBar(
            scTitle: 'Assign Team for CT',
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Get.back(),
          ),
          body: SingleChildScrollView(
            child: Column(
              children: [
                _readOnlyField(ctrl.beneficiaryDetails?.beneficiaryName ?? '', userRound, 'Name'),
                _readOnlyField(ctrl.beneficiaryDetails?.pinCode ?? '', icMapPin, 'Pincode').paddingOnly(top: 8),
                const SizedBox(height: 8),
                _readOnlyField(ctrl.beneficiaryDetails?.area ?? '', icMapPin, 'Area'),
                const SizedBox(height: 8),
                _readOnlyField(ctrl.beneficiaryDetails?.address ?? '', icMapPin, 'Address', multiline: true),
                const SizedBox(height: 8),
                Row(
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    SizedBox(
                      width: responsiveWidth(150),
                      child: AppActiveButton(
                        buttontitle: 'View Details',
                        onTap: () {
                          Get.delete<CTSampleCollectionController>(force: true);
                          Get.to(
                            () => CTSampleCollectionScreen(
                              beneficiaryDetails: ctrl.beneficiaryDetails,
                              isAppointmentFlow: false,
                            ),
                          );
                        },
                      ),
                    ),
                  ],
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    Text(
                      'Team & Camp Details',
                      style: TextStyle(
                        color: kBlackColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w400,
                        fontSize: responsiveFont(16),
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 10),
                _readOnlyField(ctrl.beneficiaryDetails?.campId.toString() ?? '', icnTent, 'Camp ID'),
                const SizedBox(height: 10),
                Row(
                  children: [
                    Expanded(child: _readOnlyField(ctrl.beneficiaryDetails?.campType ?? '', icnTent, 'Camp Type')),
                    const SizedBox(width: 10),
                    Expanded(child: _readOnlyField(ctrl.beneficiaryDetails?.campDate ?? '', icCalendarMonth, 'Camp Date')),
                  ],
                ),
                const SizedBox(height: 10),
                Container(
                  width: SizeConfig.screenWidth,
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(10),
                    boxShadow: [
                      BoxShadow(
                        offset: const Offset(0, 1),
                        color: Colors.black.withValues(alpha: 0.15),
                        spreadRadius: 0,
                        blurRadius: 4,
                      ),
                    ],
                  ),
                  padding: const EdgeInsets.all(14),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        '${ctrl.beneficiaryDetails?.teamname ?? ''} / IsTeamActive - ${ctrl.beneficiaryDetails?.isTeamActive ?? ''}',
                        style: TextStyle(
                          color: kBlackColor,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w600,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                      const SizedBox(height: 8),
                      _memberRow('${ctrl.beneficiaryDetails?.member1 ?? ''} (${ctrl.beneficiaryDetails?.member1MOB ?? ''})'),
                      const SizedBox(height: 8),
                      _memberRow('${ctrl.beneficiaryDetails?.member2 ?? ''} (${ctrl.beneficiaryDetails?.member2MOB ?? ''})'),
                    ],
                  ),
                ),
                if (ctrl.isShowAssignButton) ...[
                  const SizedBox(height: 10),
                  _buildAssignModeSelector(context, ctrl),
                ],
                if (ctrl.isSelectedAssignD2DTeam) ...[
                  const SizedBox(height: 4),
                  _buildShowAllTeamsToggle(ctrl),
                ],
                if (ctrl.isShowTeamDropDown) ...[
                  _buildDropTap(
                    ctrl.selectedTeam?.teamName ?? '',
                    icUsersGroup,
                    'Select Team',
                    () => _openTeamSheet(context, ctrl),
                  ),
                ],
                if (ctrl.isShowExecutiveDropDown) ...[
                  const SizedBox(height: 10),
                  _buildDropTap(
                    ctrl.selectedExecutive?.uSERNAME ?? '',
                    icUsersGroup,
                    'Select Executive',
                    () {
                      if (ctrl.selectedCT.arId != 4) {
                        _openExecutiveSheet(context, ctrl);
                      }
                    },
                  ),
                ],
                Visibility(
                  visible: ctrl.isUserAlreadyAssigned,
                  child: Align(
                    alignment: Alignment.centerLeft,
                    child: CommonText(
                      text: 'User Already Assigned *',
                      fontSize: 14,
                      fontWeight: FontWeight.normal,
                      textColor: kPrimaryColor,
                      textAlign: TextAlign.start,
                    ),
                  ).paddingOnly(left: 4),
                ),
                const SizedBox(height: 20),
                Padding(
                  padding: const EdgeInsets.fromLTRB(0, 4, 0, 20),
                  child: AppActiveButton(
                    buttontitle: 'Assign',
                    onTap: () async {
                      if (ctrl.selectedCT.arId == 4) return;
                      final ok = await ctrl.submitAssignment();
                      if (ok) {
                        ToastManager.showSuccessPopup(
                          context,
                          icSuccessIcon,
                          'User Assigned successfully',
                          () { Get.back(); Get.back(); },
                        );
                      }
                    },
                  ),
                ),
              ],
            ),
          ).paddingSymmetric(vertical: 12, horizontal: 8),
        ),
      ),
    );
  }

  Widget _readOnlyField(String value, String icon, String hint, {bool multiline = false}) {
    return AppTextField(
      controller: TextEditingController(text: value),
      readOnly: true,
      hint: hint,
      label: CommonText(
        text: hint,
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
      textInputType: multiline ? TextInputType.multiline : TextInputType.text,
      minLines: multiline ? 2 : null,
      maxLines: multiline ? 4 : 1,
      prefixIcon: SizedBox(
        height: 20.h,
        width: 20.w,
        child: Center(child: Image.asset(icon, height: 24.h, width: 24.w, fit: BoxFit.contain)),
      ),
    );
  }

  Widget _buildDropTap(String value, String icon, String hint, VoidCallback onTap) {
    return AppTextField(
      controller: TextEditingController(text: value),
      readOnly: true,
      hint: hint,
      label: CommonText(
        text: hint,
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
        child: Center(child: Image.asset(icon, height: 24.h, width: 24.w, fit: BoxFit.contain)),
      ),
      suffixIcon: const Icon(Icons.keyboard_arrow_down_outlined),
      onTap: onTap,
    );
  }

  Widget _memberRow(String text) {
    return Row(
      children: [
        SizedBox(
          width: responsiveHeight(20),
          height: responsiveHeight(20),
          child: Image.asset(icInitiatedBy),
        ),
        const SizedBox(width: 8),
        Expanded(
          child: Text(
            text,
            style: TextStyle(
              color: dropDownTitleHeader,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w400,
              fontSize: responsiveFont(14),
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildAssignModeSelector(BuildContext context, CTAssignmentDetailsController ctrl) {
    return Container(
      padding: const EdgeInsets.all(8),
      child: Row(
        children: [
          Expanded(child: _radioOption('Assign D2D Team', ctrl.isSelectedAssignD2DTeam, () => ctrl.toggleAssignD2DTeam(true))),
          const SizedBox(width: 10),
          Expanded(child: _radioOption('Assign User', !ctrl.isSelectedAssignD2DTeam, () => ctrl.toggleAssignD2DTeam(false))),
        ],
      ),
    );
  }

  Widget _radioOption(String label, bool selected, VoidCallback onTap) {
    return GestureDetector(
      onTap: onTap,
      child: Row(
        children: [
          SizedBox(
            width: 20,
            height: 20,
            child: Image.asset(selected ? icRadioSelected : icUnRadioSelected),
          ),
          const SizedBox(width: 4),
          Text(
            label,
            style: TextStyle(
              color: selected ? kBlackColor : dropDownTitleHeader,
              fontFamily: FontConstants.interFonts,
              fontWeight: selected ? FontWeight.w600 : FontWeight.w400,
              fontSize: responsiveFont(14),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildShowAllTeamsToggle(CTAssignmentDetailsController ctrl) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(8, 0, 8, 8),
      child: GestureDetector(
        onTap: () => ctrl.toggleShowAllTeams(!ctrl.isShowAllTeams),
        child: Row(
          children: [
            SizedBox(
              width: 20,
              height: 20,
              child: Image.asset(ctrl.isShowAllTeams ? icCheckBoxSelected : icUnCheckBoxSelected),
            ),
            const SizedBox(width: 4),
            Text(
              'Show All Teams',
              style: TextStyle(
                color: kBlackColor,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                fontSize: responsiveFont(14),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> _openTeamSheet(BuildContext context, CTAssignmentDetailsController ctrl) async {
    final teams = await ctrl.fetchTeamsForDropdown();
    if (teams.isEmpty) return;
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: true,
      enableDrag: true,
      builder: (_) => Container(
        width: double.infinity,
        height: MediaQuery.of(context).size.width * 1.38,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20),
            topRight: Radius.circular(20),
          ),
        ),
        child: RejectedBeneficiaryTeamView(
          list: teams,
          onTapTeam: ctrl.selectTeam,
        ),
      ),
    );
  }

  Future<void> _openExecutiveSheet(BuildContext context, CTAssignmentDetailsController ctrl) async {
    final list = await ctrl.fetchExecutiveList();
    if (list.isEmpty) return;
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
          titleString: 'Select Executive',
          dropDownList: list,
          dropDownMenu: DropDownTypeMenu.SelectExecutive,
          onApplyTap: (p0) => ctrl.selectExecutive(p0),
        ),
      ),
    );
  }
}
