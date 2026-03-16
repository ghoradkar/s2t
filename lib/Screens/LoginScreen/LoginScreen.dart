import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import '../../../../../Modules/constants/fonts.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:s2toperational/Modules/Json_Class/LoginResponseModel/LoginResponseModel.dart';
import 'package:s2toperational/Screens/HomeScreen/HomeScreen.dart';
import 'package:s2toperational/Screens/VerifyOTPScreen/VerifyOTPScreen.dart';
import '../../Modules/FormatterManager/FormatterManager.dart';
import '../../Modules/ToastManager/ToastManager.dart';
import '../../Modules/constants/constants.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/utilities/validators.dart';
import '../../Modules/widgets/AppButtonWithIcon.dart';
import '../../Modules/widgets/AppTextField.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => LoginScreenState();
}

class LoginScreenState extends State<LoginScreen> {
  TextEditingController usernameController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  bool isRemberMe = false;
  final formKey = GlobalKey<FormState>();
  bool isObscure = true;
  bool isSubmitted = false;
  APIManager apiManager = APIManager();

  // final LoginController loginController = Get.put(LoginController());

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      resizeToAvoidBottomInset: true,
      body: SingleChildScrollView(
        child: Container(
          color: Colors.white,
          width: SizeConfig.screenWidth,
          child: Stack(
            children: [
              Positioned(
                top: 38,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect4,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(280.37),
                ),
              ),
              Positioned(
                top: 38,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect3,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(260.37),
                ),
              ),
              Positioned(
                top: 38,
                child: Image.asset(
                  fit: BoxFit.fill,
                  rect2,
                  width: SizeConfig.screenWidth,
                  height: responsiveHeight(240.37),
                ),
              ),
              Image.asset(
                fit: BoxFit.fill,
                rect1,
                width: SizeConfig.screenWidth,
                height: responsiveHeight(270.37),
              ),
              Padding(
                padding: EdgeInsets.symmetric(horizontal: 24.h, vertical: 84),
                child: Form(
                  key: formKey,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        "Welcome!",
                        style: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: responsiveFont(24),
                          color: kWhiteColor,
                          fontWeight: FontWeight.w700,
                        ),
                      ),
                      SizedBox(height: responsiveHeight(10)),
                      SizedBox(
                        width:
                            SizeConfig.screenWidth -
                            (responsiveHeight(48)), // Subtract padding
                        child: Text(
                          "Enter your Username & Password to continue",
                          maxLines: 2,
                          overflow: TextOverflow.visible,
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontSize: responsiveFont(20),
                            color: kWhiteColor,
                            fontWeight: FontWeight.w400,
                          ),
                        ),
                      ),
                      SizedBox(height: responsiveHeight(16)),
                      Align(
                        alignment: FractionalOffset.centerLeft,
                        child: Text(
                          "Login",
                          style: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            fontSize: responsiveFont(24),
                            fontWeight: FontWeight.w500,
                            color: Colors.black,
                          ),
                        ).paddingOnly(top: 110),
                      ),
                      SizedBox(height: responsiveHeight(14)),
                      AppTextField(
                        controller: usernameController,
                        errorText:
                            isSubmitted
                                ? UIValidator.validateInput(
                                  usernameController.text,
                                )
                                : null,
                        onChange: (p0) {
                          setState(() {}); // Update the UI when the user types
                        },
                        inputStyle: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: 16,
                        ),
                        label: RichText(
                          text: TextSpan(
                            text: 'Username',
                            style: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              color: kLabelTextColor,
                              fontSize: responsiveFont(16),
                              fontWeight: FontWeight.w400,
                            ),
                            children: <TextSpan>[
                              TextSpan(
                                text: ' *',
                                style: TextStyle(
                                  fontFamily: FontConstants.interFonts,
                                  color: Colors.red,
                                  fontSize: responsiveFont(18),
                                  fontWeight: FontWeight.w400,
                                ),
                              ),
                            ],
                          ),
                        ),
                        labelStyle: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w400,
                          fontSize: responsiveFont(16),
                        ),
                        prefixIcon: Image.asset(userRound, scale: 3.5),
                      ),
                      const SizedBox(height: 20),

                      AppTextField(
                        controller: passwordController,
                        validators: UIValidator.validatePassword,
                        errorText:
                            isSubmitted
                                ? UIValidator.validatePassword(
                                  passwordController.text,
                                )
                                : null,
                        onChange: (p0) => setState(() {}),
                        obscureText: isObscure,
                        inputStyle: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontSize: 16,
                        ),
                        label: RichText(
                          text: TextSpan(
                            text: 'Password',
                            style: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              color: kLabelTextColor,
                              fontSize: responsiveFont(16),
                              fontWeight: FontWeight.w400,
                            ),
                            children: [
                              TextSpan(
                                text: ' *',
                                style: TextStyle(color: Colors.red),
                              ),
                            ],
                          ),
                        ),
                        labelStyle: TextStyle(
                          fontFamily: FontConstants.interFonts,
                          fontWeight: FontWeight.w400,
                          fontSize: responsiveFont(20),
                        ),
                        prefixIcon: Image.asset(iconPass, scale: 3.5),
                        suffixIcon: IconButton(
                          icon: Icon(
                            isObscure ? Icons.visibility_off : Icons.visibility,
                            color: Colors.grey,
                          ),
                          onPressed: toggleObscureText,
                        ),
                      ),
                      SizedBox(height: responsiveHeight(10)),
                      Transform.scale(
                        alignment: FractionalOffset.centerLeft,
                        scale: 1,
                        child: CheckboxListTile(
                          controlAffinity: ListTileControlAffinity.leading,
                          materialTapTargetSize:
                              MaterialTapTargetSize.shrinkWrap,
                          value: isRemberMe,
                          contentPadding: EdgeInsets.zero,
                          onChanged: (v) {
                            setState(() {
                              isRemberMe = !isRemberMe;
                            });

                            DataProvider().setKeepSignedIn(isRemberMe);
                          },
                          title: Text(
                            "Keep Me Sign In?",
                            style: TextStyle(
                              fontFamily: FontConstants.interFonts,
                              color: kBlackColor,
                              fontWeight: FontWeight.w500,
                              fontSize: responsiveFont(16),
                            ),
                          ),
                        ),
                      ),
                      SizedBox(height: responsiveHeight(14)),
                      Padding(
                        padding: const EdgeInsets.only(bottom: 8.0),
                        child: AppButtonWithIcon(
                          buttonColor: kButtonColor,
                          title: "Login",
                          icon: Image.asset(
                            iconArrow,
                            height: responsiveHeight(24),
                            width: responsiveHeight(24),
                          ),
                          mWidth: SizeConfig.screenWidth,
                          textStyle: TextStyle(
                            fontFamily: FontConstants.interFonts,
                            color: Colors.white,
                            fontSize: responsiveFont(16),
                            fontWeight: FontWeight.w700,
                          ),
                          onTap: () {
                            loginUser();
                          },
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void toggleObscureText() {
    setState(() {
      isObscure = !isObscure;
    });
  }

  Future<void> getKeepMeSign() async {
    String? userN = await DataProvider().read(DataProvider().kUserName);
    String? userPsw = await DataProvider().read(DataProvider().kPassword);
    bool keepFlag = await DataProvider().getKeepSignedIn();
    if (keepFlag == true) {
      isRemberMe = keepFlag;
      setState(() {});
    }

    if (keepFlag) {
      usernameController.text = userN!;
      passwordController.text = userPsw!;
    } else {
      usernameController.text = "";
      passwordController.text = "";
    }
  }

  @override
  void initState() {
    getKeepMeSign();

    super.initState();
  }

  @override
  void dispose() {
    usernameController.dispose();
    passwordController.dispose();
    // captchaController.dispose();
    super.dispose();
  }

  void loginUser() {
    String userName = usernameController.text.trim();
    String password = passwordController.text.trim();

    if (userName.isEmpty) {
      ToastManager.toast("Please enter username");
    } else if (password.isEmpty) {
      ToastManager.toast("Please enter password");
    } else {
      ToastManager.showLoader();
      Map<String, String> data = {"username": userName, "password": password};
      apiManager.getLoginAPI(data, apiUserLoginCallBack);
    }
  }

  void apiUserLoginCallBack(
    LoginResponseModel? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      // if (apiManager.apiMode == APIMode.Beta) {
      DataProvider().setIsLogin(true);

      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (BuildContext context) => HomeScreen()),
        (Route route) => false,
      );
      // }
      // else {
      //   int dESGID =
      //       DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
      //
      //   if (dESGID == 51 || dESGID == 166) {
      //     DataProvider().setIsLogin(true);
      //
      //     Navigator.of(context).pushAndRemoveUntil(
      //       MaterialPageRoute(builder: (BuildContext context) => HomeScreen()),
      //       (Route route) => false,
      //     );
      //   } else {
      //     getOtp();
      //   }
      // }
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getOtp() {
    ToastManager.showLoader();
    LoginResponseModel? loginResponseModel = DataProvider().getParsedUserData();
    int empCode = loginResponseModel?.output?.first.empCode ?? 0;
    String otpNumber = FormatterManager.generateRandomDigits(5);
    String mOBNO = loginResponseModel?.output?.first.bMobile ?? "";

    Map<String, String> params = {
      "MOBNO": mOBNO,
      "OTP": otpNumber,
      "CreatedBy": empCode.toString(),
    };

    apiManager.getOTPForLoginAPI(params, (response, error, success) {
      ToastManager.hideLoader();
      if (success) {
        ToastManager.toast("OTP sent on $mOBNO number successfully");
        showVerifyOTPPopup(context, empCode, mOBNO, otpNumber);
      } else {
        ToastManager.toast(error);
      }
    });
  }

  static void showVerifyOTPPopup(
    BuildContext parentContext,
    int empCode,
    String mOBNO,
    String otp,
  ) {
    showModalBottomSheet(
      context: parentContext,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      constraints: const BoxConstraints(minWidth: double.infinity),
      builder: (BuildContext sheetContext) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(sheetContext).size.height,
          color: Colors.black.withValues(alpha: 0.5),
          child: VerifyOTPScreen(empCode: empCode, mOBNO: mOBNO, otp: otp),
        );
      },
    );
  }
}
