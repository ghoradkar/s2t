import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';

typedef SelectionItemBuilder<T> = Widget Function(
  BuildContext context,
  T item,
  bool isSelected,
);
typedef SelectionItemBuilderWithIndex<T> = Widget Function(
  BuildContext context,
  T item,
  bool isSelected,
  int index,
  int itemCount,
);

class SelectionBottomSheet<T, V> extends StatelessWidget {
  const SelectionBottomSheet({
    super.key,
    required this.title,
    required this.items,
    required this.valueFor,
    required this.labelFor,
    required this.onItemTap,
    required this.height,
    required this.padding,
    this.selectedValue,
    this.titleTextStyle,
    this.titleBottomSpacing,
    this.itemPadding = EdgeInsets.zero,
    this.itemContainerPadding = const EdgeInsets.symmetric(
      vertical: 8.0,
      horizontal: 4.0,
    ),
    this.selectedBackgroundColor,
    this.itemTextStyle,
    this.selectedItemTextStyle,
    this.showRadio = true,
    this.radioScale = 0.75,
    this.useInkWell = true,
    this.itemBuilder,
    this.itemBuilderWithIndex,
  });

  final String title;
  final List<T> items;
  final V? selectedValue;
  final V Function(T item) valueFor;
  final String Function(T item) labelFor;
  final FutureOr<void> Function(T item) onItemTap;
  final double height;
  final EdgeInsetsGeometry padding;
  final TextStyle? titleTextStyle;
  final double? titleBottomSpacing;
  final EdgeInsetsGeometry itemPadding;
  final EdgeInsetsGeometry itemContainerPadding;
  final Color? selectedBackgroundColor;
  final TextStyle? itemTextStyle;
  final TextStyle? selectedItemTextStyle;
  final bool showRadio;
  final double radioScale;
  final bool useInkWell;
  final SelectionItemBuilder<T>? itemBuilder;
  final SelectionItemBuilderWithIndex<T>? itemBuilderWithIndex;

  @override
  Widget build(BuildContext context) {
    final resolvedTitleStyle =
        titleTextStyle ??
        TextStyle(
          fontSize: 14.sp,
          fontFamily: FontConstants.interFonts,
        );
    final resolvedItemTextStyle =
        itemTextStyle ??
        TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 13.sp,
          fontWeight: FontWeight.normal,
          color: kBlackColor,
        );
    final resolvedSelectedTextStyle =
        selectedItemTextStyle ?? resolvedItemTextStyle;
    final resolvedSelectedBgColor =
        selectedBackgroundColor ?? kPrimaryColor.withOpacity(0.1);

    return Padding(
      padding: padding,
      child: SizedBox(
        width: SizeConfig.screenWidth,
        height: height,
        child: Column(
          children: [
            Text(title, textAlign: TextAlign.center, style: resolvedTitleStyle),
            SizedBox(height: titleBottomSpacing ?? 20.h),
            Expanded(
              child: ListView.builder(
                shrinkWrap: true,
                itemCount: items.length,
                itemBuilder: (context, index) {
                  final item = items[index];
                  final itemValue = valueFor(item);
                  final isSelected = selectedValue == itemValue;

                  final child =
                      itemBuilderWithIndex != null
                          ? itemBuilderWithIndex!(
                            context,
                            item,
                            isSelected,
                            index,
                            items.length,
                          )
                          : itemBuilder != null
                          ? itemBuilder!(context, item, isSelected)
                          : Container(
                            padding: itemContainerPadding,
                            decoration: BoxDecoration(
                              color:
                                  isSelected
                                      ? resolvedSelectedBgColor
                                      : Colors.transparent,
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Row(
                              children: [
                                if (showRadio)
                                  IgnorePointer(
                                    child: Transform.scale(
                                      scale: radioScale,
                                      child: Radio<V>(
                                        value: itemValue,
                                        groupValue: selectedValue,
                                        onChanged: (value) {},
                                        materialTapTargetSize:
                                            MaterialTapTargetSize.shrinkWrap,
                                        visualDensity: const VisualDensity(
                                          horizontal: -4,
                                          vertical: -4,
                                        ),
                                      ),
                                    ),
                                  ),
                                Expanded(
                                  child: Text(
                                    labelFor(item),
                                    style:
                                        isSelected
                                            ? resolvedSelectedTextStyle
                                            : resolvedItemTextStyle,
                                  ),
                                ),
                              ],
                            ),
                          );

                  final tapChild =
                      useInkWell
                          ? InkWell(
                            onTap: () async {
                              await onItemTap(item);
                            },
                            child: child,
                          )
                          : GestureDetector(
                            onTap: () async {
                              await onItemTap(item);
                            },
                            child: child,
                          );

                  return Padding(padding: itemPadding, child: tapChild);
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
