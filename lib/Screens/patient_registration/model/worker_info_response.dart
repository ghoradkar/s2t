// ignore_for_file: file_names

class WorkerInfoResponse {
  String? status;
  String? message;
  List<WorkerInfoOutput>? output;

  WorkerInfoResponse({this.status, this.message, this.output});

  WorkerInfoResponse.fromJson(Map<String, dynamic> json) {
    status = json['status']?.toString();
    message = json['message']?.toString();
    if (json['output'] != null && json['output'] is List) {
      output = (json['output'] as List)
          .map((e) => WorkerInfoOutput.fromJson(e as Map<String, dynamic>))
          .toList();
    }
  }
}

class WorkerInfoOutput {
  String? firstNamePersonal;
  String? middleNamePersonal;
  String? lastNamePersonal;
  String? mobile;
  String? aadhaar;
  String? age;
  String? gender;
  String? maritalStatusID;
  String? maritalStatus;
  String? nextRenewalDate;

  // Residential address
  String? residentialHouseNo;
  String? residentialArea;
  String? residentialPostOffice;
  String? residentialTaluka;
  String? residentialDistrict;
  String? residentialState;
  String? residentialPincode;

  // Permanent address
  String? permanentHouseNo;
  String? permanentArea;
  String? permanentPostOffice;
  String? permanentTaluka;
  String? permanentDistrict;
  String? permanentState;
  String? permanentPincode;

  String? talLgdCode;
  String? distLgdCode;

  WorkerInfoOutput.fromJson(Map<String, dynamic> json) {
    firstNamePersonal = _nullSafe(json['firstNamePersonal']);
    middleNamePersonal = _nullSafe(json['middleNamePersonal']);
    lastNamePersonal = _nullSafe(json['lastNamePersonal']);
    mobile = _nullSafe(json['mobile']);
    aadhaar = _nullSafe(json['aadhaar']);
    age = json['age']?.toString();
    gender = _nullSafe(json['gender']);
    maritalStatusID = _nullSafe(json['MaritalStatusID']);
    maritalStatus = _nullSafe(json['MARITALSTATUS']);
    nextRenewalDate = _nullSafe(json['next_renewal_date']);

    residentialHouseNo = _nullSafe(json['residential_address_houseNo']);
    residentialArea = _nullSafe(json['residential_address_area']);
    residentialPostOffice = _nullSafe(json['residential_address_postOffice']);
    residentialTaluka = _nullSafe(json['residential_address_taluka']);
    residentialDistrict = _nullSafe(json['residential_address_district']);
    residentialState = _nullSafe(json['residential_address_state']);
    residentialPincode = _nullSafe(json['residential_address_pincode']);

    permanentHouseNo = _nullSafe(json['permanent_address_houseNo']);
    permanentArea = _nullSafe(json['permanent_address_area']);
    permanentPostOffice = _nullSafe(json['permanent_address_postOffice']);
    permanentTaluka = _nullSafe(json['permanent_address_taluka']);
    permanentDistrict = _nullSafe(json['permanent_address_district']);
    permanentState = _nullSafe(json['permanent_address_state']);
    permanentPincode = _nullSafe(json['permanent_address_pincode']);

    talLgdCode = _nullSafe(json['TALLGDCODE']);
    distLgdCode = _nullSafe(json['DISTLGDCODE']);
  }

  /// API returns the string "null" for missing values — treat it as empty
  static String? _nullSafe(dynamic v) {
    if (v == null) return null;
    final s = v.toString();
    return s == 'null' ? '' : s;
  }

  /// Full name assembled from parts
  String get fullName {
    return [
      firstNamePersonal ?? '',
      middleNamePersonal ?? '',
      lastNamePersonal ?? '',
    ].where((e) => e.trim().isNotEmpty).join(' ').trim();
  }

  /// Builds an address string matching native format:
  /// "House No.,{houseNo}{area},{postOffice},{taluka},{district},{state},{pincode}"
  ///
  /// Native always prepends "House No.," regardless of whether houseNo is empty
  /// (due to a Java reference-equality bug: `== "null"` is always false for
  /// new String objects, so the prefix is unconditionally added in the native app).
  /// Native also always includes every part separated by commas, even when empty,
  /// producing consecutive commas for missing fields (e.g. ",,,,").
  static String _buildAddress({
    required String houseNo,
    required String area,
    required String postOffice,
    required String taluka,
    required String district,
    required String state,
    required String pincode,
  }) {
    final buf = StringBuffer('House No.,$houseNo$area');
    for (final part in [postOffice, taluka, district, state, pincode]) {
      buf.write(',');
      buf.write(part);
    }
    return buf.toString();
  }

  /// Local / Current address (residential)
  String get localAddress => _buildAddress(
        houseNo: residentialHouseNo ?? '',
        area: residentialArea ?? '',
        postOffice: residentialPostOffice ?? '',
        taluka: residentialTaluka ?? '',
        district: residentialDistrict ?? '',
        state: residentialState ?? '',
        pincode: residentialPincode ?? '',
      );

  /// Permanent address
  String get permanentAddressFormatted => _buildAddress(
        houseNo: permanentHouseNo ?? '',
        area: permanentArea ?? '',
        postOffice: permanentPostOffice ?? '',
        taluka: permanentTaluka ?? '',
        district: permanentDistrict ?? '',
        state: permanentState ?? '',
        pincode: permanentPincode ?? '',
      );
}