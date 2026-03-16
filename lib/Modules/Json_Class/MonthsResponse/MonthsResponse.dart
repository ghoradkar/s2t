class MonthsResponse {
  String? status;
  String? message;
  List<MonthsOutput>? output;

  MonthsResponse({this.status, this.message, this.output});

  MonthsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <MonthsOutput>[];
      json['output'].forEach((v) {
        output!.add(MonthsOutput.fromJson(v));
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

class MonthsOutput {
  int? monthId;
  String? monthNameEng;
  bool isSelected = false;

  MonthsOutput({this.monthId, this.monthNameEng});

  MonthsOutput.fromJson(Map<String, dynamic> json) {
    monthId = json['Month_id'];
    monthNameEng = json['Month_Name_Eng'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Month_id'] = monthId;
    data['Month_Name_Eng'] = monthNameEng;
    return data;
  }
}
