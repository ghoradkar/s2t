// ignore_for_file: must_be_immutable, avoid_print, file_names

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import '../../../Modules/APIManager/APIManager.dart';
import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/Json_Class/CampIdListResponse/CampIdListResponse.dart';
import '../../../Modules/Json_Class/CampTypeResponse/CampTypeResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppActiveButton.dart';
import '../../../Modules/widgets/AppDateTextfield.dart';
import '../../../Modules/widgets/AppDropdownTextfield.dart';
import '../../DropDownListScreen/DropDownListScreen.dart';
import '../../../../../Modules/constants/fonts.dart';

class CampReadinessFormFilterScreen extends StatefulWidget {
  CampReadinessFormFilterScreen({
    super.key,
    required this.campDate,
    required this.onTapFilter,
  });

  String campDate;
  Function(CampTypeOutput, CampIdOutput) onTapFilter;
  @override
  State<CampReadinessFormFilterScreen> createState() =>
      _CampReadinessFormFilterScreenState();
}

class _CampReadinessFormFilterScreenState
    extends State<CampReadinessFormFilterScreen> {
  int dESGID = 0;
  int empCode = 0;
  int districtId = 0;
  String districtName = "";
  CampTypeOutput? selectedCampType;
  CampIdOutput? selectedCampID;
  APIManager apiManager = APIManager();

  @override
  void initState() {
    super.initState();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    districtId =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
    districtName =
        DataProvider().getParsedUserData()?.output?.first.district ?? "";
    setState(() {});
  }

  chooseCampType() {
    ToastManager.showLoader();

    if (dESGID == 129 || dESGID == 146) {
      apiManager.getCampTypeFlexiAPI(apiCampTypeFlexiCallBack);
    } else if (dESGID == 138 ||
        dESGID == 137 ||
        dESGID == 169 ||
        dESGID == 177 ||
        dESGID == 31 ||
        dESGID == 176) {
      apiManager.getCampTypeMMUAPI(apiCampTypeMMUCallBack);
    } else if (dESGID == 35 || dESGID == 86) {
      getCampTypeD2D();
    } else {
      getCampTypeRegular();
    }
  }

  getCampTypeRegular() {
    ToastManager.hideLoader();
    List<CampTypeOutput> campTypeList = [];
    campTypeList.add(
      CampTypeOutput(cAMPTYPE: 1, campTypeDescription: "REGULAR"),
    );

    _showDropDownBottomSheet(
      "Camp Type",
      campTypeList,
      DropDownTypeMenu.CampType,
    );
  }

  getCampTypeD2D() {
    ToastManager.hideLoader();
    List<CampTypeOutput> campTypeList = [];
    campTypeList.add(
      CampTypeOutput(cAMPTYPE: 3, campTypeDescription: "DOOR TO DOOR"),
    );

    _showDropDownBottomSheet(
      "Camp Type",
      campTypeList,
      DropDownTypeMenu.CampType,
    );
  }

  void apiCampTypeFlexiCallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void apiCampTypeMMUCallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  // void apiCampTypeD2DCallBack(
  //   CampTypeResponse? response,
  //   String errorMessage,
  //   bool success,
  // ) async {
  //   ToastManager.hideLoader();

  //   if (success) {
  //     _showDropDownBottomSheet(
  //       "Camp Type",
  //       response?.output ?? [],
  //       DropDownTypeMenu.CampType,
  //     );
  //   } else {
  //     ToastManager.toast(errorMessage);
  //   }
  // }

  chooseCampID() {
    if (selectedCampType == null) {
      ToastManager.toast("Please select Camp Type");
    } else {
      Map<String, String> dict = {
        "CampDATE": widget.campDate,
        "UserId": empCode.toString(),
        "DISTLGDCODE": districtId.toString(),
        "CampType": selectedCampType?.cAMPTYPE.toString() ?? "0",
        "LABCODE": "0",
      };

      print(dict);
      apiManager.getCampListCampReadinessAPI(dict, apiCampIDCallBack);
    }
  }

  void apiCampIDCallBack(
    CampIdListResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp ID",
        response?.output ?? [],
        DropDownTypeMenu.CampID,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
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
              if (dropDownType == DropDownTypeMenu.CampType) {
                selectedCampType = p0;
              } else if (dropDownType == DropDownTypeMenu.CampID) {
                selectedCampID = p0;
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

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: EdgeInsets.fromLTRB(20, 0, 20, 0),
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(20, 20, 20, 20),
            child: Text(
              "Filters",
              style: TextStyle(
                color: kBlackColor,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.normal,
                fontSize: responsiveFont(22),
              ),
            ),
          ),
          const SizedBox(height: 20),
          AppDateTextfield(
            icon: icCalendarMonth,
            titleHeaderString: "Camp Date*",
            valueString: widget.campDate,
            valueColor: Colors.grey,
            onTap: () {
              // _selectCampDate(context);
            },
          ),
          const SizedBox(height: 20),
          AppDropdownTextfield(
            icon: icnTent,
            titleHeaderString: "Camp Type*",
            valueString: selectedCampType?.campTypeDescription ?? "",
            onTap: () {
              chooseCampType();
            },
          ),
          const SizedBox(height: 20),
          AppDropdownTextfield(
            icon: icnTent,
            titleHeaderString: "Camp id*",
            valueString: selectedCampID?.campId?.toString() ?? "",
            onTap: () {
              chooseCampID();
            },
          ),
          const Spacer(),
          SizedBox(
            width: MediaQuery.of(context).size.width,
            child: Row(
              children: [
                Expanded(
                  child: AppActiveButton(
                    buttontitle: "Cancel",
                    isCancel: true,
                    onTap: () {
                      Navigator.pop(context);
                    },
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: AppActiveButton(
                    buttontitle: "Apply",
                    onTap: () {
                      if (selectedCampType == null) {
                        ToastManager.toast("Please select Camp Type");
                      } else if (selectedCampID == null) {
                        ToastManager.toast("Please select Camp Id");
                      } else {
                        widget.onTapFilter(selectedCampType!, selectedCampID!);
                        Navigator.pop(context);
                      }
                    },
                  ),
                ),
              ],
            ),
          ),
          SizedBox(height: 20),
        ],
      ),
    );
  }
}
