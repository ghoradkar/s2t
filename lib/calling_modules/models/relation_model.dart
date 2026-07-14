class RelationModel {
  String? status;
  String? message;
  List<RelationOutput>? output;

  RelationModel({this.status, this.message, this.output});

  RelationModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <RelationOutput>[]
      ;
      json['output'].forEach((v) {
        output!.add(RelationOutput.fromJson(v));
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

class RelationOutput {
  int? relId;
  String? relName;
  String? relMName;

  RelationOutput({this.relId, this.relName, this.relMName});

  RelationOutput.fromJson(Map<String, dynamic> json) {
    relId = json['RelId'];
    relName = json['RelName'];
    relMName = json['RelMName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['RelId'] = relId;
    data['RelName'] = relName;
    data['RelMName'] = relMName;
    return data;
  }
}
