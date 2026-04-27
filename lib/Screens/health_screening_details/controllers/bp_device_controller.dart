import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:get/get.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'omron_bp_channel.dart';

class BpDeviceController extends GetxController {
  // ── Static in-memory cache — survives controller recreation within same app session.
  static String _sMac  = '';
  static String _sName = '';
  static int?   _sSys;
  static int?   _sDia;

  final RxBool isConnected = false.obs;
  final RxBool isConnecting = false.obs;
  final RxBool isWaitingForReading = false.obs;
  final RxString statusStr = RxString(_sMac.isNotEmpty ? 'Listening for new reading from device...' : 'Not connected');
  final RxString connectedDeviceName = ''.obs;
  final RxString connectedDeviceMac = ''.obs;
  final RxString savedDeviceMac  = RxString(_sMac);
  final RxString savedDeviceName = RxString(_sName);
  final RxString lastReadingStr  = RxString(
    (_sSys != null && _sDia != null) ? 'Systolic: $_sSys mmHg  |  Diastolic: $_sDia mmHg' : '',
  );

  BluetoothDevice? _device;
  BluetoothCharacteristic? _cmdChar;
  BluetoothCharacteristic? _dataChar;
  BluetoothCharacteristic? _statusChar;
  StreamSubscription<List<int>>? _notifySub;
  StreamSubscription<List<int>>? _notifySub2;
  StreamSubscription<BluetoothConnectionState>? _connectionSub;

  // Proactive background scan — started as soon as device is paired
  StreamSubscription<List<ScanResult>>? _proactiveScanSub;
  bool _proactiveScanRunning = false;

  bool _connecting = false;
  bool _clockSynced = false;
  bool _recordsRequested = false;
  int _currentUserSlot = 0;
  bool _triedSlot1 = false;

  int? _lastSystolic;
  int? _lastDiastolic;

  static const String _omronServiceShort    = 'fe4a';
  static const String _omronDataCharShort   = 'b305b680';
  static const String _omronCmdCharShort    = 'db5b55e0';
  static const String _omronStatusCharShort = '49123040';

  static const List<int> _cmdGetRecords = [0x15, 0x00, 0x01];

  static const String _prefMac       = 'bp_device_mac';
  static const String _prefName      = 'bp_device_name';
  static const String _prefSystolic  = 'bp_last_systolic';
  static const String _prefDiastolic = 'bp_last_diastolic';

  @override
  void onInit() {
    super.onInit();
    if (_sMac.isNotEmpty) {
      _lastSystolic = _sSys;
      _lastDiastolic = _sDia;
    }
    _loadSavedDevice();
  }

  @override
  void onClose() {
    _stopProactiveScan();
    _cancelSubs();
    _device?.disconnect();
    super.onClose();
  }

  void _cancelSubs() {
    _notifySub?.cancel();
    _notifySub2?.cancel();
    _connectionSub?.cancel();
  }

  // ── Proactive background BLE scan ────────────────────────────────────────
  // Starts scanning immediately when screen opens (if device is paired).
  // The native Omron app background service is always scanning. Flutter must
  // also be scanning at the moment the device takes a measurement — so we can
  // parse the reading from advertising data (~100 ms) before the native app
  // can complete its GATT transfer (~1.5 s).

  Future<void> _startProactiveScan() async {
    if (_proactiveScanRunning) return;
    final mac = savedDeviceMac.value.toLowerCase().replaceAll(':', '');
    if (mac.isEmpty) return;

    _proactiveScanRunning = true;
    print('BpDevice PROACTIVE: Starting background scan for $mac');

    try {
      if (FlutterBluePlus.isScanningNow) {
        await FlutterBluePlus.stopScan();
        await Future.delayed(const Duration(milliseconds: 200));
      }

      // Low-latency mode — highest scan frequency, best chance of catching
      // the short window when device advertises a stored reading.
      await FlutterBluePlus.startScan(
        androidScanMode: AndroidScanMode.lowLatency,
        // No timeout — runs until stopped manually
      );

      _proactiveScanSub = FlutterBluePlus.scanResults.listen((results) {
        if (!_proactiveScanRunning) return;
        for (final r in results) {
          final devMac = r.device.remoteId.toString().toLowerCase().replaceAll(':', '');
          if (devMac != mac) continue;

          _logAdvData(r);

          final reading = _parseBpFromAdvData(r.advertisementData.manufacturerData);
          if (reading != null) {
            print('BpDevice PROACTIVE: Got reading! sys=${reading[0]} dia=${reading[1]}');
            _applyReading(reading[0], reading[1]);
            _stopProactiveScan();
            return;
          }
        }
      });
    } catch (e) {
      print('BpDevice PROACTIVE: scan error: $e');
      _proactiveScanRunning = false;
    }
  }

