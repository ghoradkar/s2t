package com.myhindlab.abkat.smart_scale

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

interface BluetoothScaleListener {
    fun onDataReceived(weight: Double, resistance: Int, productId: Int, macAddress: String)
}

@SuppressLint("MissingPermission")
class BluetoothScaleScanner(
    private val viewModel: BluetoothScaleViewModel,
    private val context: Context
) {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val scanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner

    private var isProcessingAllowed = true

    //    Yoda1
    fun startScan() {
        isProcessingAllowed = true // ✅ Reset processing flag on every new scan
        viewModel.updateScannerStatus("Scanning for devices...")  // Update UI status

        val filters = listOf(ScanFilter.Builder().setDeviceName("Yoda1").build())
        val settings =
            ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()
        scanner?.startScan(filters, settings, scanCallback)

        // Stop scanning & processing data after 15 seconds
        CoroutineScope(Dispatchers.Main).launch {
            delay(30000) // Wait for 15 seconds
            stopScan()
            viewModel.updateScannerStatus("Scanning stopped.")
        }// 15 seconds
    }

    fun stopScan() {
        viewModel.updateScannerStatus("Scan stopped")
        scanner?.stopScan(scanCallback)
        isProcessingAllowed = false // Ensure it stops processing
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            Log.i("TAG", "onScanResult:${result?.device?.name}")
            viewModel.updateDevice(result?.device)

//            result?.device?.address?.let { viewModel.updateMacAddress(it) }




            result?.scanRecord?.bytes?.let { data ->

                if (data.size >= 17 && isProcessingAllowed) {  // Process only if allowed
                    parseData(data)
                    viewModel.updateScannerStatus("Device Found! Processing data...")
//                    stopScan()
//                    viewModel.updateScannerStatus("Scanning Stopped...")

                }
            }

//            result?.device?.let { device ->
//                if (isProcessingAllowed) {
//                    if (device.address.equals("24:16:51:A4:4E:30")) {
//                        viewModel.updateScannerStatus("Device found: ${device.name}, Connecting...")
//
//                        // ✅ First, establish a connection before processing data
//                        connectToDevice(device)
//                    }
//                }
//            }
        }

        override fun onScanFailed(errorCode: Int) {
            viewModel.updateScannerStatus("Scan failed with error: $errorCode")
        }
    }



    private fun parseData(data: ByteArray) {


//        data.forEachIndexed { index, byte ->
//            Log.d("RAW_DATA", "[$index] = ${String.format("%02X", byte)}")
//        }

//        val rawString = data.contentToString()
//        viewModel.updateData(0.0, 0, 0, "", rawString)

        if (data[0] != 0x10.toByte() || data[1] != 0xFF.toByte()) return // Validate protocol



        val version = data[2]
        val serialNumber = data[3].toInt() and 0xFF
        val weightHigh = data[4].toInt() and 0xFF
        val weightLow = data[5].toInt() and 0xFF
        val resistanceHigh = data[6].toInt() and 0xFF
        val resistanceLow = data[7].toInt() and 0xFF
        val productId = (data[8].toInt() shl 8) or (data[9].toInt() and 0xFF)
        val messageProperty = data[10]
        val macAddress = data.copyOfRange(11, 17).joinToString(":") { String.format("%02X", it) }

        val rawWeight = (weightHigh shl 8) or weightLow
//
//        val isDecimal = (messageProperty.toInt() and 0x01) == 1
//
//        val weight = if (isDecimal) {
//            rawWeight / 10.0
//        } else {
//            rawWeight.toDouble()
//        }


        val weight10 = rawWeight / 10.0
        val weight100 = rawWeight / 100.0

        val weight = when {
            weight10 in 20.0..300.0 -> weight10
            weight100 in 20.0..300.0 -> weight100
            else -> weight10
        }

        val weightFormatted = String.format(Locale.ENGLISH,"%.1f", weight)
        val resistance = (resistanceHigh shl 8) or resistanceLow

        Log.d(
            "ScaleData",
            "Version: $version, Serial: $serialNumber, Weight: $weightFormatted kg, Resistance: $resistance, Product ID: $productId, MAC: $macAddress"
        )
//        listener.onDataReceived(weight, resistance, productId, macAddress)

        val rawHex = data.joinToString(" ") { "%02X".format(it) }


        viewModel.updateData(weight, resistance, productId, macAddress,rawHex)
        viewModel.updateScannerStatus("Data received!")

        //Version: -64, Serial: 47, Weight: 49.2 kg, Resistance: 5000, Product ID: 0, MAC: 24:16:51:A4:4E:30
    }



