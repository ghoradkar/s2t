// ignore_for_file: file_names, avoid_print, unused_element

import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/DeviceMappingManager/DeviceMappingManager.dart';
import '../../Modules/Enums/Enums.dart';
import '../../Modules/Json_Class/CampListV3Response/CampListV3Response.dart';
import '../../Modules/Json_Class/CampTypeResponse/CampTypeResponse.dart';
import '../../Modules/Json_Class/ConsumablesListResponse/ConsumablesListResponse.dart';
import '../../Modules/Json_Class/DevicesListResponse/DevicesListResponse.dart';
import '../../Modules/Json_Class/ResourceListResponse/ResourceListResponse.dart';
import '../../Modules/Json_Class/SubDevicesListResponse/SubDevicesListResponse.dart';
import '../../Modules/Json_Class/SubResourceListResponse/SubResourceListResponse.dart';
import '../../Modules/Json_Class/SubmitDeviceMappingResponse/SubmitDeviceMappingResponse.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/widgets/AppActiveButton.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import '../../Views/DropDownListScreen/DropDownListScreen.dart';
import '../../Views/MultiSelectionDropDownListScreen/MultiSelectionDropDownListScreen.dart';
import '../../Views/SubDeviceDropDownScreen/SubDeviceDropDownScreen.dart';

class DeviceAllocationScreen extends StatefulWidget {
  const DeviceAllocationScreen({super.key});

  @override
  State<DeviceAllocationScreen> createState() => _DeviceAllocationScreenState();
}

class _DeviceAllocationScreenState extends State<DeviceAllocationScreen> {
  String selectedCampDate = "";
  CampTypeOutput? selectedCampType;
  CampListV3Output? selectedCampId;

  ToastManager toastManager = ToastManager();
  DeviceMappingManager deviceMappingManager = DeviceMappingManager();
  APIManager apiManager = APIManager();
  int selectedSegmentIndex = 0;
  DevicesOutput selectedDevice = DevicesOutput();
  ResourceOutput? selectedResource;

