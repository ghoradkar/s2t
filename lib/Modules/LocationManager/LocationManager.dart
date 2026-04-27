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

      final status = await ph.Permission.location.status;

      if (status.isGranted) {
        return LocationPermissionResult.granted;
      }

      if (status.isPermanentlyDenied) {
        return LocationPermissionResult.permanentlyDenied;
      }

      final result = await ph.Permission.location.request();

      if (result.isGranted) {
        return LocationPermissionResult.granted;
      } else if (result.isPermanentlyDenied) {
        return LocationPermissionResult.permanentlyDenied;
      } else {
        return LocationPermissionResult.denied;
      }
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
