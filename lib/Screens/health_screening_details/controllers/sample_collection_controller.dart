import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:simple_barcode_scanner/simple_barcode_scanner.dart';
import 'package:s2toperational/Modules/Json_Class/DishaResponse/DishaResponse.dart';
import 'package:s2toperational/Modules/Json_Class/UserAttendancesUsingSitedetailsIDResponse/UserAttendancesUsingSitedetailsIDResponse.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';
import 'package:s2toperational/Modules/utilities/DataProvider.dart';
import 'package:s2toperational/Screens/health_screening_details/repository/health_screening_repository.dart';

class SampleCollectionController extends GetxController {
  final UserAttendancesUsingSitedetailsIDOutput patientItem;
  final int campId;

  SampleCollectionController({
    required this.patientItem,
    required this.campId,
  });

  final HealthScreeningRepository _repo = HealthScreeningRepository();

  final TextEditingController barcode1Ctrl = TextEditingController();
  final TextEditingController dateCtrl = TextEditingController();
  final TextEditingController timeCtrl = TextEditingController();

  final RxBool toDishaLIS = false.obs;
  final RxBool isScannedByScanner = false.obs;
  final RxBool isSubmitting = false.obs;

  late final DateTime _today;

  String? _dishaToken;
  String? _dishaCustomerId;
  List<DishaTestDetail> _dishaTests = [];

  @override
  void onInit() {
    super.onInit();
    _today = DateTime.now();

    if ((patientItem.antiBarcode ?? '').isNotEmpty) {
      barcode1Ctrl.text = patientItem.antiBarcode!;
    }

    dateCtrl.text = DateFormat('dd/MM/yyyy').format(_today);
    timeCtrl.text = DateFormat('HH:mm').format(_today);

    _initDishaData();
  }

  Future<void> _initDishaData() async {
    final results = await Future.wait([
      _repo.dishaLogin(),
      Future.value(null),
    ]);
    final token = results[0] as String?;
    if (token == null) return;
    _dishaToken = token;

    final testsResponse = await _repo.dishaGetTests(token);
    if (testsResponse != null) {
      _dishaCustomerId = testsResponse.customerId;
      _dishaTests = testsResponse.tests;
    }
  }

  bool get barcode1Locked => (patientItem.antiBarcode ?? '').isNotEmpty;

  @override
  void onClose() {
    barcode1Ctrl.dispose();
    dateCtrl.dispose();
    timeCtrl.dispose();
    super.onClose();
  }

  Future<void> pickDate(BuildContext context) async {
    final min = _today.subtract(const Duration(days: 2));
    final picked = await showDatePicker(
      context: context,
      initialDate: _today,
      firstDate: min,
      lastDate: _today,
    );
    if (picked != null) {
      dateCtrl.text = DateFormat('dd/MM/yyyy').format(picked);
      timeCtrl.text = '';
    }
  }

  Future<void> pickTime(BuildContext context) async {
    final now = TimeOfDay.now();
    final picked = await showTimePicker(
      context: context,
      initialTime: now,
      builder: (ctx, child) => MediaQuery(
        data: MediaQuery.of(ctx).copyWith(alwaysUse24HourFormat: true),
        child: child!,
      ),
    );
    if (picked == null) return;

    final selectedDateStr = dateCtrl.text;
    final todayStr = DateFormat('dd/MM/yyyy').format(_today);
    if (selectedDateStr == todayStr) {
      if (picked.hour > now.hour ||
          (picked.hour == now.hour && picked.minute > now.minute)) {
        ToastManager.toast('You cannot select a future time.');
        return;
      }
    }

    timeCtrl.text =
        '${picked.hour.toString().padLeft(2, '0')}:${picked.minute.toString().padLeft(2, '0')}';
  }

  Future<void> scanBarcode(BuildContext context) async {
    final res = await SimpleBarcodeScanner.scanBarcode(
      context,
      barcodeAppBar: const BarcodeAppBar(
        appBarTitle: 'Scan Barcode',
        centerTitle: false,
        enableBackButton: true,
        backButtonIcon: Icon(Icons.arrow_back_ios),
      ),
      isShowFlashIcon: true,
      delayMillis: 2000,
      cameraFace: CameraFace.back,
    );
    if (res == null || res == '-1') return;
    barcode1Ctrl.text = res;
    isScannedByScanner.value = true;
  }

  bool _isBarCodeValid(String barcode) => barcode.isNotEmpty;

