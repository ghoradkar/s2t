// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/data_provider.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Screens/login/screens/logout_screen.dart';

class SideDrawerMenu extends StatelessWidget {
  const SideDrawerMenu({super.key, required this.appVersion});

  final String appVersion;

  @override
  Widget build(BuildContext context) {
    final fullName = DataProvider().getParsedUserData()?.output?.first.name ?? '';
    final designation = DataProvider().getParsedUserData()?.output?.first.designation ?? '';

    return SafeArea(
      child: Container(
        height: SizeConfig.screenHeight,
        width: SizeConfig.screenWidth * 0.8,
        color: Colors.white,
        child: Stack(
          children: [
            Positioned(
              top: 0,
              bottom: 0,
              left: 0,
              right: 0,
              child: Container(color: Colors.white),
            ),
            Positioned(
              bottom: 10,
              child: Image.asset(
                fit: BoxFit.fill,
                rect4,
                width: SizeConfig.screenWidth,
                height: responsiveHeight(300.37),
              ),
            ),
            Positioned(
              bottom: 22,
              child: Image.asset(
                fit: BoxFit.fill,
                rect3,
                width: SizeConfig.screenWidth,
                height: responsiveHeight(300.37),
              ),
            ),
            Positioned(
              bottom: 32,
              child: Image.asset(
                fit: BoxFit.fill,
                rect2,
                width: SizeConfig.screenWidth,
                height: responsiveHeight(300.37),
              ),
            ),
            Positioned(
              bottom: 46,
              child: Image.asset(
                fit: BoxFit.fill,
                rect5,
                width: SizeConfig.screenWidth,
                height: responsiveHeight(600.37),
              ),
            ),
            Positioned(
              top: 0,
              left: 0,
              right: 0,
              child: Container(color: kPrimaryColor, height: 500),
            ),
            Positioned(
              top: 8,
              left: 8,
              right: 8,
              bottom: 8,
              child: Container(
                color: Colors.transparent,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    const SizedBox(height: 30),
                    Container(
                      width: 87,
                      height: 87,
                      decoration: BoxDecoration(
                        color: Colors.white,
                        borderRadius: BorderRadius.circular(8),
                        border: Border.all(color: Colors.white, width: 2),
                      ),
                      child: ClipRRect(
                        borderRadius: BorderRadius.circular(8),
                        child: Image.asset(appIcons),
                      ),
                    ),
                    const SizedBox(height: 28),
                    Text(
                      fullName,
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        color: Colors.white,
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      designation,
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        color: Colors.white,
                        fontSize: 14,
                        fontWeight: FontWeight.normal,
                      ),
                    ),
                    const SizedBox(height: 50),
                    GestureDetector(
                      onTap: () => Navigator.of(context).pop(),
                      child: Container(
                        width: SizeConfig.screenWidth,
                        height: 50,
                        color: Colors.transparent,
                        padding: const EdgeInsets.fromLTRB(20, 0, 20, 0),
                        child: Row(
                          children: [
                            SizedBox(
                              width: 30,
                              height: 30,
                              child: Image.asset(icChangePasswordIcon),
                            ),
                            const SizedBox(width: 10),
                            Text(
                              'Change Password',
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: Colors.white,
                                fontSize: 14,
                                fontWeight: FontWeight.normal,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                    GestureDetector(
                      onTap: () => Get.to(() => const LogOutScreen()),
                      child: Container(
                        width: SizeConfig.screenWidth,
                        height: 50,
                        color: Colors.transparent,
                        padding: const EdgeInsets.fromLTRB(20, 0, 20, 0),
                        child: Row(
                          children: [
                            SizedBox(
                              width: 30,
                              height: 30,
                              child: Image.asset(icLogoutIcon),
                            ),
                            const SizedBox(width: 10),
                            Text(
                              'Logout',
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: Colors.white,
                                fontSize: 14,
                                fontWeight: FontWeight.normal,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                    const Spacer(),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          'Version : ',
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: Colors.black,
                            fontSize: 14,
                            fontWeight: FontWeight.normal,
                          ),
                        ),
                        Text(
                          appVersion,
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: Colors.black,
                            fontSize: 16,
                            fontWeight: FontWeight.normal,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
