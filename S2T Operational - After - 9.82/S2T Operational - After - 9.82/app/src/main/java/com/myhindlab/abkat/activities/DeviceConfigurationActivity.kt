package com.myhindlab.abkat.activities

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.myhindlab.abkat.R
import com.myhindlab.abkat.adapters.BluetoothDeviceListAdapter
import com.myhindlab.abkat.databinding.ActivityDeviceConfigurationBinding
import com.myhindlab.abkat.models.BluetoothDeviceModel
import com.myhindlab.abkat.utilities.RecyclerItemClickListener
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities


class DeviceConfigurationActivity : AppCompatActivity() {
    lateinit var binding: ActivityDeviceConfigurationBinding;
    private val REQUEST_ENABLE_BT: Int = 101
    lateinit var bluetoothManager: BluetoothManager
    lateinit var bluetoothAdapter: BluetoothAdapter
    lateinit var deviceList: ArrayList<BluetoothDevice>
    lateinit var progressDialog: AlertDialog
    var selectedIndex = 0
    lateinit var session: UserSessionManager

    lateinit var bluetoothLeScanner:BluetoothLeScanner
    private var scanning = false
    private val handler = Handler()

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    //    private val leDeviceListAdapter = LeDeviceListAdapter()
    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)

            val action = intent.action
            Log.i("TAG", "onScanResult $action")
//            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
//                Log.i("TAG", "Discovery finished, hide loading")
//                binding.btnWeightMachineConfigure.visibility = View.VISIBLE
//                binding.progressBarWeight.visibility = View.GONE
//                binding.btnBerryMedConfigure.visibility = View.VISIBLE
//                binding.progressBarBerryMed.visibility = View.GONE
//                binding.btnBOLTConfigure.visibility = View.VISIBLE
//                binding.progressBarBOLT.visibility = View.GONE
//                binding.btnSafeyConfigure.visibility = View.VISIBLE
//                binding.progressBarSafey.visibility = View.GONE
//
//                Log.d(TAG, "onReceive: ${deviceList.size}")
//                progressDialog.dismiss()
//
//                val alertDialog = AlertDialog.Builder(this@DeviceConfigurationActivity);
//                alertDialog.setTitle("Bluetooth Devices")
//                alertDialog.setCancelable(false)
//                val dialogView =
//                    layoutInflater.inflate(R.layout.bluetooth_device_list_dialog, null, false)
//                val rvList = dialogView.findViewById<RecyclerView>(R.id.rvList)
//                val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)
//                alertDialog.setView(dialogView)
//
//
//                rvList.hasFixedSize()
//                rvList.layoutManager = LinearLayoutManager(this@DeviceConfigurationActivity)
//                rvList.adapter = BluetoothDeviceListAdapter(deviceList)
//
//
//                val alert = alertDialog.create()
//
//                alert.setButton(
//                    DialogInterface.BUTTON_POSITIVE,
//                    "Rescan",
//                    DialogInterface.OnClickListener { dialogInterface, i ->
//                        startDiscovery()
//                        progressDialog.show()
//
//                    })
//                alert.setButton(
//                    DialogInterface.BUTTON_NEGATIVE,
//                    "Cancel",
//                    DialogInterface.OnClickListener { dialogInterface, i ->
//                        dialogInterface.dismiss()
//
//                    })
//
//                rvList.addOnItemTouchListener(
//                    RecyclerItemClickListener(this@DeviceConfigurationActivity,
//                        RecyclerItemClickListener.OnItemClickListener { view, position ->
//                            if (selectedIndex == 0) {
//                                binding.tvNameWeightMachine.text = deviceList[position].name
//                                binding.tvMacWeightMachine.text = deviceList[position].address
//
//                                val bleDevice = BluetoothDeviceModel(
//                                    deviceList[position].name,
//                                    deviceList[position].address
//                                )
//                                Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
//                                session.createWeightMachineSession(Gson().toJson(bleDevice))
//
//                            } else if (selectedIndex == 1) {
//                                binding.tvNameBerryMed.text = deviceList[position].name
//                                binding.tvMacBerryMed.text = deviceList[position].address
//
//                                val bleDevice = BluetoothDeviceModel(
//                                    deviceList[position].name,
//                                    deviceList[position].address
//                                )
//                                Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
//                                session.createBPMachineSession(Gson().toJson(bleDevice))
//
//                            } else if (selectedIndex == 2) {
//                                binding.tvNameBOLT.text = deviceList[position].name
//                                binding.tvMacBOLT.text = deviceList[position].address
//
//                                val bleDevice = BluetoothDeviceModel(
//                                    deviceList[position].name,
//                                    deviceList[position].address
//                                )
//                                Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
//                                session.createLFT1MachineSession(Gson().toJson(bleDevice))
//
//                            } else if (selectedIndex == 3) {
//                                binding.tvNameSafey.text = deviceList[position].name
//                                binding.tvMacSafey.text = deviceList[position].address
//                                val bleDevice = BluetoothDeviceModel(
//                                    deviceList[position].name,
//                                    deviceList[position].address
//                                )
//                                Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
//                                session.createLFT2MachineSession(Gson().toJson(bleDevice))
//
//                            }
//                            alert.dismiss()
//                        })
//                )
//
//                alert.show()
//
//            } else if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    result.device

                device?.let {
                    if (!deviceList.contains(it))
                        deviceList.add(it)

                }

                Log.i("TAG", "Device Name: " + (device?.name ?: ""))
                Log.i("TAG", "Device Address:" + (device?.address ?: ""))
