// ignore_for_file: must_be_immutable, file_names
import 'dart:convert';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import '../../../../../Modules/constants/fonts.dart';
import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/Json_Class/BeneficiaryDetailsforAssignTeamidDetailsResponse/BeneficiaryDetailsforAssignTeamidDetailsResponse.dart';
import '../../../Modules/Json_Class/SelectedTeamsDataListResponse/SelectedTeamsDataListResponse.dart';
import '../../../Modules/Json_Class/T2TCTBeneficiaryDetailsResponse/T2TCTBeneficiaryDetailsResponse.dart';
import '../../../Modules/Json_Class/T2TCTTeamandBeneficiaryResponse/T2TCTTeamandBeneficiaryResponse.dart';
import '../../../Modules/Json_Class/T2TCTUserDetailsResponse/T2TCTUserDetailsResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../../../Views/DropDownListScreen/DropDownListScreen.dart';
import '../../../Views/RejectedBeneficiaryTeamView/RejectedBeneficiaryTeamView.dart';
import '../AssignTeamForCTSampleCollection/AssignTeamForCTSampleCollection.dart';

class AssignTeamForCTDetails extends StatefulWidget {
  AssignTeamForCTDetails({super.key, required this.selectedCT});

  T2TCTBeneficiaryDetailsOutput selectedCT;

  @override
  State<AssignTeamForCTDetails> createState() => _AssignTeamForCTDetailsState();
}

class _AssignTeamForCTDetailsState extends State<AssignTeamForCTDetails> {
  bool isSelectedAssignD2DTeam = false;
  bool isShowAllTeams = false;

  int teamID = 0;
  String teamName = "";
  SelectedTeamsDataLisOutput? selectedTeam;
  T2TCTUserDetailsOutput? selectedExecutive;
  int empCode = 0;
  APIManager apiManager = APIManager();

  bool isShowAssignButton = true;
  bool isShowTeamDropDown = false;
  bool isShowExecutiveDropDown = true;
  bool isUserAlreadyAssigned = false;

