import Flutter
import UIKit

@objc class OmronBpPlugin: NSObject, FlutterPlugin {

    private static let channelName    = "com.s2t.operational/omron_bp"
    private static let apiKey         = "8ACE95FC-D532-4004-A88E-873921B18539"
    private static let userHash       = "s2t.operational@s2t.com"
    private static let timeoutSecs    = 60.0
    private static let configWaitSecs = 30.0

    private var pendingResult:    FlutterResult?
    private var timeoutTimer:     Timer?
    private var configTimer:      Timer?
    private var pendingLocalName  = ""
    private var pendingUUID       = ""

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

        manager.setAPIKey(OmronBpPlugin.apiKey, options: nil)

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
        configTimer?.invalidate()
        configTimer = nil
        NotificationCenter.default.removeObserver(
            self,
            name: .OMRONBLEConfigDeviceAvailability,
            object: nil
        )

        // notification.object is NSNumber with OMRONConfigurationStatus raw value
        let rawValue = (notification.object as? NSNumber)?.uint32Value ?? 0
        let isSuccess = rawValue == UInt32(OMRONConfigurationFileSuccess.rawValue)
        let isUpdateError = rawValue == UInt32(OMRONConfigurationFileUpdateError.rawValue)

        if isSuccess || isUpdateError {
            NSLog("[OmronBP] Config downloaded (rawValue=\(rawValue)), starting transfer")
            doTransfer(localName: pendingLocalName, uuid: pendingUUID)
        } else {
            deliverError(code: "CONFIG_ERROR",
                         message: "Omron config download failed with rawValue: \(rawValue)")
        }
    }

    // MARK: - Core transfer

    private func doTransfer(localName: String, uuid: String) {
        let config = manager.getConfiguration()!
        config.userHashId       = OmronBpPlugin.userHash
        config.timeoutInterval  = OmronBpPlugin.timeoutSecs
        config.enableAllDataRead = true
        manager.setConfiguration(config)
        manager.start()

        guard let peripheral = OmronPeripheral(localName: localName, andUUID: uuid) else {
            deliverError(code: "INIT_FAILED", message: "Could not create OmronPeripheral")
            return
        }

        let users: [NSNumber] = [1]
        NSLog("[OmronBP] startDataTransfer localName=\(localName) uuid=\(uuid)")

        manager.startDataTransfer(
            from: peripheral,
            withUsers: users,
            withWait: true
        ) { [weak self] (resultPeripheral: OmronPeripheral?, error: Error?) in
            guard let self = self else { return }

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

    // MARK: - Data extraction

    private func extractAndDeliver(peripheral: OmronPeripheral) {
        guard let vitalData = peripheral.getVitalData() as? [AnyHashable: Any] else {
            deliverError(code: "NO_DATA", message: "No vital data returned by device")
            return
        }

        guard let bpList = vitalData[OMRONVitalDataBloodPressureKey] as? [[AnyHashable: Any]],
              let reading = bpList.last else {
            deliverError(code: "NO_BP_RECORDS",
                         message: "Device returned 0 BP records — take a measurement on the device first")
            return
        }

        NSLog("[OmronBP] BP record: \(reading)")

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
        guard let result = pendingResult else { return }
        pendingResult = nil
        DispatchQueue.main.async {
            result(["systolic": systolic, "diastolic": diastolic])
        }
    }

    private func deliverError(code: String, message: String) {
        NSLog("[OmronBP] ERROR [\(code)]: \(message)")
        cancelTimeout()
        guard let result = pendingResult else { return }
        pendingResult = nil
        DispatchQueue.main.async {
            result(FlutterError(code: code, message: message, details: nil))
        }
    }

    private func cancelPending() {
        cancelTimeout()
        pendingResult = nil
    }

    private func cancelTimeout() {
        timeoutTimer?.invalidate()
        timeoutTimer = nil
        configTimer?.invalidate()
        configTimer = nil
    }
}