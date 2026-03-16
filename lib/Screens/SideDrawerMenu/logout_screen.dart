import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/LoginScreen/LoginScreen.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppButtonWithIcon.dart';

class LogOutScreen extends StatelessWidget {
  const LogOutScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
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
            top: 20,
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
                      icLogoutIcon,
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
            left: 61,
            right: 61,
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
                Row(
                  children: [
                    AppButtonWithIcon(
                      onTap: () async {
                        final prefs = await SharedPreferences.getInstance();
                        String? userN = await DataProvider().read(
                          DataProvider().kUserName,
                        );
                        String? userPsw = await DataProvider().read(
                          DataProvider().kPassword,
                        );

                        bool keepFlag = await DataProvider().getKeepSignedIn();

                        DataProvider().clearSession(context);

                        if (userN != null) {
                          await DataProvider().save(
                            DataProvider().kUserName,
                            userN,
                          );
                        }
                        if (userPsw != null) {
                          await DataProvider().save(
                            DataProvider().kPassword,
                            userPsw,
                          );
                        }

                        await DataProvider().setKeepSignedIn(keepFlag);


                        Navigator.of(context).pushAndRemoveUntil(
                          MaterialPageRoute(
                            builder: (BuildContext context) => LoginScreen(),
                          ),
                          (Route route) => false,
                        );
                      },
                      title: "Yes",
                      mWidth: responsiveWidth(110),
                    ),
                    SizedBox(width: 30),
                    AppButtonWithIcon(
                      buttonColor: offWhite.withValues(alpha: 0.2),
                      textStyle: TextStyle(
                        color: kPrimaryColor,
                        fontSize: responsiveFont(14),
                      ),
                      icon: Image.asset(
                        iconArrow,
                        height: responsiveHeight(24),
                        width: responsiveHeight(24),
                        color: kPrimaryColor,
                      ),
                      onTap: () {
                        Get.back();
                      },
                      title: "No",
                      mWidth: responsiveWidth(110),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
