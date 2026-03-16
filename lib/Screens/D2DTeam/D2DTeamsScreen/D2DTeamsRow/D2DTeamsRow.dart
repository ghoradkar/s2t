// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/Json_Class/D2DNonWorkingTeamsResponse/D2DNonWorkingTeamsResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';

class D2DTeamsRow extends StatelessWidget {
  D2DTeamsRow({super.key, required this.item, required this.onCallingTap});

  D2DNonWorkingTeamsOutput item;

  Function() onCallingTap;
  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        boxShadow: [
          BoxShadow(
            offset: Offset(0, 1),
            color: Colors.grey,
            spreadRadius: 0,
            blurRadius: 4,
          ),
        ],
        color: Color(0XFFFFFFFF),
        borderRadius: BorderRadius.circular(10),
      ),
      padding: const EdgeInsets.all(8),
      child: Row(
        children: [
          Expanded(
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // SizedBox(
                    //   width: responsiveWidth(22),
                    //   height: responsiveWidth(22),
                    //   child: Image.asset(icInitiatedBy),
                    // ),
                    // const SizedBox(width: 8),
                    Expanded(
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "Member 1 : ",
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(14),
                            ),
                          ),
                          Expanded(
                            child: Text(
                              item.member1 ?? "",
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
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // SizedBox(
                    //   width: responsiveWidth(22),
                    //   height: responsiveWidth(22),
                    //   child: Image.asset(icInitiatedBy),
                    // ),
                    // const SizedBox(width: 8),
                    Expanded(
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "Member 2 : ",
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(14),
                            ),
                          ),
                          Expanded(
                            child: Text(
                              item.member2 ?? "",
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
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // SizedBox(
                    //   width: responsiveWidth(22),
                    //   height: responsiveWidth(22),
                    //   child: Image.asset(icInitiatedBy),
                    // ),
                    // const SizedBox(width: 8),
                    Expanded(
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "Reg. Beneficiary : ",
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(14),
                            ),
                          ),
                          Expanded(
                            child: Text(
                              "${item.regBeneficieries ?? 0}",
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
              ],
            ),
          ),
          Center(
            child: GestureDetector(
              onTap: onCallingTap,
              child: SizedBox(
                width: 30,
                height: 30,
                child: Image.asset(icCallingExpected),
              ),
            ),
          ),
        ],
      ),
    ).paddingSymmetric(horizontal: 8);
  }
}
