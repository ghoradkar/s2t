import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';

import '../../../Modules/constants/images.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../CampAwarenessFormScreen/CampAwarenessFormScreen.dart';
import 'CampAwarenessCampRow/CampAwarenessCampRow.dart';

class CampAwarenessActivityScreen extends StatefulWidget {
  const CampAwarenessActivityScreen({super.key});

  @override
  State<CampAwarenessActivityScreen> createState() =>
      _CampAwarenessActivityScreenState();
}

class _CampAwarenessActivityScreenState
    extends State<CampAwarenessActivityScreen> {
  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Awareness Camp List",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
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
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) => CampAwarenessFormScreen(),
                              ),
                            );
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
  }
}
