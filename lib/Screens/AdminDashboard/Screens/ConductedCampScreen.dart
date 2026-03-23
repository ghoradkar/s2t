// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/AdminDashboard/Controller/AdminController.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/CampConductedResponse.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'package:s2toperational/Screens/CampDetailsScreen/CampDetailsScreen.dart';

class ConductedCampsScreen extends StatefulWidget {
  final bool isDistAndYearVisiable;
  final String title;
  final String totalString;

  const ConductedCampsScreen({
    super.key,
    required this.isDistAndYearVisiable,
    required this.title, required this.totalString,
  });

  @override
  State<ConductedCampsScreen> createState() => _ConductedCampsScreenState();
}

class _ConductedCampsScreenState extends State<ConductedCampsScreen> {
  final AdminController adminController = Get.put(AdminController());

  @override
  void initState() {
    adminController.checkInternet();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: widget.title,
        leadingIcon: iconBackArrow,
        onLeadingIconClick: Get.back,
      ),
      body: GetBuilder<AdminController>(
        builder: (controller) {
          final tableHelper = ConductedCampsTableHelper();
          final bool isLoading = controller.isConductedCampsLoading;
          List<CampRecord> rawRows = const [];
          bool hasRows = false;

          if (!isLoading) {
            rawRows =
                widget.title == "All Camp List"
                    ? (controller.campsConductedResponse?.output ?? []).where((
                      camp,
                    ) {
                      try {
                        final campDate = DateTime.parse(camp.campDate);
                        final today = DateTime.now();
                        return campDate.year == today.year &&
                            campDate.month == today.month &&
                            campDate.day == today.day;
                      } catch (_) {
                        return false;
                      }
                    }).toList()
                    : List<CampRecord>.from(
                      controller.campsConductedResponse?.output ?? [],
                    );

            rawRows.sort(
              (a, b) => a.registerWorkers.compareTo(b.registerWorkers),
            );

            hasRows = rawRows.isNotEmpty;
          }

          return adminController.hasInternet
              ? SizedBox(
                height: SizeConfig.screenHeight,
                width: SizeConfig.screenWidth,
                child: CustomScrollView(
                  slivers: [
                    SliverPadding(
                      padding: EdgeInsets.fromLTRB(16.w, 12.h, 16.w, 0),
                      sliver: SliverList(
                        delegate: SliverChildListDelegate([
                          filtersCard(context),
                          SizedBox(height: 12.h),
                          legendRow(),
                          SizedBox(height: 12.h),
                          tableHelper.buildHeader(context),
                          SizedBox(height: 2.h),
                        ]),
                      ),
                    ),
                    SliverPadding(
                      padding: EdgeInsets.fromLTRB(16.w, 0, 16.w, 24.h),
                      sliver:
                          isLoading
                              ? SliverToBoxAdapter(
                                child: CommonSkeletonInvoiceTable(
                                  itemCount: 12,
                                  rowHeight: 46,
                                  shrinkWrap: true,
                                  physics: const NeverScrollableScrollPhysics(),
                                ),
                              )
                              : hasRows
                              ? SliverList(
                                delegate: SliverChildBuilderDelegate((
                                  context,
                                  i,
                                ) {
                                  final r = rawRows[i];
                                  return Padding(
                                    padding: EdgeInsets.only(
                                      bottom: i == rawRows.length - 1 ? 0 : 2.h,
                                    ),
                                    child: tableHelper.buildRow(context, r, i),
                                  );
                                }, childCount: rawRows.length),
                              )
                              : SliverToBoxAdapter(
                                child: tableHelper.emptyState(),
                              ),
                    ),
                  ],
                ),
              )
              : NoInternetWidget(
                onRetryPressed: () {
                  return adminController.checkInternet();
                },
              );
        },
      ),
    );
  }

  Widget filtersCard(BuildContext context) {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(vertical: 12.h, horizontal: 12.w),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(12),
        color: kWhiteColor,
        boxShadow: [
          BoxShadow(
            color: kPrimaryColor.withValues(alpha: 0.2),
            spreadRadius: 2,
            blurRadius: 6,
            offset: const Offset(0, 0), // keep offset (0,0) for equal shadow
          ),
        ],
      ),
      child: Column(
        children: [
          //district
          Visibility(
            visible: widget.isDistAndYearVisiable,
            child: Padding(
              padding: EdgeInsets.symmetric(vertical: 4.h),
              child: filterField(
                icon: icMapPin,
                label: 'District',
                value: adminController.selectedDistrict ?? 'ALL',
                onTap:
                    () => pickFromList(
                      context: context,
                      title: 'Select District',
                      items: adminController.districtNamesForPicker,
                      // includes "ALL"
                      selected: adminController.selectedDistrict ?? 'ALL',
                      defaultOption: 'ALL',

                      onApply: (v) async {
                        adminController.setDistrictFromName(v);
                        await adminController.refreshTableForCurrentFilters();
                      },
                    ),
              ),
            ),
          ),

          // Month & Year
          Padding(
            padding: EdgeInsets.symmetric(vertical: 4.h),
            child: filterField(
              icon: icnTent,
              label: 'Camp Type',
              value: adminController.selectedCampType ?? 'ALL CAMP',
              onTap:
                  () => pickFromList(
                    context: context,
                    title: 'Select Camp Type',
                    items: adminController.campTypeLabelsForPicker,
                    // has "ALL CAMP"
                    selected: adminController.selectedCampType ?? 'ALL CAMP',
                    defaultOption: 'ALL CAMP',

                    onApply: (v) async {
                      adminController.setCampTypeFromLabel(v);

                      await adminController.refreshTableForCurrentFilters();
                    },
                  ),
            ),
          ),

          //year month
          Visibility(
            visible: widget.isDistAndYearVisiable,

            child: Padding(
              padding: EdgeInsets.symmetric(vertical: 4.h),
              child: filterField(
                icon: icCalendarMonth,
                label: 'Year and Month',
                value: FormatterManager.formatMonth(
                  adminController.selectedMonth,
                ),
                onTap: () async {
                  await _showYearMonthDialog(context);
                },
              ),
            ),
          ),

          SizedBox(height: 10.h),

          // if (widget.title != "All Camp List")
            Align(
              alignment: Alignment.centerLeft,
              child: CommonText(
                text:
                    '${widget.totalString}: ${adminController.campsConductedResponse?.output.length ?? '0'}',
                fontSize: 12.sp,
                fontWeight: FontWeight.w600,
                textColor: kBlackColor,
                textAlign: TextAlign.start,
              ),
            ),
        ],
      ),
    );
  }

  Future<void> _showYearMonthDialog(BuildContext context) async {
    final initial = adminController.selectedMonth;
    final currentYear = DateTime.now().year;
    final years = [for (int y = 2019; y <= currentYear; y++) y];
    final months = List<String>.generate(
      12,
      (i) => DateFormat.MMMM().format(DateTime(2000, i + 1, 1)),
    );

    int selectedYear = initial.year;
    int selectedMonthIndex = initial.month - 1;

    await showDialog<void>(
      context: context,
      barrierDismissible: false,
      builder: (dialogContext) {
        return AlertDialog(
          title: const Text('Select Month And Year'),
          content: StatefulBuilder(
            builder: (context, setModalState) {
              return Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  ListTile(
                    contentPadding: EdgeInsets.zero,
                    title: const Text('Year'),
                    subtitle: Text(selectedYear.toString()),
                    trailing: const Icon(Icons.keyboard_arrow_down_rounded),
                    onTap: () async {
                      final picked = await _pickFromSimpleList<int>(
                        context,
                        title: 'Select Year',
                        items: years,
                        labelOf: (y) => y.toString(),
                      );
                      if (picked != null) {
                        setModalState(() {
                          selectedYear = picked;
                        });
                      }
                    },
                  ),
                  ListTile(
                    contentPadding: EdgeInsets.zero,
                    title: const Text('Month'),
                    subtitle: Text(months[selectedMonthIndex]),
                    trailing: const Icon(Icons.keyboard_arrow_down_rounded),
                    onTap: () async {
                      final picked = await _pickFromSimpleList<int>(
                        context,
                        title: 'Select Month',
                        items: List<int>.generate(12, (i) => i),
                        labelOf: (i) => months[i],
                      );
                      if (picked != null) {
                        setModalState(() {
                          selectedMonthIndex = picked;
                        });
                      }
                    },
                  ),
                ],
              );
            },
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(dialogContext),
              child: const Text('Cancel'),
            ),
            TextButton(
              onPressed: () async {
                Navigator.pop(context);
                adminController.selectedMonth = DateTime(
                  selectedYear,
                  selectedMonthIndex + 1,
                  1,
                );
                await adminController.refreshTableForCurrentFilters();
                if (context.mounted) Navigator.pop(dialogContext);
              },
              child: const Text('Ok'),
            ),
          ],
        );
      },
    );
  }

  Future<T?> _pickFromSimpleList<T>(
    BuildContext context, {
    required String title,
    required List<T> items,
    required String Function(T) labelOf,
  }) async {
    return showDialog<T>(
      context: context,
      builder: (context) {
        return SimpleDialog(
          title: Text(title),
          children:
              items
                  .map(
                    (item) => SimpleDialogOption(
                      onPressed: () => Navigator.pop(context, item),
                      child: Text(labelOf(item)),
                    ),
                  )
                  .toList(),
        );
      },
    );
  }

  Widget filterField({
    required String icon,
    required String label,
    String? value,
    required VoidCallback onTap,
  }) {
    return InkWell(
      onTap: onTap,
      borderRadius: BorderRadius.circular(10),
      child: Container(
        height: 50.h,
        padding: EdgeInsets.symmetric(horizontal: 12.h),
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(10),
          border: Border.all(
            color: segmentButtonBorderColor.withValues(alpha: 0.5),
          ),
          color: kWhiteColor,
        ),
        child: Row(
          children: [
            Image.asset(icon, height: 20.h),
            SizedBox(width: 10.w),
            Expanded(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  CommonText(
                    text: label,
                    fontSize: 14.sp,
                    fontWeight: FontWeight.normal,
                    textColor: kLabelTextColor,
                    textAlign: TextAlign.start,
                  ),
                  CommonText(
                    text: (value == null || value.isEmpty) ? 'ALL' : value,
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w600,
                    textColor: kBlackColor,
                    textAlign: TextAlign.start,
                    overflow: TextOverflow.ellipsis,
                  ),
                ],
              ),
            ),
            const Icon(
              Icons.keyboard_arrow_down_rounded,
              color: Color(0xFF9D9D9D),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> pickFromList({
    required BuildContext context,
    required String title,
    required List<String> items,
    required String selected,
    required String defaultOption,
    required ValueChanged<String?> onApply,
  }) async {
    String tempSelected = selected;
    final List<String> original = List<String>.from(items);
    List<String> filtered = List<String>.from(original);
    final searchCtrl = TextEditingController();

    await showModalBottomSheet(
      context: context,
      isScrollControlled: true,

      backgroundColor: Colors.transparent,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder:
          (_) => StatefulBuilder(
            builder: (context, setModalState) {
              void runFilter(String q) {
                final query = q.trim().toLowerCase();
                setModalState(() {
                  if (query.isEmpty) {
                    filtered = List<String>.from(original);
                  } else {
                    filtered =
                        original
                            .where((e) => e.toLowerCase().contains(query))
                            .toList();
                  }
                });
              }

              return SafeArea(
                child: FractionallySizedBox(
                  heightFactor: 0.85,
                  child: Container(
                    clipBehavior: Clip.hardEdge,
                    decoration: const BoxDecoration(
                      color: Colors.white,
                      borderRadius: BorderRadius.vertical(
                        top: Radius.circular(16),
                      ),
                    ),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        SizedBox(height: 8.h),
                        Container(
                          height: 4.h,
                          width: 36.w,
                          decoration: BoxDecoration(
                            color: Colors.black12,
                            borderRadius: BorderRadius.circular(100),
                          ),
                        ),
                        SizedBox(height: 12.h),

                        // Title
                        CommonText(
                          text: title,
                          fontSize: 16.sp,
                          fontWeight: FontWeight.w600,
                          textColor: kTextColor,
                          textAlign: TextAlign.center,
                        ),
                        SizedBox(height: 8.h),

                        // Search field
                        Padding(
                          padding: EdgeInsets.symmetric(horizontal: 16.w),
                          child: TextField(
                            controller: searchCtrl,
                            onChanged: runFilter,
                            textInputAction: TextInputAction.search,
                            decoration: InputDecoration(
                              hintText: 'Search',
                              prefixIcon: const Icon(Icons.search),
                              suffixIcon:
                                  (searchCtrl.text.isEmpty)
                                      ? null
                                      : IconButton(
                                        icon: const Icon(Icons.clear),
                                        onPressed: () {
                                          searchCtrl.clear();
                                          runFilter('');
                                        },
                                      ),
                              isDense: true,
                              border: OutlineInputBorder(
                                borderSide: BorderSide(color: kPrimaryColor),
                                borderRadius: BorderRadius.circular(8),
                              ),

                              enabledBorder: OutlineInputBorder(
                                borderRadius: BorderRadius.circular(8),
                                borderSide: BorderSide(
                                  color: kTextColor.withValues(alpha: 0.28),
                                  width: 1,
                                ),
                              ),

                              contentPadding: EdgeInsets.symmetric(
                                horizontal: 12.w,
                                vertical: 10.h,
                              ),
                            ),
                          ),
                        ),
                        SizedBox(height: 8.h),

                        // List (or empty state)
                        Expanded(
                          child:
                              filtered.isEmpty
                                  ? Center(
                                    child: CommonText(
                                      text: 'No results',
                                      fontSize: 12.sp,
                                      fontWeight: FontWeight.w600,
                                      textColor: kTextBlackColor,
                                      textAlign: TextAlign.center,
                                    ),
                                  )
                                  : ListView.separated(
                                    padding: EdgeInsets.only(bottom: 12.h),
                                    itemCount: filtered.length,
                                    separatorBuilder:
                                        (_, a) => const Divider(height: 1),
                                    itemBuilder: (_, i) {
                                      final e = filtered[i];
                                      final isSelected =
                                          e.trim().toUpperCase() ==
                                          tempSelected.trim().toUpperCase();
                                      return InkWell(
                                        onTap: () {
                                          setModalState(() => tempSelected = e);
                                        },
                                        child: Row(
                                          mainAxisAlignment:
                                              MainAxisAlignment.spaceBetween,
                                          children: [
                                            Expanded(
                                              child: CommonText(
                                                text: e,
                                                fontSize: 14.sp,
                                                fontWeight: FontWeight.w600,
                                                textColor: kTextColor,
                                                textAlign: TextAlign.start,
                                              ),
                                            ),
                                            isSelected
                                                ? const Icon(
                                                  Icons.check,
                                                  color: kPrimaryColor,
                                                )
                                                : SizedBox.shrink(),
                                          ],
                                        ).paddingSymmetric(
                                          vertical: 6,
                                          horizontal: 16,
                                        ),
                                      );

                                      // ListTile(
                                      //   title: CommonText(
                                      //     text: e,
                                      //     fontSize: 12.sp,
                                      //     fontWeight: FontWeight.normal,
                                      //     textColor: kTextColor,
                                      //     textAlign: TextAlign.start,
                                      //   ),
                                      //   trailing:
                                      //       isSelected
                                      //           ? const Icon(
                                      //             Icons.check,
                                      //             color: kPrimaryColor,
                                      //           )
                                      //           : null,
                                      //   onTap:
                                      //       () => setModalState(
                                      //         () => tempSelected = e,
                                      //       ),
                                      // );
                                    },
                                  ),
                        ),

                        // Sticky buttons
                        Padding(
                          padding: EdgeInsets.fromLTRB(16.w, 4.h, 16.w, 16.h),
                          child: Row(
                            children: [
                              Expanded(
                                child: AppActiveButton(
                                  buttontitle: "Cancel",
                                  isCancel: true,
                                  onTap: () {
                                    Get.back();
                                  },
                                ),
                              ),
                              SizedBox(width: 16.w),
                              Expanded(
                                child: AppActiveButton(
                                  buttontitle: "Apply",
                                  onTap: () {
                                    onApply(tempSelected);
                                    Get.back();
                                  },
                                ),
                              ),
                            ],
                          ),
                        ),

                        // Lift sheet above keyboard
                        SizedBox(
                          height: MediaQuery.of(context).viewInsets.bottom,
                        ),
                      ],
                    ),
                  ),
                ),
              );
            },
          ),
    );
  }

  Widget legendRow() {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(horizontal: 14.w, vertical: 12.w),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(10),
        color: kWhiteColor,
        boxShadow: [
          BoxShadow(
            color: kPrimaryColor.withValues(alpha: 0.2),
            spreadRadius: 2,
            blurRadius: 6,
            offset: const Offset(0, 0), // keep offset (0,0) for equal shadow
          ),
        ],
      ),
      child: Row(
        children: [
          Container(
            width: 14.w,
            height: 14.w,
            decoration: BoxDecoration(
              color: kWhatsappColor.withValues(alpha: 0.2),
              borderRadius: BorderRadius.circular(4),
              border: Border.all(color: kTextBlackColor.withValues(alpha: 0.2)),
            ),
          ),
          SizedBox(width: 8.w),
          CommonText(
            text: "Today's Open Camps",
            fontSize: 10.sp,
            fontWeight: FontWeight.normal,
            textColor: kTextBlackColor,
            textAlign: TextAlign.start,
          ),
          SizedBox(width: 24.w),
          Container(
            width: 14.w,
            height: 14.w,
            decoration: BoxDecoration(
              color: kInstaColor.withValues(alpha: 0.2),
              borderRadius: BorderRadius.circular(4),
              border: Border.all(color: kTextBlackColor.withValues(alpha: 0.2)),
            ),
          ),
          SizedBox(width: 8.w),
          CommonText(
            text: "Open Camps",
            fontSize: 10.sp,
            fontWeight: FontWeight.normal,
            textColor: kTextBlackColor,
            textAlign: TextAlign.start,
          ),
        ],
      ),
    );
  }
}

