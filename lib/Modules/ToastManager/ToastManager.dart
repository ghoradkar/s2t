// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import '../constants/constants.dart';
import '../constants/fonts.dart';
import '../constants/images.dart';
import '../utilities/SizeConfig.dart';
import '../widgets/AppButtonWithIcon.dart';
import '../widgets/CommonText.dart';
import '../widgets/S2TAlertView.dart';

class ToastManager {
  static final ToastManager _singleton = ToastManager._internal();

  factory ToastManager() {
    return _singleton;
  }

  ToastManager._internal();

  static void showLoader() {
    EasyLoading.instance
      ..maskType = EasyLoadingMaskType.custom
      ..maskColor = Colors.black.withValues(alpha: 0.3)
      ..dismissOnTap = false
      ..userInteractions = false;

    EasyLoading.show(status: "Please wait...");
  }

  static void hideLoader() {
    EasyLoading.dismiss();
  }

  static void toast(String msg) {
    Fluttertoast.showToast(
      msg: msg,
      toastLength: Toast.LENGTH_LONG,
      gravity: ToastGravity.BOTTOM,
      timeInSecForIosWeb: 2,
      backgroundColor: Colors.black,
      textColor: Colors.white,
      fontSize: 15.0,
    );
  }

  static showAlertDialog(
    BuildContext context,
    String message,
    Function onTap, {
    String? title,
  }) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          // title: Text(title),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Image.asset(warning, width: 100.w),
              ),
              Visibility(
                visible: title != null,
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: CommonText(
                    text: title!,
                    fontSize: 18.sp,
                    fontWeight: FontWeight.w600,
                    textColor: kBlackColor,
                    textAlign: TextAlign.center,
                  ),
                ),
              ),

              Padding(
                padding: const EdgeInsets.all(8.0),
                child: CommonText(
                  text: message,
                  fontSize: 18.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.center,
                ),
              ),

              SizedBox(
                width: 80.w,
                child: AppActiveButton(
                  buttontitle: "OK",
                  onTap: () {
                    onTap();
                  },
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  void showConfirmationDialog({
    required BuildContext context,
    required String message,
    required Function(bool) didSelectYes,
  }) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Image.asset(icQuestionMark, width: 100.w),
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text(message, textAlign: TextAlign.center),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  // TextButton(
                  //   child: const Text("No"),
                  //   onPressed: () {
                  //     didSelectYes(false);
                  //   },
                  // ),
                  AppButtonWithIcon(
                    mWidth: 100.w,
                    mHeight: 40.h,
                    title: 'No',
                    onTap: () {
                      didSelectYes(false);
                    },
                  ),
                  SizedBox(width: 10.w),
                  // TextButton(
                  //   child: const Text("Yes"),
                  //   onPressed: () {
                  //     didSelectYes(true);
                  //   },
                  // ),
                  AppButtonWithIcon(
                    mWidth: 108.w,
                    mHeight: 40.h,
                    title: 'Yes',
                    onTap: () {
                      didSelectYes(true);
                    },
                  ),
                ],
              ),
            ],
          ),
        );
      },
    );
  }

  void showSuccessOkayDialog({
    required BuildContext context,
    required String title,
    required String message,
    required Function onTap,
  }) {
    showDialog(
      context: context,
      builder:
          (context) => AlertDialog(
            title: Text(
              title,
              style: TextStyle(fontFamily: FontConstants.interFonts),
            ),
            content: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                Image.asset(icSuccessRoundGreen, width: 100.w),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(
                    message,
                    style: TextStyle(fontFamily: FontConstants.interFonts),
                  ),
                ),
              ],
            ),
            actions: [
              SizedBox(
                width: 80.w,
                child: AppActiveButton(
                  buttontitle: "OK",
                  onTap: () {
                    onTap();
                  },
                ),
              ),
            ],
          ),
    );
  }

  // void showSuccessOkayDialog({
  //   required BuildContext context,
  //   required String title,
  //   required String message,
  //   required Function(bool) didSelectYes,
  // }) {
  //   showDialog(
  //     context: context,
  //     builder: (BuildContext context) {
  //       return AlertDialog(
  //         title: Text(title),
  //         content: Text(message),
  //         actions: [
  //           TextButton(
  //             child: const Text("Okay"),
  //             onPressed: () {
  //               Navigator.pop(context);
  //               didSelectYes(true);
  //             },
  //           ),
  //           // TextButton(
  //           //   child: const Text("No"),
  //           //   onPressed: () {
  //           //     Navigator.pop(context);
  //           //     didSelectYes(false);
  //           //   },
  //           // ),
  //         ],
  //       );
  //     },
  //   );
  // }

  static void showAlertYesNoDialog(
    BuildContext context,
    String title,
    String message,
    void Function(bool) didSelectYes,
  ) {
    // show the dialog
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text(title),
          content: Text(message),
          actions: [
            TextButton(
              child: const Text("Yes"),
              onPressed: () {
                Navigator.pop(context);
                didSelectYes(true);
              },
            ),
            TextButton(
              child: const Text("No"),
              onPressed: () {
                Navigator.pop(context);
                didSelectYes(false);
              },
            ),
          ],
        );
      },
    );
  }

  void showAlertOkayDialog(
    BuildContext context,
    String title,
    String message,
    void Function() didTap,
  ) {
    // show the dialog
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text(title),
          content: Text(message),
          actions: [
            TextButton(
              child: const Text("Okay"),
              onPressed: () {
                didTap;
              },
            ),
          ],
        );
      },
    );
  }

  void showAlertMessage(BuildContext context, String content, Color color) {
    ScaffoldMessenger.of(context)
      ..clearSnackBars()
      ..showSnackBar(
        SnackBar(
          content: Text(content),
          backgroundColor: color,
          duration: const Duration(seconds: 3),
        ),
      );
  }

  static void showSuccessPopup(
    BuildContext parentContext,
    String icon,
    String title,
    Function callB,
  ) {
    showModalBottomSheet(
      context: parentContext,
      isScrollControlled: false,
      enableDrag: false,
      isDismissible: false,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.height,
          color: Colors.transparent,
          child: S2TAlertView(
            icon: icon,
            message: title,
            onTap: () {
              callB();
            },
          ),
        );
      },
    );
  }

  static void showWarningPopup(
    BuildContext parentContext,
    String icon,
    String title,
  ) {
    showModalBottomSheet(
      context: parentContext,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.height,
          color: Colors.transparent,
          child: S2TAlertView(
            icon: icon,
            message: title,
            onTap: () {
              Navigator.of(sheetContext).pop();
            },
          ),
        );
      },
    );
  }

  static Future<void> showInfoPopup(BuildContext context) async {
    await showDialog<void>(
      context: context,
      barrierDismissible: false,
      builder: (context) {
        return AlertDialog(
          title: Text(
            "Info",
            style: TextStyle(
              color: Colors.black,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w400,
              fontSize: 18.sp,
            ),
          ),
          content: Column(
            // mainAxisAlignment: MainAxisAlignment.center,
            mainAxisSize: MainAxisSize.min,
            children: [
              const SizedBox(height: 10),
              SizedBox(
                height: 30,
                child: Row(
                  children: [
                    Container(
                      width: 20,
                      height: 20,
                      decoration: BoxDecoration(
                        color: prescriptionAssignedColor,
                        borderRadius: BorderRadius.circular(6),
                      ),
                    ),
                    const SizedBox(width: 10),
                    Expanded(
                      child: Text(
                        "Medicine Delivery Not Attempted",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w400,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(
                height: 30,
                child: Row(
                  children: [
                    Container(
                      width: 20,
                      height: 20,
                      decoration: BoxDecoration(
                        color: prescriptionAcceptedColor,
                        borderRadius: BorderRadius.circular(6),
                      ),
                    ),
                    const SizedBox(width: 10),
                    Expanded(
                      child: Text(
                        "Need to Re Attempt Medicine Delivery",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w400,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(
                height: 30,
                child: Row(
                  children: [
                    Container(
                      width: 20,
                      height: 20,
                      decoration: BoxDecoration(
                        color: packetReceivedColor,
                        borderRadius: BorderRadius.circular(6),
                      ),
                    ),
                    const SizedBox(width: 10),
                    Expanded(
                      child: Text(
                        "Medicines Delivered Successfully",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w400,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(
                height: 30,
                child: Row(
                  children: [
                    Container(
                      width: 20,
                      height: 20,
                      decoration: BoxDecoration(
                        color: beneficiaryNotAvailableColor,
                        borderRadius: BorderRadius.circular(6),
                      ),
                    ),
                    const SizedBox(width: 10),
                    Expanded(
                      child: Text(
                        "Medicine Denied by Beneficiary",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w400,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              Center(
                child: AppButtonWithIcon(
                  buttonColor: kButtonColor,
                  mHeight: 40,
                  title: "Okay",
                  icon: Image.asset(
                    iconArrow,
                    height: responsiveHeight(24),
                    width: responsiveHeight(24),
                  ),
                  mWidth: 140.w,
                  textStyle: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    color: Colors.white,
                    fontSize: responsiveFont(14),
                  ),
                  onTap: () {
                    Get.back();
                  },
                ),
              ),
            ],
          ),
        );
      },
    );

    // showModalBottomSheet(
    //   context: parentContext,
    //   isScrollControlled: true,
    //   constraints: const BoxConstraints(minWidth: double.infinity),
    //   backgroundColor: Colors.black.withValues(alpha: 0.3),
    //   isDismissible: false,
    //   enableDrag: false,
    //   builder: (BuildContext sheetContext) {
    //     return Container(
    //       width: SizeConfig.screenWidth,
    //       decoration: BoxDecoration(
    //         color: Colors.white,
    //         borderRadius: BorderRadius.circular(20),
    //       ),
    //       padding: EdgeInsets.all(12),
    //       child: Column(
    //         mainAxisAlignment: MainAxisAlignment.center,
    //         children: [
    //           Text(
    //             "Info",
    //             style: TextStyle(
    //               color: Colors.black,
    //               fontFamily: FontConstants.interFonts,
    //               fontWeight: FontWeight.w400,
    //               fontSize: 18.sp,
    //             ),
    //           ),
    //           const SizedBox(height: 10),
    //           SizedBox(
    //             height: 30,
    //             child: Row(
    //               children: [
    //                 Container(
    //                   width: 20,
    //                   height: 20,
    //                   decoration: BoxDecoration(
    //                     color: prescriptionAssignedColor,
    //                     borderRadius: BorderRadius.circular(6),
    //                   ),
    //                 ),
    //                 const SizedBox(width: 10),
    //                 Text(
    //                   "Medicine Delivery Not Attempted",
    //                   style: TextStyle(
    //                     color: Colors.black,
    //                     fontFamily: FontConstants.interFonts,
    //                     fontWeight: FontWeight.w400,
    //                     fontSize: responsiveFont(14),
    //                   ),
    //                 ),
    //               ],
    //             ),
    //           ),
    //           SizedBox(
    //             height: 30,
    //             child: Row(
    //               children: [
    //                 Container(
    //                   width: 20,
    //                   height: 20,
    //                   decoration: BoxDecoration(
    //                     color: prescriptionAcceptedColor,
    //                     borderRadius: BorderRadius.circular(6),
    //                   ),
    //                 ),
    //                 const SizedBox(width: 10),
    //                 Text(
    //                   "Need to Re Attempt Medicine Delivery",
    //                   style: TextStyle(
    //                     color: Colors.black,
    //                     fontFamily: FontConstants.interFonts,
    //                     fontWeight: FontWeight.w400,
    //                     fontSize: responsiveFont(14),
    //                   ),
    //                 ),
    //               ],
    //             ),
    //           ),
    //           SizedBox(
    //             height: 30,
    //             child: Row(
    //               children: [
    //                 Container(
    //                   width: 20,
    //                   height: 20,
    //                   decoration: BoxDecoration(
    //                     color: packetReceivedColor,
    //                     borderRadius: BorderRadius.circular(6),
    //                   ),
    //                 ),
    //                 const SizedBox(width: 10),
    //                 Text(
    //                   "Medicines Delivered Successfully",
    //                   style: TextStyle(
    //                     color: Colors.black,
    //                     fontFamily: FontConstants.interFonts,
    //                     fontWeight: FontWeight.w400,
    //                     fontSize: responsiveFont(14),
    //                   ),
    //                 ),
    //               ],
    //             ),
    //           ),
    //           SizedBox(
    //             height: 30,
    //             child: Row(
    //               children: [
    //                 Container(
    //                   width: 20,
    //                   height: 20,
    //                   decoration: BoxDecoration(
    //                     color: beneficiaryNotAvailableColor,
    //                     borderRadius: BorderRadius.circular(6),
    //                   ),
    //                 ),
    //                 const SizedBox(width: 10),
    //                 Text(
    //                   "Medicine Denied by Beneficiary",
    //                   style: TextStyle(
    //                     color: Colors.black,
    //                     fontFamily: FontConstants.interFonts,
    //                     fontWeight: FontWeight.w400,
    //                     fontSize: responsiveFont(14),
    //                   ),
    //                 ),
    //               ],
    //             ),
    //           ),
    //           Center(
    //             child: Padding(
    //               padding: EdgeInsets.fromLTRB(80, 5, 80, 5),
    //               child: AppButtonWithIcon(
    //                 buttonColor: kButtonColor,
    //                 mHeight: 50,
    //                 title: "Okay",
    //                 icon: Image.asset(
    //                   iconArrow,
    //                   height: responsiveHeight(24),
    //                   width: responsiveHeight(24),
    //                 ),
    //                 mWidth: SizeConfig.screenWidth,
    //                 textStyle: TextStyle(
    //                   fontFamily: FontConstants.interFonts,
    //                   color: Colors.white,
    //                   fontSize: responsiveFont(14),
    //                 ),
    //                 onTap: () {
    //                   Navigator.pop(sheetContext);
    //                 },
    //               ),
    //             ),
    //           ),
    //         ],
    //       ),
    //     );
    //   },
    // );
  }

  static void showSaveYesNoPopup(
    BuildContext parentContext,
    String icon,
    String title,
  ) {
    showModalBottomSheet(
      context: parentContext,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.height,
          color: Colors.transparent,
          child: S2TAlertView(
            icon: icon,
            message: title,
            onTap: () {
              Navigator.of(sheetContext).pop();
              Navigator.of(parentContext).pop();
            },
          ),
        );
      },
    );
  }

  static AlertDialog commonAlert(
    BuildContext parentContext,
    String icon,
    String title,
    String content,
    Function yes,
    Function no,
    String yesButtonText,
    String noButtonText,
  ) {
    return AlertDialog(
      content: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: CommonText(
              text: title,
              fontSize: 24.sp,
              fontWeight: FontWeight.w500,
              textColor: kBlackColor,
              textAlign: TextAlign.center,
            ),
          ),
          Image.asset(icon, width: 100.w),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: CommonText(
              text: content,
              fontSize: 14.sp,
              fontWeight: FontWeight.w500,
              textColor: kBlackColor,
              textAlign: TextAlign.center,
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              AppButtonWithIcon(
                mWidth: 140.w,
                mHeight: 40.h,
                title: yesButtonText,
                onTap: () {
                  yes();
                },
              ),
              SizedBox(width: 10.w),
              TextButton(
                onPressed: () {
                  no();
                },
                child: CommonText(
                  text: noButtonText,
                  fontSize: 16.sp,
                  fontWeight: FontWeight.bold,
                  textColor: kPrimaryColor,
                  textAlign: TextAlign.center,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
