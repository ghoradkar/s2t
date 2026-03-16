// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';

import '../constants/constants.dart';
import '../constants/fonts.dart';
import '../utilities/SizeConfig.dart';
import 'AppActiveButton.dart';

class S2TAlertView extends StatelessWidget {
  S2TAlertView({
    super.key,
    required this.icon,
    required this.message,
    required this.onTap,
  });

  String icon = "";
  String message = "";
  Function() onTap;
  @override
  Widget build(BuildContext context) {
    return Container(
      color: successBGColor.withValues(alpha: 0.3),
      child: Padding(
        padding: const EdgeInsets.fromLTRB(36, 66, 36, 66),
        child: Center(
          child: IntrinsicHeight(
            child: Container(
              padding: EdgeInsets.fromLTRB(10, 30, 10, 30),
              width: MediaQuery.of(context).size.width,
              decoration: const BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.all(Radius.circular(20)),
              ),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  SizedBox(width: 80, height: 80, child: Image.asset(icon)),
                  const SizedBox(height: 20),
                  Text(
                    textAlign: TextAlign.center,
                    message,
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w400,
                      fontSize: responsiveFont(20),
                    ),
                  ),
                  const SizedBox(height: 20),
                  SizedBox(
                    width: 120,
                    child: AppActiveButton(
                      buttontitle: "Ok",
                      onTap: () {
                        onTap();
                        // Navigator.of(context);
                      },
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
