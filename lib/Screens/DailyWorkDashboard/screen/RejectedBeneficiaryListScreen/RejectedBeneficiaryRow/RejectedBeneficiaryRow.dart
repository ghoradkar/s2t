// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../model/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';

class RejectedBeneficiaryRow extends StatelessWidget {
  RejectedBeneficiaryRow({super.key, required this.index, required this.obj});

  int index;
  RecollectionBeneficiaryStatusandDetailsCountV1Output obj;
  @override
  Widget build(BuildContext context) {
    return IntrinsicHeight(
      child: Padding(
        padding: const EdgeInsets.fromLTRB(0, 6, 0, 0),
        child: Container(
          decoration: BoxDecoration(
            color: kWhiteColor,
            border: Border.all(color: kTextFieldBorder, width: 1),
          ),
          height: 36,
          child: Row(
            children: [
              Container(
                width: 48,
                decoration: BoxDecoration(
                  color: Colors.transparent,
                  border: Border(
                    right: BorderSide(color: kTextFieldBorder, width: 1),
                  ),
                ),
                child: Center(
                  child: Text(
                    "${index + 1}",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w500,
                      fontSize: responsiveFont(13),
                    ),
                  ),
                ),
              ),
              Expanded(
                child: Container(
                  decoration: BoxDecoration(
                    color: Colors.transparent,
                    border: Border(
                      right: BorderSide(color: kTextFieldBorder, width: 1),
                    ),
                  ),
                  padding: EdgeInsets.fromLTRB(4, 0, 4, 0),
                  child: Center(
                    child: Text(
                      obj.beneficiaryName ?? "",
                      textAlign: TextAlign.left,
                      style: TextStyle(
                        color: kBlackColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(13),
                      ),
                    ),
                  ),
                ),
              ),
              Container(
                width: 68,
                decoration: BoxDecoration(
                  color: Colors.transparent,
                  border: Border(
                    right: BorderSide(color: kTextFieldBorder, width: 1),
                  ),
                ),
                child: Center(
                  child: Text(
                    obj.area ?? "",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w500,
                      fontSize: responsiveFont(13),
                    ),
                  ),
                ),
              ),
              Container(
                width: 84,
                color: Colors.transparent,
                child: Center(
                  child: Text(
                    obj.pincode ?? "",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w500,
                      fontSize: responsiveFont(13),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
