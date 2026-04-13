// ignore_for_file: file_names

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../constants/constants.dart';
import '../constants/fonts.dart';
import '../utilities/SizeConfig.dart';

PreferredSize? mAppBar({
  String? scTitle,
  String? leadingIcon,
  double? elevation,
  bool? showActions = false,
  Function()? onLeadingIconClick,
  List<Widget>? actions,
}) {
  return PreferredSize(
    preferredSize: const Size.fromHeight(60.0),
    child: AppBar(
      centerTitle: false,
      titleSpacing: 0,

      // REMOVE backgroundColor & use gradient instead
      backgroundColor: Colors.transparent,
      flexibleSpace: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.bottomRight,
            end: Alignment.topLeft,
            colors: [
              kFirstAppBarcolor.withValues(alpha: 0.4),
              kFirstAppBarcolor,
            ],
          ),
        ),
      ),

      leading:
          leadingIcon != null
              ? GestureDetector(
                onTap: onLeadingIconClick,
                child: Container(
                  color: Colors.transparent,
                  child: Center(
                    child: Image.asset(
                      leadingIcon,
                      color: kWhiteColor,
                      width: getProportionateScreenWidth(26),
                      fit: BoxFit.fill,
                    ),
                  ),
                ),
              )
              : Container(),

      title:
          scTitle != null
              ? Text(
                maxLines: 2,
                scTitle,
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: 18.sp,
                  fontWeight: FontWeight.w500,
                  color: kWhiteColor,
                ),
              )
              : null,

      iconTheme: IconThemeData(color: kWhiteColor),
      elevation: elevation ?? 0,
      actions: showActions! ? actions : null,

      systemOverlayStyle: const SystemUiOverlayStyle(
        statusBarColor: Colors.transparent,
        statusBarIconBrightness: Brightness.light,
      ),
    ),
  );
}

// PreferredSize? mAppBar({
//   String? scTitle,
//   String? leadingIcon,
//   double? elevation,
//   bool? showActions = false,
//   Function()? onLeadingIconClick,
//   List<Widget>? actions,
// }) {
//   return PreferredSize(
//     preferredSize: const Size.fromHeight(50.0),
//     child: AppBar(
//       centerTitle: false,
//       titleSpacing: 0,
//       // leadingWidth: 26,
//       leading:
//           leadingIcon != null
//               ? GestureDetector(
//                 onTap: onLeadingIconClick,
//                 child: Container(
//                   color: Colors.transparent,
//                   child: Center(
//                     child: Image.asset(
//                       leadingIcon,
//                       color: kWhiteColor,
//                       width: getProportionateScreenWidth(26),
//                       fit: BoxFit.fill,
//                     ),
//                   ),
//                 ),
//               )
//               : Container(),
//       title:
//           scTitle != null
//               ? Padding(
//                 padding: const EdgeInsets.fromLTRB(0, 0, 0, 0),
//                 child: Row(
//                   children: [
//                     Text(
//                       scTitle,
//                       style: TextStyle(
//                         fontFamily: FontConstants.interFonts,
//                         fontSize: responsiveFont(18),
//                         fontWeight: FontWeight.w500,
//                         color: kWhiteColor,
//                       ),
//                     ),
//                   ],
//                 ),
//               )
//               : null,
//       backgroundColor: kPrimaryColor,
//       iconTheme: IconThemeData(color: kBlackColor),
//       elevation: elevation ?? 0,
//       actions: showActions! ? actions : null,
//       systemOverlayStyle: SystemUiOverlayStyle(
//         statusBarColor: kPrimaryColor,
//         statusBarBrightness: Brightness.dark,
//         statusBarIconBrightness: Brightness.light,
//       ),
//     ),
//   );
// }
