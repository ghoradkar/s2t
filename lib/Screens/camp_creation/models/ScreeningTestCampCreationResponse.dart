// ignore_for_file: file_names

class ScreeningTestCampCreationResponse {
  String? status;
  String? message;
  List<ScreeningTestCampCreationOutput>? output;

  ScreeningTestCampCreationResponse({this.status, this.message, this.output});

  ScreeningTestCampCreationResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ScreeningTestCampCreationOutput>[];
      json['output'].forEach((v) {
        output!.add(ScreeningTestCampCreationOutput.fromJson(v));
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

class ScreeningTestCampCreationOutput {
  int? testId;
  String? testName;
  int? isCompulsary;
  bool isSelected = false;
  ScreeningTestCampCreationOutput(
      {this.testId, this.testName, this.isCompulsary});

  ScreeningTestCampCreationOutput.fromJson(Map<String, dynamic> json) {
    testId = json['TestId'];
    testName = json['TestName'];
    isCompulsary = json['IsCompulsary'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['TestId'] = testId;
    data['TestName'] = testName;
    data['IsCompulsary'] = isCompulsary;
    return data;
  }
}
