// ignore_for_file: must_be_immutable, avoid_print, file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'dart:io';
import '../../../../../Modules/constants/fonts.dart';
import '../../../Modules/ChooseDocumentManager/ChooseDocumentManager.dart';
import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/FormatterManager/FormatterManager.dart';
import '../../../Modules/Json_Class/AdvadetailsNewVersionV2Response/AdvadetailsNewVersionV2Response.dart';
import '../../../Modules/Json_Class/BillSubmissionResponse/BillSubmissionResponse.dart';
import '../../../Modules/Json_Class/ExpenseHeadResponse/ExpenseHeadResponse.dart';
import '../../../Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import '../../../Modules/Json_Class/SubExpenseHeadsResponse/SubExpenseHeadsResponse.dart';
import '../../../Modules/ToastManager/ToastManager.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../../../Views/DropDownListScreen/DropDownListScreen.dart';

class AddBillSubmissionScreen extends StatefulWidget {
  AddBillSubmissionScreen({super.key, required this.advanceDetails});

  AdvadetailsNewOutput? advanceDetails;

  @override
  State<AddBillSubmissionScreen> createState() =>
      _AddBillSubmissionScreenState();
}

class _AddBillSubmissionScreenState extends State<AddBillSubmissionScreen> {
  TextEditingController noOfUnitTextField = TextEditingController();
  TextEditingController amountUnitTextField = TextEditingController();
  TextEditingController totalTextField = TextEditingController();
  TextEditingController remarkTextField = TextEditingController();

  APIManager apiManager = APIManager();

  int organizedById = 0;
  String organizedByName = "";
  bool isShowOrganizedBy = true;
  String registeredWorkers = "0";
  String advanceApprovedAmount = "0";

  bool showPhotoUpload = false;

  // bool isBillRequired = false;

  File? selectedFile;
  String? fileType;
  LoginOutput? userLoginDetails;
  ExpenseHeaOutput? selectedExpenseHead;
  SubExpenseHeadsOutput? selectedSubExpenseHead;

