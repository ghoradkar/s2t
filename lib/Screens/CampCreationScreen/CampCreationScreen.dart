// ignore_for_file: file_names, avoid_print

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';

import '../../Modules/Enums/Enums.dart';
import '../../Modules/FormatterManager/FormatterManager.dart';
import '../../Modules/Json_Class/CampTypeResponse/CampTypeResponse.dart';
import '../../Modules/Json_Class/HomeAndHubLabCampCreationResponse/HomeAndHubLabCampCreationResponse.dart';
import '../../Modules/Json_Class/InitiatedByResponse/InitiatedByResponse.dart';
import '../../Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
import '../../Modules/Json_Class/ScreeningTestCampCreationResponse/ScreeningTestCampCreationResponse.dart';
import '../../Modules/Json_Class/TalukaCampCreationResponse/TalukaCampCreationResponse.dart';
import '../../Modules/ToastManager/ToastManager.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/utilities/WidgetPaddingX.dart';
import '../../Modules/utilities/validators.dart';
import '../../Modules/widgets/AppActiveButton.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import '../../Views/DropDownListScreen/DropDownListScreen.dart';
import '../../Views/MultiSelectionDropDownListScreen/MultiSelectionDropDownListScreen.dart';

class CampCreationScreen extends StatefulWidget {
  const CampCreationScreen({super.key});

  @override
  State<CampCreationScreen> createState() => _CampCreationScreenState();
}

class _CampCreationScreenState extends State<CampCreationScreen> {
  TextEditingController campNameTextField = TextEditingController();
  TextEditingController campAddressTextField = TextEditingController();
  TextEditingController expectedBeneficiaryTextField = TextEditingController();

  int dESGID = 0;
  int empCode = 0;
  int districtId = 0;
  String districtName = "";
  String? _selectedCampDate;
  String? _selectedPostCampDate;

  CampTypeOutput? selectedCampType;
  InitiatedByOutput? selectedInitiatedBy;
  TalukaCampCreationOutput? selectedTaluka;
  LandingLabCampCreationOutput? selectedLandingLab;
  HomeAndHubLabCampCreationOutput? selectedHomeAndHubLab;
  List<ScreeningTestCampCreationOutput> selectedScreeningTest = [];

  ToastManager toastManager = ToastManager();
  APIManager apiManager = APIManager();

