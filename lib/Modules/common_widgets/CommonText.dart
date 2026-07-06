import 'package:flutter/cupertino.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';

class CommonText extends StatelessWidget {
  final String text;
  final double fontSize;
  final FontWeight fontWeight;
  final TextAlign textAlign;
  final Color textColor;
  final int? maxLine;
  final TextOverflow? overflow;

  const CommonText({
    super.key,
    required this.text,
    required this.fontSize,
    required this.fontWeight,
    required this.textColor,
    required this.textAlign,
    this.maxLine,
    this.overflow,
  });

  @override
  Widget build(BuildContext context) {
    return Text(
      text,
      maxLines: maxLine,
      style: TextStyle(
        fontSize: fontSize,
        fontFamily: FontConstants.interFonts,
        fontWeight: fontWeight,
        color: textColor,
      ),
      textAlign: textAlign,
      overflow: overflow,
    );
  }
}