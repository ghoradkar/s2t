// ignore_for_file: use_full_hex_values_for_flutter_colors

import 'package:flutter/material.dart';
import 'package:formz/formz.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Screens/calling_modules/controllers/add_dependent_controller.dart';
import 'package:s2toperational/Screens/calling_modules/custom_widgets/network_wrapper.dart';
import 'package:s2toperational/Screens/calling_modules/models/add_dependent_model.dart';

import '../../../Modules/constants/constants.dart';
import '../../../Modules/constants/images.dart';
import '../../../Modules/utilities/SizeConfig.dart';
import '../../../Modules/widgets/AppButtonWithIcon.dart';
import '../../../Modules/widgets/AppTextField.dart';
import '../../../Modules/widgets/S2TAppBar.dart';
import '../custom_widgets/no_data_widget.dart';

class AddDependentScreen extends GetView<AddDependentController> {
  const AddDependentScreen({super.key});

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);

    return NetworkWrapper(
      child: Scaffold(
        appBar: mAppBar(
          scTitle: 'Add Dependent',
          leadingIcon: iconBackArrow,
          onLeadingIconClick: () => Navigator.pop(context),
        ),
        body: GetBuilder<AddDependentController>(
          builder: (c) =>
              c.selectedBeneficiary != null
                  ? Form(
                    key: c.formKey,
                    child: Padding(
                      padding: EdgeInsets.all(responsiveHeight(15)),
                      child: SingleChildScrollView(
                        child: Column(
                          children: [
                            SizedBox(height: responsiveHeight(10)),
                            Container(
                              decoration: BoxDecoration(
                                boxShadow: const [
                                  BoxShadow(
                                    offset: Offset(0, 1),
                                    color: Color(0xff00000026),
                                    spreadRadius: 0,
                                    blurRadius: 10,
                                  ),
                                ],
                                color: const Color(0XFFFFFFFF),
                                borderRadius: BorderRadius.circular(10),
                              ),
                              child: Padding(
                                padding: EdgeInsets.all(responsiveHeight(10)),
                                child: Column(
                                  children: [
                                    SizedBox(height: responsiveHeight(6)),
                                    // ── Relation field ───────────────────────
                                    AppTextField(
                                      onTap: () {
                                        // API: fetch allowed relation options based on
                                        // marital status and gender.
                                        c.svc.fetchRelationData({
                                          "MaritalStatusID":
                                              c.marriedStatusID.toString(),
                                          "Gender": c.workerGender,
                                        });
                                      },
                                      controller: c.relationController,
                                      readOnly: true,
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'Relation with Worker',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: responsiveFont(14),
                                            fontFamily: FontConstants.interFonts,
                                          ),
                                          children: <TextSpan>[
                                            TextSpan(
                                              text: ' *',
                                              style: TextStyle(
                                                color: Colors.red,
                                                fontSize: responsiveFont(14),
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                      labelStyle: TextStyle(
                                        fontSize: responsiveFont(12),
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                      prefixIcon: Image.asset(
                                        icUserIcon,
                                        scale: 3.5,
                                      ),
                                      suffixIcon: Obx(
                                        () =>
                                            c.svc.getRealtionStatus.value
                                                    .isInProgress
                                                ? SizedBox(
                                                  height: responsiveHeight(20),
                                                  width: responsiveHeight(20),
                                                  child: const Center(
                                                    child:
                                                        CircularProgressIndicator(),
                                                  ),
                                                )
                                                : SizedBox(
                                                  height: responsiveHeight(24),
                                                  width: responsiveHeight(24),
                                                  child: Center(
                                                    child: Image.asset(
                                                      downArrow,
                                                      height: responsiveHeight(
                                                        24,
                                                      ),
                                                      width:
                                                          responsiveHeight(24),
                                                    ),
                                                  ),
                                                ),
                                      ),
                                    ),
                                    SizedBox(height: responsiveHeight(25)),

                                    // ── Age field ────────────────────────────
                                    AppTextField(
                                      textInputType: TextInputType.number,
                                      maxLength: 2,
                                      onTap: () {
                                        if (c.selectedReltion == null) {
                                          ToastManager.showAlertDialog(
                                            context,
                                            "Please select relation",
                                            () {
                                              c.svc.resetState();
                                              Navigator.of(context).pop();
                                            },
                                          );

                                          return;
                                        }
                                      },
                                      controller: c.ageController,
                                      validators: c.validateInput,
                                      errorText:
                                          c.isSubmitted
                                              ? c.validateInput(
                                                c.ageController.text,
                                              )
                                              : null,
                                      onChange: (value) {
                                        c.update();
                                      },
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'Age',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: responsiveFont(14),
                                            fontFamily: FontConstants.interFonts,
                                          ),
                                          children: <TextSpan>[
                                            TextSpan(
                                              text: ' *',
                                              style: TextStyle(
                                                color: Colors.red,
                                                fontSize: responsiveFont(14),
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                      labelStyle: TextStyle(
                                        fontSize: responsiveFont(12),
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                      prefixIcon: Image.asset(
                                        calendar,
                                        scale: 3.5,
                                      ),
                                    ),

                                    SizedBox(height: responsiveHeight(25)),

                                    // ── First Name ───────────────────────────
                                    AppTextField(
                                      controller: c.firstNameController,
                                      textInputType: TextInputType.text,
                                      inputFormatters: [UpperCaseTextFormatter()],
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'First Name',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: responsiveFont(14),
                                            fontFamily: FontConstants.interFonts,
                                          ),
                                          children: <TextSpan>[
                                            TextSpan(
                                              text: ' *',
                                              style: TextStyle(
                                                color: Colors.red,
                                                fontSize: responsiveFont(14),
                                              ),
                                            ),
                                          ],
                                        ),
                                      ),
                                      labelStyle: TextStyle(
                                        fontSize: responsiveFont(12),
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                      prefixIcon: Image.asset(
                                        icUserIcon,
                                        scale: 3.5,
                                      ),
                                    ),
                                    SizedBox(height: responsiveHeight(25)),

                                    // ── Middle Name ──────────────────────────
                                    AppTextField(
                                      controller: c.middleNameController,
                                      textInputType: TextInputType.text,
                                      inputFormatters: [UpperCaseTextFormatter()],
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'Middle Name',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: responsiveFont(14),
                                            fontFamily: FontConstants.interFonts,
                                          ),
                                        ),
                                      ),
                                      labelStyle: TextStyle(
                                        fontSize: responsiveFont(12),
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                      prefixIcon: Image.asset(
                                        icUserIcon,
                                        scale: 3.5,
                                      ),
                                    ),
                                    SizedBox(height: responsiveHeight(25)),

                                    // ── Last Name ────────────────────────────
                                    AppTextField(
                                      readOnly:
                                          (c.relationController.text
                                                          .toLowerCase() ==
                                                      "father in law" ||
                                                  c.relationController.text
                                                          .toLowerCase() ==
                                                      "mother in law")
                                              ? false
                                              : true,
                                      controller: c.lastNameController,
                                      textInputType: TextInputType.text,
                                      inputFormatters: [UpperCaseTextFormatter()],
                                      label: RichText(
                                        text: TextSpan(
                                          text: 'Last Name',
                                          style: TextStyle(
                                            color: kLabelTextColor,
                                            fontSize: responsiveFont(14),
                                            fontFamily: FontConstants.interFonts,
                                          ),
                                        ),
                                      ),
                                      labelStyle: TextStyle(
                                        fontSize: responsiveFont(12),
                                        fontFamily: FontConstants.interFonts,
                                      ),
                                      prefixIcon: Image.asset(
                                        icUserIcon,
                                        scale: 3.5,
                                      ),
                                    ),
                                    SizedBox(height: responsiveHeight(25)),

                                    Row(
                                      children: [
                                        Image.asset(
                                          icSquarePulsAlert,
                                          scale: 3.5,
                                          color: Colors.red,
                                        ),
                                        const SizedBox(width: 8),
                                        Text(
                                          'Please Use English Keyboard Only',
                                          style: TextStyle(
                                            color: Colors.red,
                                            fontSize: responsiveFont(12),
                                            fontFamily: FontConstants.interFonts,
                                          ),
                                        ),
                                      ],
                                    ),
                                    SizedBox(height: responsiveHeight(34)),
                                  ],
                                ),
                              ),
                            ),
                            SizedBox(height: responsiveHeight(60)),

                            // ── Save button ──────────────────────────────────
                            Obx(
                              () => Visibility(
                                visible:
                                    c.svc.addDependentOutput.length <
                                    c.numberOfDependentPending,
                                child: AppButtonWithIcon(
                                  title: "Save",
                                  mHeight: responsiveHeight(60),
                                  mWidth: responsiveWidth(140),
                                  onTap: () {
                                    c.isSubmitted = true;
                                    c.update();

                                    if (c.selectedReltion == null) {
                                      ToastManager.showAlertDialog(
                                        context,
                                        "Please select relation",
                                        () {
                                          Navigator.of(context).pop();
                                        },
                                      );

                                      return;
                                    }
                                    if (c.firstNameController.text == "") {
                                      ToastManager.showAlertDialog(
                                        context,
                                        "Please enter first name",
                                        () {
                                          Navigator.of(context).pop();
                                        },
                                      );

                                      return;
                                    }

                                    if (c.lastNameController.text == "") {
                                      ToastManager.showAlertDialog(
                                        context,
                                        "Please enter last name",
                                        () {
                                          Navigator.of(context).pop();
                                        },
                                      );

                                      return;
                                    }

                                    if (!c.formKey.currentState!.validate()) {
                                      return;
                                    } else {
                                      List<AddDependentOutput>
                                      addDependentOutput = [];
                                      addDependentOutput.addAll(
                                        c.svc.addDependentOutput,
                                      );
                                      addDependentOutput.add(
                                        AddDependentOutput(
                                          age: c.ageController.text,
                                          relId: c.selectedReltion?.relId,
                                          firstName:
                                              c.firstNameController.text,
                                          middleName:
                                              c.middleNameController.text,
                                          lastName: c.lastNameController.text,
                                          lastDependantScreeningDate: "",
                                          assignCallID: c.assignCallID,
                                        ),
                                      );

                                      c.isSaving = true;
                                      c.svc.addDependent(addDependentOutput);

                                      c.isSubmitted = false;
                                      c.update();

                                      c.svc.resetState();
                                    }
                                  },
                                  textStyle: TextStyle(
                                    color: Colors.white,
                                    fontSize: responsiveFont(14),
                                    fontWeight: FontWeight.w600,
                                    fontFamily: FontConstants.interFonts,
                                  ),
                                  buttonColor: kPrimaryColor,
                                  icon: Image.asset(
                                    iconArrow,
                                    height: responsiveHeight(24),
                                    width: responsiveHeight(24),
                                    color: kWhiteColor,
                                  ),
                                ),
                              ),
                            ),

                            SizedBox(height: responsiveHeight(44)),
                          ],
                        ),
                      ),
                    ),
                  )
                  : const NoDataFound(),
        ),
      ),
    );
  }
}
