// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Json_Class/UserAttendancesUsingSitedetailsIDResponse/UserAttendancesUsingSitedetailsIDResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';
import 'package:s2toperational/Screens/patient_registration/repository/d2d_patient_registration_repository.dart';
import 'package:s2toperational/Screens/patient_registration/screen/view_queue_patient_screen.dart';

class RegisteredPatientListScreen extends StatefulWidget {
  final String campId;
  final String campType;

  const RegisteredPatientListScreen({
    super.key,
    required this.campId,
    required this.campType,
  });

  @override
  State<RegisteredPatientListScreen> createState() =>
      _RegisteredPatientListScreenState();
}

class _RegisteredPatientListScreenState
    extends State<RegisteredPatientListScreen> {
  final _repo = D2DPatientRegistrationRepository();
  final _searchController = TextEditingController();

  bool _isLoading = false;
  List<UserAttendancesUsingSitedetailsIDOutput> _allItems = [];
  List<UserAttendancesUsingSitedetailsIDOutput> _filteredItems = [];

  @override
  void initState() {
    super.initState();
    _fetchList();
    _searchController.addListener(_onSearchChanged);
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: 'Patient List',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body:
          _isLoading
              ? Column(
                children: [
                  _searchBarDisabled(),
                  _tableHeader(),
                  const Expanded(
                    child: CommonSkeletonD2DPhysicalExamTable(rowCount: 10),
                  ),
                ],
              )
              : _allItems.isEmpty
              ? const SizedBox.shrink()
              : Column(
                children: [
                  _searchBar(),
                  _tableHeader().paddingOnly(left: 10, right: 10),
                  Expanded(
                    child:
                        _filteredItems.isEmpty
                            ? NoDataFound().paddingSymmetric(
                              vertical: 6.h,
                              horizontal: 10.w,
                            )
                            : ListView.builder(
                              padding: EdgeInsets.symmetric(horizontal: 12.w),
                              itemCount: _filteredItems.length,
                              itemBuilder: (context, index) {
                                return _PatientListRow(
                                  index: index,
                                  item: _filteredItems[index],
                                  onTap:
                                      () =>
                                          _onItemTapped(_filteredItems[index]),
                                );
                              },
                            ),
                  ),
                ],
              ),
    );
  }

  void _onSearchChanged() {
    final query = _searchController.text.trim().toLowerCase();
    setState(() {
      if (query.isEmpty) {
        _filteredItems = _allItems;
      } else {
        _filteredItems =
            _allItems.where((item) {
              final name = (item.englishName ?? '').toLowerCase();
              final regNo = (item.regdNo?.toString() ?? '').toLowerCase();
              return name.contains(query) || regNo.contains(query);
            }).toList();
      }
    });
  }

  Future<void> _fetchList() async {
    final user = DataProvider().getParsedUserData()?.output?.first;
    final empCode = (user?.empCode ?? 0).toString();

    setState(() => _isLoading = true);
    final result = await _repo.getRegisteredPatientList(
      campId: widget.campId,
      empCode: empCode,
      campType: widget.campType,
    );
    setState(() => _isLoading = false);

    if (!mounted) return;

    if (result == null) {
      ToastManager.showAlertDialog(
        context,
        'Server not responding. Please try again.',
        () => Navigator.of(context).pop(),
      );
      return;
    }

    if (result.output == null || result.output!.isEmpty) {
      ToastManager.showAlertDialog(
        context,
        'No patients found.',
        () => Navigator.of(context).pop(),
      );
      return;
    }

    setState(() {
      _allItems = result.output!;
      _filteredItems = _allItems;
    });
  }

  Widget _searchBarDisabled() {
    return Container(
      color: kWhiteColor,
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
      child: TextField(
        enabled: false,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 13.sp,
          color: kTextColor,
        ),
        decoration: InputDecoration(
          hintText: 'Search by Name / Reg. No.',
          hintStyle: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 13.sp,
            color: kLabelTextColor,
          ),
          prefixIcon: Icon(Icons.search, color: kLabelTextColor, size: 20.r),
          filled: true,
          fillColor: kBackground,
          contentPadding: EdgeInsets.symmetric(
            horizontal: 12.w,
            vertical: 10.h,
          ),
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.r),
            borderSide: BorderSide(color: kTextFieldBorder),
          ),
          enabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.r),
            borderSide: BorderSide(color: kTextFieldBorder),
          ),
          disabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.r),
            borderSide: BorderSide(color: kTextFieldBorder),
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.r),
            borderSide: BorderSide(color: kPrimaryColor, width: 1.5),
          ),
        ),
      ),
    );
  }

  Widget _searchBar() {
    return Container(
      color: kWhiteColor,
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 10.h),
      child: TextField(
        controller: _searchController,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 13.sp,
          color: kTextColor,
        ),
        decoration: InputDecoration(
          hintText: 'Search by Name / Reg. No.',
          hintStyle: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 13.sp,
            color: kLabelTextColor,
          ),
          prefixIcon: Icon(Icons.search, color: kLabelTextColor, size: 20.r),
          suffixIcon:
              _searchController.text.isNotEmpty
                  ? GestureDetector(
                    onTap: () {
                      _searchController.clear();
                    },
                    child: Icon(
                      Icons.close,
                      color: kLabelTextColor,
                      size: 18.r,
                    ),
                  )
                  : null,
          filled: true,
          fillColor: kBackground,
          contentPadding: EdgeInsets.symmetric(
            horizontal: 12.w,
            vertical: 10.h,
          ),
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.r),
            borderSide: BorderSide(color: kTextFieldBorder),
          ),
          enabledBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.r),
            borderSide: BorderSide(color: kTextFieldBorder),
          ),
          focusedBorder: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8.r),
            borderSide: BorderSide(color: kPrimaryColor, width: 1.5),
          ),
        ),
      ),
    );
  }

  Widget _tableHeader() {
    return Container(
      color: kPrimaryColor,
      padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 12.h),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          _headerCell('SN', flex: 1),
          _headerCell('Patient Name', flex: 3, align: TextAlign.left),
          _headerCell('Type', flex: 1),
          _headerCell('Reg. No.', flex: 2),
          _headerCell('Call', flex: 1),
        ],
      ),
    );
  }

  Widget _headerCell(
    String text, {
    required int flex,
    TextAlign align = TextAlign.center,
  }) {
    return Expanded(
      flex: flex,
      child: Text(
        text,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 14.sp,
          fontWeight: FontWeight.w600,
          color: kWhiteColor,
        ),
        textAlign: align,
      ),
    );
  }

  void _onItemTapped(UserAttendancesUsingSitedetailsIDOutput item) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder:
            (_) => ViewQueuePatientScreen(
              name: item.englishName ?? '',
              abhaNumber: item.abhaNumber ?? '',
              abhaAddress: item.abhaAddress ?? '',
              gender: item.gender ?? '',
              dob: item.dOB ?? '',
              ageInYears: item.age?.toString() ?? '',
              mobileNum: item.mobileNo ?? '',
              addressLine: item.localAddress ?? '',
              permAddress: item.permanentAddress ?? '',
              token: '',
              identityID: 0,
              response: '',
              isReadOnly: true,
              onGoToRegistration: (_, __) {},
            ),
      ),
    );
  }
}

