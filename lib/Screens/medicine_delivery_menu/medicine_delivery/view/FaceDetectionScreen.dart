// ignore_for_file: file_names

import 'dart:io';
import 'dart:typed_data';

import 'package:camera/camera.dart';
import 'package:flutter/material.dart';
import 'package:google_mlkit_face_detection/google_mlkit_face_detection.dart';
import 'package:s2toperational/Modules/ToastManager/ToastManager.dart';

/// Face detection screen that replicates the native FaceDetectionActivity.
///
/// Flow:
///   1. Screen opens → show instruction dialog.
///   2. User taps "Start Analysis" → ML Kit begins processing each camera frame.
///   3. Once a blink is detected (eye closed then opened) → "Capture" button
///      appears at the bottom center.
///   4. User taps "Capture" → takes picture and pops with [File].
///
/// Returns: [File] on success, [null] if user presses back.
class FaceDetectionScreen extends StatefulWidget {
  const FaceDetectionScreen({super.key});

  @override
  State<FaceDetectionScreen> createState() => _FaceDetectionScreenState();
}

class _FaceDetectionScreenState extends State<FaceDetectionScreen> {
  // ── Camera ──────────────────────────────────────────────────────────────────
  List<CameraDescription>? _cameras;
  CameraController? _cameraController;
  bool _isInitialized = false;
  bool _isStreaming = false;

  // ── Analysis state ──────────────────────────────────────────────────────────
  bool _isStarted = false;
  int _eyeClosed = 0;
  int _eyeOpened = 0;
  bool _blinkDetected = false;
  bool _isProcessingFrame = false;

  // Consecutive multi-face frames needed before showing the warning dialog.
  // A single bad frame (shadow, reflection) is ignored.
  int _multiFaceCount = 0;
  static const int _multiFaceThreshold = 8;

  // ── ML Kit ──────────────────────────────────────────────────────────────────
  final FaceDetector _faceDetector = FaceDetector(
    options: FaceDetectorOptions(
      performanceMode: FaceDetectorMode.accurate,
      enableClassification: true, // required for eyeOpenProbability
    ),
  );

  // ── Lifecycle ────────────────────────────────────────────────────────────────

  @override
  void initState() {
    super.initState();
    _initCamera();

    WidgetsBinding.instance.addPostFrameCallback(
      (_) => _showInstructionDialog(),
    );
  }

  @override
  void dispose() {
    _stopStream();
    _cameraController?.dispose();
    _faceDetector.close();
    super.dispose();
  }

  // ── Camera init ──────────────────────────────────────────────────────────────

  CameraDescription? _frontCamera;
  CameraDescription? _backCamera;

  Future<void> _initCamera() async {
    _cameras = await availableCameras();
    if (_cameras == null || _cameras!.isEmpty) return;

    for (final cam in _cameras!) {
      if (cam.lensDirection == CameraLensDirection.front) _frontCamera ??= cam;
      if (cam.lensDirection == CameraLensDirection.back) _backCamera ??= cam;
    }

    // Always start with front camera for face capture.
    final target = _frontCamera ?? _cameras!.last;

    await _startCamera(target);
  }

  Future<void> _startCamera(CameraDescription camera) async {
    await _cameraController?.dispose();
    _cameraController = CameraController(
      camera,
      ResolutionPreset.high,
      enableAudio: false,
    );

    try {
      await _cameraController!.initialize();
      if (mounted) setState(() => _isInitialized = true);
    } catch (e) {
      debugPrint('Camera init error: $e');
    }
  }

  // ── Dialogs ──────────────────────────────────────────────────────────────────

  void _showInstructionDialog() {
    ToastManager.showAlertDialog(
      context,
      "Click on Start Analysis to start\n\nYou need to blink both eye to enable capture",
      () {
        Navigator.pop(context);
      },
    );

    // showDialog(
    //   context: context,
    //   builder: (_) => AlertDialog(
    //     content: const Text(
    //       'Click on Start Analysis to start\n\nYou need to blink both eye to enable capture',
    //     ),
    //     actions: [
    //       TextButton(
    //         onPressed: () => Navigator.pop(context),
    //         child: const Text('OK'),
    //       ),
    //     ],
    //   ),
    // );
  }

