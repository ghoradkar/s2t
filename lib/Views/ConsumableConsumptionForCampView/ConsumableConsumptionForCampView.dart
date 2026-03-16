// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/Json_Class/ConsumableListDetailsResponse/ConsumableListDetailsResponse.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import 'ConsumableConsumptionForCampRow/ConsumableConsumptionForCampRow.dart';

class ConsumableConsumptionForCampView extends StatefulWidget {
  ConsumableConsumptionForCampView({
    super.key,
    required this.consumableCampList,
  });

  List<ConsumableOutput> consumableCampList = [];
  @override
  State<ConsumableConsumptionForCampView> createState() =>
      _ConsumableConsumptionForCampViewState();
}

class _ConsumableConsumptionForCampViewState
    extends State<ConsumableConsumptionForCampView> {
  bool isExpaneded = true;
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          border: Border.all(width: 1, color: droDownBGColor),
          borderRadius:
              isExpaneded == true
                  ? BorderRadius.only(
                    topLeft: Radius.circular(8),
                    topRight: Radius.circular(8),
                  )
                  : BorderRadius.all(Radius.circular(8)),
        ),
        child: Column(
          children: [
            Container(
              padding: EdgeInsets.fromLTRB(12, 4, 12, 4),
              decoration: BoxDecoration(
                color: kPrimaryColor,
                borderRadius:
                    isExpaneded == true
                        ? BorderRadius.only(
                          topLeft: Radius.circular(8),
                          topRight: Radius.circular(8),
                        )
                        : BorderRadius.all(Radius.circular(8)),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(
                      "Consumable Consumption for Camp",
                      style: TextStyle(
                        color: kWhiteColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w400,
                        fontSize: responsiveFont(16),
                      ),
                    ),
                  ),
                  GestureDetector(
                    onTap: () {
                      isExpaneded = !isExpaneded;
                      setState(() {});
                    },
                    child: SizedBox(
                      width: 30,
                      height: 30,
                      child: Image.asset(
                        isExpaneded == true ? icUpArrowIcon : icDownArrowIcon,
                      ),
                    ),
                  ),
                ],
              ),
            ),

            isExpaneded == true
                ? Container(
                  color: Colors.transparent,
                  child: ListView.builder(
                    itemCount: widget.consumableCampList.length,
                    shrinkWrap: true,
                    physics: NeverScrollableScrollPhysics(),
                    itemBuilder: (context, index) {
                      ConsumableOutput? consumableOutput =
                          widget.consumableCampList[index];
                      return ConsumableConsumptionForCampRow(
                        consumableOutput: consumableOutput,
                      );
                    },
                  ),
                )
                : Container(),
          ],
        ),
      ),
    );
  }
}
