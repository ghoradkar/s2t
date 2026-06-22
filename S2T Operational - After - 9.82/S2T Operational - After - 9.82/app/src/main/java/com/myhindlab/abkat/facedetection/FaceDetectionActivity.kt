package com.myhindlab.abkat.facedetection

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.face.Face
import com.myhindlab.abkat.databinding.ActivityFaceDetectionBinding
import com.myhindlab.abkat.facedetection.camera.CameraAnalyzer
import com.myhindlab.abkat.facedetection.camera.CameraManager

class FaceDetectionActivity : AppCompatActivity(), CameraAnalyzer.CameraAnalyzerEvent {

    private lateinit var cameraManager: CameraManager
    private val binding by lazy { ActivityFaceDetectionBinding.inflate(layoutInflater) }
    private val TAG = FaceDetectionActivity::class.java.simpleName
    var initialBlink = 0
    var eyeClosed = 0
    var eyeOpened = 0
    var isStarted = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        cameraManager = CameraManager(
            this,
            binding.viewCameraPreview,
            binding.viewGraphicOverlay,
            this,
            this@FaceDetectionActivity, intent
        )
        askCameraPermission()
        buttonClicks()

        val builder = AlertDialog.Builder(this@FaceDetectionActivity)
        builder.setMessage("Click on Start Analysis to start\n\nYou need to blink both eye to enable capture")
        val alert = builder.create()
        alert.show()

    }


    private fun buttonClicks() {
        binding.buttonTurnCamera.setOnClickListener {
            cameraManager.changeCamera()
        }
        binding.buttonStopCamera.setOnClickListener {
            cameraManager.cameraStop()
            buttonVisibility(false)
            isStarted = false
        }
        binding.buttonStartCamera.setOnClickListener {
            cameraManager.cameraStart()
            buttonVisibility(true)
            isStarted = true
            eyeClosed = 0
            eyeOpened = 0
        }
        binding.llCapture.setOnClickListener {
//            cameraManager.cameraStart()
            Log.d(TAG, "buttonClicks: ")
            Toast.makeText(this@FaceDetectionActivity,"Capture",Toast.LENGTH_LONG).show();
            cameraManager.capturePhoto()
            buttonVisibility(true)
        }
    }

    private fun buttonVisibility(forStart: Boolean) {
        if (forStart) {
            binding.llStopCamera.visibility = View.VISIBLE
            binding.llStartCamera.visibility = View.INVISIBLE
        } else {
            binding.llStopCamera.visibility = View.INVISIBLE
            binding.llStartCamera.visibility = View.VISIBLE
        }
    }

    private fun askCameraPermission() {
        if (arrayOf(android.Manifest.permission.CAMERA).all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            cameraManager.cameraStart()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraManager.cameraStart()
        } else {
            Toast.makeText(this, "Camera Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onImageAnalysis(faces: List<Face>) {
        try {


            if (isStarted && faces.isNotEmpty()) {
                if (faces.size == 1) {
                    if ((faces.get(0).leftEyeOpenProbability != null && faces.get(0).leftEyeOpenProbability!! <= 0.015) || (faces.get(
                            0
                        ).rightEyeOpenProbability != null && faces.get(0).rightEyeOpenProbability!! <= 0.015)
                    ) {
                        eyeClosed = 1
                    }

                    if (eyeClosed == 1 && ((faces.get(0).leftEyeOpenProbability != null && faces.get(
                            0
                        ).leftEyeOpenProbability!! >= 0.75)) || ((faces.get(
                            0
                        ).rightEyeOpenProbability != null && faces.get(0).rightEyeOpenProbability!! >= 0.75))
                    ) {
                        eyeOpened = 1
                    }
                    if (isStarted && (eyeClosed == 1 && eyeOpened == 1)) {
                        binding.llCapture.visibility = View.VISIBLE
//                        cameraManager.cameraStop()
//                        buttonVisibility(false)
//                        isStarted = false
                    } else {
                        binding.llCapture.visibility = View.GONE

                    }
                } else {
                    val builder = AlertDialog.Builder(this@FaceDetectionActivity)
                    builder.setMessage("Detecting more than 1 face,\nneed only one face of particular worker in frame which you want to capture image")
                    val alert = builder.create()
                    alert.show()
                    cameraManager.cameraStop()
                    buttonVisibility(false)
                    isStarted = false
                }
                Log.i(
                    TAG,
                    "onImageAnalysis: ${faces.get(0).leftEyeOpenProbability} $eyeOpened $eyeClosed"
                )
            } else {
                binding.llCapture.visibility = View.GONE
//                val builder = AlertDialog.Builder(this@MainActivity)
//                builder.setMessage("Unable to detect Human Face to capture")
//                val alert = builder.create()
//                alert.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}