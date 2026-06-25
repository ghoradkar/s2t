class Is24By7IsAccountCreatedResponse {
  String? status;
  String? message;
  List<Is24By7IsAccountCreatedOutput>? output;
  String? data;
  Is24By7IsAccountCreatedResponse({
    this.status,
    this.data,
    this.message,
    this.output,
  });

  Is24By7IsAccountCreatedResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    data = json['data'];
    if (json['output'] != null) {
      output = <Is24By7IsAccountCreatedOutput>[];
      json['output'].forEach((v) {
        output!.add(new Is24By7IsAccountCreatedOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['status'] = this.status;
    data['message'] = this.message;
    data['data'] = this.data;
    if (this.output != null) {
      data['output'] = this.output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class Is24By7IsAccountCreatedOutput {
  int? is24By7IsAccountCreated;

  Is24By7IsAccountCreatedOutput({this.is24By7IsAccountCreated});

  Is24By7IsAccountCreatedOutput.fromJson(Map<String, dynamic> json) {
    is24By7IsAccountCreated = json['is24By7IsAccountCreated'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['is24By7IsAccountCreated'] = this.is24By7IsAccountCreated;
    return data;
  }
}
