// ignore_for_file: file_names, avoid_print

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/GetCampAssignUserResponse/GetCampAssignUserResponse.dart';
import '../../../Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import '../../../Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import '../../../Modules/Json_Class/UserCampMappingStatusResponse/UserCampMappingStatusResponse.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import 'ResourceReMappingCampListRow/ResourceReMappingCampListRow.dart';
import 'ResourceReMappingUpdateScreen.dart';

class ResourceReMappingCampListScreen extends StatefulWidget {
  const ResourceReMappingCampListScreen({super.key});

  @override
  State<ResourceReMappingCampListScreen> createState() =>
      _ResourceReMappingCampListScreenState();
}

class _ResourceReMappingCampListScreenState
    extends State<ResourceReMappingCampListScreen> {
  TextEditingController searchController = TextEditingController();
  String _selectedCampDate = "";
  APIManager apiManager = APIManager();

  int dESGID = 0;

  int subOrgId = 0;
  int dISTLGDCODE = 0;
  int empCode = 0;

  List<ResourceReMappingCampOutput> campList = [];
  List<ResourceReMappingCampOutput> searchCampList = [];

  ResourceReMappingCampOutput? _resourceReMappingCampOutput;

  @override
  void initState() {
    super.initState();
    _selectedCampDate = FormatterManager.formatDateToString(DateTime.now());

    LoginOutput? userLoginDetails =
        DataProvider().getParsedUserData()?.output?.first;
    dESGID = userLoginDetails?.dESGID ?? 0;
    subOrgId = userLoginDetails?.subOrgId ?? 0;
    empCode = userLoginDetails?.empCode ?? 0;
    dISTLGDCODE = userLoginDetails?.dISTLGDCODE ?? 0;
    getApprovedCampListDetailsForApp();
  }

  Future<void> _selectCampDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        _selectedCampDate = FormatterManager.formatDateToString(picked);
        getApprovedCampListDetailsForApp();
      });
    }
  }

  void getApprovedCampListDetailsForApp() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "CampDATE": _selectedCampDate,
      "SubOrgId": subOrgId.toString(),
      "Divison": "0",
      "DISTLGDCODE": "0",
      "USERID": empCode.toString(),
      "DesgId": dESGID.toString(),
    };
    print(params);
    apiManager.getApprovedCampListDetailsForAppFlexiCampAPI(
      params,
      apiApprovedCampListDetailsForAppFlexiCampCallBack,
    );
  }

  void apiApprovedCampListDetailsForAppFlexiCampCallBack(
    ResourceReMappingCampResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      campList = response?.output ?? [];
      searchCampList = campList;
    } else {
      campList = [];
      searchCampList = campList;
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getUserCampMappingAndAttendanceStatus() {
    Map<String, String> params = {
      "CampDATE": _selectedCampDate,
      "UserId": empCode.toString(),
      "DISTLGDCODE": dISTLGDCODE.toString(),
      "CampType": _resourceReMappingCampOutput?.campType?.toString() ?? "",
      "CampID": _resourceReMappingCampOutput?.campId?.toString() ?? "",
      "TestId": "1",
    };

    apiManager.getUserCampMappingAndAttendanceStatusAPI(
      params,
      apiUserCampMappingAndAttendanceStatusCallBack,
    );
  }

  void apiUserCampMappingAndAttendanceStatusCallBack(
    UserCampMappingStatusResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      UserCampMappingStatusOutput? obj = response?.output?.first;

      if (obj != null) {
        if (obj.isCampClosed == 1) {
          ToastManager.toast("This camp is closed");
        } else {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder:
                  (context) => ResourceReMappingUpdateScreen(
                    reMappingCampOutput: _resourceReMappingCampOutput!,
                  ),
            ),
          );
        }
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getCampAssignUserList() {
    Map<String, String> params = {
      "campid": _resourceReMappingCampOutput?.campId?.toString() ?? "",
      "ResourceUserId": empCode.toString(),
      "TestId": "0",
    };

    apiManager.getCampAssignUserListAPI(params, apiCampAssignUserListCallBack);
  }

  void apiCampAssignUserListCallBack(
    GetCampAssignUserResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      List<GetCampAssignUserOutput> list = response?.output ?? [];
      bool allow = false;

      if (list.isEmpty) {
        ToastManager.toast("You are not authorise to Conduct this camp");
      } else {
        for (GetCampAssignUserOutput obj in list) {
          if (obj.resourceUserId == empCode) {
            if (obj.statusRes == 1) {
              allow = true;
            }
          }
        }

        if (allow) {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder:
                  (context) => ResourceReMappingUpdateScreen(
                    reMappingCampOutput: _resourceReMappingCampOutput!,
                  ),
            ),
          );
        } else {
          ToastManager.toast("You are not authorise to Conduct this camp");
        }
      }
    } else {
      ToastManager.toast("You are not authorise to Conduct this camp");
    }
    setState(() {});
  }

  List<ResourceReMappingCampOutput> searchByDescEn(String query) {
    return campList.where((item) {
      final desc = item.campId?.toString().toLowerCase() ?? '';
      return desc.contains(query.toLowerCase());
    }).toList();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Select Camp",
          //for Health Screening
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            SizedBox(
              width: SizeConfig.screenWidth,
              child: Row(
                children: [
                  Expanded(
                    child: Container(
                      width: MediaQuery.of(context).size.width,
                      color: Colors.transparent,
                      child: AppTextField(
                        controller: TextEditingController(
                          text: _selectedCampDate ?? "",
                        ),
                        readOnly: true,
                        onChange: (value) {},
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
                      ),
                      // AppDateTextfield(
                      //   icon: icCalendarMonth,
                      //   titleHeaderString: "Camp Date*",
                      //   valueString: _selectedCampDate,
                      //   onTap: () {
                      //     _selectCampDate(context);
                      //   },
                      // ),
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: AppTextField(
                      controller: searchController,
                      readOnly: false,
                      onChange: (value) {
                        searchCampList = searchByDescEn(value);
                        setState(() {});
                      },

                      hint: 'Search Camp ID',
                      label: CommonText(
                        text: 'Search Camp ID',
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
                    //   titleHeaderString: "Search Camp ID",
                    //   controller: searchController,
                    //   textInputType: TextInputType.number,
                    //   onChange: (value) {
                    //     searchCampList = searchByDescEn(value);
                    //     setState(() {});
                    //   },
                    // ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 12),
            Expanded(
              child: ListView.builder(
                shrinkWrap: true,
                itemCount: searchCampList.length,
                itemBuilder: (context, index) {
                  ResourceReMappingCampOutput reMappingCampOutput =
                      searchCampList[index];
                  return ResourceReMappingCampListRow(
                    reMappingCampOutput: reMappingCampOutput,
                    onSelectTap: () {
                      int createdBy = reMappingCampOutput.createdBy ?? 0;
                      if (dESGID == 29 ||
                          dESGID == 103 ||
                          dESGID == 84 ||
                          dESGID == 100 ||
                          dESGID == 34 ||
                          dESGID == 104 ||
                          dESGID == 107 ||
                          dESGID == 113 ||
                          dESGID == 92 ||
                          dESGID == 162) {
                        if (empCode != createdBy) {
                          ToastManager.toast(
                            "This camp not created by you, please select created camp",
                          );
                          return;
                        }
                        _resourceReMappingCampOutput = reMappingCampOutput;
                        getUserCampMappingAndAttendanceStatus();
                      } else {
                        getCampAssignUserList();
                      }
                    },
                  );
                },
              ),
            ),
          ],
        ).paddingSymmetric(vertical: 8.h, horizontal: 10.h),
      ),
    );
  }
}

// class ResourceReMappingCampListScreen extends StatefulWidget {
//   const ResourceReMappingCampListScreen({super.key});

//   @override
//   State<ResourceReMappingCampListScreen> createState() =>
//       _ResourceReMappingCampListScreenState();
// }

// class _ResourceReMappingCampListScreenState
//     extends State<ResourceReMappingCampListScreen> {
//   TextEditingController searchController = TextEditingController();
//   String _selectedCampDate = "";
//   APIManager apiManager = APIManager();

//   int dESGID = 0;

//   int subOrgId = 0;
//   int dISTLGDCODE = 0;
//   int empCode = 0;

//   List<ResourceReMappingCampOutput> campList = [];
//   List<ResourceReMappingCampOutput> searchCampList = [];

//   ResourceReMappingCampOutput? _resourceReMappingCampOutput;

//   @override
//   void initState() {
//     super.initState();
//     _selectedCampDate = FormatterManager.formatDateToString(DateTime.now());

//     LoginOutput? userLoginDetails =
//         DataProvider().getParsedUserData()?.output?.first;
//     dESGID = userLoginDetails?.dESGID ?? 0;
//     subOrgId = userLoginDetails?.subOrgId ?? 0;
//     empCode = userLoginDetails?.empCode ?? 0;
//     dISTLGDCODE = userLoginDetails?.dISTLGDCODE ?? 0;
//     getApprovedCampListDetailsForApp();
//   }

//   Future<void> _selectCampDate(BuildContext context) async {
//     final DateTime? picked = await showDatePicker(
//       context: context,
//       initialDate: DateTime.now(),
//       firstDate: DateTime(1880),
//       lastDate: DateTime(2101),
//     );
//     if (picked != null) {
//       setState(() {
//         _selectedCampDate = FormatterManager.formatDateToString(picked);
//         getApprovedCampListDetailsForApp();
//       });
//     }
//   }

//   getApprovedCampListDetailsForApp() {
//     ToastManager.showLoader();
//     Map<String, String> params = {
//       "CampDATE": _selectedCampDate,
//       "SubOrgId": subOrgId.toString(),
//       "Divison": "0",
//       "DISTLGDCODE": "0",
//       "USERID": empCode.toString(),
//       "DesgId": dESGID.toString(),
//     };
//     print(params);
//     apiManager.getApprovedCampListDetailsForAppFlexiCampAPI(
//       params,
//       apiApprovedCampListDetailsForAppFlexiCampCallBack,
//     );
//   }

//   void apiApprovedCampListDetailsForAppFlexiCampCallBack(
//     ResourceReMappingCampResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();
//     if (success) {
//       campList = response?.output ?? [];
//       searchCampList = campList;
//     } else {
//       campList = [];
//       searchCampList = campList;
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   getUserCampMappingAndAttendanceStatus() {
//     Map<String, String> params = {
//       "CampDATE": _selectedCampDate,
//       "UserId": empCode.toString(),
//       "DISTLGDCODE": dISTLGDCODE.toString(),
//       "CampType": _resourceReMappingCampOutput?.campType?.toString() ?? "",
//       "CampID": _resourceReMappingCampOutput?.campId?.toString() ?? "",
//       "TestId": "1",
//     };

//     apiManager.getUserCampMappingAndAttendanceStatusAPI(
//       params,
//       apiUserCampMappingAndAttendanceStatusCallBack,
//     );
//   }

//   void apiUserCampMappingAndAttendanceStatusCallBack(
//     UserCampMappingStatusResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();
//     if (success) {
//       UserCampMappingStatusOutput? obj = response?.output?.first;

//       if (obj != null) {
//         if (obj.isCampClosed == 1) {
//           ToastManager.toast("This camp is closed");
//         } else {
//           Navigator.push(
//             context,
//             MaterialPageRoute(
//               builder:
//                   (context) => ResourceReMappingUpdateScreen(
//                     reMappingCampOutput: _resourceReMappingCampOutput!,
//                   ),
//             ),
//           );
//         }
//       }
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   getCampAssignUserList() {
//     Map<String, String> params = {
//       "campid": _resourceReMappingCampOutput?.campId?.toString() ?? "",
//       "ResourceUserId": empCode.toString(),
//       "TestId": "0",
//     };

//     apiManager.getCampAssignUserListAPI(params, apiCampAssignUserListCallBack);
//   }

//   void apiCampAssignUserListCallBack(
//     GetCampAssignUserResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();
//     if (success) {
//       List<GetCampAssignUserOutput> list = response?.output ?? [];
//       bool allow = false;

//       if (list.isEmpty) {
//         ToastManager.toast("You are not authorise to Conduct this camp");
//       } else {
//         for (GetCampAssignUserOutput obj in list) {
//           if (obj.resourceUserId == empCode) {
//             if (obj.statusRes == 1) {
//               allow = true;
//             }
//           }
//         }

//         if (allow) {
//           Navigator.push(
//             context,
//             MaterialPageRoute(
//               builder:
//                   (context) => ResourceReMappingUpdateScreen(
//                     reMappingCampOutput: _resourceReMappingCampOutput!,
//                   ),
//             ),
//           );
//         } else {
//           ToastManager.toast("You are not authorise to Conduct this camp");
//         }
//       }
//     } else {
//       ToastManager.toast("You are not authorise to Conduct this camp");
//     }
//     setState(() {});
//   }

//   List<ResourceReMappingCampOutput> searchByDescEn(String query) {
//     return campList.where((item) {
//       final desc = item.campId?.toString().toLowerCase() ?? '';
//       return desc.contains(query.toLowerCase());
//     }).toList();
//   }

//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return KeyboardDismissOnTap(
//       child: Scaffold(
//         appBar: mAppBar(
//           scTitle: "Select Camp",
//           //for Health Screening
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
//                   mainAxisAlignment: MainAxisAlignment.start,
//                   children: [
//                     SizedBox(
//                       width: SizeConfig.screenWidth,
//                       child: Row(
//                         children: [
//                           Expanded(
//                             child: Container(
//                               width: MediaQuery.of(context).size.width,
//                               color: Colors.transparent,
//                               child: AppDateTextfield(
//                                 icon: icCalendarMonth,
//                                 titleHeaderString: "Camp Date*",
//                                 valueString: _selectedCampDate,
//                                 onTap: () {
//                                   _selectCampDate(context);
//                                 },
//                               ),
//                             ),
//                           ),

//                           const SizedBox(width: 8),
//                           Expanded(
//                             child: AppIconSearchTextfield(
//                               icon: icSearch,
//                               titleHeaderString: "Search Camp ID",
//                               controller: searchController,
//                               textInputType: TextInputType.number,
//                               onChange: (value) {
//                                 searchCampList = searchByDescEn(value);
//                                 setState(() {});
//                               },
//                             ),
//                           ),
//                         ],
//                       ),
//                     ),
//                     const SizedBox(height: 8),
//                     Expanded(
//                       child: ListView.builder(
//                         shrinkWrap: true,
//                         itemCount: searchCampList.length,
//                         itemBuilder: (context, index) {
//                           ResourceReMappingCampOutput reMappingCampOutput =
//                               searchCampList[index];
//                           return ResourceReMappingCampListRow(
//                             reMappingCampOutput: reMappingCampOutput,
//                             onSelectTap: () {
//                               int createdBy =
//                                   reMappingCampOutput.createdBy ?? 0;
//                               if (dESGID == 29 ||
//                                   dESGID == 103 ||
//                                   dESGID == 84 ||
//                                   dESGID == 100 ||
//                                   dESGID == 34 ||
//                                   dESGID == 104 ||
//                                   dESGID == 107 ||
//                                   dESGID == 113 ||
//                                   dESGID == 92 ||
//                                   dESGID == 162) {
//                                 if (empCode != createdBy) {
//                                   ToastManager.toast(
//                                     "This camp not created by you, please select created camp",
//                                   );
//                                   return;
//                                 }
//                                 _resourceReMappingCampOutput =
//                                     reMappingCampOutput;
//                                 getUserCampMappingAndAttendanceStatus();
//                               } else {
//                                 getCampAssignUserList();
//                               }
//                             },
//                           );
//                         },
//                       ),
//                     ),
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