  @override
  void initState() {
    super.initState();
    selectedCampDate = FormatterManager.formatDateToString(DateTime.now());

    int districtId =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;

    int empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    deviceMappingManager.empCode = empCode.toString();
    deviceMappingManager.dISTLGDCODE = districtId.toString();

    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: mAppBar(
        scTitle: "Device Allocation",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          deviceMappingManager.resetDeviceMaaping();
          Navigator.pop(context);
        },
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            Container(
              width: MediaQuery.of(context).size.width,
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.all(Radius.circular(10)),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withValues(alpha: 0.15),
                    blurRadius: 4,
                  ),
                ],
              ),
              padding: const EdgeInsets.all(10),
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
                            text: selectedCampDate,
                          ),
                          onTap: () {
                            _selectCampDate(context);
                          },
                          hint: 'Camp Date*',
                          label: CommonText(
                            text: 'Camp Date*',
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
                        ).paddingOnly(top: 12.h),
                        //   AppDateTextfield(
                        //   icon: icCalendarMonth,
                        //   titleHeaderString: "Camp Date*",
                        //   valueString: selectedCampDate,
                        //   onTap: () {
                        //     _selectCampDate(context);
                        //   },
                        // ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: AppTextField(
                          readOnly: true,
                          controller: TextEditingController(
                            text: selectedCampType?.campTypeDescription ?? "",
                          ),
                          onTap: () {
                            List<CampTypeOutput> campTypeList = [];
                            if (DataProvider().getRegularCamp()) {
                              campTypeList.add(
                                CampTypeOutput(
                                  cAMPTYPE: 1,
                                  campTypeDescription: "REGULAR",
                                ),
                              );
                            } else {
                              campTypeList.add(
                                CampTypeOutput(
                                  cAMPTYPE: 1,
                                  campTypeDescription: "REGULAR",
                                ),
                              );
                              campTypeList.add(
                                CampTypeOutput(
                                  cAMPTYPE: 2,
                                  campTypeDescription: "CSC REGULAR CAMP",
                                ),
                              );
                            }
                            _showDropDownBottomSheet(
                              "Camp Type",
                              campTypeList,
                              DropDownTypeMenu.CampType,
                            );
                          },
                          hint: 'Camp Type*',
                          label: CommonText(
                            text: 'Camp Type*',
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
                        ).paddingOnly(top: 12.h),

                        // AppDropdownTextfield(
                        //   icon: icnTent,
                        //   titleHeaderString: "Camp Type*",
                        //   valueString:
                        //       selectedCampType?.campTypeDescription ?? "",
                        //   onTap: () {
                        //     List<CampTypeOutput> campTypeList = [];
                        //     if (DataProvider().getRegularCamp()) {
                        //       campTypeList.add(
                        //         CampTypeOutput(
                        //           cAMPTYPE: 1,
                        //           campTypeDescription: "REGULAR",
                        //         ),
                        //       );
                        //     } else {
                        //       campTypeList.add(
                        //         CampTypeOutput(
                        //           cAMPTYPE: 1,
                        //           campTypeDescription: "REGULAR",
                        //         ),
                        //       );
                        //       campTypeList.add(
                        //         CampTypeOutput(
                        //           cAMPTYPE: 2,
                        //           campTypeDescription: "CSC REGULAR CAMP",
                        //         ),
                        //       );
                        //     }
                        //     _showDropDownBottomSheet(
                        //       "Camp Type",
                        //       campTypeList,
                        //       DropDownTypeMenu.CampType,
                        //     );
                        //   },
                        // ),
                      ),
                    ],
                  ),
                  // const SizedBox(height: 12),

                  // AppDropdownTextfield(
                  //   icon: icHashIcon,
                  //   titleHeaderString: "Camp ID*",
                  //   valueString: deviceMappingManager.campID,
                  //   onTap: () {
                  //     getCampList();
                  //   },
                  // ),
                  AppTextField(
                    readOnly: true,
                    controller: TextEditingController(
                      text: deviceMappingManager.campID,
                    ),
                    onTap: () {
                      getCampList();
                    },
                    hint: 'Camp Type*',
                    label: CommonText(
                      text: 'Camp Type*',
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
                          icHashIcon,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                    suffixIcon: Icon(Icons.keyboard_arrow_down),
                  ).paddingOnly(top: 12.h),
                  Container(
                    color: Colors.transparent,
                    padding: const EdgeInsets.all(8),
                    child: Text(
                      textAlign: TextAlign.left,
                      "Note: Quantity has been calculated based on the beneficiary count",
                      style: TextStyle(
                        color: Color(0xffEA1F1F),
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(12),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 12),
            SizedBox(
              width: MediaQuery.of(context).size.width,
              height: 50,
              child: Row(
                children: [
                  Expanded(
                    child: Container(
                      decoration: BoxDecoration(
                        border: Border(
                          top: BorderSide(
                            color:
                                selectedSegmentIndex == 0
                                    ? Colors.transparent
                                    : Color(0xffD1D1D1),
                            width: 1,
                          ),
                          bottom: BorderSide(
                            color:
                                selectedSegmentIndex == 0
                                    ? Colors.transparent
                                    : Color(0xffD1D1D1),
                            width: 1,
                          ),
                          left: BorderSide(
                            color:
                                selectedSegmentIndex == 0
                                    ? Colors.transparent
                                    : Color(0xffD1D1D1),
                            width: 1,
                          ),
                        ),
                        color:
                            selectedSegmentIndex == 0
                                ? kPrimaryColor
                                : Colors.white,
                        borderRadius: const BorderRadius.only(
                          topLeft: Radius.circular(20),
                          bottomLeft: Radius.circular(20),
                        ),
                      ),
                      child: Center(
                        child: Text(
                          "Device Mapping",
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            color:
                                selectedSegmentIndex == 0
                                    ? Colors.white
                                    : dropDownTitleHeader,
                            fontSize: responsiveFont(12),
                          ),
                        ),
                      ),
                    ),
                  ),
                  Expanded(
                    child: Container(
                      decoration: BoxDecoration(
                        border: Border(
                          top: BorderSide(
                            color:
                                selectedSegmentIndex == 1
                                    ? Colors.transparent
                                    : Color(0xffD1D1D1),
                            width: 1,
                          ),
                          bottom: BorderSide(
                            color:
                                selectedSegmentIndex == 1
                                    ? Colors.transparent
                                    : Color(0xffD1D1D1),
                            width: 1,
                          ),
                          left: BorderSide(
                            color:
                                selectedSegmentIndex == 1
                                    ? Colors.transparent
                                    : Color(0xffD1D1D1),
                            width: 1,
                          ),
                        ),
                        color:
                            selectedSegmentIndex == 1
                                ? kPrimaryColor
                                : Colors.white,
                      ),
                      child: Center(
                        child: Text(
                          "Consumables",
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            color:
                                selectedSegmentIndex == 1
                                    ? Colors.white
                                    : dropDownTitleHeader,
                            fontSize: responsiveFont(12),
                          ),
                        ),
                      ),
                    ),
                  ),
                  Expanded(
                    child: Container(
                      decoration: BoxDecoration(
                        border: Border(
                          top: BorderSide(
                            color:
                                selectedSegmentIndex == 2
                                    ? Colors.transparent
                                    : Color(0xffD1D1D1),
                            width: 1,
                          ),
                          bottom: BorderSide(
                            color:
                                selectedSegmentIndex == 2
                                    ? Colors.transparent
                                    : Color(0xffD1D1D1),
                            width: 1,
                          ),
                          left: BorderSide(
                            color:
                                selectedSegmentIndex == 2
                                    ? Colors.transparent
                                    : Color(0xffD1D1D1),
                            width: 1,
                          ),
                          right: BorderSide(
                            color:
                                selectedSegmentIndex == 2
                                    ? Colors.transparent
                                    : Color(0xffD1D1D1),
                            width: 1,
                          ),
                        ),
                        color:
                            selectedSegmentIndex == 2
                                ? kPrimaryColor
                                : Colors.white,
                        borderRadius: const BorderRadius.only(
                          topRight: Radius.circular(20),
                          bottomRight: Radius.circular(20),
                        ),
                      ),
                      child: Center(
                        child: Text(
                          textAlign: TextAlign.center,
                          "Resource\nAllocation",
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            color:
                                selectedSegmentIndex == 2
                                    ? Colors.white
                                    : dropDownTitleHeader,
                            fontSize: responsiveFont(12),
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 12),
            setSegmentLayout(),

            Container(
              width: MediaQuery.of(context).size.width,
              color: Colors.white,
              padding: const EdgeInsets.all(8),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  SizedBox(
                    width: MediaQuery.of(context).size.width,
                    child: Row(
                      children: [
                        selectedSegmentIndex == 2
                            ? Container()
                            : Expanded(
                              child: AppActiveButton(
                                buttontitle: "Skip",
                                isCancel: true,
                                onTap: () {
                                  deviceMappingManager.isSkipFlag = "1";
                                  nextStepScreen();
                                },
                              ),
                            ),
                        selectedSegmentIndex == 2
                            ? Container()
                            : const SizedBox(width: 67),
                        Expanded(
                          child: AppActiveButton(
                            buttontitle:
                                selectedSegmentIndex == 2 ? "Submit" : "NEXT",
                            onTap: () {
                              if (selectedSegmentIndex == 2) {
                                submitData();
                              } else {
                                nextStepScreen();
                              }
                            },
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ],
        ).paddingSymmetric(vertical: 10, horizontal: 10),
      ),
    );
  }

  Future<void> _selectCampDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1901),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        selectedCampDate = FormatterManager.formatDateToString(picked);

        deviceMappingManager.deviceList = [];
        deviceMappingManager.consumablesList = [];
        deviceMappingManager.resourceList = [];
        deviceMappingManager.campID = "";
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
                deviceMappingManager.campType =
                    selectedCampType?.cAMPTYPE.toString() ?? "0";
              } else if (dropDownType == DropDownTypeMenu.CampID) {
                selectedCampId = p0;
                deviceMappingManager.campID =
                    selectedCampId?.campId.toString() ?? "0";
                deviceMappingManager.labCode =
                    selectedCampId?.lABCODE.toString() ?? "0";
                deviceMappingManager.expectedBeneficiary =
                    selectedCampId?.expectedbeneficiarycount.toString() ?? "0";
                int createdBy = selectedCampId?.createdBy ?? 0;
                int isMappingFlag = selectedCampId?.resourceMappingFlag ?? 0;

                if (deviceMappingManager.empCode != createdBy.toString()) {
                  toastManager.showAlertMessage(
                    context,
                    "This camp not created by you,please select created camp",
                    Colors.red,
                  );
                } else if (isMappingFlag == 1) {
                  deviceMappingManager.campID = "";
                  toastManager.showAlertMessage(
                    context,
                    "Device and resource mapping already done for selected camp",
                    Colors.red,
                  );
                } else {
                  ToastManager.showLoader();
                  getDevices();
                }
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

  void _showSubDeviceDropDownBottomSheet(String title, List<dynamic> list) {
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
          child: SubDeviceDropDownScreen(
            titleString: title,
            dropDownList: list,
            onApplyTap: (p0) {
              print(p0);
              selectedDevice.subDeviceList = p0;
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  void _showSubResourcenDropDownBottomSheet(
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
              if (dropDownType == DropDownMultipleTypeMenu.SubResource) {
                selectedResource?.subResourceList = p0;
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

  void getCampList() {
    if (selectedCampDate.isEmpty) {
      toastManager.showAlertMessage(context, "Select Camp Date", Colors.red);
    } else if (selectedCampType == null) {
      toastManager.showAlertMessage(context, "Select Camp Type", Colors.red);
    } else {
      ToastManager.showLoader();
      Map<String, String> data = {
        "CampDATE": selectedCampDate,
        "UserId": deviceMappingManager.empCode,
        "DISTLGDCODE": deviceMappingManager.dISTLGDCODE,
        "CampType": deviceMappingManager.campType,
        "LABCODE": deviceMappingManager.labCode,
      };
      apiManager.getCampIDAPI(data, apiCampIDCallBack);
    }
  }

  void apiCampIDCallBack(
    CampListV3Response? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp ID",
        response?.output ?? [],
        DropDownTypeMenu.CampID,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getDevices() {
    ToastManager.showLoader();
    Map<String, String> data = {
      "BeneficairyCount": deviceMappingManager.expectedBeneficiary,
    };
    apiManager.getDevicesAPI(data, apiDevicesCallBack);
  }

  void apiDevicesCallBack(
    DevicesListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      deviceMappingManager.deviceList = response?.output ?? [];
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getSubDevices(DevicesOutput devicesOutputObj) {
    ToastManager.showLoader();
    selectedDevice = devicesOutputObj;
    Map<String, String> data = {
      "DevicesId": devicesOutputObj.devicesId?.toString() ?? "0",
      "CampDate": selectedCampDate,
      "DISTLGDCODE": deviceMappingManager.dISTLGDCODE,
      "LabCode": deviceMappingManager.labCode,
    };
    apiManager.getSubDevicesAPI(data, apiSubDevicesCallBack);
  }

  void apiSubDevicesCallBack(
    SubDevicesListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showSubDeviceDropDownBottomSheet("Sub Device", response?.output ?? []);
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  String subDevieSelected(DevicesOutput devicesOutputObj) {
    if (devicesOutputObj.subDeviceList.isEmpty) {
      return "";
    } else {
      return "${devicesOutputObj.subDeviceList.length}";
    }
  }

  void getConsumables() {
    ToastManager.showLoader();
    Map<String, String> data = {
      "BeneficairyCount": deviceMappingManager.expectedBeneficiary,
    };
    apiManager.getConsumablesAPI(data, apiConsumablesCallBack);
  }

  void apiConsumablesCallBack(
    ConsumablesListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      deviceMappingManager.consumablesList = response?.output ?? [];
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getResourcesAllocation() {
    ToastManager.showLoader();
    apiManager.getResourceAPI(apiResourcesAllocationCallBack);
  }

  void apiResourcesAllocationCallBack(
    ResourceListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      deviceMappingManager.resourceList = response?.output ?? [];
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void subResources(ResourceOutput devicesOutput) {
    selectedResource = devicesOutput;
    ToastManager.showLoader();
    Map<String, String> data = {
      "TestId": devicesOutput.testId.toString(),
      "Campdate": selectedCampDate,
      "DISTLGDCODE": deviceMappingManager.dISTLGDCODE,
      "PartnerID": "1",
      "LabCode": deviceMappingManager.labCode,
    };
    apiManager.getSubResourceAPI(data, apiSubResourceCallBack);
  }

  void apiSubResourceCallBack(
    SubResourceListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      List<SubResourceOutput> tempOutput = response?.output ?? [];
      tempOutput.sort((a, b) {
        final nameA = a.resourceName?.trim() ?? "";
        final nameB = b.resourceName?.trim() ?? "";
        return nameA.toLowerCase().compareTo(nameB.toLowerCase());
      });
      _showSubResourcenDropDownBottomSheet(
        "Resource",
        tempOutput,
        DropDownMultipleTypeMenu.SubResource,
      );
      setState(() {});
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  String subResourcesSelected(ResourceOutput selectedSubResourceObj) {
    if (selectedSubResourceObj.subResourceList.isEmpty) {
      return "";
    } else {
      return "${selectedSubResourceObj.subResourceList.length}";
    }
  }

  String getResourceMappingJsonNew() {
    List<Map<String, dynamic>> resourceMappingList = [];

    Map<String, dynamic> firstObj = {
      "ResourceName": "",
      "TestId": 3,
      "isChecked": true,
      "USERID": 0,
    };
    resourceMappingList.add(firstObj);
    print(deviceMappingManager.resourceList.length);
    for (ResourceOutput resource in deviceMappingManager.resourceList) {
      print(resource.subResourceList.length);
      for (SubResourceOutput subResource in resource.subResourceList) {
        Map<String, dynamic> objDict = {
          "ResourceName": subResource.resourceName ?? "",
          "TestId": subResource.testId ?? 0,
          "isChecked": true,
          "USERID": subResource.uSERID ?? 0,
        };
        print(objDict);
        resourceMappingList.add(objDict);
      }
    }

    print(resourceMappingList);

    try {
      String json = jsonEncode(resourceMappingList);
      print(json);
      return json;
    } catch (e) {
      print("Error serializing JSON: $e");
      return "";
    }
  }

  String getDeviceMappingJsonNew() {
    List<Map<String, dynamic>> deviceMapplingList = [];

    for (var resource in deviceMappingManager.deviceList) {
      for (var selectedResource in resource.subDeviceList) {
        Map<String, dynamic> objDict = {
          "DeviceCompName": selectedResource.deviceCompName ?? "",
          "DeviceModel": selectedResource.deviceModel ?? "",
          "DeviceSerial": selectedResource.deviceSerial ?? "",
          "DevicesId": selectedResource.devicesId ?? 0,
          "ISActive": "null",
          "isChecked": true,
          "SubDevicesId": selectedResource.subDevicesId ?? 0,
        };
        print(objDict);
        deviceMapplingList.add(objDict);
      }
    }

    print(deviceMapplingList);

    try {
      String json = jsonEncode(deviceMapplingList);
      print(json);
      return json;
    } catch (e) {
      print("Error serializing JSON: $e");
      return "";
    }
  }

  void submitData() {
    // ToastManager.showLoader();
    String resourceMappingJsonNew = "";
    String deviceMappingJsonNew = "";

    if (deviceMappingManager.isSkipFlag == "1") {
      deviceMappingJsonNew =
          "[{\"DeviceCompName\":\"BP-Bavdhan\",\"DeviceModel\":\"\",\"DeviceSerial\":\"BP-789\",\"DevicesId\":0,\"ISActive\":null,\"isChecked\":true,\"SubDevicesId\":0},{\"DeviceCompName\":\"DA-Bavdhan\",\"DeviceModel\":\"\",\"DeviceSerial\":\"DA-987\",\"DevicesId\":0,\"ISActive\":null,\"isChecked\":true,\"SubDevicesId\":0},{\"DeviceCompName\":\"SC-Bavdhan\",\"DeviceModel\":\"\",\"DeviceSerial\":\"SC-654\",\"DevicesId\":0,\"ISActive\":null,\"isChecked\":true,\"SubDevicesId\":0},{\"DeviceCompName\":\"SP-Pune\",\"DeviceModel\":\"\",\"DeviceSerial\":\"SP123\",\"DevicesId\":0,\"ISActive\":null,\"isChecked\":true,\"SubDevicesId\":0},{\"DeviceCompName\":\"WM-Bavdhan\",\"DeviceModel\":\"\",\"DeviceSerial\":\"WM-456\",\"DevicesId\":0,\"ISActive\":null,\"isChecked\":true,\"SubDevicesId\":0}]";
      resourceMappingJsonNew = getResourceMappingJsonNew();
    } else {
      deviceMappingJsonNew = getDeviceMappingJsonNew();
      resourceMappingJsonNew = getResourceMappingJsonNew();
    }

    Map<String, dynamic> data = {
      "DeviceMappingJson": deviceMappingJsonNew,
      "ResourceMappingJson": resourceMappingJsonNew,
      "UserId": deviceMappingManager.empCode,
      "CampId": deviceMappingManager.campID,
    };
    print(data);
    apiManager.submitDeviceAllocationAPI(
      data,
      apiSubmitDeviceAllocationCallBack,
    );
  }

  void apiSubmitDeviceAllocationCallBack(
    SubmitDeviceMappingResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      ToastManager.showSuccessPopup(
        context,
        icSuccessIcon,
        "Data Added Successfully.",
        () {
          Get.back();
          Get.back();
        },
      );
      // showAlertmessage("Data Added Successfully.");
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void showAlertmessage(String message) {
    ToastManager.showSuccessPopup(context, icSuccessIcon, message,(){
      Get.back();
      Get.back();
    });
    // Navigator.pop(context);
  }

  void deviceMappingConfirmationAlert(String alertTitle, String alertMessage) {
    showAlertDialog(BuildContext context) {
      // set up the buttons
      Widget cancelButton = TextButton(
        child: const Text("Cancel"),
        onPressed: () {
          Navigator.of(context).pop();
        },
      );
      Widget continueButton = TextButton(
        child: const Text("Next"),
        onPressed: () {
          if (selectedSegmentIndex == 0) {
            selectedSegmentIndex = 1;
          } else if (selectedSegmentIndex == 1) {
            selectedSegmentIndex = 2;
          }
        },
      );

      AlertDialog alert = AlertDialog(
        title: Text(alertTitle),
        content: Text(alertMessage),
        actions: [cancelButton, continueButton],
      );

      showDialog(
        context: context,
        builder: (BuildContext context) {
          return alert;
        },
      );
    }
  }

  void nextStepScreen() {
    if (selectedCampType == null) {
      toastManager.showAlertMessage(context, "Select Camp Type", Colors.red);
    } else if (selectedCampId == null) {
      toastManager.showAlertMessage(context, "Select Camp ID", Colors.red);
    } else {
      if (selectedSegmentIndex == 0) {
        if (deviceMappingManager.isSkipFlag == "0") {
          int deviceRq = 0;
          int subDevicesSelectedCount = 0;
          bool isError = false;

          for (DevicesOutput devices in deviceMappingManager.deviceList) {
            int subDevices = devices.subDeviceList.length;
            deviceRq += devices.requiredDevice ?? 0;
            if (subDevices == 0) {
              isError = true;
              toastManager.showAlertMessage(
                context,
                "Select ${devices.requiredDevice ?? 0} device",
                Colors.red,
              );
            }
            subDevicesSelectedCount += subDevices;
          }
          if (!isError) {
            if (subDevicesSelectedCount >= deviceRq) {
              deviceMappingConfirmationAlert(
                "Device",
                "You have selected $subDevicesSelectedCount Devices for this camp",
              );
            }
          } else {
            toastManager.showAlertMessage(
              context,
              "Please select required devices ($deviceRq) for camp",
              Colors.red,
            );
          }
        } else {
          if (selectedSegmentIndex == 0) {
            selectedSegmentIndex = 1;
            // getConsumables();
          } else if (selectedSegmentIndex == 1) {
            selectedSegmentIndex = 2;
          }
        }
      } else {
        if (selectedSegmentIndex == 0) {
          selectedSegmentIndex = 1;
          // getConsumables();
        } else if (selectedSegmentIndex == 1) {
          getResourcesAllocation();
          selectedSegmentIndex = 2;
        } else {
          print("Resource allocation");

          bool isError = false;
          int resourcesSelectedCount = 0;

          for (ResourceOutput resource in deviceMappingManager.resourceList) {
            if (resource.subResourceList.isEmpty) {
              isError = true;
              break;
            }
            resourcesSelectedCount += resource.subResourceList.length;
          }
          if (isError) {
            toastManager.showAlertMessage(
              context,
              "Select at least 1 resource",
              Colors.red,
            );
          } else {
            if (resourcesSelectedCount >=
                deviceMappingManager.resourceList.length) {
              submitData();
            } else {
              toastManager.showAlertMessage(
                context,
                "Please select resources for camp",
                Colors.red,
              );
            }
          }
        }
      }
    }
    setState(() {});
  }

  Widget setSegmentLayout() {
    if (selectedSegmentIndex == 0) {
      return Expanded(
        child: ListView.builder(
          itemCount: deviceMappingManager.deviceList.length,
          itemBuilder: (context, index) {
            DevicesOutput devicesOutputObj =
                deviceMappingManager.deviceList[index];
            return IntrinsicHeight(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(8, 6, 8, 6),
                child: Container(
                  width: MediaQuery.of(context).size.width,
                  decoration: BoxDecoration(
                    color: Colors.white,
                    boxShadow: [
                      BoxShadow(
                        blurRadius: 10,
                        spreadRadius: 0,
                        offset: const Offset(0, 1),
                        color: const Color(0xFF000000).withValues(alpha: 0.15),
                      ),
                    ],
                    borderRadius: BorderRadius.circular(responsiveHeight(10)),
                  ),
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 20,
                            height: 20,
                            child: Image.asset(icDevicesIcon),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: RichText(
                              text: TextSpan(
                                text: "Device Name : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(14),
                                ),
                                children: [
                                  TextSpan(
                                    text: devicesOutputObj.deviceName,
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w400,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                ],
                              ),
                              softWrap: true,
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 4),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 20,
                            height: 20,
                            child: Image.asset(icnTent),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: RichText(
                              text: TextSpan(
                                text: "Quantity : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(14),
                                ),
                                children: [
                                  TextSpan(
                                    text:
                                        devicesOutputObj.requiredDevice
                                            ?.toString() ??
                                        "",
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w400,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                ],
                              ),
                              softWrap: true,
                            ),
                          ),
                        ],
                      ),
                      // const SizedBox(height: 8),
                      AppTextField(
                        readOnly: true,
                        controller: TextEditingController(
                          text: subDevieSelected(devicesOutputObj),
                        ),
                        onTap: () {
                          getSubDevices(devicesOutputObj);
                        },
                        hint: 'Select Devices',
                        label: CommonText(
                          text: 'Select Devices',
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
                              icDevicesIcon,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ).paddingOnly(top: 12.h),
                      // AppDropdownTextfield(
                      //   icon: icDevicesIcon,
                      //   titleHeaderString: "Select Devices",
                      //   valueString: subDevieSelected(devicesOutputObj),
                      //   onTap: () {
                      //     getSubDevices(devicesOutputObj);
                      //   },
                      // ),
                    ],
                  ),
                ),
              ),
            );
          },
        ),
      );
    } else if (selectedSegmentIndex == 1) {
      return Expanded(
        child: ListView.builder(
          itemCount: deviceMappingManager.consumablesList.length,
          itemBuilder: (context, index) {
            ConsumablesOutput consumablesOutput =
                deviceMappingManager.consumablesList[index];
            return IntrinsicHeight(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(0, 0, 0, 12),
                child: Container(
                  width: MediaQuery.of(context).size.width,
                  decoration: BoxDecoration(
                    color: Colors.white,
                    boxShadow: [
                      BoxShadow(
                        blurRadius: responsiveHeight(10),
                        spreadRadius: 0,
                        offset: const Offset(0, 1),
                        color: const Color(0xFF000000).withValues(alpha: 0.15),
                      ),
                    ],
                    borderRadius: BorderRadius.circular(responsiveHeight(10)),
                  ),
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 24,
                            height: 24,
                            child: Image.asset(icConsumableIcon),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: RichText(
                              text: TextSpan(
                                text: "Consumable : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(14),
                                ),
                                children: [
                                  TextSpan(
                                    text: consumablesOutput.productName,
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w400,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                ],
                              ),
                              softWrap: true,
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 4),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 24,
                            height: 24,
                            child: Image.asset(iconPerson),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: RichText(
                              text: TextSpan(
                                text: "Per Person Inventory : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(14),
                                ),
                                children: [
                                  TextSpan(
                                    text:
                                        consumablesOutput.productQuantity
                                            ?.toString() ??
                                        "0",
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w400,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                ],
                              ),
                              softWrap: true,
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 4),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 24,
                            height: 24,
                            child: Image.asset(icStockIcon),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: RichText(
                              text: TextSpan(
                                text: "Stock Available : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(14),
                                ),
                                children: [
                                  TextSpan(
                                    text:
                                        consumablesOutput.aVAILABELSTOCK
                                            ?.toString() ??
                                        "0",
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w400,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                ],
                              ),
                              softWrap: true,
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 4),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 24,
                            height: 24,
                            child: Image.asset(icPackageImport),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: RichText(
                              text: TextSpan(
                                text: "Expected Quantity : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(14),
                                ),
                                children: [
                                  TextSpan(
                                    text:
                                        consumablesOutput.expectedQuantity
                                            ?.toString() ??
                                        "0",
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w400,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                ],
                              ),
                              softWrap: true,
                            ),
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              ),
            );
          },
        ),
      );
    }
    return Expanded(
      child: ListView.builder(
        itemCount: deviceMappingManager.resourceList.length,
        itemBuilder: (context, index) {
          ResourceOutput devicesOutputObj =
              deviceMappingManager.resourceList[index];
          return IntrinsicHeight(
            child: Padding(
              padding: const EdgeInsets.fromLTRB(0, 0, 0, 12),
              child: Container(
                width: MediaQuery.of(context).size.width,
                decoration: BoxDecoration(
                  color: Colors.white,
                  boxShadow: [
                    BoxShadow(
                      blurRadius: responsiveHeight(10),
                      spreadRadius: 0,
                      offset: const Offset(0, 1),
                      color: const Color(0xFF000000).withValues(alpha: 0.15),
                    ),
                  ],
                  borderRadius: BorderRadius.circular(responsiveHeight(10)),
                ),
                padding: const EdgeInsets.all(16),
                child: Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        SizedBox(
                          width: 24,
                          height: 24,
                          child: Image.asset(icUserIcon),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: RichText(
                            text: TextSpan(
                              text: "Role : ",
                              style: TextStyle(
                                color: Colors.black,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w500,
                                fontSize: responsiveFont(14),
                              ),
                              children: [
                                TextSpan(
                                  text: devicesOutputObj.testName ?? "",
                                  style: TextStyle(
                                    color: Colors.black,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w400,
                                    fontSize: responsiveFont(14),
                                  ),
                                ),
                              ],
                            ),
                            softWrap: true,
                          ),
                        ),
                      ],
                    ),
                    // const SizedBox(height: 8),
                    // AppDropdownTextfield(
                    //   icon: icUserIcon,
                    //   titleHeaderString: "Select Resource",
                    //   valueString: subResourcesSelected(devicesOutputObj),
                    //   onTap: () {
                    //     subResources(devicesOutputObj);
                    //   },
                    // ),
                    AppTextField(
                      readOnly: true,
                      controller: TextEditingController(
                        text: subResourcesSelected(devicesOutputObj),
                      ),
                      onTap: () {
                        subResources(devicesOutputObj);
                      },
                      hint: 'Select Devices',
                      label: CommonText(
                        text: 'Select Devices',
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
                            icUserIcon,
                            height: 24.h,
                            width: 24.w,
                            fit: BoxFit.contain,
                          ),
                        ),
                      ),
                    ).paddingOnly(top: 12.h),
                  ],
                ),
              ),
            ),
          );
        },
      ),
    );
  }
}
