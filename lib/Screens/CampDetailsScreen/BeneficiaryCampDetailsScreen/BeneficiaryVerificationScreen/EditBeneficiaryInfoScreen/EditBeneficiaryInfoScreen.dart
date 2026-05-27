// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../../Modules/Json_Class/BeneficiaryWorkerResponse/BeneficiaryWorkerResponse.dart';
import '../../../../../Modules/constants/constants.dart';
import '../../../../../Modules/constants/images.dart';
import '../FullScreenImageScreen/FullScreenImageScreen.dart';

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
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        _beneficiaryInfoAccordion(),
        _rationCardSection(),
      ],
    );
  }

  Widget _beneficiaryInfoAccordion() {
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
                      "Beneficiary Info",
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
                  hint: 'Worker Name(Age/Gender)',
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
                          text: _buildAgeText(),
                        ),
                        readOnly: true,
                        onChange: (value) {},
                        hint: 'Age (Year and days)',
                        label: CommonText(
                          text: 'Age (Year and days)',
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
                ).paddingOnly(top: 8.h)
                : Container(),
            isExpaneded == true
                ? AppTextField(
                  controller: TextEditingController(
                    text: widget.beneficiaryWorkerOutput?.aadharCardNo ?? "",
                  ),
                  readOnly: true,
                  onChange: (value) {},
                  hint: 'Aadhaar Number',
                  label: CommonText(
                    text: 'Aadhaar Number',
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
                ).paddingOnly(top: 8.h, bottom: 4.h)
                : Container(),
          ],
        ),
      ),
    );
  }

  // -- Helpers -----------------------------------------------------------------

  String _buildAgeText() {
    final age = widget.beneficiaryWorkerOutput?.aGE;
    final days = widget.beneficiaryWorkerOutput?.totalDays;
    if (age == null) return '';
    final daysStr =
        (days != null && days.isNotEmpty && days != '0') ? ' $days Days' : '';
    return '$age Years$daysStr';
  }

  // ---------------------------------------------------------------------------
  // Beneficiary Ration Card Details section
  // Visible only when the beneficiary is a dependent (RelName != "Self")
  // ---------------------------------------------------------------------------

  Widget _rationCardSection() {
    final relName = widget.beneficiaryWorkerOutput?.relName ?? '';
    if (relName.toLowerCase() == 'self') return const SizedBox.shrink();

    final rcNo = widget.beneficiaryWorkerOutput?.rationCardNo1 ?? '';
    final rcPath = widget.beneficiaryWorkerOutput?.rcImagePath1;
    final rcImageUrl = _buildRationCardImageUrl(rcPath);

    return Padding(
      padding: EdgeInsets.fromLTRB(0, 10.h, 0, 0),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          border: Border.all(width: 1, color: droDownBGColor),
          borderRadius: const BorderRadius.all(Radius.circular(8)),
        ),
        child: Column(
          children: [
            // Header
            Container(
              padding: EdgeInsets.fromLTRB(12.w, 10.h, 12.w, 10.h),
              decoration: BoxDecoration(
                color: kPrimaryColor,
                borderRadius: const BorderRadius.only(
                  topLeft: Radius.circular(8),
                  topRight: Radius.circular(8),
                ),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(
                      'Beneficiary Ration Card Details',
                      style: TextStyle(
                        color: kWhiteColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w400,
                        fontSize: 12.sp,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            // Ration Card Number field
            AppTextField(
              controller: TextEditingController(text: rcNo),
              readOnly: true,
              onChange: (value) {},
              hint: 'Ration Card Number',
              label: CommonText(
                text: 'Ration Card Number',
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
            ).paddingOnly(top: 8.h, left: 0, right: 0),
            // Ration Card Image 1
            if (rcPath != null && rcPath.isNotEmpty)
              Padding(
                padding: EdgeInsets.fromLTRB(0, 8.h, 0, 8.h),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Ration Card Image',
                      style: TextStyle(
                        color: kBlackColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: 12.sp,
                      ),
                    ),
                    SizedBox(height: 6.h),
                    Row(
                      children: [
                        Container(
                          width: 90.w,
                          height: 90.h,
                          padding: EdgeInsets.symmetric(
                            vertical: 4.h,
                            horizontal: 4.w,
                          ),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(10),
                            border: Border.all(
                              width: 1,
                              color: imageBorderColor,
                            ),
                          ),
                          child: Image.network(
                            rcImageUrl,
                            fit: BoxFit.cover,
                            errorBuilder: (context, error, stackTrace) {
                              return Image.asset(icPhotoPlaceholder);
                            },
                          ),
                        ),
                        SizedBox(width: 8.w),
                        GestureDetector(
                          onTap: () {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (context) =>
                                    FullScreenImageScreen(imagePath: rcImageUrl),
                              ),
                            );
                          },
                          child: SizedBox(
                            width: 26.w,
                            height: 26.h,
                            child: Image.asset(icViewIcon),
                          ),
                        ),
                      ],
                    ),
                  ],
                ).paddingSymmetric(horizontal: 8.w),
              ),
            SizedBox(height: 4.h),
          ],
        ),
      ),
    );
  }

  String _buildRationCardImageUrl(String? rcPath) {
    if (rcPath == null || rcPath.isEmpty) return '';
    // If already a full URL, use as-is
    if (rcPath.startsWith('http')) return rcPath;

    // Derive base from regdImagePath which already has the correct path prefix
    // e.g. https://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS/CampDocs/PatientImage/...
    // → base = https://testmcwwb.myhindlab.com/BETA_MYHINDLABDOCS
    final regdPath = widget.beneficiaryWorkerOutput?.regdImagePath ?? '';
    if (regdPath.contains('/CampDocs/')) {
      final base = regdPath.substring(0, regdPath.indexOf('/CampDocs/'));
      return '$base/CampDocs/RationCard/$rcPath';
    }

    // Fallback: use domain only (live server doesn't have BETA prefix)
    final origin = Uri.parse(APIManager.kWebservicesBaseURL).origin;
    return '$origin/CampDocs/RationCard/$rcPath';
  }
}
