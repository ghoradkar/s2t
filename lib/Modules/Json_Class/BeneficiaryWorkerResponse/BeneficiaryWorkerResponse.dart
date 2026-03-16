// ignore_for_file: file_names, unnecessary_question_mark

class BeneficiaryWorkerResponse {
  String? status;
  String? message;
  List<BeneficiaryWorkerOutput>? output;

  BeneficiaryWorkerResponse({this.status, this.message, this.output});

  BeneficiaryWorkerResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != dynamic) {
      output = <BeneficiaryWorkerOutput>[];
      json['output'].forEach((v) {
        output!.add(BeneficiaryWorkerOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    if (output != dynamic) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class BeneficiaryWorkerOutput {
  int? regdId;
  int? regdNo;
  int? campId;
  int? siteDetailId;
  String? siteName;
  String? name;
  String? mOBILE;
  String? dOB;
  int? aGE;
  String? print;
  String? gENDER;
  String? permanentAddress;
  String? localAddress;
  dynamic? location;
  String? pincode;
  String? relName;
  String? relMName;
  double? heightCMs;
  double? weightKGs;
  dynamic? bloodPressure;
  dynamic? systolic;
  dynamic? diastolic;
  dynamic? bloodSugarF;
  dynamic? bloodSugarPP;
  dynamic? bloodSugarR;
  String? phleboName;
  String? phleboMobNo;
  String? regdImagePath;
  String? cardImagePath;
  String? regdImageName;
  String? cardImageName;
  int? isApproved;
  int? testId;
  String? reason;
  int? aLLTESTDONE;
  int? campCreatedBy;
  String? workerName;
  String? workerAge;
  String? workerGender;
  String? otherDescription;
  int? isDenied;
  String? sampleCollectedBarcode;
  int? isAudioTestReverification;
  int? isVisionTestReverification;
  int? isLFTTestReverification;
  int? photoVerificationStatusID;
  String? photoVerificationStatusDescription;
  int? photoSentForVerification;
  int? photoVerifiedByCT;
  int? screeningPatientID;

  BeneficiaryWorkerOutput({
    this.regdId,
    this.regdNo,
    this.campId,
    this.siteDetailId,
    this.siteName,
    this.name,
    this.mOBILE,
    this.dOB,
    this.aGE,
    this.print,
    this.gENDER,
    this.permanentAddress,
    this.localAddress,
    this.location,
    this.pincode,
    this.relName,
    this.relMName,
    this.heightCMs,
    this.weightKGs,
    this.bloodPressure,
    this.systolic,
    this.diastolic,
    this.bloodSugarF,
    this.bloodSugarPP,
    this.bloodSugarR,
    this.phleboName,
    this.phleboMobNo,
    this.regdImagePath,
    this.cardImagePath,
    this.regdImageName,
    this.cardImageName,
    this.isApproved,
    this.testId,
    this.reason,
    this.aLLTESTDONE,
    this.campCreatedBy,
    this.workerName,
    this.workerAge,
    this.workerGender,
    this.otherDescription,
    this.isDenied,
    this.sampleCollectedBarcode,
    this.isAudioTestReverification,
    this.isVisionTestReverification,
    this.isLFTTestReverification,
    this.photoVerificationStatusID,
    this.photoVerificationStatusDescription,
    this.photoSentForVerification,
    this.photoVerifiedByCT,
    this.screeningPatientID,
  });

  BeneficiaryWorkerOutput.fromJson(Map<String, dynamic> json) {
    regdId = json['RegdId'];
    regdNo = json['RegdNo'];
    campId = json['CampId'];
    siteDetailId = json['SiteDetailId'];
    siteName = json['SiteName'];
    name = json['Name'];
    mOBILE = json['MOBILE'];
    dOB = json['DOB'];
    aGE = json['AGE'];
    print = json['Print'];
    gENDER = json['GENDER'];
    permanentAddress = json['PermanentAddress'];
    localAddress = json['LocalAddress'];
    location = json['Location'];
    pincode = json['Pincode'];
    relName = json['RelName'];
    relMName = json['RelMName'];
    heightCMs = json['Height_CMs'];
    weightKGs = json['Weight_KGs'];
    bloodPressure = json['BloodPressure'];
    systolic = json['Systolic'];
    diastolic = json['Diastolic'];
    bloodSugarF = json['BloodSugar_F'];
    bloodSugarPP = json['BloodSugar_PP'];
    bloodSugarR = json['BloodSugar_R'];
    phleboName = json['PhleboName'];
    phleboMobNo = json['PhleboMobNo'];
    regdImagePath = json['RegdImagePath'];
    cardImagePath = json['CardImagePath'];
    regdImageName = json['RegdImageName'];
    cardImageName = json['CardImageName'];
    isApproved = json['IsApproved'];
    testId = json['TestId'];
    reason = json['Reason'];
    aLLTESTDONE = json['ALLTESTDONE'];
    campCreatedBy = json['CampCreatedBy'];
    workerName = json['WorkerName'];
    workerAge = json['WorkerAge'];
    workerGender = json['WorkerGender'];
    otherDescription = json['OtherDescription'];
    isDenied = json['IsDenied'];
    sampleCollectedBarcode = json['SampleCollectedBarcode'];
    isAudioTestReverification = json['IsAudioTestReverification'];
    isVisionTestReverification = json['IsVisionTestReverification'];
    isLFTTestReverification = json['IsLFTTestReverification'];
    photoVerificationStatusID = json['PhotoVerificationStatusID'];
    photoVerificationStatusDescription =
        json['PhotoVerificationStatusDescription'];
    photoSentForVerification = json['PhotoSentForVerification'];
    photoVerifiedByCT = json['PhotoVerifiedByCT'];
    screeningPatientID = json['ScreeningPatientID'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  <String, dynamic>{};
    data['RegdId'] = regdId;
    data['RegdNo'] = regdNo;
    data['CampId'] = campId;
    data['SiteDetailId'] = siteDetailId;
    data['SiteName'] = siteName;
    data['Name'] = name;
    data['MOBILE'] = mOBILE;
    data['DOB'] = dOB;
    data['AGE'] = aGE;
    data['Print'] = print;
    data['GENDER'] = gENDER;
    data['PermanentAddress'] = permanentAddress;
    data['LocalAddress'] = localAddress;
    data['Location'] = location;
    data['Pincode'] = pincode;
    data['RelName'] = relName;
    data['RelMName'] = relMName;
    data['Height_CMs'] = heightCMs;
    data['Weight_KGs'] = weightKGs;
    data['BloodPressure'] = bloodPressure;
    data['Systolic'] = systolic;
    data['Diastolic'] = diastolic;
    data['BloodSugar_F'] = bloodSugarF;
    data['BloodSugar_PP'] = bloodSugarPP;
    data['BloodSugar_R'] = bloodSugarR;
    data['PhleboName'] = phleboName;
    data['PhleboMobNo'] = phleboMobNo;
    data['RegdImagePath'] = regdImagePath;
    data['CardImagePath'] = cardImagePath;
    data['RegdImageName'] = regdImageName;
    data['CardImageName'] = cardImageName;
    data['IsApproved'] = isApproved;
    data['TestId'] = testId;
    data['Reason'] = reason;
    data['ALLTESTDONE'] = aLLTESTDONE;
    data['CampCreatedBy'] = campCreatedBy;
    data['WorkerName'] = workerName;
    data['WorkerAge'] = workerAge;
    data['WorkerGender'] = workerGender;
    data['OtherDescription'] = otherDescription;
    data['IsDenied'] = isDenied;
    data['SampleCollectedBarcode'] = sampleCollectedBarcode;
    data['IsAudioTestReverification'] = isAudioTestReverification;
    data['IsVisionTestReverification'] = isVisionTestReverification;
    data['IsLFTTestReverification'] = isLFTTestReverification;
    data['PhotoVerificationStatusID'] = photoVerificationStatusID;
    data['PhotoVerificationStatusDescription'] =
        photoVerificationStatusDescription;
    data['PhotoSentForVerification'] = photoSentForVerification;
    data['PhotoVerifiedByCT'] = photoVerifiedByCT;
    data['ScreeningPatientID'] = screeningPatientID;
    return data;
  }
}
