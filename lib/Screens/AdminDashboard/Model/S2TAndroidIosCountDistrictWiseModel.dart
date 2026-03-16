class S2TAndroidIosCountDistrictWiseModel {
  final String status;
  final String path;
  final String dateTime;
  final PatientDistrictCountDetails details;

  S2TAndroidIosCountDistrictWiseModel({
    required this.status,
    required this.path,
    required this.dateTime,
    required this.details,
  });

  factory S2TAndroidIosCountDistrictWiseModel.fromJson(Map<String, dynamic> json) {
    return S2TAndroidIosCountDistrictWiseModel(
      status: json['status'] ?? '',
      path: json['path'] ?? '',
      dateTime: json['dateTime'] ?? '',
      details: PatientDistrictCountDetails.fromJson(json['details'] ?? {}),
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

class PatientDistrictCountDetails {
  final List<PatientDistrictCount> count;

  PatientDistrictCountDetails({required this.count});

  factory PatientDistrictCountDetails.fromJson(Map<String, dynamic> json) {
    return PatientDistrictCountDetails(
      count: (json['count'] as List<dynamic>? ?? [])
          .map((e) => PatientDistrictCount.fromJson(e))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'count': count.map((e) => e.toJson()).toList(),
    };
  }
}

class PatientDistrictCount {
  final int iosCount;
  final int android;
  final String district;

  PatientDistrictCount({
    required this.iosCount,
    required this.android,
    required this.district,
  });

  factory PatientDistrictCount.fromJson(Map<String, dynamic> json) {
    return PatientDistrictCount(
      iosCount: json['iOS_Count'] ?? 0,
      android: json['Android'] ?? 0,
      district: json['District'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'iOS_Count': iosCount,
      'Android': android,
      'District': district,
    };
  }
}
