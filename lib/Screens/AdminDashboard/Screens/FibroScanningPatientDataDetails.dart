// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:open_file/open_file.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/AdminDashboard/Controller/AdminController.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/FibroScanningDistrictWiseModel.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/LiverScanningTableData.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'dart:io';
import 'package:excel/excel.dart' as ex;
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';

class FibroScanningPatientDataDetails extends StatefulWidget {
  final String distlgdcode;

  const FibroScanningPatientDataDetails({super.key, required this.distlgdcode});

  @override
  State<FibroScanningPatientDataDetails> createState() =>
      FibroScanningPatientDataDetailsState();
}

class FibroScanningPatientDataDetailsState
    extends State<FibroScanningPatientDataDetails> {
  final AdminController adminController = Get.put(AdminController());

  final ScrollController _horizontalController = ScrollController();

  late List<FibroscanDistrictData> sortedItems;

  @override
  void initState() {
    getData();
    super.initState();
  }

  void getData() async {
    Map<String, String> body = {
      'fromdate': adminController.fromDate,
      'todate': adminController.toDate,
      'distlgdcode': widget.distlgdcode,
    };

    adminController.getLiverTableDatadistrictWise(body);
  }

  @override
  void dispose() {
    _horizontalController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: "FibroScanning Patient Data",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
        showActions: true,
        actions: [
          Padding(
            padding: const EdgeInsets.only(left: 6, right: 10),
            child: InkWell(
              onTap: () {
                _exportToExcel(context);
              },
              child: Icon(
                Icons.save_alt_outlined,
                color: kWhiteColor,
                size: 26,
              ),
            ),
          ),
        ],
      ),
      body: GetBuilder<AdminController>(
        builder: (controller) {
          final data = controller.fibroScanDistirctResponse?.output ?? [];

          // Calculate totals
          int totalSuccessfulShots = 0;
          int totalShots = 0;

          for (var item in data) {
            totalSuccessfulShots +=
                int.tryParse(item.successfullShots ?? '0') ?? 0;
            totalShots += int.tryParse(item.totalShots ?? '0') ?? 0;
          }

          return controller.hasInternet
              ? AnnotatedRegion(
                value: const SystemUiOverlayStyle(
                  statusBarColor: kPrimaryColor,
                  statusBarBrightness: Brightness.light,
                  statusBarIconBrightness: Brightness.light,
                ),
                child: Scrollbar(
                  controller: _horizontalController,
                  thumbVisibility: true,
                  child: SingleChildScrollView(
                    controller: _horizontalController,
                    scrollDirection: Axis.horizontal,
                    child: SizedBox(
                      width: 600.w,
                      child: Column(
                        children: [
                          // Header Row
                          const CompactHeaderRow(),

                          // Total Row
                          CompactTotalRow(
                            totalSuccessfulShots: totalSuccessfulShots,
                            totalShots: totalShots,
                          ),

                          // Data Rows
                          Expanded(
                            child: ListView.builder(
                              padding: EdgeInsets.zero,
                              itemCount: data.length,
                              itemBuilder: (context, index) {
                                return CompactDataRow(
                                  item: data[index],
                                  index: index,
                                );
                              },
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ).paddingOnly(top: 10, left: 12, right: 12),
              )
              : NoInternetWidget(
                onRetryPressed: () {
                  return adminController.checkInternetLiverScann();
                },
              );
        },
      ),
    );
  }

  Future<void> _exportToExcel(BuildContext context) async {
    try {
      var status = await Permission.storage.request();
      if (status != PermissionStatus.granted) {
        status = await Permission.manageExternalStorage.request();
        if (status != PermissionStatus.granted) {
          if (!mounted) return;
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Storage permission is required to save file'),
            ),
          );
          return;
        }
      }

      final excel = ex.Excel.createExcel();
      final sheet = excel['FiberScanningDetailsDistrictWise'];

      // Headers
      final headers = <String>[
        'Patient Id',
        'Stiffness',
        'UAP',
        'Successful Shots',
        'Total Shots',
      ];
      for (int i = 0; i < headers.length; i++) {
        final cell = sheet.cell(
          ex.CellIndex.indexByColumnRow(columnIndex: i, rowIndex: 0),
        );
        cell.value = ex.TextCellValue(headers[i]);
        cell.cellStyle = ex.CellStyle(
          bold: true,
          backgroundColorHex: ex.ExcelColor.green,
          fontColorHex: ex.ExcelColor.white,
        );
      }

      // Safe parsers
      double _toDouble(String? s) {
        if (s == null) return 0;
        return double.tryParse(s.trim()) ?? 0;
      }

      int _toInt(String? s) {
        if (s == null) return 0;
        final i = int.tryParse(s.trim());
        if (i != null) return i;
        final d = double.tryParse(s.trim());
        return d?.round() ?? 0;
      }

      // Calculate totals
      final data = adminController.fibroScanDistirctResponse?.output ?? [];
      int totalSuccessfulShots = 0;
      int totalShots = 0;

      for (var item in data) {
        totalSuccessfulShots += _toInt(item.successfullShots);
        totalShots += _toInt(item.totalShots);
      }

      // Add Total Row (at row 1, right after header)
      sheet
          .cell(ex.CellIndex.indexByColumnRow(columnIndex: 0, rowIndex: 1))
          .value = ex.TextCellValue('TOTAL');

      sheet
          .cell(ex.CellIndex.indexByColumnRow(columnIndex: 1, rowIndex: 1))
          .value = ex.TextCellValue('-');

      sheet
          .cell(ex.CellIndex.indexByColumnRow(columnIndex: 2, rowIndex: 1))
          .value = ex.TextCellValue('-');

      sheet
          .cell(ex.CellIndex.indexByColumnRow(columnIndex: 3, rowIndex: 1))
          .value = ex.IntCellValue(totalSuccessfulShots);

      sheet
          .cell(ex.CellIndex.indexByColumnRow(columnIndex: 4, rowIndex: 1))
          .value = ex.IntCellValue(totalShots);

      // Apply styling to Total row
      for (int col = 0; col < 5; col++) {
        final cell = sheet.cell(
          ex.CellIndex.indexByColumnRow(columnIndex: col, rowIndex: 1),
        );
        cell.cellStyle = ex.CellStyle(
          bold: true,
          backgroundColorHex: ex.ExcelColor.lightBlue,
        );
      }

      // Data rows (starting from row 2)
      for (int i = 0; i < data.length; i++) {
        final r = data[i];
        final rowIndex = i + 2; // Start from row 2 since row 1 is Total

        sheet
            .cell(
              ex.CellIndex.indexByColumnRow(columnIndex: 0, rowIndex: rowIndex),
            )
            .value = ex.TextCellValue(r.patientId ?? '-');

        sheet
            .cell(
              ex.CellIndex.indexByColumnRow(columnIndex: 1, rowIndex: rowIndex),
            )
            .value = ex.DoubleCellValue(_toDouble(r.stiffness));

        sheet
            .cell(
              ex.CellIndex.indexByColumnRow(columnIndex: 2, rowIndex: rowIndex),
            )
            .value = ex.IntCellValue(_toInt(r.uap));

        sheet
            .cell(
              ex.CellIndex.indexByColumnRow(columnIndex: 3, rowIndex: rowIndex),
            )
            .value = ex.IntCellValue(_toInt(r.successfullShots));

        sheet
            .cell(
              ex.CellIndex.indexByColumnRow(columnIndex: 4, rowIndex: rowIndex),
            )
            .value = ex.IntCellValue(_toInt(r.totalShots));
      }

      Directory? directory;
      if (Platform.isAndroid) {
        final downloads = Directory('/storage/emulated/0/Download');
        directory =
            await downloads.exists()
                ? downloads
                : await getExternalStorageDirectory();
      } else {
        directory = await getApplicationDocumentsDirectory();
      }

      if (directory == null) {
        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Cannot resolve a writable directory')),
        );
        return;
      }

      final fileName =
          'FibroScanData_${DateTime.now().millisecondsSinceEpoch}.xlsx';
      final filePath = '${directory.path}/$fileName';
      final bytes = excel.save();

      if (bytes == null) {
        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Failed to create Excel bytes')),
        );
        return;
      }

      final outFile =
          File(filePath)
            ..createSync(recursive: true)
            ..writeAsBytesSync(bytes);

      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Excel file saved to: ${outFile.path}'),
          action: SnackBarAction(
            label: 'Open',
            onPressed: () => OpenFile.open(outFile.path),
          ),
        ),
      );
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Error exporting Excel: $e')));
    }
  }
}