//    private fun parseData(data: ByteArray) {
//
//        data.forEachIndexed { index, byte ->
//            Log.d("RAW_DATA", "[$index] = ${String.format("%02X", byte)}")
//        }
//        if (data.size < 17) return
//        if (data[0] != 0x10.toByte() || data[1] != 0xFF.toByte()) return
//
//        val version = data[2].toInt() and 0xFF
//        val serialNumber = data[3].toInt() and 0xFF
//
//
//        val weightRaw = ((data[5].toInt() and 0xFF) shl 8) or (data[4].toInt() and 0xFF)
//        val weight = weightRaw / 400.0
//
//        // Resistance / Height
//        val resistanceRaw = ((data[7].toInt() and 0xFF) shl 8) or (data[6].toInt() and 0xFF)
//
//        val productId = ((data[8].toInt() and 0xFF) shl 8) or (data[9].toInt() and 0xFF)
//
//        val messageProperty = data[10].toInt() and 0xFF
//
//        val macAddress = data.copyOfRange(11, 17)
//            .joinToString(":") { String.format("%02X", it) }
//
//        Log.d(
//            "ScaleData",
//            "Version: $version, Serial: $serialNumber, Weight: $weight kg, Resistance: $resistanceRaw, Product ID: $productId, MAC: $macAddress"
//        )
//
//        val rawHex = data.joinToString(" ") { "%02X".format(it) }
//
//
//        viewModel.updateData(weight, resistanceRaw, productId, macAddress,rawHex)
//        viewModel.updateScannerStatus("Data received!")
//    }



    private fun connectToDevice(device: BluetoothDevice) {
        val bluetoothGatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d("Bluetooth", "Connected to ${device.address}")
                    viewModel.updateScannerStatus("Connected to scale. Reading data...")

                    // ✅ Once connected, start reading data
                    gatt?.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d("Bluetooth", "Disconnected from ${device.address}")
                    viewModel.updateScannerStatus("Disconnected from scale.")
                    gatt?.close()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.d("Bluetooth", "Services discovered on ${gatt?.device?.address}")

                    gatt?.services?.forEach { service ->
                        Log.d("Bluetooth", "Service UUID: ${service.uuid}")

                        service.characteristics.forEach { characteristic ->
                            Log.d("Bluetooth", "Characteristic UUID: ${characteristic.uuid}")
                        }
                    }

                    // Once we log the UUIDs, manually find the one related to weight
                    startReadingData(gatt)
                }
            }

            override fun onCharacteristicChanged(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?
            ) {
                characteristic?.value?.let { data ->
                    Log.d("Bluetooth", "Raw Weight Data: ${data.joinToString()}")

                    // Convert raw data to weight (depends on scale protocol)
                    parseData(data)
//                    viewModel.updateWeight(weight)
                }
            }


        })
    }

    private fun startReadingData(gatt: BluetoothGatt?) {
        val weightCharacteristic = gatt?.services?.flatMap { it.characteristics }
            ?.find {
                it.uuid.toString().contains("SOME_UUID_SEGMENT")
            } // Replace with actual segment

        weightCharacteristic?.let {
            gatt.setCharacteristicNotification(it, true)
            Log.d("Bluetooth", "Listening for weight data on UUID: ${it.uuid}")
        }
    }

}
