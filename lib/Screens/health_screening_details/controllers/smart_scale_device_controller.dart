import 'dart:async';

import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';

class SmartScaleDeviceController extends GetxController {
  final double heightCm;
  final UserAttendancesUsingSitedetailsIDOutput? patientItem;

  SmartScaleDeviceController({
    required this.heightCm,
    this.patientItem,
  });

  final RxBool isScanning = false.obs;
  final RxBool deviceFound = false.obs;
  final RxBool scanButtonDisabled = false.obs;

  final RxString deviceName = '—'.obs;
  final RxString macAddress = '—'.obs;
  final RxString weightStr = '—'.obs;
  final RxString heightStr = '—'.obs;
  final RxString bmiStr = '—'.obs;
  final RxString statusStr = 'Ready to scan'.obs;

  StreamSubscription<List<ScanResult>>? _scanSub;

  @override
  void onInit() {
    super.onInit();
    heightStr.value = heightCm > 0 ? heightCm.toStringAsFixed(1) : '—';
  }

  @override
  void onClose() {
    _scanSub?.cancel();
    FlutterBluePlus.stopScan();
    super.onClose();
  }

  Future<void> startScan() async {
    if (isScanning.value) return;
    isScanning.value = true;
    scanButtonDisabled.value = false;
    deviceFound.value = false;
    deviceName.value = '—';
    macAddress.value = '—';
    weightStr.value = '—';
    bmiStr.value = '—';
    statusStr.value = 'Scanning...';

    _scanSub?.cancel();
    try {
      await FlutterBluePlus.startScan(
        withNames: ['Yoda1'],
        timeout: const Duration(seconds: 30),
      );
      _scanSub = FlutterBluePlus.scanResults.listen(_onScanResults);

      FlutterBluePlus.isScanning.where((s) => !s).first.then((_) {
        if (isScanning.value && !deviceFound.value) {
          isScanning.value = false;
          statusStr.value = 'No device found. Tap SCAN to retry.';
        }
      });
    } catch (e) {
      statusStr.value = 'Scan error: ${e.toString()}';
      isScanning.value = false;
    }
  }

  Future<void> stopScan() async {
    if (scanButtonDisabled.value) return;
    _scanSub?.cancel();
    await FlutterBluePlus.stopScan();
    isScanning.value = false;
    statusStr.value = 'Scan stopped. Tap SCAN to retry.';
  }

  // Native app reads weight directly from BLE advertisement data (scanRecord.bytes).
  // No GATT connection needed — Yoda1 broadcasts weight in manufacturer-specific AD payload.
  // Raw layout (same as native parseData):
  //   raw[0]=0x10 (length), raw[1]=0xFF (manufacturer-specific type)
  //   raw[2]=version, raw[3]=serialNum  → flutter manufacturerData key
  //   raw[4]=wHigh, raw[5]=wLow         → flutter payload[0], payload[1]
  //   raw[6..7]=resistance, raw[8..9]=productId, raw[10]=msgProp, raw[11..16]=MAC
  void _onScanResults(List<ScanResult> results) {
    if (deviceFound.value) return;

    for (final result in results) {
      if (result.device.platformName != 'Yoda1') continue;

      final mfData = result.advertisementData.manufacturerData;
      if (mfData.isEmpty) {
        // Device seen but no manufacturer data yet — update name/MAC and wait
        deviceName.value = result.device.platformName;
        macAddress.value = result.device.remoteId.toString();
        statusStr.value = 'Device Found! Processing data...';
        continue;
      }

      final payload = mfData.values.first;
      if (payload.length < 2) continue;

      deviceName.value = result.device.platformName;
      macAddress.value = result.device.remoteId.toString();
      statusStr.value = 'Device Found! Processing data...';

      final wHigh = payload[0] & 0xFF;
      final wLow = payload[1] & 0xFF;
      final double weight = ((wHigh << 8) | wLow) / 10.0;

      if (weight > 0) {
        weightStr.value = weight.toStringAsFixed(1);
        if (heightCm > 0) {
          final hm = heightCm / 100;
          bmiStr.value = (weight / (hm * hm)).toStringAsFixed(1);
        }
        statusStr.value = 'Success';

        deviceFound.value = true;
        scanButtonDisabled.value = true;
        _scanSub?.cancel();
        FlutterBluePlus.stopScan();
        isScanning.value = false;
      } else {
        statusStr.value = 'Device found. Step on scale...';
      }
      break;
    }
  }
}

class SmartScaleResult {
  final String weight;
  final String bmi;
  final String deviceNameStr;

  const SmartScaleResult({
    required this.weight,
    required this.bmi,
    required this.deviceNameStr,
  });
}