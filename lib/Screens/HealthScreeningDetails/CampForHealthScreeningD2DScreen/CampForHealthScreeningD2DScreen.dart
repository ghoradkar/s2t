// ignore_for_file: must_be_immutable, avoid_print, file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Screens/CallingModules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/CallingModules/custom_widgets/no_data_widget.dart';
import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/CampDetailsonLabForDoorToDoorResponse/CampDetailsonLabForDoorToDoorResponse.dart';
import '../../../Modules/Json_Class/UserCampMappingAndAttendanceStatusResponse/UserCampMappingAndAttendanceStatusResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../../../Modules/widgets/CommonSkeletonList.dart';
import '../HealthScreeningDetailsScreen/HealthScreeningDetailsScreen.dart';
import 'CampForHealthScreeningD2DRow/CampForHealthScreeningD2DRow.dart';

class CampForHealthScreeningD2DScreen extends StatefulWidget {
  CampForHealthScreeningD2DScreen({super.key, required this.testID});

  int testID = 0;

  @override
  State<CampForHealthScreeningD2DScreen> createState() =>
      _CampForHealthScreeningD2DScreenState();
}

class _CampForHealthScreeningD2DScreenState
    extends State<CampForHealthScreeningD2DScreen> {
  TextEditingController searchController = TextEditingController();

  String _selectedCampDate = "";
  int dESGID = 0;
  String? district;
  int cityCode = 0;
  int subOrgId = 0;
  int dISTLGDCODE = 0;
  int empCode = 0;
  bool isUserInteractionEnabled = true;
  bool isLoading = false;

  CampDetailsonLabForDoorToDoorOutput? selelctedObj;

  List<CampDetailsonLabForDoorToDoorOutput> campList = [];
  List<CampDetailsonLabForDoorToDoorOutput> searchCampList = [];
  APIManager apiManager = APIManager();

  @override
  void initState() {
    super.initState();
    _selectedCampDate = FormatterManager.formatDateToString(DateTime.now());
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    district = DataProvider().getParsedUserData()?.output?.first.district;
    cityCode = DataProvider().getParsedUserData()?.output?.first.cityCode ?? 0;
    subOrgId = DataProvider().getParsedUserData()?.output?.first.subOrgId ?? 0;
    dISTLGDCODE =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    if (dESGID == 29 ||
        dESGID == 141 ||
        dESGID == 108 ||
        dESGID == 169 ||
        dESGID == 139 ||
        dESGID == 35 ||
        dESGID == 92 ||
        dESGID == 136 ||
        dESGID == 146) {
      isUserInteractionEnabled = false;
    } else {
      isUserInteractionEnabled = true;
    }
    getCampDetailsonLabForDoorToDoor();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return NetworkWrapper(
      child: KeyboardDismissOnTap(
        child: Scaffold(
          appBar: mAppBar(
            scTitle: "Select Camp",
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () {
              Navigator.pop(context);
            },
          ),
          body: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              Row(
                children: [
                  Expanded(
                    child: AppTextField(
                      controller: TextEditingController(
                        text: district ?? "Pune",
                      ),
                      readOnly: !isUserInteractionEnabled,
                      onTap: () {},
                      hint: 'District',
                      label: CommonText(
                        text: 'District',
                        fontSize: 14.sp * 1.33,
                        fontWeight: FontWeight.normal,
                        textColor: kBlackColor,
                        textAlign: TextAlign.start,
                      ),
                      hintStyle: TextStyle(
                        fontSize: 14.sp * 1.33,
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
                    // AppDropdownTextfield(
                    //   icon: icMapPin,
                    //   titleHeaderString: "District",
                    //   valueString: "Pune",
                    //   isDisabled: !isUserInteractionEnabled,
                    //   onTap: () {},
                    // ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: AppTextField(
                      controller: TextEditingController(
                        text: _selectedCampDate,
                      ),
                      readOnly: true,
                      onTap: () {
                        _selectCampDate(context);
                      },
                      hint: 'Camp Date*',
                      label: CommonText(
                        text: 'Camp Date*',
                        fontSize: 14.sp * 1.33,
                        fontWeight: FontWeight.normal,
                        textColor: kBlackColor,
                        textAlign: TextAlign.start,
                      ),
                      hintStyle: TextStyle(
                        fontSize: 14.sp * 1.33,
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
                    //   valueColor: Colors.black,
                    //   onTap: () {
                    //     _selectCampDate(context);
                    //   },
                    // ),
                  ),
                ],
              ).paddingOnly(top: 10, left: 4, right: 4),
              const SizedBox(height: 8),
              Expanded(
                child:
                    isLoading
                        ? const CommonSkeletonList()
                        : searchCampList.isEmpty
                        ? NoDataFound().paddingOnly(left: 4.w, right: 4.w)
                        : ListView.builder(
                          itemCount: searchCampList.length,
                          itemBuilder: (context, index) {
                            CampDetailsonLabForDoorToDoorOutput obj =
                                searchCampList[index];
                            return CampForHealthScreeningD2DRow(
                              campDetailsonLabForDoorToDoorOutput: obj,
                              onSelectTap: () {
                                selelctedObj = obj;

                                if (dESGID == 29 ||
                                    dESGID == 92 ||
                                    dESGID == 104 ||
                                    dESGID == 160 ||
                                    dESGID == 105 ||
                                    dESGID == 77 ||
                                    dESGID == 84 ||
                                    dESGID == 30 ||
                                    dESGID == 108 ||
                                    dESGID == 147 ||
                                    dESGID == 130 ||
                                    dESGID == 139 ||
                                    dESGID == 136 ||
                                    dESGID == 141 ||
                                    dESGID == 108) {
                                  showHelthScreeningDetailsPage();
                                } else {
                                  getUserCampMappingAndAttendanceStatusD2D(
                                    obj.dISTLGDCODE ?? 0,
                                    obj.campType ?? 0,
                                    obj.campId ?? 0,
                                  );
                                }
                              },
                            );
                          },
                        ),
              ),
            ],
          ).paddingSymmetric(horizontal: 8),
        ),
      ),
    );
  }

  void getCampDetailsonLabForDoorToDoor() {
    setState(() {
      isLoading = true;
    });
    Map<String, String> params = {
      "CampDate": _selectedCampDate,
      "LabCode": cityCode.toString(),
      "SubOrgId": subOrgId.toString(),
      "Divison": "0",
      "DISTLGDCODE": dISTLGDCODE.toString(),
      "USERID": empCode.toString(),
      "dESGID": dESGID.toString(),
    };
    print(params);
    apiManager.getCampDetailsonLabForDoorToDoorV2API(
      params,
      apiCampDetailsonLabForDoorToDoorCallBack,
    );
  }

  void apiCampDetailsonLabForDoorToDoorCallBack(
    CampDetailsonLabForDoorToDoorResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      campList = response?.output ?? [];
      searchCampList = campList;
    } else {
      campList = [];
      searchCampList = [];
      ToastManager.toast(errorMessage);
    }
    isLoading = false;
    setState(() {});
  }

  void getUserCampMappingAndAttendanceStatusD2D(
    int districtCode,
    int campType,
    int campId,
  ) {
    Map<String, String> params = {
      "CampDATE": _selectedCampDate,
      "UserId": empCode.toString(),
      "DISTLGDCODE": districtCode.toString(),
      "CampType": campType.toString(),
      "CampID": campId.toString(),
    };
    print(params);

    apiManager.getUserCampMappingAndAttendanceStatusD2DAPI(
      params,
      apiUserCampMappingAndAttendanceStatusD2DCallBack,
    );
  }

  void apiUserCampMappingAndAttendanceStatusD2DCallBack(
    UserCampMappingAndAttendanceStatusResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      UserCampMappingAndAttendanceStatusOutput? obj = response?.output?.first;

      if (obj != null) {
        if (obj.isCampClosed == 1) {
          ToastManager.toast("This camp is closed");
          return;
        }

        if (obj.isReadinessFormFilled == 1) {
          ToastManager.toast("Please fill camp readiness form");
          return;
        }

        if (obj.attendanceFlag == 1) {
          ToastManager.toast("Please mark attendance first");
          return;
        }

        if (obj.campFlag == 1) {
          ToastManager.toast("This camp not mapped to you");
          return;
        }
        showHelthScreeningDetailsPage();
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
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
        getCampDetailsonLabForDoorToDoor();
      });
    }
  }

  void showHelthScreeningDetailsPage() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder:
            (context) => HealthScreeningDetailsScreen(
              testID: widget.testID,
              teamid: 0,
              districtID: selelctedObj?.dISTLGDCODE ?? 0,
              districtName: selelctedObj?.dISTNAME ?? "",
              campID: selelctedObj?.campId ?? 0,
              dISTLGDCODE: selelctedObj?.dISTLGDCODE ?? 0,
              campType: selelctedObj?.campType ?? 0,
              campDate: _selectedCampDate,
              surveyCoordinatorName: "",
              campTypeDescription: selelctedObj?.campTypeDescription ?? "",
            ),
      ),
    );
  }
}

