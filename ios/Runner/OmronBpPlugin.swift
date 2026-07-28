import Flutter
import UIKit

@objc class OmronBpPlugin: NSObject, FlutterPlugin {

    private static let channelName    = "com.s2t.operational/omron_bp"
    private static let apiKey         = "8ACE95FC-D532-4004-A88E-873921B18539"
    private static let userHash       = "s2t.operational@s2t.com"
    private static let timeoutSecs    = 60.0
    private static let configWaitSecs = 30.0

    // Static (not per-instance) to match the Android plugin's companion-object
    // flag — setAPIKey() must run exactly once per process lifetime, not once
    // per plugin instance.
    private static var apiKeyInitialized = false

    private var pendingResult:    FlutterResult?
    private var timeoutTimer:     Timer?
    private var configTimer:      Timer?
    private var pendingLocalName  = ""
    private var pendingUUID       = ""

    // Retained for the full duration of a transfer — the SDK's async BLE
    // callbacks fire well after doTransfer() returns, and without a strong
    // reference here ARC can deallocate the peripheral mid-transfer,
    // crashing the SDK's callback with EXC_BAD_ACCESS.
    private var activePeripheral: OmronPeripheral?

    // manager.start() reloads the SDK's device-list config on a background
    // queue (OmronPeripheralManager initDeviceConfigurations, confirmed via
    // `bt all` on a hung repro: thread stopped inside that block's XML
    // parse). Calling start() again on every transfer re-triggers that
    // background reload while our own getConfiguration() call below parses
    // the same config on the main thread — two threads mutating the same
    // NSMutableDictionary via NSXMLParser is what produced every crash
    // variant seen in this investigation (dangling objc_msgSend, malloc
    // double-free, heap corruption traps). Starting the manager only once
    // per process avoids re-entering that reload path.
    private var managerStarted = false

    // Cached after the first getConfiguration() call — see doTransfer() for why.
    private var cachedConfig: OmronPeripheralManagerConfig?

    // Typed helper — OmronPeripheralManager.sharedManager() returns id (Any?) in Swift
    private var manager: OmronPeripheralManager {
        return OmronPeripheralManager.sharedManager() as! OmronPeripheralManager
    }

    // MARK: - FlutterPlugin