  void _showMultiFaceDialog() {
    showDialog(
      context: context,
      barrierDismissible: false,
      builder:
          (_) => AlertDialog(
            content: const Text(
              'Detecting more than 1 face,\nneed only one face of particular worker in frame which you want to capture image',
            ),
            actions: [
              TextButton(
                onPressed: () => Navigator.pop(context),
                child: const Text('OK'),
              ),
            ],
          ),
    );
  }

  // ── Analysis control ─────────────────────────────────────────────────────────

  void _startAnalysis() {
    if (_cameraController == null || !_cameraController!.value.isInitialized)
      return;

    setState(() {
      _isStarted = true;
      _eyeClosed = 0;
      _eyeOpened = 0;
      _blinkDetected = false;
      _multiFaceCount = 0;
    });

    _cameraController!.startImageStream(_processFrame);
    setState(() => _isStreaming = true);
  }

  void _stopAnalysis() {
    _stopStream();
    setState(() {
      _isStarted = false;
      _blinkDetected = false;
    });
  }

  void _stopStream() {
    if (_isStreaming) {
      try {
        _cameraController?.stopImageStream();
      } catch (_) {}
      _isStreaming = false;
    }
  }

  // ── Frame processing ─────────────────────────────────────────────────────────

  Future<void> _processFrame(CameraImage image) async {
    if (_isProcessingFrame || !_isStarted || !mounted) return;
    _isProcessingFrame = true;

    try {
      final rotation = _inputRotation(
        _cameraController!.description.sensorOrientation,
      );

      final allBytes = <int>[];
      for (final plane in image.planes) {
        allBytes.addAll(plane.bytes);
      }
      final bytes = Uint8List.fromList(allBytes);
      final format =
          Platform.isAndroid
              ? InputImageFormat.nv21
              : InputImageFormat.bgra8888;

      final inputImage = InputImage.fromBytes(
        bytes: bytes,
        metadata: InputImageMetadata(
          size: Size(image.width.toDouble(), image.height.toDouble()),
          rotation: rotation,
          format: format,
          bytesPerRow: image.planes[0].bytesPerRow,
        ),
      );

      final faces = await _faceDetector.processImage(inputImage);
      if (!mounted || !_isStarted) return;

      if (faces.length == 1) {
        // Good frame — reset the multi-face counter
        _multiFaceCount = 0;

        final face = faces[0];
        final leftEye = face.leftEyeOpenProbability ?? 1.0;
        final rightEye = face.rightEyeOpenProbability ?? 1.0;

        // Relaxed thresholds so a single natural blink is enough.
        // "Closed"  – either eye drops below 0.35 (native was 0.015, too strict)
        // "Opened"  – either eye rises above 0.6  (native was 0.75)
        if (leftEye < 0.35 || rightEye < 0.35) {
          _eyeClosed = 1;
        }
        if (_eyeClosed == 1 && (leftEye > 0.6 || rightEye > 0.6)) {
          _eyeOpened = 1;
        }

        final blink = _eyeClosed == 1 && _eyeOpened == 1;
        if (blink != _blinkDetected && mounted) {
          setState(() => _blinkDetected = blink);
        }
      } else if (faces.length > 1) {
        // Ignore occasional false-positive frames (shadows, reflections).
        // Only stop when multiple consecutive frames confirm >1 face.
        _multiFaceCount++;
        if (_multiFaceCount >= _multiFaceThreshold) {
          _multiFaceCount = 0;
          _stopStream();
          if (mounted) {
            setState(() {
              _isStarted = false;
              _blinkDetected = false;
            });
            _showMultiFaceDialog();
          }
        }
      } else {
        // Zero faces detected — reset multi-face counter
        _multiFaceCount = 0;
      }
    } catch (e) {
      debugPrint('Frame processing error: $e');
    } finally {
      _isProcessingFrame = false;
    }
  }

