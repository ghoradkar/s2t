import 'dart:async';
import 'dart:io';

import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';

class GlucoseDeviceController extends GetxController {
  final UserAttendancesUsingSitedetailsIDOutput? patientItem;

  GlucoseDeviceController({this.patientItem});

  final RxBool isScanning = false.obs;
  final RxBool deviceFound = false.obs;
  final RxBool dataReceived = false.obs;
  final RxBool scanButtonDisabled = false.obs;

  final RxString deviceName = '—'.obs;
  final RxString macAddress = '—'.obs;
  final RxString batteryLevel = '—'.obs;
  final RxString statusStr = 'Ready to scan'.obs;
  final RxString dataStr = '—'.obs;
  final RxString rawDataStr = '—'.obs;

  StreamSubscription<List<ScanResult>>? _scanSub;
  StreamSubscription<List<int>>? _glucoseSub;
  BluetoothDevice? _connectedDevice;
  bool _connecting = false;

  // Short UUID segments — .contains() handles both full and short UUID formats
  static const String _glucoseSvcShort = '1808';
  static const String _glucoseMeasurementShort = '2a18';
  static const String _racpShort = '2a52';
  static const String _batteryServiceShort = '180f';
  static const String _batteryCharShort = '2a19';
  static const String _genericAccessShort = '1800';
  static const String _deviceNameCharShort = '2a00';

  @override
  void onClose() {
    _scanSub?.cancel();
    _glucoseSub?.cancel();
    FlutterBluePlus.stopScan();
    _connectedDevice?.disconnect();
    super.onClose();
  }

