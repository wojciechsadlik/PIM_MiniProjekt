import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class CanvasPainter extends CustomPainter {
  final Paint paintStyle = Paint()
    ..color = Color(0xFF000000)
    ..isAntiAlias = true
    ..style = PaintingStyle.stroke
    ..strokeCap = StrokeCap.round
    ..strokeJoin = StrokeJoin.round
    ..strokeWidth = 5.0;

  List<Path> paths;

  CanvasPainter(this.paths);

  @override
  void paint(Canvas canvas, Size size) {
    for (Path path in paths) {
      canvas.drawPath(path, paintStyle);
    }
  }

  @override
  bool shouldRepaint(covariant CustomPainter oldDelegate) => true;
}

class CanvasWidget extends StatefulWidget {
  CanvasWidget({Key key}): super(key: key);

  @override
  CanvasWidgetState createState() => CanvasWidgetState();
}

class CanvasWidgetState extends State<CanvasWidget> {
  List<Path> paths = List<Path>();

  void clear() => paths.clear();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: GestureDetector(
        onPanStart: (DragStartDetails details) {
          setState(() {
            Path path = Path();
            path.moveTo(details.localPosition.dx, details.localPosition.dy);
            paths.add(path);
          });
        },
        onPanUpdate: (DragUpdateDetails details) {
          setState(() {
            double x1 = details.localPosition.dx;
            double y1 = details.localPosition.dy;
            double x2 = (x1 + x1 - details.delta.dx) / 2;
            double y2 = (y1 + y1 - details.delta.dy) / 2;
            paths.last.quadraticBezierTo(x1, y1, x2, y2);
          });
        },
        child: Container(
          constraints: BoxConstraints.expand(),
          color: Color(0xFFFFF3E6),
          child: CustomPaint(
            painter: CanvasPainter(paths),
          ),
        ),
      )
    );
  }
}