// class CampForHealthScreeningD2DScreen extends StatefulWidget {
//   CampForHealthScreeningD2DScreen({super.key, required this.testID});

//   int testID = 0;

//   @override
//   State<CampForHealthScreeningD2DScreen> createState() =>
//       _CampForHealthScreeningD2DScreenState();
// }

// class _CampForHealthScreeningD2DScreenState
//     extends State<CampForHealthScreeningD2DScreen> {
//   TextEditingController searchController = TextEditingController();

//   String _selectedCampDate = "";
//   int dESGID = 0;
//   int cityCode = 0;
//   int subOrgId = 0;
//   int dISTLGDCODE = 0;
//   int empCode = 0;
//   bool isUserInteractionEnabled = true;

//   CampDetailsonLabForDoorToDoorOutput? selelctedObj;

//   List<CampDetailsonLabForDoorToDoorOutput> campList = [];
//   List<CampDetailsonLabForDoorToDoorOutput> searchCampList = [];
//   APIManager apiManager = APIManager();
//   @override
//   void initState() {
//     super.initState();
//     _selectedCampDate = FormatterManager.formatDateToString(DateTime.now());
//     dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
//     cityCode = DataProvider().getParsedUserData()?.output?.first.cityCode ?? 0;
//     subOrgId = DataProvider().getParsedUserData()?.output?.first.subOrgId ?? 0;
//     dISTLGDCODE =
//         DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
//     empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

