class SpecimenTypeResponse {
  String? status;
  String? message;
  List<SpecimenTypeOutput>? output;

  SpecimenTypeResponse({this.status, this.message, this.output});

  SpecimenTypeResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <SpecimenTypeOutput>[];
      for (final v in json['output']) {
        output!.add(SpecimenTypeOutput.fromJson(v));
      }
    }
  }
}

class SpecimenTypeOutput {
  String? specTypeId;
  String? specType;

  SpecimenTypeOutput({this.specTypeId, this.specType});

  SpecimenTypeOutput.fromJson(Map<String, dynamic> json) {
    specTypeId = json['SPECTYPEID']?.toString();
    specType = json['SPECTYPE']?.toString();
  }
}
