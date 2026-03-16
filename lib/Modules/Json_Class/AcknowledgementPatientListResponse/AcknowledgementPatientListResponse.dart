// ignore_for_file: unnecessary_question_mark, file_names, unrelated_type_equality_checks

class AcknowledgementPatientListResponse {
  String? status;
  String? message;
  List<AcknowledgementPatientOutput>? output;

  AcknowledgementPatientListResponse({this.status, this.message, this.output});

  AcknowledgementPatientListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != dynamic) {
      output = <AcknowledgementPatientOutput>[];
      json['output'].forEach((v) {
        output!.add(AcknowledgementPatientOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    if (output != dynamic) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class AcknowledgementPatientOutput {
  String? createdDate;
  int? campId;
  int? siteId;
  int? regdId;
  String? englishName;
  String? mobileNo;
  String? dOB;
  int? heightCMs;
  int? weightKGs;
  String? gender;
  dynamic? brId;
  String? bloodSugarPP;
  int? isSignature;
  int? age;
  int? regdNo;
  String? patientPhoto;
  String? healthCardPath;
  String? isHCRenewal;
  String? hCRenewalFilePath;
  String? userThumbPath;
  String? audioImage;
  int? screeningDoneCnt;
  int? subOrgId;
  String? subOrgName;
  String? isPhyForDist;
  int? isPhy;
  String? campType;
  int? campTypeID;
  dynamic? screeningPatientID;

  AcknowledgementPatientOutput({
    this.createdDate,
    this.campId,
    this.siteId,
    this.regdId,
    this.englishName,
    this.mobileNo,
    this.dOB,
    this.heightCMs,
    this.weightKGs,
    this.gender,
    this.brId,
    this.bloodSugarPP,
    this.isSignature,
    this.age,
    this.regdNo,
    this.patientPhoto,
    this.healthCardPath,
    this.isHCRenewal,
    this.hCRenewalFilePath,
    this.userThumbPath,
    this.audioImage,
    this.screeningDoneCnt,
    this.subOrgId,
    this.subOrgName,
    this.isPhyForDist,
    this.isPhy,
    this.campType,
    this.campTypeID,
    this.screeningPatientID,
  });

  AcknowledgementPatientOutput.fromJson(Map<String, dynamic> json) {
    createdDate = json['CreatedDate'];
    campId = json['CampId'];
    siteId = json['SiteId'];
    regdId = json['RegdId'];
    englishName = json['EnglishName'];
    mobileNo = json['MobileNo'];
    dOB = json['DOB'];
    heightCMs = json['Height_CMs'];
    weightKGs = json['Weight_KGs'];
    gender = json['Gender'];
    brId = json['BrId'];
    bloodSugarPP = json['BloodSugar_PP'];
    isSignature = json['IsSignature'];
    age = json['Age'];
    regdNo = json['RegdNo'];
    patientPhoto = json['patientPhoto'];
    healthCardPath = json['HealthCardPath'];
    isHCRenewal = json['IsHCRenewal'];
    hCRenewalFilePath = json['HCRenewalFilePath'];
    userThumbPath = json['UserThumbPath'];
    audioImage = json['AudioImage'];
    screeningDoneCnt = json['ScreeningDoneCnt'];
    subOrgId = json['SubOrgId'];
    subOrgName = json['SubOrgName'];
    isPhyForDist = json['IsPhyForDist'];
    isPhy = json['IsPhy'];
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
    data['Gender'] = gender;
    data['BrId'] = brId;
    data['BloodSugar_PP'] = bloodSugarPP;
    data['IsSignature'] = isSignature;
    data['Age'] = age;
    data['RegdNo'] = regdNo;
    data['patientPhoto'] = patientPhoto;
    data['HealthCardPath'] = healthCardPath;
    data['IsHCRenewal'] = isHCRenewal;
    data['HCRenewalFilePath'] = hCRenewalFilePath;
    data['UserThumbPath'] = userThumbPath;
    data['AudioImage'] = audioImage;
    data['ScreeningDoneCnt'] = screeningDoneCnt;
    data['SubOrgId'] = subOrgId;
    data['SubOrgName'] = subOrgName;
    data['IsPhyForDist'] = isPhyForDist;
    data['IsPhy'] = isPhy;
    data['CampType'] = campType;
    data['CampTypeID'] = campTypeID;
    data['ScreeningPatientID'] = screeningPatientID;
    return data;
  }
}
