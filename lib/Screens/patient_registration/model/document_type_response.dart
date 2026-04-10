// ignore_for_file: file_names

class DocumentTypeResponse {
  String? status;
  String? message;
  List<DocumentTypeOutput>? output;

  DocumentTypeResponse.fromJson(Map<String, dynamic> json) {
    status = json['status']?.toString();
    message = json['message']?.toString();
    if (json['output'] is List) {
      output = (json['output'] as List)
          .map((e) => DocumentTypeOutput.fromJson(e as Map<String, dynamic>))
          .toList();
    }
  }
}

class DocumentTypeOutput {
  int? docId;
  String? documentName;

  DocumentTypeOutput.fromJson(Map<String, dynamic> json) {
    docId = json['Doc_ID'] is int
        ? json['Doc_ID'] as int
        : int.tryParse(json['Doc_ID']?.toString() ?? '');
    documentName = json['Document_Name']?.toString();
  }
}