  Future<void> _selectCampDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        _selectedCampDate = FormatterManager.formatDateToString(picked);
        DateTime nextWeekDay = picked.add(const Duration(days: 7));
        _selectedPostCampDate = FormatterManager.formatDateToString(
          nextWeekDay,
        );
      });
    }
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
              if (dropDownType == DropDownTypeMenu.CampType) {
                selectedCampType = p0;
                selectedInitiatedBy = null;
                selectedTaluka = null;
                selectedLandingLab = null;
                selectedHomeAndHubLab = null;
                selectedScreeningTest = [];
                campNameTextField.text = "";
                campAddressTextField.text = "";
                _selectedCampDate = "";
                _selectedPostCampDate = "";
                expectedBeneficiaryTextField.text = "";
              } else if (dropDownType == DropDownTypeMenu.InitiatedBy) {
                selectedInitiatedBy = p0;
                selectedTaluka = null;
                selectedLandingLab = null;
                selectedHomeAndHubLab = null;
                selectedScreeningTest = [];
                campNameTextField.text = "";
                campAddressTextField.text = "";
                _selectedCampDate = "";
                _selectedPostCampDate = "";
                expectedBeneficiaryTextField.text = "";
              } else if (dropDownType == DropDownTypeMenu.TalukaCampList) {
                selectedTaluka = p0;
                selectedLandingLab = null;
                selectedHomeAndHubLab = null;
                selectedScreeningTest = [];
                campNameTextField.text = "";
                campAddressTextField.text = "";
                _selectedCampDate = "";
                _selectedPostCampDate = "";
                expectedBeneficiaryTextField.text = "";
              } else if (dropDownType ==
                  DropDownTypeMenu.LandingLabCampCreation) {
                selectedLandingLab = p0;
                selectedHomeAndHubLab = null;
                selectedScreeningTest = [];
                campNameTextField.text = "";
                campAddressTextField.text = "";
                _selectedCampDate = "";
                _selectedPostCampDate = "";
                expectedBeneficiaryTextField.text = "";
                getHomeAndHubLabAPI();
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

  void _showMultiSelectionDropDownBottomSheet(
    String title,
    List<dynamic> list,
    DropDownMultipleTypeMenu dropDownType,
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
          child: MultiSelectionDropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: (p0) {
              if (dropDownType == DropDownMultipleTypeMenu.ScreeningTest) {
                selectedScreeningTest = p0;
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

  void showAlertmessage(String campID) {
    ToastManager.showSuccessPopup(
      context,
      icSuccessIcon,
      "Camp Created Successfully.\nCamp ID $campID",
    );
  }

  String selectedScreeningTestSting() {
    List<String> tempScreeningTest = [];
    for (ScreeningTestCampCreationOutput screeningTest
        in selectedScreeningTest) {
      tempScreeningTest.add(screeningTest.testName ?? "");
    }
    return tempScreeningTest.join(",");
  }

  void campTypeAPI() {
    ToastManager.showLoader();
    if (DataProvider().getRegularCamp()) {
      apiManager.getCampTypeNonD2DRAPI(apiCampTypeNonD2CallBack);
    } else {
      if (dESGID == 108) {
        apiManager.getCampTypeFlexiAPI(apiCampTypeFlexiCallBack);
      } else if (dESGID == 136 || dESGID == 139) {
        apiManager.getCampTypeMMUAPI(apiCampTypeMMUCallBack);
      } else {
        apiManager.getCampTypeD2DAPI(apiCampTypeD2DCallBack);
      }
    }
  }

  void apiCampTypeNonD2CallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void apiCampTypeFlexiCallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void apiCampTypeMMUCallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void apiCampTypeD2DCallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getInitiatedByAPI() {
    ToastManager.showLoader();
    apiManager.getInitiatedByListForCampAPI(apiInitiatedByCallBack);
  }

  void apiInitiatedByCallBack(
    InitiatedByResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Initiated By",
        response?.output ?? [],
        DropDownTypeMenu.InitiatedBy,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getTalukaAPI() {
    ToastManager.showLoader();
    Map<String, String> data = {
      "STATELGDCODE": "2",
      "DISTLGDCODE": districtId.toString(),
    };
    apiManager.getTalukaAPI(data, apiTalukaCallBack);
  }

  void apiTalukaCallBack(
    TalukaCampCreationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Taluka",
        response?.output ?? [],
        DropDownTypeMenu.TalukaCampList,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getLandingLabAPI() {
    ToastManager.showLoader();
    Map<String, String> data = {"DISTLGDCODE": districtId.toString()};
    apiManager.getLandingLabAPI(data, apiLandingLabCallBack);
  }

  void apiLandingLabCallBack(
    LandingLabCampCreationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Landing Lab",
        response?.output ?? [],
        DropDownTypeMenu.LandingLabCampCreation,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getHomeAndHubLabAPI() {
    ToastManager.showLoader();
    Map<String, String> data = {
      "LabCode": selectedLandingLab?.labCode.toString() ?? "0",
      "TypeID": "0",
    };
    apiManager.getHomeLabHubLabAPI(data, apiHomeLabHubLabCallBack);
  }

  void apiHomeLabHubLabCallBack(
    HomeAndHubLabCampCreationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      selectedHomeAndHubLab = response?.output?.first;
      setState(() {});
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getScreeningTestAPI() {
    ToastManager.showLoader();
    apiManager.getScreeningTestAPI(apiScreeningTestCallBack);
  }

  void apiScreeningTestCallBack(
    ScreeningTestCampCreationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showMultiSelectionDropDownBottomSheet(
        "Screening Test",
        response?.output ?? [],
        DropDownMultipleTypeMenu.ScreeningTest,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void createCampCreation() {
    ToastManager.showLoader();
    String campName = campNameTextField.text.trim();
    String campAddress = campAddressTextField.text.trim();
    String expectedBeneficiary = expectedBeneficiaryTextField.text.trim();

    String campType = selectedCampType?.cAMPTYPE?.toString() ?? "0";
    String campTestMapping = dictionaryToString();
    String initiatedBy = selectedInitiatedBy?.iD.toString() ?? "0";
    String landingLab = selectedLandingLab?.labCode?.toString() ?? "0";
    String talukaCode = selectedTaluka?.tALLGDCODE?.toString() ?? "";

    Map<String, String> data = {
      "CampName": campName,
      "CampDate": _selectedCampDate ?? "",
      "PostCampDate": _selectedPostCampDate ?? "",
      "LABCODE": landingLab,
      "AffilatedHospitalId": "0",
      "Distlgdcode": districtId.toString(),
      "UserId": empCode.toString(),
      "CampLocation": campAddress,
      "CampTestMapping": campTestMapping,
      "CampType": campType,
      "InitiatedBy": initiatedBy,
      "LandingLab": landingLab,
      "CscpreapprovedId": "0",
      "TalukaCode": talukaCode,
      "ExpBenCount": expectedBeneficiary,
    };
    print(data);
    apiManager.createCampCreationAPI(data, apiCampCreationCallBack);
  }

  void apiCampCreationCallBack(
    LandingLabCampCreationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      showAlertmessage(response?.message ?? "");
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void saveDidPressed() {
    String campName = campNameTextField.text.trim();
    String campAddress = campAddressTextField.text.trim();
    String expectedBeneficiary = expectedBeneficiaryTextField.text.trim();
    if (selectedCampType == null) {
      toastManager.showAlertMessage(context, "Select Camp Type", Colors.red);
    } else if (selectedInitiatedBy == null) {
      toastManager.showAlertMessage(context, "Select Initiated By", Colors.red);
    } else if (districtName.isEmpty) {
      toastManager.showAlertMessage(context, "Select district", Colors.red);
    } else if (selectedTaluka == null) {
      toastManager.showAlertMessage(context, "Select taluka", Colors.red);
    } else if (selectedLandingLab == null) {
      toastManager.showAlertMessage(context, "Select Landing lab", Colors.red);
    } else if (campName.isEmpty) {
      toastManager.showAlertMessage(context, "Select camp name", Colors.red);
    } else if (campAddress.isEmpty) {
      toastManager.showAlertMessage(context, "Select Camp Address", Colors.red);
    } else if (campAddress.length <= 14) {
      toastManager.showAlertMessage(
        context,
        "Camp address should be grater than or equal to 15 character length",
        Colors.red,
      );
    } else if (_selectedCampDate == null) {
      toastManager.showAlertMessage(context, "Select camp date", Colors.red);
    } else if (selectedScreeningTest.isEmpty) {
      toastManager.showAlertMessage(
        context,
        "Select screening tests",
        Colors.red,
      );
    } else if (expectedBeneficiary.isEmpty) {
      toastManager.showAlertMessage(
        context,
        "Please Enter Expected Beneficiary",
        Colors.red,
      );
    } else if (UIValidator.isAllZeros(expectedBeneficiary)) {
      toastManager.showAlertMessage(
        context,
        "Please Enter valid Expected Beneficiary",
        Colors.red,
      );
    } else {
      createCampCreation();
    }
  }

  String dictionaryToString() {
    ToastManager.showLoader();
    List<Map<String, dynamic>> dataList = [];

    for (ScreeningTestCampCreationOutput obj in selectedScreeningTest) {
      Map<String, dynamic> dict = {
        "CampID": "0",
        "TestID": obj.testId ?? 0,
        "IsTestProcess": obj.isSelected ? "1" : "0",
        "IsActive": "1",
        "CreatedBy": empCode.toString(),
      };

      dataList.add(dict);
    }

    try {
      String json = jsonEncode(dataList);
      print(json);
      return json;
    } catch (e) {
      print("Something went wrong with parsing JSON: $e");
      return "";
    }
  }

  @override
  void initState() {
    super.initState();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    districtId =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
    districtName =
        DataProvider().getParsedUserData()?.output?.first.district ?? "";
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Camp Creation",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: SingleChildScrollView(
          child: Column(
            children: [
              AppTextField(
                controller: TextEditingController(
                  text: selectedCampType?.campTypeDescription ?? "",
                ),
                readOnly: true,
                onTap: () {
                  campTypeAPI();
                },
                hint: 'Camp Type',
                label: CommonText(
                  text: 'Camp Type',
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
                suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // AppDropdownTextfield(
              //   icon: icnTent,
              //   titleHeaderString: "Camp Type",
              //   valueString: selectedCampType?.campTypeDescription ?? "",
              //   onTap: () {
              //     campTypeAPI();
              //   },
              // ),
              // const SizedBox(height: 8),
              // AppDropdownTextfield(
              //   icon: icInitiatedBy,
              //   titleHeaderString: "Initiated By",
              //   valueString: selectedInitiatedBy?.initiatedBy ?? "",
              //   onTap: () {
              //     getInitiatedByAPI();
              //   },
              // ),
              AppTextField(
                controller: TextEditingController(
                  text: selectedInitiatedBy?.initiatedBy ?? "",
                ),
                readOnly: true,
                onTap: () {
                  getInitiatedByAPI();
                },
                hint: 'Initiated By',
                label: CommonText(
                  text: 'Initiated By',
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
                      icInitiatedBy,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // const SizedBox(height: 8),
              // AppDropdownTextfield(
              //   icon: icMapPin,
              //   titleHeaderString: "District",
              //   valueString: districtName,
              //   isDisabled: true,
              //   onTap: () {},
              // ),
              AppTextField(
                controller: TextEditingController(text: districtName),
                readOnly: true,
                hint: 'District',
                label: CommonText(
                  text: 'District',
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
              ).paddingOnly(top: 12),

              // const SizedBox(height: 8),
              AppTextField(
                controller: TextEditingController(
                  text: selectedTaluka?.tALNAME ?? "",
                ),
                readOnly: true,
                onTap: () {
                  getTalukaAPI();
                },
                hint: 'Taluka',
                label: CommonText(
                  text: 'Taluka',
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
                suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // AppDropdownTextfield(
              //   icon: icMapPin,
              //   titleHeaderString: "Taluka",
              //   valueString: selectedTaluka?.tALNAME ?? "",
              //   onTap: () {
              //     getTalukaAPI();
              //   },
              // ),
              // const SizedBox(height: 8),
              AppTextField(
                controller: TextEditingController(
                  text: selectedLandingLab?.labName ?? "",
                ),
                readOnly: true,
                onTap: () {
                  getLandingLabAPI();
                },
                hint: 'Landing Lab',
                label: CommonText(
                  text: 'Landing Lab',
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
                      icLandingLab,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // AppDropdownTextfield(
              //   icon: icLandingLab,
              //   titleHeaderString: "Landing Lab",
              //   valueString: selectedLandingLab?.labName ?? "",
              //   onTap: () {
              //     getLandingLabAPI();
              //   },
              // ),
              // const SizedBox(height: 8),
              // AppIconTextfield(
              //   icon: icnTent,
              //   titleHeaderString: "Camp Name",
              //   controller: campNameTextField,
              // ),
              // const SizedBox(height: 8),
              AppTextField(
                controller: campNameTextField,
                readOnly: true,
                onTap: () {
                  getLandingLabAPI();
                },
                hint: 'Camp Name',
                label: CommonText(
                  text: 'Camp Name',
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
                suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),
              AppTextField(
                controller: campAddressTextField,
                readOnly: false,
                hint: 'Camp Address',
                label: CommonText(
                  text: 'Camp Address',
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
                // suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // AppIconTextfield(
              //   icon: icMapPin,
              //   titleHeaderString: "Camp Address",
              //   controller: campAddressTextField,
              // ),
              // const SizedBox(height: 8),
              Container(
                width: MediaQuery.of(context).size.width,
                color: Colors.transparent,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Expanded(
                      child: AppTextField(
                        onTap: () {
                          _selectCampDate(context);
                        },
                        controller: TextEditingController(
                          text: _selectedCampDate,
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
                      ).paddingOnly(top: 12, left: 12),

                      // AppDateTextfield(
                      //   icon: icCalendarMonth,
                      //   titleHeaderString: "Camp Date",
                      //   valueString: _selectedCampDate ?? "",
                      //   onTap: () {
                      //     _selectCampDate(context);
                      //   },
                      // ),
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: AppTextField(
                        controller: TextEditingController(
                          text: _selectedPostCampDate,
                        ),
                        readOnly: true,
                        hint: 'Post Camp Date',
                        label: CommonText(
                          text: 'Post Camp Date',
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
                      ).paddingOnly(top: 12, right: 12),

                      // AppDateTextfield(
                      //   icon: icCalendarMonth,
                      //   titleHeaderString: "Post Camp Date",
                      //   valueString: _selectedPostCampDate ?? "",
                      //   onTap: () {},
                      // ),
                    ),
                  ],
                ),
              ),
              // const SizedBox(height: 8),
              // AppDropdownTextfield(
              //   icon: icScreeningTests,
              //   titleHeaderString: "Screening Tests",
              //   valueString: selectedScreeningTestSting(),
              //   onTap: () {
              //     getScreeningTestAPI();
              //   },
              // ),
              AppTextField(
                controller: TextEditingController(
                  text: selectedScreeningTestSting(),
                ),
                readOnly: true,
                onTap: () {
                  getScreeningTestAPI();
                },
                hint: 'Screening Tests',
                label: CommonText(
                  text: 'Screening Tests',
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
                      icScreeningTests,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // const SizedBox(height: 8),
              // AppDropdownTextfield(
              //   icon: icLandingLab,
              //   titleHeaderString: "Home Lab",
              //   valueString: selectedHomeAndHubLab?.homeLab ?? "",
              //   isDisabled: true,
              //   onTap: () {},
              // ),
              AppTextField(
                controller: TextEditingController(
                  text: selectedHomeAndHubLab?.homeLab ?? '',
                ),
                readOnly: true,

                hint: 'Home Lab',
                label: CommonText(
                  text: 'Home Lab',
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
                      icLandingLab,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                // suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),
              // const SizedBox(height: 8),
              // AppDropdownTextfield(
              //   icon: icLandingLab,
              //   titleHeaderString: "Hub Lab",
              //   valueString: selectedHomeAndHubLab?.hubLab ?? "",
              //   isDisabled: true,
              //   onTap: () {},
              // ),
              AppTextField(
                controller: TextEditingController(
                  text: selectedHomeAndHubLab?.hubLab ?? '',
                ),
                readOnly: true,

                hint: 'Hub Lab',
                label: CommonText(
                  text: 'Hub Lab',
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
                      icLandingLab,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                // suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // const SizedBox(height: 8),
              // AppIconTextfield(
              //   icon: icUsersGroup,
              //   titleHeaderString: "Expected Beneficiary",
              //   controller: expectedBeneficiaryTextField,
              //   textInputType: TextInputType.number,
              // ),
              AppTextField(
                controller: expectedBeneficiaryTextField,
                readOnly: true,

                hint: 'Expected Beneficiary',
                label: CommonText(
                  text: 'Expected Beneficiary',
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
                // suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              Row(
                children: [
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Cancel",
                      isCancel: true,
                      onTap: () {},
                    ),
                  ),
                  const SizedBox(width: 62),
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Save",
                      onTap: () {
                        saveDidPressed();
                      },
                    ),
                  ),
                ],
              ).paddingSymmetric(horizontal: 12, vertical: 14),
            ],
          ),
        ),
      ),
    );
  }
}
