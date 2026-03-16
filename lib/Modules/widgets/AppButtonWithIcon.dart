// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';

import '../constants/constants.dart';
import '../constants/images.dart';
import '../utilities/SizeConfig.dart';

class AppButtonWithIcon extends StatelessWidget {
  String title = "";
  double? mWidth;
  double? mHeight = 50;
  Function()? onTap;
  TextStyle? textStyle;
  Widget? icon;
  Icon? iconData;
  Color? buttonColor;
  double? iconSize;
  double? borderRadius;
  bool? isEnabled;

  AppButtonWithIcon({
    super.key,
    required this.title,
    this.mWidth,
    this.onTap,
    this.mHeight,
    this.textStyle,
    this.icon,
    this.buttonColor,
    this.iconSize,
    this.iconData,
    this.borderRadius,
    this.isEnabled,
  });

  @override
  Widget build(BuildContext context) {
    return Material(
      color: Colors.transparent,
      child: InkWell(
        splashColor: kWhiteColor.withValues(alpha: 0.4),
        highlightColor: kSecondaryColor.withValues(alpha: 0.4),
        splashFactory: InkRipple.splashFactory,
        onTap: onTap,
        child: Ink(
          height: mHeight ?? responsiveHeight(60),
          width: mWidth ?? SizeConfig.screenWidth * 0.8,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(borderRadius ?? 10),
            gradient: LinearGradient(
              begin: const Alignment(-0.5, 1),
              end: const Alignment(1, 1),
              colors: [
                buttonColor ?? kPrimaryColor,
                buttonColor ?? kPrimaryColor,
              ],
            ),
          ),
          child: Center(
            child: Padding(
              padding: const EdgeInsets.only(left: 20.0, right: 8),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Padding(
                    padding: EdgeInsets.only(
                      right: getProportionateScreenHeight(5),
                    ),
                    child: Text(
                      title,
                      style:
                          textStyle ??
                          TextStyle(
                            fontWeight: FontWeight.normal,
                            color: kWhiteColor,
                          ),
                    ),
                  ),
                  const Spacer(),
                  icon ??
                      Image.asset(
                        iconArrow,
                        height: responsiveHeight(24),
                        width: responsiveHeight(24),
                      ),
                  Padding(
                    padding: EdgeInsets.only(
                      right: getProportionateScreenHeight(5),
                      left: getProportionateScreenHeight(5),
                    ),
                    child: iconData ?? const SizedBox.shrink(),
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
