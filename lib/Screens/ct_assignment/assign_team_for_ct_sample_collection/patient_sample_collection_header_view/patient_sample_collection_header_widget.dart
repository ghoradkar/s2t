// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/constants/constants.dart';

class PatientSampleCollectionHeaderWidget extends StatelessWidget {
  PatientSampleCollectionHeaderWidget({
    super.key,
    required this.fullName,
    required this.gender,
    required this.age,
  });

  String fullName = "";
  String gender = "";
  String age = "";

  @override
  Widget build(BuildContext context) {
    final initial = fullName.isNotEmpty ? fullName[0].toUpperCase() : '?';

    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: const Color(0xFFE2DFFB), width: 1.2),
        boxShadow: [
          BoxShadow(
            offset: const Offset(0, 2),
            color: Colors.black.withValues(alpha: 0.07),
            blurRadius: 10,
          ),
        ],
      ),
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 14.h),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          // Avatar circle with initial
          CircleAvatar(
            radius: 24.r,
            backgroundColor: kPrimaryColor.withValues(alpha: 0.12),
            child: Text(
              initial,
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w700,
                fontSize: 18.sp,
                color: kPrimaryColor,
              ),
            ),
          ),
          SizedBox(width: 14.w),
          // Name + meta info
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  fullName.isNotEmpty ? fullName : '—',
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w600,
                    fontSize: 14.sp,
                    color: kBlackColor,
                  ),
                ),
                SizedBox(height: 6.h),
                Row(
                  children: [
                    _metaItem(Icons.wc_rounded, gender.isNotEmpty ? gender : '—'),
                    _divider(),
                    _metaItem(
                      Icons.cake_rounded,
                      age.isNotEmpty ? '$age yrs' : '—',
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _metaItem(IconData icon, String value) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Icon(icon, size: 13.sp, color: Colors.grey.shade500),
        SizedBox(width: 4.w),
        Text(
          value,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w400,
            fontSize: 12.sp,
            color: Colors.grey.shade600,
          ),
        ),
      ],
    );
  }

  Widget _divider() {
    return Container(
      width: 1,
      height: 12.h,
      margin: EdgeInsets.symmetric(horizontal: 10.w),
      color: Colors.grey.shade300,
    );
  }
}