  Future<void> validateAndSubmit(BuildContext context) async {
    final b1 = barcode1Ctrl.text.trim();
    final time = timeCtrl.text.trim();

    if (time.isEmpty) {
      ToastManager.toast('Please enter sample collection time.');
      return;
    }
    if (!_isBarCodeValid(b1)) {
      ToastManager.toast('Please enter barcode.');
      return;
    }

    final userId =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    final formattedDate = _convertDate(dateCtrl.text);

    isSubmitting.value = true;
    ToastManager.showLoader();

    if (toDishaLIS.value) {
      await _submitToDisha(b1, time);
      ToastManager.hideLoader();
      isSubmitting.value = false;
      return;
    }

    final result = await _repo.submitSampleCollection(
      regdId: patientItem.regdId ?? 0,
      siteId: patientItem.siteId ?? 0,
      campId: campId,
      sampleCount: '3',
      barcode1: b1,
      barcode2: b1,
      createdBy: userId,
      sampleDate: formattedDate,
      sampleTime: time,
      specTypeId: '',
      versionNo: '1.0',
      isScannedBy: isScannedByScanner.value ? '2' : '1',
    );

    ToastManager.hideLoader();
    isSubmitting.value = false;

    if (result == null) {
      ToastManager.toast('Server not responding. Please try again.');
      return;
    }

    final status = (result['status'] ?? '').toString();
    final message = (result['message'] ?? '').toString();

    if (status.toLowerCase() == 'success') {
      if (!context.mounted) return;
      showDialog(
        context: context,
        barrierDismissible: false,
        builder: (ctx) => AlertDialog(
          title: const Text('Success'),
          content:
              const Text('Sample collection details submitted successfully.'),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(ctx);
                Navigator.pop(context);
              },
              child: const Text('OK'),
            ),
          ],
        ),
      );
    } else {
      ToastManager.toast(message.isNotEmpty ? message : 'Submission failed.');
    }
  }

  Future<void> _submitToDisha(String barcode, String time) async {
    if (_dishaToken == null) {
      await _initDishaData();
    }
    if (_dishaToken == null) {
      ToastManager.toast('Disha LIS service unavailable.');
      return;
    }
    final context = Get.context;

    final p = patientItem;
    final nameParts = (p.englishName ?? '').trim().split(RegExp(r'\s+'));
    final fname = nameParts.isNotEmpty ? nameParts.first : '';
    final lname = nameParts.length > 1 ? nameParts.last : '';
    final mname = nameParts.length > 2
        ? nameParts.sublist(1, nameParts.length - 1).join(' ')
        : '';

    final email = '${fname.toLowerCase()}@gmail.com';
    final title = (p.gender ?? '').toUpperCase() == 'M'
        ? 'Mr'
        : (p.gender ?? '').toUpperCase() == 'F'
            ? 'Ms'
            : 'Mx';

    final ageParts = _calculateAge(p.dOB);

    final listTestDetails = _dishaTests
        .map((t) => {
              'subServiceId': t.subServiceId,
              'amount': 0,
              'subServiceName': t.subServiceName,
              'sampleTypeId': t.sampleTypeId,
              'barCode': barcode,
              'quantity': 1,
              'templateWise': t.templateWise,
            })
        .toList();

    final body = {
      'patientId': 0,
      'title': title,
      'fname': fname,
      'mname': mname,
      'lname': lname,
      'gender': p.gender ?? '',
      'mobile': p.mobileNo ?? '',
      'ageYears': ageParts[0],
      'ageMonth': ageParts[1],
      'ageDay': ageParts[2],
      'address': p.localAddress ?? p.permanentAddress ?? '',
      'email': email,
      'unitId': '38',
      'createdBy':
          DataProvider().getParsedUserData()?.output?.first.empCode?.toString() ?? '0',
      'weight': p.weightKGs ?? 0.0,
      'height': p.heightCMs ?? 0.0,
      'collectedDate': dateCtrl.text,
      'collectedTime': time,
      'customerId': _dishaCustomerId ?? '',
      'visitCode': (p.regdId ?? 0).toString(),
      'campId': campId.toString(),
      'totalAmount': '0',
      'listTestDetails': listTestDetails,
    };

    final dishaResult = await _repo.dishaRegisterPatient(
      token: _dishaToken!,
      body: body,
    );

    if (dishaResult == null) {
      ToastManager.toast('Disha LIS server not responding. Please try again.');
      return;
    }

    final msg = (dishaResult['message'] ?? '').toString();
    final status = (dishaResult['status'] ?? '').toString();
    final success = status.toLowerCase() == 'success';

    if (success) {
      if (context != null && context.mounted) {
        showDialog(
          context: context,
          barrierDismissible: false,
          builder: (ctx) => AlertDialog(
            title: const Text('Success'),
            content:
                const Text('Sample collection details submitted successfully.'),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.pop(ctx);
                  Navigator.pop(context);
                },
                child: const Text('OK'),
              ),
            ],
          ),
        );
      }
    } else {
      ToastManager.toast(msg.isNotEmpty ? msg : 'Disha LIS submission failed.');
    }
  }

  List<String> _calculateAge(String? dob) {
    if (dob == null || dob.isEmpty) return ['0', '0', '0'];
    try {
      final dobDate = DateFormat('dd/MM/yyyy').parse(dob);
      final today = DateTime.now();
      int years = today.year - dobDate.year;
      int months = today.month - dobDate.month;
      int days = today.day - dobDate.day;
      if (days < 0) {
        months--;
        days += DateTime(today.year, today.month, 0).day;
      }
      if (months < 0) {
        years--;
        months += 12;
      }
      return [years.toString(), months.toString(), days.toString()];
    } catch (_) {
      return ['0', '0', '0'];
    }
  }

  String _convertDate(String ddMMyyyy) {
    try {
      final parts = ddMMyyyy.split('/');
      if (parts.length == 3) return '${parts[2]}-${parts[1]}-${parts[0]}';
    } catch (_) {}
    return ddMMyyyy;
  }
}
