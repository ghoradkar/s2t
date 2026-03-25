import 'dart:async';
import 'dart:math' as math;
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/AdminDashboard/ConductedCampsTotals.dart';
import 'package:s2toperational/Modules/Json_Class/AdminDashboard/TodaysPatientsResponse.dart';
import 'package:s2toperational/Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Screens/AdminDashboard/Screens/AdminDashboardWidget.dart';
import 'package:s2toperational/Screens/AdminDashboard/Screens/D2DTeamsScreen.dart';
import 'package:s2toperational/Screens/AdminDashboard/Screens/LiverScanningScreen.dart';
import 'package:s2toperational/Screens/AdminDashboard/Screens/S2TPatientAppScreen.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/expected_beneficiary_list_controller.dart';
import 'package:s2toperational/Screens/calling_modules/screens/expected_beneficiary_list.dart';
import 'package:s2toperational/Screens/CampCalendarScreen/CampCalendarScreen.dart';
import 'package:s2toperational/Screens/D2DAvailability/D2DAvailabilityScreen.dart';
import 'package:s2toperational/Screens/HomeScreen/DashboardMenuRow/DashboardMenuOptions.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/PacketAllocation/view/PacketAllocationScreen.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/PacketCollection/view/PacketCollectionScreen.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/PacketReceive/view/PacketReceiveScreen.dart';
import 'package:s2toperational/Screens/MedicineDeliveryMenu/medicine_delivery_dash.dart';
import 'package:s2toperational/Screens/PaymentAndInvoice/payment_invoice_segment_screen.dart';
import 'package:s2toperational/Screens/SuperAdmin/Controller/SuperAdminController.dart';
import 'package:s2toperational/Screens/SuperAdmin/super_admin_card.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/D2DPhysicalExaminationDetailsScreen/D2DPhysicalExaminationDetailsScreen.dart';
import 'package:url_launcher/url_launcher.dart';
import '../../Modules/Enums/Enums.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/utilities/DeviceInfoUtil.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/AppButton.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import '../AppointmentsConfirmedList/AppointmentsConfirmedListScreen/AppointmentsConfirmedListScreen.dart';
import '../CTAssignment/CTAssignmentScreen/CTAssignmentScreen.dart';
import '../CampCreationScreen/CampCreationScreen.dart';
import '../CampReadinessForm/CampReadinessFormScreen.dart';
import '../D2DTeam/D2DTeamsScreen/D2DTeamsScreen.dart';
import '../DailyWorkDashboard/DailyWorkDashboardScreen/DailyWorkDashboardScreen.dart';
import '../DeviceAndResourceMapping/DeviceAllocationScreen.dart';
import '../ExpenseClaimScreen/ExpenseClaimDashboardScreen.dart';
import '../ResourceReMapping/ResourceReMappingCampListScreen/ResourceReMappingCampListScreen.dart';
import '../SideDrawerMenu/SideDrawerMenu.dart';
import '../TeamCampMapping/TeamCampMappingScreen/TeamCampMappingScreen.dart';
import '../health_screening_details/screens/camp_for_health_screening_d2d_screen/camp_for_health_screening_d2d_screen.dart';
import '../health_screening_details/screens/camp_for_health_screening_screen/camp_for_health_screening_screen.dart';
import '../user_attendance/screens/user_attendance_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final GlobalKey<ScaffoldState> scaffoldKey = GlobalKey<ScaffoldState>();

  List<DashboardMenu> menuList = [];
  bool regularCamp = true;
  bool doorToDoorCamp = false;
  bool isShowRadioCamp = true;

  // double topHeight = 14;
  bool isShowNameAndDesignation = true;
  int dESGID = 0;
  String? designation;
  bool? isFixedPay;
  String appVersion = "";
  APIManager apiManager = APIManager();
  TodaysPatientsResponse? todaysPatientsResponse;
  ConductedCampsResponse? campsResponse;
  ConductedCampsTotals? conductedTotals;
  TodaysPatientsTotals? todaysTotals;
  late final StreamSubscription<List<ConnectivityResult>> _connSub;
  bool? _isOnline;
  final SuperAdminController adminController = Get.put(SuperAdminController());
  bool isLoadingAdminData = false;
  bool _adminLoaded = false;

  @override
  void initState() {
    super.initState();
    final LoginResponseModel? loginResponseModel =
    DataProvider().getParsedUserData();

    dESGID = loginResponseModel?.output?.first.dESGID ?? 0;
    designation = loginResponseModel?.output?.first.designation ?? "";
    isFixedPay = loginResponseModel?.output?.first.isFixedPay;

    checkRegular();
    updateDahsboardMenu();
    getAppVersion();

    _isOnline = null;

    _connSub = Connectivity().onConnectivityChanged.listen((
        List<ConnectivityResult> results,) {
      final result =
      results.isNotEmpty ? results.first : ConnectivityResult.none;
      onConnectivityChanged(result);
    });
  }

  @override
  void dispose() {
    _connSub.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: scaffoldKey,
      appBar: mAppBar(
        scTitle: "Dashboard",
        leadingIcon: iconDrawer,
        onLeadingIconClick: () {
          scaffoldKey.currentState?.openDrawer();
        },
      ),
      drawer: SideDrawerMenu(appVersion: appVersion),
      body:
      _isOnline == null && dESGID != 166
          ? Center(child: CircularProgressIndicator()) // Loading state
          : _isOnline ?? true
          ? buildOnlineBody(context)
          : NoInternetWidget(onRetryPressed: retry),
    );
  }

  Widget buildOnlineBody(BuildContext context) {
    return SingleChildScrollView(
      child: Stack(
        children: [
          Column(
            children: [
              showRadioCampButton(),
              isAdminDash(),
              isSuperAdminDash(),
              Padding(
                padding: EdgeInsets.only(left: 8.w, right: 8.w, top: 14.h),
                child:
                (dESGID == 51 ||
                    dESGID == 166 ||
                    dESGID == 171 ||
                    dESGID == 26) //admin dash
                    ? GridView.builder(
                  gridDelegate:
                  SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 3,
                    crossAxisSpacing: 14,
                    mainAxisSpacing: 20,
                    childAspectRatio: 0.91,
                  ),
                  shrinkWrap: true,
                  physics: NeverScrollableScrollPhysics(),
                  itemCount: menuList.length,
                  itemBuilder: (context, index) {
                    final dashboardMenu = menuList[index];
                    return GestureDetector(
                      onTap: () => pushToNextScreen(dashboardMenu),
                      child: AdminDashboardMenuOptions(
                        dashboardMenu: dashboardMenu,
                      ),
                      // child: DashboardMenuRow(dashboardMenu: dashboardMenu),
                    );
                  },
                ).paddingOnly(left: 6.w, right: 6.w)
                    : GridView.builder(
                  gridDelegate:
                  SliverGridDelegateWithFixedCrossAxisCount(
                    crossAxisCount: 3,
                    crossAxisSpacing: 14,
                    mainAxisSpacing: 12,
                    childAspectRatio: 0.86,
                  ),
                  shrinkWrap: true,
                  physics: NeverScrollableScrollPhysics(),
                  itemCount:
                  // dESGID == 141 && regularCamp == true
                  //     ? 0
                  //     :
                  menuList.length,
                  itemBuilder: (context, index) {
                    final dashboardMenu = menuList[index];
                    return GestureDetector(
                      onTap: () => pushToNextScreen(dashboardMenu),
                      child: DashboardMenuOptions(
                        dashboardMenu: dashboardMenu,
                      ),
                      // child: DashboardMenuRow(dashboardMenu: dashboardMenu),
                    );
                  },
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  void onConnectivityChanged(ConnectivityResult result) {
    final connected =
        result == ConnectivityResult.mobile ||
            result == ConnectivityResult.wifi ||
            result == ConnectivityResult.ethernet;

    if (!connected) {
      if (mounted) setState(() => _isOnline = false);
      return;
    }

    if (mounted) setState(() => _isOnline = true);

    /// 🔥 LOAD DASHBOARD ONLY ONCE WHEN ONLINE
    if (!_adminLoaded) {
      _adminLoaded = true;
      checkIfAdminDashboard();
    }
  }

  Future<void> retry() async {
    if (_isOnline == false) return;

    setState(() => isLoadingAdminData = true);

    _adminLoaded = false;
    await checkIfAdminDashboard();

    setState(() => isLoadingAdminData = false);
  }

  void apiCampConducted(ConductedCampsResponse? response,
      String errorMessage,
      bool success,) {
    ToastManager.hideLoader();
    if (!success || response == null) {
      print(
        'ConductedCamp FAILED: $errorMessage  resp=${response
            ?.status}/${response?.message}',
      );
      ToastManager.toast(errorMessage);
      return;
    }

    campsResponse = response;
    conductedTotals = ConductedCampsTotals.fromRows(response.output);
    print(
      'Conducted totals: total=${conductedTotals?.total} reg=${conductedTotals
          ?.regular} d2d=${conductedTotals?.d2d} bill=${conductedTotals
          ?.billable}',
    );
    setState(() {});
  }

  void apiTodaysPatient(TodaysPatientsResponse? response,
      String errorMessage,
      bool success,) {
    ToastManager.hideLoader();

    if (!success || response == null) {
      print(
        'TodaysPatient FAILED: $errorMessage resp=${response?.status}/${response
            ?.message}',
      );
      ToastManager.toast(errorMessage);
      return;
    }

    todaysPatientsResponse = response;
    todaysTotals = TodaysPatientsTotals.fromRows(response.output);

    print(
      'Todays totals: total=${todaysTotals?.total} reg=${todaysTotals
          ?.regular} d2d=${todaysTotals?.d2d}',
    );

    setState(() {});
  }

  Future<void> checkIfAdminDashboard() async {
    if (dESGID == 51) {
      setState(() => isLoadingAdminData = true);

      await apiManager.getConductedCamp(apiCampConducted);

      final now = DateTime.now();
      final date = DateFormat('yyyy-MM-dd').format(now);

      await apiManager.getTodaysPatent(apiTodaysPatient, date);

      setState(() => isLoadingAdminData = false);
    } else if (dESGID == 166) {
      setState(() => isLoadingAdminData = true);
      await adminController.checkInternetSuperAdmin(showLoader: false);
      setState(() => isLoadingAdminData = false);
    }
  }

  void checkRegular() {
    if (DataProvider().getRegularCamp()) {
      doorToDoorCamp = false;
      regularCamp = true;
    } else {
      doorToDoorCamp = true;
      regularCamp = false;
    }
  }

  void updateDahsboardMenu() {
    if (dESGID == 30) {
      isShowRadioCamp = false;
      menuList.add(DashboardMenu.CallingList);
      menuList.add(DashboardMenu.UserAttendance);
    } else if (dESGID == 51) {
      isShowRadioCamp = false;
      // topHeight = 200;
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.DailyWorkDashboard);
      menuList.add(DashboardMenu.LiverScanning);
      menuList.add(DashboardMenu.S2TPatientApp);
      menuList.add(DashboardMenu.D2DTeams);
      // menuList.add(DashboardMenu.AnalyticalDashboard);
    } else if (dESGID == 26) {
      isShowRadioCamp = false;
      // topHeight = 200;
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.DailyWorkDashboard);
      menuList.add(DashboardMenu.LiverScanning);
      menuList.add(DashboardMenu.D2DTeams);
      menuList.add(DashboardMenu.S2TPatientApp);
      menuList.add(DashboardMenu.UserAttendance);
    } else if (dESGID == 166) {
      isShowRadioCamp = false;
      // topHeight = 200;
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.LiverScanning);
      menuList.add(DashboardMenu.S2TPatientApp);
      menuList.add(DashboardMenu.D2DTeams);
    } else if (designation == "Vice President") {
      isShowRadioCamp = false;
      // topHeight = 200;
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.DailyWorkDashboard);
      menuList.add(DashboardMenu.LiverScanning);
      menuList.add(DashboardMenu.D2DTeams);
      menuList.add(DashboardMenu.S2TPatientApp);
    } else {
      // topHeight = 80;

      if (regularCamp) {
        // if (apiManager.apiMode == APIMode.Beta) {
        regularCampMenu();
        // }
      } else {
        // if (apiManager.apiMode == APIMode.Beta) {
        doorToDoorCampBetaMenu();
        // }
      }
    }
    setState(() {});
  }

  void regularCampMenu() {
    if (dESGID == 34) {
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.HealthScreeningDetails);
      // menuList.add(DashboardMenu.Acknowledgement);
      menuList.add(DashboardMenu.ELearning);
    } else if (dESGID == 173) {
      menuList.add(DashboardMenu.UserAttendance);
    } else if (dESGID == 75) {
      isShowRadioCamp = true;
      // topHeight = 200;
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.UserAttendance);
    } else if (dESGID == 35) {
      menuList.add(DashboardMenu.HealthScreeningDetails);
      menuList.add(DashboardMenu.PatientRegistration);
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.FingerPrintUpload);
      menuList.add(DashboardMenu.Acknowledgement);
      menuList.add(DashboardMenu.ELearning);
    } else if (dESGID == 29) {
      menuList.add(DashboardMenu.DeviceAndResourceMapping);
      menuList.add(DashboardMenu.ResourceReMapping);
      menuList.add(DashboardMenu.CampCreation);
      menuList.add(DashboardMenu.ExpenseClaim);
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.CampReadinessForm);
      menuList.add(DashboardMenu.HealthScreeningDetails);
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.FingerPrintUpload);
      menuList.add(DashboardMenu.Acknowledgement);
      menuList.add(DashboardMenu.ELearning);
    } else if (dESGID == 92) {
      menuList.add(DashboardMenu.CampCreation);
      menuList.add(DashboardMenu.DeviceAndResourceMapping);
      menuList.add(DashboardMenu.ResourceReMapping);
      menuList.add(DashboardMenu.CampReadinessForm);
      menuList.add(DashboardMenu.ExpenseClaim);
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.HealthScreeningDetails);
    } else if (dESGID == 141) {
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.ELearning);
    }
    setState(() {});
  }

  void doorToDoorCampBetaMenu() {
    if (dESGID == 34) {
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.D2DPhysicalExaminationDetails);
      menuList.add(DashboardMenu.HealthScreeningDetails);
      if (isFixedPay == false) {
        menuList.add(DashboardMenu.PaymentAndInvoice);
      }
    } else if (dESGID == 75) {
      isShowRadioCamp = true;
      // topHeight = 200;
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.UserAttendance);
    } else if (dESGID == 157) {
      isShowRadioCamp = true;
      // topHeight = 200;
      menuList.add(DashboardMenu.MedicineDeliveryMenu);
    }

    
    else if (dESGID == 173) {
      isShowRadioCamp = true;
      menuList.add(DashboardMenu.MedicineDeliveryMenu);
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.UserAttendance);
    } else if (dESGID == 35) {
      menuList.add(DashboardMenu.HealthScreeningDetails);
      menuList.add(DashboardMenu.PatientRegistration);
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.FingerPrintUpload);
      menuList.add(DashboardMenu.Acknowledgement);
      menuList.add(DashboardMenu.MedicineDeliveryMenu);
      menuList.add(DashboardMenu.ELearning);
      menuList.add(DashboardMenu.PaymentAndInvoice);
    } else if (dESGID == 29) {
      menuList.add(DashboardMenu.AppointmentConfirmedList);
      menuList.add(DashboardMenu.PacketAllocation);
      menuList.add(DashboardMenu.DailyWorkDashboard);
      menuList.add(DashboardMenu.PacketReceive);
      menuList.add(DashboardMenu.TeamCampMapping);
      menuList.add(DashboardMenu.CampCreation);
      menuList.add(DashboardMenu.D2DTeam);
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.HealthScreeningDetails);
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.D2DCampActivity);
    } else if (dESGID == 92) {
      menuList.add(DashboardMenu.MedicineDeliveryMenu);
      menuList.add(DashboardMenu.DailyWorkDashboard);
      menuList.add(DashboardMenu.AppointmentConfirmedList);
      menuList.add(DashboardMenu.CommonBeneficiaryList);
      menuList.add(DashboardMenu.CampCreation);
      menuList.add(DashboardMenu.CTAssignment);
      menuList.add(DashboardMenu.D2DTeam);
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.TeamCampMapping);
      menuList.add(DashboardMenu.HealthScreeningDetails);
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.FingerPrintUpload);
      menuList.add(DashboardMenu.Acknowledgement);
      menuList.add(DashboardMenu.ELearning);
    } else if (dESGID == 139) {
      menuList.add(DashboardMenu.DailyWorkDashboard);
      menuList.add(DashboardMenu.AppointmentConfirmedList);
      menuList.add(DashboardMenu.PacketAllocation);
      menuList.add(DashboardMenu.PacketReceive);
      menuList.add(DashboardMenu.PacketCollection);
      menuList.add(DashboardMenu.CampCreation);
      menuList.add(DashboardMenu.CTAssignment);
      menuList.add(DashboardMenu.D2DTeam);
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.TeamCampMapping);
      menuList.add(DashboardMenu.HealthScreeningDetails);
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.FingerPrintUpload);
      menuList.add(DashboardMenu.Acknowledgement);
      menuList.add(DashboardMenu.ELearning);
    } else if (dESGID == 141) {
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.D2DPhysicalExaminationDetails);
      menuList.add(DashboardMenu.HealthScreeningDetails);
      // menuList.add(DashboardMenu.D2DAvailabilityScreening);
      // menuList.add(DashboardMenu.ELearning);
    }
    setState(() {});
  }

  Future<void> openYouTubeChannel() async {
    const channelId = 'UC_rxJXf5LP6yIxvQoRPUqJg';
    final appUrl = Uri.parse('vnd.youtube://channel/$channelId');
    final webUrl = Uri.parse('https://www.youtube.com/channel/$channelId');

    if (await canLaunchUrl(appUrl)) {
      await launchUrl(appUrl);
    } else {
      await launchUrl(webUrl, mode: LaunchMode.externalApplication);
    }
  }

  void pushToNextScreen(DashboardMenu dashboardMenu) {
    switch (dashboardMenu) {
      case DashboardMenu.CampCalendar:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => CampCalendarScreen()),
        );
        break;
      case DashboardMenu.CallingList:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) {
              if (!Get.isRegistered<ExpectedBeneficiaryListController>()) {
                Get.put(ExpectedBeneficiaryListController());
              }
              return const ExpectedBeneficiaryList();
            },
          ),
        );
        break;
      case DashboardMenu.UserAttendance:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => const UserAttendanceScreen()),
        );
        break;
      case DashboardMenu.PatientRegistration:
        break;
      case DashboardMenu.D2DTeams:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder:
                (context) => D2DTeamsScreen(title: 'Android Total Patient'),
          ),
        );
        break;
      case DashboardMenu.DailyWorkDashboard:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const DailyWorkDashboardScreen(),
          ),
        );
        break;
      case DashboardMenu.LiverScanning:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => const LiverScanningScreen()),
        );

        break;
      case DashboardMenu.S2TPatientApp:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => const S2tPatientAppScreen()),
        );

        break;
      case DashboardMenu.HealthScreeningDetails:
        if (DataProvider().getRegularCamp()) {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => CampForHealthScreeningScreen(testID: 3),
            ),
          );
        } else {
          Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => CampForHealthScreeningD2DScreen(testID: 16),
            ),
          );
        }
        break;
      case DashboardMenu.Acknowledgement:
      // Navigator.push(
      //   context,
      //   MaterialPageRoute(
      //     builder: (context) => AcknowledgementCampListScreen(),
      //   ),
      // );
        break;
      case DashboardMenu.ELearning:
        openYouTubeChannel();
        break;
    // case DashboardMenu.D2DAvailabilityScreening:
    // openYouTubeChannel();
    // break;
      case DashboardMenu.OtherMenu:
        break;
      case DashboardMenu.FingerPrintUpload:
        break;
      case DashboardMenu.DeviceAndResourceMapping:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const DeviceAllocationScreen(),
          ),
        );
        break;
      case DashboardMenu.ResourceReMapping:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const ResourceReMappingCampListScreen(),
          ),
        );
        break;
      case DashboardMenu.CampApproval:
        break;
      case DashboardMenu.CampCreation:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => const CampCreationScreen()),
        );
        break;
      case DashboardMenu.ExpenseClaim:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const ExpenseClaimDashboardScreen(),
          ),
        );
        break;
      case DashboardMenu.CampReadinessForm:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const CampReadinessFormScreen(),
          ),
        );
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
      case DashboardMenu.MedicineDelivery:
      // Navigator.push(
      //   context,
      //   MaterialPageRoute(builder: (context) => PacketCollectionScreen()),
      // );
        break;
      case DashboardMenu.CTAssignment:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => const CTAssignmentScreen()),
        );
        break;
      case DashboardMenu.PacketReceive:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => const PacketReceiveScreen()),
        );
        break;
      case DashboardMenu.D2DTeam:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => const D2DTeamScreen()),
        );
        break;
      case DashboardMenu.TeamCampMapping:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const TeamCampMappingScreen(),
          ),
        );
        break;
      case DashboardMenu.AppointmentConfirmedList:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder:
                (context) =>
                AppointmentsConfirmedListScreen(dashboardType: null),
          ),
        );
        break;
      case DashboardMenu.CommonBeneficiaryList:
        break;
      case DashboardMenu.D2DCampActivity:
        break;
      case DashboardMenu.D2DPhysicalExaminationDetails:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => D2DPhysicalExaminationDetailsScreen(),
          ),
        );
        break;
      case DashboardMenu.D2DAvailabilityScreening:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => D2DAvailabilityScreen()),
        );
        break;
      case DashboardMenu.MedicineReturn:
        break;
      case DashboardMenu.AppointmentAndSampleCollectionOfCT:
        break;
      case DashboardMenu.MedicineDeliveryMenu:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => MedicineDeliveryDash()),
        );
        break;
      case DashboardMenu.PaymentAndInvoice:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => PaymentInvoiceSegmentScreen(),
          ),
        );
        break;
      case DashboardMenu.PickupMedicinePacket:
        break;
    }
  }

  void getAppVersion() async {
    var deviceInfo = await DeviceInfoUtil().getPackageInfo();
    appVersion = deviceInfo.version;
    setState(() {});
  }

  Widget showRadioCampButton() {
    if (isShowRadioCamp) {
      return Padding(
        padding: EdgeInsets.fromLTRB(12.w, 12.h, 12.w, 12.h),
        child: Container(
          width: MediaQuery
              .of(context)
              .size
              .width,
          height: 46.h,
          decoration: BoxDecoration(
            color: Colors.transparent,
            border: Border.all(color: Colors.white, width: 1),
            borderRadius: const BorderRadius.only(
              topLeft: Radius.circular(20),
              bottomLeft: Radius.circular(20),
              topRight: Radius.circular(20),
              bottomRight: Radius.circular(20),
            ),
          ),
          child: Row(
            children: [
              Expanded(
                child: GestureDetector(
                  onTap: () {
                    doorToDoorCamp = false;
                    regularCamp = true;
                    DataProvider().isRegularCamp(true);
                    menuList = [];
                    updateDahsboardMenu();
                  },
                  child: Container(
                    decoration: BoxDecoration(
                      color: regularCamp == true ? kPrimaryColor : kWhiteColor,
                      borderRadius: const BorderRadius.only(
                        topLeft: Radius.circular(20),
                        bottomLeft: Radius.circular(20),
                      ),
                      border: Border.all(
                        color: Colors.grey.withValues(alpha: 0.3),
                      ),
                    ),
                    child: Center(
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.center,
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 20.w,
                            height: 20.h,
                            child: Image.asset(
                              icnTent,
                              color:
                              regularCamp == false
                                  ? Colors.black
                                  : Colors.white,
                            ),
                          ),
                          SizedBox(width: 6.w),
                          CommonText(
                            text: "Regular Camp",
                            fontSize: 13.sp,
                            fontWeight: FontWeight.w500,
                            textColor:
                            regularCamp == false
                                ? Colors.black
                                : Colors.white,
                            textAlign: TextAlign.center,
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
              Expanded(
                child: GestureDetector(
                  onTap: () {
                    doorToDoorCamp = true;
                    regularCamp = false;
                    DataProvider().isRegularCamp(false);
                    menuList = [];
                    updateDahsboardMenu();
                  },
                  child: Container(
                    decoration: BoxDecoration(
                      color:
                      doorToDoorCamp == true ? kPrimaryColor : kWhiteColor,
                      borderRadius: const BorderRadius.only(
                        topRight: Radius.circular(20),
                        bottomRight: Radius.circular(20),
                      ),
                      border: Border.all(
                        color: Colors.grey.withValues(alpha: 0.3),
                      ),
                    ),
                    child: Center(
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.center,
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          SizedBox(
                            width: 20,
                            height: 20,
                            child: Image.asset(
                              "assets/icons/home-2.png",
                              color:
                              doorToDoorCamp == false
                                  ? Colors.black
                                  : Colors.white,
                            ),
                          ),
                          SizedBox(width: 6.w),
                          CommonText(
                            text: "Door to Door Camp",
                            fontSize: 13.sp,
                            fontWeight: FontWeight.w500,
                            textColor:
                            doorToDoorCamp == false
                                ? Colors.black
                                : Colors.white,
                            textAlign: TextAlign.center,
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      );
    } else {
      return Container();
    }
  }

  Widget isAdminDash() {
    if (dESGID != 51) return const SizedBox();

    // topHeight = 364;

    if (isLoadingAdminData) {
      return const CommonSkeletonAdminDashboard();
    }

    if (conductedTotals == null || todaysTotals == null) {
      return SizedBox(
        height: 200.h,
        child: Center(
          child: Text(
            "No dashboard data available",
            style: TextStyle(fontSize: 14.sp, color: Colors.grey),
          ),
        ),
      );
    }

    return AdminDashboardWidget(
      conductedTotals: conductedTotals!,
      todaysTotals: todaysTotals!,
    );
  }

  Widget isSuperAdminDash() {
    if (dESGID == 166) {
      // topHeight = 800;

      // // Show loading while data is being fetched
      // if (isLoadingAdminData) {
      //   return SizedBox(
      //     height: 200.h,
      //     child: Center(
      //       child: CircularProgressIndicator(),
      //     ),
      //   );
      // }

      return GetBuilder<SuperAdminController>(
        builder: (controller) {
          if (adminController.isSuperAdminLoading) {
            return Column(
              children: [
                _SuperAdminGraphTabsSkeleton(),
                // SizedBox(height: 12.h),
                Padding(
                  padding: EdgeInsets.symmetric(horizontal: 12.w),
                  child: Card(
                    child: SizedBox(
                      height: 140.h,
                      child: CommonSkeletonInvoiceTable(
                        itemCount: 4,
                        rowHeight: 30,
                        shrinkWrap: false,
                        physics: const NeverScrollableScrollPhysics(),
                      ),
                    ),
                  ),
                ),

                Card(
                  child: SizedBox(
                    height: 140.h,
                    child: CommonSkeletonInvoiceTable(
                      itemCount: 4,
                      rowHeight: 30,
                      shrinkWrap: false,
                      physics: const NeverScrollableScrollPhysics(),
                    ),
                  ),
                ).paddingOnly(left: 12.w, right: 12.w, top: 10.h),
              ],
            );
          }

          final output = adminController.conductedCardSuperAdmin?.output ?? [];
          final details = adminController.todaysTableCount?.details;
          final totalDetails = adminController.totalTableCount?.details;

          // Today's Patients - Filter by SubOrgId
          final todaysOutput =
              adminController.todaysPatientCardSuperAdmin?.output ?? [];
          final todaysHllData = todaysOutput.firstWhereOrNull(
                (item) => item.subOrgId == 2,
          );
          final todaysHsccData = todaysOutput.firstWhereOrNull(
                (item) => item.subOrgId == 3,
          );

          // Conducted Camps - Filter by SubOrgId
          final conductedHllData = output.firstWhereOrNull(
                (item) => item.subOrgId == 2,
          );
          final conductedHsccData = output.firstWhereOrNull(
                (item) => item.subOrgId == 3,
          );

          final conductedGroups = [
            _SuperAdminBarGroup(
              label: "Regular",
              hll: conductedHllData?.regularCamp ?? 0,
              hscc: conductedHsccData?.regularCamp ?? 0,
            ),
            _SuperAdminBarGroup(
              label: "D2D",
              hll: conductedHllData?.d2dCamp ?? 0,
              hscc: conductedHsccData?.d2dCamp ?? 0,
            ),
          ];

          final todaysGroups = [
            _SuperAdminBarGroup(
              label: "Regular",
              hll: todaysHllData?.regularCamp ?? 0,
              hscc: todaysHsccData?.regularCamp ?? 0,
            ),
            _SuperAdminBarGroup(
              label: "D2D",
              hll: todaysHllData?.d2dCamp ?? 0,
              hscc: todaysHsccData?.d2dCamp ?? 0,
            ),
          ];

          final conductedSubtitle =
          output.isNotEmpty
              ? _formatFinancialYear(output.first.financialYear)
              : "";

          return Column(
            children: [
              _SuperAdminGraphTabs(
                conductedSubtitle: conductedSubtitle,
                conductedGroups: conductedGroups,
                todaysSubtitle: "",
                todaysGroups: todaysGroups,
              ),

              // Rest of your SuperAdminCards remain unchanged...
              SuperAdminCard(
                enableRowColor: true,

                title: "",
                subtitle:
                "*Today's Data as of :${adminController.todaysTableCount
                    ?.dateTime ?? "-"}",
                headers: ["", "", "HLL", "HSCC", "Total"],
                rows: [
                  [
                    "Treatment",
                    "Total Treatment Given",
                    details?.treatmentGivenHLL.toString() ?? "",
                    details?.treatmentGivenHSCC.toString() ?? "",
                    details?.treatmentGivenTotal.toString() ?? "",
                  ],
                  [
                    "IPD",
                    "Total IPD Registered Patient",
                    details?.ipdRegisteredHLL.toString() ?? "",
                    details?.ipdRegisteredHSCC.toString() ?? "",
                    details?.ipdRegisteredTotal.toString() ?? "",
                  ],
                  [
                    "IPD",
                    "Total Discharge Patient",
                    details?.dischargePatientHLL.toString() ?? "",
                    details?.dischargePatientHSCC.toString() ?? "",
                    details?.dischargePatientTotal.toString() ?? "",
                  ],
                  [
                    "Medicine",
                    "Prescription Given",
                    details?.prescriptionGivenHLL.toString() ?? "",
                    details?.prescriptionGivenHSCC.toString() ?? "",
                    details?.prescriptionGivenTotal.toString() ?? "",
                  ],
                  [
                    "Medicine",
                    "Prescription Issued",
                    details?.prescriptionIssuedHLL.toString() ?? "",
                    details?.prescriptionIssuedHSCC.toString() ?? "",
                    details?.prescriptionIssuedTotal.toString() ?? "",
                  ],
                ],
              ).paddingOnly(bottom: 14),

              SuperAdminCard(
                enableRowColor: true,

                title: "",
                subtitle:
                "*Total Data as of : ${adminController.totalTableCount
                    ?.dateTime ?? "-"}",
                headers: ["", "", "HLL", "HSCC", "Total"],
                rows: [
                  [
                    "Treatment",
                    "Total Treatment Given",
                    totalDetails?.treatmentGivenHLL.toString() ?? "",
                    totalDetails?.treatmentGivenHSCC.toString() ?? "",
                    totalDetails?.treatmentGivenTotal.toString() ?? "",
                  ],
                  [
                    "IPD",
                    "Total IPD Registered Patient",
                    totalDetails?.ipdRegisteredHLL.toString() ?? "",
                    totalDetails?.ipdRegisteredHSCC.toString() ?? "",
                    totalDetails?.ipdRegisteredTotal.toString() ?? "",
                  ],
                  [
                    "IPD",
                    "Total Discharge Patient",
                    totalDetails?.dischargePatientHLL.toString() ?? "",
                    totalDetails?.dischargePatientHSCC.toString() ?? "",
                    totalDetails?.dischargePatientTotal.toString() ?? "",
                  ],
                  [
                    "Medicine",
                    "Prescription Given",
                    totalDetails?.prescriptionGivenHLL.toString() ?? "",
                    totalDetails?.prescriptionGivenHSCC.toString() ?? "",
                    totalDetails?.prescriptionGivenTotal.toString() ?? "",
                  ],
                  [
                    "Medicine",
                    "Prescription Issued",
                    totalDetails?.prescriptionIssuedHLL.toString() ?? "",
                    totalDetails?.prescriptionIssuedHSCC.toString() ?? "",
                    totalDetails?.prescriptionIssuedTotal.toString() ?? "",
                  ],
                ],
              ),
            ],
          ).paddingSymmetric(vertical: 6.h, horizontal: 10.h);
        },
      );
    } else {
      return Container();
    }
  }

  Future<dynamic> logoutBottomSheet(BuildContext context) {
    return showModalBottomSheet(
      context: context,
      backgroundColor: Colors.transparent,
      constraints: BoxConstraints(maxHeight: SizeConfig.screenHeight * 0.4),
      builder: (context) {
        return SafeArea(
          child: Container(
            decoration: BoxDecoration(
              color: kWhiteColor,
              borderRadius: const BorderRadius.only(
                topLeft: Radius.circular(30),
                topRight: Radius.circular(30),
              ),
            ),
            child: Padding(
              padding: EdgeInsets.symmetric(vertical: 14.h, horizontal: 14.w),
              child: Column(
                children: [
                  SizedBox(height: 20.h),
                  const Icon(Icons.exit_to_app),
                  SizedBox(height: 27.h),
                  CommonText(
                    text: "Are you sure you want to logout?",
                    fontSize: 20.sp,
                    fontWeight: FontWeight.w600,
                    textColor: kBlackColor,
                    textAlign: TextAlign.center,
                  ),

                  SizedBox(height: 12.h),

                  CommonText(
                    text:
                    "If you logout you will not be able to use all the features of ABKAT",
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w600,
                    textColor: kBlackColor,
                    textAlign: TextAlign.center,
                  ),
                  const Spacer(),
                  Row(
                    children: [
                      Expanded(
                        flex: 1,
                        child: AppButton(
                          buttonColor: kPrimaryColor,
                          title: "Cancel",
                          textStyle: TextStyle(color: kWhiteColor),
                          onTap: () {
                            Navigator.of(context).pop();
                          },
                        ),
                      ),
                      SizedBox(width: 15.w),
                      Expanded(
                        flex: 1,
                        child: SizedBox(
                          height: 50.h,
                          child: OutlinedButton(
                            onPressed: () {
                              DataProvider().clearSession(context);
                            },
                            style: OutlinedButton.styleFrom(
                              side: BorderSide(
                                width: 1.0,
                                color: kPrimaryColor,
                              ),
                            ),
                            child: CommonText(
                              text: "Logout",
                              fontSize: 16.sp,
                              fontWeight: FontWeight.w600,
                              textColor: kBlackColor,
                              textAlign: TextAlign.center,
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
          ),
        );
      },
    );
  }

  String _formatFinancialYear(String raw) {
    final trimmed = raw.trim();
    if (trimmed.isEmpty) return "";
    if (trimmed.startsWith("*")) return trimmed;
    if (trimmed.toUpperCase().contains("FY")) {
      return "*$trimmed";
    }
    return "*FY($trimmed)";
  }
}

class _SuperAdminBarGroup {
  final String label;
  final int hll;
  final int hscc;

  const _SuperAdminBarGroup({
    required this.label,
    required this.hll,
    required this.hscc,
  });

  int get total => hll + hscc;
}

class _SuperAdminGraphTabs extends StatelessWidget {
  final String conductedSubtitle;
  final List<_SuperAdminBarGroup> conductedGroups;
  final String todaysSubtitle;
  final List<_SuperAdminBarGroup> todaysGroups;

  const _SuperAdminGraphTabs({
    required this.conductedSubtitle,
    required this.conductedGroups,
    required this.todaysSubtitle,
    required this.todaysGroups,
  });

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Padding(
        padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 6.h),
        child: Column(
          children: [
            Container(
              height: 45.h,
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(30.r),
                border: Border.all(color: Colors.grey.shade300),
              ),
              child: TabBar(
                indicatorSize: TabBarIndicatorSize.tab,
                indicatorPadding: EdgeInsets.zero,
                splashFactory: NoSplash.splashFactory,
                overlayColor: MaterialStateProperty.all(Colors.transparent),
                dividerColor: Colors.transparent,
                indicator: BoxDecoration(
                  borderRadius: BorderRadius.circular(25.r),
                  gradient: const LinearGradient(
                    colors: [kPrimaryColor, kPrimaryColor],
                  ),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.15),
                      blurRadius: 8,
                      offset: const Offset(0, 3),
                    ),
                  ],
                ),
                labelColor: Colors.white,
                unselectedLabelColor: Colors.grey.shade600,
                labelStyle: TextStyle(
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                ),
                unselectedLabelStyle: TextStyle(
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w500,
                ),
                tabs: const [
                  Tab(text: "Conducted Camps"),
                  Tab(text: "Today's Patients"),
                ],
              ),
            ),
            SizedBox(height: 12.h),
            SizedBox(
              height: 480.h,
              child: TabBarView(
                children: [
                  _SuperAdminBarChartCard(
                    subtitle: conductedSubtitle,
                    groups: conductedGroups,
                  ),
                  _SuperAdminBarChartCard(
                    subtitle: todaysSubtitle,
                    groups: todaysGroups,
                  ),
                ],
              ),
            ),
            SizedBox(height: 8.h),
          ],
        ),
      ),
    );
  }
}

class _SuperAdminBarChartCard extends StatelessWidget {
  final String subtitle;
  final List<_SuperAdminBarGroup> groups;

  const _SuperAdminBarChartCard({required this.subtitle, required this.groups});

  @override
  Widget build(BuildContext context) {
    const hllColor = Color(0xFF2AC1D1);
    const hsccColor = Color(0xFF8CD63F);

    return Padding(
      padding: EdgeInsets.symmetric(horizontal: 4.w, vertical: 4.h),
      child: Container(
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16.r),
          boxShadow: [
            BoxShadow(
              color: Colors.black.withOpacity(0.08),
              blurRadius: 8,
              spreadRadius: 0,
              offset: const Offset(0, 2),
            ),
          ],
        ),
        child: Padding(
          padding: EdgeInsets.fromLTRB(8.w, 12.h, 12.w, 16.h),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.end,
            children: [
              CommonText(
                text: subtitle,
                fontSize: 12.sp,
                textColor: kTextBlackColor,
                fontWeight: FontWeight.w500,
                textAlign: TextAlign.end,
              ),
              SizedBox(height: 4.h),
              Expanded(
                child: _SuperAdminBarChart(
                  groups: groups,
                  hllColor: hllColor,
                  hsccColor: hsccColor,
                ),
              ),
              SizedBox(height: 8.h),
              _SuperAdminLegend(hllColor: hllColor, hsccColor: hsccColor),
            ],
          ),
        ),
      ),
    );
  }
}

class _SuperAdminGraphTabsSkeleton extends StatelessWidget {
  const _SuperAdminGraphTabsSkeleton();

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Padding(
        padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 6.h),
        child: Column(
          children: [
            Container(
              height: 45.h,
              decoration: BoxDecoration(
                color: Colors.white,
                borderRadius: BorderRadius.circular(30.r),
                border: Border.all(color: Colors.grey.shade300),
              ),
              child: TabBar(
                indicatorSize: TabBarIndicatorSize.tab,
                indicatorPadding: EdgeInsets.zero,
                splashFactory: NoSplash.splashFactory,
                overlayColor: MaterialStateProperty.all(Colors.transparent),
                dividerColor: Colors.transparent,
                indicator: BoxDecoration(
                  borderRadius: BorderRadius.circular(25.r),
                  gradient: const LinearGradient(
                    colors: [kPrimaryColor, kPrimaryColor],
                  ),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.15),
                      blurRadius: 8,
                      offset: const Offset(0, 3),
                    ),
                  ],
                ),
                labelColor: Colors.white,
                unselectedLabelColor: Colors.grey.shade600,
                labelStyle: TextStyle(
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w600,
                ),
                unselectedLabelStyle: TextStyle(
                  fontSize: 13.sp,
                  fontWeight: FontWeight.w500,
                ),
                tabs: const [
                  Tab(text: "Conducted Camps"),
                  Tab(text: "Today's Patients"),
                ],
              ),
            ),
            SizedBox(height: 12.h),
            SizedBox(
              height: 480.h,
              child: TabBarView(
                children: const [
                  CommonSkeletonSuperAdminBarChartCard(),
                  CommonSkeletonSuperAdminBarChartCard(),
                ],
              ),
            ),
            SizedBox(height: 8.h),
          ],
        ),
      ),
    );
  }
}

class _SuperAdminBarChart extends StatelessWidget {
  final List<_SuperAdminBarGroup> groups;
  final Color hllColor;
  final Color hsccColor;

  const _SuperAdminBarChart({
    required this.groups,
    required this.hllColor,
    required this.hsccColor,
  });

  int _roundUpTo(int value, int step) {
    if (value <= 0) return step;
    return ((value + step - 1) ~/ step) * step;
  }

  int _calculateNiceStep(int maxValue) {
    if (maxValue <= 100) return 25;
    if (maxValue <= 500) return 100;
    if (maxValue <= 1000) return 250;
    if (maxValue <= 5000) return 1000;
    if (maxValue <= 10000) return 2000;
    if (maxValue <= 50000) return 10000;
    if (maxValue <= 100000) return 25000;
    return 50000;
  }

  @override
  Widget build(BuildContext context) {
    final maxValueRaw = groups.fold<int>(
      0,
          (maxValue, group) =>
          math.max(maxValue, math.max(group.hll, group.hscc)),
    );
    final step = _calculateNiceStep(maxValueRaw);
    final maxValue = _roundUpTo(maxValueRaw, step);
    final tickCount = 5;

    // Check if all values are non-zero to decide whether to show 0 on Y-axis
    final allNonZero = groups.every((g) => g.hll > 0 && g.hscc > 0);

    final ticks =
    List.generate(
      tickCount,
          (i) => (maxValue * i / (tickCount - 1)).round(),
    )
        .where((tick) => !allNonZero || tick > 0)
        .toList(); // Exclude 0 if all values are non-zero

    return LayoutBuilder(
      builder: (context, constraints) {
        final availableHeight = constraints.maxHeight;
        final xAxisLabelHeight = 30.h;
        final chartHeight = availableHeight - xAxisLabelHeight;

        return Column(
          children: [
            SizedBox(
              height: chartHeight,
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  // Y-axis labels
                  SizedBox(
                    width: 45.w,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      crossAxisAlignment: CrossAxisAlignment.end,
                      children:
                      ticks.reversed.map((value) {
                        return Padding(
                          padding: EdgeInsets.only(right: 8.w),
                          child: CommonText(
                            text: value.toString(),
                            fontSize: 10.sp,
                            textColor: Colors.grey.shade600,
                            fontWeight: FontWeight.w500,
                            textAlign: TextAlign.right,
                          ),
                        );
                      }).toList(),
                    ),
                  ),
                  // Chart area
                  Expanded(
                    child: Stack(
                      clipBehavior: Clip.none,
                      children: [
                        // Grid lines
                        Positioned.fill(
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: List.generate(
                              ticks.length,
                                  (index) =>
                                  Container(
                                    height: 1,
                                    color: Colors.grey.withOpacity(0.25),
                                  ),
                            ),
                          ),
                        ),
                        // Bar groups with floating total badges
                        Positioned.fill(
                          child: Row(
                            children: _buildBarGroups(
                              maxValue.toDouble(),
                              chartHeight,
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(height: 8.h),
            // X-axis labels
            SizedBox(
              height: xAxisLabelHeight - 8.h,
              child: Padding(
                padding: EdgeInsets.only(left: 45.w),
                child: Row(
                  children:
                  groups
                      .map(
                        (group) =>
                        Expanded(
                          child: CommonText(
                            text: group.label,
                            fontSize: 13.sp,
                            fontWeight: FontWeight.w600,
                            textColor: kTextBlackColor,
                            textAlign: TextAlign.center,
                          ),
                        ),
                  )
                      .toList(),
                ),
              ),
            ),
          ],
        );
      },
    );
  }

  List<Widget> _buildBarGroups(double maxValue, double chartHeight) {
    final barWidth = 48.w;
    final barSpacing = 8.w;

    return groups.map((group) {
      final hllHeight =
      maxValue <= 0 ? 0.0 : (group.hll / maxValue) * chartHeight;
      final hsccHeight =
      maxValue <= 0 ? 0.0 : (group.hscc / maxValue) * chartHeight;

      // Calculate badge offset based on visible bars only
      final visibleHeights = <double>[];
      if (group.hll > 0) visibleHeights.add(hllHeight);
      if (group.hscc > 0) visibleHeights.add(hsccHeight);
      final maxBarHeight =
      visibleHeights.isNotEmpty
          ? visibleHeights.reduce((a, b) => math.max(a, b))
          : 0.0;

      // Add extra offset for label height when labels are shown above the bars
      final labelAboveBarHeight = 10.sp + 4.h; // fontSize + spacing
      final hasLabelAboveBar = maxBarHeight < 40.h && visibleHeights.isNotEmpty;
      final badgeBottomOffset =
      hasLabelAboveBar
          ? maxBarHeight + labelAboveBarHeight + 10.h
          : maxBarHeight + 10.h;

      // Don't show total badge if total is zero
      final showTotalBadge = group.total > 0;

      return Expanded(
        child: Stack(
          clipBehavior: Clip.none,
          children: [
            // Bars
            Positioned.fill(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  _BarWithLabel(
                    height: hllHeight,
                    color: hllColor,
                    width: barWidth,
                    label: group.hll.toString(),
                    // Show actual value, not rounded
                    value: group.hll,
                  ),
                  SizedBox(width: barSpacing),
                  _BarWithLabel(
                    height: hsccHeight,
                    color: hsccColor,
                    width: barWidth,
                    label: group.hscc.toString(),
                    // Show actual value, not rounded
                    value: group.hscc,
                  ),
                ],
              ),
            ),
            // Floating total badge - only show if total > 0
            if (showTotalBadge)
              Positioned(
                bottom: badgeBottomOffset,
                left: 0,
                right: 0,
                child: Center(
                  child: Container(
                    padding: EdgeInsets.symmetric(
                      horizontal: 6.w,
                      vertical: 6.h,
                    ),
                    decoration: BoxDecoration(
                      color: const Color(0xFFE8E0F5),
                      borderRadius: BorderRadius.circular(10.r),
                    ),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        CommonText(
                          text: "${group.label} Total",
                          fontSize: 10.sp,
                          fontWeight: FontWeight.w600,
                          textColor: kPrimaryColor,
                          textAlign: TextAlign.center,
                        ),
                        CommonText(
                          text: group.total.toString(),
                          // Show actual value, not rounded
                          fontSize: 10.sp,
                          fontWeight: FontWeight.w700,
                          textColor: kPrimaryColor,
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  ),
                ),
              ),
          ],
        ),
      );
    }).toList();
  }
}

class _BarWithLabel extends StatelessWidget {
  final double height;
  final Color color;
  final double width;
  final String label;
  final int value;

  const _BarWithLabel({
    required this.height,
    required this.color,
    required this.width,
    required this.label,
    required this.value,
  });

  @override
  Widget build(BuildContext context) {
    // Don't show bar if value is zero
    if (value <= 0) {
      return SizedBox(width: width);
    }

    final showLabelInside = height >= 40.h;

    if (showLabelInside) {
      // Show label inside the bar
      return Container(
        width: width,
        height: height,
        decoration: BoxDecoration(
          color: color,
          borderRadius: BorderRadius.circular(6.r),
        ),
        child: Padding(
          padding: EdgeInsets.only(top: height * 0.25),
          child: Align(
            alignment: Alignment.topCenter,
            child: CommonText(
              text: label,
              fontSize: 10.sp,
              fontWeight: FontWeight.bold,
              textColor: Colors.white,
              textAlign: TextAlign.center,
            ),
          ),
        ),
      );
    } else {
      // Show label above the bar when bar height is too small
      return Column(
        mainAxisSize: MainAxisSize.min,
        mainAxisAlignment: MainAxisAlignment.end,
        children: [
          CommonText(
            text: label,
            fontSize: 10.sp,
            fontWeight: FontWeight.bold,
            textColor: color,
            textAlign: TextAlign.center,
          ),
          SizedBox(height: 4.h),
          Container(
            width: width,
            height: height,
            decoration: BoxDecoration(
              color: color,
              borderRadius: BorderRadius.circular(6.r),
            ),
          ),
        ],
      );
    }
  }
}

class _SuperAdminLegend extends StatelessWidget {
  final Color hllColor;
  final Color hsccColor;

  const _SuperAdminLegend({required this.hllColor, required this.hsccColor});

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        _LegendItem(color: hllColor, label: "HLL"),
        SizedBox(width: 30.w),
        _LegendItem(color: hsccColor, label: "HSCC"),
      ],
    );
  }
}

class _LegendItem extends StatelessWidget {
  final Color color;
  final String label;

  const _LegendItem({required this.color, required this.label});

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          width: 14.w,
          height: 14.w,
          decoration: BoxDecoration(
            color: color,
            borderRadius: BorderRadius.circular(3.r),
          ),
        ),
        SizedBox(width: 8.w),
        CommonText(
          text: label,
          fontSize: 13.sp,
          fontWeight: FontWeight.w600,
          textColor: kTextBlackColor,
          textAlign: TextAlign.center,
        ),
      ],
    );
  }
}
