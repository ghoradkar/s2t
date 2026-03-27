class CampWiseInvoiceDetailsResponse {
  String? status;
  String? message;
  List<CampWiseInvoiceDetailsOutput>? output;

  CampWiseInvoiceDetailsResponse({this.status, this.message, this.output});

  CampWiseInvoiceDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampWiseInvoiceDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(CampWiseInvoiceDetailsOutput.fromJson(v));
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

class CampWiseInvoiceDetailsOutput {
  String? campID;
  String? campDate;
  double? individualBillableDependent;
  double? individualBillableWorker;
  double? individualPenaltyAmount;
  double? totalBeneficiaries;
  double? billablesSameDay;
  double? billablesAnotherDay;
  double? totalIndividualBillable;
  double? totalIndividualRejected;
  int? userID;
  int? userInviceID;

  CampWiseInvoiceDetailsOutput({
    this.campID,
    this.campDate,
    this.individualBillableDependent,
    this.individualBillableWorker,
    this.individualPenaltyAmount,
    this.totalBeneficiaries,
    this.billablesSameDay,
    this.billablesAnotherDay,
    this.totalIndividualBillable,
    this.totalIndividualRejected,
    this.userID,
    this.userInviceID,
  });

  CampWiseInvoiceDetailsOutput.fromJson(Map<String, dynamic> json) {
    campID = json['CampID']?.toString();
    campDate = json['CampDate'];
    individualBillableDependent = _toDouble(json['IndividualBillableDependent']);
    individualBillableWorker = _toDouble(json['IndividualBillableWorker']);
    individualPenaltyAmount = _toDouble(json['IndividualPenaltyAmount']);
    totalBeneficiaries = _toDouble(json['TotalBeneficiaries']);
    billablesSameDay = _toDouble(json['BillablesSameDay']);
    billablesAnotherDay = _toDouble(json['BillablesAnotherDay']);
    totalIndividualBillable = _toDouble(json['TotalIndividualBillable']);
    totalIndividualRejected = _toDouble(json['TotalIndividualRejected']);
    userID = _toInt(json['UserID']);
    userInviceID = _toInt(json['UserInviceID']);
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CampID'] = campID;
    data['CampDate'] = campDate;
    data['IndividualBillableDependent'] = individualBillableDependent;
    data['IndividualBillableWorker'] = individualBillableWorker;
    data['IndividualPenaltyAmount'] = individualPenaltyAmount;
    data['TotalBeneficiaries'] = totalBeneficiaries;
    data['BillablesSameDay'] = billablesSameDay;
    data['BillablesAnotherDay'] = billablesAnotherDay;
    data['TotalIndividualBillable'] = totalIndividualBillable;
    data['TotalIndividualRejected'] = totalIndividualRejected;
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
