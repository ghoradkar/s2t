// ignore_for_file: file_names

class TotalcampAndTotalZeroCampResponse {
  String? status;
  String? message;
  List<TotalCampAndTotalZeroCampOutput>? output;

  TotalcampAndTotalZeroCampResponse({this.status, this.message, this.output});

  TotalcampAndTotalZeroCampResponse.fromJson(Map<String, dynamic> json) {
    status = json["status"];
    message = json["message"];
    if (json["output"] != null) {
      output = <TotalCampAndTotalZeroCampOutput>[];
      json["output"].forEach((v) {
        output!.add(TotalCampAndTotalZeroCampOutput.fromJson(v));
      });
    }
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data["status"] = status;
    data["message"] = message;
    if (output != null) {
      data["output"] = output!.map((v) => v.toJson()).toList();
    }
    return data;
  }
}

class TotalCampAndTotalZeroCampOutput {
  int? totalCamps;
  int? totalMonthSBeneficiaryCount;
  int? todaySBeneficiaryCount;
  int? zeroCountCamp;
  String? todaySDate;

  TotalCampAndTotalZeroCampOutput({
    this.totalCamps,
    this.totalMonthSBeneficiaryCount,
    this.todaySBeneficiaryCount,
    this.zeroCountCamp,
    this.todaySDate,
  });

  TotalCampAndTotalZeroCampOutput.fromJson(Map<String, dynamic> json) {
    totalCamps = json["Total Camps"];
    totalMonthSBeneficiaryCount = json["Total Month's Beneficiary Count"];
    todaySBeneficiaryCount = json["Today's Beneficiary Count"];
    zeroCountCamp = json["Zero Count Camp"];
    todaySDate = json["Today's Date"];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data["Total Camps"] = totalCamps;
    data["Total Month's Beneficiary Count"] = totalMonthSBeneficiaryCount;
    data["Today's Beneficiary Count"] = todaySBeneficiaryCount;
    data["Zero Count Camp"] = zeroCountCamp;
    data["Today's Date"] = todaySDate;
    return data;
  }
}
