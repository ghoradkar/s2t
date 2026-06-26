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
