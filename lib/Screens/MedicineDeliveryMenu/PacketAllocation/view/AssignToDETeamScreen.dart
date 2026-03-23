// ignore_for_file: file_names, must_be_immutable, library_private_types_in_public_api, avoid_print

import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/AppDataManager/AppDataManager.dart';
import 'package:s2toperational/Modules/DelegateManager/DelegateManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/Json_Class/PacketAcceptDataResponse/PacketAcceptDataResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/PacketAllocation/view/AssignToDETeamRow.dart';
import '../../../../../Modules/constants/fonts.dart';

class AssignToDETeamScreen extends StatefulWidget {
  const AssignToDETeamScreen({super.key});

  @override
  State<AssignToDETeamScreen> createState() => _AssignToDETeamScreenState();
}

class _AssignToDETeamScreenState extends State<AssignToDETeamScreen>
    implements RefreshDelegate {
  APIManager apiManager = APIManager();
  List<PacketAcceptDataOutput> listOfPackets = [];
  List<PacketAcceptDataOutput> listOfPacketsSearch = [];

  TextEditingController searchController = TextEditingController();

  bool get _allRowsSelected {
    if (listOfPacketsSearch.isEmpty) {
      return false;
    }
    return listOfPacketsSearch.every((item) => item.isSelected == true);
  }

  void _toggleAllRows() {
    if (listOfPacketsSearch.isEmpty) {
      return;
    }
    final shouldSelectAll = !_allRowsSelected;
    for (final item in listOfPacketsSearch) {
      item.isSelected = shouldSelectAll;
    }
    setState(() {});
  }

  @override
  void initState() {
    super.initState();
    DelegateManager().registerDelegate(this);
    if (AppDataManager.fromDate.isEmpty) {
      AppDataManager.fromDate = FormatterManager.formatDateToString(
        DateTime.now(),
      );
    }
    if (AppDataManager.toDate.isEmpty) {
      AppDataManager.toDate = FormatterManager.formatDateToString(
        DateTime.now(),
      );
    }
    callRefreshAPI();
    // ToastManager.showLoader();
    // getDataForPacketAssignment();
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Column(
        children: [

          AppTextField(
            controller: searchController,
            readOnly: false,
            onChange: (value) {
              listOfPacketsSearch = searchByDescEn(value);
              setState(() {});
            },
            hint: 'Search Beneficiary Name',
            label: CommonText(
              text: 'Search Beneficiary Name',
              fontSize: 12.sp,
              fontWeight: FontWeight.normal,
              textColor: kBlackColor,
              textAlign: TextAlign.start,
            ),
            textInputType: TextInputType.text,
            hintStyle: TextStyle(
              fontSize: 12.sp,
              fontWeight: FontWeight.w400,
              fontFamily: FontConstants.interFonts,
            ),
            fieldRadius: 10,
            suffixIcon: SizedBox(
              height: 20.h,
              width: 20.w,
              child: Center(
                child: Image.asset(
                  icSearch,
                  height: 24.h,
                  width: 24.w,
                  fit: BoxFit.contain,
                ),
              ),
            ),
          ).paddingOnly(bottom: 8),

          Expanded(
            child: Container(
              color: Colors.transparent,
              child: Column(
                children: [
                  Container(
                    decoration: BoxDecoration(
                      color: kPrimaryColor,
                      borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(8),
                        topRight: Radius.circular(8),
                      ),
                    ),
                    height: 40,
                    child: Row(
                      children: [
                        Container(
                          width: 40,
                          decoration: BoxDecoration(
                            border: Border(
                              right: BorderSide(color: Colors.grey, width: 0.5),
                            ),
                          ),
                          child: Center(
                            child: Text(
                              "Sr. No.",
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                color: Colors.white,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w500,
                                fontSize: responsiveFont(12),
                              ),
                            ),
                          ),
                        ),
                        SizedBox(width: 6),
                        Expanded(
                          child: Container(
                            decoration: BoxDecoration(
                              border: Border(
                                right: BorderSide(color: Colors.grey, width: 0.5),
                              ),
                            ),
                            child: Center(
                              child: Text(
                                "Patient Name",
                                textAlign: TextAlign.center,
                                style: TextStyle(
                                  color: Colors.white,
                                  fontFamily: FontConstants.interFonts,
                                  fontWeight: FontWeight.w500,
                                  fontSize: responsiveFont(12),
                                ),
                              ),
                            ),
                          ),
                        ),
                        Container(
                          width: 110,
                          decoration: BoxDecoration(
                            border: Border(
                              right: BorderSide(color: Colors.grey, width: 0.5),
                            ),
                          ),
                          child: Center(
                            child: Text(
                              "Pack No.",
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                color: Colors.white,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w500,
                                fontSize: responsiveFont(12),
                              ),
                            ),
                          ),
                        ),
                        Container(
                          width: 60,
                          decoration: BoxDecoration(
                            border: Border(
                              right: BorderSide(color: Colors.grey, width: 0.5),
                            ),
                          ),
                          child: Center(
                            child: Text(
                              "Delivery\nChallan No.",
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                color: Colors.white,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w500,
                                fontSize: responsiveFont(12),
                              ),
                            ),
                          ),
                        ),
                        GestureDetector(
                          onTap: _toggleAllRows,
                          child: Container(
                            padding: EdgeInsets.all(4),
                            width: 30,
                            height: 30,
                            child: Image.asset(
                              _allRowsSelected
                                  ? icCheckBoxSelected
                                  : icUnCheckBoxSelected,
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                  Expanded(
                    child:
                        listOfPacketsSearch.isNotEmpty
                            ? ListView.builder(
                              itemCount: listOfPacketsSearch.length,
                              itemBuilder: (context, index) {
                                return AssignToDETeamRow(
                                  index: index,
                                  obj: listOfPacketsSearch[index],
                                  onSelectionChanged: () {
                                    setState(() {});
                                  },
                                );
                              },
                            )
                            : Center(child: Text("No Data Available")),
                  ),
                  if (listOfPacketsSearch.isNotEmpty)
                    Center(
                      child: Padding(
                        padding: EdgeInsets.fromLTRB(80, 5, 80, 5),
                        child: AppButtonWithIcon(
                          buttonColor: kPrimaryColor,
                          title: "Packet Assign",
                          icon: Image.asset(
                            iconArrow,
                            height: responsiveHeight(24),
                            width: responsiveHeight(24),
                          ),
                          mWidth: SizeConfig.screenWidth,
                          textStyle: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: Colors.white,
                            fontSize: responsiveFont(16),
                          ),
                          onTap: () {
                            submitData();
                          },
                        ),
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

  @override
  void callRefreshAPI() {
    ToastManager.showLoader();
    getDataForPacketAssignment();
  }

  void getDataForPacketAssignment() {
    Map<String, String> data = {
      "FromDate": AppDataManager.fromDate,
      "ToDate": AppDataManager.toDate,
      "Labcode": "0",
      "TALLGDCODE": AppDataManager.selectedTaluka?.tALLGDCODE.toString() ?? "0",
    };
    //AppDataManager.selectedReportDeliveryExecutive?.d.toString() ??

    apiManager.getDataForPacketAssignmentAPI(
      data,
      apiDataForPacketAssignmentBack,
    );
  }

  void apiDataForPacketAssignmentBack(
    PacketAcceptDataResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      listOfPackets = response?.output ?? [];
      listOfPacketsSearch = listOfPackets;
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  List<PacketAcceptDataOutput> searchByDescEn(String query) {
    return listOfPackets.where((item) {
      final desc = item.patientName?.toString().toLowerCase() ?? '';
      return desc.contains(query.toLowerCase());
    }).toList();
  }

  String getPacketDetailsJson() {
    List<Map<String, String>> packetJsonArray = [];

    for (PacketAcceptDataOutput packetObj in listOfPackets) {
      if (packetObj.isSelected) {
        final dict = {
          "DCID": packetObj.deliveryChallanID ?? "",
          "PacketID": packetObj.packetNumber ?? "",
          "TreatmentID": packetObj.prescriptionID?.toString() ?? "",
          "TeamId": AppDataManager.selectedResource?.teamid.toString() ?? "0",
        };
        packetJsonArray.add(dict);
      }
    }

    try {
      String json = jsonEncode(packetJsonArray);
      print(json);
      return json;
    } catch (e) {
      print("something went wrong with parsing json: $e");
      return "";
    }
  }

  void submitData() {
    bool isSelected = false;
    for (PacketAcceptDataOutput packetObj in listOfPackets) {
      if (packetObj.isSelected) {
        isSelected = true;
        break;
      }
    }

    if (AppDataManager.toDate.isEmpty) {
      ToastManager.toast("Please select To Date");
      return;
    }
    if (AppDataManager.selectedResource == null) {
      ToastManager.toast("Please select report delivery executive");
      return;
    }
    if (!isSelected) {
      ToastManager.toast("Please select at least one patient");
      return;
    }
    // showDialog(
    //   context: context,
    //   builder: (BuildContext context) {
    //     return AlertDialog(
    //       title: Text("Alert"),
    //       content: Text("Are you sure you want to Continue?"),
    //       actions: [
    //         TextButton(
    //           child: const Text("Yes"),
    //           onPressed: () {
    //             Navigator.pop(context);
    //             ToastManager.showLoader();
    //             insertPacketAssignDetailsManually();
    //           },
    //         ),
    //         TextButton(
    //           child: const Text("No"),
    //           onPressed: () {
    //             Navigator.pop(context);
    //           },
    //         ),
    //       ],
    //     );
    //   },
    // );

    ToastManager().showConfirmationDialog(
      context: context,
      message: 'Are you sure you want to Continue?',
      didSelectYes: (bool p1) {
        if(p1 == true){
          Navigator.pop(context);
          ToastManager.showLoader();
          insertPacketAssignDetailsManually();
        }else  if(p1 == false){
          Navigator.pop(context);

        }
      },
    );
  }

  void insertPacketAssignDetailsManually() {
    String packetDetails = getPacketDetailsJson();

    if (packetDetails.isEmpty) {
      ToastManager.toast("No packets selected for assignment");
      return;
    }
    int empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    Map<String, String> data = {
      "CreatedBy": empCode.toString(),
      "PacketDetails": packetDetails,
      "DeliveryExecutiveID":
          AppDataManager.selectedResource?.userID.toString() ?? "0",
    };

    apiManager.insertPacketAssignDetailsManuallyAPI(
      data,
      apiInsertPacketAssignDetailsManuallyBack,
    );
  }

  void apiInsertPacketAssignDetailsManuallyBack(
    PacketAcceptDataResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      ToastManager.toast("Packet assigned successfully");
      listOfPacketsSearch = [];
      listOfPackets = [];
      searchController.clear();
      callRefreshAPI();
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }
}
