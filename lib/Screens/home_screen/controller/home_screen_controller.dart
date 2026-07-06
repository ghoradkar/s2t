import 'dart:async';
import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:s2toperational/Modules/utilities/enums.dart';
import 'package:s2toperational/Modules/utilities/toast_manager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/utilities/data_provider.dart';
import 'package:s2toperational/Modules/utilities/device_Info_util.dart';
import 'package:s2toperational/Modules/common_widgets/CommonText.dart';
import 'package:s2toperational/Screens/admin_dashboard/model/conducted_camps_totals.dart';
import 'package:s2toperational/Screens/admin_dashboard/model/todays_patients_response.dart';
import 'package:s2toperational/Screens/forgot_password/controller/password_reset_controller.dart';
import 'package:s2toperational/Screens/login/models/login_response_model.dart';
import 'package:s2toperational/Screens/super_admin/controller/super_admin_controller.dart';
import 'package:s2toperational/Screens/home_screen/screen/update_password_dialog.dart';
import 'package:s2toperational/Screens/appointments_confirmed_list/screen/appointments_confirmed_list_screen.dart';
import 'package:s2toperational/Screens/calling_modules/controller/calling_dashboard_controller.dart';
import 'package:s2toperational/Screens/calling_modules/controller/expected_beneficiary_list_controller.dart';
import 'package:s2toperational/Screens/calling_modules/repository/calling_dashboard_repository.dart';
import 'package:s2toperational/Screens/calling_modules/screens/calling_dashboard_screen.dart';
import 'package:s2toperational/Screens/calling_modules/screens/expected_beneficiary_list.dart';
import 'package:s2toperational/Screens/d2d_availability/controller/d2d_availability_controller.dart';
import 'package:s2toperational/Screens/d2d_availability/screens/d2d_availability_screen.dart';
import 'package:s2toperational/Screens/camp_calendar/screen/camp_calendar_screen.dart';
import 'package:s2toperational/Screens/d2d_teams/screen/d2d_teams_screen.dart';
import 'package:s2toperational/Screens/liver_scanning/screen/liver_scanning_screen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/packet_allocation_screen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/packet_collection_screen.dart';
import 'package:s2toperational/Screens/medicine_delivery_menu/screens/medicine_delivery_dash.dart';
import 'package:s2toperational/Screens/s2t_patient_app/screen/S2TPatientAppScreen.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/screens/d2d_physical_examination_details/d2d_physical_examination_details_screen.dart';
import 'package:s2toperational/Screens/team_camp_mapping/screen/team_camp_mapping_screen.dart';
import 'package:s2toperational/Screens/team_photos/screen/team_photos_screen.dart';
import 'package:s2toperational/Screens/patient_registration/controller/select_camp_controller.dart';
import 'package:s2toperational/Screens/patient_registration/screen/select_camp_screen.dart';
import 'package:s2toperational/Screens/ct_assignment/screen/ct_assignment_screen.dart';
import 'package:s2toperational/Screens/camp_creation/controllers/camp_creation_controller.dart';
import 'package:s2toperational/Screens/camp_creation/screens/camp_creation_screen.dart';
import 'package:s2toperational/Screens/camp_readiness_form/screens/camp_readiness_form_screen.dart';
import 'package:s2toperational/Screens/d2d_team/screen/d2d_team_screen.dart';
import 'package:s2toperational/Screens/device_and_resource_mapping/screens/device_allocation_screen.dart';
import 'package:s2toperational/Screens/expense_claim/screen/expense_claim_dashboard_screen.dart';
import 'package:s2toperational/Screens/resource_re_mapping/screens/resource_re_mapping_camp_list_screen.dart';
import 'package:s2toperational/Screens/appointment_sample_collection_ct/screens/ct_appointment_list_screen.dart';
import 'package:s2toperational/Screens/health_screening_details/screens/camp_for_health_screening_d2d_screen/camp_for_health_screening_d2d_screen.dart';
import 'package:s2toperational/Screens/health_screening_details/screens/camp_for_health_screening_screen/camp_for_health_screening_screen.dart';
import 'package:s2toperational/Screens/payment_and_invoice/screens/payment_invoice_segment_screen.dart';
import 'package:s2toperational/Screens/user_attendance/screens/user_attendance_screen.dart';
import 'package:s2toperational/Screens/acknowledgement/screens/select _camp_acknowledgement_screen.dart';
import 'package:s2toperational/Screens/daily_work_dashboard/screen/daily_work_dashboard_screen.dart';
import '../repository/home_screen_repository.dart';

class HomeScreenController extends GetxController {
  final _repo = HomeScreenRepository();

