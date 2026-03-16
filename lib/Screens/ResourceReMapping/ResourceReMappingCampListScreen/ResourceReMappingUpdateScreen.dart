// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/Json_Class/CampDetailsntApprovalResponse/CampDetailsntApprovalResponse.dart';
import '../../../Modules/Json_Class/CampResourceAllocationResponse/CampResourceAllocationResponse.dart';
import '../../../Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import '../../../Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import '../../../Modules/Json_Class/UpdateSubResourceListResponse/UpdateSubResourceListResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../../../Views/AddRemoveResourceDropDownScreen/AddRemoveResourceDropDownScreen.dart';
import '../../../Views/ResourceReMappingCampDetails/ResourceReMappingCampDetails.dart';
import '../../../../../Modules/constants/fonts.dart';

class ResourceReMappingUpdateScreen extends StatefulWidget {
  ResourceReMappingUpdateScreen({super.key, required this.reMappingCampOutput});

  ResourceReMappingCampOutput reMappingCampOutput;

  @override
  State<ResourceReMappingUpdateScreen> createState() =>
      _ResourceReMappingUpdateScreenState();
}

class _ResourceReMappingUpdateScreenState
    extends State<ResourceReMappingUpdateScreen> {
  APIManager apiManager = APIManager();

  CampDetailsntApprovalOutput? _campDetails;
  List<CampResourceAllocationOutput> resourceAllocationList = [];
  CampResourceAllocationOutput? selectedResource;

  int empCode = 0;

  void subResources(CampResourceAllocationOutput devicesOutput) {
    selectedResource = devicesOutput;
    ToastManager.showLoader();
    Map<String, String> data = {
      "TestId": devicesOutput.testId.toString(),
      "Campid": devicesOutput.campId.toString(),
      "Campdate": FormatterManager.campDateStringFromStringDate(
        widget.reMappingCampOutput.campDate ?? "",
      ),
      "DISTLGDCODE": widget.reMappingCampOutput.dISTLGDCODE?.toString() ?? "0",
      "PartnerID": "1",
      "LabCode": widget.reMappingCampOutput.lABCODE?.toString() ?? "0",
    };
    apiManager.getApproveResourcelstForUpdateAPI(data, apiSubResourceCallBack);
  }

  void apiSubResourceCallBack(
    UpdateSubResourceListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      List<UpdateSubResourceOutput> tempOutput = response?.output ?? [];
      tempOutput.sort((a, b) {
        final nameA = a.resourceName?.trim() ?? "";
        final nameB = b.resourceName?.trim() ?? "";
        return nameA.toLowerCase().compareTo(nameB.toLowerCase());
      });

      _showSubResourcenDropDownBottomSheet(
        "Resource",
        tempOutput,
        DropDownMultipleTypeMenu.UpdateSubResource,
      );
      setState(() {});
    } else {
      ToastManager.toast(errorMessage);
    }
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
      isDismissible: true,
      enableDrag: true,
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
          child: AddRemoveResourceDropDownScreen(
            titleString: title,
            dropDownList: list,
            reMappingCampOutput: widget.reMappingCampOutput,
            empCode: empCode,
            onRefreshData: (p0) {
              groupAPI();
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  @override
  void initState() {
    super.initState();
    LoginOutput? userLoginDetails =
        DataProvider().getParsedUserData()?.output?.first;
    empCode = userLoginDetails?.empCode ?? 0;
    groupAPI();
  }

  void groupAPI() {
    ToastManager.showLoader();

    getCampDetails();
    getResourcesForApproval();
  }

  void getCampDetails() {
    Map<String, String> params = {
      "campid": widget.reMappingCampOutput.campId?.toString() ?? "0",
    };
    apiManager.getCampDetailsForAppForIntApprovalAPI(
      params,
      apiCampDetailsForAppForIntApprovalCallBack,
    );
  }

  void apiCampDetailsForAppForIntApprovalCallBack(
    CampDetailsntApprovalResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _campDetails = response?.output?.first;
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getResourcesForApproval() {
    Map<String, String> params = {
      "campid": widget.reMappingCampOutput.campId?.toString() ?? "0",
    };
    apiManager.getResourcesForApprovalAPI(
      params,
      apiResourcesForApprovalCallBack,
    );
  }

  void apiResourcesForApprovalCallBack(
    CampResourceAllocationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      resourceAllocationList = response?.output ?? [];
    } else {
      resourceAllocationList = [];
      ToastManager.toast("Devices not available");
    }
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Resource Re-Mapping",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            ResourceReMappingCampDetails(campDetails: _campDetails),
            SizedBox(height: 8.h),
            Text(
              "Resource Allocation",
              style: TextStyle(
                color: Colors.black,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                fontSize: responsiveFont(16),
              ),
            ),
            SizedBox(height: 4.h),
            Expanded(
              child: ListView.builder(
                itemCount: resourceAllocationList.length,
                itemBuilder: (context, index) {
                  CampResourceAllocationOutput object =
                      resourceAllocationList[index];
                  return IntrinsicHeight(
                    child: Padding(
                      padding: EdgeInsets.fromLTRB(0, 4.h, 0, 4.h),
                      child: Container(
                        width: MediaQuery.of(context).size.width,
                        decoration: BoxDecoration(color: Colors.white),
                        padding: const EdgeInsets.fromLTRB(0, 6, 0, 6),
                        child: Column(
                          children: [
                            Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Expanded(
                                  child: Padding(
                                    padding: EdgeInsets.fromLTRB(
                                      4.w,
                                      0,
                                      4.w,
                                      0,
                                    ),
                                    child: RichText(
                                      text: TextSpan(
                                        text: "Role : ",
                                        style: TextStyle(
                                          color: Colors.black,
                                          fontFamily: FontConstants.interFonts,
                                          fontWeight: FontWeight.w600,
                                          fontSize: 16.sp,
                                        ),
                                        children: [
                                          TextSpan(
                                            text: object.testName ?? "",
                                            style: TextStyle(
                                              color: Colors.black,
                                              fontFamily:
                                                  FontConstants.interFonts,
                                              fontWeight: FontWeight.w400,
                                              fontSize: 16.sp,
                                            ),
                                          ),
                                        ],
                                      ),
                                      softWrap: true,
                                    ),
                                  ),
                                ),
                                SizedBox(
                                  width: 212.w,
                                  child: AppTextField(
                                    controller: TextEditingController(
                                      text: object.resourceName ?? "",
                                    ),
                                    readOnly: true,
                                    onTap: () {
                                      subResources(object);
                                    },
                                    hint: 'Select Resource',
                                    label: CommonText(
                                      text: 'Select Resource',
                                      fontSize: 14.sp,
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
                                    suffixIcon: Icon(Icons.keyboard_arrow_down),
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
            ),
            SizedBox(height: 8.h),
          ],
        ).paddingSymmetric(vertical: 10.h, horizontal: 14.w),
      ),
    );
  }
}

// class ResourceReMappingUpdateScreen extends StatefulWidget {
//   ResourceReMappingUpdateScreen({super.key, required this.reMappingCampOutput});

//   ResourceReMappingCampOutput reMappingCampOutput;

//   @override
//   State<ResourceReMappingUpdateScreen> createState() =>
//       _ResourceReMappingUpdateScreenState();
// }

// class _ResourceReMappingUpdateScreenState
//     extends State<ResourceReMappingUpdateScreen> {
//   APIManager apiManager = APIManager();

//   CampDetailsntApprovalOutput? _campDetails;
//   List<CampResourceAllocationOutput> resourceAllocationList = [];
//   CampResourceAllocationOutput? selectedResource;

//   int empCode = 0;

//   void subResources(CampResourceAllocationOutput devicesOutput) {
//     selectedResource = devicesOutput;
//     ToastManager.showLoader();
//     Map<String, String> data = {
//       "TestId": devicesOutput.testId.toString(),
//       "Campid": devicesOutput.campId.toString(),
//       "Campdate": FormatterManager.campDateStringFromStringDate(
//         widget.reMappingCampOutput.campDate ?? "",
//       ),
//       "DISTLGDCODE": widget.reMappingCampOutput.dISTLGDCODE?.toString() ?? "0",
//       "PartnerID": "1",
//       "LabCode": widget.reMappingCampOutput.lABCODE?.toString() ?? "0",
//     };
//     apiManager.getApproveResourcelstForUpdateAPI(data, apiSubResourceCallBack);
//   }

//   void apiSubResourceCallBack(
//     UpdateSubResourceListResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       List<UpdateSubResourceOutput> tempOutput = response?.output ?? [];
//       tempOutput.sort((a, b) {
//         final nameA = a.resourceName?.trim() ?? "";
//         final nameB = b.resourceName?.trim() ?? "";
//         return nameA.toLowerCase().compareTo(nameB.toLowerCase());
//       });

//       _showSubResourcenDropDownBottomSheet(
//         "Resource",
//         tempOutput,
//         DropDownMultipleTypeMenu.UpdateSubResource,
//       );
//       setState(() {});
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//   }

//   void _showSubResourcenDropDownBottomSheet(
//     String title,
//     List<dynamic> list,
//     DropDownMultipleTypeMenu dropDownType,
//   ) {
//     showModalBottomSheet(
//       context: context,
//       isScrollControlled: true,
//       constraints: const BoxConstraints(minWidth: double.infinity),
//       backgroundColor: Colors.white,
//       isDismissible: true,
//       enableDrag: true,
//       builder: (BuildContext context) {
//         return Container(
//           width: double.infinity,
//           height: MediaQuery.of(context).size.width * 1.33,
//           decoration: const BoxDecoration(
//             color: Colors.white,
//             borderRadius: BorderRadius.only(
//               topLeft: Radius.circular(20),
//               topRight: Radius.circular(20),
//             ),
//           ),
//           child: AddRemoveResourceDropDownScreen(
//             titleString: title,
//             dropDownList: list,
//             reMappingCampOutput: widget.reMappingCampOutput,
//             empCode: empCode,
//             onRefreshData: (p0) {
//               groupAPI();
//             },
//           ),
//         );
//       },
//     ).whenComplete(() {
//       setState(() {});
//     });
//   }

//   @override
//   void initState() {
//     super.initState();
//     LoginOutput? userLoginDetails =
//         DataProvider().getParsedUserData()?.output?.first;
//     empCode = userLoginDetails?.empCode ?? 0;
//     groupAPI();
//   }

//   void groupAPI() {
//     ToastManager.showLoader();

//     getCampDetails();
//     getResourcesForApproval();
//   }

//   void getCampDetails() {
//     Map<String, String> params = {
//       "campid": widget.reMappingCampOutput.campId?.toString() ?? "0",
//     };
//     apiManager.getCampDetailsForAppForIntApprovalAPI(
//       params,
//       apiCampDetailsForAppForIntApprovalCallBack,
//     );
//   }

//   void apiCampDetailsForAppForIntApprovalCallBack(
//     CampDetailsntApprovalResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       _campDetails = response?.output?.first;
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   void getResourcesForApproval() {
//     Map<String, String> params = {
//       "campid": widget.reMappingCampOutput.campId?.toString() ?? "0",
//     };
//     apiManager.getResourcesForApprovalAPI(
//       params,
//       apiResourcesForApprovalCallBack,
//     );
//   }

//   void apiResourcesForApprovalCallBack(
//     CampResourceAllocationResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       resourceAllocationList = response?.output ?? [];
//     } else {
//       resourceAllocationList = [];
//       ToastManager.toast("Devices not available");
//     }
//     setState(() {});
//   }

//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return KeyboardDismissOnTap(
//       child: Scaffold(
//         appBar: mAppBar(
//           scTitle: "Resource Re-Mapping",
//           leadingIcon: iconBackArrow,
//           onLeadingIconClick: () {
//             Navigator.pop(context);
//           },
//         ),
//         body: SizedBox(
//           height: SizeConfig.screenHeight,
//           width: SizeConfig.screenWidth,
//           child: Stack(
//             children: [
//               Positioned(
//                 top: 74,
//                 child: Image.asset(
//                   fit: BoxFit.fill,
//                   rect4,
//                   width: SizeConfig.screenWidth,
//                   height: responsiveHeight(300.37),
//                 ),
//               ),
//               Positioned(
//                 top: 53,
//                 child: Image.asset(
//                   fit: BoxFit.fill,
//                   rect3,
//                   width: SizeConfig.screenWidth,
//                   height: responsiveHeight(300.37),
//                 ),
//               ),
//               Positioned(
//                 top: 30,
//                 child: Image.asset(
//                   fit: BoxFit.fill,
//                   rect2,
//                   width: SizeConfig.screenWidth,
//                   height: responsiveHeight(300.37),
//                 ),
//               ),
//               Image.asset(
//                 fit: BoxFit.fill,
//                 rect1,
//                 width: SizeConfig.screenWidth,
//                 height: responsiveHeight(300.37),
//               ),
//               Positioned(
//                 top: 0,
//                 bottom: 8,
//                 left: 8,
//                 right: 8,
//                 child: Column(
//                   crossAxisAlignment: CrossAxisAlignment.start,
//                   mainAxisAlignment: MainAxisAlignment.start,
//                   children: [
//                     ResourceReMappingCampDetails(campDetails: _campDetails),
//                     const SizedBox(height: 8),
//                     Text(
//                       "Resource Allocation",
//                       style: TextStyle(
//                         color: Colors.white,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w600,
//                         fontSize: responsiveFont(16),
//                       ),
//                     ),
//                     const SizedBox(height: 8),
//                     Expanded(
//                       child: ListView.builder(
//                         itemCount: resourceAllocationList.length,
//                         itemBuilder: (context, index) {
//                           CampResourceAllocationOutput object =
//                               resourceAllocationList[index];
//                           return IntrinsicHeight(
//                             child: Padding(
//                               padding: const EdgeInsets.fromLTRB(0, 0, 0, 12),
//                               child: Container(
//                                 width: MediaQuery.of(context).size.width,
//                                 decoration: BoxDecoration(
//                                   color: Colors.white,
//                                   boxShadow: [
//                                     BoxShadow(
//                                       blurRadius: responsiveHeight(10),
//                                       spreadRadius: 0,
//                                       offset: const Offset(0, 1),
//                                       color: const Color(
//                                         0xFF000000,
//                                       ).withValues(alpha: 0.15),
//                                     ),
//                                   ],
//                                   borderRadius: BorderRadius.circular(
//                                     responsiveHeight(10),
//                                   ),
//                                 ),
//                                 padding: const EdgeInsets.all(16),
//                                 child: Column(
//                                   children: [
//                                     Row(
//                                       mainAxisAlignment:
//                                           MainAxisAlignment.start,
//                                       crossAxisAlignment:
//                                           CrossAxisAlignment.center,
//                                       children: [
//                                         SizedBox(
//                                           width: 24,
//                                           height: 24,
//                                           child: Image.asset(icUserIcon),
//                                         ),
//                                         const SizedBox(width: 8),
//                                         Expanded(
//                                           child: RichText(
//                                             text: TextSpan(
//                                               text: "Role : ",
//                                               style: TextStyle(
//                                                 color: Colors.black,
//                                                 fontFamily:
//                                                     FontConstants.interFonts,
//                                                 fontWeight: FontWeight.w500,
//                                                 fontSize: responsiveFont(14),
//                                               ),
//                                               children: [
//                                                 TextSpan(
//                                                   text: object.testName ?? "",
//                                                   style: TextStyle(
//                                                     color: Colors.black,
//                                                     fontFamily:
//                                                         FontConstants
//                                                             .interFonts,
//                                                     fontWeight: FontWeight.w400,
//                                                     fontSize: responsiveFont(
//                                                       14,
//                                                     ),
//                                                   ),
//                                                 ),
//                                               ],
//                                             ),
//                                             softWrap: true,
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                     const SizedBox(height: 8),
//                                     AppDropdownTextfield(
//                                       icon: icUserIcon,
//                                       titleHeaderString: "Select Resource",
//                                       valueString: object.resourceName ?? "",
//                                       onTap: () {
//                                         subResources(object);
//                                       },
//                                     ),
//                                   ],
//                                 ),
//                               ),
//                             ),
//                           );
//                         },
//                       ),
//                     ),
//                     const SizedBox(height: 8),
//                   ],
//                 ),
//               ),
//             ],
//           ),
//         ),
//       ),
//     );
//   }
// }
