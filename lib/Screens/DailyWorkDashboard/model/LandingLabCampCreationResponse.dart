// export 'package:s2toperational/Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';

class LandingLabCampCreationResponse {
  String? status;
  String? message;
  List<LandingLabCampCreationOutput>? output;

  LandingLabCampCreationResponse({this.status, this.message, this.output});

  LandingLabCampCreationResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <LandingLabCampCreationOutput>[];
      json['output'].forEach((v) {
        output!.add(LandingLabCampCreationOutput.fromJson(v));
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

class LandingLabCampCreationOutput {
  int? labCode;
  String? labName;
  bool isSelected = false;
  LandingLabCampCreationOutput({this.labCode, this.labName});

  LandingLabCampCreationOutput.fromJson(Map<String, dynamic> json) {
    labCode = json['LabCode'];
    labName = json['LabName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['LabCode'] = labCode;
    data['LabName'] = labName;
    return data;
  }
}
