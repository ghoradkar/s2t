// ignore_for_file: file_names

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

  CampCloseCampDetailsOutput.fromJson(Map<String, dynamic> json) {
    dISTLGDCODE = json['DISTLGDCODE'];
    dISTNAME = json['DISTNAME'];
    scheduledCamps = json['ScheduledCamps'];
    cAMPID = json['CAMPID'];
    campNo = json['CampNo'];
    campLocation = json['CampLocation'];
    campDate = json['CampDate'];
    campStatus = json['CampStatus'];
    conductedCamps = json['ConductedCamps'];
    facilitatedWorkers = json['FacilitatedWorkers'];
    registration = json['Registration'];
    basicDetails = json['BasicDetails'];
    physicalExamination = json['PhysicalExamination'];
    lungFunctioinTest = json['LungFunctioinTest'];
    audioScreeningTest = json['AudioScreeningTest'];
    visionScreening = json['VisionScreening'];
    barcode = json['Barcode'];
    pPSampleCollection = json['PPSampleCollection'];
    ackowledgement = json['Ackowledgement'];
    urineSampleCollection = json['UrineSampleCollection'];
    questionnaireDetails = json['Questionnaire Details'];
    breastScreening = json['BreastScreening'];
    rTPCR = json['RTPCR'];
    antigen = json['Antigen'];
    totalTests = json['TotalTests'];
    todayConductedCamps = json['TodayConductedCamps'];
    todayFacilitatedWorkers = json['TodayFacilitatedWorkers'];
    rejectedBeneficiaries = json['RejectedBeneficiaries'];
    approvedBeneficiaries = json['ApprovedBeneficiaries'];
    holdBeneficiaries = json['HoldBeneficiaries'];
    verifiedBeneficiaries = json['VerifiedBeneficiaries'];
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
