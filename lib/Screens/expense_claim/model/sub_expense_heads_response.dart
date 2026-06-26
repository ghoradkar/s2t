// ignore_for_file: file_names

class SubExpenseHeadsResponse {
  String? status;
  String? message;
  List<SubExpenseHeadsOutput>? output;

  SubExpenseHeadsResponse({this.status, this.message, this.output});

  SubExpenseHeadsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <SubExpenseHeadsOutput>[];
      json['output'].forEach((v) {
        output!.add(SubExpenseHeadsOutput.fromJson(v));
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

class SubExpenseHeadsOutput {
  int? expenseHead;
  int? subExpenseID;
  String? subexpenseName;
  String? unit;
  bool? isbillrequired;
  double? maxAllowedAmt;
  double? meghaMaxAllowedAmt;
  int? subExpenseIDPk;
  bool isSelected = false;

  SubExpenseHeadsOutput({
    this.expenseHead,
    this.subExpenseID,
    this.subexpenseName,
    this.unit,
    this.isbillrequired,
    this.maxAllowedAmt,
    this.meghaMaxAllowedAmt,
    this.subExpenseIDPk,
  });

  SubExpenseHeadsOutput.fromJson(Map<String, dynamic> json) {
    expenseHead = json['ExpenseHead'];
    subExpenseID = json['subExpenseID'];
    subexpenseName = json['SubexpenseName'];
    unit = json['Unit'];
    isbillrequired = json['isbillrequired'];
    maxAllowedAmt = json['MaxAllowedAmt'];
    meghaMaxAllowedAmt = json['meghaMaxAllowedAmt'];
    subExpenseIDPk = json['subExpenseIDPk'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['ExpenseHead'] = expenseHead;
    data['subExpenseID'] = subExpenseID;
    data['SubexpenseName'] = subexpenseName;
    data['Unit'] = unit;
    data['isbillrequired'] = isbillrequired;
    data['MaxAllowedAmt'] = maxAllowedAmt;
    data['meghaMaxAllowedAmt'] = meghaMaxAllowedAmt;
    data['subExpenseIDPk'] = subExpenseIDPk;
    return data;
  }
}
