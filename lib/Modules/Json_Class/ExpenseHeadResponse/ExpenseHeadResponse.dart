class ExpenseHeadResponse {
  String? status;
  String? message;
  List<ExpenseHeaOutput>? output;

  ExpenseHeadResponse({this.status, this.message, this.output});

  ExpenseHeadResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ExpenseHeaOutput>[];
      json['output'].forEach((v) {
        output!.add( ExpenseHeaOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class ExpenseHeaOutput {
  int? expenseHead;
  String? expenseHeadName;
  String? createdOn;
  int? createdBy;
  int? isActive;
  bool isSelected = false;

  ExpenseHeaOutput({
    this.expenseHead,
    this.expenseHeadName,
    this.createdOn,
    this.createdBy,
    this.isActive,
  });

  ExpenseHeaOutput.fromJson(Map<String, dynamic> json) {
    expenseHead = json['ExpenseHead'];
    expenseHeadName = json['ExpenseHeadName'];
    createdOn = json['CreatedOn'];
    createdBy = json['CreatedBy'];
    isActive = json['IsActive'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['ExpenseHead'] = expenseHead;
    data['ExpenseHeadName'] = expenseHeadName;
    data['CreatedOn'] = createdOn;
    data['CreatedBy'] = createdBy;
    data['IsActive'] = isActive;
    return data;
  }
}
