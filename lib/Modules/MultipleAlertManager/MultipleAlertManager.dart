// ignore_for_file: file_names

import 'package:flutter/material.dart';

class MultipleAlertManager {
  final List<String> messagesList = [];

  // Call this to start showing alerts
  void showAlertMessages(BuildContext context) {
    _showAlert(context);
  }

  void _showAlert(BuildContext context) {
    if (messagesList.isEmpty) return;

    String message = messagesList.first;

    showDialog(
      context: context,
      barrierDismissible: false, // Prevent dismissal by tapping outside
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text("Alert"),
          content: Text(message),
          actions: [
            TextButton(
              child: Text("Okay"),
              onPressed: () {
                Navigator.of(context).pop(); // Close current dialog
                messagesList.removeAt(0); // Remove current message
                _showAlert(context); // Show next message
              },
            ),
          ],
        );
      },
    );
  }
}
