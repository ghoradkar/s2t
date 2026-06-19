import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/camp_awareness_activity/controllers/camp_awareness_activity_controller.dart';
import 'package:s2toperational/Screens/camp_awareness_activity/screens/camp_awareness_form_screen.dart';
import 'package:s2toperational/Screens/camp_awareness_activity/widgets/camp_awareness_camp_row.dart';

class CampAwarenessActivityScreen extends StatelessWidget {
  const CampAwarenessActivityScreen({super.key});

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<CampAwarenessActivityController>(
      init: CampAwarenessActivityController(),
      dispose: (_) => Get.delete<CampAwarenessActivityController>(),
      builder: (ctrl) {
        return Scaffold(
          appBar: mAppBar(
            scTitle: "Awareness Camp List",
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () {
              Get.back();
            },
            showActions: true,
            actions: [
              Padding(
                padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
                child: GestureDetector(
                  onTap: () {},
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
              child: Stack(
                children: [
                  Positioned(
                    top: 74,
                    child: Image.asset(
                      fit: BoxFit.fill,
                      rect4,
                      width: SizeConfig.screenWidth,
                      height: responsiveHeight(300.37),
                    ),
                  ),
                  Positioned(
                    top: 53,
                    child: Image.asset(
                      fit: BoxFit.fill,
                      rect3,
                      width: SizeConfig.screenWidth,
                      height: responsiveHeight(300.37),
                    ),
                  ),
                  Positioned(
                    top: 30,
                    child: Image.asset(
                      fit: BoxFit.fill,
                      rect2,
                      width: SizeConfig.screenWidth,
                      height: responsiveHeight(300.37),
                    ),
                  ),
                  Image.asset(
                    fit: BoxFit.fill,
                    rect1,
                    width: SizeConfig.screenWidth,
                    height: responsiveHeight(300.37),
                  ),
                  Positioned(
                    top: 0,
                    bottom: 8,
                    left: 8,
                    right: 8,
                    child: Padding(
                      padding: const EdgeInsets.fromLTRB(8, 0, 8, 8),
                      child: Container(
                        color: Colors.red,
                        child: ListView.builder(
                          itemCount: 1,
                          itemBuilder: (context, index) {
                            return CampAwarenessCampRow(
                              onSelectTap: () {
                                Get.to(() => const CampAwarenessFormScreen());
                              },
                            );
                          },
                        ),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }
}