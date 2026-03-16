// ignore_for_file: unnecessary_question_mark, file_names

class BeneficiaryDetailsforAssignTeamidDetailsResponse {
  String? status;
  String? message;
  List<BeneficiaryDetailsforAssignTeamidOutput>? output;

  BeneficiaryDetailsforAssignTeamidDetailsResponse({
    this.status,
    this.message,
    this.output,
  });

  BeneficiaryDetailsforAssignTeamidDetailsResponse.fromJson(
    Map<String, dynamic> json,
  ) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <BeneficiaryDetailsforAssignTeamidOutput>[];
      json['output'].forEach((v) {
        output!.add(BeneficiaryDetailsforAssignTeamidOutput.fromJson(v));
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

class BeneficiaryDetailsforAssignTeamidOutput {
  int? campId;
  String? campDate;
  String? campType;
  int? regdid;
  String? regdno;
  String? beneficiaryName;
  int? landingLab;
  String? landingLabName;
  String? pinCode;
  String? area;
  String? address;
  int? dISTLGDCODE;
  String? dISTNAME;
  String? sampleCollection;
  String? assignTeamid;
  String? assignTeamName;
  String? isTeamAssign;
  int? teamid;
  String? teamname;
  int? memberUserID1;
  String? member1;
  String? member1MOB;
  int? memberUserID2;
  String? member2;
  String? member2MOB;
  String? isTeamActive;
  int? uSERID;
  String? uSERNAME;
  String? mOBNO;
  bool? iSACTIVE;

  BeneficiaryDetailsforAssignTeamidOutput({
    this.campId,
    this.campDate,
    this.campType,
    this.regdid,
    this.regdno,
    this.beneficiaryName,
    this.landingLab,
    this.landingLabName,
    this.pinCode,
    this.area,
    this.address,
    this.dISTLGDCODE,
    this.dISTNAME,
    this.sampleCollection,
    this.assignTeamid,
    this.assignTeamName,
    this.isTeamAssign,
    this.teamid,
    this.teamname,
    this.memberUserID1,
    this.member1,
    this.member1MOB,
    this.memberUserID2,
    this.member2,
    this.member2MOB,
    this.isTeamActive,
    this.uSERID,
    this.uSERNAME,
    this.mOBNO,
    this.iSACTIVE,
  });

  BeneficiaryDetailsforAssignTeamidOutput.fromJson(Map<String, dynamic> json) {
    campId = json['CampId'];
    campDate = json['CampDate'];
    campType = json['CampType'];
    regdid = json['Regdid'];
    regdno = json['Regdno'];
    beneficiaryName = json['BeneficiaryName'];
    landingLab = json['LandingLab'];
    landingLabName = json['LandingLabName'];
    pinCode = json['PinCode'];
    area = json['Area'];
    address = json['Address'];
    dISTLGDCODE = json['DISTLGDCODE'];
    dISTNAME = json['DISTNAME'];
    sampleCollection = json['SampleCollection'];
    assignTeamid = json['AssignTeamid'];
    assignTeamName = json['AssignTeamName'];
    isTeamAssign = json['IsTeamAssign'];
    teamid = json['teamid'];
    teamname = json['teamname'];
    memberUserID1 = json['MemberUserID1'];
    member1 = json['Member1'];
    member1MOB = json['Member1MOB'];
    memberUserID2 = json['MemberUserID2'];
    member2 = json['Member2'];
    member2MOB = json['Member2MOB'];
    isTeamActive = json['IsTeamActive'];
    uSERID = json['USERID'];
    uSERNAME = json['USERNAME'];
    mOBNO = json['MOBNO'];
    iSACTIVE = json['ISACTIVE'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['CampId'] = campId;
    data['CampDate'] = campDate;
    data['CampType'] = campType;
    data['Regdid'] = regdid;
    data['Regdno'] = regdno;
    data['BeneficiaryName'] = beneficiaryName;
    data['LandingLab'] = landingLab;
    data['LandingLabName'] = landingLabName;
    data['PinCode'] = pinCode;
    data['Area'] = area;
    data['Address'] = address;
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['DISTNAME'] = dISTNAME;
    data['SampleCollection'] = sampleCollection;
    data['AssignTeamid'] = assignTeamid;
    data['AssignTeamName'] = assignTeamName;
    data['IsTeamAssign'] = isTeamAssign;
    data['teamid'] = teamid;
    data['teamname'] = teamname;
    data['MemberUserID1'] = memberUserID1;
    data['Member1'] = member1;
    data['Member1MOB'] = member1MOB;
    data['MemberUserID2'] = memberUserID2;
    data['Member2'] = member2;
    data['Member2MOB'] = member2MOB;
    data['IsTeamActive'] = isTeamActive;
    data['USERID'] = uSERID;
    data['USERNAME'] = uSERNAME;
    data['MOBNO'] = mOBNO;
    data['ISACTIVE'] = iSACTIVE;
    return data;
  }
}
