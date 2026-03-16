// ignore_for_file: file_names

class PatientCheckupAnalysisReportResponse {
  String? status;
  String? message;
  List<PatientCheckupAnalysisReportOutput>? output;

  PatientCheckupAnalysisReportResponse({
    this.status,
    this.message,
    this.output,
  });

  PatientCheckupAnalysisReportResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <PatientCheckupAnalysisReportOutput>[];
      json['output'].forEach((v) {
        output!.add(PatientCheckupAnalysisReportOutput.fromJson(v));
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

class PatientCheckupAnalysisReportOutput {
  String? gender;
  int? campId;
  int? regdId;
  String? patientName;
  int? regdNo;
  String? registration;
  String? basicDetails;
  String? physicalExamination;
  String? lungFunctioinTest;
  String? audioScreeningTest;
  String? visionScreening;
  String? barcode;
  String? pPSampleCollection;
  String? ackowledgement;
  String? urineSampleCollection;
  String? questionnaireDetails;
  String? breastScreening;
  String? rTPCR;
  String? antigen;

  PatientCheckupAnalysisReportOutput({
    this.gender,
    this.campId,
    this.regdId,
    this.patientName,
    this.regdNo,
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
  });

  PatientCheckupAnalysisReportOutput.fromJson(Map<String, dynamic> json) {
    gender = json['Gender'];
    campId = json['CampId'];
    regdId = json['RegdId'];
    patientName = json['PatientName'];
    regdNo = json['RegdNo'];
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
    breastScreening = json['Breast Screening'];
    rTPCR = json['RTPCR'];
    antigen = json['Antigen'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Gender'] = gender;
    data['CampId'] = campId;
    data['RegdId'] = regdId;
    data['PatientName'] = patientName;
    data['RegdNo'] = regdNo;
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
    data['Breast Screening'] = breastScreening;
    data['RTPCR'] = rTPCR;
    data['Antigen'] = antigen;
    return data;
  }
}
