class CampReadinessFormSubmittResponse {
  String? status;
  String? message;

  CampReadinessFormSubmittResponse({this.status, this.message});

  CampReadinessFormSubmittResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    return data;
  }
}
