// ignore_for_file: file_names, avoid_print, must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Views/AppointmentsConfirmedFilterView/AppointmentsConfirmedFilterView.dart';

import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/AppoinmentExpectedBeneficiariesResponse/AppoinmentExpectedBeneficiariesResponse.dart';
import '../../../Modules/Json_Class/AppointmentStatusResponse/AppointmentStatusResponse.dart';
import '../../../Modules/Json_Class/TeamCCResponse/TeamCCResponse.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/fonts.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../../../Views/AppointmentsConfirmedTeamView/AppointmentsConfirmedTeamView.dart';
import '../AppointmentsConfirmedBeneficiaryDetailsScreen/AppointmentsConfirmedBeneficiaryDetailsScreen.dart';
import 'AppointmentsConfirmedRow/AppointmentsConfirmedRow.dart';
import 'package:s2toperational/Screens/patient_registration/controller/d2d_select_camp_controller.dart';
import 'package:s2toperational/Screens/patient_registration/screen/d2d_select_camp_screen.dart';

class AppointmentsConfirmedListScreen extends StatefulWidget {
  AppointmentsConfirmedListScreen({super.key, required this.dashboardType});

  DashboardMenu? dashboardType;

  @override
  State<AppointmentsConfirmedListScreen> createState() =>
      _AppointmentsConfirmedListScreenState();
}

