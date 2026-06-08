class GetDocListD2DResponse {
  String? status;
  String? message;
  List<GetDocListD2DOutput>? output;

  GetDocListD2DResponse({this.status, this.message, this.output});

  GetDocListD2DResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <GetDocListD2DOutput>[];
      json['output'].forEach((v) {
        output!.add(GetDocListD2DOutput.fromJson(v));
      });
    }
  }
}

class GetDocListD2DOutput {
  int? userId;
  String? doctorName;
  int? docStatus;
  String? mobNo;

  GetDocListD2DOutput({this.userId, this.doctorName, this.docStatus, this.mobNo});

  GetDocListD2DOutput.fromJson(Map<String, dynamic> json) {
    userId = json['USERID'];
    doctorName = json['DoctorName'];
    docStatus = json['DocStatus'];
    mobNo = json['MOBNO'];
  }
}
