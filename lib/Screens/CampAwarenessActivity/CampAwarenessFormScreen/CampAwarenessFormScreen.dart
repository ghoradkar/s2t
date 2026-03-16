import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';

import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';

class CampAwarenessFormScreen extends StatefulWidget {
  const CampAwarenessFormScreen({super.key});

  @override
  State<CampAwarenessFormScreen> createState() =>
      _CampAwarenessFormScreenState();
}

class _CampAwarenessFormScreenState extends State<CampAwarenessFormScreen> {
  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Camp Awareness ",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
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
                  child: Container(color: Colors.red),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
