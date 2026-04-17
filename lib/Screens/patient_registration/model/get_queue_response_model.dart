// ignore_for_file: file_names

class GetQueueResponseModel {
  String? status;
  String? message;
  List<QueueOutput> output;

  GetQueueResponseModel({this.status, this.message, this.output = const []});

  factory GetQueueResponseModel.fromJson(Map<String, dynamic> json) {
    final list = json['output'];
    return GetQueueResponseModel(
      status: json['status']?.toString(),
      message: json['message']?.toString(),
      output:
          (list is List)
              ? list
                  .map(
                    (e) => QueueOutput.fromJson(e as Map<String, dynamic>),
                  )
                  .toList()
              : [],
    );
  }
}

class QueueOutput {
  int? identityID;
  String? requestId;
  String? timeStamp;
  String? hipCode;
  String? healthId;
  String? healthIdNumber;
  String? name;
  String? gender;
  String? address;
  String? yearOfBirth;
  String? dayOfBirth;
  String? monthOfBirth;
  String? identifiers;
  String? patient;
  String? response;
  String? createdOn;
  String? errorMessage;
  String? authtoken;
  int? isRegistered;
  String? mobileNo;
  int? campId;

  QueueOutput.fromJson(Map<String, dynamic> json) {
    identityID = _toInt(json['identityID']);
    requestId = json['requestId']?.toString();
    timeStamp = json['time_stamp']?.toString();
    hipCode = json['hipCode']?.toString();
    healthId = json['healthId']?.toString();
    healthIdNumber = json['healthIdNumber']?.toString();
    name = json['name']?.toString();
    gender = json['gender']?.toString();
    address = json['address']?.toString();
    yearOfBirth = json['yearOfBirth']?.toString();
    dayOfBirth = json['dayOfBirth']?.toString();
    monthOfBirth = json['monthOfBirth']?.toString();
    identifiers = json['identifiers']?.toString();
    patient = json['patient']?.toString();
    response = json['response']?.toString();
    createdOn = json['CreatedOn']?.toString();
    errorMessage = json['ErrorMessage']?.toString();
    authtoken = json['authtoken']?.toString();
    isRegistered = _toInt(json['IsRegistered']);
    mobileNo = json['MobileNo']?.toString();
    campId = _toInt(json['Campid']);
  }

  static int? _toInt(dynamic v) {
    if (v == null) return null;
    if (v is int) return v;
    return int.tryParse(v.toString());
  }
}