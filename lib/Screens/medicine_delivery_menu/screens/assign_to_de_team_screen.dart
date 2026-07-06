// ignore_for_file: file_names, must_be_immutable, library_private_types_in_public_api, avoid_print

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/controller/app_data_manager.dart';
import 'package:s2toperational/Modules/utilities/formatter_manager.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/AppButtonWithIcon.dart';
import 'package:s2toperational/Modules/common_widgets/AppTextField.dart';
import 'package:s2toperational/Modules/common_widgets/CommonText.dart';
import 'package:s2toperational/Screens/calling_modules/widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/controller/packet_allocation_controller.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/assign_to_de_team_row.dart';
import '../../../../Modules/constants/fonts.dart';

class AssignToDETeamScreen extends StatefulWidget {
  const AssignToDETeamScreen({super.key});

  @override
  State<AssignToDETeamScreen> createState() => _AssignToDETeamScreenState();
}

class _AssignToDETeamScreenState extends State<AssignToDETeamScreen> {
  late final PacketAllocationController controller;
  final TextEditingController searchController = TextEditingController();

  bool get _allRowsSelected {
    if (controller.listOfPacketsSearch.isEmpty) return false;
    return controller.listOfPacketsSearch.every((item) => item.isSelected == true);
  }

  void _toggleAllRows() {
    if (controller.listOfPacketsSearch.isEmpty) return;
    final shouldSelectAll = !_allRowsSelected;
    for (final item in controller.listOfPacketsSearch) {
      item.isSelected = shouldSelectAll;
    }
    controller.listOfPacketsSearch.refresh();
  }

  @override
  void initState() {
    super.initState();
    controller = Get.find<PacketAllocationController>();
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
    controller.fetchPackets();
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
              controller.filterByName(value);
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
                        ),
                      ],
                    ),
                  ),
                  Expanded(
                    child: Obx(
                      () => controller.listOfPacketsSearch.isNotEmpty
                          ? ListView.builder(
                              itemCount: controller.listOfPacketsSearch.length,
                              itemBuilder: (context, index) {
                                return AssignToDETeamRow(
                                  index: index,
                                  obj: controller.listOfPacketsSearch[index],
                                  onSelectionChanged: () {
                                    controller.listOfPacketsSearch.refresh();
                                  },
                                );
                              },
                            )
                          : Center(child: Text("No Data Available")),
                    ),
                  ),
                  Obx(
                    () => controller.listOfPacketsSearch.isNotEmpty
                        ? Padding(
                            padding: EdgeInsets.only(
                              bottom: MediaQuery.viewPaddingOf(context).bottom,
                            ),
                            child: Center(
                              child: Padding(
                                padding: EdgeInsets.fromLTRB(80, 8, 80, 8),
                                child: AppButtonWithIcon(
                                  buttonColor: kPrimaryColor,
                                  title: "Accept in Lab",
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
                          )
                        : const SizedBox.shrink(),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  void submitData() {
    final isSelected = controller.listOfPackets.any((p) => p.isSelected);

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

    ToastManager().showConfirmationDialog(
      context: context,
      message: 'Are you sure you want to Continue?',
      didSelectYes: (bool p1) {
        if (p1 == true) {
          Navigator.pop(context);
          ToastManager.showLoader();
          controller.insertPacketAssignDetailsManually(
            onSuccess: () => searchController.clear(),
          );
        } else if (p1 == false) {
          Navigator.pop(context);
        }
      },
    );
  }
}