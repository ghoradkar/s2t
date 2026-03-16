// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';

import '../constants/constants.dart';
import '../constants/fonts.dart';
import '../utilities/SizeConfig.dart';

class AppIconSearchTextfield extends StatelessWidget {
  AppIconSearchTextfield({
    super.key,
    required this.icon,
    required this.titleHeaderString,
    required this.controller,
    this.isDisabled = false,
    this.textInputType,
    required this.onChange,
  });

  String icon;
  String titleHeaderString;
  TextEditingController controller;
  bool isDisabled = false;
  TextInputType? textInputType;
  Function(String) onChange;
  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width,
      padding: const EdgeInsets.fromLTRB(6, 6, 6, 6),
      decoration: BoxDecoration(
        color:
            isDisabled == true
                ? droDownBGColor.withValues(alpha: 0.8)
                : Colors.white,
        border: Border.all(color: dropDownBorder),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          const SizedBox(width: 4),
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
                TextField(
                  keyboardType: textInputType,
                  controller: controller,
                  style: TextStyle(
                    color: Colors.black,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w600,
                    fontSize: responsiveFont(16),
                  ),
                  decoration: const InputDecoration(
                    isDense: true,
                    contentPadding: EdgeInsets.zero,
                    border: InputBorder.none,
                  ),
                  onChanged: (value) {
                    onChange(value);
                  },
                ),
              ],
            ),
          ),
          SizedBox(width: 20, height: 20, child: Image.asset(icon)),
          const SizedBox(width: 10),
        ],
      ),
    );
  }
}
