// ignore_for_file: avoid_print, must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/Json_Class/MonthsResponse/MonthsResponse.dart';
import 'package:s2toperational/Modules/Json_Class/YearsResponse/YearsResponse.dart';
import 'package:s2toperational/Modules/Json_Class/CompanyListResponse/CompanyListResponse.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/Enums/Enums.dart';
import '../../Modules/Json_Class/AllDistrictListForPhyExamResponse/AllDistrictListForPhyExamResponse.dart';
import '../../Modules/Json_Class/AppointmentStatusResponse/AppointmentStatusResponse.dart';
import '../../Modules/Json_Class/AssignResourcesResponse/AssignResourcesResponse.dart';
import '../../Modules/Json_Class/AssignTypeModel/AssignTypeModel.dart';
import '../../Modules/Json_Class/AssignmentRemarksResponse/AssignmentRemarksResponse.dart';
import '../../Modules/Json_Class/BindDistrictResponse/BindDistrictResponse.dart';
import '../../Modules/Json_Class/CallStatusListResponse/CallStatusListResponse.dart';
import '../../Modules/Json_Class/CampIdListResponse/CampIdListResponse.dart';
import '../../Modules/Json_Class/CampListV3Response/CampListV3Response.dart';
import '../../Modules/Json_Class/CampTypeAndCatagoryResponse/CampTypeAndCatagoryResponse.dart';
import '../../Modules/Json_Class/CampTypeResponse/CampTypeResponse.dart';
import '../../Modules/Json_Class/BindDivisionResponse/BindDivisionResponse.dart';
import '../../Modules/Json_Class/DistrictResponse/DistrictResponse.dart';
import '../../Modules/Json_Class/ExpenseHeadResponse/ExpenseHeadResponse.dart';
import '../../Modules/Json_Class/InitiatedByResponse/InitiatedByResponse.dart';
import '../../Modules/Json_Class/LabByUserIDResponse/LabByUserIDResponse.dart';
import '../../Modules/Json_Class/LabDataResponse/LabDataResponse.dart';
import '../../Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
import '../../Modules/Json_Class/OtherReasonForPatientRejectionResponse/OtherReasonForPatientRejectionResponse.dart';
import '../../Modules/Json_Class/RecollectionAssignmentRemarksResponse/RecollectionAssignmentRemarksResponse.dart';
import '../../Modules/Json_Class/RemarkListResponse/RemarkListResponse.dart';
import '../../Modules/Json_Class/ReportDeliveryExecutiveResponse/ReportDeliveryExecutiveResponse.dart';
import '../../Modules/Json_Class/SubExpenseHeadsResponse/SubExpenseHeadsResponse.dart';
import '../../Modules/Json_Class/SubOrganizationResponse/SubOrganizationResponse.dart';
import '../../Modules/Json_Class/T2TCTUserDetailsResponse/T2TCTUserDetailsResponse.dart';
import '../../Modules/Json_Class/TalukaCampCreationResponse/TalukaCampCreationResponse.dart';
import '../../Modules/Json_Class/TeamCampLabResponse/TeamCampLabResponse.dart';
import '../../Modules/Json_Class/TeamDetailsListResponse/TeamDetailsListResponse.dart';
import '../../Modules/Json_Class/TestListForRejectResponse/TestListForRejectResponse.dart';
import '../../Modules/Json_Class/UserMappedTalukaResponse/UserMappedTalukaResponse.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/widgets/AppActiveButton.dart';

class DropDownListScreen extends StatefulWidget {
  DropDownListScreen({
    super.key,
    required this.titleString,
    required this.dropDownList,
    required this.dropDownMenu,
    required this.onApplyTap,
  });

  List<dynamic> dropDownList = [];
  String titleString = "";
  DropDownTypeMenu dropDownMenu;
  Function(dynamic) onApplyTap;

  @override
  State<DropDownListScreen> createState() => _DropDownListScreenState();
}

class _DropDownListScreenState extends State<DropDownListScreen> {
  List<dynamic> searchList = [];

  int selectedIndex = -1;

  TextEditingController searchController = TextEditingController();

