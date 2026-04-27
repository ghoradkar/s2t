package com.example.s2toperational

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Interface.OmronPeripheralManagerDataTransferListener
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.LibraryManager.OmronPeripheralManager
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Model.OmronErrorInfo
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.Model.OmronPeripheral
import com.omronhealthcare.OmronConnectivityLibrary.OmronLibrary.OmronUtility.OmronConstants
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

/**
 * Flutter plugin wrapping the Omron Connectivity Library SDK.
 *
 * First call on a fresh install: SDK must download its device configuration from
 * Omron servers after setAPIKey(). We wait for OMRONBLEConfigDeviceAvailabilityNotification
 * (or a 30-second timeout) before calling startManager() and startDataTransferFromPeripheral().
 *
 * Subsequent calls use the cached config — no network wait needed.
 *
 * Channel: "com.s2t.operational/omron_bp"
 * Methods:
 *   transfer({localName, uuid}) → {systolic: Int, diastolic: Int}
 *   cancel()
 */
class OmronBpPlugin : FlutterPlugin, MethodCallHandler {

    companion object {
        const val CHANNEL    = "com.s2t.operational/omron_bp"
        const val TAG        = "OmronBP"
        const val API_KEY    = "8ACE95FC-D532-4004-A88E-873921B18539"
        const val USER_HASH  = "s2t.operational@s2t.com"
        const val TIMEOUT_MS = 60_000L   // overall transfer timeout
        const val CONFIG_WAIT_MS = 30_000L  // max wait for first-time config download
    }

    private lateinit var methodChannel: MethodChannel
    private var appContext: Context? = null
    private var pendingResult: MethodChannel.Result? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    private var timeoutRunnable: Runnable? = null
    private var configReceiver: BroadcastReceiver? = null
    private var configReceiverRegistered = false

