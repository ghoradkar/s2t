// ignore_for_file: file_names, avoid_print, use_build_context_synchronously

import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ChooseDocumentManager/ChooseDocumentManager.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Screens/expense_claim/model/expense_camp_id_list_v1_response.dart';
import 'package:s2toperational/Screens/expense_claim/model/expense_head_response.dart';
import 'package:s2toperational/Screens/expense_claim/model/sub_expense_heads_response.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Modules/widgets/MultiSelectionDropDownListScreen/MultiSelectionDropDownListScreen.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import '../controller/upload_bill_controller.dart';

class UploadBillScreen extends StatelessWidget {
  const UploadBillScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final controller = Get.put(UploadBillController());
    SizeConfig().init(context);
    return GetBuilder<UploadBillController>(
      builder: (ctrl) => KeyboardDismissOnTap(
        child: Scaffold(
          appBar: mAppBar(
            scTitle: 'Upload Bill',
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
              padding: const EdgeInsets.fromLTRB(12, 8, 12, 8),
              child: SingleChildScrollView(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    AppTextField(
                      controller: TextEditingController(
                        text: ctrl.selectedExpenseHead?.expenseHeadName ?? '',
                      ),
                      readOnly: true,
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
                    const SizedBox(height: 12),
                    AppTextField(
                      controller: TextEditingController(
                        text: ctrl.selectedSubExpenseHead?.subexpenseName ?? '',
                      ),
                      readOnly: true,
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
                            prefixIcon: _prefixIcon(icCalendarMonth),
                          ),
                        ),
                        const SizedBox(width: 12),
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
                            prefixIcon: _prefixIcon(icCalendarMonth),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 8),
                    AppTextField(
                      controller: TextEditingController(text: ctrl.getCampIDText()),
                      readOnly: true,
                      onTap: () => _showCampIDSheet(context, ctrl),
                      hint: 'Camp ID*',
                      label: CommonText(
                        text: 'Camp ID*',
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
                    Row(
                      children: [
                        Expanded(
                          child: AppTextField(
                            controller: ctrl.totalAmountController,
                            readOnly: true,
                            hint: 'Total Amount*',
                            label: CommonText(
                              text: 'Total Amount*',
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
                            textInputType: TextInputType.number,
                            fieldRadius: 10,
                            prefixIcon: _prefixIcon(icCurrencyRupeeIcon),
                          ),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: AppTextField(
                            controller: ctrl.amountOnBillController,
                            readOnly: false,
                            hint: 'Amount On Bill*',
                            label: CommonText(
                              text: 'Amount On Bill*',
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
                            textInputType: TextInputType.number,
                            fieldRadius: 10,
                            prefixIcon: _prefixIcon(icCurrencyRupeeIcon),
                          ),
                        ),
                      ],
                    ),
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
                            onTap: () => _chooseDocumentTypeAlert(context, ctrl),
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
                    const SizedBox(height: 8),
                    ListView.builder(
                      physics: const NeverScrollableScrollPhysics(),
                      shrinkWrap: true,
                      itemCount: ctrl.fileAttachmentList.length,
                      itemBuilder: (context, index) {
                        final fileObj = ctrl.fileAttachmentList[index];
                        return Padding(
                          padding: const EdgeInsets.fromLTRB(0, 0, 0, 12),
                          child: Container(
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
                                        FormatterManager.getFileNameInfo(fileObj),
                                        style: TextStyle(
                                          color: uploadBillTitleColor,
                                          fontFamily: FontConstants.interFonts,
                                          fontWeight: FontWeight.normal,
                                          fontSize: responsiveFont(14),
                                        ),
                                      ),
                                      const SizedBox(height: 4),
                                      Text(
                                        'Size: ${FormatterManager.getFormattedFileSize(fileObj)}',
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
                                  onTap: () => ctrl.removeFile(index),
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
                        );
                      },
                    ),
                    const SizedBox(height: 12),
                    AppActiveButton(
                      buttontitle: 'Submit',
                      onTap: () async {
                        if (!ctrl.validations(context)) return;
                        final success = await ctrl.submitData();
                        if (success) {
                          ToastManager.showSuccessPopup(
                            context,
                            icSuccessIcon,
                            'Bill Details Saved successfully.',
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

  void _chooseDocumentTypeAlert(BuildContext context, UploadBillController ctrl) {
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

  Future<void> _handleFilePick(UploadBillController ctrl, FileSourceType type) async {
    final result = await ChooseDocumentManager.pickFile(type);
    if (result != null) ctrl.addFile(result.file);
  }

  Future<void> _showExpenseHeadSheet(
    BuildContext context,
    UploadBillController ctrl,
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
    UploadBillController ctrl,
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

  Future<void> _showCampIDSheet(
    BuildContext context,
    UploadBillController ctrl,
  ) async {
    final list = await ctrl.fetchCampIDList();
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
          child: MultiSelectionDropDownListScreen(
            titleString: 'Camp ID',
            dropDownList: list,
            dropDownMenu: DropDownMultipleTypeMenu.CampID,
            onApplyTap: (p0) {
              ctrl.setCampIDList(p0.cast<ExpenseCampIDListV1Output>());
            },
          ),
        );
      },
    );
  }

  Future<void> _selectFromDate(BuildContext context, UploadBillController ctrl) async {
    final now = DateTime.now();
    final picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: DateTime(now.year - 100, now.month, 1),
      lastDate: DateTime(now.year, now.month, now.day),
    );
    if (picked != null) ctrl.setFromDate(picked);
  }

  Future<void> _selectToDate(BuildContext context, UploadBillController ctrl) async {
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
    if (picked != null) ctrl.setToDate(picked);
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
