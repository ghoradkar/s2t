// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

class HeadCell extends StatelessWidget {
  final String text;

  const HeadCell(this.text, {super.key});

  @override
  Widget build(BuildContext context) {
    return Expanded(
      flex: 1,
      child: Align(
        alignment: Alignment.centerLeft,
        child: Text(
          text,
          maxLines: 4,
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.w700,
            fontSize: 12.sp,
          ),
        ),
      ),
    );
  }
}
