// ignore_for_file: file_names

import 'dart:io';

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:image_picker/image_picker.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';
import 'package:s2toperational/utilities/api_manager.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import 'package:s2toperational/team_photos/model/attendance_details_response.dart';
import 'package:s2toperational/team_photos/model/attendance_image_response.dart';
import 'package:s2toperational/team_photos/model/camp_list_response.dart';
import 'package:s2toperational/team_photos/model/teams_details_response.dart';
import 'package:s2toperational/team_photos/repository/team_photos_repository.dart';

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
  // isMarkInOut: "0" = none, "1" = check-in done, "2" = check-in + check-out done

  final inPhotoLocalPath = ''.obs;
  final outPhotoLocalPath = ''.obs;
  final duringPhotoLocalPath = ''.obs;
  final inPhotoServerUrl = ''.obs;
  final outPhotoServerUrl = ''.obs;
  final duringPhotoServerUrl = ''.obs;
  final inPhotoUploadedOn = ''.obs;
  final outPhotoUploadedOn = ''.obs;
  final duringPhotoUploadedOn = ''.obs;
  final isMarkInOut = '0'.obs;

  // Approval statuses: "" | "Pending" | "Approved" | "Rejected"
  final inPhotoApprovalStatus = ''.obs;
  final outPhotoApprovalStatus = ''.obs;
  final duringPhotoApprovalStatus = ''.obs;

  // ─── Loading ──────────────────────────────────────────────────────────────────

  final isLoadingAttendance = false.obs;
  final isLoadingCamps = false.obs;
  final isLoadingTeams = false.obs;
  final isUploadingPhoto = false.obs;

  // ─── Computed ─────────────────────────────────────────────────────────────────

  bool get inPhotoUploaded => inPhotoServerUrl.value.isNotEmpty;
  bool get outPhotoUploaded => outPhotoServerUrl.value.isNotEmpty;
  bool get duringPhotoUploaded => duringPhotoServerUrl.value.isNotEmpty;

  bool get isCheckInApproved {
    final status = inPhotoApprovalStatus.value;
    if (status.toLowerCase() == 'approved') return true;
    // Fallback: if the API doesn't return an approval status field but the
    // check-in photo is already on the server, allow the during-camp card.
    if (status.isEmpty && inPhotoServerUrl.value.isNotEmpty) return true;
    return false;
  }

  // All 3 photos uploaded — gate for Camp Closing Confirmation button
  bool get bothPhotosUploaded =>
      inPhotoUploaded && duringPhotoUploaded && outPhotoUploaded;

  // Native treats an empty attendance list as "all marked" (pending counts stay
  // at 0), so we match that: [].every(...) returns true in Dart (vacuous truth).
  bool get _allCheckedIn => attendanceList.every((m) => !m.isInPending);
  bool get _allCheckedOut => attendanceList.every((m) => !m.isOutPending);

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

  // ─── Camp selection ──────────────────────────────────────────────────────────

  Future<void> onCampSelected(CampListOutput camp) async {
    selectedCamp.value = camp;
    selectedTeam.value = null;
    teamList.clear();
    attendanceList.clear();
    _resetPhotoState();

    ToastManager.showLoader();
    try {
      if (isD2DOrMMU && hideTabToggle) {
        // DESGID 35 in D2D: team is auto-resolved → _fetchAttendance → _fetchCampImages.
        await _autoResolveTeamAndFetchAttendance(camp.campId ?? '');
      } else if (isD2DOrMMU) {
        // D2D / MMU with team picker: team not selected yet, so fetch images now.
        // Images refresh again when team is selected → _fetchAttendance → _fetchCampImages.
        await Future.wait([
          _fetchCampImages(camp.campId ?? ''),
          fetchTeamList(camp.campId ?? ''),
        ]);
      } else {
        // Regular camp: _fetchAttendance calls _fetchCampImages internally.
        await _fetchAttendance();
      }
    } finally {
      ToastManager.hideLoader();
    }
  }

  /// For DESGID 35 in D2D: calls GetTeamNumberByCampIdAndUSerId to silently
  /// determine the user's team, then loads attendance for that team.
  Future<void> _autoResolveTeamAndFetchAttendance(String campId) async {
    final team = await _repository.getTeamNumberByUser(
      campId: campId,
      userId: empCode.toString(),
    );
    if (team != null) {
      selectedTeam.value = team;
      await _fetchAttendance();
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
    // Mirror native: getImages() is always called after attendance loads,
    // so photo approval status stays fresh whenever attendance refreshes.
    await _fetchCampImages(campId);
  }

  // ─── Camp images ─────────────────────────────────────────────────────────────

  Future<void> _fetchCampImages(String campId) async {
    final result = await _repository.getCampImages(campId: campId);
    final img = result?.output?.firstOrNull;
    if (img == null) return;

    if (img.hasInImage) {
      inPhotoServerUrl.value = '$mediaBaseUrl/CampTeamAttendance/${img.inImage}';
      inPhotoUploadedOn.value = img.inImageUploadedOn ?? '';
      inPhotoApprovalStatus.value = img.isInImageApproved ?? '';
    }
    if (img.hasDuringCampImage) {
      duringPhotoServerUrl.value =
          '$mediaBaseUrl/CampTeamAttendance/${img.duringCampImage}';
      duringPhotoUploadedOn.value = img.duringCampImageUploadedOn ?? '';
      duringPhotoApprovalStatus.value = img.isDuringImageApproved ?? '';
    }
    if (img.hasOutImage) {
      outPhotoServerUrl.value =
          '$mediaBaseUrl/CampTeamAttendance/${img.outImage}';
      outPhotoUploadedOn.value = img.outImageUploadedOn ?? '';
      outPhotoApprovalStatus.value = img.isOutImageApproved ?? '';
    }

    // Derive isMarkInOut from uploaded photos for backward compat
    if (img.hasInImage && img.hasOutImage) {
      isMarkInOut.value = '2';
    } else if (img.hasInImage) {
      isMarkInOut.value = '1';
    } else {
      isMarkInOut.value = '0';
    }

    // Show alert dialogs matching native AttendanceDetailsActivity conditions
    _showImageAlerts(img);
  }

  void _showImageAlerts(AttendanceImageOutput img) {
    if (attendanceList.isNotEmpty) {
      if (!img.hasInImage && _allCheckedIn) {
        _showAlertDialog('कॅम्प सुरू करण्यासाठी टीमचा फोटो अपलोड करा.');
        return;
      }
      if (img.hasInImage &&
          (img.isInImageApproved ?? '').toLowerCase() == 'approved' &&
          !_allCheckedOut) {
        _showAlertDialog(
          'काही कॅम्प टीम सदस्यांनी out attendance मार्क केलेली नाही.\nकॅम्प close करण्यापूर्वी out attendance मार्क करा व टीमचा फोटो अपलोड करा.',
        );
        return;
      }
      if (!img.hasOutImage && _allCheckedIn && _allCheckedOut) {
        _showAlertDialog(
          'कॅम्प closing confirmation देण्यापूर्वी टीमचा फोटो अपलोड करा.',
        );
        return;
      }
    }
    if ((img.isInImageApproved ?? '') == 'Rejected') {
      _showAlertDialog(
        'तुमचा चेक इन फोटो रिजेक्ट केला आहे, फोटो पुन्हा अपलोड करा.',
      );
    }
    if ((img.isDuringImageApproved ?? '') == 'Rejected') {
      _showAlertDialog(
        'तुमचा कॅम्प फोटो रिजेक्ट केला आहे, फोटो पुन्हा अपलोड करा.',
      );
    }
    if ((img.isOutImageApproved ?? '') == 'Rejected') {
      _showAlertDialog(
        'तुमचा चेक आऊट फोटो रिजेक्ट केला आहे, फोटो पुन्हा अपलोड करा.',
      );
    }
  }

  void _showAlertDialog(String message) {
    Get.dialog(
      AlertDialog(
        title: const Text('Alert'),
        content: Text(message),
        actions: [
          TextButton(onPressed: () => Get.back(), child: const Text('OK')),
        ],
      ),
    );
  }

  // ─── Capture + Upload (tap photo area) ───────────────────────────────────────

  /// Opens camera, shows local preview immediately, then auto-uploads.
  /// [statusId]: "1"=checkIn, "2"=checkOut, "3"=duringCamp
  Future<void> captureAndUploadPhoto({required String statusId}) async {
    if (selectedDate.value.isEmpty) {
      ToastManager.toast('Please select date');
      return;
    }
    final campId = selectedCamp.value?.campId ?? '';
    if (campId.isEmpty) {
      ToastManager.toast('Please select camp ID');
      return;
    }
    if (isD2DOrMMU && selectedTeam.value == null) {
      ToastManager.toast('Please select team number');
      return;
    }
    if (statusId == '1' && !_allCheckedIn) {
      ToastManager.toast('काही टीम सदस्यांनी उपस्थिती नोंदवलेली नाही');
      return;
    }
    if (statusId == '2' && !_allCheckedOut) {
      ToastManager.toast('काही टीम सदस्यांनी check-out नोंदवलेली नाही');
      return;
    }

    final picked = await _picker.pickImage(
      source: ImageSource.camera,
      imageQuality: 80,
    );
    if (picked == null) return;

    // Show local preview immediately while upload runs
    switch (statusId) {
      case '1': inPhotoLocalPath.value = picked.path;
      case '3': duringPhotoLocalPath.value = picked.path;
      case '2': outPhotoLocalPath.value = picked.path;
    }

    isUploadingPhoto.value = true;
    ToastManager.showLoader();
    try {
      final success = await _repository.uploadPhoto(
        campId: campId,
        userId: empCode.toString(),
        statusId: statusId,
        photoFile: File(picked.path),
      );
      if (success) {
        final msg = switch (statusId) {
          '1' =>
            'चेक इन फोटो यशस्वीरीत्या अपलोड झाला आहे. तुमच्या manager च्या approval नंतर तुम्ही कॅम्प सुरु करू शकतात.',
          '3' =>
            'Camp सुरु असतानाचा beneficiary सोबतचा फोटो यशस्वीरीत्या अपलोड झाला आहे.',
          _ =>
            'चेक आउट फोटो यशस्वीरीत्या अपलोड झाला आहे. तुमच्या manager च्या approval नंतर तुम्ही कॅम्प closing confirmation देऊ शकतात.',
        };
        ToastManager.toast(msg);
        await _fetchCampImages(campId);
      } else {
        ToastManager.toast('Photo upload failed. Please try again.');
      }
    } finally {
      // Clear local path regardless — on success server URL is now set,
      // on failure user taps again to recapture.
      switch (statusId) {
        case '1': inPhotoLocalPath.value = '';
        case '3': duringPhotoLocalPath.value = '';
        case '2': outPhotoLocalPath.value = '';
      }
      isUploadingPhoto.value = false;
      ToastManager.hideLoader();
    }
  }

  // ─── Approve / Reject photo (DESGID 92 only) ─────────────────────────────────

  /// [statusId]: "1"=checkIn, "2"=checkOut, "3"=duringCamp
  /// [approvalStatusId]: "1"=approve, "2"=reject
  Future<void> approveRejectPhoto({
    required String statusId,
    required String approvalStatusId,
  }) async {
    final campId = selectedCamp.value?.campId ?? '';
    if (campId.isEmpty) return;

    // Guard: no photo uploaded yet
    final serverUrl = statusId == '1'
        ? inPhotoServerUrl.value
        : statusId == '3'
            ? duringPhotoServerUrl.value
            : outPhotoServerUrl.value;
    if (serverUrl.isEmpty) {
      ToastManager.toast('टीमकडून फोटो अजून अपलोड झालेला नाही.');
      return;
    }

    ToastManager.showLoader();
    try {
      final success = await _repository.approveRejectPhoto(
        campId: campId,
        statusId: statusId,
        approvalStatusId: approvalStatusId,
        createdBy: empCode.toString(),
      );
      if (success) {
        ToastManager.toast(
          approvalStatusId == '1'
              ? 'फोटो approve करण्यात आलेला आहे.'
              : 'फोटो reject करण्यात आलेला आहे.',
        );
        await _fetchCampImages(campId);
      } else {
        ToastManager.toast('Action failed. Please try again.');
      }
    } finally {
      ToastManager.hideLoader();
    }
  }

  // ─── Camp closing guard ───────────────────────────────────────────────────────

  /// Returns true if all photo approvals are in place for camp closing.
  /// Shows Marathi blocking dialog via Get.dialog() and returns false otherwise.
  bool checkCampClosingAllowed() {
    final outApproved = outPhotoApprovalStatus.value == 'Approved';
    final duringApproved = duringPhotoApprovalStatus.value == 'Approved';
    if (!outApproved || !duringApproved) {
      Get.dialog(
        AlertDialog(
          content: const Text(
            'सर्व फोटो approve झाल्याशिवाय कॅम्प क्लोसिंग confirmation देता येणार नाही याची नोंद घ्यावी',
          ),
          actions: [
            TextButton(
              onPressed: () => Get.back(),
              child: const Text('OK'),
            ),
          ],
        ),
      );
      return false;
    }
    return true;
  }

  // ─── Refresh ──────────────────────────────────────────────────────────────────

  Future<void> refreshAttendance() async {
    await _fetchAttendance();
  }

  /// Pull-to-refresh / Refresh Status button: re-fetches camp images only.
  Future<void> refreshCampImages() async {
    final campId = selectedCamp.value?.campId ?? '';
    if (campId.isEmpty) return;
    await _fetchCampImages(campId);
  }

  // ─── Helpers ─────────────────────────────────────────────────────────────────

  void _resetPhotoState() {
    inPhotoLocalPath.value = '';
    outPhotoLocalPath.value = '';
    duringPhotoLocalPath.value = '';
    inPhotoServerUrl.value = '';
    outPhotoServerUrl.value = '';
    duringPhotoServerUrl.value = '';
    inPhotoUploadedOn.value = '';
    outPhotoUploadedOn.value = '';
    duringPhotoUploadedOn.value = '';
    isMarkInOut.value = '0';
    inPhotoApprovalStatus.value = '';
    outPhotoApprovalStatus.value = '';
    duringPhotoApprovalStatus.value = '';
  }
}
