// ignore_for_file: use_build_context_synchronously, avoid_print
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/beneficiary_card_controller.dart';
import 'package:s2toperational/Screens/calling_modules/models/BeneficiaryResponseModel.dart';
import 'package:s2toperational/Screens/calling_modules/repository/beneficiary_card_repository.dart';
import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/DataProvider.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../routes/app_routes.dart';

class BeneficiaryCard extends StatefulWidget {
  final BeneficiaryOutput beneficiary;

  final int empCode;
  final int desId;
  final int agentId;
  final int mobileNo;
  final String? myOperatorUserId;
  final int index;
  final Future<void> Function()? onAppointmentSaved;

  BeneficiaryCard({
    super.key,
    required this.beneficiary,
    required this.index,
    this.onAppointmentSaved,
    int? empCode,
    int? desId,
    int? mobileNo,
    String? myOperatorUserId,
    int? agentId,
  }) : empCode =
           empCode ??
           DataProvider().getParsedUserData()?.output?[0].empCode ??
           0,
       desId =
           desId ?? DataProvider().getParsedUserData()?.output?[0].dESGID ?? 0,
       mobileNo =
           mobileNo ??
           int.tryParse(
             DataProvider()
                     .getParsedUserData()
                     ?.output?[0]
                     .bMobile
                     ?.toString() ??
                 '',
           ) ??
           0,
       myOperatorUserId =
           myOperatorUserId ??
           DataProvider().getParsedUserData()?.output?[0].myOperatorUserID ??
           "",
       agentId =
           agentId ??
           int.tryParse(
             DataProvider()
                     .getParsedUserData()
                     ?.output?[0]
                     .agentID
                     ?.toString() ??
                 '',
           ) ??
           0;

  @override
  State<BeneficiaryCard> createState() => _BeneficiaryCardState();
}

class _BeneficiaryCardState extends State<BeneficiaryCard> {
  late final BeneficiaryCardController _controller;
  late final String _controllerTag;

  static final _phoneButtonDecoration = BoxDecoration(
    color: kPrimaryColor.withValues(alpha: 0.85),
    borderRadius: BorderRadius.circular(50),
  );

  static final _eyeButtonDecoration = BoxDecoration(
    color: kPrimaryColor.withValues(alpha: 0.85),
    borderRadius: BorderRadius.circular(10),
  );

  @override
  void initState() {
    super.initState();
    _controllerTag = 'card_${widget.beneficiary.assignCallID ?? widget.index}';
    _controller = Get.put(
      BeneficiaryCardController(
        beneficiary: widget.beneficiary,
        empCode: widget.empCode,
        desId: widget.desId,
        agentId: widget.agentId,
        mobileNo: widget.mobileNo,
        myOperatorUserId: widget.myOperatorUserId,
        index: widget.index,
        repository: BeneficiaryCardRepository(),
      ),
      tag: _controllerTag,
    );
  }

  @override
  void dispose() {
    Get.delete<BeneficiaryCardController>(tag: _controllerTag, force: true);
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);

