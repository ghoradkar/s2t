import 'dart:async';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/Json_Class/AdminDashboard/ConductedCampsTotals.dart';
import 'package:s2toperational/Modules/Json_Class/AdminDashboard/TodaysPatientsResponse.dart';
import 'package:s2toperational/Screens/login/models/login_response_model.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Screens/admin_dashboard/screen/admin_dashboard_widget.dart';
import 'package:s2toperational/Screens/daily_work_dashboard/screen/daily_work_dashboard_screen.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/calling_dashboard_controller.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/expected_beneficiary_list_controller.dart';
import 'package:s2toperational/Screens/calling_modules/repository/calling_dashboard_repository.dart';
import 'package:s2toperational/Screens/calling_modules/screens/calling_dashboard_screen.dart';
import 'package:s2toperational/Screens/calling_modules/screens/expected_beneficiary_list.dart';
import 'package:s2toperational/Screens/d2d_availability/controller/d2d_availability_controller.dart';
import 'package:s2toperational/Screens/d2d_availability/screens/d2d_availability_screen.dart';
import 'package:s2toperational/Screens/HomeScreen/DashboardMenuRow/DashboardMenuOptions.dart';
import 'package:s2toperational/Screens/camp_calendar/screen/camp_calendar_screen.dart';
import 'package:s2toperational/Screens/d2d_teams/screen/D2DTeamsScreen.dart';
import 'package:s2toperational/Screens/liver_scanning/screen/LiverScanningScreen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/packet_allocation_screen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/packet_collection_screen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/packet_receive_screen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/medicine_delivery_dash.dart';
import 'package:s2toperational/Screens/s2t_patient_app/screen/S2TPatientAppScreen.dart';
import 'package:s2toperational/Screens/super_admin/controller/super_admin_controller.dart';
import 'package:s2toperational/Screens/super_admin/screens/super_admin_dashboard.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/D2DPhysicalExaminationDetailsScreen/D2DPhysicalExaminationDetailsScreen.dart';
import 'package:s2toperational/Screens/team_camp_mapping/screen/team_camp_mapping_screen.dart';
import 'package:s2toperational/Screens/team_photos/screen/team_photos_screen.dart';
import 'package:s2toperational/Screens/patient_registration/controller/select_camp_controller.dart';
import 'package:s2toperational/Screens/patient_registration/screen/select_camp_screen.dart';
import 'package:url_launcher/url_launcher.dart';
import '../../Modules/Enums/Enums.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/utilities/DeviceInfoUtil.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import '../appointments_confirmed_list/screen/appointments_confirmed_list_screen.dart';
import '../ct_assignment/screen/ct_assignment_screen.dart';
import '../camp_creation/controllers/camp_creation_controller.dart';
import '../camp_creation/screens/camp_creation_screen.dart';
import '../camp_readiness_form/screens/camp_readiness_form_screen.dart';
import '../d2d_team/screen/d2d_team_screen.dart';
import '../device_and_resource_mapping/screens/device_allocation_screen.dart';
import '../expense_claim/screen/expense_claim_dashboard_screen.dart';
import '../resource_re_mapping/screens/resource_re_mapping_camp_list_screen.dart';
import '../SideDrawerMenu/SideDrawerMenu.dart';
import '../appointment_sample_collection_ct/screens/ct_appointment_list_screen.dart';
import '../health_screening_details/screens/camp_for_health_screening_d2d_screen/camp_for_health_screening_d2d_screen.dart';
import '../health_screening_details/screens/camp_for_health_screening_screen/camp_for_health_screening_screen.dart';
import '../payment_and_invoice/screens/payment_invoice_segment_screen.dart';
import '../user_attendance/screens/user_attendance_screen.dart';
import '../acknowledgement/screens/select _camp_acknowledgement_screen.dart';

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
  SuperAdminController? _superAdminController;
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

    if (dESGID == 166) {
      _superAdminController = Get.put(SuperAdminController());
    }

    checkRegular();
    updateDahsboardMenu();
    getAppVersion();

    _isOnline = null;

    _connSub = Connectivity().onConnectivityChanged.listen((
      List<ConnectivityResult> results,
    ) {
      final result =
          results.isNotEmpty ? results.first : ConnectivityResult.none;
      onConnectivityChanged(result);
    });

    // Check current connectivity on mount â€” the stream only fires on changes,
    // so if the device is already online the APIs would never be called otherwise.
    WidgetsBinding.instance.addPostFrameCallback((_) async {
      final results = await Connectivity().checkConnectivity();
      final result =
          results.isNotEmpty ? results.first : ConnectivityResult.none;
      onConnectivityChanged(result);
    });
  }

  @override
  void dispose() {
    _connSub.cancel();
    if (dESGID == 166) {
      Get.delete<SuperAdminController>();
    }
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
              if (dESGID == 166 && _superAdminController != null)
                SuperAdminDashboard(controller: _superAdminController!),
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
                                desigId: dESGID,
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
    final connected = result != ConnectivityResult.none;

    if (!connected) {
      if (mounted) setState(() => _isOnline = false);
      return;
    }

    if (mounted) setState(() => _isOnline = true);

    /// ðŸ”¥ LOAD DASHBOARD ONLY ONCE WHEN ONLINE
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

  void apiCampConducted(
    ConductedCampsResponse? response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();
    if (!success || response == null) {
      print(
        'ConductedCamp FAILED: $errorMessage  resp=${response?.status}/${response?.message}',
      );
      ToastManager.toast(errorMessage);
      return;
    }

    campsResponse = response;
    conductedTotals = ConductedCampsTotals.fromRows(response.output);
    print(
      'Conducted totals: total=${conductedTotals?.total} reg=${conductedTotals?.regular} d2d=${conductedTotals?.d2d} bill=${conductedTotals?.billable}',
    );
    setState(() {});
  }

  void apiTodaysPatient(
    TodaysPatientsResponse? response,
    String errorMessage,
    bool success,
  ) {
    ToastManager.hideLoader();

    if (!success || response == null) {
      print(
        'TodaysPatient FAILED: $errorMessage resp=${response?.status}/${response?.message}',
      );
      ToastManager.toast(errorMessage);
      return;
    }

    todaysPatientsResponse = response;
    todaysTotals = TodaysPatientsTotals.fromRows(response.output);

    print(
      'Todays totals: total=${todaysTotals?.total} reg=${todaysTotals?.regular} d2d=${todaysTotals?.d2d}',
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
      await _superAdminController?.checkInternetSuperAdmin(showLoader: false);
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
      menuList.add(DashboardMenu.CallingDashboard);
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
      // menuList.add(DashboardMenu.acknowledgement);
      menuList.add(DashboardMenu.ELearning);
    } else if (dESGID == 173 || dESGID == 172) {
      menuList.add(DashboardMenu.UserAttendance);
    } else if (dESGID == 75) {
      isShowRadioCamp = true;
      // topHeight = 200;
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.UserAttendance);
    } else if (dESGID == 35) {
      menuList.add(DashboardMenu.HealthScreeningDetails);
      menuList.add(DashboardMenu.TeamPhotos);
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
      menuList.add(DashboardMenu.TeamPhotos);
    } else if (dESGID == 92) {
      menuList.add(DashboardMenu.TeamPhotos);
      menuList.add(DashboardMenu.CampCreation);
      menuList.add(DashboardMenu.DeviceAndResourceMapping);
      menuList.add(DashboardMenu.ResourceReMapping);
      menuList.add(DashboardMenu.CampReadinessForm);
      menuList.add(DashboardMenu.ExpenseClaim);
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.HealthScreeningDetails);
    }
    // else if (dESGID == 141) {
    //   menuList.add(DashboardMenu.UserAttendance);
    //   menuList.add(DashboardMenu.ELearning);
    // }
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
      menuList.add(DashboardMenu.AppointmentAndSampleCollectionOfCT);
      menuList.add(DashboardMenu.UserAttendance);
    } else if (dESGID == 173 || dESGID == 172) {
      isShowRadioCamp = true;
      menuList.add(DashboardMenu.MedicineDeliveryMenu);
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.UserAttendance);
    } else if (dESGID == 35) {
      menuList.add(DashboardMenu.TeamPhotos);
      menuList.add(DashboardMenu.CampReadinessForm);
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.DailyWorkDashboard);
      menuList.add(DashboardMenu.PatientRegistration);
      menuList.add(DashboardMenu.AppointmentAndSampleCollectionOfCT);
      menuList.add(DashboardMenu.HealthScreeningDetails);
      menuList.add(DashboardMenu.MedicineDeliveryMenu);
      menuList.add(DashboardMenu.PaymentAndInvoice);
      menuList.add(DashboardMenu.Acknowledgement);
      menuList.add(DashboardMenu.ELearning);
    } else if (dESGID == 29) {
      menuList.add(DashboardMenu.AppointmentConfirmedList);
      menuList.add(DashboardMenu.PacketAllocation);
      menuList.add(DashboardMenu.DailyWorkDashboard);
      // menuList.add(DashboardMenu.PacketReceive);
      menuList.add(DashboardMenu.TeamCampMapping);
      menuList.add(DashboardMenu.CampCreation);
      menuList.add(DashboardMenu.D2DTeam);
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.HealthScreeningDetails);
      menuList.add(DashboardMenu.UserAttendance);
      menuList.add(DashboardMenu.D2DCampActivity);
      menuList.add(DashboardMenu.TeamPhotos);
    } else if (dESGID == 92) {
      menuList.add(DashboardMenu.MedicineDeliveryMenu);
      menuList.add(DashboardMenu.TeamPhotos);
      menuList.add(DashboardMenu.DailyWorkDashboard);
      menuList.add(DashboardMenu.AppointmentConfirmedList);
      menuList.add(DashboardMenu.CampCreation);
      menuList.add(DashboardMenu.CTAssignment);
      menuList.add(DashboardMenu.D2DTeam);
      menuList.add(DashboardMenu.CampCalendar);
      menuList.add(DashboardMenu.TeamCampMapping);
      // menuList.add(DashboardMenu.CommonBeneficiaryList);
      menuList.add(DashboardMenu.HealthScreeningDetails);
      menuList.add(DashboardMenu.UserAttendance);
      // menuList.add(DashboardMenu.FingerPrintUpload);
      menuList.add(DashboardMenu.Acknowledgement);
      menuList.add(DashboardMenu.ELearning);
    } else if (dESGID == 139) {
      menuList.add(DashboardMenu.DailyWorkDashboard);
      menuList.add(DashboardMenu.AppointmentConfirmedList);
      menuList.add(DashboardMenu.PacketAllocation);
      // menuList.add(DashboardMenu.PacketReceive);
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
      menuList.add(DashboardMenu.HealthScreeningDetails);
      menuList.add(DashboardMenu.D2DPhysicalExaminationDetails);
      menuList.add(DashboardMenu.ELearning);
      menuList.add(DashboardMenu.UserAttendance);
      // menuList.add(DashboardMenu.D2DAvailabilityScreening);
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
      case DashboardMenu.CallingDashboard:
        Get.delete<CallingDashboardController>(force: true);
        Get.put(
          CallingDashboardController(repository: CallingDashboardRepository()),
        );
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => const CallingDashboardScreen(),
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
        if (regularCamp) {
          Get.delete<SelectCampController>(force: true);
          final sc = Get.put(SelectCampController());
          sc.navCampType = '1';
          Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => const SelectCampScreen()),
          );
        } else {
          Get.to(
            () => const AppointmentsConfirmedListScreen(),
            arguments: DashboardMenu.PatientRegistration,
          );
        }
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
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (_) => AcknowledgementCampListScreenNew(isD2D: doorToDoorCamp),
          ),
        );
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
        Get.delete<CampCreationController>(force: true);
        Get.put(CampCreationController());
        Get.to(() => const CampCreationScreen());
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
        Get.to(() => const CTAssignmentScreen());
        break;
      // case DashboardMenu.PacketReceive:
      //   Navigator.push(
      //     context,
      //     MaterialPageRoute(builder: (context) => const PacketReceiveScreen()),
      //   );
      //   break;
      case DashboardMenu.D2DTeam:
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => const D2DTeamScreen()),
        );
        break;
      case DashboardMenu.TeamCampMapping:
        Get.to(() => const TeamCampMappingScreen());
        break;
      case DashboardMenu.AppointmentConfirmedList:
        Get.to(() => const AppointmentsConfirmedListScreen());
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
        Get.to(
          () => const D2DAvailabilityScreen(),
          binding: BindingsBuilder.put(() => D2DAvailabilityController()),
        );
        break;
      case DashboardMenu.MedicineReturn:
        break;
      case DashboardMenu.AppointmentAndSampleCollectionOfCT:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => const CTAppointmentListScreen(),
          ),
        );
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
      case DashboardMenu.TeamPhotos:
        Navigator.push(
          context,
          MaterialPageRoute(
            builder:
                (context) =>
                    TeamPhotosScreen(initialCampType: regularCamp ? '1' : '3'),
          ),
        );
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
          width: MediaQuery.of(context).size.width,
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
}
