// ignore_for_file: unnecessary_question_mark, file_names

class D2DPhysicalExamninationDetailsResponse {
  String? status;
  String? message;
  List<D2DPhysicalExamninationDetailsOutput>? output;

  D2DPhysicalExamninationDetailsResponse({
    this.status,
    this.message,
    this.output,
  });

  D2DPhysicalExamninationDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <D2DPhysicalExamninationDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(D2DPhysicalExamninationDetailsOutput.fromJson(v));
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

class D2DPhysicalExamninationDetailsOutput {
  String? siteId;
  String? sensory;
  String? motor;
  int? campId;
  String? campdate;
  String? campLocation;
  String? siteAddress;
  String? beneficiaryName;
  String? healthCardNo;
  int? beneficiaryRegdNo;
  String? gender;
  int? age;
  String? mobNo;
  double? heightCMs;
  double? weightKGs;
  String? bloodPressure;
  String? bloodPressureHigh;
  String? bloodPressureLow;
  String? bloodSugarF;
  String? bloodSugarPP;
  String? bloodSugarR;
  double? bMI;
  String? bMIStatus;
  String? underWT;
  String? temparture;
  double? systolic;
  double? diastolic;
  int? bMIStatus1;
  String? bloodGroup;
  String? maritalStatus;
  int? noOfChildren;
  String? familyPlanOperation;
  String? alcohol;
  String? smoking;
  String? tobaco;
  String? patBarCode;
  String? physicalDoctor;
  String? lungClear;
  int? lungComment;
  String? abnormalSound;
  String? abnormalCheck;
  String? abnormalComment;
  String? otherAbnormality;
  String? sIS2Normal;
  String? anyMurmurs;
  String? cVSComment;
  String? palpationabdomen;
  String? anyhernia;
  String? massperAbdomen;
  String? gITComment;
  String? patwellOriented;
  String? cNSComment;
  String? finalRemark;
  String? finalComment;
  String? lMP;
  String? gynecProblem;
  String? iSBreastCancer;
  String? lMPDate;
  String? evidanceThyroid;
  String? famillyPlaning;
  String? comment;
  String? isPregnant;
  String? pregComment;
  String? fSH;
  String? fSHComment;
  String? recentDelivery;
  String? deliveryDate;
  String? deliveryComment;
  String? famillyAdvice;
  String? finalComment1;
  double? pulseRate;
  String? isAlcohol;
  int? alcoholSinceMonth;
  int? alcoholSinceYear;
  String? isSmoking;
  int? smokingSinceMonth;
  int? smokingSinceYear;
  String? isTobaco;
  int? tobacoSinceMonth;
  int? tobacoSinceYear;
  String? isDrug;
  int? drugSinceMonth;
  int? drugSinceYear;
  String? temperature;
  String? sPO2;
  String? visionInjuryEvidenceRight;
  String? visionInjuryEvidenceLeft;
  String? visionSnellelchartR;
  String? visionSnellelchartL;
  String? visionSnellelchartL1;
  int? iSGlasses;
  String? suggestion;
  String? otherRemark;
  String? bSFinalRemark;
  String? cancerHisComment;
  String? psComment;
  String? pSObservation;
  String? lumpObservation;
  String? breastScrenningRemark;
  String? fastingHrs;

