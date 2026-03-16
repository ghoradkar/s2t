// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/utilities/SizeConfig.dart';

class WorkingTeamsCountView extends StatefulWidget {
  int workingTeamsCount = 0;
  int notWorkingTeamsCount = 0;
  int totalTeamsCount = 0;

  Function() onWorkingTeamsTap;
  Function() onNotWorkingTeamsTap;
  Function() onTotalTeamsTap;

  WorkingTeamsCountView({
    super.key,
    required this.workingTeamsCount,
    required this.notWorkingTeamsCount,
    required this.totalTeamsCount,
    required this.onWorkingTeamsTap,
    required this.onNotWorkingTeamsTap,
    required this.onTotalTeamsTap,
  });

  @override
  State<WorkingTeamsCountView> createState() => _WorkingTeamsCountViewState();
}

class _WorkingTeamsCountViewState extends State<WorkingTeamsCountView> {
  int selectedIndex = 0;

  @override
  Widget build(BuildContext context) {
    return Container(
      // padding: EdgeInsets.all(4),
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
      child: Row(
        children: [
          Expanded(
            child: GestureDetector(
              onTap: () {
                setState(() {
                  selectedIndex = 0;
                });
                widget.onWorkingTeamsTap();
              },
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const SizedBox(height: 10),
                  Container(
                    width: 20,
                    height: 20,
                    decoration: BoxDecoration(
                      color: kWhatsappColor,
                      borderRadius: BorderRadius.circular(5),
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    "Working\nTeams",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontFamily: "Inter",
                      fontSize: responsiveFont(14),
                      fontWeight: FontWeight.w400,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    "${widget.workingTeamsCount}",
                    style: TextStyle(
                      fontFamily: "Inter",
                      fontSize: responsiveFont(16),
                      fontWeight: FontWeight.w700,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 10),
                  selectedIndex == 0
                      ? Container(height: 4, color: kWhatsappColor)
                      : Container(),
                ],
              ),
            ),
          ),
          Expanded(
            child: GestureDetector(
              onTap: () {
                setState(() {
                  selectedIndex = 1;
                });
                widget.onNotWorkingTeamsTap();
              },
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const SizedBox(height: 10),
                  Container(
                    width: 20,
                    height: 20,
                    decoration: BoxDecoration(
                      color: kActiveBtnBorderColor,
                      borderRadius: BorderRadius.circular(5),
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    "Not Working\nTeams",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontFamily: "Inter",
                      fontSize: responsiveFont(14),
                      fontWeight: FontWeight.w400,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    "${widget.notWorkingTeamsCount}",
                    style: TextStyle(
                      fontFamily: "Inter",
                      fontSize: responsiveFont(16),
                      fontWeight: FontWeight.w700,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 10),
                  selectedIndex == 1
                      ? Container(height: 4, color: kActiveBtnBorderColor)
                      : Container(),
                ],
              ),
            ),
          ),
          Expanded(
            child: GestureDetector(
              onTap: () {
                setState(() {
                  selectedIndex = 2;
                });
              },
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const SizedBox(height: 10),
                  Container(
                    width: 20,
                    height: 20,
                    decoration: BoxDecoration(
                      color: beneficiariesVerifiedColor,
                      borderRadius: BorderRadius.circular(5),
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    "Total Teams\n",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontFamily: "Inter",
                      fontSize: responsiveFont(14),
                      fontWeight: FontWeight.w400,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 10),
                  Text(
                    "${widget.totalTeamsCount}",
                    style: TextStyle(
                      fontFamily: "Inter",
                      fontSize: responsiveFont(16),
                      fontWeight: FontWeight.w700,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 10),
                  // selectedIndex == 2
                  //     ? Container(height: 4, color: beneficiariesVerifiedColor)
                  //     : Container(),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
