// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../constants/constants.dart';
import '../constants/fonts.dart';
import '../utilities/SizeConfig.dart';

class AppMultilineTextField extends StatelessWidget {
  TextEditingController controller;
  Widget? label;

  String? hint;
  Widget? prefixIcon;
  Widget? suffixIcon;
  TextStyle? labelStyle;
  TextStyle? hintStyle;
  int? maxLength;
  int? maxLines;
  int minLines;
  String? Function(String?)? validators;
  String? errorText;
  Function(String)? onChange;
  Function()? onTap;
  double? fieldRadius;
  List<TextInputFormatter>? inputFormatters;
  AutovalidateMode? autovalidateMode;
  TextStyle? inputStyle;

  AppMultilineTextField({
    super.key,
    required this.controller,
    this.label,
    this.suffixIcon,
    this.prefixIcon,
    this.labelStyle,
    this.maxLength,
    this.maxLines,
    this.minLines = 3,
    this.validators,
    this.errorText,
    this.onChange,
    this.hint,
    this.hintStyle,
    this.fieldRadius,
    this.onTap,
    this.inputStyle,
    this.inputFormatters,
    this.autovalidateMode,
  });

  @override
  Widget build(BuildContext context) {
    return TextFormField(
      autovalidateMode: autovalidateMode,
      controller: controller,
      keyboardType: TextInputType.multiline,
      style: inputStyle ?? TextStyle(color: Colors.black, fontSize: 12.sp),
      maxLength: maxLength,
      maxLines: maxLines,
      minLines: minLines,
      cursorColor: kPrimaryColor,
      validator: validators,
      onChanged: onChange,
      onTap: onTap,
      inputFormatters: inputFormatters,
      textCapitalization: TextCapitalization.none,
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
          borderRadius: BorderRadius.circular(fieldRadius ?? 8),
          borderSide: const BorderSide(
            color: kTextFieldBorder,
            width: 1.5,
          ),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(fieldRadius ?? 8),
          borderSide: const BorderSide(
            color: kTextFieldBorder,
            width: 1.5,
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