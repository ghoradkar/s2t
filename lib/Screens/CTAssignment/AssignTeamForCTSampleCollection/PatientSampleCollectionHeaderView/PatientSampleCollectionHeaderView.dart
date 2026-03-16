// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';

class PatientSampleCollectionHeaderView extends StatelessWidget {
  PatientSampleCollectionHeaderView({
    super.key,
    required this.fullName,
    required this.gender,
    required this.age,
  });

  String fullName = "";
  String gender = "";
  String age = "";
  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            offset: Offset(0, 1),
            color: Colors.black.withValues(alpha: 0.15),
            spreadRadius: 0,
            blurRadius: 4,
          ),
        ],
      ),
      padding: EdgeInsets.all(10),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              SizedBox(
                width: responsiveHeight(20),
                height: responsiveHeight(20),
                child: Image.asset(icInitiatedBy),
              ),
              const SizedBox(width: 4),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Name : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(16),
                      ),
                    ),
                    Expanded(
                      child: Text(
                        fullName,
                        style: TextStyle(
                          color: dropDownTitleHeader,
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
            children: [
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    SizedBox(
                      width: responsiveHeight(20),
                      height: responsiveHeight(20),
                      child: Image.asset(icGenderIcon),
                    ),
                    const SizedBox(width: 4),
                    Expanded(
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "Gender : ",
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(16),
                            ),
                          ),
                          Expanded(
                            child: Text(
                              gender,
                              style: TextStyle(
                                color: dropDownTitleHeader,
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
              ),
              const SizedBox(width: 10),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    SizedBox(
                      width: responsiveHeight(20),
                      height: responsiveHeight(20),
                      child: Image.asset(icCalendarMonth),
                    ),
                    const SizedBox(width: 4),
                    Expanded(
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.start,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            "Age : ",
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(16),
                            ),
                          ),
                          Expanded(
                            child: Text(
                              age,
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
              ),
            ],
          ),
        ],
      ),
    );
  }
}
