package com.myhindlab.abkat.smart_scale

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BluetoothScaleViewModel : ViewModel() {
    private val _scaleData = MutableLiveData<Double>()
    val scaleData: LiveData<Double> get() = _scaleData

    private val _rawData = MutableLiveData<String>()
    val rawData: LiveData<String> get() = _rawData

    private val _scannerStatus = MutableLiveData<String>()  // New status LiveData
    val scannerStatus: LiveData<String> get() = _scannerStatus


    private val _scale = MutableLiveData<BluetoothDevice?>()
    val scale: LiveData<BluetoothDevice?> get() = _scale

    private val _macAddress = MutableLiveData<String>()  // New status LiveData
    val macAddress: LiveData<String> get() = _macAddress

    fun updateData(weight: Double, resistance: Int, productId: Int, macAddress: String,raw:String) {
        _scaleData.postValue(weight)
        _rawData.postValue(raw)

    }

    fun updateScannerStatus(status: String) {
        _scannerStatus.postValue(status)
    }

    fun updateDevice(name: BluetoothDevice?) {
        _scale.postValue(name)
    }

    fun updateMacAddress(data: String) {
        _macAddress.postValue(data)
    }

}
