// ignore_for_file: use_build_context_synchronously

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/app_active_button.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/ct_assignment/model/beneficiary_details_for_assign_teamid_details_response.dart';
import 'package:s2toperational/ct_assignment/screen/ct_sample_collection_screen.dart';
import 'package:s2toperational/appointment_sample_collection_ct/controller/ct_appointment_confirmation_controller.dart';
import 'package:s2toperational/appointment_sample_collection_ct/models/ct_appointment_beneficiary_model.dart';
import 'package:s2toperational/calling_modules/widgets/network_wrapper.dart';
import 'package:url_launcher/url_launcher.dart';

class CTAppointmentConfirmationScreen extends StatefulWidget {
  final String beneficiaryName;
  final String regNo;
  final String mobile;
  final String status;

  const CTAppointmentConfirmationScreen({
    super.key,
    required this.beneficiaryName,
    required this.regNo,
    required this.mobile,
    required this.status,
  });

  @override
  State<CTAppointmentConfirmationScreen> createState() =>
      _CTAppointmentConfirmationScreenState();
}

class _CTAppointmentConfirmationScreenState
    extends State<CTAppointmentConfirmationScreen> {
  late final CTAppointmentConfirmationController _c;

  @override
  void initState() {
    super.initState();
    Get.delete<CTAppointmentConfirmationController>(force: true);
    _c = Get.put(
      CTAppointmentConfirmationController(
        beneficiaryName: widget.beneficiaryName,
        regNo: widget.regNo,
        mobile: widget.mobile,
        status: widget.status,
      ),
    );
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (mounted) _c.loadBeneficiaryDetails();
      if (mounted && widget.status == '4') _showNotInterestedAlert();
    });
  }

  @override
  void dispose() {
    Get.delete<CTAppointmentConfirmationController>(force: true);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Appointment Confirmation',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: GetBuilder<CTAppointmentConfirmationController>(
          builder: (c) {
            if (c.isLoading) {
              return const Center(child: CircularProgressIndicator());
            }
            return SingleChildScrollView(
              padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 12.h),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Note text
                  Text(
                    '  Note : Click on beneficiary name for sample collection',
                    style: TextStyle(
                      fontSize: 11.sp,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w600,
                      color: Colors.red,
                    ),
                  ),
                  SizedBox(height: 8.h),

                  // Dependents table
                  if (c.dependentList.isNotEmpty) ...[
                    _buildTableHeader(),
                    ...c.dependentList.map((dep) => _buildTableRow(dep)),
                    SizedBox(height: 12.h),
                  ],

                  // Call button
                  SizedBox(
                    width: double.infinity,
                    child: OutlinedButton.icon(
                      onPressed: () => _callBeneficiary(widget.mobile),
                      icon: const Icon(Icons.phone),
                      label: Text(
                        'Call To Beneficiary',
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: 14.sp,
                        ),
                      ),
                      style: OutlinedButton.styleFrom(
                        foregroundColor: kPrimaryColor,
                        side: BorderSide(color: kPrimaryColor),
                        padding: EdgeInsets.symmetric(vertical: 12.h),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8),
                        ),
                      ),
                    ),
                  ),
                  SizedBox(height: 14.h),

                  // Location fields
                  _readOnlyField(
                    controller: c.districtController,
                    label: 'District',
                  ),
                  SizedBox(height: 10.h),
                  _readOnlyField(
                    controller: c.areaController,
                    label: 'Area / Taluka',
                  ),
                  SizedBox(height: 10.h),
                  _readOnlyField(
                    controller: c.addressController,
                    label: 'Address',
                    maxLines: 2,
                  ),
                  SizedBox(height: 10.h),

                  // Remark + Appointment Date (disabled if status==4)
                  AbsorbPointer(
                    absorbing: c.isControlsDisabled,
                    child: Opacity(
                      opacity: c.isControlsDisabled ? 0.45 : 1.0,
                      child: Column(
                        children: [
                          AppTextField(
                            controller: c.remarkController,
                            readOnly: true,
                            onTap: () => c.fetchRemarks(),
                            label: CommonText(
                              text: 'Remark *',
                              fontSize: 12.sp,
                              fontWeight: FontWeight.w400,
                              textColor: kTextColor,
                              textAlign: TextAlign.start,
                            ),
                            hint: 'Select Remark',
                            hintStyle: TextStyle(
                              fontSize: 12.sp,
                              fontFamily: FontConstants.interFonts,
                            ),
                            suffixIcon: Icon(
                              Icons.arrow_drop_down,
                              color: kPrimaryColor,
                            ),
                            fieldRadius: 8,
                          ),
                          if (c.showAppointmentDate) ...[
                            SizedBox(height: 10.h),
                            AppTextField(
                              controller: c.appointmentDateController,
                              readOnly: true,
                              onTap: () => c.pickAppointmentDate(context),
                              label: CommonText(
                                text: 'Appointment Date',
                                fontSize: 12.sp,
                                fontWeight: FontWeight.w400,
                                textColor: kTextColor,
                                textAlign: TextAlign.start,
                              ),
                              hint: 'dd-MM-yyyy',
                              hintStyle: TextStyle(
                                fontSize: 12.sp,
                                fontFamily: FontConstants.interFonts,
                              ),
                              suffixIcon: Icon(
                                Icons.calendar_today,
                                size: 16.sp,
                                color: kPrimaryColor,
                              ),
                              fieldRadius: 8,
                            ),
                          ],
                        ],
                      ),
                    ),
                  ),
                  SizedBox(height: 20.h),

                  // Confirm button
                  SizedBox(
                    width: double.infinity,
                    height: 46.h,
                    child: AppActiveButton(
                      buttontitle: 'Confirm',
                      onTap: () => _c.submit(context),
                    ),
                  ),
                  SizedBox(height: 20.h),
                ],
              ),
            );
          },
        ),
      ),
    );
  }

  Widget _buildTableHeader() {
    return Container(
      decoration: BoxDecoration(
        color: kPrimaryColor,
        borderRadius: const BorderRadius.vertical(top: Radius.circular(6)),
      ),
      child: Row(
        children: [

          Expanded(
            flex: 3,
            child: Padding(
              padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 8.h),
              child: Text(
                'Beneficiary Name',
                style: TextStyle(
                  fontSize: 12.sp,
                  fontFamily: FontConstants.interFonts,
                  fontWeight: FontWeight.w600,
                  color: Colors.white,
                ),
              ),
            ),
          ),
          Container(width: 1, height: 36.h, color: Colors.white30),
          Expanded(
            flex: 2,
            child: Padding(
              padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 8.h),
              child: Text(
                'Relation With Worker',
                style: TextStyle(
                  fontSize: 12.sp,
                  fontFamily: FontConstants.interFonts,
                  fontWeight: FontWeight.w600,
                  color: Colors.white,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  void _showNotInterestedAlert() {
    ToastManager.showAlertDialog(
      context,
      'You are not allowed to change the status as the beneficiary is not interested in CT',
      () => Get.back(),
    );
  }

  void _openSampleCollection(CTAppointmentBeneficiaryOutput dep) {
    if (widget.status == '4') {
      _showNotInterestedAlert();
      return;
    }
    final details = BeneficiaryDetailsforAssignTeamidOutput(
      regdno: dep.regdNo,
      regdid: int.tryParse(dep.regdId ?? '') ?? 0,
      beneficiaryName: dep.beneficiaryName,
      t2tOrderId: int.tryParse(dep.t2tOrderId ?? '') ?? 0,
      dISTLGDCODE: dep.distLgdCode ?? 0,
      sampleCollection: dep.sampleCollection,
      arId: int.tryParse(dep.arId ?? ''),
    );
    Navigator.push(
      context,
      MaterialPageRoute(
        builder:
            (_) => CTSampleCollectionScreen(
              beneficiaryDetails: details,
              isAppointmentFlow: true,
            ),
      ),
    ).then((submitted) {
      if (submitted == true) _c.reloadData();
    });
  }

  Widget _buildTableRow(CTAppointmentBeneficiaryOutput dep) {
    final isSampleDone = (dep.sampleCollection ?? '').toUpperCase() == 'Y';
    return GestureDetector(
      onTap: () => _openSampleCollection(dep),
      child: IntrinsicHeight(
        child: Container(
          decoration: BoxDecoration(
            color: isSampleDone ? Colors.green[50] : Colors.white,
            border: Border(
              left: BorderSide(color: Colors.grey.withValues(alpha: 0.3)),
              right: BorderSide(color: Colors.grey.withValues(alpha: 0.3)),
              bottom: BorderSide(color: Colors.grey.withValues(alpha: 0.3)),
            ),
          ),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Expanded(
                flex: 3,
                child: Padding(
                  padding: EdgeInsets.symmetric(
                    horizontal: 10.w,
                    vertical: 8.h,
                  ),
                  child: Text(
                    dep.beneficiaryName ?? 'N/A',
                    style: TextStyle(
                      fontSize: 12.sp,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w500,
                      color: isSampleDone ? Colors.green[800] : kBlackColor,
                    ),
                  ),
                ),
              ),
              Container(width: 1, color: Colors.grey.withValues(alpha: 0.3)),
              Expanded(
                flex: 2,
                child: Padding(
                  padding: EdgeInsets.symmetric(
                    horizontal: 10.w,
                    vertical: 8.h,
                  ),
                  child: Text(
                    dep.relationWithWorker ?? 'N/A',
                    style: TextStyle(
                      fontSize: 12.sp,
                      fontFamily: FontConstants.interFonts,
                      color: kBlackColor,
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _readOnlyField({
    required TextEditingController controller,
    required String label,
    int maxLines = 1,
  }) {
    return AppTextField(
      controller: controller,
      readOnly: true,
      maxLines: maxLines,
      label: CommonText(
        text: label,
        fontSize: 12.sp,
        fontWeight: FontWeight.w400,
        textColor: kTextColor,
        textAlign: TextAlign.start,
      ),
      fieldRadius: 8,
    );
  }

  void _callBeneficiary(String mobile) async {
    if (mobile.trim().isEmpty) return;
    final uri = Uri(scheme: 'tel', path: mobile.trim());
    if (await canLaunchUrl(uri)) {
      await launchUrl(uri);
    }
  }
}
