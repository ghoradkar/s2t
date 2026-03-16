// ignore_for_file: prefer_conditional_assignment, must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/AppDataManager/AppDataManager.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/APIManager/APIManager.dart';
import '../../Modules/Enums/Enums.dart';
import '../../Modules/FormatterManager/FormatterManager.dart';
import '../../Modules/Json_Class/ReportDeliveryExecutiveResponse/ReportDeliveryExecutiveResponse.dart';
import '../../Modules/Json_Class/UserMappedTalukaResponse/UserMappedTalukaResponse.dart';
import '../../Modules/ToastManager/ToastManager.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/AppActiveButton.dart';
import '../DropDownListScreen/DropDownListScreen.dart';

class AssignToDETeamScreenFilterView extends StatefulWidget {
  AssignToDETeamScreenFilterView({
    super.key,
    // required this.fromDate,
    // required this.toDate,
    // required this.selectedTaluka,
    // required this.selectedReportDeliveryExecutive,
    required this.onTapApply,
  });

  // String fromDate = "";
  // String toDate = "";
  // UserMappedTalukaOutput? selectedTaluka;
  // ReportDeliveryExecutiveOutput? selectedReportDeliveryExecutive;
  Function() onTapApply;

  @override
  State<AssignToDETeamScreenFilterView> createState() =>
      _AssignToDETeamScreenFilterViewState();
}

