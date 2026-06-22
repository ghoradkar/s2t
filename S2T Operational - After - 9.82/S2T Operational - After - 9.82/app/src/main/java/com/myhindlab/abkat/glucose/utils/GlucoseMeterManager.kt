package com.bioland.bledemo.glucose.utils

import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.myhindlab.abkat.glucose.state.GlucoseViewModel
import java.util.UUID


@Suppress("MissingPermission")
class GlucoseMeterManager(
    private val context: Context,
    val viewModel: GlucoseViewModel,
) {
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private var bluetoothGatt: BluetoothGatt? = null

    // GATT Callback
    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("BLE", "Connected to GATT server.")
                viewModel.updateScannerStatus("Connected to GATT server.")

                gatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("BLE", "Disconnected from GATT server.")
                viewModel.updateScannerStatus("Disconnected from GATT server.")

                gatt?.close()
                bluetoothGatt = null
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLE", "GATT services discovered.")
                viewModel.updateScannerStatus("GATT services discovered.")

                // Enable notifications here
                enableNotifications(gatt)
                // Read characteristics here
                readCharacteristics(gatt)
            } else {
                Log.w("BLE", "GATT services discovery failed with status: $status")
                viewModel.updateScannerStatus("GATT services discovery failed with status: $status")

            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLE", "Characteristic read: ${characteristic?.uuid}")
                viewModel.updateScannerStatus("Characteristic read: ${characteristic?.uuid}")

                // Handle read characteristic value
                handleCharacteristicRead(characteristic)
            } else {
                Log.e(
                    "BLE",
                    "Failed to read characteristic: ${characteristic?.uuid}, status: $status"
                )
                viewModel.updateScannerStatus("Failed to read characteristic: ${characteristic?.uuid}, status: $status")

            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            Log.d("BLE", "Characteristic changed: ${characteristic?.uuid}")
            viewModel.updateScannerStatus("Characteristic changed: ${characteristic?.uuid}")

            // Handle notification data here
            handleCharacteristicChanged(characteristic)
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLE", "Descriptor written successfully: ${descriptor?.uuid}")
                viewModel.updateScannerStatus("Descriptor written successfully: ${descriptor?.uuid}")

            } else {
                Log.e("BLE", "Failed to write descriptor: ${descriptor?.uuid}, status: $status")
                viewModel.updateScannerStatus("Failed to write descriptor: ${descriptor?.uuid}, status: $status")

            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLE", "Characteristic written successfully: ${characteristic?.uuid}")
                viewModel.updateScannerStatus("Characteristic written successfully: ${characteristic?.uuid}")

            } else {
                Log.e(
                    "BLE",
                    "Failed to write characteristic: ${characteristic?.uuid}, status: $status"
                )
                viewModel.updateScannerStatus("Failed to write characteristic: ${characteristic?.uuid}, status: $status")

            }
        }

        override fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
            super.onReliableWriteCompleted(gatt, status)
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
        }
    }

    // Connect to a BLE device
    fun connect(deviceAddress: String): Boolean {
        val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
        if (device == null) {
            Log.w("BLE", "Device not found. Unable to connect.")
            viewModel.updateScannerStatus("Device not found. Unable to connect.")

            return false
        }
        //autoConnect = false
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
        return true
    }

    // Disconnect from the GATT server
    fun disconnect() {
        bluetoothGatt?.disconnect()
    }

    // Close the GATT connection
    fun close() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    private fun enableNotifications(gatt: BluetoothGatt?) {
        // Implement enabling notifications for specific characteristics here
        // For example, for Battery Level:
        val batteryLevelCharacteristic =
            gatt?.getService(UUID.fromString("0000180F-0000-1000-8000-00805F9B34FB"))
                ?.getCharacteristic(UUID.fromString("00002A19-0000-1000-8000-00805F9B34FB"))
        if (batteryLevelCharacteristic != null) {
            gatt.setCharacteristicNotification(batteryLevelCharacteristic, true)
            val descriptor = batteryLevelCharacteristic.getDescriptor(
                UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
            )
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
        }

        // Enable RX notifications (0xFFE4)
        val uartRxService =
            gatt?.getService(UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB"))
        val rxCharacteristic =
            uartRxService?.getCharacteristic(UUID.fromString("0000FFE4-0000-1000-8000-00805F9B34FB"))
        if (rxCharacteristic != null) {
            gatt.setCharacteristicNotification(rxCharacteristic, true)
            val descriptor = rxCharacteristic.getDescriptor(
                UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")
            )
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
        }

        //Enable Glucose Measurement notifications (0x2A18)
        val glucoseService =
            gatt?.getService(UUID.fromString("00001808-0000-1000-8000-00805F9B34FB"))
        val glucoseMeasurementCharacteristic =
            glucoseService?.getCharacteristic(UUID.fromString("00002A18-0000-1000-8000-00805F9B34FB"))
        if (glucoseMeasurementCharacteristic != null) {
            gatt.setCharacteristicNotification(glucoseMeasurementCharacteristic, true)
            val descriptor = glucoseMeasurementCharacteristic.getDescriptor(
                UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")
            )
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
        }

        //Enable Glucose Measurement Context notifications (0x2A34)
        val glucoseMeasurementContextCharacteristic =
            glucoseService?.getCharacteristic(UUID.fromString("00002A34-0000-1000-8000-00805F9B34FB"))
        if (glucoseMeasurementContextCharacteristic != null) {
            gatt.setCharacteristicNotification(glucoseMeasurementContextCharacteristic, true)
            val descriptor = glucoseMeasurementContextCharacteristic.getDescriptor(
                UUID.fromString("00002902-0000-1000-8000-00805F9B34FB")
            )
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
        }
    }

    private fun readCharacteristics(gatt: BluetoothGatt?) {
        // Implement reading characteristics here
        // For example, for Device Name (0x2A00):
        val deviceNameCharacteristic =
            gatt?.getService(UUID.fromString("00001800-0000-1000-8000-00805F9B34FB"))
                ?.getCharacteristic(UUID.fromString("00002A00-0000-1000-8000-00805F9B34FB"))
        if (deviceNameCharacteristic != null) {
            gatt.readCharacteristic(deviceNameCharacteristic)
        }

        // Read other characteristics as needed...
        val PPCPCharacteristic =
            gatt?.getService(UUID.fromString("00001800-0000-1000-8000-00805F9B34FB"))
                ?.getCharacteristic(UUID.fromString("00002A04-0000-1000-8000-00805F9B34FB"))
        if (PPCPCharacteristic != null) {
            gatt.readCharacteristic(PPCPCharacteristic)
        }

        val deviceInformationService =
            gatt?.getService(UUID.fromString("0000180A-0000-1000-8000-00805F9B34FB"))

        val manufacturerNameCharacteristic =
            deviceInformationService?.getCharacteristic(UUID.fromString("00002A29-0000-1000-8000-00805F9B34FB"))
        if (manufacturerNameCharacteristic != null) {
            gatt.readCharacteristic(manufacturerNameCharacteristic)
        }

        val modelNumberCharacteristic =
            deviceInformationService?.getCharacteristic(UUID.fromString("00002A24-0000-1000-8000-00805F9B34FB"))
        if (modelNumberCharacteristic != null) {
            gatt.readCharacteristic(modelNumberCharacteristic)
        }

        val serialNumberCharacteristic =
            deviceInformationService?.getCharacteristic(UUID.fromString("00002A25-0000-1000-8000-00805F9B34FB"))
        if (serialNumberCharacteristic != null) {
            gatt.readCharacteristic(serialNumberCharacteristic)
        }

        val hardwareRevisionCharacteristic =
            deviceInformationService?.getCharacteristic(UUID.fromString("00002A27-0000-1000-8000-00805F9B34FB"))
        if (hardwareRevisionCharacteristic != null) {
            gatt.readCharacteristic(hardwareRevisionCharacteristic)
        }

        val firmwareRevisionCharacteristic =
            deviceInformationService?.getCharacteristic(UUID.fromString("00002A26-0000-1000-8000-00805F9B34FB"))
        if (firmwareRevisionCharacteristic != null) {
            gatt.readCharacteristic(firmwareRevisionCharacteristic)
        }

        val softwareRevisionCharacteristic =
            deviceInformationService?.getCharacteristic(UUID.fromString("00002A28-0000-1000-8000-00805F9B34FB"))
        if (softwareRevisionCharacteristic != null) {
            gatt.readCharacteristic(softwareRevisionCharacteristic)
        }

        val systemIdCharacteristic =
            deviceInformationService?.getCharacteristic(UUID.fromString("00002A23-0000-1000-8000-00805F9B34FB"))
        if (systemIdCharacteristic != null) {
            gatt.readCharacteristic(systemIdCharacteristic)
        }

        val regCertDataListCharacteristic =
            deviceInformationService?.getCharacteristic(UUID.fromString("00002A2A-0000-1000-8000-00805F9B34FB"))
        if (regCertDataListCharacteristic != null) {
            gatt.readCharacteristic(regCertDataListCharacteristic)
        }

        val pnpIdCharacteristic =
            deviceInformationService?.getCharacteristic(UUID.fromString("00002A50-0000-1000-8000-00805F9B34FB"))
        if (pnpIdCharacteristic != null) {
            gatt.readCharacteristic(pnpIdCharacteristic)
        }

        // Initialize glucoseService here:
        val glucoseService =
            gatt?.getService(UUID.fromString("00001808-0000-1000-8000-00805F9B34FB"))

        val glucoseFeatureCharacteristic =
            glucoseService?.getCharacteristic(UUID.fromString("00002A51-0000-1000-8000-00805F9B34FB"))
        if (glucoseFeatureCharacteristic != null) {
            gatt.readCharacteristic(glucoseFeatureCharacteristic)
        }
    }

    private fun handleCharacteristicRead(characteristic: BluetoothGattCharacteristic?) {
        when (characteristic?.uuid) {
            UUID.fromString("00002A00-0000-1000-8000-00805F9B34FB") -> {
                val deviceName = characteristic?.getStringValue(0)
                Log.d("BLE", "Device Name: $deviceName")
                viewModel.updateScannerStatus("Device Name: $deviceName")

                // Handle device name
            }

            UUID.fromString("00002A04-0000-1000-8000-00805F9B34FB") -> {
                // Handle PPCP Characteristic
                Log.d("BLE", "PPCP Characteristic read")
            }

            UUID.fromString("00002A29-0000-1000-8000-00805F9B34FB") -> {
                val manufacturerName = characteristic?.getStringValue(0)
                Log.d("BLE", "Manufacturer Name: $manufacturerName")
                // Handle Manufacturer Name
            }

            UUID.fromString("00002A24-0000-1000-8000-00805F9B34FB") -> {
                val modelNumber = characteristic?.getStringValue(0)
                Log.d("BLE", "Model Number: $modelNumber")
                // Handle Model Number
            }

            UUID.fromString("00002A25-0000-1000-8000-00805F9B34FB") -> {
                val serialNumber = characteristic?.getStringValue(0)
                Log.d("BLE", "Serial Number: $serialNumber")
                // Handle Serial Number
            }

            UUID.fromString("00002A27-0000-1000-8000-00805F9B34FB") -> {
                val hardwareRevision = characteristic?.getStringValue(0)
                Log.d("BLE", "Hardware Revision: $hardwareRevision")
                // Handle Hardware Revision
            }

            UUID.fromString("00002A26-0000-1000-8000-00805F9B34FB") -> {
                val firmwareRevision = characteristic?.getStringValue(0)
                Log.d("BLE", "Firmware Revision: $firmwareRevision")
                // Handle Firmware Revision
            }

            UUID.fromString("00002A28-0000-1000-8000-00805F9B34FB") -> {
                val softwareRevision = characteristic?.getStringValue(0)
                Log.d("BLE", "Software Revision: $softwareRevision")
                // Handle Software Revision
            }

            UUID.fromString("00002A23-0000-1000-8000-00805F9B34FB") -> {
                val systemId = characteristic?.getStringValue(0)
                Log.d("BLE", "System ID: $systemId")
                // Handle System ID
            }

            UUID.fromString("00002A2A-0000-1000-8000-00805F9B34FB") -> {
                // Handle RegCertDataList
                Log.d("BLE", "RegCertDataList read")
            }

            UUID.fromString("00002A50-0000-1000-8000-00805F9B34FB") -> {
                // Handle PNP Id
                Log.d("BLE", "PNP Id read")
            }

            UUID.fromString("00002A51-0000-1000-8000-00805F9B34FB") -> {
                // Handle Glucose Feature
                Log.d("BLE", "Glucose Feature read")
            }

            else -> {
                Log.w("BLE", "Unknown characteristic read: ${characteristic?.uuid}")
            }
        }
    }

    private fun handleCharacteristicChanged(characteristic: BluetoothGattCharacteristic?) {
        when (characteristic?.uuid) {
            UUID.fromString("00002A19-0000-1000-8000-00805F9B34FB") -> {
                if (characteristic == null) {
                    return
                }
                // Battery Level
                val batteryLevel =
                    characteristic!!.value[0].toInt() and 0xFF // Convert unsigned byte to int
                val temperature = characteristic!!.value[1].toInt() and 0xFF
                Log.d("BLE", "Battery Level: $batteryLevel %")
                Log.d("BLE", "Temperature: $temperature C")
                // Handle battery level and temperature here
            }

            UUID.fromString("0000FFE4-0000-1000-8000-00805F9B34FB") -> {
                // RX - Transparent data from glucose meter
                val rxData = characteristic?.value
                Log.d("BLE", "RX Data: ${rxData?.joinToString(" ") { "%02X".format(it) }}")
                // Handle RX data here - this is device-specific, you'll need to know the data format
            }

            UUID.fromString("00002A18-0000-1000-8000-00805F9B34FB") -> {
                // Glucose Measurement
                val glucoseMeasurementData = characteristic?.value
                Log.d(
                    "BLE",
                    "Glucose Measurement Data: ${
                        glucoseMeasurementData?.joinToString(" ") {
                            "%02X".format(it)
                        }
                    }"
                )
                // Handle glucose measurement data here
                // You'll need to parse this according to the Bluetooth Glucose Profile
            }

            UUID.fromString("00002A34-0000-1000-8000-00805F9B34FB") -> {
                // Glucose Measurement Context
                val glucoseContextData = characteristic?.value
                Log.d(
                    "BLE",
                    "Glucose Context Data: ${glucoseContextData?.joinToString(" ") { "%02X".format(it) }}"
                )
                // Handle glucose context data here
                // You'll need to parse this according to the Bluetooth Glucose Profile
            }

            else -> {
                Log.w("BLE", "Unknown characteristic changed: ${characteristic?.uuid}")
            }
        }
    }
}