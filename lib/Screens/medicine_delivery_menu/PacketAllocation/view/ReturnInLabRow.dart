// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/Json_Class/PatientListReAllocationforMedicineDeliveryResponse/PatientListReAllocationforMedicineDeliveryResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import '../../../../Modules/constants/fonts.dart';

class ReturnInLabRow extends StatefulWidget {
  ReturnInLabRow({
    super.key,
    required this.index,
    required this.obj,
    this.onSelectionChanged,
  });

  int index;
  PatientListReAllocationforMedicineDeliveryOutput obj;
  VoidCallback? onSelectionChanged;
  @override
  State<ReturnInLabRow> createState() => _ReturnInLabRowState();
}

class _ReturnInLabRowState extends State<ReturnInLabRow> {
  Color checkBoxColor() {
    if (widget.obj.overallStatusID != null) {
      if (widget.obj.overallStatusID == 2) {
        return Color.fromRGBO(198, 255, 210, 1.0);
      } else if (widget.obj.overallStatusID == 3) {
        return Color.fromRGBO(173, 216, 230, 1.0);
      } else if (widget.obj.overallStatusID == 4) {
        return Color.fromRGBO(255, 213, 128, 1.0);
      } else if (widget.obj.overallStatusID == 5) {
        return Color.fromRGBO(121, 154, 235, 1.0);
      } else if (widget.obj.overallStatusID == 7) {
        return Color.fromRGBO(255, 198, 198, 1.0);
      }
    }
    return Colors.white;
  }

  @override
  Widget build(BuildContext context) {
    return IntrinsicHeight(
      child: Padding(
        padding: const EdgeInsets.fromLTRB(0, 0, 0, 8),
        child: Container(
          decoration: BoxDecoration(
            border: Border.all(color: Colors.grey, width: 0.5),
          ),
          child: Row(
            children: [
              Container(
                width: 48,
                decoration: BoxDecoration(
                  border: Border(
                    right: BorderSide(color: Colors.grey, width: 0.5),
                  ),
                ),
                child: Center(
                  child: Text(
                    "${widget.index + 1}",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: uploadBillTitleColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w500,
                      fontSize: responsiveFont(12),
                    ),
                  ),
                ),
              ),
              Container(
                width: 6,
                height: double.infinity,
                color: checkBoxColor(),
              ),
              Expanded(
                child: Container(
                  decoration: BoxDecoration(
                    border: Border(
                      right: BorderSide(color: Colors.grey, width: 0.5),
                    ),
                  ),
                  child: Align(
                    alignment: Alignment.centerLeft,
                    child: Text(
                      widget.obj.patientName?.trim() ?? "",
                      textAlign: TextAlign.left,
                      style: TextStyle(
                        color: uploadBillTitleColor,
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w500,
                        fontSize: responsiveFont(12),
                      ),
                    ),
                  ),
                ),
              ),
              Container(
                width: 110,
                decoration: BoxDecoration(
                  border: Border(
                    right: BorderSide(color: Colors.grey, width: 0.5),
                  ),
                ),
                child: Center(
                  child: Text(
                    widget.obj.packetNumber ?? "",
                    textAlign: TextAlign.center,
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(
                      color: uploadBillTitleColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w500,
                      fontSize: responsiveFont(12),
                    ),
                  ),
                ),
              ),
              Container(
                width: 60,
                decoration: BoxDecoration(
                  border: Border(
                    right: BorderSide(color: Colors.grey, width: 0.5),
                  ),
                ),
                child: Center(
                  child: Text(
                    widget.obj.deliveryChallanID ?? "",
                    textAlign: TextAlign.center,
                    maxLines: 1,
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(
                      color: uploadBillTitleColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w500,
                      fontSize: responsiveFont(12),
                    ),
                  ),
                ),
              ),
              GestureDetector(
                onTap: () {
                  widget.obj.isSelected = !widget.obj.isSelected;
                  widget.onSelectionChanged?.call();
                  setState(() {});
                },
                child: Container(
                  padding: const EdgeInsets.all(8),
                  width: 30,
                  height: 00,
                  child: Image.asset(
                    widget.obj.isSelected == true
                        ? icCheckBoxSelected
                        : icUnCheckBoxSelected,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
