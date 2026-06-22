package com.myhindlab.abkat.camera

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.mlkit.vision.face.Face
import com.myhindlab.abkat.BuildConfig
import com.myhindlab.abkat.R
import com.myhindlab.abkat.camera.camera.CameraAnalyzer
import com.myhindlab.abkat.camera.camera.CameraManager
import com.myhindlab.abkat.camera.utils.CaptureMode
import com.myhindlab.abkat.databinding.ActivityCameraBinding
import java.io.File
import java.io.FileOutputStream

class CameraActivity : AppCompatActivity(), CameraAnalyzer.CameraAnalyzerEvent {

    private lateinit var cameraManager: CameraManager
    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    private val TAG = CameraActivity::class.java.simpleName
    var initialBlink = 0
    var eyeClosed = 0
    var eyeOpened = 0
    var isStarted = false;
    private var hasCameraPermission = false
    private var captureMode = CaptureMode.FACE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        captureMode = intent.getStringExtra("CAPTURE_MODE")
            ?.let { CaptureMode.valueOf(it) }
            ?: CaptureMode.FACE

        setupUiForMode()
        cameraManager = CameraManager(
            this,
            binding.viewCameraPreview,
            this,

            )

        askCameraPermission()
        buttonClicks()
//        showInstructionDialog()

    }

    private fun setupUiForMode() {
        when (captureMode) {
            CaptureMode.FACE -> {
//                setPreviewSize(260, 340)
                switchToFaceMode()
            }

            CaptureMode.ID_CARD -> {
//                setPreviewSize(300, 190)
                switchToIdMode()
            }

            CaptureMode.DOCUMENT -> {
//                setPreviewSize(300, 600)
                switchToDocumentMode()
            }


            CaptureMode.GROUP_PHOTO -> {
//                setPreviewSize(300, 600)
                switchToGroupPhoto()
            }
        }
    }



    fun setPreviewSize(widthDp: Int, heightDp: Int) {
        val params = binding.viewCameraPreview.layoutParams
        params.width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            widthDp.toFloat(),
            resources.displayMetrics
        ).toInt()
        params.height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            heightDp.toFloat(),
            resources.displayMetrics
        ).toInt()
        binding.viewCameraPreview.layoutParams = params
    }

    private fun showInstructionDialog() {
        AlertDialog.Builder(this)
            .setTitle("Instructions")
            .setMessage(
                "Click on Start Analysis to begin.\n\n" +
                        "• Keep your face inside the oval\n" +
                        "• Blink both eyes to enable capture\n" +
                        "• Ensure good lighting"
            )
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }

    private fun switchToFaceMode() {
        captureMode = CaptureMode.FACE

        binding.captureFrame.apply {
            setBackgroundResource(R.drawable.square_outline)
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.face_width)
            layoutParams.height = resources.getDimensionPixelSize(R.dimen.face_height)
            requestLayout()
        }

        binding.instructionText.text = "Align your face inside the frame"

    }

    private fun switchToIdMode() {
        captureMode = CaptureMode.ID_CARD

        binding.captureFrame.apply {
            setBackgroundResource(R.drawable.id_card_outline)
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.id_width)
            layoutParams.height = resources.getDimensionPixelSize(R.dimen.id_height)
            requestLayout()
        }

        binding.instructionText.text = "Place ID card inside the frame"
    }

    private fun switchToDocumentMode() {
        captureMode = CaptureMode.DOCUMENT

        binding.captureFrame.apply {
            setBackgroundResource(R.drawable.document_outline)
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.doc_width)
            layoutParams.height = resources.getDimensionPixelSize(R.dimen.doc_height)
            requestLayout()
        }

        binding.instructionText.text = "Place Document inside the frame"
    }



    private fun switchToGroupPhoto() {
        captureMode = CaptureMode.GROUP_PHOTO

        binding.captureFrame.apply {
            setBackgroundResource(R.drawable.document_outline)
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.grp_width)
            layoutParams.height = resources.getDimensionPixelSize(R.dimen.grp_height)
            requestLayout()
        }

        binding.instructionText.text = "Take group photo"
    }

    private fun buttonClicks() {

        binding.buttonTurnCamera.setOnClickListener {
            cameraManager.changeCamera()
        }
//        binding.buttonStopCamera.setOnClickListener {
//            cameraManager.cameraStop()
//            buttonVisibility(false)
//            isStarted = false
//        }
        binding.buttonStartCamera.setOnClickListener {
            cameraManager.cameraStart()
            buttonVisibility(true)
            isStarted = true
            eyeClosed = 0
            eyeOpened = 0
        }


        binding.buttonFlash.setOnClickListener {
            cameraManager.toggleFlash()

        }

        binding.llCapture.setOnClickListener {
            Toast.makeText(this@CameraActivity, "Capture", Toast.LENGTH_SHORT).show()
//            cameraManager.cameraStart()


            cameraManager.capturePhoto { uri ->
//                if (captureMode == CaptureMode.FACE) {
                val bitmap = decodeBitmap(uri) ?: return@capturePhoto

                val cropped =
                    cropToCaptureFrameV1(bitmap, binding.viewCameraPreview, binding.captureFrame)

                val finalBitmap = cropped

                saveFinalBitmap(finalBitmap)
//                } else {
//                    val cropImageOptions = CropImageOptions()
//                    cropImageOptions.guidelines = CropImageView.Guidelines.ON
//                    cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.JPEG
//                    val options = CropImageContractOptions(uri, cropImageOptions)
//                    cropImageLauncher.launch(options)
//                }
            }


//            buttonVisibility(true)
        }
    }

    var cropImageLauncher: ActivityResultLauncher<CropImageContractOptions> =
        registerForActivityResult<CropImageContractOptions, CropImageView.CropResult>(
            CropImageContract(),
            ActivityResultCallback<CropImageView.CropResult> { result: CropImageView.CropResult ->
                if (result.isSuccessful) {
                    // Use the cropped image URI.
                    Log.d(TAG, "onImageSaved: Photo Saved ${result.uriContent}")
                    val fName = System.currentTimeMillis().toString() + "_PR.jpg"

                    // Use the cropped image URI.
                    val path = result.getUriFilePath(this@CameraActivity, true)
                    val intent = Intent()
                    intent.putExtra("fileURI", path)
                    intent.putExtra("fName", fName)
                    setResult(RESULT_OK, intent)
                    finish()


//                    sendBroadcast(result.uriContent, fName)

//                    savefile(result.uriContent)
                    // Process the cropped image URI as needed.
                } else {
                    // An error occurred.
                    val exception: Exception? = result.error
                    // Handle the error.
                }
            }
        )

    private fun sendBroadcast(savedUri: Uri?, fName: String) {
        try {
            val className =
                "${BuildConfig.APPLICATION_ID}.activities.doortodoor.D2DPatientRegistration_Activity\$FaceDetectionReceiver"

            val mIntent = Intent()
            mIntent.component = ComponentName(
                BuildConfig.APPLICATION_ID,
                className
            )
            mIntent.action = "com.csc.facedetectionapp.communication"

//            intent.data=savedUri;
            mIntent.putExtra("FaceData", savedUri?.path)
            mIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            sendBroadcast(mIntent)
            Log.i(TAG, "sendBroadcast: ")
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "Exception while sending broadcast : $e")
        }
    }

    private fun buttonVisibility(forStart: Boolean) {
        if (forStart) {
//            binding.llStopCamera.visibility = View.VISIBLE
            binding.llStartCamera.visibility = View.INVISIBLE
        } else {
//            binding.llStopCamera.visibility = View.INVISIBLE
            binding.llStartCamera.visibility = View.VISIBLE
        }
    }

    private fun askCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            hasCameraPermission = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                0
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            hasCameraPermission = true
        } else {
            Toast.makeText(this, "Camera Permission Denied!", Toast.LENGTH_SHORT).show()
            finish()
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
                    val builder = AlertDialog.Builder(this@CameraActivity)
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

    override fun onResume() {
        super.onResume()

        if (::cameraManager.isInitialized && hasCameraPermission) {
            binding.viewCameraPreview.post {
                cameraManager.cameraStart()
            }
        }
    }

    private fun decodeBitmap(uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }
        } catch (e: Exception) {
            null
        }
    }


    fun cropToPreview(
        bitmap: Bitmap,
        previewView: PreviewView
    ): Bitmap {

        val previewLocation = IntArray(2)
        previewView.getLocationOnScreen(previewLocation)

        val previewWidth = previewView.width
        val previewHeight = previewView.height

        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height

        // Map preview size → bitmap size
        val scaleX = bitmapWidth.toFloat() / previewView.width
        val scaleY = bitmapHeight.toFloat() / previewView.height

        val cropX = ((previewLocation[0] - previewLocation[0]) * scaleX).toInt()
        val cropY = ((previewLocation[1] - previewLocation[1]) * scaleY).toInt()

        val cropWidth = (previewWidth * scaleX).toInt()
        val cropHeight = (previewHeight * scaleY).toInt()

        return Bitmap.createBitmap(
            bitmap,
            cropX.coerceAtLeast(0),
            cropY.coerceAtLeast(0),
            cropWidth.coerceAtMost(bitmapWidth),
            cropHeight.coerceAtMost(bitmapHeight)
        )
    }

    fun maskToOval(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        canvas.drawOval(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    private fun applyOvalMask(input: Bitmap): Bitmap {

        val bitmap =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                input.config == Bitmap.Config.HARDWARE
            ) {
                input.copy(Bitmap.Config.ARGB_8888, true)
            } else {
                input
            }

        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        // Draw oval mask
        canvas.drawOval(rectF, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        paint.xfermode = null

        return output
    }


//    override fun onImageSaved(uri: Uri) {
//
//        val fullBitmap = decodeBitmap(uri) ?: return
//
//        val croppedBitmap =
//            cropToPreviewView(fullBitmap, binding.viewCameraPreview)
//
//        val finalBitmap =
//            if (captureMode == CaptureMode.FACE)
//                applyOvalMask(croppedBitmap)
//            else
//                croppedBitmap
//
//        saveFinalBitmap(finalBitmap)
//    }


    private fun cropToCaptureFrame(
        bitmap: Bitmap,
        previewView: PreviewView,
        captureFrame: View
    ): Bitmap {

        val previewLocation = IntArray(2)
        val frameLocation = IntArray(2)

        previewView.getLocationOnScreen(previewLocation)
        captureFrame.getLocationOnScreen(frameLocation)

        val previewLeft = previewLocation[0]
        val previewTop = previewLocation[1]

        val frameLeft = frameLocation[0]
        val frameTop = frameLocation[1]

        val frameWidth = captureFrame.width
        val frameHeight = captureFrame.height

        val scaleX = bitmap.width.toFloat() / previewView.width
        val scaleY = bitmap.height.toFloat() / previewView.height

        val cropX = ((frameLeft - previewLeft) * scaleX).toInt()
        val cropY = ((frameTop - previewTop) * scaleY).toInt()
        val cropWidth = (frameWidth * scaleX).toInt()
        val cropHeight = (frameHeight * scaleY).toInt()

        val safeX = cropX.coerceIn(0, bitmap.width - 1)
        val safeY = cropY.coerceIn(0, bitmap.height - 1)
        val safeWidth = cropWidth.coerceAtMost(bitmap.width - safeX)
        val safeHeight = cropHeight.coerceAtMost(bitmap.height - safeY)

        return Bitmap.createBitmap(
            bitmap,
            safeX,
            safeY,
            safeWidth,
            safeHeight
        )
    }


    private fun saveFinalBitmap(bitmap: Bitmap) {

        val outputDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?: run {
                Toast.makeText(this, "Storage not available", Toast.LENGTH_SHORT).show()
                return
            }

        if (!outputDir.exists()) outputDir.mkdirs()

        val outputFile = File(
            outputDir,
            "final_${System.currentTimeMillis()}.jpg"
        )

        FileOutputStream(outputFile).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        }

        val intent = Intent().apply {
            putExtra("fileURI", outputFile.path)
        }

        setResult(Activity.RESULT_OK, intent)
//        if (captureMode == CaptureMode.ID_CARD) {
        val cropImageOptions = CropImageOptions()
        cropImageOptions.guidelines = CropImageView.Guidelines.ON
        cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.JPEG
        val options = CropImageContractOptions(outputFile.toUri(), cropImageOptions)
        cropImageLauncher.launch(options)
//        } else {
//            finish()
//        }
    }

    private fun cropToCaptureFrameV1(
        bitmap: Bitmap,
        previewView: PreviewView,
        captureFrame: View
    ): Bitmap {

        val previewW = previewView.width.toFloat()
        val previewH = previewView.height.toFloat()
        val bitmapW = bitmap.width.toFloat()
        val bitmapH = bitmap.height.toFloat()

        // Scale used by PreviewView (fillCenter = centerCrop)
        val scale = maxOf(
            previewW / bitmapW,
            previewH / bitmapH
        )

        val scaledW = bitmapW * scale
        val scaledH = bitmapH * scale

        // Cropped offset introduced by fillCenter
        val offsetX = (scaledW - previewW) / 2
        val offsetY = (scaledH - previewH) / 2

        // Locations
        val previewLoc = IntArray(2)
        val frameLoc = IntArray(2)
        previewView.getLocationOnScreen(previewLoc)
        captureFrame.getLocationOnScreen(frameLoc)

        val frameLeftInPreview = frameLoc[0] - previewLoc[0]
        val frameTopInPreview = frameLoc[1] - previewLoc[1]

        // Map to bitmap coordinates
        val cropX = ((frameLeftInPreview + offsetX) / scale).toInt()
        val cropY = ((frameTopInPreview + offsetY) / scale).toInt()
        val cropW = (captureFrame.width / scale).toInt()
        val cropH = (captureFrame.height / scale).toInt()

        // Safety bounds
        val safeX = cropX.coerceIn(0, bitmap.width - 1)
        val safeY = cropY.coerceIn(0, bitmap.height - 1)
        val safeW = cropW.coerceAtMost(bitmap.width - safeX)
        val safeH = cropH.coerceAtMost(bitmap.height - safeY)

        return Bitmap.createBitmap(bitmap, safeX, safeY, safeW, safeH)
    }

}