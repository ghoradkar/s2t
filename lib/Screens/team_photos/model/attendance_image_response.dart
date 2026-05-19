// ignore_for_file: file_names

class AttendanceImageResponse {
  String? status;
  String? message;
  List<AttendanceImageOutput>? output;

  AttendanceImageResponse({this.status, this.message, this.output});

  AttendanceImageResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <AttendanceImageOutput>[];
      json['output'].forEach((v) {
        output!.add(AttendanceImageOutput.fromJson(v));
      });
    }
  }
}

class AttendanceImageOutput {
  int? campId;
  double? latitude;
  double? longitude;
  String? inImage;
  String? inImageUploadedOn;
  String? outImage;
  String? outImageUploadedOn;
  String? duringCampImage;
  String? duringCampImageUploadedOn;
  String? isInImageApproved;
  String? isOutImageApproved;
  String? isDuringImageApproved;

  AttendanceImageOutput({
    this.campId,
    this.latitude,
    this.longitude,
    this.inImage,
    this.inImageUploadedOn,
    this.outImage,
    this.outImageUploadedOn,
    this.duringCampImage,
    this.duringCampImageUploadedOn,
    this.isInImageApproved,
    this.isOutImageApproved,
    this.isDuringImageApproved,
  });

  AttendanceImageOutput.fromJson(Map<String, dynamic> json) {
    campId = json['CampId'];
    latitude = (json['Lattitude'] as num?)?.toDouble();
    longitude = (json['Longitude'] as num?)?.toDouble();
    inImage = json['In_Image'];
    inImageUploadedOn = json['In_Image_UploadedOn'];
    outImage = json['Out_Image'];
    outImageUploadedOn = json['Out_Image_UploadedOn'];
    duringCampImage = json['DuringCamp_Image'];
    duringCampImageUploadedOn = json['DuringCamp_Image_UploadedOn'];
    isInImageApproved = json['IsInImageApproved'];
    isOutImageApproved = json['IsOutImageApproved'];
    isDuringImageApproved = json['IsDuringImageApproved'];
  }

  bool get hasInImage => inImage != null && inImage!.isNotEmpty && inImage != 'NA';
  bool get hasOutImage => outImage != null && outImage!.isNotEmpty && outImage != 'NA';
  bool get hasDuringCampImage =>
      duringCampImage != null && duringCampImage!.isNotEmpty && duringCampImage != 'NA';
}
