// ignore_for_file: file_names, avoid_print, use_build_context_synchronously

import 'dart:convert';
import 'dart:io';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';

import '../../../../../Modules/constants/fonts.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';

import '../../../Modules/ChooseDocumentManager/ChooseDocumentManager.dart';
import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/BillSubmissionResponse/BillSubmissionResponse.dart';
import '../../../Modules/Json_Class/ExpenseCampIDListV1Response/ExpenseCampIDListV1Response.dart';
import '../../../Modules/Json_Class/ExpenseHeadResponse/ExpenseHeadResponse.dart';
import '../../../Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import '../../../Modules/Json_Class/SubExpenseHeadsResponse/SubExpenseHeadsResponse.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppActiveButton.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../../../Views/DropDownListScreen/DropDownListScreen.dart';
import '../../../Views/MultiSelectionDropDownListScreen/MultiSelectionDropDownListScreen.dart';

class UploadBillScreen extends StatefulWidget {
  const UploadBillScreen({super.key});

  @override
  State<UploadBillScreen> createState() => _UploadBillScreenState();
}

class _UploadBillScreenState extends State<UploadBillScreen> {
  TextEditingController amountOnBillTextField = TextEditingController();
  TextEditingController totalAmountTextField = TextEditingController();
  TextEditingController campIDTextField = TextEditingController();

  ExpenseHeaOutput? selectedExpenseHead;
  SubExpenseHeadsOutput? selectedSubExpenseHead;
  List<ExpenseCampIDListV1Output> campIDList = [];
  List<File> fileAttachmentList = [];

  LoginOutput? userLoginDetails;
  APIManager apiManager = APIManager();
  ToastManager toastManager = ToastManager();
  String fromDate = "";
  String toDate = "";

  DateTime? selectedFromDate;

  @override
  void initState() {
    super.initState();
    userLoginDetails = DataProvider().getParsedUserData()?.output?.first;

    Future.delayed(const Duration(milliseconds: 500), () {
      ToastManager.showWarningPopup(
        context,
        icWarningIcon,
        "कृपया नोंद घ्या, बिल मंजूर झाल्यानंतर कॅम्प यादीमधून काढून टाकला जाईल. त्यामुळे तुम्ही सर्व आवश्यक उप - शिर्षांसाठी च्या खर्च्यांची नोंद केल्याची खात्री करा.\n\nPlease note after bill approve, camp will be removed from list. So make sure you have entered expenses for all sub-expense heads. ",
      );
    });
    resetFromAndToDate();
  }

  void resetFromAndToDate() {
    fromDate = FormatterManager.formatDateToString(DateTime.now());
    toDate = FormatterManager.formatDateToString(DateTime.now());
    setState(() {});
  }

