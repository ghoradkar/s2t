// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

import '../../../Modules/Enums/Enums.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';

class DashboardMenuRow extends StatelessWidget {
  DashboardMenuRow({super.key, required this.dashboardMenu});

  DashboardMenu dashboardMenu;

  String getIconName() {
    String icon;
    switch (dashboardMenu) {
      case DashboardMenu.CallingList:
        icon = iconCalling;
        break;
      case DashboardMenu.UserAttendance:
        icon = iconAttendance;
        break;
      case DashboardMenu.HealthScreeningDetails:
        icon = icHealthScreeningDetails;
        break;
      case DashboardMenu.D2DAvailabilityScreening:
        icon = icCampCalendar;
        break;
      case DashboardMenu.PatientRegistration:
        icon = iconAttendance;
        break;
      case DashboardMenu.CampCalendar:
        icon = icCampCalendar;
        break;
      case DashboardMenu.D2DTeams:
        icon = icD2dTeams;
        break;
      case DashboardMenu.DailyWorkDashboard:
        icon = icDailyWorkDashboard;
        break;
      case DashboardMenu.LiverScanning:
        icon = icLiverScanning;
        break;
      case DashboardMenu.S2TPatientApp:
        icon = icS2tPatientApp;
        break;
      // case DashboardMenu.CampAwarenessActivity:
      //   icon = icAampawareness;
      //   break;
      case DashboardMenu.Acknowledgement:
        icon = iconAttendance;
        break;
      case DashboardMenu.ELearning:
        icon = icelearning;
        break;
      case DashboardMenu.OtherMenu:
        icon = icelearning;
      case DashboardMenu.FingerPrintUpload:
        icon = icFingerprintUpload;
      case DashboardMenu.DeviceAndResourceMapping:
        icon = icDeviceAndResourceMapping;
      case DashboardMenu.ResourceReMapping:
        icon = icResourceReMapping;
      case DashboardMenu.CampApproval:
        icon = icCampApproval;
      case DashboardMenu.CampCreation:
        icon = icCampCreation;
      case DashboardMenu.ExpenseClaim:
        icon = icExpenseClaim;
      case DashboardMenu.CampReadinessForm:
        icon = icCampReadinessForm;
      case DashboardMenu.PacketAllocation:
        icon = icPacketAllocation;
      case DashboardMenu.PacketCollection:
        icon = icPacketCollection;
      case DashboardMenu.CTAssignment:
        icon = icCTAssignment;
      case DashboardMenu.PacketReceive:
        icon = icPacketReceived;
      case DashboardMenu.D2DTeam:
        icon = icCampReadinessForm;
      case DashboardMenu.TeamCampMapping:
        icon = icCampReadinessForm;
      case DashboardMenu.AppointmentConfirmedList:
        icon = icAppointmentConfimList;
      case DashboardMenu.CommonBeneficiaryList:
        icon = icCampReadinessForm;
      case DashboardMenu.D2DCampActivity:
        icon = icPacketReceived;

      case DashboardMenu.D2DPhysicalExaminationDetails:
        icon = iconAttendance;
        break;

      // case DashboardMenu.AnalyticalDashboard:
      //   icon = analyticalDash;
      //   break;
      case DashboardMenu.AppointmentAndSampleCollectionOfCT:
        icon = iconAttendance;
        break;
      case DashboardMenu.MedicineDeliveryMenu:
        icon = icMedicineDeliveryMenu;
        break;
      case DashboardMenu.MedicineDelivery:
        icon = icMedicineDelivery;
        break;
      case DashboardMenu.MedicineReturn:
        icon = icMedicineReturn;
        break;

      case DashboardMenu.PickupMedicinePacket:
        icon = icPickUpMedicinePacket;
        break;
      case DashboardMenu.PaymentAndInvoice:
        icon = iconAttendance;
        break;
    }
    return icon;
  }

