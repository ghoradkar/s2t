// ignore_for_file: file_names, avoid_print

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:geolocator/geolocator.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:skeletonizer/skeletonizer.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/LocationManager/LocationManager.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import '../../Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import '../../Modules/Json_Class/UserAttandanceResponse/UserAttandanceResponse.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import '../../Views/MonthlyScreen/MonthlyScreen.dart';

class UserAttendanceScreen extends StatefulWidget {
  const UserAttendanceScreen({super.key});

  @override
  State<UserAttendanceScreen> createState() => _UserAttendanceScreenState();
}

class _UserAttendanceScreenState extends State<UserAttendanceScreen> {
  final formKey = GlobalKey<FormState>();
  APIManager apiManager = APIManager();
  int year = 0;
  int month = 0;
  int dESGID = 0;
  String projectId = "0";
  int subOrgId = 0;
  int empCode = 0;
  bool _isLoading = false;
  DateTime _visibleMonth = DateTime.now();

  Map<DateTime, Color> attendanceMap = {};

  @override
  void initState() {
    super.initState();
    DateTime dateTime = DateTime.now();
    year = dateTime.year;
    month = dateTime.month;
    _visibleMonth = DateTime(dateTime.year, dateTime.month, 1);

    LoginOutput? userLoginDetails =
        DataProvider().getParsedUserData()?.output?.first;
    dESGID = userLoginDetails?.dESGID ?? 0;
    projectId = userLoginDetails?.projectId ?? "0";
    subOrgId = userLoginDetails?.subOrgId ?? 0;
    empCode = userLoginDetails?.empCode ?? 0;
    getUserAttendance();
  }

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
            child: Container(
              color: Colors.white,
              height: SizeConfig.screenHeight + 50,
              width: SizeConfig.screenWidth,
              child:
                  _isLoading
                      ? CommonSkeletonCalender()
                      : MonthlyScreen(
                        initialMonth: _visibleMonth,
                        attendanceMap: attendanceMap,
                        didChangeDate: (p0) {
                          setState(() {
                            _visibleMonth = DateTime(p0.year, p0.month, 1);
                            year = p0.year;
                            month = p0.month;
                          });
                          getUserAttendance();
                        },
                        onDateSelectedTap: (date) {
                          if (attendanceMap.containsKey(date)) {
                            ToastManager.toast(
                              "You have already marked your availability.",
                            );
                          } else {
                            String currentDate =
                                FormatterManager.formatDateToString(
                                  DateTime.now(),
                                );
                            String selectedDate =
                                FormatterManager.formatDateToString(date);
                            print("Now $currentDate");
                            print("selected $selectedDate");
                            if (currentDate == selectedDate) {
                              print(date);
                              onFetchData(selectedDate);
                              // ToastManager.toast(date.toString());
                            } else {
                              print("Date not found.");
                            }
                          }
                        },
                      ),
            ),
          ),
        ),
      ),
    );
  }

  // Widget _buildSkeletonCalendar() {
  //   return Skeletonizer(
  //     enabled: true,
  //     child: Container(
  //       height: 370,
  //       decoration: const BoxDecoration(
  //         color: Colors.white,
  //         borderRadius: BorderRadius.only(
  //           topLeft: Radius.circular(20),
  //           topRight: Radius.circular(20),
  //         ),
  //       ),
  //       padding: const EdgeInsets.fromLTRB(8, 8, 8, 8),
  //       child: Column(
  //         children: [
  //           Row(
  //             mainAxisAlignment: MainAxisAlignment.spaceBetween,
  //             children: [
  //               _skeletonBox(height: 24, width: 24, radius: 6),
  //               _skeletonBox(height: 20, width: 160, radius: 6),
  //               _skeletonBox(height: 24, width: 24, radius: 6),
  //             ],
  //           ),
  //           const SizedBox(height: 8),
  //           Row(
  //             mainAxisAlignment: MainAxisAlignment.spaceBetween,
  //             children: List.generate(
  //               7,
  //               (index) => Expanded(
  //                 child: Center(
  //                   child: _skeletonBox(height: 14, width: 32, radius: 4),
  //                 ),
  //               ),
  //             ),
  //           ),
  //           const SizedBox(height: 8),
  //           Expanded(
  //             child: GridView.builder(
  //               physics: const NeverScrollableScrollPhysics(),
  //               gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
  //                 crossAxisCount: 7,
  //               ),
  //               itemCount: 42,
  //               itemBuilder: (context, index) {
  //                 return Padding(
  //                   padding: const EdgeInsets.all(4.0),
  //                   child: Center(
  //                     child: _skeletonBox(height: 32, width: 32, radius: 8),
  //                   ),
  //                 );
  //               },
  //             ),
  //           ),
  //         ],
  //       ),
  //     ),
  //   );
  // }
  //
  // Widget _skeletonBox({
  //   required double height,
  //   required double width,
  //   double radius = 8,
  // }) {
  //   return Container(
  //     height: height,
  //     width: width,
  //     decoration: BoxDecoration(
  //       color: Colors.grey.shade300,
  //       borderRadius: BorderRadius.circular(radius),
  //     ),
  //   );
  // }

  getUserAttendance() {
    setState(() {
      _isLoading = true;
    });
    Map<String, String> params = {
      "Year": year.toString(),
      "Month": month.toString(),
      "ProjectId": projectId,
      "SubOrgId": subOrgId.toString(),
      "USERID": empCode.toString(),
    };
    print(params);
    apiManager.getUserAttendanceAPI(params, apiUserAttendanceBack);
  }

  void apiUserAttendanceBack(
    UserAttandanceResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      attendanceMap = {};
      List<UserAttandanceOutput> tempList = response?.output ?? [];
      for (UserAttandanceOutput attendace in tempList) {
        attendanceMap[DateTime(
          attendace.year ?? 0,
          attendace.month ?? 0,
          attendace.day ?? 0,
        )] = Color.fromRGBO(100, 167, 90, 1.0);
      }
    } else {
      attendanceMap = {};
      ToastManager.toast(errorMessage);
    }
    setState(() {
      _isLoading = false;
    });
  }

  void onFetchData(String date) async {
    bool locationAllowed = await LocationManager.checkAndRequestLocation();
    if (!locationAllowed) {
      // Show a dialog or toast
      print("Location permission is required.");
      return;
    }

    insertUserAttendance(date);
  }

  insertUserAttendance(String attendanceDate) async {
    Position? location = await LocationManager.getCurrentLocation();

    Map<String, String> params = {
      "LATITUDE": location?.latitude.toString() ?? "0",
      "LONGITUDE": location?.longitude.toString() ?? "0",
      "ATTENDANCEMARKBY": empCode.toString(),
      "ATTENDANCEDATE": attendanceDate,
      "Userid": empCode.toString(),
      "MACID": "0",
    };
    apiManager.getInsertUserAttendancAPI(params, apiInsertUserAttendancBack);
  }

  void apiInsertUserAttendancBack(
    UserAttandanceResponse? response,
    String errorMessage,
    bool success,
  ) async {
    if (success) {
      ToastManager.toast("Attendance marked successfully");
      getUserAttendance();
    } else {
      ToastManager.toast(errorMessage);
    }
    setState(() {});
  }
}