  void _stopProactiveScan() {
    if (!_proactiveScanRunning) return;
    _proactiveScanRunning = false;
    _proactiveScanSub?.cancel();
    _proactiveScanSub = null;
    try { FlutterBluePlus.stopScan(); } catch (_) {}
    print('BpDevice PROACTIVE: stopped');
  }

  // ── Comprehensive advertising data logger ─────────────────────────────────

  void _logAdvData(ScanResult r) {
    final mfr = r.advertisementData.manufacturerData;
    if (mfr.isEmpty) {
      print('BpDevice ADV: no manufacturer data (rssi=${r.rssi})');
    } else {
      mfr.forEach((id, bytes) {
        print('BpDevice ADV mfr[0x${id.toRadixString(16).padLeft(4, '0')}] '
            '(${bytes.length}B): '
            '${bytes.map((b) => b.toRadixString(16).padLeft(2, '0')).join(' ')}');
      });
    }

    final svcUuids = r.advertisementData.serviceUuids;
    if (svcUuids.isNotEmpty) {
      print('BpDevice ADV serviceUuids: ${svcUuids.map((u) => u.toString()).join(', ')}');
    }

    final svcData = r.advertisementData.serviceData;
    svcData.forEach((uuid, bytes) {
      print('BpDevice ADV svcData[$uuid] (${bytes.length}B): '
          '${bytes.map((b) => b.toRadixString(16).padLeft(2, '0')).join(' ')}');
    });
  }

  // ── Persistence ──────────────────────────────────────────────────────────

  Future<void> _loadSavedDevice() async {
    final prefs = await SharedPreferences.getInstance();
    final mac  = prefs.getString(_prefMac)  ?? '';
    final name = prefs.getString(_prefName) ?? '';
    final sys  = prefs.getInt(_prefSystolic);
    final dia  = prefs.getInt(_prefDiastolic);
    _sMac = mac; _sName = name; _sSys = sys; _sDia = dia;
    savedDeviceMac.value  = mac;
    savedDeviceName.value = name;
    if (sys != null && dia != null) {
      _lastSystolic = sys;
      _lastDiastolic = dia;
      lastReadingStr.value = 'Systolic: $sys mmHg  |  Diastolic: $dia mmHg';
    }
    if (mac.isNotEmpty) {
      statusStr.value = 'Listening for new reading from device...';
      // Start background scan immediately — same as native Omron app's always-on scan
      _startProactiveScan();
    }
  }

