// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/patient_registration/controller/abha_success_controller.dart';

class AbhaSuccessScreen extends StatefulWidget {
  final String abhaAddress;
  final String accessToken;
  final String authToken;
  final Map<String, dynamic> healthCard;

  const AbhaSuccessScreen({
    super.key,
    required this.abhaAddress,
    required this.accessToken,
    required this.authToken,
    required this.healthCard,
  });

  @override
  State<AbhaSuccessScreen> createState() => _AbhaSuccessScreenState();
}

class _AbhaSuccessScreenState extends State<AbhaSuccessScreen> {
  late final AbhaSuccessController ctrl;

  @override
  void initState() {
    super.initState();
    ctrl = Get.put(AbhaSuccessController()
      ..abhaAddress = widget.abhaAddress
      ..accessToken = widget.accessToken
      ..authToken = widget.authToken
      ..healthCard = widget.healthCard);
  }

  @override
  void dispose() {
    Get.delete<AbhaSuccessController>(force: true);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.grey.shade100,
      appBar: mAppBar(
        scTitle: 'ABHA Registration',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => ctrl.onGoToRegistration(),
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.all(16.w),
        child: Column(
          children: [
            SizedBox(height: 16.h),
            _buildSuccessBanner(),
            SizedBox(height: 16.h),
            _buildCardPreview(),
            SizedBox(height: 16.h),
            _buildDetailsCard(),
            SizedBox(height: 24.h),
            _buildButtons(),
            SizedBox(height: 24.h),
          ],
        ),
      ),
    );
  }

  // ── Success banner ────────────────────────────────────────────────

  Widget _buildSuccessBanner() {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(vertical: 20.h, horizontal: 20.w),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12.r),
        border: Border.all(color: Colors.green.shade200),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.04),
            blurRadius: 8,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        children: [
          Container(
            width: 48.w,
            height: 48.w,
            decoration: BoxDecoration(
              color: Colors.green.shade50,
              shape: BoxShape.circle,
            ),
            child: Icon(
              Icons.check_circle_rounded,
              color: Colors.green.shade600,
              size: 30.sp,
            ),
          ),
          SizedBox(width: 14.w),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                CommonText(
                  text: 'ABHA Address Created Successfully',
                  fontSize: 14.sp,
                  fontWeight: FontWeight.w700,
                  textColor: Colors.green.shade700,
                  textAlign: TextAlign.start,
                ),
                SizedBox(height: 4.h),
                CommonText(
                  text: widget.abhaAddress,
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                  textColor: kPrimaryColor,
                  textAlign: TextAlign.start,
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  // ── ABHA Card preview ─────────────────────────────────────────────

  Widget _buildCardPreview() {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10.r),
        border: Border.all(color: Colors.grey.shade200),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.04),
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
              borderRadius:
                  BorderRadius.vertical(top: Radius.circular(10.r)),
            ),
            child: Row(
              children: [
                Icon(Icons.credit_card_rounded,
                    color: Colors.white, size: 16.sp),
                SizedBox(width: 6.w),
                CommonText(
                  text: 'Ayushman Bharat Health Account Card',
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                  textColor: Colors.white,
                  textAlign: TextAlign.start,
                ),
              ],
            ),
          ),
          // Card image
          Padding(
            padding: EdgeInsets.all(14.w),
            child: Obx(() {
              if (ctrl.cardLoading.value) {
                return SizedBox(
                  height: 180.h,
                  child: Center(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        CircularProgressIndicator(
                          color: kPrimaryColor,
                          strokeWidth: 2.5,
                        ),
                        SizedBox(height: 12.h),
                        CommonText(
                          text: 'Loading ABHA card...',
                          fontSize: 12.sp,
                          fontWeight: FontWeight.w400,
                          textColor: Colors.grey.shade500,
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  ),
                );
              }

              if (ctrl.cardError.value || ctrl.cardBytes.value == null) {
                return SizedBox(
                  height: 120.h,
                  child: Center(
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Icon(Icons.image_not_supported_outlined,
                            color: Colors.grey.shade400, size: 32.sp),
                        SizedBox(height: 8.h),
                        CommonText(
                          text: 'Unable to load card preview',
                          fontSize: 12.sp,
                          fontWeight: FontWeight.w400,
                          textColor: Colors.grey.shade500,
                          textAlign: TextAlign.center,
                        ),
                        SizedBox(height: 8.h),
                        TextButton(
                          onPressed: ctrl.retryFetchCard,
                          child: CommonText(
                            text: 'Retry',
                            fontSize: 12.sp,
                            fontWeight: FontWeight.w600,
                            textColor: kPrimaryColor,
                            textAlign: TextAlign.center,
                          ),
                        ),
                      ],
                    ),
                  ),
                );
              }

              return ClipRRect(
                borderRadius: BorderRadius.circular(8.r),
                child: Image.memory(
                  ctrl.cardBytes.value!,
                  width: double.infinity,
                  fit: BoxFit.fitWidth,
                ),
              );
            }),
          ),
        ],
      ),
    );
  }

  // ── Details card ──────────────────────────────────────────────────

  Widget _buildDetailsCard() {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10.r),
        border: Border.all(color: Colors.grey.shade200),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.04),
            blurRadius: 6,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
            decoration: BoxDecoration(
              color: kPrimaryColor,
              borderRadius:
                  BorderRadius.vertical(top: Radius.circular(10.r)),
            ),
            child: Row(
              children: [
                Icon(Icons.verified_user_rounded,
                    color: Colors.white, size: 16.sp),
                SizedBox(width: 6.w),
                CommonText(
                  text: 'ABHA Details',
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                  textColor: Colors.white,
                  textAlign: TextAlign.start,
                ),
              ],
            ),
          ),
          Padding(
            padding: EdgeInsets.fromLTRB(14.w, 12.h, 14.w, 14.h),
            child: Column(
              children: [
                _detailRow('Name', ctrl.profileName),
                _divider(),
                _detailRow('ABHA Number', ctrl.profileAbhaNumber),
                _divider(),
                _detailRow('ABHA Address', widget.abhaAddress),
              ],
            ),
          ),
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
            width: 96.w,
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

  Widget _divider() =>
      Divider(height: 1, thickness: 0.6, color: Colors.grey.shade200);

  // ── Buttons ───────────────────────────────────────────────────────

  Widget _buildButtons() {
    return Column(
      children: [
        Obx(() => SizedBox(
              width: double.infinity,
              height: 48.h,
              child: ElevatedButton.icon(
                onPressed: (ctrl.downloading.value ||
                        ctrl.cardLoading.value ||
                        ctrl.cardBytes.value == null)
                    ? null
                    : ctrl.onDownload,
                style: ElevatedButton.styleFrom(
                  backgroundColor: kPrimaryColor,
                  disabledBackgroundColor: kPrimaryColor.withOpacity(0.5),
                  elevation: 0,
                  shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(8.r)),
                ),
                icon: ctrl.downloading.value
                    ? SizedBox(
                        width: 18.w,
                        height: 18.w,
                        child: const CircularProgressIndicator(
                          color: Colors.white,
                          strokeWidth: 2,
                        ),
                      )
                    : Icon(Icons.download_rounded,
                        color: Colors.white, size: 20.sp),
                label: CommonText(
                  text: ctrl.downloading.value
                      ? 'Saving...'
                      : 'Download',
                  fontSize: 14.sp,
                  fontWeight: FontWeight.w600,
                  textColor: Colors.white,
                  textAlign: TextAlign.center,
                ),
              ),
            )),
        SizedBox(height: 12.h),
        SizedBox(
          width: double.infinity,
          height: 48.h,
          child: OutlinedButton.icon(
            onPressed: ctrl.onGoToRegistration,
            style: OutlinedButton.styleFrom(
              foregroundColor: kPrimaryColor,
              side: BorderSide(color: kPrimaryColor, width: 1.5),
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8.r)),
            ),
            icon: Icon(Icons.app_registration_rounded,
                color: kPrimaryColor, size: 20.sp),
            label: CommonText(
              text: 'Go-To Registration',
              fontSize: 14.sp,
              fontWeight: FontWeight.w600,
              textColor: kPrimaryColor,
              textAlign: TextAlign.center,
            ),
          ),
        ),
      ],
    );
  }
}