// ignore_for_file: unnecessary_question_mark, file_names

class BeneficiaryListByRegIDResponse {
  String? status;
  String? message;
  List<BeneficiaryListByRegIDOutput>? output;

  BeneficiaryListByRegIDResponse({
    this.status,
    this.message,
    this.output,
  });

  BeneficiaryListByRegIDResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <BeneficiaryListByRegIDOutput>[];
      json['output'].forEach((v) {
        output!.add(BeneficiaryListByRegIDOutput.fromJson(v));
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

class BeneficiaryListByRegIDOutput {
  int? campId;
  String? campDate;
  int? regdId;
  String? englishName;
  int? beneficiaryRegdId;
  String? type;
  int? beneficiaryRegdNo;
  String? peStatus;
  String? doctorMappedStatus;
  String? doctorName;
  String? doctorMobile;

  BeneficiaryListByRegIDOutput({
    this.campId,
    this.campDate,
    this.regdId,
    this.englishName,
    this.beneficiaryRegdId,
    this.type,
    this.beneficiaryRegdNo,
    this.peStatus,
    this.doctorMappedStatus,
    this.doctorName,
    this.doctorMobile,
  });

  BeneficiaryListByRegIDOutput.fromJson(Map<String, dynamic> json) {
    campId = int.tryParse(json['CampId']?.toString() ?? '');
    campDate = json['Campdate']?.toString();
    regdId = int.tryParse(json['RegdId']?.toString() ?? '');
    englishName = json['EnglishName']?.toString();
    beneficiaryRegdId = int.tryParse(json['BenificiaryRegdID']?.toString() ?? '');
    type = json['Type']?.toString();
    beneficiaryRegdNo = int.tryParse(json['BenificiaryRegdNo']?.toString() ?? '');
    peStatus = json['PE_Status']?.toString();
    doctorMappedStatus = json['DoctorMapped_Status']?.toString();
    doctorName = json['DoctorName']?.toString();
    doctorMobile = json['DoctorMobile']?.toString();
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CampId'] = campId;
    data['Campdate'] = campDate;
    data['RegdId'] = regdId;
    data['EnglishName'] = englishName;
    data['BenificiaryRegdID'] = beneficiaryRegdId;
    data['Type'] = type;
    data['BenificiaryRegdNo'] = beneficiaryRegdNo;
    data['PE_Status'] = peStatus;
    data['DoctorMapped_Status'] = doctorMappedStatus;
    data['DoctorName'] = doctorName;
    data['DoctorMobile'] = doctorMobile;
    return data;
  }
}
