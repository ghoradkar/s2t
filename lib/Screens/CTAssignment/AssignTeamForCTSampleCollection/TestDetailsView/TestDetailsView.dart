// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/Json_Class/ConfirmatoryTestsScreeningResponse/ConfirmatoryTestsScreeningResponse.dart';
import '../../../../Modules/constants/constants.dart';

class TestDetailsView extends StatelessWidget {
  TestDetailsView({super.key, required this.list});

  List<ConfirmatoryTestsScreeningOutput> list = [];

  static const _col1Flex = 3;
  static const _col2Flex = 2;

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(12),
        border: Border.all(color: const Color(0xFFE2DFFB), width: 1.2),
        boxShadow: [
          BoxShadow(
            offset: const Offset(0, 2),
            color: Colors.black.withValues(alpha: 0.07),
            blurRadius: 10,
          ),
        ],
      ),
      clipBehavior: Clip.antiAlias,
      child: Column(
        children: [
          // ── Title bar ──────────────────────────────────────────
          Container(
            width: double.infinity,
            padding: EdgeInsets.symmetric(vertical: 10.h),
            color: kPrimaryColor,
            child: Text(
              'Test Details',
              textAlign: TextAlign.center,
              style: TextStyle(
                color: Colors.white,
                fontFamily: FontConstants.interFonts,
                fontWeight: FontWeight.w700,
                fontSize: 13.sp,
                letterSpacing: 0.3,
              ),
            ),
          ),

          // ── Column headers ────────────────────────────────────
          Container(
            color: const Color(0xFFEDEBFD),
            child: Row(
              children: [
                _headerCell('Test Name', flex: _col1Flex, align: TextAlign.start),
                _vDivider(height: 36.h, color: const Color(0xFFB8B2F0)),
                _headerCell('Sample Qty (ml)', flex: _col2Flex, align: TextAlign.center),
              ],
            ),
          ),

          // ── Data rows ─────────────────────────────────────────
          if (list.isEmpty)
            _emptyState('No test details available')
          else
            ListView.separated(
              itemCount: list.length,
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              separatorBuilder: (_, __) =>
                  Divider(height: 1, color: const Color(0xFFEEEEEE)),
              itemBuilder: (_, i) {
                final obj = list[i];
                final isEven = i % 2 == 0;
                return Container(
                  color: isEven ? Colors.white : const Color(0xFFFAF9FF),
                  child: Row(
                    children: [
                      _dataCell(
                        obj.serviceName ?? '—',
                        flex: _col1Flex,
                        align: TextAlign.start,
                      ),
                      _vDivider(color: const Color(0xFFEEEEEE)),
                      _dataCell(
                        obj.sampleQuantityMl ?? '—',
                        flex: _col2Flex,
                        align: TextAlign.center,
                      ),
                    ],
                  ),
                );
              },
            ),
        ],
      ),
    );
  }

  Widget _headerCell(String text, {required int flex, TextAlign align = TextAlign.start}) {
    return Expanded(
      flex: flex,
      child: Padding(
        padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 9.h),
        child: Text(
          text,
          textAlign: align,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w600,
            fontSize: 11.sp,
            color: kPrimaryColor,
          ),
        ),
      ),
    );
  }

  Widget _dataCell(String text, {required int flex, TextAlign align = TextAlign.start}) {
    return Expanded(
      flex: flex,
      child: Padding(
        padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 10.h),
        child: Text(
          text,
          textAlign: align,
          style: TextStyle(
            fontFamily: FontConstants.interFonts,
            fontWeight: FontWeight.w400,
            fontSize: 12.sp,
            color: kBlackColor,
          ),
        ),
      ),
    );
  }

  Widget _vDivider({double? height, Color color = const Color(0xFFEEEEEE)}) {
    return Container(
      width: 1,
      height: height,
      color: color,
    );
  }

  Widget _emptyState(String message) {
    return Padding(
      padding: EdgeInsets.symmetric(vertical: 18.h),
      child: Text(
        message,
        textAlign: TextAlign.center,
        style: TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 12.sp,
          color: Colors.grey.shade400,
        ),
      ),
    );
  }
}
