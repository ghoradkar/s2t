// ignore_for_file: file_names

import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/patient_registration/model/get_queue_response_model.dart';
import 'package:s2toperational/Screens/patient_registration/repository/d2d_patient_registration_repository.dart';
import 'package:s2toperational/Screens/patient_registration/screen/view_queue_patient_screen.dart';

class PatientQueueListScreen extends StatefulWidget {
  final String campId;

  /// Fires when "Go To Registration" is confirmed from the detail screen.
  /// Passes the full response JSON string and the auth token back to the
  /// D2D registration screen to pre-fill the form.
  final void Function(String response, String authToken) onGoToRegistration;

  const PatientQueueListScreen({
    super.key,
    required this.campId,
    required this.onGoToRegistration,
  });

  @override
  State<PatientQueueListScreen> createState() => _PatientQueueListScreenState();
}

class _PatientQueueListScreenState extends State<PatientQueueListScreen> {
  final _repo = D2DPatientRegistrationRepository();
  bool _isLoading = false;
  List<QueueOutput> _items = [];

  @override
  void initState() {
    super.initState();
    _fetchQueue();
  }

  Future<void> _fetchQueue() async {
    setState(() => _isLoading = true);
    try {
      final result = await _repo.getPatientQueue(campId: widget.campId);
      if (!mounted) return;
      if (result == null || result.output.isEmpty) {
        _showEmptyAlert();
        return;
      }
      setState(() => _items = result.output);
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  void _showEmptyAlert() {
    ToastManager.showAlertDialog(
      context,
      'No patient in queue',
      () {
        Navigator.of(context, rootNavigator: true).pop(); // close alert
        if (mounted) Navigator.of(context).pop(); // close this screen
      },
      title: 'Queue is empty',
    );
  }

  void _onViewTap(QueueOutput item) {
    // Extract address line safely from the nested response JSON
    String addressLine = '';
    try {
      if (item.response != null && item.response!.isNotEmpty) {
        final responseObj = jsonDecode(item.response!) as Map<String, dynamic>;
        final profileObj = responseObj['profile'] as Map<String, dynamic>;
        final patientObj = profileObj['patient'] as Map<String, dynamic>;
        final addressObj = patientObj['address'] as Map<String, dynamic>;
        addressLine = addressObj['line']?.toString() ?? '';
      }
    } catch (_) {}

    // Compute age from year/month/day of birth
    String ageStr = '';
    try {
      final year = int.tryParse(item.yearOfBirth ?? '') ?? 0;
      final month = int.tryParse(item.monthOfBirth ?? '') ?? 1;
      final day = int.tryParse(item.dayOfBirth ?? '') ?? 1;
      if (year > 0) {
        final now = DateTime.now();
        int age = now.year - year;
        if (now.month < month || (now.month == month && now.day < day)) age--;
        ageStr = age.toString();
      }
    } catch (_) {}

    final dob =
        '${item.yearOfBirth ?? ''}/${item.monthOfBirth ?? ''}/${item.dayOfBirth ?? ''}';

    Navigator.push(
      context,
      MaterialPageRoute(
        builder:
            (_) => ViewQueuePatientScreen(
              name: item.name ?? '',
              abhaNumber: item.healthIdNumber ?? '',
              abhaAddress: item.healthId ?? '',
              gender: item.gender ?? '',
              dob: dob,
              ageInYears: ageStr,
              mobileNum: item.mobileNo ?? '',
              addressLine: addressLine,
              token: item.authtoken ?? '',
              identityID: item.identityID ?? 0,
              response: item.response ?? '',
              onGoToRegistration: (response, authToken) {
                // Pop ViewQueuePatientScreen then PatientQueueListScreen,
                // then invoke the callback to pre-fill D2D registration form.
                Navigator.of(context)
                  ..pop() // ViewQueuePatientScreen
                  ..pop(); // PatientQueueListScreen
                widget.onGoToRegistration(response, authToken);
              },
            ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return NetworkWrapper(
      child: Scaffold(
        backgroundColor: kBackground,
        appBar: mAppBar(
          scTitle: 'Patient Queue',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body:
            _isLoading
                ? const Center(child: CircularProgressIndicator())
                : _items.isEmpty
                ? const SizedBox.shrink()
                : ListView.separated(
                  padding: EdgeInsets.symmetric(
                    horizontal: 14.w,
                    vertical: 14.h,
                  ),
                  itemCount: _items.length,
                  separatorBuilder: (_, __) => SizedBox(height: 8.h),
                  itemBuilder: (_, index) => _itemCard(_items[index]),
                ),
      ),
    );
  }

  Widget _itemCard(QueueOutput item) {
    return Container(
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(8.r),
        border: Border.all(color: kPrimaryColor.withValues(alpha: 0.15)),
      ),
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 12.h),
      child: Row(
        children: [
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                CommonText(
                  text: item.name ?? '—',
                  fontSize: 14.sp,
                  fontWeight: FontWeight.w600,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                SizedBox(height: 4.h),
                CommonText(
                  text: 'Token: ${item.identityID ?? '—'}',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  textColor: kTextColor,
                  textAlign: TextAlign.start,
                ),
              ],
            ),
          ),
          OutlinedButton(
            onPressed: () => _onViewTap(item),
            style: OutlinedButton.styleFrom(
              foregroundColor: kPrimaryColor,
              side: const BorderSide(color: kPrimaryColor),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(6.r),
              ),
              padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 8.h),
            ),
            child: CommonText(
              text: 'View',
              fontSize: 13.sp,
              fontWeight: FontWeight.w500,
              textColor: kPrimaryColor,
              textAlign: TextAlign.center,
            ),
          ),
        ],
      ),
    );
  }
}
