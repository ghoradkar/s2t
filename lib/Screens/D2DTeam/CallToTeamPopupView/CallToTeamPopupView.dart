// ignore_for_file: must_be_immutable

import 'package:flutter/material.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../../Modules/Json_Class/D2DTeamMemberDetailsResponse/D2DTeamMemberDetailsResponse.dart';
import 'CallToTeamRow/CallToTeamRow.dart';

class CallToTeamPopupView extends StatelessWidget {
  CallToTeamPopupView({super.key, required this.callingList});

  List<D2DTeamMemberDetailsOutput> callingList = [];
  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.white,
      child: Column(
        children: [
          Container(
            height: 60,
            alignment: Alignment.center,
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Expanded(
                  child: Text(
                    "Call To Team",
                    style: TextStyle(
                      color: Colors.black,
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(8, 0, 8, 0),
                  child: GestureDetector(
                    onTap: () {
                      Navigator.pop(context);
                    },
                    child: Container(
                      width: 60,
                      height: 30,
                      decoration: BoxDecoration(
                        color: kPrimaryColor,
                        borderRadius: BorderRadius.circular(5),
                      ),
                      child: Center(
                        child: Text(
                          "Close",
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.all(12.0),
              child: ListView.builder(
                itemCount: 1,
                itemBuilder: (context, index) {
                  D2DTeamMemberDetailsOutput item = callingList[index];
                  return CallToTeamRow(
                    item: item,
                    onCallingTap: () {
                      launchUrl(Uri.parse('tel:${item.mOBNO ?? ""}'));
                    },
                  );
                },
              ),
            ),
          ),
        ],
      ),
    );
  }
}
