// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../../../Modules/Json_Class/AppoinmentExpectedBeneficiariesResponse/AppoinmentExpectedBeneficiariesResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/fonts.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';

class AppointmentsConfirmedRow extends StatelessWidget {
  AppointmentsConfirmedRow({super.key, required this.obj});

  AppoinmentExpectedBeneficiariesOutput obj;

  int totalScreeningPending() {
    return (obj.dependantScreeningPending ?? 0) +
        (obj.workerScreeninPending ?? 0);
  }

  @override
  Widget build(BuildContext context) {
    return Container(
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
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Expanded(
                child: Container(
                  color: Colors.transparent,
                  child: Text(
                    obj.beneficiaryName ?? "",
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w600,
                      fontSize: responsiveFont(14),
                    ),
                  ),
                ),
              ),
              GestureDetector(
                onTap: () {
                  launchUrl(Uri.parse('tel:${obj.mobile ?? ""}'));
                },
                child: Container(
                  width: 30,
                  height: 30,
                  decoration: BoxDecoration(
                    color: kPrimaryColor,
                    borderRadius: BorderRadius.circular(100),
                  ),
                  padding: EdgeInsets.all(8),
                  child: Image.asset(icPhoneWhiteIcon),
                ),
              ),
            ],
          ).paddingOnly(bottom: 4),
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(
                width: responsiveHeight(24),
                height: responsiveHeight(24),
                child: Image.asset(icHashIcon),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Beneficiary ID : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(14),
                      ),
                    ),

                    Expanded(
                      child: Text(
                        obj.beneficiaryNo ?? "",
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
              SizedBox(
                width: responsiveHeight(24),
                height: responsiveHeight(24),
                child: Image.asset(iconCalender),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Appointment Date : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(14),
                      ),
                    ),

                    Expanded(
                      child: Text(
                        obj.appoinmentDateTime ?? "",
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
              SizedBox(
                width: responsiveHeight(24),
                height: responsiveHeight(24),
                child: Image.asset(iconPerson),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Worker Screened : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(14),
                      ),
                    ),

                    Expanded(
                      child: Text(
                        obj.isWorkerScreened ?? "",
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
              SizedBox(
                width: responsiveHeight(24),
                height: responsiveHeight(24),
                child: Image.asset(icScreeningPendingIcon),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Screening Pending : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(14),
                      ),
                    ),

                    Expanded(
                      child: Text(
                        totalScreeningPending().toString(),
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
              SizedBox(
                width: responsiveHeight(24),
                height: responsiveHeight(24),
                child: Image.asset(icMapPin),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Area : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(14),
                      ),
                    ),

                    Expanded(
                      child: Text(
                        obj.area ?? "",
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
              SizedBox(
                width: responsiveHeight(24),
                height: responsiveHeight(24),
                child: Image.asset(iconPerson),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Landmark : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(14),
                      ),
                    ),

                    Expanded(
                      child: Text(
                        obj.landMark ?? "",
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
              SizedBox(
                width: responsiveHeight(24),
                height: responsiveHeight(24),
                child: Image.asset(icPhleboRemarkIcon),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Phlebo Remark : ",
                      style: TextStyle(
                        color: Colors.black,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(14),
                      ),
                    ),

                    Expanded(
                      child: Text(
                        obj.phleboRemark ?? "",
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
        ],
      ).paddingSymmetric(vertical: 4, horizontal: 4),
    ).paddingSymmetric(vertical: 4, horizontal: 4);
  }
}
