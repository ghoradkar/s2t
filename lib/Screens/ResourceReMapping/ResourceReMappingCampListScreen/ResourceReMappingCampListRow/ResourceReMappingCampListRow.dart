// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../../../../Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class ResourceReMappingCampListRow extends StatelessWidget {
  ResourceReMappingCampListRow({
    super.key,
    required this.reMappingCampOutput,
    required this.onSelectTap,
  });

  ResourceReMappingCampOutput reMappingCampOutput;
  Function() onSelectTap;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.fromLTRB(0, 0, 0, 10.h),
      child: InkWell(
        onTap: () {
          onSelectTap();
        },
        child: Container(
          decoration: BoxDecoration(
            color: Colors.white,
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.15),
                blurRadius: 4,
              ),
            ],
            borderRadius: BorderRadius.circular(10),
          ),
          padding: EdgeInsets.symmetric(vertical: 12.h, horizontal: 12.w),
          child: Column(
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  SizedBox(
                    width: responsiveHeight(22),
                    height: responsiveHeight(22),
                    child: Image.asset(icHashIcon),
                  ),
                  SizedBox(width: 8.w),
                  Expanded(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "Camp ID : ",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                        Expanded(
                          child: Text(
                            "${reMappingCampOutput.campId ?? 0}",
                            style: TextStyle(
                              color: Colors.black,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(14),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: 8.h),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  SizedBox(
                    width: responsiveHeight(22),
                    height: responsiveHeight(22),
                    child: Image.asset(icMapPin),
                  ),
                  SizedBox(width: 8.w),
                  Expanded(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "Address : ",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                        Expanded(
                          child: Text(
                            reMappingCampOutput.campLocation ?? "",
                            style: TextStyle(
                              color: dropDownTitleHeader,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: responsiveFont(14),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: 8.h),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  SizedBox(
                    width: responsiveHeight(22),
                    height: responsiveHeight(22),
                    child: Image.asset(icnTent),
                  ),
                  SizedBox(width: 8.w),
                  Expanded(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "Camp Type : ",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                        Expanded(
                          child: Text(
                            reMappingCampOutput.campTypeDescription ?? "",
                            style: TextStyle(
                              color: dropDownTitleHeader,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: responsiveFont(14),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              SizedBox(height: 8.h),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  SizedBox(
                    width: responsiveHeight(22),
                    height: responsiveHeight(22),
                    child: Image.asset(icInitiatedByIcon),
                  ),
                  SizedBox(width: 8.w),
                  Expanded(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          "Initiated By : ",
                          style: TextStyle(
                            color: Colors.black,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                        Expanded(
                          child: Text(
                            reMappingCampOutput.initiatedBy1 ?? "",
                            style: TextStyle(
                              color: dropDownTitleHeader,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: responsiveFont(14),
                            ),
                          ),
                        ),
                        SizedBox(
                          width: 26,
                          height: 26,
                          child: Image.asset(icViewIcon),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
