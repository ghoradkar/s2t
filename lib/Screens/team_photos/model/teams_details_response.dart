// ignore_for_file: file_names

class TeamsDetailsResponse {
  String? status;
  String? message;
  List<TeamsDetailsOutput>? output;

  TeamsDetailsResponse({this.status, this.message, this.output});

  TeamsDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <TeamsDetailsOutput>[];
      json['output'].forEach((v) {
        output!.add(TeamsDetailsOutput.fromJson(v));
      });
    }
  }
}

class TeamsDetailsOutput {
  String? teamNumber;
  String? teamName;
  String? member1;
  String? member2;
  bool isSelected = false;

  TeamsDetailsOutput({
    this.teamNumber,
    this.teamName,
    this.member1,
    this.member2,
  });

  TeamsDetailsOutput.fromJson(Map<String, dynamic> json) {
    teamNumber = json['TeamNumber']?.toString();
    teamName = json['TeamName'];
    member1 = json['Member1'];
    member2 = json['Member2'];
  }

  String get displayName {
    final parts = <String>[
      if (teamName != null && teamName!.isNotEmpty) teamName!,
      if (member1 != null && member1!.isNotEmpty) member1!,
      if (member2 != null && member2!.isNotEmpty) member2!,
    ];
    return parts.join(' | ');
  }
}
