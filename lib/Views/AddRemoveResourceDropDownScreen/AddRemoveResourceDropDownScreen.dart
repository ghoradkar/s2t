// ignore_for_file: must_be_immutable, file_names, avoid_print

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import '../../Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import '../../Modules/Json_Class/UpdateSubResourceListResponse/UpdateSubResourceListResponse.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/AppActiveButton.dart';
import '../../../../../Modules/constants/fonts.dart';

class AddRemoveResourceDropDownScreen extends StatefulWidget {
  AddRemoveResourceDropDownScreen({
    super.key,
    required this.titleString,
    required this.dropDownList,
    required this.reMappingCampOutput,
    required this.onRefreshData,
    required this.empCode,
  });

  List<dynamic> dropDownList = [];
  String titleString = "";
  Function(UpdateSubResourceOutput?) onRefreshData;
  ResourceReMappingCampOutput reMappingCampOutput;
  int empCode = 0;

  @override
  State<AddRemoveResourceDropDownScreen> createState() =>
      _AddRemoveResourceDropDownScreenState();
}

class _AddRemoveResourceDropDownScreenState
    extends State<AddRemoveResourceDropDownScreen> {
  List<dynamic> searchList = [];
  int _selectedCount = 0;

  UpdateSubResourceOutput? selectedSubResource;
  APIManager apiManager = APIManager();
  TextEditingController searchController = TextEditingController();
  String titleDropDown(int index) {
    UpdateSubResourceOutput type = searchList[index];
    return type.resourceName ?? "";
  }

  radioButtonDidSelected(int index) {
    for (UpdateSubResourceOutput campType in searchList) {
      campType.isSelected = false;
    }

    UpdateSubResourceOutput initiatedBy = searchList[index];
    initiatedBy.isSelected = true;
    selectedSubResource = initiatedBy;

    setState(() {});
  }

  Widget selectedRadioButton(int index) {
    UpdateSubResourceOutput type = searchList[index];
    return Image.asset(
      type.isSelected == true ? icCheckBoxSelected : icUnCheckBoxSelected,
    );
  }

  @override
  void initState() {
    super.initState();
    searchList = widget.dropDownList;

    for (UpdateSubResourceOutput obj in widget.dropDownList) {
      if (obj.resStatus?.toLowerCase() == "Selected".toLowerCase()) {
        print(obj.resStatus ?? "");
        _selectedCount += 1;
        obj.isSelected = true;
        selectedSubResource = obj;
      }
    }
  }

  bool addValidation() {
    if (selectedSubResource == null) {
      ToastManager.toast("Please select at least one resource");
      return false;
    }

    return true;
  }

  addResourceAllcoation() {
    Map<String, String> prams = {
      "ResourceUserId": selectedSubResource?.uSERID?.toString() ?? "0",
      "CampId": widget.reMappingCampOutput.campId?.toString() ?? "0",
      "TestId": selectedSubResource?.testId?.toString() ?? "0",
      "UserId": widget.empCode.toString(),
    };

    apiManager.addResourceAllcoationAPI(
      prams,
      apiAddResourceAllcoationCallBack,
    );
  }

  void apiAddResourceAllcoationCallBack(
    UpdateSubResourceListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      ToastManager.toast("Resource updated successfully");
    } else {
      ToastManager.toast("Resource already exists.");
    }
    widget.onRefreshData(selectedSubResource);
    Navigator.pop(context);
  }

  bool removeValidation() {
    if (_selectedCount == 1) {
      ToastManager.toast(
        "Atleast one resource is needed for test you need to add resource then remove resource",
      );
      return false;
    } else {
      if (selectedSubResource == null) {
        ToastManager.toast("Please select at least one resource");
        return false;
      }
    }
    return true;
  }

  removeResourceAllcoation() {
    Map<String, String> prams = {
      "ResourceUserId": selectedSubResource?.uSERID?.toString() ?? "0",
      "CampId": widget.reMappingCampOutput.campId?.toString() ?? "0",
      "TestId": selectedSubResource?.testId?.toString() ?? "0",
      "UserId": widget.empCode.toString(),
    };

    apiManager.removeResourceAllcoationAPI(
      prams,
      apiRemoveResourceAllcoationCallBack,
    );
  }

  void apiRemoveResourceAllcoationCallBack(
    UpdateSubResourceListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      ToastManager.toast("Resource remove successfully");
    } else {
      ToastManager.toast(
        "Resource can not be removed as test has been performed by resource.",
      );
    }
    widget.onRefreshData(selectedSubResource);
    Navigator.pop(context);
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
                fontWeight: FontWeight.normal,
                color: kBlackColor,
                fontFamily: FontConstants.interFonts,
                fontSize: responsiveFont(18),
              ),
            ),
          ),
        ),

        Expanded(
          child: ListView.builder(
            itemCount: searchList.length,
            itemBuilder: (context, index) {
              return Padding(
                padding: const EdgeInsets.fromLTRB(8, 0, 8, 4),
                child: Container(
                  width: MediaQuery.of(context).size.width,
                  color: Colors.transparent,
                  padding: const EdgeInsets.fromLTRB(8, 4, 8, 4),
                  child: Row(
                    children: [
                      GestureDetector(
                        onTap: () {
                          radioButtonDidSelected(index);
                        },
                        child: Container(
                          padding: const EdgeInsets.all(2),
                          width: 30,
                          height: 30,
                          child: selectedRadioButton(index),
                        ),
                      ),
                      const SizedBox(width: 10),
                      Expanded(
                        child: Text(
                          titleDropDown(index),
                          style: TextStyle(
                            fontWeight: FontWeight.normal,
                            color: kBlackColor,
                            fontFamily: FontConstants.interFonts,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                      ),
                    ],
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
                    buttontitle: "Remove",
                    isCancel: true,
                    onTap: () {
                      if (removeValidation()) {
                        removeResourceAllcoation();
                      }
                    },
                  ),
                ),
                const SizedBox(width: 16),
                Expanded(
                  child: AppActiveButton(
                    buttontitle: "Add",
                    onTap: () {
                      if (addValidation()) {
                        addResourceAllcoation();
                      }
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
}
