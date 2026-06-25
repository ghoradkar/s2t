// ignore_for_file: file_names

class YearsResponse {
  String? status;
  String? message;
  List<YearsOutput>? output;

  YearsResponse({this.status, this.message, this.output});

  YearsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <YearsOutput>[];
      json['output'].forEach((v) {
        output!.add(YearsOutput.fromJson(v));
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

class YearsOutput {
  int? yearID;
  String? yearName;
  bool isSelected = false;

  YearsOutput({this.yearID, this.yearName});

  YearsOutput.fromJson(Map<String, dynamic> json) {
    yearID = json['YearID'];
    yearName = json['Year_Name'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['YearID'] = yearID;
    data['Year_Name'] = yearName;
    return data;
  }
}
