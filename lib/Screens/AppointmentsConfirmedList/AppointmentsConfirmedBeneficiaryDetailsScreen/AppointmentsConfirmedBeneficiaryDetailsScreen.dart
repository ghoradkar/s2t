// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppDropdownTextfield.dart';
import 'package:s2toperational/Screens/patient_registration/controller/d2d_select_camp_controller.dart';
import 'package:s2toperational/Screens/patient_registration/screen/d2d_select_camp_screen.dart';

import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/Json_Class/AppoinmentExpectedBeneficiariesResponse/AppoinmentExpectedBeneficiariesResponse.dart';
import '../../../Modules/Json_Class/BeneficiariesDetailsResponse/BeneficiariesDetailsResponse.dart';
import '../../../Modules/Json_Class/CallStatusListResponse/CallStatusListResponse.dart';
import '../../../Modules/Json_Class/RemarkListResponse/RemarkListResponse.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppActiveButton.dart';
import '../../../Modules/widgets/AppDateTextfield.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../../../Views/DropDownListScreen/DropDownListScreen.dart';
import 'AppointmentConfirmScreenedBenefiView/AppointmentConfirmScreenedBenefiView.dart';
import 'ScreeningDetailsView/ScreeningDetailsAppointConfimedView.dart';

class AppointmentsConfirmedBeneficiaryDetailsScreen extends StatefulWidget {
  AppointmentsConfirmedBeneficiaryDetailsScreen({
    super.key,
    required this.selectedBeneficiary,
    this.isPatientRegistration = false,
  });

  AppoinmentExpectedBeneficiariesOutput selectedBeneficiary;
  final bool isPatientRegistration;
  @override
  State<AppointmentsConfirmedBeneficiaryDetailsScreen> createState() =>
      _AppointmentsConfirmedBeneficiaryDetailsScreenState();
}

