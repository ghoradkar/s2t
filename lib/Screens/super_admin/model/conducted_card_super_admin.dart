class ConductedCardSuperAdmin {
  final String status;
  final String message;
  final List<LandingPageOutput> output;

  ConductedCardSuperAdmin({
    required this.status,
    required this.message,
    required this.output,
  });

  factory ConductedCardSuperAdmin.fromJson(Map<String, dynamic> json) {
    return ConductedCardSuperAdmin(
      status: json['status'],
      message: json['message'],
      output:
          (json['output'] as List)
              .map((e) => LandingPageOutput.fromJson(e))
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

class LandingPageOutput {
  final String financialYear;
  final int subOrgId;
  final String subOrgName;
  final int totalCamp;
  final int regularCamp;
  final int d2dCamp;
  final int totalBillableCount;
  final DateTime updatedTime;

  LandingPageOutput({
    required this.financialYear,
    required this.subOrgId,
    required this.subOrgName,
    required this.totalCamp,
    required this.regularCamp,
    required this.d2dCamp,
    required this.totalBillableCount,
    required this.updatedTime,
  });

  factory LandingPageOutput.fromJson(Map<String, dynamic> json) {
    // Parsing /Date(1758629353260)/ format
    final dateString = json['UpdatedTime'] as String;
    final milliseconds = int.parse(
      RegExp(r'\d+').firstMatch(dateString)!.group(0)!,
    ); // extract number
    return LandingPageOutput(
      financialYear: json['FinancialYear'],
      subOrgId: json['SubOrgId'],
      subOrgName: json['SubOrgName'],
      totalCamp: json['Total Camp'],
      regularCamp: json['Regular Camp'],
      d2dCamp: json['D2D Camp'],
      totalBillableCount: json['TotalBillablecount'],
      updatedTime: DateTime.fromMillisecondsSinceEpoch(milliseconds),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'FinancialYear': financialYear,
      'SubOrgId': subOrgId,
      'SubOrgName': subOrgName,
      'Total Camp': totalCamp,
      'Regular Camp': regularCamp,
      'D2D Camp': d2dCamp,
      'TotalBillablecount': totalBillableCount,
      'UpdatedTime': '/Date(${updatedTime.millisecondsSinceEpoch})/',
    };
  }
}