  final scaffoldKey = GlobalKey<ScaffoldState>();

  // User info
  int dESGID = 0;
  String designation = '';
  bool? isFixedPay;
  String appVersion = '';

  // Camp toggle
  bool regularCamp = true;
  bool doorToDoorCamp = false;
  bool isShowRadioCamp = true;

  // Menu
  List<DashboardMenu> menuList = [];

  // Admin dashboard data
  ConductedCampsResponse? campsResponse;
  ConductedCampsTotals? conductedTotals;
  TodaysPatientsResponse? todaysPatientsResponse;
  TodaysPatientsTotals? todaysTotals;
  bool isLoadingAdminData = false;

  // Connectivity
  bool? isOnline;
  bool _adminLoaded = false;
  late final StreamSubscription<List<ConnectivityResult>> _connSub;

  SuperAdminController? superAdminController;

  @override
  void onInit() {
    super.onInit();
    final LoginResponseModel? loginData = DataProvider().getParsedUserData();
    dESGID = loginData?.output?.first.dESGID ?? 0;
    designation = loginData?.output?.first.designation ?? '';
    isFixedPay = loginData?.output?.first.isFixedPay;

    if (dESGID == 166) {
      superAdminController = Get.put(SuperAdminController());
    }

    _checkRegular();
    _buildMenu();
    _getAppVersion();

    _connSub = Connectivity().onConnectivityChanged.listen((results) {
      final result =
          results.isNotEmpty ? results.first : ConnectivityResult.none;
      _onConnectivityChanged(result);
    });

    WidgetsBinding.instance.addPostFrameCallback((_) async {
      final results = await Connectivity().checkConnectivity();
      final result =
          results.isNotEmpty ? results.first : ConnectivityResult.none;
      _onConnectivityChanged(result);

      final int passwordChangeDays =
          loginData?.output?.first.passwordChageDays ?? 0;
      if (passwordChangeDays >= 30) {
        ///Todo reset password uncomment when need to check reset password functionality
        // _showPasswordExpiredAlert();
      }
    });
  }

  @override
  void onClose() {
    _connSub.cancel();
    if (dESGID == 166) {
      Get.delete<SuperAdminController>();
    }
    super.onClose();
  }

  void _onConnectivityChanged(ConnectivityResult result) {
    isOnline = result != ConnectivityResult.none;
    update();
    if (isOnline == false) return;
    if (!_adminLoaded) {
      _adminLoaded = true;
      _checkIfAdminDashboard();
    }
  }

  Future<void> retry() async {
    if (isOnline == false) return;
    isLoadingAdminData = true;
    update();
    _adminLoaded = false;
    await _checkIfAdminDashboard();
    isLoadingAdminData = false;
    update();
  }

  Future<void> _checkIfAdminDashboard() async {
    if (dESGID == 51) {
      isLoadingAdminData = true;
      update();

      final camps = await _repo.fetchConductedCamps();
      if (camps != null) {
        campsResponse = camps;
        conductedTotals = ConductedCampsTotals.fromRows(camps.output);
      } else {
        ToastManager.toast('Failed to load conducted camps');
      }

      final date = DateFormat('yyyy-MM-dd').format(DateTime.now());
      final patients = await _repo.fetchTodaysPatients(date);
      if (patients != null) {
        todaysPatientsResponse = patients;
        todaysTotals = TodaysPatientsTotals.fromRows(patients.output);
      } else {
        ToastManager.toast("Failed to load today's patients");
      }

      isLoadingAdminData = false;
      update();
    } else if (dESGID == 166) {
      isLoadingAdminData = true;
      update();
      await superAdminController?.checkInternetSuperAdmin(showLoader: false);
      isLoadingAdminData = false;
      update();
    }
  }

  void _checkRegular() {
    if (DataProvider().getRegularCamp()) {
      doorToDoorCamp = false;
      regularCamp = true;
    } else {
      doorToDoorCamp = true;
      regularCamp = false;
    }
  }

