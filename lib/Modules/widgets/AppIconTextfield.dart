// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../constants/constants.dart';
import '../constants/fonts.dart';
import '../utilities/SizeConfig.dart';

class AppIconTextfield extends StatelessWidget {
  AppIconTextfield({
    super.key,
    required this.icon,
    required this.titleHeaderString,
    required this.controller,
    this.isDisabled = false,
    this.textInputType,
    this.maxLength,
    this.enabled,
    this.didChangeText,
    this.maxLines,
  });

  String icon;
  String titleHeaderString;
  TextEditingController controller;
  bool isDisabled = false;
  TextInputType? textInputType;
  int? maxLength;
  int? maxLines;
  final bool? enabled;
  Function(String)? didChangeText;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width,
      padding: const EdgeInsets.fromLTRB(10, 4, 10, 4),
      decoration: BoxDecoration(
        color: isDisabled == true ? droDownBGColor : Colors.white,
        border: Border.all(color: dropDownBorder),
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          SizedBox(width: 20, height: 20, child: Image.asset(icon)),
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
                //
                TextField(
                  enabled: enabled,
                  minLines: 1,
                  maxLines: maxLines,
                  maxLength: maxLength,
                  keyboardType: textInputType,
                  controller: controller,
                  readOnly: isDisabled,
                  inputFormatters:
                      textInputType == TextInputType.number
                          ? [FilteringTextInputFormatter.digitsOnly]
                          : [],
                  onChanged: (value) {
                    didChangeText!(value);
                  },
                  style: TextStyle(
                    color: Colors.black,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w500,
                    fontSize: responsiveFont(16),
                  ),
                  decoration: const InputDecoration(
                    counterText: "",
                    isDense: true,
                    contentPadding: EdgeInsets.zero,
                    border: InputBorder.none,
                  ),
                ),
                const SizedBox(height: 4),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
