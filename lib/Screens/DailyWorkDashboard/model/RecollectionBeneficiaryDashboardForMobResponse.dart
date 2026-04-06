// export 'package:s2toperational/Modules/Json_Class/RecollectionBeneficiaryDashboardForMobResponse/RecollectionBeneficiaryDashboardForMobResponse.dart';
class RecollectionBeneficiaryDashboardForMobResponse {
  String? status;
  String? message;
  List<RecollectionBeneficiaryDashboardForMobOutput>? output;

  RecollectionBeneficiaryDashboardForMobResponse({
    this.status,
    this.message,
    this.output,
  });

  RecollectionBeneficiaryDashboardForMobResponse.fromJson(
      Map<String, dynamic> json,
      ) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <RecollectionBeneficiaryDashboardForMobOutput>[];
      json['output'].forEach((v) {
        output!.add(
          new RecollectionBeneficiaryDashboardForMobOutput.fromJson(v),
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

class RecollectionBeneficiaryDashboardForMobOutput {
  String? district;
  int? distlgdcode;
  int? rejectedBeneficiaries;
  int? assignedForScreening;
  int? notAssignedForScreening;
  int? interestedInScreening;
  int? notInterestedInScreening;
  int? notAvailableForScreening;
  int? deniedForScreening;
  int? reScreeningPendingAsCardExpired;
  int? reScreenedBeneficiaries;

  RecollectionBeneficiaryDashboardForMobOutput({
    this.district,
    this.distlgdcode,
    this.rejectedBeneficiaries,
    this.assignedForScreening,
    this.notAssignedForScreening,
    this.interestedInScreening,
    this.notInterestedInScreening,
    this.notAvailableForScreening,
    this.deniedForScreening,
    this.reScreeningPendingAsCardExpired,
    this.reScreenedBeneficiaries,
  });

  RecollectionBeneficiaryDashboardForMobOutput.fromJson(
      Map<String, dynamic> json,
      ) {
    district = json['District'];
    distlgdcode = json['DISTLGDCODE'];
    rejectedBeneficiaries = json['RejectedBeneficiaries'];
    assignedForScreening = json['AssignedForScreening'];
    notAssignedForScreening = json['NotAssignedForScreening'];
    interestedInScreening = json['InterestedInScreening'];
    notInterestedInScreening = json['NotInterestedInScreening'];
    notAvailableForScreening = json['NotAvailableForScreening'];
    deniedForScreening = json['DeniedForScreening'];
    reScreeningPendingAsCardExpired = json['ReScreeningPendingAsCardExpired'];
    reScreenedBeneficiaries = json['ReScreenedBeneficiaries'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['District'] = this.district;
    data['DISTLGDCODE'] = this.distlgdcode;
    data['RejectedBeneficiaries'] = this.rejectedBeneficiaries;
    data['AssignedForScreening'] = this.assignedForScreening;
    data['NotAssignedForScreening'] = this.notAssignedForScreening;
    data['InterestedInScreening'] = this.interestedInScreening;
    data['NotInterestedInScreening'] = this.notInterestedInScreening;
    data['NotAvailableForScreening'] = this.notAvailableForScreening;
    data['DeniedForScreening'] = this.deniedForScreening;
    data['ReScreeningPendingAsCardExpired'] =
        this.reScreeningPendingAsCardExpired;
    data['ReScreenedBeneficiaries'] = this.reScreenedBeneficiaries;
    return data;
  }
}