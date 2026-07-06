// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';

class WorkingTeamsCountView extends StatefulWidget {
  WorkingTeamsCountView({
    super.key,
    required this.workingTeamsCount,
    required this.notWorkingTeamsCount,
    required this.totalTeamsCount,
    required this.onWorkingTeamsTap,
    required this.onNotWorkingTeamsTap,
    required this.onTotalTeamsTap,
  });

  int workingTeamsCount;
  int notWorkingTeamsCount;
  int totalTeamsCount;

  Function() onWorkingTeamsTap;
  Function() onNotWorkingTeamsTap;
  Function() onTotalTeamsTap;

  @override
  State<WorkingTeamsCountView> createState() => _WorkingTeamsCountViewState();
}

class _WorkingTeamsCountViewState extends State<WorkingTeamsCountView> {
  int selectedIndex = 1;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        boxShadow: const [
          BoxShadow(
            offset: Offset(0, 1),
            color: Colors.grey,
            spreadRadius: 0,
            blurRadius: 4,
          ),
        ],
        color: const Color(0XFFFFFFFF),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Row(
        children: [
          Expanded(
            child: GestureDetector(
              onTap: () {
                setState(() => selectedIndex = 0);
                widget.onWorkingTeamsTap();
              },
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const SizedBox(height: 6),
                  Container(
                    width: 20,
                    height: 20,
                    decoration: BoxDecoration(
                      color: const Color(0xff37B956),
                      borderRadius: BorderRadius.circular(5),
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    "Working Teams",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(14),
                      fontWeight: FontWeight.w400,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    "${widget.workingTeamsCount}",
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(16),
                      fontWeight: FontWeight.w700,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 4),
                  selectedIndex == 0
                      ? Container(height: 4, color: kPrimaryColor)
                      : const SizedBox(),
                ],
              ),
            ),
          ),
          Expanded(
            child: GestureDetector(
              onTap: () {
                setState(() => selectedIndex = 1);
                widget.onNotWorkingTeamsTap();
              },
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const SizedBox(height: 6),
                  Container(
                    width: 20,
                    height: 20,
                    decoration: BoxDecoration(
                      color: const Color(0xffDB5369),
                      borderRadius: BorderRadius.circular(5),
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    "Not Working Team",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(14),
                      fontWeight: FontWeight.w400,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    "${widget.notWorkingTeamsCount}",
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(16),
                      fontWeight: FontWeight.w700,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 4),
                  selectedIndex == 1
                      ? Container(height: 4, color: kPrimaryColor)
                      : const SizedBox(),
                ],
              ),
            ),
          ),
          Expanded(
            child: GestureDetector(
              onTap: () {},
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const SizedBox(height: 6),
                  Container(
                    width: 20,
                    height: 20,
                    decoration: BoxDecoration(
                      color: const Color(0xffA27AD3),
                      borderRadius: BorderRadius.circular(5),
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    "Total Teams",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(14),
                      fontWeight: FontWeight.w400,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    "${widget.totalTeamsCount}",
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: responsiveFont(16),
                      fontWeight: FontWeight.w700,
                      color: kBlackColor,
                    ),
                  ),
                  const SizedBox(height: 4),
                  selectedIndex == 2
                      ? Container(height: 4, color: kPrimaryColor)
                      : const SizedBox(),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
