class D2DTeamsLabModel {
  final String status;
  final String message;
  final List<LabItem> output;

  D2DTeamsLabModel({
    required this.status,
    required this.message,
    required this.output,
  });

  factory D2DTeamsLabModel.fromJson(Map<String, dynamic> json) {
    final list = (json['output'] as List<dynamic>? ?? [])
        .map((e) => LabItem.fromJson(e as Map<String, dynamic>))
        .toList();

    return D2DTeamsLabModel(
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

class LabItem {
  final int distLgdCode;
  final String distName;
  final int labCode;
  final String labName;

  LabItem({
    required this.distLgdCode,
    required this.distName,
    required this.labCode,
    required this.labName,
  });

  factory LabItem.fromJson(Map<String, dynamic> json) => LabItem(
    distLgdCode: (json['DISTLGDCODE'] ?? 0) as int,
    distName: (json['DISTNAME'] ?? '').toString(),
    labCode: (json['LabCode'] ?? 0) as int,
    labName: (json['LabName'] ?? '').toString(),
  );

  Map<String, dynamic> toJson() => {
    'DISTLGDCODE': distLgdCode,
    'DISTNAME': distName,
    'LabCode': labCode,
    'LabName': labName,
  };
}
