// ignore_for_file: file_names
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../Model/HomeLabPendingCountTableModel.dart';



class PendingCountDataRow extends StatelessWidget {
  final PendingCountItem item;
  final int index;

  const PendingCountDataRow({
    super.key,
    required this.item,
    required this.index,
  });

  @override
  Widget build(BuildContext context) {
    final isEven = index % 2 == 0;
    final bgColor = isEven ? Colors.white : const Color(0xFFF5F5FF);

    return Container(
      margin: EdgeInsets.symmetric(horizontal: 8.w, vertical: 1.h),
      decoration: BoxDecoration(
        color: bgColor,
        borderRadius: BorderRadius.circular(4.r),
        border: Border.all(color: Colors.grey.withOpacity(0.15), width: 0.5),
      ),
      child: Row(
        children: [
          _buildDataCell(
            item.divName,
            flex: 2,
            isText: true,
            textAlign: TextAlign.left,
          ),
          _buildDataCell(item.hubLabProcessingDelayed.toString(), flex: 2),
          _buildDataCell(item.homeLabProcessingDelayed.toString(), flex: 2),
          _buildDataCell(item.doctorScreeningDelayed.toString(), flex: 2),
        ],
      ),
    );
  }

  Widget _buildDataCell(
    String value, {
    int flex = 1,
    bool isText = false,
    TextAlign textAlign = TextAlign.center,
  }) {
    return Expanded(
      flex: flex,
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 13.h, horizontal: 6.w),
        child: Text(
          value,
          textAlign: textAlign,
          maxLines: 3,
          overflow: TextOverflow.ellipsis,
          style: TextStyle(
            color: const Color(0xFF2D2D2D),
            fontSize: 12.sp,
            fontWeight: isText ? FontWeight.w500 : FontWeight.w400,
            height: 1.2,
          ),
        ),
      ),
    );
  }
}

// Total Row Widget
class TotalRow extends StatelessWidget {
  final List<PendingCountItem> list;

  const TotalRow({super.key, required this.list});

  @override
  Widget build(BuildContext context) {
    // Calculate totals
    int totalPatientCount = 0;
    int totalAbnormalPatients = 0;
    int totalReferralWarranted = 0;

    for (var item in list) {
      totalPatientCount += item.hubLabProcessingDelayed;
      totalAbnormalPatients += item.homeLabProcessingDelayed;
      totalReferralWarranted += item.doctorScreeningDelayed;
    }

    return Container(
      margin: EdgeInsets.symmetric(horizontal: 8.w),
      decoration: BoxDecoration(
        color: const Color(0xFFF5F5FF),
        borderRadius: BorderRadius.circular(4.r),
      ),
      child: Row(
        children: [
          _buildTotalCell('Total', flex: 2, isText: true),
          _buildTotalCell(totalPatientCount.toString(), flex: 2),
          _buildTotalCell(totalAbnormalPatients.toString(), flex: 2),
          _buildTotalCell(totalReferralWarranted.toString(), flex: 2),
        ],
      ),
    );
  }

  Widget _buildTotalCell(String value, {int flex = 1, bool isText = false}) {
    return Expanded(
      flex: flex,
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 13.h, horizontal: 6.w),
        child: Text(
          value,
          textAlign: isText ? TextAlign.left : TextAlign.center,
          maxLines: 3,
          overflow: TextOverflow.ellipsis,
          style: TextStyle(
            color: const Color(0xFF2D2D2D),
            fontSize: 12.sp,
            fontWeight: FontWeight.w700, // Bold for total row
            height: 1.2,
          ),
        ),
      ),
    );
  }
}
