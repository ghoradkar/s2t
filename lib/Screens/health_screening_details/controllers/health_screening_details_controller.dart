import 'package:get/get.dart';
import 'package:s2toperational/Modules/Enums/Enums.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';

class HealthScreeningDetailsController extends GetxController {
  final RxList<HealthScreeningDetailsMenu> menuList =
      <HealthScreeningDetailsMenu>[].obs;

  int dESGID = 0;

  @override
  void onInit() {
    super.onInit();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    _buildMenu();
  }

  void _buildMenu() {
    menuList.clear();

    if (dESGID == 34 || dESGID == 147 || dESGID == 130) {
      if (DataProvider().getRegularCamp()) {
        menuList.add(HealthScreeningDetailsMenu.PhysicalExamination);
      }
    }

    if (dESGID == 139 ||
        dESGID == 136 ||
        dESGID == 92 ||
        dESGID == 29 ||
        dESGID == 108) {
      menuList.add(HealthScreeningDetailsMenu.ScreeningStatus);
      menuList.add(HealthScreeningDetailsMenu.CampClosing);
    } else if (dESGID == 141 || dESGID == 34) {
      menuList.add(HealthScreeningDetailsMenu.ScreeningStatus);
    } else if (dESGID == 92 ||
        dESGID == 104 ||
        dESGID == 108 ||
        dESGID == 29 ||
        dESGID == 35 ||
        dESGID == 137 ||
        dESGID == 139 ||
        dESGID == 138 ||
        dESGID == 141 ||
        dESGID == 136 ||
        dESGID == 169 ||
        dESGID == 146 ||
        dESGID == 177) {
      menuList.add(HealthScreeningDetailsMenu.SampleCollection);
      menuList.add(HealthScreeningDetailsMenu.BasicHealthInfo);

      if (!DataProvider().getRegularCamp()) {
        menuList.add(HealthScreeningDetailsMenu.D2DPhysicalExamination);
      }

      menuList.add(HealthScreeningDetailsMenu.LungFunctionTest);
      menuList.add(HealthScreeningDetailsMenu.AudioScreeningTest);
      menuList.add(HealthScreeningDetailsMenu.VisualScreeningTest);
      menuList.add(HealthScreeningDetailsMenu.ScreeningStatus);
    }
  }
}