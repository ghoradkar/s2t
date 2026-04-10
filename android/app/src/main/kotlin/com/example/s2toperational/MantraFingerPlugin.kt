package com.example.s2toperational

import android.content.Context
import android.os.SystemClock
import android.util.Base64
import com.mantra.mfs100.FingerData
import com.mantra.mfs100.MFS100
import com.mantra.mfs100.MFS100Event
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import java.io.File
import java.io.FileOutputStream

class MantraFingerPlugin : FlutterPlugin, MethodCallHandler, MFS100Event {

    companion object {
        const val METHOD_CHANNEL = "com.s2t.operational/mantra_mfs100"
        const val EVENT_CHANNEL  = "com.s2t.operational/mantra_mfs100_events"
        private const val THRESHOLD: Long = 1500
    }

    private lateinit var methodChannel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private var eventSink: EventChannel.EventSink? = null

    private var context: Context? = null
    private var mfs100: MFS100? = null

    /** True once Init() returned 0 after device attachment */
    var isInitialized = false
        private set

    private var isCaptureRunning = false
    private var mLastAttTime: Long = 0
    private var mLastDttTime: Long = 0

    // ── FlutterPlugin ────────────────────────────────────────────────────────

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        context = binding.applicationContext

        methodChannel = MethodChannel(binding.binaryMessenger, METHOD_CHANNEL)
        methodChannel.setMethodCallHandler(this)

        eventChannel = EventChannel(binding.binaryMessenger, EVENT_CHANNEL)
        eventChannel.setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                eventSink = events
            }
            override fun onCancel(arguments: Any?) {
                eventSink = null
            }
        })

        initMfs100()
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
        disposeMfs100()
        context = null
    }

    // ── Internal setup ───────────────────────────────────────────────────────

    private fun initMfs100() {
        try {
            mfs100 = MFS100(this)
            context?.let { mfs100?.SetApplicationContext(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun disposeMfs100() {
        try {
            if (isCaptureRunning) mfs100?.StopAutoCapture()
            Thread.sleep(300)
            mfs100?.Dispose()
            mfs100 = null
            isInitialized = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ── MethodCallHandler ────────────────────────────────────────────────────

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {

            "isDeviceConnected" -> {
                result.success(isInitialized && mfs100 != null)
            }

            "startCapture" -> {
                val timeoutMs = call.argument<Int>("timeoutMs") ?: 10000
                startCapture(timeoutMs, result)
            }

            "stopCapture" -> {
                try {
                    if (isCaptureRunning) mfs100?.StopAutoCapture()
                    result.success(null)
                } catch (e: Exception) {
                    result.error("STOP_FAILED", e.message, null)
                }
            }

            "getDeviceInfo" -> {
                try {
                    val info = mfs100?.GetDeviceInfo()
                    result.success(
                        mapOf(
                            "serial"     to (info?.SerialNo()  ?: ""),
                            "make"       to (info?.Make()      ?: ""),
                            "model"      to (info?.Model()     ?: ""),
                            "sdkVersion" to (mfs100?.GetSDKVersion() ?: ""),
                        )
                    )
                } catch (e: Exception) {
                    result.error("INFO_FAILED", e.message, null)
                }
            }

            else -> result.notImplemented()
        }
    }

    // ── Capture ──────────────────────────────────────────────────────────────

    private fun startCapture(timeoutMs: Int, result: MethodChannel.Result) {
        if (isCaptureRunning) {
            result.error("CAPTURE_RUNNING", "Capture already in progress", null)
            return
        }
        if (!isInitialized || mfs100 == null) {
            result.error(
                "NOT_INITIALIZED",
                "Scanner not initialised. Please connect the Mantra device.",
                null
            )
            return
        }

        Thread {
            isCaptureRunning = true
            try {
                val fingerData = FingerData()
                val ret = mfs100!!.AutoCapture(fingerData, timeoutMs, false)

                if (ret != 0) {
                    val errMsg = mfs100?.GetErrorMsg(ret) ?: "Capture failed (code $ret)"
                    result.error("CAPTURE_FAILED", errMsg, null)
                } else {
                    val imageBytes = fingerData.FingerImage()
                    val base64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
                    val filePath = saveToTempFile(imageBytes)

                    result.success(
                        mapOf(
                            "base64"   to base64,
                            "filePath" to (filePath ?: ""),
                            "quality"  to fingerData.Quality(),
                            "nfiq"     to fingerData.Nfiq(),
                        )
                    )
                }
            } catch (e: Exception) {
                result.error("CAPTURE_EXCEPTION", e.message, null)
            } finally {
                isCaptureRunning = false
            }
        }.start()
    }

    private fun saveToTempFile(imageBytes: ByteArray): String? {
        return try {
            val dir = context?.cacheDir ?: return null
            val file = File(dir, "mantra_fp_${System.currentTimeMillis()}.png")
            FileOutputStream(file).use { it.write(imageBytes) }
            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    // ── MFS100Event ──────────────────────────────────────────────────────────

    override fun OnDeviceAttached(vid: Int, pid: Int, hasPermission: Boolean) {
        if (SystemClock.elapsedRealtime() - mLastAttTime < THRESHOLD) return
        mLastAttTime = SystemClock.elapsedRealtime()

        if (!hasPermission) {
            eventSink?.success(mapOf("event" to "attached", "status" to "no_permission"))
            return
        }

        try {
            val ret: Int = when {
                (vid == 1204 || vid == 11279) && pid == 34323 -> mfs100?.LoadFirmware() ?: -1
                else -> mfs100?.Init() ?: -1
            }

            if (ret == 0) {
                isInitialized = true
                val info = mfs100?.GetDeviceInfo()
                eventSink?.success(
                    mapOf(
                        "event"  to "attached",
                        "status" to "ready",
                        "serial" to (info?.SerialNo() ?: ""),
                        "model"  to (info?.Model()    ?: ""),
                    )
                )
            } else {
                val errMsg = mfs100?.GetErrorMsg(ret) ?: "Init failed (code $ret)"
                eventSink?.success(
                    mapOf("event" to "attached", "status" to "init_failed", "error" to errMsg)
                )
            }
        } catch (e: Exception) {
            eventSink?.success(
                mapOf("event" to "attached", "status" to "error", "error" to (e.message ?: ""))
            )
        }
    }

    override fun OnDeviceDetached() {
        if (SystemClock.elapsedRealtime() - mLastDttTime < THRESHOLD) return
        mLastDttTime = SystemClock.elapsedRealtime()

        try {
            mfs100?.UnInit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isInitialized = false
        eventSink?.success(mapOf("event" to "detached"))
    }

    override fun OnHostCheckFailed(err: String?) {
        eventSink?.success(
            mapOf("event" to "host_check_failed", "error" to (err ?: ""))
        )
    }
}