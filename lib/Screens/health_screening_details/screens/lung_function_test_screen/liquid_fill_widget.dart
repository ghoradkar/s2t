import 'dart:math' as math;

import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';

/// Liquid-fill animation.
/// - U-shaped container (square top, rounded bottom)
/// - Two animated sine waves
/// - Green target line at [targetProgress]
/// - Progress percentage + instruction text inside
class LiquidFillWidget extends StatefulWidget {
  final int progress;       // 0 – maxProgress
  final int maxProgress;    // default 150
  final int targetProgress; // green line threshold (default 100)
  final String bottomText;  // instruction shown inside the fill

  const LiquidFillWidget({
    super.key,
    required this.progress,
    this.maxProgress = 150,
    this.targetProgress = 100,
    this.bottomText = '',
  });

  @override
  State<LiquidFillWidget> createState() => _LiquidFillWidgetState();
}

class _LiquidFillWidgetState extends State<LiquidFillWidget>
    with SingleTickerProviderStateMixin {
  late final Ticker _ticker;
  double _shiftX = 0;
  int _lastMs = 0;

  @override
  void initState() {
    super.initState();
    _ticker = createTicker((elapsed) {
      final ms = elapsed.inMilliseconds;
      final dt = ms - _lastMs;
      _lastMs = ms;
      // Native: shiftX += 25 every 70 ms
      setState(() => _shiftX += 25.0 * dt / 70.0);
    })
      ..start();
  }

  @override
  void dispose() {
    _ticker.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return CustomPaint(
      painter: _LiquidPainter(
        progress: widget.progress,
        maxProgress: widget.maxProgress,
        targetProgress: widget.targetProgress,
        shiftX: _shiftX,
        bottomText: widget.bottomText,
      ),
    );
  }
}

class _LiquidPainter extends CustomPainter {
  final int progress;
  final int maxProgress;
  final int targetProgress;
  final double shiftX;
  final String bottomText;

  _LiquidPainter({
    required this.progress,
    required this.maxProgress,
    required this.targetProgress,
    required this.shiftX,
    required this.bottomText,
  });

  // ── Wave constants matching native defaults ──────────────────────────────
  static const int _strong = 70;
  static const int _waveOffset = 10;
  // Native behind-wave colour: #443030d5 (ARGB), front: slightly more opaque
  static const Color _behindColor = Color(0x443030D5);
  static const Color _frontColor  = Color(0x883030D5);
  static const Color _borderColor = Colors.black;
  static const Color _targetColor = Color(0xFF4CAF50); // green

  @override
  void paint(Canvas canvas, Size size) {
    final w = size.width;
    final h = size.height;
    final viewSize = math.min(w, h);

    // ── U-shape clip path (square top, fully-rounded bottom) ──────────────
    final bottomR = w / 2;
    final uPath = Path()
      ..moveTo(0, 0)
      ..lineTo(w, 0)
      ..lineTo(w, h - bottomR)
      ..arcToPoint(Offset(0, h - bottomR),
          radius: Radius.circular(bottomR), clockwise: true)
      ..close();

    // ── White background inside shape ─────────────────────────────────────
    canvas.drawPath(
        uPath, Paint()..color = const Color(0xFFF0F4FF)); // very light blue-white

    canvas.save();
    canvas.clipPath(uPath);

    // ── Wave geometry (mirrors native createShader logic) ─────────────────
    final clampedP = progress.clamp(0, maxProgress);
    final levelRatio = (maxProgress - clampedP) / maxProgress;
    final level = levelRatio * viewSize + (h / 2 - viewSize / 2);

    final waveW     = 2 * math.pi / viewSize;
    final waveLevel = _strong * (viewSize / 20) / 150;
    final zzz       = viewSize * ((_waveOffset - 50) / 150.0) / (viewSize / 6.25);
    final shiftX2   = shiftX + zzz;

    final behindPaint = Paint()
      ..color = _behindColor
      ..strokeWidth = 2;
    final frontPaint = Paint()
      ..color = _frontColor
      ..strokeWidth = 2;

    for (int x = 0; x <= w.toInt(); x++) {
      final xd = x.toDouble();
      final y1 = waveLevel * math.sin(waveW * x + shiftX) + level;
      canvas.drawLine(Offset(xd, y1), Offset(xd, h), behindPaint);
      final y2 = waveLevel * math.sin(waveW * x + shiftX2) + level;
      canvas.drawLine(Offset(xd, y2), Offset(xd, h), frontPaint);
    }

    // ── Green target line ─────────────────────────────────────────────────
    final targetRatio  = (maxProgress - targetProgress) / maxProgress;
    final targetY = targetRatio * viewSize + (h / 2 - viewSize / 2);
    canvas.drawLine(
      Offset(0, targetY),
      Offset(w, targetY),
      Paint()
        ..color = _targetColor
        ..strokeWidth = 2.5
        ..style = PaintingStyle.stroke,
    );

    canvas.restore();

    // ── Border ────────────────────────────────────────────────────────────
    canvas.drawPath(
      uPath,
      Paint()
        ..color = _borderColor
        ..strokeWidth = 4
        ..style = PaintingStyle.stroke,
    );

    // ── Percentage text (centre) ──────────────────────────────────────────
    final pct = (clampedP * 150 / maxProgress).toStringAsFixed(1);
    _drawText(
      canvas,
      '$pct%',
      TextStyle(
        color: clampedP >= targetProgress
            ? _targetColor
            : const Color(0xFF1A237E),
        fontSize: 28,
        fontWeight: FontWeight.bold,
      ),
      size,
      Offset(0, -h * 0.07),
    );

    // ── Bottom instruction text ───────────────────────────────────────────
    if (bottomText.isNotEmpty) {
      _drawText(
        canvas,
        bottomText,
        const TextStyle(
          color: Color(0xFF1A237E),
          fontSize: 13,
          fontWeight: FontWeight.w600,
        ),
        size,
        Offset(0, h * 0.10),
      );
    }
  }

  void _drawText(
      Canvas canvas, String text, TextStyle style, Size size, Offset offset) {
    final tp = TextPainter(
      text: TextSpan(text: text, style: style),
      textDirection: TextDirection.ltr,
      textAlign: TextAlign.center,
    )..layout(maxWidth: size.width);
    tp.paint(
      canvas,
      Offset(
        (size.width - tp.width) / 2 + offset.dx,
        (size.height - tp.height) / 2 + offset.dy,
      ),
    );
  }

  @override
  bool shouldRepaint(_LiquidPainter old) =>
      old.shiftX != shiftX ||
      old.progress != progress ||
      old.bottomText != bottomText;
}
