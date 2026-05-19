// ignore_for_file: file_names

class DependentListResponse {
  String? status;
  String? message;
  List<DependentOutput>? output;

  DependentListResponse({this.status, this.message, this.output});

  DependentListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status']?.toString();
    message = json['message']?.toString();
    if (json['output'] != null && json['output'] is List) {
      output = (json['output'] as List)
          .map((e) => DependentOutput.fromJson(e as Map<String, dynamic>))
          .toList();
    }
  }
}

/// Mirrors GetDependentListModel.Output (GetDependentDetailsFromBoardData API)
class DependentOutput {
  /// Dependent's BOCW ID (Bocw_idDepend)
  String? bocwIdDepend;

  /// Board reg number (BOCWRegNO)
  String? bocwRegNo;

  /// Date of birth in "dd-MM-yyyy" format (DOB)
  String? dob;

  /// Relation name text, e.g. "Wife" (relation)
  String? relation;

  /// Family ID (family_id)
  String? familyId;

  /// Full name for display (full_name)
  String? fullName;

  /// Relation ID — drives gender lock (RelId)
  String? relId;

  /// Gram Panchayat LGD code (GPLGDCODE)
  String? gpLgdCode;

  /// Gram Panchayat name (GPNAME)
  String? gpName;

  /// 0 = Rural, 1 = Urban (IsUrban)
  String? isUrban;

  /// Gender string (Gender)
  String? gender;

  /// UID (UID)
  String? uid;

  DependentOutput.fromJson(Map<String, dynamic> json) {
    bocwIdDepend = _s(json['Bocw_idDepend']);
    bocwRegNo    = _s(json['BOCWRegNO']);
    dob          = _s(json['DOB']);
    relation     = _s(json['relation']);
    familyId     = _s(json['family_id']);
    fullName     = _s(json['full_name']);
    relId        = _s(json['RelId']);
    gpLgdCode    = _s(json['GPLGDCODE']);
    gpName       = _s(json['GPNAME']);
    isUrban      = _s(json['IsUrban']);
    gender       = _s(json['Gender']);
    uid          = _s(json['UID']);
  }

  static String? _s(dynamic v) {
    if (v == null) return null;
    final s = v.toString();
    return s == 'null' ? '' : s;
  }

  String get displayName => (fullName?.isNotEmpty == true) ? fullName! : '—';
}
