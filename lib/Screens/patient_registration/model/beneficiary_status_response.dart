// ignore_for_file: file_names

class BeneficiaryStatusResponse {
  String? status;
  String? message;
  String? messageId;
  List<BeneficiaryStatusOutput>? output;

  BeneficiaryStatusResponse({
    this.status,
    this.message,
    this.messageId,
    this.output,
  });

  BeneficiaryStatusResponse.fromJson(Map<String, dynamic> json) {
    status = json['status']?.toString();
    message = json['message']?.toString();
    messageId = json['messageId']?.toString();
    if (json['output'] is List) {
      output = (json['output'] as List)
          .map((e) => BeneficiaryStatusOutput.fromJson(e as Map<String, dynamic>))
          .toList();
    }
  }
}

class BeneficiaryStatusOutput {
  String? isConfirmed;

  BeneficiaryStatusOutput({this.isConfirmed});

  BeneficiaryStatusOutput.fromJson(Map<String, dynamic> json) {
    isConfirmed = json['IsConfirmed']?.toString() ?? json['isConfirmed']?.toString();
  }
}
