import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';
import 'no_internet_widget.dart'; // import your NoInternetWidget

class NetworkWrapper extends StatelessWidget {
  final Widget child;

  const NetworkWrapper({super.key, required this.child});

  @override
  Widget build(BuildContext context) {
    return StreamBuilder<ConnectivityResult>(
      stream: Connectivity().onConnectivityChanged.map((event) => event.first),
      builder: (context, snapshot) {
        // Handle no connection
        if (snapshot.hasData &&
            snapshot.data != ConnectivityResult.mobile &&
            snapshot.data != ConnectivityResult.wifi) {
          return const NoInternetWidget();
        }

        // Default: show the actual view
        return child;
      },
    );
  }
}
