// ignore_for_file: file_names

import 'package:flutter/material.dart';

import '../../../../../Modules/constants/constants.dart';
import '../../../../../Modules/widgets/CommonText.dart';

class Cell extends StatelessWidget {
  final String text;
  final int flex;
  final bool right;
  final bool bold;
  final double fontSize;

  const Cell(
    this.text, {
    super.key,
    this.flex = 1,
    this.right = false,
    this.bold = false,
    this.fontSize = 10,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      flex: flex,
      child: Align(
        alignment: Alignment.centerLeft,
        child: CommonText(
          text: text,
          fontSize: fontSize,
          fontWeight: bold ? FontWeight.w600 : FontWeight.w400,
          textColor: kBlackColor,
          textAlign: TextAlign.start,
        ),
      ),
    );
  }
}
