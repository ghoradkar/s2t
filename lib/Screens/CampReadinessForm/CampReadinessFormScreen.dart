// ignore_for_file: file_names, avoid_print

import 'dart:convert';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';

import '../../../../../Modules/constants/fonts.dart';
import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import '../../Modules/Enums/Enums.dart';
import '../../Modules/FormatterManager/FormatterManager.dart';
import '../../Modules/Json_Class/CampIdListResponse/CampIdListResponse.dart';
import '../../Modules/Json_Class/CampReadinessFormListResponse/CampReadinessFormListResponse.dart';
import '../../Modules/Json_Class/CampReadinessFormSubmittResponse/CampReadinessFormSubmittResponse.dart';
import '../../Modules/Json_Class/CampTypeResponse/CampTypeResponse.dart';
import '../../Screens/d2d_physical_examination/model/TeamNumberByCampIdAndUserIdListResponse.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/AppActiveButton.dart';
import '../../Modules/widgets/AppDropdownTextfield.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import '../../Views/DropDownListScreen/DropDownListScreen.dart';
import 'CampReadinessFormRow/CampReadinessFormRow.dart';

class CampReadinessFormScreen extends StatefulWidget {
  const CampReadinessFormScreen({super.key});

  @override
  State<CampReadinessFormScreen> createState() =>
      _CampReadinessFormScreenState();
}

class _CampReadinessFormScreenState extends State<CampReadinessFormScreen> {
  final formKey = GlobalKey<FormState>();
  String _selectedCampDate = "";
  CampTypeOutput? selectedCampType;
  CampIdOutput? selectedCampID;

  int dESGID = 0;
  int empCode = 0;
  int districtId = 0;
  String districtName = "";
  String teamId = "0";
  String teamName = "";

  List<CampReadinessFormOutput> campReadinessList = [];
  ToastManager toastManager = ToastManager();
  APIManager apiManager = APIManager();

  bool showTeam = false;
  bool showSubmitButton = true;

  bool isFormSubmitted = false;

  @override
  void initState() {
    super.initState();
    _selectedCampDate = FormatterManager.formatDateToString(DateTime.now());
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    districtId =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
    districtName =
        DataProvider().getParsedUserData()?.output?.first.district ?? "";
  }

  chooseCampType() {
    ToastManager.showLoader();

    if (dESGID == 129 || dESGID == 146) {
      apiManager.getCampTypeFlexiAPI(apiCampTypeFlexiCallBack);
    } else if (dESGID == 138 ||
        dESGID == 137 ||
        dESGID == 169 ||
        dESGID == 177 ||
        dESGID == 31 ||
        dESGID == 176) {
      apiManager.getCampTypeMMUAPI(apiCampTypeMMUCallBack);
    } else if (dESGID == 35 || dESGID == 86) {
      getCampTypeD2D();
    } else {
      getCampTypeRegular();
    }
  }

  getCampTypeRegular() {
    ToastManager.hideLoader();
    List<CampTypeOutput> campTypeList = [];
    campTypeList.add(
      CampTypeOutput(cAMPTYPE: 1, campTypeDescription: "REGULAR"),
    );

    _showDropDownBottomSheet(
      "Camp Type",
      campTypeList,
      DropDownTypeMenu.CampType,
    );
  }

