// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:get/get.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import '../controller/request_bill_details_controller.dart';

class RequestBillDetailsScreen extends StatelessWidget {
  const RequestBillDetailsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    Get.put(RequestBillDetailsController());
    SizeConfig().init(context);
    return GetBuilder<RequestBillDetailsController>(
      builder: (ctrl) => Scaffold(
        appBar: mAppBar(
          scTitle: 'Request Bill Details',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Get.back(),
        ),
        body: KeyboardDismissOnTap(
          dismissOnCapturedTaps: true,
          child: SizedBox(
            height: SizeConfig.screenHeight,
            width: SizeConfig.screenWidth,
            child: Stack(
              children: [
                Positioned(
                  top: 0,
                  bottom: 8,
                  left: 8,
                  right: 8,
                  child: Container(
                    decoration: const BoxDecoration(color: Colors.white),
                    child: Padding(
                      padding: const EdgeInsets.fromLTRB(0, 8, 0, 8),
                      child: SingleChildScrollView(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.start,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            _row(icHashIcon, 'Camp ID : ',
                                '${ctrl.advancesRequestDetails?.campid ?? 0}'),
                            _row(icMapPin, 'District : ',
                                ctrl.advancesRequestDetails?.dISTNAME ?? ''),
                            _row(icCalendarMonth, 'Camp Date : ',
                                ctrl.advancesRequestDetails?.campDate ?? ''),
                            _row(icUsersGroup, 'Expected Beneficiary : ',
                                '${ctrl.advancesRequestDetails?.expectedbeneficiarycount ?? 0}'),
                            _row(icAdvance, 'Total Advance Taken : ',
                                'RS. ${ctrl.advancesRequestDetails?.totalAdvanceTaken ?? 0}'),
                            _row(iconPerson, 'Fund Requested By : ',
                                '${ctrl.advancesRequestDetails?.fundRequestedBy ?? 0}'),
                            _row(icBeneficiary, 'Beneficiary Refreshment : ',
                                'RS. ${ctrl.advancesRequestDetails?.beneficiaryRefreshment ?? 0}'),
                            _row(icAwareness, 'Camp Awareness using Bhopu : ',
                                'RS. ${ctrl.advancesRequestDetails?.campAwarenessUsingBhopu ?? 0}'),
                            _row(
                                icHall,
                                'Camp Hall Grampanchayat/School/Govt. Office/Tent : ',
                                'RS. ${ctrl.advancesRequestDetails?.campHallGramPanchayatSchoolGovtOfficeTent ?? 0}'),
                            _row(icChairs, 'Chairs : ',
                                'RS. ${ctrl.advancesRequestDetails?.chairs ?? 0}'),
                            _row(icCleaning, 'Cleaning Charges : ',
                                'RS. ${ctrl.advancesRequestDetails?.cleaningCharges ?? 0}'),
                            _row(icDrinking, 'Drinking Water : ',
                                'RS. ${ctrl.advancesRequestDetails?.drinkingWater ?? 0}'),
                            _row(icFood, 'Food to Staff (TA) Allowance : ',
                                'RS. ${ctrl.advancesRequestDetails?.foodToStaffTAAllowance ?? 0}'),
                            _row(icnTent, 'Post Camp Expense : ', 'RS: 0'),
                            _row(
                                icRunnerBoy,
                                'Sample Movement to Lab - TSRTC or Any other Cargo : ',
                                'RS. ${ctrl.advancesRequestDetails?.sampleMovementToLabRunnerBoy ?? 0}'),
                            _row(
                                icBillUploadedIcon,
                                'Transportation of Staff (TA) Group of Transport : ',
                                'RS. ${ctrl.advancesRequestDetails?.transportationOfStaffTAGroupOfTransport ?? 0}'),
                            _row(
                                icTransportation,
                                'Transportation of Staff (TA) Individual : ',
                                'RS. ${ctrl.advancesRequestDetails?.transportationOfStaffTAIndividual ?? 0}'),
                            _row(icBillUploadedIcon, 'Total Amount : ',
                                'RS. ${ctrl.advancesRequestDetails?.totalBillSubmittedAmt ?? 0}'),
                            _row(icProgressIcon, 'Approval Status : ',
                                ctrl.advancesRequestDetails?.approvalStatus ?? ''),
                          ],
                        ),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _row(String icon, String label, String value) {
    return Column(
      children: [
        SizedBox(
          child: Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              SizedBox(
                width: responsiveWidth(22),
                height: responsiveWidth(22),
                child: Image.asset(icon),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: RichText(
                  text: TextSpan(
                    children: [
                      TextSpan(
                        text: label,
                        style: TextStyle(
                          color: Colors.black,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w500,
                          fontSize: responsiveFont(16),
                        ),
                      ),
                      TextSpan(
                        text: value,
                        style: TextStyle(
                          color: dropDownTitleHeader,
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w400,
                          fontSize: responsiveFont(16),
                        ),
                      ),
                    ],
                  ),
                  textAlign: TextAlign.start,
                  maxLines: null,
                  overflow: TextOverflow.visible,
                ),
              ),
            ],
          ),
        ),
        const SizedBox(height: 8),
      ],
    );
  }
}
