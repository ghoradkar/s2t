// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import '../../../../Modules/Json_Class/AppoinmentExpectedBeneficiariesResponse/AppoinmentExpectedBeneficiariesResponse.dart';
import '../../../../Modules/Json_Class/BeneficiaryDependantDetailsResponse/BeneficiaryDependantDetailsResponse.dart';
import '../../../../Modules/ToastManager/ToastManager.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/fonts.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/widgets/AppDropdownTextfield.dart';

class ScreeningDetailsAppointConfimedView extends StatefulWidget {
  ScreeningDetailsAppointConfimedView({
    super.key,
    required this.selectedBeneficiary,
  });

  AppoinmentExpectedBeneficiariesOutput selectedBeneficiary;
  @override
  State<ScreeningDetailsAppointConfimedView> createState() =>
      _ScreeningDetailsAppointConfimedViewState();
}

class _ScreeningDetailsAppointConfimedViewState
    extends State<ScreeningDetailsAppointConfimedView> {
  bool isExpaneded = false;
  APIManager apiManager = APIManager();

  List<BeneficiaryDependantDetailsOutput> dependentList = [];

  @override
  void initState() {
    super.initState();

    getDependentListAPI();
  }

  void getDependentListAPI() {
    // Call your API to get the dependent list here
    // For example, you can use a repository or service to fetch the data
    Map<String, String> params = {
      "AssignCallID": widget.selectedBeneficiary.assignCallID.toString(),
    };
    apiManager.getDependentListAPI(params, apiDependentListBack);
  }

  void apiDependentListBack(
    BeneficiaryDependantDetailsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      dependentList = response?.output ?? [];
    } else {
      dependentList = [];
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  String dependentScreeningPending() {
    int dependantScreeningPending =
        widget.selectedBeneficiary.dependantScreeningPending ?? 0;

    return dependantScreeningPending.toString();
  }

  String relation(int relId) {
    if (relId == 7) {
      return "Son";
    } else if (relId == 8) {
      return "Daughter";
    } else if (relId == 10) {
      return "Wife";
    } else if (relId == 9) {
      return "Husband";
    } else if (relId == 1) {
      return "Father";
    } else if (relId == 2) {
      return "Mother";
    } else if (relId == 21) {
      return "Mother in law";
    } else if (relId == 22) {
      return "Father in law";
    }
    return "Other";
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
                      "Screening Details",
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
                ? Padding(
                  padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                  child: AppDropdownTextfield(
                    icon: iconPersons,
                    titleHeaderString: "No. of Dependent*",
                    valueString:
                        widget.selectedBeneficiary.noOfDependants.toString(),
                    onTap: () {},
                  ),
                )
                : Container(),
            isExpaneded == true ? const SizedBox(height: 8) : Container(),

            isExpaneded == true
                ? Padding(
                  padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                  child: AppDropdownTextfield(
                    icon: iconPersons,
                    titleHeaderString: "Dependent Screening Pending",
                    valueString: dependentScreeningPending(),
                    onTap: () {},
                  ),
                )
                : Container(),
            isExpaneded == true ? const SizedBox(height: 10) : Container(),
            isExpaneded == true
                ? ListView.builder(
                  itemCount: dependentList.length,
                  shrinkWrap: true,
                  physics: const NeverScrollableScrollPhysics(),
                  itemBuilder: (context, index) {
                    BeneficiaryDependantDetailsOutput dependant =
                        dependentList[index];
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
                            Row(
                              children: [
                                Expanded(
                                  child: Text(
                                    "${dependant.firstName ?? ""} ${dependant.middleName ?? ""} ${dependant.lastName ?? ""}",
                                    style: TextStyle(
                                      color: kBlackColor,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w600,
                                      fontSize: responsiveFont(16),
                                    ),
                                  ),
                                ),
                                const SizedBox(width: 10),
                                GestureDetector(
                                  onTap: () {
                                    // Handle delete action
                                  },
                                  child: SizedBox(
                                    width: 20,
                                    height: 20,
                                    child: Image.asset(icDelete),
                                  ),
                                ),
                                const SizedBox(width: 10),
                              ],
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
                                  relation(dependant.relId ?? 0),
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
                                  dependant.lastDependantScreeningDate ?? "",
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
                                  dependant.age ?? "",
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
          ],
        ),
      ),
    );
  }
}
