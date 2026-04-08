// ignore_for_file: file_names, avoid_print

import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter_keyboard_visibility/flutter_keyboard_visibility.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:s2toperational/Modules/APIManager/APIManager.dart';
import 'package:s2toperational/Modules/constants/constants.dart';
import 'package:s2toperational/Modules/constants/fonts.dart';
import 'package:s2toperational/Modules/widgets/AppTextField.dart';
import 'package:s2toperational/Modules/widgets/CommonText.dart';
import 'package:android_intent_plus/android_intent.dart';
import 'package:url_launcher/url_launcher.dart';

import '../../Modules/Enums/Enums.dart';
import '../../Modules/FormatterManager/FormatterManager.dart';
import '../../Modules/Json_Class/CampTypeResponse/CampTypeResponse.dart';
import '../../Modules/Json_Class/HomeAndHubLabCampCreationResponse/HomeAndHubLabCampCreationResponse.dart';
import '../../Modules/Json_Class/InitiatedByResponse/InitiatedByResponse.dart';
import '../../Modules/Json_Class/LandingLabCampCreationResponse/LandingLabCampCreationResponse.dart';
import '../../Modules/Json_Class/ScreeningTestCampCreationResponse/ScreeningTestCampCreationResponse.dart';
import '../../Modules/Json_Class/TalukaCampCreationResponse/TalukaCampCreationResponse.dart';
import '../../Modules/ToastManager/ToastManager.dart';
import '../../Modules/constants/images.dart';
import '../../Modules/utilities/DataProvider.dart';
import '../../Modules/utilities/SizeConfig.dart';
import '../../Modules/utilities/WidgetPaddingX.dart';
import '../../Modules/utilities/validators.dart';
import '../../Modules/widgets/AppActiveButton.dart';
import '../../Modules/widgets/S2TAppBar.dart';
import '../../Views/DropDownListScreen/DropDownListScreen.dart';
import '../../Views/MultiSelectionDropDownListScreen/MultiSelectionDropDownListScreen.dart';

class CampCreationScreen extends StatefulWidget {
  const CampCreationScreen({super.key});

  @override
  State<CampCreationScreen> createState() => _CampCreationScreenState();
}

class _CampCreationScreenState extends State<CampCreationScreen> {
  TextEditingController campNameTextField = TextEditingController();
  TextEditingController campAddressTextField = TextEditingController();
  TextEditingController expectedBeneficiaryTextField = TextEditingController();

  int dESGID = 0;
  int empCode = 0;
  int districtId = 0;
  String districtName = "";
  String? _selectedCampDate;
  String? _selectedPostCampDate;

  // Location state
  double _campLatitude = 0.0;
  double _campLongitude = 0.0;
  bool _isManualAddressEntry = false;

  static const _googleMapsApiKey = 'AIzaSyDbtPLpwrcS571PfdJw9ednQAemxBiNhUA';

  CampTypeOutput? selectedCampType;
  InitiatedByOutput? selectedInitiatedBy;
  TalukaCampCreationOutput? selectedTaluka;
  LandingLabCampCreationOutput? selectedLandingLab;
  HomeAndHubLabCampCreationOutput? selectedHomeAndHubLab;
  List<ScreeningTestCampCreationOutput> selectedScreeningTest = [];

  ToastManager toastManager = ToastManager();
  APIManager apiManager = APIManager();

