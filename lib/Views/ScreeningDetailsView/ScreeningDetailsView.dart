// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/constants.dart';

import '../../Modules/Json_Class/CampDetailsResponse/CampDetailsResponse.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';
// ignore_for_file: must_be_immutable



class ScreeningDetailsView extends StatelessWidget {
  ScreeningDetailsView({super.key, required this.campDetailOutput});

  CampDetailOutput? campDetailOutput;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width,
      decoration: BoxDecoration(
        color: Colors.white,
      ),
      child: Padding(
        padding:  EdgeInsets.fromLTRB(10.w, 6.h, 10.w, 0),
        child: Column(
          children: [
            Container(
              decoration: BoxDecoration(
                color: kPrimaryColor,
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(10),
                  topRight: Radius.circular(10),
                ),
              ),
              width: SizeConfig.screenWidth,
              height: 36.h,
              child: Center(
                child: Text(
                  "Screening Details",
                  style: TextStyle(
                    color: kWhiteColor,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w400,
                    fontSize:14.sp,
                  ),
                ),
              ),
            ),
             SizedBox(height: 4.h),
            _buildRow(
              "Facilitated Beneficiary",
              campDetailOutput?.facilitatedWorkers.toString() ?? "0",
              uploadBillTitleColor,
              isFirst: true,
            ),
             SizedBox(height: 4.h),
            _buildRow(
              "Approved Beneficiaries",
              campDetailOutput?.approvedBeneficiaries.toString() ?? "0",
              approvedBeneficiariesColor,
            ),
             SizedBox(height: 4.h),
            _buildRow(
              "Rejected Beneficiaries",
              campDetailOutput?.rejectedBeneficiaries.toString() ?? "0",
              rejectedBeneficiariesColor,
            ),
             SizedBox(height: 4.h),
            _buildRow(
              "Beneficiaries Verified",
              campDetailOutput?.verifiedBeneficiaries.toString() ?? "0",
              beneficiariesVerifiedColor,
            ),
             SizedBox(height: 4.h),
            _buildRow(
              "Basic Detail",
              campDetailOutput?.basicDetails.toString() ?? "0",
              uploadBillTitleColor,
            ),
             SizedBox(height: 4.h),
            _buildRow(
              "Physical Examination",
              campDetailOutput?.physicalExamination.toString() ?? "0",
              uploadBillTitleColor,
            ),
             SizedBox(height: 4.h),
            _buildRow(
              "Lung Function Test",
              campDetailOutput?.lungFunctioinTest.toString() ?? "0",
              uploadBillTitleColor,
            ),
             SizedBox(height: 4.h),
            _buildRow(
              "Audio Screening Test",
              campDetailOutput?.audioScreeningTest.toString() ?? "0",
              uploadBillTitleColor,
            ),
             SizedBox(height: 4.h),
            _buildRow(
              "Vision Screening Test",
              campDetailOutput?.visionScreening.toString() ?? "0",
              uploadBillTitleColor,
            ),
             SizedBox(height: 4.h),
            _buildRow(
              "Sample Collection",
              campDetailOutput?.barcode.toString() ?? "0",
              uploadBillTitleColor,
            ),
             SizedBox(height: 4.h),
            _buildRow(
              "Acknowledgement",
              campDetailOutput?.ackowledgement.toString() ?? "0",
              uploadBillTitleColor,
              isLast: true,
            ),
             SizedBox(height: 20.h),
          ],
        ),
      ),
    );
  }

  Widget _buildRow(
      String label,
      String value,
      Color valueColor, {
        bool isFirst = false,
        bool isLast = false,
      }) {
    return Container(
      decoration: BoxDecoration(
        border: Border.all(color: Color(0xffDEDEDE), width: 1),
        color: kWhiteColor,
        borderRadius: isFirst
            ? BorderRadius.only(
          topLeft: Radius.circular(10),
          topRight: Radius.circular(10),
        )
            : isLast
            ? BorderRadius.only(
          bottomLeft: Radius.circular(10),
          bottomRight: Radius.circular(10),
        )
            : null,
      ),
      width: SizeConfig.screenWidth,
      constraints: BoxConstraints(minHeight: 36.h), // Minimum height instead of fixed
      child: IntrinsicHeight( // Makes children same height
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.stretch, // Stretch to fill height
          children: [
            Expanded(
              child: Container(
                padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 8.h),
                color: Colors.transparent,
                child: Text(
                  label,
                  style: TextStyle(
                    color: uploadBillTitleColor,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w500,
                    fontSize: 14.sp,
                  ),
                ),
              ),
            ),
            Container(
              width: 102.w,
              color: Colors.transparent,
              padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 8.h),
              child: Center(
                child: Text(
                  value,
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    color: valueColor,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w500,
                    fontSize: 12.sp,
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
// class ScreeningDetailsView extends StatelessWidget {
//   ScreeningDetailsView({super.key, required this.campDetailOutput});
//
//   CampDetailOutput? campDetailOutput;
//   @override
//   Widget build(BuildContext context) {
//     return Container(
//       width: MediaQuery.of(context).size.width,
//
//       decoration: BoxDecoration(
//         color: Colors.white,
//         // boxShadow: [
//         //   BoxShadow(
//         //     color: Colors.black.withValues(alpha: 0.15),
//         //     blurRadius: 10,
//         //   ),
//         // ],
//         // borderRadius: BorderRadius.circular(10),
//       ),
//       child: Padding(
//         padding: const EdgeInsets.fromLTRB(10, 6, 10, 0),
//         child: Column(
//           children: [
//             Container(
//               decoration: BoxDecoration(
//                 color: kPrimaryColor,
//                 borderRadius: BorderRadius.only(
//                   topLeft: Radius.circular(10),
//                   topRight: Radius.circular(10),
//                 ),
//               ),
//               width: SizeConfig.screenWidth,
//               height: 30,
//               child: Center(
//                 child: Text(
//                   "Screening Details",
//                   style: TextStyle(
//                     color: kWhiteColor,
//                     fontFamily: FontConstants.interFonts,
//                     fontWeight: FontWeight.w400,
//                     fontSize: responsiveFont(14),
//                   ),
//                 ),
//               ),
//             ),
//             const SizedBox(height: 4),
//             Container(
//               decoration: BoxDecoration(
//                 border: Border.all(color: Color(0xffDEDEDE), width: 1),
//                 color: kWhiteColor,
//                 borderRadius: BorderRadius.only(
//                   topLeft: Radius.circular(10),
//                   topRight: Radius.circular(10),
//                 ),
//               ),
//               width: SizeConfig.screenWidth,
//               height: 36,
//               child: Row(
//                 children: [
//                   Expanded(
//                     child: Container(
//                       padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                       color: Colors.transparent,
//                       child: Text(
//                         "Facilitated Beneficiary",
//                         style: TextStyle(
//                           color: uploadBillTitleColor,
//                           fontFamily: FontConstants.interFonts,
//                           fontWeight: FontWeight.w500,
//                           fontSize: responsiveFont(14),
//                         ),
//                       ),
//                     ),
//                   ),
//                   Container(
//                     width: 102,
//                     color: Colors.transparent,
//                     padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                     child: Text(
//                       campDetailOutput?.facilitatedWorkers.toString() ?? "",
//                       textAlign: TextAlign.center,
//                       style: TextStyle(
//                         color: uploadBillTitleColor,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w500,
//                         fontSize: responsiveFont(14),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//             const SizedBox(height: 4),
//             Container(
//               decoration: BoxDecoration(
//                 border: Border.all(color: Color(0xffDEDEDE), width: 1),
//                 color: kWhiteColor,
//               ),
//               width: SizeConfig.screenWidth,
//               height: 36,
//               child: Row(
//                 children: [
//                   Expanded(
//                     child: Container(
//                       padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                       color: Colors.transparent,
//                       child: Text(
//                         "Approved Beneficiaries",
//                         style: TextStyle(
//                           color: uploadBillTitleColor,
//                           fontFamily: FontConstants.interFonts,
//                           fontWeight: FontWeight.w500,
//                           fontSize: responsiveFont(14),
//                         ),
//                       ),
//                     ),
//                   ),
//                   Container(
//                     width: 102,
//                     color: Colors.transparent,
//                     padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                     child: Text(
//                       campDetailOutput?.approvedBeneficiaries.toString() ?? "",
//                       textAlign: TextAlign.center,
//                       style: TextStyle(
//                         color: approvedBeneficiariesColor,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w500,
//                         fontSize: responsiveFont(14),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//             const SizedBox(height: 4),
//             Container(
//               decoration: BoxDecoration(
//                 border: Border.all(color: Color(0xffDEDEDE), width: 1),
//                 color: kWhiteColor,
//               ),
//               width: SizeConfig.screenWidth,
//               height: 36,
//               child: Row(
//                 children: [
//                   Expanded(
//                     child: Container(
//                       padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                       color: Colors.transparent,
//                       child: Text(
//                         "Rejected Beneficiaries",
//                         style: TextStyle(
//                           color: uploadBillTitleColor,
//                           fontFamily: FontConstants.interFonts,
//                           fontWeight: FontWeight.w500,
//                           fontSize: responsiveFont(14),
//                         ),
//                       ),
//                     ),
//                   ),
//                   Container(
//                     width: 102,
//                     color: Colors.transparent,
//                     padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                     child: Text(
//                       campDetailOutput?.rejectedBeneficiaries.toString() ?? "",
//                       textAlign: TextAlign.center,
//                       style: TextStyle(
//                         color: rejectedBeneficiariesColor,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w500,
//                         fontSize: responsiveFont(14),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//             const SizedBox(height: 4),
//             Container(
//               decoration: BoxDecoration(
//                 border: Border.all(color: Color(0xffDEDEDE), width: 1),
//                 color: kWhiteColor,
//               ),
//               width: SizeConfig.screenWidth,
//               height: 36,
//               child: Row(
//                 children: [
//                   Expanded(
//                     child: Container(
//                       padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                       color: Colors.transparent,
//                       child: Text(
//                         "Beneficiaries Verified",
//                         style: TextStyle(
//                           color: uploadBillTitleColor,
//                           fontFamily: FontConstants.interFonts,
//                           fontWeight: FontWeight.w500,
//                           fontSize: responsiveFont(14),
//                         ),
//                       ),
//                     ),
//                   ),
//                   Container(
//                     width: 102,
//                     color: Colors.transparent,
//                     padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                     child: Text(
//                       campDetailOutput?.verifiedBeneficiaries.toString() ?? "",
//                       textAlign: TextAlign.center,
//                       style: TextStyle(
//                         color: beneficiariesVerifiedColor,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w500,
//                         fontSize: responsiveFont(14),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//             const SizedBox(height: 4),
//             Container(
//               decoration: BoxDecoration(
//                 border: Border.all(color: Color(0xffDEDEDE), width: 1),
//                 color: kWhiteColor,
//               ),
//               width: SizeConfig.screenWidth,
//               height: 36,
//               child: Row(
//                 children: [
//                   Expanded(
//                     child: Container(
//                       padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                       color: Colors.transparent,
//                       child: Text(
//                         "Basic Detail",
//                         style: TextStyle(
//                           color: uploadBillTitleColor,
//                           fontFamily: FontConstants.interFonts,
//                           fontWeight: FontWeight.w500,
//                           fontSize: responsiveFont(14),
//                         ),
//                       ),
//                     ),
//                   ),
//                   Container(
//                     width: 102,
//                     color: Colors.transparent,
//                     padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                     child: Text(
//                       campDetailOutput?.basicDetails.toString() ?? "",
//                       textAlign: TextAlign.center,
//                       style: TextStyle(
//                         color: uploadBillTitleColor,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w500,
//                         fontSize: responsiveFont(14),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//             const SizedBox(height: 4),
//             Container(
//               decoration: BoxDecoration(
//                 border: Border.all(color: Color(0xffDEDEDE), width: 1),
//                 color: kWhiteColor,
//               ),
//               width: SizeConfig.screenWidth,
//               height: 36,
//               child: Row(
//                 children: [
//                   Expanded(
//                     child: Container(
//                       padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                       color: Colors.transparent,
//                       child: Text(
//                         "Physical Examination",
//                         style: TextStyle(
//                           color: uploadBillTitleColor,
//                           fontFamily: FontConstants.interFonts,
//                           fontWeight: FontWeight.w500,
//                           fontSize: responsiveFont(14),
//                         ),
//                       ),
//                     ),
//                   ),
//                   Container(
//                     width: 102,
//                     color: Colors.transparent,
//                     padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                     child: Text(
//                       campDetailOutput?.physicalExamination.toString() ?? "",
//                       textAlign: TextAlign.center,
//                       style: TextStyle(
//                         color: uploadBillTitleColor,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w500,
//                         fontSize: responsiveFont(14),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//             const SizedBox(height: 4),
//             Container(
//               decoration: BoxDecoration(
//                 border: Border.all(color: Color(0xffDEDEDE), width: 1),
//                 color: kWhiteColor,
//               ),
//               width: SizeConfig.screenWidth,
//               height: 36,
//               child: Row(
//                 children: [
//                   Expanded(
//                     child: Container(
//                       padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                       color: Colors.transparent,
//                       child: Text(
//                         "Lung Function Test",
//                         style: TextStyle(
//                           color: uploadBillTitleColor,
//                           fontFamily: FontConstants.interFonts,
//                           fontWeight: FontWeight.w500,
//                           fontSize: responsiveFont(14),
//                         ),
//                       ),
//                     ),
//                   ),
//                   Container(
//                     width: 102,
//                     color: Colors.transparent,
//                     padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                     child: Text(
//                       campDetailOutput?.lungFunctioinTest.toString() ?? "",
//                       textAlign: TextAlign.center,
//                       style: TextStyle(
//                         color: uploadBillTitleColor,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w500,
//                         fontSize: responsiveFont(14),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//             const SizedBox(height: 4),
//             Container(
//               decoration: BoxDecoration(
//                 border: Border.all(color: Color(0xffDEDEDE), width: 1),
//                 color: kWhiteColor,
//               ),
//               width: SizeConfig.screenWidth,
//               height: 36,
//               child: Row(
//                 children: [
//                   Expanded(
//                     child: Container(
//                       padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                       color: Colors.transparent,
//                       child: Text(
//                         "Audio Screening Test",
//                         style: TextStyle(
//                           color: uploadBillTitleColor,
//                           fontFamily: FontConstants.interFonts,
//                           fontWeight: FontWeight.w500,
//                           fontSize: responsiveFont(14),
//                         ),
//                       ),
//                     ),
//                   ),
//                   Container(
//                     width: 102,
//                     color: Colors.transparent,
//                     padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                     child: Text(
//                       campDetailOutput?.audioScreeningTest.toString() ?? "",
//                       textAlign: TextAlign.center,
//                       style: TextStyle(
//                         color: uploadBillTitleColor,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w500,
//                         fontSize: responsiveFont(14),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//             const SizedBox(height: 4),
//             Container(
//               decoration: BoxDecoration(
//                 border: Border.all(color: Color(0xffDEDEDE), width: 1),
//                 color: kWhiteColor,
//               ),
//               width: SizeConfig.screenWidth,
//               height: 36,
//               child: Row(
//                 children: [
//                   Expanded(
//                     child: Container(
//                       padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                       color: Colors.transparent,
//                       child: Text(
//                         "Vision Screening Test",
//                         style: TextStyle(
//                           color: uploadBillTitleColor,
//                           fontFamily: FontConstants.interFonts,
//                           fontWeight: FontWeight.w500,
//                           fontSize: responsiveFont(14),
//                         ),
//                       ),
//                     ),
//                   ),
//                   Container(
//                     width: 102,
//                     color: Colors.transparent,
//                     padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                     child: Text(
//                       campDetailOutput?.visionScreening.toString() ?? "",
//                       textAlign: TextAlign.center,
//                       style: TextStyle(
//                         color: uploadBillTitleColor,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w500,
//                         fontSize: responsiveFont(14),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//             const SizedBox(height: 4),
//             Container(
//               decoration: BoxDecoration(
//                 border: Border.all(color: Color(0xffDEDEDE), width: 1),
//                 color: kWhiteColor,
//               ),
//               width: SizeConfig.screenWidth,
//               height: 36,
//               child: Row(
//                 children: [
//                   Expanded(
//                     child: Container(
//                       padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                       color: Colors.transparent,
//                       child: Text(
//                         "Sample Collection",
//                         style: TextStyle(
//                           color: uploadBillTitleColor,
//                           fontFamily: FontConstants.interFonts,
//                           fontWeight: FontWeight.w500,
//                           fontSize: responsiveFont(14),
//                         ),
//                       ),
//                     ),
//                   ),
//                   Container(
//                     width: 102,
//                     color: Colors.transparent,
//                     padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                     child: Text(
//                       campDetailOutput?.barcode.toString() ?? "",
//                       textAlign: TextAlign.center,
//                       style: TextStyle(
//                         color: uploadBillTitleColor,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w500,
//                         fontSize: responsiveFont(1),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//             const SizedBox(height: 4),
//             Container(
//               decoration: BoxDecoration(
//                 border: Border.all(color: Color(0xffDEDEDE), width: 1),
//                 color: kWhiteColor,
//                 borderRadius: BorderRadius.only(
//                   bottomLeft: Radius.circular(10),
//                   bottomRight: Radius.circular(10),
//                 ),
//               ),
//               width: SizeConfig.screenWidth,
//               height: 36,
//               child: Row(
//                 children: [
//                   Expanded(
//                     child: Container(
//                       padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                       color: Colors.transparent,
//                       child: Text(
//                         "Acknowledgement huigggvcgytttt tf6y5dtrdrgsrefssbn ",
//                         style: TextStyle(
//                           color: uploadBillTitleColor,
//                           fontFamily: FontConstants.interFonts,
//                           fontWeight: FontWeight.w500,
//                           fontSize: responsiveFont(14),
//                         ),
//                       ),
//                     ),
//                   ),
//                   Container(
//                     width: 102,
//                     color: Colors.transparent,
//                     padding: EdgeInsets.fromLTRB(8, 0, 8, 0),
//                     child: Text(
//                       campDetailOutput?.ackowledgement.toString() ?? "",
//                       textAlign: TextAlign.center,
//                       style: TextStyle(
//                         color: uploadBillTitleColor,
//                         fontFamily: FontConstants.interFonts,
//                         fontWeight: FontWeight.w500,
//                         fontSize: responsiveFont(14),
//                       ),
//                     ),
//                   ),
//                 ],
//               ),
//             ),
//             const SizedBox(height: 20),
//           ],
//         ),
//       ),
//     );
//   }
// }