// Header Row Widget
class CompactHeaderRow extends StatelessWidget {
  const CompactHeaderRow({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 50.h,
      decoration: BoxDecoration(
        color: kPrimaryColor,
        borderRadius: const BorderRadius.only(
          topLeft: Radius.circular(12),
          topRight: Radius.circular(12),
        ),
      ),
      child: Row(
        children: [
          CompactHeadCell('Patient Id', flex: 2),
          CompactHeadCell('Stiffness', flex: 1),
          CompactHeadCell('UAE', flex: 1),
          CompactHeadCell('Success\nShots', flex: 1),
          CompactHeadCell('Total\nShots', flex: 1),
        ],
      ),
    );
  }
}

class CompactHeadCell extends StatelessWidget {
  final String text;
  final int flex;

  const CompactHeadCell(this.text, {super.key, this.flex = 1});

  @override
  Widget build(BuildContext context) {
    return Expanded(
      flex: flex,
      child: Container(
        height: double.infinity,
        padding: EdgeInsets.symmetric(horizontal: 8.w),
        alignment: Alignment.center,
        decoration: BoxDecoration(
          border: Border(
            right: BorderSide(color: Colors.white.withOpacity(0.3), width: 1),
          ),
        ),
        child: Text(
          text,
          maxLines: 2,
          textAlign: TextAlign.center,
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.w600,
            fontSize: 10.sp,
            height: 1.2,
          ),
        ),
      ),
    );
  }
}

