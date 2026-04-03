// ignore_for_file: file_names

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Views/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/team_photos/model/attendance_details_response.dart';
import 'package:s2toperational/Screens/team_photos/model/camp_list_response.dart';
import 'package:s2toperational/Screens/team_photos/model/teams_details_response.dart';
import 'package:s2toperational/Screens/team_photos/repository/team_photos_repository.dart';

class TeamPhotosController extends GetxController {
  final _repository = TeamPhotosRepository();
  final _picker = ImagePicker();

  // ─── Nav args (set before Get.to) ─────────────────────────────────────────────

  String navFromDate = '';
  String navToDate = '';
  String navOrganizationId = '';
  String navDivisionId = '';
  String navDistLgd = '';
  String navTalukaId = '';
  String navLabCode = '';

  /// Camp type selected in the dashboard before navigating here.
  /// "1" = Regular, "3" = D2D.  Set this before calling Get.to().
  String navCampType = '1';

  // ─── User session ─────────────────────────────────────────────────────────────

  int empCode = 0;
  int dESGID = 0;
  String distLgdCode = '';
  String talLgdCode = '';

  // ─── Camp type ────────────────────────────────────────────────────────────────
  // campType: "1" = Regular, "3" = D2D, "6" = MMU T2T
  // statusType: "1" = D2D/MMU, "2" = Regular

  final campType = '1'.obs;
  final statusType = '2'.obs;

  bool get isD2DOrMMU => campType.value == '3' || campType.value == '6';

  /// DESGID 29, 162, 92, 139, 136 can see both tabs and switch freely.
  /// All other designations only see the tab matching the dashboard selection.
  bool get showBothTabs => [29, 162, 92, 139, 136].contains(dESGID);

  /// DESGID 35 sees no tab toggle at all.
  bool get hideTabToggle => dESGID == 35;

  // ─── Filter ───────────────────────────────────────────────────────────────────

  final selectedDate = ''.obs;
  final selectedCamp = Rxn<CampListOutput>();
  final selectedTeam = Rxn<TeamsDetailsOutput>();

  // ─── Lists ────────────────────────────────────────────────────────────────────

  final campList = <CampListOutput>[].obs;
  final teamList = <TeamsDetailsOutput>[].obs;
  final attendanceList = <AttendanceDetailsOutput>[].obs;

  // ─── Photo state ──────────────────────────────────────────────────────────────
  // isMarkInOut: "0" = both needed, "1" = check-in done, "2" = both done

  final inPhotoLocalPath = ''.obs;
  final outPhotoLocalPath = ''.obs;
  final inPhotoServerUrl = ''.obs;
  final outPhotoServerUrl = ''.obs;
  final inPhotoUploadedOn = ''.obs;
  final outPhotoUploadedOn = ''.obs;
  final isMarkInOut = '0'.obs;

  // ─── Loading ──────────────────────────────────────────────────────────────────

  final isLoadingAttendance = false.obs;
  final isLoadingCamps = false.obs;
  final isLoadingTeams = false.obs;
  final isUploadingPhoto = false.obs;

  // ─── Computed ─────────────────────────────────────────────────────────────────

  bool get allAttendanceMarked =>
      attendanceList.isNotEmpty &&
      attendanceList.every((m) => !m.isInPending && !m.isOutPending);

  bool get canUploadIn =>
      inPhotoLocalPath.value.isNotEmpty && isMarkInOut.value == '0';

  bool get canUploadOut =>
      outPhotoLocalPath.value.isNotEmpty && isMarkInOut.value == '1';

  bool get bothPhotosUploaded => isMarkInOut.value == '2';

  /// Capture Photo button is always visible once the screen is open.
  bool get canCapturePhoto => true;

  bool get _allCheckedIn =>
      attendanceList.isNotEmpty &&
      attendanceList.every((m) => !m.isInPending);

  bool get _allCheckedOut =>
      attendanceList.isNotEmpty &&
      attendanceList.every((m) => !m.isOutPending);

  String get mediaBaseUrl => APIManager.kMediaBaseURL;

  // ─── Lifecycle ────────────────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    final user = DataProvider().getParsedUserData()?.output?.first;
    empCode = user?.empCode ?? 0;
    dESGID = user?.dESGID ?? 0;
    distLgdCode = user?.dISTLGDCODE?.toString() ?? '0';
    talLgdCode = user?.tALLGDCODE?.toString() ?? '0';

    // Default date = today
    selectedDate.value = FormatterManager.formatDateToString(DateTime.now());

