// ignore_for_file: file_names
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:open_file/open_file.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/S2TAppBar.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Screens/AdminDashboard/Controller/AdminController.dart';
import 'package:s2toperational/Screens/AdminDashboard/Model/S2TAndroidIosCountDistrictWiseModel.dart';
import 'package:s2toperational/Screens/AdminDashboard/Screens/FibroScanningPatientDataDetails.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_internet_widget.dart';
import 'dart:io';
import 'package:excel/excel.dart' as ex;
import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';

class AndroidAndIosPatientCountScreen extends StatefulWidget {
  final String title;
  final String? total;

  const AndroidAndIosPatientCountScreen({
    super.key,
    required this.title,
    this.total,
  });

  @override
  State<AndroidAndIosPatientCountScreen> createState() =>
      AndroidAndIosPatientCountScreenState();
}

class AndroidAndIosPatientCountScreenState
    extends State<AndroidAndIosPatientCountScreen> {
  final AdminController adminController = Get.put(AdminController());

  @override
  void initState() {
    adminController.checkInternetS2TAppDistrictWise();
    super.initState();
  }

  bool get _isAndroid =>
      widget.title.toLowerCase().contains('android'); // title driven
  // bool get _isIos => widget.title.toLowerCase().contains('ios'); // title driven

  int _pickCount(PatientDistrictCount r) => _isAndroid ? r.android : r.iosCount;

  @override
  @override
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: widget.title,
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
        showActions: true,
        actions: [
          InkWell(
            onTap: () async {
              await adminController.getS2tAndroidIosDistrictWiseList(
                showLoader: false,
              );
            },
            child: const Icon(Icons.refresh, color: kWhiteColor, size: 26),
          ),
          Padding(
            padding: const EdgeInsets.only(left: 6, right: 10),
            child: InkWell(
              onTap: () => _exportToExcel(context),
              child: const Icon(
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
          final details =
              controller.s2tAndroidIosCountDistrictWiseModel?.details;
          final list = (details?.count ?? []).toList();

          // Remove ONLY the TOTAL row since it's already shown at the top
          list.removeWhere((e) => e.district.trim().toUpperCase() == 'TOTAL');

          // Optional: sort for stable order
          list.sort((a, b) => a.district.compareTo(b.district));

          return controller.hasInternet
              ? AnnotatedRegion(
                value: const SystemUiOverlayStyle(
                  statusBarColor: kPrimaryColor,
                  statusBarBrightness: Brightness.light,
                  statusBarIconBrightness: Brightness.light,
                ),
                child: Column(
                  children: [
                    const ModernHeaderRow(),
                    ModernTotalRow(total: widget.total ?? ''),
                    Divider(
                      height: 0,
                      thickness: 1,
                      color: Colors.grey.withOpacity(0.2),
                    ),
                    Expanded(
                      child:
                          controller.isS2tAppDistrictLoading
                              ? const CommonSkeletonInvoiceTable(itemCount: 20,)
                              : list.isEmpty
                              ? const Center(child: Text('No data available'))
                              : ListView.builder(
                                shrinkWrap: true,
                                padding: EdgeInsets.zero,
                                physics: const AlwaysScrollableScrollPhysics(),
                                itemCount: list.length,
                                itemBuilder: (context, i) {
                                  final r = list[i];
                                  final districtDisplay =
                                      r.district.isEmpty ? '-' : r.district;

                                  return ModernDataRow(
                                    srNo: i + 1,
                                    district: districtDisplay,
                                    count: _pickCount(r),
                                    onTap: () {
                                      // Only navigate for valid districts
                                      if (r.district.trim().isNotEmpty &&
                                          r.district != '-' &&
                                          r.district.toUpperCase() !=
                                              'UNKNOWN') {
                                        Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                            builder:
                                                (_) =>
                                                    FibroScanningPatientDataDetails(
                                                      distlgdcode: r.district,
                                                    ),
                                          ),
                                        );
                                      }
                                    },
                                  );
                                },
                              ),
                    ),
                  ],
                ).paddingSymmetric(vertical: 10, horizontal: 12),
              )
              : NoInternetWidget(
                onRetryPressed: () {
                  return controller.checkInternetS2TAppDistrictWise();
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
          if (context.mounted) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text('Storage permission is required to save file'),
              ),
            );
          }
          return;
        }
      }

      final excel = ex.Excel.createExcel();
      ex.Sheet sheet;
      if (excel.sheets.containsKey('Sheet1')) {
        excel.rename('Sheet1', 'AndroidIosCount');
        sheet = excel['AndroidIosCount'];
      } else {
        sheet = excel['AndroidIosCount'];
      }

      // headers
      sheet.cell(ex.CellIndex.indexByString("A1")).value = ex.TextCellValue(
        "Sr. No.",
      );
      sheet.cell(ex.CellIndex.indexByString("B1")).value = ex.TextCellValue(
        "District",
      );
      sheet.cell(ex.CellIndex.indexByString("C1")).value = ex.TextCellValue(
        "Count of Registered No.",
      );

      // style headers
      for (int col = 0; col < 3; col++) {
        final cell = sheet.cell(
          ex.CellIndex.indexByColumnRow(columnIndex: col, rowIndex: 0),
        );
        cell.cellStyle = ex.CellStyle(
          bold: true,
          backgroundColorHex: ex.ExcelColor.blue,
          fontColorHex: ex.ExcelColor.white,
        );
      }

      final details =
          adminController.s2tAndroidIosCountDistrictWiseModel?.details;
      final raw = (details?.count ?? []).toList();

      // DON'T remove anything for Excel - keep ALL data including TOTAL

      for (int i = 0; i < raw.length; i++) {
        final r = raw[i];
        final rowIndex = i + 1;
        final districtDisplay = r.district.isEmpty ? '-' : r.district;

        sheet
            .cell(
              ex.CellIndex.indexByColumnRow(columnIndex: 0, rowIndex: rowIndex),
            )
            .value = ex.IntCellValue(i + 1);
        sheet
            .cell(
              ex.CellIndex.indexByColumnRow(columnIndex: 1, rowIndex: rowIndex),
            )
            .value = ex.TextCellValue(districtDisplay);
        sheet
            .cell(
              ex.CellIndex.indexByColumnRow(columnIndex: 2, rowIndex: rowIndex),
            )
            .value = ex.IntCellValue(_pickCount(r));

        // Make TOTAL row bold and highlighted in Excel
        if (r.district.trim().toUpperCase() == 'TOTAL') {
          for (int col = 0; col < 3; col++) {
            final cell = sheet.cell(
              ex.CellIndex.indexByColumnRow(
                columnIndex: col,
                rowIndex: rowIndex,
              ),
            );
            cell.cellStyle = ex.CellStyle(
              bold: true,
              backgroundColorHex: ex.ExcelColor.white,
            );
          }
        }
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

      final fileName =
          'AndroidIosPatientCount_${DateTime.now().millisecondsSinceEpoch}.xlsx';
      final filePath = '${directory!.path}/$fileName';
      final bytes = excel.save();
      if (bytes == null) throw Exception('Failed to create Excel bytes');

      final outFile =
          File(filePath)
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
        ScaffoldMessenger.of(
          context,
        ).showSnackBar(SnackBar(content: Text('Error exporting Excel: $e')));
      }
    }
  }
}

