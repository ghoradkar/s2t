// ignore_for_file: file_names

class AdvadetailsNewVersionV2Response {
  String? status;
  String? message;
  List<AdvadetailsNewOutput>? output;

  AdvadetailsNewVersionV2Response({this.status, this.message, this.output});

  AdvadetailsNewVersionV2Response.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AdvadetailsNewOutput>[];
      json['output'].forEach((v) {
        output!.add(AdvadetailsNewOutput.fromJson(v));
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

class AdvadetailsNewOutput {
  int? campid;
  int? campType;
  int? subExpenseID;
  int? distlgdcode;
  String? distname;
  String? campdate;
  int? requestType;
  int? expectedbeneficiarycount;
  int? registeredbeneficiarycount;
  String? fundRequestedBy;
  int? advRaisedbyUserid;
  int? campOrganisedBy;
  int? initiatedBy;
  String? approvedDate;
  String? advanceFundStatus;
  String? actualExpenseStatus;
  double? expenseAmount;
  double? actualAmountApporved;
  double? actualBillSubmit;
  String? expenseSaved;
  String? isBillUploaded;
  String? isSecondLevelApproval;
  int? createdBy;
  double? beneficiaryRefreshment;
  double? campAwarenessUsingBhopu;
  double? campHallGramPanchayatSchoolGovtOfficeTent;
  double? chairs;
  double? cleaningCharges;
  double? doctorPhysicalScreeningExpense;
  double? doctorReportScreeningExpense;
  double? drinkingWater;
  double? foodToStaffTAAllowance;
  double? other;
  double? sampleMovementToLabRunnerBoy;
  double? sampleMovementToLabTSRTCOrAnyOtherCargo;
  double? tables;
  double? transportationOfStaffTAGroupOfTransport;
  double? transportationOfStaffTAIndividual;
  double? finalSettelment;
  double? postCampExpense;

  AdvadetailsNewOutput({
    this.campid,
    this.campType,
    this.subExpenseID,
    this.distlgdcode,
    this.distname,
    this.campdate,
    this.requestType,
    this.expectedbeneficiarycount,
    this.registeredbeneficiarycount,
    this.fundRequestedBy,
    this.advRaisedbyUserid,
    this.campOrganisedBy,
    this.initiatedBy,
    this.approvedDate,
    this.advanceFundStatus,
    this.actualExpenseStatus,
    this.expenseAmount,
    this.actualAmountApporved,
    this.actualBillSubmit,
    this.expenseSaved,
    this.isBillUploaded,
    this.isSecondLevelApproval,
    this.createdBy,
    this.beneficiaryRefreshment,
    this.campAwarenessUsingBhopu,
    this.campHallGramPanchayatSchoolGovtOfficeTent,
    this.chairs,
    this.cleaningCharges,
    this.doctorPhysicalScreeningExpense,
    this.doctorReportScreeningExpense,
    this.drinkingWater,
    this.foodToStaffTAAllowance,
    this.other,
    this.sampleMovementToLabRunnerBoy,
    this.sampleMovementToLabTSRTCOrAnyOtherCargo,
    this.tables,
    this.transportationOfStaffTAGroupOfTransport,
    this.transportationOfStaffTAIndividual,
    this.finalSettelment,
    this.postCampExpense,
  });

  AdvadetailsNewOutput.fromJson(Map<String, dynamic> json) {
    campid = json['campid'];
    campType = json['CampType'];
    subExpenseID = json['SubExpenseID'];
    distlgdcode = json['distlgdcode'];
    distname = json['Distname'];
    campdate = json['campdate'];
    requestType = json['RequestType'];
    expectedbeneficiarycount = json['Expectedbeneficiarycount'];
    registeredbeneficiarycount = json['Registeredbeneficiarycount'];
    fundRequestedBy = json['FundRequestedBy'];
    advRaisedbyUserid = json['AdvRaisedbyUserid'];
    campOrganisedBy = json['CampOrganisedBy'];
    initiatedBy = json['InitiatedBy'];
    approvedDate = json['ApprovedDate'];
    advanceFundStatus = json['AdvanceFundStatus'];
    actualExpenseStatus = json['ActualExpenseStatus'];
    expenseAmount = json['ExpenseAmount'];
    actualAmountApporved = json['ActualAmountApporved'];
    actualBillSubmit = json['ActualBillSubmit'];
    expenseSaved = json['ExpenseSaved'];
    isBillUploaded = json['IsBillUploaded'];
    isSecondLevelApproval = json['IsSecondLevelApproval'];
    createdBy = json['CreatedBy'];
    beneficiaryRefreshment = json['Beneficiary refreshment'];
    campAwarenessUsingBhopu = json['Camp Awareness using Bhopu'];
    campHallGramPanchayatSchoolGovtOfficeTent =
        json['Camp Hall Gram Panchayat/School/Govt Office/Tent'];
    chairs = json['Chairs'];
    cleaningCharges = json['Cleaning Charges'];
    doctorPhysicalScreeningExpense = json['Doctor Physical Screening Expense'];
    doctorReportScreeningExpense = json['Doctor Report Screening Expense'];
    drinkingWater = json['Drinking Water'];
    foodToStaffTAAllowance = json['Food to Staff (TA) Allowance'];
    other = json['Other'];
    sampleMovementToLabRunnerBoy = json['Sample movement to lab - Runner boy'];
    sampleMovementToLabTSRTCOrAnyOtherCargo =
        json['Sample movement to lab - TSRTC or any other Cargo'];
    tables = json['Tables'];
    transportationOfStaffTAGroupOfTransport =
        json['Transportation of Staff (TA) Group of Transport'];
    transportationOfStaffTAIndividual =
        json['Transportation of Staff (TA) Individual'];
    finalSettelment = json['FinalSettelment'];
    postCampExpense = json['PostCampExpense'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['campid'] = campid;
    data['CampType'] = campType;
    data['SubExpenseID'] = subExpenseID;
    data['distlgdcode'] = distlgdcode;
    data['Distname'] = distname;
    data['campdate'] = campdate;
    data['RequestType'] = requestType;
    data['Expectedbeneficiarycount'] = expectedbeneficiarycount;
    data['Registeredbeneficiarycount'] = registeredbeneficiarycount;
    data['FundRequestedBy'] = fundRequestedBy;
    data['AdvRaisedbyUserid'] = advRaisedbyUserid;
    data['CampOrganisedBy'] = campOrganisedBy;
    data['InitiatedBy'] = initiatedBy;
    data['ApprovedDate'] = approvedDate;
    data['AdvanceFundStatus'] = advanceFundStatus;
    data['ActualExpenseStatus'] = actualExpenseStatus;
    data['ExpenseAmount'] = expenseAmount;
    data['ActualAmountApporved'] = actualAmountApporved;
    data['ActualBillSubmit'] = actualBillSubmit;
    data['ExpenseSaved'] = expenseSaved;
    data['IsBillUploaded'] = isBillUploaded;
    data['IsSecondLevelApproval'] = isSecondLevelApproval;
    data['CreatedBy'] = createdBy;
    data['Beneficiary refreshment'] = beneficiaryRefreshment;
    data['Camp Awareness using Bhopu'] = campAwarenessUsingBhopu;
    data['Camp Hall Gram Panchayat/School/Govt Office/Tent'] =
        campHallGramPanchayatSchoolGovtOfficeTent;
    data['Chairs'] = chairs;
    data['Cleaning Charges'] = cleaningCharges;
    data['Doctor Physical Screening Expense'] = doctorPhysicalScreeningExpense;
    data['Doctor Report Screening Expense'] = doctorReportScreeningExpense;
    data['Drinking Water'] = drinkingWater;
    data['Food to Staff (TA) Allowance'] = foodToStaffTAAllowance;
    data['Other'] = other;
    data['Sample movement to lab - Runner boy'] = sampleMovementToLabRunnerBoy;
    data['Sample movement to lab - TSRTC or any other Cargo'] =
        sampleMovementToLabTSRTCOrAnyOtherCargo;
    data['Tables'] = tables;
    data['Transportation of Staff (TA) Group of Transport'] =
        transportationOfStaffTAGroupOfTransport;
    data['Transportation of Staff (TA) Individual'] =
        transportationOfStaffTAIndividual;
    data['FinalSettelment'] = finalSettelment;
    data['PostCampExpense'] = postCampExpense;
    return data;
  }
}