    // Apply the camp type that was selected in the dashboard
    applyCampType(navCampType);
  }

  void applyCampType(String type) {
    // MMU designations always use "6" instead of "3" for D2D
    const mmuDesig = [136, 139, 176];
    if (type != '1' && mmuDesig.contains(dESGID)) {
      campType.value = '6';
      statusType.value = '1';
    } else if (type == '1') {
      campType.value = '1';
      statusType.value = '2';
    } else {
      campType.value = '3';
      statusType.value = '1';
    }
  }

  // ─── Camp type toggle ─────────────────────────────────────────────────────────

  void selectRegularCamp() {
    applyCampType('1');
    _resetFilters();
  }

  void selectD2DCamp() {
    applyCampType('3');
    _resetFilters();
  }

  void _resetFilters() {
    selectedCamp.value = null;
    selectedTeam.value = null;
    campList.clear();
    teamList.clear();
    attendanceList.clear();
    _resetPhotoState();
  }

  // ─── Date change ─────────────────────────────────────────────────────────────

  Future<void> onDateChanged(BuildContext context) async {
    final now = DateTime.now();
    final minDate = now.subtract(const Duration(days: 7));
    final picked = await showDatePicker(
      context: context,
      initialDate: now,
      firstDate: minDate,
      lastDate: now,
    );
    if (picked != null) {
      selectedDate.value = FormatterManager.formatDateToString(picked);
      selectedCamp.value = null;
      selectedTeam.value = null;
      campList.clear();
      teamList.clear();
      attendanceList.clear();
      _resetPhotoState();
      await fetchCampList();
    }
  }

  // ─── Camp list ───────────────────────────────────────────────────────────────

  Future<void> fetchCampList() async {
    if (selectedDate.value.isEmpty) return;
    isLoadingCamps.value = true;
    try {
      final result = await _repository.getCampList(
        userId: empCode.toString(),
        campDate: selectedDate.value,
        campType: campType.value,
      );
      campList.value = result?.output ?? [];
      if (campList.isEmpty) ToastManager.toast('No camps found for selected date');
    } finally {
      isLoadingCamps.value = false;
    }
  }

  // ─── Camp dropdown ───────────────────────────────────────────────────────────

  void showCampDropdown(BuildContext context) {
    if (selectedDate.value.isEmpty) {
      ToastManager.toast('Please select a date first');
      return;
    }
    if (campList.isEmpty) {
      _fetchThenShowCampDropdown(context);
      return;
    }
    _showDropDownBottomSheet(
      context,
      'Select Camp ID',
      campList.toList(),
      DropDownTypeMenu.TeamCampID,
      (selected) => onCampSelected(selected as CampListOutput),
    );
  }

  Future<void> _fetchThenShowCampDropdown(BuildContext context) async {
    ToastManager.showLoader();
    try {
      final result = await _repository.getCampList(
        userId: empCode.toString(),
        campDate: selectedDate.value,
        campType: campType.value,
      );
      campList.value = result?.output ?? [];
      if (campList.isEmpty) {
        ToastManager.toast('No camps found for selected date');
        return;
      }
      _showDropDownBottomSheet(
        context,
        'Select Camp ID',
        campList.toList(),
        DropDownTypeMenu.TeamCampID,
        (selected) => onCampSelected(selected as CampListOutput),
      );
    } finally {
      ToastManager.hideLoader();
    }
  }

  void _showDropDownBottomSheet(
    BuildContext context,
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
    Function(dynamic) onApply,
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
            onApplyTap: (p0) => onApply(p0),
          ),
        );
      },
    );
  }

  // ─── Camp selection ──────────────────────────────────────────────────────────

  Future<void> onCampSelected(CampListOutput camp) async {
    selectedCamp.value = camp;
    selectedTeam.value = null;
    teamList.clear();
    attendanceList.clear();
    _resetPhotoState();

    ToastManager.showLoader();
    try {
      await Future.wait([
        _fetchCampImages(camp.campId ?? ''),
        if (isD2DOrMMU) fetchTeamList(camp.campId ?? '') else _fetchAttendance(),
      ]);
    } finally {
      ToastManager.hideLoader();
    }
  }

  // ─── Team list (D2D / MMU only) ───────────────────────────────────────────────

  Future<void> fetchTeamList(String campId) async {
    final result = await _repository.getTeamDetails(
      campId: campId,
      campDate: selectedDate.value,
      campType: campType.value,
    );
    teamList.value = result?.output ?? [];
  }

  // ─── Team selection ──────────────────────────────────────────────────────────

  Future<void> onTeamSelected(TeamsDetailsOutput team) async {
    selectedTeam.value = team;
    attendanceList.clear();
    ToastManager.showLoader();
    try {
      await _fetchAttendance();
    } finally {
      ToastManager.hideLoader();
    }
  }

  // ─── Attendance ──────────────────────────────────────────────────────────────

  Future<void> _fetchAttendance() async {
    final campId = selectedCamp.value?.campId ?? '';
    if (campId.isEmpty) return;
    final result = await _repository.getAttendanceDetails(
      campDate: selectedDate.value,
      teamId: selectedTeam.value?.teamNumber ?? '0',
      campId: campId,
      statusId: statusType.value,
    );
    attendanceList.value = result?.output ?? [];
  }

  // ─── Camp images ─────────────────────────────────────────────────────────────

  Future<void> _fetchCampImages(String campId) async {
    final result = await _repository.getCampImages(campId: campId);
    final img = result?.output?.firstOrNull;
    if (img != null) {
      if (img.hasInImage) {
        inPhotoServerUrl.value =
            '$mediaBaseUrl/CampTeamAttendance/${img.inImage}';
        inPhotoUploadedOn.value = img.inImageUploadedOn ?? '';
        isMarkInOut.value = img.hasOutImage ? '2' : '1';
      }
      if (img.hasOutImage) {
        outPhotoServerUrl.value =
            '$mediaBaseUrl/CampTeamAttendance/${img.outImage}';
        outPhotoUploadedOn.value = img.outImageUploadedOn ?? '';
      }
    }
  }

  // ─── Camera capture ───────────────────────────────────────────────────────────

  Future<void> capturePhoto({required bool isCheckIn}) async {
    // Validate: date
    if (selectedDate.value.isEmpty) {
      ToastManager.toast('Please select date');
      return;
    }
    // Validate: camp ID
    if (selectedCamp.value == null) {
      ToastManager.toast('Please select camp ID');
      return;
    }
    // Validate: team (D2D / MMU only)
    if (isD2DOrMMU && selectedTeam.value == null) {
      ToastManager.toast('Please select team number');
      return;
    }
    // Validate: all members must have marked attendance before team photo
    if (isCheckIn && !_allCheckedIn) {
      ToastManager.toast('काही टीम सदस्यांनी उपस्थिती नोंदवलेली नाही');
      return;
    }
    if (!isCheckIn && !_allCheckedOut) {
      ToastManager.toast('काही टीम सदस्यांनी check-out नोंदवलेली नाही');
      return;
    }

    final picked = await _picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked == null) return;
    if (isCheckIn) {
      inPhotoLocalPath.value = picked.path;
    } else {
      outPhotoLocalPath.value = picked.path;
    }
  }

  // ─── Upload photo ─────────────────────────────────────────────────────────────

  Future<void> uploadPhoto({required bool isCheckIn}) async {
    // Validate: date
    if (selectedDate.value.isEmpty) {
      ToastManager.toast('Please select date');
      return;
    }
    // Validate: camp ID
    final campId = selectedCamp.value?.campId ?? '';
    if (campId.isEmpty) {
      ToastManager.toast('Please select campId');
      return;
    }
    // Validate: team (D2D / MMU only)
    if (isD2DOrMMU && selectedTeam.value == null) {
      ToastManager.toast('Please select team number');
      return;
    }

    // Validate: photo captured
    final localPath = isCheckIn ? inPhotoLocalPath.value : outPhotoLocalPath.value;
    if (localPath.isEmpty) {
      ToastManager.toast(
        isCheckIn ? 'Please capture check in photo' : 'Please capture check out photo',
      );
      return;
    }

    isUploadingPhoto.value = true;
    ToastManager.showLoader();
    try {
      final statusId = isCheckIn ? '1' : '2';
      final success = await _repository.uploadPhoto(
        campId: campId,
        userId: empCode.toString(),
        statusId: statusId,
        photoFile: File(localPath),
      );

      if (success) {
        ToastManager.toast(
          isCheckIn ? 'Check-in photo uploaded successfully' : 'Check-out photo uploaded successfully',
        );
        // Refresh images from server
        await _fetchCampImages(campId);
        // Clear local path after successful upload
        if (isCheckIn) {
          inPhotoLocalPath.value = '';
        } else {
          outPhotoLocalPath.value = '';
        }
      } else {
        ToastManager.toast('Photo upload failed. Please try again.');
      }
    } finally {
      isUploadingPhoto.value = false;
      ToastManager.hideLoader();
    }
  }

  // ─── Refresh ──────────────────────────────────────────────────────────────────

  Future<void> refreshAttendance() async {
    await _fetchAttendance();
  }

  // ─── Helpers ─────────────────────────────────────────────────────────────────

  void _resetPhotoState() {
    inPhotoLocalPath.value = '';
    outPhotoLocalPath.value = '';
    inPhotoServerUrl.value = '';
    outPhotoServerUrl.value = '';
    inPhotoUploadedOn.value = '';
    outPhotoUploadedOn.value = '';
    isMarkInOut.value = '0';
  }
}
