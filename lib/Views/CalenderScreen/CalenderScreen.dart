// ignore_for_file: must_be_immutable, collection_methods_unrelated_type, file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/constants/constants.dart';

class CalenderScreen extends StatefulWidget {
  CalenderScreen({
    super.key,
    required this.attendanceMap,
    required this.onDateSelectedTap,
    required this.didChangeDate,
  });

  late DateTime currentMonth;
  late List<DateTime> datesGrid;
  late DateTime todayMonths;

  Map<DateTime, Map<String, dynamic>> attendanceMap;

  Function(DateTime) onDateSelectedTap;
  Function(DateTime) didChangeDate;

  @override
  State<CalenderScreen> createState() => _CalenderScreenState();
}

class _CalenderScreenState extends State<CalenderScreen> {
  late DateTime currentMonth;
  late List<DateTime?> datesGrid;
  late DateTime todayMonths;

  @override
  void initState() {
    super.initState();
    currentMonth = DateTime.now();
    todayMonths = DateTime.now();
    datesGrid = _generateDatesGrid(currentMonth);
  }

  List<DateTime?> _generateDatesGrid(DateTime month) {
    int numDays = DateTime(month.year, month.month + 1, 0).day;
    int firstWeekday =
        DateTime(month.year, month.month, 1).weekday - 1; // FIXED
    List<DateTime?> dates = [];

    // Fill leading empty boxes
    for (int i = firstWeekday; i > 0; i--) {
      dates.add(null);
    }

    // Fill current month's dates
    for (int day = 1; day <= numDays; day++) {
      dates.add(DateTime(month.year, month.month, day));
    }

    // Fill remaining boxes to complete 6x7 grid
    int remainingBoxes = 42 - dates.length;
    for (int day = 1; day <= remainingBoxes; day++) {
      dates.add(null);
    }

    return dates;
  }

  void _changeMonth(int offset) {
    currentMonth = DateTime(currentMonth.year, currentMonth.month + offset);
    datesGrid = _generateDatesGrid(currentMonth);
    widget.didChangeDate(currentMonth);
    setState(() {});
  }

  Color isSameDay(DateTime date) {
    if (todayMonths.day == date.day && todayMonths.month == date.month) {
      return Colors.white;
    } else {
      return Colors.white;
    }
  }

  Color getDateColor(DateTime date) {
    Map<String, dynamic>? status =
        widget.attendanceMap[DateTime(date.year, date.month, date.day)];
    return status?['color'] ?? isSameDay(date);
  }

