// ignore_for_file: file_names

class PatientDetailsOnRegNoResponse {
  String? status;
  String? message;
  List<PatientDetailsOnRegNoOutput>? output;

  PatientDetailsOnRegNoResponse({this.status, this.message, this.output});

  PatientDetailsOnRegNoResponse.fromJson(Map<String, dynamic> json) {
    status = json['status']?.toString();
    message = json['message']?.toString();
    if (json['output'] != null && json['output'] is List) {
      output = (json['output'] as List)
          .map((e) => PatientDetailsOnRegNoOutput.fromJson(e as Map<String, dynamic>))
          .toList();
    }
  }
}

class PatientDetailsOnRegNoOutput {
  String? regdId;
  String? regdNo;
  String? englishName;
  String? title;
  String? gender;
  String? age;
  String? dobFormatted;
  String? uid;
  String? mobileNo;
  String? permanentAddress;
  String? localAddress;
  String? location;
  String? pincode;
  String? isThumbExist;

  PatientDetailsOnRegNoOutput.fromJson(Map<String, dynamic> json) {
    regdId = json['RegdId']?.toString();
    regdNo = json['RegdNo']?.toString();
    englishName = json['EnglishName']?.toString();
    title = json['Title']?.toString();
    gender = json['Gender']?.toString();
    age = json['Age']?.toString();
    dobFormatted = json['DOBFormated']?.toString();
    uid = json['UID']?.toString();
    mobileNo = json['MobileNo']?.toString();
    permanentAddress = json['PermanentAddress']?.toString();
    localAddress = json['LocalAddress']?.toString();
    location = json['Location']?.toString();
    pincode = json['Pincode']?.toString();
    isThumbExist = json['isThumbExist']?.toString() ?? '0';
  }
}