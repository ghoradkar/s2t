// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/size_config.dart';
import 'package:s2toperational/Modules/common_widgets/S2TAppBar.dart';

class FullScreenImageScreen extends StatefulWidget {
  FullScreenImageScreen({super.key, required this.imagePath});

  String imagePath = "";
  String titleName = "";

  @override
  State<FullScreenImageScreen> createState() => _FullScreenImageScreenState();
}

class _FullScreenImageScreenState extends State<FullScreenImageScreen> {
  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return KeyboardDismissOnTap(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: widget.titleName,
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () {
            Navigator.pop(context);
          },
        ),
        body: AnnotatedRegion(
          value: const SystemUiOverlayStyle(
            statusBarColor: kPrimaryColor,
            statusBarBrightness: Brightness.light,
            statusBarIconBrightness: Brightness.light,
          ),

          child: Container(
            color: Colors.white,
            height: SizeConfig.screenHeight,
            width: SizeConfig.screenWidth,
            child: Image.network(
              widget.imagePath,
              fit: BoxFit.cover,
              errorBuilder: (context, error, stackTrace) {
                return Image.asset(
                  icPhotoPlaceholder,
                ); // your local placeholder
              },
            ),
          ).paddingSymmetric(vertical: 10, horizontal: 10),
        ),
      ),
    );
  }
}

//   @override
//   Widget build(BuildContext context) {
//     return Image.network(
//       widget.imagePath,
//       fit: BoxFit.cover,
//       errorBuilder: (context, error, stackTrace) {
//         return Image.asset(icPhotoPlaceholder); // your local placeholder
//       },
//     );
//   }
// }
