class CallingDashboardModel {
  String? status;
  String? message;
  String? lastUpdatedOn;
  List<CallingDashboardOutput>? output;

  CallingDashboardModel({
    this.status,
    this.message,
    this.lastUpdatedOn,
    this.output,
  });

  CallingDashboardModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    lastUpdatedOn = json['LastUpdatedOn'];
    if (json['output'] != null) {
      output = <CallingDashboardOutput>[];
      json['output'].forEach((v) {
        output!.add(CallingDashboardOutput.fromJson(v));
      });
    }
  }
}

class CallingDashboardOutput {
  String? columnName;
  String? totalValue;

  CallingDashboardOutput({this.columnName, this.totalValue});

  CallingDashboardOutput.fromJson(Map<String, dynamic> json) {
    columnName = json['ColumnName'];
    totalValue = json['TotalValue']?.toString();
  }
}
