// ignore_for_file: use_build_context_synchronously, file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:open_file/open_file.dart';
import 'package:s2toperational/Modules/FormatterManager/FormatterManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/LiverScanningTableData.dart';
// import 'package:s2toperational/Screens/AdminDashboard/Model/LiverScanningTableData.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'package:s2toperational/Screens/liver_scanning/controller/liver_scanning_controller.dart';
import 'package:s2toperational/Screens/liver_scanning/screen/FibroScanningPatientDataDetails.dart';
import 'dart:io';
import 'package:excel/excel.dart' as ex;
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';

class FibroScanningPatientDataScreen extends StatelessWidget {
  const FibroScanningPatientDataScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return GetBuilder<LiverScanningController>(
      builder: (ctrl) {
        final items = ctrl.fibroScanResponse?.output ?? [];
        int totalPatientCount = 0;
        int totalAbnormalPatients = 0;
        int totalModerateSevere = 0;
        int totalSuccessfulShots = 0;
        int totalShots = 0;

        for (var item in items) {
          totalPatientCount += item.patientCount;
          totalAbnormalPatients += item.abnormalPatientCount;
          totalModerateSevere += item.moderateSevereCount;
          totalSuccessfulShots += item.successfullShots;
          totalShots += item.totalShots;
        }

        return Scaffold(
          backgroundColor: Colors.white,
          appBar: mAppBar(
            scTitle: "FibroScanning Patient Data",
            leadingIcon: iconBackArrow,
            onLeadingIconClick: () => Navigator.pop(context),
            showActions: true,
            actions: [
              InkWell(
                onTap: () => ctrl.refreshTableData(),
                child: Icon(Icons.refresh, color: kWhiteColor, size: 26),
              ),
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
                  child: Container(
                    color: Colors.white,
                    height: SizeConfig.screenHeight,
                    width: SizeConfig.screenWidth,
                    child: Column(
                      children: [
                        // Date Filters
                        Container(
                          width: double.infinity,
                          margin: const EdgeInsets.only(top: 0),
                          padding: const EdgeInsets.symmetric(horizontal: 0),
                          decoration: BoxDecoration(
                            color: Colors.transparent,
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: Row(
                            children: [
                              Expanded(
                                child: AppTextField(
                                  onTap: () {
                                    ctrl.selectFromDate(context);
                                  },
                                  controller: ctrl.fromDateController,
                                  readOnly: true,
                                  label: RichText(
                                    text: TextSpan(
                                      text: 'From Date *',
                                      style: TextStyle(
                                        color: kLabelTextColor,
                                        fontSize: 14.sp,
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                    ),
                                  ),
                                  prefixIcon: Image.asset(
                                    icCalendarMonth,
                                    color: kPrimaryColor,
                                  ).paddingOnly(left: 6.w),
                                ),
                              ),
                              const SizedBox(width: 10),
                              Expanded(
                                child: AppTextField(
                                  onTap: () {
                                    ctrl.selectToDate(context);
                                  },
                                  controller: ctrl.toDateController,
                                  readOnly: true,
                                  label: RichText(
                                    text: TextSpan(
                                      text: 'To Date *',
                                      style: TextStyle(
                                        color: kLabelTextColor,
                                        fontSize: 14.sp,
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                    ),
                                  ),
                                  prefixIcon: Image.asset(
                                    icCalendarMonth,
                                    color: kPrimaryColor,
                                  ).paddingOnly(left: 6.w),
                                ),
                              ),
                            ],
                          ),
                        ).paddingSymmetric(horizontal: 12),

                        const SizedBox(height: 4),

                        // Table with Total Row
                        Expanded(
                          child: ctrl.isTableLoading
                              ? const CommonSkeletonScreeningDetailsTable(
                                  rowCount: 14,
                                )
                              : Container(
                                  margin: const EdgeInsets.symmetric(
                                    horizontal: 12,
                                    vertical: 8,
                                  ),
                                  decoration: BoxDecoration(
                                    color: Colors.white,
                                    borderRadius: BorderRadius.circular(12),
                                    boxShadow: [
                                      BoxShadow(
                                        offset: const Offset(0, 2),
                                        color: Colors.black.withOpacity(0.08),
                                        blurRadius: 8,
                                      ),
                                    ],
                                  ),
                                  child: Scrollbar(
                                    controller:
                                        ctrl.districtTableScrollController,
                                    thumbVisibility: true,
                                    child: SingleChildScrollView(
                                      controller:
                                          ctrl.districtTableScrollController,
                                      scrollDirection: Axis.horizontal,
                                      child: SizedBox(
                                        width: 600.w,
                                        child: Column(
                                          children: [
                                            // Header
                                            Container(
                                              decoration: BoxDecoration(
                                                color: kPrimaryColor,
                                                borderRadius:
                                                    const BorderRadius.only(
                                                  topLeft:
                                                      Radius.circular(12),
                                                  topRight:
                                                      Radius.circular(12),
                                                ),
                                              ),
                                              child: const ModernHeaderRow(),
                                            ),

                                            // Total Row
                                            Container(
                                              decoration: BoxDecoration(
                                                border: Border(
                                                  bottom: BorderSide(
                                                    color: Colors.grey
                                                        .withOpacity(0.2),
                                                    width: 1,
                                                  ),
                                                ),
                                              ),
                                              child: ModernTotalRow(
                                                totalPatientCount:
                                                    totalPatientCount,
                                                totalAbnormalPatients:
                                                    totalAbnormalPatients,
                                                totalModerateSevere:
                                                    totalModerateSevere,
                                                totalSuccessfulShots:
                                                    totalSuccessfulShots,
                                                totalShots: totalShots,
                                              ),
                                            ),

                                            // Data Rows
                                            Expanded(
                                              child: ListView.builder(
                                                padding: EdgeInsets.zero,
                                                itemCount: items.length,
                                                itemBuilder: (context, index) {
                                                  return ModernDataRow(
                                                    item: items[index],
                                                    index: index,
                                                  );
                                                },
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                    ),
                                  ),
                                ),
                        ),
                      ],
                    ).paddingSymmetric(vertical: 14),
                  ),
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
      ex.Sheet sheet;
      if (excel.sheets.containsKey('Sheet1')) {
        excel.rename('Sheet1', 'Liver Scanning District Data');
        sheet = excel['Liver Scanning District Data'];
      } else {
        sheet = excel['Liver Scanning District Data'];
      }

      // Headers
      sheet.cell(ex.CellIndex.indexByString("A1")).value =
          ex.TextCellValue("District");
      sheet.cell(ex.CellIndex.indexByString("B1")).value =
          ex.TextCellValue("Patient Count");
      sheet.cell(ex.CellIndex.indexByString("C1")).value =
          ex.TextCellValue("Abnormal Patients");
      sheet.cell(ex.CellIndex.indexByString("D1")).value =
          ex.TextCellValue("Referral Warranted(Moderate-Severe)");
      sheet.cell(ex.CellIndex.indexByString("E1")).value =
          ex.TextCellValue("Successful Shots");
      sheet.cell(ex.CellIndex.indexByString("F1")).value =
          ex.TextCellValue("Total Shots");

      // Style headers
      for (int col = 0; col < 6; col++) {
        final cell = sheet.cell(
          ex.CellIndex.indexByColumnRow(columnIndex: col, rowIndex: 0),
        );
        cell.cellStyle = ex.CellStyle(
          bold: true,
          backgroundColorHex: ex.ExcelColor.green,
          fontColorHex: ex.ExcelColor.white,
        );
      }

      final items = ctrl.fibroScanResponse?.output ?? [];

      // Calculate totals
      int totalPatientCount = 0;
      int totalAbnormalPatients = 0;
      int totalModerateSevere = 0;
      int totalSuccessfulShots = 0;
      int totalShots = 0;

      for (var item in items) {
        totalPatientCount += item.patientCount;
        totalAbnormalPatients += item.abnormalPatientCount;
        totalModerateSevere += item.moderateSevereCount;
        totalSuccessfulShots += item.successfullShots;
        totalShots += item.totalShots;
      }

      // Total Row
      sheet
          .cell(ex.CellIndex.indexByColumnRow(columnIndex: 0, rowIndex: 1))
          .value = ex.TextCellValue('TOTAL');
      sheet
          .cell(ex.CellIndex.indexByColumnRow(columnIndex: 1, rowIndex: 1))
          .value = ex.IntCellValue(totalPatientCount);
      sheet
          .cell(ex.CellIndex.indexByColumnRow(columnIndex: 2, rowIndex: 1))
          .value = ex.IntCellValue(totalAbnormalPatients);
      sheet
          .cell(ex.CellIndex.indexByColumnRow(columnIndex: 3, rowIndex: 1))
          .value = ex.IntCellValue(totalModerateSevere);
      sheet
          .cell(ex.CellIndex.indexByColumnRow(columnIndex: 4, rowIndex: 1))
          .value = ex.IntCellValue(totalSuccessfulShots);
      sheet
          .cell(ex.CellIndex.indexByColumnRow(columnIndex: 5, rowIndex: 1))
          .value = ex.IntCellValue(totalShots);

      for (int col = 0; col < 6; col++) {
        final cell = sheet.cell(
          ex.CellIndex.indexByColumnRow(columnIndex: col, rowIndex: 1),
        );
        cell.cellStyle = ex.CellStyle(
          bold: true,
          backgroundColorHex: ex.ExcelColor.lightBlue,
        );
      }

      // Data rows
      for (int i = 0; i < items.length; i++) {
        final r = items[i];
        final rowIndex = i + 2;
        sheet
            .cell(ex.CellIndex.indexByColumnRow(
                columnIndex: 0, rowIndex: rowIndex))
            .value = ex.TextCellValue(r.district);
        sheet
            .cell(ex.CellIndex.indexByColumnRow(
                columnIndex: 1, rowIndex: rowIndex))
            .value = ex.IntCellValue(r.patientCount);
        sheet
            .cell(ex.CellIndex.indexByColumnRow(
                columnIndex: 2, rowIndex: rowIndex))
            .value = ex.IntCellValue(r.abnormalPatientCount);
        sheet
            .cell(ex.CellIndex.indexByColumnRow(
                columnIndex: 3, rowIndex: rowIndex))
            .value = ex.IntCellValue(r.moderateSevereCount);
        sheet
            .cell(ex.CellIndex.indexByColumnRow(
                columnIndex: 4, rowIndex: rowIndex))
            .value = ex.IntCellValue(r.successfullShots);
        sheet
            .cell(ex.CellIndex.indexByColumnRow(
                columnIndex: 5, rowIndex: rowIndex))
            .value = ex.IntCellValue(r.totalShots);
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

      final fileName =
          'LiverScanningDistrictWiseData_${FormatterManager.formatDateToStringInDash(DateTime.now())}.xlsx';
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

class ModernHeaderRow extends StatelessWidget {
  const ModernHeaderRow({super.key});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 72.h,
      child: Row(
        children: [
          ModernHeadCell('District', flex: 1),
          ModernHeadCell('Patient Count', flex: 1),
          ModernHeadCell('Abnormal Patient', flex: 1),
          ModernHeadCell('Referral Warranted(Moderate-Severe)', flex: 1),
          ModernHeadCell('Successful Shots', flex: 1),
          ModernHeadCell('Total Shots', flex: 1),
        ],
      ),
    );
  }
}

class ModernHeadCell extends StatelessWidget {
  final String text;
  final int flex;

  const ModernHeadCell(this.text, {super.key, this.flex = 1});

  @override
  Widget build(BuildContext context) {
    return Expanded(
      flex: flex,
      child: Container(
        height: double.infinity,
        padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 4.h),
        alignment: Alignment.centerLeft,
        decoration: BoxDecoration(
          border: Border(
            right: BorderSide(color: Colors.white.withOpacity(0.3), width: 1),
          ),
        ),
        child: Text(
          text,
          maxLines: 4,
          textAlign: TextAlign.left,
          softWrap: true,
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.w600,
            fontSize: 12.sp,
            height: 1.15,
          ),
        ),
      ),
    );
  }
}

class ModernTotalRow extends StatelessWidget {
  final int totalPatientCount;
  final int totalAbnormalPatients;
  final int totalModerateSevere;
  final int totalSuccessfulShots;
  final int totalShots;

  const ModernTotalRow({
    super.key,
    required this.totalPatientCount,
    required this.totalAbnormalPatients,
    required this.totalModerateSevere,
    required this.totalSuccessfulShots,
    required this.totalShots,
  });

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: 40.h,
      child: Row(
        children: [
          ModernDataCell('Total', flex: 1, isBold: true, txtColor: kBlackColor),
          ModernDataCell(totalPatientCount.toString(),
              flex: 1, isBold: true, txtColor: kBlackColor),
          ModernDataCell(totalAbnormalPatients.toString(),
              flex: 1, isBold: true, txtColor: kBlackColor),
          ModernDataCell(totalModerateSevere.toString(),
              flex: 1, isBold: true, txtColor: kBlackColor),
          ModernDataCell(totalSuccessfulShots.toString(),
              flex: 1, isBold: true, txtColor: kBlackColor),
          ModernDataCell(totalShots.toString(),
              flex: 1, isBold: true, txtColor: kBlackColor),
        ],
      ),
    );
  }
}

class ModernDataRow extends StatelessWidget {
  final FibroscanDistrictData? item;
  final int index;

  const ModernDataRow({super.key, this.item, required this.index});

  @override
  Widget build(BuildContext context) {
    return Container(
      constraints: BoxConstraints(minHeight: 66.h),
      decoration: BoxDecoration(
        color: Colors.white,
        border: Border(
          bottom:
              BorderSide(color: Colors.grey.withOpacity(0.15), width: 1),
        ),
      ),
      child: Row(
        children: [
          Expanded(
            flex: 1,
            child: InkWell(
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => FibroScanningPatientDataDetails(
                      distlgdcode: item?.distLgdCode.toString() ?? '0',
                    ),
                  ),
                );
              },
              child: Container(
                padding:
                    EdgeInsets.symmetric(horizontal: 8.w, vertical: 8.h),
                alignment: Alignment.centerLeft,
                decoration: BoxDecoration(
                  border: Border(
                    right: BorderSide(
                        color: Colors.grey.withOpacity(0.1), width: 1),
                  ),
                ),
                child: Text(
                  item?.district ?? '-',
                  textAlign: TextAlign.left,
                  maxLines: 3,
                  softWrap: true,
                  style: TextStyle(
                    fontSize: 14.sp,
                    fontWeight: FontWeight.w400,
                    color: kHintColor,
                  ),
                ),
              ),
            ),
          ),
          ModernDataCell('${item?.patientCount ?? '-'}',
              flex: 1, txtColor: kBlackColor),
          ModernDataCell('${item?.abnormalPatientCount ?? '-'}',
              flex: 1, txtColor: kBlackColor),
          ModernDataCell('${item?.moderateSevereCount ?? '-'}',
              flex: 1, txtColor: kBlackColor),
          ModernDataCell('${item?.successfullShots ?? '-'}',
              flex: 1, txtColor: kBlackColor),
          ModernDataCell('${item?.totalShots ?? '-'}',
              flex: 1, txtColor: kBlackColor),
        ],
      ),
    );
  }
}

class ModernDataCell extends StatelessWidget {
  final String text;
  final int flex;
  final Color txtColor;
  final bool isBold;

  const ModernDataCell(
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
        padding: EdgeInsets.symmetric(horizontal: 8.w, vertical: 8.h),
        alignment: Alignment.centerLeft,
        decoration: BoxDecoration(
          border: Border(
            right:
                BorderSide(color: Colors.grey.withOpacity(0.1), width: 1),
          ),
        ),
        child: Text(
          text,
          textAlign: TextAlign.left,
          style: TextStyle(
            fontSize: 14.sp,
            fontWeight: isBold ? FontWeight.w600 : FontWeight.w400,
            color: txtColor,
          ),
        ),
      ),
    );
  }
}