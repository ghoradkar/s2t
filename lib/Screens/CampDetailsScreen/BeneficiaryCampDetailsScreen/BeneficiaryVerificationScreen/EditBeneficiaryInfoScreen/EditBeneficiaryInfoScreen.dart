// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../../Modules/Json_Class/BeneficiaryWorkerResponse/BeneficiaryWorkerResponse.dart';
import '../../../../../Modules/constants/constants.dart';
import '../../../../../Modules/constants/images.dart';

class EditBeneficiaryInfoScreen extends StatefulWidget {
  EditBeneficiaryInfoScreen({super.key, required this.beneficiaryWorkerOutput});

  BeneficiaryWorkerOutput? beneficiaryWorkerOutput;

  @override
  State<EditBeneficiaryInfoScreen> createState() =>
      _EditBeneficiaryInfoScreenState();
}

class _EditBeneficiaryInfoScreenState extends State<EditBeneficiaryInfoScreen> {
  bool isExpaneded = true;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: EdgeInsets.fromLTRB(0, 10.h, 0, 0),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          border: Border.all(width: 1, color: droDownBGColor),
          borderRadius:
              isExpaneded == true
                  ? BorderRadius.only(
                    topLeft: Radius.circular(8),
                    topRight: Radius.circular(8),
                  )
                  : BorderRadius.all(Radius.circular(8)),
        ),
        child: Column(
          children: [
            Container(
              padding: EdgeInsets.fromLTRB(12.w, 3.h, 12.w, 3.h),
              decoration: BoxDecoration(
                color: kPrimaryColor,
                borderRadius:
                    isExpaneded == true
                        ? BorderRadius.only(
                          topLeft: Radius.circular(8),
                          topRight: Radius.circular(8),
                        )
                        : BorderRadius.all(Radius.circular(8)),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(
                      "Edit Beneficiary Info",
                      style: TextStyle(
                        color: kWhiteColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w400,
                        fontSize: 12.sp,
                      ),
                    ),
                  ),
                  GestureDetector(
                    onTap: () {
                      isExpaneded = !isExpaneded;
                      setState(() {});
                    },
                    child: SizedBox(
                      width: 30.w,
                      height: 30.h,
                      child: Image.asset(
                        isExpaneded == true ? icUpArrowIcon : icDownArrowIcon,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            // isExpaneded == true
            //     ? Padding(
            //       padding: const EdgeInsets.fromLTRB(0, 8, 0, 0),
            //       child: AppIconTextfield(
            //         icon: icInitiatedBy,
            //         titleHeaderString: "Worker Name",
            //         controller: TextEditingController(
            //           text:
            //               "${widget.beneficiaryWorkerOutput?.workerName ?? ""} [${widget.beneficiaryWorkerOutput?.aGE ?? "-"}/${widget.beneficiaryWorkerOutput?.gENDER ?? ""}]",
            //         ),
            //         isDisabled: true,
            //         enabled: false,
            //       ),
            //     )
            //     : Container(),
            isExpaneded == true
                ? AppTextField(
                  readOnly: true,
                  controller: TextEditingController(
                    text:
                        "${widget.beneficiaryWorkerOutput?.workerName ?? ""} [${widget.beneficiaryWorkerOutput?.aGE ?? "-"}/${widget.beneficiaryWorkerOutput?.gENDER ?? ""}]",
                  ),
                  onChange: (value) {},
                  hint: 'Worker Name',
                  label: CommonText(
                    text: 'Worker Name',
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
                        icInitiatedBy,
                        height: 24.h,
                        width: 24.w,
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                ).paddingOnly(top: 12.h)
                : Container(),
            isExpaneded == true
                ? Row(
                  children: [
                    // Expanded(
                    //   child: AppIconTextfield(
                    //     icon: icInitiatedBy,
                    //     titleHeaderString: "Relation",
                    //     controller: TextEditingController(
                    //       text: widget.beneficiaryWorkerOutput?.relName ?? "",
                    //     ),
                    //     isDisabled: true,
                    //     enabled: false,
                    //   ),
                    // ),
                    Expanded(
                      child: AppTextField(
                        controller: TextEditingController(
                          text: widget.beneficiaryWorkerOutput?.relName ?? "",
                        ),
                        readOnly: true,
                        onChange: (value) {},
                        hint: 'Relation',
                        label: CommonText(
                          text: 'Relation',
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
                              icInitiatedBy,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ),
                    ),
                    SizedBox(width: 8.w),

                    // Expanded(
                    //   child: AppIconTextfield(
                    //     icon: icCalendarMonth,
                    //     titleHeaderString: "Age",
                    //     controller: TextEditingController(
                    //       text:
                    //           widget.beneficiaryWorkerOutput?.aGE
                    //               .toString() ??
                    //           "",
                    //     ),
                    //     isDisabled: true,
                    //     enabled: false,
                    //   ),
                    // ),
                    Expanded(
                      child: AppTextField(
                        controller: TextEditingController(
                          text:
                              widget.beneficiaryWorkerOutput?.aGE.toString() ??
                              "",
                        ),
                        readOnly: true,
                        onChange: (value) {},
                        hint: 'Age',
                        label: CommonText(
                          text: 'Age',
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
                              icCalendarMonth,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ],
                ).paddingOnly(top: 8.h)
                : Container(),
            isExpaneded == true
                ? Row(
                  children: [
                    // Expanded(
                    //   child: AppIconTextfield(
                    //     icon: icHeightIcon,
                    //     titleHeaderString: "Height (cms)",
                    //     controller: TextEditingController(
                    //       text:
                    //           widget.beneficiaryWorkerOutput?.heightCMs !=
                    //                   null
                    //               ? widget.beneficiaryWorkerOutput?.heightCMs
                    //                   .toString()
                    //               : ""
                    //                   "",
                    //     ),
                    //     isDisabled: true,
                    //     enabled: false,
                    //   ),
                    // ),
                    Expanded(
                      child: AppTextField(
                        controller: TextEditingController(
                          text:
                              widget.beneficiaryWorkerOutput?.heightCMs != null
                                  ? widget.beneficiaryWorkerOutput?.heightCMs
                                      .toString()
                                  : ""
                                      "",
                        ),
                        readOnly: true,
                        onChange: (value) {},
                        hint: 'Height (cms)',
                        label: CommonText(
                          text: 'Height (kg)',
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
                              icHeightIcon,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ),
                    ),
                    SizedBox(width: 8.w),
                    // Expanded(
                    //   child: AppIconTextfield(
                    //     icon: icWeightIcon,
                    //     titleHeaderString: "Weight (kg)",
                    //     controller: TextEditingController(
                    //       text:
                    //           widget.beneficiaryWorkerOutput?.weightKGs !=
                    //                   null
                    //               ? widget.beneficiaryWorkerOutput?.weightKGs
                    //                   .toString()
                    //               : "",
                    //     ),
                    //     isDisabled: true,
                    //     enabled: false,
                    //   ),
                    // ),
                    Expanded(
                      child: AppTextField(
                        controller: TextEditingController(
                          text:
                              widget.beneficiaryWorkerOutput?.weightKGs != null
                                  ? widget.beneficiaryWorkerOutput?.weightKGs
                                      .toString()
                                  : "",
                        ),
                        readOnly: true,
                        onChange: (value) {},
                        hint: 'Weight (kg)',
                        label: CommonText(
                          text: 'Weight (kg)',
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
                              icWeightIcon,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ],
                ).paddingOnly(top: 8.h)
                : Container(),
            isExpaneded == true
                ? AppTextField(
                  controller: TextEditingController(
                    text:
                        widget
                            .beneficiaryWorkerOutput
                            ?.sampleCollectedBarcode ??
                        "",
                  ),
                  readOnly: true,
                  onChange: (value) {},
                  hint: 'Scanned Sample Barcode',
                  label: CommonText(
                    text: 'Scanned Sample Barcode',
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
                        icBarcodeIcon,
                        height: 24.h,
                        width: 24.w,
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                ).paddingOnly(top: 8.h,bottom: 4.h)
                : Container(),
          ],
        ),
      ),
    );
  }
}
