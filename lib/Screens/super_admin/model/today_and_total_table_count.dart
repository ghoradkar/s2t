class TodaysAndTotalTableCount {
  final String status;
  final String path;
  final String dateTime;
  final CountDetails details;

  TodaysAndTotalTableCount({
    required this.status,
    required this.path,
    required this.dateTime,
    required this.details,
  });

  factory TodaysAndTotalTableCount.fromJson(Map<String, dynamic> json) {
    return TodaysAndTotalTableCount(
      status: json['status'] ?? '',
      path: json['path'] ?? '',
      dateTime: json['dateTime'] ?? '',
      details: CountDetails.fromJson(json['details'] ?? {}),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'status': status,
      'path': path,
      'dateTime': dateTime,
      'details': details.toJson(),
    };
  }
}

class CountDetails {
  final int treatmentGivenHSCC;
  final int treatmentGivenHLL;
  final int treatmentGivenTotal;
  final int ipdRegisteredHSCC;
  final int ipdRegisteredHLL;
  final int ipdRegisteredTotal;
  final int dischargePatientHSCC;
  final int dischargePatientHLL;
  final int dischargePatientTotal;
  final int prescriptionGivenHSCC;
  final int prescriptionGivenHLL;
  final int prescriptionGivenTotal;
  final int prescriptionIssuedHSCC;
  final int prescriptionIssuedHLL;
  final int prescriptionIssuedTotal;

  CountDetails({
    required this.treatmentGivenHSCC,
    required this.treatmentGivenHLL,
    required this.treatmentGivenTotal,
    required this.ipdRegisteredHSCC,
    required this.ipdRegisteredHLL,
    required this.ipdRegisteredTotal,
    required this.dischargePatientHSCC,
    required this.dischargePatientHLL,
    required this.dischargePatientTotal,
    required this.prescriptionGivenHSCC,
    required this.prescriptionGivenHLL,
    required this.prescriptionGivenTotal,
    required this.prescriptionIssuedHSCC,
    required this.prescriptionIssuedHLL,
    required this.prescriptionIssuedTotal,
  });

  factory CountDetails.fromJson(Map<String, dynamic> json) {
    return CountDetails(
      treatmentGivenHSCC: json['treatmentGivenHSCC'] ?? 0,
      treatmentGivenHLL: json['treatmentGivenHLL'] ?? 0,
      treatmentGivenTotal: json['treatmentGivenTotal'] ?? 0,
      ipdRegisteredHSCC: json['ipdRegisteredHSCC'] ?? 0,
      ipdRegisteredHLL: json['ipdRegisteredHLL'] ?? 0,
      ipdRegisteredTotal: json['ipdRegisteredTotal'] ?? 0,
      dischargePatientHSCC: json['dischargePatientHSCC'] ?? 0,
      dischargePatientHLL: json['dischargePatientHLL'] ?? 0,
      dischargePatientTotal: json['dischargePatientTotal'] ?? 0,
      prescriptionGivenHSCC: json['prescriptionGivenHSCC'] ?? 0,
      prescriptionGivenHLL: json['prescriptionGivenHLL'] ?? 0,
      prescriptionGivenTotal: json['prescriptionGivenTotal'] ?? 0,
      prescriptionIssuedHSCC: json['prescriptionIssuedHSCC'] ?? 0,
      prescriptionIssuedHLL: json['prescriptionIssuedHLL'] ?? 0,
      prescriptionIssuedTotal: json['prescriptionIssuedTotal'] ?? 0,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'treatmentGivenHSCC': treatmentGivenHSCC,
      'treatmentGivenHLL': treatmentGivenHLL,
      'treatmentGivenTotal': treatmentGivenTotal,
      'ipdRegisteredHSCC': ipdRegisteredHSCC,
      'ipdRegisteredHLL': ipdRegisteredHLL,
      'ipdRegisteredTotal': ipdRegisteredTotal,
      'dischargePatientHSCC': dischargePatientHSCC,
      'dischargePatientHLL': dischargePatientHLL,
      'dischargePatientTotal': dischargePatientTotal,
      'prescriptionGivenHSCC': prescriptionGivenHSCC,
      'prescriptionGivenHLL': prescriptionGivenHLL,
      'prescriptionGivenTotal': prescriptionGivenTotal,
      'prescriptionIssuedHSCC': prescriptionIssuedHSCC,
      'prescriptionIssuedHLL': prescriptionIssuedHLL,
      'prescriptionIssuedTotal': prescriptionIssuedTotal,
    };
  }
}
