// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../../../Modules/constants/constants.dart';
import '../../FibroScanningPatientDataScreen.dart';

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
