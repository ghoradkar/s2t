// ignore_for_file: unnecessary_question_mark, file_names

class D2DCampMappedDoctorListResponse {
  String? status;
  String? message;
  List<D2DCampMappedDoctorOutput>? output;

  D2DCampMappedDoctorListResponse({
    this.status,
    this.message,
    this.output,
  });

  D2DCampMappedDoctorListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <D2DCampMappedDoctorOutput>[];
      json['output'].forEach((v) {
        output!.add(D2DCampMappedDoctorOutput.fromJson(v));
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

class D2DCampMappedDoctorOutput {
  int? campId;
  String? campDate;
  int? userId;
  String? fullName;

  D2DCampMappedDoctorOutput({
    this.campId,
    this.campDate,
    this.userId,
    this.fullName,
  });

  D2DCampMappedDoctorOutput.fromJson(Map<String, dynamic> json) {
    campId = int.tryParse(json['CampId']?.toString() ?? '');
    campDate = json['CampDate']?.toString();
    userId = int.tryParse(json['USERID']?.toString() ?? '');
    fullName = json['Fullname']?.toString();
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CampId'] = campId;
    data['CampDate'] = campDate;
    data['USERID'] = userId;
    data['Fullname'] = fullName;
    return data;
  }
}
