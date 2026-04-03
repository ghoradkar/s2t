// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import '../../../Modules/Json_Class/TeamsMMUDoctorListResponse/TeamsMMUDoctorListResponse.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/fonts.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';

class MMUDoctorView extends StatefulWidget {
  MMUDoctorView({
    super.key,
    required this.list,
    required this.deleteDidPressed,
  });
  List<TeamsMMUDoctorListOutput> list = [];
  Function(TeamsMMUDoctorListOutput) deleteDidPressed;

  @override
  State<MMUDoctorView> createState() => _MMUDoctorViewState();
}

class _MMUDoctorViewState extends State<MMUDoctorView> {
  bool isExpaneded = false;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 4, 0, 0),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          border: Border.all(width: 1, color: droDownBGColor),
          borderRadius:
              isExpaneded == true
                  ? BorderRadius.only(
                    topLeft: Radius.circular(8),
                    topRight: Radius.circular(8),
                  )
                  : BorderRadius.all(Radius.circular(8)),
        ),
        child: Column(
          children: [
            Container(
              padding: EdgeInsets.fromLTRB(12, 4, 12, 4),
              decoration: BoxDecoration(
                color: kPrimaryColor,
                borderRadius:
                    isExpaneded == true
                        ? BorderRadius.only(
                          topLeft: Radius.circular(8),
                          topRight: Radius.circular(8),
                        )
                        : BorderRadius.all(Radius.circular(8)),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(
                      "MMU Doctor",
                      style: TextStyle(
                        color: kWhiteColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w400,
                        fontSize: responsiveFont(16),
                      ),
                    ),
                  ),
                  GestureDetector(
                    onTap: () {
                      isExpaneded = !isExpaneded;
                      setState(() {});
                    },
                    child: SizedBox(
                      width: 30,
                      height: 30,
                      child: Image.asset(
                        isExpaneded == true ? icUpArrowIcon : icDownArrowIcon,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            ListView.builder(
              physics: NeverScrollableScrollPhysics(),
              shrinkWrap: true,
              itemCount: widget.list.length,
              itemBuilder: (context, index) {
                TeamsMMUDoctorListOutput teamCampDetailsOutput =
                    widget.list[index];
                return Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Container(
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.all(Radius.circular(10)),
                      border: Border.all(color: borderDashboardColor),
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withValues(alpha: 0.15),
                          blurRadius: 4,
                        ),
                      ],
                    ),
                    padding: EdgeInsets.all(8),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Expanded(
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.start,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                "Team Number :- ${teamCampDetailsOutput.teamNumber ?? "NA"}",
                                style: TextStyle(
                                  color: kBlackColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w600,
                                  fontSize: responsiveFont(14),
                                ),
                              ),
                              const SizedBox(height: 4),
                              Text(
                                teamCampDetailsOutput.memberName ?? "NA",
                                style: TextStyle(
                                  color: kLabelTextColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w600,
                                  fontSize: responsiveFont(14),
                                ),
                              ),
                            ],
                          ),
                        ),
                        GestureDetector(
                          onTap: () {
                            widget.deleteDidPressed(teamCampDetailsOutput);
                          },
                          child: SizedBox(
                            width: 20,
                            height: 20,
                            child: Image.asset(icDelete, fit: BoxFit.contain),
                          ),
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
