// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:get/get.dart';
import 'package:latlong2/latlong.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:skeletonizer/skeletonizer.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/user_attendance/controller/user_attendance_controller.dart';
import 'package:s2toperational/Screens/user_attendance/repository/user_attendance_repository.dart';
import 'package:s2toperational/Views/MonthlyScreen/MonthlyScreen.dart';

class UserAttendanceScreen extends StatelessWidget {
  const UserAttendanceScreen({super.key});

  Widget _legendDot(Color color) => Container(
    width: 14,
    height: 14,
    decoration: BoxDecoration(color: color, shape: BoxShape.circle),
  );

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
          showActions: true,
          actions: [
            InkWell(
              onTap: () {
                showDialog(
                  context: context,
                  barrierDismissible: false,

                  builder: (BuildContext context) {
                    return AlertDialog(
                      content: Column(
                        mainAxisSize: MainAxisSize.min,
                        children: [
                          Row(
                            children: [
                              _legendDot(
                                const Color.fromRGBO(100, 167, 90, 1.0),
                              ),
                              const SizedBox(width: 4),

                              CommonText(
                                text: "Checked In",
                                fontSize: 12,
                                fontWeight: FontWeight.normal,
                                textColor: kBlackColor,
                                textAlign: TextAlign.start,
                              ),
                            ],
                          ),

                          Row(
                            children: [
                              _legendDot(
                                const Color.fromRGBO(33, 150, 243, 1.0),
                              ),
                              const SizedBox(width: 4),

                              CommonText(
                                text: "Checked Out",
                                fontSize: 12,
                                fontWeight: FontWeight.normal,
                                textColor: kBlackColor,
                                textAlign: TextAlign.start,
                              ),
                            ],
                          ),
                          Row(
                            children: [
                              _legendDot(Colors.orange),
                              const SizedBox(width: 4),

                              CommonText(
                                text: "Current Location",
                                fontSize: 12,
                                fontWeight: FontWeight.normal,
                                textColor: kBlackColor,
                                textAlign: TextAlign.start,
                              ),
                            ],
                          ),
                          Row(
                            children: [
                              _legendDot(Colors.blue),
                              const SizedBox(width: 4),

                              CommonText(
                                text: "Camp Location",
                                fontSize: 12,
                                fontWeight: FontWeight.normal,
                                textColor: kBlackColor,
                                textAlign: TextAlign.start,
                              ),
                            ],
                          ),
                        ],
                      ),
                      actions: [
                        TextButton(
                          child: Text("Okay"),
                          onPressed: () {
                            Navigator.of(context).pop();
                          },
                        ),
                      ],
                    );
                  },
                );
              },
              child: Icon(Icons.info_outline, color: kWhiteColor),
            ).paddingOnly(right: 12),
          ],
        ),
        body: AnnotatedRegion(
          value: const SystemUiOverlayStyle(
            statusBarColor: kPrimaryColor,
            statusBarBrightness: Brightness.light,
            statusBarIconBrightness: Brightness.light,
          ),
          child: GetBuilder<UserAttendanceController>(
            init: UserAttendanceController(
              repository: UserAttendanceRepository(),
            ),
            builder:
                (c) => LayoutBuilder(
                  builder: (ctx, constraints) {
                    final calH = constraints.maxHeight * 0.48;
                    final mapH = constraints.maxHeight - calH;
                    return Column(
                      children: [
                        SizedBox(
                          height: calH - 38,
                          child:
                              c.isLoading
                                  ? CommonSkeletonCalender()
                                  : MonthlyScreen(
                                    initialMonth: c.visibleMonth,
                                    attendanceMap: c.attendanceMap,
                                    didChangeDate: (p0) => c.onMonthChanged(p0),
                                    onDateSelectedTap:
                                        (date) => c.onDateSelected(date),
                                  ),
                        ),
                        if (c.hasTodayAttendance)
                          SizedBox(
                            height: mapH,
                            child:
                                c.isMapReady
                                    ? _MapSection(c: c)
                                    : const _MapShimmer(),
                          ),
                      ],
                    );
                  },
                ),
          ),
        ),
      ),
    );
  }
}

class _MapSection extends StatelessWidget {
  final UserAttendanceController c;

  const _MapSection({required this.c});

