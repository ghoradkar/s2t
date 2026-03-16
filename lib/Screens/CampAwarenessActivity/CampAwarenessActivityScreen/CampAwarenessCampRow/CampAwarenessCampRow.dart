// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';

import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/fonts.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';

class CampAwarenessCampRow extends StatelessWidget {
  CampAwarenessCampRow({super.key, required this.onSelectTap});

  Function() onSelectTap;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 0, 0, 10),
      child: Container(
        width: MediaQuery.of(context).size.width,
        decoration: BoxDecoration(
          color: Colors.white,
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.15),
              blurRadius: 10,
            ),
          ],
          borderRadius: BorderRadius.circular(10),
        ),
        padding: EdgeInsets.all(12),
        child: Column(
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.center,
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
                        child: Text(
                          "0",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
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
                  child: Image.asset(icMapPin),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "Address : ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(16),
                        ),
                      ),
                      Expanded(
                        child: Text(
                          "",

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
                  child: Image.asset(icnTent),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "Camp Type : ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(16),
                        ),
                      ),
                      Expanded(
                        child: Text(
                          "",
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
                  child: Image.asset(icInitiatedByIcon),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "Initiated By : ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(16),
                        ),
                      ),
                      Expanded(
                        child: Text(
                          "",
                          style: TextStyle(
                            color: dropDownTitleHeader,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: responsiveFont(16),
                          ),
                        ),
                      ),
                      GestureDetector(
                        onTap: () {
                          onSelectTap();
                        },
                        child: SizedBox(
                          width: 26,
                          height: 26,
                          child: Image.asset(icViewIcon),
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


/*

*/