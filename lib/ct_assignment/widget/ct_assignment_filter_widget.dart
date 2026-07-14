// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/utilities/enums.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';
import 'package:s2toperational/appointment_sample_collection_ct/models/assignment_remarks_response.dart';
import 'package:s2toperational/ct_assignment/model/department_type_response.dart';
import 'package:s2toperational/camp_creation/models/district_response.dart';
import 'package:s2toperational/camp_creation/models/taluka_camp_creation_response.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/app_active_button.dart';
import 'package:s2toperational/common_widgets/drop_down_list_screen/drop_down_list_screen.dart';

class CTAssignmentFilterWidget extends StatefulWidget {
  CTAssignmentFilterWidget({
    super.key,
    required this.onSelectedFromDte,
    required this.onSelectedToDte,
    required this.onSelectedDistrict,
    required this.onSelectedTaluka,
    required this.onSelectedPinCode,
    required this.onSelectedStatusRemark,
    required this.onSelectedDeptType,
    required this.applyDidPressed,
  });

  Function(DateTime?) onSelectedFromDte;
  Function(DateTime?) onSelectedToDte;
  Function(DistrictOutput?) onSelectedDistrict;
  Function(TalukaCampCreationOutput?) onSelectedTaluka;
  Function(String) onSelectedPinCode;
  Function(AssignmentRemarksOutput?) onSelectedStatusRemark;
  Function(DepartmentTypeOutput?) onSelectedDeptType;
  Function() applyDidPressed;

  @override
  State<CTAssignmentFilterWidget> createState() =>
      _CTAssignmentFilterWidgetState();
}

class _CTAssignmentFilterWidgetState extends State<CTAssignmentFilterWidget> {
  TextEditingController pinCodeTextEditingController = TextEditingController();

  DateTime? fromDate;
  DateTime? toDate;
  DistrictOutput? selectedDistrict;
  TalukaCampCreationOutput? selectedTaluka;
  AssignmentRemarksOutput? selectedStatusRemark;
  DepartmentTypeOutput? selectedDeptType;
  int empCode = 0;
  int empDistrictCode = 0;
  APIManager apiManager = APIManager();

