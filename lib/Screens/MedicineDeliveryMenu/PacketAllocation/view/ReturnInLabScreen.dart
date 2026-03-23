// ignore_for_file: use_full_hex_values_for_flutter_colors, file_names, avoid_print, prefer_conditional_assignment

import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/AppDataManager/AppDataManager.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/Json_Class/PatientListReAllocationforMedicineDeliveryResponse/PatientListReAllocationforMedicineDeliveryResponse.dart';
import 'package:s2toperational/Modules/Json_Class/UserMappedTalukaResponse/UserMappedTalukaResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppBarCodeTextfield.dart';
import 'package:s2toperational/Modules/widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/PacketAllocation/view/ReturnInLabRow.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';
import '../../../../../Modules/constants/fonts.dart';

class ReturnInLabScreen extends StatefulWidget {
  const ReturnInLabScreen({super.key});

  @override
  State<ReturnInLabScreen> createState() => _ReturnInLabScreenState();
}

class _ReturnInLabScreenState extends State<ReturnInLabScreen> {
  APIManager apiManager = APIManager();
  int dISTLGDCODE = 0;
  int empCode = 0;
  bool isShowTaluka = false;

  bool isPacketSelected = false;
  bool canAcceptInLab = true;
  List<PatientListReAllocationforMedicineDeliveryOutput>? returnInLabList;

  TextEditingController barcodeController = TextEditingController();

  String resultBarCode = "";

  bool get _allRowsSelected {
    final list = returnInLabList;
    if (list == null || list.isEmpty) {
      return false;
    }
    return list.every((item) => item.isSelected == true);
  }

  void _toggleAllRows() {
    final list = returnInLabList;
    if (list == null || list.isEmpty) {
      return;
    }
    final shouldSelectAll = !_allRowsSelected;
    for (final item in list) {
      item.isSelected = shouldSelectAll;
    }
    setState(() {});
  }

