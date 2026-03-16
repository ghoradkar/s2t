// import 'package:flutter/material.dart';
// import 'package:flutter_screenutil/flutter_screenutil.dart';
// import 'package:get/get.dart';
// import 'package:s2toperational/Modules/constants/constants.dart';
// import 'package:s2toperational/Modules/widgets/CommonText.dart';
//
// class SuperAdminDashCard extends StatelessWidget {
//   final String title;
//   final String? icon;
//   final String subtitle;
//   final List<String> headers; // expected e.g. ["HLL","HSCC","Total"]
//   // sections: map sectionTitle -> list of (label, hll, hscc, total)
//   final Map<String, List<List<String>>> sections;
//
//   const SuperAdminDashCard({
//     super.key,
//     required this.title,
//     required this.subtitle,
//     required this.headers,
//     required this.sections,
//     this.icon,
//   });
//
//   // Colors for left label blocks
//   Color _sectionColor(String section) {
//     switch (section.toLowerCase()) {
//       case 'treatment':
//         return const Color(0xFFF0E9FF); // light purple
//       case 'ipd':
//         return const Color(0xFFFFF0E0); // light orange
//       case 'medicine':
//         return const Color(0xFFF0FFF2); // light green
//       default:
//         return const Color(0xFFEFEFEF);
//     }
//   }
//
//   Color _sectionLabelColor(String section) {
//     switch (section.toLowerCase()) {
//       case 'treatment':
//         return kPrimaryColor;
//       case 'ipd':
//         return const Color(0xFFCF8A3A);
//       case 'medicine':
//         return const Color(0xFF2FA44F);
//       default:
//         return kTextColor;
//     }
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     TextStyle cellStyle = TextStyle(
//       color: kBlackColor,
//       fontSize: 8.sp,
//       fontWeight: FontWeight.w500,
//     );
//
//     return Card(
//       elevation: 2,
//       shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
//       margin: EdgeInsets.symmetric(vertical: 8.h, horizontal: 10.w),
//       child: Padding(
//         padding: EdgeInsets.all(8.w),
//         child: Column(
//           crossAxisAlignment: CrossAxisAlignment.start,
//           children: [
//             /// Title + Subtitle row
//             Row(
//               children: [
//                 CommonText(
//                   text: title,
//                   fontSize: 14.sp,
//                   fontWeight: FontWeight.w600,
//                   textColor: kBlackColor,
//                   textAlign: TextAlign.start,
//                 ),
//                 const Spacer(),
//                 CommonText(
//                   text: subtitle,
//                   fontSize: 10.sp,
//                   fontWeight: FontWeight.w600,
//                   textColor: kTextColor,
//                   textAlign: TextAlign.start,
//                 ),
//               ],
//             ),
//             SizedBox(height: 8.h),
//
//             /// Use LayoutBuilder to compute available width and split columns
//             LayoutBuilder(
//               builder: (context, constraints) {
//                 final totalWidth = constraints.maxWidth;
//
//                 // proportions - tweak these for visual fit
//                 final leftBlockRatio = 0.18; // colored section block width
//                 final detailRatio = 0.36; // label/detail column width
//                 final numericAreaRatio = 1 - leftBlockRatio - detailRatio;
//
//                 final leftBlockWidth = totalWidth * leftBlockRatio;
//                 final detailWidth = totalWidth * detailRatio;
//                 final numericWidthTotal = totalWidth * numericAreaRatio;
//                 final perNumeric =
//                     numericWidthTotal / (headers.isEmpty ? 3 : headers.length);
//
//                 // Header bar (aligned with detail + numeric area)
//                 return Column(
//                   crossAxisAlignment: CrossAxisAlignment.start,
//                   children: [
//                     // Header row: blank left block + detail header + numeric headers
//                     Container(
//                       decoration: BoxDecoration(
//                         color: kPrimaryColor,
//                         borderRadius: BorderRadius.circular(8),
//                       ),
//                       padding: EdgeInsets.symmetric(
//                         vertical: 8.h,
//                         horizontal: 8.w,
//                       ),
//                       child: Row(
//                         children: [
//                           SizedBox(width: leftBlockWidth),
//                           // blank under left colored block
//                           SizedBox(
//                             width: detailWidth,
//                             child: CommonText(
//                               text: '',
//                               // optional detail header title empty to match design
//                               fontSize: 8.sp,
//                               fontWeight: FontWeight.w600,
//                               textColor: kWhiteColor,
//                               textAlign: TextAlign.left,
//                             ),
//                           ),
//                           // numeric headers
//                           for (final h in headers)
//                             SizedBox(
//                               width: perNumeric,
//                               child: CommonText(
//                                 text: h,
//                                 fontSize: 8.sp,
//                                 fontWeight: FontWeight.w600,
//                                 textColor: kWhiteColor,
//                                 textAlign: TextAlign.center,
//                               ),
//                             ),
//                         ],
//                       ),
//                     ),
//                     SizedBox(height: 8.h),
//
//                     // Sections
//                     for (final entry in sections.entries)
//                       Padding(
//                         padding: EdgeInsets.only(bottom: 10.h),
//                         child: Row(
//                           crossAxisAlignment: CrossAxisAlignment.start,
//                           children: [
//                             // Left colored label block
//                             Container(
//                               width: leftBlockWidth,
//                               decoration: BoxDecoration(
//                                 color: _sectionColor(entry.key),
//                                 borderRadius: BorderRadius.circular(8),
//                               ),
//                               padding: EdgeInsets.symmetric(
//                                 vertical: 8.h,
//                                 horizontal: 6.w,
//                               ),
//                               child: Center(
//                                 child: CommonText(
//                                   text: entry.key,
//                                   fontSize: 8.sp,
//                                   fontWeight: FontWeight.w700,
//                                   textColor: _sectionLabelColor(entry.key),
//                                   textAlign: TextAlign.center,
//                                 ),
//                               ),
//                             ),
//
//                             SizedBox(width: 12.w),
//
//                             // Right area (detail column + numeric columns)
//                             Column(
//                               crossAxisAlignment: CrossAxisAlignment.start,
//                               children: [
//                                 // For each subrow in this section
//                                 for (final subrow in entry.value)
//                                   Container(
//                                     width: detailWidth + numericWidthTotal,
//                                     margin: EdgeInsets.only(bottom: 6.h),
//                                     padding: EdgeInsets.symmetric(
//                                       vertical: 10.h,
//                                       horizontal: 8.w,
//                                     ),
//                                     decoration: BoxDecoration(
//                                       color: kWhiteColor,
//                                       border: Border.all(
//                                         color: const Color(0xFFE0E0E0),
//                                       ),
//                                       borderRadius: BorderRadius.circular(8),
//                                     ),
//                                     child: Row(
//                                       children: [
//                                         /// Detail column (expands & wraps)
//                                         SizedBox(
//                                           width: detailWidth - 8.w,
//                                           child: Text(
//                                             // subrow format support: if first is section label, second is detail; else use first
//                                             subrow.length >= 5
//                                                 ? subrow[1]
//                                                 : (subrow.length >= 2
//                                                     ? subrow[1]
//                                                     : subrow[0]),
//                                             style: cellStyle,
//                                             maxLines: 2,
//                                             overflow: TextOverflow.ellipsis,
//                                           ),
//                                         ),
//
//                                         // numeric columns
//                                         for (int i = 0; i < headers.length; i++)
//                                           SizedBox(
//                                             width: perNumeric,
//                                             child: CommonText(
//                                               text:
//                                                   (subrow.length >=
//                                                           headers.length)
//                                                       ? subrow.sublist(
//                                                         subrow.length -
//                                                             headers.length,
//                                                       )[i]
//                                                       : '',
//                                               fontSize: 8.sp,
//                                               fontWeight: FontWeight.w700,
//                                               textColor: _sectionLabelColor(
//                                                 entry.key,
//                                               ),
//                                               textAlign: TextAlign.center,
//                                             ),
//                                           ),
//                                       ],
//                                     ),
//                                   ),
//                               ],
//                             ),
//                           ],
//                         ),
//                       ),
//                   ],
//                 );
//               },
//             ),
//           ],
//         ),
//       ),
//     );
//   }
// }