class _AssignToDETeamScreenFilterViewState
    extends State<AssignToDETeamScreenFilterView> {
  int dISTLGDCODE = 0;
  int empCode = 0;
  APIManager apiManager = APIManager();

  Future<void> selectFromDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      AppDataManager.fromDate = FormatterManager.formatDateToString(picked);
      AppDataManager.toDate = "";
      setState(() {});
    }
  }

  Future<void> selectToDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime.now(),
    );
    if (picked != null) {
      AppDataManager.toDate = FormatterManager.formatDateToString(picked);
      setState(() {});
    }
  }

  getTaluka({bool showDropdown = true}) {
    Map<String, String> data = {
      "UserId": empCode.toString(),
      "DISTLGDCODE": dISTLGDCODE.toString(),
    };
    apiManager.getUserMappedTalukaAPI(data, (response, errorMessage, success) {
      apiUserMappedTalukaBack(response, errorMessage, success, showDropdown);
    });
  }

  void apiUserMappedTalukaBack(
    UserMappedTalukaResponse? response,
    String errorMessage,
    bool success,
    bool showDropdown,
  ) async {
    if (success) {
      ToastManager.hideLoader();
      if (AppDataManager.selectedTaluka == null &&
          (response?.output ?? []).isNotEmpty) {
        AppDataManager.selectedTaluka = response?.output?.first;
      }
      if (showDropdown) {
        _showDropDownBottomSheet(
          "Taluka",
          response?.output ?? [],
          DropDownTypeMenu.UserMappedTaluka,
        );
      } else {
        setState(() {});
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void _showDropDownBottomSheet(
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
              if (dropDownType == DropDownTypeMenu.UserMappedTaluka) {
                AppDataManager.selectedTaluka = p0;
              } else if (dropDownType ==
                  DropDownTypeMenu.ReportDeliveryExecutive) {
                AppDataManager.selectedResource = p0;
              }
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  getReportDeliveryExecutiveAPI() {
    if (AppDataManager.selectedTaluka == null) {
      ToastManager.toast("Please select Taluka");
      return;
    }
    Map<String, String> data = {
      "TALLGDCODE": AppDataManager.selectedTaluka?.tALLGDCODE.toString() ?? "0",
    };
    apiManager.getReportDeliveryExecutiveAPI(
      data,
      apiReportDeliveryExecutiveBack,
    );
  }

  void apiReportDeliveryExecutiveBack(
    ReportDeliveryExecutiveResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      ToastManager.hideLoader();
      _showDropDownBottomSheet(
        "Select Resource",
        response?.output ?? [],
        DropDownTypeMenu.ReportDeliveryExecutive,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  @override
  void initState() {
    super.initState();
    dISTLGDCODE =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    if (AppDataManager.selectedTaluka == null) {
      ToastManager.showLoader();
      getTaluka(showDropdown: false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(10, 20, 10, 10),
      child: Column(
        children: [
          Text(
            "Filters",
            style: TextStyle(
              color: kBlackColor,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w400,
              fontSize: responsiveFont(16),
            ),
          ),
          const SizedBox(height: 16),
          Row(
            children: [
              Expanded(
                child: AppTextField(
                  controller: TextEditingController(
                    text: AppDataManager.fromDate,
                  ),
                  readOnly: true,
                  onTap: () {
                    selectFromDate(context);
                  },
                  hint: 'From Date *',
                  label: CommonText(
                    text: 'From Date *',
                    fontSize: 12.sp,
                    fontWeight: FontWeight.normal,
                    textColor: kBlackColor,
                    textAlign: TextAlign.start,
                  ),
                  hintStyle: TextStyle(
                    fontSize: 12.sp,
                    fontWeight: FontWeight.w400,
                    fontFamily: FontConstants.interFonts,
                  ),
                  fieldRadius: 10,
                  prefixIcon: SizedBox(
                    height: 20.h,
                    width: 20.w,
                    child: Center(
                      child: Image.asset(
                        icCalendarMonth,
                        height: 24.h,
                        width: 24.w,
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                ),

                // AppDateTextfield(
                //   icon: icCalendarMonth,
                //   titleHeaderString: "From Date *",
                //   valueString: AppDataManager.fromDate,
                //   onTap: () {
                //     selectFromDate(context);
                //   },
                // ),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: AppTextField(
                  controller: TextEditingController(
                    text: AppDataManager.toDate,
                  ),
                  readOnly: true,
                  onTap: () {
                    selectToDate(context);
                  },
                  hint: 'To Date *',
                  label: CommonText(
                    text: 'To Date *',
                    fontSize: 12.sp,
                    fontWeight: FontWeight.normal,
                    textColor: kBlackColor,
                    textAlign: TextAlign.start,
                  ),
                  hintStyle: TextStyle(
                    fontSize: 12.sp,
                    fontWeight: FontWeight.w400,
                    fontFamily: FontConstants.interFonts,
                  ),
                  fieldRadius: 10,
                  prefixIcon: SizedBox(
                    height: 20.h,
                    width: 20.w,
                    child: Center(
                      child: Image.asset(
                        icCalendarMonth,
                        height: 24.h,
                        width: 24.w,
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                ),
                // AppDateTextfield(
                //   icon: icCalendarMonth,
                //   titleHeaderString: "To Date *",
                //   valueString: AppDataManager.toDate,
                //   onTap: () {
                //     selectToDate(context);
                //   },
                // ),
              ),
            ],
          ),
          const SizedBox(height: 8),
          AppTextField(
            controller: TextEditingController(
              text: AppDataManager.selectedTaluka?.tALNAME ?? "",
            ),
            readOnly: true,
            onTap: () {
              ToastManager.showLoader();
              getTaluka(showDropdown: true);
            },
            hint: 'Taluka *',
            label: CommonText(
              text: 'Taluka *',
              fontSize: 12.sp,
              fontWeight: FontWeight.normal,
              textColor: kBlackColor,
              textAlign: TextAlign.start,
            ),
            hintStyle: TextStyle(
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              fontFamily: FontConstants.interFonts,
            ),
            fieldRadius: 10,
            prefixIcon: SizedBox(
              height: 20.h,
              width: 20.w,
              child: Center(
                child: Image.asset(
                  icMapPin,
                  height: 24.h,
                  width: 24.w,
                  fit: BoxFit.contain,
                ),
              ),
            ),
            suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
          ),
          const SizedBox(height: 8),
          AppTextField(
            controller: TextEditingController(
              text: AppDataManager.selectedResource?.userName ?? "",
            ),
            readOnly: true,
            onTap: () {
              ToastManager.showLoader();
              getReportDeliveryExecutiveAPI();
            },
            hint: 'Delivery Executive / Team *',
            label: CommonText(
              text: 'Delivery Executive / Team *',
              fontSize: 12.sp,
              fontWeight: FontWeight.normal,
              textColor: kBlackColor,
              textAlign: TextAlign.start,
            ),
            hintStyle: TextStyle(
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              fontFamily: FontConstants.interFonts,
            ),
            fieldRadius: 10,
            prefixIcon: SizedBox(
              height: 20.h,
              width: 20.w,
              child: Center(
                child: Image.asset(
                  icUsersGroup,
                  height: 24.h,
                  width: 24.w,
                  fit: BoxFit.contain,
                ),
              ),
            ),
            suffixIcon: Icon(Icons.keyboard_arrow_down_outlined),
          ),

          const Spacer(),
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 4, 16, 20),
            child: Container(
              width: MediaQuery.of(context).size.width,
              color: Colors.transparent,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Clear",
                      isCancel: true,
                      onTap: () {
                        Navigator.pop(context);
                      },
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Apply",
                      onTap: () {
                        if (AppDataManager.selectedResource == null) {
                          ToastManager.toast(
                            "Select Delivery Executive / Team",
                          );
                          return;
                        }
                        Navigator.pop(context);
                        widget.onTapApply();
                      },
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
