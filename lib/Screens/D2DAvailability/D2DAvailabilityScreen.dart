import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';

import '../../Modules/constants/constants.dart';
import '../../Modules/constants/fonts.dart';
import '../../Modules/utilities/SizeConfig.dart';
import 'controller/d2d_availability_controller.dart';

class D2DAvailabilityScreen extends StatefulWidget {
  const D2DAvailabilityScreen({super.key});

  @override
  State<D2DAvailabilityScreen> createState() => _D2DAvailabilityScreenState();
}

class _D2DAvailabilityScreenState extends State<D2DAvailabilityScreen> {
  late final D2DAvailabilityController controller;

  @override
  void initState() {
    super.initState();
    controller = Get.put(D2DAvailabilityController());
  }

  @override
  void dispose() {
    Get.delete<D2DAvailabilityController>();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    Widget statusOption(String text, int value) {
      return Expanded(
        child: Obx(() {
          final disabled = controller.isUpdating.value;
          return InkWell(
            onTap:
                disabled
                    ? null
                    : () {
                      controller.updateStatus(value);
                    },
            child: Row(
              children: [
                Radio<int>(
                  value: value,
                  groupValue: controller.selectedStatus.value,
                  onChanged:
                      disabled
                          ? null
                          : (status) {
                            if (status != null) {
                              controller.updateStatus(status);
                            }
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
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
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
              SizedBox(height: 60.h,)
            ],
          ),
        ),
      ),
    );
  }
}
