package com.myhindlab.abkat.glucose

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.myhindlab.abkat.glucose.state.GlucoseViewModel
import com.bioland.bledemo.glucose.utils.GlucoseMeterManager
import java.util.UUID

@Suppress("MissingPermission")
class GlucoseMeterBLE(val context: Context, private val viewModel: GlucoseViewModel) {
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val scanner = bluetoothAdapter.bluetoothLeScanner
    private var bluetoothGatt: BluetoothGatt? = null
    private val glucoseServiceUUID = UUID.fromString("00001808-0000-1000-8000-00805f9b34fb")
    private val glucoseMeasurementUUID = UUID.fromString("00002A18-0000-1000-8000-00805f9b34fb")
    private val racpUUID = UUID.fromString("00002A52-0000-1000-8000-00805f9b34fb")
    private val BATTERY_SERVICE_UUID = UUID.fromString("0000180F-0000-1000-8000-00805f9b34fb")
    private val BATTERY_CHAR_UUID = UUID.fromString("00002A19-0000-1000-8000-00805f9b34fb")

    private val glucoseMeterManager = GlucoseMeterManager(context, viewModel)

    fun startScan() {
        val filters = listOf(ScanFilter.Builder().setDeviceName("Meter").build())
        val settings =
            ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
        viewModel.updateScannerStatus("Scanning for devices...")
        scanner?.startScan(scanCallback)

    }

