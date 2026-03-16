// ignore_for_file: file_names

import 'dart:convert';

class TodaysPatientsResponse {
  final String status;
  final String message;
  final List<TodaysPatientsRow> output;

  TodaysPatientsResponse({
    required this.status,
    required this.message,
    required this.output,
  });

  factory TodaysPatientsResponse.fromJson(Map<String, dynamic> json) {
    final list =
        (json['output'] as List? ?? [])
            .map((e) => TodaysPatientsRow.fromJson(e as Map<String, dynamic>))
            .toList();
    return TodaysPatientsResponse(
      status: json['status']?.toString() ?? '',
      message: json['message']?.toString() ?? '',
      output: list,
    );
  }

  static TodaysPatientsResponse fromRaw(String body) =>
      TodaysPatientsResponse.fromJson(jsonDecode(body) as Map<String, dynamic>);
}

class TodaysPatientsRow {
  final int subOrgId;
  final String subOrgName;
  final int total; // Total
  final int regularCamp; // RegularCamp
  final int d2dCamp; // D2DCamp
  // extra fields present but not needed for the 3 metrics:
  final int regular;
  final int cscRegular;
  final int d2d;
  final int cscD2d;
  final int flexi;
  final int mmuCamp;

  TodaysPatientsRow({
    required this.subOrgId,
    required this.subOrgName,
    required this.total,
    required this.regularCamp,
    required this.d2dCamp,
    required this.regular,
    required this.cscRegular,
    required this.d2d,
    required this.cscD2d,
    required this.flexi,
    required this.mmuCamp,
  });

  factory TodaysPatientsRow.fromJson(Map<String, dynamic> json) {
    return TodaysPatientsRow(
      subOrgId: _toInt(json['SubOrgId']),
      subOrgName: json['SubOrgName']?.toString() ?? '',
      total: _toInt(json['Total']),
      regularCamp: _toInt(json['RegularCamp']),
      d2dCamp: _toInt(json['D2DCamp']),
      regular: _toInt(json['Regular']),
      cscRegular: _toInt(json['CSCRegular']),
      d2d: _toInt(json['D2D']),
      cscD2d: _toInt(json['CSCD2D']),
      flexi: _toInt(json['Flexi']),
      mmuCamp: _toInt(json['MMUCamp']),
    );
  }
}

// TodaysPatientsTotals.dart
class TodaysPatientsTotals {
  final int total;
  final int regular;
  final int d2d;

  TodaysPatientsTotals({
    required this.total,
    required this.regular,
    required this.d2d,
  });

  /// Native logic mirror:
  /// - Use server "Total" if present; fall back to recompute.
  /// - Regular = (Regular or RegularCamp) + CSCRegular
  /// - D2D     = (D2D or D2DCamp)     + CSCD2D
  /// - If Total missing, Total = Regular + D2D (+ Flexi + MMUCamp if native included those)
  static TodaysPatientsTotals fromRows(List<TodaysPatientsRow> rows) {
    int total = 0, regular = 0, d2d = 0, flexi = 0, mmu = 0;

    for (final r in rows) {
      final regBase = (r.regular);
      final d2dBase = (r.d2d);

      regular += regBase + (r.cscRegular);
      d2d += d2dBase + (r.cscD2d);

      // prefer API "Total" when provided
      total += (r.total);

      // keep these in case native added them into total
      flexi += (r.flexi);
      mmu += (r.mmuCamp);
    }

    // If API "Total" is missing/zero but we have components, recompute:
    if (total == 0 && (regular > 0 || d2d > 0 || flexi > 0 || mmu > 0)) {
      total = regular + d2d + flexi + mmu;
    }

    return TodaysPatientsTotals(total: total, regular: regular, d2d: d2d);
  }
}

int _toInt(dynamic v) {
  if (v == null) return 0;
  if (v is int) return v;
  if (v is double) return v.toInt();
  return int.tryParse('$v') ?? 0;
}
