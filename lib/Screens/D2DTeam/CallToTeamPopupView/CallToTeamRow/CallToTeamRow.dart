// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/Json_Class/D2DTeamMemberDetailsResponse/D2DTeamMemberDetailsResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';

class CallToTeamRow extends StatelessWidget {
  CallToTeamRow({super.key, required this.item, required this.onCallingTap});

  D2DTeamMemberDetailsOutput item;
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
      padding: const EdgeInsets.all(12),
      child: Row(
        children: [
          Expanded(
            child: Column(
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.center,
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
                            "Team Memeber : ",
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(16),
                            ),
                          ),
                          Expanded(
                            child: Text(
                              item.teamName ?? "",
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
                  crossAxisAlignment: CrossAxisAlignment.center,
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
                            "Designation : ",
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(16),
                            ),
                          ),
                          Expanded(
                            child: Text(
                              item.desgName ?? "",
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
              ],
            ),
          ),
          SizedBox(
            width: 50,
            child: Center(
              child: GestureDetector(
                onTap: onCallingTap,
                child: SizedBox(
                  width: 40,
                  height: 40,
                  child: Image.asset(icCallingExpected),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
