// ignore_for_file: file_names

import 'package:flutter/material.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/utilities/SizeConfig.dart';

class BeneficiaryCampColorInfoView extends StatelessWidget {
  const BeneficiaryCampColorInfoView({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: Colors.white,
        border: Border.all(color: Colors.white, width: 1),
        borderRadius: const BorderRadius.only(
          topLeft: Radius.circular(10),
          bottomLeft: Radius.circular(10),
          topRight: Radius.circular(10),
          bottomRight: Radius.circular(10),
        ),
      ),
      padding: EdgeInsets.all(6),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                width: 20,
                height: 20,
                decoration: BoxDecoration(
                  color: rejectionIsPendingColor,
                  borderRadius: BorderRadius.circular(5),
                ),
              ),
              const SizedBox(width: 6),
              Expanded(
                child: Text(
                  "Beneficiary Approval or Rejection is Pending",
                  style: TextStyle(
                    color: Colors.black,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w400,
                    fontSize: responsiveFont(14),
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 8),
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                width: 20,
                height: 20,
                decoration: BoxDecoration(
                  color: rejectedBeneficiariesColor,
                  borderRadius: BorderRadius.circular(5),
                ),
              ),
              const SizedBox(width: 6),
              Expanded(
                child: Text(
                  "Rejected Beneficiary",
                  style: TextStyle(
                    color: Colors.black,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w400,
                    fontSize: responsiveFont(14),
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 8),
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                width: 20,
                height: 20,
                decoration: BoxDecoration(
                  color: approvedBeneficiariesColor,
                  borderRadius: BorderRadius.circular(5),
                ),
              ),
              const SizedBox(width: 6),
              Expanded(
                child: Text(
                  "Approved Beneficiary",
                  style: TextStyle(
                    color: Colors.black,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w400,
                    fontSize: responsiveFont(14),
                  ),
                ),
              ),
            ],
          ),
          const SizedBox(height: 8),
          Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                width: 20,
                height: 20,
                decoration: BoxDecoration(
                  color: beneficiariesVerifiedColor,
                  borderRadius: BorderRadius.circular(5),
                ),
              ),
              const SizedBox(width: 6),
              Expanded(
                child: Text(
                  "Re-Verified Required",
                  style: TextStyle(
                    color: Colors.black,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w400,
                    fontSize: responsiveFont(14),
                  ),
                ),
              ),
            ],
          ),
          // Row(
          //   mainAxisAlignment: MainAxisAlignment.start,
          //   crossAxisAlignment: CrossAxisAlignment.start,
          //   children: [
          //     Expanded(
          //       child: Container(
          //         padding: EdgeInsets.all(6),
          //         color: Colors.transparent,
          //         child:

          //       ),
          //     ),
          //     Expanded(
          //       child: Container(
          //         padding: EdgeInsets.all(6),
          //         color: Colors.transparent,
          //         child:

          //       ),
          //     ),
          //   ],
          // ),
        ],
      ),
    );
  }
}
