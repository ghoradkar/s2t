// ignore_for_file: file_names, avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/AdvadetailsNewVersionV2Response/AdvadetailsNewVersionV2Response.dart';
import '../../../Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../AddBillSubmissionScreen/AddBillSubmissionScreen.dart';
import '../RequestBillDetailsScreen/RequestBillDetailsScreen.dart';
import 'BillSubmissionRow/BillSubmissionRow.dart';
import '../../../../../Modules/constants/fonts.dart';

class BillSubmissionScreen extends StatefulWidget {
  const BillSubmissionScreen({super.key});

  @override
  State<BillSubmissionScreen> createState() => _BillSubmissionScreenState();
}

class _BillSubmissionScreenState extends State<BillSubmissionScreen> {
  List<String> lsit = ["Sandeep"];

  LoginOutput? userLoginDetails;

  String fromDate = "";
  String toDate = "";

  DateTime? selectedFromDate;

  APIManager apiManager = APIManager();

  List<AdvadetailsNewOutput> campExprenessList = [];

  Widget bottomDataLayout() {
    if (lsit.isEmpty) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SizedBox(width: 120, height: 120, child: Image.asset(icMoneyIcon)),
            SizedBox(height: 23),
            Text(
              "No Advance Requested.",
              style: TextStyle(
                color: Colors.black,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                fontSize: responsiveFont(16),
              ),
            ),
          ],
        ),
      );
    }

    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 10, 0, 0),
      child: ListView.builder(
        itemCount: campExprenessList.length,
        itemBuilder: (context, index) {
          AdvadetailsNewOutput obj = campExprenessList[index];
          return BillSubmissionRow(
            object: obj,
            onEyesIconTap: (object) {
              int dESGID = userLoginDetails?.dESGID ?? 0;

              int empCode = userLoginDetails?.empCode ?? 0;

              if (dESGID != 102) {
                if (obj.advRaisedbyUserid != null &&
                    obj.advRaisedbyUserid != empCode) {
                  ToastManager.showAlertDialog(
                    context,
                    "This advance not requested by you.",(){
                    Navigator.pop(context);

                  }
                  );
                  return;
                }
              }
              if (obj.actualExpenseStatus != null &&
                  obj.actualExpenseStatus?.toLowerCase() ==
                      "Approved".toLowerCase()) {
                ToastManager.showAlertDialog(
                  context,
                  "This bill already approved",(){
                  Navigator.pop(context);

                }
                );
              } else {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder:
                        (context) =>
                            AddBillSubmissionScreen(advanceDetails: obj),
                  ),
                );
              }
            },
            onViewDetailsTap: (object) {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder:
                      (context) =>
                          RequestBillDetailsScreen(campid: obj.campid ?? 0),
                ),
              );
            },
          );
        },
      ),
    );
  }

  Future<void> _selectFromDate(BuildContext context) async {
    DateTime now = DateTime.now();
    DateTime hundredYearsAgo = DateTime(now.year - 100, now.month, 1);
    DateTime lastDate = DateTime(now.year, now.month, now.day);

    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: hundredYearsAgo,
      lastDate: lastDate,
    );

    if (picked != null) {
      setState(() {
        toDate = "";
        selectedFromDate = picked;
        fromDate = FormatterManager.formatDateToString(picked);
      });
    }
  }

  Future<void> _selectToDate(BuildContext context) async {
    if (selectedFromDate == null) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Please select From Date first')));
      return;
    }

    DateTime maxAllowed = DateTime(
      selectedFromDate!.year + 100,
      selectedFromDate!.month,
      selectedFromDate!.day,
    );
    DateTime lastDate = maxAllowed;
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: selectedFromDate!,
      firstDate: selectedFromDate!,
      lastDate: lastDate,
    );

    if (picked != null) {
      setState(() {
        toDate = FormatterManager.formatDateToString(picked);
        getAdvanceDetails();
      });
    }
  }

  @override
  void initState() {
    super.initState();

    userLoginDetails = DataProvider().getParsedUserData()?.output?.first;
    fromDate = FormatterManager.formatDateToString(DateTime.now());
    toDate = FormatterManager.formatDateToString(DateTime.now());
    setState(() {});
    getAdvanceDetails();
  }

  void getAdvanceDetails() {
    ToastManager.showLoader();
    Map<String, String> dict = {
      "FromReqDate": fromDate,
      "ToReqDate": toDate,
      "distlgdcode": userLoginDetails?.dISTLGDCODE?.toString() ?? "0",
      "USERID": userLoginDetails?.empCode?.toString() ?? "0",
    };
    print(dict);
    apiManager.getAdvadetailsNewVersionV2API(
      dict,
      apiAdvadetailsNewVersionV2Back,
    );
  }

  void apiAdvadetailsNewVersionV2Back(
    AdvadetailsNewVersionV2Response? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      campExprenessList = response?.output ?? [];
    } else {
      campExprenessList = [];
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
          scTitle: "Bill Submission",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: AnnotatedRegion(
          value: SystemUiOverlayStyle(
            statusBarColor: kPrimaryColor,
            statusBarBrightness: Brightness.dark,
            statusBarIconBrightness: Brightness.light,
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                width: MediaQuery.of(context).size.width,
                decoration: const BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.all(Radius.circular(10)),
                ),
                padding: const EdgeInsets.all(10),
                child: Column(
                  children: [
                    AppTextField(
                      controller: TextEditingController(
                        text: userLoginDetails?.district,
                      ),
                      readOnly: true,
                      // onTap: () {
                      //   _selectToDate(context);
                      // },
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
                    ),
                    // AppDropdownTextfield(
                    //   icon: icMapPin,
                    //   titleHeaderString: "District",
                    //   valueString: userLoginDetails?.district ?? "",
                    //   isDisabled: true,
                    //   onTap: () {},
                    // ),
                    const SizedBox(height: 8),
                    Container(
                      width: MediaQuery.of(context).size.width,
                      color: Colors.transparent,
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Expanded(
                            child: AppTextField(
                              controller: TextEditingController(text: fromDate),
                              readOnly: true,
                              onTap: () {
                                _selectFromDate(context);
                              },
                              hint: 'From Date',
                              label: CommonText(
                                text: 'From Date',
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
                            //   titleHeaderString: "From Date",
                            //   valueString: fromDate,
                            //   onTap: () {
                            //     _selectFromDate(context);
                            //   },
                            // ),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: AppTextField(
                              controller: TextEditingController(text: toDate),
                              readOnly: true,
                              onTap: () {
                                _selectToDate(context);
                              },
                              hint: 'To Date',
                              label: CommonText(
                                text: 'To Date',
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
                            //   titleHeaderString: "To Date",
                            //   valueString: toDate,
                            //   onTap: () {
                            //     _selectToDate(context);
                            //   },
                            // ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),

              Expanded(
                child: Container(
                  color: Colors.transparent,
                  child: bottomDataLayout(),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

// class BillSubmissionScreen extends StatefulWidget {
//   const BillSubmissionScreen({super.key});

//   @override
//   State<BillSubmissionScreen> createState() => _BillSubmissionScreenState();
// }

// class _BillSubmissionScreenState extends State<BillSubmissionScreen> {
//   List<String> lsit = ["Sandeep"];

//   LoginOutput? userLoginDetails;

//   String fromDate = "";
//   String toDate = "";

//   DateTime? selectedFromDate;

//   APIManager apiManager = APIManager();

//   List<AdvadetailsNewOutput> campExprenessList = [];

//   Widget bottomDataLayout() {
//     if (lsit.isEmpty) {
//       return Center(
//         child: Column(
//           mainAxisAlignment: MainAxisAlignment.center,
//           crossAxisAlignment: CrossAxisAlignment.center,
//           children: [
//             SizedBox(width: 120, height: 120, child: Image.asset(icMoneyIcon)),
//             SizedBox(height: 23),
//             Text(
//               "No Advance Requested.",
//               style: TextStyle(
//                 color: Colors.black,
//                 fontFamily: FontConstants.interFonts,
//                 fontWeight: FontWeight.w600,
//                 fontSize: responsiveFont(14),
//               ),
//             ),
//           ],
//         ),
//       );
//     }

//     return Padding(
//       padding: const EdgeInsets.fromLTRB(0, 10, 0, 0),
//       child: ListView.builder(
//         itemCount: campExprenessList.length,
//         itemBuilder: (context, index) {
//           AdvadetailsNewOutput obj = campExprenessList[index];
//           return BillSubmissionRow(
//             object: obj,
//             onEyesIconTap: (object) {
//               int dESGID = userLoginDetails?.dESGID ?? 0;

//               int empCode = userLoginDetails?.empCode ?? 0;

//               if (dESGID != 102) {
//                 if (obj.advRaisedbyUserid != null &&
//                     obj.advRaisedbyUserid != empCode) {
//                   ToastManager.showAlertDialog(
//                     context,
//                     "Alert",
//                     "This advance not requested by you.",
//                   );
//                   return;
//                 }
//               }
//               if (obj.actualExpenseStatus != null &&
//                   obj.actualExpenseStatus?.toLowerCase() ==
//                       "Approved".toLowerCase()) {
//                 ToastManager.showAlertDialog(
//                   context,
//                   "Alert",
//                   "This bill already approved",
//                 );
//               } else {
//                 Navigator.push(
//                   context,
//                   MaterialPageRoute(
//                     builder:
//                         (context) =>
//                             AddBillSubmissionScreen(advanceDetails: obj),
//                   ),
//                 );
//               }
//             },
//             onViewDetailsTap: (object) {
//               Navigator.push(
//                 context,
//                 MaterialPageRoute(
//                   builder:
//                       (context) =>
//                           RequestBillDetailsScreen(campid: obj.campid ?? 0),
//                 ),
//               );
//             },
//           );
//         },
//       ),
//     );
//   }

//   Future<void> _selectFromDate(BuildContext context) async {
//     DateTime now = DateTime.now();
//     DateTime hundredYearsAgo = DateTime(now.year - 100, now.month, 1);
//     DateTime lastDate = DateTime(now.year, now.month, now.day);

//     final DateTime? picked = await showDatePicker(
//       context: context,
//       initialDate: now,
//       firstDate: hundredYearsAgo,
//       lastDate: lastDate,
//     );

//     if (picked != null) {
//       setState(() {
//         toDate = "";
//         selectedFromDate = picked;
//         fromDate = FormatterManager.formatDateToString(picked);
//       });
//     }
//   }

//   Future<void> _selectToDate(BuildContext context) async {
//     if (selectedFromDate == null) {
//       ScaffoldMessenger.of(
//         context,
//       ).showSnackBar(SnackBar(content: Text('Please select From Date first')));
//       return;
//     }

//     DateTime maxAllowed = DateTime(
//       selectedFromDate!.year + 100,
//       selectedFromDate!.month,
//       selectedFromDate!.day,
//     );
//     DateTime lastDate = maxAllowed;
//     final DateTime? picked = await showDatePicker(
//       context: context,
//       initialDate: selectedFromDate!,
//       firstDate: selectedFromDate!,
//       lastDate: lastDate,
//     );

//     if (picked != null) {
//       setState(() {
//         toDate = FormatterManager.formatDateToString(picked);
//         getAdvanceDetails();
//       });
//     }
//   }

//   @override
//   void initState() {
//     super.initState();

//     userLoginDetails = DataProvider().getParsedUserData()?.output?.first;
//     fromDate = FormatterManager.formatDateToString(DateTime.now());
//     toDate = FormatterManager.formatDateToString(DateTime.now());
//     setState(() {});
//     getAdvanceDetails();
//   }

//   getAdvanceDetails() {
//     ToastManager.showLoader();
//     Map<String, String> dict = {
//       "FromReqDate": fromDate,
//       "ToReqDate": toDate,
//       "distlgdcode": userLoginDetails?.dISTLGDCODE?.toString() ?? "0",
//       "USERID": userLoginDetails?.empCode?.toString() ?? "0",
//     };
//     print(dict);
//     apiManager.getAdvadetailsNewVersionV2API(
//       dict,
//       apiAdvadetailsNewVersionV2Back,
//     );
//   }

//   void apiAdvadetailsNewVersionV2Back(
//     AdvadetailsNewVersionV2Response? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       campExprenessList = response?.output ?? [];
//     } else {
//       campExprenessList = [];
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
//           scTitle: "Bill Submission",
//           leadingIcon: iconBackArrow,
//           onLeadingIconClick: () {
//             Navigator.pop(context);
//           },
//         ),
//         body: AnnotatedRegion(
//           value: SystemUiOverlayStyle(
//             statusBarColor: kPrimaryColor,
//             statusBarBrightness: Brightness.dark,
//             statusBarIconBrightness: Brightness.light,
//           ),
//           child: Container(
//             color: Colors.white,
//             height: SizeConfig.screenHeight + 50,
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
//                   top: 10,
//                   bottom: 8,
//                   left: 20,
//                   right: 20,
//                   child: Column(
//                     mainAxisAlignment: MainAxisAlignment.start,
//                     crossAxisAlignment: CrossAxisAlignment.start,
//                     children: [
//                       Container(
//                         width: MediaQuery.of(context).size.width,
//                         decoration: const BoxDecoration(
//                           color: Colors.white,
//                           borderRadius: BorderRadius.all(Radius.circular(10)),
//                         ),
//                         padding: const EdgeInsets.all(10),
//                         child: Column(
//                           children: [
//                             AppDropdownTextfield(
//                               icon: icMapPin,
//                               titleHeaderString: "District",
//                               valueString: userLoginDetails?.district ?? "",
//                               isDisabled: true,
//                               onTap: () {},
//                             ),
//                             const SizedBox(height: 12),
//                             Container(
//                               width: MediaQuery.of(context).size.width,
//                               color: Colors.transparent,
//                               child: Row(
//                                 mainAxisAlignment: MainAxisAlignment.start,
//                                 crossAxisAlignment: CrossAxisAlignment.start,
//                                 children: [
//                                   Expanded(
//                                     child: AppDateTextfield(
//                                       icon: icCalendarMonth,
//                                       titleHeaderString: "From Date",
//                                       valueString: fromDate,
//                                       onTap: () {
//                                         _selectFromDate(context);
//                                       },
//                                     ),
//                                   ),
//                                   const SizedBox(width: 12),
//                                   Expanded(
//                                     child: AppDateTextfield(
//                                       icon: icCalendarMonth,
//                                       titleHeaderString: "To Date",
//                                       valueString: toDate,
//                                       onTap: () {
//                                         _selectToDate(context);
//                                       },
//                                     ),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                           ],
//                         ),
//                       ),

//                       Expanded(
//                         child: Container(
//                           color: Colors.transparent,
//                           child: bottomDataLayout(),
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
