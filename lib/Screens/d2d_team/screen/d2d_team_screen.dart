// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/S2TAppBar.dart';

import '../controller/d2d_team_controller.dart';
import 'call_to_team_popup_view.dart';
import 'd2d_team_filter_view.dart';
import 'd2d_teams_row.dart';
import 'working_teams_count_view.dart';

class D2DTeamScreen extends StatelessWidget {
  const D2DTeamScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final controller = Get.put(D2DTeamController());
    return GetBuilder<D2DTeamController>(
      builder: (_) {
        return Scaffold(
          appBar: mAppBar(
            scTitle: controller.naviTitleString,
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Get.back(),
            showActions: true,
            actions: [
              Padding(
                padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
                child: GestureDetector(
                  onTap: () => _showFilterBottomSheet(context),
                  child: SizedBox(
                    width: 20,
                    height: 20,
                    child: Image.asset(icFilter),
                  ),
                ),
              ),
            ],
          ),
          body: KeyboardDismissOnTap(
            dismissOnCapturedTaps: true,
            child: SizedBox(
              height: SizeConfig.screenHeight,
              width: SizeConfig.screenWidth,
              child: Padding(
                padding: const EdgeInsets.fromLTRB(0, 12, 0, 12),
                child: Column(
                  children: [
                    WorkingTeamsCountView(
                      workingTeamsCount: controller.workingTeamsCount,
                      notWorkingTeamsCount: controller.notWorkingTeamsCount,
                      totalTeamsCount: controller.totalTeamsCount,
                      onNotWorkingTeamsTap: () => controller.groupAPICall(),
                      onWorkingTeamsTap: () => controller.loadWorkingTeams(),
                      onTotalTeamsTap: () {},
                    ).paddingSymmetric(horizontal: 8),
                    const SizedBox(height: 12),
                    Expanded(
                      child: ListView.builder(
                        itemCount: controller.teamList.length,
                        itemBuilder: (context, index) {
                          final item = controller.teamList[index];
                          return Padding(
                            padding: const EdgeInsets.fromLTRB(0, 0, 0, 10),
                            child: D2DTeamsRow(
                              item: item,
                              onCallingTap: () async {
                                final success = await controller
                                    .loadTeamsCalling(item.teamid ?? 0);
                                if (success && context.mounted) {
                                  _showCallingBottomSheet(context, controller);
                                }
                              },
                            ),
                          );
                        },
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        );
      },
    );
  }

  void _showCallingBottomSheet(
    BuildContext context,
    D2DTeamController controller,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.height,
          color: Colors.transparent,
          child: CallToTeamPopupView(callingList: controller.callingList),
        );
      },
    );
  }

  void _showFilterBottomSheet(BuildContext context) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext ctx) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(ctx).size.width * 0.76,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: const D2DTeamFilterView(),
        );
      },
    );
  }
}
