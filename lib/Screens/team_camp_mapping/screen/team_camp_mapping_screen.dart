// ignore_for_file: file_names, avoid_print, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Screens/device_and_resource_mapping/models/assign_resources_response.dart';
import 'package:s2toperational/Screens/team_camp_mapping/model/assign_type_model.dart';
import 'package:s2toperational/Screens/camp_calendar/model/camp_list_v3_response.dart';
import 'package:s2toperational/Screens/camp_creation/models/camp_type_response.dart';
import 'package:s2toperational/Screens/camp_creation/models/district_response.dart';
import 'package:s2toperational/Screens/team_camp_mapping/model/team_camp_lab_response.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/selection_bottom_sheet.dart';
import '../controller/team_camp_mapping_controller.dart';
import '../model/team_details_list_for_assign_response.dart';
import '../model/teams_camp_type_wise_response.dart';
import '../model/teams_doctor_list_response.dart';
import '../widget/assigned_doctors_view.dart';
import '../widget/assigned_flexi_doctors_view.dart';
import '../widget/assigned_teams_view.dart';
import '../widget/mmu_doctor_view.dart';
import '../widget/team_details_list_for_assign_view.dart';

class TeamCampMappingScreen extends StatelessWidget {
  const TeamCampMappingScreen({super.key});

  @override
  Widget build(BuildContext context) {
    Get.put(TeamCampMappingController());
    SizeConfig().init(context);
    return GetBuilder<TeamCampMappingController>(
      builder: (ctrl) {
        if (ctrl.shouldOpenTeamSheet) {
          ctrl.shouldOpenTeamSheet = false;
          WidgetsBinding.instance.addPostFrameCallback(
            (_) => _showAppointmentTeamBottomSheet(context, ctrl),
          );
        }
        return KeyboardDismissOnTap(
          dismissOnCapturedTaps: true,
          child: Scaffold(
            appBar: mAppBar(
              scTitle: 'Team Camp Mapping',
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () => Get.back(),
            ),
            body: SingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Container(
                    color: kWhiteColor,
                    child: Column(
                      children: [
                        Row(
                          children: [
                            Expanded(child: _buildTextField(ctrl.fromDate, icCalendarMonth, 'From Date*', () => _selectDate(context, ctrl))),
                            const SizedBox(width: 8),
                            Expanded(child: _buildDropdownField(ctrl.selectedCampType?.campTypeDescription ?? '', icnTent, 'Camp Type*', () => _showCampTypeSheet(context, ctrl))),
                          ],
                        ),
                        const SizedBox(height: 8),
                        Row(
                          children: [
                            Expanded(child: _buildDropdownField(ctrl.selectedDistrict?.dISTNAME ?? '', icMapPin, 'District*', () => _showDistrictSheet(context, ctrl))),
                            const SizedBox(width: 8),
                            Expanded(child: _buildDropdownField(ctrl.selectedLab?.labName ?? '', icLandingLab, 'Lab*', () => _showLabSheet(context, ctrl))),
                          ],
                        ),
                        const SizedBox(height: 8),
                        _buildDropdownField(ctrl.selectedCampID?.campId.toString() ?? '', icHashIcon, 'Camp ID*', () => _showCampIDSheet(context, ctrl)),
                        if (ctrl.showTeamDropDown) ...[
                          const SizedBox(height: 8),
                          _buildDropdownField(ctrl.selectedTeam?.typeName ?? '', icUsersGroup, 'Select Team*', () => _showTeamTypeSheet(context, ctrl)),
                          const SizedBox(height: 8),
                          _buildDropdownField(ctrl.selectedTeams?.teamName ?? '', icUsersGroup, 'Select Teams*', () => _showTeamDetailsSheet(context, ctrl)),
                          const SizedBox(height: 8),
                          _buildDropdownField(ctrl.selectedAssignResources?.desgName ?? '', icUsersGroup, 'Assign Resources*', () => _showAssignResourcesSheet(context, ctrl)),
                        ],
                      ],
                    ),
                  ),
                  const SizedBox(height: 20),
                  SizedBox(
                    height: 40,
                    child: Center(
                      child: SizedBox(
                        width: 120,
                        child: AppActiveButton(
                          buttontitle: 'Assign',
                          isCancel: ctrl.assignButtonDisabled,
                          onTap: () async {
                            if (ctrl.assignButtonDisabled) return;
                            final ok = await ctrl.submitAssignment();
                            if (ok) _showSuccessDialog(context, ctrl, isTeamRefresh: false);
                          },
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 8),
                  AssignedTeamsView(
                    assignedTeamsList: ctrl.assignedTeamsList,
                    deleteDidPressed: (item) => _confirmDelete(context, ctrl, item.member1 ?? '', item.teamNumber ?? '', '0', 1),
                  ),
                  AssignedDoctorsView(
                    list: ctrl.assignedDoctorsList,
                    deleteDidPressed: (item) => _confirmDelete(context, ctrl, item.memberName ?? '', item.teamNumber ?? '0', item.userID?.toString() ?? '0', 2),
                  ),
                  AssignedFlexiDoctorsView(
                    list: ctrl.assignedFlexDoctorsList,
                    deleteDidPressed: (item) => _confirmDelete(context, ctrl, item.memberName ?? '', '0', '0', 5),
                  ),
                  MMUDoctorView(
                    list: ctrl.assignedMMUDoctorList,
                    deleteDidPressed: (item) => _confirmDelete(context, ctrl, item.memberName ?? '', '0', '0', 6),
                  ),
                ],
              ).paddingOnly(left: 10, right: 10, top: 16),
            ),
          ),
        );
      },
    );
  }

