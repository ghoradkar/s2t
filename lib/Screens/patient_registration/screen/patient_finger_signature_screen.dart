// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';

class PatientFingerAndSignatureScreen extends StatelessWidget {
  final String campId;
  final String siteId;
  final String regNo;

  const PatientFingerAndSignatureScreen({
    super.key,
    required this.campId,
    required this.siteId,
    required this.regNo,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Fingerprint & Signature',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: Center(
        child: Padding(
          padding: EdgeInsets.all(16.w),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.fingerprint, size: 64, color: kPrimaryColor),
              SizedBox(height: 12.h),
              CommonText(
                text: 'Fingerprint and Signature capture will be added here.',
                fontSize: 14.sp,
                fontWeight: FontWeight.w500,
                textColor: kTextColor,
                textAlign: TextAlign.center,
              ),
              SizedBox(height: 8.h),
              CommonText(
                text: 'Camp ID: $campId\nSite ID: $siteId\nReg No: $regNo',
                fontSize: 12.sp,
                fontWeight: FontWeight.w400,
                textColor: kLabelTextColor,
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