class _AppointmentsConfirmedListScreenState
    extends State<AppointmentsConfirmedListScreen> {
  TextEditingController searchTextField = TextEditingController();

  String selectedCampDate = "";
  AppointmentStatusOutput? selectedAppointmentStatus;

  bool showTeamDropDown = true;
  TeamCCOutput? selectedTeam;
  APIManager apiManager = APIManager();
  int empCode = 0;
  int callStatusID = 0;
  int teamId = 0;
  int dESGID = 0;

  List<AppoinmentExpectedBeneficiariesOutput> listOfSearchBeneficiaries = [];
  List<AppoinmentExpectedBeneficiariesOutput> listOfBeneficiaries = [];

  @override
  void initState() {
    super.initState();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    selectedAppointmentStatus = AppointmentStatusOutput(
      assignStatusID: 2,
      appointmentStatus: "Confirmed",
    );
    selectedCampDate = FormatterManager.formatDateToString(DateTime.now());
    callAPI();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Appointments Confirmed List',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
          showActions: true,
          actions: [
            Padding(
              padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
              child: GestureDetector(
                onTap: () {
                  showAppointmentFilterBottomSheet();
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
        floatingActionButton:
            widget.dashboardType == DashboardMenu.PatientRegistration
                ? FloatingActionButton.extended(
                  backgroundColor: kPrimaryColor,
                  icon: const Icon(
                    Icons.person_add_rounded,
                    color: Colors.white,
                  ),
                  label: CommonText(
                    text: 'Register Patient',
                    fontSize: 13.sp,
                    fontWeight: FontWeight.w600,
                    textColor: Colors.white,
                    textAlign: TextAlign.center,
                  ),
                  onPressed: () {
                    Get.delete<D2DSelectCampController>(force: true);
                    Get.put(D2DSelectCampController());
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => const D2DSelectCampScreen(),
                      ),
                    );
                  },
                )
                : null,
        body: AnnotatedRegion(
          value: const SystemUiOverlayStyle(
            statusBarColor: kPrimaryColor,
            statusBarBrightness: Brightness.light,
            statusBarIconBrightness: Brightness.light,
          ),

          child: Container(
            color: Colors.white,
            height: SizeConfig.screenHeight,
            width: SizeConfig.screenWidth,
            child: Column(
              children: [
                Container(
                  width: SizeConfig.screenWidth,
                  height: 50,
                  decoration: BoxDecoration(
                    color: Colors.white,
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withValues(alpha: 0.15),
                        blurRadius: 4,
                      ),
                    ],
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: Row(
                    children: [
                      Expanded(
                        child: TextField(
                          textAlign: TextAlign.left,
                          style: TextStyle(
                            color: kBlackColor,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(14),
                          ),
                          controller: searchTextField,
                          decoration: InputDecoration(
                            contentPadding: EdgeInsets.only(left: 6),
                            border: InputBorder.none,
                            focusedBorder: InputBorder.none,
                            hintText:
                                "Search Beneficiary No/Worker Name/MobileNo/Area/Pincode",
                          ),
                          onChanged: (text) {
                            print('Entered text: $text');
                            listOfSearchBeneficiaries = searchByDescEn(text);
                            setState(() {});
                          },
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.fromLTRB(0, 0, 4, 0),
                        child: SizedBox(
                          width: 20,
                          height: 20,
                          child: Image.asset(icSearch),
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 10),
                Align(
                  alignment: Alignment.centerLeft,
                  child: Text(
                    "Total Beneficaires : ${listOfSearchBeneficiaries.length}",
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.bold,
                      fontSize: responsiveFont(16),
                    ),
                    textAlign: TextAlign.start,
                  ),
                ),
                const SizedBox(height: 4),

                Expanded(
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(0, 6, 0, 10),
                    child: ListView.builder(
                      itemCount: listOfSearchBeneficiaries.length,
                      itemBuilder: (context, index) {
                        AppoinmentExpectedBeneficiariesOutput obj =
                            listOfSearchBeneficiaries[index];
                        return GestureDetector(
                          onTap: () {
                            final canNavigate =
                                dESGID == 35 ||
                                widget.dashboardType ==
                                    DashboardMenu.PatientRegistration;
                            if (canNavigate) {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder:
                                      (context) =>
                                          AppointmentsConfirmedBeneficiaryDetailsScreen(
                                            selectedBeneficiary: obj,
                                            isPatientRegistration:
                                                widget.dashboardType ==
                                                DashboardMenu
                                                    .PatientRegistration,
                                          ),
                                ),
                              );
                            }
                          },
                          child: AppointmentsConfirmedRow(obj: obj),
                        );
                      },
                    ),
                  ),
                ),
              ],
            ).paddingSymmetric(vertical: 6, horizontal: 12),
          ),
        ),
      ),
    );
  }

  void callAPI() {
    ToastManager.showLoader();
    if (DataProvider().getRegularCamp()) {
      getBeneficiaryList();
      showTeamDropDown = false;
    } else {
      if (widget.dashboardType == DashboardMenu.PatientRegistration) {
        getBeneficiaryList();
        showTeamDropDown = true;
      } else {
        showTeamDropDown = true;
        getBeneficiaryListForMasJas();
      }
    }
  }

  void getBeneficiaryListForMasJas() {
    Map<String, String> params = {
      "UserID": empCode.toString(),
      "AppoinmentDate": selectedCampDate,
      "AssignStatusID":
          selectedAppointmentStatus?.assignStatusID?.toString() ?? "",
      "TeamId": selectedTeam?.teamid.toString() ?? "0",
    };
    apiManager.getAppointmentBeneficiariesMASListAPI(
      params,
      apiBeneficiaryListForMasJasCallBack,
    );
  }

  void apiBeneficiaryListForMasJasCallBack(
    AppoinmentExpectedBeneficiariesResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      listOfBeneficiaries = response?.output ?? [];
      listOfSearchBeneficiaries = listOfBeneficiaries;
    } else {
      listOfBeneficiaries = [];
      listOfSearchBeneficiaries = listOfBeneficiaries;
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getBeneficiaryList() {
    Map<String, String> params = {
      "UserID": empCode.toString(),
      "AppoinmentDate": selectedCampDate,
      "AssignStatusID":
          selectedAppointmentStatus?.assignStatusID?.toString() ?? "",
    };
    apiManager.getAppointmentBeneficiariesListAPI(
      params,
      apiBeneficiaryListForMasJasCallBack,
    );
  }

  void fetchTeamData() {
    ToastManager.showLoader();
    Map<String, String> param = {"UserID": empCode.toString()};
    apiManager.getTeamListCCAPI(param, apiTeamListCCCallBack);
  }

  void apiTeamListCCCallBack(
    TeamCCResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      showAppointmentTeamBottomSheet(response?.output ?? []);
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void showAppointmentFilterBottomSheet() {
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
          height: MediaQuery.of(context).size.width * 1.08,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: AppointmentsConfirmedFilterView(
            selectedAppointmentStatus: selectedAppointmentStatus,
            selectedCampDate: selectedCampDate,
            selectedTeam: selectedTeam,
            showTeam: widget.dashboardType != DashboardMenu.PatientRegistration,
            onTapApply: (p0, p1, p2) {
              selectedAppointmentStatus = p0;
              selectedCampDate = p1;
              selectedTeam = p2;
              callAPI();
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  void showAppointmentTeamBottomSheet(List<TeamCCOutput> list) {
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
          child: AppointmentsConfirmedTeamView(
            list: list,
            onTapTeam: (p0) {
              selectedTeam = p0;
              print(p0.member1);
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  List<AppoinmentExpectedBeneficiariesOutput> searchByDescEn(String query) {
    final lowerQuery = query.toLowerCase();

    return listOfBeneficiaries.where((item) {
      final name = item.beneficiaryName?.toString().toLowerCase() ?? '';
      final benNo = item.beneficiaryNo?.toString().toLowerCase() ?? '';
      final mobNo = item.mobile?.toString().toLowerCase() ?? '';
      final area = item.area?.toString().toLowerCase() ?? '';
      final pin = item.pincode?.toLowerCase() ?? '';

      return name.contains(lowerQuery) ||
          pin.contains(lowerQuery) ||
          benNo.contains(lowerQuery) ||
          mobNo.contains(lowerQuery) ||
          area.contains(lowerQuery);
    }).toList();
  }
}

// class AppointmentsConfirmedListScreen extends StatefulWidget {
//   AppointmentsConfirmedListScreen({super.key, required this.dashboardType});

//   DashboardMenu? dashboardType;
//   @override
//   State<AppointmentsConfirmedListScreen> createState() =>
//       _AppointmentsConfirmedListScreenState();
// }

// class _AppointmentsConfirmedListScreenState
//     extends State<AppointmentsConfirmedListScreen> {
//   TextEditingController searchTextField = TextEditingController();

//   String selectedCampDate = "";
//   AppointmentStatusOutput? selectedAppointmentStatus;

//   bool showTeamDropDown = true;
//   TeamCCOutput? selectedTeam;
//   APIManager apiManager = APIManager();
//   int empCode = 0;
//   int callStatusID = 0;
//   int teamId = 0;
//   int dESGID = 0;

//   List<AppoinmentExpectedBeneficiariesOutput>? listOfSearchBeneficiaries = [];
//   List<AppoinmentExpectedBeneficiariesOutput>? listOfBeneficiaries = [];
//   @override
//   void initState() {
//     super.initState();
//     dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
//     empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
//     selectedAppointmentStatus = AppointmentStatusOutput(
//       assignStatusID: 2,
//       appointmentStatus: "Confirmed",
//     );
//     selectedCampDate = FormatterManager.formatDateToString(DateTime.now());
//     callAPI();
//   }

//   callAPI() {
//     ToastManager.showLoader();
//     if (DataProvider().getRegularCamp()) {
//       getBeneficiaryList();
//       showTeamDropDown = true;
//     } else {
//       if (widget.dashboardType == DashboardMenu.PatientRegistration) {
//         getBeneficiaryList();
//         showTeamDropDown = true;
//       } else {
//         showTeamDropDown = false;
//         getBeneficiaryListForMasJas();
//       }
//     }
//   }

//   getBeneficiaryListForMasJas() {
//     Map<String, String> params = {
//       "UserID": empCode.toString(),
//       "AppoinmentDate": selectedCampDate,
//       "AssignStatusID":
//           selectedAppointmentStatus?.assignStatusID?.toString() ?? "",
//       "TeamId": selectedTeam?.teamid.toString() ?? "0",
//     };
//     apiManager.getAppointmentBeneficiariesMASListAPI(
//       params,
//       apiBeneficiaryListForMasJasCallBack,
//     );
//   }

//   void apiBeneficiaryListForMasJasCallBack(
//     AppoinmentExpectedBeneficiariesResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       listOfBeneficiaries = response?.output ?? [];
//       listOfSearchBeneficiaries = listOfBeneficiaries;
//     } else {
//       listOfBeneficiaries = [];
//       listOfSearchBeneficiaries = listOfBeneficiaries;
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   getBeneficiaryList() {
//     Map<String, String> params = {
//       "UserID": empCode.toString(),
//       "AppoinmentDate": selectedCampDate,
//       "AssignStatusID":
//           selectedAppointmentStatus?.assignStatusID?.toString() ?? "",
//     };
//     apiManager.getAppointmentBeneficiariesListAPI(
//       params,
//       apiBeneficiaryListForMasJasCallBack,
//     );
//   }

//   fetchTeamData() {
//     ToastManager.showLoader();
//     Map<String, String> param = {"UserID": empCode.toString()};
//     apiManager.getTeamListCCAPI(param, apiTeamListCCCallBack);
//   }

//   void apiTeamListCCCallBack(
//     TeamCCResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       showAppointmentTeamBottomSheet(response?.output ?? []);
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//   }

//   void showAppointmentFilterBottomSheet() {
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
//           height: MediaQuery.of(context).size.width * 1.08,
//           decoration: const BoxDecoration(
//             color: Colors.white,
//             borderRadius: BorderRadius.only(
//               topLeft: Radius.circular(20),
//               topRight: Radius.circular(20),
//             ),
//           ),
//           child: AppointmentsConfirmedFilterView(
//             selectedAppointmentStatus: selectedAppointmentStatus,
//             selectedCampDate: selectedCampDate,
//             selectedTeam: selectedTeam,
//             onTapApply: (p0, p1, p2) {
//               selectedAppointmentStatus = p0;
//               selectedCampDate = p1;
//               selectedTeam = p2;
//               callAPI();
//             },
//           ),
//         );
//       },
//     ).whenComplete(() {
//       setState(() {});
//     });
//   }

//   void showAppointmentTeamBottomSheet(List<TeamCCOutput> list) {
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
//           height: MediaQuery.of(context).size.width * 1.38,
//           decoration: const BoxDecoration(
//             color: Colors.white,
//             borderRadius: BorderRadius.only(
//               topLeft: Radius.circular(20),
//               topRight: Radius.circular(20),
//             ),
//           ),
//           child: AppointmentsConfirmedTeamView(
//             list: list,
//             onTapTeam: (p0) {
//               selectedTeam = p0;
//               print(p0.member1);
//             },
//           ),
//         );
//       },
//     ).whenComplete(() {
//       setState(() {});
//     });
//   }

//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return KeyboardDismissOnTap(
//       child: Scaffold(
//         appBar: mAppBar(
//           scTitle: 'Appointments Confirmed List',
//           leadingIcon: iconBackArrow,
//           onLeadingIconClick: () {
//             Navigator.pop(context);
//           },
//           showActions: true,
//           actions: [
//             Padding(
//               padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
//               child: GestureDetector(
//                 onTap: () {
//                   showAppointmentFilterBottomSheet();
//                 },
//                 child: SizedBox(
//                   width: 20,
//                   height: 20,
//                   child: Image.asset(icFilter),
//                 ),
//               ),
//             ),
//           ],
//         ),
//         body: AnnotatedRegion(
//           value: const SystemUiOverlayStyle(
//             statusBarColor: kPrimaryColor,
//             statusBarBrightness: Brightness.light,
//             statusBarIconBrightness: Brightness.light,
//           ),

//           child: Container(
//             color: Colors.white,
//             height: SizeConfig.screenHeight,
//             width: SizeConfig.screenWidth,
//             child: Stack(
//               children: [
//                 Positioned(
//                   top: 74,
//                   child: Image.asset(
//                     fit: BoxFit.fill,
//                     rect4,
//                     width: SizeConfig.screenWidth,
//                     height: responsiveHeight(300.37),
//                   ),
//                 ),
//                 Positioned(
//                   top: 53,
//                   child: Image.asset(
//                     fit: BoxFit.fill,
//                     rect3,
//                     width: SizeConfig.screenWidth,
//                     height: responsiveHeight(300.37),
//                   ),
//                 ),
//                 Positioned(
//                   top: 30,
//                   child: Image.asset(
//                     fit: BoxFit.fill,
//                     rect2,
//                     width: SizeConfig.screenWidth,
//                     height: responsiveHeight(300.37),
//                   ),
//                 ),
//                 Image.asset(
//                   fit: BoxFit.fill,
//                   rect1,
//                   width: SizeConfig.screenWidth,
//                   height: responsiveHeight(300.37),
//                 ),
//                 Positioned(
//                   top: 0,
//                   right: 10,
//                   left: 10,
//                   bottom: 0,
//                   child: Column(
//                     children: [
//                       Container(
//                         width: SizeConfig.screenWidth,
//                         height: 60,
//                         decoration: BoxDecoration(
//                           color: Colors.white,
//                           borderRadius: BorderRadius.circular(100),
//                         ),
//                         child: Row(
//                           children: [
//                             Expanded(
//                               child: Padding(
//                                 padding: EdgeInsets.fromLTRB(16, 2, 8, 2),
//                                 child: TextField(
//                                   textAlign: TextAlign.left,
//                                   style: TextStyle(
//                                     color: kBlackColor,
//                                     fontFamily: FontConstants.interFonts,
//                                     fontWeight: FontWeight.w500,
//                                     fontSize: responsiveFont(16),
//                                   ),
//                                   controller: searchTextField,
//                                   decoration: InputDecoration(
//                                     border: InputBorder.none,
//                                     focusedBorder: InputBorder.none,
//                                     hintText:
//                                         "Search Beneficiary No/Worker Name/MobileNo/Area/Pincode",
//                                   ),
//                                   onChanged: (text) {
//                                     print('Entered text: $text');
//                                   },
//                                 ),
//                               ),
//                             ),
//                             Padding(
//                               padding: const EdgeInsets.fromLTRB(0, 0, 14, 0),
//                               child: SizedBox(
//                                 width: 30,
//                                 height: 30,
//                                 child: Image.asset(icSearch),
//                               ),
//                             ),
//                           ],
//                         ),
//                       ),
//                       showTeamDropDown == true
//                           ? const SizedBox(height: 8)
//                           : Container(),
//                       showTeamDropDown == true
//                           ? AppDropdownTextfield(
//                             icon: icUsersGroup,
//                             titleHeaderString: "Team",
//                             valueString:
//                                 selectedAppointmentStatus?.appointmentStatus ??
//                                 "",
//                             onTap: () {
//                               fetchTeamData();
//                             },
//                           )
//                           : Container(),
//                       Expanded(
//                         child: Padding(
//                           padding: const EdgeInsets.fromLTRB(0, 10, 0, 10),
//                           child: ListView.builder(
//                             itemCount: listOfSearchBeneficiaries?.length ?? 0,
//                             itemBuilder: (context, index) {
//                               AppoinmentExpectedBeneficiariesOutput obj =
//                                   listOfSearchBeneficiaries![index];
//                               return GestureDetector(
//                                 onTap: () {
//                                   if (dESGID == 35) {
//                                     Navigator.push(
//                                       context,
//                                       MaterialPageRoute(
//                                         builder:
//                                             (context) =>
//                                                 AppointmentsConfirmedBeneficiaryDetailsScreen(
//                                                   selectedBeneficiary: obj,
//                                                 ),
//                                       ),
//                                     );
//                                   }
//                                 },
//                                 child: AppointmentsConfirmedRow(obj: obj),
//                               );
//                             },
//                           ),
//                         ),
//                       ),
//                     ],
//                   ),
//                 ),
//               ],
//             ),
//           ),
//         ),
//       ),
//     );
//   }
// }
