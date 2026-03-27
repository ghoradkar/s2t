// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';

import '../models/month_wise_invoice_model.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/fonts.dart';
import '../../../Modules/utilities/SizeConfig.dart';

class InvoiceRow extends StatelessWidget {
  const InvoiceRow({
    super.key,
    required this.obj,
    required this.onMonthTap,
    required this.onActionTap,
    required this.isDoctor,
  });

  final MonthWiseInvoiceOutput obj;
  final VoidCallback onMonthTap;
  final VoidCallback onActionTap;
  final bool isDoctor;

  Widget invoiceStatusOrButton() {
    if (obj.invoiceStatus == "Pending") {
      return _badge(
        "Pending",
        textColor: const Color(0xFF7A5A00),
        bgColor: const Color(0xFFFFF2C2),
      );
    }
    if (obj.invoiceStatus == "Raise") {
      return Container(
        padding: const EdgeInsets.fromLTRB(10, 4, 8, 4),
        decoration: BoxDecoration(
          color: kPrimaryColor,
          borderRadius: BorderRadius.circular(12),
        ),
        child: Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              "Raise",
              style: TextStyle(
                color: kWhiteColor,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w600,
                fontSize: responsiveFont(10),
              ),
            ),
            const SizedBox(width: 6),
            Icon(Icons.arrow_forward, size: 14, color: kWhiteColor),
          ],
        ),
      );
    }
    if (obj.invoiceStatus == "VIEW") {
      final color =
          obj.invoiceApprovedStatus == 1
              ? invoiceApprovedStatus1
              : invoiceApprovedStatus2;
      final label = isDoctor ? "Raised" : "VIEW";
      return _badge(
        label,
        textColor: color,
        bgColor: kWhiteColor,
        outlined: true,
      );
    }

    return _badge(
      "Pending",
      textColor: kWhiteColor,
      bgColor: pendingInvoiceColor,
    );
  }

  Widget _badge(
    String text, {
    required Color textColor,
    required Color bgColor,
    bool outlined = false,
  }) {
    return Container(
      padding: const EdgeInsets.fromLTRB(10, 4, 10, 4),
      decoration: BoxDecoration(
        color: bgColor,
        borderRadius: BorderRadius.circular(12),
        border: outlined ? Border.all(color: kTextFieldBorder, width: 1) : null,
      ),
      child: Text(
        text,
        textAlign: TextAlign.center,
        style: TextStyle(
          color: textColor,
          fontFamily: FontConstants.interFonts,
          fontWeight: FontWeight.w500,
          fontSize: responsiveFont(10),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
      child: Container(
        decoration: BoxDecoration(
          color: kWhiteColor,
          border: Border(
            left: BorderSide(color: kTextFieldBorder, width: 1),
            right: BorderSide(color: kTextFieldBorder, width: 1),
            bottom: BorderSide(color: kTextFieldBorder, width: 1),
          ),
        ),
        height: 40,
        child: Row(
          children: [
            SizedBox(
              width: 70,
              child: GestureDetector(
                onTap: onMonthTap,
                child: Center(
                  child: Text(
                    obj.invoiceMonth ?? "",
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w500,
                      fontSize: responsiveFont(10),
                    ),
                  ),
                ),
              ),
            ),
            Container(
              decoration: const BoxDecoration(
                border: Border(
                  left: BorderSide(color: kTextFieldBorder, width: 1),
                ),
              ),
              width: 70,
              child: Center(
                child: Text(
                  obj.serviceDays.toString(),
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    color: kBlackColor,
                    fontFamily: FontConstants.interFonts,
                    fontWeight: FontWeight.w500,
                    fontSize: responsiveFont(10),
                  ),
                ),
              ),
            ),
            Expanded(
              child: Container(
                decoration: const BoxDecoration(
                  border: Border(
                    left: BorderSide(color: kTextFieldBorder, width: 1),
                  ),
                ),
                child: Center(
                  child: Text(
                    obj.totalIndividualBillable.toString(),
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: kBlackColor,
                      fontFamily: FontConstants.interFonts,
                      fontWeight: FontWeight.w500,
                      fontSize: responsiveFont(10),
                    ),
                  ),
                ),
              ),
            ),
            Container(
              decoration: const BoxDecoration(
                border: Border(
                  left: BorderSide(color: kTextFieldBorder, width: 1),
                ),
              ),
              width: 90,
              child: GestureDetector(
                onTap: onActionTap,
                child: Center(child: invoiceStatusOrButton()),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
