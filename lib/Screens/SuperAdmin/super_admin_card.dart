import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';

class SuperAdminCard extends StatelessWidget {
  final String title;
  final String? icon;
  final String subtitle;
  final List<String> headers;
  final List<List<String>> rows;
  final bool enableRowColor;

  const SuperAdminCard({
    super.key,
    required this.title,
    required this.subtitle,
    required this.headers,
    required this.rows,
    this.icon,
    this.enableRowColor = false,
  });

  Map<String, dynamic> _getRowStyling(String rowType) {
    switch (rowType.trim().toLowerCase()) {
      case 'total':
        return {'backgroundColor': kPrimaryColor.withOpacity(0.1)};
      case 'regular':
        return {'backgroundColor': Colors.green.withOpacity(0.1)};
      case 'd2d':
        return {'backgroundColor': Colors.orange.withOpacity(0.1)};
      case 'treatment':
        return {'backgroundColor': kPrimaryColor.withOpacity(0.1)};
      case 'ipd':
        return {'backgroundColor': Colors.orange.withOpacity(0.15)};
      case 'discharge':
        return {'backgroundColor': Colors.orange.withOpacity(0.12)};
      case 'medicine':
        return {'backgroundColor': Colors.green.withOpacity(0.1)};
      default:
        return {'backgroundColor': Colors.transparent};
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        /// HEADER
        Visibility(
          visible: icon != null,
          child: Row(
            children: [
              if (icon != null)
                Image.asset(icon!, width: 30.w).paddingOnly(right: 6.w),
              CommonText(
                text: title,
                fontSize: 12.sp,
                fontWeight: FontWeight.w600,
                textAlign: TextAlign.start,
                textColor: kBlackColor,
              ),
            ],
          ),
        ),

        Visibility(
          visible: subtitle.isNotEmpty,
          child: CommonText(
            text: subtitle,
            fontSize: 12.sp,
            fontWeight: FontWeight.w600,
            textAlign: TextAlign.start,
            textColor: kTextColor,
          ).paddingOnly(top: 2.h),
        ),

        SizedBox(height: 2.h),

        SizedBox(
          width: MediaQuery.of(context).size.width,
          child: Table(
            defaultVerticalAlignment: TableCellVerticalAlignment.middle,
            border: TableBorder.symmetric(
              inside: BorderSide.none,
              outside: BorderSide.none,
            ),
            // columnWidths: {
            //   for (int i = 0; i < headers.length; i++)
            //     i: const IntrinsicColumnWidth(),
            // },
            columnWidths: {
              0: const FlexColumnWidth(1.2), // Type
              1: const FlexColumnWidth(2.5), // Description
              2: const FlexColumnWidth(1),
              3: const FlexColumnWidth(1),
              4: const FlexColumnWidth(1),
            },
            children: [
              /// HEADER ROW
              TableRow(
                decoration: BoxDecoration(color: kPrimaryColor),
                children:
                    headers.map((h) {
                      return Padding(
                        padding: EdgeInsets.all(8.h),
                        child: CommonText(
                          text: h,
                          fontSize: 10.sp,
                          textColor: kWhiteColor,
                          fontWeight: FontWeight.w600,
                          textAlign: TextAlign.start,
                        ),
                      );
                    }).toList(),
              ),

              /// DATA ROWS WITH GREY BORDER
              ...rows.map((row) {
                final rowType = row.isNotEmpty ? row[0] : '';
                final styling =
                    enableRowColor
                        ? _getRowStyling(rowType)
                        : {'backgroundColor': Colors.transparent};

                return TableRow(
                  decoration: BoxDecoration(
                    color: styling['backgroundColor'],
                    border: Border.all(color: Colors.grey.shade300, width: 1),
                    // borderRadius: BorderRadius.circular(6),
                  ),
                  children:
                      row.asMap().entries.map((entry) {
                        final index = entry.key;
                        final cell = entry.value;

                        return Padding(
                          padding: EdgeInsets.symmetric(
                            vertical: 8.h,
                            horizontal: 6.w,
                          ),
                          child: CommonText(
                            text: cell,
                            fontSize: 10.sp,
                            fontWeight:
                                index == 0
                                    ? FontWeight.w600
                                    : FontWeight.normal,
                            textColor: kBlackColor,
                            textAlign: TextAlign.start,
                          ),
                        );
                      }).toList(),
                );
              }),
            ],
          ),
        ),
      ],
    ).paddingSymmetric(horizontal: 8);
    //   Card(
    //   elevation: 2,
    //   shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
    //   margin: EdgeInsets.symmetric(vertical: 8.h, horizontal: 8.w),
    //   child: Padding(
    //     padding: const EdgeInsets.all(10.0),
    //     child:
    //     Column(
    //       crossAxisAlignment: CrossAxisAlignment.start,
    //       children: [
    //         /// HEADER
    //         Visibility(
    //           visible: icon != null,
    //           child: Row(
    //             children: [
    //               if (icon != null)
    //                 Image.asset(icon!, width: 30.w).paddingOnly(right: 6.w),
    //               CommonText(
    //                 text: title,
    //                 fontSize: 10.sp,
    //                 fontWeight: FontWeight.w600,
    //                 textAlign: TextAlign.start,
    //                 textColor: kBlackColor,
    //               ),
    //             ],
    //           ),
    //         ),
    //
    //         Visibility(
    //           visible: subtitle.isNotEmpty,
    //           child: CommonText(
    //             text: subtitle,
    //             fontSize: 10.sp,
    //             fontWeight: FontWeight.w600,
    //             textAlign: TextAlign.start,
    //             textColor: kTextColor,
    //           ).paddingOnly(top: 2.h),
    //         ),
    //
    //         SizedBox(height: 2.h),
    //
    //         SizedBox(
    //           width: MediaQuery.of(context).size.width,
    //           child: Table(
    //             defaultVerticalAlignment: TableCellVerticalAlignment.middle,
    //             border: TableBorder.symmetric(
    //               inside: BorderSide.none,
    //               outside: BorderSide.none,
    //             ),
    //             // columnWidths: {
    //             //   for (int i = 0; i < headers.length; i++)
    //             //     i: const IntrinsicColumnWidth(),
    //             // },
    //             columnWidths: {
    //               0: const FlexColumnWidth(1.2), // Type
    //               1: const FlexColumnWidth(2.5), // Description
    //               2: const FlexColumnWidth(1),
    //               3: const FlexColumnWidth(1),
    //               4: const FlexColumnWidth(1),
    //             },
    //             children: [
    //               /// HEADER ROW
    //               TableRow(
    //                 decoration: BoxDecoration(color: kPrimaryColor),
    //                 children:
    //                     headers.map((h) {
    //                       return Padding(
    //                         padding: EdgeInsets.all(8.h),
    //                         child: CommonText(
    //                           text: h,
    //                           fontSize: 8.sp,
    //                           textColor: kWhiteColor,
    //                           fontWeight: FontWeight.w600,
    //                           textAlign: TextAlign.start,
    //                         ),
    //                       );
    //                     }).toList(),
    //               ),
    //
    //               /// DATA ROWS WITH GREY BORDER
    //               ...rows.map((row) {
    //                 final rowType = row.isNotEmpty ? row[0] : '';
    //                 final styling =
    //                     enableRowColor
    //                         ? _getRowStyling(rowType)
    //                         : {'backgroundColor': Colors.transparent};
    //
    //                 return TableRow(
    //                   decoration: BoxDecoration(
    //                     color: styling['backgroundColor'],
    //                     border: Border.all(
    //                       color: Colors.grey.shade300,
    //                       width: 1,
    //                     ),
    //                     // borderRadius: BorderRadius.circular(6),
    //                   ),
    //                   children:
    //                       row.asMap().entries.map((entry) {
    //                         final index = entry.key;
    //                         final cell = entry.value;
    //
    //                         return Padding(
    //                           padding: EdgeInsets.symmetric(
    //                             vertical: 8.h,
    //                             horizontal: 6.w,
    //                           ),
    //                           child: CommonText(
    //                             text: cell,
    //                             fontSize: 8.sp,
    //                             fontWeight:
    //                                 index == 0
    //                                     ? FontWeight.w600
    //                                     : FontWeight.normal,
    //                             textColor: kBlackColor,
    //                             textAlign: TextAlign.start,
    //                           ),
    //                         );
    //                       }).toList(),
    //                 );
    //               }),
    //             ],
    //           ),
    //         ),
    //
    //       ],
    //     ),
    //   ),
    // );
  }
}
