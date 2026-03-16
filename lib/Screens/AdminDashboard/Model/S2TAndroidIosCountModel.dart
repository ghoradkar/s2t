class S2TAndroidIosCountModel {
  final String status;
  final String path;
  final String dateTime;
  final PatientAppCountDetails details;

  S2TAndroidIosCountModel({
    required this.status,
    required this.path,
    required this.dateTime,
    required this.details,
  });

  factory S2TAndroidIosCountModel.fromJson(Map<String, dynamic> json) {
    return S2TAndroidIosCountModel(
      status: json['status'] ?? '',
      path: json['path'] ?? '',
      dateTime: json['dateTime'] ?? '',
      details: PatientAppCountDetails.fromJson(json['details'] ?? {}),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'status': status,
      'path': path,
      'dateTime': dateTime,
      'details': details.toJson(),
    };
  }
}

class PatientAppCountDetails {
  final List<PatientAppCount> count;

  PatientAppCountDetails({required this.count});

  factory PatientAppCountDetails.fromJson(Map<String, dynamic> json) {
    return PatientAppCountDetails(
      count: (json['count'] as List<dynamic>? ?? [])
          .map((e) => PatientAppCount.fromJson(e))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'count': count.map((e) => e.toJson()).toList(),
    };
  }
}

class PatientAppCount {
  final int iOS;
  final int android;

  PatientAppCount({
    required this.iOS,
    required this.android,
  });

  factory PatientAppCount.fromJson(Map<String, dynamic> json) {
    return PatientAppCount(
      iOS: json['iOS'] ?? 0,
      android: json['Android'] ?? 0,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'iOS': iOS,
      'Android': android,
    };
  }
}
