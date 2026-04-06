// export 'package:s2toperational/Modules/Json_Class/CampCountWithDayResponse/CampCountWithDayResponse.dart';
class CampCountWithDayResponse {
  String? status;
  String? message;
  List<CampCountWithDayOutput>? output;

  CampCountWithDayResponse({this.status, this.message, this.output});

  CampCountWithDayResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <CampCountWithDayOutput>[];
      json['output'].forEach((v) {
        output!.add( CampCountWithDayOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class CampCountWithDayOutput {
  int? day;
  int? month;
  int? year;
  String? campDate;
  int? campCount;

  CampCountWithDayOutput({
    this.day,
    this.month,
    this.year,
    this.campDate,
    this.campCount,
  });

  CampCountWithDayOutput.fromJson(Map<String, dynamic> json) {
    day = json['Day'];
    month = json['Month'];
    year = json['Year'];
    campDate = json['CampDate'];
    campCount = json['CampCount'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  <String, dynamic>{};
    data['Day'] = day;
    data['Month'] = month;
    data['Year'] = year;
    data['CampDate'] = campDate;
    data['CampCount'] = campCount;
    return data;
  }
}
