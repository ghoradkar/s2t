// ignore_for_file: avoid_print
import 'package:geolocator/geolocator.dart';
import 'package:permission_handler/permission_handler.dart' as ph;

enum LocationPermissionResult {
  granted,
  denied,
  permanentlyDenied,
  serviceDisabled,
}

class LocationManager {
  static Future<LocationPermissionResult> checkAndRequestLocation() async {
    try {
      final bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
      if (!serviceEnabled) {
        await Geolocator.openLocationSettings();
        return LocationPermissionResult.serviceDisabled;
      }

      // Use Geolocator's permission API — more reliable on iOS than permission_handler
      LocationPermission permission = await Geolocator.checkPermission();

      if (permission == LocationPermission.denied) {
        // Not yet asked or denied once — show the native iOS dialog
        permission = await Geolocator.requestPermission();
      }

      if (permission == LocationPermission.deniedForever) {
        return LocationPermissionResult.permanentlyDenied;
      }

      if (permission == LocationPermission.whileInUse ||
          permission == LocationPermission.always) {
        return LocationPermissionResult.granted;
      }

      return LocationPermissionResult.denied;
    } catch (e) {
      print('[LocationManager] error: $e');
      return LocationPermissionResult.denied;
    }
  }

  static Future<Position?> getCurrentLocation() async {
    try {
      final Position position = await Geolocator.getCurrentPosition(
        locationSettings: const LocationSettings(
          accuracy: LocationAccuracy.high,
        ),
      );
      print(
        '[LocationManager] lat=${position.latitude} lng=${position.longitude}',
      );
      return position;
    } catch (e) {
      print('[LocationManager] getCurrentLocation error: $e');
      return null;
    }
  }

  static Future<void> openAppSettings() => ph.openAppSettings();
}