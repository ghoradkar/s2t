// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Screens/CTAssignment/CTAssignmentScreen/CTAssignmentRow/CTAssignmentRow.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';

import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/AssignmentRemarksResponse/AssignmentRemarksResponse.dart';
import '../../../Modules/Json_Class/DistrictResponse/DistrictResponse.dart';
import '../../../Modules/Json_Class/T2TCTBeneficiaryDetailsResponse/T2TCTBeneficiaryDetailsResponse.dart';
import '../../../Modules/Json_Class/TalukaCampCreationResponse/TalukaCampCreationResponse.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../AssignTeamForCTDetails/AssignTeamForCTDetails.dart';
import 'CTAssignmentFilterView/CTAssignmentFilterView.dart';

class CTAssignmentScreen extends StatefulWidget {
  const CTAssignmentScreen({super.key});

  @override
  State<CTAssignmentScreen> createState() => _CTAssignmentScreenState();
}

class _CTAssignmentScreenState extends State<CTAssignmentScreen> {
  TextEditingController searchBeneficiaryNamePincodeTextField =
      TextEditingController();
  APIManager apiManager = APIManager();

  String fromDateString = "2020/09/01";
  String toDateString = "2025/09/01";
  DistrictOutput? selectedDistrict;
  TalukaCampCreationOutput? selectedTaluka;
  String pinCode = "";
  AssignmentRemarksOutput? selectedStatusRemark;

  int empCode = 0;

  List<T2TCTBeneficiaryDetailsOutput> list = [];
  List<T2TCTBeneficiaryDetailsOutput> searchList = [];


  @override
  void initState() {
    super.initState();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    int dISTLGDCODE =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
    String district =
        DataProvider().getParsedUserData()?.output?.first.district ?? "";
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    selectedStatusRemark = AssignmentRemarksOutput(
      arId: 2,
      assignmentRemarks: "Assignment Pending",
    );
    selectedDistrict = DistrictOutput(
      dISTLGDCODE: dISTLGDCODE,
      dISTNAME: district,
    );
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    DateTime toDate = DateTime.now();
    DateTime fromDate = toDate.add(const Duration(days: -3));
    fromDateString = FormatterManager.formatDateToString(fromDate);
    toDateString = FormatterManager.formatDateToString(toDate);

    getPostCampDetails();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Assign Team for CT",
          showActions: true,
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
          actions: [
            Padding(
              padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
              child: GestureDetector(
                onTap: () {
                  showAAssignmentFilterBottomSheet();
                },
                child: SizedBox(
                  width: 20,
                  height: 20,
                  child: Image.asset(icFilter),
                ),
              ),
            ),
          ],
        ),
        body: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Column(
            children: [
              Padding(
                padding: const EdgeInsets.fromLTRB(8, 8, 8, 4),
                child: AppTextField(
                  readOnly: false,
                  controller: searchBeneficiaryNamePincodeTextField,
                  onChange: (p0) {
                    setState(() {
                      searchList = searchByDescEn(p0);
                    });
                  },
                  hint: 'Search Beneficiary Name / Pincode',
                  label: CommonText(
                    text: 'Search Beneficiary Name / Pincode',
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
                // AppIconSearchTextfield(
                //   icon: icSearch,
                //   titleHeaderString: "Search Beneficiary Name / Pincode",
                //   controller: searchBeneficiaryNamePincodeTextField,
                //   onChange: (p0) {
                //     setState(() {
                //       searchList = searchByDescEn(p0);
                //     });
                //   },
                // ),
              ),
              const SizedBox(height: 16),
              Expanded(
                child: ListView.builder(
                  itemCount: searchList.length,
                  itemBuilder: (context, index) {
                    T2TCTBeneficiaryDetailsOutput obj = searchList[index];
                    return CTAssignmentRow(
                      obj: obj,
                      onSelectTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder:
                                (context) =>
                                    AssignTeamForCTDetails(selectedCT: obj),
                          ),
                        ).then((value) {
                          searchBeneficiaryNamePincodeTextField.clear();
                          getPostCampDetails();
                        });
                      },
                    );
                  },
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }


  void showAAssignmentFilterBottomSheet() {
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
          height: MediaQuery.of(context).size.width * 1.58,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: CTAssignmentFilterView(
            onSelectedFromDte: (fromDate) {
              fromDateString = FormatterManager.formatDateToString(fromDate!);
            },
            onSelectedToDte: (toDate) {
              toDateString = FormatterManager.formatDateToString(toDate!);
            },
            onSelectedDistrict: (district) {
              selectedDistrict = district;
            },
            onSelectedTaluka: (taluka) {
              selectedTaluka = taluka;
            },
            onSelectedPinCode: (pincodeString) {
              pinCode = pincodeString;
            },
            onSelectedStatusRemark: (statusRemark) {
              selectedStatusRemark = statusRemark;
            },
            applyDidPressed: () {
              ToastManager.showLoader();
              getPostCampDetails();
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  void getPostCampDetails() {
    ToastManager.showLoader();
    Map<String, String> param = {
      "FROMDATE": fromDateString,
      "TODATE": toDateString,
      "USERID": "$empCode",
      "DISTLGDCODE": selectedDistrict?.dISTLGDCODE.toString() ?? "0",
      "TALLGDCODE": selectedTaluka?.tALLGDCODE.toString() ?? "0",
      "PINCODE": pinCode.isEmpty ? "0" : pinCode,
      "TYPE": selectedStatusRemark?.arId.toString() ?? "0",
    };

    apiManager.getPostCampDetailsAPI(param, apiPostCampDetailsCallBack);
  }

  void apiPostCampDetailsCallBack(
      T2TCTBeneficiaryDetailsResponse? response,
      String errorMessage,
      bool success,
      ) async {
    ToastManager.hideLoader();
    list = response?.output ?? [];
    searchList = list;
    if (success) {
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  List<T2TCTBeneficiaryDetailsOutput> searchByDescEn(String query) {
    final lowerQuery = query.toLowerCase();

    return list.where((item) {
      final name = item.beneficiaryName?.toString().toLowerCase() ?? '';
      final pin = item.pinCode?.toString().toLowerCase() ?? '';

      return name.contains(lowerQuery) || pin.contains(lowerQuery);
    }).toList();
  }
}
