// ignore_for_file: file_names, unnecessary_question_mark

class LoginResponseModel {
  String? status;
  String? message;
  List<LoginOutput>? output;

  LoginResponseModel({this.status, this.message, this.output});

  LoginResponseModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <LoginOutput>[];
      json['output'].forEach((v) {
        output!.add(LoginOutput.fromJson(v));
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

class LoginOutput {
  int? iD;
  int? empCode;
  int? sUBORGWISEDESGID;
  int? subOrgId;
  String? subOrgName;
  int? orgId;
  String? orgName;
  String? projectId;
  String? projectName;
  String? uSERNAME;
  String? name;
  String? firstName;
  String? middleName;
  String? lastName;
  String? spouseName;
  int? children;
  String? motherName;
  String? fatherName;
  String? voterId;
  String? perEmail;
  String? perMobile;
  String? pAddress;
  String? cAddress;
  String? dob;
  int? aGE;
  String? aadhar;
  String? pancard;
  String? imagePath;
  String? joiningdate;
  String? bloodgroup;
  int? bLOODID;
  int? eDUCDTLID;
  String? edu;
  String? universityname;
  String? qualification;
  int? qUALID;
  int? eDTYPEID;
  int? uNIVID;
  int? yearofpassing;
  int? percentage;
  String? cast;
  String? religion;
  int? religionId;
  String? category;
  String? maritialstatus;
  String? anniversarydate;
  String? workLocationName;
  String? designation;
  int? dESGID;
  int? wORKLOCID;
  int? sTATELGDCODE;
  int? dISTLGDCODE;
  int? tALLGDCODE;
  int? gPLGDCODE;
  int? dESGLEVELID;
  String? sTATE;
  String? district;
  String? taluka;
  String? accountno;
  String? bankname;
  int? bANKID;
  String? branchname;
  String? ifsccode;
  String? oMTCSCID;
  String? iPADDRESS;
  String? bCompany;
  String? bAddress;
  String? bEmail;
  String? bMobile;
  String? categoryId;
  String? maritialstatusId;
  int? hLLDISTRICTID;
  int? pincode;
  String? bankaddress;
  int? centerTypeID;
  int? centerID;
  String? centerName;
  int? address;
  int? facilityID;
  String? facilityName;
  String? labName;
  String? labIncharge;
  String? lABAddress;
  String? lABdetails;
  int? tYPEID;
  String? tYPENAME;
  String? passbook;
  int? gender;
  int? patchCode;
  String? patchName;
  int? cityCode;
  String? cityName;
  double? latitude;
  double? longitude;
  int? avgPatient;
  int? avgTest;
  bool? iNTERESTINFRANCH;
  String? tYPEOFSETUP;
  Null? noOfBed;
  Null? hOSPITALCONTACT;
  Null? iSLABAVAILABLE;
  String? lINAME;
  String? lIMOBNO;
  Null? hPID;
  Null? lIHostGender;
  Null? lIEmail;
  Null? lPID;
  String? lABCONTACT;
  Null? iSTIEUPS;
  Null? labCode;
  Null? iSTEST;
  Null? dOCID;
  Null? sPECIALITYID;
  Null? sPECIALITYNAME;
  Null? pRACSPECIALITY;
  Null? sETUPTYPE;
  Null? hOSCONTACT;
  Null? hOSEMAILID;
  Null? hOSMOBNO;
  Null? iSATTACHED;
  String? aTTCHHOSPITALNAME;
  Null? iSLABAVIALABLE;
  String? lIDOCNAME;
  String? lICONTACT;
  Null? lIGENDER;
  Null? vlePId;
  Null? cSCVLEID;
  Null? landlineno;
  Null? customerCode;
  Null? potliuid;
  Null? potliuserId;
  Null? potliwalletId;
  int? iSASPIRANT;
  int? divid;
  String? dIVNAME;
  String? agentID;
  String? myOperatorUserID;
  int? is24By7IsAccountCreated;
  bool? isFixedPay;

  LoginOutput({
    this.iD,
    this.empCode,
    this.sUBORGWISEDESGID,
    this.subOrgId,
    this.subOrgName,
    this.orgId,
    this.orgName,
    this.projectId,
    this.projectName,
    this.uSERNAME,
    this.name,
    this.firstName,
    this.middleName,
    this.lastName,
    this.spouseName,
    this.children,
    this.motherName,
    this.fatherName,
    this.voterId,
    this.perEmail,
    this.perMobile,
    this.pAddress,
    this.cAddress,
    this.dob,
    this.aGE,
    this.aadhar,
    this.pancard,
    this.imagePath,
    this.joiningdate,
    this.bloodgroup,
    this.bLOODID,
    this.eDUCDTLID,
    this.edu,
    this.universityname,
    this.qualification,
    this.qUALID,
    this.eDTYPEID,
    this.uNIVID,
    this.yearofpassing,
    this.percentage,
    this.cast,
    this.religion,
    this.religionId,
    this.category,
    this.maritialstatus,
    this.anniversarydate,
    this.workLocationName,
    this.designation,
    this.dESGID,
    this.wORKLOCID,
    this.sTATELGDCODE,
    this.dISTLGDCODE,
    this.tALLGDCODE,
    this.gPLGDCODE,
    this.dESGLEVELID,
    this.sTATE,
    this.district,
    this.taluka,
    this.accountno,
    this.bankname,
    this.bANKID,
    this.branchname,
    this.ifsccode,
    this.oMTCSCID,
    this.iPADDRESS,
    this.bCompany,
    this.bAddress,
    this.bEmail,
    this.bMobile,
    this.categoryId,
    this.maritialstatusId,
    this.hLLDISTRICTID,
    this.pincode,
    this.bankaddress,
    this.centerTypeID,
    this.centerID,
    this.centerName,
    this.address,
    this.facilityID,
    this.facilityName,
    this.labName,
    this.labIncharge,
    this.lABAddress,
    this.lABdetails,
    this.tYPEID,
    this.tYPENAME,
    this.passbook,
    this.gender,
    this.patchCode,
    this.patchName,
    this.cityCode,
    this.cityName,
    this.latitude,
    this.longitude,
    this.avgPatient,
    this.avgTest,
    this.iNTERESTINFRANCH,
    this.tYPEOFSETUP,
    this.noOfBed,
    this.hOSPITALCONTACT,
    this.iSLABAVAILABLE,
    this.lINAME,
    this.lIMOBNO,
    this.hPID,
    this.lIHostGender,
    this.lIEmail,
    this.lPID,
    this.lABCONTACT,
    this.iSTIEUPS,
    this.labCode,
    this.iSTEST,
    this.dOCID,
    this.sPECIALITYID,
    this.sPECIALITYNAME,
    this.pRACSPECIALITY,
    this.sETUPTYPE,
    this.hOSCONTACT,
    this.hOSEMAILID,
    this.hOSMOBNO,
    this.iSATTACHED,
    this.aTTCHHOSPITALNAME,
    this.iSLABAVIALABLE,
    this.lIDOCNAME,
    this.lICONTACT,
    this.lIGENDER,
    this.vlePId,
    this.cSCVLEID,
    this.landlineno,
    this.customerCode,
    this.potliuid,
    this.potliuserId,
    this.potliwalletId,
    this.iSASPIRANT,
    this.divid,
    this.dIVNAME,
    this.agentID,
    this.myOperatorUserID,
    this.is24By7IsAccountCreated,
    this.isFixedPay,
  });

  LoginOutput.fromJson(Map<String, dynamic> json) {
    iD = json['ID'];
    empCode = json['EmpCode'];
    sUBORGWISEDESGID = json['SUBORGWISEDESGID'];
    subOrgId = json['SubOrgId'];
    subOrgName = json['SubOrgName'];
    orgId = json['OrgId'];
    orgName = json['OrgName'];
    projectId = json['ProjectId'];
    projectName = json['ProjectName'];
    uSERNAME = json['USERNAME'];
    name = json['name'];
    firstName = json['FirstName'];
    middleName = json['MiddleName'];
    lastName = json['LastName'];
    spouseName = json['SpouseName'];
    children = json['Children'];
    motherName = json['MotherName'];
    fatherName = json['FatherName'];
    voterId = json['VoterId'];
    perEmail = json['per_email'];
    perMobile = json['per_mobile'];
    pAddress = json['PAddress'];
    cAddress = json['CAddress'];
    dob = json['dob'];
    aGE = json['AGE'];
    aadhar = json['Aadhar'];
    pancard = json['pancard'];
    imagePath = json['ImagePath'];
    joiningdate = json['joiningdate'];
    bloodgroup = json['bloodgroup'];
    bLOODID = json['BLOODID'];
    eDUCDTLID = json['EDUCDTLID'];
    edu = json['edu'];
    universityname = json['universityname'];
    qualification = json['qualification'];
    qUALID = json['QUALID'];
    eDTYPEID = json['EDTYPEID'];
    uNIVID = json['UNIVID'];
    yearofpassing = json['yearofpassing'];
    percentage = json['percentage'];
    cast = json['cast'];
    religion = json['religion'];
    religionId = json['ReligionId'];
    category = json['category'];
    maritialstatus = json['Maritialstatus'];
    anniversarydate = json['anniversarydate'];
    workLocationName = json['WorkLocationName'];
    designation = json['Designation'];
    dESGID = json['DESGID'];
    wORKLOCID = json['WORKLOCID'];
    sTATELGDCODE = json['STATELGDCODE'];
    dISTLGDCODE = json['DISTLGDCODE'];
    tALLGDCODE = json['TALLGDCODE'];
    gPLGDCODE = json['GPLGDCODE'];
    dESGLEVELID = json['DESGLEVELID'];
    sTATE = json['STATE'];
    district = json['district'];
    taluka = json['taluka'];
    accountno = json['accountno'];
    bankname = json['bankname'];
    bANKID = json['BANKID'];
    branchname = json['branchname'];
    ifsccode = json['ifsccode'];
    oMTCSCID = json['OMTCSCID'];
    iPADDRESS = json['IPADDRESS'];
    bCompany = json['BCompany'];
    bAddress = json['BAddress'];
    bEmail = json['BEmail'];
    bMobile = json['BMobile'];
    categoryId = json['CategoryId'];
    maritialstatusId = json['MaritialstatusId'];
    hLLDISTRICTID = json['HLLDISTRICTID'];
    pincode = json['pincode'];
    bankaddress = json['bankaddress'];
    centerTypeID = json['CenterTypeID'];
    centerID = json['CenterID'];
    centerName = json['CenterName'];
    address = json['Address'];
    facilityID = json['FacilityID'];
    facilityName = json['FacilityName'];
    labName = json['LabName'];
    labIncharge = json['LabIncharge'];
    lABAddress = json['LABAddress'];
    lABdetails = json['LABdetails'];
    tYPEID = json['TYPEID'];
    tYPENAME = json['TYPENAME'];
    passbook = json['passbook'];
    gender = json['Gender'];
    patchCode = json['PatchCode'];
    patchName = json['PatchName'];
    cityCode = json['CityCode'];
    cityName = json['CityName'];
    latitude = json['Latitude'];
    longitude = json['Longitude'];
    avgPatient = json['AvgPatient'];
    avgTest = json['AvgTest'];
    iNTERESTINFRANCH = json['INTERESTINFRANCH'];
    tYPEOFSETUP = json['TYPEOFSETUP'];
    noOfBed = json['NoOfBed'];
    hOSPITALCONTACT = json['HOSPITAL_CONTACT'];
    iSLABAVAILABLE = json['ISLABAVAILABLE'];
    lINAME = json['LI_NAME'];
    lIMOBNO = json['LI_MOBNO'];
    hPID = json['HPID'];
    lIHostGender = json['LI_HostGender'];
    lIEmail = json['LI_Email'];
    lPID = json['LPID'];
    lABCONTACT = json['LAB_CONTACT'];
    iSTIEUPS = json['ISTIEUPS'];
    labCode = json['LabCode'];
    iSTEST = json['ISTEST'];
    dOCID = json['DOCID'];
    sPECIALITYID = json['SPECIALITYID'];
    sPECIALITYNAME = json['SPECIALITYNAME'];
    pRACSPECIALITY = json['PRAC_SPECIALITY'];
    sETUPTYPE = json['SETUPTYPE'];
    hOSCONTACT = json['HOS_CONTACT'];
    hOSEMAILID = json['HOS_EMAILID'];
    hOSMOBNO = json['HOS_MOBNO'];
    iSATTACHED = json['ISATTACHED'];
    aTTCHHOSPITALNAME = json['ATTCH_HOSPITAL_NAME'];
    iSLABAVIALABLE = json['ISLABAVIALABLE'];
    lIDOCNAME = json['LI_DOCNAME'];
    lICONTACT = json['LI_CONTACT'];
    lIGENDER = json['LI_GENDER'];
    vlePId = json['VlePId'];
    cSCVLEID = json['CSC_VLE_ID'];
    landlineno = json['Landlineno'];
    customerCode = json['CustomerCode'];
    potliuid = json['Potliuid'];
    potliuserId = json['Potliuser_id'];
    potliwalletId = json['Potliwallet_id'];
    iSASPIRANT = json['ISASPIRANT'];
    divid = json['divid'];
    dIVNAME = json['DIVNAME'];
    agentID = json['AgentID'];
    myOperatorUserID = json['MyOperator_UserID'];
    is24By7IsAccountCreated = json['Is24By7IsAccountCreated'];
    if (json['IsFixedPay'] != null) {
      isFixedPay = json['IsFixedPay'].toString().toLowerCase() == 'true';
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['ID'] = iD;
    data['EmpCode'] = empCode;
    data['SUBORGWISEDESGID'] = sUBORGWISEDESGID;
    data['SubOrgId'] = subOrgId;
    data['SubOrgName'] = subOrgName;
    data['OrgId'] = orgId;
    data['OrgName'] = orgName;
    data['ProjectId'] = projectId;
    data['ProjectName'] = projectName;
    data['USERNAME'] = uSERNAME;
    data['name'] = name;
    data['FirstName'] = firstName;
    data['MiddleName'] = middleName;
    data['LastName'] = lastName;
    data['SpouseName'] = spouseName;
    data['Children'] = children;
    data['MotherName'] = motherName;
    data['FatherName'] = fatherName;
    data['VoterId'] = voterId;
    data['per_email'] = perEmail;
    data['per_mobile'] = perMobile;
    data['PAddress'] = pAddress;
    data['CAddress'] = cAddress;
    data['dob'] = dob;
    data['AGE'] = aGE;
    data['Aadhar'] = aadhar;
    data['pancard'] = pancard;
    data['ImagePath'] = imagePath;
    data['joiningdate'] = joiningdate;
    data['bloodgroup'] = bloodgroup;
    data['BLOODID'] = bLOODID;
    data['EDUCDTLID'] = eDUCDTLID;
    data['edu'] = edu;
    data['universityname'] = universityname;
    data['qualification'] = qualification;
    data['QUALID'] = qUALID;
    data['EDTYPEID'] = eDTYPEID;
    data['UNIVID'] = uNIVID;
    data['yearofpassing'] = yearofpassing;
    data['percentage'] = percentage;
    data['cast'] = cast;
    data['religion'] = religion;
    data['ReligionId'] = religionId;
    data['category'] = category;
    data['Maritialstatus'] = maritialstatus;
    data['anniversarydate'] = anniversarydate;
    data['WorkLocationName'] = workLocationName;
    data['Designation'] = designation;
    data['DESGID'] = dESGID;
    data['WORKLOCID'] = wORKLOCID;
    data['STATELGDCODE'] = sTATELGDCODE;
    data['DISTLGDCODE'] = dISTLGDCODE;
    data['TALLGDCODE'] = tALLGDCODE;
    data['GPLGDCODE'] = gPLGDCODE;
    data['DESGLEVELID'] = dESGLEVELID;
    data['STATE'] = sTATE;
    data['district'] = district;
    data['taluka'] = taluka;
    data['accountno'] = accountno;
    data['bankname'] = bankname;
    data['BANKID'] = bANKID;
    data['branchname'] = branchname;
    data['ifsccode'] = ifsccode;
    data['OMTCSCID'] = oMTCSCID;
    data['IPADDRESS'] = iPADDRESS;
    data['BCompany'] = bCompany;
    data['BAddress'] = bAddress;
    data['BEmail'] = bEmail;
    data['BMobile'] = bMobile;
    data['CategoryId'] = categoryId;
    data['MaritialstatusId'] = maritialstatusId;
    data['HLLDISTRICTID'] = hLLDISTRICTID;
    data['pincode'] = pincode;
    data['bankaddress'] = bankaddress;
    data['CenterTypeID'] = centerTypeID;
    data['CenterID'] = centerID;
    data['CenterName'] = centerName;
    data['Address'] = address;
    data['FacilityID'] = facilityID;
    data['FacilityName'] = facilityName;
    data['LabName'] = labName;
    data['LabIncharge'] = labIncharge;
    data['LABAddress'] = lABAddress;
    data['LABdetails'] = lABdetails;
    data['TYPEID'] = tYPEID;
    data['TYPENAME'] = tYPENAME;
    data['passbook'] = passbook;
    data['Gender'] = gender;
    data['PatchCode'] = patchCode;
    data['PatchName'] = patchName;
    data['CityCode'] = cityCode;
    data['CityName'] = cityName;
    data['Latitude'] = latitude;
    data['Longitude'] = longitude;
    data['AvgPatient'] = avgPatient;
    data['AvgTest'] = avgTest;
    data['INTERESTINFRANCH'] = iNTERESTINFRANCH;
    data['TYPEOFSETUP'] = tYPEOFSETUP;
    data['NoOfBed'] = noOfBed;
    data['HOSPITAL_CONTACT'] = hOSPITALCONTACT;
    data['ISLABAVAILABLE'] = iSLABAVAILABLE;
    data['LI_NAME'] = lINAME;
    data['LI_MOBNO'] = lIMOBNO;
    data['HPID'] = hPID;
    data['LI_HostGender'] = lIHostGender;
    data['LI_Email'] = lIEmail;
    data['LPID'] = lPID;
    data['LAB_CONTACT'] = lABCONTACT;
    data['ISTIEUPS'] = iSTIEUPS;
    data['LabCode'] = labCode;
    data['ISTEST'] = iSTEST;
    data['DOCID'] = dOCID;
    data['SPECIALITYID'] = sPECIALITYID;
    data['SPECIALITYNAME'] = sPECIALITYNAME;
    data['PRAC_SPECIALITY'] = pRACSPECIALITY;
    data['SETUPTYPE'] = sETUPTYPE;
    data['HOS_CONTACT'] = hOSCONTACT;
    data['HOS_EMAILID'] = hOSEMAILID;
    data['HOS_MOBNO'] = hOSMOBNO;
    data['ISATTACHED'] = iSATTACHED;
    data['ATTCH_HOSPITAL_NAME'] = aTTCHHOSPITALNAME;
    data['ISLABAVIALABLE'] = iSLABAVIALABLE;
    data['LI_DOCNAME'] = lIDOCNAME;
    data['LI_CONTACT'] = lICONTACT;
    data['LI_GENDER'] = lIGENDER;
    data['VlePId'] = vlePId;
    data['CSC_VLE_ID'] = cSCVLEID;
    data['Landlineno'] = landlineno;
    data['CustomerCode'] = customerCode;
    data['Potliuid'] = potliuid;
    data['Potliuser_id'] = potliuserId;
    data['Potliwallet_id'] = potliwalletId;
    data['ISASPIRANT'] = iSASPIRANT;
    data['divid'] = divid;
    data['DIVNAME'] = dIVNAME;
    data['AgentID'] = agentID;
    data['MyOperator_UserID'] = myOperatorUserID;
    data['Is24By7IsAccountCreated'] = is24By7IsAccountCreated;
    data['IsFixedPay'] = isFixedPay;
    return data;
  }
}
