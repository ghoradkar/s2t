// ignore_for_file: file_names, depend_on_referenced_packages

import 'dart:io';
import 'dart:math';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:intl/intl.dart';
import 'package:http_parser/http_parser.dart';
import 'package:path/path.dart' as path;
import 'package:s2toperational/Modules/constants/fonts.dart';

import '../constants/constants.dart';

class FormatterManager {
  static String formatDateToString(DateTime date) {
    var outputFormat = DateFormat('yyyy/MM/dd');
    return outputFormat.format(date);
  }

  static String getDay(DateTime date) {
    var outputFormat = DateFormat('dd');
    return outputFormat.format(date);
  }

  static String campDateStringFromStringDate(String date) {
    final dateFormatter2 = DateFormat('dd/MM/yyy');
    final DateTime picked = dateFormatter2.parse(date);
    return FormatterManager.formatDateToFormatterString(picked, 'yyyy-MM-dd');
  }

  static String postCampDateFromDateString(String date) {
    final dateFormatter2 = DateFormat('yyyy-MM-dd');

    final DateTime picked = dateFormatter2.parse(date);

    DateTime nextWeekDay = picked.add(const Duration(days: 7));
    return FormatterManager.formatDateToFormatterString(
      nextWeekDay,
      'yyyy-MM-dd',
    );
  }

  static DateTime formatStringToDate(String date) {
    final dateFormatter2 = DateFormat('yyyy-MM-dd');
    return dateFormatter2.parse(date);
  }

  static String getFileNameInfo(File? selectedFile) {
    if (selectedFile != null) {
      String fileName = selectedFile.path.split('/').last;

      return fileName;
    }
    return "";
  }

  static String formatDateToFormatterString(DateTime date, String formatter) {
    var outputFormat = DateFormat(formatter);
    return outputFormat.format(date);
  }

  static String getFormattedFileSize(File? file) {
    if (file != null) {
      int bytes = file.lengthSync();

      if (bytes < 1024) {
        return "$bytes B";
      } else if (bytes < 1024 * 1024) {
        double kb = bytes / 1024;
        return "${kb.toStringAsFixed(2)} KB";
      } else if (bytes < 1024 * 1024 * 1024) {
        double mb = bytes / (1024 * 1024);
        return "${mb.toStringAsFixed(2)} MB";
      } else {
        double gb = bytes / (1024 * 1024 * 1024);
        return "${gb.toStringAsFixed(2)} GB";
      }
    } else {
      return "0 B";
    }
  }

  static MediaType getMediaTypeFromFile(File file) {
    final extension = path.extension(file.path).toLowerCase();

    switch (extension) {
      case '.jpg':
      case '.jpeg':
        return MediaType('image', 'jpeg');
      case '.png':
        return MediaType('image', 'png');
      case '.gif':
        return MediaType('image', 'gif');
      case '.pdf':
        return MediaType('application', 'pdf');
      case '.doc':
        return MediaType('application', 'msword');
      case '.docx':
        return MediaType(
          'application',
          'vnd.openxmlformats-officedocument.wordprocessingml.document',
        );
      case '.mp4':
        return MediaType('video', 'mp4');
      case '.mp3':
        return MediaType('audio', 'mpeg');
      case '.zip':
        return MediaType('application', 'zip');
      default:
        return MediaType('application', 'octet-stream'); // Generic binary
    }
  }

  static int compareTwoDates(String fromDate, String toDate) {
    final from = DateTime.parse('$fromDate 00:00:00');
    final to = DateTime.parse('$toDate 00:00:00');

    if (from.isAfter(to)) return 1;
    if (from.isBefore(to)) return 2;
    if (from.isAtSameMomentAs(to)) return 3;
    return 0;
  }

  //static Color alternateColor(int index) =>
  //     index % 2 == 0 ? Colors.white : const Color(0xFFCCCCCC);

