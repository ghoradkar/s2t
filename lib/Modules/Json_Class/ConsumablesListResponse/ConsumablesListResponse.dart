class ConsumablesListResponse {
  String? status;
  String? message;
  List<ConsumablesOutput>? output;

  ConsumablesListResponse({this.status, this.message, this.output});

  ConsumablesListResponse.fromJson(Map<String, dynamic> json) {
    status = json['status'];
    message = json['message'];
    if (json['output'] != null) {
      output = <ConsumablesOutput>[];
      json['output'].forEach((v) {
        output!.add(ConsumablesOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['status'] = status;
    data['message'] = message;
    if (output != null) {
      data['output'] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class ConsumablesOutput {
  int? aVAILABELSTOCK;
  int? expectedQuantity;
  String? productName;
  int? productQuantity;
  String? productUnit;
  int? productid;

  ConsumablesOutput(
      {this.aVAILABELSTOCK,
      this.expectedQuantity,
      this.productName,
      this.productQuantity,
      this.productUnit,
      this.productid});

  ConsumablesOutput.fromJson(Map<String, dynamic> json) {
    aVAILABELSTOCK = json['AVAILABELSTOCK'];
    expectedQuantity = json['ExpectedQuantity'];
    productName = json['ProductName'];
    productQuantity = json['ProductQuantity'];
    productUnit = json['ProductUnit'];
    productid = json['Productid'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data =  Map<String, dynamic>();
    data['AVAILABELSTOCK'] = aVAILABELSTOCK;
    data['ExpectedQuantity'] = expectedQuantity;
    data['ProductName'] = productName;
    data['ProductQuantity'] = productQuantity;
    data['ProductUnit'] = productUnit;
    data['Productid'] = productid;
    return data;
  }
}
