// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/patient_registration/controller/ration_card_photo_controller.dart';

class RationCardPhotoScreen extends StatelessWidget {
  final String regdId;
  final String dependentBocId;
  final String rationCardNumber;
  final VoidCallback? onSuccess;

  const RationCardPhotoScreen({
    super.key,
    required this.regdId,
    required this.dependentBocId,
    required this.rationCardNumber,
    this.onSuccess,
  });

  @override
  Widget build(BuildContext context) {
    if (Get.isRegistered<RationCardPhotoController>()) {
      Get.delete<RationCardPhotoController>(force: true);
    }
    final c = Get.put(
      RationCardPhotoController(
        regdId: regdId,
        dependentBocId: dependentBocId,
        rationCardNumber: rationCardNumber,
        onSuccess: onSuccess,
      ),
    );

    return NetworkWrapper(
      child: Scaffold(
        backgroundColor: kBackground,
        appBar: mAppBar(scTitle: 'Ration Card Photos'),
        body: Obx(() {
          if (c.isUploading.value) {
            return const Center(child: CircularProgressIndicator());
          }
          return _Body(c: c);
        }),
      ),
    );
  }
}

class _Body extends StatelessWidget {
  final RationCardPhotoController c;

  const _Body({required this.c});

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      padding: EdgeInsets.all(16.w),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // ── Ration card number info ──────────────────────────────────────
          if (c.rationCardNumber.isNotEmpty) ...[
            _SectionCard(
              child: Row(
                children: [
                  CommonText(
                    text: 'Ration Card No : ',
                    fontSize: 13.sp,
                    fontWeight: FontWeight.w600,
                    textColor: kBlackColor,
                    textAlign: TextAlign.start,
                  ),
                  Expanded(
                    child: CommonText(
                      text: c.rationCardNumber,
                      fontSize: 13.sp,
                      fontWeight: FontWeight.normal,
                      textColor: kTextColor,
                      textAlign: TextAlign.start,
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(height: 16.h),
          ],

          // ── Ration card type selection ───────────────────────────────────
          _sectionLabel('Ration Card Type'),
          SizedBox(height: 8.h),
          Obx(
            () => Row(
              children: [
                _RadioOption(
                  label: 'Manual',
                  value: 'manual',
                  groupValue: c.selectedType.value,
                  onChanged: c.selectType,
                ),
                SizedBox(width: 24.w),
                _RadioOption(
                  label: 'Digital',
                  value: 'digital',
                  groupValue: c.selectedType.value,
                  onChanged: c.selectType,
                ),
              ],
            ),
          ),
          SizedBox(height: 20.h),

          // ── Photo capture area ───────────────────────────────────────────
          Obx(() {
            final maxPhotos = c.maxPhotos;
            return Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _sectionLabel(
                  'Ration Card Photo(s) (${c.photos.length}/$maxPhotos)',
                ),
                SizedBox(height: 8.h),
                if (c.photos.isNotEmpty)
                  GridView.builder(
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    itemCount: c.photos.length,
                    gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      crossAxisSpacing: 8.w,
                      mainAxisSpacing: 8.h,
                    ),
                    itemBuilder: (ctx, i) => _PhotoTile(
                      file: c.photos[i],
                      onRemove: () => c.removePhoto(i),
                    ),
                  ),
                if (c.photos.length < maxPhotos) ...[
                  SizedBox(height: c.photos.isNotEmpty ? 8.h : 0),
                  GestureDetector(
                    onTap: () => c.capturePhoto(context),
                    child: Container(
                      width: double.infinity,
                      height: 130.h,
                      decoration: BoxDecoration(
                        color: kWhiteColor,
                        borderRadius: BorderRadius.circular(12),
                        border: Border.all(
                          color: kPrimaryColor.withValues(alpha: 0.35),
                          style: BorderStyle.solid,
                        ),
                        boxShadow: [
                          BoxShadow(
                            color: Colors.black.withValues(alpha: 0.04),
                            blurRadius: 8,
                          ),
                        ],
                      ),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Icon(
                            Icons.add_a_photo_outlined,
                            size: 48,
                            color: kPrimaryColor.withValues(alpha: 0.55),
                          ),
                          SizedBox(height: 8.h),
                          CommonText(
                            text: 'Tap to capture photo',
                            fontSize: 13.sp,
                            fontWeight: FontWeight.w400,
                            textColor: kLabelTextColor,
                            textAlign: TextAlign.center,
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ],
            );
          }),
          SizedBox(height: 28.h),

          // ── Upload button ────────────────────────────────────────────────
          AppActiveButton(
            buttontitle: 'Upload',
            onTap: () => c.uploadPhotos(context),
          ),
          SizedBox(height: 16.h),
        ],
      ),
    );
  }

  Widget _sectionLabel(String text) => Text(
    text,
    style: TextStyle(
      fontSize: 14.sp,
      fontFamily: FontConstants.interFonts,
      fontWeight: FontWeight.w600,
      color: kLabelTextColor,
    ),
  );
}

// ── Radio option ──────────────────────────────────────────────────────────────

class _RadioOption extends StatelessWidget {
  final String label;
  final String value;
  final String groupValue;
  final ValueChanged<String> onChanged;

  const _RadioOption({
    required this.label,
    required this.value,
    required this.groupValue,
    required this.onChanged,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => onChanged(value),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Radio<String>(
            value: value,
            groupValue: groupValue,
            onChanged: (v) {
              if (v != null) onChanged(v);
            },
            visualDensity: const VisualDensity(
              horizontal: -4,
              vertical: -4,
            ),
            materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
          ),
          SizedBox(width: 4.w),
          Text(
            label,
            style: TextStyle(
              fontSize: 14.sp,
              fontFamily: FontConstants.interFonts,
              color: kBlackColor,
            ),
          ),
        ],
      ),
    );
  }
}

// ── Photo tile with remove button ─────────────────────────────────────────────

class _PhotoTile extends StatelessWidget {
  final dynamic file;
  final VoidCallback onRemove;

  const _PhotoTile({required this.file, required this.onRemove});

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        ClipRRect(
          borderRadius: BorderRadius.circular(10),
          child: Image.file(
            file,
            fit: BoxFit.cover,
            width: double.infinity,
            height: double.infinity,
          ),
        ),
        Positioned(
          top: 4,
          right: 4,
          child: GestureDetector(
            onTap: onRemove,
            child: Container(
              padding: const EdgeInsets.all(2),
              decoration: const BoxDecoration(
                color: Colors.black54,
                shape: BoxShape.circle,
              ),
              child: const Icon(Icons.close, color: Colors.white, size: 16),
            ),
          ),
        ),
      ],
    );
  }
}

// ── Shared card container ─────────────────────────────────────────────────────

class _SectionCard extends StatelessWidget {
  final Widget child;

  const _SectionCard({required this.child});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.all(12.w),
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(12),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 10,
          ),
        ],
      ),
      child: child,
    );
  }
}
