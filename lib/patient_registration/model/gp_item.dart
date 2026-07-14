// ignore_for_file: file_names

class GpItem {
  final String gpName;
  final String gpLgdCode;

  GpItem({required this.gpName, required this.gpLgdCode});

  factory GpItem.fromJson(Map<String, dynamic> json) => GpItem(
        gpName: json['GPNAME']?.toString() ?? '',
        gpLgdCode: json['GPLGDCODE']?.toString() ?? '',
      );
}
