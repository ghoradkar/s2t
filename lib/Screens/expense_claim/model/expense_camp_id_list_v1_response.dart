// ignore_for_file: file_names

class ExpenseCampIDListV1Response {
  String? status;
  String? message;
  List<ExpenseCampIDListV1Output>? output;

  ExpenseCampIDListV1Response({this.status, this.message, this.output});

  ExpenseCampIDListV1Response.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ExpenseCampIDListV1Output>[];
      json['output'].forEach((v) {
        output!.add(ExpenseCampIDListV1Output.fromJson(v));
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

class ExpenseCampIDListV1Output {
  int? campid;
  double? expenseAmount;
  bool isSelected = false;

  ExpenseCampIDListV1Output({this.campid, this.expenseAmount});

  ExpenseCampIDListV1Output.fromJson(Map<String, dynamic> json) {
    campid = json['campid'];
    expenseAmount = json['ExpenseAmount'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['campid'] = campid;
    data['ExpenseAmount'] = expenseAmount;
    return data;
  }
}
