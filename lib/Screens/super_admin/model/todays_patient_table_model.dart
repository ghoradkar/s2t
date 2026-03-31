class TodaysPatientTableModel {
  String? status;
  String? message;
  List<PatientCountOutput>? output;

  TodaysPatientTableModel({
    this.status,
    this.message,
    this.output,
  });

  factory TodaysPatientTableModel.fromJson(Map<String, dynamic> json) {
    return TodaysPatientTableModel(
      status: json['status'] as String?,
      message: json['message'] as String?,
      output: (json['output'] as List<dynamic>?)
          ?.map((e) => PatientCountOutput.fromJson(e as Map<String, dynamic>))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() => {
    'status': status,
    'message': message,
    'output': output?.map((e) => e.toJson()).toList(),
  };
}

class PatientCountOutput {
  int? subOrgId;
  String? subOrgName;
  int? total;
  int? regularCamp;
  int? d2dCamp;
  int? regular;
  int? cscRegular;
  int? d2d;
  int? cscD2d;
  int? flexi;

  PatientCountOutput({
    this.subOrgId,
    this.subOrgName,
    this.total,
    this.regularCamp,
    this.d2dCamp,
    this.regular,
    this.cscRegular,
    this.d2d,
    this.cscD2d,
    this.flexi,
  });

  factory PatientCountOutput.fromJson(Map<String, dynamic> json) {
    return PatientCountOutput(
      subOrgId: json['SubOrgId'] as int?,
      subOrgName: json['SubOrgName'] as String?,
      total: json['Total'] as int?,
      regularCamp: json['RegularCamp'] as int?,
      d2dCamp: json['D2DCamp'] as int?,
      regular: json['Regular'] as int?,
      cscRegular: json['CSCRegular'] as int?,
      d2d: json['D2D'] as int?,
      cscD2d: json['CSCD2D'] as int?,
      flexi: json['Flexi'] as int?,
    );
  }

  Map<String, dynamic> toJson() => {
    'SubOrgId': subOrgId,
    'SubOrgName': subOrgName,
    'Total': total,
    'RegularCamp': regularCamp,
    'D2DCamp': d2dCamp,
    'Regular': regular,
    'CSCRegular': cscRegular,
    'D2D': d2d,
    'CSCD2D': cscD2d,
    'Flexi': flexi,
  };
}
