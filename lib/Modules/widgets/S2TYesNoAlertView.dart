// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';

import '../constants/constants.dart';
import '../constants/fonts.dart';

class S2TYesNoAlertView extends StatelessWidget {
  S2TYesNoAlertView({
    super.key,
    required this.icon,
    required this.message,
    required this.onYesTap,
    required this.onNoTap,
  });

  String icon = "";
  String message = "";
  Function() onYesTap;
  Function() onNoTap;
  @override
  Widget build(BuildContext context) {
    return Container(
      color: successBGColor.withValues(alpha: 0.3),
      child: Padding(
        padding: const EdgeInsets.fromLTRB(36, 0, 36, 0),
        child: Center(
          child: IntrinsicHeight(
            child: Container(
              padding: const EdgeInsets.fromLTRB(30, 30, 30, 30),
              decoration: const BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.all(Radius.circular(20)),
              ),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  SizedBox(width: 100, height: 100, child: Image.asset(icon)),
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
                  const SizedBox(height: 40),
                  SizedBox(
                    width: MediaQuery.of(context).size.width,
                    child: Row(
                      children: [
                        Expanded(
                          child: AppActiveButton(
                            buttontitle: "No",
                            isCancel: true,
                            onTap: () {
                              onNoTap();
                            },
                          ),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: AppActiveButton(
                            buttontitle: "Yes",
                            onTap: () {
                              onYesTap();
                            },
                          ),
                        ),
                      ],
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
