import 'dart:async';

import 'package:s2toperational/Modules/utilities/api_manager.dart';
import '../models/CampDetailsntApprovalResponse.dart';
import '../models/CampResourceAllocationResponse.dart';
import '../models/GetCampAssignUserResponse.dart';
import '../models/ResourceReMappingCampResponse.dart';
import '../models/UpdateSubResourceListResponse.dart';
import '../models/UserCampMappingStatusResponse.dart';

class ResourceReMappingRepository {
  final APIManager _api = APIManager();

  Future<ResourceReMappingCampResponse> fetchApprovedCampList(
    Map<String, String> params,
  ) {
    final c = Completer<ResourceReMappingCampResponse>();
    _api.getApprovedCampListDetailsForAppFlexiCampAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }

  Future<UserCampMappingStatusResponse> fetchCampMappingStatus(
    Map<String, String> params,
  ) {
    final c = Completer<UserCampMappingStatusResponse>();
    _api.getUserCampMappingAndAttendanceStatusAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }

  Future<GetCampAssignUserResponse> fetchCampAssignUserList(
    Map<String, String> params,
  ) {
    final c = Completer<GetCampAssignUserResponse>();
    _api.getCampAssignUserListAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }

  Future<CampDetailsntApprovalResponse> fetchCampDetails(
    Map<String, String> params,
  ) {
    final c = Completer<CampDetailsntApprovalResponse>();
    _api.getCampDetailsForAppForIntApprovalAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }

  Future<CampResourceAllocationResponse> fetchResourcesForApproval(
    Map<String, String> params,
  ) {
    final c = Completer<CampResourceAllocationResponse>();
    _api.getResourcesForApprovalAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }

  Future<UpdateSubResourceListResponse> fetchSubResourceList(
    Map<String, String> params,
  ) {
    final c = Completer<UpdateSubResourceListResponse>();
    _api.getApproveResourcelstForUpdateAPI(params, (res, err, ok) {
      ok ? c.complete(res) : c.completeError(err);
    });
    return c.future;
  }
}
