// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/constants/constants.dart';

class CampDetailsSegmentView extends StatefulWidget {
  CampDetailsSegmentView({super.key, required this.onChangedTap});

  Function(int) onChangedTap;

  @override
  State<CampDetailsSegmentView> createState() => _CampDetailsSegmentViewState();
}

class _CampDetailsSegmentViewState extends State<CampDetailsSegmentView> {
  bool isScreeningTest = true;
  bool isBeneficiary = false;
  bool isPatientStatus = false;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: MediaQuery.of(context).size.width,
      height: 50,
      decoration: BoxDecoration(
        // color: Colors.grey[400],
        border: Border.all(color: Colors.grey[300]!, width: 1),
        borderRadius: const BorderRadius.only(
          topLeft: Radius.circular(20),
          bottomLeft: Radius.circular(20),
          topRight: Radius.circular(20),
          bottomRight: Radius.circular(20),
        ),
      ),
      child: Row(
        children: [
          Expanded(
            child: GestureDetector(
              onTap: () {
                isScreeningTest = true;
                isBeneficiary = false;
                isPatientStatus = false;
                widget.onChangedTap(0);
                setState(() {});
              },
              child: Container(
                decoration: BoxDecoration(
                  color:
                      isScreeningTest == true ? kPrimaryColor : kWhiteColor,
                  borderRadius: const BorderRadius.only(
                    topLeft: Radius.circular(20),
                    bottomLeft: Radius.circular(20),
                  ),
                  border: Border(right: BorderSide(color: Colors.grey[300]!)),
                ),
                child: Center(
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        "Screening Test",
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          color:
                              isScreeningTest == false
                                  ? Colors.black
                                  : Colors.white,
                          fontSize: responsiveFont(13),
                          fontWeight: FontWeight.w500,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
          Expanded(
            child: GestureDetector(
              onTap: () {
                isScreeningTest = false;
                isBeneficiary = true;
                isPatientStatus = false;
                widget.onChangedTap(1);
                setState(() {});
              },
              child: Container(
                decoration: BoxDecoration(
                  color: isBeneficiary == true ? kPrimaryColor : kWhiteColor,
                  border: Border.symmetric(
                    vertical: BorderSide(width: 1, color: Colors.grey[300]!),
                  ),
                ),
                child: Center(
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        "Beneficiary",
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          color:
                              isBeneficiary == false
                                  ? Colors.black
                                  : Colors.white,
                          fontSize: responsiveFont(14),
                          fontWeight: FontWeight.normal,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
          Expanded(
            child: GestureDetector(
              onTap: () {
                isScreeningTest = false;
                isBeneficiary = false;
                isPatientStatus = true;
                widget.onChangedTap(2);
                setState(() {});
              },
              child: Container(
                decoration: BoxDecoration(
                  color:
                      isPatientStatus == true ? kPrimaryColor : kWhiteColor,
                  borderRadius: const BorderRadius.only(
                    topRight: Radius.circular(20),
                    bottomRight: Radius.circular(20),
                  ),
                ),
                child: Center(
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        "Patient Status",
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          color:
                              isPatientStatus == false
                                  ? Colors.black
                                  : Colors.white,
                          fontSize: responsiveFont(14),
                          fontWeight: FontWeight.normal,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