  D2DPhysicalExamninationDetailsOutput({
    this.siteId, this.sensory, this.motor, this.campId, this.campdate,
    this.campLocation, this.siteAddress, this.beneficiaryName, this.healthCardNo,
    this.beneficiaryRegdNo, this.gender, this.age, this.mobNo, this.heightCMs,
    this.weightKGs, this.bloodPressure, this.bloodPressureHigh, this.bloodPressureLow,
    this.bloodSugarF, this.bloodSugarPP, this.bloodSugarR, this.bMI, this.bMIStatus,
    this.underWT, this.temparture, this.systolic, this.diastolic, this.bMIStatus1,
    this.bloodGroup, this.maritalStatus, this.noOfChildren, this.familyPlanOperation,
    this.alcohol, this.smoking, this.tobaco, this.patBarCode, this.physicalDoctor,
    this.lungClear, this.lungComment, this.abnormalSound, this.abnormalCheck,
    this.abnormalComment, this.otherAbnormality, this.sIS2Normal, this.anyMurmurs,
    this.cVSComment, this.palpationabdomen, this.anyhernia, this.massperAbdomen,
    this.gITComment, this.patwellOriented, this.cNSComment, this.finalRemark,
    this.finalComment, this.lMP, this.gynecProblem, this.iSBreastCancer, this.lMPDate,
    this.evidanceThyroid, this.famillyPlaning, this.comment, this.isPregnant,
    this.pregComment, this.fSH, this.fSHComment, this.recentDelivery, this.deliveryDate,
    this.deliveryComment, this.famillyAdvice, this.finalComment1, this.pulseRate,
    this.isAlcohol, this.alcoholSinceMonth, this.alcoholSinceYear, this.isSmoking,
    this.smokingSinceMonth, this.smokingSinceYear, this.isTobaco, this.tobacoSinceMonth,
    this.tobacoSinceYear, this.isDrug, this.drugSinceMonth, this.drugSinceYear,
    this.temperature, this.sPO2, this.visionInjuryEvidenceRight, this.visionInjuryEvidenceLeft,
    this.visionSnellelchartR, this.visionSnellelchartL, this.visionSnellelchartL1,
    this.iSGlasses, this.suggestion, this.otherRemark, this.bSFinalRemark,
    this.cancerHisComment, this.psComment, this.pSObservation, this.lumpObservation,
    this.breastScrenningRemark, this.fastingHrs,
  });