  Future<void> startScan() async {
    if (isScanning.value || _connecting) return;
    isScanning.value = true;
    scanButtonDisabled.value = false;
    deviceFound.value = false;
    dataReceived.value = false;
    deviceName.value = '—';
    macAddress.value = '—';
    batteryLevel.value = '—';
    dataStr.value = '—';
    rawDataStr.value = '—';
    statusStr.value = 'Scanning for devices...';
    _connecting = false;

    if (_connectedDevice != null) {
      try {
        await _connectedDevice!.disconnect();
      } catch (_) {}
      _connectedDevice = null;
    }

    _scanSub?.cancel();
    try {
      await FlutterBluePlus.startScan(timeout: const Duration(seconds: 30));
      _scanSub = FlutterBluePlus.scanResults.listen(_onScanResults);

      FlutterBluePlus.isScanning.where((s) => !s).first.then((_) {
        if (isScanning.value && !_connecting && !deviceFound.value) {
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

  void _onScanResults(List<ScanResult> results) {
    if (_connecting || deviceFound.value) return;

    final target = results.firstWhereOrNull((r) {
      final name = r.device.platformName;
      return name.startsWith('Meter') && !name.toLowerCase().contains('spiro');
    });

    if (target == null) return;

    _connecting = true;
    deviceFound.value = true;
    _scanSub?.cancel();
    FlutterBluePlus.stopScan();
    isScanning.value = false;

    deviceName.value = target.device.platformName;
    macAddress.value = target.device.remoteId.toString();
    statusStr.value = 'Device found! Connecting...';

    _connectAndRead(target.device);
  }

  Future<void> _connectAndRead(BluetoothDevice device) async {
    try {
      // Clear any stale GATT cache
      try {
        await device.disconnect();
      } catch (_) {}
      await Future.delayed(const Duration(milliseconds: 500));

      // ── Step 1: GATT connect ──────────────────────────────────────────────
      // flutter_blue_plus requires a GATT connection before createBond()
      await device.connect(
        license: License.free,
        timeout: const Duration(seconds: 15),
        autoConnect: false,
      );
      _connectedDevice = device;
      statusStr.value = 'Connected. Initiating pairing...';

      // ── Step 2: Bond / pair (matching native connectToDevice) ─────────────
      // createBond() shows the Android system pairing dialog and waits until
      // the user enters the PIN and bonding completes (90s default timeout).
      // If already bonded, it returns immediately without showing the dialog.
      if (Platform.isAndroid) {
        final currentBond = await device.bondState.first;
        if (currentBond != BluetoothBondState.bonded) {
          statusStr.value = 'Device not paired. Initiating pairing...';
          await Future.delayed(const Duration(milliseconds: 300));
          statusStr.value = 'Enter PIN on the system popup and confirm.';
          await device.createBond(timeout: 90);
          statusStr.value = 'Paired. Connecting...';
        } else {
          statusStr.value = 'Device already paired. Connecting...';
        }
      }

      if (!_connecting) return;

      // ── Step 3: Discover services ─────────────────────────────────────────
      statusStr.value = 'Connected to device. Discovering services...';
      final services = await device.discoverServices();

      // Debug: log all service UUIDs
      final svcUuids =
          services.map((s) => s.uuid.toString().toLowerCase()).toList();
      final hasGlucose = svcUuids.any((u) => u.contains(_glucoseSvcShort));
      if (!hasGlucose) {
        statusStr.value = 'Services found: ${svcUuids.join(', ')}';
        _connecting = false;
        return;
      }

      await _readDeviceName(device, services);
      await _enableGlucoseNotifications(device, services);
      await Future.delayed(const Duration(seconds: 1));
      await _readBattery(device, services);
      await Future.delayed(const Duration(seconds: 1));
      await _requestStoredRecords(device, services);

      _connecting = false;
    } catch (e) {
      statusStr.value = 'Connection failed. Tap SCAN to retry.';
      _connecting = false;
      deviceFound.value = false;
      scanButtonDisabled.value = false;
    }
  }

  Future<void> _readDeviceName(
    BluetoothDevice device,
    List<BluetoothService> services,
  ) async {
    try {
      final svc = services.firstWhereOrNull(
        (s) => s.uuid.toString().toLowerCase().contains(_genericAccessShort),
      );
      final char = svc?.characteristics.firstWhereOrNull(
        (c) => c.uuid.toString().toLowerCase().contains(_deviceNameCharShort),
      );
      if (char != null && char.properties.read) {
        final val = await char.read();
        final name = String.fromCharCodes(val);
        if (name.isNotEmpty) deviceName.value = name;
      }
    } catch (_) {}
  }

  Future<void> _enableGlucoseNotifications(
    BluetoothDevice device,
    List<BluetoothService> services,
  ) async {
    try {
      final glucoseSvc = services.firstWhereOrNull(
        (s) => s.uuid.toString().toLowerCase().contains(_glucoseSvcShort),
      );
      if (glucoseSvc == null) {
        statusStr.value = 'Glucose service not found.';
        return;
      }

      final glucoseChar = glucoseSvc.characteristics.firstWhereOrNull(
        (c) =>
            c.uuid.toString().toLowerCase().contains(_glucoseMeasurementShort),
      );
      if (glucoseChar != null &&
          (glucoseChar.properties.notify || glucoseChar.properties.indicate)) {
        await glucoseChar.setNotifyValue(true);
        _glucoseSub = glucoseChar.onValueReceived.listen(
          _onGlucoseNotification,
        );
        statusStr.value = 'Enabled notifications for Glucose Measurement.';
      }

      final racpChar = glucoseSvc.characteristics.firstWhereOrNull(
        (c) => c.uuid.toString().toLowerCase().contains(_racpShort),
      );
      if (racpChar != null &&
          (racpChar.properties.notify || racpChar.properties.indicate)) {
        await racpChar.setNotifyValue(true);
      }
    } catch (e) {
      statusStr.value = 'Notification setup error: $e';
    }
  }

  Future<void> _readBattery(
    BluetoothDevice device,
    List<BluetoothService> services,
  ) async {
    try {
      final svc = services.firstWhereOrNull(
        (s) => s.uuid.toString().toLowerCase().contains(_batteryServiceShort),
      );
      final char = svc?.characteristics.firstWhereOrNull(
        (c) => c.uuid.toString().toLowerCase().contains(_batteryCharShort),
      );
      if (char != null && char.properties.read) {
        statusStr.value = 'Reading Battery Level...';
        final val = await char.read();
        if (val.isNotEmpty) batteryLevel.value = '${val[0] & 0xFF}%';
      }
    } catch (_) {}
  }

  Future<void> _requestStoredRecords(
    BluetoothDevice device,
    List<BluetoothService> services,
  ) async {
    try {
      final glucoseSvc = services.firstWhereOrNull(
        (s) => s.uuid.toString().toLowerCase().contains(_glucoseSvcShort),
      );
      final racpChar = glucoseSvc?.characteristics.firstWhereOrNull(
        (c) => c.uuid.toString().toLowerCase().contains(_racpShort),
      );
      if (racpChar != null && racpChar.properties.write) {
        await racpChar.write([0x01, 0x01], withoutResponse: false);
        statusStr.value = 'Requested glucose records';
      }
    } catch (e) {
      statusStr.value = 'RACP write error: $e';
    }
  }

  void _onGlucoseNotification(List<int> data) {
    rawDataStr.value = data
        .map((b) => b.toRadixString(16).padLeft(2, '0').toUpperCase())
        .join(' ');

    final glucose = _parseGlucoseMeasurement(data);
    final timestamp = _parseGlucoseTimestamp(data);

    if (glucose >= 0) {
      dataStr.value = '${glucose.toStringAsFixed(2)} mg/dL $timestamp'.trim();
      statusStr.value = 'Data received!';
      dataReceived.value = true;
      scanButtonDisabled.value = true;
    }
  }

  // Port of native parseGlucoseMeasurement
  double _parseGlucoseMeasurement(List<int> data) {
    if (data.length < 12) return -1;

    final flags = data[0] & 0xFF;
    final lsb = data[10] & 0xFF;
    final msb = data[11] & 0xFF;
    final raw16 = (msb << 8) | lsb;

    int mantissa = raw16 & 0x0FFF;
    int exponent = (raw16 >> 12) & 0x0F;

    if (mantissa >= 0x800) mantissa -= 0x1000;
    if (exponent >= 0x8) exponent -= 0x10;

    final sfloat = mantissa * _pow10(exponent);
    const unitMolarMask = 0x04;
    final isMolPerL = (flags & unitMolarMask) != 0;

    if (!isMolPerL) {
      return double.parse((sfloat * 100000.0).toStringAsFixed(2));
    } else {
      final mmolPerL = sfloat * 1000.0;
      return double.parse((mmolPerL * 18.01559).toStringAsFixed(2));
    }
  }

  double _pow10(int exp) {
    if (exp == 0) return 1.0;
    double result = 1.0;
    if (exp > 0) {
      for (int i = 0; i < exp; i++) result *= 10.0;
    } else {
      for (int i = 0; i < -exp; i++) result /= 10.0;
    }
    return result;
  }

  // Port of native parseGlucoseTimestamp
  String _parseGlucoseTimestamp(List<int> data) {
    if (data.length < 10) return '';
    final year = ((data[4] & 0xFF) << 8) | (data[3] & 0xFF);
    final month = data[5] & 0xFF;
    final day = data[6] & 0xFF;
    final hour = data[7] & 0xFF;
    final minute = data[8] & 0xFF;
    return '$year-${_pad(month)}-${_pad(day)} ${_pad(hour)}:${_pad(minute)}';
  }

  String _pad(int n) => n.toString().padLeft(2, '0');
}

class GlucoseResult {
  final String glucose;
  final String deviceNameStr;

  const GlucoseResult({required this.glucose, required this.deviceNameStr});
}
