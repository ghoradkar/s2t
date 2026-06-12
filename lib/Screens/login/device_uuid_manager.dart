// File: Modules/utilities/DeviceUUIDManager.dart

import 'package:flutter_udid/flutter_udid.dart';
import 'package:shared_preferences/shared_preferences.dart';

class DeviceUUIDManager {
  // Singleton pattern (like your APIManager)
  static final DeviceUUIDManager _singleton = DeviceUUIDManager._internal();

  factory DeviceUUIDManager() => _singleton;

  DeviceUUIDManager._internal();

  static const String _keyDeviceUUID = 'device_uuid_persistent';

  /// Get device UUID - creates new one ONLY if doesn't exist
  /// This UUID will persist across:
  /// - App restarts
  /// - User logout
  ///
  /// Will be cleared only on:
  /// - App uninstall
  /// - Manual call to clearDeviceUUID()
  Future<String> getDeviceUUID() async {
    try {
      // Prefer platform-provided UDID (ANDROID_ID on Android).
      final String udid = await FlutterUdid.consistentUdid;

      final prefs = await SharedPreferences.getInstance();

      // Try to read existing UUID
      String? existingUUID = prefs.getString(_keyDeviceUUID);

      if (existingUUID != null && existingUUID.isNotEmpty) {
        print('Using existing Device UUID: $existingUUID');
        return existingUUID;
      }

      // Store UDID if none exists
      await prefs.setString(_keyDeviceUUID, udid);

      print('Stored Device UUID: $udid');
      return udid;
    } catch (e) {
      print('Error getting Device UUID: $e');
      // Fallback: return a non-persisted value (avoid crashing)
      return DateTime.now().microsecondsSinceEpoch.toString();
    }
  }

  /// Clear device UUID - Use ONLY if you want to reset device registration
  /// NOT recommended to call on logout
  Future<void> clearDeviceUUID() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.remove(_keyDeviceUUID);
      print('Device UUID cleared');
    } catch (e) {
      print('Error clearing Device UUID: $e');
    }
  }

  /// Check if device UUID exists
  Future<bool> hasDeviceUUID() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      String? uuid = prefs.getString(_keyDeviceUUID);
      return uuid != null && uuid.isNotEmpty;
    } catch (e) {
      return false;
    }
  }
}
