// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import '../../../Modules/DispatchGroup/DispatchGroup.dart';
import '../../../Modules/Json_Class/BeneficiaryDetailsforAssignTeamidDetailsResponse/BeneficiaryDetailsforAssignTeamidDetailsResponse.dart';
import '../../../Modules/Json_Class/ConfirmatoryTestsScreeningResponse/ConfirmatoryTestsScreeningResponse.dart';
import '../../../Modules/Json_Class/ConfirmatoryTestsScreeningTubeResponse/ConfirmatoryTestsScreeningTubeResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import 'PatientSampleCollectionHeaderView/PatientSampleCollectionHeaderView.dart';
import 'TestDetailsView/TestDetailsView.dart';
import 'TubeDetailsView/TubeDetailsView.dart';

class AssignTeamForCTSampleCollection extends StatefulWidget {
  AssignTeamForCTSampleCollection({
    super.key,
    required this.beneficiaryDetails,
  });

  BeneficiaryDetailsforAssignTeamidOutput? beneficiaryDetails;
  @override
  State<AssignTeamForCTSampleCollection> createState() =>
      _AssignTeamForCTSampleCollectionState();
}

class _AssignTeamForCTSampleCollectionState
    extends State<AssignTeamForCTSampleCollection> {
  int empCode = 0;
  ConfirmatoryTestsScreeningOutput? testDetailslist;
  List<ConfirmatoryTestsScreeningTubeOutput> tubesDetailslist = [];
  APIManager apiManager = APIManager();
  List<ConfirmatoryTestsScreeningOutput> list = [];

  DispatchGroup dispatchGroup = DispatchGroup();
  @override
  void initState() {
    super.initState();

    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    dispatchGroup.enter();
    dispatchGroup.enter();
    ToastManager.showLoader();
    getTestInfo();
    getTestInfoCount();

    dispatchGroup.notify(() {
      dispatchGroup.reset();
      debugPrint("All APIs Completed!");
      ToastManager.hideLoader();
    });
  }

  void getTestInfo() {
    Map<String, String> params = {
      "USERID": empCode.toString(),
      "DISTLGDCODE": "0",
      "AREA": "0",
      "Type": "9",
      "REDNO": widget.beneficiaryDetails?.regdno ?? "",
      "Regdid": widget.beneficiaryDetails?.regdid.toString() ?? "0",
      "FROMDATE": "2024/01/01",
      "TODATE": "2025/07/24",
    };

    apiManager.getTestInfoAPI(params, apiTestInfoCallBack);
  }

  void apiTestInfoCallBack(
    ConfirmatoryTestsScreeningResponse? response,
    String errorMessage,
    bool success,
  ) async {
    dispatchGroup.leave();
    if (success) {
      testDetailslist = response?.output?.first;
      list = response?.output ?? [];
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getTestInfoCount() {
    Map<String, String> params = {
      "USERID": empCode.toString(),
      "DISTLGDCODE": "0",
      "AREA": "0",
      "Type": "10",
      "REDNO": widget.beneficiaryDetails?.regdno ?? "",
      "Regdid": widget.beneficiaryDetails?.regdid.toString() ?? "0",
      "FROMDATE": "2024/01/01",
      "TODATE": "2025/07/24",
    };

    apiManager.getTestInfoCount(params, apiTestInfoCountCallBack);
  }

  void apiTestInfoCountCallBack(
    ConfirmatoryTestsScreeningTubeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    dispatchGroup.leave();
    if (success) {
      tubesDetailslist = response?.output ?? [];
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Sample Collection",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Stack(
            children: [

              Positioned(
                top: 8,
                bottom: 8,
                left: 8,
                right: 8,
                child: SingleChildScrollView(
                  child: Column(
                    children: [
                      PatientSampleCollectionHeaderView(
                        fullName:
                            widget.beneficiaryDetails?.beneficiaryName ?? "",
                        gender: testDetailslist?.gender ?? "",
                        age: testDetailslist?.patAge.toString() ?? "",
                      ),
                      const SizedBox(height: 10),
                      TestDetailsView(list: list),
                      const SizedBox(height: 10),
                      TubeDetailsView(list: tubesDetailslist),
                      // const SizedBox(height: 20),
                      // Padding(
                      //   padding: const EdgeInsets.fromLTRB(0, 4, 0, 20),
                      //   child: Container(
                      //     width: MediaQuery.of(context).size.width,
                      //     color: Colors.transparent,
                      //     child: Row(
                      //       mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      //       crossAxisAlignment: CrossAxisAlignment.center,
                      //       children: [
                      //         Expanded(
                      //           child: AppActiveButton(
                      //             buttontitle: "Cancel",
                      //             isCancel: true,
                      //             onTap: () {
                      //               Navigator.pop(context);
                      //             },
                      //           ),
                      //         ),
                      //         const SizedBox(width: 16),
                      //         Expanded(
                      //           child: AppActiveButton(
                      //             buttontitle: "Save",
                      //             onTap: () {
                      //               Navigator.pop(context);
                      //             },
                      //           ),
                      //         ),
                      //       ],
                      //     ),
                      //   ),
                      // ),
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

// class AssignTeamForCTSampleCollection extends StatefulWidget {
//   AssignTeamForCTSampleCollection({
//     super.key,
//     required this.beneficiaryDetails,
//   });

//   BeneficiaryDetailsforAssignTeamidOutput? beneficiaryDetails;
//   @override
//   State<AssignTeamForCTSampleCollection> createState() =>
//       _AssignTeamForCTSampleCollectionState();
// }

// class _AssignTeamForCTSampleCollectionState
//     extends State<AssignTeamForCTSampleCollection> {
//   int empCode = 0;
//   ConfirmatoryTestsScreeningOutput? testDetailslist;
//   List<ConfirmatoryTestsScreeningTubeOutput> tubesDetailslist = [];
//   APIManager apiManager = APIManager();
//   List<ConfirmatoryTestsScreeningOutput> list = [];
//   @override
//   void initState() {
//     super.initState();

//     empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

//     getTestInfo();
//     getTestInfoCount();
//   }

//   getTestInfo() {
//     Map<String, String> params = {
//       "USERID": empCode.toString(),
//       "DISTLGDCODE": "0",
//       "AREA": "0",
//       "Type": "9",
//       "REDNO": widget.beneficiaryDetails?.regdno ?? "",
//       "Regdid": widget.beneficiaryDetails?.regdid.toString() ?? "0",
//       "FROMDATE": "2024/01/01",
//       "TODATE": "2025/07/24",
//     };

//     apiManager.getTestInfoAPI(params, apiTestInfoCallBack);
//   }

//   void apiTestInfoCallBack(
//     ConfirmatoryTestsScreeningResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       testDetailslist = response?.output?.first;
//       list = response?.output ?? [];
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   getTestInfoCount() {
//     Map<String, String> params = {
//       "USERID": empCode.toString(),
//       "DISTLGDCODE": "0",
//       "AREA": "0",
//       "Type": "10",
//       "REDNO": widget.beneficiaryDetails?.regdno ?? "",
//       "Regdid": widget.beneficiaryDetails?.regdid.toString() ?? "0",
//       "FROMDATE": "2024/01/01",
//       "TODATE": "2025/07/24",
//     };

//     apiManager.getTestInfoCount(params, apiTestInfoCountCallBack);
//   }

//   void apiTestInfoCountCallBack(
//     ConfirmatoryTestsScreeningTubeResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     if (success) {
//       tubesDetailslist = response?.output ?? [];
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return Scaffold(
//       appBar: mAppBar(
//         scTitle: "Sample Collection",
//         leadingIcon: iconBackArrow,
//         onLeadingIconClick: () {
//           Navigator.pop(context);
//         },
//       ),
//       body: SizedBox(
//         height: SizeConfig.screenHeight,
//         width: SizeConfig.screenWidth,
//         child: Stack(
//           children: [
//             Positioned(
//               top: 74,
//               child: Image.asset(
//                 fit: BoxFit.fill,
//                 rect4,
//                 width: SizeConfig.screenWidth,
//                 height: responsiveHeight(300.37),
//               ),
//             ),
//             Positioned(
//               top: 53,
//               child: Image.asset(
//                 fit: BoxFit.fill,
//                 rect3,
//                 width: SizeConfig.screenWidth,
//                 height: responsiveHeight(300.37),
//               ),
//             ),
//             Positioned(
//               top: 30,
//               child: Image.asset(
//                 fit: BoxFit.fill,
//                 rect2,
//                 width: SizeConfig.screenWidth,
//                 height: responsiveHeight(300.37),
//               ),
//             ),
//             Image.asset(
//               fit: BoxFit.fill,
//               rect1,
//               width: SizeConfig.screenWidth,
//               height: responsiveHeight(300.37),
//             ),
//             Positioned(
//               top: 8,
//               bottom: 10,
//               left: 10,
//               right: 10,
//               child: SingleChildScrollView(
//                 child: Column(
//                   children: [
//                     PatientSampleCollectionHeaderView(
//                       fullName:
//                           widget.beneficiaryDetails?.beneficiaryName ?? "",
//                       gender: testDetailslist?.gender ?? "",
//                       age: testDetailslist?.patAge.toString() ?? "",
//                     ),
//                     const SizedBox(height: 10),
//                     TestDetailsView(list: list),
//                     const SizedBox(height: 20),
//                     TubeDetailsView(list: tubesDetailslist),
//                     // const SizedBox(height: 20),
//                     // Padding(
//                     //   padding: const EdgeInsets.fromLTRB(0, 4, 0, 20),
//                     //   child: Container(
//                     //     width: MediaQuery.of(context).size.width,
//                     //     color: Colors.transparent,
//                     //     child: Row(
//                     //       mainAxisAlignment: MainAxisAlignment.spaceBetween,
//                     //       crossAxisAlignment: CrossAxisAlignment.center,
//                     //       children: [
//                     //         Expanded(
//                     //           child: AppActiveButton(
//                     //             buttontitle: "Cancel",
//                     //             isCancel: true,
//                     //             onTap: () {
//                     //               Navigator.pop(context);
//                     //             },
//                     //           ),
//                     //         ),
//                     //         const SizedBox(width: 16),
//                     //         Expanded(
//                     //           child: AppActiveButton(
//                     //             buttontitle: "Save",
//                     //             onTap: () {
//                     //               Navigator.pop(context);
//                     //             },
//                     //           ),
//                     //         ),
//                     //       ],
//                     //     ),
//                     //   ),
//                     // ),
//                   ],
//                 ),
//               ),
//             ),
//           ],
//         ),
//       ),
//     );
//   }
// }
