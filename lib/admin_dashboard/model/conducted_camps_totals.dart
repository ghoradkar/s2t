// ignore_for_file: file_names

import 'dart:convert';

int _toInt(dynamic v) {
  if (v == null) return 0;
  if (v is int) return v;
  if (v is double) return v.toInt();
  return int.tryParse('$v') ?? 0;
}

DateTime? _parseDotNetDate(String? s) {
  if (s == null) return null;
  final m = RegExp(r'\/Date\((\d+)\)\/').firstMatch(s);
  if (m == null) return null;
  return DateTime.fromMillisecondsSinceEpoch(int.parse(m.group(1)!));
}

/// ---- API response root
class ConductedCampsResponse {
  final String status;
  final String message;
  final List<ConductedCampsRow> output;

  ConductedCampsResponse({
    required this.status,
    required this.message,
    required this.output,
  });

  factory ConductedCampsResponse.fromJson(Map<String, dynamic> json) {
    final list =
        (json['output'] as List? ?? [])
            .map((e) => ConductedCampsRow.fromJson(e as Map<String, dynamic>))
            .toList();
    return ConductedCampsResponse(
      status: json['status']?.toString() ?? '',
      message: json['message']?.toString() ?? '',
      output: list,
    );
  }

  static ConductedCampsResponse fromRaw(String body) =>
      ConductedCampsResponse.fromJson(jsonDecode(body) as Map<String, dynamic>);
}

/// ---- Each row
class ConductedCampsRow {
  final String financialYear; // e.g. "*FY(2025-2026)"
  final int subOrgId; // 2(HLL) / 3(HSCC)
  final String subOrgName;
  final int totalCamp; // "Total Camp"
  final int regularCamp; // "Regular Camp"
  final int d2dCamp; // "D2D Camp"
  final int totalBillableCount; // "TotalBillablecount"
  final DateTime? updatedAt; // "/Date(....)/"

  ConductedCampsRow({
    required this.financialYear,
    required this.subOrgId,
    required this.subOrgName,
    required this.totalCamp,
    required this.regularCamp,
    required this.d2dCamp,
    required this.totalBillableCount,
    required this.updatedAt,
  });

  factory ConductedCampsRow.fromJson(Map<String, dynamic> json) {
    return ConductedCampsRow(
      financialYear: json['FinancialYear']?.toString() ?? '',
      subOrgId: _toInt(json['SubOrgId']),
      subOrgName: json['SubOrgName']?.toString() ?? '',
      totalCamp: _toInt(json['Total Camp']),
      regularCamp: _toInt(json['Regular Camp']),
      d2dCamp: _toInt(json['D2D Camp']),
      totalBillableCount: _toInt(json['TotalBillablecount']),
      updatedAt: _parseDotNetDate(json['UpdatedTime']?.toString()),
    );
  }
}

/// ---- Aggregated totals across rows (HLL + HSCC)
// ConductedCampsTotals.dart
class ConductedCampsTotals {
  final String? financialYear;
  final int total;
  final int regular;
  final int d2d;
  final int billable;

  ConductedCampsTotals({
    required this.financialYear,
    required this.total,
    required this.regular,
    required this.d2d,
    required this.billable,
  });

  static ConductedCampsTotals fromRows(List<ConductedCampsRow> rows) {
    int total = 0, regular = 0, d2d = 0, billable = 0;
    String? fy = rows.isNotEmpty ? rows.first.financialYear : null;

    for (final r in rows) {
      total += (r.totalCamp);
      regular += (r.regularCamp);
      d2d += (r.d2dCamp);
      billable += (r.totalBillableCount);
    }

    return ConductedCampsTotals(
      financialYear: fy,
      total: total,
      regular: regular,
      d2d: d2d,
      billable: billable,
    );
  }
}
