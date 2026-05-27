import 'package:flutter/material.dart';
import 'dart:math' as math;

import 'package:s2toperational/Modules/constants/constants.dart';

class CustomTimePickerDialog extends StatefulWidget {
  final TimeOfDay initialTime;
  final DateTime selectedDate;

  const CustomTimePickerDialog({
    super.key,
    required this.initialTime,
    required this.selectedDate,
  });

  @override
  State<CustomTimePickerDialog> createState() => _CustomTimePickerDialogState();
}

class _CustomTimePickerDialogState extends State<CustomTimePickerDialog> {
  late int _selectedHour;
  late int _selectedMinute;
  late bool _isPM;
  bool _isSelectingHour = true;

  @override
  void initState() {
    super.initState();
    // Initialize with the first enabled time instead of widget.initialTime
    _initializeWithFirstEnabledTime();
  }

  void _initializeWithFirstEnabledTime() {
    if (_isToday()) {
      final now = TimeOfDay.now();
      final currentHour24 = now.hour;
      final currentMinute = now.minute;

      // Calculate minimum allowed time (current time + 30 minutes)
      int minAllowedHour24 = currentHour24;
      int minAllowedMinute = currentMinute + 30;

      // If adding 30 minutes crosses the hour boundary
      if (minAllowedMinute >= 60) {
        minAllowedHour24 += 1;
        minAllowedMinute -= 60;
      }

      // Set the first enabled time
      _isPM = minAllowedHour24 >= 12;

      // Convert to 12-hour format
      if (minAllowedHour24 > 12) {
        _selectedHour = minAllowedHour24 - 12;
      } else if (minAllowedHour24 == 0) {
        _selectedHour = 12;
      } else {
        _selectedHour = minAllowedHour24;
      }

      // Set appropriate minute (0 or 30)
      if (minAllowedMinute > 0 && minAllowedMinute <= 30) {
        _selectedMinute = 30;
      } else if (minAllowedMinute > 30) {
        // Need to move to next hour with :00
        _selectedMinute = 0;
        _selectedHour += 1;
        if (_selectedHour > 12) {
          _selectedHour = 1;
          _isPM = !_isPM;
        }
      } else {
        _selectedMinute = 0;
      }
    } else {
      // For future dates, use the initial time or default to first slot
      _selectedHour = widget.initialTime.hourOfPeriod == 0
          ? 12
          : widget.initialTime.hourOfPeriod;
      _selectedMinute = widget.initialTime.minute;
      _isPM = widget.initialTime.period == DayPeriod.pm;
    }
  }

  bool _isToday() {
    final now = DateTime.now();
    return widget.selectedDate.year == now.year &&
        widget.selectedDate.month == now.month &&
        widget.selectedDate.day == now.day;
  }

  List<int> _getDisabledHoursForDisplay() {
    List<int> disabled = [];

    if (_isToday()) {
      final now = TimeOfDay.now();
      final currentHour24 = now.hour;
      final currentMinute = now.minute;

      // Add 30 minutes to current time
      int minAllowedHour24 = currentHour24;
      int minAllowedMinute = currentMinute + 30;

      // If adding 30 minutes crosses the hour boundary
      if (minAllowedMinute >= 60) {
        minAllowedHour24 += 1;
        minAllowedMinute -= 60;
      }

      for (int i = 1; i <= 12; i++) {
        int hour24 = _isPM ? (i == 12 ? 12 : i + 12) : (i == 12 ? 0 : i);

        // Disable if hour is less than minimum allowed hour
        if (hour24 < minAllowedHour24) {
          disabled.add(i);
        }
        // If hour equals minimum allowed hour, check if any valid minutes exist
        else if (hour24 == minAllowedHour24) {
          // If minAllowedMinute is 0-29, :30 is available, so hour is valid
          // If minAllowedMinute is 30+, no valid minutes, so disable hour
          if (minAllowedMinute >= 30) {
            disabled.add(i);
          }
        }
      }
    }

    return disabled;
  }

  List<int> _getDisabledMinutes() {
    if (_isToday()) {
      final now = TimeOfDay.now();
      final currentHour24 = now.hour;
      final currentMinute = now.minute;

      // Add 30 minutes to current time
      int minAllowedHour24 = currentHour24;
      int minAllowedMinute = currentMinute + 30;

      // If adding 30 minutes crosses the hour boundary
      if (minAllowedMinute >= 60) {
        minAllowedHour24 += 1;
        minAllowedMinute -= 60;
      }

      // Get selected hour in 24-hour format
      int selectedHour24 = _isPM
          ? (_selectedHour == 12 ? 12 : _selectedHour + 12)
          : (_selectedHour == 12 ? 0 : _selectedHour);

      // If selected hour is the minimum allowed hour
      if (selectedHour24 == minAllowedHour24) {
        List<int> disabled = [];

        // If minAllowedMinute is 0-29, :00 is past, only :30 is valid
        if (minAllowedMinute > 0 && minAllowedMinute <= 30) {
          disabled.add(0); // :00 is disabled, :30 is enabled
        }
        // If minAllowedMinute is 30+, both :00 and :30 are past
        else if (minAllowedMinute > 30) {
          disabled.add(0);
          disabled.add(30);
        }
        // If minAllowedMinute is 0, both are enabled

        return disabled;
      }
      // If selected hour is after minimum allowed hour, both 0 and 30 are enabled
      return [];
    }

    // For future dates, both 0 and 30 are always enabled
    return [];
  }

  bool _isHourDisabled(int hour) {
    return _getDisabledHoursForDisplay().contains(hour);
  }

