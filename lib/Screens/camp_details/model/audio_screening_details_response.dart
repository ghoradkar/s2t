// ignore_for_file: file_names

class AudioScreeningDetailsResponse {
  String? status;
  String? message;
  List<AudioScreeningDetailsOutput>? output;

  AudioScreeningDetailsResponse({this.status, this.message, this.output});

  AudioScreeningDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AudioScreeningDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(AudioScreeningDetailsOutput.fromJson(v));
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

class AudioScreeningDetailsOutput {
  double? frequency;
  double? rightDB;
  double? leftDB;
  String? remark;
  String? rightRemark;

  AudioScreeningDetailsOutput({
    this.frequency,
    this.rightDB,
    this.leftDB,
    this.remark,
    this.rightRemark,
  });

  AudioScreeningDetailsOutput.fromJson(Map<String, dynamic> json) {
    frequency = json['Frequency'];
    rightDB = json['Right_DB'];
    leftDB = json['Left_DB'];
    remark = json['Remark'];
    rightRemark = json['Right_Remark'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Frequency'] = frequency;
    data['Right_DB'] = rightDB;
    data['Left_DB'] = leftDB;
    data['Remark'] = remark;
    data['Right_Remark'] = rightRemark;
    return data;
  }
}
