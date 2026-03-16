class CampTypeListModel {
  final String status;
  final String message;
  final List<CampTypeItem> output;

  CampTypeListModel({
    required this.status,
    required this.message,
    required this.output,
  });

  factory CampTypeListModel.fromJson(Map<String, dynamic> json) {
    final list = (json['output'] as List<dynamic>? ?? [])
        .map((e) => CampTypeItem.fromJson(e as Map<String, dynamic>))
        .toList();

    return CampTypeListModel(
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

class CampTypeItem {
  final int campType;
  final String description;

  CampTypeItem({
    required this.campType,
    required this.description,
  });

  factory CampTypeItem.fromJson(Map<String, dynamic> json) => CampTypeItem(
    campType: (json['CAMPTYPE'] ?? 0) as int,
    description: (json['CampTypeDescription'] ?? '').toString(),
  );

  Map<String, dynamic> toJson() => {
    'CAMPTYPE': campType,
    'CampTypeDescription': description,
  };
}
