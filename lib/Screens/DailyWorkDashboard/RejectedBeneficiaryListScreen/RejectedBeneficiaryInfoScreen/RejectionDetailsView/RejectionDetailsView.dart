// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import '../../../../../Modules/Json_Class/BeneficiaryStatusAndDetailsResponse/BeneficiaryStatusAndDetailsResponse.dart';
import '../../../../../Modules/constants/constants.dart';
import '../../../../../Modules/constants/images.dart';
import '../../../../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class RejectionDetailsView extends StatelessWidget {
  RejectionDetailsView({super.key, required this.beneficiaryObj});

  BeneficiaryStatusAndDetailsOutput? beneficiaryObj;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.all(10),
      width: MediaQuery.of(context).size.width - 40,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            offset: Offset(0, 1),
            color: Colors.black.withValues(alpha: 0.15),
            spreadRadius: 0,
            blurRadius: 10,
          ),
        ],
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Expanded(
                child: Text(
                  "Rejection Details",
                  textAlign: TextAlign.left,
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    color: kPrimaryColor,
                    fontSize: responsiveFont(14),
                    fontWeight: FontWeight.w600,
                  ),
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
                        beneficiaryObj?.rejCampID.toString() ?? "",
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
                        beneficiaryObj?.campTypeDescription ?? "",
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
                child: Image.asset(icCalendarMonth),
              ),
              const SizedBox(width: 8),
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
                        fontSize: responsiveFont(16),
                      ),
                    ),
                    Expanded(
                      child: Text(
                        beneficiaryObj?.campDate ?? "",
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
                child: Image.asset(icMapPin),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Rejected At : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(16),
                      ),
                    ),
                    Expanded(
                      child: Text(
                        beneficiaryObj?.rejectedFrom ?? "",
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
                child: Image.asset(icRejectionReason),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Rejection Reason : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(16),
                      ),
                    ),
                    Expanded(
                      child: Text(
                        beneficiaryObj?.rejectedReason ?? "",
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
                child: Image.asset(icLandingLab),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Rejected By (Lab) : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(16),
                      ),
                    ),
                    Expanded(
                      child: Text(
                        beneficiaryObj?.rejectedLabName ?? "",
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
    );
  }
}
