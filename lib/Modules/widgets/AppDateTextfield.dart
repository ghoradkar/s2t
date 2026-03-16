// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';

import '../constants/constants.dart';
import '../constants/fonts.dart';
import '../utilities/SizeConfig.dart';

class AppDateTextfield extends StatelessWidget {
  AppDateTextfield({
    super.key,
    required this.icon,
    required this.titleHeaderString,
    required this.valueString,
    this.isDisabled = false,
    required this.onTap,
    this.valueColor = kBlackColor,
  });

  String icon;
  String titleHeaderString;
  String valueString;
  bool isDisabled = false;
  Function() onTap;
  Color? valueColor;
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        onTap();
      },
      child: Container(
        width: MediaQuery.of(context).size.width,
        padding: const EdgeInsets.fromLTRB(6, 6, 6, 6),
        decoration: BoxDecoration(
          color:
              isDisabled == true
                  ? droDownBGColor.withValues(alpha: 0.8)
                  : Colors.white,
          border: Border.all(color: dropDownBorder),
          borderRadius: BorderRadius.circular(8),
        ),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SizedBox(width: 20, height: 20, child: Image.asset(icon)),
            const SizedBox(width: 6),
            Expanded(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    titleHeaderString,
                    style: TextStyle(
                      color: dropDownTitleHeader,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w400,
                      fontSize: responsiveFont(14),
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    valueString,
                    style: TextStyle(
                      color: valueColor ?? Colors.black,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w400,
                      fontSize: responsiveFont(16),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
