// ignore_for_file: file_names

class TestListForRejectResponse {
  String? status;
  String? message;
  List<TestListForRejectOutput>? output;

  TestListForRejectResponse({this.status, this.message, this.output});

  TestListForRejectResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TestListForRejectOutput>[];
      json['output'].forEach((v) {
        output!.add(TestListForRejectOutput.fromJson(v));
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

class TestListForRejectOutput {
  int? testId;
  String? testName;
  int? isCompulsary;
  bool isSelected = false;

  TestListForRejectOutput({this.testId, this.testName, this.isCompulsary});

  TestListForRejectOutput.fromJson(Map<String, dynamic> json) {
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
