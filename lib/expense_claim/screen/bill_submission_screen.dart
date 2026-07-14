// ignore_for_file: file_names, avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import '../controller/bill_submission_controller.dart';
import '../model/advadetails_new_version_v2_response.dart';
import 'add_bill_submission_screen.dart';
import 'bill_submission_row.dart';
import 'request_bill_details_screen.dart';

class BillSubmissionScreen extends StatelessWidget {
  const BillSubmissionScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final controller = Get.put(BillSubmissionController());
    SizeConfig().init(context);
    return GetBuilder<BillSubmissionController>(
      builder: (ctrl) => KeyboardDismissOnTap(
        child: Scaffold(
          appBar: mAppBar(
            scTitle: 'Bill Submission',
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Get.back(),
          ),
          body: AnnotatedRegion(
            value: const SystemUiOverlayStyle(
              statusBarColor: kPrimaryColor,
              statusBarBrightness: Brightness.dark,
              statusBarIconBrightness: Brightness.light,
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Container(
                  width: MediaQuery.of(context).size.width,
                  decoration: const BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.all(Radius.circular(10)),
                  ),
                  padding: const EdgeInsets.all(10),
                  child: Column(
                    children: [
                      AppTextField(
                        controller: TextEditingController(
                          text: ctrl.userLoginDetails?.district,
                        ),
                        readOnly: true,
                        hint: 'District',
                        label: CommonText(
                          text: 'District',
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
                              icMapPin,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ),
                      const SizedBox(height: 8),
                      Row(
                        children: [
                          Expanded(
                            child: AppTextField(
                              controller: TextEditingController(text: ctrl.fromDate),
                              readOnly: true,
                              onTap: () => _selectFromDate(context, ctrl),
                              hint: 'From Date',
                              label: CommonText(
                                text: 'From Date',
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
                          const SizedBox(width: 8),
                          Expanded(
                            child: AppTextField(
                              controller: TextEditingController(text: ctrl.toDate),
                              readOnly: true,
                              onTap: () => _selectToDate(context, ctrl),
                              hint: 'To Date',
                              label: CommonText(
                                text: 'To Date',
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
                      ),
                    ],
                  ),
                ),
                Expanded(
                  child: Container(
                    color: Colors.transparent,
                    child: _buildList(context, ctrl),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildList(BuildContext context, BillSubmissionController ctrl) {
    if (ctrl.campExpensesList.isEmpty) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SizedBox(width: 120, height: 120, child: Image.asset(icMoneyIcon)),
            const SizedBox(height: 23),
            Text(
              'No Advance Requested.',
              style: TextStyle(
                color: Colors.black,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                fontSize: responsiveFont(16),
              ),
            ),
          ],
        ),
      );
    }

    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 10, 0, 0),
      child: ListView.builder(
        itemCount: ctrl.campExpensesList.length,
        itemBuilder: (context, index) {
          final obj = ctrl.campExpensesList[index];
          return BillSubmissionRow(
            object: obj,
            onEyesIconTap: (object) => _onEyesTap(context, ctrl, object),
            onViewDetailsTap: (object) {
              Get.to(
                () => const RequestBillDetailsScreen(),
                arguments: obj.campid ?? 0,
              );
            },
          );
        },
      ),
    );
  }

  void _onEyesTap(
    BuildContext context,
    BillSubmissionController ctrl,
    AdvadetailsNewOutput obj,
  ) {
    final dESGID = ctrl.userLoginDetails?.dESGID ?? 0;
    final empCode = ctrl.userLoginDetails?.empCode ?? 0;

    if (dESGID != 102) {
      if (obj.advRaisedbyUserid != null && obj.advRaisedbyUserid != empCode) {
        ToastManager.showAlertDialog(
          context,
          'This advance not requested by you.',
          () => Get.back(),
        );
        return;
      }
    }
    if (obj.actualExpenseStatus != null &&
        obj.actualExpenseStatus?.toLowerCase() == 'approved') {
      ToastManager.showAlertDialog(
        context,
        'This bill already approved',
        () => Get.back(),
      );
    } else {
      Get.to(() => const AddBillSubmissionScreen(), arguments: obj);
    }
  }

  Future<void> _selectFromDate(
    BuildContext context,
    BillSubmissionController ctrl,
  ) async {
    final now = DateTime.now();
    final picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: DateTime(now.year - 100, now.month, 1),
      lastDate: DateTime(now.year, now.month, now.day),
    );
    if (picked != null) ctrl.setFromDate(picked);
  }

  Future<void> _selectToDate(
    BuildContext context,
    BillSubmissionController ctrl,
  ) async {
    if (ctrl.selectedFromDate == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select From Date first')),
      );
      return;
    }
    final maxDate = DateTime(
      ctrl.selectedFromDate!.year + 100,
      ctrl.selectedFromDate!.month,
      ctrl.selectedFromDate!.day,
    );
    final picked = await showDatePicker(
      context: context,
      initialDate: ctrl.selectedFromDate!,
      firstDate: ctrl.selectedFromDate!,
      lastDate: maxDate,
    );
    if (picked != null) await ctrl.setToDate(picked);
  }
}