  @override
  void initState() {
    super.initState();
    userLoginDetails = DataProvider().getParsedUserData()?.output?.first;
    organizedById = widget.advanceDetails?.initiatedBy ?? 0;

    noOfUnitTextField.text = "";
    amountUnitTextField.text = "";
    totalTextField.text = "";
    remarkTextField.text = "";
    registeredWorkers = "";
    advanceApprovedAmount = "Rs. 0.0";

    if (organizedById == 1) {
      organizedByName = "Internal Team";
      isShowOrganizedBy = true;
    } else if (organizedById == 2) {
      organizedByName = "Government";
      isShowOrganizedBy = true;
    } else if (organizedById == 3) {
      organizedByName = "NGO";
      isShowOrganizedBy = true;
    } else if (organizedById == 4) {
      organizedByName = "Labor Contractor";
      isShowOrganizedBy = true;
    } else if (organizedById == 5) {
      organizedByName = "Kit Vendor";
      isShowOrganizedBy = true;
    } else if (organizedById == 6) {
      organizedByName = "ACL Office";
      isShowOrganizedBy = true;
    } else if (organizedById == 7) {
      organizedByName = "vendor/union Leader";
      isShowOrganizedBy = true;
    } else if (organizedById == 8) {
      organizedByName = "Flexi camp";
      isShowOrganizedBy = true;
    } else if (organizedById == 9) {
      organizedByName = "GramPanchyat";
      isShowOrganizedBy = true;
    } else if (organizedById == 10) {
      organizedByName = "Self-organized";
      isShowOrganizedBy = true;
    } else {
      isShowOrganizedBy = false;
    }
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
                noOfUnitTextField.text = "";
                amountUnitTextField.text = "";
                totalTextField.text = "";
                remarkTextField.text = "";
                registeredWorkers = "";
                advanceApprovedAmount = "Rs. 0.0";
                addDataOnUI();
              } else if (dropDownType == DropDownTypeMenu.SubExpenseHead) {
                selectedSubExpenseHead = p0;
                noOfUnitTextField.text = "";
                amountUnitTextField.text = "";
                totalTextField.text = "";
                remarkTextField.text = "";
                registeredWorkers = "";
                advanceApprovedAmount = "Rs. 0.0";
                addDataOnUI();
              }
              // else if (dropDownType == DropDownTypeMenu.TalukaCampList) {
              //   selectedTaluka = p0;
              // } else if (dropDownType ==
              //     DropDownTypeMenu.LandingLabCampCreation) {
              //   selectedLandingLab = p0;
              //   getHomeAndHubLabAPI();
              // } else if (dropDownType == DropDownTypeMenu.ScreeningTest) {
              //   selectedScreeningTest = p0;
              // }
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  void addDataOnUI() {
    double maxAllowedAmt = selectedSubExpenseHead?.maxAllowedAmt ?? 0;
    print(maxAllowedAmt);
    double campHall =
        widget.advanceDetails?.campHallGramPanchayatSchoolGovtOfficeTent ?? 0.0;
    double chairs = widget.advanceDetails?.chairs ?? 0.0;
    double tables = widget.advanceDetails?.tables ?? 0.0;
    double cleaningCharges = widget.advanceDetails?.cleaningCharges ?? 0.0;
    double drinkingWater = widget.advanceDetails?.drinkingWater ?? 0.0;
    double beneficiaryRefreshment =
        widget.advanceDetails?.beneficiaryRefreshment ?? 0.0;
    double sampleMovementToLabTSRTCOrAnyOtherCargo =
        widget.advanceDetails?.sampleMovementToLabTSRTCOrAnyOtherCargo ?? 0.0;
    double sampleMovementToLabRunnerBoy =
        widget.advanceDetails?.sampleMovementToLabRunnerBoy ?? 0.0;
    double campAwarenessUsingBhopu =
        widget.advanceDetails?.campAwarenessUsingBhopu ?? 0.0;
    double transportationOfStaffTAAllowanceToStaffIndividuals =
        widget.advanceDetails?.transportationOfStaffTAIndividual ?? 0.0;
    double
    transportationOfStaffTAAllowanceToStaffGroupTransportationLikeAutoTumTum =
        widget.advanceDetails?.transportationOfStaffTAGroupOfTransport ?? 0.0;
    double foodToStaffTAAllowance =
        widget.advanceDetails?.foodToStaffTAAllowance ?? 0.0;
    double postCampExpense = widget.advanceDetails?.postCampExpense ?? 0.0;
    switch (selectedSubExpenseHead?.subExpenseID) {
      case 1:
        advanceApprovedAmount = "Rs. $campHall";
      case 2:
        advanceApprovedAmount = "Rs. $chairs";
      case 3:
        advanceApprovedAmount = "Rs.  $tables";
      case 4:
        advanceApprovedAmount = "Rs. $cleaningCharges";
      case 5:
        advanceApprovedAmount = "Rs. $drinkingWater";
      case 6:
        advanceApprovedAmount = "Rs. $beneficiaryRefreshment";
      case 7:
        advanceApprovedAmount = "Rs. $sampleMovementToLabTSRTCOrAnyOtherCargo";
      case 8:
        advanceApprovedAmount = "Rs. $sampleMovementToLabRunnerBoy";
      case 9:
        advanceApprovedAmount = "Rs. $campAwarenessUsingBhopu";
      case 10:
        advanceApprovedAmount =
            "Rs. $transportationOfStaffTAAllowanceToStaffIndividuals";
      case 11:
        advanceApprovedAmount =
            "Rs. $transportationOfStaffTAAllowanceToStaffGroupTransportationLikeAutoTumTum";
      case 12:
        advanceApprovedAmount = "Rs. $foodToStaffTAAllowance";
      case 13:
        advanceApprovedAmount = "Rs. $postCampExpense";

      default:
        break;
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
      "OrganisedBy": widget.advanceDetails?.initiatedBy.toString() ?? "0",
      "CampType": widget.advanceDetails?.campType.toString() ?? "0",
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

  bool? calculateTotalNoOfUnitToAmount(String amount) {
    String noOfUnit = noOfUnitTextField.text.trim();

    if (selectedSubExpenseHead == null) {
      ToastManager.toast("Please select Sub Expense Head");
      return false;
    }
    if (noOfUnit == "0") {
      ToastManager.toast("Please enter No Of Unit");
      return false;
    }

    if (noOfUnit.isEmpty) {
      noOfUnit = "0";
    }
    int total = 0;

    double maxAllowedAmt = selectedSubExpenseHead?.maxAllowedAmt ?? 0.0;
    print(maxAllowedAmt);

    int count = int.parse(noOfUnit);
    int amountInt = int.parse(amount);
    total = amountInt * count;
    if (total > maxAllowedAmt) {
      showPhotoUpload = true;
      ToastManager.showAlertDialog(
        context,
        "Only one file is allowed to upload as a proof of permission",(){
        Navigator.pop(context);

      }
      );
    } else {
      showPhotoUpload = false;
    }

    totalTextField.text = "${int.parse(noOfUnit) * int.parse(amount)}";

    setState(() {});
    return true;
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
      setState(() {
        selectedFile = result.file;
        fileType = result.fileType;
        print(fileType);
      });
    }
  }

  bool validations() {
    String noOfUnit = noOfUnitTextField.text.trim();
    String amountUnit = amountUnitTextField.text.trim();
    String totalAmount = totalTextField.text.trim();
    String remark = remarkTextField.text.trim();
    if (selectedExpenseHead == null) {
      ToastManager.showAlertDialog(
        context,
        "Please choose Expense Head",(){
        Navigator.pop(context);

      }
      );
      return false;
    } else if (selectedSubExpenseHead == null) {
      ToastManager.showAlertDialog(
        context,
        "Please choose Sub Expense Head",(){
        Navigator.pop(context);

      }
      );
      return false;
    } else if (organizedByName.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        "Please choose Organized By",(){
        Navigator.pop(context);

      }
      );
      return false;
    } else if (noOfUnit.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        "Please enter No Of Units Required",(){
        Navigator.pop(context);

      }
      );
      return false;
    } else if (amountUnit.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        "Please enter Amount Per Unit",(){
        Navigator.pop(context);

      }
      );
      return false;
    } else if (totalAmount.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        "Please enter Total Amount",(){
        Navigator.pop(context);

      }
      );
      return false;
    } else if (remark.isEmpty) {
      ToastManager.showAlertDialog(context, "Please enter remark",(){
        Navigator.pop(context);

      });
      return false;
    } else if (showPhotoUpload) {
      if (selectedFile == null) {
        ToastManager.showAlertDialog(
          context,
          "Choose files to upload",(){
          Navigator.pop(context);

        }
        );
        return false;
      }
    }
    return true;
  }

  void saveBillDetails() {
    Map<String, String> params = {
      "Campid": widget.advanceDetails?.campid?.toString() ?? "",
      "SubExpenseID": selectedSubExpenseHead?.subExpenseID.toString() ?? "0",
      "ExpenseAmount": totalTextField.text.trim(),
      "CreatedBy": userLoginDetails?.empCode.toString() ?? "",
      "ProcessID": "1",
      "ApprovalStatus": "0",
      "RequestType": "2",
      "expeDescription": remarkTextField.text.trim(),
      "NoOfItems": noOfUnitTextField.text.trim(),
      "PerItemPrice": amountUnitTextField.text.trim(),
      "OrganisedBy": organizedById.toString(),
    };

    apiManager.saveBillDetailsAPI(params, apiSaveBillDetailsBack);
  }

  void apiSaveBillDetailsBack(
    BillSubmissionResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      if (showPhotoUpload) {
        uploadBills(response?.message ?? "");
      } else {
        ToastManager.hideLoader();
        ToastManager.showSuccessPopup(
          context,
          icSuccessIcon,
          "Bill Submitted Successfully.",
        );
      }
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void uploadBills(String message) {
    print("uploadBills");
    Map<String, String> params = {
      "Billid": message,
      "createdBy": userLoginDetails?.empCode.toString() ?? "",
      "ExpenseHead": selectedExpenseHead?.expenseHead.toString() ?? "",
    };
    print(params);
    apiManager.uploadBillsAPI(
      params,
      selectedFile!,
      fileType!,
      apiUploadBillsBack,
    );
  }

  void apiUploadBillsBack(
    String? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();
    print("Result uploadBills");
    if (success) {
      ToastManager.showSuccessPopup(
        context,
        icSuccessIcon,
        "Bill Submitted Successfully.",
      );
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: "Bill Submission",
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
            padding: const EdgeInsets.fromLTRB(10, 8, 10, 8),
            child: SingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  AppTextField(
                    readOnly: true,
                    controller: TextEditingController(
                      text: selectedExpenseHead?.expenseHeadName ?? "",
                    ),
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
                  const SizedBox(height: 8),
                  AppTextField(
                    readOnly: true,
                    controller: TextEditingController(
                      text: selectedSubExpenseHead?.subexpenseName ?? "",
                    ),
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

                  // AppDropdownTextfield(
                  //   icon: icReceiptIcon,
                  //   titleHeaderString: "Sub Expense Head",
                  //   valueString:
                  //   selectedSubExpenseHead?.subexpenseName ?? "",
                  //   onTap: () {
                  //     getSubExpenseHeads();
                  //   },
                  // ),
                  Visibility(
                    visible: isShowOrganizedBy == true,
                    child: const SizedBox(height: 8),
                  ),
                  Visibility(
                    visible: isShowOrganizedBy == true,
                    child: AppTextField(
                      readOnly: true,
                      controller: TextEditingController(text: organizedByName),

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
                      prefixIcon: SizedBox(
                        height: 20.h,
                        width: 20.w,
                        child: Center(
                          child: Image.asset(
                            icUserIcon,
                            height: 24.h,
                            width: 24.w,
                            fit: BoxFit.contain,
                          ),
                        ),
                      ),
                    ),
                    // AppDropdownTextfield(
                    //   icon: icUserIcon,
                    //   titleHeaderString: "Organized By",
                    //   valueString: organizedByName,
                    //   onTap: () {
                    //     // getTalukaAPI();
                    //   },
                    // ),
                  ),
                  const SizedBox(height: 8),
                  Container(
                    color: Colors.transparent,
                    child: Row(
                      children: [
                        SizedBox(
                          width: 20,
                          height: 20,
                          child: Image.asset(icUserIcon),
                        ),
                        const SizedBox(width: 6),
                        Text(
                          "Registered Workers : ",
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kBlackColor,
                            fontSize: responsiveFont(14),
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                        Text(
                          registeredWorkers,
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kBlackColor,
                            fontSize: responsiveFont(14),
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 8),
                  Container(
                    color: Colors.transparent,
                    child: Row(
                      children: [
                        SizedBox(
                          width: 20,
                          height: 20,
                          child: Image.asset(icCurrencyRupeeIcon),
                        ),
                        const SizedBox(width: 6),
                        Text(
                          "Advance Approved Amount : ",
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kBlackColor,
                            fontSize: responsiveFont(14),
                            fontWeight: FontWeight.w500,
                          ),
                        ),
                        Text(
                          advanceApprovedAmount,
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: kBlackColor,
                            fontSize: responsiveFont(14),
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    "Note: Approved Bill Amount will be deducted from your Bill Amount",
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      color: noteRedColor,
                      fontSize: responsiveFont(14),
                      fontWeight: FontWeight.w500,
                    ),
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
                            readOnly: false,
                            controller: noOfUnitTextField,
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
                            prefixIcon: SizedBox(
                              height: 20.h,
                              width: 20.w,
                              child: Center(
                                child: Image.asset(
                                  icHashIcon,
                                  height: 24.h,
                                  width: 24.w,
                                  fit: BoxFit.contain,
                                ),
                              ),
                            ),
                          ),

                          // AppIconTextfield(
                          //   icon: icHashIcon,
                          //   titleHeaderString: "No. of Unit*",
                          //   controller: noOfUnitTextField,
                          //   textInputType: TextInputType.number,
                          //   maxLength: 1,
                          //   didChangeText: (p0) {},
                          // ),
                        ),
                        const SizedBox(width: 8),
                        Expanded(
                          child: AppTextField(
                            readOnly: false,
                            textInputType: TextInputType.number,

                            controller: noOfUnitTextField,
                            onChange: (value) {
                              calculateTotalNoOfUnitToAmount(value);
                            },
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
                            prefixIcon: SizedBox(
                              height: 20.h,
                              width: 20.w,
                              child: Center(
                                child: Image.asset(
                                  icHashIcon,
                                  height: 24.h,
                                  width: 24.w,
                                  fit: BoxFit.contain,
                                ),
                              ),
                            ),
                          ),
                          // AppIconTextfield(
                          //   icon: icHashIcon,
                          //   titleHeaderString: "Amount/Unit*",
                          //   controller: amountUnitTextField,
                          //   textInputType: TextInputType.number,
                          //   maxLength: 4,
                          //   didChangeText: (p0) {
                          //     calculateTotalNoOfUnitToAmount(p0);
                          //   },
                          // ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 8),
                  AppTextField(
                    readOnly: true,
                    controller: totalTextField,
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
                  ),
                  // AppIconTextfield(
                  //   icon: icCurrencyRupeeIcon,
                  //   titleHeaderString: "Total*",
                  //   controller: totalTextField,
                  //   textInputType: TextInputType.number,
                  //   enabled: false,
                  //   isDisabled: true,
                  // ),
                  const SizedBox(height: 8),
                  AppTextField(
                    maxLines: 2,
                    readOnly: false,
                    controller: remarkTextField,
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
                    prefixIcon: SizedBox(
                      height: 20.h,
                      width: 20.w,
                      child: Center(
                        child: Image.asset(
                          icRemarkIconn,
                          height: 24.h,
                          width: 24.w,
                          fit: BoxFit.contain,
                        ),
                      ),
                    ),
                  ),

                  // AppMultilineTextField(
                  //   leftIcon: icRemarkIconn,
                  //   titleString: "Remark",
                  //   controller: remarkTextField,
                  // ),
                  showPhotoUpload == true
                      ? const SizedBox(height: 8)
                      : SizedBox(),
                  showPhotoUpload == true
                      ? Container(
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
                                if (selectedFile == null) {
                                  chooseDocumentTypeAlert();
                                } else {
                                  ToastManager.showAlertDialog(
                                    context,
                                    "You already selected file",(){
                                    Navigator.pop(context);

                                  }
                                  );
                                }
                              },
                              child: Container(
                                width: 50,
                                height: 50,
                                padding: EdgeInsets.all(10),
                                decoration: BoxDecoration(
                                  color: Colors.white,
                                  border: Border.all(
                                    color: borderColor,
                                    width: 1,
                                  ),
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
                      )
                      : Container(),
                  selectedFile == null
                      ? const SizedBox()
                      : const SizedBox(height: 8),
                  selectedFile == null
                      ? Container()
                      : Container(
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
                                    FormatterManager.getFileNameInfo(
                                      selectedFile,
                                    ),
                                    style: TextStyle(
                                      color: uploadBillTitleColor,
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.normal,
                                      fontSize: responsiveFont(14),
                                    ),
                                  ),
                                  const SizedBox(height: 4),
                                  Text(
                                    "Size: ${FormatterManager.getFormattedFileSize(selectedFile)}",
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
                                selectedFile = null;
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
                  const SizedBox(height: 8),
                  Align(
                    alignment: Alignment.center,
                    child: SizedBox(
                      width: 200.w,
                      child: AppActiveButton(
                        buttontitle: "Save",
                        onTap: () {
                          if (validations()) {
                            // uploadBills("6188");
                            saveBillDetails();
                          }
                        },
                      ),
                    ),
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

// class AddBillSubmissionScreen extends StatefulWidget {
//   AddBillSubmissionScreen({super.key, required this.advanceDetails});

//   AdvadetailsNewOutput? advanceDetails;
//   @override
//   State<AddBillSubmissionScreen> createState() =>
//       _AddBillSubmissionScreenState();
// }

// class _AddBillSubmissionScreenState extends State<AddBillSubmissionScreen> {
//   TextEditingController noOfUnitTextField = TextEditingController();
//   TextEditingController amountUnitTextField = TextEditingController();
//   TextEditingController totalTextField = TextEditingController();
//   TextEditingController remarkTextField = TextEditingController();

//   APIManager apiManager = APIManager();

//   int organizedById = 0;
//   String organizedByName = "";
//   bool isShowOrganizedBy = true;
//   String registeredWorkers = "0";
//   String advanceApprovedAmount = "0";

//   bool showPhotoUpload = false;
//   // bool isBillRequired = false;

//   File? selectedFile;
//   String? fileType;
//   LoginOutput? userLoginDetails;
//   ExpenseHeaOutput? selectedExpenseHead;
//   SubExpenseHeadsOutput? selectedSubExpenseHead;
//   @override
//   void initState() {
//     super.initState();
//     userLoginDetails = DataProvider().getParsedUserData()?.output?.first;
//     organizedById = widget.advanceDetails?.initiatedBy ?? 0;

//     noOfUnitTextField.text = "";
//     amountUnitTextField.text = "";
//     totalTextField.text = "";
//     remarkTextField.text = "";
//     registeredWorkers = "";
//     advanceApprovedAmount = "Rs. 0.0";

//     if (organizedById == 1) {
//       organizedByName = "Internal Team";
//       isShowOrganizedBy = true;
//     } else if (organizedById == 2) {
//       organizedByName = "Government";
//       isShowOrganizedBy = true;
//     } else if (organizedById == 3) {
//       organizedByName = "NGO";
//       isShowOrganizedBy = true;
//     } else if (organizedById == 4) {
//       organizedByName = "Labor Contractor";
//       isShowOrganizedBy = true;
//     } else if (organizedById == 5) {
//       organizedByName = "Kit Vendor";
//       isShowOrganizedBy = true;
//     } else if (organizedById == 6) {
//       organizedByName = "ACL Office";
//       isShowOrganizedBy = true;
//     } else if (organizedById == 7) {
//       organizedByName = "vendor/union Leader";
//       isShowOrganizedBy = true;
//     } else if (organizedById == 8) {
//       organizedByName = "Flexi camp";
//       isShowOrganizedBy = true;
//     } else if (organizedById == 9) {
//       organizedByName = "GramPanchyat";
//       isShowOrganizedBy = true;
//     } else if (organizedById == 10) {
//       organizedByName = "Self-organized";
//       isShowOrganizedBy = true;
//     } else {
//       isShowOrganizedBy = false;
//     }
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
//                 noOfUnitTextField.text = "";
//                 amountUnitTextField.text = "";
//                 totalTextField.text = "";
//                 remarkTextField.text = "";
//                 registeredWorkers = "";
//                 advanceApprovedAmount = "Rs. 0.0";
//                 addDataOnUI();
//               } else if (dropDownType == DropDownTypeMenu.SubExpenseHead) {
//                 selectedSubExpenseHead = p0;
//                 noOfUnitTextField.text = "";
//                 amountUnitTextField.text = "";
//                 totalTextField.text = "";
//                 remarkTextField.text = "";
//                 registeredWorkers = "";
//                 advanceApprovedAmount = "Rs. 0.0";
//                 addDataOnUI();
//               }
//               // else if (dropDownType == DropDownTypeMenu.TalukaCampList) {
//               //   selectedTaluka = p0;
//               // } else if (dropDownType ==
//               //     DropDownTypeMenu.LandingLabCampCreation) {
//               //   selectedLandingLab = p0;
//               //   getHomeAndHubLabAPI();
//               // } else if (dropDownType == DropDownTypeMenu.ScreeningTest) {
//               //   selectedScreeningTest = p0;
//               // }
//               setState(() {});
//             },
//           ),
//         );
//       },
//     ).whenComplete(() {
//       setState(() {});
//     });
//   }

//   addDataOnUI() {
//     double maxAllowedAmt = selectedSubExpenseHead?.maxAllowedAmt ?? 0;
//     print(maxAllowedAmt);
//     double campHall =
//         widget.advanceDetails?.campHallGramPanchayatSchoolGovtOfficeTent ?? 0.0;
//     double chairs = widget.advanceDetails?.chairs ?? 0.0;
//     double tables = widget.advanceDetails?.tables ?? 0.0;
//     double cleaningCharges = widget.advanceDetails?.cleaningCharges ?? 0.0;
//     double drinkingWater = widget.advanceDetails?.drinkingWater ?? 0.0;
//     double beneficiaryRefreshment =
//         widget.advanceDetails?.beneficiaryRefreshment ?? 0.0;
//     double sampleMovementToLabTSRTCOrAnyOtherCargo =
//         widget.advanceDetails?.sampleMovementToLabTSRTCOrAnyOtherCargo ?? 0.0;
//     double sampleMovementToLabRunnerBoy =
//         widget.advanceDetails?.sampleMovementToLabRunnerBoy ?? 0.0;
//     double campAwarenessUsingBhopu =
//         widget.advanceDetails?.campAwarenessUsingBhopu ?? 0.0;
//     double transportationOfStaffTAAllowanceToStaffIndividuals =
//         widget.advanceDetails?.transportationOfStaffTAIndividual ?? 0.0;
//     double
//     transportationOfStaffTAAllowanceToStaffGroupTransportationLikeAutoTumTum =
//         widget.advanceDetails?.transportationOfStaffTAGroupOfTransport ?? 0.0;
//     double foodToStaffTAAllowance =
//         widget.advanceDetails?.foodToStaffTAAllowance ?? 0.0;
//     double postCampExpense = widget.advanceDetails?.postCampExpense ?? 0.0;
//     switch (selectedSubExpenseHead?.subExpenseID) {
//       case 1:
//         advanceApprovedAmount = "Rs. $campHall";
//       case 2:
//         advanceApprovedAmount = "Rs. $chairs";
//       case 3:
//         advanceApprovedAmount = "Rs.  $tables";
//       case 4:
//         advanceApprovedAmount = "Rs. $cleaningCharges";
//       case 5:
//         advanceApprovedAmount = "Rs. $drinkingWater";
//       case 6:
//         advanceApprovedAmount = "Rs. $beneficiaryRefreshment";
//       case 7:
//         advanceApprovedAmount = "Rs. $sampleMovementToLabTSRTCOrAnyOtherCargo";
//       case 8:
//         advanceApprovedAmount = "Rs. $sampleMovementToLabRunnerBoy";
//       case 9:
//         advanceApprovedAmount = "Rs. $campAwarenessUsingBhopu";
//       case 10:
//         advanceApprovedAmount =
//             "Rs. $transportationOfStaffTAAllowanceToStaffIndividuals";
//       case 11:
//         advanceApprovedAmount =
//             "Rs. $transportationOfStaffTAAllowanceToStaffGroupTransportationLikeAutoTumTum";
//       case 12:
//         advanceApprovedAmount = "Rs. $foodToStaffTAAllowance";
//       case 13:
//         advanceApprovedAmount = "Rs. $postCampExpense";

//       default:
//         break;
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
//       "OrganisedBy": widget.advanceDetails?.initiatedBy.toString() ?? "0",
//       "CampType": widget.advanceDetails?.campType.toString() ?? "0",
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

//   calculateTotalNoOfUnitToAmount(String amount) {
//     String noOfUnit = noOfUnitTextField.text.trim();

//     if (selectedSubExpenseHead == null) {
//       ToastManager.toast("Please select Sub Expense Head");
//       return false;
//     }
//     if (noOfUnit == "0") {
//       ToastManager.toast("Please enter No Of Unit");
//       return false;
//     }

//     if (noOfUnit.isEmpty) {
//       noOfUnit = "0";
//     }
//     int total = 0;

//     double maxAllowedAmt = selectedSubExpenseHead?.maxAllowedAmt ?? 0.0;
//     print(maxAllowedAmt);

//     int count = int.parse(noOfUnit);
//     int amountInt = int.parse(amount);
//     total = amountInt * count;
//     if (total > maxAllowedAmt) {
//       showPhotoUpload = true;
//       ToastManager.showAlertDialog(
//         context,
//         "Note",
//         "Only one file is allowed to upload as a proof of permission",
//       );
//     } else {
//       showPhotoUpload = false;
//     }

//     totalTextField.text = "${int.parse(noOfUnit) * int.parse(amount)}";
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
//       setState(() {
//         selectedFile = result.file;
//         fileType = result.fileType;
//         print(fileType);
//       });
//     }
//   }

//   bool validations() {
//     String noOfUnit = noOfUnitTextField.text.trim();
//     String amountUnit = amountUnitTextField.text.trim();
//     String totalAmount = totalTextField.text.trim();
//     String remark = remarkTextField.text.trim();
//     if (selectedExpenseHead == null) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Please choose Expense Head",
//       );
//       return false;
//     } else if (selectedSubExpenseHead == null) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Please choose Sub Expense Head",
//       );
//       return false;
//     } else if (organizedByName.isEmpty) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Please choose Organized By",
//       );
//       return false;
//     } else if (noOfUnit.isEmpty) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Please enter No Of Units Required",
//       );
//       return false;
//     } else if (amountUnit.isEmpty) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Please enter Amount Per Unit",
//       );
//       return false;
//     } else if (totalAmount.isEmpty) {
//       ToastManager.showAlertDialog(
//         context,
//         "Alert",
//         "Please enter Total Amount",
//       );
//       return false;
//     } else if (remark.isEmpty) {
//       ToastManager.showAlertDialog(context, "Alert", "Please enter remark");
//       return false;
//     } else if (showPhotoUpload) {
//       if (selectedFile == null) {
//         ToastManager.showAlertDialog(
//           context,
//           "Alert",
//           "Choose files to upload",
//         );
//         return false;
//       }
//     }
//     return true;
//   }

//   saveBillDetails() {
//     Map<String, String> params = {
//       "Campid": widget.advanceDetails?.campid?.toString() ?? "",
//       "SubExpenseID": selectedSubExpenseHead?.subExpenseID.toString() ?? "0",
//       "ExpenseAmount": totalTextField.text.trim(),
//       "CreatedBy": userLoginDetails?.empCode.toString() ?? "",
//       "ProcessID": "1",
//       "ApprovalStatus": "0",
//       "RequestType": "2",
//       "expeDescription": remarkTextField.text.trim(),
//       "NoOfItems": noOfUnitTextField.text.trim(),
//       "PerItemPrice": amountUnitTextField.text.trim(),
//       "OrganisedBy": organizedById.toString(),
//     };

//     apiManager.saveBillDetailsAPI(params, apiSaveBillDetailsBack);
//   }

//   void apiSaveBillDetailsBack(
//     BillSubmissionResponse? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     if (success) {
//       if (showPhotoUpload) {
//         uploadBills(response?.message ?? "");
//       } else {
//         ToastManager.hideLoader();
//         ToastManager.showSuccessPopup(
//           context,
//           icSuccessIcon,
//           "Bill Submitted Successfully.",
//         );
//       }
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   uploadBills(String message) {
//     print("uploadBills");
//     Map<String, String> params = {
//       "Billid": message,
//       "createdBy": userLoginDetails?.empCode.toString() ?? "",
//       "ExpenseHead": selectedExpenseHead?.expenseHead.toString() ?? "",
//     };
//     print(params);
//     apiManager.uploadBillsAPI(
//       params,
//       selectedFile!,
//       fileType!,
//       apiUploadBillsBack,
//     );
//   }

//   void apiUploadBillsBack(
//     String? response,
//     String errorMessage,
//     bool success,
//   ) async {
//     ToastManager.hideLoader();
//     print("Result uploadBills");
//     if (success) {
//       ToastManager.showSuccessPopup(
//         context,
//         icSuccessIcon,
//         "Bill Submitted Successfully.",
//       );
//     } else {
//       ToastManager.toast(errorMessage);
//     }
//     setState(() {});
//   }

//   @override
//   Widget build(BuildContext context) {
//     SizeConfig().init(context);
//     return KeyboardDismissOnTap(
//       child: Scaffold(
//         appBar: mAppBar(
//           scTitle: "Bill Submission",
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
//                             isShowOrganizedBy == true
//                                 ? const SizedBox(height: 12)
//                                 : Container(),
//                             isShowOrganizedBy == true
//                                 ? AppDropdownTextfield(
//                                   icon: icUserIcon,
//                                   titleHeaderString: "Organized By",
//                                   valueString: organizedByName,
//                                   onTap: () {
//                                     // getTalukaAPI();
//                                   },
//                                 )
//                                 : Container(),
//                             const SizedBox(height: 12),
//                             Container(
//                               color: Colors.transparent,
//                               child: Row(
//                                 children: [
//                                   SizedBox(
//                                     width: 20,
//                                     height: 20,
//                                     child: Image.asset(icUserIcon),
//                                   ),
//                                   const SizedBox(width: 6),
//                                   Text(
//                                     "Registered Workers : ",
//                                     style: TextStyle(
//                                       fontFamily: FontConstants.interFonts,
//                                       color: kBlackColor,
//                                       fontSize: responsiveFont(14),
//                                       fontWeight: FontWeight.w500,
//                                     ),
//                                   ),
//                                   Text(
//                                     registeredWorkers,
//                                     style: TextStyle(
//                                       fontFamily: FontConstants.interFonts,
//                                       color: kBlackColor,
//                                       fontSize: responsiveFont(14),
//                                       fontWeight: FontWeight.bold,
//                                     ),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             const SizedBox(height: 12),
//                             Container(
//                               color: Colors.transparent,
//                               child: Row(
//                                 children: [
//                                   SizedBox(
//                                     width: 20,
//                                     height: 20,
//                                     child: Image.asset(icCurrencyRupeeIcon),
//                                   ),
//                                   const SizedBox(width: 6),
//                                   Text(
//                                     "Advance Approved Amount : ",
//                                     style: TextStyle(
//                                       fontFamily: FontConstants.interFonts,
//                                       color: kBlackColor,
//                                       fontSize: responsiveFont(14),
//                                       fontWeight: FontWeight.w500,
//                                     ),
//                                   ),
//                                   Text(
//                                     advanceApprovedAmount,
//                                     style: TextStyle(
//                                       fontFamily: FontConstants.interFonts,
//                                       color: kBlackColor,
//                                       fontSize: responsiveFont(14),
//                                       fontWeight: FontWeight.bold,
//                                     ),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             const SizedBox(height: 12),
//                             Text(
//                               "Note: Approved Bill Amount will be deducted from your Bill Amount",
//                               style: TextStyle(
//                                 fontFamily: FontConstants.interFonts,
//                                 color: noteRedColor,
//                                 fontSize: responsiveFont(14),
//                                 fontWeight: FontWeight.w500,
//                               ),
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
//                                     child: AppIconTextfield(
//                                       icon: icHashIcon,
//                                       titleHeaderString: "No. of Unit*",
//                                       controller: noOfUnitTextField,
//                                       textInputType: TextInputType.number,
//                                       maxLength: 1,
//                                       didChangeText: (p0) {},
//                                     ),
//                                   ),
//                                   const SizedBox(width: 12),
//                                   Expanded(
//                                     child: AppIconTextfield(
//                                       icon: icHashIcon,
//                                       titleHeaderString: "Amount/Unit*",
//                                       controller: amountUnitTextField,
//                                       textInputType: TextInputType.number,
//                                       maxLength: 4,
//                                       didChangeText: (p0) {
//                                         calculateTotalNoOfUnitToAmount(p0);
//                                       },
//                                     ),
//                                   ),
//                                 ],
//                               ),
//                             ),
//                             const SizedBox(height: 12),
//                             AppIconTextfield(
//                               icon: icCurrencyRupeeIcon,
//                               titleHeaderString: "Total*",
//                               controller: totalTextField,
//                               textInputType: TextInputType.number,
//                               enabled: false,
//                               isDisabled: true,
//                             ),
//                             const SizedBox(height: 12),
//                             AppMultilineTextField(
//                               leftIcon: icRemarkIconn,
//                               titleString: "Remark",
//                               controller: remarkTextField,
//                             ),
//                             showPhotoUpload == true
//                                 ? const SizedBox(height: 12)
//                                 : SizedBox(),
//                             showPhotoUpload == true
//                                 ? Container(
//                                   width: MediaQuery.of(context).size.width,
//                                   height: 152,
//                                   decoration: BoxDecoration(
//                                     color: Colors.white,
//                                     border: Border.all(
//                                       color: borderColor,
//                                       width: 1,
//                                     ),
//                                     borderRadius: BorderRadius.circular(8),
//                                   ),
//                                   child: Column(
//                                     mainAxisAlignment: MainAxisAlignment.center,
//                                     crossAxisAlignment:
//                                         CrossAxisAlignment.center,
//                                     children: [
//                                       GestureDetector(
//                                         onTap: () {
//                                           if (selectedFile == null) {
//                                             chooseDocumentTypeAlert();
//                                           } else {
//                                             ToastManager.showAlertDialog(
//                                               context,
//                                               "Alert",
//                                               "You already selected file",
//                                             );
//                                           }
//                                         },
//                                         child: Container(
//                                           width: 50,
//                                           height: 50,
//                                           padding: EdgeInsets.all(10),
//                                           decoration: BoxDecoration(
//                                             color: Colors.white,
//                                             border: Border.all(
//                                               color: borderColor,
//                                               width: 1,
//                                             ),
//                                             borderRadius: BorderRadius.circular(
//                                               100,
//                                             ),
//                                           ),
//                                           child: Image.asset(icCameraIcon),
//                                         ),
//                                       ),
//                                       const SizedBox(height: 8),
//                                       Text(
//                                         "Click on Camera to Upload the Bill",
//                                         style: TextStyle(
//                                           color: uploadBillTitleColor,
//                                           fontFamily: FontConstants.interFonts,
//                                           fontWeight: FontWeight.w400,
//                                           fontSize: responsiveFont(14),
//                                         ),
//                                       ),
//                                     ],
//                                   ),
//                                 )
//                                 : Container(),
//                             selectedFile == null
//                                 ? const SizedBox()
//                                 : const SizedBox(height: 12),
//                             selectedFile == null
//                                 ? Container()
//                                 : Container(
//                                   decoration: BoxDecoration(
//                                     color: attachmentBGColor,
//                                     border: Border.all(
//                                       color: attachmentBorderColor,
//                                       width: 1,
//                                     ),
//                                     borderRadius: BorderRadius.circular(8),
//                                   ),
//                                   height: 60,
//                                   padding: EdgeInsets.fromLTRB(10, 0, 10, 0),
//                                   child: Row(
//                                     children: [
//                                       SizedBox(
//                                         width: 30,
//                                         height: 30,
//                                         child: Image.asset(icPNGIcon),
//                                       ),
//                                       const SizedBox(width: 10),
//                                       Expanded(
//                                         child: Column(
//                                           mainAxisAlignment:
//                                               MainAxisAlignment.center,
//                                           crossAxisAlignment:
//                                               CrossAxisAlignment.start,
//                                           children: [
//                                             Text(
//                                               FormatterManager.getFileNameInfo(
//                                                 selectedFile,
//                                               ),
//                                               style: TextStyle(
//                                                 color: uploadBillTitleColor,
//                                                 fontFamily:
//                                                     FontConstants.interFonts,
//                                                 fontWeight: FontWeight.normal,
//                                                 fontSize: responsiveFont(14),
//                                               ),
//                                             ),
//                                             const SizedBox(height: 4),
//                                             Text(
//                                               "Size: ${FormatterManager.getFormattedFileSize(selectedFile)}",
//                                               style: TextStyle(
//                                                 color: dropDownTitleHeader,
//                                                 fontFamily:
//                                                     FontConstants.interFonts,
//                                                 fontWeight: FontWeight.normal,
//                                                 fontSize: responsiveFont(10),
//                                               ),
//                                             ),
//                                           ],
//                                         ),
//                                       ),
//                                       GestureDetector(
//                                         onTap: () {
//                                           selectedFile = null;
//                                           setState(() {});
//                                         },
//                                         child: Container(
//                                           padding: EdgeInsets.all(6),
//                                           width: 30,
//                                           height: 30,
//                                           child: Image.asset(icTrashIcon),
//                                         ),
//                                       ),
//                                     ],
//                                   ),
//                                 ),
//                             const SizedBox(height: 12),
//                             AppActiveButton(
//                               buttontitle: "Save",
//                               onTap: () {
//                                 if (validations()) {
//                                   // uploadBills("6188");
//                                   saveBillDetails();
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
