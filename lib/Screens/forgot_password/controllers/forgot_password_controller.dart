import 'package:formz/formz.dart';
import 'package:get/get.dart';

import '../repository/forgot_password_repository.dart';

class ForgotPasswordController extends GetxController {
  final ForgotPasswordRepository repository;

  ForgotPasswordController({required this.repository});

  final checkAndGenerateOTPStatus = FormzSubmissionStatus.initial.obs;
  final checkAndGenerateOTPResponse = ''.obs;
  final checkGeneratedOTPStatus = FormzSubmissionStatus.initial.obs;
  final checkGeneratedOTPResponse = ''.obs;
  final forgotPasswordRequestStatus = FormzSubmissionStatus.initial.obs;
  final forgotPasswordRequestResponse = ''.obs;

  Future<void> checkAndGenerateOTP(Map<String, dynamic> payload) async {
    // TODO: implement when repository is ready
  }

  Future<void> checkGeneratedOTP(Map<String, dynamic> payload) async {
    // TODO: implement when repository is ready
  }

  Future<void> forgotPasswordRequest(Map<String, dynamic> payload) async {
    // TODO: implement when repository is ready
  }
}