class _AppointmentsConfirmedBeneficiaryDetailsScreenState
    extends State<AppointmentsConfirmedBeneficiaryDetailsScreen> {
  APIManager apiManager = APIManager();

  CallStatusListOutput? selectedCallStatus;
  BeneficiariesDetailsOutput? beneficiaryDetails;

  String selectedDate = "";
  String selectedTime = "";
  RemarkListOutput? selectedRemark;

  int empCode = 0;
  @override
  void initState() {
    super.initState();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    selectedCallStatus = CallStatusListOutput(
      assignStatusID: 2,
      callingStatus: "Booking Confirmed",
    );
    getBeneficiaryData();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Beneficiary Details',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: AnnotatedRegion(
          value: const SystemUiOverlayStyle(
            statusBarColor: kPrimaryColor,
            statusBarBrightness: Brightness.light,
            statusBarIconBrightness: Brightness.light,
          ),
          child: SingleChildScrollView(
            child: Column(
              children: [
                Container(
                  width: SizeConfig.screenWidth,
                  padding: EdgeInsets.all(10),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(10),
                    boxShadow: [
                      BoxShadow(
                        offset: Offset(0, 1),
                        color: Colors.black.withValues(alpha: 0.15),
                        spreadRadius: 0,
                        blurRadius: 10,
                      ),
                    ],
                  ),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        widget.selectedBeneficiary.beneficiaryName ??
                            "",
                        style: TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.w600,
                          color: Colors.black,
                        ),
                      ),
                      const SizedBox(height: 5),
                      Row(
                        children: [
                          SizedBox(
                            width: 20,
                            height: 20,
                            child: Image.asset(icCalendarMonth),
                          ),
                          const SizedBox(width: 5),
                          Text(
                            "Age :",
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.w500,
                              color: Colors.black,
                            ),
                          ),
                          const SizedBox(width: 5),
                          Text(
                            "${widget.selectedBeneficiary.age ?? ""} Years",
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.w500,
                              color: dropDownTitleHeader,
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 10),
                      Padding(
                        padding: const EdgeInsets.fromLTRB(
                          10,
                          10,
                          10,
                          0,
                        ),
                        child: AppDropdownTextfield(
                          icon: icPhoneCallIcon,
                          titleHeaderString: "Call Status*",
                          valueString:
                          selectedCallStatus?.callingStatus ?? "",
                          onTap: () {
                            getPhleboCallStatusList();
                          },
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 10),
                ScreeningDetailsAppointConfimedView(
                  selectedBeneficiary: widget.selectedBeneficiary,
                ),
                AppointmentConfirmScreenedBenefiView(
                  selectedBeneficiary: widget.selectedBeneficiary,
                ),
                const SizedBox(height: 10),
                Row(
                  children: [
                    Expanded(
                      child: AppDateTextfield(
                        icon: icCalendarMonth,
                        titleHeaderString: "Date",
                        valueString:
                        beneficiaryDetails?.appoinmentDate ?? " ",
                        onTap: () {},
                      ),
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: AppDateTextfield(
                        icon: iconClock,
                        titleHeaderString: "Time",
                        valueString:
                        beneficiaryDetails?.appoinmentTime ?? " ",
                        onTap: () {},
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 10),
                AppDropdownTextfield(
                  icon: iconFile,
                  titleHeaderString: "Remark*",
                  valueString: selectedRemark?.callingRemark ?? "",
                  onTap: () {
                    getCallingRemarkV1();
                  },
                ),
                const SizedBox(height: 10),
                SizedBox(
                  width: MediaQuery.of(context).size.width,
                  child: Row(
                    children: [
                      SizedBox(
                        width: 130,
                        child: AppActiveButton(
                          buttontitle: "Save",
                          isCancel: true,
                          onTap: () {},
                        ),
                      ),

                      const SizedBox(width: 12),
                      Expanded(
                        child: AppActiveButton(
                          buttontitle: "Register Beneficiary",
                          onTap: () {
                            if (widget.isPatientRegistration) {
                              _showRegisterBeneficiaryAlert();
                            }
                          },
                        ),
                      ),
                    ],
                  ),
                ),
                const SizedBox(height: 10),
              ],
            ).paddingSymmetric(vertical: 10,horizontal: 10),
          ),
        ),
      ),
    );
  }


  void _showRegisterBeneficiaryAlert() {
    final beneficiaryNo = widget.selectedBeneficiary.beneficiaryNo ?? '';
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (dialogContext) {
        return AlertDialog(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12.r),
          ),
          contentPadding: EdgeInsets.fromLTRB(20.w, 20.h, 20.w, 0),
          actionsPadding: EdgeInsets.fromLTRB(16.w, 0, 16.w, 16.h),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'नोंद',
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 16.sp,
                  fontWeight: FontWeight.w700,
                  color: Colors.black,
                ),
              ),
              SizedBox(height: 10.h),
              Text(
                'कृपया खालील लाभार्थी नोंदणी क्रमांक नोंद करा किंवा कॉपी करा. '
                'पेशंट नोंदणी स्क्रीनमध्ये Beneficiary Reg No. फील्डमध्ये हा क्रमांक वापरा.',
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 13.sp,
                  color: Colors.black87,
                ),
              ),
              SizedBox(height: 12.h),
              Container(
                width: double.infinity,
                padding:
                    EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
                decoration: BoxDecoration(
                  color: Colors.grey.shade100,
                  borderRadius: BorderRadius.circular(8.r),
                  border: Border.all(color: Colors.grey.shade300),
                ),
                child: Text(
                  beneficiaryNo,
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 15.sp,
                    fontWeight: FontWeight.w700,
                    color: Colors.black,
                    letterSpacing: 0.5,
                  ),
                  textAlign: TextAlign.center,
                ),
              ),
              SizedBox(height: 16.h),
            ],
          ),
          actions: [
            Row(
              children: [
                Expanded(
                  child: OutlinedButton(
                    onPressed: () {
                      Clipboard.setData(ClipboardData(text: beneficiaryNo));
                      ToastManager.toast('Copied to clipboard');
                    },
                    style: OutlinedButton.styleFrom(
                      side: const BorderSide(color: kPrimaryColor),
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8.r),
                      ),
                      padding: EdgeInsets.symmetric(vertical: 12.h),
                    ),
                    child: Text(
                      'Copy',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: 14.sp,
                        fontWeight: FontWeight.w600,
                        color: kPrimaryColor,
                      ),
                    ),
                  ),
                ),
                SizedBox(width: 12.w),
                Expanded(
                  child: ElevatedButton(
                    onPressed: () {
                      Navigator.pop(dialogContext);
                      Get.delete<D2DSelectCampController>(force: true);
                      final sc = Get.put(D2DSelectCampController());
                      sc.navBeneficiaryNo = beneficiaryNo.toString();
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => const D2DSelectCampScreen(),
                        ),
                      );
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: kPrimaryColor,
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(8.r),
                      ),
                      padding: EdgeInsets.symmetric(vertical: 12.h),
                    ),
                    child: Text(
                      'Next',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: 14.sp,
                        fontWeight: FontWeight.w600,
                        color: Colors.white,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ],
        );
      },
    );
  }

  void getBeneficiaryData() {
    Map<String, String> params = {
      "AssignCallID": widget.selectedBeneficiary.assignCallID.toString(),
    };
    apiManager.getBeneficiaryDataAPI(params, apiBeneficiaryDataBack);
  }

  void apiBeneficiaryDataBack(
      BeneficiariesDetailsResponse? response,
      String errorMessage,
      bool success,
      ) async {
    ToastManager.hideLoader();

    if (success) {
      beneficiaryDetails = response?.output?.first;
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }

  void getPhleboCallStatusList() {
    // Call the API to get the list of phlebo call statuses
    // This is a placeholder for the actual API call
    // You can replace it with your API call logic

    ToastManager.showLoader();
    Map<String, String> params = {
      "AssignCallID": widget.selectedBeneficiary.assignCallID.toString(),
    };
    apiManager.getPhleboCallStatusListAPI(params, apiTPhleboCallStatusListBack);
  }

  void apiTPhleboCallStatusListBack(
      CallStatusListResponse? response,
      String errorMessage,
      bool success,
      ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Select Call Status",
        response?.output ?? [],
        DropDownTypeMenu.CallStatus,
      );
    } else {
      ToastManager.toast(errorMessage);
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
              if (dropDownType == DropDownTypeMenu.CallStatus) {
                selectedCallStatus = p0;
              } else if (dropDownType == DropDownTypeMenu.CallingRemark) {
                selectedRemark = p0 as RemarkListOutput;
                // Handle the selected remark
                // You can store it or use it as needed
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

  void getCallingRemarkV1() {
    ToastManager.showLoader();
    Map<String, String> params = {
      "CallStatusID": selectedCallStatus?.assignStatusID.toString() ?? "0",
    };
    apiManager.getCallingRemarkV1API(params, apiCallingRemarkBack);
  }

  void apiCallingRemarkBack(
      RemarkListResponse? response,
      String errorMessage,
      bool success,
      ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Select Remark",
        response?.output ?? [],
        DropDownTypeMenu.CallingRemark,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void saveData() {
    // Implement the logic to save the data
    // This is a placeholder for the actual save logic

    String callStatus = selectedCallStatus?.callingStatus ?? "";
    String appointmentDate = selectedDate;
    String appointmentTime = selectedTime;
    String remark = selectedRemark?.callingRemark ?? "";

    if (callStatus.isEmpty) {
      ToastManager.toast("Please select call status");
      return;
    }
    if (appointmentDate.isEmpty) {
      ToastManager.toast("Please select appointment date");
      return;
    }
    if (appointmentTime.isEmpty) {
      ToastManager.toast("Please select appointment time");
      return;
    }
    if (remark.isEmpty || remark.toLowerCase() == "NA".toLowerCase()) {
      ToastManager.toast("Please select remark");
      return;
    }

    Map<String, String> params = {
      "AssignCallID": widget.selectedBeneficiary.assignCallID.toString(),
      "CallStatusID": selectedCallStatus?.assignStatusID.toString() ?? "0",
      "AppoinmentDate": appointmentDate,
      "AppoinmentTime": appointmentTime,
      "PhleboRemark": selectedRemark?.callingRemark ?? "",
      "PhleboRemarkID": selectedRemark?.cReamrkID.toString() ?? "0",
      "CReatedBy": empCode.toString(),
    };

    apiManager.updateAppointmentDetailsMobAPI(params, (
        response,
        errorMessage,
        success,
        ) {
      ToastManager.hideLoader();
      if (success) {
        ToastManager.toast("Details saved successfully");
        Navigator.pop(context);
      } else {
        if (errorMessage == "20 appointments are already booked") {
          ToastManager.toast(
            "20 appointments are already booked $appointmentDate for this team .Please book an appointment for another date.",
          );
        } else {
          ToastManager.toast(errorMessage);
        }
      }
    });
  }

}