  Future<void> _persistDevice(String mac, String name) async {
    _sMac = mac; _sName = name;
    savedDeviceMac.value  = mac;
    savedDeviceName.value = name;
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_prefMac,  mac);
    await prefs.setString(_prefName, name);
  }

  Future<void> _clearPersistedDevice() async {
    _sMac = ''; _sName = ''; _sSys = null; _sDia = null;
    savedDeviceMac.value  = '';
    savedDeviceName.value = '';
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_prefMac);
    await prefs.remove(_prefName);
    await prefs.remove(_prefSystolic);
    await prefs.remove(_prefDiastolic);
  }

  Future<void> _persistReading(int sys, int dia) async {
    _sSys = sys; _sDia = dia;
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt(_prefSystolic, sys);
    await prefs.setInt(_prefDiastolic, dia);
  }

  // ── SCAN: just save device, no BLE connection ────────────────────────────

  Future<void> saveDevice(BluetoothDevice device) async {
    final mac  = device.remoteId.toString();
    final name = device.platformName.isNotEmpty ? device.platformName : mac;
    await _persistDevice(mac, name);
    statusStr.value = 'Listening for new reading from device...';
    // Start proactive scan now that device is paired
    _startProactiveScan();
  }

  // ── Connect (BLE GATT only — no protocol commands) ───────────────────────

  Future<void> autoConnect() async {
    final mac = savedDeviceMac.value;
    if (mac.isEmpty || _connecting || isConnected.value) return;
    final device = BluetoothDevice(remoteId: DeviceIdentifier(mac));
    await connectDevice(device);
  }

  Future<void> connectDevice(BluetoothDevice device) async {
    if (_connecting) return;
    _connecting = true;
    isConnecting.value = true;
    isConnected.value = false;
    statusStr.value = 'Connecting...';

    try {
      _cancelSubs();
      try { await _device?.disconnect(); } catch (_) {}
      _cmdChar = null;
      _dataChar = null;
      _statusChar = null;

      await device.connect(
        license: License.free,
        timeout: const Duration(seconds: 15),
        autoConnect: false,
      );
      _device = device;
      connectedDeviceName.value = device.platformName;
      connectedDeviceMac.value  = device.remoteId.toString();

      _connectionSub = device.connectionState.listen((state) {
        if (state == BluetoothConnectionState.disconnected) {
          isConnected.value = false;
          statusStr.value = 'Disconnected. Tap TRANSFER to reconnect.';
        }
      });

      statusStr.value = 'Discovering services...';
      await Future.delayed(const Duration(milliseconds: 600));
      final services = await device.discoverServices();

      final omronSvc = services.firstWhereOrNull(
        (s) => s.uuid.toString().toLowerCase().contains(_omronServiceShort),
      );
      if (omronSvc == null) {
        statusStr.value = 'Device not supported.';
        _connecting = false;
        isConnecting.value = false;
        return;
      }

      _dataChar = omronSvc.characteristics.firstWhereOrNull(
        (c) => c.uuid.toString().toLowerCase().contains(_omronDataCharShort),
      );
      _cmdChar = omronSvc.characteristics.firstWhereOrNull(
        (c) => c.uuid.toString().toLowerCase().contains(_omronCmdCharShort),
      );
      _statusChar = omronSvc.characteristics.firstWhereOrNull(
        (c) => c.uuid.toString().toLowerCase().contains(_omronStatusCharShort),
      );

      if (_dataChar == null) {
        statusStr.value = 'Data characteristic not found.';
        _connecting = false;
        isConnecting.value = false;
        return;
      }

      bool subscribed = false;
      for (int attempt = 0; attempt < 4 && !subscribed; attempt++) {
        try {
          await _dataChar!.setNotifyValue(true);
          subscribed = true;
        } catch (e) {
          print('BpDevice setNotify attempt ${attempt + 1} failed: $e');
          if (attempt < 3) {
            statusStr.value = 'Pairing in progress, please wait...';
            await Future.delayed(const Duration(seconds: 5));
          }
        }
      }
      if (!subscribed) {
        statusStr.value = 'Connection failed. Tap TRANSFER to retry.';
        isConnected.value = false;
        _connecting = false;
        isConnecting.value = false;
        return;
      }

      _notifySub = _dataChar!.onValueReceived.listen(_onMeasurementData);

      if (_statusChar != null) {
        try {
          await _statusChar!.setNotifyValue(true);
          _notifySub2 = _statusChar!.onValueReceived.listen(_onStatusData);
        } catch (_) {}
      }

      isConnected.value = true;
      await _persistDevice(device.remoteId.toString(), device.platformName);
      statusStr.value = 'Connected. Requesting measurement...';
      _connecting = false;
      isConnecting.value = false;
    } catch (e) {
      print('BpDevice connect exception: $e');
      try { await device.disconnect(); } catch (_) {}
      statusStr.value = 'Connection failed. Tap TRANSFER to retry.';
      isConnected.value = false;
      _connecting = false;
      isConnecting.value = false;
    }
  }

  // ── Commands ─────────────────────────────────────────────────────────────

  Future<void> _writeCmd(List<int> cmd) async {
    try {
      if (_cmdChar != null) {
        await _cmdChar!.write(cmd, withoutResponse: false);
      } else {
        await _dataChar!.write(cmd, withoutResponse: false);
      }
      print('BpDevice CMD: $cmd');
    } catch (e) {
      print('BpDevice CMD error: $e');
    }
  }

  List<int> _sessionStartCmd(int userSlot) => [0x14, userSlot, 0x01];

  Future<void> _sendClockSync() async {
    final now = DateTime.now();
    final year = now.year;
    final cmd = [
      0x16,
      (year >> 8) & 0xFF,
      year & 0xFF,
      now.month,
      now.day,
      now.hour,
      now.minute,
      now.second,
    ];
    print('BpDevice CLOCK SYNC: $cmd');
    statusStr.value = 'Syncing device clock...';
    await _writeCmd(cmd);
  }

  // ── Status handler (49123040) ────────────────────────────────────────────

  void _onStatusData(List<int> data) {
    print('BpDevice STATUS [${data.length}]: ${data.map((b) => b.toRadixString(16).padLeft(2, '0')).join(' ')}');
    if (data.isEmpty || data.every((b) => b == 0)) return;

    if (data[0] == 0x08) {
      if (!_clockSynced) {
        _clockSynced = true;
        _sendClockSync();
      } else if (!_recordsRequested) {
        _recordsRequested = true;
        print('BpDevice: Requesting records for user slot $_currentUserSlot...');
        statusStr.value = 'Fetching measurement from device...';
        _writeCmd(_cmdGetRecords);
        // Also try direct read of DATA char — some Omron models store last
        // measurement as the readable value of b305b680 regardless of transfer flag
        Future.delayed(const Duration(milliseconds: 500), _tryDirectRead);
      } else if (!_triedSlot1 && _lastSystolic == null) {
        _triedSlot1 = true;
        _clockSynced = false;
        _recordsRequested = false;
        _currentUserSlot = 1;
        print('BpDevice: No data on slot 0 — retrying with user slot 1...');
        statusStr.value = 'Trying alternate user slot...';
        _writeCmd(_sessionStartCmd(1));
      }
    } else {
      print('BpDevice STATUS non-ACK, trying as measurement...');
      _onMeasurementData(data);
    }
  }

  // Direct GATT READ of DATA characteristic (b305b680 has read=true).
  // Omron device may store the last measurement as the readable value,
  // which is always accessible even after native app does notify-based transfer.
  Future<void> _tryDirectRead() async {
    if (_lastSystolic != null) return;

    // Read DATA char directly — b305b680 has read=true.
    // May hold last measurement value independently of notification transfer.
    if (_dataChar != null) {
      try {
        print('BpDevice: Direct READ of DATA char (b305b680)...');
        final value = await _dataChar!.read();
        print('BpDevice DATA (read) [${value.length}]: '
            '${value.map((b) => b.toRadixString(16).padLeft(2, '0')).join(' ')}');
        if (value.isNotEmpty) _onMeasurementData(value);
      } catch (e) {
        print('BpDevice DATA direct read error: $e');
      }
    }

    // Read STATUS char directly — 49123040 has read=true.
    if (_statusChar != null && _lastSystolic == null) {
      try {
        print('BpDevice: Direct READ of STATUS char (49123040)...');
        final value = await _statusChar!.read();
        print('BpDevice STATUS (read) [${value.length}]: '
            '${value.map((b) => b.toRadixString(16).padLeft(2, '0')).join(' ')}');
        // STATUS char normally sends 0x08 ACK — if it's something else, try as BP
        if (value.isNotEmpty && value[0] != 0x08) _onMeasurementData(value);
      } catch (e) {
        print('BpDevice STATUS direct read error: $e');
      }
    }
  }

  // ── Measurement data handler (b305b680) ──────────────────────────────────

  void _onMeasurementData(List<int> data) {
    print('BpDevice DATA [${data.length}]: ${data.map((b) => b.toRadixString(16).padLeft(2, '0')).join(' ')}');
    if (data.isEmpty || data.every((b) => b == 0)) return;
    if (data[0] == 0x08) return;

    final candidates = <String, _Candidate>{};
    if (data.length >= 5) {
      candidates['BE[1,2][3,4]'] = _Candidate((data[1] << 8) | data[2], (data[3] << 8) | data[4]);
      candidates['LE[1,2][3,4]'] = _Candidate((data[2] << 8) | data[1], (data[4] << 8) | data[3]);
    }
    if (data.length >= 7) {
      candidates['BE[3,4][5,6]'] = _Candidate((data[3] << 8) | data[4], (data[5] << 8) | data[6]);
    }
    if (data.length >= 3) {
      candidates['byte[1][2]'] = _Candidate(data[1], data[2]);
    }
    if (data.length >= 8) {
      candidates['byte[6][7]'] = _Candidate(data[6], data[7]);
    }

    for (final entry in candidates.entries) {
      print('BpDevice ${entry.key}: sys=${entry.value.sys} dia=${entry.value.dia}');
      if (_isValidBp(entry.value.sys, entry.value.dia)) {
        _applyReading(entry.value.sys, entry.value.dia);
        return;
      }
    }
    print('BpDevice: no valid BP in packet');
  }

  bool _isValidBp(int sys, int dia) =>
      sys >= 70 && sys <= 260 && dia >= 40 && dia <= 160 && sys > dia;

  void _applyReading(int sys, int dia) {
    _lastSystolic = sys;
    _lastDiastolic = dia;
    lastReadingStr.value = 'Systolic: $sys mmHg  |  Diastolic: $dia mmHg';
    statusStr.value = 'New reading detected! Tap TRANSFER to apply.';
    _persistReading(sys, dia);
    print('BpDevice APPLIED: sys=$sys dia=$dia');
  }

  Map<String, int>? getLastReading() {
    if (_lastSystolic == null || _lastDiastolic == null) return null;
    return {'systolic': _lastSystolic!, 'diastolic': _lastDiastolic!};
  }

  // ── TRANSFER ─────────────────────────────────────────────────────────────

  Future<void> requestData() async {
    isWaitingForReading.value = true;
    _stopProactiveScan();

    // Return immediately if proactive scan already caught a reading this session
    if (_lastSystolic != null && _lastDiastolic != null) {
      statusStr.value = 'Reading ready — tap TRANSFER to apply.';
      isWaitingForReading.value = false;
      return;
    }

    // ── 1. Omron SDK via native MethodChannel ────────────────────────────
    // Uses the same authenticated protocol as the native Omron app, so it can
    // read records that raw BLE cannot (device only delivers data to registered clients).
    statusStr.value = 'Connecting to Omron device via SDK...';
    try {
      final result = await OmronBpChannel.transfer(
        localName: savedDeviceName.value,
        uuid: savedDeviceMac.value,
      );
      _applyReading(result['systolic']!, result['diastolic']!);
      isWaitingForReading.value = false;
      return;
    } on PlatformException catch (e) {
      if (e.code == 'NO_BP_RECORDS') {
        // SDK connected fine but device has 0 unread records.
        // Take a measurement on the device first, then transfer.
        statusStr.value =
            'No new readings on device.\n'
            'Take a measurement on the Omron device first, '
            'then tap TRANSFER.';
        isWaitingForReading.value = false;
        _startProactiveScan();
        return;
      }
      // Other SDK errors (timeout, BT off, not bonded etc.) — fall through to GATT fallback
      print('BpDevice SDK error [${e.code}]: ${e.message}');
    } catch (e) {
      print('BpDevice SDK exception: $e');
    }

    // ── 2. GATT fallback (raw BLE, kept as safety net) ───────────────────
    statusStr.value = 'SDK unavailable — connecting via GATT...';
    if (!isConnected.value) {
      await autoConnect();
    }

    if (isConnected.value) {
      _clockSynced = false;
      _recordsRequested = false;
      _currentUserSlot = 0;
      _triedSlot1 = false;
      statusStr.value = 'Requesting measurement from device...';
      await _writeCmd(_sessionStartCmd(0));
      for (int i = 0; i < 50 && _lastSystolic == null; i++) {
        await Future.delayed(const Duration(milliseconds: 500));
      }
      if (_lastSystolic == null) await _tryDirectRead();
      if (_lastSystolic == null && _dataChar != null) {
        await _writeCmd([0x15, 0x00, 0x00]);
        for (int i = 0; i < 20 && _lastSystolic == null; i++) {
          await Future.delayed(const Duration(milliseconds: 500));
        }
        if (_lastSystolic == null) await _tryDirectRead();
      }
    }

    isWaitingForReading.value = false;

    if (_lastSystolic == null) {
      statusStr.value =
          'No reading found.\n'
          'Take a measurement on the Omron device, '
          'then tap TRANSFER.';
      _startProactiveScan();
    }
  }

  // ── Parse BP from BLE advertising manufacturer data ───────────────────────
  // Omron HEM-7140T1-AP format (company 0x020e, 5 bytes after company ID):
  //   byte[0] = status: 0x01 = all records already transferred by another app
  //                     other values = fresh unread reading available
  //   byte[1] = total stored record count (e.g. 0x08 = 8 records)
  //   byte[2] = pulse rate (bpm), e.g. 0x4d = 77 bpm
  //   byte[3] = unknown / flags
  //   byte[4] = unknown
  // BP values are NOT encoded in advertising for this model — only via GATT.
  // This parser is kept as a best-effort fallback in case a future observation
  // reveals a different advertising state with actual BP bytes.

  List<int>? _parseBpFromAdvData(Map<int, List<int>> manufacturerData) {
    for (final entry in manufacturerData.entries) {
      final bytes = entry.value;
      print('BpDevice ADV parse [${bytes.length}B]: '
          '${bytes.map((b) => b.toRadixString(16).padLeft(2, '0')).join(' ')}');

      if (bytes.isNotEmpty) {
        final status = bytes[0];
        final recordCount = bytes.length > 1 ? bytes[1] : 0;
        print('BpDevice ADV status=0x${status.toRadixString(16)} '
            'records=$recordCount');

        // 0x01 = all records already transferred (native app consumed them).
        // Any other value could mean fresh unread data — log prominently.
        if (status != 0x01) {
          print('BpDevice ADV *** FRESH STATE detected (status=0x${status.toRadixString(16)}) ***');
        }
      }

      // Try every byte-pair combination as a best-effort; log all, return first valid
      final candidates = <String, List<int>>{};
      if (bytes.length >= 3) { candidates['byte[1][2]'] = [bytes[1], bytes[2]]; }
      if (bytes.length >= 5) {
        candidates['byte[2][4]'] = [bytes[2], bytes[4]];
        candidates['byte[3][4]'] = [bytes[3], bytes[4]];
        candidates['BE[1,2][3,4]'] = [(bytes[1] << 8) | bytes[2], (bytes[3] << 8) | bytes[4]];
        candidates['LE[1,2][3,4]'] = [(bytes[2] << 8) | bytes[1], (bytes[4] << 8) | bytes[3]];
      }
      if (bytes.length >= 7) {
        candidates['byte[5][6]'] = [bytes[5], bytes[6]];
        candidates['BE[3,4][5,6]'] = [(bytes[3] << 8) | bytes[4], (bytes[5] << 8) | bytes[6]];
      }
      if (bytes.length >= 9) { candidates['byte[7][8]'] = [bytes[7], bytes[8]]; }

      for (final c in candidates.entries) {
        print('BpDevice ADV ${c.key}: sys=${c.value[0]} dia=${c.value[1]}');
        if (_isValidBp(c.value[0], c.value[1])) {
          print('BpDevice ADV MATCHED ${c.key}: sys=${c.value[0]} dia=${c.value[1]}');
          return c.value;
        }
      }
    }
    return null;
  }

  // ── Forget device ────────────────────────────────────────────────────────

  Future<void> forgetDevice() async {
    _stopProactiveScan();
    _cancelSubs();
    try { _device?.disconnect(); } catch (_) {}
    _device = null;
    _cmdChar = null;
    _dataChar = null;
    _statusChar = null;
    isConnected.value = false;
    _lastSystolic = null;
    _lastDiastolic = null;
    lastReadingStr.value = '';
    connectedDeviceName.value = '';
    connectedDeviceMac.value = '';
    statusStr.value = 'Not connected';
    _clockSynced = false;
    _recordsRequested = false;
    _currentUserSlot = 0;
    _triedSlot1 = false;
    await _clearPersistedDevice();
  }

  // ── Disconnect (keeps saved device + cached reading) ─────────────────────

  void disconnect() {
    _cancelSubs();
    try { _device?.disconnect(); } catch (_) {}
    _device = null;
    _cmdChar = null;
    _dataChar = null;
    _statusChar = null;
    isConnected.value = false;
    connectedDeviceName.value = '';
    connectedDeviceMac.value = '';
    statusStr.value = savedDeviceName.value.isNotEmpty
        ? 'Listening for new reading from device...'
        : 'Not connected';
  }
}

class _Candidate {
  final int sys;
  final int dia;
  _Candidate(this.sys, this.dia);
}