  D2DPhysicalExamninationDetailsOutput.fromJson(Map<String, dynamic> json) {
    siteId = json['SiteId'];
    sensory = json['Sensory'];
    motor = json['Motor'];
    campId = json['CampId'];
    campdate = json['Campdate'];
    campLocation = json['CampLocation'];
    siteAddress = json['SiteAddress'];
    beneficiaryName = json['BeneficiaryName'];
    healthCardNo = json['HealthCardNo'];
    beneficiaryRegdNo = json['BeneficiaryRegdNo'];
    gender = json['Gender'];
    age = json['Age'];
    mobNo = json['MobNo'];
    heightCMs = json['Height_CMs'];
    weightKGs = json['Weight_KGs'];
    bloodPressure = json['BloodPressure'];
    bloodPressureHigh = json['BloodPressure_High'];
    bloodPressureLow = json['BloodPressure_Low'];
    bloodSugarF = json['BloodSugar_F'];
    bloodSugarPP = json['BloodSugar_PP'];
    bloodSugarR = json['BloodSugar_R'] is int ? json['BloodSugar_R'].toString() : json['BloodSugar_R'];
    bMI = json['BMI'];
    bMIStatus = json['BMIStatus'];
    underWT = json['UnderWT'];
    temparture = json['Temparture'];
    systolic = json['Systolic'];
    diastolic = json['Diastolic'];
    bMIStatus1 = json['BMIStatus1'];
    bloodGroup = json['BloodGroup'];
    maritalStatus = json['MaritalStatus'];
    noOfChildren = json['NoOfChildren'];
    familyPlanOperation = json['FamilyPlanOperation'];
    alcohol = json['Alcohol'];
    smoking = json['Smoking'];
    tobaco = json['Tobaco'];
    patBarCode = json['PatBarCode'];
    physicalDoctor = json['PhysicalDoctor'];
    lungClear = json['LungClear'];
    lungComment = json['LungComment'];
    abnormalSound = json['AbnormalSound'];
    abnormalCheck = json['AbnormalCheck'];
    abnormalComment = json['AbnormalComment'];
    otherAbnormality = json['OtherAbnormality'];
    sIS2Normal = json['SIS2Normal'];
    anyMurmurs = json['AnyMurmurs'];
    cVSComment = json['CVSComment'];
    palpationabdomen = json['Palpationabdomen'];
    anyhernia = json['Anyhernia'];
    massperAbdomen = json['MassperAbdomen'];
    gITComment = json['GITComment'];
    patwellOriented = json['PatwellOriented'];
    cNSComment = json['CNSComment'];
    finalRemark = json['FinalRemark'];
    finalComment = json['FinalComment'];
    lMP = json['LMP'];
    gynecProblem = json['GynecProblem'];
    iSBreastCancer = json['ISBreastCancer'];
    lMPDate = json['LMPDate'];
    evidanceThyroid = json['EvidanceThyroid'];
    famillyPlaning = json['FamillyPlaning'];
    comment = json['Comment'];
    isPregnant = json['IsPregnant'];
    pregComment = json['PregComment'];
    fSH = json['FSH'];
    fSHComment = json['FSHComment'];
    recentDelivery = json['RecentDelivery'];
    deliveryDate = json['DeliveryDate'];
    deliveryComment = json['DeliveryComment'];
    famillyAdvice = json['FamillyAdvice'];
    finalComment1 = json['FinalComment1'];
    pulseRate = json['PulseRate'];
    isAlcohol = json['IsAlcohol'];
    alcoholSinceMonth = json['AlcoholSinceMonth'];
    alcoholSinceYear = json['AlcoholSinceYear'];
    isSmoking = json['IsSmoking'];
    smokingSinceMonth = json['SmokingSinceMonth'];
    smokingSinceYear = json['SmokingSinceYear'];
    isTobaco = json['IsTobaco'];
    tobacoSinceMonth = json['TobacoSinceMonth'];
    tobacoSinceYear = json['TobacoSinceYear'];
    isDrug = json['IsDrug'];
    drugSinceMonth = json['DrugSinceMonth'];
    drugSinceYear = json['DrugSinceYear'];
    temperature = json['Temperature'];
    sPO2 = json['SPO2'];
    visionInjuryEvidenceRight = json['Vision_Injury_Evidence_right'];
    visionInjuryEvidenceLeft = json['Vision_Injury_Evidence_Left'];
    visionSnellelchartR = json['Vision_Snellelchart_R'];
    visionSnellelchartL = json['Vision_Snellelchart_L'];
    visionSnellelchartL1 = json['Vision_Snellelchart_L1'];
    iSGlasses = json['iSGlasses'];
    suggestion = json['Suggestion'];
    otherRemark = json['other_remark'];
    bSFinalRemark = json['BSFinalRemark'];
    cancerHisComment = json['CancerHisComment'];
    psComment = json['PsComment'];
    pSObservation = json['PSObservation'];
    lumpObservation = json['LumpObservation'];
    breastScrenningRemark = json['BreastScrenningRemark'];
    fastingHrs = json['FastingHrs'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['SiteId'] = siteId;
    data['Sensory'] = sensory;
    data['Motor'] = motor;
    data['CampId'] = campId;
    data['Campdate'] = campdate;
    data['CampLocation'] = campLocation;
    data['SiteAddress'] = siteAddress;
    data['BeneficiaryName'] = beneficiaryName;
    data['HealthCardNo'] = healthCardNo;
    data['BeneficiaryRegdNo'] = beneficiaryRegdNo;
    data['Gender'] = gender;
    data['Age'] = age;
    data['MobNo'] = mobNo;
    data['Height_CMs'] = heightCMs;
    data['Weight_KGs'] = weightKGs;
    data['BloodPressure'] = bloodPressure;
    data['BloodPressure_High'] = bloodPressureHigh;
    data['BloodPressure_Low'] = bloodPressureLow;
    data['BloodSugar_F'] = bloodSugarF;
    data['BloodSugar_PP'] = bloodSugarPP;
    data['BloodSugar_R'] = bloodSugarR;
    data['BMI'] = bMI;
    data['BMIStatus'] = bMIStatus;
    data['UnderWT'] = underWT;
    data['Temparture'] = temparture;
    data['Systolic'] = systolic;
    data['Diastolic'] = diastolic;
    data['BMIStatus1'] = bMIStatus1;
    data['BloodGroup'] = bloodGroup;
    data['MaritalStatus'] = maritalStatus;
    data['NoOfChildren'] = noOfChildren;
    data['FamilyPlanOperation'] = familyPlanOperation;
    data['Alcohol'] = alcohol;
    data['Smoking'] = smoking;
    data['Tobaco'] = tobaco;
    data['PatBarCode'] = patBarCode;
    data['PhysicalDoctor'] = physicalDoctor;
    data['LungClear'] = lungClear;
    data['LungComment'] = lungComment;
    data['AbnormalSound'] = abnormalSound;
    data['AbnormalCheck'] = abnormalCheck;
    data['AbnormalComment'] = abnormalComment;
    data['OtherAbnormality'] = otherAbnormality;
    data['SIS2Normal'] = sIS2Normal;
    data['AnyMurmurs'] = anyMurmurs;
    data['CVSComment'] = cVSComment;
    data['Palpationabdomen'] = palpationabdomen;
    data['Anyhernia'] = anyhernia;
    data['MassperAbdomen'] = massperAbdomen;
    data['GITComment'] = gITComment;
    data['PatwellOriented'] = patwellOriented;
    data['CNSComment'] = cNSComment;
    data['FinalRemark'] = finalRemark;
    data['FinalComment'] = finalComment;
    data['LMP'] = lMP;
    data['GynecProblem'] = gynecProblem;
    data['ISBreastCancer'] = iSBreastCancer;
    data['LMPDate'] = lMPDate;
    data['EvidanceThyroid'] = evidanceThyroid;
    data['FamillyPlaning'] = famillyPlaning;
    data['Comment'] = comment;
    data['IsPregnant'] = isPregnant;
    data['PregComment'] = pregComment;
    data['FSH'] = fSH;
    data['FSHComment'] = fSHComment;
    data['RecentDelivery'] = recentDelivery;
    data['DeliveryDate'] = deliveryDate;
    data['DeliveryComment'] = deliveryComment;
    data['FamillyAdvice'] = famillyAdvice;
    data['FinalComment1'] = finalComment1;
    data['PulseRate'] = pulseRate;
    data['IsAlcohol'] = isAlcohol;
    data['AlcoholSinceMonth'] = alcoholSinceMonth;
    data['AlcoholSinceYear'] = alcoholSinceYear;
    data['IsSmoking'] = isSmoking;
    data['SmokingSinceMonth'] = smokingSinceMonth;
    data['SmokingSinceYear'] = smokingSinceYear;
    data['IsTobaco'] = isTobaco;
    data['TobacoSinceMonth'] = tobacoSinceMonth;
    data['TobacoSinceYear'] = tobacoSinceYear;
    data['IsDrug'] = isDrug;
    data['DrugSinceMonth'] = drugSinceMonth;
    data['DrugSinceYear'] = drugSinceYear;
    data['Temperature'] = temperature;
    data['SPO2'] = sPO2;
    data['Vision_Injury_Evidence_right'] = visionInjuryEvidenceRight;
    data['Vision_Injury_Evidence_Left'] = visionInjuryEvidenceLeft;
    data['Vision_Snellelchart_R'] = visionSnellelchartR;
    data['Vision_Snellelchart_L'] = visionSnellelchartL;
    data['Vision_Snellelchart_L1'] = visionSnellelchartL1;
    data['iSGlasses'] = iSGlasses;
    data['Suggestion'] = suggestion;
    data['other_remark'] = otherRemark;
    data['BSFinalRemark'] = bSFinalRemark;
    data['CancerHisComment'] = cancerHisComment;
    data['PsComment'] = psComment;
    data['PSObservation'] = pSObservation;
    data['LumpObservation'] = lumpObservation;
    data['BreastScrenningRemark'] = breastScrenningRemark;
    data['FastingHrs'] = fastingHrs;
    return data;
  }
}