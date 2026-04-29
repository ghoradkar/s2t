import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Screens/health_screening_details/models/lung_function_test_model.dart';
import 'package:s2toperational/Screens/health_screening_details/models/patient_list_model.dart';
import 'package:s2toperational/Screens/health_screening_details/repository/health_screening_repository.dart';

enum LftDeviceStatus { idle, scanning, connected, testing, done }

class LungFunctionTestController extends GetxController {
  final HealthScreeningRepository _repo = HealthScreeningRepository();

  final UserAttendancesUsingSitedetailsIDOutput patient;
  final int campId;

  LungFunctionTestController({required this.patient, required this.campId});

  static const _method = MethodChannel('com.s2toperational/safey_spirometry');
  static const _events = EventChannel(
    'com.s2toperational/safey_spirometry_events',
  );

  // ── Device state ───────────────────────────────────────────────────────────
  final Rx<LftDeviceStatus> deviceStatus = LftDeviceStatus.idle.obs;
  final RxString connectedDeviceName = ''.obs;
  final RxString connectedDeviceAddress = ''.obs;

  // Latest discovered device (shown in UI as "Device Found [name] [address]")
  final Rx<Map<String, String>?> foundDevice = Rx<Map<String, String>?>(null);

  final RxList<Map<String, String>> scannedDevices =
      <Map<String, String>>[].obs;

  // ── Test state ─────────────────────────────────────────────────────────────
  final RxInt testProgress = 0.obs;
  final RxString infoMessage = ''.obs;
  final RxBool hasResult = false.obs;
  final Rx<LungFunctionTestResult?> result = Rx(null);

  // ── Submission ─────────────────────────────────────────────────────────────
  final RxBool isSubmitting = false.obs;

  StreamSubscription<dynamic>? _eventSub;

  // ── Lifecycle ──────────────────────────────────────────────────────────────

  @override
  void onInit() {
    super.onInit();
    _listenToEvents();
    // Auto-scan on open, matching native behaviour (brief delay for SDK init)
    Future.delayed(const Duration(milliseconds: 400), () {
      if (!isClosed) scan();
    });
  }

  @override
  void onClose() {
    _eventSub?.cancel();
    _method.invokeMethod<void>('disconnect');
    super.onClose();
  }

  // ── Event stream ───────────────────────────────────────────────────────────

  void _listenToEvents() {
    _eventSub = _events.receiveBroadcastStream().listen(
      _onEvent,
      onError: (dynamic err) {
        debugPrint('SafeyEvent error: $err');
        ToastManager.toast('Spirometer error: $err');
      },
    );
  }

