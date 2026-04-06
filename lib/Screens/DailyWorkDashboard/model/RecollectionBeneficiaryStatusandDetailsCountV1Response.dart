// export 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryStatusandDetailsCountV1Response/RecollectionBeneficiaryStatusandDetailsCountV1Response.dart';
class RecollectionBeneficiaryStatusandDetailsCountV1Response {
  String? status;
  String? message;
  List<RecollectionBeneficiaryStatusandDetailsCountV1Output>? output;

  RecollectionBeneficiaryStatusandDetailsCountV1Response({
    this.status,
    this.message,
    this.output,
  });

  RecollectionBeneficiaryStatusandDetailsCountV1Response.fromJson(
      Map<String, dynamic> json,
      ) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <RecollectionBeneficiaryStatusandDetailsCountV1Output>[];
      json['output'].forEach((v) {
        output!.add(
          new RecollectionBeneficiaryStatusandDetailsCountV1Output.fromJson(v),
        );
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    if (this.output != null) {
      data['output'] = this.output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class RecollectionBeneficiaryStatusandDetailsCountV1Output {
  int? rejRegdid;
  String? beneficiaryNumber;
  String? beneficiaryName;
  String? area;
  String? mobileNo;
  String? pincode;
  int? arid;
  int? rejCampID;
  String? taluka;
  int? isAppointmentConfirm;

  RecollectionBeneficiaryStatusandDetailsCountV1Output({
    this.rejRegdid,
    this.beneficiaryNumber,
    this.beneficiaryName,
    this.area,
    this.mobileNo,
    this.pincode,
    this.arid,
    this.rejCampID,
    this.taluka,
    this.isAppointmentConfirm,
  });

  RecollectionBeneficiaryStatusandDetailsCountV1Output.fromJson(
      Map<String, dynamic> json,
      ) {
    rejRegdid = json['Rej_Regdid'];
    beneficiaryNumber = json['BeneficiaryNumber'];
    beneficiaryName = json['BeneficiaryName'];
    area = json['Area'];
    mobileNo = json['MobileNo'];
    pincode = json['Pincode'];
    arid = json['Arid'];
    rejCampID = json['Rej_CampID'];
    taluka = json['Taluka'];
    isAppointmentConfirm = json['IsAppointmentConfirm'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['Rej_Regdid'] = this.rejRegdid;
    data['BeneficiaryNumber'] = this.beneficiaryNumber;
    data['BeneficiaryName'] = this.beneficiaryName;
    data['Area'] = this.area;
    data['MobileNo'] = this.mobileNo;
    data['Pincode'] = this.pincode;
    data['Arid'] = this.arid;
    data['Rej_CampID'] = this.rejCampID;
    data['Taluka'] = this.taluka;
    data['IsAppointmentConfirm'] = this.isAppointmentConfirm;
    return data;
  }
}