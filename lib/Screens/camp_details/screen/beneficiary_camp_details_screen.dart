// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Screens/camp_details/model/team_details_list_response.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/widgets/AppDropdownTextfield.dart';
import 'package:s2toperational/Modules/widgets/CommonSkeletonList.dart';
import 'package:s2toperational/Modules/widgets/DropDownListScreen/DropDownListScreen.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/no_data_widget.dart';
import '../controller/beneficiary_camp_details_controller.dart';
import '../widget/beneficiary_camp_row.dart';

class BeneficiaryCampDetailsScreen extends StatelessWidget {
  const BeneficiaryCampDetailsScreen({
    super.key,
    required this.campId,
    required this.cAMPTYPE,
    required this.campTypeDescription,
  });

  final int campId;
  final int? cAMPTYPE;
  final String? campTypeDescription;

  @override
  Widget build(BuildContext context) {
    Get.put(BeneficiaryCampDetailsController(
      campId: campId,
      cAMPTYPE: cAMPTYPE,
      campTypeDescription: campTypeDescription,
    ));
    return GetBuilder<BeneficiaryCampDetailsController>(
      builder: (ctrl) => NetworkWrapper(
        child: Expanded(
          child: Column(
            children: [
              SizedBox(height: 6.h),
              ctrl.isShowTeamDropDown
                  ? AppDropdownTextfield(
                    icon: icTeamIconn,
                    titleHeaderString: 'Team',
                    valueString: ctrl.teamName,
                    isDisabled: false,
                    onTap: () => _openTeamSheet(context, ctrl),
                  )
                  : const SizedBox.shrink(),
              ctrl.isShowTeamDropDown ? SizedBox(height: 10.h) : const SizedBox.shrink(),
              Expanded(
                child: ctrl.isLoading
                    ? const CommonSkeletonPatientList()
                    : ctrl.beneficiaryWorkerList.isNotEmpty
                    ? ListView.builder(
                      itemCount: ctrl.beneficiaryWorkerList.length,
                      itemBuilder: (context, index) {
                        final obj = ctrl.beneficiaryWorkerList[index];
                        return BeneficiaryCampRow(
                          index: index,
                          obj: obj,
                          onRefresh: ctrl.fetchBeneficiaryList,
                        );
                      },
                    )
                    : NoDataFound(),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future<void> _openTeamSheet(BuildContext context, BeneficiaryCampDetailsController ctrl) async {
    final teams = await ctrl.fetchCampWiseTeams();
    if (teams.isEmpty || !context.mounted) return;
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (_) => Container(
        width: double.infinity,
        height: MediaQuery.of(context).size.width * 1.33,
        decoration: const BoxDecoration(
          color: Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: Radius.circular(20),
            topRight: Radius.circular(20),
          ),
        ),
        child: DropDownListScreen(
          titleString: 'Select Team',
          dropDownList: teams,
          dropDownMenu: DropDownTypeMenu.CampDetailsTeam,
          onApplyTap: (p0) => ctrl.selectTeam(p0 as TeamDetailsOutput),
        ),
      ),
    ).whenComplete(() => ctrl.update());
  }
}