  String titleDropDown(int index) {
    switch (widget.dropDownMenu) {
      case DropDownTypeMenu.CampType:
        CampTypeOutput type = searchList[index];
        return type.campTypeDescription ?? "";
      case DropDownTypeMenu.InitiatedBy:
        InitiatedByOutput type = searchList[index];
        return type.initiatedBy ?? "";
      case DropDownTypeMenu.TalukaCampList:
        TalukaCampCreationOutput type = searchList[index];
        return type.tALNAME ?? "";
      case DropDownTypeMenu.LandingLabCampCreation:
        LandingLabCampCreationOutput type = searchList[index];
        return type.labName ?? "";
      case DropDownTypeMenu.CampID:
        CampListV3Output type = searchList[index];
        return "${type.campId}(${type.campCreatedBy})";
      case DropDownTypeMenu.ExpenseHead:
        ExpenseHeaOutput type = searchList[index];
        return "${type.expenseHeadName}";
      case DropDownTypeMenu.SubExpenseHead:
        SubExpenseHeadsOutput type = searchList[index];
        return "${type.subexpenseName}";
      case DropDownTypeMenu.SubOrganization:
        SubOrganizationOutput type = searchList[index];
        return "${type.subOrgName}";
      case DropDownTypeMenu.BindDivision:
        BindDivisionOutput type = searchList[index];
        return "${type.dIVNAME}";
      case DropDownTypeMenu.BindDistrict:
        BindDistrictOutput type = searchList[index];
        return "${type.dISTNAME}";
      case DropDownTypeMenu.CampTypeAndCatagory:
        CampTypeAndCatagoryOutput type = searchList[index];
        return "${type.campTypeDescription}";
      case DropDownTypeMenu.CampReadinessCampID:
        CampIdOutput type = searchList[index];
        return "${type.campId}(${type.campCreatedBy})";
      case DropDownTypeMenu.TestToReject:
        TestListForRejectOutput type = searchList[index];
        return "${type.testName}";
      case DropDownTypeMenu.ReasonTest:
        OtherReasonOutput type = searchList[index];
        return "${type.reasonDescription}";
      case DropDownTypeMenu.CampDetailsTeam:
        TeamDetailsOutput type = searchList[index];
        return "${type.teamNumber}";
      case DropDownTypeMenu.AppointmentStatus:
        AppointmentStatusOutput type = searchList[index];
        return "${type.appointmentStatus}";
      case DropDownTypeMenu.UserMappedTaluka:
        UserMappedTalukaOutput type = searchList[index];
        return "${type.tALNAME}";
      case DropDownTypeMenu.ReportDeliveryExecutive:
        ReportDeliveryExecutiveOutput type = searchList[index];
        return "${type.userName}";
      case DropDownTypeMenu.CallStatus:
        CallStatusListOutput type = searchList[index];
        return "${type.callingStatus}";
      case DropDownTypeMenu.CallingRemark:
        RemarkListOutput type = searchList[index];
        return "${type.callingRemark}";
      case DropDownTypeMenu.DailyWorkDashboardLab:
        LabDataOutput type = searchList[index];
        return "${type.labName}";
      case DropDownTypeMenu.RejectedStatus:
        RecollectionAssignmentRemarksOutput type = searchList[index];
        return "${type.assignmentRemarks}";
      case DropDownTypeMenu.SearchBy:
        RecollectionAssignmentRemarksOutput type = searchList[index];
        return "${type.assignmentRemarks}";
      case DropDownTypeMenu.Years:
        YearsOutput type = searchList[index];
        return "${type.yearName}";
      case DropDownTypeMenu.Months:
        MonthsOutput type = searchList[index];
        return "${type.monthNameEng}";
      case DropDownTypeMenu.PaidByCompany:
        CompanyListOutput type = searchList[index];
        return "${type.paidByCompany}";
      case DropDownTypeMenu.AllDistrictListForPhyExam:
        AllDistrictListForPhyExamOutput type = searchList[index];
        return "${type.district}";
      case DropDownTypeMenu.District:
        DistrictOutput type = searchList[index];
        return "${type.dISTNAME}";
      case DropDownTypeMenu.StatusRemark:
        AssignmentRemarksOutput type = searchList[index];
        return "${type.assignmentRemarks}";
      case DropDownTypeMenu.SelectExecutive:
        T2TCTUserDetailsOutput type = searchList[index];
        return "${type.uSERNAME}";
      case DropDownTypeMenu.TalukaPacketRecive:
        LabByUserIDOutput type = searchList[index];
        return "${type.labName}";
      case DropDownTypeMenu.TeamCampLab:
        TeamCampLabOutput type = searchList[index];
        return "${type.labName}";
      case DropDownTypeMenu.SelectTeam:
        AssignTypeModel type = searchList[index];
        return type.typeName;
      case DropDownTypeMenu.AssignResources:
        AssignResourcesOutput type = searchList[index];
        return type.desgName ?? "";
      case DropDownTypeMenu.BindLab:
        LandingLabCampCreationOutput type = searchList[index];
        return "${type.labName}";
    }
  }