  void _buildMenu() {
    menuList.clear();
    isShowRadioCamp = true;

    if (dESGID == 30) {
      isShowRadioCamp = false;
      menuList.addAll([
        DashboardMenu.CallingList,
        DashboardMenu.CallingDashboard,
        DashboardMenu.UserAttendance,
      ]);
    } else if (dESGID == 51) {
      isShowRadioCamp = false;
      menuList.addAll([
        DashboardMenu.CampCalendar,
        DashboardMenu.DailyWorkDashboard,
        DashboardMenu.LiverScanning,
        DashboardMenu.S2TPatientApp,
        DashboardMenu.D2DTeams,
      ]);
    } else if (dESGID == 26) {
      isShowRadioCamp = false;
      menuList.addAll([
        DashboardMenu.CampCalendar,
        DashboardMenu.DailyWorkDashboard,
        DashboardMenu.LiverScanning,
        DashboardMenu.D2DTeams,
        DashboardMenu.S2TPatientApp,
        DashboardMenu.UserAttendance,
      ]);
    } else if (dESGID == 166) {
      isShowRadioCamp = false;
      menuList.addAll([
        DashboardMenu.CampCalendar,
        DashboardMenu.LiverScanning,
        DashboardMenu.S2TPatientApp,
        DashboardMenu.D2DTeams,
      ]);
    } else if (designation == 'Vice President') {
      isShowRadioCamp = false;
      menuList.addAll([
        DashboardMenu.CampCalendar,
        DashboardMenu.DailyWorkDashboard,
        DashboardMenu.LiverScanning,
        DashboardMenu.D2DTeams,
        DashboardMenu.S2TPatientApp,
      ]);
    } else {
      if (regularCamp) {
        _regularCampMenu();
      } else {
        _doorToDoorCampBetaMenu();
      }
    }
    update();
  }

  void switchToRegularCamp() {
    doorToDoorCamp = false;
    regularCamp = true;
    DataProvider().isRegularCamp(true);
    _buildMenu();
  }

  void switchToDoorToDoorCamp() {
    doorToDoorCamp = true;
    regularCamp = false;
    DataProvider().isRegularCamp(false);
    _buildMenu();
  }

  void _regularCampMenu() {
    if (dESGID == 34) {
      menuList.addAll([
        DashboardMenu.UserAttendance,
        DashboardMenu.HealthScreeningDetails,
        DashboardMenu.ELearning,
      ]);
    } else if (dESGID == 173 || dESGID == 172) {
      menuList.add(DashboardMenu.UserAttendance);
    } else if (dESGID == 75) {
      isShowRadioCamp = true;
      menuList.addAll([
        DashboardMenu.CampCalendar,
        DashboardMenu.UserAttendance,
      ]);
    } else if (dESGID == 35) {
      menuList.addAll([
        DashboardMenu.HealthScreeningDetails,
        DashboardMenu.TeamPhotos,
        DashboardMenu.PatientRegistration,
        DashboardMenu.UserAttendance,
        DashboardMenu.FingerPrintUpload,
        DashboardMenu.Acknowledgement,
        DashboardMenu.ELearning,
      ]);
    } else if (dESGID == 29) {
      menuList.addAll([
        DashboardMenu.DeviceAndResourceMapping,
        DashboardMenu.ResourceReMapping,
        DashboardMenu.CampCreation,
        DashboardMenu.ExpenseClaim,
        DashboardMenu.CampCalendar,
        DashboardMenu.CampReadinessForm,
        DashboardMenu.HealthScreeningDetails,
        DashboardMenu.UserAttendance,
        DashboardMenu.FingerPrintUpload,
        DashboardMenu.Acknowledgement,
        DashboardMenu.ELearning,
        DashboardMenu.TeamPhotos,
      ]);
    } else if (dESGID == 92) {
      menuList.addAll([
        DashboardMenu.TeamPhotos,
        DashboardMenu.CampCreation,
        DashboardMenu.DeviceAndResourceMapping,
        DashboardMenu.ResourceReMapping,
        DashboardMenu.CampReadinessForm,
        DashboardMenu.ExpenseClaim,
        DashboardMenu.CampCalendar,
        DashboardMenu.HealthScreeningDetails,
      ]);
    }
  }

