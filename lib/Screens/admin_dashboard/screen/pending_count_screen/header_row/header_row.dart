// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../../../Modules/constants/constants.dart';

class HeaderRow extends StatelessWidget {
  const HeaderRow({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 74.h,
      decoration: BoxDecoration(
        color: kPrimaryColor,
        border: Border.all(color: offWhite),
      ),
      padding: EdgeInsets.symmetric(horizontal: 12.w),
      child: Row(
        children: [
          HeadCell('Division\nName'),
          HeadCell('Hub Lab Processing Delayed'),
          HeadCell('Home Processing Delayed'),
          HeadCell('Dr. Screening Completion Delayed'),
        ],
      ),
    );
  }
}

class HeadCell extends StatelessWidget {
  final String text;
  final int flex;
  final bool isNumeric;

  const HeadCell(this.text, {super.key, this.flex = 1, this.isNumeric = false});

  @override
  Widget build(BuildContext context) {
    return Expanded(
      flex: flex,
      child: Container(
        height: double.infinity,
        padding: EdgeInsets.symmetric(horizontal: 10.w),
        alignment: Alignment.centerLeft,
        child: Text(
          text,
          maxLines: 3,
          textAlign: TextAlign.left,
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.w700,
            fontSize: 14.sp,
          ),
        ),
      ),
    );
  }
}
