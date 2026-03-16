// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/constants/constants.dart';

import '../../constants/fonts.dart';
import '../../utilities/SizeConfig.dart';

class AppMultilineTextField extends StatelessWidget {
  AppMultilineTextField({
    super.key,
    required this.leftIcon,
    required this.controller,
    required this.titleString,
  });

  TextEditingController controller;
  String titleString;
  String leftIcon;
  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: dropDownBorder, width: 1),
      ),
      child: Padding(
        padding: const EdgeInsets.fromLTRB(8, 8, 8, 8),
        child: Row(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              width: 30,
              height: 30,
              color: Colors.transparent,
              child: Image.asset(leftIcon),
            ),
            Expanded(
              child: Container(
                width: double.infinity,
                color: Colors.transparent,
                child: Padding(
                  padding: const EdgeInsets.fromLTRB(8, 0, 8, 0),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        titleString,
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: responsiveFont(14),
                          fontWeight: FontWeight.w400,
                          color: dropDownTitleHeader,
                        ),
                      ),
                      Container(
                        color: Colors.transparent,
                        child: TextField(
                          controller: controller,
                          keyboardType: TextInputType.multiline,
                          minLines: 1,
                          maxLines: 6,
                          textAlignVertical: TextAlignVertical.top,
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: responsiveFont(16),
                          ),
                          decoration: InputDecoration(
                            hintText: "",
                            contentPadding: EdgeInsets.zero,
                            isDense: true,
                            focusedBorder: const OutlineInputBorder(
                              borderSide: BorderSide(
                                color: Colors.transparent,
                                width: 1.0,
                              ),
                            ),
                            hintStyle: TextStyle(
                              color: dropDownTitleHeader,
                              fontFamily: FontConstants.interFonts,
                              fontSize: responsiveFont(14),
                              fontWeight: FontWeight.w400,
                            ),
                            border: const OutlineInputBorder(
                              borderSide: BorderSide(color: Colors.transparent),
                            ),
                            enabledBorder: const OutlineInputBorder(
                              borderSide: BorderSide(color: Colors.transparent),
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
