// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../Modules/Json_Class/CampReadinessFormListResponse/CampReadinessFormListResponse.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';

class CampReadinessFormRow extends StatefulWidget {
  CampReadinessFormRow({
    super.key,
    required this.index,
    required this.object,
    required this.isFormSubmitted,
  });

  CampReadinessFormOutput object;
  int index;
  bool isFormSubmitted;

  @override
  State<CampReadinessFormRow> createState() => _CampReadinessFormRowState();
}

class _CampReadinessFormRowState extends State<CampReadinessFormRow> {
  @override
  Widget build(BuildContext context) {
    if (widget.object.itemStatus == 1) {
      widget.object.isAvailableSelected = true;
      widget.object.isNotAvailableSelected = false;
      widget.object.isNotWorkingSelected = false;
    } else if (widget.object.itemStatus == 2) {
      widget.object.isAvailableSelected = false;
      widget.object.isNotAvailableSelected = true;
      widget.object.isNotWorkingSelected = false;
    } else if (widget.object.itemStatus == 3) {
      widget.object.isAvailableSelected = false;
      widget.object.isNotAvailableSelected = false;
      widget.object.isNotWorkingSelected = true;
    }

    return IntrinsicHeight(
      child: Stack(
        children: [
          Padding(
            padding: const EdgeInsets.symmetric(vertical: 8.0, horizontal: 8),
            child: Container(
              decoration: BoxDecoration(
                color: Colors.white,
                boxShadow: [
                  BoxShadow(
                    blurRadius: 4,
                    spreadRadius: 0,
                    offset: const Offset(0, 1),
                    color: const Color(0xFF000000).withValues(alpha: 0.15),
                  ),
                ],
                borderRadius: BorderRadius.circular(responsiveHeight(10)),
              ),
              padding: EdgeInsets.all(10),
              child: Column(
                children: [
                  SizedBox(height: 4),
                  Row(
                    children: [
                      const SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          widget.object.itemName ?? "",
                          style: TextStyle(
                            color: kBlackColor,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w600,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 6),
                  Row(
                    children: [
                      Expanded(
                        child: Container(
                          color: Colors.white,
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              const SizedBox(height: 6),
                              GestureDetector(
                                onTap: () {
                                  if (!widget.isFormSubmitted) {
                                    widget.object.isAvailableSelected = true;
                                    widget.object.isNotAvailableSelected =
                                        false;
                                    widget.object.isNotWorkingSelected = false;
                                    widget.object.itemStatus = 1;
                                    setState(() {});
                                  }
                                },
                                child: SizedBox(
                                  width: responsiveHeight(26),
                                  height: responsiveHeight(26),
                                  child: Image.asset(
                                    widget.object.isAvailableSelected == true
                                        ? icRadioSelected
                                        : icUnRadioSelected,
                                  ),
                                ),
                              ),
                              const SizedBox(height: 6),
                              Text(
                                "Available",
                                style: TextStyle(
                                  color: kBlackColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.normal,
                                  fontSize: responsiveFont(14),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                      Expanded(
                        child: Container(
                          color: Colors.white,
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              const SizedBox(height: 6),
                              GestureDetector(
                                onTap: () {
                                  if (!widget.isFormSubmitted) {
                                    widget.object.isAvailableSelected = false;
                                    widget.object.isNotAvailableSelected = true;
                                    widget.object.isNotWorkingSelected = false;
                                    widget.object.itemStatus = 2;
                                  }

                                  setState(() {});
                                },
                                child: SizedBox(
                                  width: responsiveHeight(26),
                                  height: responsiveHeight(26),
                                  child: Image.asset(
                                    widget.object.isNotAvailableSelected == true
                                        ? icRadioSelected
                                        : icUnRadioSelected,
                                  ),
                                ),
                              ),
                              const SizedBox(height: 6),
                              Text(
                                "Not Available",
                                style: TextStyle(
                                  color: kBlackColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.normal,
                                  fontSize: responsiveFont(14),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                      Expanded(
                        child: Container(
                          color: Colors.white,
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              const SizedBox(height: 6),
                              GestureDetector(
                                onTap: () {
                                  if (!widget.isFormSubmitted) {
                                    widget.object.isAvailableSelected = false;
                                    widget.object.isNotAvailableSelected =
                                        false;
                                    widget.object.isNotWorkingSelected = true;
                                    widget.object.itemStatus = 3;
                                    setState(() {});
                                  }
                                },
                                child: SizedBox(
                                  width: responsiveHeight(26),
                                  height: responsiveHeight(26),
                                  child: Image.asset(
                                    widget.object.isNotWorkingSelected == true
                                        ? icRadioSelected
                                        : icUnRadioSelected,
                                  ),
                                ),
                              ),
                              const SizedBox(height: 6),
                              Text(
                                "Not Working",
                                style: TextStyle(
                                  color: kBlackColor,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.normal,
                                  fontSize: responsiveFont(14),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
          Positioned(
            top: 0,
            left: 16,
            child: Container(
              width: responsiveHeight(24),
              height: responsiveHeight(24),
              decoration: BoxDecoration(
                color: kPrimaryColor,
                borderRadius: BorderRadius.circular(50),
              ),
              child: Center(
                child: Text(
                  "${widget.index + 1}",
                  style: TextStyle(
                    color: Colors.white,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w600,
                    fontSize: responsiveFont(12),
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