  bool _isMinuteDisabled(int minute) {
    return _getDisabledMinutes().contains(minute);
  }

  void _handleHourSelected(int hour, {bool fromDrag = false}) {
    if (!_isHourDisabled(hour)) {
      setState(() {
        _selectedHour = hour;
        // Reset minute to valid value when hour changes
        final disabledMinutes = _getDisabledMinutes();

        // If both 00 and 30 are enabled, select 00
        if (!disabledMinutes.contains(0) && !disabledMinutes.contains(30)) {
          _selectedMinute = 0;
        }
        // If only 30 is enabled, select 30
        else if (!disabledMinutes.contains(30) && disabledMinutes.contains(0)) {
          _selectedMinute = 30;
        }
        // If current minute is disabled, find the first enabled one
        else if (_isMinuteDisabled(_selectedMinute)) {
          if (!disabledMinutes.contains(0)) {
            _selectedMinute = 0;
          } else if (!disabledMinutes.contains(30)) {
            _selectedMinute = 30;
          }
        }

        if (!fromDrag) {
          _isSelectingHour = false;
        }
      });
    }
  }

  void _handleMinuteSelected(int minute, {bool fromDrag = false}) {
    if (!_isMinuteDisabled(minute)) {
      setState(() {
        _selectedMinute = minute;
      });
    }
  }

  void _switchToMinutes() {
    if (_isSelectingHour) {
      setState(() {
        _isSelectingHour = false;
      });
    }
  }

  void _togglePeriod(bool pm) {
    // Only allow toggle if it's valid
    if (_isToday()) {
      final now = TimeOfDay.now();
      final currentIsPM = now.hour >= 12;

      // If current time is PM, don't allow switching to AM
      if (currentIsPM && !pm) {
        return; // Block AM selection
      }
    }

    setState(() {
      _isPM = pm;
    });
  }

  TimeOfDay _getTimeOfDay() {
    int hour24 = _isPM
        ? (_selectedHour == 12 ? 12 : _selectedHour + 12)
        : (_selectedHour == 12 ? 0 : _selectedHour);
    return TimeOfDay(hour: hour24, minute: _selectedMinute);
  }