    // ── FlutterPlugin ────────────────────────────────────────────────────────

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        appContext    = binding.applicationContext
        methodChannel = MethodChannel(binding.binaryMessenger, CHANNEL)
        methodChannel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
        appContext = null
    }

    // ── MethodCallHandler ────────────────────────────────────────────────────

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "transfer" -> {
                val localName = call.argument<String>("localName") ?: ""
                val uuid      = call.argument<String>("uuid")      ?: ""
                if (localName.isEmpty() && uuid.isEmpty()) {
                    result.error("INVALID_ARGS", "localName or uuid is required", null)
                    return
                }
                if (pendingResult != null) {
                    result.error("BUSY", "Transfer already in progress", null)
                    return
                }
                pendingResult = result
                startTransfer(localName, uuid)
            }
            "cancel" -> {
                cancelPending()
                result.success(null)
            }
            else -> result.notImplemented()
        }
    }

    // ── Transfer entry point ─────────────────────────────────────────────────

    private fun startTransfer(localName: String, uuid: String) {
        val ctx = appContext ?: run {
            deliverError("NO_CONTEXT", "Application context not available")
            return
        }

        // Overall timeout — fires if nothing completes within TIMEOUT_MS
        timeoutRunnable = Runnable {
            deliverError("TIMEOUT", "Transfer timed out after ${TIMEOUT_MS / 1000}s")
        }
        mainHandler.postDelayed(timeoutRunnable!!, TIMEOUT_MS)

        try {
            // Authenticate SDK (may trigger async device-config download on first use)
            OmronPeripheralManager.sharedManager(ctx).setAPIKey(API_KEY, null)

            // Check if device config is already cached from a previous run
            val existingConfig = OmronPeripheralManager.sharedManager(ctx).retrieveManagerConfiguration()
            if (existingConfig != null) {
                // Config is ready — proceed immediately
                Log.d(TAG, "Device config already cached, starting transfer")
                doTransfer(ctx, localName, uuid)
            } else {
                // First install — SDK needs to download config from Omron servers.
                // Wait for OMRONBLEConfigDeviceAvailabilityNotification.
                Log.d(TAG, "Device config not yet available, waiting for download…")
                waitForConfigThenTransfer(ctx, localName, uuid)
            }
        } catch (e: Exception) {
            Log.e(TAG, "startTransfer exception", e)
            deliverError("EXCEPTION", e.message ?: "Unknown error")
        }
    }

    // ── Wait for config download (first install) ─────────────────────────────

    private fun waitForConfigThenTransfer(ctx: Context, localName: String, uuid: String) {
        var configReceived = false
        val configTimeoutRunnable = Runnable {
            if (!configReceived) {
                unregisterConfigReceiver(ctx)
                deliverError("CONFIG_TIMEOUT",
                    "Omron device configuration download timed out. " +
                    "Check internet connection and try again.")
            }
        }
        mainHandler.postDelayed(configTimeoutRunnable, CONFIG_WAIT_MS)

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action != OmronConstants.OMRONBLEConfigDeviceAvailabilityNotification) return
                configReceived = true
                mainHandler.removeCallbacks(configTimeoutRunnable)
                unregisterConfigReceiver(ctx)
                Log.d(TAG, "Device config downloaded, starting transfer")
                doTransfer(ctx, localName, uuid)
            }
        }
        configReceiver = receiver
        try {
            LocalBroadcastManager.getInstance(ctx).registerReceiver(
                receiver,
                IntentFilter(OmronConstants.OMRONBLEConfigDeviceAvailabilityNotification)
            )
            configReceiverRegistered = true
        } catch (e: Exception) {
            Log.w(TAG, "registerReceiver failed: ${e.message}")
            // Try proceeding anyway after a short delay
            mainHandler.removeCallbacks(configTimeoutRunnable)
            mainHandler.postDelayed({ doTransfer(ctx, localName, uuid) }, 3000)
        }
    }

    private fun unregisterConfigReceiver(ctx: Context) {
        if (configReceiverRegistered) {
            try {
                configReceiver?.let {
                    LocalBroadcastManager.getInstance(ctx).unregisterReceiver(it)
                }
            } catch (_: Exception) {}
            configReceiverRegistered = false
            configReceiver = null
        }
    }

    // ── Core transfer logic ───────────────────────────────────────────────────

    private fun doTransfer(ctx: Context, localName: String, uuid: String) {
        try {
            // Configure OmronPeripheralManager
            val config = OmronPeripheralManager.sharedManager(ctx).getConfiguration()
            config.userHashId        = USER_HASH
            config.timeoutInterval   = (TIMEOUT_MS / 1000).toInt()
            config.enableAllDataRead = true    // fetch all records, not just unread; avoids sequence-number mismatch on second call
            OmronPeripheralManager.sharedManager(ctx).setConfiguration(config)

            // Start manager (requires device config to be cached — guaranteed by this point)
            OmronPeripheralManager.sharedManager(ctx).startManager()

            // Build peripheral from saved localName + uuid (MAC)
            val peripheral = OmronPeripheral(localName, uuid)
            val users = arrayListOf(1)   // HEM-7140T1-AP uses user slot 1

            Log.d(TAG, "startDataTransferFromPeripheral: localName=$localName uuid=$uuid")

            OmronPeripheralManager.sharedManager(ctx).startDataTransferFromPeripheral(
                peripheral, users, true,
                object : OmronPeripheralManagerDataTransferListener {
                    override fun onDataTransferCompleted(p: OmronPeripheral?, info: OmronErrorInfo?) {
                        Log.d(TAG, "startTransfer cb: success=${info?.isSuccess} msg=${info?.messageInfo}")
                        if (info?.isSuccess == true && p != null) {
                            OmronPeripheralManager.sharedManager(ctx).endDataTransferFromPeripheral(
                                object : OmronPeripheralManagerDataTransferListener {
                                    override fun onDataTransferCompleted(p2: OmronPeripheral?, info2: OmronErrorInfo?) {
                                        Log.d(TAG, "endTransfer cb: success=${info2?.isSuccess} msg=${info2?.messageInfo}")
                                        if (info2?.isSuccess == true && p2 != null) {
                                            extractAndDeliver(p2)
                                        } else {
                                            deliverError("END_FAILED",
                                                info2?.messageInfo ?: "endDataTransfer failed")
                                        }
                                    }
                                }
                            )
                        } else {
                            deliverError("START_FAILED",
                                info?.messageInfo ?: "startDataTransfer failed")
                        }
                    }
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "doTransfer exception", e)
            deliverError("EXCEPTION", e.message ?: "Unknown error in doTransfer")
        }
    }

    // ── Data extraction ───────────────────────────────────────────────────────

    @Suppress("UNCHECKED_CAST")
    private fun extractAndDeliver(peripheral: OmronPeripheral) {
        try {
            val vitalData = peripheral.vitalData as? HashMap<*, *>
            if (vitalData == null) {
                deliverError("NO_DATA", "No vital data returned by device")
                return
            }
            val bpList = vitalData[OmronConstants.OMRONVitalDataBloodPressureKey]
                as? ArrayList<HashMap<String, Any?>>
            if (bpList.isNullOrEmpty()) {
                deliverError("NO_BP_RECORDS",
                    "Device returned 0 blood pressure records — take a measurement on the device first")
                return
            }
            val reading = bpList.last()
            Log.d(TAG, "BP record: $reading")

            val sys = reading[OmronConstants.OMRONVitalData.SystolicKey]?.let { (it as Number).toInt() }
            val dia = reading[OmronConstants.OMRONVitalData.DiastolicKey]?.let { (it as Number).toInt() }

            if (sys == null || dia == null) {
                deliverError("PARSE_FAILED",
                    "Systolic/diastolic not found. Available keys: ${reading.keys}")
                return
            }
            Log.d(TAG, "Delivering sys=$sys dia=$dia")
            deliverSuccess(sys, dia)
        } catch (e: Exception) {
            Log.e(TAG, "extractAndDeliver exception", e)
            deliverError("EXTRACT_ERROR", e.message ?: "Data extraction error")
        }
    }

    // ── Result delivery ───────────────────────────────────────────────────────

    private fun deliverSuccess(systolic: Int, diastolic: Int) {
        cancelTimeout()
        val r = pendingResult ?: return
        pendingResult = null
        mainHandler.post { r.success(mapOf("systolic" to systolic, "diastolic" to diastolic)) }
    }

    private fun deliverError(code: String, message: String) {
        Log.e(TAG, "OmronBP [$code]: $message")
        cancelTimeout()
        val r = pendingResult ?: return
        pendingResult = null
        mainHandler.post { r.error(code, message, null) }
    }

    private fun cancelPending() {
        cancelTimeout()
        pendingResult = null
    }

    private fun cancelTimeout() {
        timeoutRunnable?.let { mainHandler.removeCallbacks(it) }
        timeoutRunnable = null
    }
}