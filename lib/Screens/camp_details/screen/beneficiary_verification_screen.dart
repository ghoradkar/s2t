// ignore_for_file: file_names, use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/utilities/choose_document_manager.dart';
import 'package:s2toperational/Modules/utilities/enums.dart';
import 'package:s2toperational/Screens/camp_details/model/other_reason_for_patient_rejection_response.dart';
import 'package:s2toperational/Screens/camp_details/model/test_list_for_reject_response.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/AppTextField.dart';
import 'package:s2toperational/Modules/common_widgets/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Modules/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/Modules/common_widgets/S2TYesNoAlertView.dart';
import '../controller/beneficiary_verification_controller.dart';
import '../model/beneficiary_worker_response.dart';
import '../widget/audio_screening_test_info_widget.dart';
import '../widget/beneficiary_verification_info_widget.dart';
import '../widget/blood_pressure_sugar_info_widget.dart';
import '../widget/edit_beneficiary_info_widget.dart';
import '../widget/lung_function_test_info_widget.dart';
import '../widget/reject_test_in_camp_info_widget.dart';
import '../widget/vision_screening_test_info_widget.dart';

class BeneficiaryVerificationScreen extends StatelessWidget {
  const BeneficiaryVerificationScreen({super.key, required this.obj});

  final BeneficiaryWorkerOutput obj;

