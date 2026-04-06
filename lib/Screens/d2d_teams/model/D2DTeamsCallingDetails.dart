// export 'package:s2toperational/Screens/AdminDashboard/Model/D2DTeamsCallingDetails.dart';
class D2DTeamsCallingDetails {
  final String status;
  final String message;
  final List<TeamMember> output;

  D2DTeamsCallingDetails({
    required this.status,
    required this.message,
    required this.output,
  });

  factory D2DTeamsCallingDetails.fromJson(Map<String, dynamic> json) {
    return D2DTeamsCallingDetails(
      status: json['status'] ?? '',
      message: json['message'] ?? '',
      output: (json['output'] as List<dynamic>)
          .map((e) => TeamMember.fromJson(e))
          .toList(),
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

class TeamMember {
  final int teamId;
  final String teamName;
  final int userId;
  final String memberName;
  final String mobNo;
  final String desgName;
  final String desgShortCode;

  TeamMember({
    required this.teamId,
    required this.teamName,
    required this.userId,
    required this.memberName,
    required this.mobNo,
    required this.desgName,
    required this.desgShortCode,
  });

  factory TeamMember.fromJson(Map<String, dynamic> json) {
    return TeamMember(
      teamId: json['Teamid'] ?? 0,
      teamName: json['TeamName'] ?? '',
      userId: json['UserID'] ?? 0,
      memberName: json['MemberName'] ?? '',
      mobNo: json['MOBNO'] ?? '',
      desgName: json['DesgName'] ?? '',
      desgShortCode: json['DesgShortCode'] ?? '',
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'Teamid': teamId,
      'TeamName': teamName,
      'UserID': userId,
      'MemberName': memberName,
      'MOBNO': mobNo,
      'DesgName': desgName,
      'DesgShortCode': desgShortCode,
    };
  }
}