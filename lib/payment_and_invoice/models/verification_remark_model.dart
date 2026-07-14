class VerificationRemarkResponse {
  String? status;
  String? message;
  List<VerificationRemarkOutput>? output;

  VerificationRemarkResponse({this.status, this.message, this.output});

  VerificationRemarkResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <VerificationRemarkOutput>[];
      json['output'].forEach((v) {
        output!.add(VerificationRemarkOutput.fromJson(v));
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

class VerificationRemarkOutput {
  int? verificationRemarkID;
  String? verificationRemark;
  int? rankID;

  VerificationRemarkOutput({
    this.verificationRemarkID,
    this.verificationRemark,
    this.rankID,
  });

  VerificationRemarkOutput.fromJson(Map<String, dynamic> json) {
    verificationRemarkID = json['VerificationRemarkID'];
    verificationRemark = json['VerificationRemark'];
    rankID = json['RankID'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['VerificationRemarkID'] = verificationRemarkID;
    data['VerificationRemark'] = verificationRemark;
    data['RankID'] = rankID;
    return data;
  }
}
