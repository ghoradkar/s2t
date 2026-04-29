import 'dart:convert';

// ── Graph point ────────────────────────────────────────────────────────────────

class FlowVolumePoint {
  final double flow;
  final double volume;
  const FlowVolumePoint({required this.flow, required this.volume});
}

// ── Per-row measurement ────────────────────────────────────────────────────────

class TestMeasurement {
  final String name;
  final double value;
  final String unit;
  final String predicted;
  final String lln;
  final String uln;
  final String zScore;
  final double predictedPer;

  const TestMeasurement({
    required this.name,
    required this.value,
    required this.unit,
    required this.predicted,
    required this.lln,
    required this.uln,
    required this.zScore,
    required this.predictedPer,
  });

  static double _round2(double v) => (v * 100).round() / 100.0;
  static String _fmt2(double v) => v.toStringAsFixed(2);

  String get formattedValue {
    final u = unit.trim();
    return u.isEmpty ? _fmt2(value) : '${_fmt2(value)} $u';
  }

  bool get _hasPred {
    final t = predicted.trim();
    return t.isNotEmpty && t != '-' && t != ' -  ' && t != ' - ';
  }

  bool get _hasLln {
    final t = lln.trim();
    return t.isNotEmpty && t != '-' && t != ' -  ' && t != ' - ';
  }

  bool get _hasZScore {
    final t = zScore.trim();
    return t.isNotEmpty && t != '-' && t != ' -  ' && t != ' - ';
  }

  String get formattedPredPer => _hasPred ? '${_fmt2(predictedPer)}%' : ' - ';

  String get formattedLln {
    if (!_hasLln) return ' - ';
    final v = double.tryParse(lln.trim());
    return v != null ? _fmt2(_round2(v)) : lln.trim();
  }

  String get formattedZScore {
    if (!_hasZScore) return ' - ';
    final v = double.tryParse(zScore.trim());
    return v != null ? _fmt2(_round2(v)) : zScore.trim();
  }

  bool get predPerGreen => _hasPred && predictedPer >= 100;

  factory TestMeasurement.fromJson(Map<String, dynamic> m) {
    String s(List<String> keys) {
      for (final k in keys) {
        final v = m[k];
        if (v != null) return v.toString();
      }
      return ' - ';
    }

    return TestMeasurement(
      name: s(['measurement', 'name']),
      value: (m['measuredValue'] as num?)?.toDouble() ?? 0.0,
      unit: s(['unit']),
      predicted: s(['predicted', 'predictedValue']),
      lln: s(['LLN', 'lln', 'llN']),
      uln: s(['ULN', 'uln', 'ulN']),
      zScore: s(['zScore', 'zscore', 'z_score']),
      predictedPer: (m['predictedPer'] as num?)?.toDouble() ?? 0.0,
    );
  }
}

// ── Top-level result ───────────────────────────────────────────────────────────

class LungFunctionTestResult {
  final List<TestMeasurement> measurements;

  // All trials' graph points (index 0 = trial 0, etc.)
  final List<List<FlowVolumePoint>> trialGraphPoints;

  // Index of the "best" trial in trialGraphPoints (-1 if unknown)
  final int bestTrialIndex;

  // Individual values kept for API submission
  final double fvc;
  final double fev1;
  final double feviFvc;
  final double pef;
  final double fef2575;
  final double fivc;
  final double pif;
  final double fet;

  final String diagnosis;
  final String sessionScore;
  final String deviceId;

  const LungFunctionTestResult({
    this.measurements = const [],
    this.trialGraphPoints = const [],
    this.bestTrialIndex = -1,
    this.fvc = 0,
    this.fev1 = 0,
    this.feviFvc = 0,
    this.pef = 0,
    this.fef2575 = 0,
    this.fivc = 0,
    this.pif = 0,
    this.fet = 0,
    this.diagnosis = '',
    this.sessionScore = '',
    this.deviceId = '',
  });

