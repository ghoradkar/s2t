// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/user_attendance/controller/user_attendance_controller.dart';
import 'package:s2toperational/Screens/user_attendance/repository/user_attendance_repository.dart';
import 'package:s2toperational/Views/MonthlyScreen/MonthlyScreen.dart';

class UserAttendanceScreen extends StatelessWidget {
  const UserAttendanceScreen({super.key});

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'User Attendance',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: AnnotatedRegion(
          value: const SystemUiOverlayStyle(
            statusBarColor: kPrimaryColor,
            statusBarBrightness: Brightness.light,
            statusBarIconBrightness: Brightness.light,
          ),
          child: SingleChildScrollView(
            child: GetBuilder<UserAttendanceController>(
              init: UserAttendanceController(
                repository: UserAttendanceRepository(),
              ),
              builder: (c) => Container(
                color: Colors.white,
                height: SizeConfig.screenHeight + 50,
                width: SizeConfig.screenWidth,
                child: c.isLoading
                    ? CommonSkeletonCalender()
                    : MonthlyScreen(
                        initialMonth: c.visibleMonth,
                        attendanceMap: c.attendanceMap,
                        didChangeDate: (p0) => c.onMonthChanged(p0),
                        onDateSelectedTap: (date) => c.onDateSelected(date),
                      ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}