  Widget _buildTextField(String value, String icon, String hint, VoidCallback onTap) {
    return AppTextField(
      readOnly: true,
      controller: TextEditingController(text: value),
      onTap: onTap,
      hint: hint,
      label: CommonText(text: hint, fontSize: 12.sp, fontWeight: FontWeight.normal, textColor: kBlackColor, textAlign: TextAlign.start),
      hintStyle: TextStyle(fontSize: 12.sp, fontWeight: FontWeight.w400, fontFamily: FontConstants.interFonts),
      fieldRadius: 10,
      prefixIcon: SizedBox(height: 20.h, width: 20.w, child: Center(child: Image.asset(icon, height: 24.h, width: 24.w, fit: BoxFit.contain))),
    );
  }

  Widget _buildDropdownField(String value, String icon, String hint, VoidCallback onTap) {
    return AppTextField(
      readOnly: true,
      controller: TextEditingController(text: value),
      onTap: onTap,
      hint: hint,
      label: CommonText(text: hint, fontSize: 12.sp, fontWeight: FontWeight.normal, textColor: kBlackColor, textAlign: TextAlign.start),
      hintStyle: TextStyle(fontSize: 12.sp, fontWeight: FontWeight.w400, fontFamily: FontConstants.interFonts),
      fieldRadius: 10,
      prefixIcon: SizedBox(height: 20.h, width: 20.w, child: Center(child: Image.asset(icon, height: 24.h, width: 24.w, fit: BoxFit.contain))),
      suffixIcon: const Icon(Icons.keyboard_arrow_down_outlined),
    );
  }

