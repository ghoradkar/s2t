

import '../Json_Class/ReportDeliveryExecutiveResponse/ReportDeliveryExecutiveResponse.dart';
import '../Json_Class/UserMappedTalukaResponse/UserMappedTalukaResponse.dart';

class AppDataManager {
  static String fromDate = "";
  static String toDate = "";
  static UserMappedTalukaOutput? selectedTaluka;
  static ReportDeliveryExecutiveOutput? selectedResource;
  // static AppDataManager? instance;

  // AppDataManager._internal();

  // factory AppDataManager() {
  //   if (instance == null) {
  //     instance = AppDataManager._internal();
  //   }
  //   return instance!;
  // }
}
