// ignore_for_file: file_names, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/patient_registration/controller/abha_address_creation_controller.dart';

class AbhaAddressCreationScreen extends StatefulWidget {
  final String accessToken;
  final String txnId;
  final String authToken;
  final Map<String, dynamic> healthCard;
  final bool isNew;
  final String existingABHAAddress;
  final String mobile;
  final String campId;
  final String siteId;
  final String distLgdCode;
  final String district;
  final String campType;

  const AbhaAddressCreationScreen({
    super.key,
    required this.accessToken,
    required this.txnId,
    required this.authToken,
    required this.healthCard,
    required this.isNew,
    this.existingABHAAddress = '',
    this.mobile = '',
    required this.campId,
    required this.siteId,
    required this.distLgdCode,
    required this.district,
    required this.campType,
  });

  @override
  State<AbhaAddressCreationScreen> createState() =>
      _AbhaAddressCreationScreenState();
}

class _AbhaAddressCreationScreenState extends State<AbhaAddressCreationScreen> {
  late final AbhaAddressCreationController ctrl;

  @override
  void initState() {
    super.initState();
    ctrl = Get.put(
      AbhaAddressCreationController()
        ..accessToken = widget.accessToken
        ..txnId = widget.txnId
        ..authToken = widget.authToken
        ..healthCard = widget.healthCard
        ..isNew = widget.isNew
        ..existingABHAAddress = widget.existingABHAAddress
        ..mobile = widget.mobile
        ..campId = widget.campId
        ..siteId = widget.siteId
        ..distLgdCode = widget.distLgdCode
        ..district = widget.district
        ..campType = widget.campType,
    );
  }

