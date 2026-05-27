// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/Json_Class/ExpenseCampIDListV1Response/ExpenseCampIDListV1Response.dart';
import 'package:s2toperational/Modules/Json_Class/ScreeningTestCampCreationResponse/ScreeningTestCampCreationResponse.dart';
import 'package:s2toperational/Modules/Json_Class/SubResourceListResponse/SubResourceListResponse.dart';
import 'package:s2toperational/Modules/Json_Class/UpdateSubResourceListResponse/UpdateSubResourceListResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';

class MultiSelectionDropDownListScreen extends StatefulWidget {
  MultiSelectionDropDownListScreen({
    super.key,
    required this.titleString,
    required this.dropDownList,
    required this.dropDownMenu,
    required this.onApplyTap,
    this.preSelectedList,
  });

  List<dynamic> dropDownList = [];
  String titleString = "";
  DropDownMultipleTypeMenu dropDownMenu;
  Function(dynamic) onApplyTap;
  List<dynamic>? preSelectedList;

  @override
  State<MultiSelectionDropDownListScreen> createState() =>
      _MultiSelectionDropDownListScreenState();
}

class _MultiSelectionDropDownListScreenState
    extends State<MultiSelectionDropDownListScreen> {
  List<dynamic> searchList = [];
  int _selectedCount = 0;

  TextEditingController searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
    searchList = widget.dropDownList;
    if (widget.dropDownMenu == DropDownMultipleTypeMenu.ScreeningTest) {
      for (ScreeningTestCampCreationOutput initiatedBy in searchList) {
        if (initiatedBy.isCompulsary == 1) {
          initiatedBy.isSelected = true;
        }
      }
    } else if (widget.dropDownMenu ==
        DropDownMultipleTypeMenu.UpdateSubResource) {
      for (UpdateSubResourceOutput obj in widget.dropDownList) {
        if (obj.resStatus?.toLowerCase() == "Selected".toLowerCase()) {
          _selectedCount += 1;
          obj.isSelected = true;
        }
      }
    }
    _restorePreSelectedItems();
  }

  void _restorePreSelectedItems() {
    final pre = widget.preSelectedList;
    if (pre == null || pre.isEmpty) return;
    for (var item in searchList) {
      switch (widget.dropDownMenu) {
        case DropDownMultipleTypeMenu.ScreeningTest:
          final t = item as ScreeningTestCampCreationOutput;
          if (pre.any((s) => (s as ScreeningTestCampCreationOutput).testId == t.testId)) {
            t.isSelected = true;
          }
        case DropDownMultipleTypeMenu.SubResource:
          final t = item as SubResourceOutput;
          if (pre.any((s) => (s as SubResourceOutput).uSERID == t.uSERID)) {
            t.isSelected = true;
          }
        case DropDownMultipleTypeMenu.CampID:
          final t = item as ExpenseCampIDListV1Output;
          if (pre.any((s) => (s as ExpenseCampIDListV1Output).campid == t.campid)) {
            t.isSelected = true;
          }
        case DropDownMultipleTypeMenu.UpdateSubResource:
          final t = item as UpdateSubResourceOutput;
          if (pre.any((s) => (s as UpdateSubResourceOutput).uSERID == t.uSERID)) {
            t.isSelected = true;
          }
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          width: MediaQuery.of(context).size.width,
          padding: const EdgeInsets.fromLTRB(0, 12, 0, 12),
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: Center(
            child: Text(
              widget.titleString,
              style: TextStyle(
                fontWeight: FontWeight.w400,
                color: kBlackColor,
                fontFamily: FontConstants.interFonts,
                fontSize: responsiveFont(18),
              ),
            ),
          ),
        ),

        Padding(
          padding: const EdgeInsets.fromLTRB(8, 0, 8, 8),
          child: AppTextField(
            controller: searchController,
            readOnly: false,
            hint: 'Search',
            textInputType: TextInputType.text,
            hintStyle: TextStyle(
              fontSize: responsiveFont(14),
              fontWeight: FontWeight.w400,
              fontFamily: FontConstants.interFonts,
            ),
            fieldRadius: 10,
            suffixIcon: Padding(
              padding: const EdgeInsets.all(10),
              child: Image.asset(icSearch, height: 20, width: 20, fit: BoxFit.contain),
            ),
            onChange: (value) {
              _filterList(value);
            },
          ),
        ),

        Expanded(
          child: ListView.builder(
            itemCount: searchList.length,
            itemBuilder: (context, index) {
              return Padding(
                padding: const EdgeInsets.fromLTRB(8, 0, 8, 4),
                child: GestureDetector(
                  onTap: () {
                    radioButtonDidSelected(index);
                  },
                  child: Container(
                    width: MediaQuery.of(context).size.width,
                    color: Colors.transparent,
                    padding: const EdgeInsets.fromLTRB(8, 4, 8, 4),
                    child: Row(
                      children: [
                        Container(
                          padding: const EdgeInsets.all(2),
                          width: 30,
                          height: 30,
                          child: selectedRadioButton(index),
                        ),
                        const SizedBox(width: 10),
                        Text(
                          titleDropDown(index),
                          style: TextStyle(
                            fontWeight: selectedTextFontWeight(index),
                            color: selectedTextColor(index),
                            fontFamily: FontConstants.interFonts,
                            fontSize: responsiveFont(18),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              );
            },
          ),
        ),
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
                    buttontitle: "Back",
                    isCancel: true,
                    onTap: () {
                      Navigator.pop(context);
                    },
                  ),
                ),
                const SizedBox(width: 67),
                Expanded(
                  child: AppActiveButton(
                    buttontitle: "Done",
                    onTap: () {
                      if (widget.dropDownMenu ==
                          DropDownMultipleTypeMenu.UpdateSubResource) {
                        if (_selectedCount == 0) {}
                      } else {}
                      List<dynamic> selectedList =
                          widget.dropDownList
                              .where((item) => (item.isSelected == true))
                              .toList();
                      Navigator.pop(context);
                      widget.onApplyTap(selectedList);
                    },
                  ),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }

  void _filterList(String query) {
    if (query.isEmpty) {
      searchList = widget.dropDownList;
    } else {
      searchList = widget.dropDownList.where((item) {
        String title = '';
        switch (widget.dropDownMenu) {
          case DropDownMultipleTypeMenu.ScreeningTest:
            title = (item as ScreeningTestCampCreationOutput).testName ?? '';
          case DropDownMultipleTypeMenu.SubResource:
            title = (item as SubResourceOutput).resourceName ?? '';
          case DropDownMultipleTypeMenu.CampID:
            title = (item as ExpenseCampIDListV1Output).campid?.toString() ?? '';
          case DropDownMultipleTypeMenu.UpdateSubResource:
            title = (item as UpdateSubResourceOutput).resourceName ?? '';
        }
        return title.toLowerCase().contains(query.toLowerCase());
      }).toList();
    }
    setState(() {});
  }

  String titleDropDown(int index) {
    switch (widget.dropDownMenu) {
      case DropDownMultipleTypeMenu.ScreeningTest:
        ScreeningTestCampCreationOutput type = searchList[index];
        return type.testName ?? "";
      case DropDownMultipleTypeMenu.SubResource:
        SubResourceOutput type = searchList[index];
        return type.resourceName ?? "";
      case DropDownMultipleTypeMenu.CampID:
        ExpenseCampIDListV1Output type = searchList[index];
        return type.campid?.toString() ?? "";
      case DropDownMultipleTypeMenu.UpdateSubResource:
        UpdateSubResourceOutput type = searchList[index];
        return type.resourceName ?? "";
    }
  }

  void radioButtonDidSelected(int index) {
    switch (widget.dropDownMenu) {
      case DropDownMultipleTypeMenu.ScreeningTest:
        ScreeningTestCampCreationOutput initiatedBy = searchList[index];
        if (initiatedBy.isCompulsary != 1) {
          initiatedBy.isSelected = !initiatedBy.isSelected;
        }
      case DropDownMultipleTypeMenu.SubResource:
        SubResourceOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = !initiatedBy.isSelected;
      case DropDownMultipleTypeMenu.CampID:
        ExpenseCampIDListV1Output initiatedBy = searchList[index];
        initiatedBy.isSelected = !initiatedBy.isSelected;
      case DropDownMultipleTypeMenu.UpdateSubResource:
        UpdateSubResourceOutput initiatedBy = searchList[index];
        initiatedBy.isSelected = !initiatedBy.isSelected;
    }

    setState(() {});
  }

  Widget selectedRadioButton(int index) {
    switch (widget.dropDownMenu) {
      case DropDownMultipleTypeMenu.ScreeningTest:
        ScreeningTestCampCreationOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icCheckBoxSelected : icUnCheckBoxSelected,
        );
      case DropDownMultipleTypeMenu.SubResource:
        SubResourceOutput type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icCheckBoxSelected : icUnCheckBoxSelected,
        );
      case DropDownMultipleTypeMenu.CampID:
        ExpenseCampIDListV1Output type = searchList[index];
        return Image.asset(
          type.isSelected == true ? icCheckBoxSelected : icUnCheckBoxSelected,
        );
      case DropDownMultipleTypeMenu.UpdateSubResource:
        UpdateSubResourceOutput type = searchList[index];

        return Image.asset(
          type.isSelected == true ? icCheckBoxSelected : icUnCheckBoxSelected,
        );
    }
  }

  Color selectedTextColor(int index) {
    switch (widget.dropDownMenu) {
      case DropDownMultipleTypeMenu.ScreeningTest:
        ScreeningTestCampCreationOutput type = searchList[index];
        return type.isSelected ? kBlackColor : kLabelTextColor;
      case DropDownMultipleTypeMenu.SubResource:
        SubResourceOutput type = searchList[index];
        return type.isSelected ? kBlackColor : kLabelTextColor;
      case DropDownMultipleTypeMenu.CampID:
        ExpenseCampIDListV1Output type = searchList[index];
        return type.isSelected ? kBlackColor : kLabelTextColor;
      case DropDownMultipleTypeMenu.UpdateSubResource:
        UpdateSubResourceOutput type = searchList[index];
        return type.isSelected ? kBlackColor : kLabelTextColor;
    }
  }

  FontWeight selectedTextFontWeight(int index) {
    switch (widget.dropDownMenu) {
      case DropDownMultipleTypeMenu.ScreeningTest:
        ScreeningTestCampCreationOutput type = searchList[index];
        return type.isSelected ? FontWeight.w600 : FontWeight.w400;
      case DropDownMultipleTypeMenu.SubResource:
        SubResourceOutput type = searchList[index];
        return type.isSelected ? FontWeight.w600 : FontWeight.w400;
      case DropDownMultipleTypeMenu.CampID:
        ExpenseCampIDListV1Output type = searchList[index];
        return type.isSelected ? FontWeight.w600 : FontWeight.w400;
      case DropDownMultipleTypeMenu.UpdateSubResource:
        UpdateSubResourceOutput type = searchList[index];
        return type.isSelected ? FontWeight.w600 : FontWeight.w400;
    }
  }
}
