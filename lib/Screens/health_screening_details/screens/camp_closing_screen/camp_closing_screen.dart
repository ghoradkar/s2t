// ignore_for_file: file_names, must_be_immutable, unrelated_type_equality_checks
import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:get/get.dart';
import '../../../../../Modules/constants/fonts.dart';
import '../../../../Modules/constants/constants.dart';
import '../../../../Modules/constants/images.dart';
import '../../../../Modules/utilities/SizeConfig.dart';
import '../../../../Modules/widgets/AppButtonWithIcon.dart';
import '../../../../Modules/widgets/AppIconTextfield.dart';
import '../../../../Modules/widgets/S2TAppBar.dart';
import '../../../../Views/CampClosingColorInfoView/CampClosingColorInfoView.dart';
import '../../../../Views/CampClosingScreeningDetailsView/CampClosingScreeningDetailsView.dart';
import '../../../../Views/CampClosingSummaryView/CampClosingSummaryView.dart';
import '../../../../Views/ConsumableConsumptionForCampView/ConsumableConsumptionForCampView.dart';
import '../../controllers/camp_closing_controller.dart';

class CampClosingScreen extends StatefulWidget {
  final int campID;
  final String campDate;
  final int dISTLGDCODE;

  const CampClosingScreen({
    super.key,
    required this.campID,
    required this.campDate,
    required this.dISTLGDCODE,
  });

  @override
  State<CampClosingScreen> createState() => _CampClosingScreenState();
}

class _CampClosingScreenState extends State<CampClosingScreen> {
  late CampClosingController controller;

  @override
  void initState() {
    super.initState();
    controller = Get.put(CampClosingController());
    controller.loadData(
      campId: widget.campID,
      distLgdCode: widget.dISTLGDCODE,
      campDate: widget.campDate,
    );
  }

  @override
  void dispose() {
    Get.delete<CampClosingController>();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: mAppBar(
        scTitle: 'Camp Closing',
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () => Navigator.pop(context),
        showActions: true,
        actions: [
          Padding(
            padding: const EdgeInsets.fromLTRB(0, 0, 10, 0),
            child: GestureDetector(
              onTap: () => _showBottomPopup(context),
              child: const Icon(Icons.info, color: Colors.white, size: 24.0),
            ),
          ),
        ],
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: SizedBox(
          height: SizeConfig.screenHeight,
          width: SizeConfig.screenWidth,
          child: Stack(
            children: [
              Positioned(
                top: 0,
                bottom: 8,
                left: 8,
                right: 8,
                child: SingleChildScrollView(
                  child: Obx(
                    () => Column(
                      children: [
                        CampClosingScreeningDetailsView(
                          facilitedWorkers:
                              controller.facilitedWorkers.value,
                          approvedBeneficiaries:
                              controller.approvedBeneficiaries.value,
                          rejectedBeneficiaries:
                              controller.rejectedBeneficiaries.value,
                          verifiedBeneficiaries:
                              controller.verifiedBeneficiaries.value,
                          basicDetails: controller.basicDetails.value,
                          physicalExamination:
                              controller.physicalExamination.value,
                          lungFunctioinTest:
                              controller.lungFunctioinTest.value,
                          audioScreeningTest:
                              controller.audioScreeningTest.value,
                          visionScreening: controller.visionScreening.value,
                          sampleCollection:
                              controller.sampleCollection.value,
                          ackowledgement: controller.ackowledgement.value,
                          totalPhysicalExam:
                              controller.totalPhysicalExam.value,
                          totalLungTest: controller.totalLungTest.value,
                          totalAudioTest: controller.totalAudioTest.value,
                          totalVisionTest: controller.totalVisionTest.value,
                          totalUrineCount: controller.totalUrineCount.value,
                          totalBene: controller.totalBene.value,
                        ),
                        CampClosingSummaryView(
                          totalApprovedBeneTextField:
                              controller.totalApprovedBeneTextField,
                          sampleCollectionTextField:
                              controller.sampleCollectionTextField,
                          rejectedBeneficiaryTextField:
                              controller.rejectedBeneficiaryTextField,
                        ),
                        ConsumableConsumptionForCampView(
                          consumableCampList:
                              controller.consumableCampList.toList(),
                        ),
                        if (controller.isShowRemark.value) ...[
                          SizedBox(height: responsiveHeight(20)),
                          AppIconTextfield(
                            icon: icScreeningTests,
                            titleHeaderString: 'Remark',
                            controller: controller.remarkTextField,
                          ),
                        ],
                        SizedBox(height: responsiveHeight(26)),
                        Center(
                          child: SizedBox(
                            width: 146,
                            height: 40,
                            child: AppButtonWithIcon(
                              buttonColor:
                                  controller.isUserInteractionEnabled.value
                                      ? kButtonColor
                                      : Colors.grey,
                              title: 'Close Camp',
                              icon: Image.asset(
                                iconArrow,
                                height: responsiveHeight(24),
                                width: responsiveHeight(24),
                              ),
                              mWidth: SizeConfig.screenWidth,
                              textStyle: TextStyle(
                                fontFamily: FontConstants.interFonts,
                                color: Colors.white,
                                fontSize: responsiveFont(16),
                              ),
                              onTap: () {
                                if (controller.isUserInteractionEnabled.value) {
                                  controller.validate();
                                }
                              },
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void _showBottomPopup(BuildContext parentContext) {
    showModalBottomSheet(
      context: parentContext,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return GestureDetector(
          onTap: () => Navigator.of(sheetContext).pop(),
          child: Container(
            width: double.infinity,
            height: MediaQuery.of(sheetContext).size.height,
            color: Colors.transparent,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Padding(
                  padding: const EdgeInsets.fromLTRB(30, 0, 30, 0),
                  child: CampClosingColorInfoView(),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}