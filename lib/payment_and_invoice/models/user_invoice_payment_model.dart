class UserInvoicePaymentDetailsResponse {
  String? status;
  String? message;
  List<UserInvoicePaymentDetailsOutput>? output;

  UserInvoicePaymentDetailsResponse({this.status, this.message, this.output});

  UserInvoicePaymentDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <UserInvoicePaymentDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(UserInvoicePaymentDetailsOutput.fromJson(v));
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

class UserInvoicePaymentDetailsOutput {
  double? finalPayableAmount;
  double? grossAmount;
  double? payableAmount;
  String? invoiceUrl;
  String? isPaymentRaised;
  int? payementNotReceivedStatus;
  int? payementReceivedStatus;
  String? paymentDate;
  double? penaltyAmount;
  int? sendForVerification;
  double? tDSAmount;
  String? uTRNo;
  int? userID;
  int? userInviceID;

  UserInvoicePaymentDetailsOutput({
    this.finalPayableAmount,
    this.grossAmount,
    this.payableAmount,
    this.invoiceUrl,
    this.isPaymentRaised,
    this.payementNotReceivedStatus,
    this.payementReceivedStatus,
    this.paymentDate,
    this.penaltyAmount,
    this.sendForVerification,
    this.tDSAmount,
    this.uTRNo,
    this.userID,
    this.userInviceID,
  });

  UserInvoicePaymentDetailsOutput.fromJson(Map<String, dynamic> json) {
    finalPayableAmount = _toDouble(json['FinalPayableAmount']);
    grossAmount = _toDouble(json['GrossAmount']);
    payableAmount = _toDouble(json['PayableAmount']);
    invoiceUrl = json['InvoiceUrl']?.toString();
    isPaymentRaised = json['IsPaymentRaised']?.toString();
    payementNotReceivedStatus = _toInt(json['PayementNotReceivedStatus']);
    payementReceivedStatus = _toInt(json['PayementReceivedStatus']);
    paymentDate = json['PaymentDate']?.toString();
    penaltyAmount = _toDouble(json['PenaltyAmount']);
    sendForVerification = _toInt(json['SendForVerification']);
    tDSAmount = _toDouble(json['TDSAmount']);
    uTRNo = json['UTRNo']?.toString();
    userID = _toInt(json['UserID']);
    userInviceID = _toInt(json['UserInviceID']);
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['FinalPayableAmount'] = finalPayableAmount;
    data['GrossAmount'] = grossAmount;
    data['PayableAmount'] = payableAmount;
    data['InvoiceUrl'] = invoiceUrl;
    data['IsPaymentRaised'] = isPaymentRaised;
    data['PayementNotReceivedStatus'] = payementNotReceivedStatus;
    data['PayementReceivedStatus'] = payementReceivedStatus;
    data['PaymentDate'] = paymentDate;
    data['PenaltyAmount'] = penaltyAmount;
    data['SendForVerification'] = sendForVerification;
    data['TDSAmount'] = tDSAmount;
    data['UTRNo'] = uTRNo;
    data['UserID'] = userID;
    data['UserInviceID'] = userInviceID;
    return data;
  }

  static double? _toDouble(dynamic value) {
    if (value == null) return null;
    if (value is num) return value.toDouble();
    return double.tryParse(value.toString());
  }

  static int? _toInt(dynamic value) {
    if (value == null) return null;
    if (value is int) return value;
    if (value is num) return value.toInt();
    return int.tryParse(value.toString());
  }
}
