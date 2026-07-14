import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/constants/constants.dart';
import 'package:s2toperational/constants/fonts.dart';
import 'package:s2toperational/utilities/size_config.dart';
import 'package:s2toperational/common_widgets/CommonText.dart';
import 'package:s2toperational/acknowledgement/model/acknowledgement_patient_list_response.dart';

class AcknowledgementPatientRow extends StatelessWidget {
  final AcknowledgementPatientOutput patient;
  final VoidCallback onTap;

  const AcknowledgementPatientRow({
    super.key,
    required this.patient,
    required this.onTap,
  });

  String get _genderLabel {
    switch (patient.gender?.toUpperCase()) {
      case 'M':
        return 'Male';
      case 'F':
        return 'Female';
      default:
        return 'Other';
    }
  }

  bool get _isSigned => (patient.isSignature ?? 0) == 1;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(10),
        child: Container(
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(10),
            border: Border.all(color: borderDashboardColor),
            boxShadow: [
              BoxShadow(
                color: Colors.black.withValues(alpha: 0.1),
                blurRadius: 8,
              ),
            ],
          ),
          padding: const EdgeInsets.all(12),
          child: Row(
            children: [
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    CommonText(
                      text: patient.englishName ?? '-',
                      fontSize: 14.sp,
                      fontWeight: FontWeight.w600,
                      textColor: kBlackColor,
                      textAlign: TextAlign.start,
                    ),
                    const SizedBox(height: 4),
                    Row(
                      children: [
                        _Tag(label: 'Reg No: ${patient.regdNo ?? '-'}'),
                        const SizedBox(width: 8),
                        _Tag(label: 'Age: ${patient.age ?? '-'}'),
                        const SizedBox(width: 8),
                        _Tag(label: _genderLabel),
                      ],
                    ),
                    const SizedBox(height: 4),
                    Row(
                      children: [
                        _Tag(
                          label:
                              'Reg ID: ${patient.regdId ?? '-'}',
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              const SizedBox(width: 8),
              Column(
                children: [
                  Container(
                    padding: const EdgeInsets.symmetric(
                      horizontal: 10,
                      vertical: 4,
                    ),
                    decoration: BoxDecoration(
                      color: _isSigned
                          ? Colors.green.withValues(alpha: 0.15)
                          : Colors.orange.withValues(alpha: 0.15),
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: CommonText(
                      text: _isSigned ? 'Signed' : 'Pending',
                      fontSize: 11.sp,
                      fontWeight: FontWeight.w600,
                      textColor:
                          _isSigned ? Colors.green : Colors.orange,
                      textAlign: TextAlign.center,
                    ),
                  ),
                  const SizedBox(height: 6),
                  Icon(
                    Icons.arrow_forward_ios,
                    size: responsiveHeight(16),
                    color: kPrimaryColor,
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class _Tag extends StatelessWidget {
  final String label;
  const _Tag({required this.label});

  @override
  Widget build(BuildContext context) {
    return Text(
      label,
      style: TextStyle(
        color: dropDownTitleHeader,
        fontFamily: FontConstants.interFonts,
        fontWeight: FontWeight.w400,
        fontSize: responsiveFont(11),
      ),
    );
  }
}