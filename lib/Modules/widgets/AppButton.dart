// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';

import '../constants/constants.dart';
import '../utilities/SizeConfig.dart';

class AppButton extends StatelessWidget {
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
  AppButton({
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
          height: mHeight ?? 50,
          width: mWidth ?? SizeConfig.screenWidth * 0.8,
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(borderRadius ?? 10),
            border:
                buttonColor != null
                    ? Border.all(color: buttonColor!, width: 1)
                    : null,
            gradient: LinearGradient(
              begin: const Alignment(-0.5, 1),
              end: const Alignment(1, 1),
              colors: [
                buttonColor ?? const Color(0xFF27A9E3),
                buttonColor ?? const Color(0xFF07B259),
              ],
            ),
          ),
          child: Center(
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                icon ?? const SizedBox.shrink(),
                Padding(
                  padding: EdgeInsets.only(
                    right: getProportionateScreenHeight(5),
                    left: getProportionateScreenHeight(5),
                  ),
                  child: iconData ?? const SizedBox.shrink(),
                ),
                Padding(
                  padding: EdgeInsets.only(
                    right: getProportionateScreenHeight(5),
                  ),
                  child: Text(
                    title,
                    style:
                        textStyle ??
                        const TextStyle(
                          fontWeight: FontWeight.bold,
                          color: kPrimaryColor,
                        ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
