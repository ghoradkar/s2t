// ignore_for_file: unnecessary_question_mark, file_names

class AttendancesListUsingSiteDetailsIDResponse {
  String? status;
  String? message;
  List<AttendancesListUsingSiteDetailsIDOutput>? output;

  AttendancesListUsingSiteDetailsIDResponse({
    this.status,
    this.message,
    this.output,
  });

  AttendancesListUsingSiteDetailsIDResponse.fromJson(
    Map<String, dynamic> json,
  ) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AttendancesListUsingSiteDetailsIDOutput>[];
      json['output'].forEach((v) {
        output!.add(AttendancesListUsingSiteDetailsIDOutput.fromJson(v));
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

class AttendancesListUsingSiteDetailsIDOutput {
  String? createdDate;
  int? campId;
  int? siteId;
  int? regdId;
  String? englishName;
  String? mobileNo;
  String? dOB;
  double? heightCMs;
  double? weightKGs;
  String? uID;
  String? permanentAddress;
  String? localAddress;
  String? pincode;
  String? gender;
  int? bloodSugarPP;
  String? isSignature;
  int? age;
  int? regdNo;
  String? patientPhoto;
  String? healthCardPath;
  String? isHCRenewal;
  String? hCRenewalFilePath;
  String? userThumbPath;
  String? screeningDoneCnt;
  int? isRtpcr;
  int? isDependent;
  int? releationID;
  String? relName;
  String? isCall;
  int? subOrgId;
  String? subOrgName;
  int? isPhy;
  String? isPhyForDist;
  String? campType;
  int? campTypeID;
  int? screeningPatientID;

  AttendancesListUsingSiteDetailsIDOutput({
    this.createdDate,
    this.campId,
    this.siteId,
    this.regdId,
    this.englishName,
    this.mobileNo,
    this.dOB,
    this.heightCMs,
    this.weightKGs,
    this.uID,
    this.permanentAddress,
    this.localAddress,
    this.pincode,
    this.gender,
    this.bloodSugarPP,
    this.isSignature,
    this.age,
    this.regdNo,
    this.patientPhoto,
    this.healthCardPath,
    this.isHCRenewal,
    this.hCRenewalFilePath,
    this.userThumbPath,
    this.screeningDoneCnt,
    this.isRtpcr,
    this.isDependent,
    this.releationID,
    this.relName,
    this.isCall,
    this.subOrgId,
    this.subOrgName,
    this.isPhy,
    this.isPhyForDist,
    this.campType,
    this.campTypeID,
    this.screeningPatientID,
  });

  AttendancesListUsingSiteDetailsIDOutput.fromJson(Map<String, dynamic> json) {
    createdDate = json['CreatedDate'];
    campId = json['CampId'];
    siteId = json['SiteId'];
    regdId = json['RegdId'];
    englishName = json['EnglishName'];
    mobileNo = json['MobileNo'];
    dOB = json['DOB'];
    heightCMs = json['Height_CMs'];
    weightKGs = json['Weight_KGs'];
    uID = json['UID'];
    permanentAddress = json['PermanentAddress'];
    localAddress = json['LocalAddress'];
    pincode = json['Pincode'];
    gender = json['Gender'];
    bloodSugarPP = json['BloodSugar_PP'];
    isSignature = json['IsSignature'];
    age = json['Age'];
    regdNo = json['RegdNo'];
    patientPhoto = json['patientPhoto'];
    healthCardPath = json['HealthCardPath'];
    isHCRenewal = json['IsHCRenewal'];
    hCRenewalFilePath = json['HCRenewalFilePath'];
    userThumbPath = json['UserThumbPath'];
    screeningDoneCnt = json['ScreeningDoneCnt'];
    isRtpcr = json['IsRtpcr'];
    isDependent = json['IsDependent'];
    releationID = json['ReleationID'];
    relName = json['RelName'];
    isCall = json['IsCall'];
    subOrgId = json['SubOrgId'];
    subOrgName = json['SubOrgName'];
    isPhy = json['IsPhy'];
    isPhyForDist = json['IsPhyForDist'];
    campType = json['CampType'];
    campTypeID = json['CampTypeID'];
    screeningPatientID = json['ScreeningPatientID'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CreatedDate'] = createdDate;
    data['CampId'] = campId;
    data['SiteId'] = siteId;
    data['RegdId'] = regdId;
    data['EnglishName'] = englishName;
    data['MobileNo'] = mobileNo;
    data['DOB'] = dOB;
    data['Height_CMs'] = heightCMs;
    data['Weight_KGs'] = weightKGs;
    data['UID'] = uID;
    data['PermanentAddress'] = permanentAddress;
    data['LocalAddress'] = localAddress;
    data['Pincode'] = pincode;
    data['Gender'] = gender;
    data['BloodSugar_PP'] = bloodSugarPP;
    data['IsSignature'] = isSignature;
    data['Age'] = age;
    data['RegdNo'] = regdNo;
    data['patientPhoto'] = patientPhoto;
    data['HealthCardPath'] = healthCardPath;
    data['IsHCRenewal'] = isHCRenewal;
    data['HCRenewalFilePath'] = hCRenewalFilePath;
    data['UserThumbPath'] = userThumbPath;
    data['ScreeningDoneCnt'] = screeningDoneCnt;
    data['IsRtpcr'] = isRtpcr;
    data['IsDependent'] = isDependent;
    data['ReleationID'] = releationID;
    data['RelName'] = relName;
    data['IsCall'] = isCall;
    data['SubOrgId'] = subOrgId;
    data['SubOrgName'] = subOrgName;
    data['IsPhy'] = isPhy;
    data['IsPhyForDist'] = isPhyForDist;
    data['CampType'] = campType;
    data['CampTypeID'] = campTypeID;
    data['ScreeningPatientID'] = screeningPatientID;
    return data;
  }
}
