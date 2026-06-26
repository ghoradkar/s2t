// ignore_for_file: file_names

import 'package:get/get.dart';

class CampDetailsController extends GetxController {
  CampDetailsController({
    required this.campId,
    required this.dISTLGDCODE,
    required this.campDate,
    required this.surveyCoordinatorName,
    required this.dISTNAME,
    required this.mOBNO,
    required this.isHealthScreeing,
    this.cAMPTYPE,
    this.campTypeDescription,
  });

  final int campId;
  final int dISTLGDCODE;
  final String campDate;
  final String surveyCoordinatorName;
  final String dISTNAME;
  final String mOBNO;
  final bool isHealthScreeing;
  final int? cAMPTYPE;
  final String? campTypeDescription;

  int selectedIndex = 0;

  void changeTab(int index) {
    selectedIndex = index;
    update();
  }
}