class ModernHeaderRow extends StatelessWidget {
  const ModernHeaderRow({super.key});

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
          ModernHeadCell('Sr. No.', flex: 1, alignment: Alignment.center),
          ModernHeadCell('District', flex: 3, alignment: Alignment.centerLeft),
          ModernHeadCell(
            'Count of Registered No.',
            flex: 3,
            alignment: Alignment.center,
          ),
        ],
      ),
    );
  }
}

class ModernHeadCell extends StatelessWidget {
  final String text;
  final int flex;
  final Alignment alignment;

  const ModernHeadCell(
    this.text, {
    super.key,
    this.flex = 1,
    this.alignment = Alignment.center,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      flex: flex,
      child: Container(
        height: double.infinity,
        padding: EdgeInsets.symmetric(horizontal: 12.w),
        alignment: alignment,
        decoration: BoxDecoration(
          border: Border(
            right: BorderSide(color: Colors.white.withOpacity(0.3), width: 1),
          ),
        ),
        child: Text(
          text,
          maxLines: 2,
          textAlign:
              alignment == Alignment.center ? TextAlign.center : TextAlign.left,
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.w600,
            fontSize: 12.sp,
            height: 1.2,
          ),
        ),
      ),
    );
  }
}

// Modern Total Row
class ModernTotalRow extends StatelessWidget {
  final String total;

