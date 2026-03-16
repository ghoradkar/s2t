// ignore_for_file: use_build_context_synchronously, file_names, must_be_immutable

import 'package:device_info_plus/device_info_plus.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/SizeConfig.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:excel/excel.dart' as ex;
import 'dart:io';
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:open_file/open_file.dart';
import 'package:s2toperational/Screens/AdminDashboard/Screens/PendingCountScreen/DataRow/PendingCountDataRow.dart';

import '../../../../Modules/ToastManager/ToastManager.dart';
import '../../Model/HomeLabPendingCountTableModel.dart';

class PendingCountScreen extends StatefulWidget {
  PendingCountScreen({
    super.key,
    required this.monthId,
    required this.year,
    required this.subOrgId,
    required this.dIVID,
    required this.distcode,
    required this.userId,
    required this.dESGID,
    required this.campType,
  });

  String monthId = "";
  String year = "";
  String subOrgId = "";
  String dIVID = "";
  String distcode = "";
  String userId = "";
  String dESGID = "";
  String campType = "";

  @override
  State<PendingCountScreen> createState() => _PendingCountScreenState();
}

class _PendingCountScreenState extends State<PendingCountScreen> {
  APIManager apiManager = APIManager();
  List<PendingCountItem> list = [];

  @override
  void initState() {
    super.initState();
    ToastManager.showLoader();
    sampleProcessingDashboardCount();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: 'Pending Count',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
        showActions: true,
        actions: [
          Padding(
            padding: EdgeInsets.only(right: 14.w),
            child: InkWell(
              onTap: () {
                if (list.isNotEmpty) {
                  exportToExcel(context);
                }
              },
              child: Icon(
                Icons.save_alt_sharp,
                color: kWhiteColor,
                size: 24.sp,
              ),
            ),
          ),
        ],
      ),
      body: AnnotatedRegion(
        value: const SystemUiOverlayStyle(
          statusBarColor: kPrimaryColor,
          statusBarBrightness: Brightness.light,
          statusBarIconBrightness: Brightness.light,
        ),
        child: Container(
          color: Colors.white,
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Container(
            color: Colors.transparent,
            width: SizeConfig.screenWidth,
            child: Column(
              children: [
                SizedBox(height: 12.h),
                HeaderRow(),
                SizedBox(height: 6.h),
                list.isNotEmpty
                    ? Expanded(
                  child: ListView.builder(
                    itemCount: list.length + 1, // +1 for total row
                    itemBuilder: (context, index) {
                      if (index == 0) {
                        // Show total row at first position
                        return TotalRow(list: list);
                      }
                      // Adjust index for actual data (subtract 1 because total is at 0)
                      PendingCountItem pendingCountItem = list[index - 1];
                      return PendingCountDataRow(
                        item: pendingCountItem,
                        index: index - 1,
                      );
                    },
                  ),
                )
                // Expanded(
                //       child: ListView.builder(
                //         itemCount: list.length,
                //         itemBuilder: (context, index) {
                //           PendingCountItem pendingCountItem = list[index];
                //           return PendingCountDataRow(
                //             item: pendingCountItem,
                //             index: index,
                //           );
                //         },
                //       ),
                //     )
                    : Expanded(child: emptyState()),
                SizedBox(height: 12.h),
              ],
            ).paddingSymmetric(horizontal: 6.w),
          ),
        ),
      ),
    );
  }

  void sampleProcessingDashboardCount() {
    Map<String, String> params = {
      "MonthId": widget.monthId,
      "Year": widget.year,
      "SubOrgId": widget.subOrgId,
      "DIVID": widget.dIVID,
      "Distcode": widget.distcode,
      "UserId": widget.userId,
      "DESGID": widget.dESGID,
      "CampType": widget.campType,
    };

    apiManager.getHomeLabPendingTableData(params, apiGetHomeHubProcessedTable);
  }

  void apiGetHomeHubProcessedTable(HomeLabPendingCountTableModel? response,
      String errorMessage,
      bool success,) async {
    ToastManager.hideLoader();
    if (success) {
      list = response?.output ?? [];
    } else {
      if (errorMessage.isNotEmpty) {
        ToastManager.toast(errorMessage);
      }
    }
    setState(() {});
  }



  Future<void> exportToExcel(BuildContext context) async {
    try {
      // For Android 13+ (API 33+), we don't need storage permission for Downloads
      // For Android 10-12 (API 29-32), we need manageExternalStorage
      // For Android 9 and below (API 28-), we need storage permission

      if (Platform.isAndroid) {
        final androidInfo = await DeviceInfoPlugin().androidInfo;

        if (androidInfo.version.sdkInt >= 33) {
          // Android 13+ - No permission needed for Downloads
          // Continue without permission check
        } else if (androidInfo.version.sdkInt >= 30) {
          // Android 11-12 - Need manageExternalStorage
          var status = await Permission.manageExternalStorage.request();
          if (!status.isGranted) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text('Storage permission is required to save file'),
              ),
            );
            return;
          }
        } else {
          // Android 10 and below - Need storage permission
          var status = await Permission.storage.request();
          if (!status.isGranted) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text('Storage permission is required to save file'),
              ),
            );
            return;
          }
        }
      }

      // Create Excel file
      var excel = ex.Excel.createExcel();
      ex.Sheet sheetObject = excel['Pending Count Data'];
      excel.delete('Sheet1');

      // Add headers
      sheetObject
          .cell(ex.CellIndex.indexByString("A1"))
          .value =  ex.TextCellValue("Division Name");
      sheetObject
          .cell(ex.CellIndex.indexByString("B1"))
          .value =  ex.TextCellValue("Hub Lab Processing Delayed");
      sheetObject
          .cell(ex.CellIndex.indexByString("C1"))
          .value =  ex.TextCellValue("Home Processing Delayed");
      sheetObject
          .cell(ex.CellIndex.indexByString("D1"))
          .value =  ex.TextCellValue("Dr. Screening Completion Delayed");

      // Style headers
      for (int col = 0; col < 4; col++) {
        var cell = sheetObject.cell(
          ex.CellIndex.indexByColumnRow(columnIndex: col, rowIndex: 0),
        );
        cell.cellStyle = ex.CellStyle(
          bold: true,
          backgroundColorHex: ex.ExcelColor.green,
          fontColorHex: ex.ExcelColor.white,
        );
      }

      // Add Total Row
      int totalHub = 0;
      int totalHome = 0;
      int totalDr = 0;

      for (var item in list) {
        totalHub += item.hubLabProcessingDelayed;
        totalHome += item.homeLabProcessingDelayed;
        totalDr += item.doctorScreeningDelayed;
      }

      sheetObject.cell(ex.CellIndex.indexByColumnRow(columnIndex: 0, rowIndex: 1))
          .value =  ex.TextCellValue("Total");
      sheetObject.cell(ex.CellIndex.indexByColumnRow(columnIndex: 1, rowIndex: 1))
          .value = ex.IntCellValue(totalHub);
      sheetObject.cell(ex.CellIndex.indexByColumnRow(columnIndex: 2, rowIndex: 1))
          .value = ex.IntCellValue(totalHome);
      sheetObject.cell(ex.CellIndex.indexByColumnRow(columnIndex: 3, rowIndex: 1))
          .value = ex.IntCellValue(totalDr);

      // Style total row
      for (int col = 0; col < 4; col++) {
        var cell = sheetObject.cell(
          ex.CellIndex.indexByColumnRow(columnIndex: col, rowIndex: 1),
        );
        cell.cellStyle = ex.CellStyle(
          bold: true,
          backgroundColorHex: ex.ExcelColor.blue,
          fontColorHex: ex.ExcelColor.white,
        );
      }

      // Add data rows (starting from row 3, index 2)
      for (int i = 0; i < list.length; i++) {
        int rowIndex = i + 2; // +2 because row 0 is header, row 1 is total
        PendingCountItem items = list[i];
        sheetObject
            .cell(
          ex.CellIndex.indexByColumnRow(columnIndex: 0, rowIndex: rowIndex),
        )
            .value = ex.TextCellValue(items.divName);
        sheetObject
            .cell(
          ex.CellIndex.indexByColumnRow(columnIndex: 1, rowIndex: rowIndex),
        )
            .value = ex.IntCellValue(items.hubLabProcessingDelayed);
        sheetObject
            .cell(
          ex.CellIndex.indexByColumnRow(columnIndex: 2, rowIndex: rowIndex),
        )
            .value = ex.IntCellValue(items.homeLabProcessingDelayed);
        sheetObject
            .cell(
          ex.CellIndex.indexByColumnRow(columnIndex: 3, rowIndex: rowIndex),
        )
            .value = ex.IntCellValue(items.doctorScreeningDelayed);
      }

      // Get directory to save file
      Directory? directory;
      if (Platform.isAndroid) {
        // Try to use Downloads directory
        directory = Directory('/storage/emulated/0/Download');

        // If Downloads doesn't exist, try other options
        if (!await directory.exists()) {
          directory = Directory('/storage/emulated/0/Downloads');
        }

        if (!await directory.exists()) {
          directory = await getExternalStorageDirectory();
        }
      } else {
        directory = await getApplicationDocumentsDirectory();
      }

      if (directory != null) {
        final now = DateTime.now();
        final dateStr = '${now.day.toString().padLeft(2, '0')}-${now.month.toString().padLeft(2, '0')}-${now.year}';
        String fileName = 'PendingCountData_$dateStr.xlsx';
        String filePath = '${directory.path}/$fileName';

        // Save file
        List<int>? fileBytes = excel.save();
        if (fileBytes != null) {
          File(filePath)
            ..createSync(recursive: true)
            ..writeAsBytesSync(fileBytes);

          // Show success message
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text('Excel file saved to: $filePath'),
              duration: const Duration(seconds: 4),
              action: SnackBarAction(
                label: 'Open',
                onPressed: () => OpenFile.open(filePath),
              ),
            ),
          );
        }
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error saving file: $e')),
      );
    }
  }

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
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.center,
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
          const SizedBox(height: 20),
          CommonText(
            text: 'No data found',
            fontSize: 18.sp,
            fontWeight: FontWeight.w600,
            textColor: kTextBlackColor,
            textAlign: TextAlign.center,
          ),
        ],
      ),
    );
  }
}

class HeaderRow extends StatelessWidget {
  const HeaderRow({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.symmetric(horizontal: 8.w),
      decoration: BoxDecoration(
        color: kPrimaryColor,
        borderRadius: BorderRadius.circular(8.r),
      ),
      child: Row(
        children: [
          _buildHeaderCell('Division Name', flex: 2),
          _buildHeaderCell('HubLab Processing Delayed', flex: 2),
          _buildHeaderCell('Home Processing Delayed', flex: 2),
          _buildHeaderCell('Dr.Screening completion Delayed', flex: 2),
        ],
      ),
    );
  }

  Widget _buildHeaderCell(String title, {int flex = 1}) {
    return Expanded(
      flex: flex,
      child: Container(
        padding: EdgeInsets.symmetric(vertical: 12.h, horizontal: 6.w),
        child: Text(
          title,
          textAlign: TextAlign.center,
          maxLines: 3,
          style: TextStyle(
            color: Colors.white,
            fontSize: 12.sp,
            fontWeight: FontWeight.w600,
            height: 1.2,
          ),
        ),
      ),
    );
  }
}