//            }

        }
    }


    val TAG: String? = DeviceConfigurationActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setDefault()
        eventListener()

    }

    fun init() {
        progressDialog = Utilities.ProgressDialog(this, "Searching for bluetooth devices").create()
        session = UserSessionManager(this@DeviceConfigurationActivity)
        bluetoothManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getSystemService(BluetoothManager::class.java)
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        bluetoothAdapter = bluetoothManager.adapter

        if (!Utilities.isBluetoothEnabled()) {
            binding.switchBluetooth.isChecked = false

            Utilities.showAlertDialog(
                this,
                "Bluetooth is disabled",
                "Please turn on bluetooth first",
                false,
                "Okay", DialogInterface.OnClickListener { dialogInterface, i ->
                    enableBluetooth()
                }
            )

            return

        } else {
            binding.switchBluetooth.isChecked = true
        }

        val intentFilter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        registerReceiver(bluetoothReceiver, intentFilter)
        deviceList = ArrayList()

        bluetoothLeScanner=bluetoothAdapter.bluetoothLeScanner

    }


    private fun scanLeDevice() {

        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@postDelayed
                }
                bluetoothLeScanner.stopScan(leScanCallback)
            }, SCAN_PERIOD)
            scanning = true
            bluetoothLeScanner.startScan(leScanCallback)
        } else {
            scanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
            Log.i("TAG", "Discovery finished, hide loading")
            binding.btnWeightMachineConfigure.visibility = View.VISIBLE
            binding.progressBarWeight.visibility = View.GONE
            binding.btnBerryMedConfigure.visibility = View.VISIBLE
            binding.progressBarBerryMed.visibility = View.GONE
            binding.btnBOLTConfigure.visibility = View.VISIBLE
            binding.progressBarBOLT.visibility = View.GONE
            binding.btnSafeyConfigure.visibility = View.VISIBLE
            binding.progressBarSafey.visibility = View.GONE

            Log.d(TAG, "onReceive: ${deviceList.size}")
            progressDialog.dismiss()

            val alertDialog = AlertDialog.Builder(this@DeviceConfigurationActivity);
            alertDialog.setTitle("Bluetooth Devices")
            alertDialog.setCancelable(false)
            val dialogView =
                layoutInflater.inflate(R.layout.bluetooth_device_list_dialog, null, false)
            val rvList = dialogView.findViewById<RecyclerView>(R.id.rvList)
            val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)
            alertDialog.setView(dialogView)


            rvList.hasFixedSize()
            rvList.layoutManager = LinearLayoutManager(this@DeviceConfigurationActivity)
            rvList.adapter = BluetoothDeviceListAdapter(deviceList)


            val alert = alertDialog.create()

            alert.setButton(
                DialogInterface.BUTTON_POSITIVE,
                "Rescan",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    startDiscovery()
                    progressDialog.show()

                })
            alert.setButton(
                DialogInterface.BUTTON_NEGATIVE,
                "Cancel",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()

                })

            rvList.addOnItemTouchListener(
                RecyclerItemClickListener(this@DeviceConfigurationActivity,
                    RecyclerItemClickListener.OnItemClickListener { view, position ->
                        if (selectedIndex == 0) {
//                            binding.tvNameWeightMachine.text = deviceList[position].name
//                            binding.tvMacWeightMachine.text = deviceList[position].address
//
//                            val bleDevice = BluetoothDeviceModel(
//                                deviceList[position].name,
//                                deviceList[position].address
//                            )
//                            Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
//                            session.createWeightMachineSession(Gson().toJson(bleDevice))

                        } else if (selectedIndex == 1) {
//                            binding.tvNameBerryMed.text = deviceList[position].name
//                            binding.tvMacBerryMed.text = deviceList[position].address
//
//                            val bleDevice = BluetoothDeviceModel(
//                                deviceList[position].name,
//                                deviceList[position].address
//                            )
//                            Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
//                            session.createBPMachineSession(Gson().toJson(bleDevice))

                        } else if (selectedIndex == 2) {
                            binding.tvNameBOLT.text = deviceList[position].name
                            binding.tvMacBOLT.text = deviceList[position].address

                            val bleDevice = BluetoothDeviceModel(
                                deviceList[position].name,
                                deviceList[position].address
                            )
                            Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
                            session.createLFT1MachineSession(Gson().toJson(bleDevice))

                        } else if (selectedIndex == 3) {
                            binding.tvNameSafey.text = deviceList[position].name
                            binding.tvMacSafey.text = deviceList[position].address
                            val bleDevice = BluetoothDeviceModel(
                                deviceList[position].name,
                                deviceList[position].address
                            )
                            Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
                            session.createLFT2MachineSession(Gson().toJson(bleDevice))

                        }
                        alert.dismiss()
                    })
            )

            alert.show()

        }
    }

    @SuppressLint("MissingPermission")
    fun setDefault() {
        try {


//            if (session.isWeightDeviceConfigured) {
//                val device = session.weightDevice
//                binding.tvNameWeightMachine.text = device.name
//                binding.tvMacWeightMachine.text = device.address
//            }
//            if (session.isBPDeviceConfigured) {
//                val device = session.bpDevice
//                binding.tvNameBerryMed.text = device.name
//                binding.tvMacBerryMed.text = device.address
//            }
            if (session.isLFT1DeviceConfigured) {
                val device = session.lfT1Device
                binding.tvNameBOLT.text = device.name
                binding.tvMacBOLT.text = device.address
            }
            if (session.isLFT2DeviceConfigured) {
                val device = session.lfT2Device
                binding.tvNameSafey.text = device.name
                binding.tvMacSafey.text = device.address
            }
        } catch (e: java.lang.RuntimeException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    fun eventListener() {
        binding.switchBluetooth.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                enableBluetooth()

            } else {
                bluetoothAdapter.disable()
            }
        }

        binding.btnWeightMachineConfigure.setOnClickListener {
            selectedIndex = 0
            startDiscovery()
            binding.btnWeightMachineConfigure.visibility = View.GONE
            binding.progressBarWeight.visibility = View.VISIBLE

        }
        binding.btnBerryMedConfigure.setOnClickListener {
            selectedIndex = 1
            startDiscovery()
            binding.btnBerryMedConfigure.visibility = View.GONE
            binding.progressBarBerryMed.visibility = View.VISIBLE

        }
        binding.btnBOLTConfigure.setOnClickListener {
            selectedIndex = 2
            startDiscovery()
            binding.btnBOLTConfigure.visibility = View.GONE
            binding.progressBarBOLT.visibility = View.VISIBLE

        }
        binding.btnSafeyConfigure.setOnClickListener {
            selectedIndex = 3
            startDiscovery()
            scanLeDevice()
            binding.btnSafeyConfigure.visibility = View.GONE
            binding.progressBarSafey.visibility = View.VISIBLE

        }

    }


    private fun enableBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(Array(1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Manifest.permission.BLUETOOTH_CONNECT
                    } else {
                        TODO("VERSION.SDK_INT < S")
                    }
                }, 123)
            }
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }


    // Create a BroadcastReceiver for ACTION_FOUND.
    private val bluetoothReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            Log.i("TAG", "onReceive $action")
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                Log.i("TAG", "Discovery finished, hide loading")
                binding.btnWeightMachineConfigure.visibility = View.VISIBLE
                binding.progressBarWeight.visibility = View.GONE
                binding.btnBerryMedConfigure.visibility = View.VISIBLE
                binding.progressBarBerryMed.visibility = View.GONE
                binding.btnBOLTConfigure.visibility = View.VISIBLE
                binding.progressBarBOLT.visibility = View.GONE
                binding.btnSafeyConfigure.visibility = View.VISIBLE
                binding.progressBarSafey.visibility = View.GONE

                Log.d(TAG, "onReceive: ${deviceList.size}")
                progressDialog.dismiss()

                val alertDialog = AlertDialog.Builder(this@DeviceConfigurationActivity);
                alertDialog.setTitle("Bluetooth Devices")
                alertDialog.setCancelable(false)
                val dialogView =
                    layoutInflater.inflate(R.layout.bluetooth_device_list_dialog, null, false)
                val rvList = dialogView.findViewById<RecyclerView>(R.id.rvList)
                val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)
                alertDialog.setView(dialogView)


                rvList.hasFixedSize()
                rvList.layoutManager = LinearLayoutManager(this@DeviceConfigurationActivity)
                rvList.adapter = BluetoothDeviceListAdapter(deviceList)


                val alert = alertDialog.create()

                alert.setButton(
                    DialogInterface.BUTTON_POSITIVE,
                    "Rescan",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        startDiscovery()
                        progressDialog.show()

                    })
                alert.setButton(
                    DialogInterface.BUTTON_NEGATIVE,
                    "Cancel",
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()

                    })

                rvList.addOnItemTouchListener(
                    RecyclerItemClickListener(this@DeviceConfigurationActivity,
                        RecyclerItemClickListener.OnItemClickListener { view, position ->
                            if (selectedIndex == 0) {
//                                binding.tvNameWeightMachine.text = deviceList[position].name
//                                binding.tvMacWeightMachine.text = deviceList[position].address
//
//                                val bleDevice = BluetoothDeviceModel(
//                                    deviceList[position].name,
//                                    deviceList[position].address
//                                )
//                                Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
//                                session.createWeightMachineSession(Gson().toJson(bleDevice))

                            } else if (selectedIndex == 1) {
//                                binding.tvNameBerryMed.text = deviceList[position].name
//                                binding.tvMacBerryMed.text = deviceList[position].address
//
//                                val bleDevice = BluetoothDeviceModel(
//                                    deviceList[position].name,
//                                    deviceList[position].address
//                                )
//                                Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
//                                session.createBPMachineSession(Gson().toJson(bleDevice))

                            } else if (selectedIndex == 2) {
                                binding.tvNameBOLT.text = deviceList[position].name
                                binding.tvMacBOLT.text = deviceList[position].address

                                val bleDevice = BluetoothDeviceModel(
                                    deviceList[position].name,
                                    deviceList[position].address
                                )
                                Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
                                session.createLFT1MachineSession(Gson().toJson(bleDevice))

                            } else if (selectedIndex == 3) {
                                binding.tvNameSafey.text = deviceList[position].name
                                binding.tvMacSafey.text = deviceList[position].address
                                val bleDevice = BluetoothDeviceModel(
                                    deviceList[position].name,
                                    deviceList[position].address
                                )
                                Log.d(TAG, "onReceive: ${Gson().toJson(bleDevice)}")
                                session.createLFT2MachineSession(Gson().toJson(bleDevice))

                            }
                            alert.dismiss()
                        })
                )

                alert.show()

            } else if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                device?.let {
                    if (!deviceList.contains(it))
                        deviceList.add(it)

                }

                Log.i("TAG", "Device Name: " + (device?.name ?: ""))
                Log.i("TAG", "Device Address:" + (device?.address ?: ""))
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startDiscovery() {
        deviceList.clear()
        if (bluetoothAdapter?.isDiscovering == true) {
            Log.i("TAG", "cancel start discovery")
            bluetoothAdapter?.cancelDiscovery()
        }
        Log.i("TAG", "start discovery, show loading")
        bluetoothAdapter?.startDiscovery()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult: $requestCode $grantResults")
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()

        try {
            // Don't forget to unregister the ACTION_FOUND receiver.
            bluetoothAdapter?.cancelDiscovery();
            unregisterReceiver(bluetoothReceiver);
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }


}