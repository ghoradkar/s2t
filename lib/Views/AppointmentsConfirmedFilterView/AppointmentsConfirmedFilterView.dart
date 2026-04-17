// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/Enums/Enums.dart';
import '../../Modules/FormatterManager/FormatterManager.dart';
import '../../Modules/Json_Class/AppointmentStatusResponse/AppointmentStatusResponse.dart';
import '../../Modules/Json_Class/TeamCCResponse/TeamCCResponse.dart';
import '../../Modules/ToastManager/ToastManager.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/AppActiveButton.dart';
import '../AppointmentsConfirmedTeamView/AppointmentsConfirmedTeamView.dart';
import '../DropDownListScreen/DropDownListScreen.dart';

class AppointmentsConfirmedFilterView extends StatefulWidget {
  Function(AppointmentStatusOutput, String, TeamCCOutput?) onTapApply;
  AppointmentStatusOutput? selectedAppointmentStatus;
  String selectedCampDate = "";
  TeamCCOutput? selectedTeam;
  final bool showTeam;

  AppointmentsConfirmedFilterView({
    super.key,
    required this.selectedAppointmentStatus,
    required this.selectedCampDate,
    required this.selectedTeam,
    required this.onTapApply,
    this.showTeam = true,
  });

  @override
  State<AppointmentsConfirmedFilterView> createState() =>
      _AppointmentsConfirmedFilterViewState();
}

class _AppointmentsConfirmedFilterViewState
    extends State<AppointmentsConfirmedFilterView> {
  APIManager apiManager = APIManager();
  int empCode = 0;

  @override
  void initState() {
    super.initState();
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(30, 20, 30, 10),
      child: Column(
        children: [
          Text(
            "Search By",
            style: TextStyle(
              color: kBlackColor,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w400,
              fontSize: responsiveFont(16),
            ),
          ),
          const SizedBox(height: 20),
          // AppDropdownTextfield(
          //   icon: icProgressIcon,
          //   titleHeaderString: "Appointment Status",
          //   valueString:
          //       widget.selectedAppointmentStatus?.appointmentStatus ?? "",
          //   onTap: () {
          //     ToastManager.showLoader();
          //     getAppointmentStatusList();
          //   },
          // ),
          AppTextField(
            onTap: () {
              ToastManager.showLoader();
              getAppointmentStatusList();
            },
            controller: TextEditingController(
              text: widget.selectedAppointmentStatus?.appointmentStatus ?? "",
            ),
            readOnly: true,
            hint: 'Appointment Status',
            label: CommonText(
              text: 'Appointment Status',
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
                  icProgressIcon,
                  height: 24.h,
                  width: 24.w,
                  fit: BoxFit.contain,
                ),
              ),
            ),
            suffixIcon: Icon(Icons.keyboard_arrow_down),
          ),
          const SizedBox(height: 15),
          // AppDateTextfield(
          //   icon: icCalendarMonth,
          //   titleHeaderString: "Appointment Date",
          //   valueString: widget.selectedCampDate,
          //   onTap: () {
          //     selectCampDate(context);
          //   },
          // ),
          AppTextField(
            onTap: () {
              selectCampDate(context);
            },
            controller: TextEditingController(text: widget.selectedCampDate),
            readOnly: true,
            hint: 'Appointment Date',
            label: CommonText(
              text: 'Appointment Date',
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
          ),

          if (widget.showTeam) ...[
            const SizedBox(height: 15),
            AppTextField(
              onTap: () {
                fetchTeamData();
              },
              controller: TextEditingController(
                text: widget.selectedTeam?.teamName ?? "",
              ),
              readOnly: true,
              hint: 'Team',
              label: CommonText(
                text: 'Team',
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
                    icUsersGroup,
                    height: 24.h,
                    width: 24.w,
                    fit: BoxFit.contain,
                  ),
                ),
              ),
              suffixIcon: Icon(Icons.keyboard_arrow_down),
            ),
          ],
          const SizedBox(height: 30),
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 4, 16, 20),
            child: Container(
              width: MediaQuery.of(context).size.width,
              color: Colors.transparent,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Back",
                      isCancel: true,
                      onTap: () {
                        Navigator.pop(context);
                      },
                    ),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Apply",
                      onTap: () {
                        widget.onTapApply(
                          widget.selectedAppointmentStatus!,
                          widget.selectedCampDate,
                          widget.selectedTeam,
                        );
                        Navigator.pop(context);
                      },
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Future<void> selectCampDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1880),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      widget.selectedCampDate = FormatterManager.formatDateToString(picked);
      setState(() {});
      // widget.onTapDate(selectedCampDate);
    }
  }

  void getAppointmentStatusList() {
    int empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    Map<String, String> params = {"UserID": empCode.toString()};
    apiManager.getAppointmentStatusListAPI(
      params,
      apiAppointmentStatusCallBack,
    );
  }

  void apiAppointmentStatusCallBack(
    AppointmentStatusResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Select Call Status",
        response?.output ?? [],
        DropDownTypeMenu.AppointmentStatus,
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
              if (dropDownType == DropDownTypeMenu.AppointmentStatus) {
                widget.selectedAppointmentStatus = p0;

                if (widget.selectedAppointmentStatus?.assignStatusID == 0) {
                  widget.selectedCampDate = "";
                }
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

  void fetchTeamData() {
    ToastManager.showLoader();
    Map<String, String> param = {"UserID": empCode.toString()};
    apiManager.getTeamListCCAPI(param, apiTeamListCCCallBack);
  }

  void apiTeamListCCCallBack(
    TeamCCResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      showAppointmentTeamBottomSheet(response?.output ?? []);
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void showAppointmentTeamBottomSheet(List<TeamCCOutput> list) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: true,
      enableDrag: true,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.38,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: AppointmentsConfirmedTeamView(
            list: list,
            onTapTeam: (p0) {
              widget.selectedTeam = p0;
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }
}
