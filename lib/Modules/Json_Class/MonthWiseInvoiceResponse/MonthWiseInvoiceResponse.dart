// ignore_for_file: file_names

class MonthWiseInvoiceResponse {
  String? status;
  String? message;
  List<MonthWiseInvoiceOutput>? output;

  MonthWiseInvoiceResponse({this.status, this.message, this.output});

  MonthWiseInvoiceResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <MonthWiseInvoiceOutput>[];
      json['output'].forEach((v) {
        output!.add(MonthWiseInvoiceOutput.fromJson(v));
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

class MonthWiseInvoiceOutput {
  int? invoiceApprovedStatus;
  String? invoiceMonth;
  String? invoiceStatus;
  int? invoiceStatusID;
  String? invoiceUrl;
  String? invoiceYear;
  int? month;
  int? rankno;
  int? sendForVerification;
  int? serviceDays;
  double? totalIndividualBillable;
  int? userID;
  int? userInviceID;

  MonthWiseInvoiceOutput({
    this.invoiceApprovedStatus,
    this.invoiceMonth,
    this.invoiceStatus,
    this.invoiceStatusID,
    this.invoiceUrl,
    this.invoiceYear,
    this.month,
    this.rankno,
    this.sendForVerification,
    this.serviceDays,
    this.totalIndividualBillable,
    this.userID,
    this.userInviceID,
  });

  MonthWiseInvoiceOutput.fromJson(Map<String, dynamic> json) {
    invoiceApprovedStatus = json['InvoiceApprovedStatus'];
    invoiceMonth = json['InvoiceMonth'];
    invoiceStatus = json['InvoiceStatus'];
    invoiceStatusID = json['InvoiceStatusID'];
    invoiceUrl = json['InvoiceUrl'];
    invoiceYear = json['InvoiceYear'];
    month = json['Month'];
    rankno = json['Rankno'];
    sendForVerification = json['SendForVerification'];
    serviceDays = json['ServiceDays'];
    totalIndividualBillable = json['TotalIndividualBillable'];
    userID = json['UserID'];
    userInviceID = json['UserInviceID'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['InvoiceApprovedStatus'] = invoiceApprovedStatus;
    data['InvoiceMonth'] = invoiceMonth;
    data['InvoiceStatus'] = invoiceStatus;
    data['InvoiceStatusID'] = invoiceStatusID;
    data['InvoiceUrl'] = invoiceUrl;
    data['InvoiceYear'] = invoiceYear;
    data['Month'] = month;
    data['Rankno'] = rankno;
    data['SendForVerification'] = sendForVerification;
    data['ServiceDays'] = serviceDays;
    data['TotalIndividualBillable'] = totalIndividualBillable;
    data['UserID'] = userID;
    data['UserInviceID'] = userInviceID;
    return data;
  }
}
