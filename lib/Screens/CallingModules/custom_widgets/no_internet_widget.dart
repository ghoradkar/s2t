import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppButtonWithIcon.dart';
import '../calling/bloc/expected_beneficiary_bloc.dart';

class NoInternetWidget extends StatelessWidget {
  final Future Function()? onRetryPressed;

  const NoInternetWidget({super.key, this.onRetryPressed});

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      height: SizeConfig.screenHeight,
      width: SizeConfig.screenWidth,
      child: Stack(
        children: [
          Positioned(
            top: 80,
            child: Image.asset(
              fit: BoxFit.fill,
              rect4,
              width: SizeConfig.screenWidth,
              height: responsiveHeight(300.37),
            ),
          ),
          Positioned(
            top: 60,
            child: Image.asset(
              fit: BoxFit.fill,
              rect3,
              width: SizeConfig.screenWidth,
              height: responsiveHeight(300.37),
            ),
          ),
          Positioned(
            top: 40,
            child: Image.asset(
              fit: BoxFit.fill,
              rect2,
              width: SizeConfig.screenWidth,
              height: responsiveHeight(300.37),
            ),
          ),
          Image.asset(
            fit: BoxFit.fill,
            rect1,
            width: SizeConfig.screenWidth,
            height: responsiveHeight(300.37),
          ),
          Positioned(
            top: 20,
            left: 105,
            right: 105,
            child: Stack(
              children: [
                Image.asset(
                  icCircle,
                  height: responsiveHeight(219),
                  width: responsiveHeight(219),
                ),
                Positioned(
                  top: 0,
                  bottom: 0,
                  left: 0,
                  right: 0,
                  child: Center(
                    child: Image.asset(
                      iconNoInternet,
                      height: responsiveHeight(100),
                      width: responsiveHeight(100),
                    ),
                  ),
                ),
              ],
            ),
          ),
          Positioned(
            bottom: 123,
            left: 61,
            right: 61,
            child: Column(
              children: [
                Text(
                  "Oh No!",
                  style: TextStyle(
                    fontWeight: FontWeight.w700,
                    fontSize: responsiveFont(24),
                  ),
                ),
                SizedBox(height: responsiveHeight(25)),
                Text(
                  "No Internet found. Check your connection or try again.",
                  style: TextStyle(fontSize: responsiveFont(18)),
                  textAlign: TextAlign.center,
                ),
                SizedBox(height: responsiveHeight(50)),
                AppButtonWithIcon(
                  onTap: () async {
                    // 1) trigger app-specific retry if you want (kept from your code)
                    //    this is optional; remove if not needed
                    // ignore: use_build_context_synchronously
                    context.read<ExpectedBeneficiaryBloc>().add(
                      BeneficiaryRequest(
                        payload: {
                          "CallStatusID": "0",
                          "TeamID": "0",
                          "GroupID": "1",
                        },
                      ),
                    );

                    // 2) call back to parent to re-check connectivity and refresh
                    if (onRetryPressed != null) {
                      await onRetryPressed!();
                    }
                  },
                  title: "Retry",
                  mWidth: responsiveWidth(175),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