  void _onEvent(dynamic raw) {
    if (raw is! Map) return;
    final event = Map<String, dynamic>.from(raw);
    final type = event['type'] as String? ?? '';

    switch (type) {
      case 'deviceFound':
        final name = event['name'] as String? ?? 'Safey Device';
        final address = event['address'] as String? ?? '';
        if (address.isNotEmpty) {
          ToastManager.hideLoader();
          foundDevice.value = {'name': name, 'address': address};
          if (scannedDevices.every((d) => d['address'] != address)) {
            scannedDevices.add({'name': name, 'address': address});
          }
          infoMessage.value = 'Device Found\n$name $address';
        }
        break;

      case 'connected':
        // For auto-connect (lastConnectedDeviceFound), use foundDevice name
        if (connectedDeviceName.value.isEmpty && foundDevice.value != null) {
          connectedDeviceName.value =
              foundDevice.value!['name'] ?? 'Safey Device';
          connectedDeviceAddress.value = foundDevice.value!['address'] ?? '';
        }
        deviceStatus.value = LftDeviceStatus.connected;
        // Don't overwrite infoMessage — batteryStatus event fires before this
        // and sets the "Device found: [name] [address] Battery: X%" message.
        ToastManager.hideLoader();

        ToastManager().showSuccessOkayDialog(
          context: Get.context!,
          title: 'Connected',
          message: 'Connected to device now you can start Test',
          onTap: () {
            Get.back();
          },
        );

        // Get.dialog(
        //   AlertDialog(
        //     title: const Text('Connected'),
        //     content: const Text('Connected to device now you can start Test'),
        //     actions: [TextButton(onPressed: Get.back, child: const Text('OK'))],
        //   ),
        //   barrierDismissible: false,
        // );
        break;

      case 'disconnected':
        if (deviceStatus.value != LftDeviceStatus.done) {
          deviceStatus.value = LftDeviceStatus.idle;
        }
        break;

      case 'progress':
        final value = (event['value'] as int?) ?? 0;
        testProgress.value = value;
        if (deviceStatus.value != LftDeviceStatus.testing) {
          deviceStatus.value = LftDeviceStatus.testing;
        }
        break;

      case 'info':
        final code = event['code'] as String? ?? '';
        if (code == 'INF_01') {
          // Device not found — reset to idle and show alert like native
          ToastManager.hideLoader();
          deviceStatus.value = LftDeviceStatus.idle;
          infoMessage.value = '';

          ToastManager.showAlertDialog(
            Get.context!,
            "Safey device not found around you.",
            () {
              Get.back();
            },
            title: "Device Not Found",
          );
          // Get.dialog(
          //   AlertDialog(
          //     title: const Text('Device Not Found'),
          //     content: const Text('Safey device not found around you.'),
          //     actions: [
          //       TextButton(onPressed: Get.back, child: const Text('OK')),
          //     ],
          //   ),
          //   barrierDismissible: false,
          // );
        } else {
          final msg = _codeToMessage(code);
          if (msg.isNotEmpty) infoMessage.value = msg;
        }
        break;

      case 'batteryStatus':
        final battery = event['battery'] as String? ?? '';
        final bName = event['name'] as String? ?? connectedDeviceName.value;
        final bAddress =
            event['address'] as String? ?? connectedDeviceAddress.value;
        infoMessage.value =
            'Device found :\n$bName $bAddress\nBattery Percentage : $battery%';
        break;

      case 'testResult':
        _handleTestResult(event);
        break;

      case 'error':
        final msg = event['message'] as String? ?? 'Unknown error';
        debugPrint('SafeyPlugin error: $msg');
        ToastManager.hideLoader();
        ToastManager.toast('Device error: $msg');
        if (deviceStatus.value == LftDeviceStatus.scanning ||
            deviceStatus.value == LftDeviceStatus.testing) {
          deviceStatus.value = LftDeviceStatus.idle;
        }
        break;
    }
  }

  void _handleTestResult(Map<String, dynamic> event) {
    final jsonStr = event['json'] as String? ?? '';
    final sessionScore = event['sessionScore'] as String? ?? '';
    final deviceId = connectedDeviceAddress.value;

    result.value = LungFunctionTestResult.fromSafeyJson(
      jsonStr,
      sessionScore,
      deviceId,
    );
    hasResult.value = true;
    deviceStatus.value = LftDeviceStatus.done;
    infoMessage.value = 'Test complete. Review results below.';
    testProgress.value = 100;
  }

  String _codeToMessage(String code) {
    switch (code) {
      case 'INF_01':
        return 'Safey device not found nearby. Please try again.';
      case 'INF_03':
        return 'Invalid trial — please retry.';
      case 'INF_05':
        return 'Start blowing — blow hard and fast into the device!';
      case 'INF_10':
        return 'Done!';
      case 'INF_11':
        return 'Start blowing — blow hard and fast into the device!';
      case 'INF_12':
        return 'Keep blowing!';
      case 'INF_07':
        return 'Test complete. Checking results…';
      case 'INF_13':
        return 'Insufficient blow — please try harder.';
      case 'INF_17':
        return 'Timeout — please blow more forcefully next time.';
      case 'INF_18':
        return 'Searching for device…';
      case 'INF_19':
        return 'Device ready.';
      case 'INF_20':
        return '';
      case 'INF_22':
        return 'Connection error. Retrying…';
      case 'testCompleted':
        return 'Post-test complete.';
      default:
        return code;
    }
  }