  getCampTypeD2D() {
    ToastManager.hideLoader();
    List<CampTypeOutput> campTypeList = [];
    campTypeList.add(
      CampTypeOutput(cAMPTYPE: 3, campTypeDescription: "DOOR TO DOOR"),
    );

    _showDropDownBottomSheet(
      "Camp Type",
      campTypeList,
      DropDownTypeMenu.CampType,
    );
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

  chooseCampID() {
    if (selectedCampType == null) {
      ToastManager.toast("Please select Camp Type");
    } else {
      Map<String, String> dict = {
        "CampDATE": _selectedCampDate,
        "UserId": empCode.toString(),
        "DISTLGDCODE": districtId.toString(),
        "CampType": selectedCampType?.cAMPTYPE.toString() ?? "0",
        "LABCODE": "0",
      };

      print(dict);
      apiManager.getCampListCampReadinessAPI(dict, apiCampIDCallBack);
    }
  }

  void apiCampIDCallBack(
    CampIdListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp ID",
        response?.output ?? [],
        DropDownTypeMenu.CampReadinessCampID,
      );
    } else {
      ToastManager.toast(errorMessage);
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
              } else if (dropDownType == DropDownTypeMenu.CampReadinessCampID) {
                selectedCampID = p0;
                if (dESGID == 35 ||
                    dESGID == 129 ||
                    dESGID == 86 ||
                    dESGID == 146 ||
                    dESGID == 138 ||
                    dESGID == 137 ||
                    dESGID == 169 ||
                    dESGID == 177 ||
                    dESGID == 31 ||
                    dESGID == 176) {
                  getTeamId();
                } else {
                  getCampdetails();
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

  getTeamId() {
    Map<String, String> dict = {
      "campid": selectedCampID?.campId.toString() ?? "0",
      "UserID": empCode.toString(),
    };
    apiManager.getTeamNumberByCampIdAndUSerIdAPI(dict, apiTeamIdCallBack);
  }

  void apiTeamIdCallBack(
    TeamNumberByCampIdAndUserIdListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      TeamNumberByCampIdOutput? firstobj = response?.output?.first;

      if (firstobj != null) {
        teamId = firstobj.teamNumber ?? "0";
        teamName = firstobj.teamName ?? "";
        showTeam = true;
        getCampdetails();
      } else {
        ToastManager.toast("Data not found");
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  getCampdetails() {
    ToastManager.showLoader();

    apiManager.getCampReadinessFormItemsAPI(
      selectedCampID?.campId ?? 0,
      int.parse(teamId),
      apiCampReadinessFormItemsCallBack,
    );
  }

  void apiCampReadinessFormItemsCallBack(
    CampReadinessFormListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      campReadinessList = [];
      setState(() {});
      campReadinessList = response?.output ?? [];

      if (campReadinessList.isNotEmpty) {
        CampReadinessFormOutput firstObj = campReadinessList.first;

        if (firstObj.itemStatus != null) {
          showSubmitButton = false;
          isFormSubmitted = true;
        } else {
          isFormSubmitted = false;
          showSubmitButton = true;
        }
      }
    } else {
      campReadinessList = [];
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  String campReadinessString(List<Map<String, dynamic>> items) {
    try {
      String jsonString = jsonEncode(items);
      return jsonString;
    } catch (e) {
      print("Error encoding JSON: $e");
      return "";
    }
  }

  validations() {
    bool isSuccess = true;
    List<Map<String, String>> items = [];
    for (var objIn in campReadinessList) {
      items.add({
        "ItemId": objIn.itemId.toString(),
        "ItemStatus": objIn.itemStatus.toString(),
      });

      if (objIn.itemStatus == 0 || objIn.itemStatus == null) {
        isSuccess = false;
        break;
      }
    }
    String jsonString = campReadinessString(items);
    if (isSuccess) {
      submitData(jsonString);
    } else {
      ToastManager.toast("Please select all the items.");
    }
  }

  submitData(String jsonString) {
    ToastManager.showLoader();
    Map<String, String> dict = {
      "CampID": selectedCampID?.campId.toString() ?? "0",
      "CampType": selectedCampType?.cAMPTYPE.toString() ?? "0",
      "Createdby": empCode.toString(),
      "TeamId": teamId,
      "Type_CampReadinessForm": jsonString,
    };
    apiManager.insertCampReadinessFormDetailsAPI(
      dict,
      apiInsertCampReadinessFormDetailsCallBack,
    );
  }

  void apiInsertCampReadinessFormDetailsCallBack(
    CampReadinessFormSubmittResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      ToastManager.showLoader();
      getCampdetails();
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Camp Readiness Form",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: Container(
          decoration: BoxDecoration(
            color: Colors.transparent,
            // boxShadow: [
            //   BoxShadow(
            //     color: Colors.black.withValues(alpha: 0.15),
            //     blurRadius: 10,
            //   ),
            // ],
            // borderRadius: BorderRadius.circular(10),
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              Container(
                width: MediaQuery.of(context).size.width,
                decoration: const BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.all(Radius.circular(10)),
                ),
                padding: const EdgeInsets.all(8),
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
                              text: _selectedCampDate,
                            ),

                            inputStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontSize: 14,
                            ),
                            label: RichText(
                              text: TextSpan(
                                text: 'Camp Date*',
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color: kLabelTextColor,
                                  fontSize: responsiveFont(20),
                                  fontWeight: FontWeight.w400,
                                ),
                                children: <TextSpan>[],
                              ),
                            ),
                            labelStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: responsiveFont(14),
                            ),
                            prefixIcon: Image.asset(
                              icCalendarMonth,
                              scale: 4.0,
                            ),
                          ),
                          // AppDateTextfield(
                          //   icon: icCalendarMonth,
                          //   titleHeaderString: "Camp Date*",
                          //   valueString: _selectedCampDate,
                          //   valueColor: Colors.grey,
                          //   onTap: () {},
                          // ),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: AppTextField(
                            readOnly: true,
                            onTap: () {
                              chooseCampType();
                            },
                            controller: TextEditingController(
                              text: selectedCampType?.campTypeDescription ?? "",
                            ),

                            inputStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontSize: 14,
                            ),
                            label: RichText(
                              text: TextSpan(
                                text: 'Camp Type*',
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color: kLabelTextColor,
                                  fontSize: responsiveFont(14),
                                  fontWeight: FontWeight.w400,
                                ),
                                children: <TextSpan>[],
                              ),
                            ),
                            labelStyle: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: responsiveFont(14),
                            ),
                            prefixIcon: Image.asset(icnTent, scale: 4.0),
                            suffixIcon: Icon(Icons.keyboard_arrow_down),
                          ),

                          // AppDropdownTextfield(
                          //   icon: icnTent,
                          //   titleHeaderString: "Camp Type*",
                          //   valueString:
                          //       selectedCampType?.campTypeDescription ?? "",
                          //   onTap: () {
                          //     chooseCampType();
                          //   },
                          // ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    // AppDropdownTextfield(
                    //   icon: icHashIcon,
                    //   titleHeaderString: "Camp ID*",
                    //   valueString: selectedCampID?.campId.toString() ?? "",
                    //   onTap: () {
                    //     chooseCampID();
                    //   },
                    // ),
                    AppTextField(
                      readOnly: true,
                      onTap: () {
                        chooseCampID();
                      },
                      controller: TextEditingController(
                        text: selectedCampID?.campId.toString() ?? "",
                      ),

                      inputStyle: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: 14,
                      ),
                      label: RichText(
                        text: TextSpan(
                          text: 'Camp Type*',
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kLabelTextColor,
                            fontSize: responsiveFont(20),
                            fontWeight: FontWeight.w400,
                          ),
                          children: <TextSpan>[],
                        ),
                      ),
                      labelStyle: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w400,
                        fontSize: responsiveFont(14),
                      ),
                      prefixIcon: Image.asset(icHashIcon, scale: 4.0),
                      suffixIcon: Icon(Icons.keyboard_arrow_down),
                    ),
                    showTeam == true ? const SizedBox(height: 8) : Container(),
                    showTeam == true
                        ? AppDropdownTextfield(
                          icon: icTeamIcon,
                          titleHeaderString: "Team Id*",
                          valueString: teamName,
                          onTap: () {},
                        )
                        : Container(),
                  ],
                ),
              ),
              const SizedBox(height: 8),
              Expanded(
                child: ListView.builder(
                  itemCount: campReadinessList.length,
                  itemBuilder: (context, index) {
                    CampReadinessFormOutput object = campReadinessList[index];
                    return CampReadinessFormRow(
                      index: index,
                      object: object,
                      isFormSubmitted: isFormSubmitted,
                    );
                  },
                ),
              ),
              Container(
                width: MediaQuery.of(context).size.width,
                color: Colors.white,
                padding: const EdgeInsets.all(8),
                child:
                    showSubmitButton == true
                        ? Column(
                          mainAxisAlignment: MainAxisAlignment.start,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            SizedBox(
                              width: MediaQuery.of(context).size.width,
                              child: Row(
                                children: [
                                  Expanded(
                                    child: AppActiveButton(
                                      buttontitle: "Cancel",
                                      isCancel: true,
                                      onTap: () {
                                        Navigator.pop(context);
                                      },
                                    ),
                                  ),
                                  const SizedBox(width: 12),
                                  Expanded(
                                    child: AppActiveButton(
                                      buttontitle: "Submit",
                                      onTap: () {
                                        validations();
                                      },
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ],
                        )
                        : Text(
                          textAlign: TextAlign.center,
                          "Camp readiness form already submitted",
                          style: TextStyle(
                            color: Colors.green,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w600,
                            fontSize: responsiveFont(16),
                          ),
                        ),
              ),
            ],
          ),
        ).paddingSymmetric(vertical: 4.h, horizontal: 8.w),
      ),
    );
  }
}