  void _doorToDoorCampBetaMenu() {
    if (dESGID == 34) {
      menuList.addAll([
        DashboardMenu.UserAttendance,
        DashboardMenu.D2DPhysicalExaminationDetails,
        DashboardMenu.HealthScreeningDetails,
      ]);
      if (isFixedPay == false) menuList.add(DashboardMenu.PaymentAndInvoice);
    } else if (dESGID == 75) {
      isShowRadioCamp = true;
      menuList.addAll([
        DashboardMenu.CampCalendar,
        DashboardMenu.UserAttendance,
      ]);
    } else if (dESGID == 157) {
      isShowRadioCamp = true;
      menuList.addAll([
        DashboardMenu.MedicineDeliveryMenu,
        DashboardMenu.AppointmentAndSampleCollectionOfCT,
        DashboardMenu.UserAttendance,
      ]);
    } else if (dESGID == 173 || dESGID == 172) {
      isShowRadioCamp = true;
      menuList.addAll([
        DashboardMenu.MedicineDeliveryMenu,
        DashboardMenu.CampCalendar,
        DashboardMenu.UserAttendance,
      ]);
    } else if (dESGID == 35) {
      menuList.addAll([
        DashboardMenu.TeamPhotos,
        DashboardMenu.CampReadinessForm,
        DashboardMenu.UserAttendance,
        DashboardMenu.DailyWorkDashboard,
        DashboardMenu.PatientRegistration,
        DashboardMenu.AppointmentAndSampleCollectionOfCT,
        DashboardMenu.HealthScreeningDetails,
        DashboardMenu.MedicineDeliveryMenu,
        DashboardMenu.PaymentAndInvoice,
        DashboardMenu.Acknowledgement,
        DashboardMenu.ELearning,
      ]);
    } else if (dESGID == 29) {
      menuList.addAll([
        DashboardMenu.AppointmentConfirmedList,
        DashboardMenu.PacketAllocation,
        DashboardMenu.DailyWorkDashboard,
        DashboardMenu.TeamCampMapping,
        DashboardMenu.CampCreation,
        DashboardMenu.D2DTeam,
        DashboardMenu.CampCalendar,
        DashboardMenu.HealthScreeningDetails,
        DashboardMenu.UserAttendance,
        DashboardMenu.D2DCampActivity,
        DashboardMenu.TeamPhotos,
      ]);
    } else if (dESGID == 92) {
      menuList.addAll([
        DashboardMenu.MedicineDeliveryMenu,
        DashboardMenu.TeamPhotos,
        DashboardMenu.DailyWorkDashboard,
        DashboardMenu.AppointmentConfirmedList,
        DashboardMenu.CampCreation,
        DashboardMenu.CTAssignment,
        DashboardMenu.D2DTeam,
        DashboardMenu.CampCalendar,
        DashboardMenu.TeamCampMapping,
        DashboardMenu.HealthScreeningDetails,
        DashboardMenu.UserAttendance,
        DashboardMenu.Acknowledgement,
        DashboardMenu.ELearning,
      ]);
    } else if (dESGID == 139) {
      menuList.addAll([
        DashboardMenu.DailyWorkDashboard,
        DashboardMenu.AppointmentConfirmedList,
        DashboardMenu.PacketAllocation,
        DashboardMenu.PacketCollection,
        DashboardMenu.CampCreation,
        DashboardMenu.CTAssignment,
        DashboardMenu.D2DTeam,
        DashboardMenu.CampCalendar,
        DashboardMenu.TeamCampMapping,
        DashboardMenu.HealthScreeningDetails,
        DashboardMenu.UserAttendance,
        DashboardMenu.FingerPrintUpload,
        DashboardMenu.Acknowledgement,
        DashboardMenu.ELearning,
      ]);
    } else if (dESGID == 141) {
      menuList.addAll([
        DashboardMenu.HealthScreeningDetails,
        DashboardMenu.D2DPhysicalExaminationDetails,
        DashboardMenu.ELearning,
        DashboardMenu.UserAttendance,
      ]);
    }
  }

