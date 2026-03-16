// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/utilities/WidgetPaddingX.dart';

import '../../Modules/widgets/CommonText.dart';

class SuperAdminCard extends StatelessWidget {
  final String title;
  final String? icon;
  final String subtitle;
  final List<String> headers;
  final List<List<String>> rows;

  const SuperAdminCard({
    super.key,
    required this.title,
    required this.subtitle,
    required this.headers,
    required this.rows,
    this.icon,
  });

  // Helper method to get row styling based on the row type
  Map<String, dynamic> _getRowStyling(String rowType) {
    switch (rowType.toLowerCase()) {
      case 'total':
        return {'backgroundColor': kPrimaryColor.withValues(alpha: 0.1)};
      case 'regular':
        return {'backgroundColor': Colors.green.withValues(alpha: 0.1)};
      case 'd2d':
        return {'backgroundColor': Colors.orange.withValues(alpha: 0.1)};
      case 'treatment': // Changed from 'Treatment' to 'treatment'
        return {'backgroundColor': kPrimaryColor.withValues(alpha: 0.1)};
      case 'ipd': // Changed from 'IPD' to 'ipd'
        return {'backgroundColor': Colors.orange.withValues(alpha: 0.1)};
      case 'medicine': // Changed from 'Medicine' to 'medicine'
        return {'backgroundColor': Colors.green.withValues(alpha: 0.1)};
      default:
        return {
          'backgroundColor': Colors.transparent,
          'textColor': kBlackColor,
        };
    }
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
      margin: EdgeInsets.symmetric(vertical: 8.h, horizontal: 10.w),
      child: Padding(
        padding: const EdgeInsets.all(12.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            /// Header
            Row(
              children: [
                if (icon != null)
                  Image.asset(icon!, width: 30.w).paddingOnly(right: 6.w),
                CommonText(
                  text: title,
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w600,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
              ],
            ),
            CommonText(
              text: subtitle,
              fontSize: 10.sp,
              fontWeight: FontWeight.w600,
              textColor: kTextColor,
              textAlign: TextAlign.start,
            ).paddingOnly(top: 2.h),
            SizedBox(height: 8.h),

            /// Table
            SingleChildScrollView(
              scrollDirection: Axis.horizontal,
              child: DataTable(
                headingRowColor: WidgetStateProperty.all(kPrimaryColor),
                columns:
                    headers
                        .map(
                          (h) => DataColumn(
                            label: CommonText(
                              text: h,
                              fontSize: 10.sp,
                              fontWeight: FontWeight.normal,
                              textColor: kWhiteColor,
                              textAlign: TextAlign.start,
                            ),
                          ),
                        )
                        .toList(),
                rows:
                    rows.map((row) {
                      final String rowType = row.isNotEmpty ? row[0] : '';
                      final styling = _getRowStyling(rowType);

                      return DataRow(
                        color: WidgetStateProperty.all(
                          styling['backgroundColor'],
                        ),
                        cells:
                            row.asMap().entries.map((entry) {
                              final int index = entry.key;
                              final String cell = entry.value;

                              return DataCell(
                                CommonText(
                                  text: cell,
                                  fontSize: 10.sp,
                                  fontWeight:
                                      index == 0
                                          ? FontWeight.w600
                                          : FontWeight.normal,
                                  // Only first column (label) gets bold
                                  textColor: kBlackColor,
                                  textAlign: TextAlign.start,
                                ),
                              );
                            }).toList(),
                      );
                    }).toList(),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