  InputImageRotation _inputRotation(int sensorOrientation) {
    switch (sensorOrientation) {
      case 90:
        return InputImageRotation.rotation90deg;
      case 180:
        return InputImageRotation.rotation180deg;
      case 270:
        return InputImageRotation.rotation270deg;
      default:
        return InputImageRotation.rotation0deg;
    }
  }

  // ── Capture ───────────────────────────────────────────────────────────────────

  Future<void> _capturePhoto() async {
    if (_cameraController == null || !_cameraController!.value.isInitialized)
      return;
    try {
      _stopStream();
      await Future.delayed(const Duration(milliseconds: 100));
      final xFile = await _cameraController!.takePicture();
      if (mounted) Navigator.of(context).pop(File(xFile.path));
    } catch (e) {
      debugPrint('Capture error: $e');
    }
  }

  // ── Flip camera ───────────────────────────────────────────────────────────────

  Future<void> _flipCamera() async {
    if (_frontCamera == null || _backCamera == null) return;
    if (_frontCamera == _backCamera) return; // only one camera

    // Toggle strictly between front and back; ignore other lenses.
    final isFront =
        _cameraController?.description.lensDirection ==
        CameraLensDirection.front;
    final target = isFront ? _backCamera! : _frontCamera!;
    _stopStream();
    setState(() {
      _isInitialized = false;
      _isStarted = false;
      _blinkDetected = false;
      _multiFaceCount = 0;
    });

    await _startCamera(target);
  }

  // ── UI ────────────────────────────────────────────────────────────────────────

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      body: Stack(
        children: [
          // Full-screen camera preview
          if (_isInitialized && _cameraController != null)
            Positioned.fill(child: CameraPreview(_cameraController!))
          else
            const Center(child: CircularProgressIndicator(color: Colors.white)),

          // Back button
          Positioned(
            top: MediaQuery.of(context).padding.top + 8,
            left: 8,
            child: IconButton(
              icon: const Icon(Icons.arrow_back, color: Colors.white),
              onPressed: () => Navigator.of(context).pop(null),
            ),
          ),

          // Bottom controls
          Positioned(
            bottom: 0,
            left: 0,
            right: 0,
            child: _buildBottomControls(),
          ),
        ],
      ),
    );
  }

  Widget _buildBottomControls() {
    return Container(
      padding: const EdgeInsets.fromLTRB(20, 20, 20, 40),
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.bottomCenter,
          end: Alignment.topCenter,
          colors: [Colors.black.withValues(alpha: 0.7), Colors.transparent],
        ),
      ),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          // Flip camera (left)
          FloatingActionButton(
            heroTag: 'fd_flip',
            onPressed: _flipCamera,
            backgroundColor: Colors.white24,
            child: const Icon(Icons.flip_camera_android, color: Colors.white),
          ),

          // Capture button (center, shown only on blink)
          if (_blinkDetected)
            Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                FloatingActionButton(
                  heroTag: 'fd_capture',
                  onPressed: _capturePhoto,
                  backgroundColor: Colors.white,
                  child: const Icon(Icons.camera_alt, color: Colors.black),
                ),
                const SizedBox(height: 4),
                const Text(
                  'Capture',
                  style: TextStyle(color: Colors.white, fontSize: 12),
                ),
              ],
            )
          else
            const SizedBox(width: 56),

          // Start / Stop Analysis (right)
          Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              FloatingActionButton(
                heroTag: 'fd_startstop',
                onPressed: _isStarted ? _stopAnalysis : _startAnalysis,
                backgroundColor: Colors.white24,
                mini: true,
                child: Icon(
                  _isStarted ? Icons.stop : Icons.play_arrow,
                  color: Colors.white,
                ),
              ),
              const SizedBox(height: 4),
              Text(
                _isStarted ? 'Stop\nAnalysis' : 'Start\nAnalysis',
                style: const TextStyle(color: Colors.white, fontSize: 11),
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ],
      ),
    );
  }
}
