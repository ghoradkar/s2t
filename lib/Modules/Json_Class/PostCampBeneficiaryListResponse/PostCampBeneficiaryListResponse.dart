// ignore_for_file: file_names

import 'dart:convert';

PostCampBeneficiaryListResponse postCampBeneficiaryListModelFromJson(
  String str,
) => PostCampBeneficiaryListResponse.fromJson(json.decode(str));

String postCampBeneficiaryListModelToJson(
  PostCampBeneficiaryListResponse data,
) => json.encode(data.toJson());

class PostCampBeneficiaryListResponse {
  String? status;
  String? message;
  List<PostCampBeneficiaryOutput>? output;

  PostCampBeneficiaryListResponse({this.status, this.message, this.output});

  factory PostCampBeneficiaryListResponse.fromJson(Map<String, dynamic> json) =>
      PostCampBeneficiaryListResponse(
        status: json["status"],
        message: json["message"],
        output:
            json["output"] == null
                ? []
                : List<PostCampBeneficiaryOutput>.from(
                  json["output"].map(
                    (x) => PostCampBeneficiaryOutput.fromJson(x),
                  ),
                ),
      );

  Map<String, dynamic> toJson() => {
    "status": status,
    "message": message,
    "output":
        output == null
            ? []
            : List<dynamic>.from(output!.map((x) => x.toJson())),
  };
}

class PostCampBeneficiaryOutput {
  int? medicalDelivaryId;
  int? treatmentId;
  int? campid;
  int? regdId;
  String? beneficiryNumber;
  String? patientName;
  String? age;
  String? gender;
  String? beneficiryAddress;
  int? pincode;
  String? talname;
  String? distname;
  String? delivarystatus;
  int? deliveryStatusRemarkId;
  String? deliveryStatusName;
  int? deliveryRemarkId;
  String? deliveryRemark;
  String? consentFormPhotoPath;
  String? beneficiaryPhotoPath;
  String? mobileNo;
  int? teamid;
  int? rn;
  String? deliveryChallanId;
  String? otherRemark;
  String? workersMob;
  String? alternateMobNo;
  String? deliveryChallanPhotoPath;

  PostCampBeneficiaryOutput({
    this.medicalDelivaryId,
    this.treatmentId,
    this.campid,
    this.regdId,
    this.beneficiryNumber,
    this.patientName,
    this.age,
    this.gender,
    this.beneficiryAddress,
    this.pincode,
    this.talname,
    this.distname,
    this.delivarystatus,
    this.deliveryStatusRemarkId,
    this.deliveryStatusName,
    this.deliveryRemarkId,
    this.deliveryRemark,
    this.consentFormPhotoPath,
    this.beneficiaryPhotoPath,
    this.mobileNo,
    this.teamid,
    this.rn,
    this.deliveryChallanId,
    this.otherRemark,
    this.workersMob,
    this.alternateMobNo,
    this.deliveryChallanPhotoPath,
  });

  factory PostCampBeneficiaryOutput.fromJson(Map<String, dynamic> json) =>
      PostCampBeneficiaryOutput(
        medicalDelivaryId: json["MedicalDelivaryID"],
        treatmentId: json["TreatmentID"],
        campid: json["campid"],
        regdId: json["RegdId"],
        beneficiryNumber: json["Beneficiry_Number"],
        patientName: json["Patient_Name"],
        age: json["Age"],
        gender: json["Gender"],
        beneficiryAddress: json["Beneficiry_Address"],
        pincode: json["Pincode"],
        talname: json["TALNAME"],
        distname: json["DISTNAME"],
        delivarystatus: json["Delivarystatus"],
        deliveryStatusRemarkId: json["DeliveryStatusRemarkID"],
        deliveryStatusName: json["deliveryStatusName"],
        deliveryRemarkId: json["DeliveryRemarkID"],
        deliveryRemark: json["DeliveryRemark"],
        consentFormPhotoPath: json["Consent_Form_PhotoPath"],
        beneficiaryPhotoPath: json["Beneficiary_photoPath"],
        mobileNo: json["MobileNo"],
        teamid: json["Teamid"],
        rn: json["RN"],
        deliveryChallanId: json["DeliveryChallanID"],
        otherRemark: json["OtherRemark"],
        workersMob: json["WorkersMob"],
        alternateMobNo: json["AlternateMobNo"],
        deliveryChallanPhotoPath: json["DeliveryChallan_PhotoPath"],
      );

  Map<String, dynamic> toJson() => {
    "MedicalDelivaryID": medicalDelivaryId,
    "TreatmentID": treatmentId,
    "campid": campid,
    "RegdId": regdId,
    "Beneficiry_Number": beneficiryNumber,
    "Patient_Name": patientName,
    "Age": age,
    "Gender": gender,
    "Beneficiry_Address": beneficiryAddress,
    "Pincode": pincode,
    "TALNAME": talname,
    "DISTNAME": distname,
    "Delivarystatus": delivarystatus,
    "DeliveryStatusRemarkID": deliveryStatusRemarkId,
    "deliveryStatusName": deliveryStatusName,
    "DeliveryRemarkID": deliveryRemarkId,
    "DeliveryRemark": deliveryRemark,
    "Consent_Form_PhotoPath": consentFormPhotoPath,
    "Beneficiary_photoPath": beneficiaryPhotoPath,
    "MobileNo": mobileNo,
    "Teamid": teamid,
    "RN": rn,
    "DeliveryChallanID": deliveryChallanId,
    "OtherRemark": otherRemark,
    "WorkersMob": workersMob,
    "AlternateMobNo": alternateMobNo,
    "DeliveryChallan_PhotoPath": deliveryChallanPhotoPath,
  };
}
