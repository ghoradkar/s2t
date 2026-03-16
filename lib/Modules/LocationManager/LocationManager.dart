import 'package:geolocator/geolocator.dart';

class LocationManager {
  static Future<bool> checkAndRequestLocation() async {
    bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      // Request to enable location
      await Geolocator.openLocationSettings();
      return false; // wait for user to come back and recheck
    }

    LocationPermission permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        return false;
      }
    }

    if (permission == LocationPermission.deniedForever) {
      return false; // You can guide the user to settings
    }

    return true;
  }

  static Future<Position?> getCurrentLocation() async {
    bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      await Geolocator.openLocationSettings();
      return null;
    }

    LocationPermission permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) {
        print('Location permission denied');
        return null;
      }
    }

    if (permission == LocationPermission.deniedForever) {
      print('Location permission permanently denied');
      return null;
    }

    // ✅ Use platform-aware settings
    LocationSettings locationSettings = LocationSettings(
      accuracy: LocationAccuracy.high,
      distanceFilter: 10,
    );

    Position position = await Geolocator.getCurrentPosition(
      locationSettings: locationSettings,
    );

    print("Latitude: ${position.latitude}, Longitude: ${position.longitude}");
    return position;
  }
}
