// ignore_for_file: file_names

class CampListResponse {
  String? status;
  String? message;
  List<CampListOutput>? output;

  CampListResponse({this.status, this.message, this.output});

  CampListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampListOutput>[];
      json['output'].forEach((v) {
        output!.add(CampListOutput.fromJson(v));
      });
    }
  }
}

class CampListOutput {
  int? campId;
  String? campDate;
  String? campName;
  String? campNo;
  int? campType;

  CampListOutput({
    this.campId,
    this.campDate,
    this.campName,
    this.campNo,
    this.campType,
  });

  CampListOutput.fromJson(Map<String, dynamic> json) {
    campId = json['CampId'];
    campDate = json['CampDate'];
    campName = json['CampName'];
    campNo = json['CampNo'];
    campType = json['CampType'];
  }

  String get displayLabel => campName ?? campNo ?? 'Camp $campId';
}
