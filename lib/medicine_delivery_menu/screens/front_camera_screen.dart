// ignore_for_file: file_names

import 'dart:io';

import 'package:camera/camera.dart';
import 'package:flutter/material.dart';

/// A full-screen camera screen that opens the front camera by default.
/// User can switch to the back camera using the flip button.
/// Returns [File] on capture or [null] if the user presses back.
class FrontCameraScreen extends StatefulWidget {
  const FrontCameraScreen({super.key});

  @override
  State<FrontCameraScreen> createState() => _FrontCameraScreenState();
}

class _FrontCameraScreenState extends State<FrontCameraScreen> {
  List<CameraDescription> _cameras = [];
  CameraDescription? _frontCamera;
  CameraDescription? _backCamera;

  CameraController? _controller;
  bool _isInitialized = false;
  bool _isCapturing = false;

  @override
  void initState() {
    super.initState();
    _initCamera();
  }

  @override
  void dispose() {
    _controller?.dispose();
    super.dispose();
  }

  Future<void> _initCamera() async {
    _cameras = await availableCameras();
    if (_cameras.isEmpty) return;

    for (final cam in _cameras) {
      if (cam.lensDirection == CameraLensDirection.front) _frontCamera ??= cam;
      if (cam.lensDirection == CameraLensDirection.back) _backCamera ??= cam;
    }

    // Default: front camera; fallback to last in list if not found
    final target = _frontCamera ?? _cameras.last;
    await _startCamera(target);
  }

  Future<void> _startCamera(CameraDescription camera) async {
    await _controller?.dispose();
    setState(() => _isInitialized = false);

    _controller = CameraController(
      camera,
      ResolutionPreset.high,
      enableAudio: false,
    );

    try {
      await _controller!.initialize();
      if (mounted) setState(() => _isInitialized = true);
    } catch (e) {
      debugPrint('FrontCameraScreen init error: $e');
    }
  }

  Future<void> _flipCamera() async {
    if (_frontCamera == null || _backCamera == null) return;
    final current = _controller?.description;
    final target =
        current?.lensDirection == CameraLensDirection.front
            ? _backCamera!
            : _frontCamera!;
    await _startCamera(target);
  }

  Future<void> _capture() async {
    if (_controller == null ||
        !_controller!.value.isInitialized ||
        _isCapturing) {
      return;
    }
    setState(() => _isCapturing = true);
    try {
      final xFile = await _controller!.takePicture();
      if (mounted) Navigator.of(context).pop(File(xFile.path));
    } catch (e) {
      debugPrint('FrontCameraScreen capture error: $e');
      if (mounted) setState(() => _isCapturing = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final hasMultipleCameras = _frontCamera != null && _backCamera != null;

    return Scaffold(
      backgroundColor: Colors.black,
      body: Stack(
        children: [
          // Camera preview
          if (_isInitialized && _controller != null)
            Positioned.fill(child: CameraPreview(_controller!))
          else
            const Center(child: CircularProgressIndicator(color: Colors.white)),

          // Back button (top-left)
          Positioned(
            top: MediaQuery.of(context).padding.top + 8,
            left: 8,
            child: IconButton(
              icon: const Icon(Icons.arrow_back, color: Colors.white),
              onPressed: () => Navigator.of(context).pop(null),
            ),
          ),

          // Flip button (top-right), only shown when both cameras exist
          if (hasMultipleCameras)
            Positioned(
              top: MediaQuery.of(context).padding.top + 8,
              right: 8,
              child: IconButton(
                icon: const Icon(Icons.flip_camera_android,
                    color: Colors.white, size: 28),
                onPressed: _flipCamera,
              ),
            ),

          // Capture button (bottom-center)
          Positioned(
            bottom: 40,
            left: 0,
            right: 0,
            child: Center(
              child: GestureDetector(
                onTap: _isCapturing ? null : _capture,
                child: Container(
                  width: 72,
                  height: 72,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    border: Border.all(color: Colors.white, width: 4),
                    color: Colors.white24,
                  ),
                  child: _isCapturing
                      ? const Padding(
                          padding: EdgeInsets.all(16),
                          child: CircularProgressIndicator(
                              color: Colors.white, strokeWidth: 2),
                        )
                      : const Icon(Icons.camera_alt,
                          color: Colors.white, size: 32),
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
