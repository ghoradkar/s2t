package com.example.s2toperational

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Handler
import android.os.Looper
import android.util.Log
import info.safey.safey_sdk.FlowVolumeData
import info.safey.safey_sdk.IConnectionCallback
import info.safey.safey_sdk.IDeviceCallback
import info.safey.safey_sdk.IErrorCallback
import info.safey.safey_sdk.IScannerCallback
import info.safey.safey_sdk.ITestCallback
import info.safey.safey_sdk.ITrialCallback
import info.safey.safey_sdk.SafeyDeviceKit
import info.safey.safey_sdk.SafeyPerson
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/**
 * Flutter plugin bridging the Safey Spirometer SDK to Flutter.
 *
 * MethodChannel: "com.s2toperational/safey_spirometry"
 *   scan()           — start BLE scan for Safey devices
 *   connect(address) — connect to a discovered device by MAC address
 *   startTest(gender, weight, age, height) — start spirometry test
 *   disconnect()     — disconnect and clean up
 *
 * EventChannel: "com.s2toperational/safey_spirometry_events"
 *   Events (Map<String, Any?>):
 *     {type: "deviceFound", name: String, address: String}
 *     {type: "connected"}
 *     {type: "disconnected"}
 *     {type: "progress", value: Int}
 *     {type: "info", code: String}
 *     {type: "testResult", json: String, trialCount: Int, sessionScore: String}
 *     {type: "error", message: String}
 */
