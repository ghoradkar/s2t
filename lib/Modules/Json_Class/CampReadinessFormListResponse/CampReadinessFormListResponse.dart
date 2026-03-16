// ignore_for_file: unnecessary_question_mark

class CampReadinessFormListResponse {
  String? status;
  String? message;
  List<CampReadinessFormOutput>? output;

  CampReadinessFormListResponse({this.status, this.message, this.output});

  CampReadinessFormListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampReadinessFormOutput>[];
      json['output'].forEach((v) {
        output!.add(CampReadinessFormOutput.fromJson(v));
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

class CampReadinessFormOutput {
  int? itemId;
  String? itemName;
  int? itemType;
  int? isActive;
  int? itemStatus;

  bool isAvailableSelected = false;
  bool isNotAvailableSelected = false;
  bool isNotWorkingSelected = false;

  CampReadinessFormOutput({
    this.itemId,
    this.itemName,
    this.itemType,
    this.isActive,
    this.itemStatus,
  });

  CampReadinessFormOutput.fromJson(Map<String, dynamic> json) {
    itemId = json['ItemId'];
    itemName = json['ItemName'];
    itemType = json['ItemType'];
    isActive = json['IsActive'];
    itemStatus = json['ItemStatus'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  <String, dynamic>{};
    data['ItemId'] = itemId;
    data['ItemName'] = itemName;
    data['ItemType'] = itemType;
    data['IsActive'] = isActive;
    data['ItemStatus'] = itemStatus;
    return data;
  }
}
