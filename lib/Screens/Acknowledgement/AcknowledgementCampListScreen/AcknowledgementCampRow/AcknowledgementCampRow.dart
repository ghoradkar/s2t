// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';

import '../../../../Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/fonts.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';

class AcknowledgementCampRow extends StatelessWidget {
  AcknowledgementCampRow({
    super.key,
    required this.reMappingCampOutput,
    required this.onSelectTap,
    required this.isRegular,
  });

  ResourceReMappingCampOutput reMappingCampOutput;
  Function() onSelectTap;
  bool isRegular = true;
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
                          "${reMappingCampOutput.campId ?? 0}",
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
                        isRegular == true ? "Address : " : "District",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(16),
                        ),
                      ),
                      Expanded(
                        child: Text(
                          isRegular == true
                              ? reMappingCampOutput.campLocation ?? ""
                              : reMappingCampOutput.dISTNAME ?? "",

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
                          reMappingCampOutput.campTypeDescription ?? "",
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
                          reMappingCampOutput.initiatedBy1 ?? "",
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
