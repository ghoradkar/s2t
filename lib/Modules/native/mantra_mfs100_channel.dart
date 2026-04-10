// ignore_for_file: file_names

import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

/// Result returned after a successful fingerprint capture.
class MantraCaptureResult {
  /// Base64-encoded PNG of the fingerprint image.
  final String base64;

  /// Absolute path to the saved PNG file in the app cache directory.
  /// Empty string if file save failed.
  final String filePath;

  /// Fingerprint quality score (higher = better).
  final int quality;

  /// NFIQ score (1 = best, 5 = worst).
  final int nfiq;

  const MantraCaptureResult({
    required this.base64,
    required this.filePath,
    required this.quality,
    required this.nfiq,
  });

  /// Returns [File] if [filePath] is non-empty and the file exists.
  File? get file {
    if (filePath.isEmpty) return null;
    final f = File(filePath);
    return f.existsSync() ? f : null;
  }
}

/// Device event received via the event stream.
class MantraDeviceEvent {
  /// One of: `attached`, `detached`, `host_check_failed`
  final String event;

  /// One of: `ready`, `init_failed`, `no_permission`, `error`  (only for `attached`)
  final String? status;

  final String? serial;
  final String? model;
  final String? error;

  const MantraDeviceEvent({
    required this.event,
    this.status,
    this.serial,
    this.model,
    this.error,
  });

  bool get isReady => event == 'attached' && status == 'ready';
  bool get isDetached => event == 'detached';

  @override
  String toString() =>
      'MantraDeviceEvent(event=$event, status=$status, serial=$serial, error=$error)';
}

/// Flutter-side wrapper for the Mantra MFS100 fingerprint scanner.
///
/// All calls are no-ops on non-Android platforms (or when the device is absent).
class MantraMfs100Channel {
  MantraMfs100Channel._();

  static const _method = MethodChannel('com.s2t.operational/mantra_mfs100');
  static const _events = EventChannel('com.s2t.operational/mantra_mfs100_events');

  static StreamSubscription<MantraDeviceEvent>? _eventSub;
  static final _deviceEventController =
      StreamController<MantraDeviceEvent>.broadcast();

  /// Broadcast stream of device attach/detach events.
  static Stream<MantraDeviceEvent> get deviceEvents =>
      _deviceEventController.stream;

  /// Call once (e.g. in main or in the controller's [onInit]) to start
  /// listening for device plug/unplug events.
  static void startListening() {
    if (!Platform.isAndroid) return;
    _eventSub ??= _events
        .receiveBroadcastStream()
        .cast<Map<Object?, Object?>>()
        .map(
          (raw) => MantraDeviceEvent(
            event: raw['event']?.toString() ?? '',
            status: raw['status']?.toString(),
            serial: raw['serial']?.toString(),
            model: raw['model']?.toString(),
            error: raw['error']?.toString(),
          ),
        )
        .listen(
          _deviceEventController.add,
          onError: (_) {},
        );
  }

  static void stopListening() {
    _eventSub?.cancel();
    _eventSub = null;
  }

  /// Returns `true` if the scanner is connected and initialised.
  static Future<bool> isDeviceConnected() async {
    if (!Platform.isAndroid) return false;
    try {
      return await _method.invokeMethod<bool>('isDeviceConnected') ?? false;
    } on PlatformException {
      return false;
    }
  }

  /// Starts fingerprint auto-capture.
  ///
  /// [timeoutMs] — max wait in milliseconds (default 10 s).
  ///
  /// Throws [MantraCaptureException] on failure.
  static Future<MantraCaptureResult> startCapture({int timeoutMs = 10000}) async {
    if (!Platform.isAndroid) {
      throw const MantraCaptureException(
        'NOT_ANDROID',
        'Fingerprint scanner only available on Android.',
      );
    }
    try {
      final raw = await _method.invokeMapMethod<String, dynamic>(
        'startCapture',
        {'timeoutMs': timeoutMs},
      );
      if (raw == null) {
        throw const MantraCaptureException('NULL_RESULT', 'No data returned.');
      }
      return MantraCaptureResult(
        base64: raw['base64'] as String? ?? '',
        filePath: raw['filePath'] as String? ?? '',
        quality: raw['quality'] as int? ?? 0,
        nfiq: raw['nfiq'] as int? ?? 5,
      );
    } on PlatformException catch (e) {
      throw MantraCaptureException(e.code, e.message ?? 'Unknown error');
    }
  }

  /// Stops any in-progress capture.
  static Future<void> stopCapture() async {
    if (!Platform.isAndroid) return;
    try {
      await _method.invokeMethod<void>('stopCapture');
    } on PlatformException {
      // swallow
    }
  }

  /// Returns device information (serial, make, model, sdkVersion).
  static Future<Map<String, String>> getDeviceInfo() async {
    if (!Platform.isAndroid) return {};
    try {
      final raw = await _method.invokeMapMethod<String, dynamic>('getDeviceInfo');
      return raw?.map((k, v) => MapEntry(k, v.toString())) ?? {};
    } on PlatformException {
      return {};
    }
  }
}

/// Thrown when fingerprint capture fails.
class MantraCaptureException implements Exception {
  final String code;
  final String message;

  const MantraCaptureException(this.code, this.message);

  @override
  String toString() => 'MantraCaptureException[$code]: $message';
}