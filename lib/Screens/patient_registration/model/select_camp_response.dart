// ignore_for_file: file_names

class SelectCampResponse {
  String? status;
  String? message;
  List<SelectCampOutput>? output;

  SelectCampResponse({this.status, this.message, this.output});

  SelectCampResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <SelectCampOutput>[];
      json['output'].forEach((v) {
        output!.add(SelectCampOutput.fromJson(v));
      });
    }
  }
}

class SelectCampOutput {
  String? campId;
  String? distName;
  String? siteDetailId;
  String? distLgdCode;
  String? campType;
  String? campLocation;
  String? campTypeDescription;
  String? initiatedBy1;

  SelectCampOutput({
    this.campId,
    this.distName,
    this.siteDetailId,
    this.distLgdCode,
    this.campType,
    this.campLocation,
    this.campTypeDescription,
    this.initiatedBy1,
  });

  SelectCampOutput.fromJson(Map<String, dynamic> json) {
    campId = json['CampId']?.toString();
    distName = json['DISTNAME']?.toString();
    siteDetailId = json['SiteDetailId']?.toString();
    distLgdCode = json['DISTLGDCODE']?.toString();
    campType = json['CampType']?.toString();
    campLocation = json['CampLocation']?.toString();
    campTypeDescription = json['CampTypeDescription']?.toString();
    initiatedBy1 = json['InitiatedBy1']?.toString();
  }
}
