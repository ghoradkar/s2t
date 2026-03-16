// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../constants/constants.dart';
import '../constants/fonts.dart';
import '../utilities/SizeConfig.dart';

// class UpperCaseTextFormatter extends TextInputFormatter {
//   @override
//   TextEditingValue formatEditUpdate(
//     TextEditingValue oldValue,
//     TextEditingValue newValue,
//   ) {
//     // Remove any non-alphabetic characters and convert to uppercase
//     String filteredText = newValue.text.replaceAll(RegExp(r'[^A-Z]'), '');
//
//     return newValue.copyWith(
//       text: filteredText,
//       selection: TextSelection.collapsed(offset: filteredText.length),
//     );
//   }
// }

class UpperCaseTextFormatter extends TextInputFormatter {
  @override
  TextEditingValue formatEditUpdate(
      TextEditingValue oldValue,
      TextEditingValue newValue,
      ) {
    // Convert to uppercase instead of removing non-uppercase characters
    final String uppercasedText = newValue.text.toUpperCase();

    return newValue.copyWith(
      text: uppercasedText,
      selection: TextSelection.collapsed(offset: uppercasedText.length),
    );
  }
}

class AppTextField extends StatelessWidget {
  TextEditingController controller;
  Widget? label;

  String? hint;
  Widget? prefixIcon;
  Widget? suffixIcon;
  TextStyle? labelStyle;
  TextStyle? hintStyle;
  TextInputType? textInputType;
  int? maxLength;
  int? maxLines;
  int? minLines;
  bool? obscureText;
  String? Function(String?)? validators;
  String? errorText;
  Function(String)? onChange;
  Function()? onTap;
  double? fieldRadius;
  bool? readOnly;
  List<TextInputFormatter>? inputFormatters;
  TextCapitalization? textCapitalization;
  AutovalidateMode? autovalidateMode;

  TextStyle? inputStyle;

  AppTextField({
    super.key,
    required this.controller,
    this.label,
    this.suffixIcon,
    this.prefixIcon,
    this.labelStyle,
    this.textInputType,
    this.maxLength,
    this.maxLines,
    this.minLines,
    this.obscureText,
    this.validators,
    this.errorText,
    this.onChange,
    this.hint,
    this.hintStyle,
    this.fieldRadius,
    this.readOnly,
    this.onTap,
    this.inputStyle,
    this.inputFormatters,
    this.textCapitalization,
    this.autovalidateMode,
  });

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      autovalidateMode: autovalidateMode,
      // autovalidateMode: autovalidateMode,
      readOnly: readOnly ?? false,
      controller: controller,
      keyboardType: textInputType,
      style: inputStyle ?? TextStyle(color: Colors.black, fontSize: 12.sp),
      maxLength: maxLength,
      maxLines: (obscureText ?? false) ? 1 : maxLines ?? 1,
      minLines: (obscureText ?? false) ? 1 : minLines,
      cursorColor: kPrimaryColor,
      obscureText: obscureText ?? false,
      validator: validators,
      onChanged: onChange,
      onTap: onTap,
      inputFormatters: inputFormatters,
      textCapitalization: textCapitalization ?? TextCapitalization.none,
      decoration: InputDecoration(
        floatingLabelBehavior: FloatingLabelBehavior.always,
        fillColor: kWhiteColor,
        filled: true,
        constraints: BoxConstraints(maxWidth: SizeConfig.screenWidth * 0.94),
        suffixIcon: suffixIcon,
        prefixIcon: prefixIcon,
        prefixIconConstraints: BoxConstraints(minWidth: 40.w, maxWidth: 40.w),
        labelStyle: TextStyle(
          color: Colors.black,
          fontFamily: FontConstants.interFonts,
          fontSize: 14.sp,
        ),
        floatingLabelStyle: TextStyle(
          color: Colors.black,
          fontFamily: FontConstants.interFonts,
          fontSize: 14.sp,
        ),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(
            fieldRadius ?? 8,
          ), // Rounded corners
          borderSide: const BorderSide(
            color: kTextFieldBorder, // Border color
            width: 1.5, // Border width
          ),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(
            fieldRadius ?? 8,
          ), // Rounded corners
          borderSide: const BorderSide(
            color: kTextFieldBorder, // Border color
            width: 1.5, // Border width
          ),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(fieldRadius ?? 8),
          borderSide: const BorderSide(color: Colors.blue, width: 1.0),
        ),
        errorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(fieldRadius ?? 8),
          borderSide: const BorderSide(color: kTextFieldBorder, width: 1.0),
        ),
        focusedErrorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(fieldRadius ?? 8),
          borderSide: const BorderSide(color: kTextFieldBorder, width: 1.0),
        ),
        // contentPadding: const EdgeInsets.symmetric(
        //   vertical: 12.0,
        //   horizontal: 16.0,
        // ),
        label: label,
        errorText: errorText,
        errorStyle: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 12.sp,
          fontWeight: FontWeight.w400,
        ),
        counterText: "",
        hintText: hint,
        hintStyle: hintStyle,
      ),
    );
  }
}
