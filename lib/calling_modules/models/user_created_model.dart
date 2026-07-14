class UserCreatedModel {
  String? status;
  String? message;
  List<UserCreatedOutput>? output;

  UserCreatedModel({this.status, this.message, this.output});

  UserCreatedModel.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <UserCreatedOutput>[];
      json['output'].forEach((v) {
        output!.add(UserCreatedOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class UserCreatedOutput {
  int? is24By7IsAccountCreated;

  UserCreatedOutput({this.is24By7IsAccountCreated});

  UserCreatedOutput.fromJson(Map<String, dynamic> json) {
    is24By7IsAccountCreated = json['Is24By7IsAccountCreated'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['Is24By7IsAccountCreated'] = is24By7IsAccountCreated;
    return data;
  }
}
