// ignore_for_file: file_names, avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/patient_registration/controller/d2d_select_camp_controller.dart';
import 'package:s2toperational/Screens/patient_registration/screen/d2d_select_camp_screen.dart';
import '../controller/appointments_list_controller.dart';
import '../model/appoinment_expected_beneficiaries_response.dart';
import '../model/team_cc_response.dart';
import '../widget/appointments_confirmed_filter_view.dart';
import '../widget/appointments_confirmed_row.dart';
import '../widget/appointments_confirmed_team_view.dart';
import 'appointments_confirmed_beneficiary_details_screen.dart';

class AppointmentsConfirmedListScreen extends StatelessWidget {
  const AppointmentsConfirmedListScreen({super.key});

  @override
  Widget build(BuildContext context) {
    Get.put(AppointmentsListController());
    SizeConfig().init(context);
    return GetBuilder<AppointmentsListController>(
      builder: (ctrl) => KeyboardDismissOnTap(
        child: Scaffold(
          appBar: mAppBar(
            scTitle: 'Appointments Confirmed List',
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Get.back(),
            showActions: true,
            actions: [
              Padding(
                padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
                child: GestureDetector(
                  onTap: () => _showFilterSheet(context, ctrl),
                  child: SizedBox(
                    width: 20,
                    height: 20,
                    child: Image.asset(icFilter),
                  ),
                ),
              ),
            ],
          ),
          floatingActionButton:
              ctrl.dashboardType == DashboardMenu.PatientRegistration
                  ? FloatingActionButton.extended(
                    backgroundColor: kPrimaryColor,
                    icon: const Icon(Icons.person_add_rounded, color: Colors.white),
                    label: CommonText(
                      text: 'Register Patient',
                      fontSize: 13.sp,
                      fontWeight: FontWeight.w600,
                      textColor: Colors.white,
                      textAlign: TextAlign.center,
                    ),
                    onPressed: () {
                      Get.delete<D2DSelectCampController>(force: true);
                      Get.put(D2DSelectCampController());
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => const D2DSelectCampScreen(),
                        ),
                      );
                    },
                  )
                  : null,
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
              child: Column(
                children: [
                  Container(
                    width: SizeConfig.screenWidth,
                    height: 50,
                    decoration: BoxDecoration(
                      color: Colors.white,
                      boxShadow: [
                        BoxShadow(
                          color: Colors.black.withValues(alpha: 0.15),
                          blurRadius: 4,
                        ),
                      ],
                      borderRadius: BorderRadius.circular(10),
                    ),
                    child: Row(
                      children: [
                        Expanded(
                          child: TextField(
                            textAlign: TextAlign.left,
                            style: TextStyle(
                              color: kBlackColor,
                              fontFamily: FontConstants.interFonts,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(14),
                            ),
                            controller: ctrl.searchController,
                            decoration: const InputDecoration(
                              contentPadding: EdgeInsets.only(left: 6),
                              border: InputBorder.none,
                              focusedBorder: InputBorder.none,
                              hintText:
                                  'Search Beneficiary No/Worker Name/MobileNo/Area/Pincode',
                            ),
                            onChanged: ctrl.search,
                          ),
                        ),
                        Padding(
                          padding: const EdgeInsets.fromLTRB(0, 0, 4, 0),
                          child: SizedBox(
                            width: 20,
                            height: 20,
                            child: Image.asset(icSearch),
                          ),
                        ),
                      ],
                    ),
                  ),
                  const SizedBox(height: 10),
                  Align(
                    alignment: Alignment.centerLeft,
                    child: Text(
                      'Total Beneficaires : ${ctrl.filteredBeneficiaries.length}',
                      style: TextStyle(
                        color: kBlackColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.bold,
                        fontSize: responsiveFont(16),
                      ),
                      textAlign: TextAlign.start,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Expanded(
                    child: Padding(
                      padding: const EdgeInsets.fromLTRB(0, 6, 0, 10),
                      child: ListView.builder(
                        itemCount: ctrl.filteredBeneficiaries.length,
                        itemBuilder: (context, index) {
                          final obj = ctrl.filteredBeneficiaries[index];
                          return GestureDetector(
                            onTap: () => _onRowTap(context, ctrl, obj),
                            child: AppointmentsConfirmedRow(obj: obj),
                          );
                        },
                      ),
                    ),
                  ),
                ],
              ).paddingSymmetric(vertical: 6, horizontal: 12),
            ),
          ),
        ),
      ),
    );
  }

  void _onRowTap(
    BuildContext context,
    AppointmentsListController ctrl,
    AppoinmentExpectedBeneficiariesOutput obj,
  ) {
    final canNavigate = ctrl.dESGID == 35 ||
        ctrl.dashboardType == DashboardMenu.PatientRegistration;
    if (!canNavigate) return;
    Get.to(
      () => const AppointmentsConfirmedBeneficiaryDetailsScreen(),
      arguments: {
        'beneficiary': obj,
        'isPatientRegistration':
            ctrl.dashboardType == DashboardMenu.PatientRegistration,
      },
    );
  }

  void _showFilterSheet(BuildContext context, AppointmentsListController ctrl) {
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
          height: MediaQuery.of(ctx).size.width * 1.08,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: AppointmentsConfirmedFilterView(
            selectedAppointmentStatus: ctrl.selectedAppointmentStatus,
            selectedCampDate: ctrl.selectedCampDate,
            selectedTeam: ctrl.selectedTeam,
            showTeam: ctrl.dashboardType != DashboardMenu.PatientRegistration,
            onTapApply: (status, date, team) {
              ctrl.applyFilter(status, date, team);
            },
          ),
        );
      },
    );
  }

  void _showTeamSheet(
    BuildContext context,
    AppointmentsListController ctrl,
    List<TeamCCOutput> list,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: true,
      enableDrag: true,
      builder: (BuildContext ctx) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(ctx).size.width * 1.38,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: AppointmentsConfirmedTeamView(
            list: list,
            onTapTeam: (team) {
              ctrl.selectedTeam = team;
              ctrl.update();
            },
          ),
        );
      },
    );
  }
}
