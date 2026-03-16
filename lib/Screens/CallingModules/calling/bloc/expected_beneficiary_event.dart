// ignore_for_file: must_be_immutable

part of 'expected_beneficiary_bloc.dart';

sealed class ExpectedBeneficiaryEvent extends Equatable {
  const ExpectedBeneficiaryEvent();

  @override
  List<Object> get props => [];
}

class BeneficiaryRequest extends ExpectedBeneficiaryEvent {
  Map<String, dynamic> payload;

  BeneficiaryRequest({required this.payload});

  @override
  List<Object> get props => [payload];
}

class BeneficiaryRequestForDateType extends ExpectedBeneficiaryEvent {
  Map<String, dynamic> payload;

  BeneficiaryRequestForDateType({required this.payload});

  @override
  List<Object> get props => [payload];
}

class GetCallStatusRequest extends ExpectedBeneficiaryEvent {}

class ResetExpectedBeneficiaryState extends ExpectedBeneficiaryEvent {}

class TeamStatusRequest extends ExpectedBeneficiaryEvent {
  Map<String, dynamic> payload;

  TeamStatusRequest({required this.payload});

  @override
  List<Object> get props => [payload];
}

class GetRealationData extends ExpectedBeneficiaryEvent {
  Map<String, dynamic> payload;

  GetRealationData({required this.payload});

  @override
  List<Object> get props => [payload];
}

class GetAppointmentCount extends ExpectedBeneficiaryEvent {
  Map<String, dynamic> payload;

  GetAppointmentCount({required this.payload});

  @override
  // TODO: implement props
  List<Object> get props => [payload];
}

class GetRemark extends ExpectedBeneficiaryEvent {
  Map<String, dynamic> payload;

  GetRemark({required this.payload});

  @override
  // TODO: implement props
  List<Object> get props => [payload];
}

class GetAddressDetails extends ExpectedBeneficiaryEvent {
  Map<String, dynamic> payload;

  GetAddressDetails({required this.payload});

  @override
  // TODO: implement props
  List<Object> get props => [payload];
}

class GetDependentDetails extends ExpectedBeneficiaryEvent {
  Map<String, dynamic> payload;

  GetDependentDetails({required this.payload});

  @override
  // TODO: implement props
  List<Object> get props => [payload];
}

class GetScreenedDependentDetails extends ExpectedBeneficiaryEvent {
  Map<String, dynamic> payload;

  GetScreenedDependentDetails({required this.payload});

  @override
  // TODO: implement props
  List<Object> get props => [payload];
}

// class GetOrganization extends ExpectedBeneficiaryEvent {
//   Map<String, dynamic> payload;

//   GetOrganization({required this.payload});

//   @override
//   // TODO: implement props
//   List<Object> get props => [payload];
// }

// class InsertCallLog extends ExpectedBeneficiaryEvent {
//   Map<String, dynamic> payload;

//   InsertCallLog({required this.payload});

//   @override
//   // TODO: implement props
//   List<Object> get props => [payload];
// }

// class InsertCallLogNew extends ExpectedBeneficiaryEvent {
//   Map<String, dynamic> payload;

//   InsertCallLogNew({required this.payload});

//   @override
//   // TODO: implement props
//   List<Object> get props => [payload];
// }
// class GetAPIKeyForAgentId extends ExpectedBeneficiaryEvent {
//   Map<String, dynamic> payload;

//   GetAPIKeyForAgentId({required this.payload});

//   @override
//   // TODO: implement props
//   List<Object> get props => [payload];
// }
// class GetAPIKeyForMyoperator extends ExpectedBeneficiaryEvent {
//   Map<String, dynamic> payload;

//   GetAPIKeyForMyoperator({required this.payload});

//   @override
//   // TODO: implement props
//   List<Object> get props => [payload];
// }
// class GetAPIKeyForCallPAtching extends ExpectedBeneficiaryEvent {
//   Map<String, dynamic> payload;

//   GetAPIKeyForCallPAtching({required this.payload});

//   @override
//   // TODO: implement props
//   List<Object> get props => [payload];
// }

// class TwentyFourBySevenWithAgentID extends ExpectedBeneficiaryEvent {
//   Map<String, dynamic> payload;

