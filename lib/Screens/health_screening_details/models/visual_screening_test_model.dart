class VisualOption {
  final String id;
  final String name;
  const VisualOption(this.id, this.name);
}

class VisualScreeningData {
  // Visually impaired
  String blindnessId;
  String blindnessName;

  // Injury / disease
  String injuryRightId;
  String injuryRightName;
  String injuryLeftId;
  String injuryLeftName;

  // Snellen chart
  String snellenRight;
  String snellenLeft;

  // Snellen remarks (auto-populated)
  String snellenRightRemark;
  String snellenLeftRemark;

  // Jaegar (near vision)
  String jaegarRight;
  String jaegarLeft;

  // Overall remarks (auto-populated)
  String rightRemark;
  String leftRemark;
  String nearRemark;

  // Spectacles
  bool wearsGlasses;

  VisualScreeningData()
      : blindnessId = '',
        blindnessName = '',
        injuryRightId = '',
        injuryRightName = '',
        injuryLeftId = '',
        injuryLeftName = '',
        snellenRight = '',
        snellenLeft = '',
        snellenRightRemark = '',
        snellenLeftRemark = '',
        jaegarRight = '',
        jaegarLeft = '',
        rightRemark = '',
        leftRemark = '',
        nearRemark = '',
        wearsGlasses = true;

  // ── Static option lists ──────────────────────────────────────────────────

  static const List<VisualOption> blindnessOptions = [
    VisualOption('1', 'Right Eye'),
    VisualOption('2', 'Left Eye'),
    VisualOption('3', 'Both Eye'),
    VisualOption('4', 'Not Blind'),
  ];

  static const List<VisualOption> injuryOptions = [
    VisualOption('1', 'Normal'),
    VisualOption('7', 'Any Injury'),
    VisualOption('8', 'Any Foreign Body'),
    VisualOption('9', 'Cataract'),
    VisualOption('9', 'Any Operation'),
    VisualOption('10', 'Other'),
  ];

  static const List<VisualOption> snellenOptions = [
    VisualOption('1', '6/6'),
    VisualOption('2', '6/9'),
    VisualOption('3', '6/12'),
    VisualOption('4', '6/18'),
    VisualOption('5', '6/24'),
    VisualOption('6', '6/36'),
    VisualOption('7', '6/60'),
  ];

  static const List<VisualOption> jaegarOptions = [
    VisualOption('1', 'N5'),
    VisualOption('2', 'N6'),
    VisualOption('3', 'N8'),
    VisualOption('4', 'N10'),
    VisualOption('5', 'N12'),
    VisualOption('6', 'N18'),
  ];

  // ── Auto-logic helpers ───────────────────────────────────────────────────

  static String snellenRemark(String value) {
    const normal = {'6/6', '6/9', '6/12', '6/18', '6/24', '6/36'};
    return normal.contains(value.trim())
        ? 'Normal Vision'
        : 'To be referred to ophthalmologist';
  }

  static String jaegarRemark(String value) {
    const normal = {'N5', 'N6', 'N8'};
    return normal.contains(value.trim())
        ? 'Normal Vision'
        : 'To be referred to ophthalmologist';
  }

  // ── Blindness selection side-effects (mirrors native logic) ─────────────

  void applyBlindness(String id, String name) {
    blindnessId = id;
    blindnessName = name;

    if (id == '1') {
      // Right eye blind
      injuryRightId = '11';
      injuryRightName = 'Right Eye Blind';
      snellenRight = 'NA';
      jaegarRight = 'NA';
      snellenRightRemark = 'Right Eye Blind';
      rightRemark = 'Right Eye Blind';
      nearRemark = 'Right Eye Blind';
      // clear left
      injuryLeftId = '';
      injuryLeftName = '';
      snellenLeft = '';
      jaegarLeft = '';
      snellenLeftRemark = '';
      leftRemark = '';
    } else if (id == '2') {
      // Left eye blind
      injuryLeftId = '11';
      injuryLeftName = 'Left Eye Blind';
      snellenLeft = 'NA';
      jaegarLeft = 'NA';
      snellenLeftRemark = 'Left Eye Blind';
      leftRemark = 'Left Eye Blind';
      nearRemark = 'Left Eye Blind';
      // clear right
      injuryRightId = '';
      injuryRightName = '';
      snellenRight = '';
      jaegarRight = '';
      snellenRightRemark = '';
      rightRemark = '';
    } else if (id == '3') {
      // Both eyes blind
      injuryRightId = '11';
      injuryRightName = 'Right Eye Blind';
      injuryLeftId = '11';
      injuryLeftName = 'Left Eye Blind';
      snellenRight = 'NA';
      snellenLeft = 'NA';
      jaegarRight = 'NA';
      jaegarLeft = 'NA';
      snellenRightRemark = 'Right Eye Blind';
      snellenLeftRemark = 'Left Eye Blind';
      rightRemark = 'Right Eye Blind';
      leftRemark = 'Left Eye Blind';
      nearRemark = 'Both Eye Blind';
      wearsGlasses = false;
    } else {
      // Not blind or reset — clear everything
      injuryRightId = '';
      injuryRightName = '';
      injuryLeftId = '';
      injuryLeftName = '';
      snellenRight = '';
      snellenLeft = '';
      jaegarRight = '';
      jaegarLeft = '';
      snellenRightRemark = '';
      snellenLeftRemark = '';
      rightRemark = '';
      leftRemark = '';
      nearRemark = '';
      wearsGlasses = true;
    }
  }

  bool get rightBlind => blindnessId == '1' || blindnessId == '3';
  bool get leftBlind => blindnessId == '2' || blindnessId == '3';
}
