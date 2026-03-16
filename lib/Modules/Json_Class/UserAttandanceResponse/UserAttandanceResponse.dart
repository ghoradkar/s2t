// ignore_for_file: file_names

class UserAttandanceResponse {
  String? status;
  String? message;
  List<UserAttandanceOutput>? output;

  UserAttandanceResponse({this.status, this.message, this.output});

  UserAttandanceResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <UserAttandanceOutput>[];
      json['output'].forEach((v) {
        output!.add(UserAttandanceOutput.fromJson(v));
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

class UserAttandanceOutput {
  int? year;
  int? month;
  int? day;
  int? dAYTYPE;

  UserAttandanceOutput({this.year, this.month, this.day, this.dAYTYPE});

  UserAttandanceOutput.fromJson(Map<String, dynamic> json) {
    year = json['Year'];
    month = json['Month'];
    day = json['Day'];
    dAYTYPE = json['DAYTYPE'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Year'] = year;
    data['Month'] = month;
    data['Day'] = day;
    data['DAYTYPE'] = dAYTYPE;
    return data;
  }
}
