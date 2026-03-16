class FibroScanningDistrictWiseModel {
  final String? status;
  final String? message;
  final List<FibroscanPatientData>? output;

  FibroScanningDistrictWiseModel({
    this.status,
    this.message,
    this.output,
  });

  factory FibroScanningDistrictWiseModel.fromJson(Map<String, dynamic> json) {
    return FibroScanningDistrictWiseModel(
      status: json['Status'] as String?,
      message: json['Message'] as String?,
      output: (json['Output'] as List<dynamic>?)
          ?.map((e) => FibroscanPatientData.fromJson(e))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'Status': status,
      'Message': message,
      'Output': output?.map((e) => e.toJson()).toList(),
    };
  }
}

class FibroscanPatientData {
  final String? vpName;
  final String? district;
  final String? patientId;
  final String? datetime;
  final String? stiffness;
  final String? uap;
  final String? successfullShots;
  final String? totalShots;
  final String? mmu;
  final String? machineNo;

  FibroscanPatientData({
    this.vpName,
    this.district,
    this.patientId,
    this.datetime,
    this.stiffness,
    this.uap,
    this.successfullShots,
    this.totalShots,
    this.mmu,
    this.machineNo,
  });

  factory FibroscanPatientData.fromJson(Map<String, dynamic> json) {
    return FibroscanPatientData(
      vpName: json['VPName'] as String?,
      district: json['District'] as String?,
      patientId: json['PatientID'] as String?,
      datetime: json['datetime'] as String?,
      stiffness: json['Stiffness'] as String?,
      uap: json['UAP'] as String?,
      successfullShots: json['SuccessfullShots'] as String?,
      totalShots: json['TotalShots'] as String?,
      mmu: json['MMU'] as String?,
      machineNo: json['MachineNo'] as String?,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'VPName': vpName,
      'District': district,
      'PatientID': patientId,
      'datetime': datetime,
      'Stiffness': stiffness,
      'UAP': uap,
      'SuccessfullShots': successfullShots,
      'TotalShots': totalShots,
      'MMU': mmu,
      'MachineNo': machineNo,
    };
  }
}
