// ignore_for_file: file_names

class D2DTeamWisePhyExamDetailsResponse {
  String? status;
  String? message;
  List<D2DTeamWisePhyExamDetailsOutput>? output;

  D2DTeamWisePhyExamDetailsResponse({this.status, this.message, this.output});

  D2DTeamWisePhyExamDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <D2DTeamWisePhyExamDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(D2DTeamWisePhyExamDetailsOutput.fromJson(v));
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

class D2DTeamWisePhyExamDetailsOutput {
  int? dISTLGDCODE;
  String? district;
  int? campId;
  String? campCoordinatorName;
  String? campCoordinatorMobNo;
  String? campDate;
  String? teamNo;
  String? teamid;
  int? assigned;
  int? doctorID;
  int? callingPending;
  int? phyExamPending;

  D2DTeamWisePhyExamDetailsOutput({
    this.dISTLGDCODE,
    this.district,
    this.campId,
    this.campCoordinatorName,
    this.campCoordinatorMobNo,
    this.campDate,
    this.teamNo,
    this.teamid,
    this.assigned,
    this.doctorID,
    this.callingPending,
    this.phyExamPending,
  });

  D2DTeamWisePhyExamDetailsOutput.fromJson(Map<String, dynamic> json) {
    dISTLGDCODE = json['DISTLGDCODE'];
    district = json['District'];
    campId = json['CampId'];
    campCoordinatorName = json['CampCoordinatorName'];
    campCoordinatorMobNo = json['CampCoordinatorMobNo'];
    campDate = json['CampDate'];
    teamNo = json['TeamNo'];
    teamid = json['Teamid'];
    assigned = json['Assigned'];
    doctorID = json['DoctorID'];
    callingPending = json['CallingPending'];
    phyExamPending = json['PhyExamPending'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = Map<String, dynamic>();
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['District'] = district;
    data['CampId'] = campId;
    data['CampCoordinatorName'] = campCoordinatorName;
    data['CampCoordinatorMobNo'] = campCoordinatorMobNo;
    data['CampDate'] = campDate;
    data['TeamNo'] = teamNo;
    data['Teamid'] = teamid;
    data['Assigned'] = assigned;
    data['DoctorID'] = doctorID;
    data['CallingPending'] = callingPending;
    data['PhyExamPending'] = phyExamPending;
    return data;
  }
}
