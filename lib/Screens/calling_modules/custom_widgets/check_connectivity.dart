import 'package:connectivity_plus/connectivity_plus.dart';

class CheckConnectivity {
  CheckConnectivity._();

  static final CheckConnectivity instance = CheckConnectivity._();

  static Future<bool> checkInternetAndLoadData() async {
    final List<ConnectivityResult> results = await Connectivity().checkConnectivity();
    final ConnectivityResult result =
    results.isNotEmpty ? results.first : ConnectivityResult.none;

    if (result == ConnectivityResult.mobile ||
        result == ConnectivityResult.wifi) {
      return true;
    } else {
      return false;
    }
  }

}
