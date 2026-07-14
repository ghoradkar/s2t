// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../../../constants/fonts.dart';
import '../model/confirmatory_tests_screening_tube_response.dart';
import '../../../../constants/constants.dart';

class TubeDetailsWidget extends StatefulWidget {
  TubeDetailsWidget({
    super.key,
    required this.list,
    this.countControllers,
  });

  List<ConfirmatoryTestsScreeningTubeOutput> list = [];
  final List<TextEditingController>? countControllers;

  @override
  State<TubeDetailsWidget> createState() => _TubeDetailsWidgetState();
}

class _TubeDetailsWidgetState extends State<TubeDetailsWidget> {
  static const _col1Flex = 3;
  static const _col2Flex = 2;

  Color _parseTubeColor(String? name) {
    switch ((name ?? '').toLowerCase().trim()) {
      case 'red':      return const Color(0xFFE53935);
      case 'yellow':   return const Color(0xFFFBC02D);
      case 'purple':   return const Color(0xFF7B1FA2);
      case 'lavender': return const Color(0xFFB39DDB);
      case 'grey':
      case 'gray':     return const Color(0xFF9E9E9E);
      case 'blue':     return const Color(0xFF1976D2);
      case 'green':    return const Color(0xFF388E3C);
      case 'orange':   return const Color(0xFFF57C00);
      case 'pink':     return const Color(0xFFE91E63);
      case 'white':    return Colors.white;
      default:         return kPrimaryColor;
    }
  }

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
              'Tube Details',
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
                Expanded(
                  flex: _col1Flex,
                  child: Padding(
                    padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 9.h),
                    child: Text(
                      'Tube Name',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w600,
                        fontSize: 11.sp,
                        color: kPrimaryColor,
                      ),
                    ),
                  ),
                ),
                Container(width: 1, height: 36.h, color: const Color(0xFFB8B2F0)),
                Expanded(
                  flex: _col2Flex,
                  child: Padding(
                    padding: EdgeInsets.symmetric(horizontal: 12.w, vertical: 9.h),
                    child: Text(
                      'Count',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontWeight: FontWeight.w600,
                        fontSize: 11.sp,
                        color: kPrimaryColor,
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),

          // ── Data rows ─────────────────────────────────────────
          if (widget.list.isEmpty)
            Padding(
              padding: EdgeInsets.symmetric(vertical: 18.h),
              child: Text(
                'No tube details available',
                textAlign: TextAlign.center,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 12.sp,
                  color: Colors.grey.shade400,
                ),
              ),
            )
          else
            ListView.separated(
              itemCount: widget.list.length,
              shrinkWrap: true,
              physics: const NeverScrollableScrollPhysics(),
              separatorBuilder: (_, __) =>
                  const Divider(height: 1, color: Color(0xFFEEEEEE)),
              itemBuilder: (_, i) {
                final obj = widget.list[i];
                final isEven = i % 2 == 0;
                final hasCtrl = widget.countControllers != null &&
                    i < widget.countControllers!.length;
                final tubeColor = _parseTubeColor(obj.tubColor);

                return Container(
                  color: isEven ? Colors.white : const Color(0xFFFAF9FF),
                  child: Row(
                    children: [
                      // Tube name + colour dot
                      Expanded(
                        flex: _col1Flex,
                        child: Padding(
                          padding: EdgeInsets.symmetric(
                            horizontal: 12.w,
                            vertical: 10.h,
                          ),
                          child: Row(
                            children: [
                              Container(
                                width: 10.w,
                                height: 10.w,
                                decoration: BoxDecoration(
                                  color: tubeColor,
                                  shape: BoxShape.circle,
                                  border: Border.all(
                                    color: Colors.grey.shade300,
                                    width: 0.8,
                                  ),
                                ),
                              ),
                              SizedBox(width: 8.w),
                              Expanded(
                                child: Text(
                                  obj.tubName ?? '—',
                                  style: TextStyle(
                                    fontFamily: FontConstants.interFonts,
                                    fontWeight: FontWeight.w400,
                                    fontSize: 12.sp,
                                    color: kBlackColor,
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                      Container(
                        width: 1,
                        color: const Color(0xFFEEEEEE),
                      ),
                      // Count
                      Expanded(
                        flex: _col2Flex,
                        child: Center(
                          child: hasCtrl
                              ? SizedBox(
                                  width: 56.w,
                                  child: TextField(
                                    controller: widget.countControllers![i],
                                    keyboardType: TextInputType.number,
                                    textAlign: TextAlign.center,
                                    inputFormatters: [
                                      FilteringTextInputFormatter.digitsOnly,
                                    ],
                                    decoration: InputDecoration(
                                      isDense: true,
                                      contentPadding: EdgeInsets.symmetric(
                                        horizontal: 6.w,
                                        vertical: 7.h,
                                      ),
                                      border: OutlineInputBorder(
                                        borderRadius: BorderRadius.circular(6),
                                        borderSide: BorderSide(
                                          color: kPrimaryColor.withValues(alpha: 0.35),
                                        ),
                                      ),
                                      enabledBorder: OutlineInputBorder(
                                        borderRadius: BorderRadius.circular(6),
                                        borderSide: BorderSide(
                                          color: kPrimaryColor.withValues(alpha: 0.25),
                                        ),
                                      ),
                                      focusedBorder: OutlineInputBorder(
                                        borderRadius: BorderRadius.circular(6),
                                        borderSide: BorderSide(color: kPrimaryColor),
                                      ),
                                    ),
                                    style: TextStyle(
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w600,
                                      fontSize: 13.sp,
                                      color: kPrimaryColor,
                                    ),
                                  ),
                                )
                              : Padding(
                                  padding: EdgeInsets.symmetric(vertical: 10.h),
                                  child: Text(
                                    '${obj.tubCount ?? 0}',
                                    textAlign: TextAlign.center,
                                    style: TextStyle(
                                      fontFamily: FontConstants.interFonts,
                                      fontWeight: FontWeight.w600,
                                      fontSize: 13.sp,
                                      color: kPrimaryColor,
                                    ),
                                  ),
                                ),
                        ),
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
}
