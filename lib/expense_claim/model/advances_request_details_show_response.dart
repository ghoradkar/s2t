// ignore_for_file: file_names

class AdvancesRequestDetailsShowResponse {
  String? status;
  String? message;
  List<AdvancesRequestDetailsShowOutput>? output;

  AdvancesRequestDetailsShowResponse({this.status, this.message, this.output});

  AdvancesRequestDetailsShowResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AdvancesRequestDetailsShowOutput>[];
      json['output'].forEach((v) {
        output!.add(AdvancesRequestDetailsShowOutput.fromJson(v));
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

class AdvancesRequestDetailsShowOutput {
  int? campid;
  String? dISTNAME;
  String? campDate;
  int? expectedbeneficiarycount;
  String? fundRequestedBy;
  String? desgName;
  double? totalAdvanceTaken;
  String? advanceApprovedBy;
  double? totalBillSubmittedAmt;
  double? totalRequestedExpense;
  String? priviousApprovalStatus;
  String? approvalStatus;
  double? stateMgrAdvApprove;
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

  AdvancesRequestDetailsShowOutput({
    this.campid,
    this.dISTNAME,
    this.campDate,
    this.expectedbeneficiarycount,
    this.fundRequestedBy,
    this.desgName,
    this.totalAdvanceTaken,
    this.advanceApprovedBy,
    this.totalBillSubmittedAmt,
    this.totalRequestedExpense,
    this.priviousApprovalStatus,
    this.approvalStatus,
    this.stateMgrAdvApprove,
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
  });

  AdvancesRequestDetailsShowOutput.fromJson(Map<String, dynamic> json) {
    campid = json['campid'];
    dISTNAME = json['DISTNAME'];
    campDate = json['CampDate'];
    expectedbeneficiarycount = json['Expectedbeneficiarycount'];
    fundRequestedBy = json['FundRequestedBy'];
    desgName = json['DesgName'];
    totalAdvanceTaken = json['TotalAdvanceTaken'];
    advanceApprovedBy = json['AdvanceApprovedBy'];
    totalBillSubmittedAmt = json['TotalBillSubmittedAmt'];
    totalRequestedExpense = json['TotalRequestedExpense'];
    priviousApprovalStatus = json['PriviousApprovalStatus'];
    approvalStatus = json['ApprovalStatus'];
    stateMgrAdvApprove = json['StateMgrAdvApprove'];
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
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['campid'] = campid;
    data['DISTNAME'] = dISTNAME;
    data['CampDate'] = campDate;
    data['Expectedbeneficiarycount'] = expectedbeneficiarycount;
    data['FundRequestedBy'] = fundRequestedBy;
    data['DesgName'] = desgName;
    data['TotalAdvanceTaken'] = totalAdvanceTaken;
    data['AdvanceApprovedBy'] = advanceApprovedBy;
    data['TotalBillSubmittedAmt'] = totalBillSubmittedAmt;
    data['TotalRequestedExpense'] = totalRequestedExpense;
    data['PriviousApprovalStatus'] = priviousApprovalStatus;
    data['ApprovalStatus'] = approvalStatus;
    data['StateMgrAdvApprove'] = stateMgrAdvApprove;
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
    return data;
  }
}
