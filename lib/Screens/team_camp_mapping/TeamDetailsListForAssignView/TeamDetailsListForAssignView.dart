// ignore_for_file: must_be_immutable, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/Json_Class/TeamsDoctorListResponse/TeamsDoctorListResponse.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';

class TeamDetailsListForAssignView extends StatefulWidget {
  TeamDetailsListForAssignView({
    super.key,
    required this.titleString,
    required this.list,
    required this.onTapTeam,
  });
  String titleString;
  List<TeamsDoctorListOutput> list;
  Function(List<TeamsDoctorListOutput>) onTapTeam;

  @override
  State<TeamDetailsListForAssignView> createState() =>
      _TeamDetailsListForAssignViewState();
}

class _TeamDetailsListForAssignViewState
    extends State<TeamDetailsListForAssignView> {
  final TextEditingController _searchController = TextEditingController();
  List<TeamsDoctorListOutput> _filteredList = [];

  @override
  void initState() {
    super.initState();
    _filteredList = List.from(widget.list);
    _searchController.addListener(_onSearch);
  }

  void _onSearch() {
    final q = _searchController.text.toLowerCase();
    setState(() {
      _filteredList = q.isEmpty
          ? List.from(widget.list)
          : widget.list
              .where(
                (d) =>
                    (d.resourceName ?? '').toLowerCase().contains(q) ||
                    (d.uSERID?.toString() ?? '').contains(q),
              )
              .toList();
    });
  }

  @override
  void dispose() {
    _searchController.removeListener(_onSearch);
    _searchController.dispose();
    super.dispose();
  }

  String _parseName(String? resourceName) {
    if (resourceName == null || resourceName.isEmpty) return '';
    final lastDash = resourceName.lastIndexOf('-');
    if (lastDash == -1) return resourceName.trim();
    return resourceName.substring(0, lastDash).trim();
  }

  String _initials(String name) {
    final parts = name.trim().split(RegExp(r'\s+'));
    if (parts.isEmpty || parts[0].isEmpty) return '?';
    if (parts.length == 1) return parts[0][0].toUpperCase();
    return '${parts[0][0]}${parts[parts.length - 1][0]}'.toUpperCase();
  }

  int get _selectedCount => widget.list.where((d) => d.selected).length;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        // Drag handle
        Container(
          margin: const EdgeInsets.only(top: 12, bottom: 8),
          width: 40,
          height: 4,
          decoration: BoxDecoration(
            color: Colors.grey.shade300,
            borderRadius: BorderRadius.circular(2),
          ),
        ),

        // Title + selected badge
        Padding(
          padding: EdgeInsets.symmetric(horizontal: 20.w, vertical: 4.h),
          child: Row(
            children: [
              Text(
                'Select ${widget.titleString}',
                style: TextStyle(
                  fontSize: 16.sp,
                  fontWeight: FontWeight.w600,
                  fontFamily: FontConstants.interFonts,
                  color: kBlackColor,
                ),
              ),
              const Spacer(),
              if (_selectedCount > 0)
                AnimatedContainer(
                  duration: const Duration(milliseconds: 200),
                  padding: const EdgeInsets.symmetric(
                    horizontal: 10,
                    vertical: 4,
                  ),
                  decoration: BoxDecoration(
                    color: kPrimaryColor,
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Text(
                    '$_selectedCount selected',
                    style: TextStyle(
                      fontSize: 11.sp,
                      color: Colors.white,
                      fontWeight: FontWeight.w600,
                      fontFamily: FontConstants.interFonts,
                    ),
                  ),
                ),
            ],
          ),
        ),

        // Search field
        Padding(
          padding: EdgeInsets.symmetric(horizontal: 16.w, vertical: 8.h),
          child: AppTextField(
            controller: _searchController,
            hint: 'Search by name or ID...',
            hintStyle: TextStyle(
              fontSize: 13.sp,
              color: Colors.grey.shade400,
              fontFamily: FontConstants.interFonts,
            ),
            fieldRadius: 12,
            prefixIcon: Padding(
              padding: const EdgeInsets.only(left: 4),
              child: Icon(Icons.search, color: Colors.grey.shade400, size: 20),
            ),
            onChange: (_) {},
          ),
        ),

        // List
        Expanded(
          child: _filteredList.isEmpty
              ? Center(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(
                        Icons.search_off_rounded,
                        size: 40,
                        color: Colors.grey.shade300,
                      ),
                      SizedBox(height: 8.h),
                      Text(
                        'No results found',
                        style: TextStyle(
                          color: Colors.grey.shade400,
                          fontSize: 13.sp,
                          fontFamily: FontConstants.interFonts,
                        ),
                      ),
                    ],
                  ),
                )
              : ListView.separated(
                  padding: EdgeInsets.symmetric(
                    horizontal: 16.w,
                    vertical: 4.h,
                  ),
                  itemCount: _filteredList.length,
                  separatorBuilder: (_, __) => SizedBox(height: 8.h),
                  itemBuilder: (context, index) {
                    final obj = _filteredList[index];
                    final name = _parseName(obj.resourceName);
                    final initials = _initials(name);
                    final isSelected = obj.selected;

                    return GestureDetector(
                      onTap: () {
                        obj.selected = !obj.selected;
                        setState(() {});
                      },
                      child: AnimatedContainer(
                        duration: const Duration(milliseconds: 180),
                        padding: EdgeInsets.symmetric(
                          horizontal: 14.w,
                          vertical: 12.h,
                        ),
                        decoration: BoxDecoration(
                          color: isSelected
                              ? kPrimaryColor.withValues(alpha: 0.07)
                              : Colors.white,
                          borderRadius: BorderRadius.circular(12),
                          border: Border.all(
                            color: isSelected
                                ? kPrimaryColor
                                : Colors.grey.shade200,
                            width: isSelected ? 1.5 : 1,
                          ),
                          boxShadow: isSelected
                              ? []
                              : [
                                  BoxShadow(
                                    color: Colors.black.withValues(alpha: 0.04),
                                    blurRadius: 6,
                                    offset: const Offset(0, 2),
                                  ),
                                ],
                        ),
                        child: Row(
                          children: [
                            // Gradient avatar with initials
                            Container(
                              width: 42.w,
                              height: 42.w,
                              decoration: BoxDecoration(
                                gradient: LinearGradient(
                                  colors: [
                                    kFirstAppBarcolor.withValues(alpha: 0.75),
                                    kFirstAppBarcolor,
                                  ],
                                  begin: Alignment.topLeft,
                                  end: Alignment.bottomRight,
                                ),
                                shape: BoxShape.circle,
                              ),
                              child: Center(
                                child: Text(
                                  initials,
                                  style: TextStyle(
                                    color: Colors.white,
                                    fontSize: 14.sp,
                                    fontWeight: FontWeight.w700,
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                ),
                              ),
                            ),
                            SizedBox(width: 12.w),

                            // Name + ID
                            Expanded(
                              child: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    name,
                                    style: TextStyle(
                                      fontSize: 13.sp,
                                      fontWeight: FontWeight.w600,
                                      fontFamily: FontConstants.interFonts,
                                      color: kBlackColor,
                                    ),
                                  ),
                                  SizedBox(height: 3.h),
                                  Row(
                                    children: [
                                      Icon(
                                        Icons.badge_outlined,
                                        size: 12,
                                        color: Colors.grey.shade500,
                                      ),
                                      SizedBox(width: 3.w),
                                      Text(
                                        'ID: ${obj.uSERID ?? ''}',
                                        style: TextStyle(
                                          fontSize: 11.sp,
                                          color: Colors.grey.shade500,
                                          fontFamily: FontConstants.interFonts,
                                        ),
                                      ),
                                    ],
                                  ),
                                ],
                              ),
                            ),

                            // Animated check icon
                            AnimatedSwitcher(
                              duration: const Duration(milliseconds: 200),
                              child: isSelected
                                  ? Icon(
                                      Icons.check_circle_rounded,
                                      color: kPrimaryColor,
                                      size: 24,
                                      key: const ValueKey(true),
                                    )
                                  : Icon(
                                      Icons.radio_button_unchecked_rounded,
                                      color: Colors.grey.shade300,
                                      size: 24,
                                      key: const ValueKey(false),
                                    ),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                ),
        ),

        // Bottom buttons — SafeArea handles gesture nav bar on tall phones
        SafeArea(
          top: false,
          child: Padding(
            padding: EdgeInsets.fromLTRB(16.w, 8.h, 16.w, 20.h),
            child: Row(
              children: [
                Expanded(
                  child: AppActiveButton(
                    buttontitle: 'Back',
                    isCancel: true,
                    onTap: () => Navigator.pop(context),
                  ),
                ),
                SizedBox(width: 12.w),
                Expanded(
                  child: AppActiveButton(
                    buttontitle: _selectedCount > 0
                        ? 'Confirm ($_selectedCount)'
                        : 'Confirm',
                    onTap: () {
                      final selectedList = widget.list
                          .where((obj) => obj.selected)
                          .toList();
                      Navigator.pop(context);
                      widget.onTapTeam(selectedList);
                    },
                  ),
                ),
              ],
            ),
          ),
        ),
      ],
    );
  }
}