  factory LungFunctionTestResult.fromSafeyJson(
    String jsonStr,
    String sessionScore,
    String deviceId,
  ) {
    try {
      final data = json.decode(jsonStr) as Map<String, dynamic>;

      // Debug top-level keys (helpful to verify clean JSON arrived)
      // ignore: avoid_print
      print('=== LFT JSON top-level keys: ${data.keys.toList()}');

      final testResults = data['testResults'] as List<dynamic>? ?? [];
      if (testResults.isEmpty) {
        return LungFunctionTestResult(
            sessionScore: sessionScore, deviceId: deviceId);
      }

      // Prefer isBest trial (non-post), fall back to first non-post, then first
      int bestIdx = -1;
      Map<String, dynamic>? bestTrial;

      for (int i = 0; i < testResults.length; i++) {
        final t = testResults[i];
        if (t is! Map) continue;
        final tm = Map<String, dynamic>.from(t);
        final isBest = tm['isBest'] == true;
        final isPost = tm['isPost'] == true;
        if (isBest && !isPost) {
          bestTrial = tm;
          bestIdx = i;
          break;
        }
      }
      if (bestTrial == null) {
        for (int i = 0; i < testResults.length; i++) {
          final t = testResults[i];
          if (t is! Map) continue;
          final tm = Map<String, dynamic>.from(t);
          if (tm['isPost'] != true) {
            bestTrial = tm;
            bestIdx = i;
            break;
          }
        }
      }
      bestTrial ??= Map<String, dynamic>.from(testResults.first as Map);

      // Parse measurements from the best trial
      final rawList = _findMeasurementsList(bestTrial);
      final measurements = rawList
          .whereType<Map>()
          .map((m) => TestMeasurement.fromJson(Map<String, dynamic>.from(m)))
          .toList();

      // Parse graph points for all trials
      final trialGraphPoints = <List<FlowVolumePoint>>[];
      for (final t in testResults) {
        if (t is! Map) {
          trialGraphPoints.add([]);
          continue;
        }
        final tm = Map<String, dynamic>.from(t);
        final gpList = tm['graphPoints'] as List<dynamic>? ?? [];
        final pts = gpList.whereType<Map>().map((p) {
          final pm = Map<String, dynamic>.from(p);
          return FlowVolumePoint(
            flow: (pm['flow'] as num?)?.toDouble() ?? 0,
            volume: (pm['volume'] as num?)?.toDouble() ?? 0,
          );
        }).toList();
        trialGraphPoints.add(pts);
      }

      // Values by position (matching native submitLFT indices)
      double byIndex(int i) =>
          i < measurements.length ? measurements[i].value : 0.0;

      double byName(String n) {
        for (final m in measurements) {
          if (m.name.toLowerCase() == n.toLowerCase()) return m.value;
        }
        return 0.0;
      }

      return LungFunctionTestResult(
        measurements: measurements,
        trialGraphPoints: trialGraphPoints,
        bestTrialIndex: bestIdx,
        fvc: byIndex(0),
        fev1: byIndex(1),
        pef: byIndex(2),
        feviFvc: byIndex(11),
        fef2575: byIndex(14),
        fivc: byName('FIVC'),
        pif: byName('PIF'),
        fet: byIndex(25),
        diagnosis: (data['suggestedDiagnosis'] as String? ?? '').trim(),
        sessionScore: sessionScore,
        deviceId: deviceId,
      );
    } catch (e) {
      // ignore: avoid_print
      print('LungFunctionTestResult.fromSafeyJson error: $e');
      return LungFunctionTestResult(
          sessionScore: sessionScore, deviceId: deviceId);
    }
  }

  static List<dynamic> _findMeasurementsList(Map<String, dynamic> trial) {
    const knownKeys = [
      'measuredValues',
      'mesurementlist',
      'measuredValueList',
      'measuredvaluelist',
      'measurementList',
      'measurements',
      'resultData',
      'results',
    ];
    for (final k in knownKeys) {
      final v = trial[k];
      if (v is List && v.isNotEmpty) return v;
    }
    for (final entry in trial.entries) {
      final v = entry.value;
      if (v is List && v.isNotEmpty && v.first is Map) {
        final first = v.first as Map;
        if (first.containsKey('measuredValue') ||
            first.containsKey('measurement')) {
          // ignore: avoid_print
          print('=== LFT: found measurements under key "${entry.key}"');
          return v;
        }
      }
    }
    return [];
  }

  String fmt(double v) => v == 0 ? '--' : v.toStringAsFixed(3);
}
