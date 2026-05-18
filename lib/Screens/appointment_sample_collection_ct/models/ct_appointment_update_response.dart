class CTAppointmentUpdateResponse {
  String? status;
  String? message;

  CTAppointmentUpdateResponse({this.status, this.message});

  CTAppointmentUpdateResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
  }
}