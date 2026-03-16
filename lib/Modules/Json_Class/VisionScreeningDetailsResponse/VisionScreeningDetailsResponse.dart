// ignore_for_file: prefer_collection_literals

class VisionScreeningDetailsResponse {
  String? status;
  String? message;
  List<VisionScreeningDetailsOutput>? output;

  VisionScreeningDetailsResponse({this.status, this.message, this.output});

  VisionScreeningDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <VisionScreeningDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(VisionScreeningDetailsOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class VisionScreeningDetailsOutput {
  dynamic abnormalCheck;
  dynamic abnormalComment;
  String? abnormalSound;
  int? age;
  String? alcohol;
  int? alcoholSinceMonth;
  int? alcoholSinceYear;
  String? anyMurmurs;
  String? anyhernia;
  double? bMI;
  String? bMIStatus;
  int? bMIStatus1;
  String? beneficiaryName;
  int? beneficiaryRegdNo;
  String? bloodGroup;
  String? bloodPressure;
  dynamic bloodPressureHigh;
  dynamic bloodPressureLow;
  String? bloodSugarF;
  String? bloodSugarPP;
  String? bloodSugarR;
  dynamic cNSComment;
  dynamic cVSComment;
  int? campId;
  dynamic campLocation;
  dynamic campdate;
  dynamic comment;
  dynamic deliveryComment;
  dynamic deliveryDate;
  double? diastolic;
  int? drugSinceMonth;
  int? drugSinceYear;
  String? evidanceThyroid;
  String? fSH;
  dynamic fSHComment;
  dynamic famillyAdvice;
  String? famillyPlaning;
  String? familyPlanOperation;
  dynamic finalComment;
  dynamic finalComment1;
  String? finalRemark;
  dynamic gITComment;
  String? gender;
  String? gynecProblem;
  dynamic healthCardNo;
  double? heightCMs;
  String? iSBreastCancer;
  String? isAlcohol;
  String? isDrug;
  dynamic isPregnant;
  String? isSmoking;
  String? isTobaco;
  dynamic lMP;
  String? lMPDate;
  String? leftRemark;
  String? lungClear;
  dynamic lungComment;
  String? maritalStatus;
  String? massperAbdomen;
  String? mobNo;
  String? motor;
  String? nearVisionRemark;
  dynamic nearVisionRemarkfooter;
  int? noOfChildren;
  dynamic otherAbnormality;
  String? palpationabdomen;
  dynamic patBarCode;
  String? patwellOriented;
  String? physicalDoctor;
  dynamic pregComment;
  double? pulseRate;
  String? recentDelivery;
  String? rightRemark;
  String? sIS2Normal;
  String? sensory;
  dynamic siteAddress;
  dynamic siteId;
  String? smoking;
  int? smokingSinceMonth;
  int? smokingSinceYear;
  dynamic suggestion;
  double? systolic;
  dynamic temparture;
  String? tobaco;
  int? tobacoSinceMonth;
  int? tobacoSinceYear;
  dynamic underWT;
  dynamic visionInjuryEvidenceLeft;
  dynamic visionInjuryEvidenceRight;
  String? visionSnellelchartL;
  String? visionSnellelchartL1;
  String? visionSnellelchartR;
  double? weightKGs;
  dynamic iSGlasses;
  dynamic otherRemark;

  VisionScreeningDetailsOutput({
    this.abnormalCheck,
    this.abnormalComment,
    this.abnormalSound,
    this.age,
    this.alcohol,
    this.alcoholSinceMonth,
    this.alcoholSinceYear,
    this.anyMurmurs,
    this.anyhernia,
    this.bMI,
    this.bMIStatus,
    this.bMIStatus1,
    this.beneficiaryName,
    this.beneficiaryRegdNo,
    this.bloodGroup,
    this.bloodPressure,
    this.bloodPressureHigh,
    this.bloodPressureLow,
    this.bloodSugarF,
    this.bloodSugarPP,
    this.bloodSugarR,
    this.cNSComment,
    this.cVSComment,
    this.campId,
    this.campLocation,
    this.campdate,
    this.comment,
    this.deliveryComment,
    this.deliveryDate,
    this.diastolic,
    this.drugSinceMonth,
    this.drugSinceYear,
    this.evidanceThyroid,
    this.fSH,
    this.fSHComment,
    this.famillyAdvice,
    this.famillyPlaning,
    this.familyPlanOperation,
    this.finalComment,
    this.finalComment1,
    this.finalRemark,
    this.gITComment,
    this.gender,
    this.gynecProblem,
    this.healthCardNo,
    this.heightCMs,
    this.iSBreastCancer,
    this.isAlcohol,
    this.isDrug,
    this.isPregnant,
    this.isSmoking,
    this.isTobaco,
    this.lMP,
    this.lMPDate,
    this.leftRemark,
    this.lungClear,
    this.lungComment,
    this.maritalStatus,
    this.massperAbdomen,
    this.mobNo,
    this.motor,
    this.nearVisionRemark,
    this.nearVisionRemarkfooter,
    this.noOfChildren,
    this.otherAbnormality,
    this.palpationabdomen,
    this.patBarCode,
    this.patwellOriented,
    this.physicalDoctor,
    this.pregComment,
    this.pulseRate,
    this.recentDelivery,
    this.rightRemark,
    this.sIS2Normal,
    this.sensory,
    this.siteAddress,
    this.siteId,
    this.smoking,
    this.smokingSinceMonth,
    this.smokingSinceYear,
    this.suggestion,
    this.systolic,
    this.temparture,
    this.tobaco,
    this.tobacoSinceMonth,
    this.tobacoSinceYear,
    this.underWT,
    this.visionInjuryEvidenceLeft,
    this.visionInjuryEvidenceRight,
    this.visionSnellelchartL,
    this.visionSnellelchartL1,
    this.visionSnellelchartR,
    this.weightKGs,
    this.iSGlasses,
    this.otherRemark,
  });

  factory VisionScreeningDetailsOutput.fromJson(Map<String, dynamic> json) {
    return VisionScreeningDetailsOutput(
      abnormalCheck: json['AbnormalCheck'],
      abnormalComment: json['AbnormalComment'],
      abnormalSound: json['AbnormalSound'],
      age: json['Age'],
      alcohol: json['Alcohol'],
      alcoholSinceMonth: json['AlcoholSinceMonth'],
      alcoholSinceYear: json['AlcoholSinceYear'],
      anyMurmurs: json['AnyMurmurs'],
      anyhernia: json['Anyhernia'],
      bMI: (json['BMI'] != null) ? json['BMI'].toDouble() : null,
      bMIStatus: json['BMIStatus'],
      bMIStatus1: json['BMIStatus1'],
      beneficiaryName: json['BeneficiaryName'],
      beneficiaryRegdNo: json['BeneficiaryRegdNo'],
      bloodGroup: json['BloodGroup'],
      bloodPressure: json['BloodPressure'],
      bloodPressureHigh: json['BloodPressure_High'],
      bloodPressureLow: json['BloodPressure_Low'],
      bloodSugarF: json['BloodSugar_F'],
      bloodSugarPP: json['BloodSugar_PP'],
      bloodSugarR: json['BloodSugar_R'],
      cNSComment: json['CNSComment'],
      cVSComment: json['CVSComment'],
      campId: json['CampId'],
      campLocation: json['CampLocation'],
      campdate: json['Campdate'],
      comment: json['Comment'],
      deliveryComment: json['DeliveryComment'],
      deliveryDate: json['DeliveryDate'],
      diastolic: json['Diastolic'],
      drugSinceMonth: json['DrugSinceMonth'],
      drugSinceYear: json['DrugSinceYear'],
      evidanceThyroid: json['EvidanceThyroid'],
      fSH: json['FSH'],
      fSHComment: json['FSHComment'],
      famillyAdvice: json['FamillyAdvice'],
      famillyPlaning: json['FamillyPlaning'],
      familyPlanOperation: json['FamilyPlanOperation'],
      finalComment: json['FinalComment'],
      finalComment1: json['FinalComment1'],
      finalRemark: json['FinalRemark'],
      gITComment: json['GITComment'],
      gender: json['Gender'],
      gynecProblem: json['GynecProblem'],
      healthCardNo: json['HealthCardNo'],
      heightCMs: json['Height_CMs'],
      iSBreastCancer: json['ISBreastCancer'],
      isAlcohol: json['IsAlcohol'],
      isDrug: json['IsDrug'],
      isPregnant: json['IsPregnant'],
      isSmoking: json['IsSmoking'],
      isTobaco: json['IsTobaco'],
      lMP: json['LMP'],
      lMPDate: json['LMPDate'],
      leftRemark: json['LeftRemark'],
      lungClear: json['LungClear'],
      lungComment: json['LungComment'],
      maritalStatus: json['MaritalStatus'],
      massperAbdomen: json['MassperAbdomen'],
      mobNo: json['MobNo'],
      motor: json['Motor'],
      nearVisionRemark: json['NearVisionRemark'],
      nearVisionRemarkfooter: json['NearVisionRemarkfooter'],
      noOfChildren: json['NoOfChildren'],
      otherAbnormality: json['OtherAbnormality'],
      palpationabdomen: json['Palpationabdomen'],
      patBarCode: json['PatBarCode'],
      patwellOriented: json['PatwellOriented'],
      physicalDoctor: json['PhysicalDoctor'],
      pregComment: json['PregComment'],
      pulseRate: json['PulseRate'],
      recentDelivery: json['RecentDelivery'],
      rightRemark: json['RightRemark'],
      sIS2Normal: json['SIS2Normal'],
      sensory: json['Sensory'],
      siteAddress: json['SiteAddress'],
      siteId: json['SiteId'],
      smoking: json['Smoking'],
      smokingSinceMonth: json['SmokingSinceMonth'],
      smokingSinceYear: json['SmokingSinceYear'],
      suggestion: json['Suggestion'],
      systolic: json['Systolic'],
      temparture: json['Temparture'],
      tobaco: json['Tobaco'],
      tobacoSinceMonth: json['TobacoSinceMonth'],
      tobacoSinceYear: json['TobacoSinceYear'],
      underWT: json['UnderWT'],
      visionInjuryEvidenceLeft: json['Vision_Injury_Evidence_Left'],
      visionInjuryEvidenceRight: json['Vision_Injury_Evidence_right'],
      visionSnellelchartL: json['Vision_Snellelchart_L'],
      visionSnellelchartL1: json['Vision_Snellelchart_L1'],
      visionSnellelchartR: json['Vision_Snellelchart_R'],
      weightKGs: json['Weight_KGs'],
      iSGlasses: json['iSGlasses'],
      otherRemark: json['other_remark'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'AbnormalCheck': abnormalCheck,
      'AbnormalComment': abnormalComment,
      'AbnormalSound': abnormalSound,
      'Age': age,
      'Alcohol': alcohol,
      'AlcoholSinceMonth': alcoholSinceMonth,
      'AlcoholSinceYear': alcoholSinceYear,
      'AnyMurmurs': anyMurmurs,
      'Anyhernia': anyhernia,
      'BMI': bMI,
      'BMIStatus': bMIStatus,
      'BMIStatus1': bMIStatus1,
      'BeneficiaryName': beneficiaryName,
      'BeneficiaryRegdNo': beneficiaryRegdNo,
      'BloodGroup': bloodGroup,
      'BloodPressure': bloodPressure,
      'BloodPressure_High': bloodPressureHigh,
      'BloodPressure_Low': bloodPressureLow,
      'BloodSugar_F': bloodSugarF,
      'BloodSugar_PP': bloodSugarPP,
      'BloodSugar_R': bloodSugarR,
      'CNSComment': cNSComment,
      'CVSComment': cVSComment,
      'CampId': campId,
      'CampLocation': campLocation,
      'Campdate': campdate,
      'Comment': comment,
      'DeliveryComment': deliveryComment,
      'DeliveryDate': deliveryDate,
      'Diastolic': diastolic,
      'DrugSinceMonth': drugSinceMonth,
      'DrugSinceYear': drugSinceYear,
      'EvidanceThyroid': evidanceThyroid,
      'FSH': fSH,
      'FSHComment': fSHComment,
      'FamillyAdvice': famillyAdvice,
      'FamillyPlaning': famillyPlaning,
      'FamilyPlanOperation': familyPlanOperation,
      'FinalComment': finalComment,
      'FinalComment1': finalComment1,
      'FinalRemark': finalRemark,
      'GITComment': gITComment,
      'Gender': gender,
      'GynecProblem': gynecProblem,
      'HealthCardNo': healthCardNo,
      'Height_CMs': heightCMs,
      'ISBreastCancer': iSBreastCancer,
      'IsAlcohol': isAlcohol,
      'IsDrug': isDrug,
      'IsPregnant': isPregnant,
      'IsSmoking': isSmoking,
      'IsTobaco': isTobaco,
      'LMP': lMP,
      'LMPDate': lMPDate,
      'LeftRemark': leftRemark,
      'LungClear': lungClear,
      'LungComment': lungComment,
      'MaritalStatus': maritalStatus,
      'MassperAbdomen': massperAbdomen,
      'MobNo': mobNo,
      'Motor': motor,
      'NearVisionRemark': nearVisionRemark,
      'NearVisionRemarkfooter': nearVisionRemarkfooter,
      'NoOfChildren': noOfChildren,
      'OtherAbnormality': otherAbnormality,
      'Palpationabdomen': palpationabdomen,
      'PatBarCode': patBarCode,
      'PatwellOriented': patwellOriented,
      'PhysicalDoctor': physicalDoctor,
      'PregComment': pregComment,
      'PulseRate': pulseRate,
      'RecentDelivery': recentDelivery,
      'RightRemark': rightRemark,
      'SIS2Normal': sIS2Normal,
      'Sensory': sensory,
      'SiteAddress': siteAddress,
      'SiteId': siteId,
      'Smoking': smoking,
      'SmokingSinceMonth': smokingSinceMonth,
      'SmokingSinceYear': smokingSinceYear,
      'Suggestion': suggestion,
      'Systolic': systolic,
      'Temparture': temparture,
      'Tobaco': tobaco,
      'TobacoSinceMonth': tobacoSinceMonth,
      'TobacoSinceYear': tobacoSinceYear,
      'UnderWT': underWT,
      'Vision_Injury_Evidence_Left': visionInjuryEvidenceLeft,
      'Vision_Injury_Evidence_right': visionInjuryEvidenceRight,
      'Vision_Snellelchart_L': visionSnellelchartL,
      'Vision_Snellelchart_L1': visionSnellelchartL1,
      'Vision_Snellelchart_R': visionSnellelchartR,
      'Weight_KGs': weightKGs,
      'iSGlasses': iSGlasses,
      'other_remark': otherRemark,
    };
  }
}