    return GetBuilder<BeneficiaryCardController>(
      tag: _controllerTag,
      builder:
          (c) => Padding(
            padding: EdgeInsets.only(
              top: responsiveHeight(16),
              bottom: responsiveHeight(4),
              left: responsiveWidth(15),
              right: responsiveWidth(15),
            ),
            child: InkWell(
              onTap: () async {
                final result = await Navigator.of(context).pushNamed(
                  AppRoutes.appointmentConfirmation,
                  arguments: widget.beneficiary,
                );
                if (result == true) {
                  await widget.onAppointmentSaved?.call();
                }
              },
              child: Container(
                decoration: BoxDecoration(
                  borderRadius: BorderRadius.circular(10),
                  color: getStatusColor(
                    widget.beneficiary.groupID ?? 0,
                  ).withValues(alpha: 0.1),
                ),
                child: Material(
                  color: Colors.transparent,
                  child: InkWell(
                    borderRadius: BorderRadius.circular(responsiveHeight(10)),

                    child: Ink(
                      child: Padding(
                        padding: EdgeInsets.symmetric(
                          vertical: 8.h,
                          horizontal: 8.w,
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              children: [
                                Text(
                                  "${(widget.index + 1).toString()}. ",
                                  style: TextStyle(
                                    fontSize: responsiveFont(14),
                                    fontWeight: FontWeight.w600,
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                ).paddingOnly(left: 4.w),
                                SizedBox(width: 2.w),
                                Expanded(
                                  child: Text(
                                    widget.beneficiary.beneficiaryName
                                            ?.toUpperCase() ??
                                        '',
                                    style: TextStyle(
                                      fontSize: responsiveFont(14),
                                      fontWeight: FontWeight.w600,
                                      fontFamily: FontConstants.interFonts,
                                    ),
                                  ),
                                ),

                                GestureDetector(
                                  behavior: HitTestBehavior.translucent,

                                  ///on call button tap
                                  onTap: () => c.handleCallTap(),
                                  child: Container(
                                    width: responsiveHeight(36),
                                    height: responsiveHeight(36),
                                    decoration: _phoneButtonDecoration,
                                    child: Center(
                                      child:
                                          c.isCallingLoading
                                              ? SizedBox(
                                                width: responsiveHeight(22),
                                                height: responsiveHeight(22),
                                                child:
                                                    const CircularProgressIndicator(
                                                      color: kWhiteColor,
                                                      strokeWidth: 2,
                                                    ),
                                              )
                                              : Icon(
                                                Icons.phone_outlined,
                                                color: kWhiteColor,
                                                size: 20,
                                              ),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                            SizedBox(height: responsiveHeight(2)),

                            infoRow(
                              icHashIcon,
                              "Beneficiary ID",
                              '${widget.beneficiary.beneficiaryNo ?? ''}',
                            ),
                            SizedBox(height: responsiveHeight(2)),
                            infoRow(
                              iconCalender,
                              "Renewal Date",
                              widget.beneficiary.nextRenewalDate ?? '',
                            ),
                            SizedBox(height: responsiveHeight(2)),
                            infoRow(
                              iconPerson,
                              "Worker Screened/Date",
                              '${widget.beneficiary.isWorkerScreened ?? ''}/${widget.beneficiary.lastScreeningDate ?? ''}',
                            ),
                            SizedBox(height: responsiveHeight(2)),
                            infoRow(
                              iconPersons,
                              "Dependent Screen Pending",
                              '${widget.beneficiary.dependantScreeningPending ?? ''}',
                            ),
                            SizedBox(height: responsiveHeight(2)),
                            infoRow(
                              iconMap,
                              "Area",
                              widget.beneficiary.area ?? '',
                            ),
                            SizedBox(height: responsiveHeight(2)),
                            (widget.beneficiary.groupID == 2 ||
                                    widget.beneficiary.groupID == 5)
                                ? Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    infoRow(
                                      iconMap,
                                      "Appoint.Date/Time",
                                      '${widget.beneficiary.appoinmentDate ?? ''}/${widget.beneficiary.appoinmentTime ?? ''}',
                                    ),
                                    SizedBox(height: responsiveHeight(12)),
                                  ],
                                )
                                : const SizedBox.shrink(),
                            infoRow(
                              iconDocument,
                              "Phlebo Remark",
                              widget.beneficiary.phleboRemark ?? '',
                            ),
                            SizedBox(height: responsiveHeight(2)),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Container(
                                  decoration: BoxDecoration(
                                    borderRadius: BorderRadius.circular(
                                      responsiveHeight(5),
                                    ),

                                    color: getStatusColor(
                                      widget.beneficiary.groupID ?? 0,
                                    ),

                                    // String statusName = beneficiary.assignStatus
                                  ),
                                  child: Padding(
                                    padding: const EdgeInsets.all(4.0),
                                    child: Text(
                                      widget.beneficiary.assignStatus
                                          .toString(),
                                      style: const TextStyle(
                                        color: kWhiteColor,
                                        fontSize: 11,
                                        fontWeight: FontWeight.w500,
                                      ),
                                    ),
                                  ),
                                ),

                                Container(
                                  width: responsiveHeight(36),
                                  height: responsiveHeight(36),
                                  decoration: _eyeButtonDecoration,
                                  child: Center(
                                    child: Icon(
                                      Icons.remove_red_eye_outlined,
                                      color: kWhiteColor,
                                      size: 20,
                                    ),
                                  ),
                                ),

                                // ),
                              ],
                            ),
                          ],
                        ),
                      ),
                    ),
                  ),
                ),
              ),
            ),
          ),
    );
  }

  Widget infoRow(String icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 0),
      child: Row(
        children: [
          Image.asset(
            icon,
            height: 18,
            width: 18,
            filterQuality: FilterQuality.low,
          ),
          const SizedBox(width: 10),
          Text(
            "$label : ",
            style: TextStyle(
              fontWeight: FontWeight.w500,
              fontFamily: FontConstants.interFonts,
              fontSize: 14.sp,
              color: kBlackColor,
            ),
          ),
          Expanded(
            child: Text(
              capitalizeEachWord(value),
              style: TextStyle(
                color: Colors.grey[600],
                fontWeight: FontWeight.w600,
                fontSize: 14.sp,
                fontFamily: FontConstants.interFonts,
              ),
            ),
          ),
        ],
      ),
    );
  }

  String capitalizeEachWord(String text) {
    return text
        .split(' ')
        .map(
          (word) =>
              word.isNotEmpty
                  ? word[0].toUpperCase() + word.substring(1).toLowerCase()
                  : '',
        )
        .join(' ');
  }
}

Color getStatusColor(int? groupID) {
  if (groupID == null) return Colors.grey; // Handle null values

  switch (groupID) {
    case 1:
      return const Color.fromRGBO(103, 103, 103, 1);
    case 2:
      return const Color.fromRGBO(77, 191, 129, 1);
    case 3:
      return const Color.fromRGBO(249, 133, 83, 1);
    case 4:
      return const Color.fromRGBO(26, 195, 207, 1);

    case 5:
      return const Color.fromRGBO(183, 102, 210, 1);

    case 7:
      return const Color.fromRGBO(249, 133, 83, 1);

    default:
      return Colors.grey; // Default color
  }
}

class Beneficiary {
  final String name;
  final String id;
  final String renewalDate;
  final String workerScreened;
  final String dependentPending;
  final String area;
  final String phleboRemark;

  Beneficiary({
    required this.name,
    required this.id,
    required this.renewalDate,
    required this.workerScreened,
    required this.dependentPending,
    required this.area,
    required this.phleboRemark,
  });
}
