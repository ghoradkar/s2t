import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/utilities/enums.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/home_screen/widget/DashboardMenuOptions.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/packet_allocation_screen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/packet_collection_screen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/packet_receive_screen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/medicine_delivery_screen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/medicine_return_screen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/pick_up_medicine_packet_screen.dart';

class MedicineDeliveryDash extends StatefulWidget {
  const MedicineDeliveryDash({super.key});

  @override
  State<MedicineDeliveryDash> createState() => _MedicineDeliveryDashState();
}

class _MedicineDeliveryDashState extends State<MedicineDeliveryDash> {
  List<DashboardMenu> list = [
    DashboardMenu.PacketCollection,
    // DashboardMenu.PacketReceive,
    DashboardMenu.PacketAllocation,
    DashboardMenu.MedicineReturn,
    DashboardMenu.PickupMedicinePacket,
    DashboardMenu.MedicineDelivery,
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Medicine Delivery",
        showActions: true,
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
      ),
      body: GridView.builder(
        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 3,
          crossAxisSpacing: 14,
          mainAxisSpacing: 12,
          childAspectRatio: 0.86,
        ),
        shrinkWrap: true,
        physics: NeverScrollableScrollPhysics(),
        itemCount: list.length,
        itemBuilder: (context, index) {
          final dashboardMenu = list[index];
          return GestureDetector(
            onTap: () => pushToNextScreen(dashboardMenu),
            child: DashboardMenuOptions(dashboardMenu: dashboardMenu),
            // child: DashboardMenuRow(dashboardMenu: dashboardMenu),
          );
        },
      ).paddingOnly(left: 6.w, right: 6.w, top: 12),
    );
  }

  void pushToNextScreen(DashboardMenu dashboardMenu) {
    switch (dashboardMenu) {
      case DashboardMenu.CampCalendar:
      case DashboardMenu.TeamPhotos:
      case DashboardMenu.PatientRegistration:
      case DashboardMenu.D2DTeams:
      case DashboardMenu.DailyWorkDashboard:
      case DashboardMenu.LiverScanning:
      case DashboardMenu.S2TPatientApp:
      case DashboardMenu.CallingList:
      case DashboardMenu.UserAttendance:
      case DashboardMenu.HealthScreeningDetails:
      case DashboardMenu.Acknowledgement:
      case DashboardMenu.ELearning:
      case DashboardMenu.OtherMenu:
      case DashboardMenu.FingerPrintUpload:
      case DashboardMenu.DeviceAndResourceMapping:
      case DashboardMenu.ResourceReMapping:
      case DashboardMenu.CampApproval:
      case DashboardMenu.CampCreation:
      case DashboardMenu.ExpenseClaim:
      case DashboardMenu.CampReadinessForm:
      case DashboardMenu.PacketAllocation:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const PacketAllocationScreen(),
          ),
        );
        break;
      case DashboardMenu.PacketCollection:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => PacketCollectionScreen()),
        );
        break;

      case DashboardMenu.CTAssignment:
      // case DashboardMenu.PacketReceive:
      //   Navigator.push(
      //     context,
      //     MaterialPageRoute(builder: (context) => PacketReceiveScreen()),
      //   );
      //   break;
      case DashboardMenu.D2DTeam:
      case DashboardMenu.TeamCampMapping:
      case DashboardMenu.AppointmentConfirmedList:
      case DashboardMenu.CommonBeneficiaryList:
      case DashboardMenu.D2DCampActivity:
      case DashboardMenu.AppointmentAndSampleCollectionOfCT:
      case DashboardMenu.D2DAvailabilityScreening:
      case DashboardMenu.D2DPhysicalExaminationDetails:
      case DashboardMenu.PaymentAndInvoice:
      case DashboardMenu.MedicineDeliveryMenu:
      case DashboardMenu.MedicineReturn:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => MedicineReturnScreen()),
        );
        break;
      case DashboardMenu.PickupMedicinePacket:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => PickUpMedicinePacketScreen()),
        );
        break;

      case DashboardMenu.MedicineDelivery:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => MedicineDeliveryScreen()),
        );
        break;
      case DashboardMenu.CallingDashboard:
        break;
    }
  }
}
