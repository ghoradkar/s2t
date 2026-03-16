// ignore_for_file: unnecessary_brace_in_string_interps, avoid_print, use_build_context_synchronously

import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../../Screens/LoginScreen/LoginScreen.dart';
import '../Json_Class/LoginResponseModel/LoginResponseModel.dart';

class DataProvider {
  static late SharedPreferences _prefs;
  static const _isKeepSigned = 'isKeepSigned';
  String get kUserName => 'kUserName';

  String get kPassword => 'kPassword';

  static init() async {
    _prefs = await SharedPreferences.getInstance();
  }

  bool isLoggedIn() {
    return _prefs.getBool("isLoggedIn") ?? false;
  }

  void setIsLogin(bool isLogin) async {
    await _prefs.setBool("isLoggedIn", isLogin);
  }

  Future<void> setKeepSignedIn(bool isLoggedIn) async {
    final prefs = await SharedPreferences.getInstance();
    prefs.setBool(_isKeepSigned, isLoggedIn);
  }

  Future<bool> getKeepSignedIn() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getBool(_isKeepSigned) ?? false;
  }

  Future<dynamic> read(String key) async {
    final prefs = await SharedPreferences.getInstance();
    final strRes = prefs.getString(key);
    debugPrint("User data fetched = $strRes");
    if (strRes != null) {
      final jsonRes = json.decode(strRes);
      return jsonRes;
    }
  }

  Future<void> save(String key, value) async {
    final prefs = await SharedPreferences.getInstance();
    final str = json.encode(value);
    debugPrint("save str = $str");
    prefs.setString(key, str);

    debugPrint("saved val = $str");
  }

  Future<void> storeUserData(String data) async {
    // await _prefs.setBool("isLoggedIn", true);
    await _prefs.setString("userData", data);
    print("data stored");
  }

  Future<void> storeUserCredential(String data1) async {
    await _prefs.setString("userCredentials", data1);
    print("data stored ${data1}");
  }

  Future<void> clearSession(BuildContext context) async {
    await _prefs.remove('userData');
    await _prefs.remove('isLoggedIn');

    _navigateToSignIn(context);
  }

  Future<void> setAutoLogoutDate(String day) async {
    await _prefs.setString("AutoLogoutDate", day);
  }

  String getAutoLogoutDate() {
    return _prefs.getString("AutoLogoutDate") ?? "";
  }

  void _navigateToSignIn(BuildContext context) {
    Navigator.pushAndRemoveUntil<void>(
      context,
      MaterialPageRoute<void>(
        builder: (BuildContext context) => const LoginScreen(),
      ),
      ModalRoute.withName('/'),
    );
  }

  // String? getUserCredentials() {
  //   String? data = _prefs.getString("userCredentials");
  //   print('getUserCredentials');
  //   print(data);
  //   return data;
  // }

  String? getUserData() {
    print('user Data');
    print(_prefs.getString("userData"));
    if (_prefs.getString("userData") != null) {
      return _prefs.getString("userData")!;
    } else {
      return null;
    }
  }

  int? getUserId() {
    String? userDetails = _prefs.getString("userData");
    print('user Data');
    print(userDetails);
    if (userDetails != null) {
      var jsonParsed = jsonDecode(userDetails);
      return jsonParsed['data'][0]['EmpCode'];
    } else {
      return null;
    }
  }

  LoginResponseModel? getParsedUserData() {
    print(_prefs.getString("userData"));
    if (_prefs.getString("userData") != null) {
      print('user Data Not null');
      return LoginResponseModel.fromJson(
        jsonDecode(_prefs.getString("userData")!),
      );
    } else {
      print('user Data null');
      return null;
    }
  }



  isRegularCamp(bool isRegularCamp) async {
    await _prefs.setBool("isRegularCamp", isRegularCamp);
  }

  bool getRegularCamp() {
    return _prefs.getBool("isRegularCamp") ?? false;
  }

  void clearUserData() async {
    try {
      print("clear data ");
      await _prefs.clear();
      print("data cleared");
      // await _prefs.setBool("isLoggedIn", false);
      // await _prefs.setString("userData", "");
    } catch (e) {
      print(e);
    }
  }

  // String getToken() {
  //   print(getUserData()!.token);
  //   return getUserData()!.token;
  // }

  // String getUserReferralCode() {
  //   print(getUserData()!.data![0].referral!);
  //   return getUserData()!.data![0].referral!;
  // }

  void navigateToSignIn(BuildContext context) {
    WidgetsFlutterBinding.ensureInitialized();
    Navigator.pushAndRemoveUntil<void>(
      context,
      MaterialPageRoute<void>(
        builder: (BuildContext context) => const LoginScreen(),
      ),
      ModalRoute.withName('/'),
    );
  }

  Map<String, dynamic> getRelationData() {
    var res = {
      "code": 0,
      "status": "Success",
      "data": [
        {
          "lookupDetId": 423,
          "lookupDetValue": "HUS",
          "lookupDetDescEn": "HUSBAND",
          "lookupDetDescRg": null,
          "lookupDetParentId": null,
          "lookupDetParentLevel": null,
          "lookupDetParentName": "",
          "lookupDetList": null,
          "ulbId": null,
        },
        {
          "lookupDetId": 18,
          "lookupDetValue": "SELF",
          "lookupDetDescEn": "SELF",
          "lookupDetDescRg": null,
          "lookupDetParentId": null,
          "lookupDetParentLevel": null,
          "lookupDetParentName": "",
          "lookupDetList": null,
          "ulbId": null,
        },
        {
          "lookupDetId": 12,
          "lookupDetValue": "SON",
          "lookupDetDescEn": "SON",
          "lookupDetDescRg": null,
          "lookupDetParentId": null,
          "lookupDetParentLevel": null,
          "lookupDetParentName": "",
          "lookupDetList": null,
          "ulbId": null,
        },
        {
          "lookupDetId": 19,
          "lookupDetValue": "DAO",
          "lookupDetDescEn": "DAUGHTER",
          "lookupDetDescRg": null,
          "lookupDetParentId": null,
          "lookupDetParentLevel": null,
          "lookupDetParentName": "",
          "lookupDetList": null,
          "ulbId": null,
        },
        {
          "lookupDetId": 13,
          "lookupDetValue": "FAO",
          "lookupDetDescEn": "FATHER",
          "lookupDetDescRg": null,
          "lookupDetParentId": null,
          "lookupDetParentLevel": null,
          "lookupDetParentName": "",
          "lookupDetList": null,
          "ulbId": null,
        },
        {
          "lookupDetId": 14,
          "lookupDetValue": "UNC",
          "lookupDetDescEn": "UNCLE",
          "lookupDetDescRg": null,
          "lookupDetParentId": null,
          "lookupDetParentLevel": null,
          "lookupDetParentName": "",
          "lookupDetList": null,
          "ulbId": null,
        },
        {
          "lookupDetId": 15,
          "lookupDetValue": "AUN",
          "lookupDetDescEn": "AUNTY",
          "lookupDetDescRg": null,
          "lookupDetParentId": null,
          "lookupDetParentLevel": null,
          "lookupDetParentName": "",
          "lookupDetList": null,
          "ulbId": null,
        },
        {
          "lookupDetId": 16,
          "lookupDetValue": "FRN",
          "lookupDetDescEn": "FRIEND",
          "lookupDetDescRg": null,
          "lookupDetParentId": null,
          "lookupDetParentLevel": null,
          "lookupDetParentName": "",
          "lookupDetList": null,
          "ulbId": null,
        },
        {
          "lookupDetId": 17,
          "lookupDetValue": "OTH",
          "lookupDetDescEn": "OTHER",
          "lookupDetDescRg": null,
          "lookupDetParentId": null,
          "lookupDetParentLevel": null,
          "lookupDetParentName": "",
          "lookupDetList": null,
          "ulbId": null,
        },
        {
          "lookupDetId": 443,
          "lookupDetValue": "WIO",
          "lookupDetDescEn": "WIFE",
          "lookupDetDescRg": null,
          "lookupDetParentId": null,
          "lookupDetParentLevel": null,
          "lookupDetParentName": "",
          "lookupDetList": null,
          "ulbId": null,
        },
      ],
    };

    return res;
  }

  // int? getUserId() {
  //   // print(getUserData()!.data![0].id);
  //   return getUserData()!.data![0].id;
  // }
}
