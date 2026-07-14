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
  String? campId;
  String? campConfirmation;
  bool isSelected = false;

  CampListOutput({this.campId, this.campConfirmation});

  CampListOutput.fromJson(Map<String, dynamic> json) {
    campId = json['CampId']?.toString();
    campConfirmation = json['CampConfirmation']?.toString();
  }

  bool get isConfirmed => campConfirmation == '1';
}
