// ignore_for_file: file_names

class D2DPhysicalExamDetailsResponse {
  String? status;
  String? message;
  List<D2DPhysicalExamDetailsOutput>? output;

  D2DPhysicalExamDetailsResponse({this.status, this.message, this.output});

  D2DPhysicalExamDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <D2DPhysicalExamDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(D2DPhysicalExamDetailsOutput.fromJson(v));
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

class D2DPhysicalExamDetailsOutput {
  int? dISTLGDCODE;
  String? district;
  int? campId;
  String? campDate;
  int? doctorID;
  int? assigned;
  int? callingPending;
  int? phyExamPending;

  D2DPhysicalExamDetailsOutput({
    this.dISTLGDCODE,
    this.district,
    this.campId,
    this.campDate,
    this.doctorID,
    this.assigned,
    this.callingPending,
    this.phyExamPending,
  });

  D2DPhysicalExamDetailsOutput.fromJson(Map<String, dynamic> json) {
    dISTLGDCODE = json['DISTLGDCODE'];
    district = json['District'];
    campId = json['CampId'];
    campDate = json['CampDate'];
    doctorID = json['DoctorID'];
    assigned = json['Assigned'];
    callingPending = json['CallingPending'];
    phyExamPending = json['PhyExamPending'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['District'] = district;
    data['CampId'] = campId;
    data['CampDate'] = campDate;
    data['DoctorID'] = doctorID;
    data['Assigned'] = assigned;
    data['CallingPending'] = callingPending;
    data['PhyExamPending'] = phyExamPending;
    return data;
  }
}
