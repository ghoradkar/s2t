// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/SideDrawerMenu/logout_screen.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class SideDrawerMenu extends StatefulWidget {
  final String appVersion;

  const SideDrawerMenu({super.key, required this.appVersion});

  @override
  State<SideDrawerMenu> createState() => _SideDrawerMenuState();
}

class _SideDrawerMenuState extends State<SideDrawerMenu> {
  String fulNameStirng = "";
  String designation = "";

  @override
  void initState() {
    super.initState();
    fulNameStirng =
        DataProvider().getParsedUserData()?.output?.first.name ?? "";
    designation =
        DataProvider().getParsedUserData()?.output?.first.designation ?? "";
  }

  @override
  Widget build(BuildContext context) {
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
                      fulNameStirng,
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
                      onTap: () {
                        Navigator.of(context).pop();
                      },
                      child: Container(
                        width: SizeConfig.screenWidth,
                        height: 50,
                        color: Colors.transparent,
                        padding: EdgeInsets.fromLTRB(20, 0, 20, 0),
                        child: Row(
                          children: [
                            SizedBox(
                              width: 30,
                              height: 30,
                              child: Image.asset(icChangePasswordIcon),
                            ),
                            const SizedBox(width: 10),
                            Text(
                              "Change Password",
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
                      onTap: () {
                        Get.to(LogOutScreen());
                      },
                      child: Container(
                        width: SizeConfig.screenWidth,
                        height: 50,
                        color: Colors.transparent,
                        padding: EdgeInsets.fromLTRB(20, 0, 20, 0),
                        child: Row(
                          children: [
                            SizedBox(
                              width: 30,
                              height: 30,
                              child: Image.asset(icLogoutIcon),
                            ),
                            const SizedBox(width: 10),
                            Text(
                              "Logout",
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
                    Spacer(),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          "Version : ",
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: Colors.black,
                            fontSize: 14,
                            fontWeight: FontWeight.normal,
                          ),
                        ),
                        Text(
                          widget.appVersion,
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
      // Container(
      //   width: SizeConfig.screenWidth * 0.8,
      //   decoration: const BoxDecoration(color: kWhiteColor),
      //   child: Column(
      //     children: [
      //       Container(
      //         width: SizeConfig.screenWidth * 0.8,
      //         color: kPrimaryColor,
      //         child: Column(
      //           mainAxisAlignment: MainAxisAlignment.center,
      //           crossAxisAlignment: CrossAxisAlignment.center,
      //           children: [
      //             const SizedBox(height: 16),
      //             Container(height: 60, width: 60, color: Colors.red),
      //             const SizedBox(height: 8),

      //             const SizedBox(height: 16),
      //           ],
      //         ),
      //       ),
      //     ],
      //   ),
      //   // ListView(
      //   //   padding: EdgeInsets.zero,
      //   //   children: [
      //   //     Container(
      //   //       height: responsiveHeight(200),
      //   //       padding: const EdgeInsets.all(20),
      //   //       decoration: const BoxDecoration(color: kPrimaryColor),
      //   //       alignment: Alignment.centerLeft,
      //   //       child: Column(
      //   //         mainAxisAlignment: MainAxisAlignment.center,
      //   //         crossAxisAlignment: CrossAxisAlignment.center,
      //   //         children: [
      //   //           const Text(
      //   //             'Menu',
      //   //             textAlign: TextAlign.center,
      //   //             style: TextStyle(
      //   //               fontFamily: FontConstants.interFonts,
      //   //               color: Colors.white,
      //   //               fontSize: 20,
      //   //               fontWeight: FontWeight.bold,
      //   //             ),
      //   //           ),
      //   //         ],
      //   //       ),
      //   //     ),
      //   //     // ListTile(
      //   //     //   leading: const Icon(Icons.home),
      //   //     //   title: const Text('Home'),
      //   //     //   onTap: () {
      //   //     //     Navigator.of(context).pop();
      //   //     //   },
      //   //     // ),
      //   //     // ListTile(
      //   //     //   leading: const Icon(Icons.person),
      //   //     //   title: const Text('Profile'),
      //   //     //   onTap: () {
      //   //     //     Navigator.of(context).pop();
      //   //     //   },
      //   //     // ),
      //   //     ListTile(
      //   //       leading: const Icon(Icons.logout),
      //   //       title: const Text('Logout'),
      //   //       onTap: () {

      //   //   ],
      //   // ),
      // ),
    );
  }
}