class ConductedCampsTableHelper {
  final AdminController adminController = Get.find();

  Widget emptyState() {
    return Container(
      width: double.infinity,
      padding: EdgeInsets.symmetric(vertical: 16.h),
      alignment: Alignment.center,
      decoration: BoxDecoration(
        color: kWhiteColor,
        border: Border.all(color: kTextColor.withValues(alpha: 0.2), width: 1),
      ),
      child: Column(
        children: [
          Stack(
            children: [
              Image.asset(
                icCircle,
                color: kPrimaryColor,
                height: 200.h,
                width: 200.2,
              ),
              Positioned(
                top: 0,
                bottom: 0,
                left: 0,
                right: 0,
                child: Center(
                  child: Image.asset(
                    icFolder,
                    color: kPrimaryColor,
                    height: 100.h,
                    width: 100.w,
                  ),
                ),
              ),
            ],
          ),
          CommonText(
            text: 'No data found',
            fontSize: 12.sp,
            fontWeight: FontWeight.w600,
            textColor: kTextBlackColor,
            textAlign: TextAlign.center,
          ),
        ],
      ),
    );
  }

  Widget buildHeader(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        color: kPrimaryColor,
        boxShadow: [
          BoxShadow(
            blurRadius: 8,
            offset: const Offset(0, 2),
            color: kBlackColor.withValues(alpha: 0.4),
          ),
        ],
      ),
      child: const Row(
        children: [
          GridHeaderCell('Sr.No', flex: 12),
          GridHeaderCell('Camp ID', flex: 20),
          GridHeaderCell('District', flex: 26),
          GridHeaderCell('Date', flex: 24),
          GridHeaderCell('Reg.\nWorkers', flex: 20),
        ],
      ),
    );
  }

  Widget buildRow(BuildContext context, CampRecord r, int index) {
    final bg = _rowBgColor(r);

    return InkWell(
      onTap: () {
        Get.to(
          CampDetailsScreen(
            campId: r.campId,
            dISTLGDCODE: r.distLgdCode,
            campDate: r.campDate,
            surveyCoordinatorName: r.surveyCoordinatorName,
            dISTNAME: r.distName,
            mOBNO: r.mobNo,
            cAMPTYPE: int.parse(
              adminController.campTypes
                  .firstWhere(
                    (e) => e.campType == adminController.selectedCampType,
                  )
                  .campTypeId,
            ),
            campTypeDescription:
                adminController.campTypes
                    .firstWhere(
                      (e) => e.campType == adminController.selectedCampType,
                    )
                    .campType,
            isHealthScreeing: false,
          ),
        );
      },
      child: Container(
        decoration: BoxDecoration(
          color: bg,
          border: Border.all(
            color: kTextColor.withValues(alpha: 0.2),
            width: 1,
          ),
        ),
        child: Row(
          children: [
            GridCell(
              CommonText(
                text: (index + 1).toString(),
                fontSize: 10.sp,
                textColor: kTextBlackColor,
                textAlign: TextAlign.center,
                fontWeight: FontWeight.normal,
              ),
              flex: 10,
            ),
            GridCell(
              CommonText(
                text: r.campId.toString(),
                fontSize: 10.sp,
                textColor: kTextBlackColor,
                textAlign: TextAlign.center,
                fontWeight: FontWeight.normal,
              ),
              flex: 18,
            ),
            GridCell(
              CommonText(
                text: r.distName,
                fontSize: 10.sp,
                textColor: kTextBlackColor,
                textAlign: TextAlign.center,
                overflow: TextOverflow.ellipsis,
                fontWeight: FontWeight.normal,
              ),
              flex: 28,
            ),
            GridCell(
              CommonText(
                text: r.campDate,
                fontSize: 10.sp,
                textColor: kTextBlackColor,
                textAlign: TextAlign.center,
                overflow: TextOverflow.ellipsis,
                fontWeight: FontWeight.normal,
              ),
              flex: 28,
            ),
            GridCell(
              CommonText(
                text: r.registerWorkers.toString(),
                fontSize: 10.sp,
                textColor: kTextBlackColor,
                textAlign: TextAlign.center,
                overflow: TextOverflow.ellipsis,
                fontWeight: FontWeight.normal,
              ),
              flex: 18,
            ),
          ],
        ),
      ),
    );
  }

  // ---------- color logic ----------
  Color _rowBgColor(CampRecord r) {
    final s = (r.status).toUpperCase(); // D/E/P/W/R
    final isOpenStatus = s == 'D' || s == 'E' || s == 'P';

    DateTime? dt;
    try {
      dt = DateTime.parse(r.campDate);
    } catch (_) {}

    final now = DateTime.now();
    final today = DateTime(now.year, now.month, now.day);
    final isToday =
        dt != null &&
        dt.year == today.year &&
        dt.month == today.month &&
        dt.day == today.day;

    if (isToday && s != 'W' && s != 'R') {
      return kWhatsappColor.withValues(alpha: 0.2);
    }

    final isFuture = dt != null && dt.isAfter(today);
    if (isOpenStatus || isFuture) {
      return kInstaColor.withValues(alpha: 0.2);
    }

    return kWhiteColor;
  }
}

class GridHeaderCell extends StatelessWidget {
  final String text;
  final int flex;

  const GridHeaderCell(this.text, {super.key, required this.flex});

  @override
  Widget build(BuildContext context) {
    return Expanded(
      flex: flex,
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 2.h),
        child: CommonText(
          text: text,
          fontSize: 9.sp,
          fontWeight: FontWeight.w600,
          textColor: kWhiteColor,
          textAlign: TextAlign.center,
        ),
      ),
    );
  }
}

class GridCell extends StatelessWidget {
  final Widget child;
  final int flex;
  final bool alignRight;
  final bool isLast;

  const GridCell(
    this.child, {
    super.key,
    required this.flex,
    this.alignRight = false,
    this.isLast = false,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      flex: flex,
      child: Container(
        padding: EdgeInsets.symmetric(horizontal: 4.w, vertical: 8.h),
        child: DefaultTextStyle.merge(
          style: TextStyle(fontSize: 10.sp, color: kTextBlackColor),
          child: child,
        ),
      ),
    );
  }
}
