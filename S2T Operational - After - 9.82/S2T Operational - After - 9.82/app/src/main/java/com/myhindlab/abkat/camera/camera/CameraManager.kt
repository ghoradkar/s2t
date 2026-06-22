package com.myhindlab.abkat.camera.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.io.FileOutputStream

class CameraManager(
    private val context: Context,
    private val previewView: PreviewView,
    private val lifecycleOwner: LifecycleOwner
) {

    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var preview: Preview
    private lateinit var camera: Camera
    private var imageCapture: ImageCapture? = null
    private var isFlashOn = false

    fun cameraStart() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(
            {
                cameraProvider = cameraProviderFuture.get()

                preview = Preview.Builder().build()

                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(cameraOption)
                    .build()

                bindCamera(cameraSelector)
            },
            ContextCompat.getMainExecutor(context)
        )
    }

    private fun bindCamera(cameraSelector: CameraSelector) {
        try {
            cameraProvider.unbindAll()

            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )

            preview.setSurfaceProvider(previewView.surfaceProvider)

        } catch (e: Exception) {
            Log.e(TAG, "bindCamera error", e)
        }
    }

    fun cameraStop() {
        if (::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
        }
    }

    fun changeCamera() {
        cameraStop()
        cameraOption =
            if (cameraOption == CameraSelector.LENS_FACING_FRONT)
                CameraSelector.LENS_FACING_BACK
            else
                CameraSelector.LENS_FACING_FRONT

        cameraStart()
    }

    fun toggleFlash() {
        if (!::camera.isInitialized) {
            Log.e(TAG, "Camera not initialized")
            return
        }

        // Flash works only on BACK camera
        if (cameraOption != CameraSelector.LENS_FACING_BACK) {
            Toast.makeText(context, "Flash available only on back camera", Toast.LENGTH_SHORT)
                .show()
            return
        }

        isFlashOn = !isFlashOn
        camera.cameraControl.enableTorch(isFlashOn)
    }

    /**
     * Capture photo and return URI via callback.
     * Cropping / masking MUST be done in Activity.
     */
    fun capturePhoto(onResult: (Uri) -> Unit) {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "IMG_${System.currentTimeMillis()}.jpg"
        )

        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                    if (cameraOption == CameraSelector.LENS_FACING_FRONT) {
                        mirrorImage(photoFile)
                    }
                    onResult(Uri.fromFile(photoFile))
                }

                override fun onError(exception: ImageCaptureException) {
//                    Toast.makeText(context, "Capture failed", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Capture failed", exception)
                }
            }
        )
    }


    private fun mirrorImage(file: File) {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath) ?: return

        val exif = ExifInterface(file.absolutePath)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val matrix = Matrix()

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
        }

        matrix.postScale(-1f, 1f)

        val fixedBitmap = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )

        FileOutputStream(file).use { out ->
            fixedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
        }

        exif.setAttribute(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL.toString()
        )
        exif.saveAttributes()

        bitmap.recycle()
        fixedBitmap.recycle()
    }

//    private fun mirrorImage(file: File) {
//        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
//
//        val matrix = Matrix().apply {
//            preScale(-1f, 1f) // Horizontal flip
//        }
//
//        val mirroredBitmap = Bitmap.createBitmap(
//            bitmap,
//            0,
//            0,
//            bitmap.width,
//            bitmap.height,
//            matrix,
//            true
//        )
//
//        FileOutputStream(file).use { out ->
//            mirroredBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
//        }
//
//        bitmap.recycle()
//        mirroredBitmap.recycle()
//    }

    companion object {
        private const val TAG = "CameraManager"
        var cameraOption: Int = CameraSelector.LENS_FACING_FRONT
    }
}
