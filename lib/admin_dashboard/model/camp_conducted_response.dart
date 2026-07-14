import 'dart:convert';

/// Top-level response
class CampsConductedResponse {
  final String status;
  final String message;
  final List<CampRecord> output;

  const CampsConductedResponse({
    required this.status,
    required this.message,
    required this.output,
  });

  /// Parse from a JSON string
  factory CampsConductedResponse.fromJson(String source) =>
      CampsConductedResponse.fromMap(json.decode(source) as Map<String, dynamic>);

  /// Parse from a decoded map
  factory CampsConductedResponse.fromMap(Map<String, dynamic> map) {
    final rawList = map['output'];
    return CampsConductedResponse(
      status: _s(map['status']),
      message: _s(map['message']),
      output: (rawList is List ? rawList : const <dynamic>[])
          .map((e) => CampRecord.fromMap(e as Map<String, dynamic>))
          .toList(growable: false),
    );
  }

  Map<String, dynamic> toMap() => {
    'status': status,
    'message': message,
    'output': output.map((e) => e.toMap()).toList(growable: false),
  };

  String toJson() => json.encode(toMap());
}

/// One row in the table
class CampRecord {
  final int campId;                    // CampId
  final String campTypeDescription;    // CampTypeDescription
  final String campNo;                 // CampNo
  final String campLocation;           // CampLocation
  final String campDate;               // CampDate (yyyy-MM-dd)
  final String campStatus;             // CampStatus
  final String status;                 // Status (P/D/E/W/R...)
  final String description;            // Description
  final int distLgdCode;               // DISTLGDCODE
  final String distName;               // DISTNAME
  final int registerWorkers;           // REGISTERWORKERS
  final String surveyCoordinatorName;  // SurveyCoordinatorName
  final String cordinatorName;         // CordinatorName
  final String mobNo;                  // MOBNO
  final String campName;               // CampName
  final int total;                     // Total
  final int screeningDone;             // ScreeningDone
  final int screeningNotDone;          // ScreeningNotDone

  const CampRecord({
    required this.campId,
    required this.campTypeDescription,
    required this.campNo,
    required this.campLocation,
    required this.campDate,
    required this.campStatus,
    required this.status,
    required this.description,
    required this.distLgdCode,
    required this.distName,
    required this.registerWorkers,
    required this.surveyCoordinatorName,
    required this.cordinatorName,
    required this.mobNo,
    required this.campName,
    required this.total,
    required this.screeningDone,
    required this.screeningNotDone,
  });

  /// Convenient parsed DateTime (null if invalid)
  DateTime? get campDateParsed {
    try {
      return DateTime.parse(campDate);
    } catch (_) {
      return null;
    }
  }

  factory CampRecord.fromJson(String source) =>
      CampRecord.fromMap(json.decode(source) as Map<String, dynamic>);

  factory CampRecord.fromMap(Map<String, dynamic> map) => CampRecord(
    campId: _i(map['CampId']),
    campTypeDescription: _s(map['CampTypeDescription']),
    campNo: _s(map['CampNo']),
    campLocation: _s(map['CampLocation']),
    campDate: _s(map['CampDate']),
    campStatus: _s(map['CampStatus']),
    status: _s(map['Status']),
    description: _s(map['Description']),
    distLgdCode: _i(map['DISTLGDCODE']),
    distName: _s(map['DISTNAME']),
    registerWorkers: _i(map['REGISTERWORKERS']),
    surveyCoordinatorName: _s(map['SurveyCoordinatorName']),
    cordinatorName: _s(map['CordinatorName']),
    mobNo: _s(map['MOBNO']),
    campName: _s(map['CampName']),
    total: _i(map['Total']),
    screeningDone: _i(map['ScreeningDone']),
    screeningNotDone: _i(map['ScreeningNotDone']),
  );

  Map<String, dynamic> toMap() => {
    'CampId': campId,
    'CampTypeDescription': campTypeDescription,
    'CampNo': campNo,
    'CampLocation': campLocation,
    'CampDate': campDate,
    'CampStatus': campStatus,
    'Status': status,
    'Description': description,
    'DISTLGDCODE': distLgdCode,
    'DISTNAME': distName,
    'REGISTERWORKERS': registerWorkers,
    'SurveyCoordinatorName': surveyCoordinatorName,
    'CordinatorName': cordinatorName,
    'MOBNO': mobNo,
    'CampName': campName,
    'Total': total,
    'ScreeningDone': screeningDone,
    'ScreeningNotDone': screeningNotDone,
  };

  String toJson() => json.encode(toMap());
}



String _s(dynamic v) => v == null ? '' : v.toString();

int _i(dynamic v) {
  if (v == null) return 0;
  if (v is num) return v.toInt();
  return int.tryParse(v.toString()) ?? 0;
}
