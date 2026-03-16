// ignore_for_file: must_be_immutable, file_names

import 'dart:async';
import 'package:s2toperational/Screens/LoginScreen/device_uuid_manager.dart';

import '../../../../../Modules/constants/fonts.dart';
import 'package:flutter/material.dart';
import 'package:package_info_plus/package_info_plus.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/images.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Modules/widgets/AppActiveButton.dart';
import 'package:s2toperational/Modules/widgets/AppIconTextfield.dart';
import 'package:s2toperational/Screens/HomeScreen/HomeScreen.dart';
import 'package:s2toperational/Screens/LoginScreen/LoginScreen.dart';
import '../../Modules/FormatterManager/FormatterManager.dart';
import '../../Modules/ToastManager/ToastManager.dart';
import '../../Modules/utilities/SizeConfig.dart';

class VerifyOTPScreen extends StatefulWidget {
  VerifyOTPScreen({
    super.key,
    required this.empCode,
    required this.mOBNO,
    required this.otp,
  });

  int empCode;
  String mOBNO;
  String otp;

  @override
  State<VerifyOTPScreen> createState() => _VerifyOTPScreenState();
}

class _VerifyOTPScreenState extends State<VerifyOTPScreen> {
  Timer? _timer;
  int _start = 120;
  bool isButtonEnabled = false;

  TextEditingController otpTextField = TextEditingController();
  APIManager apiManager = APIManager();

  String version = "1.0";

  @override
  void initState() {
    super.initState();
    PackageInfo.fromPlatform().then((PackageInfo packageInfo) {
      version = packageInfo.version;
      setState(() {});
    });
    startTimer();
  }