    fun stopScan() {
        viewModel.updateScannerStatus("Scan stopped")
        scanner?.stopScan(scanCallback)

    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            Log.i("scanCallback", "onScanResult: ${result.device.name}")
            val deviceName = result.device.name ?: return
            if (deviceName.startsWith("Meter", ignoreCase = false)&&!deviceName.contains("spiro",ignoreCase = true)) {
                Log.d("BLE", "Found matching device: $deviceName")
                // Connect or do something with the device
                scanner?.stopScan(this)
                viewModel.updateScannerStatus("Device found! Connecting...")
                connectToDevice(result.device)
            }

        }
    }

    fun connectToDevice(device: BluetoothDevice) {
        Log.d("Bluetooth", "Connecting to ${device.name} (${device.address})")

        if (device.bondState != BluetoothDevice.BOND_BONDED) {
            Log.d("Bluetooth", "Device not paired. Initiating pairing...")
            viewModel.updateScannerStatus("Device not paired. Initiating pairing...")
            removeBond(device)  // Ensure the device is unpaired
            device.createBond()  // 🔹 Force pairing request
        } else {
            Log.d("Bluetooth", "Device already paired. Connecting...")
            viewModel.updateScannerStatus("Device already paired. Connecting...")
            bluetoothGatt = device.connectGatt(context, false, gattCallback)
        }
        viewModel.updateMacAddress(device.address)

    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("Bluetooth", "Connected to device. Discovering services...")
                viewModel.updateScannerStatus("Connected to device. Discovering services...")

                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("Bluetooth", "Disconnected from device.")
                viewModel.updateScannerStatus("Disconnected")
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {

                Log.d("Bluetooth", "Services discovered. Reading device name...")
                //0x2A04
                val deviceNameCharacteristic =
                    gatt?.getService(UUID.fromString("00001800-0000-1000-8000-00805f9b34fb")) // Generic Access Service
                        ?.getCharacteristic(UUID.fromString("00002A00-0000-1000-8000-00805f9b34fb")) // Device Name Characteristic

                if (deviceNameCharacteristic != null) {
                    Log.d("Bluetooth", "Device name characteristic found. Reading value...")
                    viewModel.updateScannerStatus("Device name characteristic found. Reading value...")
                    gatt.readCharacteristic(deviceNameCharacteristic)

                    // ✅ Now request glucose records
//                    requestStoredGlucoseRecords(gatt)


                } else {
                    Log.e("Bluetooth", "Device name characteristic NOT found!")
                    viewModel.updateScannerStatus("Device name characteristic NOT found!")

                }

//                val batteryService = gatt?.getService(BATTERY_SERVICE_UUID)
//                val batteryChar = batteryService?.getCharacteristic(BATTERY_CHAR_UUID)
//                if (batteryChar != null) {
//                    val result = gatt.readCharacteristic(batteryChar)
//                    Log.d("Battery", "🔋 Battery read initiated: $result")
//                } else {
//                    Log.w("Battery", "Battery characteristic not found!")
//                }



            } else {
                Log.e("Bluetooth", "Service discovery failed with status: $status")
                viewModel.updateScannerStatus("Service discovery failed with status: $status")

            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                when (characteristic?.uuid) {
                    UUID.fromString("00002A00-0000-1000-8000-00805f9b34fb") -> { // Device Name
                        val deviceName = characteristic?.getStringValue(0) // Read as String
                        Log.d("Bluetooth", "Device Name: $deviceName")
                        viewModel.updateDeviceName("$deviceName")

                        Log.d(
                            "Bluetooth",
                            "Services discovered. Enabling notifications for glucose..."
                        )
                        enableGlucoseNotifications(gatt)
                        Handler(Looper.getMainLooper()).postDelayed({
                            readBatteryCharacteristic(gatt)

                            Handler(Looper.getMainLooper()).postDelayed({
                                requestStoredGlucoseRecords(gatt)

                            }, 1000)
                        }, 1000)

                        // Small delay to ensure notifications are fully enabled


                    }

                    racpUUID -> { // RACP Response
                        val response = characteristic?.value
                        Log.d(
                            "Bluetooth",
                            "📩 Received RACP Response: ${
                                response?.joinToString(" ") {
                                    "%02X".format(it)
                                }
                            }"
                        )

                        if (response != null && response[0] == 0x06.toByte()) {
                            Log.d(
                                "Bluetooth",
                                "✅ RACP Response Confirmed! Data transfer should start now."
                            )
                        } else {
                            Log.d(
                                "Bluetooth",
                                "✅ response is null."
                            )
                        }
                    }

                    BATTERY_CHAR_UUID -> {
                        val batteryLevel =
                            characteristic?.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)
                        Log.d("Battery", "🔋 Battery Level (0x2A0F): $batteryLevel%")
                        viewModel.updateBatteryLevel("$batteryLevel%")

                    }

                    else -> Log.d(
                        "Bluetooth",
                        "Unknown characteristic read: ${characteristic?.uuid}"
                    )
                }
            } else {
                Log.e("Bluetooth", "Characteristic read failed with status: $status")
            }
        }


        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            val uuid = characteristic?.uuid
            val rawData =
                characteristic?.value?.joinToString(" ") { "%02X".format(it) } ?: "No Data"
            Log.d("Bluetooth", "📩 Raw Data from $uuid: $rawData")

            viewModel.updateRawData(rawData)

            when (uuid) {
                glucoseMeasurementUUID -> { // Glucose Measurement
                    characteristic?.value?.let { data ->
                        val glucoseValue = parseGlucoseMeasurement(data)
                        Log.d("Bluetooth", "📊 Glucose Measurement: $glucoseValue mg/dL")
//                        viewModel.updateData("$glucoseValue mg/dL")

                        val timestamp = parseGlucoseTimestamp(data)
                        Log.d("Bluetooth", "📊 Glucose Time: $timestamp")
                        viewModel.updateData("$glucoseValue mg/dL $timestamp")

                        // ✅ Extract Battery Level only if available
//                        if (data.size > 6) { // Ensure valid index
//                            val batteryLevel = data[5].toInt() and 0xFF
//                            Log.d("Bluetooth", "🔋 Battery Level from 0x2A18: $batteryLevel%")
//                            viewModel.updateBatteryLevel("$batteryLevel%")
//                        }
//                        if (data.size > 11) {
//                            val batteryLevel = data[10].toInt() and 0xFF
//                            Log.d("Bluetooth", "🔋 Battery Level from 0x2A18: $batteryLevel%")
//                            viewModel.updateBatteryLevel("$batteryLevel%")
//                        }
                    }
                }

                UUID.fromString("00002A34-0000-1000-8000-00805f9b34fb") -> { // Glucose Measurement Context
                    Log.d("Bluetooth", "📌 Received Glucose Context Data: $rawData")
                }

                racpUUID -> { // RACP Response
                    characteristic?.value?.let { response ->
                        Log.d(
                            "Bluetooth",
                            "📩 Received RACP Response: ${
                                response.joinToString(" ") {
                                    "%02X".format(it)
                                }
                            }"
                        )

                        if (response.isNotEmpty() && response[0] == 0x06.toByte()) {
                            Log.d(
                                "Bluetooth",
                                "✅ RACP Response Confirmed! Data transfer should start now."
                            )
                        }
                    }
                }

                else -> Log.d("Bluetooth", "Unknown notification received from $uuid")
            }
        }


        override fun onDescriptorWrite(
            gatt: BluetoothGatt,
            descriptor: BluetoothGattDescriptor,
            status: Int
        ) {
            Log.d("Bluetooth", "Descriptor written. Status: $status")
            viewModel.updateScannerStatus("Descriptor written. Status: $status")

        }

    }