  Future<void> _getAppVersion() async {
    final info = await DeviceInfoUtil().getPackageInfo();
    appVersion = info.version;
    update();
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

  void _showPasswordExpiredAlert() {
    Get.put(PasswordResetController());
    Get.dialog(
      PopScope(
        canPop: false,
        child: AlertDialog(
          title: CommonText(
            text: 'Alert',
            fontSize: 16,
            fontWeight: FontWeight.w600,
            textColor: Colors.black,
            textAlign: TextAlign.left,
          ),
          content: CommonText(
            text: 'Your password is expired,\nplease reset password',
            fontSize: 14,
            fontWeight: FontWeight.w400,
            textColor: Colors.black,
            textAlign: TextAlign.left,
          ),
          actions: [
            GetBuilder<PasswordResetController>(
              builder:
                  (ctrl) =>
                      ctrl.isSendingOtp
                          ? const Padding(
                            padding: EdgeInsets.all(8),
                            child: CircularProgressIndicator(),
                          )
                          : TextButton(
                            onPressed: () async {
                              await ctrl.sendOtp();
                              if (ctrl.otpSent) {
                                Get.back();
                                _showUpdatePasswordDialog();
                              }
                            },
                            child: CommonText(
                              text: 'UPDATE PASSWORD',
                              fontSize: 14,
                              fontWeight: FontWeight.w600,
                              textColor: kPrimaryColor,
                              textAlign: TextAlign.center,
                            ),
                          ),
            ),
          ],
        ),
      ),
      barrierDismissible: false,
    );
  }

  void _showUpdatePasswordDialog() {
    Get.dialog(
      const PopScope(canPop: false, child: UpdatePasswordDialog()),
      barrierDismissible: false,
    ).then((_) => Get.delete<PasswordResetController>(force: true));
  }

  void pushToNextScreen(DashboardMenu dashboardMenu) {
    switch (dashboardMenu) {
      case DashboardMenu.CampCalendar:
        Get.to(() => CampCalendarScreen());
        break;
      case DashboardMenu.CallingList:
        if (!Get.isRegistered<ExpectedBeneficiaryListController>()) {
          Get.put(ExpectedBeneficiaryListController());
        }
        Get.to(() => const ExpectedBeneficiaryList());
        break;
      case DashboardMenu.CallingDashboard:
        Get.delete<CallingDashboardController>(force: true);
        Get.put(
          CallingDashboardController(repository: CallingDashboardRepository()),
        );
        Get.to(() => const CallingDashboardScreen());
        break;
      case DashboardMenu.UserAttendance:
        Get.to(() => const UserAttendanceScreen());
        break;
      case DashboardMenu.PatientRegistration:
        if (regularCamp) {
          Get.delete<SelectCampController>(force: true);
          final sc = Get.put(SelectCampController());
          sc.navCampType = '1';
          Get.to(() => const SelectCampScreen());
        } else {
          Get.to(
            () => const AppointmentsConfirmedListScreen(),
            arguments: DashboardMenu.PatientRegistration,
          );
        }
        break;
      case DashboardMenu.D2DTeams:
        Get.to(() => D2DTeamsScreen(title: 'Android Total Patient'));
        break;
      case DashboardMenu.DailyWorkDashboard:
        Get.to(() => const DailyWorkDashboardScreen());
        break;
      case DashboardMenu.LiverScanning:
        Get.to(() => const LiverScanningScreen());
        break;
      case DashboardMenu.S2TPatientApp:
        Get.to(() => const S2tPatientAppScreen());
        break;
      case DashboardMenu.HealthScreeningDetails:
        if (DataProvider().getRegularCamp()) {
          Get.to(() => CampForHealthScreeningScreen(testID: 3));
        } else {
          Get.to(() => CampForHealthScreeningD2DScreen(testID: 16));
        }
        break;
      case DashboardMenu.Acknowledgement:
        Get.to(() => AcknowledgementCampListScreenNew(isD2D: doorToDoorCamp));
        break;
      case DashboardMenu.ELearning:
        openYouTubeChannel();
        break;
      case DashboardMenu.OtherMenu:
        break;
      case DashboardMenu.FingerPrintUpload:
        break;
      case DashboardMenu.DeviceAndResourceMapping:
        Get.to(() => const DeviceAllocationScreen());
        break;
      case DashboardMenu.ResourceReMapping:
        Get.to(() => const ResourceReMappingCampListScreen());
        break;
      case DashboardMenu.CampApproval:
        break;
      case DashboardMenu.CampCreation:
        Get.delete<CampCreationController>(force: true);
        Get.put(CampCreationController());
        Get.to(() => const CampCreationScreen());
        break;
      case DashboardMenu.ExpenseClaim:
        Get.to(() => const ExpenseClaimDashboardScreen());
        break;
      case DashboardMenu.CampReadinessForm:
        Get.to(() => const CampReadinessFormScreen());
        break;
      case DashboardMenu.PacketAllocation:
        Get.to(() => const PacketAllocationScreen());
        break;
      case DashboardMenu.PacketCollection:
        Get.to(() => PacketCollectionScreen());
        break;
      case DashboardMenu.MedicineDelivery:
        break;
      case DashboardMenu.CTAssignment:
        Get.to(() => const CTAssignmentScreen());
        break;
      case DashboardMenu.D2DTeam:
        Get.to(() => const D2DTeamScreen());
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
        Get.to(() => D2DPhysicalExaminationDetailsScreen());
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
        Get.to(() => const CTAppointmentListScreen());
        break;
      case DashboardMenu.MedicineDeliveryMenu:
        Get.to(() => MedicineDeliveryDash());
        break;
      case DashboardMenu.PaymentAndInvoice:
        Get.to(() => PaymentInvoiceSegmentScreen());
        break;
      case DashboardMenu.PickupMedicinePacket:
        break;
      case DashboardMenu.TeamPhotos:
        Get.to(
          () => TeamPhotosScreen(initialCampType: regularCamp ? '1' : '3'),
        );
        break;
    }
  }
}
