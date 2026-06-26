import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/fonts.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../controller/d2d_availability_controller.dart';

class D2DAvailabilityScreen extends GetView<D2DAvailabilityController> {
  const D2DAvailabilityScreen({super.key});

  @override
  Widget build(BuildContext context) {
    Widget statusOption(String text, int value) {
      return Expanded(
        child: Obx(() {
          final disabled = controller.isUpdating.value;
          return InkWell(
            onTap: disabled ? null : () => controller.updateStatus(value),
            child: Row(
              children: [
                Radio<int>(
                  value: value,
                  groupValue: controller.selectedStatus.value,
                  onChanged:
                      disabled
                          ? null
                          : (v) {
                            if (v != null) controller.updateStatus(v);
                          },
                  activeColor: kPrimaryColor,
                ),
                Expanded(
                  child: Text(
                    text,
                    style: TextStyle(
                      color: kBlackColor,
                      fontSize: 12,
                      fontWeight: FontWeight.w600,
                      fontFamily: FontConstants.interFonts,
                    ),
                  ),
                ),
              ],
            ),
          );
        }),
      );
    }

    return Scaffold(
      appBar: mAppBar(
        scTitle: "D2D Availability",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Get.back(),
      ),
      body: SizedBox(
        height: SizeConfig.screenHeight,
        width: SizeConfig.screenWidth,
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            children: [
              Text(
                "You Are Online For D2D Checkup",
                textAlign: TextAlign.center,
                style: TextStyle(
                  color: kBlackColor,
                  fontSize: 14,
                  fontWeight: FontWeight.w700,
                  fontFamily: FontConstants.interFonts,
                ),
              ),
              const SizedBox(height: 16),
              Row(
                children: [
                  statusOption("Go Online For D2D", 1),
                  statusOption("Go Offline For D2D", 0),
                ],
              ),
              const Spacer(),
              Image.asset(
                icD2DPhysicalExamination,
                width: 150,
                height: 150,
                fit: BoxFit.contain,
              ),
              SizedBox(height: 60.h),
            ],
          ),
        ),
      ),
    );
  }
}