  Future<void> _selectDate(BuildContext context, TeamCampMappingController ctrl) async {
    final picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(2000),
      lastDate: DateTime.now(),
    );
    if (picked != null) ctrl.selectDate(picked);
  }

  Future<void> _showCampTypeSheet(BuildContext context, TeamCampMappingController ctrl) async {
    final list = await ctrl.fetchCampType();
    if (list.isEmpty) return;
    _showDropDownSheet(context, 'Camp Type', list, DropDownTypeMenu.CampType, (p0) {
      ctrl.setCampType(p0 as CampTypeOutput);
    });
  }

  Future<void> _showDistrictSheet(BuildContext context, TeamCampMappingController ctrl) async {
    final list = await ctrl.fetchDistrictList();
    if (list.isEmpty) return;
    _showDropDownSheet(context, 'Select District', list, DropDownTypeMenu.District, (p0) {
      ctrl.setDistrict(p0 as DistrictOutput);
    });
  }

  Future<void> _showLabSheet(BuildContext context, TeamCampMappingController ctrl) async {
    final list = await ctrl.fetchLabList();
    if (list.isEmpty) return;
    _showDropDownSheet(context, 'Select Lab', list, DropDownTypeMenu.TeamCampLab, (p0) {
      ctrl.setLab(p0 as TeamCampLabOutput);
    });
  }

  Future<void> _showCampIDSheet(BuildContext context, TeamCampMappingController ctrl) async {
    final list = await ctrl.fetchCampList();
    if (list.isEmpty) return;
    _showDropDownSheet(context, 'Camp ID', list, DropDownTypeMenu.CampID, (p0) {
      ctrl.setCampID(p0 as CampListV3Output);
    });
  }

  Future<void> _showTeamTypeSheet(BuildContext context, TeamCampMappingController ctrl) async {
    final typeList = [AssignTypeModel(typeId: 1, typeName: 'Team')];
    _showDropDownSheet(context, 'Select Team', typeList, DropDownTypeMenu.SelectTeam, (p0) async {
      await ctrl.onTeamTypeSelected(p0 as AssignTypeModel);
    });
  }

  Future<void> _showTeamDetailsSheet(BuildContext context, TeamCampMappingController ctrl) async {
    final list = await ctrl.fetchTeamDetailsListForAssign();
    if (list.isEmpty) {
      ToastManager.toast('Team not assigned for selected campID, assign team first');
      return;
    }
    _showSelectTeamBottomSheet(context, ctrl, list);
  }

  Future<void> _showAssignResourcesSheet(BuildContext context, TeamCampMappingController ctrl) async {
    final resList = await ctrl.fetchAssignResourcesList();
    if (resList.isEmpty) return;
    _showDropDownSheet(context, 'Assign Resources', resList, DropDownTypeMenu.AssignResources, (p0) async {
      ctrl.setAssignResources(p0 as AssignResourcesOutput);
      final desgId = ctrl.selectedAssignResources?.desgId;
      if (desgId == 34 || desgId == 35 || desgId == 86) {
        if (ctrl.selectedTeams == null) {
          ToastManager.toast('Please Select Assigned Team Before Adding Resource');
          return;
        }
      }
      await _fetchAndShowResourceSheet(context, ctrl, desgId);
    });
  }

  Future<void> _fetchAndShowResourceSheet(BuildContext context, TeamCampMappingController ctrl, int? desgId) async {
    List<TeamsDoctorListOutput> list = [];
    int sheetIndex = 2;
    if (desgId == 34) {
      list = await ctrl.fetchDoctorList();
      sheetIndex = 2;
    } else if (desgId == 35) {
      list = await ctrl.fetchPhleboList();
      sheetIndex = 0;
    } else if (desgId == 86) {
      list = await ctrl.fetchDeOpList();
      sheetIndex = 1;
    } else if (desgId == 146 || desgId == 129) {
      list = await ctrl.fetchFlexiPhleboList();
      sheetIndex = 2;
    } else if (desgId == 147) {
      list = await ctrl.fetchFlexiDoctorList();
      sheetIndex = 2;
    } else if (desgId == 141) {
      list = await ctrl.fetchMMUDoctorList();
      sheetIndex = 2;
    }
    if (list.isEmpty) return;
    _showSelectPhleboBottomSheet(context, ctrl, list, sheetIndex);
  }

  void _showDropDownSheet(BuildContext context, String title, List<dynamic> list, DropDownTypeMenu type, Function(dynamic) onApply) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (ctx) => Container(
        width: double.infinity,
        height: MediaQuery.of(ctx).size.width * 1.33 + MediaQuery.of(ctx).viewPadding.bottom,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(topLeft: Radius.circular(20), topRight: Radius.circular(20)),
        ),
        child: DropDownListScreen(titleString: title, dropDownList: list, dropDownMenu: type, onApplyTap: onApply),
      ),
    );
  }

  void _showAppointmentTeamBottomSheet(BuildContext context, TeamCampMappingController ctrl) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(20))),
      builder: (ctx) {
        TeamsCampTypeWiseOutput? tempSelected;
        return StatefulBuilder(
          builder: (c, sheetState) => SelectionBottomSheet<TeamsCampTypeWiseOutput, int>(
            title: 'Select Team',
            items: ctrl.teamList,
            selectedValue: tempSelected?.teamid,
            valueFor: (item) => item.teamid ?? 0,
            labelFor: (item) => '${item.teamid ?? ''} ${item.teamname ?? ''} ${item.member1 ?? ''} ${item.member2 ?? ''}',
            showSearch: true,
            height: MediaQuery.of(ctx).size.height * 0.7,
            padding: EdgeInsets.only(top: responsiveHeight(20), left: responsiveHeight(20), right: responsiveHeight(20), bottom: responsiveHeight(20)),
            titleTextStyle: TextStyle(fontSize: responsiveFont(16), fontWeight: FontWeight.normal, fontFamily: FontConstants.interFonts),
            titleBottomSpacing: responsiveHeight(16),
            showRadio: false,
            useInkWell: false,
            itemBuilder: (context, item, isSelected) => _buildTeamCard(item, isSelected),
            onItemTap: (item) {
              sheetState(() => tempSelected = item);
              for (final t in ctrl.teamList) t.selected = false;
              item.selected = true;
              Navigator.pop(ctx);
              ctrl.submitTeamOnly().then((success) {
                if (success && context.mounted) {
                  _showSuccessDialog(context, ctrl, isTeamRefresh: true);
                }
              });
            },
          ),
        );
      },
    );
  }

  void _showSelectTeamBottomSheet(BuildContext context, TeamCampMappingController ctrl, List<TeamDetailsListForAssignOutput> list) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(20))),
      builder: (ctx) {
        TeamDetailsListForAssignOutput? tempSelected = ctrl.selectedTeams;
        return StatefulBuilder(
          builder: (c, sheetState) => SelectionBottomSheet<TeamDetailsListForAssignOutput, String>(
            title: 'Select Team',
            items: list,
            selectedValue: tempSelected?.teamNumber,
            valueFor: (item) => item.teamNumber ?? '',
            labelFor: (item) => item.teamName ?? 'NA',
            height: MediaQuery.of(ctx).size.height * 0.7,
            padding: EdgeInsets.only(top: responsiveHeight(20), left: responsiveHeight(20), right: responsiveHeight(20), bottom: responsiveHeight(20)),
            titleTextStyle: TextStyle(fontSize: responsiveFont(16), fontWeight: FontWeight.normal, fontFamily: FontConstants.interFonts),
            titleBottomSpacing: responsiveHeight(16),
            showRadio: false,
            useInkWell: false,
            itemBuilder: (context, item, isSelected) => _buildTeamDetailsCard(item, isSelected),
            onItemTap: (item) {
              sheetState(() => tempSelected = item);
              ctrl.setAssignedTeam(item);
              Navigator.pop(ctx);
            },
          ),
        );
      },
    );
  }

  void _showSelectPhleboBottomSheet(BuildContext context, TeamCampMappingController ctrl, List<TeamsDoctorListOutput> list, int index) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      isDismissible: true,
      enableDrag: true,
      shape: const RoundedRectangleBorder(borderRadius: BorderRadius.vertical(top: Radius.circular(20))),
      builder: (ctx) => SizedBox(
        height: MediaQuery.of(ctx).size.height * 0.75,
        child: TeamDetailsListForAssignView(
          titleString: 'Doctor',
          list: list,
          onTapTeam: (selected) {
            if (index == 0) ctrl.setPhleboList(selected);
            else if (index == 1) ctrl.setDeList(selected);
            else ctrl.setDoctorList(selected);
          },
        ),
      ),
    );
  }

  Widget _buildTeamCard(TeamsCampTypeWiseOutput item, bool isSelected) {
    return Container(
      margin: EdgeInsets.only(bottom: 8.h),
      decoration: BoxDecoration(
        color: isSelected ? kTextOutlineColor : Colors.white,
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: isSelected ? const Color(0xFF3B5998) : Colors.grey.shade300, width: 1),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 16.w),
            decoration: BoxDecoration(
              gradient: LinearGradient(begin: Alignment.bottomRight, end: Alignment.topLeft, colors: [kFirstAppBarcolor.withValues(alpha: 0.4), kFirstAppBarcolor]),
              borderRadius: const BorderRadius.only(topLeft: Radius.circular(8), topRight: Radius.circular(8)),
            ),
            child: Text('( ${item.teamname ?? 'NA'} )', textAlign: TextAlign.center, style: TextStyle(color: Colors.white, fontWeight: FontWeight.w600, fontSize: 14.sp, fontFamily: FontConstants.interFonts)),
          ),
          if (item.member1 != null && item.member1!.isNotEmpty)
            Padding(padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 6.h), child: Text(item.member1!, style: TextStyle(fontSize: 12.sp, fontWeight: FontWeight.w500, fontFamily: FontConstants.interFonts, color: isSelected ? Colors.white : Colors.black87))),
          if (item.member1 != null && item.member2 != null)
            Padding(padding: EdgeInsets.symmetric(horizontal: 16.w), child: Divider(height: 1, color: isSelected ? Colors.white38 : Colors.grey.shade300)),
          if (item.member2 != null && item.member2!.isNotEmpty)
            Padding(padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 6.h), child: Text(item.member2!, style: TextStyle(fontSize: 12.sp, fontWeight: FontWeight.w500, fontFamily: FontConstants.interFonts, color: isSelected ? Colors.white : Colors.black87))),
        ],
      ),
    );
  }

  Widget _buildTeamDetailsCard(TeamDetailsListForAssignOutput item, bool isSelected) {
    return Container(
      margin: EdgeInsets.only(bottom: 8.h),
      decoration: BoxDecoration(
        color: isSelected ? kTextOutlineColor : Colors.white,
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: isSelected ? const Color(0xFF3B5998) : Colors.grey.shade300, width: 1),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(vertical: 4.h, horizontal: 16.w),
            decoration: BoxDecoration(
              gradient: LinearGradient(begin: Alignment.bottomRight, end: Alignment.topLeft, colors: [kFirstAppBarcolor.withValues(alpha: 0.4), kFirstAppBarcolor]),
              borderRadius: const BorderRadius.only(topLeft: Radius.circular(8), topRight: Radius.circular(8)),
            ),
            child: Text('( ${item.teamName ?? 'NA'} )', textAlign: TextAlign.center, style: TextStyle(color: Colors.white, fontWeight: FontWeight.w600, fontSize: 14.sp, fontFamily: FontConstants.interFonts)),
          ),
          if (item.member1 != null && item.member1!.isNotEmpty)
            Padding(padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 6.h), child: Text(item.member1!, style: TextStyle(fontSize: 12.sp, fontWeight: FontWeight.w500, fontFamily: FontConstants.interFonts, color: isSelected ? Colors.white : Colors.black87))),
          if (item.member1 != null && item.member2 != null)
            Padding(padding: EdgeInsets.symmetric(horizontal: 16.w), child: Divider(height: 1, color: isSelected ? Colors.white38 : Colors.grey.shade300)),
          if (item.member2 != null && item.member2!.isNotEmpty)
            Padding(padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 6.h), child: Text(item.member2!, style: TextStyle(fontSize: 12.sp, fontWeight: FontWeight.w500, fontFamily: FontConstants.interFonts, color: isSelected ? Colors.white : Colors.black87))),
        ],
      ),
    );
  }

  void _confirmDelete(BuildContext context, TeamCampMappingController ctrl, String member, String teamId, String userId, int removeType) {
    ToastManager().showConfirmationDialog(
      context: context,
      message: 'Do you really want to remove $member?',
      didSelectYes: (isYes) {
        Navigator.pop(context);
        if (isYes) ctrl.performRemove(member, teamId, userId, removeType);
      },
    );
  }

  void _showSuccessDialog(BuildContext context, TeamCampMappingController ctrl, {required bool isTeamRefresh}) {
    ToastManager().showSuccessOkayDialog(
      context: context,
      title: 'Success',
      message: 'Teams Assign successfully',
      onTap: () {
        Navigator.pop(context);
        ctrl.reloadAfterAssign(isTeamRefresh: isTeamRefresh);
      },
    );
  }
}