  List<Marker> _buildMarkers() {
    final markers = <Marker>[];

    if (c.currentLat != 0 || c.currentLng != 0) {
      markers.add(
        Marker(
          point: LatLng(c.currentLat, c.currentLng),
          child: const Icon(Icons.location_pin, color: Colors.orange, size: 36),
        ),
      );
    }

    if (c.campLat != 0 || c.campLng != 0) {
      markers.add(
        Marker(
          point: LatLng(c.campLat, c.campLng),
          child: const Icon(Icons.location_pin, color: Colors.blue, size: 36),
        ),
      );
    }

    return markers;
  }

  @override
  Widget build(BuildContext context) {
    final hasCamp = c.campLat != 0 || c.campLng != 0;
    return Column(
      children: [
        // Current Location | Camp Location side by side
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 6),
          child: Row(
            children: [
              Expanded(
                child: _locationCard(
                  iconColor: Colors.orange,
                  label: 'Current Location',
                  value:
                      '${c.currentLat.toStringAsFixed(7)}, ${c.currentLng.toStringAsFixed(7)}',
                ),
              ),
              const SizedBox(width: 8),
              Expanded(
                child: _locationCard(
                  iconColor: Colors.blue,
                  label: 'Camp Location',
                  value:
                      hasCamp
                          ? '${c.campLat.toStringAsFixed(7)}, ${c.campLng.toStringAsFixed(7)}'
                          : '0.0, 0.0',
                ),
              ),
            ],
          ),
        ),

        // Distance banner
        Container(
          width: double.infinity,
          color: Colors.lightBlue[100],
          padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
          child: CommonText(
            text:
                hasCamp
                    ? 'Distance between current location and camp location is ${c.distanceKm.toStringAsFixed(2)} km'
                    : 'Distance between current location and camp location is NA',
            fontSize: 12,
            fontWeight: FontWeight.normal,
            textColor: kBlackColor,
            textAlign: TextAlign.start,
          ),
        ).paddingSymmetric(horizontal: 10),
        // OpenStreetMap — no API key needed
        Expanded(
          child: FlutterMap(
            options: MapOptions(
              initialCenter: LatLng(c.currentLat, c.currentLng),
              initialZoom: 14,
            ),
            children: [
              TileLayer(
                urlTemplate: 'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
                userAgentPackageName: 'com.s2t.operational',
              ),
              MarkerLayer(markers: _buildMarkers()),
            ],
          ).paddingSymmetric(horizontal: 10),
        ),
      ],
    );
  }
}

Widget _locationCard({
  required Color iconColor,
  required String label,
  required String value,
}) {
  return Container(
    padding: const EdgeInsets.all(8),
    decoration: BoxDecoration(
      color: Colors.white,
      borderRadius: BorderRadius.circular(8),
      boxShadow: const [
        BoxShadow(color: Colors.black12, blurRadius: 4, offset: Offset(0, 1)),
      ],
    ),
    child: Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Icon(Icons.location_on, color: iconColor, size: 20),
        const SizedBox(width: 6),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              CommonText(
                text: label,
                fontSize: 12,
                fontWeight: FontWeight.bold,
                textColor: kBlackColor,
                textAlign: TextAlign.start,
              ),

              const SizedBox(height: 2),

              CommonText(
                text: value,
                fontSize: 10,
                fontWeight: FontWeight.normal,
                textColor: kTextColor,
                textAlign: TextAlign.start,
              ),
            ],
          ),
        ),
      ],
    ),
  );
}

class _MapShimmer extends StatelessWidget {
  const _MapShimmer();

  @override
  Widget build(BuildContext context) {
    return Skeletonizer(
      enabled: true,
      child: Column(
        children: [
          // Real cards with empty values — shimmer animates over them
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 6),
            child: Row(
              children: [
                Expanded(
                  child: _locationCard(
                    iconColor: Colors.orange,
                    label: 'Current Location',
                    value: '00.0000000, 00.0000000',
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: _locationCard(
                    iconColor: Colors.blue,
                    label: 'Camp Location',
                    value: '00.0000000, 00.0000000',
                  ),
                ),
              ],
            ),
          ),
          // Distance banner
          Container(
            width: double.infinity,
            color: Colors.lightBlue[100],
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
            child: const Text(
              'Distance between current location and camp location is --',
              style: TextStyle(fontSize: 12, color: Colors.black87),
            ),
          ).paddingSymmetric(horizontal: 8),
          // Map area shimmer
          Expanded(
            child: Container(
              color: Colors.grey[300],
            ).paddingSymmetric(horizontal: 8),
          ),
        ],
      ),
    );
  }
}
