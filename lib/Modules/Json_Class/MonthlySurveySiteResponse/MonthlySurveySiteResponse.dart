class MonthlySurveySiteResponse {
  String? status;
  String? message;
  List<MonthlySurveySiteOutput>? output;

  MonthlySurveySiteResponse({this.status, this.message, this.output});

  MonthlySurveySiteResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <MonthlySurveySiteOutput>[];
      json['output'].forEach((v) {
        output!.add(new MonthlySurveySiteOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    if (this.output != null) {
      data['output'] = this.output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class MonthlySurveySiteOutput {
  int? campId;
  String? campTypeDescription;
  String? campStatus;
  String? cordinatorName;
  String? mOBNO;
  String? campNo;
  String? campLocation;
  String? campDate;
  String? status;
  String? description;
  int? dISTLGDCODE;
  String? dISTNAME;
  int? rEGISTERWORKERS;
  String? surveyCoordinatorName;
  String? cordinatorName1;
  String? mOBNO1;
  String? campName;
  int? total;
  int? screeningDone;
  int? screeningNotDone;

  MonthlySurveySiteOutput({
    this.campId,
    this.campTypeDescription,
    this.campStatus,
    this.cordinatorName,
    this.mOBNO,
    this.campNo,
    this.campLocation,
    this.campDate,
    this.status,
    this.description,
    this.dISTLGDCODE,
    this.dISTNAME,
    this.rEGISTERWORKERS,
    this.surveyCoordinatorName,
    this.cordinatorName1,
    this.mOBNO1,
    this.campName,
    this.total,
    this.screeningDone,
    this.screeningNotDone,
  });

  MonthlySurveySiteOutput.fromJson(Map<String, dynamic> json) {
    campId = json['CampId'];
    campTypeDescription = json['CampTypeDescription'];
    campStatus = json['CampStatus'];
    cordinatorName = json['CordinatorName'];
    mOBNO = json['MOBNO'];
    campNo = json['CampNo'];
    campLocation = json['CampLocation'];
    campDate = json['CampDate'];
    status = json['Status'];
    description = json['Description'];
    dISTLGDCODE = json['DISTLGDCODE'];
    dISTNAME = json['DISTNAME'];
    rEGISTERWORKERS = json['REGISTERWORKERS'];
    surveyCoordinatorName = json['SurveyCoordinatorName'];
    cordinatorName1 = json['CordinatorName1'];
    mOBNO1 = json['MOBNO1'];
    campName = json['CampName'];
    total = json['Total'];
    screeningDone = json['ScreeningDone'];
    screeningNotDone = json['ScreeningNotDone'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['CampId'] = this.campId;
    data['CampTypeDescription'] = this.campTypeDescription;
    data['CampStatus'] = this.campStatus;
    data['CordinatorName'] = this.cordinatorName;
    data['MOBNO'] = this.mOBNO;
    data['CampNo'] = this.campNo;
    data['CampLocation'] = this.campLocation;
    data['CampDate'] = this.campDate;
    data['Status'] = this.status;
    data['Description'] = this.description;
    data['DISTLGDCODE'] = this.dISTLGDCODE;
    data['DISTNAME'] = this.dISTNAME;
    data['REGISTERWORKERS'] = this.rEGISTERWORKERS;
    data['SurveyCoordinatorName'] = this.surveyCoordinatorName;
    data['CordinatorName1'] = this.cordinatorName1;
    data['MOBNO1'] = this.mOBNO1;
    data['CampName'] = this.campName;
    data['Total'] = this.total;
    data['ScreeningDone'] = this.screeningDone;
    data['ScreeningNotDone'] = this.screeningNotDone;
    return data;
  }
}