// Total Row Widget
class CompactTotalRow extends StatelessWidget {
  final int totalSuccessfulShots;
  final int totalShots;

  const CompactTotalRow({
    super.key,
    required this.totalSuccessfulShots,
    required this.totalShots,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 48.h,
      decoration: BoxDecoration(
        // color: kPrimaryColor.withOpacity(0.1),
        border: Border(bottom: BorderSide(color: Colors.grey.withOpacity(0.3))),
      ),
      child: Row(
        children: [
          CompactDataCell(
            'TOTAL',
            flex: 2,
            txtColor: kPrimaryColor,
            isBold: true,
          ),
          CompactDataCell('-', flex: 1, txtColor: kPrimaryColor, isBold: true),
          CompactDataCell('-', flex: 1, txtColor: kPrimaryColor, isBold: true),
          CompactDataCell(
            totalSuccessfulShots.toString(),
            flex: 1,
            txtColor: kPrimaryColor,
            isBold: true,
          ),
          CompactDataCell(
            totalShots.toString(),
            flex: 1,
            txtColor: kPrimaryColor,
            isBold: true,
          ),
        ],
      ),
    );
  }
}

// Data Row Widget
class CompactDataRow extends StatelessWidget {
  final FibroscanPatientData? item;
  final int index;

  const CompactDataRow({super.key, this.item, required this.index});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 48.h,
      decoration: BoxDecoration(
        color: Colors.white,
        border: Border(
          bottom: BorderSide(color: Colors.grey.withOpacity(0.15), width: 1),
        ),
      ),
      child: Row(
        children: [
          CompactDataCell(
            item?.patientId ?? '-',
            flex: 2,
            txtColor: kBlackColor,
          ),
          CompactDataCell(
            item?.stiffness ?? '-',
            flex: 1,
            txtColor: kBlackColor,
          ),
          CompactDataCell(item?.uap ?? '-', flex: 1, txtColor: kBlackColor),
          CompactDataCell(
            item?.successfullShots ?? '-',
            flex: 1,
            txtColor: kBlackColor,
          ),
          CompactDataCell(
            item?.totalShots ?? '-',
            flex: 1,
            txtColor: kBlackColor,
          ),
        ],
      ),
    );
  }
}

