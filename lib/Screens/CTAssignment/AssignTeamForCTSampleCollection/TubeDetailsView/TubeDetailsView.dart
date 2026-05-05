// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/Json_Class/ConfirmatoryTestsScreeningTubeResponse/ConfirmatoryTestsScreeningTubeResponse.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/utilities/SizeConfig.dart';

class TubeDetailsView extends StatefulWidget {
  TubeDetailsView({
    super.key,
    required this.list,
    this.countControllers,
  });

  List<ConfirmatoryTestsScreeningTubeOutput> list = [];
  final List<TextEditingController>? countControllers;

  @override
  State<TubeDetailsView> createState() => _TubeDetailsViewState();
}

class _TubeDetailsViewState extends State<TubeDetailsView> {
  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(color: Colors.white),
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
                borderRadius: const BorderRadius.only(
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
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Expanded(
                  child: Container(
                    width: SizeConfig.screenWidth,
                    height: 30,
                    padding: const EdgeInsets.fromLTRB(10, 0, 0, 0),
                    decoration: BoxDecoration(
                      color: const Color(0xffE2DFFB),
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
                      color: const Color(0xffE2DFFB),
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
              ],
            ),
            ListView.builder(
              itemCount: widget.list.length,
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              itemBuilder: (context, index) {
                final obj = widget.list[index];
                final hasController = widget.countControllers != null &&
                    index < widget.countControllers!.length;
                return IntrinsicHeight(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      Expanded(
                        child: Container(
                          padding: const EdgeInsets.fromLTRB(0, 4, 0, 4),
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
                            child: hasController
                                ? TextField(
                                    controller:
                                        widget.countControllers![index],
                                    keyboardType: TextInputType.number,
                                    textAlign: TextAlign.center,
                                    inputFormatters: [
                                      FilteringTextInputFormatter.digitsOnly,
                                    ],
                                    decoration: InputDecoration(
                                      isDense: true,
                                      contentPadding:
                                          const EdgeInsets.symmetric(
                                        horizontal: 4,
                                        vertical: 6,
                                      ),
                                      border: InputBorder.none,
                                    ),
                                    style: TextStyle(
                                      color: Colors.black,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w700,
                                      fontSize: responsiveFont(14),
                                    ),
                                  )
                                : Padding(
                                    padding: const EdgeInsets.fromLTRB(
                                        4, 0, 4, 0),
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