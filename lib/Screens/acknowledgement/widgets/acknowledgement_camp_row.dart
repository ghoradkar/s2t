import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';

class AcknowledgementNewCampRow extends StatelessWidget {
  final ResourceReMappingCampOutput camp;
  final VoidCallback onTap;

  const AcknowledgementNewCampRow({
    super.key,
    required this.camp,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(10),
        child: Container(
          width: double.infinity,
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(10),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.12),
                blurRadius: 8,
              ),
            ],
          ),
          padding: const EdgeInsets.all(12),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _InfoRow(
                icon: icHashIcon,
                label: 'Camp ID',
                value: camp.campId?.toString() ?? '-',
              ),
              const SizedBox(height: 6),
              _InfoRow(
                icon: icCalendarMonth,
                label: 'Camp Date',
                value: camp.campDate ?? '-',
              ),
              const SizedBox(height: 6),
              _InfoRow(
                icon: icMapPin,
                label: 'Location',
                value: camp.campLocation ?? camp.dISTNAME ?? '-',
              ),
              const SizedBox(height: 6),
              Row(
                children: [
                  SizedBox(
                    width: responsiveHeight(24),
                    height: responsiveHeight(24),
                    child: Image.asset(icnTent),
                  ),
                  const SizedBox(width: 8),
                  Expanded(
                    child: CommonText(
                      text: camp.campTypeDescription ?? '-',
                      fontSize: 13.sp,
                      fontWeight: FontWeight.w400,
                      textColor: dropDownTitleHeader,
                      textAlign: TextAlign.start,
                    ),
                  ),
                  Image.asset(icViewIcon, width: 24, height: 24),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _InfoRow extends StatelessWidget {
  final String icon;
  final String label;
  final String value;

  const _InfoRow({
    required this.icon,
    required this.label,
    required this.value,
  });

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        SizedBox(
          width: responsiveHeight(24),
          height: responsiveHeight(24),
          child: Image.asset(icon),
        ),
        const SizedBox(width: 8),
        Text(
          '$label: ',
          style: TextStyle(
            color: Colors.black,
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w600,
            fontSize: responsiveFont(13),
          ),
        ),
        Expanded(
          child: Text(
            value,
            style: TextStyle(
              color: dropDownTitleHeader,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w400,
              fontSize: responsiveFont(13),
            ),
          ),
        ),
      ],
    );
  }
}