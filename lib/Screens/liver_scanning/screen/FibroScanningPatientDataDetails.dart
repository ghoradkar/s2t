// ignore_for_file: use_build_context_synchronously, file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:open_file/open_file.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/FibroScanningDistrictWiseModel.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'package:s2toperational/Screens/liver_scanning/controller/liver_scanning_controller.dart';
import 'dart:io';
import 'package:excel/excel.dart' as ex;
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';

class FibroScanningPatientDataDetails extends StatelessWidget {
  final String distlgdcode;

  const FibroScanningPatientDataDetails({
    super.key,
    required this.distlgdcode,
  });

  @override
  Widget build(BuildContext context) {
    return GetBuilder<LiverScanningController>(
      initState: (_) {
        Get.find<LiverScanningController>().loadDistrictWiseData(distlgdcode);
      },
      builder: (ctrl) {
        final data = ctrl.fibroScanDistirctResponse?.output ?? [];

        int totalSuccessfulShots = 0;
        int totalShots = 0;

        for (var item in data) {
          totalSuccessfulShots +=
              int.tryParse(item.successfullShots ?? '0') ?? 0;
          totalShots += int.tryParse(item.totalShots ?? '0') ?? 0;
        }

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
                    _exportToExcel(context, ctrl);
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
          body: ctrl.hasInternet
              ? AnnotatedRegion(
                  value: const SystemUiOverlayStyle(
                    statusBarColor: kPrimaryColor,
                    statusBarBrightness: Brightness.light,
                    statusBarIconBrightness: Brightness.light,
                  ),
                  child: Scrollbar(
                    controller: ctrl.patientTableScrollController,
                    thumbVisibility: true,
                    child: SingleChildScrollView(
                      controller: ctrl.patientTableScrollController,
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
                  onRetryPressed: () => ctrl.checkInternetAndLoad(),
                ),
        );
      },
    );
  }

  Future<void> _exportToExcel(
      BuildContext context, LiverScanningController ctrl) async {
    try {
      var status = await Permission.storage.request();
      if (status != PermissionStatus.granted) {
        status = await Permission.manageExternalStorage.request();
        if (status != PermissionStatus.granted) {
          if (!context.mounted) return;
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Storage permission is required to save file'),
            ),
          );
          return;
        }
      }

      final excel = ex.Excel.createExcel();
      final sheet = excel['Liver Scanning Patient Data'];
      excel.delete('Sheet1');

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

      double toDouble(String? s) {
        if (s == null) return 0;
        return double.tryParse(s.trim()) ?? 0;
      }

      int toInt(String? s) {
        if (s == null) return 0;
        final i = int.tryParse(s.trim());
        if (i != null) return i;
        final d = double.tryParse(s.trim());
        return d?.round() ?? 0;
      }

      final data = ctrl.fibroScanDistirctResponse?.output ?? [];
      int totalSuccessfulShots = 0;
      int totalShots = 0;

      for (var item in data) {
        totalSuccessfulShots += toInt(item.successfullShots);
        totalShots += toInt(item.totalShots);
      }

      // Total Row
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

      for (int col = 0; col < 5; col++) {
        final cell = sheet.cell(
          ex.CellIndex.indexByColumnRow(columnIndex: col, rowIndex: 1),
        );
        cell.cellStyle = ex.CellStyle(
          bold: true,
          backgroundColorHex: ex.ExcelColor.lightBlue,
        );
      }

      // Data rows
      for (int i = 0; i < data.length; i++) {
        final r = data[i];
        final rowIndex = i + 2;
        sheet
            .cell(ex.CellIndex.indexByColumnRow(
                columnIndex: 0, rowIndex: rowIndex))
            .value = ex.TextCellValue(r.patientId ?? '-');
        sheet
            .cell(ex.CellIndex.indexByColumnRow(
                columnIndex: 1, rowIndex: rowIndex))
            .value = ex.DoubleCellValue(toDouble(r.stiffness));
        sheet
            .cell(ex.CellIndex.indexByColumnRow(
                columnIndex: 2, rowIndex: rowIndex))
            .value = ex.IntCellValue(toInt(r.uap));
        sheet
            .cell(ex.CellIndex.indexByColumnRow(
                columnIndex: 3, rowIndex: rowIndex))
            .value = ex.IntCellValue(toInt(r.successfullShots));
        sheet
            .cell(ex.CellIndex.indexByColumnRow(
                columnIndex: 4, rowIndex: rowIndex))
            .value = ex.IntCellValue(toInt(r.totalShots));
      }

      Directory? directory;
      if (Platform.isAndroid) {
        final downloads = Directory('/storage/emulated/0/Download');
        directory = await downloads.exists()
            ? downloads
            : await getExternalStorageDirectory();
      } else {
        directory = await getApplicationDocumentsDirectory();
      }

      if (directory == null) {
        if (!context.mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
              content: Text('Cannot resolve a writable directory')),
        );
        return;
      }

      final now = DateTime.now();
      final dateStr =
          '${now.year}${now.month.toString().padLeft(2, '0')}${now.day.toString().padLeft(2, '0')}_${now.hour.toString().padLeft(2, '0')}${now.minute.toString().padLeft(2, '0')}${now.second.toString().padLeft(2, '0')}';
      final fileName = 'LiverScanningPatient_$dateStr.xlsx';
      final filePath = '${directory.path}/$fileName';
      final bytes = excel.save();

      if (bytes == null) {
        if (!context.mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Failed to create Excel bytes')),
        );
        return;
      }

      final outFile = File(filePath)
        ..createSync(recursive: true)
        ..writeAsBytesSync(bytes);

      if (context.mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Excel file saved to: ${outFile.path}'),
            action: SnackBarAction(
              label: 'Open',
              onPressed: () => OpenFile.open(outFile.path),
            ),
          ),
        );
      }
    } catch (e) {
      if (context.mounted) {
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text('Error exporting Excel: $e')));
      }
    }
  }
}

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
        border: Border(
            bottom: BorderSide(color: Colors.grey.withOpacity(0.3))),
      ),
      child: Row(
        children: [
          CompactDataCell('TOTAL',
              flex: 2, txtColor: kPrimaryColor, isBold: true),
          CompactDataCell('-', flex: 1, txtColor: kPrimaryColor, isBold: true),
          CompactDataCell('-', flex: 1, txtColor: kPrimaryColor, isBold: true),
          CompactDataCell(totalSuccessfulShots.toString(),
              flex: 1, txtColor: kPrimaryColor, isBold: true),
          CompactDataCell(totalShots.toString(),
              flex: 1, txtColor: kPrimaryColor, isBold: true),
        ],
      ),
    );
  }
}

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
          bottom:
              BorderSide(color: Colors.grey.withOpacity(0.15), width: 1),
        ),
      ),
      child: Row(
        children: [
          CompactDataCell(item?.patientId ?? '-',
              flex: 2, txtColor: kBlackColor),
          CompactDataCell(item?.stiffness ?? '-',
              flex: 1, txtColor: kBlackColor),
          CompactDataCell(item?.uap ?? '-', flex: 1, txtColor: kBlackColor),
          CompactDataCell(item?.successfullShots ?? '-',
              flex: 1, txtColor: kBlackColor),
          CompactDataCell(item?.totalShots ?? '-',
              flex: 1, txtColor: kBlackColor),
        ],
      ),
    );
  }
}

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