// class CampReadinessFormScreen extends StatefulWidget {
//   const CampReadinessFormScreen({super.key});

//   @override
//   State<CampReadinessFormScreen> createState() =>
//       _CampReadinessFormScreenState();
// }

// class _CampReadinessFormScreenState extends State<CampReadinessFormScreen> {
//   final formKey = GlobalKey<FormState>();
//   String _selectedCampDate = "";
//   CampTypeOutput? selectedCampType;
//   CampIdOutput? selectedCampID;

//   int dESGID = 0;
//   int empCode = 0;
//   int districtId = 0;
//   String districtName = "";
//   String teamId = "0";
//   String teamName = "";

//   List<CampReadinessFormOutput> campReadinessList = [];
//   ToastManager toastManager = ToastManager();
//   APIManager apiManager = APIManager();

//   bool showTeam = false;
//   bool showSubmitButton = true;

//   bool isFormSubmitted = false;
//   @override
//   void initState() {
//     super.initState();
//     _selectedCampDate = FormatterManager.formatDateToString(DateTime.now());
//     dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
//     empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
//     districtId =
//         DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
//     districtName =
//         DataProvider().getParsedUserData()?.output?.first.district ?? "";
//   }

//   chooseCampType() {
//     ToastManager.showLoader();

//     if (dESGID == 129 || dESGID == 146) {
//       apiManager.getCampTypeFlexiAPI(apiCampTypeFlexiCallBack);
//     } else if (dESGID == 138 ||
//         dESGID == 137 ||
//         dESGID == 169 ||
//         dESGID == 177 ||
//         dESGID == 31 ||
//         dESGID == 176) {
//       apiManager.getCampTypeMMUAPI(apiCampTypeMMUCallBack);
//     } else if (dESGID == 35 || dESGID == 86) {
//       getCampTypeD2D();
//     } else {
//       getCampTypeRegular();
//     }
//   }

