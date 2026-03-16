import 'dart:convert';

class HomeLabPendingCountTableModel {
  final String status;
  final String message;
  final List<PendingCountItem> output;

  HomeLabPendingCountTableModel({
    required this.status,
    required this.message,
    required this.output,
  });

  factory HomeLabPendingCountTableModel.fromJson(Map<String, dynamic> json) {
    return HomeLabPendingCountTableModel(
      status: json['status'] ?? '',
      message: json['message'] ?? '',
      output: (json['output'] as List<dynamic>? ?? [])
          .map((e) => PendingCountItem.fromJson(e as Map<String, dynamic>))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() => {
    'status': status,
    'message': message,
    'output': output.map((e) => e.toJson()).toList(),
  };

  /// Convenience: compute totals across all divisions
  PendingTotals get totals => PendingTotals.fromItems(output);

  /// Quick decode helper
  static HomeLabPendingCountTableModel fromJsonString(String source) =>
      HomeLabPendingCountTableModel.fromJson(json.decode(source) as Map<String, dynamic>);
}

class PendingCountItem {
  final int divId;
  final String divName;
  final int homeLabProcessingDelayed;
  final int hubLabProcessingDelayed;
  final int doctorScreeningDelayed;

  PendingCountItem({
    required this.divId,
    required this.divName,
    required this.homeLabProcessingDelayed,
    required this.hubLabProcessingDelayed,
    required this.doctorScreeningDelayed,
  });

  factory PendingCountItem.fromJson(Map<String, dynamic> json) {
    int _toInt(dynamic v) => (v is num) ? v.toInt() : int.tryParse('$v') ?? 0;

    return PendingCountItem(
      divId: _toInt(json['DIVID']),
      divName: json['DIVNAME']?.toString() ?? '',
      homeLabProcessingDelayed: _toInt(json['HomeLabProcessingDelayed']),
      hubLabProcessingDelayed: _toInt(json['HubLabProcessingDelayed']),
      doctorScreeningDelayed: _toInt(json['DoctorScreeningDelayed']),
    );
  }

  Map<String, dynamic> toJson() => {
    'DIVID': divId,
    'DIVNAME': divName,
    'HomeLabProcessingDelayed': homeLabProcessingDelayed,
    'HubLabProcessingDelayed': hubLabProcessingDelayed,
    'DoctorScreeningDelayed': doctorScreeningDelayed,
  };
}

class PendingTotals {
  final int homeLabTotal;
  final int hubLabTotal;
  final int doctorScreeningTotal;

  const PendingTotals({
    required this.homeLabTotal,
    required this.hubLabTotal,
    required this.doctorScreeningTotal,
  });

  factory PendingTotals.fromItems(List<PendingCountItem> items) {
    int sumHome = 0, sumHub = 0, sumDoc = 0;
    for (final i in items) {
      sumHome += i.homeLabProcessingDelayed;
      sumHub += i.hubLabProcessingDelayed;
      sumDoc += i.doctorScreeningDelayed;
    }
    return PendingTotals(
      homeLabTotal: sumHome,
      hubLabTotal: sumHub,
      doctorScreeningTotal: sumDoc,
    );
  }
}
