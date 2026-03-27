import 'package:connectivity_plus/connectivity_plus.dart';
import 'package:flutter/material.dart';
import 'no_internet_widget.dart'; // import your NoInternetWidget

class NetworkWrapper extends StatelessWidget {
  final Widget child;
  final Function? onRetry;

  const NetworkWrapper({super.key, required this.child, this.onRetry});

  @override
  Widget build(BuildContext context) {
    return StreamBuilder<ConnectivityResult>(
      stream: Connectivity().onConnectivityChanged.map((event) => event.first),
      builder: (context, snapshot) {
        // Handle no connection
        if (snapshot.hasData &&
            snapshot.data != ConnectivityResult.mobile &&
            snapshot.data != ConnectivityResult.wifi) {
          return NoInternetWidget(
            onRetryPressed: () async {
              if (onRetry != null) {
                onRetry!();
              }
            },
          );
        }

        // Default: show the actual view
        return child;
      },
    );
  }
}
