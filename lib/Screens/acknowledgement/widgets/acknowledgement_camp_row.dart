import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Screens/resource_re_mapping/models/ResourceReMappingCampResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/CommonText.dart';

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
    // Camp name: D2D has campName, Regular Camp has campNo/campLocation
    final displayName = camp.campName?.isNotEmpty == true
        ? camp.campName!
        : (camp.campNo?.isNotEmpty == true ? camp.campNo! : (camp.campLocation ?? '-'));

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
              _InfoRow(label: 'Camp ID', value: camp.campId?.toString() ?? '-'),
              const SizedBox(height: 6),
              _InfoRow(label: 'District', value: camp.dISTNAME ?? '-'),
              const SizedBox(height: 6),
              _InfoRow(label: 'Camp Type', value: camp.campTypeDescription ?? '-'),
              const SizedBox(height: 6),
              _InfoRow(label: 'Camp Name', value: displayName),
              const SizedBox(height: 6),
              _InfoRow(label: 'Initiated By', value: camp.initiatedBy1 ?? '-'),
              if ((camp.campCreatedBy?.isNotEmpty == true) || camp.createdBy != null) ...[
                const SizedBox(height: 6),
                _InfoRow(
                  label: 'Created By',
                  value: camp.campCreatedBy?.isNotEmpty == true
                      ? camp.campCreatedBy!
                      : camp.createdBy?.toString() ?? '-',
                ),
              ],
            ],
          ),
        ),
      ),
    );
  }
}

class _InfoRow extends StatelessWidget {
  final String label;
  final String value;

  const _InfoRow({required this.label, required this.value});

  @override
  Widget build(BuildContext context) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(
          width: 90.w,
          child: Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w600,
              fontSize: responsiveFont(13),
              color: Colors.black87,
            ),
          ),
        ),
        Text(
          ': ',
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: responsiveFont(13),
            color: Colors.black87,
          ),
        ),
        Expanded(
          child: Text(
            value,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w400,
              fontSize: responsiveFont(13),
              color: dropDownTitleHeader,
            ),
          ),
        ),
      ],
    );
  }
}
