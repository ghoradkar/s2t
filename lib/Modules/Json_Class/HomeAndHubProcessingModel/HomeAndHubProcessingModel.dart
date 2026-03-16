// ignore_for_file: file_names

class HomeAndHubProcessingModel {
  final String status;
  final String message;
  final List<HomeAndHubProcessOutput> output;

  HomeAndHubProcessingModel({
    required this.status,
    required this.message,
    required this.output,
  });

  factory HomeAndHubProcessingModel.fromJson(Map<String, dynamic> json) {
    return HomeAndHubProcessingModel(
      status: json['status'] ?? '',
      message: json['message'] ?? '',
      output:
          (json['output'] as List<dynamic>?)
              ?.map((e) => HomeAndHubProcessOutput.fromJson(e))
              .toList() ??
          [],
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

class HomeAndHubProcessOutput {
  final int totalMonthsBeneficiaryCount;
  final int todaysBeneficiaryCount;
  final int homeLabProcessedCount;
  final int homeLabProcessedPendingCount;
  final int hubLabProcessedCount;
  final int hubLabProcessedPendingCount;

  HomeAndHubProcessOutput({
    required this.totalMonthsBeneficiaryCount,
    required this.todaysBeneficiaryCount,
    required this.homeLabProcessedCount,
    required this.homeLabProcessedPendingCount,
    required this.hubLabProcessedCount,
    required this.hubLabProcessedPendingCount,
  });

  factory HomeAndHubProcessOutput.fromJson(Map<String, dynamic> json) {
    return HomeAndHubProcessOutput(
      totalMonthsBeneficiaryCount: json['TotalMonthsBeneficiaryCount'] ?? 0,
      todaysBeneficiaryCount: json['TodaysBeneficiaryCount'] ?? 0,
      homeLabProcessedCount: json['HomeLabProcessedCount'] ?? 0,
      homeLabProcessedPendingCount: json['HomeLabProcessedPendingCount'] ?? 0,
      hubLabProcessedCount: json['HubLabProcessedCount'] ?? 0,
      hubLabProcessedPendingCount: json['HubLabProcessedPendingCount'] ?? 0,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'TotalMonthsBeneficiaryCount': totalMonthsBeneficiaryCount,
      'TodaysBeneficiaryCount': todaysBeneficiaryCount,
      'HomeLabProcessedCount': homeLabProcessedCount,
      'HomeLabProcessedPendingCount': homeLabProcessedPendingCount,
      'HubLabProcessedCount': hubLabProcessedCount,
      'HubLabProcessedPendingCount': hubLabProcessedPendingCount,
    };
  }
}
