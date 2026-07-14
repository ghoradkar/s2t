import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/acknowledgement/controller/acknowledgement_patient_list_controller.dart';
import 'package:s2toperational/acknowledgement/model/acknowledgement_patient_list_response.dart';
import 'package:s2toperational/acknowledgement/screens/acknowledgement_confirmation_screen.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/common_widgets/AppTextField.dart';
import 'package:s2toperational/common_widgets/CommonSkeletonList.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/calling_modules/widgets/no_data_widget.dart';

class AcknowledgementPatientListScreenNew extends StatefulWidget {
  final int campId;
  final int siteDetailId;
  final String districtName;

  const AcknowledgementPatientListScreenNew({
    super.key,
    required this.campId,
    required this.siteDetailId,
    required this.districtName,
  });

  @override
  State<AcknowledgementPatientListScreenNew> createState() =>
      _AcknowledgementPatientListScreenNewState();
}

class _AcknowledgementPatientListScreenNewState
    extends State<AcknowledgementPatientListScreenNew> {
  late final AcknowledgementPatientListController controller;
  String _searchText = '';

  @override
  void initState() {
    super.initState();
    controller = Get.put(
      AcknowledgementPatientListController(
        campId: widget.campId,
        siteDetailId: widget.siteDetailId,
        districtName: widget.districtName,
      ),
      tag: 'ack_patient_${widget.campId}',
    );
    controller.searchController.addListener(() {
      setState(() => _searchText = controller.searchController.text);
    });
  }

  @override
  void dispose() {
    Get.delete<AcknowledgementPatientListController>(
      tag: 'ack_patient_${widget.campId}',
    );
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Patient List',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: Obx(() {
        if (controller.isLoading.value) {
          return Column(
            children: [
              _searchBarDisabled().paddingSymmetric(
                horizontal: 12.w,
                vertical: 8.h,
              ),
              _tableHeader().paddingSymmetric(horizontal: 12.w),
              const Expanded(child: CommonSkeletonInvoiceTable()),
            ],
          );
        }

        if (controller.patientList.isEmpty) {
          return Column(
            children: [
              _searchBarDisabled().paddingSymmetric(
                horizontal: 12.w,
                vertical: 8.h,
              ),
              Expanded(
                child: NoDataFound().paddingSymmetric(horizontal: 12.w),
              ),
            ],
          );
        }

        return Column(
          children: [
            _searchBar().paddingSymmetric(horizontal: 12.w, vertical: 8.h),
            _tableHeader().paddingOnly(left: 12.w, right: 12.w),
            Expanded(
              child: Obx(() {
                if (controller.searchList.isEmpty) {
                  return NoDataFound().paddingSymmetric(
                    vertical: 6.h,
                    horizontal: 12.w,
                  );
                }
                return ListView.builder(
                  padding: EdgeInsets.symmetric(horizontal: 12.w),
                  itemCount: controller.searchList.length,
                  itemBuilder: (context, index) {
                    final patient = controller.searchList[index];
                    return _PatientRow(
                      index: index,
                      patient: patient,
                      onTap: () => _onRowTapped(context, patient),
                    );
                  },
                );
              }),
            ),
          ],
        );
      }),
    );
  }

  void _onRowTapped(
      BuildContext context, AcknowledgementPatientOutput patient) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => AcknowledgementConfirmationScreen(
          patient: patient,
          campId: widget.campId,
        ),
      ),
    ).then((_) => controller.fetchPatients());
  }

  Widget _searchBarDisabled() {
    return AppTextField(
      controller: controller.searchController,
      readOnly: true,
      hint: 'Search by Name / Reg. No.',
      label: CommonText(
        text: 'Search by Name / Reg. No.',
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
      fieldRadius: 8,
      prefixIcon: Icon(Icons.search, color: kLabelTextColor, size: 20.r)
          .paddingOnly(left: 6.w),
    );
  }

  Widget _searchBar() {
    return AppTextField(
      controller: controller.searchController,
      readOnly: false,
      onChange: controller.filterBySearch,
      hint: 'Search by Name / Reg. No.',
      label: CommonText(
        text: 'Search by Name / Reg. No.',
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
      fieldRadius: 8,
      prefixIcon: Icon(Icons.search, color: kLabelTextColor, size: 20.r)
          .paddingOnly(left: 6.w),
      suffixIcon: _searchText.isNotEmpty
          ? GestureDetector(
              onTap: () {
                controller.searchController.clear();
                controller.filterBySearch('');
              },
              child: Icon(Icons.close, color: kLabelTextColor, size: 18.r),
            )
          : null,
    );
  }

  Widget _tableHeader() {
    return Container(
      color: kPrimaryColor,
      padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 12.h),
      child: Row(
        children: [
          _headerCell('SN', flex: 1),
          _headerCell('Patient Name', flex: 3, align: TextAlign.left),
          _headerCell('Type', flex: 1),
          _headerCell('Reg. No.', flex: 2),
          _headerCell('Call', flex: 1),
        ],
      ),
    );
  }

  Widget _headerCell(String text,
      {required int flex, TextAlign align = TextAlign.center}) {
    return Expanded(
      flex: flex,
      child: Text(
        text,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 14.sp,
          fontWeight: FontWeight.w600,
          color: kWhiteColor,
        ),
        textAlign: align,
      ),
    );
  }
}

class _PatientRow extends StatelessWidget {
  final int index;
  final AcknowledgementPatientOutput patient;
  final VoidCallback onTap;

  const _PatientRow({
    required this.index,
    required this.patient,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final isEven = index % 2 == 0;
    final typeLabel = (patient.isDependent == 1) ? 'D' : 'W';

    return GestureDetector(
      onTap: onTap,
      child: Container(
        color: isEven ? kWhiteColor : kBackground,
        padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 12.h),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            _cell('${index + 1}', flex: 1),
            _cell(patient.englishName ?? '-', flex: 3, align: TextAlign.left),
            _cell(typeLabel, flex: 1),
            _cell(patient.regdNo?.toString() ?? '-', flex: 2),
            Expanded(
              flex: 1,
              child: Center(
                child: GestureDetector(
                  onTap: () async {
                    final uri = Uri(scheme: 'tel', path: patient.mobileNo ?? '');
                    if (await canLaunchUrl(uri)) await launchUrl(uri);
                  },
                  child: Icon(Icons.phone, color: kPrimaryColor, size: 18.r),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _cell(String text,
      {required int flex, TextAlign align = TextAlign.center}) {
    return Expanded(
      flex: flex,
      child: Align(
        alignment:
            align == TextAlign.left ? Alignment.centerLeft : Alignment.center,
        child: Text(
          text,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 13.sp,
            color: kTextColor,
          ),
          textAlign: align,
          maxLines: 2,
          overflow: TextOverflow.ellipsis,
        ),
      ),
    );
  }
}
