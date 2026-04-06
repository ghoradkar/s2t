// export 'package:s2toperational/Modules/Json_Class/CampCloseCampDetailsResponse/CampCloseCampDetailsResponse.dart';
// export 'package:s2toperational/Modules/Json_Class/CampCloseDetailsResponse/CampCloseDetailsResponse.dart';
// export 'package:s2toperational/Modules/Json_Class/ConsumableListDetailsResponse/ConsumableListDetailsResponse.dart';

// ignore_for_file: file_names

import 'package:flutter/material.dart';

class CampCloseCampDetailsResponse {
  String? status;
  String? message;
  List<CampCloseCampDetailsOutput>? output;

  CampCloseCampDetailsResponse({this.status, this.message, this.output});

  CampCloseCampDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampCloseCampDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(CampCloseCampDetailsOutput.fromJson(v));
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

class CampCloseCampDetailsOutput {
  int? dISTLGDCODE;
  String? dISTNAME;
  int? scheduledCamps;
  int? cAMPID;
  String? campNo;
  String? campLocation;
  String? campDate;
  String? campStatus;
  int? conductedCamps;
  int? facilitatedWorkers;
  int? registration;
  int? basicDetails;
  int? physicalExamination;
  int? lungFunctioinTest;
  int? audioScreeningTest;
  int? visionScreening;
  int? barcode;
  int? pPSampleCollection;
  int? ackowledgement;
  int? urineSampleCollection;
  int? questionnaireDetails;
  int? breastScreening;
  int? rTPCR;
  int? antigen;
  int? totalTests;
  int? todayConductedCamps;
  int? todayFacilitatedWorkers;
  int? rejectedBeneficiaries;
  int? approvedBeneficiaries;
  int? holdBeneficiaries;
  int? verifiedBeneficiaries;

  CampCloseCampDetailsOutput({
    this.dISTLGDCODE,
    this.dISTNAME,
    this.scheduledCamps,
    this.cAMPID,
    this.campNo,
    this.campLocation,
    this.campDate,
    this.campStatus,
    this.conductedCamps,
    this.facilitatedWorkers,
    this.registration,
    this.basicDetails,
    this.physicalExamination,
    this.lungFunctioinTest,
    this.audioScreeningTest,
    this.visionScreening,
    this.barcode,
    this.pPSampleCollection,
    this.ackowledgement,
    this.urineSampleCollection,
    this.questionnaireDetails,
    this.breastScreening,
    this.rTPCR,
    this.antigen,
    this.totalTests,
    this.todayConductedCamps,
    this.todayFacilitatedWorkers,
    this.rejectedBeneficiaries,
    this.approvedBeneficiaries,
    this.holdBeneficiaries,
    this.verifiedBeneficiaries,
  });

  // Native ScrenningTestModel stores all count fields as String ("25" not 25).
  // This helper handles both string-encoded and actual int values from the API.
  static int? _toInt(dynamic v) {
    if (v == null) return null;
    if (v is int) return v;
    return int.tryParse(v.toString());
  }

  CampCloseCampDetailsOutput.fromJson(Map<String, dynamic> json) {
    dISTLGDCODE = _toInt(json['DISTLGDCODE']);
    dISTNAME = json['DISTNAME'];
    scheduledCamps = _toInt(json['ScheduledCamps']);
    cAMPID = _toInt(json['CAMPID']);
    campNo = json['CampNo'];
    campLocation = json['CampLocation'];
    campDate = json['CampDate'];
    campStatus = json['CampStatus'];
    conductedCamps = _toInt(json['ConductedCamps']);
    facilitatedWorkers = _toInt(json['FacilitatedWorkers']);
    registration = _toInt(json['Registration']);
    basicDetails = _toInt(json['BasicDetails']);
    physicalExamination = _toInt(json['PhysicalExamination']);
    lungFunctioinTest = _toInt(json['LungFunctioinTest']);
    audioScreeningTest = _toInt(json['AudioScreeningTest']);
    visionScreening = _toInt(json['VisionScreening']);
    barcode = _toInt(json['Barcode']);
    pPSampleCollection = _toInt(json['PPSampleCollection']);
    ackowledgement = _toInt(json['Ackowledgement']);
    urineSampleCollection = _toInt(json['UrineSampleCollection']);
    questionnaireDetails = _toInt(json['Questionnaire Details']);
    breastScreening = _toInt(json['BreastScreening']);
    rTPCR = _toInt(json['RTPCR']);
    antigen = _toInt(json['Antigen']);
    totalTests = _toInt(json['TotalTests']);
    todayConductedCamps = _toInt(json['TodayConductedCamps']);
    todayFacilitatedWorkers = _toInt(json['TodayFacilitatedWorkers']);
    rejectedBeneficiaries = _toInt(json['RejectedBeneficiaries']);
    approvedBeneficiaries = _toInt(json['ApprovedBeneficiaries']);
    holdBeneficiaries = _toInt(json['HoldBeneficiaries']);
    verifiedBeneficiaries = _toInt(json['VerifiedBeneficiaries']);
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['DISTNAME'] = dISTNAME;
    data['ScheduledCamps'] = scheduledCamps;
    data['CAMPID'] = cAMPID;
    data['CampNo'] = campNo;
    data['CampLocation'] = campLocation;
    data['CampDate'] = campDate;
    data['CampStatus'] = campStatus;
    data['ConductedCamps'] = conductedCamps;
    data['FacilitatedWorkers'] = facilitatedWorkers;
    data['Registration'] = registration;
    data['BasicDetails'] = basicDetails;
    data['PhysicalExamination'] = physicalExamination;
    data['LungFunctioinTest'] = lungFunctioinTest;
    data['AudioScreeningTest'] = audioScreeningTest;
    data['VisionScreening'] = visionScreening;
    data['Barcode'] = barcode;
    data['PPSampleCollection'] = pPSampleCollection;
    data['Ackowledgement'] = ackowledgement;
    data['UrineSampleCollection'] = urineSampleCollection;
    data['Questionnaire Details'] = questionnaireDetails;
    data['BreastScreening'] = breastScreening;
    data['RTPCR'] = rTPCR;
    data['Antigen'] = antigen;
    data['TotalTests'] = totalTests;
    data['TodayConductedCamps'] = todayConductedCamps;
    data['TodayFacilitatedWorkers'] = todayFacilitatedWorkers;
    data['RejectedBeneficiaries'] = rejectedBeneficiaries;
    data['ApprovedBeneficiaries'] = approvedBeneficiaries;
    data['HoldBeneficiaries'] = holdBeneficiaries;
    data['VerifiedBeneficiaries'] = verifiedBeneficiaries;
    return data;
  }
}


class CampCloseDetailsResponse {
  String? status;
  String? message;
  List<CampCloseDetailsOutput>? output;

