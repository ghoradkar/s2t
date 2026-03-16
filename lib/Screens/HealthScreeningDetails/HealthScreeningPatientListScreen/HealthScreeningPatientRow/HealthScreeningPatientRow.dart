// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import '../../../../Modules/Enums/Enums.dart';
import '../../../../Modules/Json_Class/UserAttendancesUsingSitedetailsIDResponse/UserAttendancesUsingSitedetailsIDResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class HealthScreeningPatientRow extends StatelessWidget {
  HealthScreeningPatientRow({
    super.key,
    required this.obj,
    required this.screeningMenu,
    required this.onDidPressed,
  });
  Function() onDidPressed;
  UserAttendancesUsingSitedetailsIDOutput obj;
  HealthScreeningDetailsMenu screeningMenu;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 0, 0, 10),
      child: Container(
        padding: EdgeInsets.fromLTRB(12, 8, 12, 8),
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
        child: Row(
          children: [
            Expanded(
              child: GestureDetector(
                onTap: () {
                  onDidPressed();
                },
                child: Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        SizedBox(
                          width: responsiveHeight(24),
                          height: responsiveHeight(24),
                          child: Image.asset(icInitiatedBy),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                "Patient Name : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(12),
                                ),
                              ),

                              Expanded(
                                child: Text(
                                  obj.englishName ?? "",
                                  style: TextStyle(
                                    color: dropDownTitleHeader,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w400,
                                    fontSize: responsiveFont(12),
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
                          child: Image.asset(icHashIcon),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                "Unique Screening Id : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(12),
                                ),
                              ),
                              Expanded(
                                child: Text(
                                  "${obj.screeningPatientID ?? "NA"}",
                                  style: TextStyle(
                                    color: dropDownTitleHeader,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w400,
                                    fontSize: responsiveFont(12),
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
                          child: Image.asset(icHashIcon),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                "Type : ",
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(12),
                                ),
                              ),
                              Expanded(
                                child: Text(
                                  "W",
                                  style: TextStyle(
                                    color: dropDownTitleHeader,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w400,
                                    fontSize: responsiveFont(12),
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
            ),
            GestureDetector(
              onTap: () async {
                final Uri url = Uri(scheme: 'tel', path: obj.mobileNo ?? "");
                if (await canLaunchUrl(url)) {
                  await launchUrl(url);
                } else {
                  throw 'Could not launch $url';
                }
              },
              child: Container(
                width: 30,
                height: 30,
                padding: EdgeInsets.all(6),
                decoration: BoxDecoration(
                  color: kPrimaryColor,
                  borderRadius: BorderRadius.circular(100),
                ),
                child: Image.asset(icPhoneWhiteIcon),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
