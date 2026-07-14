// ignore_for_file: file_names

class BeneficiaryDetailsResponse {
  String? status;
  String? message;
  BeneficiaryOutput? output;

  BeneficiaryDetailsResponse({this.status, this.message, this.output});

  BeneficiaryDetailsResponse.fromJson(Map<String, dynamic> json) {
    status = json['status']?.toString();
    message = json['message']?.toString();
    if (json['output'] != null) {
      output = BeneficiaryOutput.fromJson(json['output']);
    }
  }
}

class BeneficiaryOutput {
  String? name;
  String? mobileNo;
  String? aadhaarNo;
  String? dob;
  String? age;
  String? gender;
  String? address;
  String? registrationDate;
  String? patientPhotoUrl;
  String? healthCardPhotoUrl;
  String? renewalPhotoUrl;
  String? isHCRenewal;
  String? expiryDate;
  String? pinCode;
  String? title;

  BeneficiaryOutput({
    this.name,
    this.mobileNo,
    this.aadhaarNo,
    this.dob,
    this.age,
    this.gender,
    this.address,
    this.registrationDate,
    this.patientPhotoUrl,
    this.healthCardPhotoUrl,
    this.renewalPhotoUrl,
    this.isHCRenewal,
    this.expiryDate,
    this.pinCode,
    this.title,
  });

  BeneficiaryOutput.fromJson(Map<String, dynamic> json) {
    name = json['name']?.toString() ?? json['Name']?.toString();
    mobileNo = json['mobileNo']?.toString() ?? json['MobileNo']?.toString();
    aadhaarNo =
        json['aadhaarNo']?.toString() ?? json['AadhaarNo']?.toString();
    dob = json['dob']?.toString() ?? json['DOB']?.toString();
    age = json['age']?.toString() ?? json['Age']?.toString();
    gender = json['gender']?.toString() ?? json['Gender']?.toString();
    address = json['address']?.toString() ?? json['Address']?.toString();
    registrationDate =
        json['registrationDate']?.toString() ??
        json['RegistrationDate']?.toString();
    patientPhotoUrl =
        json['patientPhotoUrl']?.toString() ?? json['PatientPhotoUrl']?.toString();
    healthCardPhotoUrl =
        json['healthCardPhotoUrl']?.toString() ??
        json['HealthCardPhotoUrl']?.toString();
    renewalPhotoUrl =
        json['renewalPhotoUrl']?.toString() ??
        json['RenewalPhotoUrl']?.toString();
    isHCRenewal =
        json['isHCRenewal']?.toString() ?? json['IsHCRenewal']?.toString();
    expiryDate =
        json['expiryDate']?.toString() ?? json['ExpiryDate']?.toString();
    pinCode = json['pinCode']?.toString() ?? json['PinCode']?.toString();
    title = json['title']?.toString() ?? json['Title']?.toString();
  }
}