  const ModernTotalRow({super.key, required this.total});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 48.h,
      decoration: BoxDecoration(color: kTextColor.withOpacity(0.1)),
      child: Row(
        children: [
          ModernCell('', flex: 1, alignment: Alignment.center, isBold: true),
          ModernCell(
            'Total',
            flex: 3,
            alignment: Alignment.centerLeft,
            isBold: true,
          ),
          ModernCell(total, flex: 3, alignment: Alignment.center, isBold: true),
        ],
      ),
    );
  }
}

// Modern Data Row
class ModernDataRow extends StatelessWidget {
  final int srNo;
  final String district;
  final int count;
  final VoidCallback onTap;

  const ModernDataRow({
    super.key,
    required this.srNo,
    required this.district,
    required this.count,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      // onTap: onTap,
      child: Container(
        height: 48.h,
        decoration: BoxDecoration(
          color: Colors.white,
          border: Border(
            bottom: BorderSide(color: Colors.grey.withOpacity(0.15), width: 1),
          ),
        ),
        child: Row(
          children: [
            ModernCell(
              '$srNo',
              flex: 1,
              alignment: Alignment.center,
              txtColor: kBlackColor,
            ),
            ModernCell(
              district,
              flex: 3,
              alignment: Alignment.centerLeft,
              txtColor: kBlackColor,
            ),
            ModernCell(
              '$count',
              flex: 3,
              alignment: Alignment.center,
              txtColor: kBlackColor,
            ),
          ],
        ),
      ),
    );
  }
}

// Modern Cell
class ModernCell extends StatelessWidget {
  final String text;
  final int flex;
  final Alignment alignment;
  final Color? txtColor;
  final bool isBold;

  const ModernCell(
    this.text, {
    super.key,
    this.flex = 1,
    this.alignment = Alignment.center,
    this.txtColor,
    this.isBold = false,
  });

  @override
  Widget build(BuildContext context) {
    return Expanded(
      flex: flex,
      child: Container(
        height: double.infinity,
        padding: EdgeInsets.symmetric(horizontal: 12.w),
        alignment: alignment,
        decoration: BoxDecoration(
          border: Border(
            right: BorderSide(color: Colors.grey.withOpacity(0.1), width: 1),
          ),
        ),
        child: Text(
          text,
          textAlign:
              alignment == Alignment.center
                  ? TextAlign.center
                  : (alignment == Alignment.centerRight
                      ? TextAlign.right
                      : TextAlign.left),
          style: TextStyle(
            fontSize: 12.sp,
            fontWeight: isBold ? FontWeight.w600 : FontWeight.w400,
            color: txtColor ?? kBlackColor,
          ),
        ),
      ),
    );
  }
}

