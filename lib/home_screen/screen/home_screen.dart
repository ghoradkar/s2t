import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/constants/images.dart';
import 'package:s2toperational/common_widgets/CommonSkeletonList.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/admin_dashboard/screen/admin_dashboard_widget.dart';
import 'package:s2toperational/calling_modules/widgets/no_internet_widget.dart';
import 'package:s2toperational/super_admin/screens/super_admin_dashboard.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/common_widgets/S2TAppBar.dart';
import 'package:s2toperational/home_screen/widget/dashboard_menu_options.dart';
import '../widget/side_drawer_menu.dart';
import '../controller/home_screen_controller.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    Get.put(HomeScreenController());
    return GetBuilder<HomeScreenController>(
      builder: (c) => Scaffold(
        key: c.scaffoldKey,
        appBar: mAppBar(
          scTitle: 'Dashboard',
          leadingIcon: iconDrawer,
          onLeadingIconClick: () => c.scaffoldKey.currentState?.openDrawer(),
        ),
        drawer: SideDrawerMenu(appVersion: c.appVersion),
        body: c.isOnline == null && c.dESGID != 166
            ? const Center(child: CircularProgressIndicator())
            : (c.isOnline ?? true)
                ? _OnlineBody(c: c)
                : NoInternetWidget(onRetryPressed: c.retry),
      ),
    );
  }
}

class _OnlineBody extends StatelessWidget {
  const _OnlineBody({required this.c});
  final HomeScreenController c;

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Column(
        children: [
          _RadioCampToggle(c: c),
          _AdminDash(c: c),
          if (c.dESGID == 166 && c.superAdminController != null)
            SuperAdminDashboard(controller: c.superAdminController!),
          Padding(
            padding: EdgeInsets.only(left: 8.w, right: 8.w, top: 14.h),
            child: (c.dESGID == 51 ||
                    c.dESGID == 166 ||
                    c.dESGID == 171 ||
                    c.dESGID == 26)
                ? GridView.builder(
                    gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 3,
                      crossAxisSpacing: 14,
                      mainAxisSpacing: 20,
                      childAspectRatio: 0.91,
                    ),
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    itemCount: c.menuList.length,
                    itemBuilder: (context, index) {
                      final menu = c.menuList[index];
                      return GestureDetector(
                        onTap: () => c.pushToNextScreen(menu),
                        child: AdminDashboardMenuOptions(
                          dashboardMenu: menu,
                          desigId: c.dESGID,
                        ),
                      );
                    },
                  ).paddingOnly(left: 6.w, right: 6.w)
                : GridView.builder(
                    gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 3,
                      crossAxisSpacing: 14,
                      mainAxisSpacing: 12,
                      childAspectRatio: 0.86,
                    ),
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    itemCount: c.menuList.length,
                    itemBuilder: (context, index) {
                      final menu = c.menuList[index];
                      return GestureDetector(
                        onTap: () => c.pushToNextScreen(menu),
                        child: DashboardMenuOptions(dashboardMenu: menu),
                      );
                    },
                  ),
          ),
        ],
      ),
    );
  }
}

class _RadioCampToggle extends StatelessWidget {
  const _RadioCampToggle({required this.c});
  final HomeScreenController c;

  @override
  Widget build(BuildContext context) {
    if (!c.isShowRadioCamp) return const SizedBox.shrink();
    return Padding(
      padding: EdgeInsets.fromLTRB(12.w, 12.h, 12.w, 12.h),
      child: Container(
        width: MediaQuery.of(context).size.width,
        height: 46.h,
        decoration: BoxDecoration(
          color: Colors.transparent,
          border: Border.all(color: Colors.white, width: 1),
          borderRadius: BorderRadius.circular(20),
        ),
        child: Row(
          children: [
            Expanded(
              child: GestureDetector(
                onTap: c.switchToRegularCamp,
                child: Container(
                  decoration: BoxDecoration(
                    color: c.regularCamp ? kPrimaryColor : kWhiteColor,
                    borderRadius: const BorderRadius.only(
                      topLeft: Radius.circular(20),
                      bottomLeft: Radius.circular(20),
                    ),
                    border: Border.all(color: Colors.grey.withValues(alpha: 0.3)),
                  ),
                  child: Center(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        SizedBox(
                          width: 20.w,
                          height: 20.h,
                          child: Image.asset(
                            icnTent,
                            color: c.regularCamp ? Colors.white : Colors.black,
                          ),
                        ),
                        SizedBox(width: 6.w),
                        CommonText(
                          text: 'Regular Camp',
                          fontSize: 13.sp,
                          fontWeight: FontWeight.w500,
                          textColor: c.regularCamp ? Colors.white : Colors.black,
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
            Expanded(
              child: GestureDetector(
                onTap: c.switchToDoorToDoorCamp,
                child: Container(
                  decoration: BoxDecoration(
                    color: c.doorToDoorCamp ? kPrimaryColor : kWhiteColor,
                    borderRadius: const BorderRadius.only(
                      topRight: Radius.circular(20),
                      bottomRight: Radius.circular(20),
                    ),
                    border: Border.all(color: Colors.grey.withValues(alpha: 0.3)),
                  ),
                  child: Center(
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        SizedBox(
                          width: 20,
                          height: 20,
                          child: Image.asset(
                            'assets/icons/home-2.png',
                            color:
                                c.doorToDoorCamp ? Colors.white : Colors.black,
                          ),
                        ),
                        SizedBox(width: 6.w),
                        CommonText(
                          text: 'Door to Door Camp',
                          fontSize: 13.sp,
                          fontWeight: FontWeight.w500,
                          textColor:
                              c.doorToDoorCamp ? Colors.white : Colors.black,
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _AdminDash extends StatelessWidget {
  const _AdminDash({required this.c});
  final HomeScreenController c;

  @override
  Widget build(BuildContext context) {
    if (c.dESGID != 51) return const SizedBox.shrink();

    if (c.isLoadingAdminData) return const CommonSkeletonAdminDashboard();

    if (c.conductedTotals == null || c.todaysTotals == null) {
      return SizedBox(
        height: 200.h,
        child: Center(
          child: Text(
            'No dashboard data available',
            style: TextStyle(fontSize: 14.sp, color: Colors.grey),
          ),
        ),
      );
    }

    return AdminDashboardWidget(
      conductedTotals: c.conductedTotals!,
      todaysTotals: c.todaysTotals!,
    );
  }
}
