// ignore_for_file: file_names, must_be_immutable, avoid_print

import 'package:flutter/material.dart';

import 'package:s2toperational/Screens/health_screening_details/models/camp_closing_model.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../../../Modules/constants/fonts.dart';

class ConsumableConsumptionForCampRow extends StatefulWidget {
  ConsumableConsumptionForCampRow({super.key, required this.consumableOutput});

  ConsumableOutput? consumableOutput;
  @override
  State<ConsumableConsumptionForCampRow> createState() =>
      _ConsumableConsumptionForCampRowState();
}

class _ConsumableConsumptionForCampRowState
    extends State<ConsumableConsumptionForCampRow> {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
          child: Column(
            children: [
              Container(
                height: 52,
                decoration: const BoxDecoration(color: Colors.transparent),
                child: Row(
                  children: [
                    // LEFT CELL
                    Expanded(
                      child: Container(
                        alignment: Alignment.centerLeft,
                        padding: const EdgeInsets.symmetric(horizontal: 10),
                        decoration: const BoxDecoration(
                          color: Colors.white,
                          border: Border(
                            left: BorderSide(color: Colors.grey, width: 0.5),
                            // top: BorderSide(
                            //   color: Colors.grey,
                            //   width: 0.5,
                            // ),
                            bottom: BorderSide(color: Colors.grey, width: 0.5),
                          ),
                        ),
                        child: Text(
                          widget.consumableOutput?.consumableName ?? "",
                          style: TextStyle(
                            color: uploadBillTitleColor,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(16),
                          ),
                        ),
                      ),
                    ),
                    // RIGHT CELL
                    Expanded(
                      child: Container(
                        alignment: Alignment.center,
                        decoration: const BoxDecoration(
                          color: Colors.white,
                          border: Border(
                            left: BorderSide(color: Colors.grey, width: 0.5),
                            right: BorderSide(color: Colors.grey, width: 0.5),
                            // top: BorderSide(
                            //   color: Colors.grey,
                            //   width: 0.5,
                            // ),
                            bottom: BorderSide(color: Colors.grey, width: 0.5),
                          ),
                        ),
                        padding: const EdgeInsets.symmetric(horizontal: 20),
                        child: TextField(
                          textAlign: TextAlign.center,
                          readOnly: true,
                          controller:
                              widget.consumableOutput?.textEditingController,
                          keyboardType: TextInputType.number,
                          style: TextStyle(
                            color: kBlackColor,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w500,
                            fontSize: responsiveFont(16),
                          ),
                          decoration: InputDecoration(
                            isDense: true, // ✅ tighter but balanced height
                            contentPadding: const EdgeInsets.symmetric(
                              vertical: 10,
                              horizontal: 8,
                            ), // ✅ clean inner padding
                            filled: true,
                            fillColor: Colors.white,
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(8),
                              borderSide: BorderSide.none,
                            ),
                            enabledBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(8),
                              borderSide: BorderSide(
                                color: Colors.grey,
                                width: 0.5,
                              ),
                            ),
                            focusedBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(8),
                              borderSide: BorderSide.none,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
        // Padding(
        //   padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
        //   child: Column(
        //     children: [
        //       Container(
        //         height: 60,
        //         decoration: BoxDecoration(
        //           color: Colors.white,
        //           border: Border.all(width: 1, color: droDownBGColor),
        //         ),
        //         child: Row(
        //           children: [
        //             Expanded(
        //               child: Container(
        //                 alignment: Alignment.centerLeft,
        //                 padding: EdgeInsets.fromLTRB(10, 0, 0, 0),
        //                 decoration: BoxDecoration(color: Colors.white),
        //                 child: Text(
        //                   widget.consumableOutput?.consumableName ?? "",
        //                   style: TextStyle(
        //                     color: kBlackColor,
        //                     fontFamily: FontConstants.interFonts,
        //                     fontWeight: FontWeight.w500,
        //                     fontSize: responsiveFont(16),
        //                   ),
        //                 ),
        //               ),
        //             ),
        //             Expanded(
        //               child: Container(
        //                 alignment: Alignment.centerLeft,
        //                 decoration: BoxDecoration(
        //                   color: Colors.white,
        //                   borderRadius: BorderRadius.only(
        //                     topRight: Radius.circular(8),
        //                   ),
        //                   border: Border(
        //                     left: BorderSide(color: Colors.grey, width: 0.5),
        //                   ),
        //                 ),
        //                 child: Padding(
        //                   padding: const EdgeInsets.fromLTRB(8, 4, 8, 4),
        //                   child: Container(
        //                     decoration: BoxDecoration(
        //                       color: Colors.white,
        //                       border: Border.all(
        //                         width: 1,
        //                         color: droDownBGColor,
        //                       ),
        //                       borderRadius: BorderRadius.circular(8),
        //                     ),
        //                     child: Padding(
        //                       padding: const EdgeInsets.all(2.0),
        //                       child: TextField(
        //                         textAlign: TextAlign.center,
        //                         style: TextStyle(
        //                           color: kBlackColor,
        //                           fontFamily: FontConstants.interFonts,
        //                           fontWeight: FontWeight.w500,
        //                           fontSize: responsiveFont(16),
        //                         ),
        //                         maxLength: 3,
        //                         controller:
        //                             widget
        //                                 .consumableOutput
        //                                 ?.textEditingController,
        //                         keyboardType: TextInputType.number,
        //                         decoration: InputDecoration(
        //                           counterText: "",
        //                           border: OutlineInputBorder(),
        //                           focusedBorder: InputBorder.none,
        //                         ),
        //                         onChanged: (text) {
        //                           // Handle text changes here
        //                           if (text.isEmpty) {
        //                             widget.consumableOutput?.totalCount = 0;
        //                           } else {
        //                             widget
        //                                 .consumableOutput
        //                                 ?.totalCount = int.parse(text);
        //                           }
        //                           print('Entered text: $text');
        //                         },
        //                       ),
        //                     ),
        //                   ),
        //                 ),
        //               ),
        //             ),
        //           ],
        //         ),
        //       ),
        //       const SizedBox(height: 10),
        //     ],
        //   ),
        // ),
      ],
    );
  }
}
