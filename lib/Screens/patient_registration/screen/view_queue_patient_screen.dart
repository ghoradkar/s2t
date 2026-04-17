// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';

class ViewQueuePatientScreen extends StatelessWidget {
  final String name;
  final String abhaNumber;
  final String abhaAddress;
  final String gender;
  final String dob;
  final String ageInYears;
  final String mobileNum;
  final String addressLine;
  final String token;
  final int identityID;
  final String response;
  final String permAddress;

  /// When true: hides "Go To Registration" and token, shows permanent address.
  /// Matches native ViewQueuePatientActivity type="ReadyOnly".
  final bool isReadOnly;

  /// Called when "Go To Registration" is tapped.
  /// Passes [response] and [authToken] back to the caller.
  final void Function(String response, String authToken) onGoToRegistration;

  const ViewQueuePatientScreen({
    super.key,
    required this.name,
    required this.abhaNumber,
    required this.abhaAddress,
    required this.gender,
    required this.dob,
    required this.ageInYears,
    required this.mobileNum,
    required this.addressLine,
    this.permAddress = '',
    required this.token,
    required this.identityID,
    required this.response,
    this.isReadOnly = false,
    required this.onGoToRegistration,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Patient Details',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 14.h),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _card([
              _field('Full Name', name),
              _field('ABHA Number', abhaNumber),
              _field('ABHA Address', abhaAddress),
              _field('Gender', gender),
              _field('Date of Birth', dob),
              _field('Age', ageInYears),
              _field('Mobile', mobileNum),
              _field('Address', addressLine),
              if (isReadOnly) _field('Permanent Address', permAddress),
              if (!isReadOnly) _field('Token No.', identityID.toString()),
            ]),
            if (!isReadOnly) ...[
              SizedBox(height: 24.h),
              SizedBox(
                width: double.infinity,
                height: 48.h,
                child: ElevatedButton(
                  onPressed: () => _onGoToRegistration(context),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: kPrimaryColor,
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(8.r),
                    ),
                    elevation: 0,
                  ),
                  child: Text(
                    'Go To Registration',
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: 15.sp,
                      fontWeight: FontWeight.w600,
                      color: kWhiteColor,
                    ),
                  ),
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }

  void _onGoToRegistration(BuildContext context) {
    if (token.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'Unable to get token',
        () => Navigator.of(context).pop(),
      );
      return;
    }
    onGoToRegistration(response, token);
  }

  Widget _card(List<Widget> children) {
    return Container(
      width: double.infinity,
      decoration: BoxDecoration(
        color: kWhiteColor,
        borderRadius: BorderRadius.circular(8.r),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.06),
            blurRadius: 4,
            offset: const Offset(0, 2),
          ),
        ],
      ),
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 14.h),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: children,
      ),
    );
  }

  Widget _field(String label, String value) {
    return Padding(
      padding: EdgeInsets.only(bottom: 12.h),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            label,
            style: TextStyle(
              fontFamily: FontConstants.interFonts,
              fontSize: 14.sp,
              color: kBlackColor,
            ),
          ),
          SizedBox(height: 2.h),
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(horizontal: 10.w, vertical: 10.h),
            decoration: BoxDecoration(
              color: kBackground,
              borderRadius: BorderRadius.circular(6.r),
              border: Border.all(color: kTextFieldBorder),
            ),
            child: Text(
              value.isNotEmpty ? value : '—',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 14.sp,
                color: kTextColor,
              ),
            ),
          ),
        ],
      ),
    );
  }
}