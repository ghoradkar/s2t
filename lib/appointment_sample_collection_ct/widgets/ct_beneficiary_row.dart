import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/appointment_sample_collection_ct/models/ct_confirmatory_list_model.dart';

class CTConfirmatoryRow extends StatelessWidget {
  final CTConfirmatoryListOutput item;
  final VoidCallback onTap;
  final int index;
  final bool isLast;

  const CTConfirmatoryRow({
    super.key,
    required this.item,
    required this.onTap,
    this.index = 0,
    this.isLast = false,
  });

  @override
  Widget build(BuildContext context) {
    final isEven = index % 2 == 0;
    return GestureDetector(
      onTap: onTap,
      child: Container(
        decoration: BoxDecoration(
          color: isEven ? kWhiteColor : const Color(0xFFF7F8FA),
          borderRadius: isLast
              ? const BorderRadius.only(
                  bottomLeft: Radius.circular(12),
                  bottomRight: Radius.circular(12),
                )
              : null,
          border: Border(
            left: BorderSide(color: Colors.grey.shade200),
            right: BorderSide(color: Colors.grey.shade200),
            bottom: BorderSide(color: Colors.grey.shade200),
          ),
        ),
        padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 12.h),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            // Sr No
            SizedBox(
              width: 28.w,
              child: Text(
                '${index + 1}',
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w400,
                  color: kTextColor,
                ),
              ),
            ),
            SizedBox(width: 8.w),
            // Beneficiary Name
            Expanded(
              flex: 3,
              child: Text(
                item.beneficiaryName ?? 'N/A',
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w500,
                  color: kBlackColor,
                ),
              ),
            ),
            // Member Count
            SizedBox(
              width: 70.w,
              child: Text(
                item.memberCount ?? '0',
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                  color: kPrimaryColor,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
