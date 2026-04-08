// ignore_for_file: file_names

class DistrictListResponse {
  String? status;
  String? message;
  List<DistrictOutput>? output;

  DistrictListResponse({this.status, this.message, this.output});

  DistrictListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <DistrictOutput>[];
      json['output'].forEach((v) {
        output!.add(DistrictOutput.fromJson(v));
      });
    }
  }
}

class DistrictOutput {
  String? distLgdCode;
  String? distName;

  DistrictOutput({this.distLgdCode, this.distName});

  DistrictOutput.fromJson(Map<String, dynamic> json) {
    distLgdCode = json['DISTLGDCODE']?.toString();
    distName = json['DISTNAME']?.toString();
  }
}