  void radioButtonDidSelected(int index) {
    switch (widget.dropDownMenu) {
      case DropDownTypeMenu.CampType:
        for (CampTypeOutput campType in searchList) {
          campType.isSelected = false;
        }
        CampTypeOutput campType = searchList[index];
        campType.isSelected = true;
      case DropDownTypeMenu.InitiatedBy:
        for (InitiatedByOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        InitiatedByOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.TalukaCampList:
        for (TalukaCampCreationOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        TalukaCampCreationOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.LandingLabCampCreation:
        for (LandingLabCampCreationOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        LandingLabCampCreationOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;

      case DropDownTypeMenu.CampID:
        for (CampListV3Output initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        CampListV3Output initiatedBy = searchList[index];
        initiatedBy.isSelected = true;

      case DropDownTypeMenu.ExpenseHead:
        for (ExpenseHeaOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        ExpenseHeaOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.SubExpenseHead:
        for (SubExpenseHeadsOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        SubExpenseHeadsOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.SubOrganization:
        for (SubOrganizationOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        SubOrganizationOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.BindDivision:
        for (BindDivisionOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        BindDivisionOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.BindDistrict:
        for (BindDistrictOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        BindDistrictOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.CampTypeAndCatagory:
        for (CampTypeAndCatagoryOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        CampTypeAndCatagoryOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.CampReadinessCampID:
        for (CampIdOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        CampIdOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.TestToReject:
        for (TestListForRejectOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        TestListForRejectOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.ReasonTest:
        for (OtherReasonOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        OtherReasonOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.CampDetailsTeam:
        for (TeamDetailsOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        TeamDetailsOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.AppointmentStatus:
        for (AppointmentStatusOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        AppointmentStatusOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.UserMappedTaluka:
        for (UserMappedTalukaOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        UserMappedTalukaOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.ReportDeliveryExecutive:
        for (ReportDeliveryExecutiveOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        ReportDeliveryExecutiveOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.CallStatus:
        for (CallStatusListOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        CallStatusListOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.CallingRemark:
        for (RemarkListOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        RemarkListOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.DailyWorkDashboardLab:
        for (LabDataOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        LabDataOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.RejectedStatus:
        for (RecollectionAssignmentRemarksOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        RecollectionAssignmentRemarksOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.SearchBy:
        for (RecollectionAssignmentRemarksOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        RecollectionAssignmentRemarksOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.Years:
        for (YearsOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        YearsOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.Months:
        for (MonthsOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        MonthsOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.PaidByCompany:
        for (CompanyListOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        CompanyListOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.AllDistrictListForPhyExam:
        for (AllDistrictListForPhyExamOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        AllDistrictListForPhyExamOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.District:
        for (DistrictOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        DistrictOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.StatusRemark:
        for (AssignmentRemarksOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        AssignmentRemarksOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.SelectExecutive:
        for (T2TCTUserDetailsOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        T2TCTUserDetailsOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.TalukaPacketRecive:
        for (LabByUserIDOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        LabByUserIDOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.TeamCampLab:
        for (TeamCampLabOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        TeamCampLabOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.SelectTeam:
        for (AssignTypeModel initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        AssignTypeModel initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.AssignResources:
        for (AssignResourcesOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        AssignResourcesOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
      case DropDownTypeMenu.BindLab:
        for (LandingLabCampCreationOutput initiatedBy in searchList) {
          initiatedBy.isSelected = false;
        }
        LandingLabCampCreationOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = true;
    }

    selectedIndex = index;
    setState(() {});
  }

  Widget selectedRadioButton(int index) {
    switch (widget.dropDownMenu) {
      case DropDownTypeMenu.CampType:
        CampTypeOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.InitiatedBy:
        InitiatedByOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.TalukaCampList:
        TalukaCampCreationOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.LandingLabCampCreation:
        LandingLabCampCreationOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );

      case DropDownTypeMenu.CampID:
        CampListV3Output type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );

      case DropDownTypeMenu.ExpenseHead:
        ExpenseHeaOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.SubExpenseHead:
        SubExpenseHeadsOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.SubOrganization:
        SubOrganizationOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.BindDivision:
        BindDivisionOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.BindDistrict:
        BindDistrictOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.CampTypeAndCatagory:
        CampTypeAndCatagoryOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.CampReadinessCampID:
        CampIdOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.TestToReject:
        TestListForRejectOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.ReasonTest:
        OtherReasonOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.CampDetailsTeam:
        TeamDetailsOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.AppointmentStatus:
        AppointmentStatusOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.UserMappedTaluka:
        UserMappedTalukaOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.ReportDeliveryExecutive:
        ReportDeliveryExecutiveOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.CallStatus:
        CallStatusListOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.CallingRemark:
        RemarkListOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.DailyWorkDashboardLab:
        LabDataOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.RejectedStatus:
        RecollectionAssignmentRemarksOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.SearchBy:
        RecollectionAssignmentRemarksOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.Years:
        YearsOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.Months:
        MonthsOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.PaidByCompany:
        CompanyListOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.AllDistrictListForPhyExam:
        AllDistrictListForPhyExamOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.District:
        DistrictOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.StatusRemark:
        AssignmentRemarksOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.SelectExecutive:
        T2TCTUserDetailsOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.TalukaPacketRecive:
        LabByUserIDOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.TeamCampLab:
        TeamCampLabOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.SelectTeam:
        AssignTypeModel type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.AssignResources:
        AssignResourcesOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
      case DropDownTypeMenu.BindLab:
        LandingLabCampCreationOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icRadioSelected : icUnRadioSelected,
        );
    }
  }

  @override
  void initState() {
    super.initState();
    searchList = widget.dropDownList;
    searchController.addListener(onSearchChanged);
  }

  @override
  void dispose() {
    searchController.removeListener(onSearchChanged);
    searchController.dispose();
    super.dispose();
  }

  void onSearchChanged() {
    final String query = searchController.text.toLowerCase();
    print(query);
    if (query.isEmpty) {
      searchList = widget.dropDownList = [];
    } else {
      if (widget.dropDownMenu == DropDownTypeMenu.CampType) {
        searchList =
            widget.dropDownList
                .where(
                  (item) =>
                      item.campTypeDescription?.toLowerCase().contains(
                        query.toLowerCase(),
                      ) ??
                      false,
                )
                .toList();
      }
    }
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          width: MediaQuery.of(context).size.width,
          padding: EdgeInsets.fromLTRB(0, 12.h, 0, 12.h),
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: Center(
            child: Text(
              widget.titleString,
              style: TextStyle(
                fontWeight: FontWeight.normal,
                color: kBlackColor,
                fontFamily: FontConstants.interFonts,
                fontSize:18.sp,
              ),
            ),
          ),
        ),

        Expanded(
          child: ListView.builder(
            itemCount: searchList.length,
            itemBuilder: (context, index) {
              return Padding(
                padding: EdgeInsets.fromLTRB(8.w, 0, 8.w, 4.h),
                child: Container(
                  width: MediaQuery.of(context).size.width,
                  color: Colors.transparent,
                  padding: EdgeInsets.fromLTRB(8.w, 4.h, 8.w, 4.h),
                  child: GestureDetector(
                    onTap: () {
                      radioButtonDidSelected(index);
                    },
                    child: Row(
                      children: [
                        SizedBox(
                          width: 30.w,
                          height: 30.h,
                          child: selectedRadioButton(index),
                        ),

                        SizedBox(width: 10.w),
                        Expanded(
                          child: Text(
                            titleDropDown(index),
                            style: TextStyle(
                              fontWeight: FontWeight.normal,
                              color: kBlackColor,
                              fontFamily: FontConstants.interFonts,
                              fontSize: 16.sp,
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              );
            },
          ),
        ),

        Padding(
          padding: EdgeInsets.fromLTRB(16.w, 4.h, 16.w, MediaQuery.of(context).viewPadding.bottom + 16.h),
          child: Container(
            width: MediaQuery.of(context).size.width,
            color: Colors.transparent,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Expanded(
                  child: AppActiveButton(
                    buttontitle: "Back",
                    isCancel: true,
                    onTap: () {
                      Navigator.pop(context);
                    },
                  ),
                ),
                SizedBox(width: 16.w),
                Expanded(
                  child: AppActiveButton(
                    buttontitle: "Select",
                    onTap: () {
                      if (selectedIndex == -1) {
                      } else {
                        Navigator.pop(context);
                        widget.onApplyTap(searchList[selectedIndex]);
                      }
                    },
                  ),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }
}
