// export 'package:s2toperational/Screens/AdminDashboard/Model/D2DTeamsListModel.dart';

import 'package:equatable/equatable.dart';

class D2DTeamsListModel {
  final String status;
  final String message;
  final List<D2DTeamItem> output;

  D2DTeamsListModel({
    required this.status,
    required this.message,
    required this.output,
  });

  factory D2DTeamsListModel.fromJson(Map<String, dynamic> json) {
    final list = (json['output'] as List<dynamic>? ?? [])
        .map((e) => D2DTeamItem.fromJson(e as Map<String, dynamic>))
        .toList();
    return D2DTeamsListModel(
      status: (json['status'] ?? '').toString(),
      message: (json['message'] ?? '').toString(),
      output: list,
    );
  }
}

class D2DTeamItem extends Equatable {
  final int distlgdcode;
  final String distname;
  final int divId;
  final int campType;
  final int campCoId;
  final String campCoordinatorName;
  final String campCoordinatorMobNo;
  final int desgId;
  final int totalTeamCount;
  final int workingTeamCount;
  final int nonWorkingTeamCount;

  const D2DTeamItem({
    required this.distlgdcode,
    required this.distname,
    required this.divId,
    required this.campType,
    required this.campCoId,
    required this.campCoordinatorName,
    required this.campCoordinatorMobNo,
    required this.desgId,
    required this.totalTeamCount,
    required this.workingTeamCount,
    required this.nonWorkingTeamCount,
  });

  factory D2DTeamItem.fromJson(Map<String, dynamic> json) => D2DTeamItem(
    distlgdcode: (json['DISTLGDCODE'] ?? 0) as int,
    distname: (json['DISTNAME'] ?? '').toString(),
    divId: (json['DivId'] ?? 0) as int,
    campType: (json['CampType'] ?? 0) as int,
    campCoId: (json['CampCoId'] ?? 0) as int,
    campCoordinatorName: (json['CampCoordinatorName'] ?? '').toString(),
    campCoordinatorMobNo: (json['CampCoordinatorMobNo'] ?? '').toString(),
    desgId: (json['DesgId'] ?? 0) as int,
    totalTeamCount: (json['TotalTeamCount'] ?? 0) as int,
    workingTeamCount: (json['WorkingTeamCount'] ?? 0) as int,
    nonWorkingTeamCount: (json['NonWorkingTeamCount'] ?? 0) as int,
  );

  @override
  List<Object?> get props => [
    distlgdcode,
    distname,
    divId,
    campType,
    campCoId,
    campCoordinatorName,
    campCoordinatorMobNo,
    desgId,
    totalTeamCount,
    workingTeamCount,
    nonWorkingTeamCount,
  ];
}

/// UI row model you’ll bind to the table
class D2DTeamRow {
  final String district;
  final String coordinator;
  final int notWorking;
  final int working;
  final String? phone; // for Call icon
  final int distLgdCode;

  D2DTeamRow({
    required this.district,
    required this.coordinator,
    required this.notWorking,
    required this.working,
    required this.distLgdCode,
    this.phone,
  });

  factory D2DTeamRow.fromItem(D2DTeamItem x) => D2DTeamRow(
    district: x.distname,
    coordinator: x.campCoordinatorName,
    notWorking: x.nonWorkingTeamCount,
    working: x.workingTeamCount,
    phone: x.campCoordinatorMobNo,
    distLgdCode: x.distlgdcode,
  );
}
