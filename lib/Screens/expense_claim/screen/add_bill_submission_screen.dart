// ignore_for_file: must_be_immutable, avoid_print, file_names, use_build_context_synchronously

import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/utilities/choose_document_manager.dart';
import 'package:s2toperational/Modules/utilities/enums.dart';
import 'package:s2toperational/Modules/utilities/formatter_manager.dart';
import 'package:s2toperational/Screens/expense_claim/model/expense_head_response.dart';
import 'package:s2toperational/Screens/expense_claim/model/sub_expense_heads_response.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/common_widgets/AppTextField.dart';
import 'package:s2toperational/Modules/common_widgets/CommonText.dart';
import 'package:s2toperational/Modules/common_widgets/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Modules/common_widgets/S2TAppBar.dart';
import '../controller/add_bill_submission_controller.dart';

class AddBillSubmissionScreen extends StatelessWidget {
  const AddBillSubmissionScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final controller = Get.put(AddBillSubmissionController());
    SizeConfig().init(context);
    return GetBuilder<AddBillSubmissionController>(
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
            child: Padding(
              padding: const EdgeInsets.fromLTRB(10, 8, 10, 8),
              child: SingleChildScrollView(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    AppTextField(
                      readOnly: true,
                      controller: TextEditingController(
                        text: ctrl.selectedExpenseHead?.expenseHeadName ?? '',
                      ),
                      onTap: () => _showExpenseHeadSheet(context, ctrl),
                      hint: 'Expense Head',
                      label: CommonText(
                        text: 'Expense Head',
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
                      prefixIcon: _prefixIcon(icReceiptIcon),
                      suffixIcon: const Icon(Icons.keyboard_arrow_down),
                    ),
                    const SizedBox(height: 8),
                    AppTextField(
                      readOnly: true,
                      controller: TextEditingController(
                        text: ctrl.selectedSubExpenseHead?.subexpenseName ?? '',
                      ),
                      onTap: () => _showSubExpenseHeadSheet(context, ctrl),
                      hint: 'Sub Expense Head',
                      label: CommonText(
                        text: 'Sub Expense Head',
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
                      prefixIcon: _prefixIcon(icReceiptIcon),
                      suffixIcon: const Icon(Icons.keyboard_arrow_down),
                    ),
                    Visibility(
                      visible: ctrl.isShowOrganizedBy,
                      child: const SizedBox(height: 8),
                    ),
                    Visibility(
                      visible: ctrl.isShowOrganizedBy,
                      child: AppTextField(
                        readOnly: true,
                        controller: TextEditingController(text: ctrl.organizedByName),
                        hint: 'Organized By',
                        label: CommonText(
                          text: 'Organized By',
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
                        prefixIcon: _prefixIcon(icUserIcon),
                      ),
                    ),
                    const SizedBox(height: 8),
                    Row(
                      children: [
                        SizedBox(width: 20, height: 20, child: Image.asset(icUserIcon)),
                        const SizedBox(width: 6),
                        Text(
                          'Registered Workers : ',
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kBlackColor,
                            fontSize: responsiveFont(14),
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                        Text(
                          ctrl.registeredWorkers,
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kBlackColor,
                            fontSize: responsiveFont(14),
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    Row(
                      children: [
                        SizedBox(width: 20, height: 20, child: Image.asset(icCurrencyRupeeIcon)),
                        const SizedBox(width: 6),
                        Text(
                          'Advance Approved Amount : ',
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kBlackColor,
                            fontSize: responsiveFont(14),
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                        Text(
                          ctrl.advanceApprovedAmount,
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kBlackColor,
                            fontSize: responsiveFont(14),
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Note: Approved Bill Amount will be deducted from your Bill Amount',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        color: noteRedColor,
                        fontSize: responsiveFont(14),
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    const SizedBox(height: 8),
                    Row(
                      children: [
                        Expanded(
                          child: AppTextField(
                            readOnly: false,
                            controller: ctrl.noOfUnitController,
                            textInputType: TextInputType.number,
                            hint: 'No. of Unit*',
                            label: CommonText(
                              text: 'No. of Unit*',
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
                            prefixIcon: _prefixIcon(icHashIcon),
                          ),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: AppTextField(
                            readOnly: false,
                            textInputType: TextInputType.number,
                            controller: ctrl.amountUnitController,
                            onChange: (value) => _calculateTotal(context, ctrl, value),
                            hint: 'Amount/Unit*',
                            label: CommonText(
                              text: 'Amount/Unit*',
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
                            prefixIcon: _prefixIcon(icHashIcon),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    AppTextField(
                      readOnly: true,
                      controller: ctrl.totalController,
                      hint: 'Total*',
                      label: CommonText(
                        text: 'Total*',
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
                      prefixIcon: _prefixIcon(icCurrencyRupeeIcon),
                    ),
                    const SizedBox(height: 8),
                    AppTextField(
                      maxLines: 2,
                      readOnly: false,
                      controller: ctrl.remarkController,
                      hint: 'Remark',
                      label: CommonText(
                        text: 'Remark',
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
                      prefixIcon: _prefixIcon(icRemarkIconn),
                    ),
                    if (ctrl.showPhotoUpload) ...[
                      const SizedBox(height: 8),
                      Container(
                        width: MediaQuery.of(context).size.width,
                        height: 152,
                        decoration: BoxDecoration(
                          color: Colors.white,
                          border: Border.all(color: borderColor, width: 1),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: [
                            GestureDetector(
                              onTap: () {
                                if (ctrl.selectedFile == null) {
                                  _chooseDocumentTypeAlert(context, ctrl);
                                } else {
                                  ToastManager.showAlertDialog(
                                    context,
                                    'You already selected file',
                                    () => Get.back(),
                                  );
                                }
                              },
                              child: Container(
                                width: 50,
                                height: 50,
                                padding: const EdgeInsets.all(10),
                                decoration: BoxDecoration(
                                  color: Colors.white,
                                  border: Border.all(color: borderColor, width: 1),
                                  borderRadius: BorderRadius.circular(100),
                                ),
                                child: Image.asset(icCameraIcon),
                              ),
                            ),
                            const SizedBox(height: 8),
                            Text(
                              'Click on Camera to Upload the Bill',
                              style: TextStyle(
                                color: uploadBillTitleColor,
                                fontFamily: FontConstants.interFonts,
                                fontWeight: FontWeight.w400,
                                fontSize: responsiveFont(14),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                    if (ctrl.selectedFile != null) ...[
                      const SizedBox(height: 8),
                      Container(
                        decoration: BoxDecoration(
                          color: attachmentBGColor,
                          border: Border.all(color: attachmentBorderColor, width: 1),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        height: 60,
                        padding: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                        child: Row(
                          children: [
                            SizedBox(
                              width: 30,
                              height: 30,
                              child: Image.asset(icPNGIcon),
                            ),
                            const SizedBox(width: 10),
                            Expanded(
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    FormatterManager.getFileNameInfo(ctrl.selectedFile!),
                                    style: TextStyle(
                                      color: uploadBillTitleColor,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.normal,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                  const SizedBox(height: 4),
                                  Text(
                                    'Size: ${FormatterManager.getFormattedFileSize(ctrl.selectedFile!)}',
                                    style: TextStyle(
                                      color: dropDownTitleHeader,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.normal,
                                      fontSize: responsiveFont(10),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                            GestureDetector(
                              onTap: () {
                                ctrl.selectedFile = null;
                                ctrl.update();
                              },
                              child: Container(
                                padding: const EdgeInsets.all(6),
                                width: 30,
                                height: 30,
                                child: Image.asset(icTrashIcon),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                    const SizedBox(height: 12),
                    AppActiveButton(
                      buttontitle: 'Submit',
                      onTap: () async {
                        if (!ctrl.validations(context)) return;
                        final success = await ctrl.submitBill();
                        if (success) {
                          ToastManager.showSuccessPopup(
                            context,
                            icSuccessIcon,
                            'Bill Submitted Successfully.',
                            () {
                              Get.back();
                              Get.back();
                            },
                          );
                        }
                      },
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }

  void _calculateTotal(
    BuildContext context,
    AddBillSubmissionController ctrl,
    String amount,
  ) {
    final noOfUnit = ctrl.noOfUnitController.text.trim();

    if (ctrl.selectedSubExpenseHead == null) {
      ToastManager.toast('Please select Sub Expense Head');
      return;
    }
    if (noOfUnit.isEmpty || amount.isEmpty) {
      ctrl.totalController.text = '';
      return;
    }

    final count = int.tryParse(noOfUnit);
    final amountInt = int.tryParse(amount);
    if (count == null || amountInt == null) {
      ctrl.totalController.text = '';
      return;
    }
    if (count == 0) {
      ToastManager.toast('Please enter No Of Unit');
      return;
    }

    final maxAllowedAmt = ctrl.selectedSubExpenseHead?.maxAllowedAmt ?? 0.0;
    final total = amountInt * count;

    if (total > maxAllowedAmt) {
      ctrl.showPhotoUpload = true;
      ToastManager.showAlertDialog(
        context,
        'Only one file is allowed to upload as a proof of permission',
        () => Get.back(),
      );
    } else {
      ctrl.showPhotoUpload = false;
    }

    ctrl.totalController.text = '$total';
    ctrl.update();
  }

  void _chooseDocumentTypeAlert(BuildContext context, AddBillSubmissionController ctrl) {
    showDialog(
      context: context,
      builder: (BuildContext ctx) {
        return AlertDialog(
          title: const Text('Select Photo'),
          content: const Text(''),
          actions: [
            TextButton(
              child: const Text('Take a Photo'),
              onPressed: () {
                Navigator.pop(ctx);
                _handleFilePick(ctrl, FileSourceType.camera);
              },
            ),
            TextButton(
              child: const Text('Choose from Photo Library'),
              onPressed: () {
                Navigator.pop(ctx);
                _handleFilePick(ctrl, FileSourceType.gallery);
              },
            ),
            TextButton(
              child: const Text('PDF'),
              onPressed: () {
                Navigator.pop(ctx);
                _handleFilePick(ctrl, FileSourceType.pdf);
              },
            ),
            TextButton(
              child: const Text('Cancel'),
              onPressed: () => Navigator.pop(ctx),
            ),
          ],
        );
      },
    );
  }

  Future<void> _handleFilePick(
    AddBillSubmissionController ctrl,
    FileSourceType type,
  ) async {
    final result = await ChooseDocumentManager.pickFile(type);
    if (result != null) {
      ctrl.setFile(result.file, result.fileType);
    }
  }

  Future<void> _showExpenseHeadSheet(
    BuildContext context,
    AddBillSubmissionController ctrl,
  ) async {
    final list = await ctrl.fetchExpenseHeads();
    if (list.isEmpty) return;
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext ctx) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(ctx).size.width * 1.33,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: DropDownListScreen(
            titleString: 'Expense Head',
            dropDownList: list,
            dropDownMenu: DropDownTypeMenu.ExpenseHead,
            onApplyTap: (p0) {
              ctrl.setExpenseHead(p0 as ExpenseHeaOutput);
            },
          ),
        );
      },
    );
  }

  Future<void> _showSubExpenseHeadSheet(
    BuildContext context,
    AddBillSubmissionController ctrl,
  ) async {
    final list = await ctrl.fetchSubExpenseHeads();
    if (list.isEmpty) return;
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext ctx) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(ctx).size.width * 1.33,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: DropDownListScreen(
            titleString: 'Sub Expense Head',
            dropDownList: list,
            dropDownMenu: DropDownTypeMenu.SubExpenseHead,
            onApplyTap: (p0) {
              ctrl.setSubExpenseHead(p0 as SubExpenseHeadsOutput);
            },
          ),
        );
      },
    );
  }

  Widget _prefixIcon(String asset) {
    return SizedBox(
      height: 20.h,
      width: 20.w,
      child: Center(
        child: Image.asset(asset, height: 24.h, width: 24.w, fit: BoxFit.contain),
      ),
    );
  }
}