  Future<void> _selectCampDate(BuildContext context) async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime.now(),
      lastDate: DateTime(2101),
    );
    if (picked != null) {
      setState(() {
        _selectedCampDate = FormatterManager.formatDateToString(picked);
        DateTime nextWeekDay = picked.add(const Duration(days: 7));
        _selectedPostCampDate = FormatterManager.formatDateToString(
          nextWeekDay,
        );
      });
    }
  }

  void _showDropDownBottomSheet(
    String title,
    List<dynamic> list,
    DropDownTypeMenu dropDownType,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.33,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: DropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: (p0) {
              if (dropDownType == DropDownTypeMenu.CampType) {
                selectedCampType = p0;
                selectedInitiatedBy = InitiatedByOutput(
                  iD: 1,
                  initiatedBy: 'Internal',
                );
                selectedTaluka = null;
                selectedLandingLab = null;
                selectedHomeAndHubLab = null;
                selectedScreeningTest = [];
                campNameTextField.text = "";
                campAddressTextField.text = "";
                _selectedCampDate = "";
                _selectedPostCampDate = "";
                expectedBeneficiaryTextField.text = "";
              } else if (dropDownType == DropDownTypeMenu.InitiatedBy) {
                selectedInitiatedBy = p0;
                selectedTaluka = null;
                selectedLandingLab = null;
                selectedHomeAndHubLab = null;
                selectedScreeningTest = [];
                campNameTextField.text = "";
                campAddressTextField.text = "";
                _selectedCampDate = "";
                _selectedPostCampDate = "";
                expectedBeneficiaryTextField.text = "";
              } else if (dropDownType == DropDownTypeMenu.TalukaCampList) {
                selectedTaluka = p0;
                selectedLandingLab = null;
                selectedHomeAndHubLab = null;
                selectedScreeningTest = [];
                campNameTextField.text = "";
                campAddressTextField.text = "";
                _selectedCampDate = "";
                _selectedPostCampDate = "";
                expectedBeneficiaryTextField.text = "";
              } else if (dropDownType ==
                  DropDownTypeMenu.LandingLabCampCreation) {
                selectedLandingLab = p0;
                selectedHomeAndHubLab = null;
                selectedScreeningTest = [];
                campNameTextField.text = "";
                campAddressTextField.text = "";
                _selectedCampDate = "";
                _selectedPostCampDate = "";
                expectedBeneficiaryTextField.text = "";
                getHomeAndHubLabAPI();
              }
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  void _showMultiSelectionDropDownBottomSheet(
    String title,
    List<dynamic> list,
    DropDownMultipleTypeMenu dropDownType,
  ) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      constraints: const BoxConstraints(minWidth: double.infinity),
      backgroundColor: Colors.white,
      isDismissible: false,
      enableDrag: false,
      builder: (BuildContext context) {
        return Container(
          width: double.infinity,
          height: MediaQuery.of(context).size.width * 1.33,
          decoration: const BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.only(
              topLeft: Radius.circular(20),
              topRight: Radius.circular(20),
            ),
          ),
          child: MultiSelectionDropDownListScreen(
            titleString: title,
            dropDownList: list,
            dropDownMenu: dropDownType,
            onApplyTap: (p0) {
              if (dropDownType == DropDownMultipleTypeMenu.ScreeningTest) {
                selectedScreeningTest = p0;
              }
              setState(() {});
            },
          ),
        );
      },
    ).whenComplete(() {
      setState(() {});
    });
  }

  void showAlertmessage(String campID) {
    // ToastManager.showSuccessPopup(
    //   context,
    //   icSuccessIcon,
    //   "Camp Created Successfully.\nCamp ID $campID",
    // );

    ToastManager().showSuccessOkayDialog(
      context: context,
      title: "Success",
      message: "Camp Created Successfully.\nCamp ID $campID",
      onTap: () {
        Navigator.pop(context);
        Navigator.pop(context);
      },
    );
  }

  String selectedScreeningTestSting() {
    List<String> tempScreeningTest = [];
    for (ScreeningTestCampCreationOutput screeningTest
        in selectedScreeningTest) {
      tempScreeningTest.add(screeningTest.testName ?? "");
    }
    return tempScreeningTest.join(",");
  }

  void campTypeAPI() {
    ToastManager.showLoader();
    if (DataProvider().getRegularCamp()) {
      apiManager.getCampTypeNonD2DRAPI(apiCampTypeNonD2CallBack);
    } else {
      if (dESGID == 108) {
        apiManager.getCampTypeFlexiAPI(apiCampTypeFlexiCallBack);
      } else if (dESGID == 136 || dESGID == 139) {
        apiManager.getCampTypeMMUAPI(apiCampTypeMMUCallBack);
      } else {
        apiManager.getCampTypeD2DAPI(apiCampTypeD2DCallBack);
      }
    }
  }

  void apiCampTypeNonD2CallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void apiCampTypeFlexiCallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void apiCampTypeMMUCallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void apiCampTypeD2DCallBack(
    CampTypeResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Camp Type",
        response?.output ?? [],
        DropDownTypeMenu.CampType,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getInitiatedByAPI() {
    ToastManager.showLoader();
    apiManager.getInitiatedByListForCampAPI(apiInitiatedByCallBack);
  }

  void apiInitiatedByCallBack(
    InitiatedByResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Initiated By",
        response?.output ?? [],
        DropDownTypeMenu.InitiatedBy,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getTalukaAPI() {
    ToastManager.showLoader();
    Map<String, String> data = {
      "STATELGDCODE": "2",
      "DISTLGDCODE": districtId.toString(),
    };
    apiManager.getTalukaAPI(data, apiTalukaCallBack);
  }

  void apiTalukaCallBack(
    TalukaCampCreationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Taluka",
        response?.output ?? [],
        DropDownTypeMenu.TalukaCampList,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getLandingLabAPI() {
    ToastManager.showLoader();
    Map<String, String> data = {"DISTLGDCODE": districtId.toString()};
    apiManager.getLandingLabAPI(data, apiLandingLabCallBack);
  }

  void apiLandingLabCallBack(
    LandingLabCampCreationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showDropDownBottomSheet(
        "Landing Lab",
        response?.output ?? [],
        DropDownTypeMenu.LandingLabCampCreation,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getHomeAndHubLabAPI() {
    ToastManager.showLoader();
    Map<String, String> data = {
      "LabCode": selectedLandingLab?.labCode.toString() ?? "0",
      "TypeID": "0",
    };
    apiManager.getHomeLabHubLabAPI(data, apiHomeLabHubLabCallBack);
  }

  void apiHomeLabHubLabCallBack(
    HomeAndHubLabCampCreationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      selectedHomeAndHubLab = response?.output?.first;
      setState(() {});
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void getScreeningTestAPI() {
    ToastManager.showLoader();
    apiManager.getScreeningTestAPI(apiScreeningTestCallBack);
  }

  void apiScreeningTestCallBack(
    ScreeningTestCampCreationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      _showMultiSelectionDropDownBottomSheet(
        "Screening Test",
        response?.output ?? [],
        DropDownMultipleTypeMenu.ScreeningTest,
      );
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void createCampCreation() {
    ToastManager.showLoader();
    String campName = campNameTextField.text.trim();
    String campAddress = campAddressTextField.text.trim();
    String expectedBeneficiary = expectedBeneficiaryTextField.text.trim();

    String campType = selectedCampType?.cAMPTYPE?.toString() ?? "0";
    String campTestMapping = dictionaryToString();
    String initiatedBy = selectedInitiatedBy?.iD.toString() ?? "0";
    String landingLab = selectedLandingLab?.labCode?.toString() ?? "0";
    String talukaCode = selectedTaluka?.tALLGDCODE?.toString() ?? "";

    Map<String, String> data = {
      "CampName": campName,
      "CampDate": _selectedCampDate ?? "",
      "PostCampDate": _selectedPostCampDate ?? "",
      "LABCODE": landingLab,
      "AffilatedHospitalId": "0",
      "Distlgdcode": districtId.toString(),
      "UserId": empCode.toString(),
      "CampLocation": campAddress,
      "CampTestMapping": campTestMapping,
      "CampType": campType,
      "InitiatedBy": initiatedBy,
      "LandingLab": landingLab,
      "CscpreapprovedId": "0",
      "TalukaCode": talukaCode,
      "ExpBenCount": expectedBeneficiary,
    };
    print(data);
    apiManager.createCampCreationAPI(data, apiCampCreationCallBack);
  }

  void apiCampCreationCallBack(
    LandingLabCampCreationResponse? response,
    String errorMessage,
    bool success,
  ) async {
    ToastManager.hideLoader();

    if (success) {
      showAlertmessage(response?.message ?? "");
    } else {
      ToastManager.toast(errorMessage);
    }
  }

  void saveDidPressed() {
    String campName = campNameTextField.text.trim();
    String campAddress = campAddressTextField.text.trim();
    String expectedBeneficiary = expectedBeneficiaryTextField.text.trim();
    if (selectedCampType == null) {
      toastManager.showAlertMessage(context, "Select Camp Type", Colors.red);
    } else if (selectedInitiatedBy == null) {
      toastManager.showAlertMessage(context, "Select Initiated By", Colors.red);
    } else if (districtName.isEmpty) {
      toastManager.showAlertMessage(context, "Select district", Colors.red);
    } else if (selectedTaluka == null) {
      toastManager.showAlertMessage(context, "Select taluka", Colors.red);
    } else if (selectedLandingLab == null) {
      toastManager.showAlertMessage(context, "Select Landing lab", Colors.red);
    } else if (campName.isEmpty) {
      toastManager.showAlertMessage(context, "Select camp name", Colors.red);
    } else if (campAddress.isEmpty) {
      toastManager.showAlertMessage(context, "Select Camp Address", Colors.red);
    } else if (campAddress.length <= 14) {
      toastManager.showAlertMessage(
        context,
        "Camp address should be grater than or equal to 15 character length",
        Colors.red,
      );
    } else if (_selectedCampDate == null) {
      toastManager.showAlertMessage(context, "Select camp date", Colors.red);
    } else if (selectedScreeningTest.isEmpty) {
      toastManager.showAlertMessage(
        context,
        "Select screening tests",
        Colors.red,
      );
    } else if (expectedBeneficiary.isEmpty) {
      toastManager.showAlertMessage(
        context,
        "Please Enter Expected Beneficiary",
        Colors.red,
      );
    } else if (UIValidator.isAllZeros(expectedBeneficiary)) {
      toastManager.showAlertMessage(
        context,
        "Please Enter valid Expected Beneficiary",
        Colors.red,
      );
    } else {
      createCampCreation();
    }
  }

  String dictionaryToString() {
    ToastManager.showLoader();
    List<Map<String, dynamic>> dataList = [];

    for (ScreeningTestCampCreationOutput obj in selectedScreeningTest) {
      Map<String, dynamic> dict = {
        "CampID": "0",
        "TestID": obj.testId ?? 0,
        "IsTestProcess": obj.isSelected ? "1" : "0",
        "IsActive": "1",
        "CreatedBy": empCode.toString(),
      };

      dataList.add(dict);
    }

    try {
      String json = jsonEncode(dataList);
      print(json);
      return json;
    } catch (e) {
      print("Something went wrong with parsing JSON: $e");
      return "";
    }
  }

  @override
  void initState() {
    super.initState();
    dESGID = DataProvider().getParsedUserData()?.output?.first.dESGID ?? 0;
    districtId =
        DataProvider().getParsedUserData()?.output?.first.dISTLGDCODE ?? 0;
    districtName =
        DataProvider().getParsedUserData()?.output?.first.district ?? "";
    empCode = DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    // Pre-select "Internal" as default (matching native app behavior)
    selectedInitiatedBy = InitiatedByOutput(iD: 1, initiatedBy: 'Internal');
  }

  // ─── Location Methods ───────────────────────────────────────────────────────

  Future<void> _searchOnMap() async {
    final confirmed = await _showLocationAlertDialog();
    if (!confirmed) return;
    _showPlacesSearchBottomSheet();
  }

  Future<bool> _showLocationAlertDialog() async {
    // return ToastManager.showAlertDialog(
    //       context,
    //       'कृपया कॅम्पचा पत्ता फ्लेबोशी कन्फर्म करूनच टाका.',
    //       () {
    //         Navigator.pop(context, true);
    //       },
    //     ) ??
    //     false;

    return await showDialog<bool>(
          context: context,
          builder:
              (ctx) => AlertDialog(
                // title: const Text('Alert'),
                content: Column(
                  mainAxisSize: MainAxisSize.min,
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Padding(
                      padding: EdgeInsets.all(8.0),
                      child: Image.asset(warning, width: 100.w),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Text(
                        "कृपया कॅम्पचा पत्ता फ्लेबोशी कन्फर्म करूनच टाका.",
                      ),
                    ),

                    SizedBox(
                      width: 80.w,
                      child: AppActiveButton(
                        buttontitle: "OK",
                        onTap: () {
                          Navigator.pop(ctx, true);
                        },
                      ),
                    ),
                  ],
                ),
              ),
        ) ??
        false;
  }

  Future<List<Map<String, dynamic>>> _autocomplete(String input) async {
    try {
      final uri = Uri.parse(
        'https://maps.googleapis.com/maps/api/place/autocomplete/json'
        '?input=${Uri.encodeComponent(input)}'
        '&components=country:in'
        '&location=19.0,76.0&radius=400000'
        '&key=$_googleMapsApiKey',
      );
      final request = await HttpClient().getUrl(uri);
      final response = await request.close();
      final body = await response.transform(utf8.decoder).join();
      final data = json.decode(body) as Map<String, dynamic>;
      if (data['status'] == 'OK' || data['status'] == 'ZERO_RESULTS') {
        return List<Map<String, dynamic>>.from(data['predictions'] ?? []);
      }
      debugPrint('Autocomplete status: ${data['status']}');
    } catch (e) {
      debugPrint('Autocomplete error: $e');
    }
    return [];
  }

  void _showPlacesSearchBottomSheet() {
    final searchController = TextEditingController();
    List<Map<String, dynamic>> predictions = [];
    bool isSearching = false;

    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.white,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (ctx) {
        return StatefulBuilder(
          builder: (ctx, setSheetState) {
            return Padding(
              padding: EdgeInsets.only(
                bottom: MediaQuery.of(ctx).viewInsets.bottom,
              ),
              child: SizedBox(
                height: MediaQuery.of(ctx).size.height * 0.6,
                child: Column(
                  children: [
                    Container(
                      margin: const EdgeInsets.only(top: 8),
                      width: 40,
                      height: 4,
                      decoration: BoxDecoration(
                        color: Colors.grey[300],
                        borderRadius: BorderRadius.circular(2),
                      ),
                    ),
                    const SizedBox(height: 12),
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 16),
                      child: TextField(
                        controller: searchController,
                        autofocus: true,
                        decoration: InputDecoration(
                          hintText: 'Search location...',
                          prefixIcon: const Icon(Icons.search),
                          border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(10),
                          ),
                          suffixIcon:
                              isSearching
                                  ? const Padding(
                                    padding: EdgeInsets.all(12),
                                    child: CircularProgressIndicator(
                                      strokeWidth: 2,
                                    ),
                                  )
                                  : null,
                        ),
                        onChanged: (value) async {
                          if (value.length < 3) {
                            setSheetState(() => predictions = []);
                            return;
                          }
                          setSheetState(() => isSearching = true);
                          final results = await _autocomplete(value);
                          setSheetState(() {
                            predictions = results;
                            isSearching = false;
                          });
                        },
                      ),
                    ),
                    const SizedBox(height: 8),
                    Expanded(
                      child: ListView.separated(
                        itemCount: predictions.length,
                        separatorBuilder:
                            (_, index) => const Divider(height: 1),
                        itemBuilder: (_, index) {
                          final p = predictions[index];
                          final fmt =
                              p['structured_formatting']
                                  as Map<String, dynamic>? ??
                              {};
                          final primary =
                              fmt['main_text'] as String? ??
                              p['description'] as String? ??
                              '';
                          final secondary =
                              fmt['secondary_text'] as String? ?? '';
                          final placeId = p['place_id'] as String? ?? '';
                          return ListTile(
                            leading: const Icon(Icons.location_on_outlined),
                            title: Text(primary),
                            subtitle:
                                secondary.isNotEmpty
                                    ? Text(
                                      secondary,
                                      maxLines: 1,
                                      overflow: TextOverflow.ellipsis,
                                    )
                                    : null,
                            onTap: () async {
                              Navigator.pop(ctx);
                              await _fetchPlaceDetails(placeId);
                            },
                          );
                        },
                      ),
                    ),
                  ],
                ),
              ),
            );
          },
        );
      },
    );
  }

  Future<void> _fetchPlaceDetails(String placeId) async {
    ToastManager.showLoader();
    try {
      final uri = Uri.parse(
        'https://maps.googleapis.com/maps/api/place/details/json'
        '?place_id=$placeId'
        '&fields=name,geometry,formatted_address'
        '&key=$_googleMapsApiKey',
      );
      final request = await HttpClient().getUrl(uri);
      final response = await request.close();
      final body = await response.transform(utf8.decoder).join();
      final data = json.decode(body) as Map<String, dynamic>;

      debugPrint('fetchPlaceDetails status: ${data['status']}');

      if (data['status'] == 'OK') {
        final result = data['result'] as Map<String, dynamic>;
        final location = result['geometry']['location'];
        final lat = (location['lat'] as num).toDouble();
        final lng = (location['lng'] as num).toDouble();
        final address = result['formatted_address'] as String? ?? '';

        setState(() {
          _campLatitude = lat;
          _campLongitude = lng;
          campAddressTextField.text = address;
          _isManualAddressEntry = false;
        });
      } else {
        ToastManager.toast('Place details error: ${data['status']}');
      }
    } catch (e) {
      debugPrint('fetchPlaceDetails error: $e');
      ToastManager.toast('Failed to fetch place details');
    } finally {
      ToastManager.hideLoader();
    }
  }

  Future<void> _viewOnMap() async {
    if (_campLatitude == 0 || _campLongitude == 0) {
      ToastManager.toast('Please search camp location or use current location');
      return;
    }
    final geoUri =
        'geo:$_campLatitude,$_campLongitude?q=$_campLatitude,$_campLongitude(Selected Location)';
    try {
      // Match native: intent with package = com.google.android.apps.maps
      final intent = AndroidIntent(
        action: 'action_view',
        data: geoUri,
        package: 'com.google.android.apps.maps',
      );
      await intent.launch();
    } catch (_) {
      // Fallback: open in browser if Google Maps is not installed
      final fallback = Uri.parse(
        'https://maps.google.com/?q=$_campLatitude,$_campLongitude',
      );
      if (await canLaunchUrl(fallback)) {
        await launchUrl(fallback, mode: LaunchMode.externalApplication);
      } else {
        ToastManager.toast('Google Maps not installed');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    SizeConfig().init(context);
    return Scaffold(
      appBar: mAppBar(
        scTitle: "Camp Creation",
        leadingIcon: iconBackArrow,
        onLeadingIconClick: () {
          Navigator.pop(context);
        },
      ),
      body: KeyboardDismissOnTap(
        dismissOnCapturedTaps: true,
        child: SingleChildScrollView(
          child: Column(
            children: [
              AppTextField(
                controller: TextEditingController(
                  text: selectedCampType?.campTypeDescription ?? "",
                ),
                readOnly: true,
                onTap: () {
                  campTypeAPI();
                },
                hint: 'Camp Type',
                label: CommonText(
                  text: 'Camp Type',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                fieldRadius: 10,
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icnTent,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // AppDropdownTextfield(
              //   icon: icnTent,
              //   titleHeaderString: "Camp Type",
              //   valueString: selectedCampType?.campTypeDescription ?? "",
              //   onTap: () {
              //     campTypeAPI();
              //   },
              // ),
              // const SizedBox(height: 8),
              // AppDropdownTextfield(
              //   icon: icInitiatedBy,
              //   titleHeaderString: "Initiated By",
              //   valueString: selectedInitiatedBy?.initiatedBy ?? "",
              //   onTap: () {
              //     getInitiatedByAPI();
              //   },
              // ),
              AppTextField(
                controller: TextEditingController(
                  text: selectedInitiatedBy?.initiatedBy ?? "",
                ),
                readOnly: true,
                onTap: () {
                  getInitiatedByAPI();
                },
                hint: 'Initiated By',
                label: CommonText(
                  text: 'Initiated By',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                fieldRadius: 10,
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icInitiatedBy,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // const SizedBox(height: 8),
              // AppDropdownTextfield(
              //   icon: icMapPin,
              //   titleHeaderString: "District",
              //   valueString: districtName,
              //   isDisabled: true,
              //   onTap: () {},
              // ),
              AppTextField(
                controller: TextEditingController(text: districtName),
                readOnly: true,
                hint: 'District',
                label: CommonText(
                  text: 'District',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                fieldRadius: 10,
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icMapPin,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
              ).paddingOnly(top: 12),

              // const SizedBox(height: 8),
              AppTextField(
                controller: TextEditingController(
                  text: selectedTaluka?.tALNAME ?? "",
                ),
                readOnly: true,
                onTap: () {
                  getTalukaAPI();
                },
                hint: 'Taluka',
                label: CommonText(
                  text: 'Taluka',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                fieldRadius: 10,
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icMapPin,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // AppDropdownTextfield(
              //   icon: icMapPin,
              //   titleHeaderString: "Taluka",
              //   valueString: selectedTaluka?.tALNAME ?? "",
              //   onTap: () {
              //     getTalukaAPI();
              //   },
              // ),
              // const SizedBox(height: 8),
              AppTextField(
                controller: TextEditingController(
                  text: selectedLandingLab?.labName ?? "",
                ),
                readOnly: true,
                onTap: () {
                  getLandingLabAPI();
                },
                hint: 'Landing Lab',
                label: CommonText(
                  text: 'Landing Lab',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                fieldRadius: 10,
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icLandingLab,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // AppDropdownTextfield(
              //   icon: icLandingLab,
              //   titleHeaderString: "Landing Lab",
              //   valueString: selectedLandingLab?.labName ?? "",
              //   onTap: () {
              //     getLandingLabAPI();
              //   },
              // ),
              // const SizedBox(height: 8),
              // AppIconTextfield(
              //   icon: icnTent,
              //   titleHeaderString: "Camp Name",
              //   controller: campNameTextField,
              // ),
              // const SizedBox(height: 8),
              AppTextField(
                controller: campNameTextField,
                readOnly: false,
                // onTap: () {
                //   getLandingLabAPI();
                // },
                hint: 'Camp Name',
                label: CommonText(
                  text: 'Camp Name',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                fieldRadius: 10,
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icnTent,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                // suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),
              // Location action buttons
              Row(
                children: [
                  InkWell(
                    onTap: () {
                      _searchOnMap();
                    },
                    child: Row(
                      children: [
                        Icon(
                          Icons.search,
                          size: 22,
                          color: kPrimaryColor,
                        ).paddingOnly(right: 2.w),
                        CommonText(
                          text: "Search On Map",
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w600,
                          textColor: kBlackColor,
                          textAlign: TextAlign.start,
                        ),
                      ],
                    ).paddingOnly(
                      top: 8.h,
                      left: 10.w,
                      right: 10.h,
                      bottom: 8.h,
                    ),
                  ),

                  SizedBox(width: 12.w),

                  // Expanded(
                  //   child: OutlinedButton.icon(
                  //     onPressed: _viewOnMap,
                  //     icon: const Icon(Icons.map_outlined, size: 15),
                  //     label: Text(
                  //       'View On Map',
                  //       style: TextStyle(fontSize: 11.sp),
                  //     ),
                  //   ),
                  // ),
                  InkWell(
                    onTap: () {
                      _viewOnMap();
                    },
                    child: Row(
                      children: [
                        Icon(
                          Icons.map_outlined,
                          size: 22,
                          color: kPrimaryColor,
                        ).paddingOnly(right: 2.w),
                        CommonText(
                          text: "View On Map",
                          fontSize: 14.sp,
                          fontWeight: FontWeight.w600,
                          textColor: kBlackColor,
                          textAlign: TextAlign.start,
                        ),
                      ],
                    ).paddingOnly(
                      top: 8.h,
                      left: 10.w,
                      right: 10.h,
                      bottom: 8.h,
                    ),
                  ),
                ],
              ).paddingOnly(top: 12),
              // Enter address manually toggle
              Row(
                children: [
                  Checkbox(
                    value: _isManualAddressEntry,
                    onChanged: (val) {
                      if (val == true) {
                        showDialog(
                          context: context,
                          builder:
                              (ctx) => AlertDialog(
                                // title: const Text('Alert'),
                                content: Column(
                                  mainAxisSize: MainAxisSize.min,
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: [
                                    Padding(
                                      padding: EdgeInsets.all(8.0),
                                      child: Image.asset(warning, width: 100.w),
                                    ),
                                    Padding(
                                      padding: const EdgeInsets.all(8.0),
                                      child: Text(
                                        "कृपया कॅम्पचा पत्ता फ्लेबोशी कन्फर्म करूनच टाका.",
                                      ),
                                    ),

                                    SizedBox(
                                      width: 80.w,
                                      child: AppActiveButton(
                                        buttontitle: "OK",
                                        onTap: () {
                                          Navigator.pop(ctx, true);
                                        },
                                      ),
                                    ),
                                  ],
                                ),
                                // actions: [
                                //   TextButton(
                                //     onPressed: () => Navigator.pop(ctx),
                                //     child: const Text('Ok'),
                                //   ),
                                // ],
                              ),
                        );
                      }
                      setState(() {
                        _isManualAddressEntry = val == true;
                        if (_isManualAddressEntry) {
                          campAddressTextField.text = '';
                          _campLatitude = 0;
                          _campLongitude = 0;
                        }
                      });
                    },
                  ),
                  Text(
                    'Enter Address Manually',
                    style: TextStyle(fontSize: 12.sp),
                  ),
                ],
              ),
              AppTextField(
                controller: campAddressTextField,
                readOnly: !_isManualAddressEntry,
                hint: 'Camp Address',
                maxLines: 4,
                minLines: 1,
                label: CommonText(
                  text: 'Camp Address',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                fieldRadius: 10,
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icMapPin,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
              ).paddingOnly(top: 4),

              // AppIconTextfield(
              //   icon: icMapPin,
              //   titleHeaderString: "Camp Address",
              //   controller: campAddressTextField,
              // ),
              // const SizedBox(height: 8),
              Container(
                width: MediaQuery.of(context).size.width,
                color: Colors.transparent,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Expanded(
                      child: AppTextField(
                        onTap: () {
                          _selectCampDate(context);
                        },
                        controller: TextEditingController(
                          text: _selectedCampDate,
                        ),
                        readOnly: true,
                        hint: 'Camp Date',
                        label: CommonText(
                          text: 'Camp Date',
                          fontSize: 12.sp,
                          fontWeight: FontWeight.normal,
                          textColor: kBlackColor,
                          textAlign: TextAlign.start,
                        ),
                        hintStyle: TextStyle(
                          fontSize: 12.sp,
                          fontWeight: FontWeight.w400,
                          fontFamily: FontConstants.interFonts,
                        ),
                        fieldRadius: 10,
                        prefixIcon: SizedBox(
                          height: 20.h,
                          width: 20.w,
                          child: Center(
                            child: Image.asset(
                              icCalendarMonth,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ).paddingOnly(top: 12, left: 12),

                      // AppDateTextfield(
                      //   icon: icCalendarMonth,
                      //   titleHeaderString: "Camp Date",
                      //   valueString: _selectedCampDate ?? "",
                      //   onTap: () {
                      //     _selectCampDate(context);
                      //   },
                      // ),
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: AppTextField(
                        controller: TextEditingController(
                          text: _selectedPostCampDate,
                        ),
                        readOnly: true,
                        hint: 'Post Camp Date',
                        label: CommonText(
                          text: 'Post Camp Date',
                          fontSize: 12.sp,
                          fontWeight: FontWeight.normal,
                          textColor: kBlackColor,
                          textAlign: TextAlign.start,
                        ),
                        hintStyle: TextStyle(
                          fontSize: 12.sp,
                          fontWeight: FontWeight.w400,
                          fontFamily: FontConstants.interFonts,
                        ),
                        fieldRadius: 10,
                        prefixIcon: SizedBox(
                          height: 20.h,
                          width: 20.w,
                          child: Center(
                            child: Image.asset(
                              icCalendarMonth,
                              height: 24.h,
                              width: 24.w,
                              fit: BoxFit.contain,
                            ),
                          ),
                        ),
                      ).paddingOnly(top: 12, right: 12),

                      // AppDateTextfield(
                      //   icon: icCalendarMonth,
                      //   titleHeaderString: "Post Camp Date",
                      //   valueString: _selectedPostCampDate ?? "",
                      //   onTap: () {},
                      // ),
                    ),
                  ],
                ),
              ),
              // const SizedBox(height: 8),
              // AppDropdownTextfield(
              //   icon: icScreeningTests,
              //   titleHeaderString: "Screening Tests",
              //   valueString: selectedScreeningTestSting(),
              //   onTap: () {
              //     getScreeningTestAPI();
              //   },
              // ),
              AppTextField(
                controller: TextEditingController(
                  text: selectedScreeningTestSting(),
                ),
                readOnly: true,
                onTap: () {
                  getScreeningTestAPI();
                },
                hint: 'Screening Tests',
                label: CommonText(
                  text: 'Screening Tests',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                fieldRadius: 10,
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icScreeningTests,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // const SizedBox(height: 8),
              // AppDropdownTextfield(
              //   icon: icLandingLab,
              //   titleHeaderString: "Home Lab",
              //   valueString: selectedHomeAndHubLab?.homeLab ?? "",
              //   isDisabled: true,
              //   onTap: () {},
              // ),
              AppTextField(
                controller: TextEditingController(
                  text: selectedHomeAndHubLab?.homeLab ?? '',
                ),
                readOnly: true,

                hint: 'Home Lab',
                label: CommonText(
                  text: 'Home Lab',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                fieldRadius: 10,
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icLandingLab,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                // suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),
              // const SizedBox(height: 8),
              // AppDropdownTextfield(
              //   icon: icLandingLab,
              //   titleHeaderString: "Hub Lab",
              //   valueString: selectedHomeAndHubLab?.hubLab ?? "",
              //   isDisabled: true,
              //   onTap: () {},
              // ),
              AppTextField(
                controller: TextEditingController(
                  text: selectedHomeAndHubLab?.hubLab ?? '',
                ),
                readOnly: true,

                hint: 'Hub Lab',
                label: CommonText(
                  text: 'Hub Lab',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                fieldRadius: 10,
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icLandingLab,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                // suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              // const SizedBox(height: 8),
              // AppIconTextfield(
              //   icon: icUsersGroup,
              //   titleHeaderString: "Expected Beneficiary",
              //   controller: expectedBeneficiaryTextField,
              //   textInputType: TextInputType.number,
              // ),
              AppTextField(
                textInputType: TextInputType.number,
                controller: expectedBeneficiaryTextField,
                readOnly: false,
                hint: 'Expected Beneficiary',
                label: CommonText(
                  text: 'Expected Beneficiary',
                  fontSize: 12.sp,
                  fontWeight: FontWeight.normal,
                  textColor: kBlackColor,
                  textAlign: TextAlign.start,
                ),
                hintStyle: TextStyle(
                  fontSize: 12.sp,
                  fontWeight: FontWeight.w400,
                  fontFamily: FontConstants.interFonts,
                ),
                fieldRadius: 10,
                prefixIcon: SizedBox(
                  height: 20.h,
                  width: 20.w,
                  child: Center(
                    child: Image.asset(
                      icUsersGroup,
                      height: 24.h,
                      width: 24.w,
                      fit: BoxFit.contain,
                    ),
                  ),
                ),
                // suffixIcon: Icon(Icons.keyboard_arrow_down),
              ).paddingOnly(top: 12),

              Row(
                children: [
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Cancel",
                      isCancel: true,
                      onTap: () {},
                    ),
                  ),
                  const SizedBox(width: 62),
                  Expanded(
                    child: AppActiveButton(
                      buttontitle: "Save",
                      onTap: () {
                        saveDidPressed();
                      },
                    ),
                  ),
                ],
              ).paddingSymmetric(horizontal: 12, vertical: 14),
            ],
          ),
        ),
      ),
    );
  }
}
