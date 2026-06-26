import '../SubResourceListResponse/SubResourceListResponse.dart';

class ResourceListResponse {
  String? status;
  String? message;
  List<ResourceOutput>? output;

  ResourceListResponse({this.status, this.message, this.output});

  ResourceListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ResourceOutput>[];
      json['output'].forEach((v) {
        output!.add(ResourceOutput.fromJson(v));
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

class ResourceOutput {
  int? testId;
  String? testName;
  String? shortTestName;
  int? createdBy;
  String? createdDate;
  int? modifiedBy;
  String? modifiedDate;
  int? isTest;
  int? isCompulsary;
  String? displayName;
  int? isActive;
  int? sqNo;
  int? iSD2dTestMap;
  List<SubResourceOutput> subResourceList = [];
  ResourceOutput({
    this.testId,
    this.testName,
    this.shortTestName,
    this.createdBy,
    this.createdDate,
    this.modifiedBy,
    this.modifiedDate,
    this.isTest,
    this.isCompulsary,
    this.displayName,
    this.isActive,
    this.sqNo,
    this.iSD2dTestMap,
  });

  ResourceOutput.fromJson(Map<String, dynamic> json) {
    testId = json['TestId'];
    testName = json['TestName'];
    shortTestName = json['ShortTestName'];
    createdBy = json['CreatedBy'];
    createdDate = json['CreatedDate'];
    modifiedBy = json['ModifiedBy'];
    modifiedDate = json['ModifiedDate'];
    isTest = json['IsTest'];
    isCompulsary = json['IsCompulsary'];
    displayName = json['DisplayName'];
    isActive = json['isActive'];
    sqNo = json['SqNo'];
    iSD2dTestMap = json['ISD2dTestMap'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['TestId'] = testId;
    data['TestName'] = testName;
    data['ShortTestName'] = shortTestName;
    data['CreatedBy'] = createdBy;
    data['CreatedDate'] = createdDate;
    data['ModifiedBy'] = modifiedBy;
    data['ModifiedDate'] = modifiedDate;
    data['IsTest'] = isTest;
    data['IsCompulsary'] = isCompulsary;
    data['DisplayName'] = displayName;
    data['isActive'] = isActive;
    data['SqNo'] = sqNo;
    data['ISD2dTestMap'] = iSD2dTestMap;
    return data;
  }
}
