// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../models/sub_devices_list_response.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/AppActiveButton.dart';

class SubDeviceDropDownScreen extends StatefulWidget {
  SubDeviceDropDownScreen({
    super.key,
    required this.titleString,
    required this.dropDownList,
    required this.onApplyTap,
  });

  List<dynamic> dropDownList = [];
  String titleString = '';
  Function(dynamic) onApplyTap;

  @override
  State<SubDeviceDropDownScreen> createState() =>
      _SubDeviceDropDownScreenState();
}

class _SubDeviceDropDownScreenState extends State<SubDeviceDropDownScreen> {
  List<dynamic> searchList = [];

  @override
  void initState() {
    super.initState();
    searchList = widget.dropDownList;
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.start,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          width: MediaQuery.of(context).size.width,
          padding: const EdgeInsets.fromLTRB(0, 19, 0, 12),
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
        Expanded(
          child: ListView.builder(
            itemCount: searchList.length,
            itemBuilder: (context, index) {
              final SubDevicesOutput typeObj = searchList[index];
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
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Container(
                          padding: const EdgeInsets.all(2),
                          width: 30,
                          height: 30,
                          child: selectedRadioButton(index),
                        ),
                        const SizedBox(width: 10),
                        Column(
                          mainAxisAlignment: MainAxisAlignment.start,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              typeObj.deviceCompName ?? '',
                              style: TextStyle(
                                fontWeight: typeObj.isSelected
                                    ? FontWeight.w600
                                    : FontWeight.w400,
                                color: typeObj.isSelected
                                    ? kBlackColor
                                    : kLabelTextColor,
                                fontSize: responsiveFont(18),
                              ),
                            ),
                            Text(
                              'Serial Number: ${typeObj.deviceSerial ?? ""}',
                              style: TextStyle(
                                fontWeight: FontWeight.w400,
                                color: kLabelTextColor,
                                fontFamily: FontConstants.interFonts,
                                fontSize: responsiveFont(18),
                              ),
                            ),
                          ],
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
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Expanded(
                child: AppActiveButton(
                  buttontitle: 'Back',
                  isCancel: true,
                  onTap: () {
                    Get.back();
                  },
                ),
              ),
              const SizedBox(width: 67),
              Expanded(
                child: AppActiveButton(
                  buttontitle: 'Done',
                  onTap: () {
                    final List<dynamic> selectedList = widget.dropDownList
                        .where((item) => item.isSelected == true)
                        .toList();
                    widget.onApplyTap(selectedList);
                    Get.back();
                  },
                ),
              ),
            ],
          ),
        ),
      ],
    );
  }

  void radioButtonDidSelected(int index) {
    final SubDevicesOutput initiatedBy = searchList[index];
    initiatedBy.isSelected = !initiatedBy.isSelected;
    setState(() {});
  }

  Widget selectedRadioButton(int index) {
    final SubDevicesOutput type = searchList[index];
    return Image.asset(
      type.isSelected == true ? icCheckBoxSelected : icUnCheckBoxSelected,
    );
  }
}
