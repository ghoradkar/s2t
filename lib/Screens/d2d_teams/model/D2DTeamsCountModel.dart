// export 'package:s2toperational/Screens/AdminDashboard/Model/D2DTeamsCountModel.dart';
class D2DTeamsCountModel {
  final String status;
  final String message;
  final List<D2DTeamsCountItem> output;

  D2DTeamsCountModel({
    required this.status,
    required this.message,
    required this.output,
  });

  factory D2DTeamsCountModel.fromJson(Map<String, dynamic> json) {
    final list = (json['output'] as List<dynamic>? ?? [])
        .map((e) => D2DTeamsCountItem.fromJson(e as Map<String, dynamic>))
        .toList();

    return D2DTeamsCountModel(
      status: (json['status'] ?? '').toString(),
      message: (json['message'] ?? '').toString(),
      output: list,
    );
  }

  Map<String, dynamic> toJson() => {
    'status': status,
    'message': message,
    'output': output.map((e) => e.toJson()).toList(),
  };
}

class D2DTeamsCountItem {
  final int totalTeamCount;
  final int workingTeamCount;
  final int nonWorkingTeamCount;

  D2DTeamsCountItem({
    required this.totalTeamCount,
    required this.workingTeamCount,
    required this.nonWorkingTeamCount,
  });

  factory D2DTeamsCountItem.fromJson(Map<String, dynamic> json) =>
      D2DTeamsCountItem(
        totalTeamCount: (json['TotalTeamCount'] ?? 0) as int,
        workingTeamCount: (json['WorkingTeamCount'] ?? 0) as int,
        nonWorkingTeamCount: (json['NonWorkingTeamCount'] ?? 0) as int,
      );

  Map<String, dynamic> toJson() => {
    'TotalTeamCount': totalTeamCount,
    'WorkingTeamCount': workingTeamCount,
    'NonWorkingTeamCount': nonWorkingTeamCount,
  };
}
