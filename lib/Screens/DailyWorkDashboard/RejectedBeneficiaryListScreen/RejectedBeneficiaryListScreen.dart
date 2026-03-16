// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/widgets/AppDropdownTextfield.dart';
import 'package:s2toperational/Modules/widgets/AppIconSearchTextfield.dart';
import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/Json_Class/RecollectionAssignmentRemarksResponse/RecollectionAssignmentRemarksResponse.dart';
import '../../../Modules/Json_Class/RecollectionBeneficiaryStatusandDetailsCountV1Response/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../../../Views/DropDownListScreen/DropDownListScreen.dart';
import 'RejectedBeneficiaryInfoScreen/RejectedBeneficiaryInfoScreen.dart';
import 'RejectedBeneficiaryRow/RejectedBeneficiaryRow.dart';
import '../../../../../Modules/constants/fonts.dart';

class RejectedBeneficiaryListScreen extends StatefulWidget {
  RejectedBeneficiaryListScreen({
    super.key,
    required this.fromDate,
    required this.toDate,
    required this.oganizationId,
    required this.divisionId,
    required this.dISTLGDCODE,
    required this.tALLGDCODE,
    required this.landingLabId,
    required this.campType,
    required this.searchFilterId,
  });

  String fromDate = "";
  String toDate = "";
  String oganizationId = "";
  String divisionId = "";
  String dISTLGDCODE = "";
  String tALLGDCODE = "";
  String landingLabId = "";
  String campType = "";
  String searchFilterId;
  @override
  State<RejectedBeneficiaryListScreen> createState() =>
      _RejectedBeneficiaryListScreenState();
}