  @override
  void initState() {
    super.initState();

    dISTLGDCODE =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;

    isShowTaluka = false;
    getTaluka();
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Column(
        children: [
          Container(
            decoration: BoxDecoration(
              boxShadow: const [
                BoxShadow(
                  offset: Offset(0, 1),
                  color: Color(0xff00000026),
                  spreadRadius: 0,
                  blurRadius: 4,
                ),
              ],
              color: const Color(0XFFFFFFFF),
              borderRadius: BorderRadius.circular(10),
            ),
            // padding: EdgeInsets.all(8),
            child: Column(
              children: [
                // AppDropdownTextfield(
                //   icon: icMapPin,
                //   titleHeaderString: "Taluka *",
                //   valueString: AppDataManager.selectedTaluka?.tALNAME ?? "",
                //   onTap: () {
                //     isShowTaluka = true;
                //     getTaluka();
                //   },
                // ),
                const SizedBox(height: 8),

                AppTextField(
                  controller: TextEditingController(
                    text: AppDataManager.selectedTaluka?.tALNAME ?? "",
                  ),
                  readOnly: true,
                  onTap: () {
                    isShowTaluka = true;
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
                const SizedBox(height: 12),

                AppBarCodeTextfield(
                  titleHeaderString: "Scan packet or Delivery Challan No.",
                  controller: barcodeController,
                  onSearch: (p0) {
                    ToastManager.showLoader();
                    getBarcodePostCampDetails();
                  },
                  onBarcodeScanned: () {
                    openBarCodeScanner();
                  },
                ),
              ],
            ),
          ),
          const SizedBox(height: 12),
          Expanded(
            child: Container(
              color: Colors.transparent,
              child: Column(
                children: [
                  Container(
                    decoration: BoxDecoration(
                      color: kPrimaryColor,
                      border: Border(
                        top: BorderSide(color: Colors.grey, width: 1),
                        left: BorderSide(color: Colors.grey, width: 1),
                        right: BorderSide(color: Colors.grey, width: 1),
                      ),
                      borderRadius: BorderRadius.only(
                        topLeft: Radius.circular(8),
                        topRight: Radius.circular(8),
                      ),
                    ),
                    height: 40,
                    child: Row(
                      children: [
                        Container(
                          width: 48,
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
                            padding: const EdgeInsets.all(8),
                            width: 46,
                            height: 40,
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
                        child:(returnInLabList == null || returnInLabList == [])
                            ? Center(child: Text("No Data Available"))
                            :  ListView.builder(
                          itemCount: returnInLabList?.length ?? 0,
                          itemBuilder: (context, index) {
                            return ReturnInLabRow(
                              index: index,
                              obj: returnInLabList![index],
                              onSelectionChanged: () {
                                setState(() {});
                              },
                            );
                          },
                        ),
                      ),
                ],
              ),
            ),
          ),
          if (returnInLabList != null && returnInLabList!.isNotEmpty)
            Center(
              child: Padding(
                padding: EdgeInsets.fromLTRB(80, 5, 80, 5),
                child: AppButtonWithIcon(
                  buttonColor: kPrimaryColor,
                  title: "Accept In Lab",
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
    );
  }

  void getTaluka() {
    Map<String, String> data = {
      "UserId": empCode.toString(),
      "DISTLGDCODE": dISTLGDCODE.toString(),
    };
    apiManager.getUserMappedTalukaAPI(data, apiUserMappedTalukaBack);
  }

  void apiUserMappedTalukaBack(
    UserMappedTalukaResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      if (isShowTaluka) {
        ToastManager.hideLoader();
        _showDropDownBottomSheet(
          "Taluka",
          response?.output ?? [],
          DropDownTypeMenu.UserMappedTaluka,
        );
      } else {
        if (AppDataManager.selectedTaluka == null) {
          AppDataManager.selectedTaluka = response?.output?.first;
        }
        // getBarcodePostCampDetails();
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getBarcodePostCampDetails() {
    String barcode = barcodeController.text.trim();
    Map<String, String> data = {
      "FromDate": "",
      "ToDate": "",
      "Labcode": "0",
      "TALLGDCODE": AppDataManager.selectedTaluka?.tALLGDCODE.toString() ?? "",
      "PacketID": barcode,
    };
    apiManager.getBarcodePostCampDetailsAPI(
      data,
      apiBarcodePostCampDetailsBack,
    );
  }

  void apiBarcodePostCampDetailsBack(
    PatientListReAllocationforMedicineDeliveryResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      returnInLabList = response?.output ?? [];
      canAcceptInLab = true;
      int packetSelect = 0;
      int packetCollect = 0;
      for (final item
          in returnInLabList ??
              <PatientListReAllocationforMedicineDeliveryOutput>[]) {
        if (item.overallStatusID == 1) {
          packetSelect += 1;
        }
        if (item.overallStatusID == 2) {
          packetCollect += 1;
        }
      }
      if (packetSelect > 0) {
        ToastManager.toast(
          "Failure to accept return, process collection of packet first and try again.",
        );
        canAcceptInLab = false;
      }
      if (packetCollect > 0) {
        ToastManager.toast(
          "Failure to accept return, process receiving of packet first and try again.",
        );
        canAcceptInLab = false;
      }
    } else {
      returnInLabList = [];
      ToastManager.toast(errorMessage);
      canAcceptInLab = false;
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

  String jsonToString() {
    List<Map<String, String>> packetJsonArray = [];
    isPacketSelected = false;
    for (PatientListReAllocationforMedicineDeliveryOutput packetObj
        in returnInLabList!) {
      if (packetObj.isSelected) {
        isPacketSelected = true;
        final dict = {
          "DISTLGDCODE": packetObj.dISTLGDCODE?.toString() ?? "",
          "TALLGDCODE":
              AppDataManager.selectedTaluka?.tALLGDCODE?.toString() ?? "",
          "Labcode": packetObj.labcode?.toString() ?? "",
          "PacketID": packetObj.packetNumber ?? "",
          "TreatmentID": packetObj.prescriptionID?.toString() ?? "",
          "OverallStatusID": packetObj.overallStatusID?.toString() ?? "",
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
    String json = jsonToString();

    if (!isPacketSelected) {
      ToastManager.toast("Please select at least one patient");
    } else if (!canAcceptInLab) {
      ToastManager.toast(
        "Failure to accept return, process collection/receiving of packet first.",
      );
    } else if (json.isEmpty) {
      ToastManager.toast("Data is not valid for submission");
    } else {
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text("Alert"),
            content: Text("Are you sure you want to Continue?"),
            actions: [
              TextButton(
                child: const Text("Yes"),
                onPressed: () {
                  Navigator.pop(context);
                  ToastManager.showLoader();
                  insertPacketDetails(json);
                },
              ),
              TextButton(
                child: const Text("No"),
                onPressed: () {
                  Navigator.pop(context);
                },
              ),
            ],
          );
        },
      );
    }
  }

  void insertPacketDetails(String json) {
    Map<String, String> data = {
      "CW_ReallocationMedicalDelivary": json,
      "USERID": empCode.toString(),
    };
    apiManager.insertPacketDetailsAPI(data, apiInsertPacketDetailsBack);
  }

  void apiInsertPacketDetailsBack(
    PatientListReAllocationforMedicineDeliveryResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      ToastManager.hideLoader();
      ToastManager.toast("Packet Accepted successfully");
      barcodeController.clear();
      isShowTaluka = false;
      ToastManager.showLoader();
      getBarcodePostCampDetails();
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void openBarCodeScanner() async {
    String? res = await SimpleBarcodeScanner.scanBarcode(
      context,
      barcodeAppBar: const BarcodeAppBar(
        appBarTitle: 'Scan Bar Code',
        centerTitle: false,
        enableBackButton: false,
        backButtonIcon: Icon(Icons.arrow_back_ios),
      ),
      isShowFlashIcon: false,
      delayMillis: 2000,
      cameraFace: CameraFace.back,
    );
    setState(() {
      resultBarCode = res as String;
      barcodeController.text = resultBarCode;
    });
    if (resultBarCode.trim().isNotEmpty) {
      ToastManager.showLoader();
      getBarcodePostCampDetails();
    }
  }
}