//    fun parseGlucoseMeasurement(data: ByteArray): Float {
//        //1A 01 00 E9 07 03 13 14 26 2D 79 B0 00 00 00
//        if (data.size < 11) return -1f // Ensure valid data
//
//        // Extract glucose value (Little-endian: LSB first)
//        val rawValue = ((data[10].toInt() and 0xFF) shl 8) or (data[11].toInt() and 0xFF)
//
//        // ✅ Apply correct scaling factor (likely 256)
//        return (data[10].toInt() and 0xFF).toFloat()
//    }


    fun parseGlucoseMeasurement(data: ByteArray): Float {
        // Need at least 12 bytes (flags + seq(2) + base time(7) + sfloat(2))
        if (data.size < 12) return -1f

        val flags = data[0].toInt() and 0xFF

        // SFLOAT is at bytes 10 (LSB) and 11 (MSB) — little-endian
        val lsb = data[10].toInt() and 0xFF
        val msb = data[11].toInt() and 0xFF
        val raw16 = (msb shl 8) or lsb // 16-bit value

        // Extract mantissa (12 bits) and exponent (4 bits) from SFLOAT
        var mantissa = raw16 and 0x0FFF
        var exponent = (raw16 shr 12) and 0x0F

        // Mantissa is signed 12-bit (two's complement)
        if (mantissa >= 0x800) mantissa -= 0x1000

        // Exponent is signed 4-bit (two's complement)
        if (exponent >= 0x8) exponent -= 0x10

        val sfloatValue = mantissa * Math.pow(10.0, exponent.toDouble()).toFloat()

        // Determine units:
        // Common mapping for Glucose Measurement flag (bit 2): 0 = kg/L, 1 = mol/L
        // Note: If your device uses a different flag map, adjust this mask.
        val UNIT_MOLAR_MASK = 0x04 // bit 2
        val isMolPerL = (flags and UNIT_MOLAR_MASK) != 0

        return if (!isMolPerL) {
            // Units = kg/L  -> convert to mg/dL
            // 1 kg/L = 100000 mg/dL (derived: 1 kg/L = 1000 g/L ; 1 g/L = 100 mg/dL => 1000*100 = 100000)
            (sfloatValue * 100_000f).let { String.format("%.2f", it).toFloat() }
        } else {
            // Units = mol/L  -> convert to mmol/L and mg/dL
            // sfloatValue in mol/L -> mmol/L = sfloatValue * 1000
            val mmolPerL = sfloatValue * 1000f
            val mgPerDl = mmolPerL * 18.01559f // glucose molar mass conversion: 1 mmol/L ≈ 18.01559 mg/dL
            String.format("%.2f", mgPerDl).toFloat()
        }
    }

    fun parseGlucoseTimestamp(data: ByteArray): String {
        if (data.size < 10) return "Invalid Data"

        // ✅ Extract Year (Little-Endian format) → SWAP `data[4]` and `data[3]`
        val year = ((data[4].toInt() and 0xFF) shl 8) or (data[3].toInt() and 0xFF)
        val month = data[5].toInt() and 0xFF
        val day = data[6].toInt() and 0xFF
        val hour = data[7].toInt() and 0xFF
        val minute = data[8].toInt() and 0xFF
//        val second = data[9].toInt() and 0xFF

        // 🛠️ Debugging Each Value
        Log.d("Debug", "Year: $year, Month: $month, Day: $day, Hour: $hour, Minute: $minute")

        return "%04d-%02d-%02d %02d:%02d".format(year, month, day, hour, minute)
    }

    fun removeBond(device: BluetoothDevice) {
        try {
            val removeBondMethod = BluetoothDevice::class.java.getMethod("removeBond")
            removeBondMethod.invoke(device)
            Log.d("Bluetooth", "Bond removed. Restarting pairing...")
        } catch (e: Exception) {
            Log.e("Bluetooth", "Failed to remove bond", e)
        }
    }

    private fun readBatteryCharacteristic(gatt: BluetoothGatt?) {
        val batteryCharacteristic =
            gatt?.getService(BATTERY_SERVICE_UUID) // Battery Service
                ?.getCharacteristic(BATTERY_CHAR_UUID) // Battery Level

        if (batteryCharacteristic != null) {
            Log.d("Bluetooth", "Reading Battery Level...")
            viewModel.updateScannerStatus("Reading Battery Level...")
            gatt.readCharacteristic(batteryCharacteristic)
        } else {
            Log.e("Bluetooth", "Battery Level Characteristic NOT found! Trying next...")
            viewModel.updateScannerStatus("Battery Level Characteristic NOT found! Trying next...")
//            readGlucoseMeasurement(gatt) // Move to the next step
        }
    }

    private fun readGlucoseMeasurement(gatt: BluetoothGatt?) {
        val glucoseCharacteristic =
            gatt?.getService(glucoseServiceUUID) // Glucose Service
                ?.getCharacteristic(glucoseMeasurementUUID) // Glucose Measurement

        if (glucoseCharacteristic != null) {
            Log.d("Bluetooth", "Reading Glucose Measurement...")
            viewModel.updateScannerStatus("Reading Glucose Measurement...")

            gatt.readCharacteristic(glucoseCharacteristic)
        } else {
            Log.e("Bluetooth", "Glucose Measurement Characteristic NOT found!")
            viewModel.updateScannerStatus("Glucose Measurement Characteristic NOT found!")

        }
    }

    private fun requestStoredGlucoseRecords(gatt: BluetoothGatt?) {
        val racpCharacteristic =
            gatt?.getService(glucoseServiceUUID) // Glucose Service
                ?.getCharacteristic(racpUUID) // Record Access Control Point (RACP)

        if (racpCharacteristic != null) {
            racpCharacteristic.value = byteArrayOf(
                0x01,
                0x01
            ) // Op Code 0x01 (Report stored records), Operator 0x01 (All records)
            racpCharacteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
            gatt.writeCharacteristic(racpCharacteristic)
            viewModel.updateScannerStatus("Requested glucose records")
            Log.d("Bluetooth", "✅ Requested stored glucose records (0x0101).")
        } else {
            Log.e("Bluetooth", "❌ RACP Characteristic NOT found!")
            viewModel.updateScannerStatus("RACP Characteristic NOT found!")

        }
    }


    private fun enableGlucoseNotifications(gatt: BluetoothGatt?) {
        val glucoseCharacteristic =
            gatt?.getService(glucoseServiceUUID)
                ?.getCharacteristic(glucoseMeasurementUUID)

        val contextCharacteristic =
            gatt?.getService(glucoseServiceUUID)
                ?.getCharacteristic(UUID.fromString("00002A34-0000-1000-8000-00805f9b34fb"))

        if (glucoseCharacteristic != null) {
            gatt.setCharacteristicNotification(glucoseCharacteristic, true)
            val descriptor =
                glucoseCharacteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
            viewModel.updateScannerStatus("Enabled notifications for Glucose Measurement.")
            Log.d("Bluetooth", "✅ Enabled notifications for Glucose Measurement.")
        } else {
            Log.e("Bluetooth", "❌ Glucose Measurement Characteristic NOT found!")
            viewModel.updateScannerStatus("Glucose Measurement Characteristic NOT found!")

        }

        if (contextCharacteristic != null) {
            gatt.setCharacteristicNotification(contextCharacteristic, true)
            val descriptor =
                contextCharacteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
            viewModel.updateScannerStatus("Enabled notifications for Glucose Context.")

            Log.d("Bluetooth", "✅ Enabled notifications for Glucose Context.")
        } else {
            Log.e("Bluetooth", "❌ Glucose Context Characteristic NOT found!")
            viewModel.updateScannerStatus("Glucose Context Characteristic NOT found!")

        }
    }

    fun toFullUuid(shortUuid: String): UUID {
        return UUID.fromString("0000$shortUuid-0000-1000-8000-00805f9b34fb")
    }

}
//Byte(s)	Value (Hex)	Description
//1A	Flags	Flags (unit, context, etc.)
//1A 00	Sequence #	Record index
//E9 07	Year	2025 (0x07E9)
//02	Month	February
//1B	Day	27th
//09	Hours	9 AM
//33	Minutes	51 minutes
//2D	Seconds	45 seconds
//18 B0	Glucose Value	⚠️ Incorrect parsing here!
//00 00 00	Other Fields	Reserved/Optional