  // ── Device actions ─────────────────────────────────────────────────────────

  Future<void> scan() async {
    // Android 12+ requires BLUETOOTH_SCAN + BLUETOOTH_CONNECT at runtime
    final statuses =
        await [Permission.bluetoothScan, Permission.bluetoothConnect].request();

    final allGranted = statuses.values.every((s) => s.isGranted);
    if (!allGranted) {
      ToastManager.toast(
        'Bluetooth permissions are required to scan for devices.',
      );
      deviceStatus.value = LftDeviceStatus.idle;
      return;
    }

    scannedDevices.clear();
    foundDevice.value = null;
    deviceStatus.value = LftDeviceStatus.scanning;
    infoMessage.value = 'Scanning for Safey spirometer…';
    ToastManager.showLoader();
    try {
      await _method.invokeMethod<void>('scan');
    } catch (e) {
      ToastManager.hideLoader();
      deviceStatus.value = LftDeviceStatus.idle;
      ToastManager.toast('Scan failed: $e');
    }
  }

  Future<void> connect(String address, String name) async {
    connectedDeviceName.value = name;
    connectedDeviceAddress.value = address;
    infoMessage.value = 'Connecting to $name…';
    try {
      await _method.invokeMethod<void>('connect', {'address': address});
    } catch (e) {
      ToastManager.toast('Connect failed: $e');
    }
  }

  Future<void> startTest() async {
    if (deviceStatus.value != LftDeviceStatus.connected) {
      ToastManager.toast('Please connect to a spirometer first.');
      return;
    }
    testProgress.value = 0;
    infoMessage.value = 'Starting test…';
    deviceStatus.value = LftDeviceStatus.testing;

    final gender = (patient.gender ?? '').toUpperCase().startsWith('M') ? 1 : 2;
    final weight = (patient.weightKGs as num?)?.toInt() ?? 60;
    final age = (patient.age as num?)?.toDouble() ?? 30.0;
    final height = (patient.heightCMs as num?)?.toInt() ?? 165;

    try {
      await _method.invokeMethod<void>('startTest', {
        'gender': gender,
        'weight': weight,
        'age': age,
        'height': height,
      });
    } catch (e) {
      deviceStatus.value = LftDeviceStatus.connected;
      ToastManager.toast('Start test failed: $e');
    }
  }

  // ── Submission ─────────────────────────────────────────────────────────────

  Future<void> submit(BuildContext context) async {
    if (!hasResult.value || result.value == null) {
      ToastManager.toast('Please complete the spirometry test first.');
      return;
    }

    final empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    final r = result.value!;

    isSubmitting.value = true;
    ToastManager.showLoader();

    final response = await _repo.submitLFTDetails(
      regId: (patient.regdId ?? 0).toString(),
      campId: campId.toString(),
      createdBy: empCode.toString(),
      versionNo: '9.63',
      deviceId: r.deviceId,
      result: r,
    );

    ToastManager.hideLoader();
    isSubmitting.value = false;

    if (response != null &&
        (response['status'] as String? ?? '').toLowerCase() == 'success') {
      if (context.mounted) {
        showDialog(
          context: context,
          barrierDismissible: false,
          builder:
              (_) => AlertDialog(
                title: const Text('Success'),
                content: const Text(
                  'Lung Function Test submitted successfully.',
                ),
                actions: [
                  TextButton(
                    onPressed: () {
                      Navigator.of(context).pop();
                      Navigator.of(context).pop();
                    },
                    child: const Text('OK'),
                  ),
                ],
              ),
        );
      }
    } else {
      final msg =
          response?['message'] as String? ??
          'Submission failed. Please try again.';
      ToastManager.toast(msg);
    }
  }
}
