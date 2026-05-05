// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/Json_Class/PostCampBeneficiaryListResponse/PostCampBeneficiaryListResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import '../../../../Modules/constants/fonts.dart';

class MedicineDeliveryRow extends StatelessWidget {
  final int index;
  final PostCampBeneficiaryOutput obj;
  final VoidCallback? onTap;

  const MedicineDeliveryRow({
    super.key,
    required this.index,
    required this.obj,
    this.onTap,
  });

  /// Native uses DeliveryStatusRemarkID as string: "0"=yellow, "1"=green,
  /// "2"=blue, "3"=red.  The Flutter model stores it as int? so we coerce.
  Color _statusIconColor() {
    // Safely coerce: API may return int or string
    final raw = obj.deliveryStatusRemarkId;
    final id = raw ?? -1;
    switch (id) {
      case 0:
        return const Color(0xFFFFCC00); // yellow
      case 1:
        return const Color(0xFF4CAF50); // green
      case 2:
        return const Color(0xFF2196F3); // blue
      case 3:
        return const Color(0xFFF44336); // red
      default:
        return Colors.grey;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 0, 0, 4),
      child: GestureDetector(
        onTap: onTap,
        child: Container(
          decoration: BoxDecoration(
            border: Border.all(color: Colors.grey, width: 0.5),
          ),
          child: IntrinsicHeight(
            child: Row(
              children: [
                // Sr. No
                _cell(
                  width: 36,
                  child: Text(
                    "${index + 1}",
                    textAlign: TextAlign.center,
                    style: _style(),
                  ),
                ),
                // Patient Name
                Expanded(
                  flex: 3,
                  child: _cell(
                    child: Text(
                      obj.patientName?.trim() ?? "",
                      textAlign: TextAlign.left,
                      style: _style(),
                    ),
                  ),
                ),
                // Delivery Challan ID
                _cell(
                  width: 56,
                  child: Text(
                    obj.deliveryChallanId ?? "",
                    textAlign: TextAlign.center,
                    maxLines: 2,
                    overflow: TextOverflow.ellipsis,
                    style: _style(),
                  ),
                ),


                // Status icon (fingerprint / delivery status)
                Container(
                  width: 40,
                  alignment: Alignment.center,
                  padding: const EdgeInsets.symmetric(vertical: 6),
                  child: Icon(
                    Icons.fingerprint,
                    color: _statusIconColor(),
                    size: 22,
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _cell({double? width, required Widget child}) {
    final inner = Container(
      padding: const EdgeInsets.symmetric(horizontal: 4, vertical: 6),
      decoration: const BoxDecoration(
        border: Border(right: BorderSide(color: Colors.grey, width: 0.5)),
      ),
      alignment: Alignment.center,
      child: child,
    );
    if (width != null) return SizedBox(width: width, child: inner);
    return inner;
  }

  TextStyle _style() => TextStyle(
    color: uploadBillTitleColor,
    fontFamily: FontConstants.interFonts,
    fontWeight: FontWeight.w500,
    fontSize: responsiveFont(11),
  );
}
