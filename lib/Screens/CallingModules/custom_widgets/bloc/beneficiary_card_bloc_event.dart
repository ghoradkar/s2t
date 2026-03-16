// ignore_for_file: must_be_immutable

part of 'beneficiary_card_bloc_bloc.dart';

sealed class BeneficiaryCardBlocEvent extends Equatable {
  const BeneficiaryCardBlocEvent();

  @override
  List<Object> get props => [];
}

class GetOrganization extends BeneficiaryCardBlocEvent {
  Map<String, dynamic> payload;

  GetOrganization({required this.payload});

  @override
  List<Object> get props => [payload];
}

class InsertCallLog extends BeneficiaryCardBlocEvent {
  Map<String, dynamic> payload;

  InsertCallLog({required this.payload});

  @override
  List<Object> get props => [payload];
}

class InsertCallLogNew extends BeneficiaryCardBlocEvent {
  Map<String, dynamic> payload;

  InsertCallLogNew({required this.payload});

  @override
  List<Object> get props => [payload];
}

class GetAPIKeyForAgentId extends BeneficiaryCardBlocEvent {
  Map<String, dynamic> payload;

  GetAPIKeyForAgentId({required this.payload});

  @override
  List<Object> get props => [payload];
}

class GetAPIKeyForMyoperator extends BeneficiaryCardBlocEvent {
  Map<String, dynamic> payload;

  GetAPIKeyForMyoperator({required this.payload});

  @override
  List<Object> get props => [payload];
}

class GetAPIKeyForCallPAtching extends BeneficiaryCardBlocEvent {
  Map<String, dynamic> payload;

  GetAPIKeyForCallPAtching({required this.payload});

  @override
  List<Object> get props => [payload];
}

class TwentyFourBySevenWithAgentID extends BeneficiaryCardBlocEvent {
  Map<String, dynamic> payload;

  TwentyFourBySevenWithAgentID({required this.payload});

  @override
  // TODO: implement props
  List<Object> get props => [payload];
}

class MyOperator extends BeneficiaryCardBlocEvent {
  final Map<String, dynamic> payload;
  final String apiKey;

  const MyOperator({required this.payload, required this.apiKey});

  @override
  List<Object> get props => [payload, apiKey];
}

class TwentyFourBySevenWithCallPatching extends BeneficiaryCardBlocEvent {
  Map<String, dynamic> payload;

  TwentyFourBySevenWithCallPatching({required this.payload});

  @override
  List<Object> get props => [payload];
}

class GetUserCreatedBy extends BeneficiaryCardBlocEvent {
  Map<String, dynamic> payload;

  GetUserCreatedBy({required this.payload});

  @override
  List<Object> get props => [payload];
}

class GetCallStatusForAppointment extends BeneficiaryCardBlocEvent {
  Map<String, dynamic> payload;

  GetCallStatusForAppointment({required this.payload});

  @override
  List<Object> get props => [payload];
}

// class AddDependentRequest extends BeneficiaryCardBlocEvent {
//   List<BeneficiaryCardBlocEvent> addDependentModel;
//   AddDependentRequest({required this.addDependentModel});

//   @override
//   List<Object> get props => [addDependentModel];
// }

class ResetBaneficiaryCardState extends BeneficiaryCardBlocEvent {}

// class DeleteDependentRequest extends ExpectedBeneficiaryEvent {
//   int dependentIndex;
//   DeleteDependentRequest({required this.dependentIndex});

//   @override
//   List<Object> get props => [dependentIndex];
// }
