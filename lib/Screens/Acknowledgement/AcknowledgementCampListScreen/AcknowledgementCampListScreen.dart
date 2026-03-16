// ignore_for_file: avoid_print, file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';

import '../../../Modules/APIManager/APIManager.dart';
import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import '../../../Modules/Json_Class/ResourceReMappingCampResponse/ResourceReMappingCampResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppDateTextfield.dart';
import '../../../Modules/widgets/AppIconSearchTextfield.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../AcknowledgementPatientListScreen/AcknowledgementPatientListScreen.dart';
import 'AcknowledgementCampRow/AcknowledgementCampRow.dart';

class AcknowledgementCampListScreen extends StatefulWidget {
  const AcknowledgementCampListScreen({super.key});

  @override
  State<AcknowledgementCampListScreen> createState() =>
      _AcknowledgementCampListScreenState();
}

class _AcknowledgementCampListScreenState
    extends State<AcknowledgementCampListScreen> {
  TextEditingController searchController = TextEditingController();
  String _selectedCampDate = "";
  APIManager apiManager = APIManager();

  int dESGID = 0;

  int subOrgId = 0;
  int dISTLGDCODE = 0;
  int empCode = 0;
  int cityCode = 0;

  List<ResourceReMappingCampOutput> campList = [];
  List<ResourceReMappingCampOutput> searchCampList = [];

  @override
  void initState() {
    super.initState();
    _selectedCampDate = FormatterManager.formatDateToString(DateTime.now());

    LoginOutput? userLoginDetails =
        DataProvider().getParsedUserData()?.output?.first;
    dESGID = userLoginDetails?.dESGID ?? 0;
    subOrgId = userLoginDetails?.subOrgId ?? 0;
    empCode = userLoginDetails?.empCode ?? 0;
    dISTLGDCODE = userLoginDetails?.dISTLGDCODE ?? 0;
    cityCode = userLoginDetails?.cityCode ?? 0;
    apiCall();
  }

  void apiCall() {
    if (DataProvider().getRegularCamp()) {
      getApprovedCampListDetailsForApp();
    } else {
      getCampDetailsonLabForDoorToDoor();
    }
  }

  void getApprovedCampListDetailsForApp() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "CampDATE": _selectedCampDate,
      "UserId": empCode.toString(),
    };
    print(params);
    apiManager.getApprovedCampListDetailsForAppAPI(
      params,
      apiApprovedCampListDetailsForAppCallBack,
    );
  }

  void apiApprovedCampListDetailsForAppCallBack(
    ResourceReMappingCampResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      campList = response?.output ?? [];
      searchCampList = campList;
    } else {
      campList = [];
      searchCampList = campList;
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  getCampDetailsonLabForDoorToDoor() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "CampDate": _selectedCampDate,
      "LabCode": cityCode.toString(),
      "SubOrgId": subOrgId.toString(),
      "Divison": "0",
      "DISTLGDCODE": dISTLGDCODE.toString(),
      "USERID": empCode.toString(),
      "DesgId": dESGID.toString(),
    };
    print(params);
    apiManager.getCampDetailsonLabForDoorToDoorV2API(
      params,
      apiCampDetailsonLabForDoorToDoorCallBack,
    );
  }

  void apiCampDetailsonLabForDoorToDoorCallBack(
    ResourceReMappingCampResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      campList = response?.output ?? [];
      searchCampList = campList;
    } else {
      campList = [];
      searchCampList = campList;
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  Future<void> _selectCampDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        _selectedCampDate = FormatterManager.formatDateToString(picked);
        apiCall();
      });
    }
  }

  List<ResourceReMappingCampOutput> searchByDescEn(String query) {
    return campList.where((item) {
      final desc = item.campId?.toString().toLowerCase() ?? '';
      return desc.contains(query.toLowerCase());
    }).toList();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Select Camp",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Stack(
            children: [
              Positioned(
                top: 74,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect4,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(300.37),
                ),
              ),
              Positioned(
                top: 53,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect3,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(300.37),
                ),
              ),
              Positioned(
                top: 30,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect2,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(300.37),
                ),
              ),
              Image.asset(
                fit: BoxFit.fill,
                rect1,
                width: SizeConfig.screenWidth,
                height: responsiveHeight(300.37),
              ),
              Positioned(
                top: 0,
                bottom: 8,
                left: 8,
                right: 8,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    SizedBox(
                      width: SizeConfig.screenWidth,
                      child: Row(
                        children: [
                          Expanded(
                            child: Container(
                              width: MediaQuery.of(context).size.width,
                              color: Colors.transparent,
                              child: Expanded(
                                child: AppDateTextfield(
                                  icon: icCalendarMonth,
                                  titleHeaderString: "Camp Date*",
                                  valueString: _selectedCampDate,
                                  onTap: () {
                                    _selectCampDate(context);
                                  },
                                ),
                              ),
                            ),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: AppIconSearchTextfield(
                              icon: icSearch,
                              titleHeaderString: "Search Camp ID",
                              controller: searchController,
                              textInputType: TextInputType.number,
                              onChange: (value) {
                                print(value);
                                searchCampList = searchByDescEn(value);
                                setState(() {});
                              },
                            ),
                          ),
                        ],
                      ),
                    ),
                    const SizedBox(height: 8),
                    Expanded(
                      child: ListView.builder(
                        itemCount: searchCampList.length,
                        itemBuilder: (context, index) {
                          ResourceReMappingCampOutput reMappingCampOutput =
                              searchCampList[index];
                          return AcknowledgementCampRow(
                            reMappingCampOutput: reMappingCampOutput,
                            isRegular: DataProvider().getRegularCamp(),
                            onSelectTap: () {
                              if (dESGID == 141 ||
                                  dESGID == 136 ||
                                  dESGID == 92) {
                                //PhysicalExaminationViewController
                              } else {
                                //CampPatientListViewController

                                if (DataProvider().getRegularCamp()) {
                                  print("getRegularCamp");
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder:
                                          (
                                            context,
                                          ) => AcknowledgementPatientListScreen(
                                            campID:
                                                reMappingCampOutput.campId ?? 0,
                                            districtID: 0,
                                            districtName:
                                                reMappingCampOutput.dISTNAME ??
                                                "",
                                            siteDetailId:
                                                reMappingCampOutput
                                                    .siteDetailId ??
                                                0,
                                            testId: 9,
                                            teamid: 0,
                                          ),
                                    ),
                                  );
                                } else {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder:
                                          (
                                            context,
                                          ) => AcknowledgementPatientListScreen(
                                            campID:
                                                reMappingCampOutput.campId ?? 0,
                                            districtID:
                                                reMappingCampOutput
                                                    .dISTLGDCODE ??
                                                0,
                                            districtName:
                                                reMappingCampOutput.dISTNAME ??
                                                "",
                                            siteDetailId:
                                                reMappingCampOutput
                                                    .siteDetailId ??
                                                0,
                                            testId: 3,
                                            teamid: 0,
                                          ),
                                    ),
                                  );
                                }
                              }
                            },
                          );
                        },
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
