import 'package:get/get.dart';
import 'package:s2toperational/utilities/formatter_manager.dart';
import 'package:s2toperational/utilities/toast_manager.dart';
import 'package:s2toperational/utilities/data_provider.dart';
import '../models/CampDetailsntApprovalResponse.dart';
import '../models/CampResourceAllocationResponse.dart';
import '../models/ResourceReMappingCampResponse.dart';
import '../models/UpdateSubResourceListResponse.dart';
import '../repository/resource_re_mapping_repository.dart';

class ResourceReMappingUpdateController extends GetxController {
  final ResourceReMappingCampOutput reMappingCampOutput;

  ResourceReMappingUpdateController({required this.reMappingCampOutput});

  final _repository = ResourceReMappingRepository();

  CampDetailsntApprovalOutput? campDetails;
  List<CampResourceAllocationOutput> resourceAllocationList = [];
  int empCode = 0;

  @override
  void onInit() {
    super.onInit();
    empCode =
        DataProvider().getParsedUserData()?.output?.first.empCode ?? 0;
    loadData();
  }

  Future<void> loadData() async {
    ToastManager.showLoader();
    try {
      await Future.wait([
        _fetchCampDetails(),
        _fetchResourcesForApproval(),
      ]);
    } finally {
      ToastManager.hideLoader();
    }
    update();
  }

  Future<void> _fetchCampDetails() async {
    try {
      final response = await _repository.fetchCampDetails({
        'campid': reMappingCampOutput.campId?.toString() ?? '0',
      });
      campDetails = response.output?.first;
    } catch (e) {
      ToastManager.toast(e.toString());
    }
  }

  Future<void> _fetchResourcesForApproval() async {
    try {
      final response = await _repository.fetchResourcesForApproval({
        'campid': reMappingCampOutput.campId?.toString() ?? '0',
      });
      resourceAllocationList = response.output ?? [];
    } catch (e) {
      resourceAllocationList = [];
      ToastManager.toast('Devices not available');
    }
  }

  Future<List<UpdateSubResourceOutput>> fetchSubResources(
    CampResourceAllocationOutput devicesOutput,
  ) async {
    ToastManager.showLoader();
    try {
      final response = await _repository.fetchSubResourceList({
        'TestId': devicesOutput.testId.toString(),
        'Campid': devicesOutput.campId.toString(),
        'Campdate': FormatterManager.campDateStringFromStringDate(
          reMappingCampOutput.campDate ?? '',
        ),
        'DISTLGDCODE': reMappingCampOutput.dISTLGDCODE?.toString() ?? '0',
        'PartnerID': '1',
        'LabCode': reMappingCampOutput.lABCODE?.toString() ?? '0',
      });
      final list = response.output ?? [];
      list.sort((a, b) {
        final nameA = a.resourceName?.trim() ?? '';
        final nameB = b.resourceName?.trim() ?? '';
        return nameA.toLowerCase().compareTo(nameB.toLowerCase());
      });
      return list;
    } catch (e) {
      ToastManager.toast(e.toString());
      return [];
    } finally {
      ToastManager.hideLoader();
    }
  }
}
