import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/acknowledgement/controllers/acknowledgement_patient_list_controller.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/Acknowledgement/ration_card_acknowledgement_screen.dart';

class PatientListAcknowledgement extends StatefulWidget {
  final int dISTLGDCODE;
  final int campId;
  final String healthScreentype;
  final String flag;

  const PatientListAcknowledgement({
    super.key,
    required this.dISTLGDCODE,
    required this.campId,
    required this.healthScreentype,
    required this.flag,
  });

  @override
  State<PatientListAcknowledgement> createState() =>
      _PatientListAcknowledgementState();
}

class _PatientListAcknowledgementState
    extends State<PatientListAcknowledgement> {
  late final AcknowledgementPatientListController _ctrl;
  String _searchText = '';

  @override
  void initState() {
    super.initState();
    _ctrl = Get.put(
      AcknowledgementPatientListController(
        campId: widget.campId,
        siteDetailId: 0,
        districtName: '',
        testId: '100',
      ),
      tag: 'hs_rc_ack_${widget.campId}',
    );
    _ctrl.searchController.addListener(() {
      setState(() => _searchText = _ctrl.searchController.text);
    });
  }

  @override
  void dispose() {
    Get.delete<AcknowledgementPatientListController>(
        tag: 'hs_rc_ack_${widget.campId}');
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Scaffold(
        backgroundColor: kBackground,
        appBar: mAppBar(
          scTitle: 'Patient List',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: Obx(() {
          if (_ctrl.isLoading.value) {
            return Column(
              children: [
                _searchBarWidget(disabled: true)
                    .paddingSymmetric(horizontal: 12.w, vertical: 8.h),
                _tableHeader(),
                const Expanded(child: CommonSkeletonInvoiceTable()),
              ],
            );
          }

          if (_ctrl.patientList.isEmpty) {
            return Column(
              children: [
                _searchBarWidget(disabled: true)
                    .paddingSymmetric(horizontal: 12.w, vertical: 8.h),
                // _tableHeader(),
                Expanded(
                    child: NoDataFound().paddingSymmetric(horizontal: 12.w)),
              ],
            );
          }

          return Column(
            children: [
              _searchBarWidget(disabled: false)
                  .paddingSymmetric(horizontal: 12.w, vertical: 8.h),
              _tableHeader(),
              Expanded(
                child: Obx(() {
                  if (_ctrl.searchList.isEmpty) {
                    return NoDataFound()
                        .paddingSymmetric(horizontal: 12.w, vertical: 6.h);
                  }
                  return ListView.builder(
                    padding: EdgeInsets.zero,
                    itemCount: _ctrl.searchList.length,
                    itemBuilder: (context, index) {
                      final patient = _ctrl.searchList[index];
                      return _PatientRow(
                        index: index,
                        patientName: patient.englishName ?? '-',
                        type: (patient.isDependent == 1) ? 'D' : 'W',
                        regdNo: patient.regdNo?.toString() ?? '-',
                        mobileNo: patient.mobileNo ?? '',
                        onTap: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                              builder: (_) => RationCardAcknowledgementScreen(
                                patient: patient,
                                campId: widget.campId,
                              ),
                            ),
                          ).then((_) => _ctrl.fetchPatients());
                        },
                      );
                    },
                  );
                }),
              ),
            ],
          );
        }),
      ),
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

  Widget _searchBarWidget({required bool disabled}) {
    return AppTextField(
      controller: _ctrl.searchController,
      readOnly: disabled,
      onChange: disabled ? null : _ctrl.filterBySearch,
      hint: 'Search by Name / Reg. No.',
      hintStyle: TextStyle(
        fontSize: 14.sp,
        fontWeight: FontWeight.w400,
        fontFamily: FontConstants.interFonts,
      ),
      fieldRadius: 8,
      prefixIcon: Image.asset(
        icSearch,
        height: 20.h,
        width: 20.w,
        fit: BoxFit.contain,
      ).paddingAll(10),
      suffixIcon: _searchText.isNotEmpty
          ? GestureDetector(
              onTap: () {
                _ctrl.searchController.clear();
                _ctrl.filterBySearch('');
              },
              child: Icon(Icons.close, size: 18.r, color: kLabelTextColor),
            )
          : null,
    );
  }
}

class _PatientRow extends StatelessWidget {
  final int index;
  final String patientName;
  final String type;
  final String regdNo;
  final String mobileNo;
  final VoidCallback onTap;

  const _PatientRow({
    required this.index,
    required this.patientName,
    required this.type,
    required this.regdNo,
    required this.mobileNo,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final isEven = index % 2 == 0;
    return GestureDetector(
      onTap: onTap,
      child: Container(
        color: isEven ? kWhiteColor : kBackground,
        padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 12.h),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            _cell('${index + 1}', flex: 1),
            _cell(patientName, flex: 3, align: TextAlign.left),
            _cell(type, flex: 1),
            _cell(regdNo, flex: 2),
            Expanded(
              flex: 1,
              child: Center(
                child: GestureDetector(
                  onTap: () async {
                    if (mobileNo.isEmpty) return;
                    final uri = Uri(scheme: 'tel', path: mobileNo);
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