  static Color getCellBackgroundColor(String campStatus, String campDate) {
    String systemDate = FormatterManager.formatDateToFormatterString(
      DateTime.now(),
      "yyyy-MM-dd",
    );

    if (campStatus.isNotEmpty) {
      if (campStatus.toLowerCase() == "camp Open".toLowerCase()) {
        final campCompare = compareTwoDates(campDate, systemDate);

        if (campCompare == 1) {
          return const Color(0xFF86C286);
        } else if (campCompare == 2) {
          return const Color(0xFFE46F6C);
        } else if (campCompare == 3) {
          return const Color(0xFF86C286);
        } else {
          return Colors.white;
        }
      } else {
        return Colors.white;
      }
    } else {
      return Colors.white;
    }
  }

  static Color getBeneficiaryCampRowColor(int isApproved) {
    // if (isApproved == 2) return Color(0xffffc6c6);
    // if (isApproved == 1) return Color(0xffC8E6C9);
    // if (isApproved == 3) return Color(0xffADD8E6);
    if (isApproved == 2) return rejectedBeneficiariesColor;
    if (isApproved == 1) return approvedBeneficiariesColor;
    if (isApproved == 3) return beneficiariesVerifiedColor;

    return Colors.white;
  }

  static String generateRandomDigits(int digitNumber) {
    final random = Random();
    String number = '';

    for (int i = 0; i < digitNumber; i++) {
      int randomNumber;
      do {
        randomNumber = random.nextInt(10);
      } while (i == 0 && randomNumber == 0);
      number += randomNumber.toString();
    }

    return number;
  }

  static String getFileNameFromDateTime() {
    final now = DateTime.now();
    final formatted =
        '${_twoDigits(now.day)}${_twoDigits(now.month)}${now.year}'
        '${_twoDigits(now.hour)}${_twoDigits(now.minute)}${_twoDigits(now.second)}';
    return formatted;
  }

  static String _twoDigits(int n) => n.toString().padLeft(2, '0');

  static bool isAscendingOrder(String fromDate, String toDate) {
    if (fromDate.isEmpty || toDate.isEmpty) return false;

    try {
      final format = DateFormat(
        "dd/MM/yyyy",
      ); // 👈 change format as per your dates
      DateTime from = format.parse(fromDate);
      DateTime to = format.parse(toDate);

      return from.isBefore(to) || from.isAtSameMomentAs(to);
    } catch (e) {
      return false;
    }
  }

  static String getMonthNameFromDate(int monthId) {
    switch (monthId) {
      case 1:
        return "January";
      case 2:
        return "February";
      case 3:
        return "March";
      case 04:
        return "April";
      case 05:
        return "May";
      case 06:
        return "June";
      case 07:
        return "July";
      case 08:
        return "August";
      case 09:
        return "September";
      case 10:
        return "October";
      case 11:
        return "November";
      case 12:
        return "December";
    }
    return "";
  }

  static Widget buildLabelWithAsterisk(String text) {
    return RichText(
      text: TextSpan(
        text: text,
        style: TextStyle(
          color: kBlackColor,
          fontFamily: FontConstants.interFonts,
          fontWeight: FontWeight.w600,
          fontSize: 14.sp,
        ),
        children: [
          TextSpan(
            text: ' *',
            style: TextStyle(
              color: Colors.red,
              fontFamily: FontConstants.interFonts,
              fontWeight: FontWeight.w400,
              fontSize: 16.sp,
            ),
          ),
        ],
      ),
    );
  }

  static String formatDateToStringInDash(DateTime date) {
    var outputFormat = DateFormat('yyyy-MM-dd');
    return outputFormat.format(date);
  }

  static String formatMonth(DateTime dt) {
    const months = [
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
    ];
    return '${months[dt.month - 1]} ${dt.year}';
  }

  static String formatCompact(num n) {
    if (n >= 100000) return '${(n / 100000).toStringAsFixed(2)} L';
    if (n >= 1000) return '${(n / 1000).toStringAsFixed(1)}k';
    return n.toStringAsFixed(0);
  }
}