//   getCampTypeRegular() {
//     ToastManager.hideLoader();
//     List<CampTypeOutput> campTypeList = [];
//     campTypeList.add(
//       CampTypeOutput(cAMPTYPE: 1, campTypeDescription: "REGULAR"),
//     );

//     _showDropDownBottomSheet(
//       "Camp Type",
//       campTypeList,
//       DropDownTypeMenu.CampType,
//     );
//   }

//   getCampTypeD2D() {
//     ToastManager.hideLoader();
//     List<CampTypeOutput> campTypeList = [];
//     campTypeList.add(
//       CampTypeOutput(cAMPTYPE: 3, campTypeDescription: "DOOR TO DOOR"),
//     );

//     _showDropDownBottomSheet(
//       "Camp Type",
//       campTypeList,
//       DropDownTypeMenu.CampType,
//     );
//   }

//   void apiCampTypeFlexiCallBack(
//     CampTypeResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       _showDropDownBottomSheet(
//         "Camp Type",
//         response?.output ?? [],
//         DropDownTypeMenu.CampType,
//       );
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//   }

//   void apiCampTypeMMUCallBack(
//     CampTypeResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       _showDropDownBottomSheet(
//         "Camp Type",
//         response?.output ?? [],
//         DropDownTypeMenu.CampType,
//       );
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//   }

//   chooseCampID() {
//     if (selectedCampType == null) {
//       ToastManager.toast("Please select Camp Type");
//     } else {
//       Map<String, String> dict = {
//         "CampDATE": _selectedCampDate,
//         "UserId": empCode.toString(),
//         "DISTLGDCODE": districtId.toString(),
//         "CampType": selectedCampType?.cAMPTYPE.toString() ?? "0",
//         "LABCODE": "0",
//       };

//       print(dict);
//       apiManager.getCampListCampReadinessAPI(dict, apiCampIDCallBack);
//     }
//   }

//   void apiCampIDCallBack(
//     CampIdListResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       _showDropDownBottomSheet(
//         "Camp ID",
//         response?.output ?? [],
//         DropDownTypeMenu.CampReadinessCampID,
//       );
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//   }