class SafeySpirometryPlugin : FlutterPlugin, MethodChannel.MethodCallHandler,
    IScannerCallback, IConnectionCallback, ITrialCallback, ITestCallback,
    IErrorCallback, IDeviceCallback {

    companion object {
        const val METHOD_CHANNEL = "com.s2toperational/safey_spirometry"
        const val EVENT_CHANNEL  = "com.s2toperational/safey_spirometry_events"
        const val LICENSE_KEY    = "7659-2779-4723-9301-2442"
        const val TAG            = "SafeySpirom"
    }

    private lateinit var methodChannel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private var eventSink: EventChannel.EventSink? = null

    private var appContext: android.content.Context? = null
    private var safeyDeviceKit: SafeyDeviceKit? = null
    private val mainHandler = Handler(Looper.getMainLooper())

    // Stores discovered BluetoothDevice objects keyed by MAC address
    private val discoveredDevices = mutableMapOf<String, BluetoothDevice>()

    // The device currently being connected / connected
    private var currentDevice: BluetoothDevice? = null

    // ── FlutterPlugin ────────────────────────────────────────────────────────

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        appContext    = binding.applicationContext
        methodChannel = MethodChannel(binding.binaryMessenger, METHOD_CHANNEL)
        methodChannel.setMethodCallHandler(this)

        eventChannel = EventChannel(binding.binaryMessenger, EVENT_CHANNEL)
        eventChannel.setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                eventSink = events
                initializeSdk()
            }
            override fun onCancel(arguments: Any?) {
                eventSink = null
            }
        })
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
        teardown()
        appContext = null
    }

    // ── SDK init / teardown ──────────────────────────────────────────────────

    private fun initializeSdk() {
        val ctx = appContext ?: return
        try {
            safeyDeviceKit = SafeyDeviceKit.Companion.init(ctx, LICENSE_KEY)
            safeyDeviceKit?.registerScannerCallback(this)
            safeyDeviceKit?.registerErrorCallback(this)
            safeyDeviceKit?.registerDeviceCallback(this)
            safeyDeviceKit?.registerConnectionCallback(this)
            safeyDeviceKit?.registerTrialCallback(this)
            safeyDeviceKit?.registerTestCallback(this)
            Log.d(TAG, "Safey SDK initialized")
        } catch (e: Exception) {
            Log.e(TAG, "SDK init error: ${e.message}")
            sendEvent(mapOf("type" to "error", "message" to (e.message ?: "SDK init failed")))
        }
    }

    private fun teardown() {
        try {
            safeyDeviceKit?.disconnect()
            safeyDeviceKit?.unregisterCallbacks()
        } catch (_: Exception) {}
        safeyDeviceKit = null
        discoveredDevices.clear()
        currentDevice = null
    }

    // ── MethodCallHandler ────────────────────────────────────────────────────

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "scan" -> {
                discoveredDevices.clear()
                safeyDeviceKit?.scanDevice()
                result.success(null)
            }
            "connect" -> {
                val address = call.argument<String>("address") ?: ""
                connectToDevice(address)
                result.success(null)
            }
            "startTest" -> {
                val gender = call.argument<Int>("gender") ?: 1
                val weight = call.argument<Int>("weight") ?: 60
                val age    = call.argument<Double>("age") ?: 30.0
                val height = call.argument<Int>("height") ?: 165
                try {
                    safeyDeviceKit?.startTest(SafeyPerson(4, gender, weight, age, height))
                    safeyDeviceKit?.testStarted = true
                    safeyDeviceKit?.startTrial(false)
                } catch (e: Exception) {
                    Log.e(TAG, "startTest error: ${e.message}")
                    sendEvent(mapOf("type" to "error", "message" to (e.message ?: "Start test failed")))
                }
                result.success(null)
            }
            "disconnect" -> {
                teardown()
                initializeSdk()
                result.success(null)
            }
            else -> result.notImplemented()
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(address: String) {
        val stored = discoveredDevices[address]
        if (stored != null) {
            currentDevice = stored
            safeyDeviceKit?.connectDevice(stored)
            return
        }
        // Fallback: get device by MAC from BluetoothAdapter
        try {
            val adapter = BluetoothAdapter.getDefaultAdapter()
            val device  = adapter?.getRemoteDevice(address)
            if (device != null) {
                currentDevice = device
                safeyDeviceKit?.connectDevice(device)
            } else {
                sendEvent(mapOf("type" to "error", "message" to "Device not found: $address"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "connect error: ${e.message}")
            sendEvent(mapOf("type" to "error", "message" to (e.message ?: "Connect failed")))
        }
    }

    // ── IScannerCallback ─────────────────────────────────────────────────────

    @SuppressLint("MissingPermission")
    override fun getBluetoothDevice(bluetoothDevice: BluetoothDevice) {
        Log.d(TAG, "getBluetoothDevice: ${bluetoothDevice.name} ${bluetoothDevice.address}")
        discoveredDevices[bluetoothDevice.address] = bluetoothDevice
        sendEvent(mapOf(
            "type"    to "deviceFound",
            "name"    to (bluetoothDevice.name ?: "Safey Device"),
            "address" to bluetoothDevice.address,
        ))
    }

    @SuppressLint("MissingPermission")
    override fun lastConnectedDeviceFound(bluetoothDevice: BluetoothDevice) {
        Log.d(TAG, "lastConnectedDeviceFound: ${bluetoothDevice.name}")
        discoveredDevices[bluetoothDevice.address] = bluetoothDevice
        currentDevice = bluetoothDevice
        sendEvent(mapOf(
            "type"    to "deviceFound",
            "name"    to (bluetoothDevice.name ?: "Safey Device"),
            "address" to bluetoothDevice.address,
        ))
        safeyDeviceKit?.connectDevice(bluetoothDevice)
    }

    override fun info(message: String) {
        Log.d(TAG, "info: $message")
        sendEvent(mapOf("type" to "info", "code" to message))
    }

    // ── IConnectionCallback ──────────────────────────────────────────────────

    override fun getConnected(isConnected: Boolean) {
        Log.d(TAG, "getConnected: $isConnected")
        if (isConnected) {
            // Set spirometry as test type (1 = Spirometry)
            try { safeyDeviceKit?.setSelectedTestType(1) } catch (_: Exception) {}
            sendEvent(mapOf("type" to "connected"))
        } else {
            sendEvent(mapOf("type" to "disconnected"))
        }
    }

    // ── ITrialCallback ───────────────────────────────────────────────────────

    override fun onProgressChange(progress: Int) {
        Log.d(TAG, "onProgressChange: $progress")
        sendEvent(mapOf("type" to "progress", "value" to progress))
    }

    override fun enableTrial() {
        Log.d(TAG, "enableTrial")
    }

    override fun testCompleted() {
        Log.d(TAG, "testCompleted")
        sendEvent(mapOf("type" to "info", "code" to "testCompleted"))
    }

    // ── ITestCallback ────────────────────────────────────────────────────────

    override fun getTestResult(flowVolumeData: FlowVolumeData, i: Int) {
        Log.d(TAG, "getTestResult: trial $i")
    }

    override fun getTestResults(testResult: String, trialCount: Int, sessionScore: String) {
        Log.d(TAG, "getTestResults: trialCount=$trialCount score=$sessionScore")
        try {
            val gson = com.google.gson.Gson()

            // Parse as raw JsonObject — SDK Kotlin @Metadata is also obfuscated so
            // class-based deserialization (getTestResults(), getFlow(), etc.) fails.
            // We read the confirmed single-letter obfuscated keys directly instead.
            //
            // TestData:       a=testType, b=deviceType, c=sessionScore,
            //                 d=suggestedDiagnosis, e=testResults(List), f=variance(List)
            // TestResult:     a=trialNo, b=isPost, c=isBest,
            //                 d=graphPoints(List), e=measuredValues(List), f=message
            // ResultData:     a=measurement, b=measuredValue, c=predicted, d=unit,
            //                 e=predictedPer, f=LLN, g=ULN, h=zScore
            // FlowVolumeData: a=flow, b=volume, c=second, d=direction

            val root = gson.fromJson(testResult, com.google.gson.JsonObject::class.java)
            val diagnosis = root.get("d")?.asString ?: ""
            val sdkScore  = root.get("c")?.asString ?: sessionScore
            val rawTrials = root.getAsJsonArray("e")

            val trials = mutableListOf<Map<String, Any?>>()
            rawTrials?.forEach { trialElem ->
                val t = trialElem.asJsonObject

                val measuredValues = mutableListOf<Map<String, Any?>>()
                t.getAsJsonArray("e")?.forEach { rElem ->
                    val r = rElem.asJsonObject
                    measuredValues.add(mapOf(
                        "measurement"   to (r.get("a")?.asString ?: ""),
                        "measuredValue" to (r.get("b")?.asDouble ?: 0.0),
                        "predicted"     to (r.get("c")?.asString ?: " - "),
                        "unit"          to (r.get("d")?.asString ?: ""),
                        "predictedPer"  to (r.get("e")?.asDouble ?: 0.0),
                        "LLN"           to (r.get("f")?.asString ?: " - "),
                        "ULN"           to (r.get("g")?.asString ?: " - "),
                        "zScore"        to (r.get("h")?.asString ?: " - "),
                    ))
                }

                val graphPoints = mutableListOf<Map<String, Any?>>()
                t.getAsJsonArray("d")?.forEach { gpElem ->
                    val gp = gpElem.asJsonObject
                    graphPoints.add(mapOf(
                        "flow"      to (gp.get("a")?.asDouble ?: 0.0),
                        "volume"    to (gp.get("b")?.asDouble ?: 0.0),
                        "second"    to (gp.get("c")?.asDouble ?: 0.0),
                        "direction" to (gp.get("d")?.asInt    ?: 0),
                    ))
                }

                trials.add(mapOf(
                    "trialNo"        to (t.get("a")?.asInt     ?: 0),
                    "isPost"         to (t.get("b")?.asBoolean ?: false),
                    "isBest"         to (t.get("c")?.asBoolean ?: false),
                    "message"        to (t.get("f")?.asString  ?: ""),
                    "measuredValues" to measuredValues,
                    "graphPoints"    to graphPoints,
                ))
            }

            val cleanMap = mapOf(
                "suggestedDiagnosis" to diagnosis,
                "sessionScore"       to sdkScore,
                "testResults"        to trials,
            )

            Log.d(TAG, "getTestResults: cleaned trials=${trials.size}")
            sendEvent(mapOf(
                "type"         to "testResult",
                "json"         to gson.toJson(cleanMap),
                "trialCount"   to trialCount,
                "sessionScore" to sessionScore,
            ))
        } catch (e: Exception) {
            Log.e(TAG, "getTestResults parse error: ${e.message}", e)
            // Fallback: send raw JSON so Flutter at least receives something
            sendEvent(mapOf(
                "type"         to "testResult",
                "json"         to testResult,
                "trialCount"   to trialCount,
                "sessionScore" to sessionScore,
            ))
        }
    }

    override fun invalidManeuver(i: Int) {
        Log.d(TAG, "invalidManeuver: $i")
        sendEvent(mapOf("type" to "info", "code" to "INF_03"))
    }

    override fun selectTestType() {
        Log.d(TAG, "selectTestType")
    }

    // ── IDeviceCallback ──────────────────────────────────────────────────────
    // IErrorCallback.info(String) is satisfied by IScannerCallback's info() above

    @SuppressLint("MissingPermission")
    override fun getBatteryStatus(s: String) {
        Log.d(TAG, "getBatteryStatus: $s")
        try { safeyDeviceKit?.setSelectedTestType(1) } catch (_: Exception) {}
        sendEvent(mapOf(
            "type"    to "batteryStatus",
            "battery" to s,
            "name"    to (currentDevice?.name ?: ""),
            "address" to (currentDevice?.address ?: ""),
        ))
    }

    override fun enableTest() {
        Log.d(TAG, "enableTest")
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private fun sendEvent(data: Map<String, Any?>) {
        mainHandler.post { eventSink?.success(data) }
    }
}
