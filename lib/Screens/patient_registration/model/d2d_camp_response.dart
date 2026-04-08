// ignore_for_file: file_names

class D2DCampResponse {
  String? status;
  String? message;
  List<D2DCampOutput>? output;

  D2DCampResponse({this.status, this.message, this.output});

  D2DCampResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <D2DCampOutput>[];
      json['output'].forEach((v) {
        output!.add(D2DCampOutput.fromJson(v));
      });
    }
  }
}

class D2DCampOutput {
  String? campId;
  String? campLocation;
  String? siteDetailId;
  String? distLgdCode;
  String? isCampClosed;

  D2DCampOutput({
    this.campId,
    this.campLocation,
    this.siteDetailId,
    this.distLgdCode,
    this.isCampClosed,
  });

  D2DCampOutput.fromJson(Map<String, dynamic> json) {
    campId = json['CampId']?.toString();
    campLocation = json['CampLocation']?.toString();
    siteDetailId = json['SiteDetailId']?.toString();
    distLgdCode = json['DistlgdCode']?.toString();
    isCampClosed = json['IsCampClosed']?.toString();
  }

  bool get closed => isCampClosed == '1';
}