  @override
  Widget build(BuildContext context) {
    Get.put(BeneficiaryVerificationController(obj: obj));
    SizeConfig().init(context);
    return GetBuilder<BeneficiaryVerificationController>(
      builder: (ctrl) {
        if (ctrl.shouldShowServerError) {
          ctrl.shouldShowServerError = false;
          WidgetsBinding.instance.addPostFrameCallback((_) {
            if (context.mounted) {
              ToastManager.showAlertDialog(
                context,
                'Server not responding',
                () => Navigator.of(context).pop(),
                title: 'Please Try Again',
              );
            }
          });
        }
        if (ctrl.shouldShowAlerts) {
          ctrl.shouldShowAlerts = false;
          WidgetsBinding.instance.addPostFrameCallback((_) {
            if (context.mounted) ctrl.alertManager.showAlertMessages(context);
          });
        }
        return KeyboardDismissOnTap(
          child: Scaffold(
            appBar: mAppBar(
              scTitle: 'Beneficiary Verification',
              leadingIcon: iconBackArrow,
              onLeadingIconClick: () => Get.back(),
            ),
            body: AnnotatedRegion(
              value: const SystemUiOverlayStyle(
                statusBarColor: kPrimaryColor,
                statusBarBrightness: Brightness.light,
                statusBarIconBrightness: Brightness.light,
              ),
              child: Container(
                color: Colors.white,
                height: SizeConfig.screenHeight,
                width: SizeConfig.screenWidth,
                child: SingleChildScrollView(
                  child: Column(
                    children: [
                      BeneficiaryVerificationInfoScreen(
                        patientCheckupAnalysisReportOutput: ctrl.patientCheckupAnalysisReportOutput,
                        obj: ctrl.obj,
                        selectedBeneficiaryFile: ctrl.selectedBeneficiaryFile,
                        selectedCardFile: ctrl.selectedCardFile,
                        onBeneficiaryImage: () => _chooseDocumentTypeAlert(context, ctrl, true),
                        onCardImage: () => _chooseDocumentTypeAlert(context, ctrl, false),
                      ),
                      EditBeneficiaryInfoScreen(beneficiaryWorkerOutput: ctrl.obj),
                      AudioScreeningTestInfoScreen(remark: ctrl.remark, rightRemark: ctrl.rightRemark),
                      VisionScreeningTestInfoScreen(visionScreeningDetailsOutput: ctrl.visionScreeningDetailsOutput),
                      BloodPressureAndSugarInfoScreen(visionScreeningDetailsOutput: ctrl.visionScreeningDetailsOutput),
                      LungFunctionTestInfoScreen(lungFunctionTest: ctrl.lungFunctionTestDetailsOutput),
                      RejectTestInCampInfoScreen(
                        testToRejectID: ctrl.testToRejectID,
                        testToRejectString: ctrl.testToRejectString,
                        reasonId: ctrl.reasonId,
                        reasonDescription: ctrl.reasonDescription,
                        isUserInteractionEnabled: ctrl.isUserInteractionEnabled,
                        otherDescription: ctrl.obj.otherDescription ?? '',
                        otherReasonTextField: ctrl.otherReasonTextField,
                        onTestToRejectTap: () => _openTestToRejectSheet(context, ctrl),
                        onReasonTap: () => _openReasonSheet(context, ctrl),
                      ),
                      ctrl.isShowPhlebotomistName ? const SizedBox(height: 10) : const SizedBox.shrink(),
                      ctrl.isShowPhlebotomistName
                          ? Row(
                            children: [
                              Expanded(
                                child: AppTextField(
                                  readOnly: false,
                                  controller: TextEditingController(text: ctrl.obj.phleboName),
                                  inputStyle: TextStyle(fontFamily: FontConstants.interFonts, fontSize: 14),
                                  label: RichText(
                                    text: TextSpan(
                                      text: 'Phlebotomist Name*',
                                      style: TextStyle(
                                        fontFamily: FontConstants.interFonts,
                                        color: kLabelTextColor,
                                        fontSize: responsiveFont(14),
                                        fontWeight: FontWeight.w400,
                                      ),
                                    ),
                                  ),
                                  labelStyle: TextStyle(
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w400,
                                    fontSize: responsiveFont(14),
                                  ),
                                  prefixIcon: Image.asset(icMapPin, scale: 4.0),
                                ),
                              ),
                              const SizedBox(width: 10),
                              SizedBox(width: 30, height: 30, child: Image.asset(icPhoneCallGreenIcon)),
                              const SizedBox(width: 10),
                            ],
                          )
                          : const SizedBox.shrink(),
                      const SizedBox(height: 10),
                      Container(
                        width: SizeConfig.screenWidth,
                        height: 40,
                        color: Colors.transparent,
                        child: Row(
                          children: [
                            ctrl.isShowDeny
                                ? Expanded(
                                  child: GestureDetector(
                                    onTap: () {
                                      if (ctrl.denyValidation()) {
                                        _showDenyPopup(context, ctrl);
                                      }
                                    },
                                    child: Container(
                                      decoration: BoxDecoration(
                                        color: Colors.orange,
                                        borderRadius: BorderRadius.circular(8),
                                      ),
                                      child: Center(
                                        child: Text(
                                          'Deny',
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily: FontConstants.interFonts,
                                            fontWeight: FontWeight.w600,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ),
                                    ),
                                  ),
                                )
                                : const SizedBox.shrink(),
                            const SizedBox(width: 10),
                            ctrl.isShowApprove
                                ? Expanded(
                                  child: GestureDetector(
                                    onTap: () {
                                      if (ctrl.approveValidation()) {
                                        _showApprovePopup(context, ctrl);
                                      }
                                    },
                                    child: Container(
                                      decoration: BoxDecoration(
                                        color: Colors.green,
                                        borderRadius: BorderRadius.circular(8),
                                      ),
                                      child: Center(
                                        child: Text(
                                          'Approve',
                                          style: TextStyle(
                                            color: Colors.black,
                                            fontFamily: FontConstants.interFonts,
                                            fontWeight: FontWeight.w600,
                                            fontSize: responsiveFont(16),
                                          ),
                                        ),
                                      ),
                                    ),
                                  ),
                                )
                                : const SizedBox.shrink(),
                          ],
                        ),
                      ),
                      SizedBox(height: 30 + MediaQuery.of(context).viewPadding.bottom),
                    ],
                  ).paddingSymmetric(vertical: 10, horizontal: 12),
                ),
              ),
            ),
          ),
        );
      },
    );
  }

  void _chooseDocumentTypeAlert(BuildContext context, BeneficiaryVerificationController ctrl, bool isBeneficiary) {
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text('Select Photo'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            TextButton(
              child: const Text('Take a Photo'),
              onPressed: () {
                Navigator.pop(context);
                _handleFilePick(FileSourceType.camera, isBeneficiary, ctrl);
              },
            ),
            TextButton(
              child: const Text('Choose from Photo Library'),
              onPressed: () {
                Navigator.pop(context);
                _handleFilePick(FileSourceType.gallery, isBeneficiary, ctrl);
              },
            ),
          ],
        ),
        actions: [
          TextButton(
            child: const Text('Cancel'),
            onPressed: () => Navigator.pop(context),
          ),
        ],
      ),
    );
  }

  Future<void> _handleFilePick(FileSourceType type, bool isBeneficiary, BeneficiaryVerificationController ctrl) async {
    final result = await ChooseDocumentManager.pickFile(type);
    if (result != null) {
      if (isBeneficiary) {
        ctrl.selectedBeneficiaryFile = result.file;
      } else {
        ctrl.selectedCardFile = result.file;
      }
      ctrl.update();
      await ctrl.uploadPhoto(result.file, isBeneficiary);
    }
  }

  Future<void> _openTestToRejectSheet(BuildContext context, BeneficiaryVerificationController ctrl) async {
    final tests = await ctrl.fetchTestsToReject();
    if (tests.isEmpty || !context.mounted) return;
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (_) => Container(
        width: double.infinity,
        height: MediaQuery.of(context).size.width * 1.33,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(topLeft: Radius.circular(20), topRight: Radius.circular(20)),
        ),
        child: DropDownListScreen(
          titleString: 'Test to Reject',
          dropDownList: tests,
          dropDownMenu: DropDownTypeMenu.TestToReject,
          onApplyTap: (p0) => ctrl.selectTestToReject(p0 as TestListForRejectOutput),
        ),
      ),
    ).whenComplete(() => ctrl.update());
  }

  Future<void> _openReasonSheet(BuildContext context, BeneficiaryVerificationController ctrl) async {
    final reasons = await ctrl.fetchRejectionReasons();
    if (reasons.isEmpty || !context.mounted) return;
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (_) => Container(
        width: double.infinity,
        height: MediaQuery.of(context).size.width * 1.33,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(topLeft: Radius.circular(20), topRight: Radius.circular(20)),
        ),
        child: DropDownListScreen(
          titleString: 'Reason',
          dropDownList: reasons,
          dropDownMenu: DropDownTypeMenu.ReasonTest,
          onApplyTap: (p0) => ctrl.selectRejectionReason(p0 as OtherReasonOutput),
        ),
      ),
    ).whenComplete(() => ctrl.update());
  }

  void _showApprovePopup(BuildContext context, BeneficiaryVerificationController ctrl) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (sheetContext) => Container(
        width: double.infinity,
        height: MediaQuery.of(sheetContext).size.height,
        color: Colors.transparent,
        child: S2TYesNoAlertView(
          icon: icApproveIcon,
          message: 'Are you sure you want to Approve Beneficiary Verification ?',
          onYesTap: () async {
            Navigator.pop(context);
            final ok = await ctrl.submitApproveOrDeny(isApproved: '1');
            if (ok && context.mounted) {
              ToastManager.showSuccessPopup(
                context,
                icSuccessIcon,
                'Approved successfully',
                () { Get.back(); Get.back(); },
              );
            }
          },
          onNoTap: () => Navigator.pop(context),
        ),
      ),
    );
  }

  void _showDenyPopup(BuildContext context, BeneficiaryVerificationController ctrl) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (sheetContext) => Container(
        width: double.infinity,
        height: MediaQuery.of(sheetContext).size.height,
        color: Colors.transparent,
        child: S2TYesNoAlertView(
          icon: icDeniedIcon,
          message: 'Are you sure you want to Deny Beneficiary Verification ?',
          onYesTap: () async {
            Navigator.pop(context);
            final ok = await ctrl.submitApproveOrDeny(isApproved: '2');
            if (ok && context.mounted) {
              ToastManager.showSuccessPopup(
                context,
                icSuccessIcon,
                'Denied successfully',
                () { Get.back(); Get.back(); },
              );
            }
          },
          onNoTap: () => Navigator.pop(context),
        ),
      ),
    );
  }
}
