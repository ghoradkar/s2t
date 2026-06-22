package com.myhindlab.abkat.facedetection.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.myhindlab.abkat.facedetection.graphic.GraphicOverlay
import com.myhindlab.abkat.facedetection.utils.CameraUtils
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraManager(
    private val context: Context,
    private val previewView: PreviewView,
    private val graphicOverlay: GraphicOverlay<*>,
    private val lifecycleOwner: LifecycleOwner,
    private val cameraAnalyzerEvent: CameraAnalyzer.CameraAnalyzerEvent,
    private val cscIntent: Intent
) {

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var preview: Preview
    private lateinit var imageAnalysis: ImageAnalysis
    private lateinit var camera: Camera
    private var imageCapture: ImageCapture? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    fun cameraStart() {
        val cameraProcessProvider = ProcessCameraProvider.getInstance(context)

        cameraProcessProvider.addListener(
            {
                cameraProvider = cameraProcessProvider.get()
                preview = Preview.Builder().build()
                imageCapture = ImageCapture.Builder().build()


                imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(
                            cameraExecutor,
                            CameraAnalyzer(graphicOverlay, cameraAnalyzerEvent)
                        )
                    }
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraOption)
                    .build()

                setCameraConfig(cameraProvider, cameraSelector)
            },
            ContextCompat.getMainExecutor(context)
        )
    }

//    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
//        preview = Preview.Builder().build()
//        imageCapture = ImageCapture.Builder().build()
//        val cameraSelector = CameraSelector.Builder()
//            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//            .build()
//        preview.setSurfaceProvider(findViewById(R.id.preview_view).getSurfaceProvider())
//        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
//    }

    private fun setCameraConfig(
        cameraProvider: ProcessCameraProvider,
        cameraSelector: CameraSelector
    ) {
        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis,
                imageCapture
            )
            preview.setSurfaceProvider(previewView.surfaceProvider)

        } catch (e: Exception) {
            Log.e(TAG, "setCameraConfig : $e")
        }
    }

    fun changeCamera() {
        cameraStop()
        cameraOption =
            if (cameraOption == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK
        CameraUtils.toggleSelector()
        cameraStart()
    }

    fun cameraStop() {
        cameraProvider.unbindAll()
    }

    fun capturePhoto(): Uri? {
        val DESTINY_DIR =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        if (!DESTINY_DIR.exists() && !DESTINY_DIR.mkdirs()) {
//            return
//        }
        val fName = System.currentTimeMillis().toString() + "_PR.jpg"
        val mainPicture = File(DESTINY_DIR, fName)
//        val file: File =
//            File(getBatchDirectoryName(), System.currentTimeMillis().toString() + ".jpg")
        val outputFileOptions = OutputFileOptions.Builder(mainPicture).build()
        var savedUri: Uri? = null;
        imageCapture?.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: OutputFileResults) {
//                    Toast.makeText(this@MainActivity, "Photo saved", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onImageSaved: Photo Saved ${outputFileResults.savedUri}")
                    savedUri = outputFileResults.savedUri
                    sendBroadcast(savedUri, fName)

                    (context as Activity).finish()

                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Photo capture failed: " + exception.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            })

        return savedUri
    }

    private fun getBatchDirectoryName(): String? {
        val app_folder_path: String = context.getFilesDir().toString() + "/captured_images"
        val dir = File(app_folder_path)
        if (!dir.exists() && !dir.mkdirs()) {
            Log.e("MainActivity", "Could not create image directory")
        }
        return app_folder_path
    }

    companion object {
        private const val TAG: String = "CameraManager"
        var cameraOption: Int = CameraSelector.LENS_FACING_FRONT

        @Throws(RuntimeException::class)
        fun saveFile(data: ByteArray?, fileName: String?, context: Context?) {
            val DESTINY_DIR =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!DESTINY_DIR.exists() && !DESTINY_DIR.mkdirs()) {
                return
            }
            val mainPicture = File(DESTINY_DIR, fileName)
            try {
                val fos = FileOutputStream(mainPicture)
                fos.write(data)
                fos.close()
            } catch (e: java.lang.Exception) {
                throw RuntimeException("Image could not be saved.", e)
            }
        }

    }

    private fun sendBroadcast(savedUri: Uri?, fName: String) {
        try {
//            val className =
//                "com.myhindlab.abkat.offline.ui.D2DPatientRegistration_Activity\$FaceDetectionReceiver"
//
//            val mIntent = Intent()
//            mIntent.component = ComponentName(
//                BuildConfig.APPLICATION_ID,
//                className
//            )
//            mIntent.action = "com.myhindlab.abkat"
//
////            intent.data=savedUri;
//            mIntent.putExtra("FaceData", savedUri?.path)
//            mIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
//            context.sendBroadcast(mIntent)

            val intent = Intent("FACE_DETECTION")
            intent.putExtra("FaceData", savedUri?.path)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

            Log.i(TAG, "sendBroadcast: ")
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Exception while sending broadcast : $e")
        }
    }
}