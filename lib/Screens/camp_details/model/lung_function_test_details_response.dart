// ignore_for_file: file_names

class LungFunctionTestDetailsResponse {
  String? status;
  String? message;
  List<LungFunctionTestDetailsOutput>? output;

  LungFunctionTestDetailsResponse({this.status, this.message, this.output});

  LungFunctionTestDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <LungFunctionTestDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(LungFunctionTestDetailsOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class LungFunctionTestDetailsOutput {
  int? age;
  String? bloodGroup;
  String? bloodPressure;
  int? createdBy;
  String? dOB;
  String? deviceId;
  double? fCV;
  double? fEF2575;
  double? fET;
  double? fEV1;
  double? fEVIFVC;
  String? fIVC;
  String? gender;
  double? heightCMs;
  String? mobileNo;
  double? pEF;
  String? pIF;
  int? regdNo;
  String? result;
  String? resultfooter;
  String? smoking;
  int? spiroId;
  String? technician;
  String? testDate;
  double? weightKGs;
  String? patientname;

  LungFunctionTestDetailsOutput({
    this.age,
    this.bloodGroup,
    this.bloodPressure,
    this.createdBy,
    this.dOB,
    this.deviceId,
    this.fCV,
    this.fEF2575,
    this.fET,
    this.fEV1,
    this.fEVIFVC,
    this.fIVC,
    this.gender,
    this.heightCMs,
    this.mobileNo,
    this.pEF,
    this.pIF,
    this.regdNo,
    this.result,
    this.resultfooter,
    this.smoking,
    this.spiroId,
    this.technician,
    this.testDate,
    this.weightKGs,
    this.patientname,
  });

  factory LungFunctionTestDetailsOutput.fromJson(Map<String, dynamic> json) {
    return LungFunctionTestDetailsOutput(
      age: json['Age'],
      bloodGroup: json['BloodGroup'],
      bloodPressure: json['BloodPressure'],
      createdBy: json['CreatedBy'],
      dOB: json['DOB'],
      deviceId: json['DeviceId'],
      fCV: (json['FCV'] as num?)?.toDouble(),
      fEF2575: (json['FEF_25_75'] as num?)?.toDouble(),
      fET: (json['FET'] as num?)?.toDouble(),
      fEV1: (json['FEV1'] as num?)?.toDouble(),
      fEVIFVC: (json['FEVI_FVC'] as num?)?.toDouble(),
      fIVC: json['FIVC'],
      gender: json['Gender'],
      heightCMs: json['Height_CMs'],
      mobileNo: json['MobileNo'],
      pEF: (json['PEF'] as num?)?.toDouble(),
      pIF: json['PIF'],
      regdNo: json['RegdNo'],
      result: json['Result'],
      resultfooter: json['Resultfooter'],
      smoking: json['Smoking'],
      spiroId: json['SpiroId'],
      technician: json['Technician'],
      testDate: json['TestDate'],
      weightKGs: json['Weight_KGs'],
      patientname: json['patientname'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      if (age != null) 'Age': age,
      if (bloodGroup != null) 'BloodGroup': bloodGroup,
      if (bloodPressure != null) 'BloodPressure': bloodPressure,
      if (createdBy != null) 'CreatedBy': createdBy,
      if (dOB != null) 'DOB': dOB,
      if (deviceId != null) 'DeviceId': deviceId,
      if (fCV != null) 'FCV': fCV,
      if (fEF2575 != null) 'FEF_25_75': fEF2575,
      if (fET != null) 'FET': fET,
      if (fEV1 != null) 'FEV1': fEV1,
      if (fEVIFVC != null) 'FEVI_FVC': fEVIFVC,
      if (fIVC != null) 'FIVC': fIVC,
      if (gender != null) 'Gender': gender,
      if (heightCMs != null) 'Height_CMs': heightCMs,
      if (mobileNo != null) 'MobileNo': mobileNo,
      if (pEF != null) 'PEF': pEF,
      if (pIF != null) 'PIF': pIF,
      if (regdNo != null) 'RegdNo': regdNo,
      if (result != null) 'Result': result,
      if (resultfooter != null) 'Resultfooter': resultfooter,
      if (smoking != null) 'Smoking': smoking,
      if (spiroId != null) 'SpiroId': spiroId,
      if (technician != null) 'Technician': technician,
      if (testDate != null) 'TestDate': testDate,
      if (weightKGs != null) 'Weight_KGs': weightKGs,
      if (patientname != null) 'patientname': patientname,
    };
  }
}