  void _showMultiSelectionDropDownBottomSheet(
    String title,
    List<dynamic> list,
    DropDownMultipleTypeMenu dropDownType,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.33,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: MultiSelectionDropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: (p0) {
              if (dropDownType == DropDownMultipleTypeMenu.CampID) {
                campIDList = p0;
                double totalSum = 0;
                for (ExpenseCampIDListV1Output obj in campIDList) {
                  totalSum += obj.expenseAmount ?? 0;
                }
                totalAmountTextField.text = "$totalSum";
              }
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  String getCampIDText() {
    if (campIDList.isEmpty) {
      return "";
    }

    return "Selected Camp ${campIDList.length}";
  }

  void _showDropDownBottomSheet(
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.33,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: DropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: (p0) {
              if (dropDownType == DropDownTypeMenu.ExpenseHead) {
                selectedExpenseHead = p0;
                selectedSubExpenseHead = null;
                totalAmountTextField.text = "";
                campIDList = [];
                resetFromAndToDate();
              } else if (dropDownType == DropDownTypeMenu.SubExpenseHead) {
                selectedSubExpenseHead = p0;
                totalAmountTextField.text = "";
                campIDList = [];
                resetFromAndToDate();
              }

              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  Future<void> _selectFromDate(BuildContext context) async {
    DateTime now = DateTime.now();
    DateTime hundredYearsAgo = DateTime(now.year - 100, now.month, 1);
    DateTime lastDate = DateTime(now.year, now.month, now.day);

    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: hundredYearsAgo,
      lastDate: lastDate,
    );

    if (picked != null) {
      setState(() {
        toDate = "";
        selectedFromDate = picked;
        fromDate = FormatterManager.formatDateToString(picked);
      });
    }
  }

  Future<void> _selectToDate(BuildContext context) async {
    if (selectedFromDate == null) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Please select From Date first')));
      return;
    }

    DateTime maxAllowed = DateTime(
      selectedFromDate!.year + 100,
      selectedFromDate!.month,
      selectedFromDate!.day,
    );
    DateTime lastDate = maxAllowed;
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: selectedFromDate!,
      firstDate: selectedFromDate!,
      lastDate: lastDate,
    );

    if (picked != null) {
      setState(() {
        toDate = FormatterManager.formatDateToString(picked);
      });
    }
  }

  void getExpenseHead() {
    ToastManager.showLoader();
    apiManager.getExpenseHeadAPI(apiExpenseHeadBack);
  }

  void apiExpenseHeadBack(
    ExpenseHeadResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      List<ExpenseHeaOutput> expenseHeadList = response?.output ?? [];
      _showDropDownBottomSheet(
        "Expense Head",
        expenseHeadList,
        DropDownTypeMenu.ExpenseHead,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getSubExpenseHeads() {
    if (selectedExpenseHead == null) {
      ToastManager.toast("Please select Expense Head");
      return;
    }
    ToastManager.showLoader();
    Map<String, String> params = {
      "ExpenseHead": selectedExpenseHead?.expenseHead?.toString() ?? "0",
      "OrganisedBy": "1",
      "CampType": "1",
    };
    print(params);
    apiManager.getSubExpenseHeadAPI(params, apiSubExpenseHeadBack);
  }

  void apiSubExpenseHeadBack(
    SubExpenseHeadsResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      List<SubExpenseHeadsOutput> expenseHeadList = response?.output ?? [];
      _showDropDownBottomSheet(
        "Sub Expense Head",
        expenseHeadList,
        DropDownTypeMenu.SubExpenseHead,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getCampID() {
    Map<String, String> params = {
      "FromReqDate": fromDate,
      "ToReqDate": toDate,
      "distlgdcode": userLoginDetails?.dISTLGDCODE.toString() ?? "0",
      "UserID": userLoginDetails?.empCode.toString() ?? "0",
      "SubExpenseID": selectedSubExpenseHead?.subExpenseID.toString() ?? "0",
    };
    print(params);
    ToastManager.showLoader();
    apiManager.getExpenseCampIDListV1API(params, apiCampIDBack);
  }

  void apiCampIDBack(
    ExpenseCampIDListV1Response? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      List<ExpenseCampIDListV1Output> expenseHeadList = response?.output ?? [];
      _showMultiSelectionDropDownBottomSheet(
        "Camp ID",
        expenseHeadList,
        DropDownMultipleTypeMenu.CampID,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void chooseDocumentTypeAlert() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Select Photo"),
          content: Text(""),
          actions: [
            TextButton(
              child: const Text("Take a Photo"),
              onPressed: () {
                Navigator.pop(context);
                _handleFilePick(FileSourceType.camera);
              },
            ),
            TextButton(
              child: const Text("Choose from Photo Library"),
              onPressed: () {
                Navigator.pop(context);
                _handleFilePick(FileSourceType.gallery);
              },
            ),
            TextButton(
              child: const Text("PDF"),
              onPressed: () {
                Navigator.pop(context);
                _handleFilePick(FileSourceType.pdf);
              },
            ),
            TextButton(
              child: const Text("Cancel"),
              onPressed: () {
                Navigator.pop(context);
              },
            ),
          ],
        );
      },
    );
  }

  Future<void> _handleFilePick(FileSourceType type) async {
    final result = await ChooseDocumentManager.pickFile(type);
    if (result != null) {
      fileAttachmentList.add(result.file);
      setState(() {});
    }
  }

  String convertCampIDsToJsonString() {
    List<Map<String, String>> campIDJsonArray = [];
    for (var campIDObj in campIDList) {
      campIDJsonArray.add({"CampID": campIDObj.campid.toString()});
    }
    return jsonEncode(campIDJsonArray);
  }

  bool validations() {
    String amountBillString = amountOnBillTextField.text.trim();
    String totalAmountString = totalAmountTextField.text.trim();

    if (selectedExpenseHead == null) {
      ToastManager.showAlertDialog(
        context,
        "Please select Expense Head",(){
        Navigator.pop(context);

      }
      );
      return false;
    }
    if (selectedSubExpenseHead == null) {
      ToastManager.showAlertDialog(
        context,
        "Please select Sub Expense Head",(){
        Navigator.pop(context);

      }
      );
      return false;
    }
    if (campIDList.isEmpty) {
      ToastManager.showAlertDialog(context, "Please select Camps",(){
        Navigator.pop(context);

      });
      return false;
    }
    if (amountBillString.isEmpty) {
      ToastManager.showAlertDialog(context, "Please Enter Amount",(){
        Navigator.pop(context);

      });
      return false;
    }
    if (double.parse(amountBillString) != double.parse(totalAmountString)) {
      ToastManager.showAlertDialog(
        context,
        "Bill amount is not matching with expenses entered for selected camps. Please verify camp wise entered expenses",(){
        Navigator.pop(context);

      }
      );
      return false;
    }
    if (fileAttachmentList.isEmpty) {
      ToastManager.showAlertDialog(context,  "Choose files to upload",(){
        Navigator.pop(context);

      });
      return false;
    }
    return true;
  }

  void submitData() {
    ToastManager.showLoader();
    String jsonString = convertCampIDsToJsonString();
    int createdBy = userLoginDetails?.empCode ?? 0;
    String amountBillString = amountOnBillTextField.text.trim();
    Map<String, String> params = {
      "TYPE_MultipleBillCampDetails": jsonString,
      "RequestType": "2",
      "SubExpenseID": selectedSubExpenseHead?.subExpenseID.toString() ?? "0",
      "AmountOnBill": amountBillString,
      "CreatedBy": createdBy.toString(),
    };

    apiManager.insertMultipleCampIDV2API(params, apiInsertMultipleCampIDV2Back);
  }

  void apiInsertMultipleCampIDV2Back(
    BillSubmissionResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      uploadBills(response?.message ?? "");
      // showDialog(
      //   context: context,
      //   builder: (BuildContext context) {
      //     return AlertDialog(
      //       title: Text("Success"),
      //       content: Text("Bill Details Saved successfully"),
      //       actions: [
      //         TextButton(
      //           child: const Text("Okay"),
      //           onPressed: () {
      //             Navigator.of(context).pop();
      //             Future.delayed(const Duration(milliseconds: 500), () {

      //             });
      //           },
      //         ),
      //       ],
      //     );
      //   },
      // );
    } else {
      ToastManager.hideLoader();
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void uploadBills(String billId) {
    print("uploadBills");
    String jsonString = convertCampIDsToJsonString();
    int createdBy = userLoginDetails?.empCode ?? 0;
    Map<String, String> params = {
      "Billid": billId,
      "createdBy": createdBy.toString(),
      "ExpenseHead": selectedExpenseHead?.expenseHead.toString() ?? "",
      "TYPE_MultipleBillCampDetails": jsonString,
      "SubExpenseID": selectedSubExpenseHead?.subExpenseID.toString() ?? "",
    };
    apiManager.uploadSequentially(
      fileAttachmentList,
      params,
      apiUploadBillsBack,
    );
  }

  void apiUploadBillsBack(
    ExpenseCampIDListV1Response? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    if (success) {
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text("Success"),
            content: Text("Bill Details Saved successfully"),
            actions: [
              TextButton(
                child: const Text("Okay"),
                onPressed: () {
                  Navigator.of(context).pop();
                  Navigator.of(context).pop();
                },
              ),
            ],
          );
        },
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Upload Bill",
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: AnnotatedRegion(
          value: SystemUiOverlayStyle(
            statusBarColor: kPrimaryColor,
            statusBarBrightness: Brightness.dark,
            statusBarIconBrightness: Brightness.light,
          ),
          child: Padding(
            padding: const EdgeInsets.fromLTRB(12, 8, 0, 8),
            child: SingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  AppTextField(
                    controller: TextEditingController(
                      text: selectedExpenseHead?.expenseHeadName ?? "",
                    ),
                    readOnly: true,
                    onTap: () {
                      getExpenseHead();
                    },
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
                    prefixIcon: SizedBox(
                      height: 20.h,
                      width: 20.w,
                      child: Center(
                        child: Image.asset(
                          icReceiptIcon,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                    suffixIcon: Icon(Icons.keyboard_arrow_down),
                  ),

                  // AppDropdownTextfield(
                  //   icon: icReceiptIcon,
                  //   titleHeaderString: "Expense Head",
                  //   valueString:
                  //   selectedExpenseHead?.expenseHeadName ?? "",
                  //   onTap: () {
                  //     getExpenseHead();
                  //   },
                  // ),
                  const SizedBox(height: 12),
                  // AppDropdownTextfield(
                  //   icon: icReceiptIcon,
                  //   titleHeaderString: "Sub Expense Head",
                  //   valueString:
                  //   selectedSubExpenseHead?.subexpenseName ?? "",
                  //   onTap: () {
                  //     getSubExpenseHeads();
                  //   },
                  // ),
                  AppTextField(
                    controller: TextEditingController(
                      text: selectedSubExpenseHead?.subexpenseName ?? "",
                    ),
                    readOnly: true,
                    onTap: () {
                      getSubExpenseHeads();
                    },
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
                    prefixIcon: SizedBox(
                      height: 20.h,
                      width: 20.w,
                      child: Center(
                        child: Image.asset(
                          icReceiptIcon,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                    suffixIcon: Icon(Icons.keyboard_arrow_down),
                  ),

                  const SizedBox(height: 8),
                  Container(
                    width: MediaQuery.of(context).size.width,
                    color: Colors.transparent,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Expanded(
                          child: AppTextField(
                            controller: TextEditingController(
                              text:
                                  selectedSubExpenseHead?.subexpenseName ?? "",
                            ),
                            readOnly: true,
                            onTap: () {
                              _selectFromDate(context);
                            },
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
                            // suffixIcon: Icon(Icons.keyboard_arrow_down),
                          ),

                          // AppDateTextfield(
                          //   icon: icCalendarMonth,
                          //   titleHeaderString: "From Date",
                          //   valueString: fromDate,
                          //   onTap: () {
                          //     _selectFromDate(context);
                          //   },
                          // ),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: AppTextField(
                            controller: TextEditingController(
                              text: toDate ?? "",
                            ),
                            readOnly: true,
                            onTap: () {
                              _selectToDate(context);
                            },
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
                            // suffixIcon: Icon(Icons.keyboard_arrow_down),
                          ).paddingOnly(right: 12),

                          // AppDateTextfield(
                          //   icon: icCalendarMonth,
                          //   titleHeaderString: "To Date",
                          //   valueString: toDate,
                          //   onTap: () {
                          //     _selectToDate(context);
                          //   },
                          // ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 8),
                  AppTextField(
                    controller: TextEditingController(text: getCampIDText()),
                    readOnly: true,
                    onTap: () {
                      getCampID();
                    },
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
                    prefixIcon: SizedBox(
                      height: 20.h,
                      width: 20.w,
                      child: Center(
                        child: Image.asset(
                          icReceiptIcon,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                    suffixIcon: Icon(Icons.keyboard_arrow_down),
                  ),
                  // AppDropdownTextfield(
                  //   icon: icReceiptIcon,
                  //   titleHeaderString: "Camp ID*",
                  //   valueString: getCampIDText(),
                  //   onTap: () {
                  //     getCampID();
                  //   },
                  // ),
                  const SizedBox(height: 8),
                  Row(
                    children: [
                      Expanded(
                        child: AppTextField(
                          controller: totalAmountTextField,
                          readOnly: true,
                          onTap: () {
                            getCampID();
                          },
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
                          prefixIcon: SizedBox(
                            height: 20.h,
                            width: 20.w,
                            child: Center(
                              child: Image.asset(
                                icCurrencyRupeeIcon,
                                height: 24.h,
                                width: 24.w,
                                fit: BoxFit.contain,
                              ),
                            ),
                          ),
                          // suffixIcon: Icon(Icons.keyboard_arrow_down),
                        ),

                        // AppIconTextfield(
                        //   icon: icCurrencyRupeeIcon,
                        //   titleHeaderString: "Total Amount*",
                        //   controller: totalAmountTextField,
                        //   textInputType: TextInputType.number,
                        //   enabled: false,
                        //   isDisabled: true,
                        // ),
                      ),
                      const SizedBox(width: 8),
                      Expanded(
                        child: AppTextField(
                          controller: amountOnBillTextField,
                          readOnly: true,
                          onTap: () {
                            getCampID();
                          },
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
                          prefixIcon: SizedBox(
                            height: 20.h,
                            width: 20.w,
                            child: Center(
                              child: Image.asset(
                                icCurrencyRupeeIcon,
                                height: 24.h,
                                width: 24.w,
                                fit: BoxFit.contain,
                              ),
                            ),
                          ),
                          // suffixIcon: Icon(Icons.keyboard_arrow_down),
                        ).paddingOnly(right: 12),

                        // AppIconTextfield(
                        //   icon: icCurrencyRupeeIcon,
                        //   titleHeaderString: "Amount On Bill*",
                        //   controller: amountOnBillTextField,
                        //   textInputType: TextInputType.number,
                        // ),
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
                          onTap: () {
                            chooseDocumentTypeAlert();
                          },
                          child: Container(
                            width: 50,
                            height: 50,
                            padding: EdgeInsets.all(10),
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
                          "Click on Camera to Upload the Bill",
                          style: TextStyle(
                            color: uploadBillTitleColor,
                            fontFamily: FontConstants.interFonts,
                            fontWeight: FontWeight.w400,
                            fontSize: responsiveFont(14),
                          ),
                        ),
                      ],
                    ),
                  ).paddingOnly(right: 12),
                  const SizedBox(height: 8),
                  ListView.builder(
                    physics: NeverScrollableScrollPhysics(),
                    shrinkWrap: true,
                    itemCount: fileAttachmentList.length,
                    itemBuilder: (context, index) {
                      File fileObj = fileAttachmentList[index];
                      return Padding(
                        padding: const EdgeInsets.fromLTRB(0, 0, 0, 12),
                        child: Container(
                          decoration: BoxDecoration(
                            color: attachmentBGColor,
                            border: Border.all(
                              color: attachmentBorderColor,
                              width: 1,
                            ),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          height: 60,
                          padding: EdgeInsets.fromLTRB(10, 0, 10, 0),
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
                                      "Size: ${FormatterManager.getFormattedFileSize(fileObj)}",
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
                                  fileAttachmentList.removeAt(index);
                                  setState(() {});
                                },
                                child: Container(
                                  padding: EdgeInsets.all(6),
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
                    buttontitle: "Submit",
                    onTap: () {
                      if (validations()) {
                        submitData();
                      }
                    },
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

// class UploadBillScreen extends StatefulWidget {
//   const UploadBillScreen({super.key});

//   @override
//   State<UploadBillScreen> createState() => _UploadBillScreenState();
// }

// class _UploadBillScreenState extends State<UploadBillScreen> {
//   TextEditingController amountOnBillTextField = TextEditingController();
//   TextEditingController totalAmountTextField = TextEditingController();
//   TextEditingController campIDTextField = TextEditingController();

//   ExpenseHeaOutput? selectedExpenseHead;
//   SubExpenseHeadsOutput? selectedSubExpenseHead;
//   List<ExpenseCampIDListV1Output> campIDList = [];
//   List<File> fileAttachmentList = [];

//   LoginOutput? userLoginDetails;
//   APIManager apiManager = APIManager();
//   ToastManager toastManager = ToastManager();
//   String fromDate = "";
//   String toDate = "";

//   DateTime? selectedFromDate;

//   @override
//   void initState() {
//     super.initState();
//     userLoginDetails = DataProvider().getParsedUserData()?.output?.first;

//     Future.delayed(const Duration(milliseconds: 500), () {
//       ToastManager.showWarningPopup(
//         context,
//         icWarningIcon,
//         "कृपया नोंद घ्या, बिल मंजूर झाल्यानंतर कॅम्प यादीमधून काढून टाकला जाईल. त्यामुळे तुम्ही सर्व आवश्यक उप - शिर्षांसाठी च्या खर्च्यांची नोंद केल्याची खात्री करा.\n\nPlease note after bill approve, camp will be removed from list. So make sure you have entered expenses for all sub-expense heads. ",
//       );
//     });
//     resetFromAndToDate();
//   }

//   resetFromAndToDate() {
//     fromDate = FormatterManager.formatDateToString(DateTime.now());
//     toDate = FormatterManager.formatDateToString(DateTime.now());
//     setState(() {});
//   }

//   void _showMultiSelectionDropDownBottomSheet(
//     String title,
//     List<dynamic> list,
//     DropDownMultipleTypeMenu dropDownType,
//   ) {
//     showModalBottomSheet(
//       context: context,
//       isScrollControlled: true,
//       constraints: const BoxConstraints(minWidth: double.infinity),
//       backgroundColor: Colors.white,
//       isDismissible: false,
//       enableDrag: false,
//       builder: (BuildContext context) {
//         return Container(
//           width: double.infinity,
//           height: MediaQuery.of(context).size.width * 1.33,
//           decoration: const BoxDecoration(
//             color: Colors.white,
//             borderRadius: BorderRadius.only(
//               topLeft: Radius.circular(20),
//               topRight: Radius.circular(20),
//             ),
//           ),
//           child: MultiSelectionDropDownListScreen(
//             titleString: title,
//             dropDownList: list,
//             dropDownMenu: dropDownType,
//             onApplyTap: (p0) {
//               if (dropDownType == DropDownMultipleTypeMenu.CampID) {
//                 campIDList = p0;
//                 double totalSum = 0;
//                 for (ExpenseCampIDListV1Output obj in campIDList) {
//                   totalSum += obj.expenseAmount ?? 0;
//                 }
//                 totalAmountTextField.text = "$totalSum";
//               }
//               setState(() {});
//             },
//           ),
//         );
//       },
//     ).whenComplete(() {
//       setState(() {});
//     });
//   }

//   String getCampIDText() {
//     if (campIDList.isEmpty) {
//       return "";
//     }

//     return "Selected Camp ${campIDList.length}";
//   }

//   void _showDropDownBottomSheet(
//     String title,
//     List<dynamic> list,
//     DropDownTypeMenu dropDownType,
//   ) {
//     showModalBottomSheet(
//       context: context,
//       isScrollControlled: true,
//       constraints: const BoxConstraints(minWidth: double.infinity),
//       backgroundColor: Colors.white,
//       isDismissible: false,
//       enableDrag: false,
//       builder: (BuildContext context) {
//         return Container(
//           width: double.infinity,
//           height: MediaQuery.of(context).size.width * 1.33,
//           decoration: const BoxDecoration(
//             color: Colors.white,
//             borderRadius: BorderRadius.only(
//               topLeft: Radius.circular(20),
//               topRight: Radius.circular(20),
//             ),
//           ),
//           child: DropDownListScreen(
//             titleString: title,
//             dropDownList: list,
//             dropDownMenu: dropDownType,
//             onApplyTap: (p0) {
//               if (dropDownType == DropDownTypeMenu.ExpenseHead) {
//                 selectedExpenseHead = p0;
//                 selectedSubExpenseHead = null;
//                 totalAmountTextField.text = "";
//                 campIDList = [];
//                 resetFromAndToDate();
//               } else if (dropDownType == DropDownTypeMenu.SubExpenseHead) {
//                 selectedSubExpenseHead = p0;
//                 totalAmountTextField.text = "";
//                 campIDList = [];
//                 resetFromAndToDate();
//               }

//               setState(() {});
//             },
//           ),
//         );
//       },
//     ).whenComplete(() {
//       setState(() {});
//     });
//   }

//   Future<void> _selectFromDate(BuildContext context) async {
//     DateTime now = DateTime.now();
//     DateTime hundredYearsAgo = DateTime(now.year - 100, now.month, 1);
//     DateTime lastDate = DateTime(now.year, now.month, now.day);

//     final DateTime? picked = await showDatePicker(
//       context: context,
//       initialDate: now,
//       firstDate: hundredYearsAgo,
//       lastDate: lastDate,
//     );

//     if (picked != null) {
//       setState(() {
//         toDate = "";
//         selectedFromDate = picked;
//         fromDate = FormatterManager.formatDateToString(picked);
//       });
//     }
//   }

//   Future<void> _selectToDate(BuildContext context) async {
//     if (selectedFromDate == null) {
//       ScaffoldMessenger.of(
//         context,
//       ).showSnackBar(SnackBar(content: Text('Please select From Date first')));
//       return;
//     }

//     DateTime maxAllowed = DateTime(
//       selectedFromDate!.year + 100,
//       selectedFromDate!.month,
//       selectedFromDate!.day,
//     );
//     DateTime lastDate = maxAllowed;
//     final DateTime? picked = await showDatePicker(
//       context: context,
//       initialDate: selectedFromDate!,
//       firstDate: selectedFromDate!,
//       lastDate: lastDate,
//     );

//     if (picked != null) {
//       setState(() {
//         toDate = FormatterManager.formatDateToString(picked);
//       });
//     }
//   }

//   getExpenseHead() {
//     ToastManager.showLoader();
//     apiManager.getExpenseHeadAPI(apiExpenseHeadBack);
//   }

//   void apiExpenseHeadBack(
//     ExpenseHeadResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       List<ExpenseHeaOutput> expenseHeadList = response?.output ?? [];
//       _showDropDownBottomSheet(
//         "Expense Head",
//         expenseHeadList,
//         DropDownTypeMenu.ExpenseHead,
//       );
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   getSubExpenseHeads() {
//     if (selectedExpenseHead == null) {
//       ToastManager.toast("Please select Expense Head");
//       return;
//     }
//     ToastManager.showLoader();
//     Map<String, String> params = {
//       "ExpenseHead": selectedExpenseHead?.expenseHead?.toString() ?? "0",
//       "OrganisedBy": "1",
//       "CampType": "1",
//     };
//     print(params);
//     apiManager.getSubExpenseHeadAPI(params, apiSubExpenseHeadBack);
//   }

//   void apiSubExpenseHeadBack(
//     SubExpenseHeadsResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       List<SubExpenseHeadsOutput> expenseHeadList = response?.output ?? [];
//       _showDropDownBottomSheet(
//         "Sub Expense Head",
//         expenseHeadList,
//         DropDownTypeMenu.SubExpenseHead,
//       );
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   getCampID() {
//     Map<String, String> params = {
//       "FromReqDate": fromDate,
//       "ToReqDate": toDate,
//       "distlgdcode": userLoginDetails?.dISTLGDCODE.toString() ?? "0",
//       "UserID": userLoginDetails?.empCode.toString() ?? "0",
//       "SubExpenseID": selectedSubExpenseHead?.subExpenseID.toString() ?? "0",
//     };
//     print(params);
//     ToastManager.showLoader();
//     apiManager.getExpenseCampIDListV1API(params, apiCampIDBack);
//   }

//   void apiCampIDBack(
//     ExpenseCampIDListV1Response? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();

//     if (success) {
//       List<ExpenseCampIDListV1Output> expenseHeadList = response?.output ?? [];
//       _showMultiSelectionDropDownBottomSheet(
//         "Camp ID",
//         expenseHeadList,
//         DropDownMultipleTypeMenu.CampID,
//       );
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   chooseDocumentTypeAlert() {
//     showDialog(
//       context: context,
//       builder: (BuildContext context) {
//         return AlertDialog(
//           title: Text("Select Photo"),
//           content: Text(""),
//           actions: [
//             TextButton(
//               child: const Text("Take a Photo"),
//               onPressed: () {
//                 Navigator.pop(context);
//                 _handleFilePick(FileSourceType.camera);
//               },
//             ),
//             TextButton(
//               child: const Text("Choose from Photo Library"),
//               onPressed: () {
//                 Navigator.pop(context);
//                 _handleFilePick(FileSourceType.gallery);
//               },
//             ),
//             TextButton(
//               child: const Text("PDF"),
//               onPressed: () {
//                 Navigator.pop(context);
//                 _handleFilePick(FileSourceType.pdf);
//               },
//             ),
//             TextButton(
//               child: const Text("Cancel"),
//               onPressed: () {
//                 Navigator.pop(context);
//               },
//             ),
//           ],
//         );
//       },
//     );
//   }

//   Future<void> _handleFilePick(FileSourceType type) async {
//     final result = await ChooseDocumentManager.pickFile(type);
//     if (result != null) {
//       fileAttachmentList.add(result.file);
//       setState(() {});
//     }
//   }

//   String convertCampIDsToJsonString() {
//     List<Map<String, String>> campIDJsonArray = [];
//     for (var campIDObj in campIDList) {
//       campIDJsonArray.add({"CampID": campIDObj.campid.toString()});
//     }
//     return jsonEncode(campIDJsonArray);
//   }

//   bool validations() {
//     String amountBillString = amountOnBillTextField.text.trim();
//     String totalAmountString = totalAmountTextField.text.trim();

//     if (selectedExpenseHead == null) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Please select Expense Head",
//       );
//       return false;
//     }
//     if (selectedSubExpenseHead == null) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Please select Sub Expense Head",
//       );
//       return false;
//     }
//     if (campIDList.isEmpty) {
//       ToastManager.showAlertDialog(context, "Alert", "Please select Camps");
//       return false;
//     }
//     if (amountBillString.isEmpty) {
//       ToastManager.showAlertDialog(context, "Alert", "Please Enter Amount");
//       return false;
//     }
//     if (double.parse(amountBillString) != double.parse(totalAmountString)) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Bill amount is not matching with expenses entered for selected camps. Please verify camp wise entered expenses",
//       );
//       return false;
//     }
//     if (fileAttachmentList.isEmpty) {
//       ToastManager.showAlertDialog(context, "Alert", "Choose files to upload");
//       return false;
//     }
//     return true;
//   }

//   submitData() {
//     ToastManager.showLoader();
//     String jsonString = convertCampIDsToJsonString();
//     int createdBy = userLoginDetails?.empCode ?? 0;
//     String amountBillString = amountOnBillTextField.text.trim();
//     Map<String, String> params = {
//       "TYPE_MultipleBillCampDetails": jsonString,
//       "RequestType": "2",
//       "SubExpenseID": selectedSubExpenseHead?.subExpenseID.toString() ?? "0",
//       "AmountOnBill": amountBillString,
//       "CreatedBy": createdBy.toString(),
//     };

//     apiManager.insertMultipleCampIDV2API(params, apiInsertMultipleCampIDV2Back);
//   }

//   void apiInsertMultipleCampIDV2Back(
//     BillSubmissionResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     if (success) {
//       uploadBills(response?.message ?? "");
//       // showDialog(
//       //   context: context,
//       //   builder: (BuildContext context) {
//       //     return AlertDialog(
//       //       title: Text("Success"),
//       //       content: Text("Bill Details Saved successfully"),
//       //       actions: [
//       //         TextButton(
//       //           child: const Text("Okay"),
//       //           onPressed: () {
//       //             Navigator.of(context).pop();
//       //             Future.delayed(const Duration(milliseconds: 500), () {

//       //             });
//       //           },
//       //         ),
//       //       ],
//       //     );
//       //   },
//       // );
//     } else {
//       ToastManager.hideLoader();
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   uploadBills(String billId) {
//     print("uploadBills");
//     String jsonString = convertCampIDsToJsonString();
//     int createdBy = userLoginDetails?.empCode ?? 0;
//     Map<String, String> params = {
//       "Billid": billId,
//       "createdBy": createdBy.toString(),
//       "ExpenseHead": selectedExpenseHead?.expenseHead.toString() ?? "",
//       "TYPE_MultipleBillCampDetails": jsonString,
//       "SubExpenseID": selectedSubExpenseHead?.subExpenseID.toString() ?? "",
//     };
//     apiManager.uploadSequentially(
//       fileAttachmentList,
//       params,
//       apiUploadBillsBack,
//     );
//   }

//   void apiUploadBillsBack(
//     ExpenseCampIDListV1Response? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();
//     if (success) {
//       showDialog(
//         context: context,
//         builder: (BuildContext context) {
//           return AlertDialog(
//             title: Text("Success"),
//             content: Text("Bill Details Saved successfully"),
//             actions: [
//               TextButton(
//                 child: const Text("Okay"),
//                 onPressed: () {
//                   Navigator.of(context).pop();
//                   Navigator.of(context).pop();
//                 },
//               ),
//             ],
//           );
//         },
//       );
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//   }

//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return KeyboardDismissOnTap(
//       child: Scaffold(
//         appBar: mAppBar(
//           scTitle: "Upload Bill",
//           leadingIcon: iconBackArrow,
//           onLeadingIconClick: () {
//             Navigator.pop(context);
//           },
//         ),
//         body: AnnotatedRegion(
//           value: SystemUiOverlayStyle(
//             statusBarColor: kPrimaryColor,
//             statusBarBrightness: Brightness.dark,
//             statusBarIconBrightness: Brightness.light,
//           ),
//           child: Container(
//             color: Colors.white,
//             height: SizeConfig.screenHeight,
//             width: SizeConfig.screenWidth,
//             child: Stack(
//               children: [
//                 Positioned(
//                   top: 74,
//                   child: Image.asset(
//                     fit: BoxFit.fill,
//                     rect4,
//                     width: SizeConfig.screenWidth,
//                     height: responsiveHeight(300.37),
//                   ),
//                 ),
//                 Positioned(
//                   top: 53,
//                   child: Image.asset(
//                     fit: BoxFit.fill,
//                     rect3,
//                     width: SizeConfig.screenWidth,
//                     height: responsiveHeight(300.37),
//                   ),
//                 ),
//                 Positioned(
//                   top: 30,
//                   child: Image.asset(
//                     fit: BoxFit.fill,
//                     rect2,
//                     width: SizeConfig.screenWidth,
//                     height: responsiveHeight(300.37),
//                   ),
//                 ),
//                 Image.asset(
//                   fit: BoxFit.fill,
//                   rect1,
//                   width: SizeConfig.screenWidth,
//                   height: responsiveHeight(300.37),
//                 ),
//                 Positioned(
//                   top: 10,
//                   bottom: 8,
//                   left: 20,
//                   right: 20,
//                   child: Container(
//                     decoration: BoxDecoration(
//                       color: Colors.white,
//                       borderRadius: BorderRadius.circular(10),
//                     ),
//                     child: Padding(
//                       padding: const EdgeInsets.fromLTRB(12, 12, 12, 12),
//                       child: SingleChildScrollView(
//                         child: Column(
//                           mainAxisAlignment: MainAxisAlignment.start,
//                           crossAxisAlignment: CrossAxisAlignment.start,
//                           children: [
//                             AppDropdownTextfield(
//                               icon: icReceiptIcon,
//                               titleHeaderString: "Expense Head",
//                               valueString:
//                                   selectedExpenseHead?.expenseHeadName ?? "",
//                               onTap: () {
//                                 getExpenseHead();
//                               },
//                             ),
//                             const SizedBox(height: 12),
//                             AppDropdownTextfield(
//                               icon: icReceiptIcon,
//                               titleHeaderString: "Sub Expense Head",
//                               valueString:
//                                   selectedSubExpenseHead?.subexpenseName ?? "",
//                               onTap: () {
//                                 getSubExpenseHeads();
//                               },
//                             ),
//                             const SizedBox(height: 12),
//                             Container(
//                               width: MediaQuery.of(context).size.width,
//                               color: Colors.transparent,
//                               child: Row(
//                                 mainAxisAlignment: MainAxisAlignment.start,
//                                 crossAxisAlignment: CrossAxisAlignment.start,
//                                 children: [
//                                   Expanded(
//                                     child: AppDateTextfield(
//                                       icon: icCalendarMonth,
//                                       titleHeaderString: "From Date",
//                                       valueString: fromDate,
//                                       onTap: () {
//                                         _selectFromDate(context);
//                                       },
//                                     ),
//                                   ),
//                                   const SizedBox(width: 12),
//                                   Expanded(
//                                     child: AppDateTextfield(
//                                       icon: icCalendarMonth,
//                                       titleHeaderString: "To Date",
//                                       valueString: toDate,
//                                       onTap: () {
//                                         _selectToDate(context);
//                                       },
//                                     ),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             const SizedBox(height: 12),
//                             AppDropdownTextfield(
//                               icon: icReceiptIcon,
//                               titleHeaderString: "Camp ID*",
//                               valueString: getCampIDText(),
//                               onTap: () {
//                                 getCampID();
//                               },
//                             ),
//                             const SizedBox(height: 12),
//                             AppIconTextfield(
//                               icon: icCurrencyRupeeIcon,
//                               titleHeaderString: "Total Amount*",
//                               controller: totalAmountTextField,
//                               textInputType: TextInputType.number,
//                               enabled: false,
//                               isDisabled: true,
//                             ),
//                             const SizedBox(height: 12),
//                             AppIconTextfield(
//                               icon: icCurrencyRupeeIcon,
//                               titleHeaderString: "Amount On Bill*",
//                               controller: amountOnBillTextField,
//                               textInputType: TextInputType.number,
//                             ),
//                             const SizedBox(height: 12),
//                             Container(
//                               width: MediaQuery.of(context).size.width,
//                               height: 152,
//                               decoration: BoxDecoration(
//                                 color: Colors.white,
//                                 border: Border.all(
//                                   color: borderColor,
//                                   width: 1,
//                                 ),
//                                 borderRadius: BorderRadius.circular(8),
//                               ),
//                               child: Column(
//                                 mainAxisAlignment: MainAxisAlignment.center,
//                                 crossAxisAlignment: CrossAxisAlignment.center,
//                                 children: [
//                                   GestureDetector(
//                                     onTap: () {
//                                       chooseDocumentTypeAlert();
//                                     },
//                                     child: Container(
//                                       width: 50,
//                                       height: 50,
//                                       padding: EdgeInsets.all(10),
//                                       decoration: BoxDecoration(
//                                         color: Colors.white,
//                                         border: Border.all(
//                                           color: borderColor,
//                                           width: 1,
//                                         ),
//                                         borderRadius: BorderRadius.circular(
//                                           100,
//                                         ),
//                                       ),
//                                       child: Image.asset(icCameraIcon),
//                                     ),
//                                   ),
//                                   const SizedBox(height: 8),
//                                   Text(
//                                     "Click on Camera to Upload the Bill",
//                                     style: TextStyle(
//                                       color: uploadBillTitleColor,
//                                       fontFamily: FontConstants.interFonts,
//                                       fontWeight: FontWeight.w400,
//                                       fontSize: responsiveFont(14),
//                                     ),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             const SizedBox(height: 12),
//                             ListView.builder(
//                               physics: NeverScrollableScrollPhysics(),
//                               shrinkWrap: true,
//                               itemCount: fileAttachmentList.length,
//                               itemBuilder: (context, index) {
//                                 File fileObj = fileAttachmentList[index];
//                                 return Padding(
//                                   padding: const EdgeInsets.fromLTRB(
//                                     0,
//                                     0,
//                                     0,
//                                     12,
//                                   ),
//                                   child: Container(
//                                     decoration: BoxDecoration(
//                                       color: attachmentBGColor,
//                                       border: Border.all(
//                                         color: attachmentBorderColor,
//                                         width: 1,
//                                       ),
//                                       borderRadius: BorderRadius.circular(8),
//                                     ),
//                                     height: 60,
//                                     padding: EdgeInsets.fromLTRB(10, 0, 10, 0),
//                                     child: Row(
//                                       children: [
//                                         SizedBox(
//                                           width: 30,
//                                           height: 30,
//                                           child: Image.asset(icPNGIcon),
//                                         ),
//                                         const SizedBox(width: 10),
//                                         Expanded(
//                                           child: Column(
//                                             mainAxisAlignment:
//                                                 MainAxisAlignment.center,
//                                             crossAxisAlignment:
//                                                 CrossAxisAlignment.start,
//                                             children: [
//                                               Text(
//                                                 FormatterManager.getFileNameInfo(
//                                                   fileObj,
//                                                 ),
//                                                 style: TextStyle(
//                                                   color: uploadBillTitleColor,
//                                                   fontFamily:
//                                                       FontConstants.interFonts,
//                                                   fontWeight: FontWeight.normal,
//                                                   fontSize: responsiveFont(14),
//                                                 ),
//                                               ),
//                                               const SizedBox(height: 4),
//                                               Text(
//                                                 "Size: ${FormatterManager.getFormattedFileSize(fileObj)}",
//                                                 style: TextStyle(
//                                                   color: dropDownTitleHeader,
//                                                   fontFamily:
//                                                       FontConstants.interFonts,
//                                                   fontWeight: FontWeight.normal,
//                                                   fontSize: responsiveFont(10),
//                                                 ),
//                                               ),
//                                             ],
//                                           ),
//                                         ),
//                                         GestureDetector(
//                                           onTap: () {
//                                             fileAttachmentList.removeAt(index);
//                                             setState(() {});
//                                           },
//                                           child: Container(
//                                             padding: EdgeInsets.all(6),
//                                             width: 30,
//                                             height: 30,
//                                             child: Image.asset(icTrashIcon),
//                                           ),
//                                         ),
//                                       ],
//                                     ),
//                                   ),
//                                 );
//                               },
//                             ),
//                             const SizedBox(height: 12),
//                             AppActiveButton(
//                               buttontitle: "Submit",
//                               onTap: () {
//                                 if (validations()) {
//                                   submitData();
//                                 }
//                               },
//                             ),
//                           ],
//                         ),
//                       ),
//                     ),
//                   ),
//                 ),
//               ],
//             ),
//           ),
//         ),
//       ),
//     );
//   }
// }