//   void _showDropDownBottomSheet(
//     String title,
//     List<dynamic> list,
//     DropDownTypeMenu dropDownType,
//   ) {
//     showModalBottomSheet(
//       context: context,
//       isScrollControlled: true,
//       constraints: const BoxConstraints(minWidth: double.infinity),
//       backgroundColor: Colors.white,
//       isDismissible: false,
//       enableDrag: false,
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
//           child: DropDownListScreen(
//             titleString: title,
//             dropDownList: list,
//             dropDownMenu: dropDownType,
//             onApplyTap: (p0) {
//               if (dropDownType == DropDownTypeMenu.CampType) {
//                 selectedCampType = p0;
//               } else if (dropDownType == DropDownTypeMenu.CampReadinessCampID) {
//                 selectedCampID = p0;
//                 if (dESGID == 35 ||
//                     dESGID == 129 ||
//                     dESGID == 86 ||
//                     dESGID == 146 ||
//                     dESGID == 138 ||
//                     dESGID == 137 ||
//                     dESGID == 169 ||
//                     dESGID == 177 ||
//                     dESGID == 31 ||
//                     dESGID == 176) {
//                   getTeamId();
//                 } else {
//                   getCampdetails();
//                 }
//               }
//               setState(() {});
//             },
//           ),
//         );
//       },
//     ).whenComplete(() {
//       setState(() {});
//     });
//   }

//   getTeamId() {
//     Map<String, String> dict = {
//       "campid": selectedCampID?.campId.toString() ?? "0",
//       "UserID": empCode.toString(),
//     };
//     apiManager.getTeamNumberByCampIdAndUSerIdAPI(dict, apiTeamIdCallBack);
//   }

//   void apiTeamIdCallBack(
//     TeamNumberByCampIdAndUserIdListResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       TeamNumberByCampIdOutput? firstobj = response?.output?.first;

