import 'package:flutter/material.dart';

import '../../../../Modules/constants/constants.dart';

class AcknowledgementPatientRow extends StatelessWidget {
  const AcknowledgementPatientRow({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 120,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.all(Radius.circular(10)),
        border: Border.all(color: borderDashboardColor),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.15),
            blurRadius: 10,
          ),
        ],
      ),
    );
  }
}
