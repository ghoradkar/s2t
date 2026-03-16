// ignore_for_file: file_names

class HubHomelabDashboardResponse {
  String? status;
  String? message;
  List<HubHomelabDashboardOutput>? output;

  HubHomelabDashboardResponse({this.status, this.message, this.output});

  HubHomelabDashboardResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <HubHomelabDashboardOutput>[];
      json['output'].forEach((v) {
        output!.add(HubHomelabDashboardOutput.fromJson(v));
      });
    }
  }
}

class HubHomelabDashboardOutput {
  int? homeLab;
  int? hubLab;
  String? monthYear;
  String? lastUpdatedDate;
  String? labName;
  String? labType;
  int? processingCount;

  HubHomelabDashboardOutput({
    this.homeLab,
    this.hubLab,
    this.monthYear,
    this.lastUpdatedDate,
    this.labName,
    this.labType,
    this.processingCount,
  });

  HubHomelabDashboardOutput.fromJson(Map<String, dynamic> json) {
    homeLab = json['HomeLab'];
    hubLab = json['HubLab'];
    monthYear = json['MonthYear'];
    lastUpdatedDate = json['LastUpdatedDate'];
    labName = json['LabName'];
    labType = json['LabType'];
    processingCount = json['ProcessingCount'];
  }
}