//     if (dESGID == 29 ||
//         dESGID == 141 ||
//         dESGID == 108 ||
//         dESGID == 169 ||
//         dESGID == 139 ||
//         dESGID == 35 ||
//         dESGID == 92 ||
//         dESGID == 136 ||
//         dESGID == 146) {
//       isUserInteractionEnabled = false;
//     } else {
//       isUserInteractionEnabled = true;
//     }
//     getCampDetailsonLabForDoorToDoor();
//   }

//   void getCampDetailsonLabForDoorToDoor() {
//     ToastManager.showLoader();
//     Map<String, String> params = {
//       "CampDate": _selectedCampDate,
//       "LabCode": cityCode.toString(),
//       "SubOrgId": subOrgId.toString(),
//       "Divison": "0",
//       "DISTLGDCODE": dISTLGDCODE.toString(),
//       "USERID": empCode.toString(),
//       "dESGID": dESGID.toString(),
//     };
//     print(params);
//     apiManager.getCampDetailsonLabForDoorToDoorV2API(
//       params,
//       apiCampDetailsonLabForDoorToDoorCallBack,
//     );
//   }

//   void apiCampDetailsonLabForDoorToDoorCallBack(
//     CampDetailsonLabForDoorToDoorResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();
//     if (success) {
//       campList = response?.output ?? [];
//       searchCampList = campList;
//     } else {
//       campList = [];
//       searchCampList = [];
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   void getUserCampMappingAndAttendanceStatusD2D(
//     int districtCode,
//     int campType,
//     int campId,
//   ) {
//     Map<String, String> params = {
//       "CampDATE": _selectedCampDate,
//       "UserId": empCode.toString(),
//       "DISTLGDCODE": districtCode.toString(),
//       "CampType": campType.toString(),
//       "CampID": campId.toString(),
//     };
//     print(params);

//     apiManager.getUserCampMappingAndAttendanceStatusD2DAPI(
//       params,
//       apiUserCampMappingAndAttendanceStatusD2DCallBack,
//     );
//   }

//   void apiUserCampMappingAndAttendanceStatusD2DCallBack(
//     UserCampMappingAndAttendanceStatusResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();
//     if (success) {
//       UserCampMappingAndAttendanceStatusOutput? obj = response?.output?.first;

//       if (obj != null) {
//         if (obj.isCampClosed == 1) {
//           ToastManager.toast("This camp is closed");
//           return;
//         }

//         if (obj.isReadinessFormFilled == 1) {
//           ToastManager.toast("Please fill camp readiness form");
//           return;
//         }

//         if (obj.attendanceFlag == 1) {
//           ToastManager.toast("Please mark attendance first");
//           return;
//         }

//         if (obj.campFlag == 1) {
//           ToastManager.toast("This camp not mapped to you");
//           return;
//         }
//         showHelthScreeningDetailsPage();
//       }
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
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
//         getCampDetailsonLabForDoorToDoor();
//       });
//     }
//   }

//   void showHelthScreeningDetailsPage() {
//     Navigator.push(
//       context,
//       MaterialPageRoute(
//         builder:
//             (context) => HealthScreeningDetailsScreen(
//               testID: widget.testID,
//               teamid: 0,
//               districtID: selelctedObj?.dISTLGDCODE ?? 0,
//               districtName: selelctedObj?.dISTNAME ?? "",
//               campID: selelctedObj?.campId ?? 0,
//               dISTLGDCODE: selelctedObj?.dISTLGDCODE ?? 0,
//               campType: selelctedObj?.campType ?? 0,
//               campDate: _selectedCampDate,
//               surveyCoordinatorName: "",
//               campTypeDescription: selelctedObj?.campTypeDescription ?? "",
//             ),
//       ),
//     );
//   }

//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return KeyboardDismissOnTap(
//       child: Scaffold(
//         appBar: mAppBar(
//           scTitle: "Select Camp",
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
//                 child: Container(
//                   color: Colors.transparent,
//                   child: Column(
//                     mainAxisAlignment: MainAxisAlignment.start,
//                     children: [
//                       AppDateTextfield(
//                         icon: icCalendarMonth,
//                         titleHeaderString: "Camp Date*",
//                         valueString: _selectedCampDate,
//                         valueColor: Colors.black,
//                         onTap: () {
//                           _selectCampDate(context);
//                         },
//                       ),
//                       const SizedBox(height: 4),
//                       Row(
//                         children: [
//                           Expanded(
//                             child: AppDropdownTextfield(
//                               icon: icMapPin,
//                               titleHeaderString: "District",
//                               valueString: "Pune",
//                               isDisabled: !isUserInteractionEnabled,
//                               onTap: () {},
//                             ),
//                           ),
//                           const SizedBox(width: 8),
//                           Expanded(
//                             child: AppIconSearchTextfield(
//                               icon: icSearch,
//                               titleHeaderString: "Search Camp ID",
//                               controller: searchController,
//                               onChange: (p0) {},
//                             ),
//                           ),
//                         ],
//                       ),
//                       const SizedBox(height: 8),
//                       Expanded(
//                         child: ListView.builder(
//                           itemCount: searchCampList.length,
//                           itemBuilder: (context, index) {
//                             CampDetailsonLabForDoorToDoorOutput obj =
//                                 searchCampList[index];
//                             return CampForHealthScreeningD2DRow(
//                               campDetailsonLabForDoorToDoorOutput: obj,
//                               onSelectTap: () {
//                                 selelctedObj = obj;

//                                 if (dESGID == 29 ||
//                                     dESGID == 92 ||
//                                     dESGID == 104 ||
//                                     dESGID == 160 ||
//                                     dESGID == 105 ||
//                                     dESGID == 77 ||
//                                     dESGID == 84 ||
//                                     dESGID == 30 ||
//                                     dESGID == 108 ||
//                                     dESGID == 147 ||
//                                     dESGID == 130 ||
//                                     dESGID == 139 ||
//                                     dESGID == 136 ||
//                                     dESGID == 141 ||
//                                     dESGID == 108) {
//                                   showHelthScreeningDetailsPage();
//                                 } else {
//                                   getUserCampMappingAndAttendanceStatusD2D(
//                                     obj.dISTLGDCODE ?? 0,
//                                     obj.campType ?? 0,
//                                     obj.campId ?? 0,
//                                   );
//                                 }
//                               },
//                             );
//                           },
//                         ),
//                       ),
//                     ],
//                   ),
//                 ),
//               ),
//             ],
//           ),
//         ),
//       ),
//     );
//   }
// }
