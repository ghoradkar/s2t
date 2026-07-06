// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/AppTextField.dart';
import 'package:s2toperational/Modules/common_widgets/CommonText.dart';
import 'package:s2toperational/Modules/common_widgets/S2TAppBar.dart';
import '../controllers/resource_re_mapping_update_controller.dart';
import '../models/CampResourceAllocationResponse.dart';
import '../models/ResourceReMappingCampResponse.dart';
import '../widgets/add_remove_resource_drop_down_screen.dart';
import '../widgets/resource_re_mapping_camp_details.dart';

class ResourceReMappingUpdateScreen extends StatelessWidget {
  ResourceReMappingUpdateScreen({super.key, required this.reMappingCampOutput});

  ResourceReMappingCampOutput reMappingCampOutput;

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return GetBuilder<ResourceReMappingUpdateController>(
      init: ResourceReMappingUpdateController(
        reMappingCampOutput: reMappingCampOutput,
      ),
      dispose: (_) => Get.delete<ResourceReMappingUpdateController>(),
      builder: (ctrl) {
        return KeyboardDismissOnTap(
          child: Scaffold(
            appBar: mAppBar(
              scTitle: 'Camp Update',
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () {
                Get.back();
              },
            ),
            body: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.start,
              children: [
                ResourceReMappingCampDetails(campDetails: ctrl.campDetails),
                SizedBox(height: 8.h),
                Text(
                  'Resource Allocation',
                  style: TextStyle(
                    color: Colors.black,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w600,
                    fontSize: responsiveFont(16),
                  ),
                ),
                SizedBox(height: 4.h),
                Expanded(
                  child: ListView.builder(
                    itemCount: ctrl.resourceAllocationList.length,
                    itemBuilder: (context, index) {
                      final CampResourceAllocationOutput object =
                          ctrl.resourceAllocationList[index];
                      return IntrinsicHeight(
                        child: Padding(
                          padding: EdgeInsets.fromLTRB(0, 4.h, 0, 4.h),
                          child: Container(
                            width: MediaQuery.of(context).size.width,
                            decoration: const BoxDecoration(
                              color: Colors.white,
                            ),
                            padding: const EdgeInsets.fromLTRB(0, 6, 0, 6),
                            child: Row(
                              mainAxisAlignment: MainAxisAlignment.start,
                              crossAxisAlignment: CrossAxisAlignment.center,
                              children: [
                                Expanded(
                                  child: Padding(
                                    padding: EdgeInsets.fromLTRB(
                                      4.w,
                                      0,
                                      4.w,
                                      0,
                                    ),
                                    child: RichText(
                                      text: TextSpan(
                                        text: 'Role : ',
                                        style: TextStyle(
                                          color: Colors.black,
                                          fontFamily: FontConstants.interFonts,
                                          fontWeight: FontWeight.w600,
                                          fontSize: 16.sp,
                                        ),
                                        children: [
                                          TextSpan(
                                            text: object.testName ?? '',
                                            style: TextStyle(
                                              color: Colors.black,
                                              fontFamily:
                                                  FontConstants.interFonts,
                                              fontWeight: FontWeight.w400,
                                              fontSize: 16.sp,
                                            ),
                                          ),
                                        ],
                                      ),
                                      softWrap: true,
                                    ),
                                  ),
                                ),
                                SizedBox(
                                  width: 212.w,
                                  child: AppTextField(
                                    controller: TextEditingController(
                                      text: object.resourceName ?? '',
                                    ),
                                    readOnly: true,
                                    onTap: () async {
                                      final list =
                                          await ctrl.fetchSubResources(object);
                                      if (list.isNotEmpty &&
                                          context.mounted) {
                                        _showSubResourceBottomSheet(
                                          context,
                                          list,
                                          ctrl,
                                        );
                                      }
                                    },
                                    hint: 'Select Resource',
                                    label: CommonText(
                                      text: 'Select Resource',
                                      fontSize: 14.sp,
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
                                          icUserIcon,
                                          height: 24.h,
                                          width: 24.w,
                                          fit: BoxFit.contain,
                                        ),
                                      ),
                                    ),
                                    suffixIcon: const Icon(
                                      Icons.keyboard_arrow_down,
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      );
                    },
                  ),
                ),
                SizedBox(height: 8.h),
              ],
            ).paddingSymmetric(vertical: 10.h, horizontal: 14.w),
          ),
        );
      },
    );
  }

  void _showSubResourceBottomSheet(
    BuildContext screenContext,
    List<dynamic> list,
    ResourceReMappingUpdateController ctrl,
  ) {
    showModalBottomSheet(
      context: screenContext,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: true,
      enableDrag: true,
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
          child: AddRemoveResourceDropDownScreen(
            titleString: 'Resource',
            dropDownList: list,
            reMappingCampOutput: reMappingCampOutput,
            empCode: ctrl.empCode,
            onRefreshData: (p0) {
              ctrl.loadData();
            },
            onAddSuccess: () {
              ToastManager().showSuccessOkayDialog(
                context: screenContext,
                title: 'Success',
                message:
                    'या कॅम्पसाठी Resource Mapping यशस्वीरीत्या पूर्ण झाले आहे. कृपया लक्षात घ्या की एकदा patient registration झाल्यानंतर या कॅम्पमध्ये कोणताही नवीन phlebotomist/doctor जोडता किंवा हटवता येणार नाही.',
                onTap: () {
                  Get.back();
                },
              );
            },
          ),
        );
      },
    ).whenComplete(() {
      ctrl.update();
    });
  }
}