  @override
  void initState() {
    super.initState();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (widget.selectedCT.sampleCollection == "Yes") {
      isShowAssignButton = false;
      isShowTeamDropDown = false;
      isShowExecutiveDropDown = false;
      isShowAllTeams = false;
      ToastManager.toast("Sample collection done for this beneficiary");
    }

    if (widget.selectedCT.arId == 4) {
      ToastManager.toast(
        "You are not allowed to change the status as the beneficiary is not interested in CT.",
      );
      isShowAssignButton = false;
    }

    getDependentInfo();
  }


  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Assign Team for CT",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: SingleChildScrollView(
          child: Column(
            children: [
              AppTextField(
                controller: TextEditingController(
                  text: beneficiaryDetails?.beneficiaryName,
                ),
                readOnly: true,
                hint: 'Name',
                label: CommonText(
                  text: 'Name',
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
                      userRound,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
              ).paddingOnly(top: 4, bottom: 8),
              AppTextField(
                controller: TextEditingController(
                  text: beneficiaryDetails?.pinCode,
                ),
                readOnly: true,
                hint: 'Pincode',
                label: CommonText(
                  text: 'Pincode',
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
                      icMapPin,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
              ),

              // AppIconTextfield(
              //   isDisabled: true,
              //   icon: icMapPin,
              //   titleHeaderString: "Pincode",
              //   controller: TextEditingController(
              //     text: beneficiaryDetails?.pinCode,
              //   ),
              // ),
              const SizedBox(height: 8),
              // AppIconTextfield(
              //   isDisabled: true,
              //   icon: icMapPin,
              //   titleHeaderString: "Area",
              //   controller: TextEditingController(text: beneficiaryDetails?.area),
              // ),
              AppTextField(
                controller: TextEditingController(text: beneficiaryDetails?.area),
                readOnly: true,
                hint: 'Area',
                label: CommonText(
                  text: 'Area',
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
                      icMapPin,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 8),
              // AppIconTextfield(
              //   isDisabled: true,
              //   textInputType: TextInputType.multiline,
              //   icon: icMapPin,
              //   titleHeaderString: "Address",
              //   controller: TextEditingController(
              //     text: beneficiaryDetails?.address,
              //   ),
              // ),
              AppTextField(
                controller: TextEditingController(
                  text: beneficiaryDetails?.address,
                ),
                readOnly: true,
                textInputType: TextInputType.multiline,
                hint: 'Address',
                label: CommonText(
                  text: 'Address',
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
                      icMapPin,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                minLines: 2,
                maxLines: 4,
              ),

              const SizedBox(height: 8),
              Row(
                mainAxisAlignment: MainAxisAlignment.end,
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  SizedBox(
                    width: responsiveWidth(150),
                    child: AppActiveButton(
                      buttontitle: "View Details",
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder:
                                (context) => AssignTeamForCTSampleCollection(
                                  beneficiaryDetails: beneficiaryDetails,
                                ),
                          ),
                        );
                      },
                    ),
                  ),
                ],
              ),
              // const SizedBox(height: 4),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    "Team & Camp Details",
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
              AppTextField(
                controller: TextEditingController(
                  text: beneficiaryDetails?.campId.toString() ?? '',
                ),
                readOnly: true,
                hint: 'Camp ID',
                label: CommonText(
                  text: 'Camp ID',
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
                      icnTent,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
              ),
              // AppIconTextfield(
              //   icon: icnTent,
              //   enabled: false,
              //   titleHeaderString: "Camp ID",
              //   controller: TextEditingController(
              //     text: "${beneficiaryDetails?.campId ?? ""}",
              //   ),
              // ),
              const SizedBox(height: 10),
              Row(
                children: [
                  Expanded(
                    child: AppTextField(
                      controller: TextEditingController(
                        text: beneficiaryDetails?.campType ?? "",
                      ),
                      readOnly: true,
                      hint: 'Camp Date',
                      label: CommonText(
                        text: 'Camp Date',
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
                            icnTent,
                            height: 24.h,
                            width: 24.w,
                            fit: BoxFit.contain,
                          ),
                        ),
                      ),
                    ),

                    // AppIconTextfield(
                    //   icon: icnTent,
                    //   enabled: false,
                    //   titleHeaderString: "Camp Type",
                    //   controller: TextEditingController(
                    //     text: beneficiaryDetails?.campType ?? "",
                    //   ),
                    // ),
                  ),
                  const SizedBox(width: 10),
                  Expanded(
                    child: AppTextField(
                      controller: TextEditingController(
                        text: beneficiaryDetails?.campDate ?? "",
                      ),
                      readOnly: true,
                      hint: 'Camp Date',
                      label: CommonText(
                        text: 'Camp Date',
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

                    // AppIconTextfield(
                    //   icon: icCalendarMonth,
                    //   enabled: false,
                    //   titleHeaderString: "Camp Date",
                    //   controller: TextEditingController(
                    //     text: beneficiaryDetails?.campDate ?? "",
                    //   ),
                    // ),
                  ),
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
                      offset: Offset(0, 1),
                      color: Colors.black.withValues(alpha: 0.15),
                      spreadRadius: 0,
                      blurRadius: 4,
                    ),
                  ],
                ),
                padding: EdgeInsets.all(14),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "${beneficiaryDetails?.teamname ?? ""} / IsTeamActive - ${beneficiaryDetails?.isTeamActive ?? ""}",
                      style: TextStyle(
                        color: kBlackColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w600,
                        fontSize: responsiveFont(14),
                      ),
                    ),
                    const SizedBox(height: 8),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        SizedBox(
                          width: responsiveHeight(20),
                          height: responsiveHeight(20),
                          child: Image.asset(icInitiatedBy),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Expanded(
                                child: Text(
                                  "${beneficiaryDetails?.member1 ?? ""} (${beneficiaryDetails?.member1MOB ?? ""})",
                                  style: TextStyle(
                                    color: dropDownTitleHeader,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w400,
                                    fontSize: responsiveFont(14),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        SizedBox(
                          width: responsiveHeight(20),
                          height: responsiveHeight(20),
                          child: Image.asset(icInitiatedBy),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Expanded(
                                child: Text(
                                  "${beneficiaryDetails?.member2 ?? ""} (${beneficiaryDetails?.member2MOB ?? ""})",
                                  style: TextStyle(
                                    color: dropDownTitleHeader,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w400,
                                    fontSize: responsiveFont(14),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              isShowAssignButton == true
                  ? const SizedBox(height: 10)
                  : Container(),
              isShowAssignButton == true
                  ? Container(
                    width: SizeConfig.screenWidth,
                    padding: EdgeInsets.all(8),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Expanded(
                          child: Container(
                            color: Colors.transparent,
                            child: Row(
                              children: [
                                GestureDetector(
                                  onTap: () {
                                    isSelectedAssignD2DTeam = true;
                                    isShowTeamDropDown = true;
                                    isShowExecutiveDropDown = false;
                                    selectedTeam = null;
                                    setState(() {});
                                  },
                                  child: SizedBox(
                                    width: 20,
                                    height: 20,
                                    child: Image.asset(
                                      isSelectedAssignD2DTeam == true
                                          ? icRadioSelected
                                          : icUnRadioSelected,
                                    ),
                                  ),
                                ),
                                const SizedBox(width: 4),
                                Text(
                                  "Assign D2D Team",
                                  style: TextStyle(
                                    color:
                                        isSelectedAssignD2DTeam == true
                                            ? kBlackColor
                                            : dropDownTitleHeader,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight:
                                        isSelectedAssignD2DTeam == true
                                            ? FontWeight.w600
                                            : FontWeight.w400,
                                    fontSize: responsiveFont(14),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                        const SizedBox(width: 10),
                        Expanded(
                          child: Container(
                            color: Colors.transparent,
                            child: Row(
                              children: [
                                GestureDetector(
                                  onTap: () {
                                    isSelectedAssignD2DTeam = false;
                                    isShowTeamDropDown = false;
                                    isShowExecutiveDropDown = true;
                                    setState(() {});
                                  },
                                  child: SizedBox(
                                    width: 20,
                                    height: 20,
                                    child: Image.asset(
                                      isSelectedAssignD2DTeam == false
                                          ? icRadioSelected
                                          : icUnRadioSelected,
                                    ),
                                  ),
                                ),
                                const SizedBox(width: 4),
                                Text(
                                  "Assign User",
                                  style: TextStyle(
                                    color:
                                        isSelectedAssignD2DTeam == false
                                            ? kBlackColor
                                            : dropDownTitleHeader,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight:
                                        isSelectedAssignD2DTeam == false
                                            ? FontWeight.w600
                                            : FontWeight.w400,
                                    fontSize: responsiveFont(14),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      ],
                    ),
                  )
                  : Container(),
              isSelectedAssignD2DTeam == true
                  ? const SizedBox(height: 4)
                  : Container(),
              isSelectedAssignD2DTeam == true
                  ? Container(
                    padding: EdgeInsets.fromLTRB(8, 0, 8, 8),
                    child: Row(
                      children: [
                        GestureDetector(
                          onTap: () {
                            isShowAllTeams = !isShowAllTeams;
                            selectedTeam = null;
                            setState(() {});
                          },
                          child: SizedBox(
                            width: 20,
                            height: 20,
                            child: Image.asset(
                              isShowAllTeams == false
                                  ? icUnCheckBoxSelected
                                  : icCheckBoxSelected,
                            ),
                          ),
                        ),
                        const SizedBox(width: 4),
                        Text(
                          "Show All Teams",
                          style: TextStyle(
                            color: kBlackColor,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w600,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                      ],
                    ),
                  )
                  : Container(),
              // isShowTeamDropDown == true
              //     ? const SizedBox(height: 10)
              //     : Container(),
              isShowTeamDropDown == true
                  ? AppTextField(
                    controller: TextEditingController(
                      text: selectedTeam?.teamName ?? "",
                    ),
                    readOnly: true,
                    hint: 'Select Team',
                    label: CommonText(
                      text: 'Select Team',
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
                          icUsersGroup,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                    suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                    onTap: () {
                      getTeamsDropDown();
                    },
                  )
                  // AppDropdownTextfield(
                  //       icon: icUsersGroup,
                  //       titleHeaderString: "Select Team",
                  //       valueString: selectedTeam?.teamName ?? "",
                  //       onTap: () {
                  //         getTeamsDropDown();
                  //       },
                  //     )
                  : Container(),
              isShowExecutiveDropDown == true
                  ? const SizedBox(height: 10)
                  : Container(),
              isShowExecutiveDropDown == true
                  ? AppTextField(
                    controller: TextEditingController(
                      text: selectedExecutive?.uSERNAME ?? "",
                    ),
                    readOnly: true,
                    hint: 'Select Executive',
                    label: CommonText(
                      text: 'Select Executive',
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
                          icUsersGroup,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                    suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                    onTap: () {
                      if (widget.selectedCT.arId != 4) {
                        getExecutiveDropDown();
                      }
                    },
                  )
                  // AppDropdownTextfield(
                  //       icon: icUsersGroup,
                  //       titleHeaderString: "Select Executive",
                  //       valueString: selectedExecutive?.uSERNAME ?? "",
                  //       onTap: () {
                  //         getExecutiveDropDown();
                  //       },
                  //     )
                  : Container(),
              Visibility(
                visible: isUserAlreadyAssigned,
                child: Align(
                  alignment: Alignment.centerLeft,
                  child: CommonText(
                    text: "User Already Assigned *",
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
                  buttontitle: "Assign",
                  onTap: () {
                    if (widget.selectedCT.arId != 4) {
                      submitData();
                    }
                  },
                ),
              ),
            ],
          ),
        ).paddingSymmetric(vertical: 12, horizontal: 8),
      ),
    );
  }

  BeneficiaryDetailsforAssignTeamidOutput? beneficiaryDetails;

  void getTeamDetailsListForAssign() {
    Map<String, String> params = {
      "Pincode": widget.selectedCT.pinCode ?? "",
      "USERID": empCode.toString(),
    };
    apiManager.getRecollectionTeamDetialsAPI(
      params,
      apiRecollectionTeamDetialsCallBack,
    );
  }

  void apiRecollectionTeamDetialsCallBack(
    SelectedTeamsDataListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      showAppointmentTeamBottomSheet(response?.output ?? []);
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void showAppointmentTeamBottomSheet(List<SelectedTeamsDataLisOutput> list) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: true,
      enableDrag: true,
      builder: (BuildContext context) {
        return Container(
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
            list: list,
            onTapTeam: (p0) {
              selectedTeam = p0;
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  @override
  void getDependentInfo() {
    ToastManager.showLoader();
    Map<String, String> param = {
      "Regdid": widget.selectedCT.regdid.toString(),
      "Regdno": widget.selectedCT.regdno ?? "0",
    };

    apiManager.getDependentInfoAPI(param, apiDependentInfoCallBack);
  }

  void apiDependentInfoCallBack(
    BeneficiaryDetailsforAssignTeamidDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      beneficiaryDetails = response?.output?.first;
      isUserAlreadyAssigned =
          (beneficiaryDetails?.isTeamAssign ?? "").toLowerCase() == "yes";

      selectedExecutive = T2TCTUserDetailsOutput(
        uSERID: beneficiaryDetails?.uSERID ?? 0,
        uSERMOBNO: beneficiaryDetails?.mOBNO,
        uSERNAME: beneficiaryDetails?.uSERNAME,
      );

      selectedTeam = SelectedTeamsDataLisOutput(
        labCode: beneficiaryDetails?.landingLab,
        labName: beneficiaryDetails?.landingLabName,
        member1: beneficiaryDetails?.member1,
        member1MOB: beneficiaryDetails?.member1MOB,
        member2: beneficiaryDetails?.member1,
        member2MOB: beneficiaryDetails?.member1MOB,
        memberUserID1: beneficiaryDetails?.memberUserID1,
        memberUserID2: beneficiaryDetails?.memberUserID2,
        teamID: beneficiaryDetails?.teamid,
        teamid: beneficiaryDetails?.teamid,
        teamName: beneficiaryDetails?.teamname,
        teamNumber: beneficiaryDetails?.teamname,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getTeamsDropDown() {
    if (isShowAllTeams) {
      getTeamsList("0");
    } else {
      getTeamsList(beneficiaryDetails?.pinCode ?? "0");
    }
  }

  void getTeamsList(String pincode) {
    ToastManager.showLoader();
    Map<String, String> param = {
      "Pincode": pincode,
      "USERID": empCode.toString(),
    };

    apiManager.getT2TTeamDetailsByPincodeAPI(param, apiTeamsCallBack);
  }

  void apiTeamsCallBack(
    SelectedTeamsDataListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      showAppointmentTeamBottomSheet(response?.output ?? []);
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getExecutiveDropDown() {
    ToastManager.showLoader();
    Map<String, String> param = {
      "DISTLGDCODE": beneficiaryDetails?.dISTLGDCODE.toString() ?? "0",
      "Pincode": "0",
    };

    apiManager.getT2TCTUserDetailsAPI(param, apiExecutiveCallBack);
  }

  void apiExecutiveCallBack(
    T2TCTUserDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Select Executive",
        response?.output ?? [],
        DropDownTypeMenu.SelectExecutive,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void _showDropDownBottomSheet(
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
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
            onApplyTap: (p0) {
              if (dropDownType == DropDownTypeMenu.SelectExecutive) {
                selectedExecutive = p0;
              }

              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  void submitData() {
    List<Map<String, dynamic>> jsonArray = [];

    if (isSelectedAssignD2DTeam) {
      if (selectedTeam == null) {
        ToastManager.toast("Please select team");
        return;
      } else {
        jsonArray.add({
          "USERID": selectedTeam?.memberUserID1 ?? 0,
          "Teamid": selectedTeam?.teamID ?? 0,
        });

        jsonArray.add({
          "USERID": selectedTeam?.memberUserID2 ?? 0,
          "Teamid": selectedTeam?.teamID ?? 0,
        });
      }
    } else {
      if (selectedExecutive == null) {
        ToastManager.toast("Please Select Resource");
        return;
      } else {
        jsonArray.add({"USERID": selectedExecutive?.uSERID ?? 0, "Teamid": 0});
      }
    }

    String jsonString = jsonToString(jsonArray);
    ToastManager.showLoader();
    Map<String, String> param = {
      "Regdid": widget.selectedCT.regdid.toString(),
      "Campid": widget.selectedCT.campId.toString(),
      "T2T_CT_TeamBene": jsonString,
      "AssignedBy": empCode.toString(),
    };

    apiManager.insertT2TCTTeamandBeneficiaryMappingAPI(
      param,
      apiInsertT2TCTTeamandBeneficiaryMappingCallBack,
    );
  }

  void apiInsertT2TCTTeamandBeneficiaryMappingCallBack(
    T2TCTTeamandBeneficiaryResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      ToastManager.showSuccessPopup(
        context,
        icSuccessIcon,
        "User Assigned successfully",(){
        Get.back();
        Get.back();
      }
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  String jsonToString(List<Map<String, dynamic>> items) {
    try {
      String jsonString = jsonEncode(items);
      return jsonString;
    } catch (e) {
      return "";
    }
  }
}
