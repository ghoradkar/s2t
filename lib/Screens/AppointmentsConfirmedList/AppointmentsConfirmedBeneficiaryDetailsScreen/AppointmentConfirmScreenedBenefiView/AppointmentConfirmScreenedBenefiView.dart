// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import '../../../../Modules/Json_Class/AppoinmentExpectedBeneficiariesResponse/AppoinmentExpectedBeneficiariesResponse.dart';
import '../../../../Modules/Json_Class/ScreenedDependentCountResponse/ScreenedDependentCountResponse.dart';
import '../../../../Modules/ToastManager/ToastManager.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/fonts.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';

class AppointmentConfirmScreenedBenefiView extends StatefulWidget {
  AppointmentConfirmScreenedBenefiView({
    super.key,
    required this.selectedBeneficiary,
  });

  AppoinmentExpectedBeneficiariesOutput selectedBeneficiary;
  @override
  State<AppointmentConfirmScreenedBenefiView> createState() =>
      _AppointmentConfirmScreenedBenefiViewState();
}

class _AppointmentConfirmScreenedBenefiViewState
    extends State<AppointmentConfirmScreenedBenefiView> {
  bool isExpaneded = false;
  List<ScreenedDependentCountOutput> screenedDependentList = [];
  APIManager apiManager = APIManager();
  @override
  void initState() {
    super.initState();
    getScreeningCount("1");
  }

  getScreeningCount(String screeningType) {
    Map<String, String> params = {
      "AssignCallID": widget.selectedBeneficiary.assignCallID.toString(),
      "Type": screeningType,
    };
    apiManager.getScreeningCountAPI(params, apiScreeningCountBack);
  }

  void apiScreeningCountBack(
    ScreenedDependentCountResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      screenedDependentList = response?.output ?? [];
    } else {
      screenedDependentList = [];
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 10, 0, 0),
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
              padding: EdgeInsets.fromLTRB(12, 8, 12, 8),
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
                      "Screened Beneficiary",
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
            isExpaneded == true ? const SizedBox(height: 10) : Container(),

            isExpaneded == true
                ? ListView.builder(
                  itemCount: screenedDependentList.length,
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  itemBuilder: (context, index) {
                    ScreenedDependentCountOutput screenedDependent =
                        screenedDependentList[index];
                    return Padding(
                      padding: const EdgeInsets.fromLTRB(10, 0, 10, 10),
                      child: Container(
                        decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border.all(
                            width: 1,
                            color: screeningCellBorderColor,
                          ),
                          borderRadius: BorderRadius.circular(10),
                        ),
                        padding: const EdgeInsets.all(10),
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.start,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              "${screenedDependent.firstName ?? ""} ${screenedDependent.middleName ?? ""} ${screenedDependent.lastName ?? ""}",
                              style: TextStyle(
                                color: kBlackColor,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w600,
                                fontSize: responsiveFont(16),
                              ),
                            ),
                            const SizedBox(height: 5),
                            Row(
                              children: [
                                SizedBox(
                                  width: 20,
                                  height: 20,
                                  child: Image.asset(iconPersons),
                                ),
                                const SizedBox(width: 5),
                                Text(
                                  "Relation :",
                                  style: TextStyle(
                                    fontSize: 14,
                                    fontWeight: FontWeight.w500,
                                    color: Colors.black,
                                  ),
                                ),
                                const SizedBox(width: 5),
                                Text(
                                  screenedDependent.relation ?? "",
                                  style: TextStyle(
                                    fontSize: 14,
                                    fontWeight: FontWeight.w500,
                                    color: dropDownTitleHeader,
                                  ),
                                ),
                              ],
                            ),
                            const SizedBox(height: 5),
                            Row(
                              children: [
                                SizedBox(
                                  width: 20,
                                  height: 20,
                                  child: Image.asset(icCalendarMonth),
                                ),
                                const SizedBox(width: 5),
                                Text(
                                  "Last Screening Date :",
                                  style: TextStyle(
                                    fontSize: 14,
                                    fontWeight: FontWeight.w500,
                                    color: Colors.black,
                                  ),
                                ),
                                const SizedBox(width: 5),
                                Text(
                                  screenedDependent.screeningDate ?? "",
                                  style: TextStyle(
                                    fontSize: 14,
                                    fontWeight: FontWeight.w500,
                                    color: dropDownTitleHeader,
                                  ),
                                ),
                              ],
                            ),
                            const SizedBox(height: 5),
                            Row(
                              children: [
                                SizedBox(
                                  width: 20,
                                  height: 20,
                                  child: Image.asset(icCalendarMonth),
                                ),
                                const SizedBox(width: 5),
                                Text(
                                  "Age :",
                                  style: TextStyle(
                                    fontSize: 14,
                                    fontWeight: FontWeight.w500,
                                    color: Colors.black,
                                  ),
                                ),
                                const SizedBox(width: 5),
                                Text(
                                  screenedDependent.age ?? "",
                                  style: TextStyle(
                                    fontSize: 14,
                                    fontWeight: FontWeight.w500,
                                    color: dropDownTitleHeader,
                                  ),
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                )
                : Container(),
            isExpaneded == true ? const SizedBox(height: 10) : Container(),
          ],
        ),
      ),
    );
  }
}