  CampCloseDetailsResponse({this.status, this.message, this.output});

  CampCloseDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampCloseDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(CampCloseDetailsOutput.fromJson(v));
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

class CampCloseDetailsOutput {
  int? florideCount;
  int? gelTubeCount;
  int? plainTubeCount;
  int? sampleCollectionCount;
  int? sampleSendToHomeLabCount;
  int? sampleSendToHubLabCount;
  int? totalBenificiary;
  int? urineContainer;
  int? campCloseUserid;

  CampCloseDetailsOutput({
    this.florideCount,
    this.gelTubeCount,
    this.plainTubeCount,
    this.sampleCollectionCount,
    this.sampleSendToHomeLabCount,
    this.sampleSendToHubLabCount,
    this.totalBenificiary,
    this.urineContainer,
    this.campCloseUserid,
  });

  CampCloseDetailsOutput.fromJson(Map<String, dynamic> json) {
    florideCount = json['FlorideCount'];
    gelTubeCount = json['GelTubeCount'];
    plainTubeCount = json['PlainTubeCount'];
    sampleCollectionCount = json['SampleCollectionCount'];
    sampleSendToHomeLabCount = json['SampleSendToHomeLabCount'];
    sampleSendToHubLabCount = json['SampleSendToHubLabCount'];
    totalBenificiary = json['TotalBenificiary'];
    urineContainer = json['UrineContainer'];
    campCloseUserid = json['CampCloseUserid'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['FlorideCount'] = florideCount;
    data['GelTubeCount'] = gelTubeCount;
    data['PlainTubeCount'] = plainTubeCount;
    data['SampleCollectionCount'] = sampleCollectionCount;
    data['SampleSendToHomeLabCount'] = sampleSendToHomeLabCount;
    data['SampleSendToHubLabCount'] = sampleSendToHubLabCount;
    data['TotalBenificiary'] = totalBenificiary;
    data['UrineContainer'] = urineContainer;
    data['CampCloseUserid'] = campCloseUserid;
    return data;
  }
}

// ignore_for_file: file_names



class ConsumableListDetailsResponse {
  String? status;
  String? message;
  List<ConsumableOutput>? output;

  ConsumableListDetailsResponse({this.status, this.message, this.output});

  ConsumableListDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ConsumableOutput>[];
      json['output'].forEach((v) {
        output!.add(ConsumableOutput.fromJson(v));
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

class ConsumableOutput {
  int? consumableID;
  String? consumableName;
  bool? isActive;
  int? createdBy;
  int? totalCount;
  TextEditingController textEditingController = TextEditingController();

  ConsumableOutput({
    this.consumableID,
    this.consumableName,
    this.isActive,
    this.createdBy,
    this.totalCount,
  });

  static int? _toInt(dynamic v) {
    if (v == null) return null;
    if (v is int) return v;
    return int.tryParse(v.toString());
  }

  ConsumableOutput.fromJson(Map<String, dynamic> json) {
    consumableID = _toInt(json['ConsumableID']);
    consumableName = json['ConsumableName'];
    isActive = json['IsActive'];
    createdBy = _toInt(json['CreatedBy']);
    totalCount = _toInt(json['TotalCount']);
    textEditingController.text = "${totalCount ?? 0}";
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['ConsumableID'] = consumableID;
    data['ConsumableName'] = consumableName;
    data['IsActive'] = isActive;
    data['CreatedBy'] = createdBy;
    data['TotalCount'] = totalCount;
    return data;
  }
}