  @override
  void dispose() {
    Get.delete<AbhaAddressCreationController>(force: true);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey.shade100,
      appBar: mAppBar(
        scTitle: 'ABHA Address Creation',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: Obx(() {
        if (ctrl.loading.value) return const SizedBox.shrink();
        return SingleChildScrollView(
          padding: EdgeInsets.all(14.w),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildProfileCard(),
              SizedBox(height: 14.h),
              _buildCreateCard(),
            ],
          ),
        );
      }),
    );
  }

  // ── ABHA Profile card ─────────────────────────────────────────────

  Widget _buildProfileCard() {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.grey.shade200),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.05),
            blurRadius: 6,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Header
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
            decoration: BoxDecoration(
              color: kPrimaryColor,
              borderRadius: BorderRadius.vertical(top: Radius.circular(10.r)),
            ),
            child: Row(
              children: [
                Icon(
                  Icons.verified_user_rounded,
                  color: Colors.white,
                  size: 16.sp,
                ),
                SizedBox(width: 6.w),
                CommonText(
                  text: 'Existing Details',
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                  textColor: Colors.white,
                  textAlign: TextAlign.start,
                ),
              ],
            ),
          ),

          // Detail rows
          Padding(
            padding: EdgeInsets.fromLTRB(14.w, 14.h, 14.w, 0),
            child: Column(
              children: [
                _detailRow('Name', ctrl.profileName),
                _rowDivider(),
                _detailRow(
                  'Address',
                  ctrl.resolvedExistingAddress.isNotEmpty
                      ? ctrl.resolvedExistingAddress
                      : '—',
                ),
                _rowDivider(),
                _detailRow('Number', ctrl.profileAbhaNumber),
              ],
            ),
          ),

          // Continue button
          if (ctrl.resolvedExistingAddress.isNotEmpty)
            Padding(
              padding: EdgeInsets.fromLTRB(14.w, 14.h, 14.w, 6.h),
              child: SizedBox(
                width: double.infinity,
                height: 44.h,
                child: OutlinedButton(
                  onPressed: ctrl.onContinueWithExisting,
                  style: OutlinedButton.styleFrom(
                    foregroundColor: kPrimaryColor,
                    side: BorderSide(color: kPrimaryColor, width: 1.5),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(8),
                    ),
                  ),
                  child: CommonText(
                    text: 'Continue With Existing Address',
                    fontSize: 13.sp,
                    fontWeight: FontWeight.w600,
                    textColor: kPrimaryColor,
                    textAlign: TextAlign.center,
                  ),
                ),
              ),
            ),

          SizedBox(height: 14.h),
        ],
      ),
    );
  }

  Widget _detailRow(String label, String value) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 9.h),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 70.w,
            child: CommonText(
              text: label,
              fontSize: 12.sp,
              fontWeight: FontWeight.w500,
              textColor: Colors.grey.shade600,
              textAlign: TextAlign.start,
            ),
          ),
          CommonText(
            text: ':  ',
            fontSize: 12.sp,
            fontWeight: FontWeight.w400,
            textColor: Colors.grey.shade500,
            textAlign: TextAlign.start,
          ),
          Expanded(
            child: CommonText(
              text: value,
              fontSize: 13.sp,
              fontWeight: FontWeight.w600,
              textColor: Colors.black87,
              textAlign: TextAlign.start,
            ),
          ),
        ],
      ),
    );
  }

  Widget _rowDivider() =>
      Divider(height: 1, thickness: 0.6, color: Colors.grey.shade200);

  Widget _infoRow(String label, String value) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          width: 6.w,
          height: 6.w,
          margin: EdgeInsets.only(top: 5.h, right: 8.w),
          decoration: BoxDecoration(
            color: kPrimaryColor,
            shape: BoxShape.circle,
          ),
        ),
        Expanded(
          child: RichText(
            text: TextSpan(
              children: [
                TextSpan(
                  text: '$label: ',
                  style: TextStyle(
                    fontWeight: FontWeight.w600,
                    fontSize: 13.sp,
                    color: Colors.black87,
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
                TextSpan(
                  text: value,
                  style: TextStyle(
                    fontWeight: FontWeight.w400,
                    fontSize: 13.sp,
                    color: Colors.black54,
                    fontFamily: FontConstants.interFonts,
                  ),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }

  // ── Create new ABHA address card ──────────────────────────────────

  Widget _buildCreateCard() {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.grey.shade200),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.05),
            blurRadius: 6,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Header
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
            decoration: BoxDecoration(
              color: kPrimaryColor,
              borderRadius: BorderRadius.vertical(top: Radius.circular(10.r)),
            ),
            child: Row(
              children: [
                Icon(
                  Icons.add_circle_outline_rounded,
                  color: Colors.white,
                  size: 16.sp,
                ),
                SizedBox(width: 6.w),
                CommonText(
                  text: 'Create New ABHA Address',
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                  textColor: Colors.white,
                  textAlign: TextAlign.start,
                ),
              ],
            ),
          ),

          Padding(
            padding: EdgeInsets.all(14.w),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                CommonText(
                  text:
                      '4–14 characters · start with a letter · letters, numbers, dot (.) or underscore (_) only',
                  fontSize: 11.sp,
                  fontWeight: FontWeight.w400,
                  textColor: Colors.grey.shade600,
                  textAlign: TextAlign.start,
                ),
                SizedBox(height: 12.h),

                // Input + @abdm suffix
                Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Expanded(
                      child: AppTextField(
                        controller: ctrl.addressCtrl,
                        label: const Text('ABHA Address'),
                        hint: 'e.g. john.doe123',
                        textInputType: TextInputType.text,
                        maxLength: 14,
                        suffixIcon: GestureDetector(
                          onTap:
                              () => showDialog(
                                context: context,
                                builder:
                                    (_) => AlertDialog(
                                      title: Row(
                                        children: [
                                          Icon(
                                            Icons.info_outline_rounded,
                                            color: kPrimaryColor,
                                            size: 20.sp,
                                          ),
                                          SizedBox(width: 8.w),
                                          CommonText(
                                            text: 'ABHA Address Format',
                                            fontSize: 15.sp,
                                            fontWeight: FontWeight.w600,
                                            textColor: Colors.black87,
                                            textAlign: TextAlign.start,
                                          ),
                                        ],
                                      ),
                                      content: Column(
                                        mainAxisSize: MainAxisSize.min,
                                        crossAxisAlignment:
                                            CrossAxisAlignment.start,
                                        children: [
                                          _infoRow(
                                            'Length',
                                            '4 to 14 characters',
                                          ),
                                          SizedBox(height: 8.h),
                                          _infoRow(
                                            'Start with',
                                            'A letter (a–z or A–Z)',
                                          ),
                                          SizedBox(height: 8.h),
                                          _infoRow(
                                            'Allowed',
                                            'Letters, numbers, dot (.) and underscore (_)',
                                          ),
                                          SizedBox(height: 8.h),
                                          _infoRow(
                                            'End with',
                                            'A letter or number (not . or _)',
                                          ),
                                        ],
                                      ),
                                      actions: [
                                        TextButton(
                                          onPressed:
                                              () => Navigator.pop(context),
                                          child: CommonText(
                                            text: 'OK',
                                            fontSize: 13.sp,
                                            fontWeight: FontWeight.w600,
                                            textColor: kPrimaryColor,
                                            textAlign: TextAlign.center,
                                          ),
                                        ),
                                      ],
                                    ),
                              ),
                          child: Icon(
                            Icons.info_outline_rounded,
                            size: 18.sp,
                            color: Colors.grey.shade500,
                          ),
                        ),
                        inputFormatters: [
                          FilteringTextInputFormatter.allow(
                            RegExp(r'[a-zA-Z0-9._]'),
                          ),
                        ],
                        onChange: ctrl.onAddressChanged,
                      ),
                    ),
                    SizedBox(width: 8.w),
                    Padding(
                      padding: EdgeInsets.only(top: 14.h),
                      child: CommonText(
                        text: '@sbx',
                        fontSize: 14.sp,
                        fontWeight: FontWeight.w600,
                        textColor: Colors.grey.shade600,
                        textAlign: TextAlign.start,
                      ),
                    ),
                  ],
                ),

                // Green banner — format valid or suggestion selected
                Obx(
                  () =>
                      ctrl.formatValid.value
                          ? Padding(
                            padding: EdgeInsets.only(top: 8.h),
                            child: Row(
                              children: [
                                Icon(
                                  Icons.check_circle_rounded,
                                  color: Colors.green.shade600,
                                  size: 16.sp,
                                ),
                                SizedBox(width: 6.w),
                                CommonText(
                                  text: 'ABHA address is available',
                                  fontSize: 12.sp,
                                  fontWeight: FontWeight.w500,
                                  textColor: Colors.green.shade700,
                                  textAlign: TextAlign.start,
                                ),
                              ],
                            ),
                          )
                          : const SizedBox.shrink(),
                ),

                // Error / validation message (red)
                Obx(
                  () =>
                      ctrl.errorMessage.value.isNotEmpty
                          ? Padding(
                            padding: EdgeInsets.only(top: 6.h),
                            child: CommonText(
                              text: ctrl.errorMessage.value,
                              fontSize: 11.sp,
                              fontWeight: FontWeight.w400,
                              textColor: Colors.red.shade600,
                              textAlign: TextAlign.start,
                            ),
                          )
                          : const SizedBox.shrink(),
                ),

                // Suggestions
                Obx(
                  () =>
                      ctrl.suggestions.isNotEmpty
                          ? Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              SizedBox(height: 14.h),
                              CommonText(
                                text: 'Suggestions',
                                fontSize: 12.sp,
                                fontWeight: FontWeight.w600,
                                textColor: Colors.grey.shade700,
                                textAlign: TextAlign.start,
                              ),
                              SizedBox(height: 8.h),
                              Wrap(
                                spacing: 8.w,
                                runSpacing: 6.h,
                                children:
                                    ctrl.suggestions.map((s) {
                                      return GestureDetector(
                                        onTap: () => ctrl.selectSuggestion(s),
                                        child: Container(
                                          padding: EdgeInsets.symmetric(
                                            horizontal: 12.w,
                                            vertical: 6.h,
                                          ),
                                          decoration: BoxDecoration(
                                            color: kPrimaryColor.withValues(
                                              alpha: 0.07,
                                            ),
                                            borderRadius: BorderRadius.circular(
                                              20,
                                            ),
                                            border: Border.all(
                                              color: kPrimaryColor.withValues(
                                                alpha: 0.3,
                                              ),
                                            ),
                                          ),
                                          child: CommonText(
                                            text: s,
                                            fontSize: 12.sp,
                                            fontWeight: FontWeight.w400,
                                            textColor: kPrimaryColor,
                                            textAlign: TextAlign.start,
                                          ),
                                        ),
                                      );
                                    }).toList(),
                              ),
                            ],
                          )
                          : const SizedBox.shrink(),
                ),

                SizedBox(height: 16.h),

                // Single button — checks availability AND creates in one tap
                SizedBox(
                  width: double.infinity,
                  height: 44.h,
                  child: OutlinedButton(
                    onPressed: ctrl.onCheckAvailability,
                    style: OutlinedButton.styleFrom(
                      foregroundColor: kPrimaryColor,
                      side: BorderSide(color: kPrimaryColor, width: 1.5),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8),
                      ),
                    ),
                    child: CommonText(
                      text: 'Check Availability',
                      fontSize: 13.sp,
                      fontWeight: FontWeight.w600,
                      textColor: kPrimaryColor,
                      textAlign: TextAlign.center,
                    ),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
