class D2DNonWorkingTeamsResponse {
  String? status;
  String? message;
  List<D2DNonWorkingTeamsOutput>? output;

  D2DNonWorkingTeamsResponse({this.status, this.message, this.output});

  D2DNonWorkingTeamsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <D2DNonWorkingTeamsOutput>[];
      json['output'].forEach((v) {
        output!.add( D2DNonWorkingTeamsOutput.fromJson(v));
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

class D2DNonWorkingTeamsOutput {
  int? teamid;
  String? teamName;
  String? dISTNAME;
  String? member1;
  String? member2;
  int? regBeneficieries;

  D2DNonWorkingTeamsOutput({
    this.teamid,
    this.teamName,
    this.dISTNAME,
    this.member1,
    this.member2,
    this.regBeneficieries,
  });

  D2DNonWorkingTeamsOutput.fromJson(Map<String, dynamic> json) {
    teamid = json['Teamid'];
    teamName = json['TeamName'];
    dISTNAME = json['DISTNAME'];
    member1 = json['Member1'];
    member2 = json['Member2'];
    regBeneficieries = json['RegBeneficieries'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['Teamid'] = teamid;
    data['TeamName'] = teamName;
    data['DISTNAME'] = dISTNAME;
    data['Member1'] = member1;
    data['Member2'] = member2;
    data['RegBeneficieries'] = regBeneficieries;
    return data;
  }
}
