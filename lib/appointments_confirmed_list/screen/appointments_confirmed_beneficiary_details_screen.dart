// ignore_for_file: file_names, avoid_print, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/appointments_confirmed_list/model/call_status_list_response.dart';
import 'package:s2toperational/appointments_confirmed_list/model/remark_list_response.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/app_active_button.dart';
import 'package:s2toperational/common_widgets/AppDateTextfield.dart';
import 'package:s2toperational/common_widgets/AppDropdownTextfield.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/common_widgets/drop_down_list_screen/drop_down_list_screen.dart';
import 'package:s2toperational/utilities/enums.dart';
import 'package:s2toperational/patient_registration/controller/d2d_select_camp_controller.dart';
import 'package:s2toperational/patient_registration/screen/d2d_select_camp_screen.dart';
import '../controller/beneficiary_details_controller.dart';
import '../widget/appointment_confirm_screened_benefi_view.dart';
import '../widget/screening_details_appoint_confimed_view.dart';

class AppointmentsConfirmedBeneficiaryDetailsScreen extends StatelessWidget {
  const AppointmentsConfirmedBeneficiaryDetailsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    Get.put(BeneficiaryDetailsController());
    SizeConfig().init(context);
    return GetBuilder<BeneficiaryDetailsController>(
      builder: (ctrl) => KeyboardDismissOnTap(
        child: Scaffold(
          appBar: mAppBar(
            scTitle: 'Beneficiary Details',
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Get.back(),
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
                    padding: const EdgeInsets.all(10),
                    decoration: BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.circular(10),
                      boxShadow: [
                        BoxShadow(
                          offset: const Offset(0, 1),
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
                          ctrl.selectedBeneficiary?.beneficiaryName ?? '',
                          style: const TextStyle(
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
                            const Text(
                              'Age :',
                              style: TextStyle(
                                fontSize: 14,
                                fontWeight: FontWeight.w500,
                                color: Colors.black,
                              ),
                            ),
                            const SizedBox(width: 5),
                            Text(
                              '${ctrl.selectedBeneficiary?.age ?? ''} Years',
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
                          padding: const EdgeInsets.fromLTRB(10, 10, 10, 0),
                          child: AppDropdownTextfield(
                            icon: icPhoneCallIcon,
                            titleHeaderString: 'Call Status*',
                            valueString: ctrl.selectedCallStatus?.callingStatus ?? '',
                            onTap: () => _showCallStatusSheet(context, ctrl),
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 10),
                  ScreeningDetailsAppointConfimedView(
                    selectedBeneficiary: ctrl.selectedBeneficiary!,
                  ),
                  AppointmentConfirmScreenedBenefiView(
                    selectedBeneficiary: ctrl.selectedBeneficiary!,
                  ),
                  const SizedBox(height: 10),
                  Row(
                    children: [
                      Expanded(
                        child: AppDateTextfield(
                          icon: icCalendarMonth,
                          titleHeaderString: 'Date',
                          valueString: ctrl.beneficiaryDetails?.appoinmentDate ?? ' ',
                          onTap: () {},
                        ),
                      ),
                      const SizedBox(width: 8),
                      Expanded(
                        child: AppDateTextfield(
                          icon: iconClock,
                          titleHeaderString: 'Time',
                          valueString: ctrl.beneficiaryDetails?.appoinmentTime ?? ' ',
                          onTap: () {},
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 10),
                  AppDropdownTextfield(
                    icon: iconFile,
                    titleHeaderString: 'Remark*',
                    valueString: ctrl.selectedRemark?.callingRemark ?? '',
                    onTap: () => _showRemarkSheet(context, ctrl),
                  ),
                  const SizedBox(height: 10),
                  SizedBox(
                    width: MediaQuery.of(context).size.width,
                    child: Row(
                      children: [
                        SizedBox(
                          width: 130,
                          child: AppActiveButton(
                            buttontitle: 'Save',
                            isCancel: true,
                            onTap: () async {
                              final ok = await ctrl.saveData();
                              if (ok) {
                                ToastManager.toast('Details saved successfully');
                                Get.back();
                              }
                            },
                          ),
                        ),
                        const SizedBox(width: 12),
                        Expanded(
                          child: AppActiveButton(
                            buttontitle: 'Register Beneficiary',
                            onTap: () {
                              if (ctrl.isPatientRegistration) {
                                _showRegisterBeneficiaryAlert(context, ctrl);
                              }
                            },
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 10),
                ],
              ).paddingSymmetric(vertical: 10, horizontal: 10),
            ),
          ),
        ),
      ),
    );
  }

  void _showRegisterBeneficiaryAlert(
    BuildContext context,
    BeneficiaryDetailsController ctrl,
  ) {
    final beneficiaryNo = ctrl.selectedBeneficiary?.beneficiaryNo ?? '';
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
                padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
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
                      sc.navBeneficiaryNo = beneficiaryNo;
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

  Future<void> _showCallStatusSheet(
    BuildContext context,
    BeneficiaryDetailsController ctrl,
  ) async {
    final list = await ctrl.fetchCallStatusList();
    if (list.isEmpty) return;
    _showDropDownSheet(
      context,
      'Select Call Status',
      list,
      DropDownTypeMenu.CallStatus,
      (p0) => ctrl.setCallStatus(p0 as CallStatusListOutput),
    );
  }

  Future<void> _showRemarkSheet(
    BuildContext context,
    BeneficiaryDetailsController ctrl,
  ) async {
    final list = await ctrl.fetchRemarkList();
    if (list.isEmpty) return;
    _showDropDownSheet(
      context,
      'Select Remark',
      list,
      DropDownTypeMenu.CallingRemark,
      (p0) => ctrl.setRemark(p0 as RemarkListOutput),
    );
  }

  void _showDropDownSheet(
    BuildContext context,
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
    Function(dynamic) onApply,
  ) {
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
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: onApply,
          ),
        );
      },
    );
  }
}
