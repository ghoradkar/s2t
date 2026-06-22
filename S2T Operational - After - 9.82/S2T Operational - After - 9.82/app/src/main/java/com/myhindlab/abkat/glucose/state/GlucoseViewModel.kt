package com.myhindlab.abkat.glucose.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GlucoseViewModel : ViewModel() {

    private val _glucoseData = MutableLiveData<String>()
    val glucoseData: LiveData<String> get() = _glucoseData


private val _glucoseRawData = MutableLiveData<String>()
    val glucoseRawData: LiveData<String> get() = _glucoseRawData

    private val _batteryData = MutableLiveData<String>()
    val batteryData: LiveData<String> get() = _batteryData

    private val _deviceName = MutableLiveData<String>()
    val deviceName: LiveData<String> get() = _deviceName

    private val _scannerStatus = MutableLiveData<String>()  // New status LiveData
    val scannerStatus: LiveData<String> get() = _scannerStatus

    private val _macAddress = MutableLiveData<String>()  // New status LiveData
    val macAddress: LiveData<String> get() = _macAddress


    fun updateData(data: String) {
        _glucoseData.postValue(data)
    }

    fun updateRawData(data: String) {
        _glucoseRawData.postValue(data)
    }

    fun updateScannerStatus(status: String) {
        _scannerStatus.postValue(status)
    }

    fun updateBatteryLevel(data: String) {
        _batteryData.postValue(data)
    }

    fun updateDeviceName(data: String) {
        _deviceName.postValue(data)
    }

    fun updateMacAddress(data: String) {
        _macAddress.postValue(data)
    }


}