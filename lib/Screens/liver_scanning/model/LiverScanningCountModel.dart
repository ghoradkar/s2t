class LiverScanningCountModel {
  final int tillDateScanningCount;
  final int todayScanningCount;
  final int machineInstalledCount;

  LiverScanningCountModel({
    required this.tillDateScanningCount,
    required this.todayScanningCount,
    required this.machineInstalledCount,
  });

  factory LiverScanningCountModel.fromJson(Map<String, dynamic> json) {
    return LiverScanningCountModel(
      tillDateScanningCount: json['TillDateScanningCount'] ?? 0,
      todayScanningCount: json['TodayScanningCount'] ?? 0,
      machineInstalledCount: json['MachineInstalledCount'] ?? 0,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'TillDateScanningCount': tillDateScanningCount,
      'TodayScanningCount': todayScanningCount,
      'MachineInstalledCount': machineInstalledCount,
    };
  }
}
