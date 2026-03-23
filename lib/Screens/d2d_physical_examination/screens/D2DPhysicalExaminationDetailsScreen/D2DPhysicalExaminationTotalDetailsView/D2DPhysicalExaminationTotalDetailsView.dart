// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';


class D2DPhysicalExaminationTotalDetailsView extends StatelessWidget {
  D2DPhysicalExaminationTotalDetailsView({
    super.key,
    required this.totalAssigned,
    required this.totalCallingPending,
    required this.totalPhyExamPending,
  });

  int totalAssigned;
  int totalCallingPending;
  int totalPhyExamPending;
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        SizedBox(
          height: 70,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Expanded(
                child: Container(
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(10),
                  ),
                  padding: EdgeInsets.fromLTRB(12, 0, 12, 0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Expanded(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              "$totalAssigned",
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: kBlackColor,
                                fontSize: responsiveFont(14),
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            Text(
                              "Total Assigned",
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: uploadBillTitleColor,
                                fontSize: responsiveFont(14),
                                fontWeight: FontWeight.w400,
                              ),
                            ),
                          ],
                        ),
                      ),
                      SizedBox(
                        width: 40,
                        height: 40,
                        child: Image.asset(icTotalAssigned),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Container(
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(10),
                  ),
                  padding: EdgeInsets.fromLTRB(12, 0, 12, 0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Expanded(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              "$totalCallingPending",
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: kBlackColor,
                                fontSize: responsiveFont(14),
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            Text(
                              "Calling Pending",
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: uploadBillTitleColor,
                                fontSize: responsiveFont(14),
                                fontWeight: FontWeight.w400,
                              ),
                            ),
                          ],
                        ),
                      ),
                      SizedBox(
                        width: 40,
                        height: 40,
                        child: Image.asset(icCallingPending),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
        const SizedBox(height: 10),
        SizedBox(
          height: 80,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Expanded(
                child: Container(
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(10),
                  ),
                  padding: EdgeInsets.fromLTRB(12, 0, 12, 0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Expanded(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              "$totalPhyExamPending",
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: kBlackColor,
                                fontSize: responsiveFont(14),
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            Text(
                              "Physical Exam Pending",
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: uploadBillTitleColor,
                                fontSize: responsiveFont(14),
                                fontWeight: FontWeight.w400,
                              ),
                            ),
                          ],
                        ),
                      ),
                      SizedBox(
                        width: 40,
                        height: 40,
                        child: Image.asset(icPhysicalExamPending),
                      ),
                    ],
                  ),
                ),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Container(
                  decoration: BoxDecoration(
                    color: Colors.transparent,
                    borderRadius: BorderRadius.circular(10),
                  ),
                  padding: EdgeInsets.fromLTRB(12, 0, 12, 0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Expanded(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [Text(""), Text("")],
                        ),
                      ),
                      SizedBox(width: 40, height: 40),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }
}
