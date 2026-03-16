// ignore_for_file: must_be_immutable
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/D2DPhysicalExamninationDetailsResponse/D2DPhysicalExamninationDetailsResponse.dart';
import 'package:s2toperational/Modules/utilities/validators.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../../Modules/Enums/Enums.dart';
import '../../../../../Modules/Json_Class/MonthsResponse/MonthsResponse.dart';
import '../../../../../Modules/Json_Class/YearsResponse/YearsResponse.dart';
import '../../../../../Modules/constants/constants.dart';
import '../../../../../Modules/constants/images.dart';
import '../../../../../Views/DropDownListScreen/DropDownListScreen.dart';

class LiverExaminationHistoryCard extends StatefulWidget {
  String titleString;
  Function(bool) selectedRadioYesChange;
  Function(YearsOutput?) selectedYearChange;
  Function(MonthsOutput?) selectedMonthChange;
  Function(String) descriptionTextFieldChange;
  final bool? isUnknown;
  Function? selectedUnknown;
  final TextEditingController? descriptionController;

  LiverExaminationHistoryCard({
    super.key,
    required this.titleString,
    required this.selectedRadioYesChange,
    required this.selectedYearChange,
    required this.selectedMonthChange,
    required this.descriptionTextFieldChange,
    this.isUnknown,
    this.selectedUnknown,
    this.descriptionController,
  });

  @override
  State<LiverExaminationHistoryCard> createState() =>
      _LiverExaminationHistoryCardState();
}