  @override
  Widget build(BuildContext context) {
    return Dialog(
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        children: [
          // Purple Header
          Container(
            width: double.infinity,
            decoration: BoxDecoration(
              gradient: LinearGradient(
                begin: Alignment.bottomCenter,
                end: Alignment.topCenter,
                colors: [kPrimaryColor, kPrimaryColor.withValues(alpha: 0.6)],
              ),
              borderRadius: const BorderRadius.only(
                topLeft: Radius.circular(8),
                topRight: Radius.circular(8),
              ),
            ),
            padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 20),
            child: Column(
              children: [
                Text(
                  'SELECT APPOINTMENT TIME',
                  style: const TextStyle(
                    color: kWhiteColor,
                    fontSize: 10,
                    fontWeight: FontWeight.w500,
                    letterSpacing: 0.5,
                  ),
                ),
                const SizedBox(height: 16),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    GestureDetector(
                      onTap: () {
                        setState(() {
                          _isSelectingHour = true;
                        });
                      },
                      child: Text(
                        _selectedHour.toString(),
                        style: TextStyle(
                          fontSize: 42,
                          fontWeight: FontWeight.w300,
                          color: _isSelectingHour
                              ? Colors.white
                              : Colors.white.withOpacity(0.6),
                          height: 1,
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.only(top: 8),
                      child: Text(
                        ':',
                        style: TextStyle(
                          fontSize: 30,
                          fontWeight: FontWeight.w300,
                          color: Colors.white.withOpacity(0.8),
                          height: 1,
                        ),
                      ),
                    ),
                    GestureDetector(
                      onTap: () {
                        setState(() {
                          _isSelectingHour = false;
                        });
                      },
                      child: Text(
                        _selectedMinute.toString().padLeft(2, '0'),
                        style: TextStyle(
                          fontSize: 42,
                          fontWeight: FontWeight.w300,
                          color: !_isSelectingHour
                              ? Colors.white
                              : Colors.white.withOpacity(0.6),
                          height: 1,
                        ),
                      ),
                    ),
                    const SizedBox(width: 8),
                    Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        GestureDetector(
                          onTap: () => _togglePeriod(false),
                          child: Text(
                            'AM',
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.w500,
                              color: !_isPM
                                  ? Colors.white
                                  : Colors.white.withOpacity(0.5),
                            ),
                          ),
                        ),
                        GestureDetector(
                          onTap: () => _togglePeriod(true),
                          child: Text(
                            'PM',
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.w500,
                              color: _isPM
                                  ? Colors.white
                                  : Colors.white.withOpacity(0.5),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ],
            ),
          ),
          // White body with clock
          Container(
            color: Colors.white,
            padding: const EdgeInsets.all(6),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                SizedBox(
                  width: 280,
                  height: 280,
                  child: _isSelectingHour
                      ? ClockPicker(
                    key: const ValueKey('hour_picker'),
                    selectedValue: _selectedHour,
                    maxValue: 12,
                    onValueSelected: _handleHourSelected,
                    onDragEnd: _switchToMinutes,
                    disabledValues: _getDisabledHoursForDisplay(),
                    isHour: true,
                  )
                      : ClockPicker(
                    key: const ValueKey('minute_picker'),
                    selectedValue: _selectedMinute,
                    maxValue: 59,
                    onValueSelected: (min, {bool fromDrag = false}) =>
                        _handleMinuteSelected(min),
                    disabledValues: _getDisabledMinutes(),
                    isMinute: true,
                  ),
                ),
                const SizedBox(height: 10),
                Row(
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    TextButton(
                      onPressed: () => Navigator.of(context).pop(),
                      child: const Text(
                        'CANCEL',
                        style: TextStyle(
                          color: kPrimaryColor,
                          fontWeight: FontWeight.w600,
                          fontSize: 16,
                        ),
                      ),
                    ),
                    const SizedBox(width: 16),
                    TextButton(
                      onPressed: () {
                        Navigator.of(context).pop(_getTimeOfDay());
                      },
                      child: const Text(
                        'OK',
                        style: TextStyle(
                          color: kPrimaryColor,
                          fontWeight: FontWeight.w600,
                          fontSize: 16,
                        ),
                      ),
                    ),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class ClockPicker extends StatefulWidget {
  final int selectedValue;
  final int maxValue;
  final Function(int, {bool fromDrag}) onValueSelected;
  final VoidCallback? onDragEnd;
  final List<int> disabledValues;
  final bool isMinute;
  final bool isHour;

  const ClockPicker({
    super.key,
    required this.selectedValue,
    required this.maxValue,
    required this.onValueSelected,
    this.onDragEnd,
    this.disabledValues = const [],
    this.isMinute = false,
    this.isHour = false,
  });

  @override
  State<ClockPicker> createState() => _ClockPickerState();
}

class _ClockPickerState extends State<ClockPicker> {
  bool _isDragging = false;

  int _getValueFromPosition(Offset localPosition, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final dx = localPosition.dx - center.dx;
    final dy = localPosition.dy - center.dy;

    double angle = math.atan2(dy, dx);
    double degrees = (angle * 180 / math.pi + 90) % 360;
    if (degrees < 0) degrees += 360;

    if (widget.isMinute) {
      int exactValue = ((degrees / 6).round()) % 60;
      if (exactValue < 15) {
        return 0;
      } else if (exactValue < 45) {
        return 30;
      } else {
        return 0;
      }
    } else if (widget.isHour) {
      int value = ((degrees / 30).round());
      if (value == 0) value = 12;
      return value;
    }
    return 0;
  }

  int? _findNearestEnabledValue(int targetValue) {
    if (!widget.disabledValues.contains(targetValue)) {
      return targetValue;
    }

    int maxSearch = widget.isHour ? 12 : 60;
    for (int offset = 1; offset < maxSearch; offset++) {
      int clockwise = widget.isHour
          ? ((targetValue + offset - 1) % 12) + 1
          : (targetValue + offset) % 60;
      if (!widget.disabledValues.contains(clockwise)) {
        return clockwise;
      }

      int counterClockwise = widget.isHour
          ? ((targetValue - offset - 1 + 12) % 12) + 1
          : (targetValue - offset + 60) % 60;
      if (!widget.disabledValues.contains(counterClockwise)) {
        return counterClockwise;
      }
    }
    return null;
  }

  void _handleTapOrDrag(
      Offset localPosition,
      Size size, {
        bool fromDrag = false,
      }) {
    final value = _getValueFromPosition(localPosition, size);
    final nearestEnabled = _findNearestEnabledValue(value);

    if (nearestEnabled != null &&
        !widget.disabledValues.contains(nearestEnabled)) {
      widget.onValueSelected(nearestEnabled, fromDrag: fromDrag);
    }
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onPanStart: (details) {
        setState(() {
          _isDragging = true;
        });
        final RenderBox box = context.findRenderObject() as RenderBox;
        final localPosition = box.globalToLocal(details.globalPosition);
        _handleTapOrDrag(localPosition, box.size, fromDrag: true);
      },
      onPanUpdate: (details) {
        final RenderBox box = context.findRenderObject() as RenderBox;
        final localPosition = box.globalToLocal(details.globalPosition);
        _handleTapOrDrag(localPosition, box.size, fromDrag: true);
      },
      onPanEnd: (details) {
        setState(() {
          _isDragging = false;
        });
        Future.delayed(const Duration(milliseconds: 50), () {
          widget.onDragEnd?.call();
        });
      },
      onTapUp: (details) {
        if (!_isDragging) {
          final RenderBox box = context.findRenderObject() as RenderBox;
          final localPosition = box.globalToLocal(details.globalPosition);
          _handleTapOrDrag(localPosition, box.size, fromDrag: false);
        }
      },
      child: CustomPaint(
        painter: ClockPainter(
          selectedValue: widget.selectedValue,
          maxValue: widget.maxValue,
          disabledValues: widget.disabledValues,
          isMinute: widget.isMinute,
          isHour: widget.isHour,
        ),
        child: Container(),
      ),
    );
  }
}

class ClockPainter extends CustomPainter {
  final int selectedValue;
  final int maxValue;
  final List<int> disabledValues;
  final bool isMinute;
  final bool isHour;

  ClockPainter({
    required this.selectedValue,
    required this.maxValue,
    required this.disabledValues,
    required this.isMinute,
    required this.isHour,
  });

  @override
  void paint(Canvas canvas, Size size) {
    final center = Offset(size.width / 2, size.height / 2);
    final radius = size.width / 2 - 20;

    final circlePaint = Paint()
      ..color = Colors.grey.shade100
      ..style = PaintingStyle.fill;
    canvas.drawCircle(center, radius, circlePaint);

    if (isHour) {
      for (int i = 1; i <= 12; i++) {
        final angle = (i * 30 - 90) * math.pi / 180;
        final numberRadius = radius - 35;
        final position = Offset(
          center.dx + numberRadius * math.cos(angle),
          center.dy + numberRadius * math.sin(angle),
        );

        final isDisabled = disabledValues.contains(i);
        final isSelected = i == selectedValue;

        if (isSelected) {
          final selectedPaint = Paint()
            ..color = kPrimaryColor
            ..style = PaintingStyle.fill;
          canvas.drawCircle(position, 20, selectedPaint);
        }

        final textPainter = TextPainter(
          text: TextSpan(
            text: i.toString(),
            style: TextStyle(
              color: isDisabled
                  ? Colors.grey.shade300
                  : isSelected
                  ? Colors.white
                  : Colors.black87,
              fontSize: 18,
              fontWeight: FontWeight.w400,
            ),
          ),
          textDirection: TextDirection.ltr,
        );
        textPainter.layout();
        textPainter.paint(
          canvas,
          Offset(
            position.dx - textPainter.width / 2,
            position.dy - textPainter.height / 2,
          ),
        );
      }

      final selectedAngle = (selectedValue * 30 - 90) * math.pi / 180;
      final handEnd = Offset(
        center.dx + (radius - 55) * math.cos(selectedAngle),
        center.dy + (radius - 55) * math.sin(selectedAngle),
      );

      final handPaint = Paint()
        ..color = kPrimaryColor
        ..strokeWidth = 2
        ..strokeCap = StrokeCap.round;
      canvas.drawLine(center, handEnd, handPaint);

      final dotPaint = Paint()
        ..color = kPrimaryColor
        ..style = PaintingStyle.fill;
      canvas.drawCircle(handEnd, 4, dotPaint);
    } else if (isMinute) {
      for (int i = 0; i < 60; i += 5) {
        final angle = (i * 6 - 90) * math.pi / 180;
        final numberRadius = radius - 35;
        final position = Offset(
          center.dx + numberRadius * math.cos(angle),
          center.dy + numberRadius * math.sin(angle),
        );

        final isDisabled = disabledValues.contains(i);
        final isSelected = i == selectedValue;
        final isSelectable = (i == 0 || i == 30) && !isDisabled;

        if (isSelected) {
          final selectedPaint = Paint()
            ..color = kPrimaryColor
            ..style = PaintingStyle.fill;
          canvas.drawCircle(position, 20, selectedPaint);
        }

        final textPainter = TextPainter(
          text: TextSpan(
            text: i.toString().padLeft(2, '0'),
            style: TextStyle(
              color: !isSelectable
                  ? Colors.grey.shade300
                  : isSelected
                  ? Colors.white
                  : Colors.black87,
              fontSize: 16,
              fontWeight: FontWeight.w400,
            ),
          ),
          textDirection: TextDirection.ltr,
        );
        textPainter.layout();
        textPainter.paint(
          canvas,
          Offset(
            position.dx - textPainter.width / 2,
            position.dy - textPainter.height / 2,
          ),
        );
      }

      final selectedAngle = (selectedValue * 6 - 90) * math.pi / 180;
      final handEnd = Offset(
        center.dx + (radius - 55) * math.cos(selectedAngle),
        center.dy + (radius - 55) * math.sin(selectedAngle),
      );

      final handPaint = Paint()
        ..color = kPrimaryColor
        ..strokeWidth = 2
        ..strokeCap = StrokeCap.round;
      canvas.drawLine(center, handEnd, handPaint);

      final dotPaint = Paint()
        ..color = kPrimaryColor
        ..style = PaintingStyle.fill;
      canvas.drawCircle(handEnd, 4, dotPaint);
    }

    final centerPaint = Paint()
      ..color = kPrimaryColor
      ..style = PaintingStyle.fill;
    canvas.drawCircle(center, 5, centerPaint);
  }

  @override
  bool shouldRepaint(ClockPainter oldDelegate) {
    return oldDelegate.selectedValue != selectedValue ||
        oldDelegate.disabledValues != disabledValues;
  }
}

// class CustomTimePickerDialog extends StatefulWidget {
//   final TimeOfDay initialTime;
//   final DateTime selectedDate;
//
//   const CustomTimePickerDialog({
//     super.key,
//     required this.initialTime,
//     required this.selectedDate,
//   });
//
//   @override
//   State<CustomTimePickerDialog> createState() => _CustomTimePickerDialogState();
// }
//
// class _CustomTimePickerDialogState extends State<CustomTimePickerDialog> {
//   late int _selectedHour;
//   late int _selectedMinute;
//   late bool _isPM;
//   bool _isSelectingHour = true;
//
//   @override
//   void initState() {
//     super.initState();
//     _selectedHour = widget.initialTime.hourOfPeriod == 0
//         ? 12
//         : widget.initialTime.hourOfPeriod;
//     _selectedMinute = widget.initialTime.minute;
//     _isPM = widget.initialTime.period == DayPeriod.pm;
//
//     // Adjust initial selection based on date
//     _adjustInitialTimeIfNeeded();
//   }
//
//   void _adjustInitialTimeIfNeeded() {
//     if (_isToday()) {
//       final now = TimeOfDay.now();
//       final currentHour24 = now.hour;
//       final currentMinute = now.minute;
//
//       // If current time is PM, force PM selection
//       if (currentHour24 >= 12) {
//         _isPM = true;
//       }
//
//       // Add 30 minutes to current time
//       int minAllowedHour24 = currentHour24;
//       int minAllowedMinute = currentMinute + 30;
//
//       // If adding 30 minutes crosses the hour boundary
//       if (minAllowedMinute >= 60) {
//         minAllowedHour24 += 1;
//         minAllowedMinute -= 60;
//       }
//
//       // Convert selected time to 24-hour format
//       int selectedHour24 = _isPM
//           ? (_selectedHour == 12 ? 12 : _selectedHour + 12)
//           : (_selectedHour == 12 ? 0 : _selectedHour);
//
//       // Check if selected time is before minimum allowed time
//       bool needsAdjustment = false;
//
//       if (selectedHour24 < minAllowedHour24) {
//         needsAdjustment = true;
//       } else if (selectedHour24 == minAllowedHour24) {
//         // If in the same hour, check minutes
//         if (_selectedMinute == 0 && minAllowedMinute > 0) {
//           needsAdjustment = true;
//         } else if (_selectedMinute == 30 && minAllowedMinute > 30) {
//           needsAdjustment = true;
//         }
//       }
//
//       if (needsAdjustment) {
//         // Set to minimum allowed time
//         selectedHour24 = minAllowedHour24;
//         _isPM = selectedHour24 >= 12;
//         _selectedHour = selectedHour24 > 12 ? selectedHour24 - 12 : (selectedHour24 == 0 ? 12 : selectedHour24);
//
//         // Set appropriate minute
//         if (minAllowedMinute > 0 && minAllowedMinute <= 30) {
//           _selectedMinute = 30;
//         } else if (minAllowedMinute > 30) {
//           _selectedMinute = 0;
//           // Move to next hour
//           _selectedHour += 1;
//           if (_selectedHour > 12) {
//             _selectedHour = 1;
//             _isPM = !_isPM;
//           }
//         } else {
//           _selectedMinute = 0;
//         }
//       }
//     }
//   }
//
//   bool _isToday() {
//     final now = DateTime.now();
//     return widget.selectedDate.year == now.year &&
//         widget.selectedDate.month == now.month &&
//         widget.selectedDate.day == now.day;
//   }
//
//   List<int> _getDisabledHoursForDisplay() {
//     List<int> disabled = [];
//
//     if (_isToday()) {
//       final now = TimeOfDay.now();
//       final currentHour24 = now.hour;
//       final currentMinute = now.minute;
//
//       // Add 30 minutes to current time
//       int minAllowedHour24 = currentHour24;
//       int minAllowedMinute = currentMinute + 30;
//
//       // If adding 30 minutes crosses the hour boundary
//       if (minAllowedMinute >= 60) {
//         minAllowedHour24 += 1;
//         minAllowedMinute -= 60;
//       }
//
//       for (int i = 1; i <= 12; i++) {
//         int hour24 = _isPM ? (i == 12 ? 12 : i + 12) : (i == 12 ? 0 : i);
//
//         // Disable if hour is less than minimum allowed hour
//         if (hour24 < minAllowedHour24) {
//           disabled.add(i);
//         }
//         // If hour equals minimum allowed hour, check if any valid minutes exist
//         else if (hour24 == minAllowedHour24) {
//           // If minAllowedMinute is 0-29, :30 is available, so hour is valid
//           // If minAllowedMinute is 30+, no valid minutes, so disable hour
//           if (minAllowedMinute >= 30) {
//             disabled.add(i);
//           }
//         }
//       }
//     }
//
//     return disabled;
//   }
//
//   List<int> _getDisabledMinutes() {
//     if (_isToday()) {
//       final now = TimeOfDay.now();
//       final currentHour24 = now.hour;
//       final currentMinute = now.minute;
//
//       // Add 30 minutes to current time
//       int minAllowedHour24 = currentHour24;
//       int minAllowedMinute = currentMinute + 30;
//
//       // If adding 30 minutes crosses the hour boundary
//       if (minAllowedMinute >= 60) {
//         minAllowedHour24 += 1;
//         minAllowedMinute -= 60;
//       }
//
//       // Get selected hour in 24-hour format
//       int selectedHour24 = _isPM
//           ? (_selectedHour == 12 ? 12 : _selectedHour + 12)
//           : (_selectedHour == 12 ? 0 : _selectedHour);
//
//       // If selected hour is the minimum allowed hour
//       if (selectedHour24 == minAllowedHour24) {
//         List<int> disabled = [];
//
//         // If minAllowedMinute is 0-29, :00 is past, only :30 is valid
//         if (minAllowedMinute > 0 && minAllowedMinute <= 30) {
//           disabled.add(0); // :00 is disabled, :30 is enabled
//         }
//         // If minAllowedMinute is 30+, both :00 and :30 are past
//         else if (minAllowedMinute > 30) {
//           disabled.add(0);
//           disabled.add(30);
//         }
//         // If minAllowedMinute is 0, both are enabled
//
//         return disabled;
//       }
//       // If selected hour is after minimum allowed hour, both 0 and 30 are enabled
//       return [];
//     }
//
//     // For future dates, both 0 and 30 are always enabled
//     return [];
//   }
//
//   bool _isHourDisabled(int hour) {
//     return _getDisabledHoursForDisplay().contains(hour);
//   }
//
//   bool _isMinuteDisabled(int minute) {
//     return _getDisabledMinutes().contains(minute);
//   }
//
//   void _handleHourSelected(int hour, {bool fromDrag = false}) {
//     if (!_isHourDisabled(hour)) {
//       setState(() {
//         _selectedHour = hour;
//         // Reset minute to valid value when hour changes
//         final disabledMinutes = _getDisabledMinutes();
//
//         // If both 00 and 30 are enabled, select 00
//         if (!disabledMinutes.contains(0) && !disabledMinutes.contains(30)) {
//           _selectedMinute = 0;
//         }
//         // If only 30 is enabled, select 30
//         else if (!disabledMinutes.contains(30) && disabledMinutes.contains(0)) {
//           _selectedMinute = 30;
//         }
//         // If current minute is disabled, find the first enabled one
//         else if (_isMinuteDisabled(_selectedMinute)) {
//           if (!disabledMinutes.contains(0)) {
//             _selectedMinute = 0;
//           } else if (!disabledMinutes.contains(30)) {
//             _selectedMinute = 30;
//           }
//         }
//
//         if (!fromDrag) {
//           _isSelectingHour = false;
//         }
//       });
//     }
//   }
//
//   void _handleMinuteSelected(int minute, {bool fromDrag = false}) {
//     if (!_isMinuteDisabled(minute)) {
//       setState(() {
//         _selectedMinute = minute;
//       });
//     }
//   }
//
//   void _switchToMinutes() {
//     if (_isSelectingHour) {
//       setState(() {
//         _isSelectingHour = false;
//       });
//     }
//   }
//
//   void _togglePeriod(bool pm) {
//     // Only allow toggle if it's valid
//     if (_isToday()) {
//       final now = TimeOfDay.now();
//       final currentIsPM = now.hour >= 12;
//
//       // If current time is PM, don't allow switching to AM
//       if (currentIsPM && !pm) {
//         return; // Block AM selection
//       }
//     }
//
//     setState(() {
//       _isPM = pm;
//     });
//   }
//
//   TimeOfDay _getTimeOfDay() {
//     int hour24 = _isPM
//         ? (_selectedHour == 12 ? 12 : _selectedHour + 12)
//         : (_selectedHour == 12 ? 0 : _selectedHour);
//     return TimeOfDay(hour: hour24, minute: _selectedMinute);
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Dialog(
//       shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8)),
//       child: Column(
//         mainAxisSize: MainAxisSize.min,
//         children: [
//           // Purple Header
//           Container(
//             width: double.infinity,
//             decoration: BoxDecoration(
//               gradient: LinearGradient(
//                 begin: Alignment.topCenter,
//                 end: Alignment.bottomCenter,
//                 colors: [Colors.purple.shade400, Colors.purple.shade600],
//               ),
//               borderRadius: const BorderRadius.only(
//                 topLeft: Radius.circular(8),
//                 topRight: Radius.circular(8),
//               ),
//             ),
//             padding: const EdgeInsets.symmetric(vertical: 16, horizontal: 20),
//             child: Column(
//               children: [
//                 Text(
//                   'SELECT APPOINTMENT TIME',
//                   style: const TextStyle(
//                     color: Colors.white,
//                     fontSize: 12,
//                     fontWeight: FontWeight.w500,
//                     letterSpacing: 0.5,
//                   ),
//                 ),
//                 const SizedBox(height: 16),
//                 Row(
//                   mainAxisAlignment: MainAxisAlignment.center,
//                   crossAxisAlignment: CrossAxisAlignment.start,
//                   children: [
//                     GestureDetector(
//                       onTap: () {
//                         setState(() {
//                           _isSelectingHour = true;
//                         });
//                       },
//                       child: Text(
//                         _selectedHour.toString(),
//                         style: TextStyle(
//                           fontSize: 42,
//                           fontWeight: FontWeight.w300,
//                           color: _isSelectingHour
//                               ? Colors.white
//                               : Colors.white.withOpacity(0.6),
//                           height: 1,
//                         ),
//                       ),
//                     ),
//                     Padding(
//                       padding: const EdgeInsets.only(top: 8),
//                       child: Text(
//                         ':',
//                         style: TextStyle(
//                           fontSize: 30,
//                           fontWeight: FontWeight.w300,
//                           color: Colors.white.withOpacity(0.8),
//                           height: 1,
//                         ),
//                       ),
//                     ),
//                     GestureDetector(
//                       onTap: () {
//                         setState(() {
//                           _isSelectingHour = false;
//                         });
//                       },
//                       child: Text(
//                         _selectedMinute.toString().padLeft(2, '0'),
//                         style: TextStyle(
//                           fontSize: 42,
//                           fontWeight: FontWeight.w300,
//                           color: !_isSelectingHour
//                               ? Colors.white
//                               : Colors.white.withOpacity(0.6),
//                           height: 1,
//                         ),
//                       ),
//                     ),
//                     const SizedBox(width: 8),
//                     Column(
//                       mainAxisSize: MainAxisSize.min,
//                       children: [
//                         GestureDetector(
//                           onTap: () => _togglePeriod(false),
//                           child: Text(
//                             'AM',
//                             style: TextStyle(
//                               fontSize: 14,
//                               fontWeight: FontWeight.w500,
//                               color: !_isPM
//                                   ? Colors.white
//                                   : Colors.white.withOpacity(0.5),
//                             ),
//                           ),
//                         ),
//                         GestureDetector(
//                           onTap: () => _togglePeriod(true),
//                           child: Text(
//                             'PM',
//                             style: TextStyle(
//                               fontSize: 14,
//                               fontWeight: FontWeight.w500,
//                               color: _isPM
//                                   ? Colors.white
//                                   : Colors.white.withOpacity(0.5),
//                             ),
//                           ),
//                         ),
//                       ],
//                     ),
//                   ],
//                 ),
//               ],
//             ),
//           ),
//           // White body with clock
//           Container(
//             color: Colors.white,
//             padding: const EdgeInsets.all(6),
//             child: Column(
//               mainAxisSize: MainAxisSize.min,
//               children: [
//                 SizedBox(
//                   width: 280,
//                   height: 280,
//                   child: _isSelectingHour
//                       ? ClockPicker(
//                     key: const ValueKey('hour_picker'),
//                     selectedValue: _selectedHour,
//                     maxValue: 12,
//                     onValueSelected: _handleHourSelected,
//                     onDragEnd: _switchToMinutes,
//                     disabledValues: _getDisabledHoursForDisplay(),
//                     isHour: true,
//                   )
//                       : ClockPicker(
//                     key: const ValueKey('minute_picker'),
//                     selectedValue: _selectedMinute,
//                     maxValue: 59,
//                     onValueSelected: (min, {bool fromDrag = false}) =>
//                         _handleMinuteSelected(min),
//                     disabledValues: _getDisabledMinutes(),
//                     isMinute: true,
//                   ),
//                 ),
//                 const SizedBox(height: 10),
//                 Row(
//                   mainAxisAlignment: MainAxisAlignment.end,
//                   children: [
//                     TextButton(
//                       onPressed: () => Navigator.of(context).pop(),
//                       child: const Text(
//                         'CANCEL',
//                         style: TextStyle(
//                           color: Colors.purple,
//                           fontWeight: FontWeight.w600,
//                           fontSize: 16,
//                         ),
//                       ),
//                     ),
//                     const SizedBox(width: 16),
//                     TextButton(
//                       onPressed: () {
//                         Navigator.of(context).pop(_getTimeOfDay());
//                       },
//                       child: const Text(
//                         'OK',
//                         style: TextStyle(
//                           color: Colors.purple,
//                           fontWeight: FontWeight.w600,
//                           fontSize: 16,
//                         ),
//                       ),
//                     ),
//                   ],
//                 ),
//               ],
//             ),
//           ),
//         ],
//       ),
//     );
//   }
// }
//
// class ClockPicker extends StatefulWidget {
//   final int selectedValue;
//   final int maxValue;
//   final Function(int, {bool fromDrag}) onValueSelected;
//   final VoidCallback? onDragEnd;
//   final List<int> disabledValues;
//   final bool isMinute;
//   final bool isHour;
//
//   const ClockPicker({
//     super.key,
//     required this.selectedValue,
//     required this.maxValue,
//     required this.onValueSelected,
//     this.onDragEnd,
//     this.disabledValues = const [],
//     this.isMinute = false,
//     this.isHour = false,
//   });
//
//   @override
//   State<ClockPicker> createState() => _ClockPickerState();
// }
//
// class _ClockPickerState extends State<ClockPicker> {
//   bool _isDragging = false;
//
//   int _getValueFromPosition(Offset localPosition, Size size) {
//     final center = Offset(size.width / 2, size.height / 2);
//     final dx = localPosition.dx - center.dx;
//     final dy = localPosition.dy - center.dy;
//
//     double angle = math.atan2(dy, dx);
//     double degrees = (angle * 180 / math.pi + 90) % 360;
//     if (degrees < 0) degrees += 360;
//
//     if (widget.isMinute) {
//       int exactValue = ((degrees / 6).round()) % 60;
//       if (exactValue < 15) {
//         return 0;
//       } else if (exactValue < 45) {
//         return 30;
//       } else {
//         return 0;
//       }
//     } else if (widget.isHour) {
//       int value = ((degrees / 30).round());
//       if (value == 0) value = 12;
//       return value;
//     }
//     return 0;
//   }
//
//   int? _findNearestEnabledValue(int targetValue) {
//     if (!widget.disabledValues.contains(targetValue)) {
//       return targetValue;
//     }
//
//     int maxSearch = widget.isHour ? 12 : 60;
//     for (int offset = 1; offset < maxSearch; offset++) {
//       int clockwise = widget.isHour
//           ? ((targetValue + offset - 1) % 12) + 1
//           : (targetValue + offset) % 60;
//       if (!widget.disabledValues.contains(clockwise)) {
//         return clockwise;
//       }
//
//       int counterClockwise = widget.isHour
//           ? ((targetValue - offset - 1 + 12) % 12) + 1
//           : (targetValue - offset + 60) % 60;
//       if (!widget.disabledValues.contains(counterClockwise)) {
//         return counterClockwise;
//       }
//     }
//     return null;
//   }
//
//   void _handleTapOrDrag(
//       Offset localPosition,
//       Size size, {
//         bool fromDrag = false,
//       }) {
//     final value = _getValueFromPosition(localPosition, size);
//     final nearestEnabled = _findNearestEnabledValue(value);
//
//     if (nearestEnabled != null &&
//         !widget.disabledValues.contains(nearestEnabled)) {
//       widget.onValueSelected(nearestEnabled, fromDrag: fromDrag);
//     }
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return GestureDetector(
//       onPanStart: (details) {
//         setState(() {
//           _isDragging = true;
//         });
//         final RenderBox box = context.findRenderObject() as RenderBox;
//         final localPosition = box.globalToLocal(details.globalPosition);
//         _handleTapOrDrag(localPosition, box.size, fromDrag: true);
//       },
//       onPanUpdate: (details) {
//         final RenderBox box = context.findRenderObject() as RenderBox;
//         final localPosition = box.globalToLocal(details.globalPosition);
//         _handleTapOrDrag(localPosition, box.size, fromDrag: true);
//       },
//       onPanEnd: (details) {
//         setState(() {
//           _isDragging = false;
//         });
//         Future.delayed(const Duration(milliseconds: 50), () {
//           widget.onDragEnd?.call();
//         });
//       },
//       onTapUp: (details) {
//         if (!_isDragging) {
//           final RenderBox box = context.findRenderObject() as RenderBox;
//           final localPosition = box.globalToLocal(details.globalPosition);
//           _handleTapOrDrag(localPosition, box.size, fromDrag: false);
//         }
//       },
//       child: CustomPaint(
//         painter: ClockPainter(
//           selectedValue: widget.selectedValue,
//           maxValue: widget.maxValue,
//           disabledValues: widget.disabledValues,
//           isMinute: widget.isMinute,
//           isHour: widget.isHour,
//         ),
//         child: Container(),
//       ),
//     );
//   }
// }
//
// class ClockPainter extends CustomPainter {
//   final int selectedValue;
//   final int maxValue;
//   final List<int> disabledValues;
//   final bool isMinute;
//   final bool isHour;
//
//   ClockPainter({
//     required this.selectedValue,
//     required this.maxValue,
//     required this.disabledValues,
//     required this.isMinute,
//     required this.isHour,
//   });
//
//   @override
//   void paint(Canvas canvas, Size size) {
//     final center = Offset(size.width / 2, size.height / 2);
//     final radius = size.width / 2 - 20;
//
//     final circlePaint = Paint()
//       ..color = Colors.grey.shade100
//       ..style = PaintingStyle.fill;
//     canvas.drawCircle(center, radius, circlePaint);
//
//     if (isHour) {
//       for (int i = 1; i <= 12; i++) {
//         final angle = (i * 30 - 90) * math.pi / 180;
//         final numberRadius = radius - 35;
//         final position = Offset(
//           center.dx + numberRadius * math.cos(angle),
//           center.dy + numberRadius * math.sin(angle),
//         );
//
//         final isDisabled = disabledValues.contains(i);
//         final isSelected = i == selectedValue;
//
//         if (isSelected) {
//           final selectedPaint = Paint()
//             ..color = Colors.purple
//             ..style = PaintingStyle.fill;
//           canvas.drawCircle(position, 20, selectedPaint);
//         }
//
//         final textPainter = TextPainter(
//           text: TextSpan(
//             text: i.toString(),
//             style: TextStyle(
//               color: isDisabled
//                   ? Colors.grey.shade300
//                   : isSelected
//                   ? Colors.white
//                   : Colors.black87,
//               fontSize: 18,
//               fontWeight: FontWeight.w400,
//             ),
//           ),
//           textDirection: TextDirection.ltr,
//         );
//         textPainter.layout();
//         textPainter.paint(
//           canvas,
//           Offset(
//             position.dx - textPainter.width / 2,
//             position.dy - textPainter.height / 2,
//           ),
//         );
//       }
//
//       final selectedAngle = (selectedValue * 30 - 90) * math.pi / 180;
//       final handEnd = Offset(
//         center.dx + (radius - 55) * math.cos(selectedAngle),
//         center.dy + (radius - 55) * math.sin(selectedAngle),
//       );
//
//       final handPaint = Paint()
//         ..color = Colors.purple
//         ..strokeWidth = 2
//         ..strokeCap = StrokeCap.round;
//       canvas.drawLine(center, handEnd, handPaint);
//
//       final dotPaint = Paint()
//         ..color = Colors.purple
//         ..style = PaintingStyle.fill;
//       canvas.drawCircle(handEnd, 4, dotPaint);
//     } else if (isMinute) {
//       for (int i = 0; i < 60; i += 5) {
//         final angle = (i * 6 - 90) * math.pi / 180;
//         final numberRadius = radius - 35;
//         final position = Offset(
//           center.dx + numberRadius * math.cos(angle),
//           center.dy + numberRadius * math.sin(angle),
//         );
//
//         final isDisabled = disabledValues.contains(i);
//         final isSelected = i == selectedValue;
//         final isSelectable = (i == 0 || i == 30) && !isDisabled;
//
//         if (isSelected) {
//           final selectedPaint = Paint()
//             ..color = Colors.purple
//             ..style = PaintingStyle.fill;
//           canvas.drawCircle(position, 20, selectedPaint);
//         }
//
//         final textPainter = TextPainter(
//           text: TextSpan(
//             text: i.toString().padLeft(2, '0'),
//             style: TextStyle(
//               color: !isSelectable
//                   ? Colors.grey.shade300
//                   : isSelected
//                   ? Colors.white
//                   : Colors.black87,
//               fontSize: 16,
//               fontWeight: FontWeight.w400,
//             ),
//           ),
//           textDirection: TextDirection.ltr,
//         );
//         textPainter.layout();
//         textPainter.paint(
//           canvas,
//           Offset(
//             position.dx - textPainter.width / 2,
//             position.dy - textPainter.height / 2,
//           ),
//         );
//       }
//
//       final selectedAngle = (selectedValue * 6 - 90) * math.pi / 180;
//       final handEnd = Offset(
//         center.dx + (radius - 55) * math.cos(selectedAngle),
//         center.dy + (radius - 55) * math.sin(selectedAngle),
//       );
//
//       final handPaint = Paint()
//         ..color = Colors.purple
//         ..strokeWidth = 2
//         ..strokeCap = StrokeCap.round;
//       canvas.drawLine(center, handEnd, handPaint);
//
//       final dotPaint = Paint()
//         ..color = Colors.purple
//         ..style = PaintingStyle.fill;
//       canvas.drawCircle(handEnd, 4, dotPaint);
//     }
//
//     final centerPaint = Paint()
//       ..color = Colors.purple
//       ..style = PaintingStyle.fill;
//     canvas.drawCircle(center, 5, centerPaint);
//   }
//
//   @override
//   bool shouldRepaint(ClockPainter oldDelegate) {
//     return oldDelegate.selectedValue != selectedValue ||
//         oldDelegate.disabledValues != disabledValues;
//   }
// }