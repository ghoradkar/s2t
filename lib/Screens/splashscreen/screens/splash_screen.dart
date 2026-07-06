import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/common_widgets/CommonText.dart';

import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/size_config.dart';
import '../controller/splash_controller.dart';

class SplashScreen extends GetView<SplashController> {
  const SplashScreen({super.key});

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      body: AnnotatedRegion<SystemUiOverlayStyle>(
        value: SystemUiOverlayStyle.light,
        child: TweenAnimationBuilder<double>(
          tween: Tween(begin: 0.0, end: 1.0),
          duration: const Duration(milliseconds: 1600),
          builder: (context, progress, _) {
            final logoOpacity = _lerp(0.0, 0.4, progress);
            final logoScale = 0.6 + 0.4 * _lerp(0.0, 0.55, progress, curve: Curves.elasticOut);
            final titleOpacity = _lerp(0.35, 0.70, progress);
            final titleSlide = 1.0 - _lerp(0.35, 0.70, progress);
            final versionOpacity = _lerp(0.65, 1.0, progress);

            return Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Spacer(),
                  // Logo — scale up + fade in
                  Opacity(
                    opacity: logoOpacity.clamp(0.0, 1.0),
                    child: Transform.scale(
                      scale: logoScale.clamp(0.6, 1.0),
                      child: Image.asset(
                        splashScreenLogoNew,
                        height: SizeConfig.screenHeight * 0.2,
                      ),
                    ),
                  ),
                  const SizedBox(height: 16),
                  // App name — slide up + fade in
                  Opacity(
                    opacity: titleOpacity.clamp(0.0, 1.0),
                    child: Transform.translate(
                      offset: Offset(0, 24 * titleSlide),
                      child: CommonText(
                        text: "S2T Executive",
                        fontSize: 18.sp,
                        fontWeight: FontWeight.w600,
                        textColor: kBlackColor,
                        textAlign: TextAlign.center,
                      ),
                    ),
                  ),
                  const Spacer(),
                  // Version — fade in last
                  Opacity(
                    opacity: versionOpacity.clamp(0.0, 1.0),
                    child: Obx(
                      () => Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          CommonText(
                            text: "Version : ",
                            fontSize: 16.sp,
                            fontWeight: FontWeight.normal,
                            textColor: kBlackColor,
                            textAlign: TextAlign.center,
                          ),
                          CommonText(
                            text: controller.appVersion.value,
                            fontSize: 16.sp,
                            fontWeight: FontWeight.normal,
                            textColor: kBlackColor,
                            textAlign: TextAlign.center,
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }

  /// Maps [progress] (0→1) through the sub-range [start→end], optionally with a curve.
  double _lerp(double start, double end, double progress, {Curve curve = Curves.easeOut}) {
    final t = ((progress - start) / (end - start)).clamp(0.0, 1.0);
    return curve.transform(t);
  }
}
