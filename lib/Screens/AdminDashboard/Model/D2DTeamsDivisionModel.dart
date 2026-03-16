class D2DTeamsDivisionModel {
  final String status;
  final String message;
  final List<DivisionItem> output;

  D2DTeamsDivisionModel({
    required this.status,
    required this.message,
    required this.output,
  });

  factory D2DTeamsDivisionModel.fromJson(Map<String, dynamic> json) {
    final list = (json['output'] as List<dynamic>? ?? [])
        .map((e) => DivisionItem.fromJson(e as Map<String, dynamic>))
        .toList();

    return D2DTeamsDivisionModel(
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

class DivisionItem {
  final int divId;
  final String divName;
  final int subOrgId;
  final String subOrgName;

  DivisionItem({
    required this.divId,
    required this.divName,
    required this.subOrgId,
    required this.subOrgName,
  });

  factory DivisionItem.fromJson(Map<String, dynamic> json) => DivisionItem(
    divId: (json['DIVID'] ?? 0) as int,
    divName: (json['DIVNAME'] ?? '').toString(),
    subOrgId: (json['SubOrgId'] ?? 0) as int,
    subOrgName: (json['SubOrgName'] ?? '').toString(),
  );

  Map<String, dynamic> toJson() => {
    'DIVID': divId,
    'DIVNAME': divName,
    'SubOrgId': subOrgId,
    'SubOrgName': subOrgName,
  };
}