  @override
  void initState() {
    super.initState();
    toDate = DateTime.now();
    fromDate = toDate!.subtract(const Duration(days: 3));
    final parsedUser = DataProvider().getParsedUserData();
    empCode = parsedUser?.output?.first.empCode ?? 0;
    empDistrictCode = parsedUser?.output?.first.dISTLGDCODE ?? 0;

    selectedDeptType = DepartmentTypeOutput(
      deptTypeId: 0,
      departmentType: "All",
    );
    getDistictInitially();
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 10),
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
          const SizedBox(height: 8),
          AppTextField(
            readOnly: true,
            controller: TextEditingController(
              text: FormatterManager.formatDateToString(fromDate!),
            ),
            onTap: () {
              _selectFromDate(context);
            },
            hint: 'From Date*',
            label: CommonText(
              text: 'From Date*',
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
          //   titleHeaderString: "From Date*",
          //   valueString: FormatterManager.formatDateToString(fromDate!),
          //   onTap: () {
          //     _selectFromDate(context);
          //   },
          // ),
          const SizedBox(height: 8),
          AppTextField(
            readOnly: true,
            controller: TextEditingController(
              text:
                  toDate == null
                      ? ""
                      : FormatterManager.formatDateToString(toDate!),
            ),
            onTap: () {
              _selectToDate(context);
            },
            hint: 'To Date*',
            label: CommonText(
              text: 'To Date*',
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
          //   titleHeaderString: "To Date*",
          //   valueString:
          //       toDate == null
          //           ? ""
          //           : FormatterManager.formatDateToString(toDate!),
          //   onTap: () {
          //     _selectToDate(context);
          //   },
          // ),
          const SizedBox(height: 8),
          AppTextField(
            readOnly: true,
            controller: TextEditingController(
              text: selectedDistrict?.dISTNAME ?? "",
            ),
            onTap: () async {
              await getDistrictByUserID();
            },
            hint: 'District *',
            label: CommonText(
              text: 'District *',
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
            suffixIcon: Icon(Icons.keyboard_arrow_down),
          ),
          // AppDropdownTextfield(
          //   icon: icMapPin,
          //   titleHeaderString: "District",
          //   valueString: selectedDistrict?.dISTNAME ?? "",
          //   onTap: () {
          //     getDistrictByUserID();
          //   },
          // ),
          const SizedBox(height: 8),
          // AppDropdownTextfield(
          //   icon: icMapPin,
          //   titleHeaderString: "Taluka",
          //   valueString: selectedTaluka?.tALNAME ?? "",
          //   onTap: () {
          //     getTaluka();
          //   },
          // ),
          AppTextField(
            readOnly: true,
            controller: TextEditingController(
              text: selectedTaluka?.tALNAME ?? "",
            ),
            onTap: () {
              getTaluka();
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
            suffixIcon: Icon(Icons.keyboard_arrow_down),
          ),
          const SizedBox(height: 8),
          AppTextField(
            readOnly: true,
            controller: TextEditingController(
              text: selectedDeptType?.departmentType ?? "",
            ),
            onTap: () {
              getDepartmentType();
            },
            hint: 'CT Prescribed From *',
            label: CommonText(
              text: 'CT Prescribed From *',
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
            suffixIcon: Icon(Icons.keyboard_arrow_down),
          ),
          const SizedBox(height: 8),

          AppTextField(
            onTap: () {
              getAssignmentRemarks();
            },
            readOnly: false,
            controller: TextEditingController(
              text: selectedStatusRemark?.assignmentRemarks ?? "",
            ),
            hint: 'Status*',
            label: CommonText(
              text: 'Status*',
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
            textInputType: TextInputType.number,
            maxLength: 6,
            suffixIcon: Icon(Icons.keyboard_arrow_down),
          ),
          const SizedBox(height: 8),

          AppTextField(
            readOnly: false,
            controller: pinCodeTextEditingController,
            hint: 'Pin Code',
            label: CommonText(
              text: 'Pin Code',
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
                  icHashIcon,
                  height: 24.h,
                  width: 24.w,
                  fit: BoxFit.contain,
                ),
              ),
            ),
            textInputType: TextInputType.number,
            maxLength: 6,
          ),

          const SizedBox(height: 8),
          Padding(
            padding: const EdgeInsets.fromLTRB(0, 4, 0, 20),
            child: Container(
              width: MediaQuery.of(context).size.width,
              color: Colors.transparent,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Back",
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
                        String pinCode = pinCodeTextEditingController.text;

                        if (pinCode.isNotEmpty) {
                          if (pinCode.length < 6) {
                            ToastManager.toast("Please 6 Digit Pin code");
                            return;
                          }
                        }

                        Navigator.pop(context);
                        widget.onSelectedPinCode(pinCode);
                        widget.applyDidPressed();
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

  Future<void> _selectFromDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: fromDate ?? DateTime.now(),
      firstDate: DateTime(2000),
      lastDate: DateTime.now(),
    );
    if (picked != null) {
      fromDate = picked;
      widget.onSelectedFromDte(fromDate);
      if (toDate != null && picked.isAfter(toDate!)) {
        widget.onSelectedToDte(null);
        toDate = null;
      }
      setState(() {});
    }
  }

  Future<void> _selectToDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: fromDate,
      firstDate: fromDate!,
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      toDate = picked;
      widget.onSelectedToDte(toDate);
      setState(() {});
    }
  }

  getDistrictByUserID() async {
    ToastManager.showLoader();
    Map<String, String> params = {"STATELGDCODE": "2", "USERID": "$empCode"};
    await apiManager.getDistrictByUserIDAPI(params, (response, error, success) {
      ToastManager.hideLoader();
      if (success) {
        List<DistrictOutput> list = response?.output ?? [];
        final matches = list
            .where((e) => e.dISTLGDCODE == empDistrictCode)
            .toList(growable: false);
        selectedDistrict =
            matches.isNotEmpty
                ? matches.first
                : (list.isNotEmpty ? list.first : null);
        _showDropDownBottomSheet(
          "Select District",
          list,
          DropDownTypeMenu.District,
        );
      }
    });
  }

  getDistictInitially() async {
    ToastManager.showLoader();
    Map<String, String> params = {"STATELGDCODE": "2", "USERID": "$empCode"};
    await apiManager.getDistrictByUserIDAPI(params, (response, error, success) {
      ToastManager.hideLoader();
      if (success) {
        List<DistrictOutput> list = response?.output ?? [];
        final matches = list
            .where((e) => e.dISTLGDCODE == empDistrictCode)
            .toList(growable: false);
        selectedDistrict =
            matches.isNotEmpty
                ? matches.first
                : (list.isNotEmpty ? list.first : null);
        widget.onSelectedDistrict(selectedDistrict);
        setState(() {});
      }
    });
  }

  void getTaluka() {
    ToastManager.showLoader();
    Map<String, String> data = {
      "STATELGDCODE": "2",
      "DISTLGDCODE": selectedDistrict?.dISTLGDCODE.toString() ?? "0",
    };
    apiManager.getTalukaAPI(data, (response, error, success) {
      ToastManager.hideLoader();

      if (success) {
        List<TalukaCampCreationOutput> list = response?.output ?? [];
        list.insert(0, TalukaCampCreationOutput(tALLGDCODE: 0, tALNAME: "All"));
        _showDropDownBottomSheet(
          "Taluka",
          list,
          DropDownTypeMenu.TalukaCampList,
        );
      } else {
        ToastManager.toast(error);
      }
    });
  }

  getAssignmentRemarks() {
    ToastManager.showLoader();
    Map<String, String> data = {"Type": "1"};
    apiManager.getAssignmentRemarksAPI(data, (response, error, success) {
      ToastManager.hideLoader();
      if (success) {
        _showDropDownBottomSheet(
          "Remark",
          response?.output ?? [],
          DropDownTypeMenu.StatusRemark,
        );
      } else {
        ToastManager.toast(error);
      }
    });
  }

  getDepartmentType() {
    ToastManager.showLoader();
    apiManager.getDepartmentTypeAPI((response, error, success) {
      ToastManager.hideLoader();
      if (success) {
        List<DepartmentTypeOutput> list = response?.output ?? [];
        list.insert(
          0,
          DepartmentTypeOutput(deptTypeId: 0, departmentType: "All"),
        );
        _showDropDownBottomSheet(
          "CT Prescribed From",
          list,
          DropDownTypeMenu.DeptType,
        );
      } else {
        ToastManager.toast(error);
      }
    });
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
              if (dropDownType == DropDownTypeMenu.District) {
                selectedDistrict = p0;
                selectedTaluka = null;
                widget.onSelectedDistrict(selectedDistrict);
                widget.onSelectedTaluka(null);
              } else if (dropDownType == DropDownTypeMenu.TalukaCampList) {
                selectedTaluka = p0;
                widget.onSelectedTaluka(selectedTaluka);
              } else if (dropDownType == DropDownTypeMenu.StatusRemark) {
                selectedStatusRemark = p0;
                widget.onSelectedStatusRemark(selectedStatusRemark);
              } else if (dropDownType == DropDownTypeMenu.DeptType) {
                selectedDeptType = p0;
                widget.onSelectedDeptType(selectedDeptType);
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
}