    static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(
            name: channelName,
            binaryMessenger: registrar.messenger()
        )
        let instance = OmronBpPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }

    // MARK: - Method call handler

    func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "transfer":
            guard let args = call.arguments as? [String: Any],
                  let localName = args["localName"] as? String,
                  let uuid      = args["uuid"]      as? String,
                  (!localName.isEmpty || !uuid.isEmpty) else {
                result(FlutterError(code: "INVALID_ARGS",
                                    message: "localName or uuid is required",
                                    details: nil))
                return
            }
            guard pendingResult == nil else {
                result(FlutterError(code: "BUSY",
                                    message: "Transfer already in progress",
                                    details: nil))
                return
            }
            pendingResult = result
            startTransfer(localName: localName, uuid: uuid)

        case "cancel":
            cancelPending()
            result(nil)

        default:
            result(FlutterMethodNotImplemented)
        }
    }

    // MARK: - Transfer entry point

    private func startTransfer(localName: String, uuid: String) {
        timeoutTimer = Timer.scheduledTimer(
            withTimeInterval: OmronBpPlugin.timeoutSecs,
            repeats: false
        ) { [weak self] _ in
            self?.deliverError(code: "TIMEOUT",
                               message: "Transfer timed out after \(Int(OmronBpPlugin.timeoutSecs))s")
        }

        // setAPIKey() must be called exactly once per process lifetime — the
        // Android build (OmronBpPlugin.kt) hit the same class of bug and
        // documented it: re-calling this on every transfer re-triggers the
        // SDK's internal auth/session init, which was corrupting session
        // state on repeated transfers.
        if !OmronBpPlugin.apiKeyInitialized {
            OmronBpPlugin.apiKeyInitialized = true
            manager.setAPIKey(OmronBpPlugin.apiKey, options: nil)
        }

        if let cached = manager.retrieveManagerConfiguration() as? [AnyHashable: Any],
           !cached.isEmpty {
            NSLog("[OmronBP] Config cached, starting transfer")
            doTransfer(localName: localName, uuid: uuid)
        } else {
            NSLog("[OmronBP] Config not cached, waiting for download...")
            waitForConfigThenTransfer(localName: localName, uuid: uuid)
        }
    }

    // MARK: - Wait for first-install config download

    private func waitForConfigThenTransfer(localName: String, uuid: String) {
        pendingLocalName = localName
        pendingUUID      = uuid

        configTimer = Timer.scheduledTimer(
            withTimeInterval: OmronBpPlugin.configWaitSecs,
            repeats: false
        ) { [weak self] _ in
            NotificationCenter.default.removeObserver(
                self as Any,
                name: .OMRONBLEConfigDeviceAvailability,
                object: nil
            )
            self?.deliverError(code: "CONFIG_TIMEOUT",
                               message: "Omron config download timed out. Check internet and retry.")
        }

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(configAvailabilityNotification(_:)),
            name: .OMRONBLEConfigDeviceAvailability,
            object: nil
        )
    }

    @objc private func configAvailabilityNotification(_ notification: Notification) {
        // NotificationCenter delivers on whatever thread posted it — the SDK's
        // config download likely posts off-main. Hop to main before touching
        // any plugin state, same reasoning as the SDK completion blocks below.
        let rawValue = (notification.object as? NSNumber)?.uint32Value ?? 0
        DispatchQueue.main.async {
            self.configTimer?.invalidate()
            self.configTimer = nil
            NotificationCenter.default.removeObserver(
                self,
                name: .OMRONBLEConfigDeviceAvailability,
                object: nil
            )

            // notification.object is NSNumber with OMRONConfigurationStatus raw value
            let isSuccess = rawValue == UInt32(OMRONConfigurationFileSuccess.rawValue)
            let isUpdateError = rawValue == UInt32(OMRONConfigurationFileUpdateError.rawValue)

            if isSuccess || isUpdateError {
                NSLog("[OmronBP] Config downloaded (rawValue=\(rawValue)), starting transfer")
                self.doTransfer(localName: self.pendingLocalName, uuid: self.pendingUUID)
            } else {
                self.deliverError(code: "CONFIG_ERROR",
                             message: "Omron config download failed with rawValue: \(rawValue)")
            }
        }
    }

    // MARK: - Core transfer

    private func doTransfer(localName: String, uuid: String) {
        // getConfiguration() triggers OmronPeripheralManager.loadConfiguration(),
        // which re-parses the SDK's device-list config XML on whatever thread
        // calls it — every single time, not just once. Confirmed via `bt all`
        // on a hung repro: this exact line (getConfiguration()! below) was the
        // frame the main thread was stuck in, at the same moment a background
        // queue was independently mid-parse of the same XML into the same
        // NSMutableDictionary. Caching the config after the first call avoids
        // re-entering that non-thread-safe reparse on every later transfer.
        let config: OmronPeripheralManagerConfig
        if let cached = cachedConfig {
            config = cached
        } else {
            config = manager.getConfiguration()!
            cachedConfig = config
        }
        // Re-applied on every call, even when config is cached — the SDK
        // clears userHashId internally after a transfer completes (observed:
        // reusing the cached object without resetting it caused
        // "User Hash for device configuration/encryption missing" on the
        // second consecutive transfer).
        config.userHashId       = OmronBpPlugin.userHash
        config.timeoutInterval  = OmronBpPlugin.timeoutSecs
        config.enableAllDataRead = true
        manager.setConfiguration(config)
        if !managerStarted {
            managerStarted = true
            manager.start()
        }

        guard let peripheral = OmronPeripheral(localName: localName, andUUID: uuid) else {
            deliverError(code: "INIT_FAILED", message: "Could not create OmronPeripheral")
            return
        }
        activePeripheral = peripheral

        // "Error trying to transfer data from invalid user profile" traced to
        // the wrong localName being passed in, not the requested user slot —
        // the SDK's bundled device catalog (BatchDL_AP.../deviceList XML)
        // resolves HEM-7140T1-AP via <bleScanLocalName>^BLE[s|S]mart_0000047E.*
        // matched against the PRE-BOND advertised name, never the post-bond
        // GAP name. iOS's platformName/localName switches to the post-bond
        // GATT Device Name ("HEM-7140T1") once bonded, so the catalog lookup
        // silently failed for every user slot regardless of which was tried.
        // The Dart side now persists and passes the raw advertised name
        // ("BLESmart_0000047E...") instead — see savedDeviceSdkName in
        // bp_device_controller.dart. Slot reverted to Android's known-good
        // value; the catalog's noOfUsers=2 for this model means slots 2-4
        // are out of range and were never a meaningful test.
        let users: [NSNumber] = [1]
        NSLog("[OmronBP] startDataTransfer localName=\(localName) uuid=\(uuid) users=\(users)")

        manager.startDataTransfer(
            from: peripheral,
            withUsers: users,
            withWait: true
        ) { [weak self] (resultPeripheral: OmronPeripheral?, error: Error?) in
            // The SDK invokes completion blocks off the main thread (observed
            // on com.apple.root.default-qos). All plugin state below —
            // activePeripheral, pendingResult, timeoutTimer — plus Timer
            // invalidation, must only ever be touched from one thread.
            // Hopping to main here confines everything downstream to main,
            // eliminating the cross-thread races that were producing
            // EXC_BAD_ACCESS / malloc heap-corruption traps.
            DispatchQueue.main.async {
                guard let self = self else { return }
                NSLog("[OmronBP] startTransfer callback on main=\(Thread.isMainThread)")

                if let error = error {
                    NSLog("[OmronBP] startTransfer error: \(error.localizedDescription)")
                    self.deliverError(code: "START_FAILED", message: error.localizedDescription)
                    return
                }
                guard resultPeripheral != nil else {
                    self.deliverError(code: "START_FAILED", message: "No peripheral returned from startTransfer")
                    return
                }

                NSLog("[OmronBP] startTransfer success, calling endDataTransfer...")
                self.manager.endDataTransferFromPeripheral { [weak self] (endPeripheral: OmronPeripheral?, endError: Error?) in
                    DispatchQueue.main.async {
                        guard let self = self else { return }

                        if let endError = endError {
                            NSLog("[OmronBP] endTransfer error: \(endError.localizedDescription)")
                            self.deliverError(code: "END_FAILED", message: endError.localizedDescription)
                            return
                        }
                        guard let endPeripheral = endPeripheral else {
                            self.deliverError(code: "END_FAILED", message: "No peripheral returned from endTransfer")
                            return
                        }

                        NSLog("[OmronBP] endTransfer success, extracting vital data...")
                        self.extractAndDeliver(peripheral: endPeripheral)
                    }
                }
            }
        }
    }

    // MARK: - Data extraction
    //
    // No explicit disconnect is issued here — the SDK manages the BLE
    // connection lifecycle itself after endDataTransferFromPeripheral.
    // An earlier attempt to force a disconnectPeripheral call here was
    // rejected by the SDK ("Your processing request was not accepted") and
    // left it in a bad internal state, which is what caused the malloc
    // double-free crash on the following TRANSFER tap. activePeripheral
    // stays retained until the next doTransfer() call overwrites it.

    private func extractAndDeliver(peripheral: OmronPeripheral) {
        guard let vitalData = peripheral.getVitalData() as? [AnyHashable: Any] else {
            deliverError(code: "NO_DATA", message: "No vital data returned by device")
            return
        }

        guard let bpList = vitalData[OMRONVitalDataBloodPressureKey] as? [[AnyHashable: Any]],
              !bpList.isEmpty else {
            deliverError(code: "NO_BP_RECORDS",
                         message: "Device returned 0 BP records — take a measurement on the device first")
            return
        }

        NSLog("[OmronBP] Total BP records received: \(bpList.count)")
        for (i, r) in bpList.enumerated() { NSLog("[OmronBP] BP record[\(i)]: \(r)") }

        // Pick the record with the highest sequence number (most recent measurement).
        // bpList order is not guaranteed — using last risks returning an older record
        // when multiple records are present (enableAllDataRead = true returns all).
        let reading = bpList.max(by: {
            let s1 = ($0[OMRONVitalDataSequenceKey] as? NSNumber)?.intValue ?? 0
            let s2 = ($1[OMRONVitalDataSequenceKey] as? NSNumber)?.intValue ?? 0
            return s1 < s2
        }) ?? bpList.last!

        NSLog("[OmronBP] Selected BP record: \(reading)")

        guard let sys = (reading[OMRONVitalDataSystolicKey]  as? NSNumber)?.intValue,
              let dia = (reading[OMRONVitalDataDiastolicKey] as? NSNumber)?.intValue else {
            deliverError(code: "PARSE_FAILED",
                         message: "Systolic/diastolic not found. Keys: \(reading.keys)")
            return
        }

        NSLog("[OmronBP] Delivering sys=\(sys) dia=\(dia)")
        deliverSuccess(systolic: sys, diastolic: dia)
    }

    // MARK: - Result delivery

    private func deliverSuccess(systolic: Int, diastolic: Int) {
        cancelTimeout()
        // Do NOT clear activePeripheral here — the SDK continues background
        // teardown (disconnect/cleanup) on its own queue after invoking this
        // completion, and freeing the peripheral while that's in flight is
        // what caused the EXC_BAD_ACCESS. It's left retained until the next
        // doTransfer() call overwrites it with a fresh instance.
        guard let result = pendingResult else { return }
        pendingResult = nil
        DispatchQueue.main.async {
            result(["systolic": systolic, "diastolic": diastolic])
        }
    }

    private func deliverError(code: String, message: String) {
        NSLog("[OmronBP] ERROR [\(code)]: \(message)")
        cancelTimeout()
        // See note in deliverSuccess — keep activePeripheral retained.
        guard let result = pendingResult else { return }
        pendingResult = nil
        DispatchQueue.main.async {
            result(FlutterError(code: code, message: message, details: nil))
        }
    }

    private func cancelPending() {
        cancelTimeout()
        // See note in deliverSuccess — keep activePeripheral retained; the
        // SDK operation isn't actually aborted, only the Flutter-side promise.
        pendingResult = nil
    }

    private func cancelTimeout() {
        timeoutTimer?.invalidate()
        timeoutTimer = nil
        configTimer?.invalidate()
        configTimer = nil
    }
}