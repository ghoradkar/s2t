// ignore_for_file: use_build_context_synchronously, use_full_hex_values_for_flutter_colors, avoid_print

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/appointment_confirmation_controller.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppButtonWithIcon.dart';
import '../../../Modules/widgets/AppTextField.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import 'beneficiary_card.dart';
import '../custom_widgets/no_data_widget.dart';
import '../custom_widgets/selection_bottom_sheet.dart';
import '../routes/app_routes.dart';

class AppointmentConfirmation
    extends GetView<AppointmentConfirmationController> {
  const AppointmentConfirmation({super.key});

  @override
  Widget build(BuildContext context) {
    return GetBuilder<AppointmentConfirmationController>(
      builder: (c) {
        SizeConfig().init(context);

        String marriedStatusIDNew = c.marriedStatusID;

        List<Map<String, dynamic>> filteredList = c.getFilteredDependents(
          marriedStatusIDNew,
        );

        String selectedDependentId = c.numberOfDependent.toString();

        List<Map<String, dynamic>> dependentList = c.generateDependentList(
          selectedDependentId,
        );

        return NetworkWrapper(
          child: Scaffold(
            appBar: mAppBar(
              scTitle: 'Appointment Confirmation',
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () {
                Navigator.pop(context);
              },
            ),
            body:
                c.selectedBeneficiary != null
                    ? Form(
                      key: c.formKey,
                      child: Padding(
                        padding: EdgeInsets.symmetric(
                          vertical: 8.h,
                          horizontal: 14.w,
                        ),
                        child: SingleChildScrollView(
                          child: Column(
                            children: [
                              SizedBox(height: 6.h),
                              Container(
                                decoration: BoxDecoration(
                                  border: Border.all(
                                    color: kTextColor.withValues(alpha: 0.2),
                                  ),
                                  color: kWhiteColor,
                                  borderRadius: BorderRadius.circular(10),
                                ),
                                child: Padding(
                                  padding: EdgeInsets.symmetric(
                                    vertical: 10.h,
                                    horizontal: 10.w,
                                  ),
                                  child: Column(
                                    children: [
                                      Row(
                                        mainAxisAlignment:
                                            MainAxisAlignment.spaceBetween,
                                        crossAxisAlignment:
                                            CrossAxisAlignment
                                                .start, // Aligns text to top
                                        children: [
                                          Expanded(
                                            // Allows text to wrap
                                            child: Text(
                                              c
                                                      .selectedBeneficiary
                                                      ?.beneficiaryName ??
                                                  "",
                                              style: TextStyle(
                                                fontSize: 14.sp,
                                                fontWeight: FontWeight.w600,
                                                fontFamily:
                                                    FontConstants.interFonts,
                                              ),
                                              softWrap:
                                                  true, // Allows text to wrap
                                            ),
                                          ),
                                          Container(
                                            decoration: BoxDecoration(
                                              borderRadius:
                                                  BorderRadius.circular(
                                                    responsiveHeight(5),
                                                  ),
                                              color: getStatusColor(
                                                c
                                                        .selectedBeneficiary
                                                        ?.groupID ??
                                                    0,
                                              ),
                                            ),
                                            padding: EdgeInsets.symmetric(
                                              vertical: 4.h,
                                              horizontal: 4.w,
                                            ),
                                            child: Text(
                                              c
                                                      .selectedBeneficiary
                                                      ?.assignStatus ??
                                                  "",
                                              style: TextStyle(
                                                color: kWhiteColor,
                                                fontSize: 11.sp,
                                                fontFamily:
                                                    FontConstants.interFonts,
                                              ),
                                            ),
                                          ),
                                        ],
                                      ),
                                      SizedBox(width: 10.w),
                                      Row(
                                        children: [
                                          Image.asset(calendar, scale: 3.5),
                                          SizedBox(width: 10.w),
                                          Text(
                                            "Age : ${c.selectedBeneficiary?.age}",
                                            style: TextStyle(
                                              fontSize: 14.sp,
                                              fontWeight: FontWeight.w500,
                                              fontFamily:
                                                  FontConstants.interFonts,
                                            ),
                                          ),
                                        ],
                                      ),
                                      SizedBox(height: 18.h),
                                      AppTextField(
                                        onTap: () {
                                          // API: fetch valid call-status options for this appointment.
                                          c.svc.fetchCallStatusForAppointment({
                                            "AssignCallID":
                                                c
                                                    .selectedBeneficiary
                                                    ?.assignCallID
                                                    .toString(),
                                          });
                                          c.remarkTextController.clear();
                                        },
                                        controller: c.callStatusTextController,
                                        readOnly: true,
                                        label: RichText(
                                          text: TextSpan(
                                            text: "Call Status",
                                            style: TextStyle(
                                              color: kLabelTextColor,
                                              fontSize:
                                                  responsiveFont(14) * 1.33,
                                              fontFamily:
                                                  FontConstants.interFonts,
                                              fontWeight: FontWeight.w600,
                                            ),
                                            children: <TextSpan>[
                                              TextSpan(
                                                text: ' *',
                                                style: TextStyle(
                                                  color: Colors.red,
                                                  fontSize: responsiveFont(14),
                                                ),
                                              ),
                                            ],
                                          ),
                                        ),
                                        inputStyle: TextStyle(
                                          fontSize: responsiveFont(14),
                                        ),
                                        labelStyle: TextStyle(
                                          fontSize: responsiveFont(14),
                                        ),
                                        prefixIcon: Image.asset(
                                          callIcon,
                                          scale: 3.5,
                                        ),
                                        suffixIcon: Image.asset(
                                          downArrow,
                                          scale: 3.5,
                                        ),
                                      ),
                                      SizedBox(height: 8.h),
                                    ],
                                  ),
                                ),
                              ),
                              SizedBox(height: 10.h),
                              Container(
                                decoration: BoxDecoration(
                                  boxShadow: const [
                                    BoxShadow(
                                      offset: Offset(0, 1),
                                      color: Color(0xff00000026),
                                      spreadRadius: 0,
                                      blurRadius: 10,
                                    ),
                                  ],
                                  color: const Color(0XFFFFFFFF),
                                  border: Border.all(
                                    color: const Color(0xFFD1D1D1),
                                    width: 1,
                                  ),
                                  borderRadius: BorderRadius.circular(10),
                                ),
                                child: Column(
                                  children: [
                                    Material(
                                      color: Colors.transparent,
                                      child: InkWell(
                                        onTap: () {
                                          c.currentAddressVisibility =
                                              !c.currentAddressVisibility;
                                          c.update();
                                        },
                                        child: Ink(
                                          decoration: BoxDecoration(
                                            color: const Color(0XFF423897),
                                            borderRadius: BorderRadius.circular(
                                              10,
                                            ),
                                          ),
                                          child: Padding(
                                            padding: EdgeInsets.symmetric(
                                              vertical: 10.h,
                                              horizontal: 10.w,
                                            ),
                                            child: Column(
                                              children: [
                                                Row(
                                                  mainAxisAlignment:
                                                      MainAxisAlignment
                                                          .spaceBetween,
                                                  children: [
                                                    Text(
                                                      "Current Address & Contact info*",
                                                      style: TextStyle(
                                                        color: const Color(
                                                          0XFFFFFFFF,
                                                        ),
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                        fontSize:
                                                            responsiveFont(14),
                                                        fontWeight:
                                                            FontWeight.w400,
                                                      ),
                                                    ),
                                                    c.currentAddressVisibility
                                                        ? Image.asset(
                                                          upArrow,
                                                          color: Colors.white,
                                                          height: 24.h,
                                                          width: 24.w,
                                                        )
                                                        : Image.asset(
                                                          downArrow,
                                                          height: 24.h,
                                                          width: 24.w,
                                                          color: Colors.white,
                                                        ),
                                                  ],
                                                ),
                                              ],
                                            ),
                                          ),
                                        ),
                                      ),
                                    ),
                                    Visibility(
                                      visible: c.currentAddressVisibility,
                                      child: Padding(
                                        padding: EdgeInsets.symmetric(
                                          vertical: 20.h,
                                          horizontal: 10.w,
                                        ),
                                        child: Column(
                                          children: [
                                            Row(
                                              children: [
                                                Expanded(
                                                  flex: 1,
                                                  child: AppTextField(
                                                    controller:
                                                        c.districtTextController,
                                                    readOnly: true,
                                                    textInputType:
                                                        TextInputType.name,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      color: Colors.black,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text: 'District',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                        children: <TextSpan>[
                                                          TextSpan(
                                                            text: '*',
                                                            style: TextStyle(
                                                              color: Colors.red,
                                                              fontSize:
                                                                  responsiveFont(
                                                                    12,
                                                                  ),
                                                            ),
                                                          ),
                                                        ],
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          iconMap,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                    suffixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          downArrow,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                                SizedBox(
                                                  width: responsiveHeight(10),
                                                ),
                                                Expanded(
                                                  flex: 1,
                                                  child: AppTextField(
                                                    controller:
                                                        c.talukaTextController,
                                                    readOnly: true,
                                                    textInputType:
                                                        TextInputType.name,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      color: Colors.black,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text: 'Taluka',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                        children: <TextSpan>[
                                                          TextSpan(
                                                            text: '*',
                                                            style: TextStyle(
                                                              color: Colors.red,
                                                              fontSize:
                                                                  responsiveFont(
                                                                    12,
                                                                  ),
                                                            ),
                                                          ),
                                                        ],
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          iconMap,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                    suffixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          downArrow,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ],
                                            ),
                                            SizedBox(
                                              height: responsiveHeight(16),
                                            ),
                                            AppTextField(
                                              controller:
                                                  c.houseNumberTextController,
                                              readOnly: !c.isChangeAddress,
                                              textInputType: TextInputType.name,
                                              inputStyle: TextStyle(
                                                fontSize: responsiveFont(12),
                                                color: Colors.black,
                                              ),
                                              label: RichText(
                                                text: TextSpan(
                                                  text:
                                                      'House Number and Building Name',
                                                  style: TextStyle(
                                                    color: kLabelTextColor,
                                                    fontSize:
                                                        responsiveFont(14) *
                                                        1.33,
                                                    fontFamily:
                                                        FontConstants
                                                            .interFonts,
                                                    fontWeight: FontWeight.w600,
                                                  ),
                                                ),
                                              ),
                                              labelStyle: TextStyle(
                                                fontSize: responsiveFont(12),
                                              ),
                                              prefixIcon: SizedBox(
                                                height: responsiveHeight(24),
                                                width: responsiveHeight(24),
                                                child: Center(
                                                  child: Image.asset(
                                                    iconMap,
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                  ),
                                                ),
                                              ),
                                            ),
                                            SizedBox(
                                              height: responsiveHeight(16),
                                            ),
                                            AppTextField(
                                              controller: c.roadTextController,
                                              readOnly: !c.isChangeAddress,
                                              textInputType: TextInputType.name,
                                              inputStyle: TextStyle(
                                                fontSize: responsiveFont(12),
                                                color: Colors.black,
                                              ),
                                              label: RichText(
                                                text: TextSpan(
                                                  text: 'Road',
                                                  style: TextStyle(
                                                    color: kLabelTextColor,
                                                    fontSize:
                                                        responsiveFont(14) *
                                                        1.33,
                                                    fontFamily:
                                                        FontConstants
                                                            .interFonts,
                                                    fontWeight: FontWeight.w600,
                                                  ),
                                                ),
                                              ),
                                              labelStyle: TextStyle(
                                                fontSize: responsiveFont(12),
                                              ),
                                              prefixIcon: SizedBox(
                                                height: responsiveHeight(24),
                                                width: responsiveHeight(24),
                                                child: Center(
                                                  child: Image.asset(
                                                    iconMap,
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                  ),
                                                ),
                                              ),
                                            ),
                                            SizedBox(
                                              height: responsiveHeight(16),
                                            ),
                                            AppTextField(
                                              controller: c.areaTextController,
                                              readOnly: !c.isChangeAddress,
                                              textInputType: TextInputType.name,
                                              inputStyle: TextStyle(
                                                fontSize: responsiveFont(12),
                                                color: Colors.black,
                                              ),
                                              label: RichText(
                                                text: TextSpan(
                                                  text: 'Area',
                                                  style: TextStyle(
                                                    color: kLabelTextColor,
                                                    fontSize:
                                                        responsiveFont(14) *
                                                        1.33,
                                                    fontFamily:
                                                        FontConstants
                                                            .interFonts,
                                                    fontWeight: FontWeight.w600,
                                                  ),
                                                ),
                                              ),
                                              labelStyle: TextStyle(
                                                fontSize: responsiveFont(12),
                                              ),
                                              prefixIcon: SizedBox(
                                                height: responsiveHeight(24),
                                                width: responsiveHeight(24),
                                                child: Center(
                                                  child: Image.asset(
                                                    iconMap,
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                  ),
                                                ),
                                              ),
                                            ),
                                            SizedBox(
                                              height: responsiveHeight(16),
                                            ),
                                            Row(
                                              children: [
                                                Expanded(
                                                  flex: 1,
                                                  child: AppTextField(
                                                    controller:
                                                        c.landMarkTextController,
                                                    readOnly:
                                                        !c.isChangeAddress,
                                                    textInputType:
                                                        TextInputType.name,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      color: Colors.black,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text: 'Landmark',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          iconMap,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                                SizedBox(
                                                  width: responsiveHeight(10),
                                                ),
                                                Expanded(
                                                  flex: 1,
                                                  child: AppTextField(
                                                    controller:
                                                        c.pincodeTextController,
                                                    validators:
                                                        c.validatePincodeInput,
                                                    errorText: c
                                                        .validatePincodeInput(
                                                          c
                                                              .pincodeTextController
                                                              .text,
                                                        ),
                                                    readOnly: true,
                                                    // readOnly: !c.isChangeAddress,
                                                    textInputType:
                                                        TextInputType.number,
                                                    maxLength: 6,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      color: Colors.black,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text: 'Pincode',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          icHashIcon,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ],
                                            ),
                                            SizedBox(
                                              height: responsiveHeight(16),
                                            ),
                                            Row(
                                              children: [
                                                Expanded(
                                                  flex: 1,
                                                  child: AppTextField(
                                                    errorText:
                                                        c.isSubmitted
                                                            ? c.validateMobileInput(
                                                              c
                                                                  .regMobileTextController
                                                                  .text,
                                                            )
                                                            : null,
                                                    onChange: (p0) {
                                                      c.update();
                                                    },
                                                    controller:
                                                        c.regMobileTextController,
                                                    readOnly: true,
                                                    // readOnly: !c.isChangeAddress,
                                                    textInputType:
                                                        TextInputType.name,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      color: Colors.black,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text: 'Reg. Mobile No.',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          iconMobile,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                                SizedBox(
                                                  width: responsiveHeight(10),
                                                ),
                                                Expanded(
                                                  flex: 1,
                                                  child: AppTextField(
                                                    controller:
                                                        c.alternateMobileTextController,

                                                    // validators:
                                                    //     c.validateMobileInput,
                                                    maxLength: 10,
                                                    readOnly:
                                                        !c.isChangeAddress,
                                                    textInputType:
                                                        TextInputType.number,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                      color: Colors.black,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text:
                                                            'Alternate Mobile',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        12,
                                                      ),
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          iconMobile,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ],
                                            ),

                                            Row(
                                              children: [
                                                Visibility(
                                                  visible:
                                                      c.isTextFieldVisibilityDateTime,
                                                  child: Expanded(
                                                    child: Transform.scale(
                                                      alignment:
                                                          FractionalOffset
                                                              .centerLeft,
                                                      scale: 1,
                                                      child: Row(
                                                        children: [
                                                          Checkbox(
                                                            materialTapTargetSize:
                                                                MaterialTapTargetSize
                                                                    .shrinkWrap,
                                                            visualDensity:
                                                                const VisualDensity(
                                                                  horizontal:
                                                                      -4,
                                                                  vertical: -4,
                                                                ),
                                                            value:
                                                                c.isChangeAddress,
                                                            onChanged:
                                                                c.toggleChangeAddress,
                                                          ),
                                                          Expanded(
                                                            child: GestureDetector(
                                                              onTap: () {
                                                                c.toggleChangeAddress(
                                                                  !c.isChangeAddress,
                                                                );
                                                              },
                                                              child: Text(
                                                                "Click Here To Change Registered Address",
                                                                style: TextStyle(
                                                                  color:
                                                                      kBlackColor,
                                                                  fontSize:
                                                                      responsiveHeight(
                                                                        12,
                                                                      ),
                                                                  fontFamily:
                                                                      FontConstants
                                                                          .interFonts,
                                                                ),
                                                              ),
                                                            ),
                                                          ),
                                                        ],
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ],
                                            ).paddingOnly(top: 20),
                                          ],
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ),

                              SizedBox(height: responsiveHeight(10)),

                              Container(
                                decoration: BoxDecoration(
                                  boxShadow: const [
                                    BoxShadow(
                                      offset: Offset(0, 1),
                                      color: Color(0xff00000026),
                                      spreadRadius: 0,
                                      blurRadius: 10,
                                    ),
                                  ],
                                  color: Colors.white,
                                  border: Border.all(
                                    color: Color(0xFFD1D1D1),
                                    width: 1,
                                  ),
                                  borderRadius: BorderRadius.circular(10),
                                ),
                                child: Column(
                                  children: [
                                    Material(
                                      color: Colors.transparent,
                                      child: InkWell(
                                        onTap: () {
                                          c.screenedVisibility =
                                              !c.screenedVisibility;
                                          c.update();
                                        },
                                        child: Ink(
                                          decoration: BoxDecoration(
                                            color: const Color(0XFF423897),
                                            borderRadius: BorderRadius.circular(
                                              10,
                                            ),
                                          ),
                                          child: Padding(
                                            padding: EdgeInsets.all(
                                              responsiveHeight(10),
                                            ),
                                            child: Row(
                                              mainAxisAlignment:
                                                  MainAxisAlignment
                                                      .spaceBetween,
                                              children: [
                                                Text(
                                                  "Screened Beneficiary",
                                                  style: TextStyle(
                                                    color: Colors.white,
                                                    fontSize: responsiveFont(
                                                      14,
                                                    ),
                                                    fontWeight: FontWeight.w400,
                                                    fontFamily:
                                                        FontConstants
                                                            .interFonts,
                                                  ),
                                                ),
                                                Image.asset(
                                                  c.screenedVisibility
                                                      ? upArrow
                                                      : downArrow,
                                                  color: Colors.white,
                                                  height: responsiveHeight(24),
                                                  width: responsiveHeight(24),
                                                ),
                                              ],
                                            ),
                                          ),
                                        ),
                                      ),
                                    ),

                                    /// Collapsible content
                                    Visibility(
                                      visible: c.screenedVisibility,
                                      child: Padding(
                                        padding: EdgeInsets.symmetric(
                                          vertical: responsiveHeight(10),
                                          horizontal: 20.w,
                                        ),
                                        child:
                                            c.screeningdataList.isEmpty
                                                ? Center(
                                                  child: Column(
                                                    children: [
                                                      // SizedBox(height: 10),
                                                      Text(
                                                        "No data available",
                                                        style: TextStyle(
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ),
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          color: Colors.grey,
                                                        ),
                                                      ),
                                                    ],
                                                  ),
                                                )
                                                : ListView.builder(
                                                  shrinkWrap: true,
                                                  physics:
                                                      NeverScrollableScrollPhysics(),
                                                  itemCount:
                                                      c
                                                          .screeningdataList
                                                          .length,
                                                  itemBuilder: (
                                                    context,
                                                    index,
                                                  ) {
                                                    final item =
                                                        c.screeningdataList[index];
                                                    return Padding(
                                                      padding:
                                                          const EdgeInsets.all(
                                                            6.0,
                                                          ),
                                                      child: Container(
                                                        decoration: BoxDecoration(
                                                          boxShadow: const [
                                                            BoxShadow(
                                                              offset: Offset(
                                                                0,
                                                                1,
                                                              ),
                                                              color: Color(
                                                                0xff00000026,
                                                              ),
                                                              spreadRadius: 0,
                                                              blurRadius: 10,
                                                            ),
                                                          ],
                                                          color: const Color(
                                                            0XFFF8F8FF,
                                                          ),
                                                          borderRadius:
                                                              BorderRadius.circular(
                                                                10,
                                                              ),
                                                          border: Border.all(
                                                            color: const Color(
                                                              0XFFCFCBEC,
                                                            ),
                                                          ),
                                                        ),
                                                        child: Padding(
                                                          padding:
                                                              EdgeInsets.all(
                                                                responsiveHeight(
                                                                  10,
                                                                ),
                                                              ),
                                                          child: Column(
                                                            crossAxisAlignment:
                                                                CrossAxisAlignment
                                                                    .start,
                                                            children: [
                                                              /// Name row (optional if you have name)
                                                              Row(
                                                                children: [
                                                                  Image.asset(
                                                                    iconPersons,
                                                                    scale: 3.5,
                                                                  ),
                                                                  const SizedBox(
                                                                    width: 8,
                                                                  ),
                                                                  Text.rich(
                                                                    TextSpan(
                                                                      text:
                                                                          'Name: ',
                                                                      style: TextStyle(
                                                                        fontSize:
                                                                            responsiveFont(
                                                                              14,
                                                                            ),
                                                                        fontWeight:
                                                                            FontWeight.w500,
                                                                        fontFamily:
                                                                            FontConstants.interFonts,
                                                                      ),
                                                                      children: [
                                                                        TextSpan(
                                                                          text:
                                                                              item.firstName ??
                                                                              "N/A",
                                                                          style: TextStyle(
                                                                            fontSize: responsiveFont(
                                                                              14,
                                                                            ),
                                                                            fontWeight:
                                                                                FontWeight.w400,
                                                                            color: Color(
                                                                              0xFF666666,
                                                                            ),
                                                                            fontFamily:
                                                                                FontConstants.interFonts,
                                                                          ),
                                                                        ),
                                                                      ],
                                                                    ),
                                                                  ),
                                                                ],
                                                              ),
                                                              SizedBox(
                                                                height:
                                                                    responsiveHeight(
                                                                      10,
                                                                    ),
                                                              ),

                                                              /// Relation
                                                              Row(
                                                                children: [
                                                                  Image.asset(
                                                                    iconPersons,
                                                                    scale: 3.5,
                                                                  ),
                                                                  const SizedBox(
                                                                    width: 8,
                                                                  ),
                                                                  Text.rich(
                                                                    TextSpan(
                                                                      text:
                                                                          'Relation: ',
                                                                      style: TextStyle(
                                                                        fontSize:
                                                                            responsiveFont(
                                                                              14,
                                                                            ),
                                                                        fontWeight:
                                                                            FontWeight.w500,
                                                                        fontFamily:
                                                                            FontConstants.interFonts,
                                                                      ),
                                                                      children: [
                                                                        TextSpan(
                                                                          text:
                                                                              item.relation ??
                                                                              "N/A",
                                                                          style: TextStyle(
                                                                            fontSize: responsiveFont(
                                                                              14,
                                                                            ),
                                                                            fontWeight:
                                                                                FontWeight.w400,
                                                                            color: Color(
                                                                              0xFF666666,
                                                                            ),
                                                                            fontFamily:
                                                                                FontConstants.interFonts,
                                                                          ),
                                                                        ),
                                                                      ],
                                                                    ),
                                                                  ),
                                                                ],
                                                              ),
                                                              SizedBox(
                                                                height:
                                                                    responsiveHeight(
                                                                      10,
                                                                    ),
                                                              ),

                                                              Row(
                                                                children: [
                                                                  Image.asset(
                                                                    iconPersons,
                                                                    scale: 3.5,
                                                                  ),
                                                                  const SizedBox(
                                                                    width: 8,
                                                                  ),
                                                                  Text.rich(
                                                                    TextSpan(
                                                                      text:
                                                                          'Last Screening Date: ',
                                                                      style: TextStyle(
                                                                        fontSize:
                                                                            responsiveFont(
                                                                              14,
                                                                            ),
                                                                        fontWeight:
                                                                            FontWeight.w500,
                                                                        fontFamily:
                                                                            FontConstants.interFonts,
                                                                      ),
                                                                      children: [
                                                                        TextSpan(
                                                                          text:
                                                                              item.screeningDate ??
                                                                              "N/A",
                                                                          style: TextStyle(
                                                                            fontSize: responsiveFont(
                                                                              14,
                                                                            ),
                                                                            fontWeight:
                                                                                FontWeight.w400,
                                                                            color: Color(
                                                                              0xFF666666,
                                                                            ),
                                                                            fontFamily:
                                                                                FontConstants.interFonts,
                                                                          ),
                                                                        ),
                                                                      ],
                                                                    ),
                                                                  ),
                                                                ],
                                                              ),

                                                              SizedBox(
                                                                height:
                                                                    responsiveHeight(
                                                                      10,
                                                                    ),
                                                              ),

                                                              /// Age
                                                              Row(
                                                                children: [
                                                                  Icon(
                                                                    Icons.cake,
                                                                    size:
                                                                        responsiveFont(
                                                                          20,
                                                                        ),
                                                                    color: Color(
                                                                      0xFF423897,
                                                                    ),
                                                                  ),
                                                                  const SizedBox(
                                                                    width: 8,
                                                                  ),
                                                                  Text.rich(
                                                                    TextSpan(
                                                                      text:
                                                                          'Age: ',
                                                                      style: TextStyle(
                                                                        fontSize:
                                                                            responsiveFont(
                                                                              14,
                                                                            ),
                                                                        fontWeight:
                                                                            FontWeight.w500,
                                                                        fontFamily:
                                                                            FontConstants.interFonts,
                                                                      ),
                                                                      children: [
                                                                        TextSpan(
                                                                          text:
                                                                              item.age?.toString() ??
                                                                              "N/A",
                                                                          style: TextStyle(
                                                                            fontSize: responsiveFont(
                                                                              14,
                                                                            ),
                                                                            fontWeight:
                                                                                FontWeight.w400,
                                                                            color: Color(
                                                                              0xFF666666,
                                                                            ),
                                                                            fontFamily:
                                                                                FontConstants.interFonts,
                                                                          ),
                                                                        ),
                                                                      ],
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
                                    ),
                                  ],
                                ),
                              ),

                              SizedBox(height: responsiveHeight(10)),

                              Container(
                                decoration: BoxDecoration(
                                  boxShadow: const [
                                    BoxShadow(
                                      offset: Offset(0, 1),
                                      color: Color(0xff00000026),
                                      spreadRadius: 0,
                                      blurRadius: 10,
                                    ),
                                  ],
                                  color: const Color(0XFFFFFFFF),
                                  border: Border.all(
                                    color: const Color(0xFFD1D1D1),
                                    width: 1,
                                  ),
                                  borderRadius: BorderRadius.circular(10),
                                ),
                                child: Column(
                                  children: [
                                    Material(
                                      color: Colors.transparent,
                                      child: InkWell(
                                        onTap: () {
                                          c.personalVisibility =
                                              !c.personalVisibility;
                                          c.update();
                                        },
                                        child: Ink(
                                          decoration: BoxDecoration(
                                            color: const Color(0XFF423897),
                                            borderRadius: BorderRadius.circular(
                                              10,
                                            ),
                                          ),
                                          child: Padding(
                                            padding: EdgeInsets.all(
                                              responsiveHeight(10),
                                            ),
                                            child: Column(
                                              children: [
                                                Row(
                                                  mainAxisAlignment:
                                                      MainAxisAlignment
                                                          .spaceBetween,
                                                  children: [
                                                    Text(
                                                      "Personal & Dependenet info*",
                                                      style: TextStyle(
                                                        color: const Color(
                                                          0XFFFFFFFF,
                                                        ),
                                                        fontSize:
                                                            responsiveFont(14),
                                                        fontWeight:
                                                            FontWeight.w400,
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                      ),
                                                    ),
                                                    c.personalVisibility
                                                        ? Image.asset(
                                                          upArrow,
                                                          color: Colors.white,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        )
                                                        : Image.asset(
                                                          downArrow,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          color: Colors.white,
                                                        ),
                                                  ],
                                                ),
                                              ],
                                            ),
                                          ),
                                        ),
                                      ),
                                    ),
                                    Visibility(
                                      visible: c.personalVisibility,
                                      child: Padding(
                                        padding: EdgeInsets.symmetric(
                                          vertical: 20.h,
                                          horizontal: 10.w,
                                        ),
                                        child: Column(
                                          children: [
                                            Row(
                                              children: [
                                                Expanded(
                                                  child: AppTextField(
                                                    onTap: () {
                                                      if (c
                                                          .callStatusTextController
                                                          .text
                                                          .isEmpty) {
                                                        ToastManager.showAlertDialog(
                                                          Get.context!,
                                                          "Please select call status first",
                                                          () {
                                                            Get.back();
                                                          },
                                                        );

                                                        return;
                                                      }

                                                      showModalBottomSheet(
                                                        context: Get.context!,
                                                        isScrollControlled:
                                                            true,
                                                        builder: (context) {
                                                          return StatefulBuilder(
                                                            builder: (
                                                              bsCtx,
                                                              sheetState,
                                                            ) {
                                                              return SelectionBottomSheet<
                                                                Map<
                                                                  String,
                                                                  dynamic
                                                                >,
                                                                int
                                                              >(
                                                                title:
                                                                    "Worker Gender",
                                                                items:
                                                                    AppointmentConfirmationController
                                                                        .genderList,
                                                                selectedValue:
                                                                    c.selectedGender?["id"],
                                                                valueFor:
                                                                    (item) =>
                                                                        item["id"] ??
                                                                        -1,
                                                                labelFor:
                                                                    (item) =>
                                                                        item["name"] ??
                                                                        'NA',
                                                                height: 260.h,
                                                                padding: EdgeInsets.only(
                                                                  top:
                                                                      responsiveHeight(
                                                                        28,
                                                                      ),
                                                                  left:
                                                                      responsiveHeight(
                                                                        35,
                                                                      ),
                                                                  right:
                                                                      responsiveHeight(
                                                                        35,
                                                                      ),
                                                                  bottom:
                                                                      responsiveHeight(
                                                                        60,
                                                                      ),
                                                                ),
                                                                titleTextStyle: TextStyle(
                                                                  fontSize:
                                                                      responsiveFont(
                                                                        16,
                                                                      ),
                                                                  fontFamily:
                                                                      FontConstants
                                                                          .interFonts,
                                                                ),
                                                                titleBottomSpacing:
                                                                    responsiveHeight(
                                                                      30,
                                                                    ),
                                                                itemContainerPadding:
                                                                    const EdgeInsets.symmetric(
                                                                      vertical:
                                                                          8.0,
                                                                      horizontal:
                                                                          4.0,
                                                                    ),
                                                                itemTextStyle: TextStyle(
                                                                  fontFamily:
                                                                      FontConstants
                                                                          .interFonts,
                                                                ),
                                                                selectedItemTextStyle:
                                                                    TextStyle(
                                                                      fontFamily:
                                                                          FontConstants
                                                                              .interFonts,
                                                                    ),
                                                                selectedBackgroundColor:
                                                                    kPrimaryColor
                                                                        .withOpacity(
                                                                          0.1,
                                                                        ),
                                                                onItemTap: (
                                                                  item,
                                                                ) {
                                                                  // STEP 1: Update the visual state FIRST using sheetState
                                                                  sheetState(() {
                                                                    c.selectedGender =
                                                                        item;
                                                                  });

                                                                  // STEP 2: Update text controller and state
                                                                  c
                                                                      .workersGenderTextController
                                                                      .text = c
                                                                          .selectedGender?["name"] ??
                                                                      "";

                                                                  c.workerGender =
                                                                      c.selectedGender?["name"] ??
                                                                      "";

                                                                  // STEP 3: Clear dependent fields
                                                                  c
                                                                      .workersMartialStatusTextController
                                                                      .text = "";
                                                                  c
                                                                      .noOfDependentTextController
                                                                      .text = "";
                                                                  c.workersMartialStatusTextController
                                                                      .clear();
                                                                  c.selectedMaritalStatus =
                                                                      null;
                                                                  c
                                                                      .dependentScreeningPendingTextController
                                                                      .text = "";
                                                                  c.selectedNumberOfDependent =
                                                                      null;

                                                                  // STEP 4: Close sheet and trigger bloc event
                                                                  Get.back();
                                                                  c.svc
                                                                      .resetState();
                                                                },
                                                              );
                                                            },
                                                          );
                                                        },
                                                      );
                                                    },
                                                    controller:
                                                        c.workersGenderTextController,
                                                    readOnly: true,
                                                    textInputType:
                                                        TextInputType.name,
                                                    inputStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        14,
                                                      ),
                                                      color: Colors.black,
                                                    ),
                                                    label: RichText(
                                                      text: TextSpan(
                                                        text: 'Workers Gender',
                                                        style: TextStyle(
                                                          color:
                                                              kLabelTextColor,
                                                          fontSize:
                                                              responsiveFont(
                                                                14,
                                                              ) *
                                                              1.33,
                                                          fontFamily:
                                                              FontConstants
                                                                  .interFonts,
                                                          fontWeight:
                                                              FontWeight.w600,
                                                        ),
                                                        children: <TextSpan>[
                                                          TextSpan(
                                                            text: ' *',
                                                            style: TextStyle(
                                                              color: Colors.red,
                                                              fontSize:
                                                                  responsiveFont(
                                                                    14,
                                                                  ),
                                                            ),
                                                          ),
                                                        ],
                                                      ),
                                                    ),
                                                    labelStyle: TextStyle(
                                                      fontSize: responsiveFont(
                                                        14,
                                                      ),
                                                    ),
                                                    prefixIcon: SizedBox(
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width: responsiveHeight(
                                                        24,
                                                      ),
                                                      child: Center(
                                                        child: Image.asset(
                                                          iconMars,
                                                          height:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                          width:
                                                              responsiveHeight(
                                                                24,
                                                              ),
                                                        ),
                                                      ),
                                                    ),
                                                    suffixIcon: Image.asset(
                                                      downArrow,
                                                      scale: 3.5,
                                                    ),
                                                  ),
                                                ),
                                                SizedBox(width: 8.w),
                                                Expanded(
                                                  child: Visibility(
                                                    visible:
                                                        c.isTextFieldVisibilityPersonalDet,
                                                    child: AppTextField(
                                                      onTap: () {
                                                        if (c
                                                            .callStatusTextController
                                                            .text
                                                            .isEmpty) {
                                                          ToastManager.showAlertDialog(
                                                            Get.context!,
                                                            "Please select call status first",
                                                            () {
                                                              Get.back();
                                                            },
                                                          );

                                                          // showDialog(
                                                          //   context: context,
                                                          //   ...
                                                          // );
                                                          return;
                                                        }

                                                        showModalBottomSheet(
                                                          context: Get.context!,
                                                          isScrollControlled:
                                                              true,
                                                          builder: (context) {
                                                            return StatefulBuilder(
                                                              builder: (
                                                                bsCtx,
                                                                sheetState,
                                                              ) {
                                                                return SelectionBottomSheet<
                                                                  Map<
                                                                    String,
                                                                    dynamic
                                                                  >,
                                                                  int
                                                                >(
                                                                  title:
                                                                      "Worker Marital Status",
                                                                  items:
                                                                      AppointmentConfirmationController
                                                                          .maritalStatusList,
                                                                  selectedValue:
                                                                      c.selectedMaritalStatus?["id"],
                                                                  valueFor:
                                                                      (item) =>
                                                                          item["id"] ??
                                                                          -1,
                                                                  labelFor:
                                                                      (item) =>
                                                                          item["name"] ??
                                                                          'NA',
                                                                  height: 300.h,
                                                                  padding: EdgeInsets.only(
                                                                    top:
                                                                        responsiveHeight(
                                                                          28,
                                                                        ),
                                                                    left:
                                                                        responsiveHeight(
                                                                          35,
                                                                        ),
                                                                    right:
                                                                        responsiveHeight(
                                                                          35,
                                                                        ),
                                                                    bottom:
                                                                        responsiveHeight(
                                                                          60,
                                                                        ),
                                                                  ),
                                                                  titleTextStyle: TextStyle(
                                                                    fontSize:
                                                                        responsiveFont(
                                                                          16,
                                                                        ),
                                                                    fontFamily:
                                                                        FontConstants
                                                                            .interFonts,
                                                                  ),
                                                                  titleBottomSpacing:
                                                                      responsiveHeight(
                                                                        30,
                                                                      ),
                                                                  itemContainerPadding:
                                                                      const EdgeInsets.symmetric(
                                                                        vertical:
                                                                            8.0,
                                                                        horizontal:
                                                                            4.0,
                                                                      ),
                                                                  selectedBackgroundColor:
                                                                      kPrimaryColor
                                                                          .withOpacity(
                                                                            0.1,
                                                                          ),
                                                                  itemTextStyle: TextStyle(
                                                                    fontFamily:
                                                                        FontConstants
                                                                            .interFonts,
                                                                    fontWeight:
                                                                        FontWeight
                                                                            .normal,
                                                                    color:
                                                                        kBlackColor,
                                                                  ),
                                                                  selectedItemTextStyle: TextStyle(
                                                                    fontFamily:
                                                                        FontConstants
                                                                            .interFonts,
                                                                    fontWeight:
                                                                        FontWeight
                                                                            .normal,
                                                                    color:
                                                                        kBlackColor,
                                                                  ),
                                                                  onItemTap: (
                                                                    item,
                                                                  ) {
                                                                    // STEP 1: Update the visual state FIRST using sheetState
                                                                    sheetState(() {
                                                                      c.selectedMaritalStatus =
                                                                          item;
                                                                    });

                                                                    // STEP 2: Update text controllers and state
                                                                    c.workersMartialStatusTextController.text =
                                                                        c.selectedMaritalStatus?["name"] ??
                                                                        "";

                                                                    c.marriedStatusID =
                                                                        c.selectedMaritalStatus?["id"]
                                                                            ?.toString() ??
                                                                        "";

                                                                    // STEP 3: Clear dependent fields
                                                                    c
                                                                        .noOfDependentTextController
                                                                        .text = "";
                                                                    c
                                                                        .dependentScreeningPendingTextController
                                                                        .text = "";
                                                                    c.numberOfDependent =
                                                                        0;
                                                                    c.numberOfDependentPending =
                                                                        0;

                                                                    // Rebuild so filteredList recalculates
                                                                    // before the dependent sheet is opened.
                                                                    c.update();

                                                                    // STEP 4: Close sheet and trigger bloc event
                                                                    Get.back();
                                                                    c.svc
                                                                        .resetState();
                                                                  },
                                                                );
                                                              },
                                                            );
                                                          },
                                                        );
                                                      },
                                                      controller:
                                                          c.workersMartialStatusTextController,
                                                      readOnly: true,
                                                      textInputType:
                                                          TextInputType.name,
                                                      inputStyle: TextStyle(
                                                        fontSize:
                                                            responsiveFont(12),
                                                        color: Colors.black,
                                                      ),
                                                      label: RichText(
                                                        text: TextSpan(
                                                          text:
                                                              'Workers Marital Status',
                                                          style: TextStyle(
                                                            color:
                                                                kLabelTextColor,
                                                            fontSize:
                                                                responsiveFont(
                                                                  14,
                                                                ) *
                                                                1.33,
                                                            fontFamily:
                                                                FontConstants
                                                                    .interFonts,
                                                            fontWeight:
                                                                FontWeight.w600,
                                                          ),
                                                          children: <TextSpan>[
                                                            TextSpan(
                                                              text: ' *',
                                                              style: TextStyle(
                                                                color:
                                                                    Colors.red,
                                                                fontSize:
                                                                    responsiveFont(
                                                                      14,
                                                                    ),
                                                              ),
                                                            ),
                                                          ],
                                                        ),
                                                      ),
                                                      labelStyle: TextStyle(
                                                        fontSize:
                                                            responsiveFont(12),
                                                      ),
                                                      prefixIcon: SizedBox(
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                        child: Center(
                                                          child: Image.asset(
                                                            icUserIcon,
                                                            height:
                                                                responsiveHeight(
                                                                  24,
                                                                ),
                                                            width:
                                                                responsiveHeight(
                                                                  24,
                                                                ),
                                                          ),
                                                        ),
                                                      ),
                                                      suffixIcon: Image.asset(
                                                        downArrow,
                                                        scale: 3.5,
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ],
                                            ),

                                            SizedBox(
                                              height: responsiveHeight(30),
                                            ),
                                            Row(
                                              children: [
                                                Expanded(
                                                  child: Visibility(
                                                    visible:
                                                        c.isTextFieldVisibilityPersonalDet,
                                                    child: AppTextField(
                                                      onTap: () {
                                                        if (c
                                                            .callStatusTextController
                                                            .text
                                                            .isEmpty) {
                                                          ToastManager.showAlertDialog(
                                                            Get.context!,
                                                            "Please select call status first",
                                                            () {
                                                              Get.back();
                                                            },
                                                          );

                                                          // showDialog(
                                                          //   ...
                                                          // );

                                                          return;
                                                        }
                                                        if (c
                                                                .workersMartialStatusTextController
                                                                .text ==
                                                            "") {
                                                          ToastManager.showAlertDialog(
                                                            Get.context!,
                                                            "Please select maritial Status",
                                                            () {
                                                              Get.back();
                                                            },
                                                          );

                                                          // showDialog(
                                                          //   ...
                                                          // );

                                                          return;
                                                        }

                                                        showModalBottomSheet(
                                                          context: Get.context!,
                                                          isScrollControlled:
                                                              true,
                                                          builder: (context) {
                                                            return StatefulBuilder(
                                                              builder: (
                                                                bsCtx,
                                                                sheetState,
                                                              ) {
                                                                return SelectionBottomSheet<
                                                                  Map<
                                                                    String,
                                                                    dynamic
                                                                  >,
                                                                  int
                                                                >(
                                                                  title:
                                                                      "No. of Dependent",
                                                                  items:
                                                                      filteredList,
                                                                  selectedValue:
                                                                      c.selectedNumberOfDependent?["id"],
                                                                  valueFor:
                                                                      (item) =>
                                                                          item["id"] ??
                                                                          -1,
                                                                  labelFor:
                                                                      (item) =>
                                                                          item["name"] ??
                                                                          'NA',
                                                                  height: 320.h,
                                                                  padding: EdgeInsets.only(
                                                                    top:
                                                                        responsiveHeight(
                                                                          16,
                                                                        ),
                                                                    left:
                                                                        responsiveHeight(
                                                                          35,
                                                                        ),
                                                                    right:
                                                                        responsiveHeight(
                                                                          35,
                                                                        ),
                                                                    bottom:
                                                                        responsiveHeight(
                                                                          60,
                                                                        ),
                                                                  ),
                                                                  titleTextStyle:
                                                                      TextStyle(
                                                                        fontSize:
                                                                            responsiveFont(
                                                                              16,
                                                                            ),
                                                                      ),
                                                                  titleBottomSpacing:
                                                                      responsiveHeight(
                                                                        30,
                                                                      ),
                                                                  itemPadding:
                                                                      EdgeInsets.symmetric(
                                                                        vertical:
                                                                            4.h,
                                                                      ),
                                                                  itemBuilderWithIndex: (
                                                                    bsCtx2,
                                                                    item,
                                                                    isSelected,
                                                                    index,
                                                                    itemCount,
                                                                  ) {
                                                                    return Padding(
                                                                      padding:
                                                                          const EdgeInsets.only(
                                                                            left:
                                                                                12,
                                                                          ),
                                                                      child: Container(
                                                                        padding: const EdgeInsets.only(
                                                                          left:
                                                                              4.0,
                                                                          right:
                                                                              4.0,
                                                                        ),
                                                                        margin:
                                                                            index ==
                                                                                    itemCount -
                                                                                        1
                                                                                ? EdgeInsets.only(
                                                                                  bottom: responsiveHeight(
                                                                                    20,
                                                                                  ),
                                                                                )
                                                                                : EdgeInsets.zero,
                                                                        decoration: BoxDecoration(
                                                                          color:
                                                                              isSelected
                                                                                  ? kPrimaryColor.withOpacity(
                                                                                    0.1,
                                                                                  )
                                                                                  : Colors.transparent,
                                                                          borderRadius:
                                                                              BorderRadius.circular(
                                                                                8,
                                                                              ),
                                                                        ),
                                                                        child: Row(
                                                                          children: [
                                                                            IgnorePointer(
                                                                              child: Transform.scale(
                                                                                scale:
                                                                                    0.75,
                                                                                child: Radio<
                                                                                  int
                                                                                >(
                                                                                  // CRITICAL FIX: Use int instead of Map
                                                                                  value:
                                                                                      item["id"] ??
                                                                                      0,
                                                                                  groupValue:
                                                                                      c.selectedNumberOfDependent?["id"] ??
                                                                                      -1,
                                                                                  onChanged:
                                                                                      (
                                                                                        value,
                                                                                      ) {},
                                                                                  // Required but never called
                                                                                  materialTapTargetSize:
                                                                                      MaterialTapTargetSize.shrinkWrap,
                                                                                  visualDensity: const VisualDensity(
                                                                                    horizontal:
                                                                                        -4,
                                                                                    vertical:
                                                                                        -4,
                                                                                  ),
                                                                                ),
                                                                              ),
                                                                            ),
                                                                            Expanded(
                                                                              child: Text(
                                                                                item["name"] ??
                                                                                    'NA',
                                                                                style: TextStyle(
                                                                                  fontFamily:
                                                                                      FontConstants.interFonts,
                                                                                  fontWeight:
                                                                                      FontWeight.normal,
                                                                                  color:
                                                                                      kBlackColor,
                                                                                ),
                                                                              ),
                                                                            ),
                                                                          ],
                                                                        ),
                                                                      ),
                                                                    );
                                                                  },
                                                                  onItemTap: (
                                                                    item,
                                                                  ) {
                                                                    // STEP 1: Update the visual state FIRST using sheetState
                                                                    sheetState(() {
                                                                      c.selectedNumberOfDependent =
                                                                          item;
                                                                    });

                                                                    c.noOfDependentTextController.text =
                                                                        c.selectedNumberOfDependent?["name"] ??
                                                                        "";

                                                                    c.numberOfDependent =
                                                                        c.selectedNumberOfDependent?["id"];

                                                                    final noOfDepartmentStr =
                                                                        c.numberOfDependent;

                                                                    int
                                                                    screenedCount =
                                                                        0;
                                                                    int
                                                                    departmentCount =
                                                                        0;

                                                                    try {
                                                                      screenedCount =
                                                                          c.screenedBeneficiaryCount;
                                                                      departmentCount =
                                                                          noOfDepartmentStr;
                                                                    } catch (
                                                                      e
                                                                    ) {
                                                                      debugPrint(
                                                                        "Invalid input: $e",
                                                                      );
                                                                    }

                                                                    c.differenceCount =
                                                                        (screenedCount -
                                                                                departmentCount)
                                                                            .abs();

                                                                    // Validation logic
                                                                    if (c.screenedBeneficiaryCount !=
                                                                        0) {
                                                                      if (departmentCount <
                                                                          screenedCount) {
                                                                        ToastManager.showAlertDialog(
                                                                          Get.context!,
                                                                          "You have added number of dependents greater than $screenedCount screened dependents.",
                                                                          () {
                                                                            Get.back();

                                                                            // Clear selection on validation failure
                                                                            sheetState(() {
                                                                              c.selectedNumberOfDependent = null;
                                                                            });
                                                                            c.noOfDependentTextController.text =
                                                                                "";
                                                                            c.numberOfDependent =
                                                                                0;
                                                                          },
                                                                        );

                                                                        return;
                                                                      }
                                                                    }

                                                                    // If check passes, clear dependent fields and close
                                                                    c
                                                                        .dependentScreeningPendingTextController
                                                                        .text = "";
                                                                    c.selectedDependentScreeningPending =
                                                                        null;

                                                                    // Rebuild so dependentList regenerates from
                                                                    // the new numberOfDependent value before the
                                                                    // Screening Pending sheet is opened.
                                                                    c.update();

                                                                    Get.back();
                                                                    c.svc
                                                                        .resetState();
                                                                  },
                                                                );
                                                              },
                                                            );
                                                          },
                                                        );
                                                      },
                                                      controller:
                                                          c.noOfDependentTextController,
                                                      readOnly: true,
                                                      textInputType:
                                                          TextInputType.name,
                                                      inputStyle: TextStyle(
                                                        fontSize:
                                                            responsiveFont(12),
                                                        color: Colors.black,
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                      ),
                                                      label: RichText(
                                                        text: TextSpan(
                                                          text:
                                                              'No. of Dependent',
                                                          style: TextStyle(
                                                            color:
                                                                kLabelTextColor,
                                                            fontSize:
                                                                responsiveFont(
                                                                  14,
                                                                ) *
                                                                1.33,
                                                            fontFamily:
                                                                FontConstants
                                                                    .interFonts,
                                                            fontWeight:
                                                                FontWeight.w600,
                                                          ),
                                                          children: <TextSpan>[
                                                            TextSpan(
                                                              text: ' *',
                                                              style: TextStyle(
                                                                color:
                                                                    Colors.red,
                                                                fontSize:
                                                                    responsiveFont(
                                                                      14,
                                                                    ),
                                                              ),
                                                            ),
                                                          ],
                                                        ),
                                                      ),
                                                      labelStyle: TextStyle(
                                                        fontSize:
                                                            responsiveFont(12),
                                                      ),
                                                      prefixIcon: SizedBox(
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                        child: Center(
                                                          child: Image.asset(
                                                            iconPersons,
                                                            height:
                                                                responsiveHeight(
                                                                  24,
                                                                ),
                                                            width:
                                                                responsiveHeight(
                                                                  24,
                                                                ),
                                                          ),
                                                        ),
                                                      ),
                                                      suffixIcon: Image.asset(
                                                        downArrow,
                                                        scale: 3.5,
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                                SizedBox(width: 8.w),

                                                Expanded(
                                                  child: Visibility(
                                                    visible:
                                                        c.isTextFieldVisibilityPersonalDet,
                                                    child: AppTextField(
                                                      onTap: () {
                                                        if (c
                                                            .callStatusTextController
                                                            .text
                                                            .isEmpty) {
                                                          ToastManager.showAlertDialog(
                                                            Get.context!,
                                                            "Please select  call status first",
                                                            () {
                                                              Navigator.of(
                                                                context,
                                                              ).pop();
                                                            },
                                                          );

                                                          return;
                                                        }
                                                        if (c.selectedNumberOfDependent ==
                                                            null) {
                                                          ToastManager.showAlertDialog(
                                                            Get.context!,
                                                            "Please Select No Of Dependent First",
                                                            () {
                                                              Navigator.of(
                                                                context,
                                                              ).pop();
                                                            },
                                                          );

                                                          return;
                                                        }

                                                        showModalBottomSheet(
                                                          context: Get.context!,
                                                          isScrollControlled:
                                                              true,
                                                          builder: (context) {
                                                            return StatefulBuilder(
                                                              builder: (
                                                                bsCtx,
                                                                sheetState,
                                                              ) {
                                                                return SelectionBottomSheet<
                                                                  Map<
                                                                    String,
                                                                    dynamic
                                                                  >,
                                                                  int
                                                                >(
                                                                  title:
                                                                      "Dependent Screening Pending",
                                                                  items:
                                                                      dependentList,
                                                                  selectedValue:
                                                                      c.selectedDependentScreeningPending?["id"],
                                                                  valueFor:
                                                                      (item) =>
                                                                          item["id"] ??
                                                                          -1,
                                                                  labelFor:
                                                                      (item) =>
                                                                          item["name"] ??
                                                                          'NA',
                                                                  height: 360.h,
                                                                  padding: EdgeInsets.only(
                                                                    top:
                                                                        responsiveHeight(
                                                                          20,
                                                                        ),
                                                                    left:
                                                                        responsiveHeight(
                                                                          35,
                                                                        ),
                                                                    right:
                                                                        responsiveHeight(
                                                                          35,
                                                                        ),
                                                                    bottom:
                                                                        responsiveHeight(
                                                                          60,
                                                                        ),
                                                                  ),
                                                                  titleTextStyle: TextStyle(
                                                                    fontFamily:
                                                                        FontConstants
                                                                            .interFonts,
                                                                    fontSize:
                                                                        responsiveFont(
                                                                          16,
                                                                        ),
                                                                  ),
                                                                  titleBottomSpacing:
                                                                      responsiveHeight(
                                                                        30,
                                                                      ),
                                                                  itemPadding:
                                                                      EdgeInsets.symmetric(
                                                                        vertical:
                                                                            4.h,
                                                                      ),
                                                                  itemContainerPadding:
                                                                      const EdgeInsets.symmetric(
                                                                        horizontal:
                                                                            4.0,
                                                                      ),
                                                                  selectedBackgroundColor:
                                                                      kPrimaryColor
                                                                          .withOpacity(
                                                                            0.1,
                                                                          ),
                                                                  itemTextStyle: TextStyle(
                                                                    fontFamily:
                                                                        FontConstants
                                                                            .interFonts,
                                                                    fontWeight:
                                                                        FontWeight
                                                                            .normal,
                                                                    color:
                                                                        kBlackColor,
                                                                  ),
                                                                  selectedItemTextStyle: TextStyle(
                                                                    fontFamily:
                                                                        FontConstants
                                                                            .interFonts,
                                                                    fontWeight:
                                                                        FontWeight
                                                                            .normal,
                                                                    color:
                                                                        kBlackColor,
                                                                  ),
                                                                  onItemTap: (
                                                                    item,
                                                                  ) {
                                                                    // STEP 1: Update the visual state FIRST using sheetState
                                                                    sheetState(() {
                                                                      c.selectedDependentScreeningPending =
                                                                          item;
                                                                    });

                                                                    // STEP 2: Update the text field
                                                                    c.dependentScreeningPendingTextController.text =
                                                                        item["name"] ??
                                                                        "";
                                                                    c.numberOfDependentPending =
                                                                        item["id"] ??
                                                                        "";

                                                                    // STEP 3: Validation logic
                                                                    if (c.screenedBeneficiaryCount !=
                                                                        0) {
                                                                      if (c.numberOfDependentPending !=
                                                                          c.differenceCount) {
                                                                        ToastManager.showAlertDialog(
                                                                          Get.context!,
                                                                          "मागील ३६५ दिवसांत ${c.screenedBeneficiaryCount} dependent ची स्क्रिनिंग झालेली आहे, त्यामुळे त्याची गणना स्क्रिनिंग pending मध्ये करू नये.\nकृपया स्क्रिनिंगसाठी pending असलेल्या dependent ची योग्य संख्या नमूद करा.\nतसेच एकूण dependent ची संख्या बरोबर नोंदलेली आहे की नाही, याचीही खात्री करा",
                                                                          () {
                                                                            Get.back();
                                                                            // Clear selection on validation failure
                                                                            sheetState(() {
                                                                              c.selectedDependentScreeningPending = null;
                                                                            });
                                                                            c.dependentScreeningPendingTextController.text =
                                                                                "";
                                                                            c.numberOfDependentPending =
                                                                                0;
                                                                          },
                                                                        );

                                                                        return;
                                                                      }
                                                                    } else {
                                                                      if (c.numberOfDependentPending !=
                                                                          c.differenceCount) {
                                                                        ToastManager.showAlertDialog(
                                                                          Get.context!,
                                                                          "एकूण dependent संख्या आणि स्क्रीनिंग pending dependent संख्या यामध्ये तफावत दिसत आहे. कृपया खात्री करून स्क्रीनिंग pending असलेल्या dependent ची अचूक संख्या भरा.",
                                                                          () {
                                                                            Get.back();
                                                                            // Clear selection on validation failure
                                                                            sheetState(() {
                                                                              c.selectedDependentScreeningPending = null;
                                                                            });
                                                                            c.dependentScreeningPendingTextController.text =
                                                                                "";
                                                                            c.numberOfDependentPending =
                                                                                0;
                                                                          },
                                                                        );

                                                                        return;
                                                                      }
                                                                    }

                                                                    // STEP 4: Close sheet and trigger bloc event
                                                                    Get.back();
                                                                    c.svc
                                                                        .resetState();
                                                                  },
                                                                );
                                                              },
                                                            );
                                                          },
                                                        );
                                                      },
                                                      controller:
                                                          c.dependentScreeningPendingTextController,
                                                      readOnly: true,
                                                      textInputType:
                                                          TextInputType.name,
                                                      inputStyle: TextStyle(
                                                        fontSize:
                                                            responsiveFont(12),
                                                        color: Colors.black,
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                      ),
                                                      label: RichText(
                                                        text: TextSpan(
                                                          text:
                                                              'Dependent Screening Pending',
                                                          style: TextStyle(
                                                            color:
                                                                kLabelTextColor,
                                                            fontSize:
                                                                responsiveFont(
                                                                  14,
                                                                ) *
                                                                1.33,
                                                            fontFamily:
                                                                FontConstants
                                                                    .interFonts,
                                                            fontWeight:
                                                                FontWeight.w600,
                                                          ),
                                                          children: <TextSpan>[
                                                            TextSpan(
                                                              text: ' *',
                                                              style: TextStyle(
                                                                color:
                                                                    Colors.red,
                                                                fontSize:
                                                                    responsiveFont(
                                                                      12,
                                                                    ),
                                                              ),
                                                            ),
                                                          ],
                                                        ),
                                                      ),
                                                      labelStyle: TextStyle(
                                                        fontSize:
                                                            responsiveFont(12),
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                      ),
                                                      prefixIcon: SizedBox(
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                        child: Center(
                                                          child: Image.asset(
                                                            iconPersons,
                                                            height:
                                                                responsiveHeight(
                                                                  24,
                                                                ),
                                                            width:
                                                                responsiveHeight(
                                                                  24,
                                                                ),
                                                          ),
                                                        ),
                                                      ),
                                                      suffixIcon: Image.asset(
                                                        downArrow,
                                                        scale: 3.5,
                                                      ),
                                                    ),
                                                  ),
                                                ),
                                              ],
                                            ),

                                            SizedBox(
                                              height: responsiveHeight(16),
                                            ),

                                            Visibility(
                                              visible:
                                                  c.isTextFieldVisibilityPersonalDet,
                                              child: AppButtonWithIcon(
                                                buttonColor: kPrimaryColor,
                                                title: "Add Dependent",
                                                icon: Image.asset(
                                                  icSquarePlus,
                                                  height: responsiveHeight(24),
                                                  width: responsiveHeight(24),
                                                ),
                                                mWidth: responsiveWidth(200),
                                                mHeight: 50.h,
                                                textStyle: TextStyle(
                                                  fontWeight: FontWeight.w600,
                                                  color: Colors.white,
                                                  fontSize: responsiveFont(14),
                                                  fontFamily:
                                                      FontConstants.interFonts,
                                                ),
                                                onTap: () async {
                                                  // Navigator.pushNamed(context,
                                                  //     AppRoutes.addDependent,
                                                  //     arguments:
                                                  //         c.selectedBeneficiary);

                                                  if (c.numberOfDependent ==
                                                      0) {
                                                    ToastManager.showAlertDialog(
                                                      Get.context!,
                                                      "Please select number of dependent",
                                                      () {
                                                        Navigator.of(
                                                          Get.context!,
                                                        ).pop();
                                                      },
                                                    );

                                                    return;
                                                  }
                                                  if (c
                                                          .workersMartialStatusTextController
                                                          .text ==
                                                      "") {
                                                    ToastManager.showAlertDialog(
                                                      Get.context!,
                                                      "Please select worker marital status",
                                                      () {
                                                        Navigator.of(
                                                          Get.context!,
                                                        ).pop();
                                                      },
                                                    );

                                                    return;
                                                  }
                                                  if (c.numberOfDependentPending ==
                                                      0) {
                                                    ToastManager.showAlertDialog(
                                                      Get.context!,
                                                      "Please select number of dependent screening pending",
                                                      () {
                                                        Navigator.of(
                                                          Get.context!,
                                                        ).pop();
                                                      },
                                                    );

                                                    return;
                                                  }

                                                  Navigator.pushNamed(
                                                    context,
                                                    AppRoutes.addDependent,
                                                    arguments: {
                                                      "selectedBeneficiary":
                                                          c.selectedBeneficiary,
                                                      "workerGender":
                                                          c.workerGender,
                                                      "noOfDependent":
                                                          c.numberOfDependent,
                                                      "numberOfDependentPending":
                                                          c.numberOfDependentPending,

                                                      "currentCount":
                                                          c
                                                              .svc
                                                              .addDependentOutput
                                                              .length,
                                                      "assignCallId":
                                                          c
                                                              .selectedBeneficiary
                                                              ?.assignCallID ??
                                                          0,
                                                      "marriedStatus":
                                                          c.marriedStatusID,
                                                      "screeningdataList":
                                                          c.screeningdataList,
                                                    },
                                                  );
                                                },
                                              ),
                                            ),
                                            SizedBox(
                                              height: responsiveHeight(10),
                                            ),
                                            Obx(
                                              () =>
                                                  c
                                                          .svc
                                                          .getDependentStatus
                                                          .value
                                                          .isInProgress
                                                      ? const CircularProgressIndicator()
                                                      : ListView.builder(
                                                        shrinkWrap: true,
                                                        physics:
                                                            const NeverScrollableScrollPhysics(),
                                                        itemCount:
                                                            c
                                                                .svc
                                                                .addDependentOutput
                                                                .length,
                                                        itemBuilder:
                                                            (
                                                              context,
                                                              index,
                                                            ) => Padding(
                                                              padding:
                                                                  const EdgeInsets.all(
                                                                    6.0,
                                                                  ),
                                                              child: Container(
                                                                decoration: BoxDecoration(
                                                                  boxShadow: const [
                                                                    BoxShadow(
                                                                      offset:
                                                                          Offset(
                                                                            0,
                                                                            1,
                                                                          ),
                                                                      color: Color(
                                                                        0xff00000026,
                                                                      ),
                                                                      spreadRadius:
                                                                          0,
                                                                      blurRadius:
                                                                          10,
                                                                    ),
                                                                  ],
                                                                  color: const Color(
                                                                    0XFFF8F8FF,
                                                                  ),
                                                                  borderRadius:
                                                                      BorderRadius.circular(
                                                                        10,
                                                                      ),
                                                                  border: Border.all(
                                                                    color: const Color(
                                                                      0XFFCFCBEC,
                                                                    ),
                                                                  ),
                                                                ),
                                                                child: Padding(
                                                                  padding:
                                                                      EdgeInsets.all(
                                                                        responsiveHeight(
                                                                          10,
                                                                        ),
                                                                      ),
                                                                  child: Column(
                                                                    children: [
                                                                      Row(
                                                                        mainAxisAlignment:
                                                                            MainAxisAlignment.spaceBetween,
                                                                        children: [
                                                                          Expanded(
                                                                            child: Text(
                                                                              '${c.svc.addDependentOutput[index].firstName} ${c.svc.addDependentOutput[index].middleName} ${c.svc.addDependentOutput[index].lastName} ',
                                                                              style: TextStyle(
                                                                                fontSize: responsiveHeight(
                                                                                  14,
                                                                                ),
                                                                                fontWeight:
                                                                                    FontWeight.w600,
                                                                                fontFamily:
                                                                                    FontConstants.interFonts,
                                                                              ),
                                                                            ),
                                                                          ),
                                                                          GestureDetector(
                                                                            onTap: () {
                                                                              c.svc.resetState();
                                                                              c.svc.deleteDependent(
                                                                                index,
                                                                              );
                                                                            },
                                                                            child: Container(
                                                                              padding: EdgeInsets.all(
                                                                                responsiveHeight(
                                                                                  8,
                                                                                ),
                                                                              ),
                                                                              // Adjust padding as needed
                                                                              decoration: BoxDecoration(
                                                                                borderRadius: BorderRadius.circular(
                                                                                  responsiveHeight(
                                                                                    5,
                                                                                  ),
                                                                                ),
                                                                              ),
                                                                              child: Image.asset(
                                                                                iconTrash,
                                                                                scale:
                                                                                    3.5,
                                                                              ),
                                                                            ),
                                                                          ),
                                                                        ],
                                                                      ),
                                                                      SizedBox(
                                                                        height:
                                                                            responsiveHeight(
                                                                              4,
                                                                            ),
                                                                      ),
                                                                      Row(
                                                                        children: [
                                                                          Image.asset(
                                                                            iconPersons,
                                                                            scale:
                                                                                3.5,
                                                                          ),
                                                                          const SizedBox(
                                                                            width:
                                                                                8,
                                                                          ),

                                                                          // Space between image and text
                                                                          Text.rich(
                                                                            TextSpan(
                                                                              text:
                                                                                  'Relation: ',
                                                                              // First part (static text)
                                                                              style: TextStyle(
                                                                                fontSize: responsiveFont(
                                                                                  14,
                                                                                ),
                                                                                fontWeight:
                                                                                    FontWeight.w500,
                                                                                fontFamily:
                                                                                    FontConstants.interFonts,
                                                                              ),
                                                                              children: [
                                                                                TextSpan(
                                                                                  text: c.getRelationText(
                                                                                    c.svc.addDependentOutput[index].relId,
                                                                                  ),
                                                                                  // Dynamic text
                                                                                  style: TextStyle(
                                                                                    fontSize: responsiveFont(
                                                                                      14,
                                                                                    ),
                                                                                    // Different size
                                                                                    fontWeight:
                                                                                        FontWeight.w400,
                                                                                    // Different weight
                                                                                    color: const Color(
                                                                                      0xFF666666,
                                                                                    ),
                                                                                    fontFamily:
                                                                                        FontConstants.interFonts,
                                                                                    // Different color (optional)
                                                                                  ),
                                                                                ),
                                                                              ],
                                                                            ),
                                                                          ),
                                                                        ],
                                                                      ),
                                                                      SizedBox(
                                                                        height:
                                                                            responsiveHeight(
                                                                              4,
                                                                            ),
                                                                      ),
                                                                      Row(
                                                                        children: [
                                                                          Image.asset(
                                                                            calendar,
                                                                            scale:
                                                                                3.5,
                                                                          ),
                                                                          const SizedBox(
                                                                            width:
                                                                                8,
                                                                          ),

                                                                          // Space between image and text
                                                                          Text.rich(
                                                                            TextSpan(
                                                                              text:
                                                                                  'Last Screening Date: ',
                                                                              // First part (static text)
                                                                              style: TextStyle(
                                                                                fontSize: responsiveFont(
                                                                                  14,
                                                                                ),
                                                                                fontWeight:
                                                                                    FontWeight.w500,
                                                                              ),
                                                                              children: [
                                                                                TextSpan(
                                                                                  text:
                                                                                      (c.svc.addDependentOutput[index].lastDependantScreeningDate),
                                                                                  // Dynamic text
                                                                                  style: TextStyle(
                                                                                    fontSize: responsiveFont(
                                                                                      14,
                                                                                    ),
                                                                                    // Different size
                                                                                    fontWeight:
                                                                                        FontWeight.w400,
                                                                                    // Different weight
                                                                                    color: const Color(
                                                                                      0xFF666666,
                                                                                    ),
                                                                                    fontFamily:
                                                                                        FontConstants.interFonts, // Different color (optional)
                                                                                  ),
                                                                                ),
                                                                              ],
                                                                            ),
                                                                          ),
                                                                        ],
                                                                      ),
                                                                      SizedBox(
                                                                        height:
                                                                            responsiveHeight(
                                                                              4,
                                                                            ),
                                                                      ),
                                                                      Row(
                                                                        children: [
                                                                          Image.asset(
                                                                            calendar,
                                                                            scale:
                                                                                3.5,
                                                                          ),
                                                                          const SizedBox(
                                                                            width:
                                                                                8,
                                                                          ),

                                                                          // Space between image and text
                                                                          Text.rich(
                                                                            TextSpan(
                                                                              text:
                                                                                  'Age: ',
                                                                              // First part (static text)
                                                                              style: TextStyle(
                                                                                fontSize: responsiveFont(
                                                                                  14,
                                                                                ),
                                                                                fontWeight:
                                                                                    FontWeight.w500,
                                                                                fontFamily:
                                                                                    FontConstants.interFonts,
                                                                              ),
                                                                              children: [
                                                                                TextSpan(
                                                                                  text:
                                                                                      (c.svc.addDependentOutput[index].age),
                                                                                  // Dynamic text
                                                                                  style: TextStyle(
                                                                                    fontSize: responsiveFont(
                                                                                      14,
                                                                                    ),
                                                                                    // Different size
                                                                                    fontWeight:
                                                                                        FontWeight.w400,
                                                                                    // Different weight
                                                                                    color: const Color(
                                                                                      0xFF666666,
                                                                                    ),
                                                                                    fontFamily:
                                                                                        FontConstants.interFonts, // Different color (optional)
                                                                                  ),
                                                                                ),
                                                                              ],
                                                                            ),
                                                                          ),
                                                                        ],
                                                                      ),
                                                                      SizedBox(
                                                                        height:
                                                                            responsiveHeight(
                                                                              4,
                                                                            ),
                                                                      ),
                                                                    ],
                                                                  ),
                                                                ),
                                                              ),
                                                            ),
                                                      ),
                                            ),

                                            SizedBox(
                                              height: responsiveHeight(10),
                                            ),

                                            Visibility(
                                              visible:
                                                  c.isTextFieldVisibilityDateTime,
                                              child: Row(
                                                children: [
                                                  Expanded(
                                                    flex: 1,
                                                    child: AppTextField(
                                                      onTap: () {
                                                        if (c
                                                            .callStatusTextController
                                                            .text
                                                            .isEmpty) {
                                                          ToastManager.showAlertDialog(
                                                            Get.context!,
                                                            "Please select call status first",
                                                            () {
                                                              Navigator.of(
                                                                Get.context!,
                                                              ).pop();
                                                            },
                                                          );

                                                          // showDialog(
                                                          //   ...
                                                          // );

                                                          return;
                                                        }

                                                        DateTime today =
                                                            DateTime.now();

                                                        showDatePicker(
                                                          context: Get.context!,
                                                          initialDate: today,
                                                          firstDate: today,
                                                          lastDate: DateTime(
                                                            2100,
                                                          ),
                                                          helpText:
                                                              "Select Date",
                                                          initialEntryMode:
                                                              DatePickerEntryMode
                                                                  .calendarOnly,
                                                        ).then((value) {
                                                          if (value != null) {
                                                            c.crrDate = value;
                                                            c.firstDayOfWeek = c
                                                                .crrDate
                                                                .subtract(
                                                                  Duration(
                                                                    days:
                                                                        c
                                                                            .crrDate
                                                                            .weekday -
                                                                        1,
                                                                  ),
                                                                );

                                                            // Format the selected date as 'dd-MMMM-yyyy'
                                                            String
                                                            formattedDate =
                                                                DateFormat(
                                                                  "dd-MMMM-yyyy",
                                                                ).format(value);

                                                            // Set the selected date to the text controller
                                                            c.timeTextController.text =
                                                                formattedDate;

                                                            // API: check appointment count/slot availability for the selected date.
                                                            c.svc.fetchAppointmentCount({
                                                              "AppoinmentDate":
                                                                  c
                                                                      .timeTextController
                                                                      .text,
                                                            });
                                                            c.update();
                                                          }
                                                        });
                                                      },
                                                      controller:
                                                          c.timeTextController,
                                                      readOnly: true,
                                                      textInputType:
                                                          TextInputType.name,
                                                      inputStyle: TextStyle(
                                                        fontSize:
                                                            responsiveFont(12),
                                                        color: Colors.black,
                                                      ),
                                                      label: RichText(
                                                        text: TextSpan(
                                                          text: 'Date',
                                                          style: TextStyle(
                                                            color:
                                                                kLabelTextColor,
                                                            fontSize:
                                                                responsiveFont(
                                                                  14,
                                                                ) *
                                                                1.33,
                                                            fontWeight:
                                                                FontWeight.w600,
                                                          ),
                                                        ),
                                                      ),
                                                      labelStyle: TextStyle(
                                                        fontSize:
                                                            responsiveFont(12),
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                      ),
                                                      prefixIcon: SizedBox(
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                        child: Center(
                                                          child: Image.asset(
                                                            calendar,
                                                            height:
                                                                responsiveHeight(
                                                                  24,
                                                                ),
                                                            width:
                                                                responsiveHeight(
                                                                  24,
                                                                ),
                                                          ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                  SizedBox(
                                                    width: responsiveHeight(10),
                                                  ),
                                                  Expanded(
                                                    flex: 1,
                                                    child: AppTextField(
                                                      onTap: () async {
                                                        c.showCustomTimePicker();
                                                      },
                                                      controller:
                                                          c.timeAppTextController,
                                                      readOnly: true,
                                                      textInputType:
                                                          TextInputType.name,
                                                      inputStyle: TextStyle(
                                                        fontSize:
                                                            responsiveFont(12),
                                                        color: Colors.black,
                                                        fontFamily:
                                                            FontConstants
                                                                .interFonts,
                                                      ),
                                                      label: RichText(
                                                        text: TextSpan(
                                                          text: 'Time',
                                                          style: TextStyle(
                                                            color:
                                                                kLabelTextColor,
                                                            fontSize:
                                                                responsiveFont(
                                                                  14,
                                                                ) *
                                                                1.33,
                                                            fontFamily:
                                                                FontConstants
                                                                    .interFonts,
                                                            fontWeight:
                                                                FontWeight.w600,
                                                          ),
                                                          children: <TextSpan>[
                                                            TextSpan(
                                                              text: ' *',
                                                              style: TextStyle(
                                                                color:
                                                                    Colors.red,
                                                                fontSize:
                                                                    responsiveFont(
                                                                      14,
                                                                    ),
                                                              ),
                                                            ),
                                                          ],
                                                        ),
                                                      ),
                                                      labelStyle: TextStyle(
                                                        fontSize:
                                                            responsiveFont(12),
                                                      ),
                                                      prefixIcon: SizedBox(
                                                        height:
                                                            responsiveHeight(
                                                              24,
                                                            ),
                                                        width: responsiveHeight(
                                                          24,
                                                        ),
                                                        child: Center(
                                                          child: Image.asset(
                                                            iconClock,
                                                            height:
                                                                responsiveHeight(
                                                                  24,
                                                                ),
                                                            width:
                                                                responsiveHeight(
                                                                  24,
                                                                ),
                                                          ),
                                                        ),
                                                      ),
                                                    ),
                                                  ),
                                                ],
                                              ),
                                            ),
                                            SizedBox(
                                              height: responsiveHeight(10),
                                            ),
                                            AppTextField(
                                              onTap: () {
                                                if (c
                                                    .callStatusTextController
                                                    .text
                                                    .isEmpty) {
                                                  ToastManager.showAlertDialog(
                                                    Get.context!,
                                                    "Please select call status first",
                                                    () {
                                                      Navigator.of(
                                                        Get.context!,
                                                      ).pop();
                                                    },
                                                  );

                                                  // showDialog(
                                                  //   ...
                                                  // );

                                                  return;
                                                }

                                                c.svc.fetchRemarks({
                                                  "CallStatusID":
                                                      c.callStatus.toString(),
                                                });
                                              },
                                              controller:
                                                  c.remarkTextController,
                                              readOnly: true,
                                              textInputType: TextInputType.name,
                                              inputStyle: TextStyle(
                                                fontSize: responsiveFont(12),
                                                color: Colors.black,
                                              ),
                                              label: RichText(
                                                text: TextSpan(
                                                  text: 'Remark',
                                                  style: TextStyle(
                                                    color: kLabelTextColor,
                                                    fontSize:
                                                        responsiveFont(14) *
                                                        1.33,
                                                    fontFamily:
                                                        FontConstants
                                                            .interFonts,
                                                    fontWeight: FontWeight.w600,
                                                  ),
                                                  children: <TextSpan>[
                                                    TextSpan(
                                                      text: ' *',
                                                      style: TextStyle(
                                                        color: Colors.red,
                                                        fontSize:
                                                            responsiveFont(14),
                                                      ),
                                                    ),
                                                  ],
                                                ),
                                              ),
                                              labelStyle: TextStyle(
                                                fontSize: responsiveFont(12),
                                                fontFamily:
                                                    FontConstants.interFonts,
                                              ),
                                              prefixIcon: SizedBox(
                                                height: responsiveHeight(24),
                                                width: responsiveHeight(24),
                                                child: Center(
                                                  child: Image.asset(
                                                    iconFile,
                                                    height: responsiveHeight(
                                                      24,
                                                    ),
                                                    width: responsiveHeight(24),
                                                  ),
                                                ),
                                              ),
                                              suffixIcon: Image.asset(
                                                downArrow,
                                                scale: 3.5,
                                              ),
                                            ),
                                            // SizedBox(
                                            //   height: responsiveHeight(29),
                                            // ),
                                          ],
                                        ),
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                              SizedBox(height: responsiveHeight(20)),
                              // Padding(
                              //   padding: const EdgeInsets.symmetric(...),
                              //   ...
                              // ),
                              AppButtonWithIcon(
                                title: "Save",
                                mHeight: responsiveHeight(50),
                                mWidth: responsiveWidth(140),
                                onTap: () {
                                  c.isSubmitted = true;
                                  c.update();
                                  if (c.callStatusTextController.text.isEmpty) {
                                    ToastManager.showAlertDialog(
                                      Get.context!,
                                      "Please select call status first",
                                      () {
                                        Navigator.of(Get.context!).pop();
                                      },
                                    );

                                    return;
                                  }
                                  if (c.callLog == 0) {
                                    ToastManager.showAlertDialog(
                                      Get.context!,
                                      "It seems you have not called to beneficiary. Please call to beneficiary first and fill in the information",
                                      () {
                                        Navigator.of(Get.context!).pop();
                                      },
                                    );

                                    return;
                                  }

                                  if (c.remarkTextController.text == "") {
                                    ToastManager.showAlertDialog(
                                      Get.context!,
                                      "Please select remark",
                                      () {
                                        Navigator.of(Get.context!).pop();
                                      },
                                    );

                                    return;
                                  }

                                  if (c.callStatus == 2) {
                                    if (!(c.svc.addDependentOutput.length ==
                                        c.numberOfDependentPending)) {
                                      // if (!(_controller.addDependentOutput.length ==
                                      //     int.parse(
                                      //       noOfDependentTextController.text,
                                      //     )));
                                      ToastManager.showAlertDialog(
                                        Get.context!,
                                        "Please add or remove dependent, Dependent count should be equal to number of dependent count",
                                        () {
                                          Navigator.of(Get.context!).pop();
                                        },
                                      );

                                      return;
                                    }

                                    if (c.workersGenderTextController.text ==
                                        "") {
                                      ToastManager.showAlertDialog(
                                        Get.context!,
                                        "Please select worker gender",
                                        () {
                                          Navigator.of(Get.context!).pop();
                                        },
                                      );

                                      return;
                                    }

                                    if (c.pincodeTextController.text == "") {
                                      ToastManager.showAlertDialog(
                                        Get.context!,
                                        "Please enter pincode",
                                        () {
                                          Navigator.of(Get.context!).pop();
                                        },
                                      );

                                      return;
                                    }

                                    if (c.talukaTextController.text == "") {
                                      ToastManager.showAlertDialog(
                                        Get.context!,
                                        "Please enter taluka",
                                        () {
                                          Navigator.of(Get.context!).pop();
                                        },
                                      );

                                      return;
                                    }
                                    if (c.districtTextController.text == "") {
                                      ToastManager.showAlertDialog(
                                        Get.context!,
                                        "Please enter district",
                                        () {
                                          Navigator.of(Get.context!).pop();
                                        },
                                      );

                                      return;
                                    }
                                    if (c
                                            .workersMartialStatusTextController
                                            .text ==
                                        "") {
                                      ToastManager.showAlertDialog(
                                        Get.context!,
                                        "Please select worker marital status",
                                        () {
                                          Navigator.of(Get.context!).pop();
                                        },
                                      );

                                      return;
                                    }

                                    if (int.tryParse(c.marriedStatusID) == 1 ||
                                        int.tryParse(c.marriedStatusID) == 2 ||
                                        int.tryParse(c.marriedStatusID) == 3 ||
                                        int.tryParse(c.marriedStatusID) == 4) {
                                      if (c.noOfDependentTextController.text ==
                                          "") {
                                        ToastManager.showAlertDialog(
                                          Get.context!,
                                          "Please select number of dependent",
                                          () {
                                            Navigator.of(Get.context!).pop();
                                          },
                                        );

                                        return;
                                      }
                                      if (c
                                              .dependentScreeningPendingTextController
                                              .text ==
                                          "") {
                                        ToastManager.showAlertDialog(
                                          Get.context!,
                                          "Please select Number Of Screening Pending Dependent",
                                          () {
                                            Navigator.of(Get.context!).pop();
                                          },
                                        );

                                        return;
                                      }
                                      if (c.timeTextController.text == "") {
                                        ToastManager.showAlertDialog(
                                          Get.context!,
                                          "Please select appointment date",
                                          () {
                                            Navigator.of(Get.context!).pop();
                                          },
                                        );

                                        return;
                                      }
                                      if (c.timeAppTextController.text == "") {
                                        ToastManager.showAlertDialog(
                                          Get.context!,
                                          "Please select appointment time",
                                          () {
                                            Navigator.of(Get.context!).pop();
                                          },
                                        );

                                        return;
                                      }
                                    }
                                  }

                                  if (!c.formKey.currentState!.validate()) {
                                    return;
                                  }

                                  String dependentJson =
                                      (c.svc.addDependentOutput.isNotEmpty)
                                          ? jsonEncode(
                                            c.svc.addDependentOutput
                                                .map((e) => e.toJson())
                                                .toList(),
                                          )
                                          : '[{"AssignCallID":"0","RelId":"0","Age":"0","FirstName":" ","MiddleName":" ","LastName":" ","LastDependantScreeningDate":" "}]';

                                  print(dependentJson);

                                  WidgetsBinding.instance.addPostFrameCallback((
                                    timeStamp,
                                  ) {
                                    // API: submit the appointment confirmation details and dependent data.
                                    c.svc.insertAppointment({
                                      "AssignCallID":
                                          c.selectedBeneficiary?.assignCallID
                                              .toString(),
                                      "CallStatusID": c.callStatus.toString(),
                                      "RegisteredAddress": c.regAddress,
                                      "CurrentAddress": c.regAddress,
                                      "IsCurrentSameAsRegd":
                                          c.isCurrentAsSameRegId.toString(),
                                      "RegMobileNo":
                                          c.selectedBeneficiary?.mobile
                                              .toString(),
                                      "AltMobileNo":
                                          c.alternateMobileTextController.text,
                                      "Pincode": c.pincodeTextController.text,
                                      "Landmark": c.landMarkTextController.text,
                                      "WorkersGender":
                                          c.workersGenderTextController.text,
                                      "WorkersMaritalStatus":
                                          (c.marriedStatusID.isEmpty)
                                              ? "0"
                                              : c.marriedStatusID,
                                      "NoOfDependants":
                                          c.numberOfDependent.toString(),
                                      "DependantScreeningPending":
                                          c.numberOfDependentPending.toString(),
                                      "AppoinmentDate":
                                          c.timeTextController.text,
                                      "AppoinmentTime":
                                          c.timeAppTextController.text,
                                      "Remark": c.remarkTextController.text,
                                      "DISTLGDCODE": c.districtCode.toString(),
                                      "TALLGDCODE": c.talukaCode.toString(),
                                      "HouseNo":
                                          c.houseNumberTextController.text,
                                      "Road": c.roadTextController.text,
                                      "Area": c.areaTextController.text,
                                      "DependantDetails": dependentJson,
                                      "CReatedBy": c.empCode.toString(),
                                      "WorkerScreeningStatus":
                                          c
                                              .selectedBeneficiary
                                              ?.isWorkerScreened
                                              .toString(),
                                      "RemarkID": c.remarkId.toString(),
                                    });
                                  });
                                },
                                textStyle: TextStyle(
                                  color: Colors.white,
                                  fontSize: responsiveFont(14),
                                  fontWeight: FontWeight.w600,
                                  fontFamily: FontConstants.interFonts,
                                ),
                                buttonColor: kPrimaryColor,
                                icon: Image.asset(
                                  iconArrow,
                                  height: responsiveHeight(24),
                                  width: responsiveHeight(24),
                                  color: kWhiteColor,
                                ),
                              ),
                              SizedBox(height: responsiveHeight(20)),
                            ],
                          ),
                        ),
                      ),
                    )
                    : const NoDataFound(),
            //   ],
            // ),
          ),
        );
      },
    );
  }
}
