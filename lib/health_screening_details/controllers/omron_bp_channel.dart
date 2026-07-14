import 'package:flutter/services.dart';

class OmronBpChannel {
  static const _channel = MethodChannel('com.s2t.operational/omron_bp');

  /// Transfers the latest unread BP reading from the Omron device.
  ///
  /// [localName] – BLE local name stored at pairing (e.g. BLESmart_0000047EFCCAD1758116)
  /// [uuid]      – MAC address stored at pairing (e.g. FC:CA:D1:75:81:16)
  ///
  /// Returns `{'systolic': int, 'diastolic': int}` or throws [PlatformException].
  static Future<Map<String, int>> transfer({
    required String localName,
    required String uuid,
  }) async {
    final result = await _channel.invokeMethod<Map>('transfer', {
      'localName': localName,
      'uuid': uuid,
    });
    return {
      'systolic': result!['systolic'] as int,
      'diastolic': result['diastolic'] as int,
    };
  }

  static Future<void> cancel() async {
    await _channel.invokeMethod<void>('cancel');
  }
}