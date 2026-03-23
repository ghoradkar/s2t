import 'package:flutter/material.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppButtonWithIcon.dart';
import '../../../Modules/widgets/S2TAppBar.dart';

class LogoutWidget extends StatelessWidget {
  const LogoutWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        appBar: mAppBar(scTitle: ""),
        body: SizedBox(
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
                left: 105,
                right: 105,
                child: Stack(
                  children: [
                    Image.asset(
                      icCircle,
                      height: responsiveHeight(219),
                      width: responsiveHeight(219),
                    ),
                    Positioned(
                      top: 0,
                      bottom: 0,
                      left: 0,
                      right: 0,
                      child: Center(
                        child: Image.asset(
                          icLogout,
                          height: responsiveHeight(100),
                          width: responsiveHeight(100),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
              Positioned(
                bottom: 123,
                left: 0,
                right: 0,
                child: Column(
                  children: [
                    Text(
                      "Logout",
                      style: TextStyle(
                        fontWeight: FontWeight.w700,
                        fontSize: responsiveFont(24),
                      ),
                    ),
                    SizedBox(height: responsiveHeight(25)),
                    Text(
                      "Are you sure , you want to logout?",
                      style: TextStyle(fontSize: responsiveFont(18)),
                      textAlign: TextAlign.center,
                    ),
                    SizedBox(height: responsiveHeight(50)),
                    Padding(
                      padding: EdgeInsets.only(
                        left: responsiveHeight(40),
                        right: responsiveHeight(40),
                      ),
                      child: Row(
                        children: [
                          Expanded(
                            child: AppButtonWithIcon(
                              onTap: () async {
                                await DataProvider().clearSession(context);
                              },
                              title: "Yes",
                              mWidth: responsiveWidth(175),
                            ),
                          ),
                          SizedBox(width: responsiveHeight(30)),
                          Expanded(
                            child: AppButtonWithIcon(
                              onTap: () {
                                Navigator.pop(context);
                              },
                              buttonColor: kButtonFaintColor,
                              title: "No",
                              mWidth: responsiveWidth(175),
                              textStyle: const TextStyle(color: kBlackColor),
                              icon: Image.asset(
                                iconArrow,
                                color: kBlackColor,
                                height: responsiveHeight(24),
                                width: responsiveHeight(24),
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
