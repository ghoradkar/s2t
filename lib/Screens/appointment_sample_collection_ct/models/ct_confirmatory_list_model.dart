class CTConfirmatoryListModel {
  String? status;
  String? message;
  List<CTConfirmatoryListOutput>? output;

  CTConfirmatoryListModel({this.status, this.message, this.output});

  CTConfirmatoryListModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CTConfirmatoryListOutput>[];
      json['output'].forEach((v) {
        output!.add(CTConfirmatoryListOutput.fromJson(v));
      });
    }
  }
}

class CTConfirmatoryListOutput {
  String? beneficiaryName;
  String? memberCount;
  String? regdNo;
  String? mobileNo;
  String? arId;
  String? appointmentDate;

  CTConfirmatoryListOutput({
    this.beneficiaryName,
    this.memberCount,
    this.regdNo,
    this.mobileNo,
    this.arId,
    this.appointmentDate,
  });

  CTConfirmatoryListOutput.fromJson(Map<String, dynamic> json) {
    beneficiaryName = json['BeneficiaryName']?.toString();
    memberCount = json['MemberCount']?.toString();
    regdNo = json['RegdNo']?.toString() ?? json['Regdno']?.toString();
    mobileNo = json['MobileNo']?.toString() ?? json['MOBNO']?.toString();
    arId = json['ArId']?.toString();
    appointmentDate = json['AppointmentDate']?.toString();
  }
}