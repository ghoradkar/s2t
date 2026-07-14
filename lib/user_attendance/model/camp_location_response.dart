// ignore_for_file: file_names

class CampLocationResponse {
  String? status;
  String? message;
  List<CampLocationOutput>? output;

  CampLocationResponse({this.status, this.message, this.output});

  CampLocationResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampLocationOutput>[];
      json['output'].forEach((v) {
        output!.add(CampLocationOutput.fromJson(v));
      });
    }
  }
}

class CampLocationOutput {
  double? latitude;
  double? longitude;

  CampLocationOutput({this.latitude, this.longitude});

  CampLocationOutput.fromJson(Map<String, dynamic> json) {
    latitude = double.tryParse(json['Lattitude']?.toString() ?? '');
    longitude = double.tryParse(json['Longitude']?.toString() ?? '');
  }
}