  int getDateValueColor(DateTime date) {
    Map<String, dynamic>? status =
        widget.attendanceMap[DateTime(date.year, date.month, date.day)];

    return status?['value'] ?? 0;
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      // height: double.infinity,
      height: MediaQuery.of(context).size.height * 0.4,
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.all(Radius.circular(20)),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withValues(alpha: 0.15),
            blurRadius: 10,
          ),
        ],
      ),
      child: Column(
        children: [
          Container(
            height: 40.h,
            padding: EdgeInsets.fromLTRB(12.w, 0, 12.w, 0),
            decoration: const BoxDecoration(
              color: kPrimaryColor,
              borderRadius: BorderRadius.only(
                topLeft: Radius.circular(10),
                topRight: Radius.circular(10),
              ),
            ),
            child: Row(
              children: [
                GestureDetector(
                  onTap: () {
                    _changeMonth(-1);
                  },
                  child: SizedBox(
                    width: 20.w,
                    height: 20.h,
                    child: Center(
                      child: Icon(Icons.arrow_back_ios, color: kWhiteColor),
                    ),
                  ),
                ),
                Expanded(
                  child: InkWell(
                    onTap: () async {
                      // Show month and year picker
                      final selectedDate = await showMonthYearPicker(
                        context: context,
                        initialDate: currentMonth,
                      );

                      if (selectedDate != null) {
                        currentMonth = selectedDate;
                        datesGrid = _generateDatesGrid(currentMonth);
                        widget.didChangeDate(currentMonth);
                        setState(() {});
                      }
                    },
                    child: Text(
                      textAlign: TextAlign.center,
                      '${_monthName(currentMonth.month)} ${currentMonth.year}',
                      style: TextStyle(
                        fontFamily: FontConstants.interFonts,
                        fontSize: 14.sp,
                        color: kWhiteColor,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                  ),
                ),
                GestureDetector(
                  onTap: () {
                    _changeMonth(1);
                  },
                  child: SizedBox(
                    width: 20.w,
                    height: 20.h,
                    child: Center(
                      child: Icon(Icons.arrow_forward_ios, color: kWhiteColor),
                    ),
                  ),
                ),
              ],
            ),
          ),
          // const SizedBox(height: 6),
          Padding(
            padding: EdgeInsets.fromLTRB(8.w, 0, 8.w, 0),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: List.generate(7, (index) {
                return Expanded(
                  child: Center(
                    child: Text(
                      ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'][index],
                      style: TextStyle(
                        fontWeight: FontWeight.w500,
                        fontSize: 14,
                        fontFamily: FontConstants.interFonts,
                        color: Colors.black,
                      ),
                    ),
                  ),
                );
              }),
            ),
          ),
          Flexible(
            child: Padding(
              padding: EdgeInsets.fromLTRB(8.w, 0, 8.w, 0),
              child: GridView.builder(
                physics: NeverScrollableScrollPhysics(),
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 7,
                ),
                itemCount: datesGrid.length,
                itemBuilder: (context, index) {
                  DateTime? date = datesGrid[index];

                  if (date == null) {
                    return const SizedBox();
                  }
                  bool isCurrentMonth = date.month == currentMonth.month;
                  return Padding(
                    padding: EdgeInsets.symmetric(
                      vertical: 2.h,
                      horizontal: 4.w,
                    ),
                    child: GestureDetector(
                      onTap: () {
                        widget.onDateSelectedTap(date);
                      },
                      child: Container(
                        decoration: BoxDecoration(
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withValues(alpha: 0.15),
                              blurRadius: 4,
                            ),
                          ],
                          color:
                              isCurrentMonth
                                  ? getDateColor(date)
                                  : Colors.white,
                          borderRadius: BorderRadius.circular(4.0),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              children: [
                                Expanded(
                                  child: Container(
                                    color: Colors.transparent,
                                    padding: EdgeInsets.fromLTRB(6, 3, 0, 0),
                                    child: Text(
                                      date.day.toString(),
                                      style: TextStyle(
                                        fontWeight: FontWeight.w500,
                                        fontSize: 10,
                                        fontFamily: FontConstants.interFonts,
                                        color: Colors.black,
                                      ),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                            Row(
                              children: [
                                Expanded(
                                  child: Container(
                                    color: Colors.transparent,
                                    padding: EdgeInsets.fromLTRB(0, 0, 6.w, 0),
                                    child: Text(
                                      getDateValueColor(date).toString(),
                                      textAlign: TextAlign.end,
                                      style: TextStyle(
                                        fontWeight: FontWeight.bold,
                                        fontSize: 10,
                                        fontFamily: FontConstants.interFonts,
                                        color: Colors.black,
                                      ),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ],
                        ),
                      ),
                    ),
                  );
                },
              ),
            ),
          ),
        ],
      ),
    );
  }

  Future<DateTime?> showMonthYearPicker({
    required BuildContext context,
    required DateTime initialDate,
  }) async {
    DateTime tempDate = initialDate;
    final int currentYear = DateTime.now().year;
    final int startYear = 2019;
    final List<int> years =
        List.generate(
          currentYear - startYear + 1,
          (index) => startYear + index,
        ).reversed.toList(); // Reverse to show latest year first

    return await showDialog<DateTime>(
      context: context,
      builder: (BuildContext context) {
        return StatefulBuilder(
          builder: (context, setState) {
            return AlertDialog(
              title: Text(
                'Select Month and Year',
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 16.sp,
                  fontWeight: FontWeight.w600,
                ),
              ),
              content: Container(
                height: 320.h,
                width: 300.w,
                child: Column(
                  children: [
                    // Year Selector (Dropdown style with scrollable list)
                    Container(
                      height: 50.h,
                      decoration: BoxDecoration(
                        border: Border.all(color: Colors.grey.withOpacity(0.3)),
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: DropdownButtonHideUnderline(
                        child: DropdownButton<int>(
                          value: tempDate.year,
                          isExpanded: true,
                          padding: EdgeInsets.symmetric(horizontal: 12.w),
                          items:
                              years.map((year) {
                                return DropdownMenuItem<int>(
                                  value: year,
                                  child: Text(
                                    '$year',
                                    style: TextStyle(
                                      fontFamily: FontConstants.interFonts,
                                      fontSize: 16.sp,
                                      fontWeight: FontWeight.w500,
                                    ),
                                  ),
                                );
                              }).toList(),
                          onChanged: (int? newYear) {
                            if (newYear != null) {
                              setState(() {
                                tempDate = DateTime(newYear, tempDate.month);
                              });
                            }
                          },
                        ),
                      ),
                    ),
                    SizedBox(height: 20.h),
                    // Month Grid
                    Expanded(
                      child: GridView.builder(
                        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                          crossAxisCount: 3,
                          childAspectRatio: 2,
                          crossAxisSpacing: 8,
                          mainAxisSpacing: 8,
                        ),
                        itemCount: 12,
                        itemBuilder: (context, index) {
                          final monthIndex = index + 1;
                          final isSelected = tempDate.month == monthIndex;

                          return GestureDetector(
                            onTap: () {
                              setState(() {
                                tempDate = DateTime(tempDate.year, monthIndex);
                              });
                            },
                            child: Container(
                              decoration: BoxDecoration(
                                color:
                                    isSelected
                                        ? kPrimaryColor
                                        : Colors.grey.withOpacity(0.2),
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: Center(
                                child: Text(
                                  _monthName(monthIndex).substring(0, 3),
                                  style: TextStyle(
                                    fontFamily: FontConstants.interFonts,
                                    fontSize: 12.sp,
                                    color:
                                        isSelected
                                            ? Colors.white
                                            : Colors.black,
                                    fontWeight:
                                        isSelected
                                            ? FontWeight.w600
                                            : FontWeight.w400,
                                  ),
                                ),
                              ),
                            ),
                          );
                        },
                      ),
                    ),
                  ],
                ),
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.of(context).pop(null),
                  child: Text(
                    'Cancel',
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      color: Colors.grey,
                    ),
                  ),
                ),
                ElevatedButton(
                  onPressed: () => Navigator.of(context).pop(tempDate),
                  style: ElevatedButton.styleFrom(
                    backgroundColor: kPrimaryColor,
                  ),
                  child: Text(
                    'OK',
                    style: TextStyle(
                      fontFamily: FontConstants.interFonts,
                      color: Colors.white,
                    ),
                  ),
                ),
              ],
            );
          },
        );
      },
    );
  }

  String _monthName(int monthNumber) {
    return [
      'January',
      'February',
      'March',
      'April',
      'May',
      'June',
      'July',
      'August',
      'September',
      'October',
      'November',
      'December',
    ][monthNumber - 1];
  }
}
