import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/models/ct_confirmatory_list_model.dart';

class CTConfirmatoryRow extends StatelessWidget {
  final CTConfirmatoryListOutput item;
  final VoidCallback onTap;
  final int index;

  const CTConfirmatoryRow({
    super.key,
    required this.item,
    required this.onTap,
    this.index = 0,
  });

  @override
  Widget build(BuildContext context) {
    final hasAppointment = (item.appointmentDate ?? '').isNotEmpty;

    return GestureDetector(
      onTap: onTap,
      child: Container(
        margin: EdgeInsets.symmetric(horizontal: 12.w, vertical: 5.h),
        padding: EdgeInsets.all(12.w),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(12),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withValues(alpha: 0.06),
              blurRadius: 6,
              spreadRadius: 6,
            ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Name row
            Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                CommonText(
                  text: '${index + 1}. ',
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w700,
                  textColor: kPrimaryColor,
                  textAlign: TextAlign.start,
                ),
                Expanded(
                  child: CommonText(
                    text: (item.beneficiaryName ?? 'N/A').toUpperCase(),
                    fontSize: 13.sp,
                    fontWeight: FontWeight.w700,
                    textColor: kBlackColor,
                    textAlign: TextAlign.start,
                  ),
                ),
                Container(
                  width: responsiveHeight(30),
                  height: responsiveHeight(30),
                  decoration: BoxDecoration(
                    color: kPrimaryColor.withValues(alpha: 0.85),
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: const Center(
                    child: Icon(
                      Icons.remove_red_eye_outlined,
                      color: kWhiteColor,
                      size: 18,
                    ),
                  ),
                ),
              ],
            ),

            // Info rows
            _infoRow(Icons.tag_rounded, 'Reg No', item.regdNo),
            _infoRow(Icons.phone_outlined, 'Mobile', item.mobileNo),
            _infoRow(Icons.people_outline_rounded, 'Members', item.memberCount),
            if (hasAppointment)
              _infoRow(
                Icons.calendar_today_outlined,
                'Appointment',
                item.appointmentDate,
              ),
          ],
        ),
      ),
    );
  }

  Widget _infoRow(IconData icon, String label, String? value) {
    return Padding(
      padding: EdgeInsets.only(top: 4.h),
      child: Row(
        children: [
          Icon(icon, size: 16.sp, color: kBlackColor),
          SizedBox(width: 6.w),
          CommonText(
            text: '$label: ',
            fontSize: 14.sp,

            fontWeight: FontWeight.w500,
            textColor: kBlackColor,
            textAlign: TextAlign.start,
          ),
          Expanded(
            child: CommonText(
              text: value ?? 'N/A',
              fontSize: 14.sp,
              fontWeight: FontWeight.w500,
              textColor: kBlackColor,
              textAlign: TextAlign.start,
              maxLine: 1,
              overflow: TextOverflow.ellipsis,
            ),
          ),
        ],
      ),
    );
  }
}