  String getTitleName() {
    String title;

    switch (dashboardMenu) {
      case DashboardMenu.CallingList:
        title = "Calling List";
        break;
      case DashboardMenu.UserAttendance:
        title = "User Attendance";
        break;
      case DashboardMenu.HealthScreeningDetails:
        title = "Health Screening Details";
        break;
      case DashboardMenu.PatientRegistration:
        title = "Patient Registration";
        break;
      case DashboardMenu.CampCalendar:
        title = "Camp Calendar";
        break;
      case DashboardMenu.D2DTeams:
        title = "D2D Teams";
        break;
      case DashboardMenu.DailyWorkDashboard:
        title = "Daily Work Dashboard";
        break;
      case DashboardMenu.LiverScanning:
        title = "Liver Scanning";
        break;
      case DashboardMenu.S2TPatientApp:
        title = "S2T Patient App";
        break;
      // case DashboardMenu.CampAwarenessActivity:
      //   title = "Camp Awareness Activity";
      //   break;
      case DashboardMenu.Acknowledgement:
        title = "Acknowledgement";
        break;
      case DashboardMenu.ELearning:
        title = "ELearning";
        break;
      case DashboardMenu.OtherMenu:
        title = "Other Menu";
        break;
      case DashboardMenu.FingerPrintUpload:
        title = "FingerPrint Upload";
        break;
      case DashboardMenu.DeviceAndResourceMapping:
        title = "Device & Resource\nMapping";
        break;
      case DashboardMenu.ResourceReMapping:
        title = "Resource Re-Mapping";
        break;
      case DashboardMenu.CampApproval:
        title = "Camp Approval";
        break;
      case DashboardMenu.CampCreation:
        title = "Camp Creation";
        break;
      case DashboardMenu.ExpenseClaim:
        title = "Expense/Claim";
        break;
      case DashboardMenu.CampReadinessForm:
        title = "Camp Readiness Form";
        break;
      case DashboardMenu.PacketAllocation:
        title = "Packet Allocation";
        break;
      case DashboardMenu.PacketCollection:
        title = "Packet Collection";
        break;
      case DashboardMenu.CTAssignment:
        title = "CT Assignment";
        break;
      case DashboardMenu.PacketReceive:
        title = "Packet Receive";
        break;
      case DashboardMenu.D2DTeam:
        title = "D2D Team";
        break;
      case DashboardMenu.TeamCampMapping:
        title = "Team Camp Mapping";
        break;
      case DashboardMenu.AppointmentConfirmedList:
        title = "Appointment Confirmed\nList";
        break;
      case DashboardMenu.CommonBeneficiaryList:
        title = "Common Beneficiary\nList";
        break;
      case DashboardMenu.D2DCampActivity:
        title = "D2D Camp\nActivity";
        break;
      case DashboardMenu.D2DAvailabilityScreening:
        title = "D2D Availability Screening";
        break;
      // case DashboardMenu.D2DHealthScreening:
      //   title = "D2D Health Screening";
      //   break;
      case DashboardMenu.D2DPhysicalExaminationDetails:
        title = "D2D Physical Examination Details";
        break;
      // case DashboardMenu.AnalyticalDashboard:
      //   title = "Analytical Dashboard";
      //   break;
      case DashboardMenu.MedicineReturn:
        title = "Medicine Return";
        break;
      case DashboardMenu.AppointmentAndSampleCollectionOfCT:
        title = "Analytical Dashboard";
        break;
      case DashboardMenu.MedicineDeliveryMenu:
        title = "Analytical Dashboard";
        break;

      case DashboardMenu.MedicineDelivery:
        title = "Medicine Delivery";
        break;
      case DashboardMenu.PickupMedicinePacket:
        title = "Pick Up Medicine Packet";
        break;

      case DashboardMenu.PaymentAndInvoice:
        title = "Payment & Invoice";
        break;

      // case DashboardMenu.AnalyticalDashboard:
      //   title = "Analytical Dashboard";
      //   break;
    }
    return title;
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            color: kPrimaryColor.withValues(alpha: 0.2),
            // spreadRadius: 2,
            blurRadius: 2,
            // offset: Offset(2, 2),
          ),
        ],
      ),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Spacer(),
          SizedBox(
            width: 42.w,
            height: 42.h,
            child: Image.asset(getIconName()),
          ),
          const Spacer(),
          Text(
            getTitleName(),
            textAlign: TextAlign.center,
            style: TextStyle(
              fontFamily: "Inter",
              fontSize: 12.sp,
              color: gridTitleColor,
              fontWeight: FontWeight.bold,
            ),
          ),
          const Spacer(),
        ],
      ),
    );
  }
}
