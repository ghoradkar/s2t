import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';

class BleDeviceListScreen extends StatefulWidget {
  final String title;
  /// If set, only devices whose name starts with this prefix are shown.
  final String? namePrefix;
  const BleDeviceListScreen({super.key, required this.title, this.namePrefix});

  @override
  State<BleDeviceListScreen> createState() => _BleDeviceListScreenState();
}

class _BleDeviceListScreenState extends State<BleDeviceListScreen> {
  bool _isScanning = false;
  List<ScanResult> _results = [];
  String _error = '';
  StreamSubscription<List<ScanResult>>? _scanSub;

  @override
  void initState() {
    super.initState();
    _startScan();
  }

  @override
  void dispose() {
    _scanSub?.cancel();
    FlutterBluePlus.stopScan();
    super.dispose();
  }

  Future<void> _startScan() async {
    if (!mounted) return;
    setState(() {
      _isScanning = true;
      _results = [];
      _error = '';
    });
    _scanSub?.cancel();
    try {
      await FlutterBluePlus.startScan(timeout: const Duration(seconds: 15));
      _scanSub = FlutterBluePlus.scanResults.listen((results) {
        if (mounted) {
          setState(() {
            _results = results.where((r) {
              final name = r.device.platformName;
              if (name.isEmpty) return false;
              final prefix = widget.namePrefix;
              if (prefix != null && prefix.isNotEmpty) {
                return name.startsWith(prefix);
              }
              return true;
            }).toList();
          });
        }
      });
      await Future.delayed(const Duration(seconds: 15));
    } catch (e) {
      if (mounted) setState(() => _error = e.toString());
    }
    if (mounted) setState(() => _isScanning = false);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: kBackground,
      appBar: mAppBar(
        scTitle: widget.title,
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
      ),
      body: _buildBody(),
    );
  }

  Widget _buildBody() {
    if (_error.isNotEmpty) {
      return Center(
        child: Padding(
          padding: EdgeInsets.symmetric(horizontal: 24.w),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.bluetooth_disabled, size: 48.r, color: Colors.red.shade400),
              SizedBox(height: 12.h),
              Text(
                _error,
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 13.sp,
                  color: Colors.red.shade700,
                ),
              ),
              SizedBox(height: 20.h),
              _retryButton(),
            ],
          ),
        ),
      );
    }

    if (_isScanning && _results.isEmpty) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SizedBox(
              width: 48.r,
              height: 48.r,
              child: CircularProgressIndicator(color: kPrimaryColor, strokeWidth: 3),
            ),
            SizedBox(height: 16.h),
            Text(
              'Scanning for devices...',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 14.sp,
                color: kTextColor,
              ),
            ),
          ],
        ),
      );
    }

    if (_results.isEmpty) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.bluetooth_searching, size: 48.r, color: kLabelTextColor),
            SizedBox(height: 12.h),
            Text(
              'No devices found',
              style: TextStyle(
                fontFamily: FontConstants.interFonts,
                fontSize: 14.sp,
                color: kTextColor,
              ),
            ),
            SizedBox(height: 20.h),
            _retryButton(),
          ],
        ),
      );
    }

    return Column(
      children: [
        if (_isScanning)
          LinearProgressIndicator(
            color: kPrimaryColor,
            backgroundColor: kPrimaryColor.withOpacity(0.15),
            minHeight: 3,
          ),
        Expanded(
          child: ListView.separated(
            padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 8.h),
            itemCount: _results.length,
            separatorBuilder: (_, __) => Divider(height: 1, color: droDownBGColor),
            itemBuilder: (context, i) {
              final r = _results[i];
              return ListTile(
                leading: CircleAvatar(
                  backgroundColor: kPrimaryColor.withOpacity(0.12),
                  child: Icon(Icons.bluetooth, color: kPrimaryColor, size: 20.r),
                ),
                title: Text(
                  r.device.platformName,
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w600,
                    color: kTextColor,
                  ),
                ),
                subtitle: Text(
                  r.device.remoteId.toString(),
                  style: TextStyle(
                    fontFamily: FontConstants.interFonts,
                    fontSize: 11.sp,
                    color: kLabelTextColor,
                  ),
                ),
                trailing: Container(
                  padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 4.h),
                  decoration: BoxDecoration(
                    color: kBackground,
                    borderRadius: BorderRadius.circular(6.r),
                    border: Border.all(color: droDownBGColor),
                  ),
                  child: Text(
                    '${r.rssi} dBm',
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      fontSize: 11.sp,
                      color: kLabelTextColor,
                    ),
                  ),
                ),
                onTap: () => Navigator.pop(context, r.device),
              );
            },
          ),
        ),
      ],
    );
  }

  Widget _retryButton() {
    return ElevatedButton.icon(
      onPressed: _startScan,
      icon: Icon(Icons.refresh, size: 18.r),
      label: Text(
        'Retry Scan',
        style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontSize: 13.sp,
            fontWeight: FontWeight.w600),
      ),
      style: ElevatedButton.styleFrom(
        backgroundColor: kPrimaryColor,
        foregroundColor: Colors.white,
        padding: EdgeInsets.symmetric(horizontal: 24.w, vertical: 12.h),
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8.r)),
      ),
    );
  }
}