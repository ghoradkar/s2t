import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';

class DashboardAdminCard extends StatelessWidget {
  final String title;
  final String? topRightCaption;
  final Widget? leading;
  final Map<String, String> metrics;
  final VoidCallback? onAction;
  final bool view;

  const DashboardAdminCard({
    super.key,
    required this.title,
    required this.metrics,
    this.leading,
    this.topRightCaption,
    this.onAction,
    required this.view,
  });

  @override
  Widget build(BuildContext context) {
    final entries = metrics.entries.toList();

    return InkWell(
      onTap: onAction,
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 10.h, horizontal: 10.w),
        decoration: BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.circular(16),
          boxShadow:  [
            BoxShadow(
              blurRadius: 4,
              spreadRadius: 2,
              color: kTextColor.withValues(alpha: 0.2),
            ),
          ],
        ),
        child: Padding(
          padding: const EdgeInsets.fromLTRB(8, 4, 8, 4),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Row(
                children: [
                  if (leading != null) leading!,
                  Expanded(
                    child: Padding(
                      padding: EdgeInsets.only(left: 12.w),
                      child: CommonText(
                        text: title,
                        fontSize: 14.sp,
                        fontWeight: FontWeight.w600,
                        textColor: kTextBlackColor,
                        textAlign: TextAlign.start,
                      ),
                    ),
                  ),
                  if (topRightCaption != null)
                    CommonText(
                      text: topRightCaption!,
                      fontSize: 12.sp,
                      fontWeight: FontWeight.normal,
                      textColor: kTextBlackColor,
                      textAlign: TextAlign.end,
                    ),
                ],
              ),

              const SizedBox(height: 10),

              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  for (int i = 0; i < entries.length; i++) ...[
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        // value
                        CommonText(
                          text: entries[i].value,
                          fontSize: 16.sp,
                          fontWeight: FontWeight.w600,
                          textColor: kTextBlackColor,
                          textAlign: TextAlign.start,
                        ),
                        // label
                        CommonText(
                          text: entries[i].key,
                          fontSize: 14.sp,
                          fontWeight: FontWeight.normal,
                          textColor: kTextBlackColor,
                          textAlign: TextAlign.start,
                        ),
                      ],
                    ),

                    if (i != entries.length - 1)
                      SizedBox(
                        height: 60,
                        child: VerticalDivider(
                          width: 1,
                          thickness: 1,
                          // keep your original styling helper
                          color: kTextBlackColor.withValues(alpha: 0.2),
                        ),
                      ),
                  ],

                  Visibility(
                    visible: view,
                    child: Image.asset(icViewIcon, height: 24.w),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
