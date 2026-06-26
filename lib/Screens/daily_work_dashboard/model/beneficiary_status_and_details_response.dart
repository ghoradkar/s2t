// ignore_for_file: file_names

class BeneficiaryStatusAndDetailsResponse {
  String? status;
  String? message;
  List<BeneficiaryStatusAndDetailsOutput>? output;

  BeneficiaryStatusAndDetailsResponse({this.status, this.message, this.output});

  BeneficiaryStatusAndDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <BeneficiaryStatusAndDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(BeneficiaryStatusAndDetailsOutput.fromJson(v));
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

class BeneficiaryStatusAndDetailsOutput {
  int? rejRegdid;
  String? regdNo;
  String? beneficiaryName;
  String? relationWithWorker;
  String? pincode;
  String? area;
  String? address;
  int? campType;
  String? campTypeDescription;
  int? rejCampID;
  String? campDate;
  int? isRejectedFrom;
  String? rejectedFrom;
  String? rejectedReason;
  String? rejectionDate;
  int? teamID;
  String? teamName;
  int? memberUserID1;
  String? uSERNAMEM1;
  String? mOBNOM1;
  int? memberUserID2;
  String? uSERNAMEM2;
  String? mOBNOM2;
  int? isTeamActive;
  int? isTeamMapped;
  String? appointmentDate;
  int? isAppointmentConfirm;
  String? remarks;
  int? arid;
  String? nextRenewalDate;
  String? rejectedLabName;

  BeneficiaryStatusAndDetailsOutput({
    this.rejRegdid,
    this.regdNo,
    this.beneficiaryName,
    this.relationWithWorker,
    this.pincode,
    this.area,
    this.address,
    this.campType,
    this.campTypeDescription,
    this.rejCampID,
    this.campDate,
    this.isRejectedFrom,
    this.rejectedFrom,
    this.rejectedReason,
    this.rejectionDate,
    this.teamID,
    this.teamName,
    this.memberUserID1,
    this.uSERNAMEM1,
    this.mOBNOM1,
    this.memberUserID2,
    this.uSERNAMEM2,
    this.mOBNOM2,
    this.isTeamActive,
    this.isTeamMapped,
    this.appointmentDate,
    this.isAppointmentConfirm,
    this.remarks,
    this.arid,
    this.nextRenewalDate,
    this.rejectedLabName,
  });

  BeneficiaryStatusAndDetailsOutput.fromJson(Map<String, dynamic> json) {
    rejRegdid = json['Rej_Regdid'];
    regdNo = json['RegdNo'];
    beneficiaryName = json['BeneficiaryName'];
    relationWithWorker = json['RelationWithWorker'];
    pincode = json['Pincode'];
    area = json['Area'];
    address = json['Address'];
    campType = json['CampType'];
    campTypeDescription = json['CampTypeDescription'];
    rejCampID = json['Rej_CampID'];
    campDate = json['CampDate'];
    isRejectedFrom = json['IsRejectedFrom'];
    rejectedFrom = json['RejectedFrom'];
    rejectedReason = json['RejectedReason'];
    rejectionDate = json['RejectionDate'];
    teamID = json['TeamID'];
    teamName = json['TeamName'];
    memberUserID1 = json['MemberUserID1'];
    uSERNAMEM1 = json['USERNAMEM1'];
    mOBNOM1 = json['MOBNOM1'];
    memberUserID2 = json['MemberUserID2'];
    uSERNAMEM2 = json['USERNAMEM2'];
    mOBNOM2 = json['MOBNOM2'];
    isTeamActive = json['IsTeamActive'];
    isTeamMapped = json['IsTeamMapped'];
    appointmentDate = json['AppointmentDate'];
    isAppointmentConfirm = json['IsAppointmentConfirm'];
    remarks = json['Remarks'];
    arid = json['Arid'];
    nextRenewalDate = json['NextRenewalDate'];
    rejectedLabName = json['RejectedLabName'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Rej_Regdid'] = rejRegdid;
    data['RegdNo'] = regdNo;
    data['BeneficiaryName'] = beneficiaryName;
    data['RelationWithWorker'] = relationWithWorker;
    data['Pincode'] = pincode;
    data['Area'] = area;
    data['Address'] = address;
    data['CampType'] = campType;
    data['CampTypeDescription'] = campTypeDescription;
    data['Rej_CampID'] = rejCampID;
    data['CampDate'] = campDate;
    data['IsRejectedFrom'] = isRejectedFrom;
    data['RejectedFrom'] = rejectedFrom;
    data['RejectedReason'] = rejectedReason;
    data['RejectionDate'] = rejectionDate;
    data['TeamID'] = teamID;
    data['TeamName'] = teamName;
    data['MemberUserID1'] = memberUserID1;
    data['USERNAMEM1'] = uSERNAMEM1;
    data['MOBNOM1'] = mOBNOM1;
    data['MemberUserID2'] = memberUserID2;
    data['USERNAMEM2'] = uSERNAMEM2;
    data['MOBNOM2'] = mOBNOM2;
    data['IsTeamActive'] = isTeamActive;
    data['IsTeamMapped'] = isTeamMapped;
    data['AppointmentDate'] = appointmentDate;
    data['IsAppointmentConfirm'] = isAppointmentConfirm;
    data['Remarks'] = remarks;
    data['Arid'] = arid;
    data['NextRenewalDate'] = nextRenewalDate;
    data['RejectedLabName'] = rejectedLabName;
    return data;
  }
}
