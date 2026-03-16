// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/Json_Class/T2TCTBeneficiaryDetailsResponse/T2TCTBeneficiaryDetailsResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';

class CTAssignmentRow extends StatelessWidget {
  CTAssignmentRow({super.key, required this.obj, required this.onSelectTap});

  T2TCTBeneficiaryDetailsOutput obj;
  Function() onSelectTap;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(8, 0, 8, 20),
      child: Container(
        width: MediaQuery.of(context).size.width,
        decoration: BoxDecoration(
          color: Colors.white,
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.15),
              blurRadius: 4,
            ),
          ],
          borderRadius: BorderRadius.circular(10),
        ),
        padding: EdgeInsets.all(12),
        child: Column(
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(
                  width: responsiveWidth(20),
                  height: responsiveWidth(20),
                  child: Image.asset(icInitiatedBy),
                ),
                const SizedBox(width: 8),
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
                          fontSize: responsiveFont(14),
                        ),
                      ),
                      Expanded(
                        child: Text(
                          obj.beneficiaryName ?? "",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
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
                  width: responsiveWidth(20),
                  height: responsiveWidth(20),
                  child: Image.asset(icHashIcon),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "Beneficiary No. : ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                      Expanded(
                        child: Text(
                          obj.regdno ?? "",

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
                  width: responsiveWidth(20),
                  height: responsiveWidth(20),
                  child: Image.asset(icHashIcon),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "Pincode : ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                      Expanded(
                        child: Text(
                          obj.pinCode ?? "",
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
                  width: responsiveWidth(20),
                  height: responsiveWidth(20),
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
                          fontSize: responsiveFont(14),
                        ),
                      ),
                      Expanded(
                        child: Text(
                          obj.address ?? "",
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
                  width: responsiveWidth(20),
                  height: responsiveWidth(20),
                  child: Image.asset(iconFile),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "CT Assignment Remark : ",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                      Expanded(
                        child: Text(
                          obj.assignmentRemarks ?? "",
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
                  width: responsiveWidth(20),
                  height: responsiveWidth(20),
                  child: Image.asset(icCalendarMonth),
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
                          obj.appointmentDate ?? "",
                          style: TextStyle(
                            color: dropDownTitleHeader,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                      ),
                      GestureDetector(
                        onTap: () {
                          onSelectTap();
                        },
                        child: SizedBox(
                          width: responsiveWidth(26),
                          height: responsiveWidth(26),
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
