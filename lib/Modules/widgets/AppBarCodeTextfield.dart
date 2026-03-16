// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/images.dart';

import '../constants/constants.dart';
import '../constants/fonts.dart';
import '../utilities/SizeConfig.dart';

class AppBarCodeTextfield extends StatelessWidget {
  String titleHeaderString;
  TextEditingController controller;
  Function(String) onSearch;
  Function() onBarcodeScanned;

  AppBarCodeTextfield({
    super.key,
    required this.titleHeaderString,
    required this.controller,
    required this.onSearch,
    required this.onBarcodeScanned,
  });

  @override
  Widget build(BuildContext context) {

    return TextFormField(
      controller: controller,
      style: TextStyle(
        color: Colors.black,
        fontFamily: FontConstants.interFonts,
        fontWeight: FontWeight.w600,
        fontSize: responsiveFont(14),
      ),
      decoration: InputDecoration(
        // floatingLabelBehavior: FloatingLabelBehavior.always,
        suffixIcon: Row(
          mainAxisAlignment: MainAxisAlignment.end,
          mainAxisSize: MainAxisSize.min,
          children: [
            GestureDetector(
              onTap: () {
                onSearch(controller.text);
              },
              child: SizedBox(
                width: responsiveHeight(30),
                height: responsiveHeight(30),
                child: Image.asset(icSearch),
              ),
            ),
            const SizedBox(width: 10),
            GestureDetector(
              onTap: () {
                onBarcodeScanned();
                print("test");
              },
              child: SizedBox(
                width: responsiveHeight(30),
                height: responsiveHeight(30),
                child: Image.asset(icBarcodeIcon),
              ),
            ),
            const SizedBox(width: 10),

          ],
        ),
        floatingLabelStyle: TextStyle(
          color: Colors.black,
          fontFamily: FontConstants.interFonts,
          fontSize: 14.sp * 1.33,
        ),
        label: Text(
          titleHeaderString,
          style: TextStyle(
            color: dropDownTitleHeader,
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w400,
            fontSize: responsiveFont(14),
          ),
        ),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8), // Rounded corners
          borderSide: const BorderSide(
            color: kTextFieldBorder, // Border color
            width: 1.5, // Border width
          ),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8), // Rounded corners
          borderSide: const BorderSide(
            color: kTextFieldBorder, // Border color
            width: 1.5, // Border width
          ),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: Colors.blue, width: 1.0),
        ),
      ),
    );
  }
}
