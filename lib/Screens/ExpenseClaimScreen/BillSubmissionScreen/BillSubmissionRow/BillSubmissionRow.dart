// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';

import '../../../../Modules/Json_Class/AdvadetailsNewVersionV2Response/AdvadetailsNewVersionV2Response.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class BillSubmissionRow extends StatelessWidget {
  BillSubmissionRow({
    super.key,
    required this.object,
    required this.onEyesIconTap,
    required this.onViewDetailsTap,
  });

  AdvadetailsNewOutput object;
  Function(AdvadetailsNewOutput object) onEyesIconTap;
  Function(AdvadetailsNewOutput object) onViewDetailsTap;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(8, 0, 8, 10),
      child: InkWell(
        onTap: () {
          onEyesIconTap(object);
        },
        child: Container(
          width: MediaQuery.of(context).size.width,
          decoration: BoxDecoration(
            color: Colors.white,
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.15),
                blurRadius: 6,
              ),
            ],
            borderRadius: BorderRadius.circular(10),
          ),
          padding: EdgeInsets.all(8),
          child: Column(
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // SizedBox(
                  //   width: responsiveHeight(30),
                  //   height: responsiveHeight(30),
                  //   child: Image.asset(icHashIcon),
                  // ),
                  // const SizedBox(width: 8),
                  Expanded(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "Camp ID : ",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                        Expanded(
                          child: Text(
                            "${object.campid ?? 0}",
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(14),
                            ),
                          ),
                        ),

                        SizedBox(
                          width: 20,
                          height: 20,
                          child: Image.asset(icViewIcon),
                        ),

                        // object.expenseSaved == "Yes"
                        //     ? GestureDetector(
                        //       onTap: () {
                        //         onViewDetailsTap(object);
                        //       },
                        //       child: Container(
                        //         width: 100,
                        //         padding: EdgeInsets.fromLTRB(4, 4, 4, 4),
                        //         decoration: BoxDecoration(
                        //           color: kPrimaryColor,
                        //           borderRadius: BorderRadius.circular(8),
                        //         ),
                        //         child: Row(
                        //           children: [
                        //             Expanded(
                        //               child: Text(
                        //                 "View Bill Details",
                        //                 textAlign: TextAlign.center,
                        //                 style: TextStyle(
                        //                   color: Colors.white,
                        //                   fontFamily: FontConstants.interFonts,
                        //                   fontWeight: FontWeight.w600,
                        //                   fontSize: responsiveFont(11),
                        //                 ),
                        //               ),
                        //             ),
                        //             SizedBox(
                        //               width: 16,
                        //               height: 16,
                        //               child: Image.asset(iconArrow),
                        //             ),
                        //           ],
                        //         ),
                        //       ),
                        //     )
                        //     : SizedBox(),
                      ],
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 8),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  // SizedBox(
                  //   width: responsiveHeight(30),
                  //   height: responsiveHeight(30),
                  //   child: Image.asset(icMapPin),
                  // ),
                  // const SizedBox(width: 8),
                  Expanded(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "District : ",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                        Expanded(
                          child: Text(
                            object.distname ?? "",
                            style: TextStyle(
                              color: dropDownTitleHeader,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: responsiveFont(14),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 8),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  // SizedBox(
                  //   width: responsiveHeight(30),
                  //   height: responsiveHeight(30),
                  //   child: Image.asset(icCalendarMonth),
                  // ),
                  // const SizedBox(width: 8),
                  Expanded(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "Camp Date : ",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                        Expanded(
                          child: Text(
                            object.campdate ?? "",
                            style: TextStyle(
                              color: dropDownTitleHeader,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: responsiveFont(14),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 8),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  // SizedBox(
                  //   width: responsiveHeight(30),
                  //   height: responsiveHeight(30),
                  //   child: Image.asset(icCalendarMonth),
                  // ),
                  // const SizedBox(width: 8),
                  Expanded(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "Approved Date : ",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                        Expanded(
                          child: Text(
                            object.approvedDate ?? "",
                            style: TextStyle(
                              color: dropDownTitleHeader,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: responsiveFont(14),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),

              const SizedBox(height: 8),
              Row(
                children: [
                  Expanded(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        // SizedBox(
                        //   width: responsiveHeight(30),
                        //   height: responsiveHeight(30),
                        //   child: Image.asset(icExpenseSavedIcon),
                        // ),
                        // const SizedBox(width: 8),
                        Expanded(
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                "Expense Saved : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(14),
                                ),
                              ),
                              Expanded(
                                child: Text(
                                  object.expenseSaved ?? "",
                                  style: TextStyle(
                                    color: dropDownTitleHeader,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w400,
                                    fontSize: responsiveFont(14),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        // SizedBox(
                        //   width: responsiveHeight(30),
                        //   height: responsiveHeight(30),
                        //   child: Image.asset(icBillUploadedIcon),
                        // ),
                        // const SizedBox(width: 8),
                        Expanded(
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                "Bill Uploaded : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(14),
                                ),
                              ),
                              Expanded(
                                child: Text(
                                  object.isBillUploaded ?? "",
                                  style: TextStyle(
                                    color: dropDownTitleHeader,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w400,
                                    fontSize: responsiveFont(14),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),

              // const SizedBox(height: 8),
            ],
          ),
        ),
      ),
    );
  }
}
