import 'package:flutter/material.dart';

class BackgroundPainter extends CustomPainter {
  @override
  void paint(Canvas canvas, Size size) {
    final Rect rectTop = Rect.fromLTRB(0.0, 0.0, size.width, size.height * 0.5);
    final Rect rectBottom = Rect.fromLTRB(0.0, size.height * 0.5, size.width, size.height);

    final Gradient gradient = const LinearGradient(
      colors: [Color(0xFF00B4DB), Color(0xFF0083B0)],
      stops: [0.0, 1.0],
      begin: Alignment.topCenter,
      end: Alignment.bottomCenter,
    );

    final Paint paint = Paint()..shader = gradient.createShader(rectTop);
    final Path pathTop = Path();

    pathTop.moveTo(0, size.height * 0.3);
    pathTop.quadraticBezierTo(size.width * 0.5, size.height * 0.4, size.width, size.height * 0.3);
    pathTop.lineTo(size.width, 0);
    pathTop.lineTo(0, 0);
    pathTop.close();

    final Paint paintBottom = Paint()..shader = gradient.createShader(rectBottom);
    final Path pathBottom = Path();

    pathBottom.moveTo(0, size.height);
    pathBottom.quadraticBezierTo(size.width * 0.5, size.height * 0.8, size.width, size.height);
    pathBottom.lineTo(size.width, size.height);
    pathBottom.lineTo(0, size.height);
    pathBottom.close();

    canvas.drawPath(pathTop, paint);
    canvas.drawPath(pathBottom, paintBottom);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return false;
  }
}