// class _HeaderRow extends StatelessWidget {
//   const _HeaderRow();
//
//   @override
//   Widget build(BuildContext context) {
//     return Container(
//       height: 50.h,
//       decoration: BoxDecoration(
//         color: kPrimaryColor,
//         border: Border.all(color: offWhite),
//       ),
//       child: Row(
//         children: const [
//           _HeadCell('Sr. No.', flex: 1, center: true),
//           _HeadCell('District', flex: 2),
//           _HeadCell('Count of Registered No.', flex: 2, right: true),
//         ],
//       ),
//     );
//   }
// }
//
// class _HeadCell extends StatelessWidget {
//   final String text;
//   final int flex;
//   final bool right;
//   final bool center;
//
//   const _HeadCell(
//     this.text, {
//     this.flex = 1,
//     this.right = false,
//     this.center = false,
//   });
//
//   @override
//   Widget build(BuildContext context) {
//     Alignment align = Alignment.centerLeft;
//     if (center) align = Alignment.center;
//     if (right) align = Alignment.centerRight;
//
//     return Expanded(
//       flex: flex,
//       child: Container(
//         padding: EdgeInsets.symmetric(horizontal: 12.w),
//         alignment: align,
//         child: Text(
//           text,
//           maxLines: 2,
//           style: TextStyle(
//             color: Colors.white,
//             fontWeight: FontWeight.w700,
//             fontSize: 12.sp,
//           ),
//         ),
//       ),
//     );
//   }
// }
//
// class _TotalRow extends StatelessWidget {
//   final String total;
//
//   const _TotalRow({required this.total});
//
//   @override
//   Widget build(BuildContext context) {
//     return Container(
//       height: 50.h,
//       decoration: BoxDecoration(
//         color: Colors.white,
//         border: Border(
//           bottom: BorderSide(
//             color: Colors.grey.withValues(alpha: 0.15),
//             width: .5,
//           ),
//         ),
//       ),
//       child: Row(
//         children: [
//           _Cell('', flex: 1, center: true, emph: true),
//           _Cell('Total', flex: 2, emph: true),
//           _Cell(total, flex: 2, right: true, emph: true),
//         ],
//       ),
//     );
//   }
// }
//
// class _DataRow extends StatelessWidget {
//   final int srNo;
//   final String district;
//   final int count;
//   final VoidCallback onTap;
//
//   const _DataRow({
//     required this.srNo,
//     required this.district,
//     required this.count,
//     required this.onTap,
//   });
//
//   @override
//   Widget build(BuildContext context) {
//     return InkWell(
//       onTap: onTap,
//       child: Container(
//         height: 50.h,
//         decoration: BoxDecoration(
//           color: Colors.white,
//           border: Border(
//             bottom: BorderSide(
//               color: Colors.grey.withValues(alpha: 0.15),
//               width: .5,
//             ),
//           ),
//         ),
//         child: Row(
//           children: [
//             _Cell('$srNo', flex: 1, center: true, faint: true),
//             _Cell(district, flex: 2),
//             _Cell('$count', flex: 2, right: true),
//           ],
//         ),
//       ),
//     );
//   }
// }
//
// class _Cell extends StatelessWidget {
//   final String text;
//   final int flex;
//   final bool right;
//   final bool center;
//   final bool emph;
//   final bool faint;
//
//   const _Cell(
//     this.text, {
//     this.flex = 1,
//     this.right = false,
//     this.center = false,
//     this.emph = false,
//     this.faint = false,
//   });
//
//   @override
//   Widget build(BuildContext context) {
//     Alignment align = Alignment.centerLeft;
//     if (center) align = Alignment.center;
//     if (right) align = Alignment.centerRight;
//
//     return Expanded(
//       flex: flex,
//       child: Container(
//         padding: EdgeInsets.symmetric(horizontal: 12.w),
//         alignment: align,
//         child: CommonText(
//           text: text,
//           fontSize: 12.sp,
//           fontWeight: emph ? FontWeight.w700 : FontWeight.w400,
//           textColor: faint ? kHintColor : kBlackColor,
//           textAlign:
//               right
//                   ? TextAlign.right
//                   : (center ? TextAlign.center : TextAlign.left),
//         ),
//       ),
//     );
//   }
// }