//       if (firstobj != null) {
//         teamId = firstobj.teamNumber ?? "0";
//         teamName = firstobj.teamName ?? "";
//         showTeam = true;
//         getCampdetails();
//       } else {
//         ToastManager.toast("Data not found");
//       }
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   getCampdetails() {
//     ToastManager.showLoader();

//     apiManager.getCampReadinessFormItemsAPI(
//       selectedCampID?.campId ?? 0,
//       int.parse(teamId),
//       apiCampReadinessFormItemsCallBack,
//     );
//   }

//   void apiCampReadinessFormItemsCallBack(
//     CampReadinessFormListResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();
//     if (success) {
//       campReadinessList = [];
//       setState(() {});
//       campReadinessList = response?.output ?? [];

//       if (campReadinessList.isNotEmpty) {
//         CampReadinessFormOutput firstObj = campReadinessList.first;

//         if (firstObj.itemStatus != null) {
//           showSubmitButton = false;
//           isFormSubmitted = true;
//         } else {
//           isFormSubmitted = false;
//           showSubmitButton = true;
//         }
//       }
//     } else {
//       campReadinessList = [];
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   String campReadinessString(List<Map<String, dynamic>> items) {
//     try {
//       String jsonString = jsonEncode(items);
//       return jsonString;
//     } catch (e) {
//       print("Error encoding JSON: $e");
//       return "";
//     }
//   }

//   validations() {
//     bool isSuccess = true;
//     List<Map<String, String>> items = [];
//     for (var objIn in campReadinessList) {
//       items.add({
//         "ItemId": objIn.itemId.toString(),
//         "ItemStatus": objIn.itemStatus.toString(),
//       });

//       if (objIn.itemStatus == 0 || objIn.itemStatus == null) {
//         isSuccess = false;
//         break;
//       }
//     }
//     String jsonString = campReadinessString(items);
//     if (isSuccess) {
//       submitData(jsonString);
//     } else {
//       ToastManager.toast("Please select all the items.");
//     }
//   }

//   submitData(String jsonString) {
//     ToastManager.showLoader();
//     Map<String, String> dict = {
//       "CampID": selectedCampID?.campId.toString() ?? "0",
//       "CampType": selectedCampType?.cAMPTYPE.toString() ?? "0",
//       "Createdby": empCode.toString(),
//       "TeamId": teamId,
//       "Type_CampReadinessForm": jsonString,
//     };
//     apiManager.insertCampReadinessFormDetailsAPI(
//       dict,
//       apiInsertCampReadinessFormDetailsCallBack,
//     );
//   }

//   void apiInsertCampReadinessFormDetailsCallBack(
//     CampReadinessFormSubmittResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();
//     if (success) {
//       ToastManager.showLoader();
//       getCampdetails();
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return KeyboardDismissOnTap(
//       child: Scaffold(
//         appBar: mAppBar(
//           scTitle: "Camp Readiness Form",
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
//                   decoration: BoxDecoration(
//                     color: Colors.transparent,
//                     // boxShadow: [
//                     //   BoxShadow(
//                     //     color: Colors.black.withValues(alpha: 0.15),
//                     //     blurRadius: 10,
//                     //   ),
//                     // ],
//                     // borderRadius: BorderRadius.circular(10),
//                   ),
//                   child: Column(
//                     mainAxisAlignment: MainAxisAlignment.start,
//                     children: [
//                       Container(
//                         width: MediaQuery.of(context).size.width,
//                         decoration: const BoxDecoration(
//                           color: Colors.white,
//                           borderRadius: BorderRadius.all(Radius.circular(10)),
//                         ),
//                         padding: const EdgeInsets.all(8),
//                         child: Column(
//                           children: [
//                             Row(
//                               mainAxisAlignment: MainAxisAlignment.start,
//                               crossAxisAlignment: CrossAxisAlignment.start,
//                               children: [
//                                 Expanded(
//                                   child: AppDateTextfield(
//                                     icon: icCalendarMonth,
//                                     titleHeaderString: "Camp Date*",
//                                     valueString: _selectedCampDate,
//                                     valueColor: Colors.grey,
//                                     onTap: () {},
//                                   ),
//                                 ),
//                                 const SizedBox(width: 8),
//                                 Expanded(
//                                   child: AppDropdownTextfield(
//                                     icon: icnTent,
//                                     titleHeaderString: "Camp Type*",
//                                     valueString:
//                                         selectedCampType?.campTypeDescription ??
//                                         "",
//                                     onTap: () {
//                                       chooseCampType();
//                                     },
//                                   ),
//                                 ),
//                               ],
//                             ),
//                             const SizedBox(height: 8),
//                             AppDropdownTextfield(
//                               icon: icHashIcon,
//                               titleHeaderString: "Camp ID*",
//                               valueString:
//                                   selectedCampID?.campId.toString() ?? "",
//                               onTap: () {
//                                 chooseCampID();
//                               },
//                             ),
//                             showTeam == true
//                                 ? const SizedBox(height: 8)
//                                 : Container(),
//                             showTeam == true
//                                 ? AppDropdownTextfield(
//                                   icon: icTeamIcon,
//                                   titleHeaderString: "Team Id*",
//                                   valueString: teamName,
//                                   onTap: () {},
//                                 )
//                                 : Container(),
//                           ],
//                         ),
//                       ),
//                       const SizedBox(height: 8),
//                       Expanded(
//                         child: ListView.builder(
//                           itemCount: campReadinessList.length,
//                           itemBuilder: (context, index) {
//                             CampReadinessFormOutput object =
//                                 campReadinessList[index];
//                             return CampReadinessFormRow(
//                               index: index,
//                               object: object,
//                               isFormSubmitted: isFormSubmitted,
//                             );
//                           },
//                         ),
//                       ),
//                       Container(
//                         width: MediaQuery.of(context).size.width,
//                         color: Colors.white,
//                         padding: const EdgeInsets.all(8),
//                         child:
//                             showSubmitButton == true
//                                 ? Column(
//                                   mainAxisAlignment: MainAxisAlignment.start,
//                                   crossAxisAlignment: CrossAxisAlignment.start,
//                                   children: [
//                                     SizedBox(
//                                       width: MediaQuery.of(context).size.width,
//                                       child: Row(
//                                         children: [
//                                           Expanded(
//                                             child: AppActiveButton(
//                                               buttontitle: "Cancel",
//                                               isCancel: true,
//                                               onTap: () {
//                                                 Navigator.pop(context);
//                                               },
//                                             ),
//                                           ),
//                                           const SizedBox(width: 12),
//                                           Expanded(
//                                             child: AppActiveButton(
//                                               buttontitle: "Submit",
//                                               onTap: () {
//                                                 validations();
//                                               },
//                                             ),
//                                           ),
//                                         ],
//                                       ),
//                                     ),
//                                   ],
//                                 )
//                                 : Text(
//                                   textAlign: TextAlign.center,
//                                   "Camp readiness form already submitted",
//                                   style: TextStyle(
//                                     color: Colors.green,
//                                     fontFamily: FontConstants.interFonts,
//                                     fontWeight: FontWeight.w600,
//                                     fontSize: responsiveFont(16),
//                                   ),
//                                 ),
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
