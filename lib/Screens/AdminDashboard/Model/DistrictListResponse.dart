import 'dart:convert';

class DistrictListResponse {
  final String status;
  final String message;
  final List<District> output;

  const DistrictListResponse({
    required this.status,
    required this.message,
    required this.output,
  });

  /// Parse from a JSON string
  factory DistrictListResponse.fromJson(String source) =>
      DistrictListResponse.fromMap(json.decode(source) as Map<String, dynamic>);

  /// Parse from a decoded map
  factory DistrictListResponse.fromMap(Map<String, dynamic> map) {
    final list = map['output'];
    return DistrictListResponse(
      status: (map['status'] ?? '').toString(),
      message: (map['message'] ?? '').toString(),
      output: (list is List
          ? list
          : const <dynamic>[]) // safety if 'output' is null or not a list
          .map((e) => District.fromMap(e as Map<String, dynamic>))
          .toList(growable: false),
    );
  }

  Map<String, dynamic> toMap() => {
    'status': status,
    'message': message,
    'output': output.map((e) => e.toMap()).toList(growable: false),
  };

  String toJson() => json.encode(toMap());
}

class District {
  final int srNo;
  final String name;    // from DISTNAME
  final int lgdCode;    // from DISTLGDCODE

  const District({
    required this.srNo,
    required this.name,
    required this.lgdCode,
  });

  factory District.fromJson(String source) =>
      District.fromMap(json.decode(source) as Map<String, dynamic>);

  factory District.fromMap(Map<String, dynamic> map) => District(
    srNo: (map['SrNo'] as num?)?.toInt() ?? 0,
    name: (map['DISTNAME'] ?? '').toString(),
    lgdCode: (map['DISTLGDCODE'] as num?)?.toInt() ?? 0,
  );

  Map<String, dynamic> toMap() => {
    'SrNo': srNo,
    'DISTNAME': name,
    'DISTLGDCODE': lgdCode,
  };

  String toJson() => json.encode(toMap());
}
