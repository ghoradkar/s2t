// ignore_for_file: must_be_immutable, file_names
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/utilities/validators.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../../Modules/Enums/Enums.dart';
import '../../../../../Modules/Json_Class/MonthsResponse/MonthsResponse.dart';
import '../../../../../Modules/Json_Class/YearsResponse/YearsResponse.dart';
import '../../../../../Modules/PhysicalExaminationFormDataManager/PhysicalExaminationFormDataManager.dart';
import '../../../../../Modules/constants/constants.dart';
import '../../../../../Modules/constants/images.dart';
import '../../../../../Views/DropDownListScreen/DropDownListScreen.dart';

class MedicalHistoryCard extends StatefulWidget {
  MedicalHistoryCard({
    super.key,
    required this.titleString,
    required this.selectedRadioYesChange,
    required this.selectedYearChange,
    required this.selectedMonthChange,
    required this.descriptionTextFieldChange,
    required this.showStar,
    required this.noHistoryChange,
    required this.isNoHistory,
  });

  String titleString;
  bool showStar;
  Function(bool) selectedRadioYesChange;
  Function(YearsOutput?) selectedYearChange;
  Function(MonthsOutput?) selectedMonthChange;
  Function(String) descriptionTextFieldChange;
  Function(bool) noHistoryChange;
  bool isNoHistory = false;

  @override
  State<MedicalHistoryCard> createState() => _MedicalHistoryCardState();
}

class _MedicalHistoryCardState extends State<MedicalHistoryCard> {
  List<YearsOutput> yearsList = [];
  List<MonthsOutput> monthsList = [];
  bool isSelectedRadioYes = false;
  YearsOutput? selectedYear;
  MonthsOutput? selectedMonth;
  TextEditingController descriptionTextField = TextEditingController();
  TextEditingController yearTextController = TextEditingController();
  TextEditingController monthTextController = TextEditingController();
  PhysicalExaminationFormDataManager physicalExaminationFormDataManager =
      PhysicalExaminationFormDataManager();

  @override
  void initState() {
    super.initState();
    yearsList.add(YearsOutput(yearID: 0, yearName: "0"));
    yearsList.add(YearsOutput(yearID: 1, yearName: "1"));
    yearsList.add(YearsOutput(yearID: 2, yearName: "2"));
    yearsList.add(YearsOutput(yearID: 3, yearName: "3"));
    yearsList.add(YearsOutput(yearID: 4, yearName: "4"));
    yearsList.add(YearsOutput(yearID: 5, yearName: "5"));
    yearsList.add(YearsOutput(yearID: 6, yearName: "More than five"));

    monthsList.add(MonthsOutput(monthId: 1, monthNameEng: "1"));
    monthsList.add(MonthsOutput(monthId: 2, monthNameEng: "2"));
    monthsList.add(MonthsOutput(monthId: 3, monthNameEng: "3"));
    monthsList.add(MonthsOutput(monthId: 4, monthNameEng: "4"));
    monthsList.add(MonthsOutput(monthId: 5, monthNameEng: "5"));
    monthsList.add(MonthsOutput(monthId: 6, monthNameEng: "6"));
    monthsList.add(MonthsOutput(monthId: 7, monthNameEng: "7"));
    monthsList.add(MonthsOutput(monthId: 8, monthNameEng: "8"));
    monthsList.add(MonthsOutput(monthId: 9, monthNameEng: "9"));
    monthsList.add(MonthsOutput(monthId: 10, monthNameEng: "10"));
    monthsList.add(MonthsOutput(monthId: 11, monthNameEng: "11"));
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

  @override
  Widget build(BuildContext context) {
    if (widget.isNoHistory) {
      isSelectedRadioYes = false;
      selectedYear = null;
      selectedMonth = null;
      descriptionTextField.text = "";
      yearTextController.text = "";
      monthTextController.text = "";
    }

    return Container(
      padding: EdgeInsets.symmetric(vertical: 10.h, horizontal: 10.w),
      decoration: BoxDecoration(
        border: Border(
          bottom: BorderSide(
            color: kTextColor.withValues(alpha: 0.08),
            width: 1.5,
          ),
        ),
        // borderRadius: BorderRadius.all(Radius.circular(12)),
      ),
      child: Column(
        children: [
          Row(
            children: [
              Expanded(
                child: RichText(
                  text: TextSpan(
                    text: widget.titleString,
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w600,
                      fontSize: 14.sp,
                    ),
                    children: <TextSpan>[
                      widget.showStar == true
                          ? TextSpan(
                            text: ' *',
                            style: TextStyle(
                              color: Colors.red,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w400,
                              fontSize: 16.sp,
                            ),
                          )
                          : TextSpan(text: ""),
                    ],
                  ),
                ),
              ),
              SizedBox(width: 8.w),

              EnableDisableButton(
                buttontitle: 'Yes',
                onTap: () {
                  if (!widget.isNoHistory) {
                    isSelectedRadioYes = true;
                    widget.selectedRadioYesChange(isSelectedRadioYes);
                  }

                  setState(() {});
                },
                isYesButton: "Yes",
                isButtonSelected: isSelectedRadioYes == true ? true : false,
                width: 70.w,
              ),

              SizedBox(width: 20.w),

              EnableDisableButton(
                buttontitle: 'No',
                onTap: () {
                  isSelectedRadioYes = false;
                  selectedYear = null;
                  selectedMonth = null;
                  descriptionTextField.text = "";
                  yearTextController.text = "";
                  monthTextController.text = "";
                  widget.selectedRadioYesChange(isSelectedRadioYes);
                  widget.selectedYearChange(selectedYear);
                  widget.selectedMonthChange(selectedMonth);
                  widget.descriptionTextFieldChange(descriptionTextField.text);
                  setState(() {});
                },
                isYesButton: "No",
                isButtonSelected: isSelectedRadioYes == false ? true : false,
                width: 70.w,
              ),
            ],
          ),
          isSelectedRadioYes == true ? SizedBox(height: 10.h) : Container(),
          isSelectedRadioYes == true
              ? Row(
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
              )
              : Container(),
          isSelectedRadioYes == true ? SizedBox(height: 10.h) : Container(),
          isSelectedRadioYes == true
              ? AppTextField(
                validators: UIValidator.validateDescription,
                autovalidateMode: AutovalidateMode.onUserInteraction,
                readOnly: false,
                controller: descriptionTextField,
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
              )
              : Container(),
        ],
      ),
    ).paddingSymmetric(vertical: 6.h);
  }
}