  @override
  void dispose() {
    _timer?.cancel();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.fromLTRB(20, 0, 20, 0),
        child: Container(
          width: SizeConfig.screenWidth,
          height: 250,
          padding: EdgeInsets.fromLTRB(20, 6, 20, 6),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(8),
            border: Border.all(color: Colors.black, width: 0.5),
          ),
          child: Column(
            children: [
              Text(
                "Verify OTP",
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: responsiveFont(18),
                  color: kBlackColor,
                  fontWeight: FontWeight.w700,
                ),
              ),
              const SizedBox(height: 10),
              AppIconTextfield(
                icon: callIcon,
                titleHeaderString: "OTP",
                controller: otpTextField,
                textInputType: TextInputType.number,
                maxLength: 5,
              ),
              const SizedBox(height: 10),
              Text(
                isButtonEnabled ? "You can resend now" : "Resend in $timerText",
                style: TextStyle(
                  fontFamily: FontConstants.interFonts,
                  fontSize: responsiveFont(18),
                  color: kBlackColor,
                  fontWeight: FontWeight.w400,
                ),
              ),
              const SizedBox(height: 20),
              Row(
                children: [
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Verify",
                      onTap: () {
                        verifyOTP();
                      },
                    ),
                  ),
                  const SizedBox(width: 10),
                  Expanded(
                    child: AppActiveButton(
                      isCancel: true,
                      buttontitle: "Resend OTP",
                      onTap: () {
                        if (isButtonEnabled) {
                          resendOTP();
                        }
                      },
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }


  void startTimer() {
    setState(() {
      _start = 120;
      isButtonEnabled = false;
    });

    _timer?.cancel();
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      if (_start == 0) {
        setState(() {
          isButtonEnabled = true;
          otpTextField.clear();
        });
        timer.cancel();
      } else {
        setState(() {
          _start--;
        });
      }
    });
  }

  String get timerText {
    int minutes = _start ~/ 60;
    int seconds = _start % 60;
    return "${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')}";
  }



  void resendOTP() {
    ToastManager.showLoader();

    widget.otp = FormatterManager.generateRandomDigits(5);

    Map<String, String> params = {
      "MOBNO": widget.mOBNO,
      "OTP": widget.otp,
      "CreatedBy": widget.empCode.toString(),
    };

    apiManager.getOTPForLoginAPI(params, (response, error, success) {
      ToastManager.hideLoader();

      if (success) {
        ToastManager.toast("OTP sent on ${widget.mOBNO} number successfully");
        startTimer();
      } else {
        ToastManager.toast(error);
      }
    });
  }

  void verifyOTP() {
    String otpString = otpTextField.text.trim();
    if (otpString.isEmpty) {
      ToastManager.toast("Please enter OTP");
    } else if (otpString.length < 5) {
      ToastManager.toast("Please enter valid OTP");
    } else {
      ToastManager.showLoader();
      Map<String, String> params = {"MOBNO": widget.mOBNO, "OTP": otpString};

      apiManager.verifyOTPMedicineDeliveryAPI(params, (
          response,
          error,
          success,
          ) {
        if (success) {
          checkAndroidID();
        } else {
          ToastManager.hideLoader();
          ToastManager.toast(error);
        }
      });
    }
  }

  Future<void> checkAndroidID() async {
    String uuidString = await DeviceUUIDManager().getDeviceUUID();
    // var uuid = Uuid();
    // String uuidString = uuid.v4();
    Map<String, String> params = {
      "UserId": "${widget.empCode}",
      "AndroidID": uuidString,
    };
    apiManager.getUSERAndroidIDAPI(params, (response, error, success) {
      if (success) {
        int? isLoginAllowed = response?.allowedForLogin;
        String status = response?.status ?? "";
        String msg = response?.message ?? "";
        ToastManager.hideLoader();
        if (response != null) {
          if (status == "fail" || status == "Fail") {
            if (msg == "0") {
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  return AlertDialog(
                    title: Text("Alert"),
                    content: Text(
                      "हा मोबाईल लॉगिनसाठी अधिकृत मोबाईल मानला जाईल, तुम्ही तुमच्या स्वतःच्या मोबाईलचा वापर करून लॉग इन करत आहात याची खात्री करा.",
                    ),
                    actions: [
                      TextButton(
                        child: const Text("Confirm"),
                        onPressed: () {
                          Navigator.pop(context);
                          saveAndroidID(uuidString);
                        },
                      ),
                      TextButton(
                        child: const Text("Cancel"),
                        onPressed: () {
                          Navigator.pop(context);
                          Navigator.pop(context);
                          Navigator.of(context).pushAndRemoveUntil(
                            MaterialPageRoute(
                              builder: (BuildContext context) => LoginScreen(),
                            ),
                                (Route route) => false,
                          );
                        },
                      ),
                    ],
                  );
                },
              );
            } else {
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  return AlertDialog(
                    title: Text("Alert"),
                    content: Text(
                      "You're trying to login from different device $uuidString . Do you want to change your device?",
                    ),
                    actions: [
                      TextButton(
                        child: const Text("Yes"),
                        onPressed: () {
                          Navigator.pop(context);
                          saveAndroidID(uuidString);
                        },
                      ),
                      TextButton(
                        child: const Text("No"),
                        onPressed: () {
                          Navigator.pop(context);
                          Navigator.pop(context);
                          Navigator.of(context).pushAndRemoveUntil(
                            MaterialPageRoute(
                              builder: (BuildContext context) => LoginScreen(),
                            ),
                                (Route route) => false,
                          );
                        },
                      ),
                    ],
                  );
                },
              );
            }
          } else {
            if (isLoginAllowed == 0) {
              showDialog(
                context: context,
                builder: (BuildContext context) {
                  return AlertDialog(
                    title: Text("Alert"),
                    content: Text(
                      "तुम्ही तुमचा मोबाईल बदलण्याची मर्यादा गाठली आहे. लॉगिन करण्यासाठी तुमचा नोंदणीकृत मोबाईल वापरा.",
                    ),
                    actions: [
                      TextButton(
                        child: const Text("Okay"),
                        onPressed: () {
                          Navigator.pop(context);
                          Navigator.pop(context);
                          Navigator.of(context).pushAndRemoveUntil(
                            MaterialPageRoute(
                              builder: (BuildContext context) => LoginScreen(),
                            ),
                                (Route route) => false,
                          );
                        },
                      ),
                    ],
                  );
                },
              );
            } else {
              saveAndroidToken(uuidString);
            }
          }
        } else {
          ToastManager.toast(error);
        }
      }
    });
  }

  saveAndroidID(String uuidString) {
    ToastManager.showLoader();
    Map<String, String> params = {
      "UserId": "${widget.empCode}",
      "AndroidID": uuidString,
      "VersionNo": version,
    };

    apiManager.saveAndroidIDAPI(params, (response, error, success) {
      if (success) {
        int? isLoginAllowed = response?.allowedForLogin;
        String status = response?.status ?? "";
        String msg = response?.message ?? "";
        ToastManager.hideLoader();
        if (status == "fail" || status == "Fail") {
          if (msg.toLowerCase() ==
              "AndroidID Already Exists With Another User".toLowerCase()) {
            showDialog(
              context: context,
              builder: (BuildContext context) {
                return AlertDialog(
                  title: Text("Alert"),
                  content: Text(
                    "हा मोबाईल आधीच दुसऱ्या वापरकर्त्याशी लिंक केलेला आहे. कृपया या ॲप्लिकेशनमध्ये प्रवेश करण्यासाठी तुमचा स्वतःचा मोबाईल वापरा.",
                  ),
                  actions: [
                    TextButton(
                      child: const Text("Okay"),
                      onPressed: () {
                        Navigator.pop(context);
                        Navigator.of(context).pushAndRemoveUntil(
                          MaterialPageRoute(
                            builder: (BuildContext context) => LoginScreen(),
                          ),
                              (Route route) => false,
                        );
                      },
                    ),
                  ],
                );
              },
            );
          } else {
            ToastManager.toast(msg);
          }
        } else {
          if (isLoginAllowed == 0) {
            showDialog(
              context: context,
              builder: (BuildContext context) {
                return AlertDialog(
                  title: Text("Alert"),
                  content: Text(
                    "तुम्ही तुमचा मोबाईल बदलण्याची मर्यादा गाठली आहे. लॉगिन करण्यासाठी तुमचा नोंदणीकृत मोबाईल वापरा.",
                  ),
                  actions: [
                    TextButton(
                      child: const Text("Okay"),
                      onPressed: () {
                        Navigator.pop(context);
                        Navigator.of(context).pushAndRemoveUntil(
                          MaterialPageRoute(
                            builder: (BuildContext context) => LoginScreen(),
                          ),
                              (Route route) => false,
                        );
                      },
                    ),
                  ],
                );
              },
            );
          } else {
            ToastManager.toast("You have changed device for $msg times");
            saveAndroidToken(uuidString);
          }
        }
      }
    });
  }

  void saveAndroidToken(String uuidString) {
    Map<String, String> params = {
      "USERID": "${widget.empCode}",
      "ANDROIEDTOKEN": uuidString,
      "ActiveStatus": "1",
    };

    apiManager.saveAndroidTokenAPI(params, (response, error, success) {
      if (success) {
        DataProvider().setIsLogin(true);
        Navigator.pop(context);
        Navigator.of(context).pushAndRemoveUntil(
          MaterialPageRoute(builder: (BuildContext context) => HomeScreen()),
              (Route route) => false,
        );
      }
    });
  }
}
