class DishaLoginResponse {
  String? token;

  DishaLoginResponse.fromJson(Map<String, dynamic> json) {
    token = json['response']?.toString();
  }
}

class DishaTestsResponse {
  String? customerId;
  String? customerCode;
  String? customerName;
  List<DishaTestDetail> tests;

  DishaTestsResponse.fromJson(Map<String, dynamic> json)
      : customerId = json['customerId']?.toString(),
        customerCode = json['customerCode']?.toString(),
        customerName = json['customerName']?.toString(),
        tests = (json['listTestDetailsDto'] as List<dynamic>? ?? [])
            .map((e) => DishaTestDetail.fromJson(e as Map<String, dynamic>))
            .toList();
}

class DishaTestDetail {
  int subServiceId;
  String subServiceName;
  int sampleTypeId;
  String templateWise;

  DishaTestDetail.fromJson(Map<String, dynamic> json)
      : subServiceId = (json['subServiceId'] ?? 0) as int,
        subServiceName = (json['subServiceName'] ?? '') as String,
        sampleTypeId = (json['sampleTypeId'] ?? 1) as int,
        templateWise = (json['templateWise'] ?? 'N') as String;
}
