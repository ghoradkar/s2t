// ignore_for_file: file_names, must_be_immutable

import 'package:flutter/material.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/utilities/SizeConfig.dart';

class MonthlyScreen extends StatefulWidget {
  MonthlyScreen({
    super.key,
    required this.initialMonth,
    required this.attendanceMap,
    required this.onDateSelectedTap,
    required this.didChangeDate,
  });

  final DateTime initialMonth;
  Map<DateTime, Color> attendanceMap;

  Function(DateTime) onDateSelectedTap;
  Function(DateTime) didChangeDate;
  @override
  State<MonthlyScreen> createState() => _MonthlyScreenState();
}

class _MonthlyScreenState extends State<MonthlyScreen> {
  late DateTime currentMonth;
  late List<DateTime?> datesGrid;
  late DateTime todayMonths;
  @override
  void initState() {
    super.initState();
    currentMonth = widget.initialMonth;
    todayMonths = DateTime.now();
    datesGrid = _generateDatesGrid(currentMonth);
  }

  @override
  void didUpdateWidget(covariant MonthlyScreen oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (oldWidget.initialMonth != widget.initialMonth) {
      setState(() {
        currentMonth = widget.initialMonth;
        datesGrid = _generateDatesGrid(currentMonth);
      });
    }
  }

  List<DateTime?> _generateDatesGrid(DateTime month) {
    int numDays = DateTime(month.year, month.month + 1, 0).day;
    int firstWeekday = DateTime(month.year, month.month, 1).weekday - 1;

    List<DateTime?> dates = [];

    // Fill blanks before first day of current month
    for (int i = 0; i < firstWeekday; i++) {
      dates.add(null);
    }

    // Fill current month's dates
    for (int day = 1; day <= numDays; day++) {
      dates.add(DateTime(month.year, month.month, day));
    }

    // Fill blanks after last day of current month
    int remainingBoxes = 42 - dates.length;
    for (int day = 1; day <= remainingBoxes; day++) {
      dates.add(null);
    }

    return dates;
  }

  void _changeMonth(int offset) {
    setState(() {
      currentMonth = DateTime(currentMonth.year, currentMonth.month + offset);
      datesGrid = _generateDatesGrid(currentMonth);
      widget.didChangeDate(currentMonth);
    });
  }

  Color isSameDay(DateTime date) {
    if (todayMonths.day == date.day && todayMonths.month == date.month) {
      return kPrimaryColor;
    } else {
      return Colors.transparent;
    }
  }

  Color getDateColor(DateTime date) {
    Color? status =
        widget.attendanceMap[DateTime(date.year, date.month, date.day)];

    return status ?? isSameDay(date);
  }

  Color isTextSameDay(DateTime date) {
    if (todayMonths.day == date.day && todayMonths.month == date.month) {
      return kWhiteColor;
    } else {
      return Colors.black;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 370,
      decoration: const BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.only(
          topLeft: Radius.circular(20),
          topRight: Radius.circular(20),
        ),
      ),
      padding: EdgeInsets.fromLTRB(8, 8, 8, 8),
      child: Column(
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              IconButton(
                icon: const Icon(Icons.arrow_back_ios),
                onPressed: () => _changeMonth(-1),
              ),
              Text(
                '${_monthName(currentMonth.month)} ${currentMonth.year}',
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: responsiveFont(22),
                  color: kBlackColor,
                  fontWeight: FontWeight.w400,
                ),
              ),
              IconButton(
                icon: const Icon(Icons.arrow_forward_ios),
                onPressed: () => _changeMonth(1),
              ),
            ],
          ),

          Row(
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
          Flexible(
            child: GridView.builder(
              physics: NeverScrollableScrollPhysics(),
              gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 7,
              ),
              itemCount: datesGrid.length,
              itemBuilder: (context, index) {
                DateTime? date = datesGrid[index];
                return Padding(
                  padding: const EdgeInsets.all(4.0),
                  child: GestureDetector(
                    onTap: () {
                      if (date != null) {
                        widget.onDateSelectedTap(date);
                      }
                    },
                    child: Container(
                      decoration: BoxDecoration(
                        color:
                            date != null
                                ? getDateColor(date)
                                : Colors.transparent,
                        borderRadius: BorderRadius.circular(8.0),
                      ),
                      child: Center(
                        child: Text(
                          date?.day.toString() ?? "",
                          style: TextStyle(
                            fontWeight: FontWeight.w500,
                            fontFamily: FontConstants.interFonts,
                            fontSize: 16,
                            color:
                                date != null
                                    ? isTextSameDay(date)
                                    : Colors.transparent,
                          ),
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
