// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/Json_Class/D2DPhysicalExamDetailsResponse/D2DPhysicalExamDetailsResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class D2DPhysicalExaminationRow extends StatelessWidget {
  D2DPhysicalExaminationRow({
    super.key,
    required this.obj,
    required this.onCampIDDidPressed,
    required this.onAssignedDidPressed,
  });

  D2DPhysicalExamDetailsOutput obj;
  Function() onCampIDDidPressed;
  Function() onAssignedDidPressed;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 0, 0, 20),
      child: Container(
        padding: EdgeInsets.all(8),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(10),
          boxShadow: const [
            BoxShadow(
              offset: Offset(0, 1),
              color: Colors.grey,
              spreadRadius: 0,
              blurRadius: 3,
            ),
          ],
        ),
        child: Column(
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(
                  width: responsiveHeight(30),
                  height: responsiveHeight(30),
                  child: Image.asset(icHashIcon),
                ),
                const SizedBox(width: 8),
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
                          fontSize: responsiveFont(16),
                        ),
                      ),
                      Expanded(
                        child: GestureDetector(
                          onTap: () {
                            onCampIDDidPressed();
                          },
                          child: Text(
                            "${obj.campId ?? ""}",
                            style: TextStyle(
                              color: kPrimaryColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w700,
                              fontSize: responsiveFont(16),
                            ),
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
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(
                  width: responsiveHeight(30),
                  height: responsiveHeight(30),
                  child: Image.asset(icMapPin),
                ),
                const SizedBox(width: 8),
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
                          fontSize: responsiveFont(16),
                        ),
                      ),

                      Expanded(
                        child: Text(
                          obj.district ?? "",
                          style: TextStyle(
                            color: dropDownTitleHeader,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: responsiveFont(16),
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
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(
                  width: responsiveHeight(30),
                  height: responsiveHeight(30),
                  child: Image.asset(icInitiatedBy),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "Assigned : ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(16),
                        ),
                      ),

                      Expanded(
                        child: GestureDetector(
                          onTap: () {
                            onAssignedDidPressed();
                          },
                          child: Text(
                            "${obj.assigned ?? 0}",
                            style: TextStyle(
                              color: dropDownTitleHeader,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w700,
                              fontSize: responsiveFont(16),
                            ),
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
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(
                  width: responsiveHeight(30),
                  height: responsiveHeight(30),
                  child: Image.asset(icPhoneCallIcon),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "Calling Pending : ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(16),
                        ),
                      ),

                      Expanded(
                        child: Text(
                          "${obj.callingPending ?? 0}",
                          style: TextStyle(
                            color: dropDownTitleHeader,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: responsiveFont(16),
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
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(
                  width: responsiveHeight(30),
                  height: responsiveHeight(30),
                  child: Image.asset(icPhysicalExamPendingList),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "Physical Exam Pending : ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(16),
                        ),
                      ),

                      Expanded(
                        child: Text(
                          "${obj.phyExamPending ?? 0}",
                          style: TextStyle(
                            color: dropDownTitleHeader,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: responsiveFont(16),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
