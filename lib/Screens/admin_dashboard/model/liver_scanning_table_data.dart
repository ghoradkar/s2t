class LiverScanningTableData {
  final String status;
  final String message;
  final List<FibroscanDistrictData> output;

  LiverScanningTableData({
    required this.status,
    required this.message,
    required this.output,
  });

  factory LiverScanningTableData.fromJson(Map<String, dynamic> json) {
    return LiverScanningTableData(
      status: json['Status'] ?? '',
      message: json['Message'] ?? '',
      output: (json['Output'] as List<dynamic>? ?? [])
          .map((e) => FibroscanDistrictData.fromJson(e))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'Status': status,
      'Message': message,
      'Output': output.map((e) => e.toJson()).toList(),
    };
  }
}

class FibroscanDistrictData {
  final String vpName;
  final int distLgdCode;
  final String district;
  final int patientCount;
  final int abnormalPatientCount;
  final int moderateSevereCount;
  final int successfullShots;
  final int totalShots;

  FibroscanDistrictData({
    required this.vpName,
    required this.distLgdCode,
    required this.district,
    required this.patientCount,
    required this.abnormalPatientCount,
    required this.moderateSevereCount,
    required this.successfullShots,
    required this.totalShots,
  });

  factory FibroscanDistrictData.fromJson(Map<String, dynamic> json) {
    return FibroscanDistrictData(
      vpName: json['VPName'] ?? '',
      distLgdCode: json['DISTLGDCODE'] ?? 0,
      district: json['District'] ?? '',
      patientCount: json['PatientCount'] ?? 0,
      abnormalPatientCount: json['AbnormalPatientCount'] ?? 0,
      moderateSevereCount: json['ModerateSevereCount'] ?? 0,
      successfullShots: json['SuccessfullShots'] ?? 0,
      totalShots: json['TotalShots'] ?? 0,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'VPName': vpName,
      'DISTLGDCODE': distLgdCode,
      'District': district,
      'PatientCount': patientCount,
      'AbnormalPatientCount': abnormalPatientCount,
      'ModerateSevereCount': moderateSevereCount,
      'SuccessfullShots': successfullShots,
      'TotalShots': totalShots,
    };
  }
}
