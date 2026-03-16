// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/Json_Class/ConfirmatoryTestsScreeningTubeResponse/ConfirmatoryTestsScreeningTubeResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/utilities/SizeConfig.dart';

class TubeDetailsView extends StatefulWidget {
  TubeDetailsView({super.key, required this.list});

  List<ConfirmatoryTestsScreeningTubeOutput> list = [];
  @override
  State<TubeDetailsView> createState() => _TubeDetailsViewState();
}

class _TubeDetailsViewState extends State<TubeDetailsView> {
  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        // borderRadius: BorderRadius.circular(10),
        // boxShadow: [
        //   BoxShadow(
        //     offset: Offset(0, 1),
        //     color: Colors.black.withValues(alpha: 0.15),
        //     spreadRadius: 0,
        //     blurRadius: 10,
        //   ),
        // ],
      ),
      child: Padding(
        padding: const EdgeInsets.all(0.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Container(
              width: SizeConfig.screenWidth,
              height: 26,
              decoration: BoxDecoration(
                color: kPrimaryColor,
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(10),
                  topRight: Radius.circular(10),
                ),
              ),
              child: Center(
                child: Text(
                  "Tube Details",
                  style: TextStyle(
                    color: Colors.white,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w700,
                    fontSize: responsiveFont(14),
                  ),
                ),
              ),
            ),
            // const SizedBox(height: 4),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Expanded(
                  child: Container(
                    width: SizeConfig.screenWidth,
                    height: 30,
                    padding: EdgeInsets.fromLTRB(10, 0, 0, 0),
                    decoration: BoxDecoration(
                      color: Color(0xffE2DFFB),
                      border: Border(
                        left: BorderSide(
                          color: kBlackColor.withValues(alpha: 0.2),
                          width: 1,
                        ),
                        top: BorderSide(
                          color: kBlackColor.withValues(alpha: 0.2),
                          width: 1,
                        ),
                        bottom: BorderSide(
                          color: kBlackColor.withValues(alpha: 0.2),
                          width: 1,
                        ),
                        right: BorderSide(
                          color: kBlackColor.withValues(alpha: 0.2),
                          width: 1,
                        ),
                      ),
                    ),
                    child: Center(
                      child: Text(
                        "Tube Name",
                        textAlign: TextAlign.center,
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w700,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                    ),
                  ),
                ),
                Expanded(
                  child: Container(
                    width: SizeConfig.screenWidth,
                    height: 30,
                    decoration: BoxDecoration(
                      color: Color(0xffE2DFFB),
                      border: Border(
                        top: BorderSide(
                          color: kBlackColor.withValues(alpha: 0.2),
                          width: 1,
                        ),
                        right: BorderSide(
                          color: kBlackColor.withValues(alpha: 0.2),
                          width: 1,
                        ),
                        bottom: BorderSide(
                          color: kBlackColor.withValues(alpha: 0.2),
                          width: 1,
                        ),
                      ),
                    ),
                    child: Center(
                      child: Text(
                        "Tube Count",
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w700,
                          fontSize: responsiveFont(14),
                        ),
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 4),
              ],
            ),
            ListView.builder(
              itemCount: widget.list.length,
              shrinkWrap: true,
              physics: NeverScrollableScrollPhysics(),
              itemBuilder: (context, index) {
                ConfirmatoryTestsScreeningTubeOutput obj = widget.list[index];
                return IntrinsicHeight(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      Expanded(
                        child: Container(
                          padding: EdgeInsets.fromLTRB(0, 4, 0, 4),
                          decoration: BoxDecoration(
                            color: kWhiteColor,
                            border: Border(
                              left: BorderSide(
                                color: kBlackColor.withValues(alpha: 0.2),
                                width: 1,
                              ),
                              top: BorderSide(
                                color: kBlackColor.withValues(alpha: 0.2),
                                width: 1,
                              ),
                              bottom: BorderSide(
                                color: kBlackColor.withValues(alpha: 0.2),
                                width: 1,
                              ),
                              right: BorderSide(
                                color: kBlackColor.withValues(alpha: 0.2),
                                width: 1,
                              ),
                            ),
                          ),
                          child: Center(
                            child: Padding(
                              padding: const EdgeInsets.fromLTRB(4, 0, 4, 0),
                              child: Text(
                                obj.tubName ?? "",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w700,
                                  fontSize: responsiveFont(14),
                                ),
                              ),
                            ),
                          ),
                        ),
                      ),
                      Expanded(
                        child: Container(
                          padding: EdgeInsets.fromLTRB(0, 4, 0, 4),
                          decoration: BoxDecoration(
                            color: kWhiteColor,
                            border: Border(
                              left: BorderSide(
                                color: kBlackColor.withValues(alpha: 0.2),
                                width: 1,
                              ),
                              top: BorderSide(
                                color: kBlackColor.withValues(alpha: 0.2),
                                width: 1,
                              ),
                              bottom: BorderSide(
                                color: kBlackColor.withValues(alpha: 0.2),
                                width: 1,
                              ),
                              right: BorderSide(
                                color: kBlackColor.withValues(alpha: 0.2),
                                width: 1,
                              ),
                            ),
                          ),
                          child: Center(
                            child: Padding(
                              padding: const EdgeInsets.fromLTRB(4, 0, 4, 0),
                              child: Text(
                                "${obj.tubCount ?? 0}",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: Colors.black,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w700,
                                  fontSize: responsiveFont(14),
                                ),
                              ),
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                );
              },
            ),
          ],
        ),
      ),
    );
  }
}