class _RejectedBeneficiaryListScreenState
    extends State<RejectedBeneficiaryListScreen> {
  RecollectionAssignmentRemarksOutput? selectedStatus;
  RecollectionAssignmentRemarksOutput? selectedSearchBy;
  int dESGID = 0;
  int empCode = 0;
  int labCode = 0;
  int tALLGDCODE = 0;
  String areaString = "[]";
  String pinCode = "[]";
  APIManager apiManager = APIManager();
  List<RecollectionBeneficiaryStatusandDetailsCountV1Output>
  rejectedBeneficaryList = [];
  List<RecollectionBeneficiaryStatusandDetailsCountV1Output>
  searchRejectedBeneficaryList = [];

  List<RecollectionAssignmentRemarksOutput> searcyByList = [];

  TextEditingController searchTextEditingController = TextEditingController();

  @override
  void initState() {
    super.initState();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    labCode = DataProvider().getParsedUserData()?.output?.first.labCode ?? 0;
    tALLGDCODE =
        DataProvider().getParsedUserData()?.output?.first.tALLGDCODE ?? 0;

    searcyByList.add(
      RecollectionAssignmentRemarksOutput(
        arId: 1,
        assignmentRemarks: "Beneficiary Name",
      ),
    );
    searcyByList.add(
      RecollectionAssignmentRemarksOutput(
        arId: 2,
        assignmentRemarks: "Pincode",
      ),
    );
    searcyByList.add(
      RecollectionAssignmentRemarksOutput(arId: 3, assignmentRemarks: "Taluka"),
    );
    searcyByList.add(
      RecollectionAssignmentRemarksOutput(arId: 4, assignmentRemarks: "Area"),
    );
    searcyByList.add(
      RecollectionAssignmentRemarksOutput(
        arId: 5,
        assignmentRemarks: "Beneficiary Number",
      ),
    );

    if (dESGID.toString() == "86" ||
        dESGID.toString() == "64" ||
        dESGID.toString() == "35" ||
        dESGID.toString() == "129" ||
        dESGID.toString() == "146") {
      selectedStatus = RecollectionAssignmentRemarksOutput(
        arId: 0,
        assignmentRemarks: "All",
      );
    } else {
      selectedStatus = RecollectionAssignmentRemarksOutput(
        arId: 2,
        assignmentRemarks: "Assignment Pending",
      );
    }
    callAPICall();
  }

  callAPICall() {
    ToastManager.showLoader();
    if (dESGID.toString() == "86" ||
        dESGID.toString() == "64" ||
        dESGID.toString() == "35" ||
        dESGID.toString() == "129" ||
        dESGID.toString() == "146") {
      if (widget.searchFilterId == "0") {
        getCountForPageloadForTeam();
      } else {
        getCountForTeam();
      }
    } else {
      if (widget.searchFilterId == "0") {
        getCountForPageload();
      } else {
        getCount();
      }
    }
  }

  getStatus() {
    if (dESGID.toString() == "86" ||
        dESGID.toString() == "64" ||
        dESGID.toString() == "35" ||
        dESGID.toString() == "129" ||
        dESGID.toString() == "146") {
      getStatusList("3");
    } else {
      getStatusList("0");
    }
  }

  getStatusList(String type) {
    Map<String, String> params = {"Type": type};

    apiManager.getRecollectionAssignmentRemarksAPI(
      params,
      apiRecollectionAssignmentRemarksCallBack,
    );
  }

  void apiRecollectionAssignmentRemarksCallBack(
    RecollectionAssignmentRemarksResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Status",
        response?.output ?? [],
        DropDownTypeMenu.RejectedStatus,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  getCountForPageloadForTeam() {
    Map<String, String> param = {
      "Fromdate": widget.fromDate,
      "Todate": widget.toDate,
      "SubOrgId": widget.oganizationId,
      "DIVID": "0",
      "DISTLGDCODE": widget.dISTLGDCODE,
      "TALLGDCODE": "0",
      "Labcode": "0",
      "Arid": selectedStatus?.arId.toString() ?? "0",
      "BeneficiaryNumber": "0",
      "UserId": empCode.toString(),
      "Type": "2",
    };

    apiManager.getCountForPageloadForTeamV1API(
      param,
      apiCountForPageloadForTeamCallBack,
    );
  }

  void apiCountForPageloadForTeamCallBack(
    RecollectionBeneficiaryStatusandDetailsCountV1Response? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      rejectedBeneficaryList = response?.output ?? [];
      searchRejectedBeneficaryList = rejectedBeneficaryList;
    } else {
      rejectedBeneficaryList = [];
      searchRejectedBeneficaryList = rejectedBeneficaryList;
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  List<RecollectionBeneficiaryStatusandDetailsCountV1Output> searchByDescEn(
    String query,
  ) {
    if (selectedSearchBy?.arId == 1) {
      return rejectedBeneficaryList.where((item) {
        final desc = item.beneficiaryName?.toString().toLowerCase() ?? '';
        return desc.contains(query.toLowerCase());
      }).toList();
    } else if (selectedSearchBy?.arId == 2) {
      return rejectedBeneficaryList.where((item) {
        final desc = item.pincode?.toString().toLowerCase() ?? '';
        return desc.contains(query.toLowerCase());
      }).toList();
    } else if (selectedSearchBy?.arId == 3) {
      return rejectedBeneficaryList.where((item) {
        final desc = item.taluka?.toString().toLowerCase() ?? '';
        return desc.contains(query.toLowerCase());
      }).toList();
    } else if (selectedSearchBy?.arId == 4) {
      return rejectedBeneficaryList.where((item) {
        final desc = item.area?.toString().toLowerCase() ?? '';
        return desc.contains(query.toLowerCase());
      }).toList();
    }

    return rejectedBeneficaryList.where((item) {
      final desc = item.beneficiaryNumber?.toString().toLowerCase() ?? '';
      return desc.contains(query.toLowerCase());
    }).toList();
  }

  getCountForPageload() {
    Map<String, String> param = {
      "Fromdate": widget.fromDate,
      "Todate": widget.toDate,
      "SubOrgId": widget.oganizationId,
      "DIVID": "0",
      "DISTLGDCODE": widget.dISTLGDCODE,
      "TALLGDCODE": tALLGDCODE.toString(),
      "Labcode": labCode.toString(),
      "Arid": selectedStatus?.arId.toString() ?? "0",
      "BeneficiaryNumber": "0",
      "UserId": empCode.toString(),
      "Type": "2",
      "DesgID": dESGID.toString(),
      "CampType": widget.campType,
    };

    apiManager.getCountForPageloadVaAPI(
      param,
      apiCountForPageloadForTeamCallBack,
    );
  }

  getCount() {
    Map<String, String> param = {
      "Fromdate": widget.fromDate,
      "Todate": widget.toDate,
      "SubOrgId": "0",
      "DIVID": "0",
      "DISTLGDCODE": "0",
      "TALLGDCODE": "0",
      "Labcode": widget.landingLabId,
      "Arid": "0",
      "BeneficiaryNumber": "0",
      "UserId": empCode.toString(),
      "Type": "1",
      "CampType": widget.campType,
    };

    apiManager.getCountV1API(param, apiCountForPageloadForTeamCallBack);
  }

  getCountForTeam() {
    Map<String, String> param = {
      "Fromdate": widget.fromDate,
      "Todate": widget.toDate,
      "SubOrgId": widget.oganizationId,
      "DIVID": widget.divisionId,
      "DISTLGDCODE": widget.dISTLGDCODE,
      "TALLGDCODE": widget.tALLGDCODE,
      "Labcode": widget.landingLabId,
      "T_AreaofPincode": areaString,
      "T_PincodeofArea": pinCode,
      "Arid": selectedStatus?.arId.toString() ?? "0",
      "BeneficiaryNumber": "0",
      "UserId": empCode.toString(),
      "Type": "2",
    };

    apiManager.getCountForTeamPI(param, apiCountForPageloadForTeamCallBack);
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
              if (dropDownType == DropDownTypeMenu.RejectedStatus) {
                selectedStatus = p0;
                callAPICall();
              } else if (dropDownType == DropDownTypeMenu.SearchBy) {
                selectedSearchBy = p0;
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

  void showRejectedBeneficiaryInfoScreenBottomSheet(
    RecollectionBeneficiaryStatusandDetailsCountV1Output obj,
  ) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder:
            (context) => RejectedBeneficiaryInfoScreen(
              obj: obj,
              fromDate: widget.fromDate,
              toDate: widget.toDate,
              oganizationId: widget.oganizationId,
              divisionId: widget.divisionId,
              dISTLGDCODE: widget.dISTLGDCODE,
              tALLGDCODE: widget.tALLGDCODE,
              landingLabId: widget.landingLabId,
              campType: widget.campType,
              statusType: selectedStatus?.arId.toString() ?? "0",
            ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Rejected Beneficiary List",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Stack(
            children: [
              Positioned(
                top: 74,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect4,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(300.37),
                ),
              ),
              Positioned(
                top: 53,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect3,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(300.37),
                ),
              ),
              Positioned(
                top: 30,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect2,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(300.37),
                ),
              ),
              Image.asset(
                fit: BoxFit.fill,
                rect1,
                width: SizeConfig.screenWidth,
                height: responsiveHeight(300.37),
              ),
              Positioned(
                top: 0,
                bottom: 8,
                left: 8,
                right: 8,
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(8, 0, 8, 8),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const SizedBox(height: 10),
                      Container(
                        width: SizeConfig.screenWidth,
                        padding: EdgeInsets.all(10),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(10),
                          boxShadow: [
                            BoxShadow(
                              offset: Offset(0, 1),
                              color: Colors.black.withValues(alpha: 0.15),
                              spreadRadius: 0,
                              blurRadius: 10,
                            ),
                          ],
                        ),
                        child: Column(
                          children: [
                            AppDropdownTextfield(
                              icon: icnTent,
                              titleHeaderString: "Status*",
                              valueString:
                                  selectedStatus?.assignmentRemarks ?? "",
                              onTap: () {
                                ToastManager.showLoader();
                                getStatus();
                              },
                            ),
                            const SizedBox(height: 10),
                            AppDropdownTextfield(
                              icon: icSearch,
                              titleHeaderString: "Search By*",
                              valueString:
                                  selectedSearchBy?.assignmentRemarks ?? "",
                              onTap: () {
                                _showDropDownBottomSheet(
                                  "Select Search By",
                                  searcyByList,
                                  DropDownTypeMenu.SearchBy,
                                );
                              },
                            ),
                            const SizedBox(height: 10),
                            AppIconSearchTextfield(
                              icon: icSearch,
                              titleHeaderString: "Search Value*",
                              controller: searchTextEditingController,
                              onChange: (value) {
                                searchRejectedBeneficaryList = searchByDescEn(
                                  value,
                                );
                                setState(() {});
                              },
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 10),
                      Expanded(
                        child: Container(
                          color: Colors.transparent,
                          child: Column(
                            children: [
                              Container(
                                decoration: BoxDecoration(
                                  color: kPrimaryColor,
                                  borderRadius: BorderRadius.only(
                                    topLeft: Radius.circular(8),
                                    topRight: Radius.circular(8),
                                  ),
                                ),
                                height: 40,
                                child: Row(
                                  children: [
                                    Container(
                                      width: 48,
                                      decoration: BoxDecoration(
                                        color: Colors.transparent,
                                        border: Border(
                                          right: BorderSide(
                                            color: kBlackColor,
                                            width: 1,
                                          ),
                                        ),
                                      ),
                                      child: Center(
                                        child: Text(
                                          "Sr. No.",
                                          textAlign: TextAlign.center,
                                          style: TextStyle(
                                            color: kWhiteColor,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(13),
                                          ),
                                        ),
                                      ),
                                    ),
                                    Expanded(
                                      child: Container(
                                        decoration: BoxDecoration(
                                          color: Colors.transparent,
                                          border: Border(
                                            right: BorderSide(
                                              color: kBlackColor,
                                              width: 1,
                                            ),
                                          ),
                                        ),
                                        padding: EdgeInsets.fromLTRB(
                                          4,
                                          0,
                                          4,
                                          0,
                                        ),
                                        child: Center(
                                          child: Text(
                                            "Beneficiary Name",
                                            textAlign: TextAlign.left,
                                            style: TextStyle(
                                              color: kWhiteColor,
                                              fontFamily:
                                                  FontConstants.interFonts,
                                              fontWeight: FontWeight.w500,
                                              fontSize: responsiveFont(13),
                                            ),
                                          ),
                                        ),
                                      ),
                                    ),
                                    Container(
                                      width: 68,
                                      decoration: BoxDecoration(
                                        color: Colors.transparent,
                                        border: Border(
                                          right: BorderSide(
                                            color: kBlackColor,
                                            width: 1,
                                          ),
                                        ),
                                      ),
                                      child: Center(
                                        child: Text(
                                          "Area",
                                          textAlign: TextAlign.center,
                                          style: TextStyle(
                                            color: kWhiteColor,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(13),
                                          ),
                                        ),
                                      ),
                                    ),
                                    Container(
                                      width: 84,
                                      color: Colors.transparent,
                                      child: Center(
                                        child: Text(
                                          "Pin code",
                                          textAlign: TextAlign.center,
                                          style: TextStyle(
                                            color: kWhiteColor,
                                            fontFamily:
                                                FontConstants.interFonts,
                                            fontWeight: FontWeight.w500,
                                            fontSize: responsiveFont(13),
                                          ),
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ),

                              Expanded(
                                child: ListView.builder(
                                  itemCount:
                                      searchRejectedBeneficaryList.length,
                                  itemBuilder: (context, index) {
                                    RecollectionBeneficiaryStatusandDetailsCountV1Output
                                    obj = searchRejectedBeneficaryList[index];
                                    return GestureDetector(
                                      onTap: () {
                                        showRejectedBeneficiaryInfoScreenBottomSheet(
                                          obj,
                                        );
                                      },
                                      child: RejectedBeneficiaryRow(
                                        index: index,
                                        obj: obj,
                                      ),
                                    );
                                  },
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                      // const SizedBox(height: 6),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
