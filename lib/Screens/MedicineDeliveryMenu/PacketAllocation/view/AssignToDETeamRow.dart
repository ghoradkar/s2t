// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/Json_Class/PacketAcceptDataResponse/PacketAcceptDataResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import '../../../../Modules/constants/fonts.dart';

class AssignToDETeamRow extends StatefulWidget {
  AssignToDETeamRow({
    super.key,
    required this.index,
    required this.obj,
    this.onSelectionChanged,
  });

  int index;
  PacketAcceptDataOutput obj;
  VoidCallback? onSelectionChanged;

  @override
  State<AssignToDETeamRow> createState() => _AssignToDETeamRowState();
}

class _AssignToDETeamRowState extends State<AssignToDETeamRow> {
  Color checkBoxColor() {
    return Colors.white;
  }

  @override
  Widget build(BuildContext context) {
    return IntrinsicHeight(
      child: Padding(
        padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
        child: Container(
          decoration: BoxDecoration(
            border: Border(
              left: BorderSide(color: Colors.grey, width: 0.5),
              bottom: BorderSide(color: Colors.grey, width: 0.5),
              right: BorderSide(color: Colors.grey, width: 0.5),
            ),
          ),
          child: Row(
            children: [
              Container(
                width: 40,
                decoration: BoxDecoration(
                  border: Border(
                    right: BorderSide(color: Colors.grey, width: 0.5),
                  ),
                ),
                child: Center(
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(4, 2, 4, 2),
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
              ),
              Expanded(
                child: Container(
                  decoration: BoxDecoration(
                    border: Border(
                      right: BorderSide(color: Colors.grey, width: 0.5),
                    ),
                  ),
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(4, 2, 4, 2),
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
              ),
              Container(
                width: 110,
                decoration: BoxDecoration(
                  border: Border(
                    right: BorderSide(color: Colors.grey, width: 0.5),
                  ),
                ),
                child: Center(
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(4, 2, 4, 2),
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
              ),
              Container(
                width: 60,
                decoration: BoxDecoration(
                  border: Border(
                    right: BorderSide(color: Colors.grey, width: 0.5),
                  ),
                ),
                child: Center(
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(4, 2, 4, 2),
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
              ),
              GestureDetector(
                onTap: () {
                  widget.obj.isSelected = !widget.obj.isSelected;
                  widget.onSelectionChanged?.call();
                  setState(() {});
                },
                child: Container(
                  padding: EdgeInsets.all(4),
                  width: 30,
                  height: 30,
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
