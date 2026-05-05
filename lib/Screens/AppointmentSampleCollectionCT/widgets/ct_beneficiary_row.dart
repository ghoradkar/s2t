import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Screens/AppointmentSampleCollectionCT/models/ct_confirmatory_list_model.dart';

class CTConfirmatoryRow extends StatelessWidget {
  final CTConfirmatoryListOutput item;
  final VoidCallback onTap;

  const CTConfirmatoryRow({super.key, required this.item, required this.onTap});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        margin: EdgeInsets.symmetric(horizontal: 12.w, vertical: 5.h),
        padding: EdgeInsets.all(12.w),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(10),
          boxShadow: [
            BoxShadow(
              color: Colors.grey.withValues(alpha: 0.15),
              blurRadius: 6,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            CommonText(
              text: item.beneficiaryName ?? 'N/A',
              fontSize: 14.sp,
              fontWeight: FontWeight.w600,
              textColor: kPrimaryColor,
              textAlign: TextAlign.start,
            ),
            SizedBox(height: 4.h),
            _infoRow('Reg No', item.regdNo),
            _infoRow('Mobile', item.mobileNo),
            _infoRow('Members', item.memberCount),
            if ((item.appointmentDate ?? '').isNotEmpty)
              _infoRow('Appointment', item.appointmentDate),
          ],
        ),
      ),
    );
  }

  Widget _infoRow(String label, String? value) {
    return Padding(
      padding: EdgeInsets.only(top: 2.h),
      child: Row(
        children: [
          Text(
            '$label: ',
            style: TextStyle(
              fontSize: 12.sp,
              fontWeight: FontWeight.w500,
              fontFamily: FontConstants.interFonts,
              color: Colors.grey[700],
            ),
          ),
          Expanded(
            child: Text(
              value ?? 'N/A',
              style: TextStyle(
                fontSize: 12.sp,
                fontFamily: FontConstants.interFonts,
                color: kBlackColor,
              ),
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }
}