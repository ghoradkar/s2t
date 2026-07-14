// ignore_for_file: use_full_hex_values_for_flutter_colors, file_names, avoid_print, prefer_conditional_assignment

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/medicine_delivery_menu/controller/app_data_manager.dart';
import 'package:s2toperational/utilities/enums.dart';
import 'package:s2toperational/medicine_delivery_menu/controller/return_in_lab_controller.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/app_bar_code_textfield.dart';
import 'package:s2toperational/common_widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/calling_modules/widgets/network_wrapper.dart';
import 'package:s2toperational/medicine_delivery_menu/screens/return_in_lab_row.dart';
import 'package:s2toperational/common_widgets/drop_down_list_screen/drop_down_list_screen.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';
import '../../../../constants/fonts.dart';

class ReturnInLabScreen extends StatefulWidget {
  const ReturnInLabScreen({super.key});

  @override
  State<ReturnInLabScreen> createState() => _ReturnInLabScreenState();
}

class _ReturnInLabScreenState extends State<ReturnInLabScreen> {
  late final ReturnInLabController controller;
  int dISTLGDCODE = 0;
  int empCode = 0;
  bool isShowTaluka = false;

  final TextEditingController barcodeController = TextEditingController();

  bool get _allRowsSelected {
    if (controller.returnInLabList.isEmpty) return false;
    return controller.returnInLabList.every((item) => item.isSelected == true);
  }

  void _toggleAllRows() {
    if (controller.returnInLabList.isEmpty) return;
    final shouldSelectAll = !_allRowsSelected;
    for (final item in controller.returnInLabList) {
      item.isSelected = shouldSelectAll;
    }
    controller.returnInLabList.refresh();
  }

  @override
  void initState() {
    super.initState();
    controller = Get.find<ReturnInLabController>();
    dISTLGDCODE =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    isShowTaluka = false;
    controller.fetchTaluka(
      userId: empCode,
      distLgdCode: dISTLGDCODE,
      showDropdown: false,
    );
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
            child: Column(
              children: [
                const SizedBox(height: 8),
                Obx(
                  () => AppTextField(
                    controller: TextEditingController(
                      text: AppDataManager.selectedTaluka?.tALNAME ?? "",
                    ),
                    readOnly: true,
                    onTap: () {
                      isShowTaluka = true;
                      ToastManager.showLoader();
                      controller.fetchTaluka(
                        userId: empCode,
                        distLgdCode: dISTLGDCODE,
                        showDropdown: true,
                        onShowDropdown: (list) {
                          _showDropDownBottomSheet(
                            "Taluka",
                            list,
                            DropDownTypeMenu.UserMappedTaluka,
                          );
                        },
                      );
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
                    suffixIcon: const Icon(Icons.keyboard_arrow_down),
                  ),
                ),
                const SizedBox(height: 12),
                AppBarCodeTextfield(
                  titleHeaderString: "Scan packet or Delivery Challan No.",
                  controller: barcodeController,
                  onSearch: (p0) {
                    ToastManager.showLoader();
                    controller.fetchBarcodePostCampDetails(
                      barcode: barcodeController.text.trim(),
                    );
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
                        const SizedBox(width: 6),
                        Expanded(
                          child: Container(
                            decoration: BoxDecoration(
                              border: Border(
                                right: BorderSide(
                                  color: Colors.grey,
                                  width: 0.5,
                                ),
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
                        Obx(
                          () => GestureDetector(
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
                        ),
                      ],
                    ),
                  ),
                  Expanded(
                    child: Obx(
                      () => controller.returnInLabList.isEmpty
                          ? const Center(child: Text("No Data Available"))
                          : ListView.builder(
                              itemCount: controller.returnInLabList.length,
                              itemBuilder: (context, index) {
                                return ReturnInLabRow(
                                  index: index,
                                  obj: controller.returnInLabList[index],
                                  onSelectionChanged: () {
                                    controller.returnInLabList.refresh();
                                  },
                                );
                              },
                            ),
                    ),
                  ),
                ],
              ),
            ),
          ),
          Obx(
            () => controller.returnInLabList.isNotEmpty
                ? Center(
                    child: Padding(
                      padding: const EdgeInsets.fromLTRB(80, 5, 80, 5),
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
                  )
                : const SizedBox.shrink(),
          ),
        ],
      ),
    );
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
                controller.returnInLabList.refresh();
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

  void submitData() {
    final isSelected =
        controller.returnInLabList.any((p) => p.isSelected);
    final json = controller.buildPacketJson();

    if (!isSelected) {
      ToastManager.toast("Please select at least one patient");
    } else if (!controller.canAcceptInLab.value) {
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
            title: const Text("Alert"),
            content: const Text("Are you sure you want to Continue?"),
            actions: [
              TextButton(
                child: const Text("Yes"),
                onPressed: () {
                  Navigator.pop(context);
                  ToastManager.showLoader();
                  controller.insertPacketDetails(
                    json: json,
                    empCode: empCode,
                    onSuccess: () {
                      barcodeController.clear();
                      isShowTaluka = false;
                      ToastManager.showLoader();
                      controller.fetchBarcodePostCampDetails(barcode: "");
                    },
                  );
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
    final scanned = res as String;
    barcodeController.text = scanned;
    if (scanned.trim().isNotEmpty) {
      ToastManager.showLoader();
      controller.fetchBarcodePostCampDetails(barcode: scanned.trim());
    }
  }
}