// Data Cell Widget
class CompactDataCell extends StatelessWidget {
  final String text;
  final int flex;
  final Color txtColor;
  final bool isBold;

  const CompactDataCell(
    this.text, {
    super.key,
    this.flex = 1,
    required this.txtColor,
    this.isBold = false,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      flex: flex,
      child: Container(
        height: double.infinity,
        padding: EdgeInsets.symmetric(horizontal: 8.w),
        alignment: Alignment.center,
        decoration: BoxDecoration(
          border: Border(
            right: BorderSide(color: Colors.grey.withOpacity(0.1)),
          ),
        ),
        child: Text(
          text,
          textAlign: TextAlign.center,
          style: TextStyle(
            fontSize: 10.sp,
            fontWeight: isBold ? FontWeight.w600 : FontWeight.w400,
            color: kBlackColor,
          ),
        ),
      ),
    );
  }
}

// // ignore_for_file: file_names
//
// import 'package:flutter/material.dart';
// import 'package:flutter/services.dart';
// import 'package:flutter_screenutil/flutter_screenutil.dart';
// import 'package:get/get.dart';
// import 'package:open_file/open_file.dart';
// import 'package:s2toperational/Modules/constants/constants.dart';
// import 'package:s2toperational/Modules/constants/images.dart';
// import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
// import 'package:s2toperational/Modules/widgets/CommonText.dart';
// import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
// import 'package:s2toperational/Screens/AdminDashboard/controller/AdminController.dart';
// import 'package:s2toperational/Screens/AdminDashboard/model/FibroScanningDistrictWiseModel.dart';
// import 'package:s2toperational/Screens/AdminDashboard/model/LiverScanningTableData.dart';
// import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
// import 'dart:io';
// import 'package:excel/excel.dart' as ex;
// import 'package:path_provider/path_provider.dart';
// import 'package:permission_handler/permission_handler.dart';
//
// class FibroScanningPatientDataDetails extends StatefulWidget {
//   final String distlgdcode;
//
//   const FibroScanningPatientDataDetails({super.key, required this.distlgdcode});
//
//   @override
//   State<FibroScanningPatientDataDetails> createState() =>
//       FibroScanningPatientDataDetailsState();
// }
//
// class FibroScanningPatientDataDetailsState
//     extends State<FibroScanningPatientDataDetails> {
//   final AdminController adminController = Get.put(AdminController());
//
//   final ScrollController _horizontalController = ScrollController();
//
//   late List<FibroscanDistrictData> sortedItems;
//
//   @override
//   void initState() {
//     getData();
//     super.initState();
//   }
//
//   void getData() async {
//     Map<String, String> body = {
//       'fromdate': adminController.fromDate,
//       'todate': adminController.toDate,
//       'distlgdcode': widget.distlgdcode,
//     };
//
//     adminController.getLiverTableDatadistrictWise(body);
//   }
//
//   @override
//   void dispose() {
//     _horizontalController.dispose();
//     super.dispose();
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: mAppBar(
//         scTitle: "FibroScanning Patient Data",
//         leadingIcon: iconBackArrow,
//         onLeadingIconClick: () => Navigator.pop(context),
//         showActions: true,
//         actions: [
//           Padding(
//             padding: const EdgeInsets.only(left: 6, right: 10),
//             child: InkWell(
//               onTap: () {
//                 _exportToExcel(context);
//               }, // Export to Excel on tap
//               child: Icon(
//                 Icons.save_alt_outlined,
//                 color: kWhiteColor,
//                 size: 26,
//               ),
//             ),
//           ),
//         ],
//       ),
//       body: GetBuilder<AdminController>(
//         builder: (controller) {
//           return controller.hasInternet
//               ? AnnotatedRegion(
//                 value: const SystemUiOverlayStyle(
//                   statusBarColor: kPrimaryColor,
//                   statusBarBrightness: Brightness.light,
//                   statusBarIconBrightness: Brightness.light,
//                 ),
//                 child: Scrollbar(
//                   controller: _horizontalController,
//                   thumbVisibility: true,
//                   child: SingleChildScrollView(
//                     controller: _horizontalController,
//                     scrollDirection: Axis.horizontal,
//                     child: SizedBox(
//                       width: 600.w, // Reduced from 900.w to 600.w
//                       child: Column(
//                         children: [
//                           const CompactHeaderRow(),
//                           Expanded(
//                             child: ListView.builder(
//                               padding: EdgeInsets.zero,
//                               itemCount:
//                                   adminController
//                                       .fibroScanDistirctResponse
//                                       ?.output
//                                       ?.length ??
//                                   0,
//                               itemBuilder: (context, index) {
//                                 return CompactDataRow(
//                                   item:
//                                       adminController
//                                           .fibroScanDistirctResponse
//                                           ?.output?[index],
//                                   index: index,
//                                 );
//                               },
//                             ),
//                           ),
//                         ],
//                       ),
//                     ),
//                   ),
//                 ).paddingOnly(top: 10, left: 12, right: 12),
//               )
//               : NoInternetWidget(
//                 onRetryPressed: () {
//                   return adminController.checkInternetLiverScann();
//                 },
//               );
//         },
//       ),
//     );
//   }
//
//   Future<void> _exportToExcel(BuildContext context) async {
//     try {
//       // 1) Request storage; on Android 11+ also try manageExternalStorage
//       var status = await Permission.storage.request();
//       if (status != PermissionStatus.granted) {
//         status = await Permission.manageExternalStorage.request();
//         if (status != PermissionStatus.granted) {
//           if (!mounted) return;
//           ScaffoldMessenger.of(context).showSnackBar(
//             const SnackBar(
//               content: Text('Storage permission is required to save file'),
//             ),
//           );
//           return;
//         }
//       }
//
//       // 2) Build workbook
//       final excel = ex.Excel.createExcel();
//       final sheet = excel['FiberScanningDetailsDistrictWise'];
//
//       // Headers (A..E in sequence)
//       final headers = <String>[
//         'Patient Id',
//         'Stiffness', // decimal like 5.2
//         'UAP', // integer
//         'Successful Shots',
//         'Total Shots',
//       ];
//       for (int i = 0; i < headers.length; i++) {
//         final cell = sheet.cell(
//           ex.CellIndex.indexByColumnRow(columnIndex: i, rowIndex: 0),
//         );
//         cell.value = ex.TextCellValue(headers[i]);
//         cell.cellStyle = ex.CellStyle(
//           bold: true,
//           backgroundColorHex: ex.ExcelColor.green,
//           fontColorHex: ex.ExcelColor.white,
//         );
//       }
//
//       // Safe parsers
//       double _toDouble(String? s) {
//         if (s == null) return 0;
//         return double.tryParse(s.trim()) ?? 0;
//       }
//
//       int _toInt(String? s) {
//         if (s == null) return 0;
//         // try int first; if it fails because of decimals, fall back to double then round
//         final i = int.tryParse(s.trim());
//         if (i != null) return i;
//         final d = double.tryParse(s.trim());
//         return d?.round() ?? 0;
//       }
//
//       // 3) Data rows
//       final data = adminController.fibroScanDistirctResponse?.output ?? [];
//       for (int i = 0; i < data.length; i++) {
//         final r = data[i];
//         final rowIndex = i + 1;
//
//         // A: Patient Id (text)
//         sheet
//             .cell(
//               ex.CellIndex.indexByColumnRow(columnIndex: 0, rowIndex: rowIndex),
//             )
//             .value = ex.TextCellValue(r.patientId ?? '-');
//
//         // B: Stiffness (double)
//         sheet
//             .cell(
//               ex.CellIndex.indexByColumnRow(columnIndex: 1, rowIndex: rowIndex),
//             )
//             .value = ex.DoubleCellValue(_toDouble(r.stiffness));
//
//         // C: UAP (int)
//         sheet
//             .cell(
//               ex.CellIndex.indexByColumnRow(columnIndex: 2, rowIndex: rowIndex),
//             )
//             .value = ex.IntCellValue(_toInt(r.uap));
//
//         // D: Successful Shots (int)
//         sheet
//             .cell(
//               ex.CellIndex.indexByColumnRow(columnIndex: 3, rowIndex: rowIndex),
//             )
//             .value = ex.IntCellValue(_toInt(r.successfullShots));
//
//         // E: Total Shots (int)
//         sheet
//             .cell(
//               ex.CellIndex.indexByColumnRow(columnIndex: 4, rowIndex: rowIndex),
//             )
//             .value = ex.IntCellValue(_toInt(r.totalShots));
//       }
//
//       // 4) Save to a writable directory
//       Directory? directory;
//       if (Platform.isAndroid) {
//         final downloads = Directory('/storage/emulated/0/Download');
//         directory =
//             await downloads.exists()
//                 ? downloads
//                 : await getExternalStorageDirectory();
//       } else {
//         directory = await getApplicationDocumentsDirectory();
//       }
//
//       if (directory == null) {
//         if (!mounted) return;
//         ScaffoldMessenger.of(context).showSnackBar(
//           const SnackBar(content: Text('Cannot resolve a writable directory')),
//         );
//         return;
//       }
//
//       final fileName =
//           'FibroScanData_${DateTime.now().millisecondsSinceEpoch}.xlsx';
//       final filePath = '${directory.path}/$fileName';
//       final bytes = excel.save();
//
//       if (bytes == null) {
//         if (!mounted) return;
//         ScaffoldMessenger.of(context).showSnackBar(
//           const SnackBar(content: Text('Failed to create Excel bytes')),
//         );
//         return;
//       }
//
//       final outFile =
//           File(filePath)
//             ..createSync(recursive: true)
//             ..writeAsBytesSync(bytes);
//
//       if (!mounted) return;
//       ScaffoldMessenger.of(context).showSnackBar(
//         SnackBar(
//           content: Text('Excel file saved to: ${outFile.path}'),
//           action: SnackBarAction(
//             label: 'Open',
//             onPressed: () => OpenFile.open(outFile.path),
//           ),
//         ),
//       );
//     } catch (e) {
//       if (!mounted) return;
//       ScaffoldMessenger.of(
//         context,
//       ).showSnackBar(SnackBar(content: Text('Error exporting Excel: $e')));
//     }
//   }
// }
//
// class CompactHeaderRow extends StatelessWidget {
//   const CompactHeaderRow({super.key});
//
//   @override
//   Widget build(BuildContext context) {
//     return Container(
//       height: 50.h,
//       decoration: BoxDecoration(
//         color: kPrimaryColor,
//         borderRadius: const BorderRadius.only(
//           topLeft: Radius.circular(12),
//           topRight: Radius.circular(12),
//         ),
//       ),
//       child: Row(
//         children: [
//           CompactHeadCell('Patient Id', flex: 2),
//           CompactHeadCell('Stiffness', flex: 2),
//           CompactHeadCell('UAE', flex: 1),
//           CompactHeadCell('Success\nShots', flex: 2),
//           CompactHeadCell('Total\nShots', flex: 2),
//         ],
//       ),
//     );
//   }
// }
//
// class CompactHeadCell extends StatelessWidget {
//   final String text;
//   final int flex;
//
//   const CompactHeadCell(this.text, {super.key, this.flex = 1});
//
//   @override
//   Widget build(BuildContext context) {
//     return Expanded(
//       flex: flex,
//       child: Container(
//         height: double.infinity,
//         padding: EdgeInsets.symmetric(horizontal: 8.w),
//         alignment: Alignment.center,
//         decoration: BoxDecoration(
//           border: Border(
//             right: BorderSide(color: Colors.white.withOpacity(0.3), width: 1),
//           ),
//         ),
//         child: Text(
//           text,
//           maxLines: 2,
//           textAlign: TextAlign.center,
//           style: TextStyle(
//             color: Colors.white,
//             fontWeight: FontWeight.w600,
//             fontSize: 11.sp,
//             height: 1.2,
//           ),
//         ),
//       ),
//     );
//   }
// }
//
// class CompactDataRow extends StatelessWidget {
//   final FibroscanPatientData? item;
//   final int index;
//
//   const CompactDataRow({super.key, this.item, required this.index});
//
//   @override
//   Widget build(BuildContext context) {
//     return Container(
//       height: 48.h,
//       decoration: BoxDecoration(
//         color: Colors.white,
//         border: Border(
//           bottom: BorderSide(color: Colors.grey.withOpacity(0.15), width: 1),
//         ),
//       ),
//       child: Row(
//         children: [
//           CompactDataCell(
//             item?.patientId ?? '-',
//             flex: 2,
//             txtColor: kBlackColor,
//           ),
//           CompactDataCell(
//             item?.stiffness ?? '-',
//             flex: 2,
//             txtColor: kBlackColor,
//           ),
//           CompactDataCell(item?.uap ?? '-', flex: 1, txtColor: kBlackColor),
//           CompactDataCell(
//             item?.successfullShots ?? '-',
//             flex: 2,
//             txtColor: kBlackColor,
//           ),
//           CompactDataCell(
//             item?.totalShots ?? '-',
//             flex: 2,
//             txtColor: kBlackColor,
//           ),
//         ],
//       ),
//     );
//   }
// }
//
// class CompactDataCell extends StatelessWidget {
//   final String text;
//   final int flex;
//   final Color txtColor;
//
//   const CompactDataCell(
//     this.text, {
//     super.key,
//     this.flex = 1,
//     required this.txtColor,
//   });
//
//   @override
//   Widget build(BuildContext context) {
//     return Expanded(
//       flex: flex,
//       child: Container(
//         height: double.infinity,
//         padding: EdgeInsets.symmetric(horizontal: 8.w),
//         alignment: Alignment.center,
//         decoration: BoxDecoration(
//           border: Border(
//             right: BorderSide(color: Colors.grey.withOpacity(0.1), width: 1),
//           ),
//         ),
//         child: Text(
//           text,
//           textAlign: TextAlign.center,
//           style: TextStyle(
//             fontSize: 12.sp,
//             fontWeight: FontWeight.w400,
//             color: txtColor,
//           ),
//         ),
//       ),
//     );
//   }
// }
//
// // class HeaderRow extends StatelessWidget {
// //   const HeaderRow({super.key});
// //
// //   @override
// //   Widget build(BuildContext context) {
// //     return Container(
// //       height: 50.h,
// //       decoration: BoxDecoration(
// //         color: kPrimaryColor,
// //         border: Border.all(color: offWhite),
// //       ),
// //       child: Row(
// //         children: [
// //           HeadCell('Patient Id', flex: 1),
// //           HeadCell('Stifness', flex: 1, isNumeric: true),
// //           HeadCell('UAE', flex: 1, isNumeric: true),
// //           HeadCell('Successful\nShots', flex: 1, isNumeric: true),
// //           HeadCell('Total Shots', flex: 1, isNumeric: true),
// //         ],
// //       ),
// //     );
// //   }
// // }
// //
// // class HeadCell extends StatelessWidget {
// //   final String text;
// //   final int flex;
// //   final bool isNumeric;
// //
// //   const HeadCell(this.text, {super.key, this.flex = 1, this.isNumeric = false});
// //
// //   @override
// //   Widget build(BuildContext context) {
// //     return Expanded(
// //       flex: flex,
// //       child: Container(
// //         height: double.infinity,
// //         padding: EdgeInsets.symmetric(horizontal: 10.w),
// //         alignment: Alignment.centerLeft,
// //         child: Text(
// //           text,
// //           maxLines: 2,
// //           textAlign: TextAlign.left,
// //           style: TextStyle(
// //             color: Colors.white,
// //             fontWeight: FontWeight.w700,
// //             fontSize: 9.sp,
// //           ),
// //         ),
// //       ),
// //     );
// //   }
// // }
// //
// // class FibroDataRow extends StatelessWidget {
// //   final FibroscanPatientData? item;
// //   final int index;
// //
// //   const FibroDataRow({super.key, this.item, required this.index});
// //
// //   @override
// //   Widget build(BuildContext context) {
// //     // More transparent background with subtle alternating colors
// //     final bg =
// //     index.isEven
// //         ? Colors.white.withValues(alpha: 0.05) // More transparent
// //         : Colors.grey.withValues(alpha: 0.05); // Even more transparent
// //
// //     return InkWell(
// //       onTap: () {
// //         // Navigate to next view - replace 'NextScreen' with your actual view
// //         // Navigator.push(
// //         //   context,
// //         //   MaterialPageRoute(
// //         //     builder: (context) => NextScreen(
// //         //       fibroData: item, // Pass the data to next view
// //         //     ),
// //         //   ),
// //         // );
// //       },
// //       child: Container(
// //         height: 50.h, // Match header height exactly
// //         decoration: BoxDecoration(
// //           color: bg,
// //           border: Border(
// //             bottom: BorderSide(
// //               color: Colors.grey.withValues(alpha: 0.15),
// //               // More transparent border
// //               width: 0.5,
// //             ),
// //           ),
// //         ),
// //         child: Row(
// //           children: [
// //             DataCell(
// //               item?.patientId ?? '-',
// //               flex: 1,
// //               isNumeric: false,
// //               txtColor: kBlackColor,
// //             ),
// //             DataCell(
// //               '${item?.stiffness}',
// //               flex: 1,
// //               isNumeric: true,
// //               txtColor: kBlackColor,
// //             ),
// //             DataCell(
// //               '${item?.uap}',
// //               flex: 1,
// //               isNumeric: true,
// //               txtColor: kBlackColor,
// //             ),
// //             DataCell(
// //               '${item?.successfullShots}',
// //               flex: 1,
// //               isNumeric: true,
// //               txtColor: kBlackColor,
// //             ),
// //             DataCell(
// //               '${item?.totalShots}',
// //               flex: 1,
// //               isNumeric: true,
// //               txtColor: kBlackColor,
// //             ),
// //
// //           ],
// //         ),
// //       ),
// //     );
// //   }
// // }
// //
// // class DataCell extends StatelessWidget {
// //   final String text;
// //   final int flex;
// //   final bool isNumeric;
// //   final Color txtColor;
// //
// //   const DataCell(this.text, {
// //     super.key,
// //     this.flex = 1,
// //     this.isNumeric = false,
// //     required this.txtColor,
// //   });
// //
// //   @override
// //   Widget build(BuildContext context) {
// //     return Expanded(
// //       flex: flex,
// //       child: Container(
// //         height: double.infinity,
// //         padding: EdgeInsets.symmetric(horizontal: 10.w),
// //         alignment: Alignment.centerLeft,
// //         child: CommonText(
// //           text: text,
// //           fontSize: 9.sp,
// //           fontWeight: FontWeight.w400,
// //           textColor: txtColor,
// //           textAlign: TextAlign.left,
// //         ),
// //       ),
// //     );
// //   }
// // }
