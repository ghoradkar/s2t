import Flutter
import UIKit
import SafeySpirometerSDK
import CoreBluetooth

@objc class SafeySpirometerPlugin: NSObject, FlutterPlugin, FlutterStreamHandler,
    SafeyScannerCallback, SafeyConnectionCallback, SafeyDeviceCallback,
    SafeyTestTypeCallback, SafeyTestCallback, SafeyErrorCallback {

    private static let methodChannelName = "com.s2toperational/safey_spirometry"
    private static let eventChannelName  = "com.s2toperational/safey_spirometry_events"

    private var eventSink: FlutterEventSink?
    private var lungManager: SafeyLungManager?
    private var discoveredDevices = [String: SafeySpirometerDevice]()
    private var currentDevice: SafeySpirometerDevice?
    private var cachedPerson: SafeyPerson?

    // MARK: - FlutterPlugin

    static func register(with registrar: FlutterPluginRegistrar) {
        let methodChannel = FlutterMethodChannel(
            name: methodChannelName,
            binaryMessenger: registrar.messenger()
        )
        let instance = SafeySpirometerPlugin()
        registrar.addMethodCallDelegate(instance, channel: methodChannel)

        let eventChannel = FlutterEventChannel(
            name: eventChannelName,
            binaryMessenger: registrar.messenger()
        )
        eventChannel.setStreamHandler(instance)
    }

    // MARK: - FlutterStreamHandler

    func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        eventSink = events
        return nil
    }

    func onCancel(withArguments arguments: Any?) -> FlutterError? {
        eventSink = nil
        return nil
    }

    // MARK: - FlutterPlugin method handler

    func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        switch call.method {
        case "scan":
            let args = call.arguments as? [String: Any] ?? [:]
            handleScan(args: args)
            result(nil)
        case "connect":
            let args = call.arguments as? [String: Any] ?? [:]
            let address = args["address"] as? String ?? ""
            handleConnect(address: address)
            result(nil)
        case "startTest":
            handleStartTest()
            result(nil)
        case "disconnect":
            handleDisconnect()
            result(nil)
        default:
            result(FlutterMethodNotImplemented)
        }
    }

    // MARK: - Actions

    private func handleScan(args: [String: Any]) {
        let genderInt = args["gender"] as? Int ?? 1
        let weight    = args["weight"] as? Int ?? 60
        let age       = args["age"] as? Double ?? 30.0
        let height    = args["height"] as? Int ?? 165

        let gender: Gender = genderInt == 2 ? .Female : .Male
        let dob = Calendar.current.date(byAdding: .year, value: -Int(age), to: Date()) ?? Date()

        let person = SafeyPerson(
            firstName: "Patient",
            lastName: "",
            gender: gender,
            library: .NHANES3,
            ethnicity: .Caucasian,
            dateOfBirth: dob,
            heightInCentimeters: height,
            weightInKg: Double(weight)
        )
        cachedPerson = person
        discoveredDevices.removeAll()

        lungManager = SafeyLungManager(person: person)
        lungManager?.scannerCallback    = self
        lungManager?.connectionCallback = self
        lungManager?.deviceCallback     = self
        lungManager?.testTypeCallback   = self
        lungManager?.testCallback       = self
        lungManager?.errorCallback      = self

        do {
            try lungManager?.scanDevice()
        } catch {
            sendEvent(["type": "error", "message": "Scan failed: \(error.localizedDescription)"])
        }
    }

    private func handleConnect(address: String) {
        guard let device = discoveredDevices[address] else {
            sendEvent(["type": "error", "message": "Device not found: \(address)"])
            return
        }
        currentDevice = device
        lungManager?.connectToDevice(Device: device)
    }

    private func handleStartTest() {
        lungManager?.startTestSession(withPreTestData: nil)
        DispatchQueue.main.asyncAfter(deadline: .now() + 2.0) { [weak self] in
            self?.lungManager?.startTrial()
        }
    }

    private func handleDisconnect() {
        lungManager?.disconnectDevice()
        discoveredDevices.removeAll()
        currentDevice = nil

        // Recreate manager so it is ready for the next scan
        if let person = cachedPerson {
            lungManager = SafeyLungManager(person: person)
            lungManager?.scannerCallback    = self
            lungManager?.connectionCallback = self
            lungManager?.deviceCallback     = self
            lungManager?.testTypeCallback   = self
            lungManager?.testCallback       = self
            lungManager?.errorCallback      = self
        } else {
            lungManager = nil
        }
    }

    // MARK: - SafeyScannerCallback

    func safeyLungManager(_ safeyLungManager: SafeyLungManager,
                          didDiscoverSafeyDevice device: SafeySpirometerDevice) {
        let uuid = device.peripheral?.identifier.uuidString ?? UUID().uuidString
        discoveredDevices[uuid] = device
        sendEvent([
            "type":    "deviceFound",
            "name":    device.deviceName,
            "address": uuid,
        ])
    }

    // MARK: - SafeyConnectionCallback

    func safeyLungManager(_ safeyLungManager: SafeyLungManager,
                          device: SafeySpirometerDevice,
                          isConnected status: Bool) {
        if status {
            currentDevice = device
            lungManager?.getAvailableTestTypes()
            lungManager?.getBatteryPercentage()
            sendEvent(["type": "connected"])
        } else {
            sendEvent(["type": "disconnected"])
        }
    }

    // MARK: - SafeyDeviceCallback

    func safeyLungManager(_ safeyLungManager: SafeyLungManager,
                          safeyDevice device: SafeySpirometerDevice,
                          batteryStatus battery: String) {
        let uuid = device.peripheral?.identifier.uuidString ?? ""
        sendEvent([
            "type":    "batteryStatus",
            "battery": battery,
            "name":    device.deviceName,
            "address": uuid,
        ])
    }

    // MARK: - SafeyTestTypeCallback

    func safeyLungManager(_ safeyLungManager: SafeyLungManager,
                          forSafeyDevice device: SafeySpirometerDevice,
                          availableTestType: [TestType]) {
        // selectTestType callback handles the response
    }

    func selectTestType(_ safeyLungManager: SafeyLungManager,
                        forSafeyDevice device: SafeySpirometerDevice) -> TestType? {
        return .FEVC
    }

    // MARK: - SafeyTestCallback

    func safeyLungManager(_ safeyLungManager: SafeyLungManager,
                          onProgressChange progress: Double,
                          flow: [Double], volume: [Double], time: [Double]) {
        // SDK scale is unconfirmed. If progress arrives as 0.0–1.0 instead of 0–100,
        // change Int(progress) to Int(progress * 100).
        let progressInt = Int(min(progress, 100.0))
        sendEvent(["type": "progress", "value": progressInt])
    }

    func safeyLungManager(_ safeyLungManager: SafeyLungManager,
                          testResults testResult: TestResultsModel) {
        let session      = testResult.preTestSessionResults ?? testResult.postTestSessionResults
        let sessionScore = session?.sessionScore ?? ""
        let trialCount   = session?.trialsList.count ?? 0
        let jsonStr      = serializeTestResults(testResult)

        sendEvent([
            "type":         "testResult",
            "json":         jsonStr,
            "trialCount":   trialCount,
            "sessionScore": sessionScore,
        ])
    }

    func safeyLungManager(_ safeyLungManager: SafeyLungManager,
                          invalidManeuver message: String) {
        sendEvent(["type": "info", "code": "INF_03"])
    }

    // MARK: - SafeyErrorCallback

    func safeyLungManager(_ safeyLungManager: SafeyLungManager,
                          infostate state: SafeyDeviceManagerInfoState,
                          detailMessage message: String) {
        let code: String
        switch state {
        case .INF_01:  code = "INF_01"   // device not found
        case .INF_02:  code = "INF_22"   // connection error
        case .INF_03:  code = "INF_03"   // invalid maneuver
        case .INF_101: code = "INF_05"   // start blowing
        case .INF_102: code = "INF_12"   // keep blowing
        case .INF_103: code = "INF_10"   // done
        case .INF_104: code = "INF_07"   // checking results
        case .INF_105: code = "INF_13"   // insufficient blow
        case .INF_106: code = "INF_17"   // timeout
        case .INF_201: code = "INF_18"   // searching
        case .INF_202: code = "INF_19"   // device ready
        case .INF_203: code = "INF_20"   // idle
        @unknown default: code = "INF_\(state.rawValue)"
        }
        sendEvent(["type": "info", "code": code])
    }

    // MARK: - JSON serialization

    private func serializeTestResults(_ model: TestResultsModel) -> String {
        var allTrials = [[String: Any]]()
        if let pre = model.preTestSessionResults {
            allTrials += buildTrials(from: pre, isPost: false)
        }
        if let post = model.postTestSessionResults {
            allTrials += buildTrials(from: post, isPost: true)
        }

        let session           = model.preTestSessionResults ?? model.postTestSessionResults
        let sessionScore      = session?.sessionScore ?? ""
        let suggestedDiagnosis = session?.bestTrialSuggestedDiagnosis ?? ""

        let clean: [String: Any] = [
            "suggestedDiagnosis": suggestedDiagnosis,
            "sessionScore":       sessionScore,
            "testResults":        allTrials,
        ]

        guard let data = try? JSONSerialization.data(withJSONObject: clean),
              let str  = String(data: data, encoding: .utf8) else {
            return "{}"
        }
        return str
    }

    private func buildTrials(from session: TestSessionModel, isPost: Bool) -> [[String: Any]] {
        let bestTrialNo = session.bestTrialNumber ?? -1

        return session.trialsList
            .sorted(by: { $0.key < $1.key })
            .map { (_, trial) -> [String: Any] in
                let trialNo = trial.trialNumber ?? 0

                // Zip flow/volume/time arrays into graphPoints
                let count = min(trial.flowArray.count,
                                min(trial.volumeArray.count, trial.timeArray.count))
                var graphPoints = [[String: Any]]()
                for i in 0..<count {
                    graphPoints.append([
                        "flow":      trial.flowArray[i],
                        "volume":    trial.volumeArray[i],
                        "second":    trial.timeArray[i],
                        "direction": 0,
                    ])
                }

                let measuredValues: [[String: Any]] = trial.parameters.map { p in
                    [
                        "measurement":   p.name,
                        "measuredValue": Double(p.value) ?? 0.0,
                        "predicted":     p.predicted,
                        "unit":          p.unit,
                        "predictedPer":  Double(p.predictedPercentage) ?? 0.0,
                        "LLN":           p.lln,
                        "ULN":           p.uln,
                        "zScore":        p.zScore,
                    ]
                }

                return [
                    "trialNo":        trialNo,
                    "isPost":         isPost,
                    "isBest":         trialNo == bestTrialNo,
                    "message":        trial.suggestedDiagnosis,
                    "graphPoints":    graphPoints,
                    "measuredValues": measuredValues,
                ]
            }
    }

    // MARK: - Helpers

    private func sendEvent(_ data: [String: Any]) {
        DispatchQueue.main.async { [weak self] in
            self?.eventSink?(data)
        }
    }
}
