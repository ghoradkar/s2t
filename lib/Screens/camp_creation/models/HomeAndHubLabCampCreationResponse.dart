// ignore_for_file: file_names

class HomeAndHubLabCampCreationResponse {
  String? status;
  String? message;
  List<HomeAndHubLabCampCreationOutput>? output;

  HomeAndHubLabCampCreationResponse({this.status, this.message, this.output});

  HomeAndHubLabCampCreationResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <HomeAndHubLabCampCreationOutput>[];
      json['output'].forEach((v) {
        output!.add(HomeAndHubLabCampCreationOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class HomeAndHubLabCampCreationOutput {
  int? homeLabcode;
  String? homeLab;
  int? hubLabcode;
  String? hubLab;
  HomeAndHubLabCampCreationOutput(
      {this.homeLabcode, this.homeLab, this.hubLabcode, this.hubLab});

  HomeAndHubLabCampCreationOutput.fromJson(Map<String, dynamic> json) {
    homeLabcode = json['HomeLabcode'];
    homeLab = json['HomeLab'];
    hubLabcode = json['HubLabcode'];
    hubLab = json['HubLab'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['HomeLabcode'] = homeLabcode;
    data['HomeLab'] = homeLab;
    data['HubLabcode'] = hubLabcode;
    data['HubLab'] = hubLab;
    return data;
  }
}
