// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';

import '../../../../Modules/Enums/Enums.dart';
import '../../../../Modules/Json_Class/BindDistrictResponse/BindDistrictResponse.dart';
import '../../../../Modules/Json_Class/LabByUserIDResponse/LabByUserIDResponse.dart';
import '../../../../Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import '../../../../Modules/ToastManager/ToastManager.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/fonts.dart';
import '../../../../Modules/utilities/DataProvider.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/widgets/AppActiveButton.dart';
import '../../../../Views/DropDownListScreen/DropDownListScreen.dart';

class D2DTeamFilterView extends StatefulWidget {
  D2DTeamFilterView({
    super.key,
    required this.selectedDistrictValue,
    required this.selectedLabValue,
    required this.onApplyTap,
  });

  Function() onApplyTap;

  Function(BindDistrictOutput?) selectedDistrictValue;
  Function(LabByUserIDOutput?) selectedLabValue;
  @override
  State<D2DTeamFilterView> createState() => _D2DTeamFilterViewState();
}

class _D2DTeamFilterViewState extends State<D2DTeamFilterView> {
  int userID = 0;
  int organizationId = 0;
  int dIVID = 0;
  int dISTLGDCODE = 0;
  int dESGID = 0;
  BindDistrictOutput? selectedDistrict;
  LabByUserIDOutput? selectedLab;
  APIManager apiManager = APIManager();

  @override
  void initState() {
    LoginResponseModel? loginResponseModel = DataProvider().getParsedUserData();
    userID = loginResponseModel?.output?.first.empCode ?? 0;
    dESGID = loginResponseModel?.output?.first.dESGID ?? 0;
    userID = loginResponseModel?.output?.first.empCode ?? 0;
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 12, 20, 12),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: MediaQuery.of(context).size.width,
            decoration: const BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.only(
                topLeft: Radius.circular(20),
                topRight: Radius.circular(20),
              ),
            ),
            child: Center(
              child: Text(
                "Filters",
                style: TextStyle(
                  fontWeight: FontWeight.normal,
                  color: kBlackColor,
                  fontFamily: FontConstants.interFonts,
                  fontSize: responsiveFont(18),
                ),
              ),
            ),
          ),
          const SizedBox(height: 8),
          // AppDropdownTextfield(
          //   icon: icMapPin,
          //   titleHeaderString: "District",
          //   valueString: selectedDistrict?.dISTNAME ?? "",
          //   onTap: () {
          //     getDistrictList();
          //   },
          // ),
          AppTextField(
            controller: TextEditingController(
              text: selectedDistrict?.dISTNAME ?? "",
            ),
            readOnly: true,
            onTap: (){
              getDistrictList();

            },
            hint: 'District',
            label: CommonText(
              text: 'District',
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
            controller: TextEditingController(
              text: selectedLab?.labName ?? "",
            ),
            readOnly: true,
            onTap: (){
              getTaluka();

            },
            hint: 'Lab',
            label: CommonText(
              text: 'Lab',
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
                  icLandingLab,
                  height: 24.h,
                  width: 24.w,
                  fit: BoxFit.contain,
                ),
              ),
            ),
            suffixIcon: Icon(Icons.keyboard_arrow_down),
          ),
          //
          // AppDropdownTextfield(
          //   icon: icLandingLab,
          //   titleHeaderString: "Lab",
          //   valueString: selectedLab?.labName ?? "",
          //   onTap: () {
          //     getTaluka();
          //   },
          // ),
          const SizedBox(height: 28),
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
                  const SizedBox(width: 16),
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Apply",
                      onTap: () {
                        Navigator.pop(context);
                        widget.onApplyTap();
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


  void getDistrictList() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "SubOrgId": "$organizationId",
      "UserID": userID.toString(),
      "DESGID": dESGID.toString(),
      "DIVID": "0",
      "DISTLGDCODE": dISTLGDCODE.toString(),
    };
    apiManager.getBindDistrictAPI(params, apiBindDistrictCallBack);
  }

  void apiBindDistrictCallBack(
      BindDistrictResponse? response,
      String errorMessage,
      bool success,
      ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "District",
        response?.output ?? [],
        DropDownTypeMenu.BindDistrict,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getTaluka() {
    if (selectedDistrict == null) {
      ToastManager.toast("Please select district");
    } else {
      ToastManager.showLoader();
      Map<String, String> params = {
        "DISTLGDCODE": selectedDistrict?.dISTLGDCODE?.toString() ?? "0",
        "USERID": userID.toString(),
      };
      apiManager.getTalukaPacketReciveAPI(
        params,
        apiTalukaPacketReciveCallBack,
      );
    }
  }

  void apiTalukaPacketReciveCallBack(
      LabByUserIDResponse? response,
      String errorMessage,
      bool success,
      ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Select Lab",
        response?.output ?? [],
        DropDownTypeMenu.TalukaPacketRecive,
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
              if (dropDownType == DropDownTypeMenu.BindDistrict) {
                selectedDistrict = p0;
                selectedLab = null;
                widget.selectedLabValue(null);
                widget.selectedDistrictValue(p0);
              } else if (dropDownType == DropDownTypeMenu.TalukaPacketRecive) {
                selectedLab = p0;
                widget.selectedLabValue(p0);
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