//   TwentyFourBySevenWithAgentID({required this.payload});

//   @override
//   // TODO: implement props
//   List<Object> get props => [payload];
// }

// class MyOperator extends ExpectedBeneficiaryEvent {
//   final Map<String, dynamic> payload;
//   final String apiKey;

//   const MyOperator({
//     required this.payload,
//     required this.apiKey,
//   });

//   @override
//   List<Object> get props => [payload, apiKey];
// }

// class TwentyFourBySevenWithCallPatching extends ExpectedBeneficiaryEvent {
//   Map<String, dynamic> payload;

//   TwentyFourBySevenWithCallPatching({required this.payload});

//   @override
//   // TODO: implement props
//   List<Object> get props => [payload];
// }

// class GetUserCreatedBy extends ExpectedBeneficiaryEvent {
//   Map<String, dynamic> payload;

//   GetUserCreatedBy({required this.payload});

//   @override
//   // TODO: implement props
//   List<Object> get props => [payload];
// }

class GetCallStatusForAppointment extends ExpectedBeneficiaryEvent {
  Map<String, dynamic> payload;

  GetCallStatusForAppointment({required this.payload});

  @override
  // TODO: implement props
  List<Object> get props => [payload];
}

class AddDependentRequest extends ExpectedBeneficiaryEvent {
  List<AddDependentOutput> addDependentModel;

  AddDependentRequest({required this.addDependentModel});

  @override
  List<Object> get props => [addDependentModel];
}

class DeleteDependentRequest extends ExpectedBeneficiaryEvent {
  int dependentIndex;

  DeleteDependentRequest({required this.dependentIndex});

  @override
  List<Object> get props => [dependentIndex];
}

class InsertAppointmentDetails extends ExpectedBeneficiaryEvent {
  Map<String, dynamic> payload;

  InsertAppointmentDetails({required this.payload});

  @override
  // TODO: implement props
  List<Object> get props => [payload];
}

// class InsertDependentRequest extends ExpectedBeneficiaryEvent {
//   final int AssignCallID;
//   final int CallStatusID;
//   final String RegisteredAddress;
//   final String CurrentAddress;
//   final int IsCurrentSameAsRegd;
//   final String RegMobileNo;
//   final String AltMobileNo;
//   final String Pincode;
//   final String Landmark;
//   final String WorkersGender;
//   final int WorkersMaritalStatus;
//   final int NoOfDependants;
//   final int DependantScreeningPending;
//   final String AppoinmentDate;
//   final String AppoinmentTime;
//   final String Remark;
//   final int DISTLGDCODE;
//   final int TALLGDCODE;
//   final String HouseNo;
//   final String Road;
//   final String Area;
//   final String DependantDetails;
//   final String CReatedBy;
//   final String WorkerScreeningStatus;
//   final int RemarkID;

//   InsertDependentRequest({
//     required this.AssignCallID,
//     required this.CallStatusID,
//     required this.RegisteredAddress,
//     required this.CurrentAddress,
//     required this.IsCurrentSameAsRegd,
//     required this.RegMobileNo,
//     required this.AltMobileNo,
//     required this.Pincode,
//     required this.Landmark,
//     required this.WorkersGender,
//     required this.WorkersMaritalStatus,
//     required this.NoOfDependants,
//     required this.DependantScreeningPending,
//     required this.AppoinmentDate,
//     required this.AppoinmentTime,
//     required this.Remark,
//     required this.DISTLGDCODE,
//     required this.TALLGDCODE,
//     required this.HouseNo,
//     required this.Road,
//     required this.Area,
//     required this.DependantDetails,
//     required this.CReatedBy,
//     required this.WorkerScreeningStatus,
//     required this.RemarkID,
//   });

//   @override
//   List<Object> get props => [AssignCallID, CallStatusID, RegisteredAddress,
//   CurrentAddress,IsCurrentSameAsRegd,RegMobileNo,AltMobileNo,Pincode,
//   Landmark,WorkersGender,WorkersMaritalStatus,NoOfDependants,AppoinmentDate,
//   AppoinmentTime,Remark,DISTLGDCODE,TALLGDCODE,HouseNo,Road,Area,DependantDetails,CReatedBy,WorkerScreeningStatus,RemarkID];
// }
