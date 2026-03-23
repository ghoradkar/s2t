import 'dart:io';

import 'package:android_intent_plus/android_intent.dart';
import 'package:android_intent_plus/flag.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/model/TeamWisePhysicalExamDetailsResponse.dart';
import 'package:s2toperational/Screens/d2d_physical_examination/repository/call_to_team_repository.dart';
import 'package:url_launcher/url_launcher.dart';

class CallToTeamController extends GetxController {
  final _repository = CallToTeamRepository();

  final physiCalList = <TeamWisePhysicalExamDetailsOutput>[].obs;
  final isLoading = false.obs;

  Future<void> fetchTeamDetails({
    required int campId,
    required int doctorId,
    required String teamId,
  }) async {
    isLoading(true);
    ToastManager.showLoader();
    try {
      final response = await _repository.fetchTeamDetails(
        campId: campId,
        doctorId: doctorId,
        teamId: teamId,
      );
      physiCalList.assignAll(response?.output ?? []);
    } catch (e) {
      ToastManager.toast(e.toString());
    } finally {
      isLoading(false);
      ToastManager.hideLoader();
      update();
    }
  }

  Future<void> launchPhoneDialer(String phoneNumber) async {
    try {
      if (phoneNumber.isEmpty) throw Exception('Phone number is empty');

      final cleanNumber = phoneNumber.replaceAll(RegExp(r'[^\d+]'), '');
      if (cleanNumber.isEmpty) throw Exception('Invalid phone number');

      if (Platform.isAndroid) {
        final intent = AndroidIntent(
          action: 'android.intent.action.DIAL',
          data: 'tel:$cleanNumber',
          flags: <int>[
            Flag.FLAG_ACTIVITY_NEW_TASK,
            Flag.FLAG_ACTIVITY_CLEAR_TOP,
            Flag.FLAG_ACTIVITY_SINGLE_TOP,
          ],
        );
        await intent.launch();
      } else {
        final telUri = Uri.parse('tel:$cleanNumber');
        if (!await canLaunchUrl(telUri)) {
          throw Exception('No dialer app available');
        }
        if (!await launchUrl(telUri, mode: LaunchMode.externalApplication)) {
          throw Exception('Failed to launch dialer');
        }
      }
    } catch (e) {
      ToastManager.toast("Cannot open dialer: ${e.toString()}");
    }
  }
}