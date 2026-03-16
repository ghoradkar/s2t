// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';

import '../../Modules/Json_Class/TeamsCampTypeWiseResponse/TeamsCampTypeWiseResponse.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/fonts.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/AppActiveButton.dart';

class TeamsCampTypeWiseTeamView extends StatefulWidget {
  TeamsCampTypeWiseTeamView({
    super.key,
    required this.list,
    required this.onTapTeam,
  });

  List<TeamsCampTypeWiseOutput> list;
  Function(TeamsCampTypeWiseOutput) onTapTeam;

  @override
  State<TeamsCampTypeWiseTeamView> createState() =>
      _TeamsCampTypeWiseTeamViewState();
}

class _TeamsCampTypeWiseTeamViewState extends State<TeamsCampTypeWiseTeamView> {
  TeamsCampTypeWiseOutput? selectedTeam;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 20, 0, 10),
      child: Column(
        children: [
          Text(
            "Select Team",
            style: TextStyle(
              color: kBlackColor,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w400,
              fontSize: responsiveFont(16),
            ),
          ),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.fromLTRB(10, 10, 10, 0),
              child: ListView.builder(
                itemCount: widget.list.length,
                itemBuilder: (context, index) {
                  TeamsCampTypeWiseOutput obj = widget.list[index];

                  return Padding(
                    padding: const EdgeInsets.fromLTRB(8, 0, 8, 26),
                    child: Container(
                      padding: EdgeInsets.fromLTRB(14, 10, 8, 10),
                      decoration: BoxDecoration(
                        color: Colors.white,
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withValues(alpha: 0.15),
                            offset: Offset(0, 0.4),
                            blurRadius: 8,
                            spreadRadius: 0,
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
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              Expanded(
                                child: Text(
                                  obj.teamname ?? "",
                                  style: TextStyle(
                                    color: kBlackColor,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w600,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                              const SizedBox(width: 8),
                              GestureDetector(
                                onTap: () {
                                  for (TeamsCampTypeWiseOutput obj1
                                      in widget.list) {
                                    obj1.selected = false;
                                  }
                                  obj.selected = true;
                                  selectedTeam = obj;
                                  setState(() {});
                                },
                                child: SizedBox(
                                  width: 26,
                                  height: 26,
                                  child: Image.asset(
                                    obj.selected == true
                                        ? icRadioSelected
                                        : icUnRadioSelected,
                                  ),
                                ),
                              ),
                              const SizedBox(width: 8),
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
                                child: Image.asset(icUserIcon),
                              ),
                              const SizedBox(width: 8),
                              Expanded(
                                child: Text(
                                  obj.member1 ?? "",
                                  style: TextStyle(
                                    color: dropDownTitleHeader,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w600,
                                    fontSize: responsiveFont(16),
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
                                child: Image.asset(icUserIcon),
                              ),
                              const SizedBox(width: 8),
                              Expanded(
                                child: Text(
                                  obj.member2 ?? "",
                                  style: TextStyle(
                                    color: dropDownTitleHeader,
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w600,
                                    fontSize: responsiveFont(16),
                                  ),
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 8),
                        ],
                      ),
                    ),
                  );
                },
              ),
            ),
          ),

          Padding(
            padding: const EdgeInsets.fromLTRB(16, 4, 16, 20),
            child: Container(
              width: MediaQuery.of(context).size.width,
              color: Colors.transparent,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Back",
                      isCancel: true,
                      onTap: () {
                        Navigator.pop(context);
                      },
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Select",
                      onTap: () {
                        Navigator.pop(context);
                        widget.onTapTeam(selectedTeam!);
                      },
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