class _PatientListRow extends StatelessWidget {
  final int index;
  final UserAttendancesUsingSitedetailsIDOutput item;
  final VoidCallback onTap;

  const _PatientListRow({
    required this.index,
    required this.item,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final typeLabel = _typeLabel();
    final isEven = index % 2 == 0;

    return GestureDetector(
      onTap: onTap,
      child: Container(
        color: isEven ? kWhiteColor : kBackground,
        padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 12.h),
        child: IntrinsicHeight(
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              _cell('${index + 1}', flex: 1),
              _cell(item.englishName ?? '', flex: 3, align: TextAlign.left),
              _cell(typeLabel, flex: 1),
              _cell(item.regdNo?.toString() ?? '', flex: 2),
              Expanded(
                flex: 1,
                child: Center(
                  child: Icon(Icons.phone, color: kPrimaryColor, size: 18.r),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  String _typeLabel() {
    final dep = item.isDependent;
    if (dep == 0) return 'W';
    if (dep == 1) return 'D';
    return '';
  }

  Widget _cell(
    String text, {
    required int flex,
    TextAlign align = TextAlign.center,
  }) {
    return Expanded(
      flex: flex,
      child: Align(
        alignment:
            align == TextAlign.left ? Alignment.centerLeft : Alignment.center,
        child: Text(
          text,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 14.sp,
            color: kTextColor,
          ),
          textAlign: align,
          maxLines: 2,
          overflow: TextOverflow.ellipsis,
        ),
      ),
    );
  }
}
