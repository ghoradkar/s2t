// ignore_for_file: use_full_hex_values_for_flutter_colors, file_names
import 'package:s2toperational/Modules/AppDataManager/AppDataManager.dart';
import 'package:s2toperational/Modules/DelegateManager/DelegateManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/CallingModules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Views/AssignToDETeamScreenFilterView/AssignToDETeamScreenFilterView.dart';

import '../../../../../Modules/constants/fonts.dart';
import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'AssignToDETeamScreen.dart';
import 'ReturnInLabScreen.dart';

class PacketAllocationScreen extends StatefulWidget {
  const PacketAllocationScreen({super.key});

  @override
  State<PacketAllocationScreen> createState() => _PacketAllocationScreenState();
}

class _PacketAllocationScreenState extends State<PacketAllocationScreen> {
  bool returnInLab = false;
  bool assiugnToDETeam = true;

  Widget showRadioCampButton() {
    return Positioned(
      top: 0,
      left: 0,
      right: 0,
      child: Stack(
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(8, 6, 8, 8),
            child: Container(
              width: MediaQuery.of(context).size.width,
              height: 50,
              decoration: BoxDecoration(
                color: Colors.transparent,
                border: Border.all(color: kTextFieldBorder, width: 1),
                borderRadius: const BorderRadius.only(
                  topLeft: Radius.circular(20),
                  bottomLeft: Radius.circular(20),
                  topRight: Radius.circular(20),
                  bottomRight: Radius.circular(20),
                ),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: GestureDetector(
                      onTap: () {
                        returnInLab = true;
                        assiugnToDETeam = false;
                        setState(() {});
                      },
                      child: Container(
                        decoration: BoxDecoration(
                          color:
                              returnInLab == true ? kPrimaryColor : kWhiteColor,
                          borderRadius: const BorderRadius.only(
                            topLeft: Radius.circular(20),
                            bottomLeft: Radius.circular(20),
                          ),
                        ),
                        child: Center(
                          child: Row(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              SizedBox(
                                width: 20,
                                height: 20,
                                child: Image.asset(
                                  icReturnInLab,
                                  color:
                                      returnInLab == false
                                          ? Colors.black
                                          : Colors.white,
                                ),
                              ),
                              const SizedBox(width: 6),
                              Text(
                                "Return In Lab",
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color:
                                      returnInLab == false
                                          ? Colors.black
                                          : Colors.white,
                                  fontSize: responsiveFont(12),
                                  fontWeight: FontWeight.normal,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ),
                  Expanded(
                    child: GestureDetector(
                      onTap: () {
                        assiugnToDETeam = true;
                        returnInLab = false;
                        setState(() {});
                      },
                      child: Container(
                        decoration: BoxDecoration(
                          color:
                              assiugnToDETeam == true
                                  ? kPrimaryColor
                                  : kWhiteColor,
                          borderRadius: const BorderRadius.only(
                            topRight: Radius.circular(20),
                            bottomRight: Radius.circular(20),
                          ),
                        ),
                        child: Center(
                          child: Row(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              SizedBox(
                                width: 20,
                                height: 20,
                                child: Image.asset(
                                  icAssignToDETeam,
                                  color:
                                      assiugnToDETeam == false
                                          ? Colors.black
                                          : Colors.white,
                                ),
                              ),
                              const SizedBox(width: 6),
                              Text(
                                "Assign To DE/Team",
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color:
                                      assiugnToDETeam == false
                                          ? Colors.black
                                          : Colors.white,
                                  fontSize: responsiveFont(12),
                                  fontWeight: FontWeight.normal,
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
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

  @override
  void initState() {
    super.initState();
  }

  void showAppointmentFilterBottomSheet() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.08,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: AssignToDETeamScreenFilterView(
            onTapApply: () {
              DelegateManager().triggerRefresh();
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Packet Allocation & Re-Allocation",
          showActions: true,
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            AppDataManager.fromDate = "";
            AppDataManager.toDate = "";
            AppDataManager.selectedTaluka = null;
            AppDataManager.selectedResource = null;
            Navigator.pop(context);
          },
          actions: [
            returnInLab == true
                ? Padding(
                  padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
                  child: GestureDetector(
                    onTap: () {
                      ToastManager.showInfoPopup(context);
                    },
                    child: Container(
                      width: 30,
                      height: 30,
                      padding: const EdgeInsets.all(2),
                      child: Image.asset(icInfo),
                    ),
                  ),
                )
                : Padding(
                  padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
                  child: GestureDetector(
                    onTap: () {
                      showAppointmentFilterBottomSheet();
                    },
                    child: SizedBox(
                      width: 26,
                      height: 26,
                      child: Image.asset(icFilter),
                    ),
                  ),
                ),
          ],
        ),
        body: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Stack(
            children: [
              showRadioCampButton(),
              Positioned(
                top: 64,
                bottom: 8,
                left: 8,
                right: 8,
                child:
                    returnInLab == true
                        ? ReturnInLabScreen()
                        : AssignToDETeamScreen(),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
