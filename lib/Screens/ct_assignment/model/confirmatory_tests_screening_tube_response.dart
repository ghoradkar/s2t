class ConfirmatoryTestsScreeningTubeResponse {
  String? status;
  String? message;
  List<ConfirmatoryTestsScreeningTubeOutput>? output;

  ConfirmatoryTestsScreeningTubeResponse({
    this.status,
    this.message,
    this.output,
  });

  ConfirmatoryTestsScreeningTubeResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ConfirmatoryTestsScreeningTubeOutput>[];
      json['output'].forEach((v) {
        output!.add(ConfirmatoryTestsScreeningTubeOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class ConfirmatoryTestsScreeningTubeOutput {
  int? regdid;
  String? regdNo;
  String? beneficiaryName;
  int? patAge;
  String? gender;
  String? mobileNo;
  String? relationWithWorker;
  int? tubeId;
  String? tubName;
  int? tubCount;
  String? tubColor;
  String? workersMob;
  String? alternateMobNo;

  ConfirmatoryTestsScreeningTubeOutput({
    this.regdid,
    this.regdNo,
    this.beneficiaryName,
    this.patAge,
    this.gender,
    this.mobileNo,
    this.relationWithWorker,
    this.tubeId,
    this.tubName,
    this.tubCount,
    this.tubColor,
    this.workersMob,
    this.alternateMobNo,
  });

  ConfirmatoryTestsScreeningTubeOutput.fromJson(Map<String, dynamic> json) {
    regdid = json['Regdid'];
    regdNo = json['RegdNo'];
    beneficiaryName = json['BeneficiaryName'];
    patAge = json['PatAge'];
    gender = json['Gender'];
    mobileNo = json['MobileNo'];
    relationWithWorker = json['RelationWithWorker'];
    tubeId = json['TubeId'];
    tubName = json['TubName'];
    tubCount = json['TubCount'];
    tubColor = json['TubColor'];
    workersMob = json['WorkersMob'];
    alternateMobNo = json['AlternateMobNo'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['Regdid'] = regdid;
    data['RegdNo'] = regdNo;
    data['BeneficiaryName'] = beneficiaryName;
    data['PatAge'] = patAge;
    data['Gender'] = gender;
    data['MobileNo'] = mobileNo;
    data['RelationWithWorker'] = relationWithWorker;
    data['TubeId'] = tubeId;
    data['TubName'] = tubName;
    data['TubCount'] = tubCount;
    data['TubColor'] = tubColor;
    data['WorkersMob'] = workersMob;
    data['AlternateMobNo'] = alternateMobNo;
    return data;
  }
}
