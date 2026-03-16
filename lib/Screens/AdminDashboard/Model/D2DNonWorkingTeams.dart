class D2dNonWorkingTeams {
  final String status;
  final String message;
  final List<D2dNonWorkingTeam> output;

  D2dNonWorkingTeams({
    required this.status,
    required this.message,
    required this.output,
  });

  factory D2dNonWorkingTeams.fromJson(Map<String, dynamic> json) {
    return D2dNonWorkingTeams(
      status: json['status'] ?? '',
      message: json['message'] ?? '',
      output: (json['output'] as List<dynamic>?)
          ?.map((e) => D2dNonWorkingTeam.fromJson(e))
          .toList() ??
          [],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'status': status,
      'message': message,
      'output': output.map((e) => e.toJson()).toList(),
    };
  }
}

class D2dNonWorkingTeam {
  final int teamId;
  final String teamName;
  final String distName;
  final String? member1;
  final String? member2;
  final int regBeneficiaries;

  D2dNonWorkingTeam({
    required this.teamId,
    required this.teamName,
    required this.distName,
    this.member1,
    this.member2,
    required this.regBeneficiaries,
  });

  factory D2dNonWorkingTeam.fromJson(Map<String, dynamic> json) {
    return D2dNonWorkingTeam(
      teamId: json['Teamid'] ?? 0,
      teamName: json['TeamName'] ?? '',
      distName: json['DISTNAME'] ?? '',
      member1: json['Member1'],
      member2: json['Member2'],
      regBeneficiaries: json['RegBeneficieries'] ?? 0,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'Teamid': teamId,
      'TeamName': teamName,
      'DISTNAME': distName,
      'Member1': member1,
      'Member2': member2,
      'RegBeneficieries': regBeneficiaries,
    };
  }
}