class _LiverExaminationHistoryCardState
    extends State<LiverExaminationHistoryCard> {
  List<YearsOutput> yearsList = [];
  List<MonthsOutput> monthsList = [];

  /// yes / no / unknown
  String selectedOption = ""; // "yes" OR "no" OR "unknown"

  YearsOutput? selectedYear;
  MonthsOutput? selectedMonth;

  TextEditingController descriptionTextField = TextEditingController();
  TextEditingController yearTextController = TextEditingController();
  TextEditingController monthTextController = TextEditingController();

  @override
  void initState() {
    super.initState();

    yearsList = [
      YearsOutput(yearID: 0, yearName: "0"),
      YearsOutput(yearID: 1, yearName: "1"),
      YearsOutput(yearID: 2, yearName: "2"),
      YearsOutput(yearID: 3, yearName: "3"),
      YearsOutput(yearID: 4, yearName: "4"),
      YearsOutput(yearID: 5, yearName: "5"),
      YearsOutput(yearID: 6, yearName: "More than five"),
    ];

    monthsList = List.generate(
      11,
      (index) => MonthsOutput(monthId: index + 1, monthNameEng: "${index + 1}"),
    );
  }



  void selectRadio(String value) {
    selectedOption = value;

    if (value == "yes") {
      widget.selectedRadioYesChange(true);
    } else {
      // CLEAR UI VALUES HERE
      selectedYear = null;
      selectedMonth = null;
      yearTextController.text = "";
      monthTextController.text = "";

      // send NULL to parent
      widget.selectedYearChange(null);
      widget.selectedMonthChange(null);

      // clear description
      (widget.descriptionController ?? descriptionTextField).clear();

      if (value == "unknown" && widget.selectedUnknown != null) {
        widget.selectedUnknown!(true);
      } else {
        widget.selectedRadioYesChange(false);
      }
    }

    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        border: Border.all(color: kTextColor.withValues(alpha: 0.2)),
        borderRadius: BorderRadius.all(Radius.circular(12)),
      ),
      padding: EdgeInsets.symmetric(vertical: 8.h, horizontal: 8.w),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          FormatterManager.buildLabelWithAsterisk(widget.titleString),
          SizedBox(height: 10.h),

          /// ---------- YES / NO / UNKNOWN ----------
          Row(
            children: [
              buildRadio("yes", "Yes"),
              buildRadio("no", "No"),
              if (widget.isUnknown == true) buildRadio("unknown", "Unknown"),
            ],
          ),

          /// ---------- TEXT FIELD & YEAR / MONTH ----------
          Visibility(
            visible: selectedOption == "yes",
            child: Column(
              children: [
                Row(
                  children: [
                    Expanded(
                      child: AppTextField(
                        validators: UIValidator.validateYear,
                        autovalidateMode: AutovalidateMode.onUserInteraction,
                        readOnly: true,
                        controller: yearTextController,
                        onTap: () {
                          showDropDownBottomSheet(
                            "Year",
                            yearsList,
                            DropDownTypeMenu.Years,
                          );
                        },
                        hint: 'Year',
                        suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                        label: CommonText(
                          text: 'Year',
                          fontSize: 14.sp * 1.33,
                          fontWeight: FontWeight.w600,
                          textColor: kBlackColor,
                          textAlign: TextAlign.start,
                        ),
                        hintStyle: TextStyle(
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w400,
                          fontFamily: FontConstants.interFonts,
                        ),
                        fieldRadius: 10,
                        prefixIcon: SizedBox(
                          height: 22.h,
                          width: 22.w,
                          child: Center(
                            child: Image.asset(
                              icCalendarMonth,
                              height: 22.h,
                              width: 22.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ),

                    ),
                    SizedBox(width: 8.w),
                    Expanded(
                      child: AppTextField(
                        validators: UIValidator.validateMonth,
                        autovalidateMode: AutovalidateMode.onUserInteraction,
                        readOnly: true,
                        controller: monthTextController,
                        onTap: () {
                          if (selectedYear?.yearID == 0) {
                            showDropDownBottomSheet(
                              "Month",
                              monthsList,
                              DropDownTypeMenu.Months,
                            );
                          }
                        },
                        hint: 'Month',
                        label: CommonText(
                          text: 'Month',
                          fontSize: 14.sp * 1.33,
                          fontWeight: FontWeight.w600,
                          textColor: kBlackColor,
                          textAlign: TextAlign.start,
                        ),
                        hintStyle: TextStyle(
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w400,
                          fontFamily: FontConstants.interFonts,
                        ),
                        fieldRadius: 10,
                        prefixIcon: SizedBox(
                          height: 22.h,
                          width: 22.w,
                          child: Center(
                            child: Image.asset(
                              icCalendarMonth,
                              height: 22.h,
                              width: 22.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                        suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                      ),

                    ),
                  ],
                ),
                SizedBox(height: 8.h),
                AppTextField(
                  validators: UIValidator.validateDescription,
                  autovalidateMode: AutovalidateMode.onUserInteraction,
                  readOnly: false,
                  controller:
                      widget.descriptionController ?? descriptionTextField,
                  onChange: (p0) {
                    widget.descriptionTextFieldChange(p0);
                  },
                  hint: 'Description',
                  label: CommonText(
                    text: 'Description',
                    fontSize: 14.sp * 1.33,
                    fontWeight: FontWeight.w600,
                    textColor: kBlackColor,
                    textAlign: TextAlign.start,
                  ),
                  hintStyle: TextStyle(
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w400,
                    fontFamily: FontConstants.interFonts,
                  ),
                  fieldRadius: 10,
                  prefixIcon: SizedBox(
                    height: 22.h,
                    width: 22.w,
                    child: Center(
                      child: Image.asset(
                        icnTent,
                        height: 22.h,
                        width: 22.w,
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                  // suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
                ),

              ],
            ).paddingOnly(top: 6.h),
          ),
        ],
      ),
    ).paddingSymmetric(vertical: 8.h);
  }

  void showDropDownBottomSheet(
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.33,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: DropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: (p0) {
              if (dropDownType == DropDownTypeMenu.Years) {
                selectedYear = p0;
                yearTextController.text = selectedYear?.yearName ?? "";
                widget.selectedYearChange(selectedYear);

                if (selectedYear?.yearID != 0) {
                  selectedMonth = MonthsOutput(monthId: 0, monthNameEng: "0");
                  monthTextController.text = selectedMonth?.monthNameEng ?? "";
                } else {
                  selectedMonth = null;
                  monthTextController.text = "";
                }
                widget.selectedMonthChange(selectedMonth);
              } else if (dropDownType == DropDownTypeMenu.Months) {
                selectedMonth = p0;
                monthTextController.text = selectedMonth?.monthNameEng ?? "";
                widget.selectedMonthChange(selectedMonth);
              }
              resetYears();
              resetMonths();
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  void resetYears() {
    for (YearsOutput element in yearsList) {
      element.isSelected = false;
    }
  }

  void resetMonths() {
    for (MonthsOutput element in monthsList) {
      element.isSelected = false;
    }
  }

  /// ---------- UI RADIO WIDGET ----------
  Widget buildRadio(String value, String label) {
    return Flexible(
      flex: 1,
      child: EnableDisableButton(
        buttontitle: label,
        onTap: () {
          selectRadio(value);
        },
        isYesButton: label,
        isButtonSelected: selectedOption == value ? true : false,
        width: 120.w,
      ),

      // GestureDetector(
      //   onTap: () => selectRadio(value),
      //   child: Row(
      //     children: [
      //       // SizedBox(width: 8),
      //       GestureDetector(
      //         child: SizedBox(
      //           width: 26.w,
      //           height: 26.h,
      //           child: Image.asset(
      //             selectedOption == value ? icRadioSelected : icUnRadioSelected,
      //           ),
      //         ),
      //       ),
      //       SizedBox(width: 8.w),
      //
      //       Text(
      //         label,
      //         style: TextStyle(
      //           color: kBlackColor,
      //           fontFamily: FontConstants.interFonts,
      //           fontWeight: FontWeight.w400,
      //           fontSize: 16.sp,
      //         ),
      //       ),
      //     ],
      //   ),
      // ),
    );
  }
}

class LiverExaminationHistoryCardDefaultSelected extends StatefulWidget {
  String titleString;
  String value;
  final bool selectedRadioYesChange;
  final D2DPhysicalExamninationDetailsOutput? patientObj;

  LiverExaminationHistoryCardDefaultSelected({
    super.key,
    required this.titleString,
    required this.value,
    required this.selectedRadioYesChange,
    this.patientObj,
  });

  @override
  State<LiverExaminationHistoryCardDefaultSelected> createState() =>
      _LiverExaminationHistoryCardDefaultSelectedState();
}

class _LiverExaminationHistoryCardDefaultSelectedState
    extends State<LiverExaminationHistoryCardDefaultSelected> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.transparent,
        border: Border.all(color: kTextColor.withValues(alpha: 0.2)),
        borderRadius: BorderRadius.all(Radius.circular(12)),
      ),
      padding: EdgeInsets.symmetric(vertical: 8.h, horizontal: 8.h),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          FormatterManager.buildLabelWithAsterisk(widget.titleString),
          SizedBox(height: 10.h),

          /// ---------- YES / NO ----------
          Row(
            children: [
              Flexible(
                flex: 1,
                child: EnableDisableButton(
                  buttontitle: 'Yes',
                  onTap: () {},
                  isYesButton: "Yes",
                  isButtonSelected:
                      widget.selectedRadioYesChange == true ? true : false,
                  width: 120.w,
                ),

                // GestureDetector(
                //   // onTap: () => selectRadio(value),
                //   child: Row(
                //     children: [
                //       // SizedBox(width: 8),
                //       GestureDetector(
                //         child: SizedBox(
                //           width: 26.w,
                //           height: 26.h,
                //           child: Image.asset(
                //             widget.selectedRadioYesChange == true
                //                 ? icRadioSelected
                //                 : icUnRadioSelected,
                //           ),
                //         ),
                //       ),
                //       SizedBox(width: 8.w),
                //
                //       Text(
                //         "Yes",
                //         style: TextStyle(
                //           color: kBlackColor,
                //           fontFamily: FontConstants.interFonts,
                //           fontWeight: FontWeight.w400,
                //           fontSize: 16.sp,
                //         ),
                //       ),
                //     ],
                //   ),
                // ),
              ),
              Flexible(
                flex: 1,
                child: EnableDisableButton(
                  buttontitle: 'No',
                  onTap: () {},
                  isYesButton: "No",
                  isButtonSelected:
                      widget.selectedRadioYesChange == false ? true : false,
                  width: 120.w,
                ),
                // GestureDetector(
                //   // onTap: () => selectRadio(value),
                //   child: Row(
                //     children: [
                //       // SizedBox(width: 8),
                //       GestureDetector(
                //         child: SizedBox(
                //           width: 26.w,
                //           height: 26.h,
                //           child: Image.asset(
                //             widget.selectedRadioYesChange == false
                //                 ? icRadioSelected
                //                 : icUnRadioSelected,
                //           ),
                //         ),
                //       ),
                //       SizedBox(width: 8.w),
                //
                //       Text(
                //         "No",
                //         style: TextStyle(
                //           color: kBlackColor,
                //           fontFamily: FontConstants.interFonts,
                //           fontWeight: FontWeight.w400,
                //           fontSize: 16.sp,
                //         ),
                //       ),
                //     ],
                //   ),
                // ),
              ),
            ],
          ),

          Align(
            alignment: Alignment.centerLeft,
            child: Text(
              "${widget.value} : ${widget.value == "BMI" ? widget.patientObj?.bMI : widget.patientObj?.bloodSugarR}",
              style: TextStyle(color: kPrimaryColor),
              textAlign: TextAlign.left,
            ).paddingOnly(left: 6.w, top: 2.h, bottom: 2.h),
          ),
        ],
      ),
    );
  }
}
