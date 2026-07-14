// ignore_for_file: file_names

import 'package:flutter/widgets.dart';

class ConsumableListDetailsResponse {
  String? status;
  String? message;
  List<ConsumableOutput>? output;

  ConsumableListDetailsResponse({this.status, this.message, this.output});

  ConsumableListDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ConsumableOutput>[];
      json['output'].forEach((v) {
        output!.add(ConsumableOutput.fromJson(v));
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

class ConsumableOutput {
  int? consumableID;
  String? consumableName;
  bool? isActive;
  int? createdBy;
  int? totalCount;
  TextEditingController textEditingController = TextEditingController();

  ConsumableOutput({
    this.consumableID,
    this.consumableName,
    this.isActive,
    this.createdBy,
    this.totalCount,
  });

  ConsumableOutput.fromJson(Map<String, dynamic> json) {
    consumableID = json['ConsumableID'];
    consumableName = json['ConsumableName'];
    isActive = json['IsActive'];
    createdBy = json['CreatedBy'];
    totalCount = json['TotalCount'];
    textEditingController.text = "${totalCount ?? 0}";
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['ConsumableID'] = consumableID;
    data['ConsumableName'] = consumableName;
    data['IsActive'] = isActive;
    data['CreatedBy'] = createdBy;
    data['TotalCount'] = totalCount;
    return data;
  }
}
