// export 'package:s2toperational/Screens/AdminDashboard/Model/OrganizationListModel.dart';

class OrganizationListModel {
  final String status;
  final String message;
  final List<OrganizationItem> output;

  OrganizationListModel({
    required this.status,
    required this.message,
    required this.output,
  });

  factory OrganizationListModel.fromJson(Map<String, dynamic> json) {
    final list = (json['output'] as List<dynamic>? ?? [])
        .map((e) => OrganizationItem.fromJson(e as Map<String, dynamic>))
        .toList();

    return OrganizationListModel(
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

class OrganizationItem {
  final int subOrgId;
  final String subOrgName;

  OrganizationItem({
    required this.subOrgId,
    required this.subOrgName,
  });

  factory OrganizationItem.fromJson(Map<String, dynamic> json) =>
      OrganizationItem(
        subOrgId: (json['SubOrgId'] ?? 0) as int,
        subOrgName: (json['SubOrgName'] ?? '').toString(),
      );

  Map<String, dynamic> toJson() => {
    'SubOrgId': subOrgId,
    'SubOrgName': subOrgName,
  };
}
