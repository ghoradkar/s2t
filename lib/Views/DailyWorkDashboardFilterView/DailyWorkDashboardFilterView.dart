// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/widgets/AppDropdownTextfield.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/Enums/Enums.dart';
import '../../Modules/Json_Class/LabDataResponse/LabDataResponse.dart';
import '../../Modules/ToastManager/ToastManager.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/AppActiveButton.dart';
import '../DropDownListScreen/DropDownListScreen.dart';

class DailyWorkDashboardFilterView extends StatefulWidget {
  DailyWorkDashboardFilterView({
    super.key,
    required this.selectedLab,
    required this.onTapApply,
  });

  LabDataOutput? selectedLab;
  Function() onTapApply;
  @override
  State<DailyWorkDashboardFilterView> createState() =>
      _DailyWorkDashboardFilterViewState();
}

class _DailyWorkDashboardFilterViewState
    extends State<DailyWorkDashboardFilterView> {
  APIManager apiManager = APIManager();
  int empCode = 0;

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
              if (dropDownType == DropDownTypeMenu.DailyWorkDashboardLab) {
                widget.selectedLab = p0;
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

  void getLabforD2DCampCoordinator() {
    ToastManager.showLoader();
    Map<String, String> params = {"UserID": "$empCode"};
    apiManager.getLabforD2DCampCoordinatorAPI(
      params,
      apiLabforD2DCampCoordinatorCallBack,
    );
  }

  void apiLabforD2DCampCoordinatorCallBack(
    LabDataResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      List<LabDataOutput> dataList = response?.output ?? [];
      for (LabDataOutput dataOutput in dataList) {
        if (widget.selectedLab?.labCode == dataOutput.labCode) {
          dataOutput.isSelected = true;
        }
      }
      _showDropDownBottomSheet(
        "Camp Lab",
        dataList,
        DropDownTypeMenu.DailyWorkDashboardLab,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  @override
  void initState() {
    super.initState();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 20, 0, 10),
      child: Column(
        children: [
          Text(
            "Select Lab",
            style: TextStyle(
              color: kBlackColor,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w400,
              fontSize: responsiveFont(16),
            ),
          ),

          Padding(
            padding: const EdgeInsets.fromLTRB(16, 16, 16, 20),
            child: AppDropdownTextfield(
              icon: icMicroscope,
              titleHeaderString: "Lab",
              valueString: widget.selectedLab?.labName ?? "",
              onTap: () {
                getLabforD2DCampCoordinator();
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
                      buttontitle: "Clean",
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
                        Navigator.pop(context);
                        widget.onTapApply();
                        // widget.onTapTeam(selectedTeam!);
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
