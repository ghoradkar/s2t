import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/AppTextField.dart';

typedef SelectionItemBuilder<T> =
    Widget Function(BuildContext context, T item, bool isSelected);
typedef SelectionItemBuilderWithIndex<T> =
    Widget Function(
      BuildContext context,
      T item,
      bool isSelected,
      int index,
      int itemCount,
    );

class SelectionBottomSheet<T, V> extends StatefulWidget {
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
    this.showSearch = false,
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
  final bool showSearch;

  @override
  State<SelectionBottomSheet<T, V>> createState() =>
      _SelectionBottomSheetState<T, V>();
}

class _SelectionBottomSheetState<T, V>
    extends State<SelectionBottomSheet<T, V>> {
  final _searchController = TextEditingController();
  List<T> _filtered = [];

  @override
  void initState() {
    super.initState();
    _filtered = widget.items;
    _searchController.addListener(_onSearchChanged);
  }

  void _onSearchChanged() {
    final query = _searchController.text.toLowerCase();
    setState(() {
      _filtered =
          query.isEmpty
              ? widget.items
              : widget.items
                  .where(
                    (item) =>
                        widget.labelFor(item).toLowerCase().contains(query),
                  )
                  .toList();
    });
  }

  @override
  void dispose() {
    _searchController.removeListener(_onSearchChanged);
    _searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final resolvedTitleStyle =
        widget.titleTextStyle ??
        TextStyle(fontSize: 14.sp, fontFamily: FontConstants.interFonts);
    final resolvedItemTextStyle =
        widget.itemTextStyle ??
        TextStyle(
          fontFamily: FontConstants.interFonts,
          fontSize: 13.sp,
          fontWeight: FontWeight.normal,
          color: kBlackColor,
        );
    final resolvedSelectedTextStyle =
        widget.selectedItemTextStyle ?? resolvedItemTextStyle;
    final resolvedSelectedBgColor =
        widget.selectedBackgroundColor ?? kPrimaryColor.withOpacity(0.1);

    final displayItems = widget.showSearch ? _filtered : widget.items;

    return Padding(
      padding: widget.padding,
      child: SizedBox(
        width: SizeConfig.screenWidth,
        height: widget.height,
        child: Column(
          children: [
            Text(
              widget.title,
              textAlign: TextAlign.center,
              style: resolvedTitleStyle,
            ),
            SizedBox(height: widget.titleBottomSpacing ?? 12.h),
            if (widget.showSearch) ...[
              AppTextField(
                controller: _searchController,
                hint: 'Search...',
                prefixIcon: const Padding(
                  padding: EdgeInsets.only(left: 4),
                  child: Icon(Icons.search, color: kPrimaryColor, size: 20),
                ),
                onChange: (_) {},
              ),
              SizedBox(height: 8.h),
            ],
            Expanded(
              child: ListView.builder(
                shrinkWrap: true,
                itemCount: displayItems.length,
                itemBuilder: (context, index) {
                  final item = displayItems[index];
                  final itemValue = widget.valueFor(item);
                  final isSelected = widget.selectedValue == itemValue;

                  final child =
                      widget.itemBuilderWithIndex != null
                          ? widget.itemBuilderWithIndex!(
                            context,
                            item,
                            isSelected,
                            index,
                            displayItems.length,
                          )
                          : widget.itemBuilder != null
                          ? widget.itemBuilder!(context, item, isSelected)
                          : Container(
                            padding: widget.itemContainerPadding,
                            decoration: BoxDecoration(
                              color:
                                  isSelected
                                      ? resolvedSelectedBgColor
                                      : Colors.transparent,
                              borderRadius: BorderRadius.circular(8),
                            ),
                            child: Row(
                              children: [
                                if (widget.showRadio)
                                  IgnorePointer(
                                    child: Transform.scale(
                                      scale: widget.radioScale,
                                      child: Radio<V>(
                                        value: itemValue,
                                        groupValue: widget.selectedValue,
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
                                    widget.labelFor(item),
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
                      widget.useInkWell
                          ? InkWell(
                            onTap: () async {
                              await widget.onItemTap(item);
                            },
                            child: child,
                          )
                          : GestureDetector(
                            onTap: () async {
                              await widget.onItemTap(item);
                            },
                            child: child,
                          );

                  return Padding(padding: widget.itemPadding, child: tapChild);
                },
              ),
            ),
          ],
        ),
      ),
    );
  }
}
