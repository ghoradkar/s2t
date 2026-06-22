package com.myhindlab.abkat.glucose.ui

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager


import com.bioland.bledemo.glucose.utils.GlucoseMeterManager
import com.myhindlab.abkat.R
import com.myhindlab.abkat.databinding.ActivityGlucoseBinding
import com.myhindlab.abkat.glucose.GlucoseMeterBLE
import com.myhindlab.abkat.glucose.state.GlucoseViewModel

class GlucoseActivity : AppCompatActivity() {
    lateinit var binding: ActivityGlucoseBinding
    private lateinit var glucoseMeterBLE: GlucoseMeterBLE
    private lateinit var glucoseViewModel: GlucoseViewModel
    private var name: String? = ""
    private var number: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityGlucoseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST)
        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        registerReceiver(pairingRequestReceiver, filter)

        val filter_ACTION_BOND_STATE_CHANGED =
            IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        registerReceiver(bondStateReceiver, filter_ACTION_BOND_STATE_CHANGED)

        glucoseViewModel = ViewModelProvider(this).get(GlucoseViewModel::class.java)
        glucoseMeterBLE = GlucoseMeterBLE(this@GlucoseActivity, glucoseViewModel)

        requestPermissions()
        setupToolbar()
        eventListener()


        if (intent != null) {

            name = intent.getStringExtra("name")
            number = intent.getStringExtra("beneficiaryNumber")

            binding.edtBeneficiaryname.setText(name)
            binding.edtBeneficiaryno.setText(number)


        }

    }

    private val pairingRequestReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_PAIRING_REQUEST == action) {
                glucoseViewModel.updateScannerStatus("Enter PIN on the system popup and confirm.")

            }
        }
    }
    private val bondStateReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

            when (action) {
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> {
                    when (device?.bondState) {
                        BluetoothDevice.BOND_BONDING -> Log.d("Bluetooth", "Pairing in progress...")
                        BluetoothDevice.BOND_BONDED -> {
                            Log.d("Bluetooth", "Paired successfully with ${device.name}!")
                            glucoseViewModel.updateScannerStatus("Paired. Connecting...")

                            // Now, safely connect to the device
                            glucoseMeterBLE.connectToDevice(device)
                        }

                        BluetoothDevice.BOND_NONE -> Log.d(
                            "Bluetooth",
                            "Pairing failed or removed."
                        )
                    }
                }
            }
        }
    }


    fun eventListener() {
        binding.btnScan.setOnClickListener {
            glucoseMeterBLE.startScan()
        }

        binding.btnStopScan.setOnClickListener {
            glucoseMeterBLE.stopScan()

        }


        // Observe scanner status
        glucoseViewModel.scannerStatus.observe(this) { status ->
            if (status.equals("Scanning for devices...", ignoreCase = true)) {
//                binding.btnStopScan.visibility = View.VISIBLE
                binding.btnScan.setText("Stop Scan")
                binding.btnScan.setOnClickListener {
                    glucoseMeterBLE.stopScan()
                }
            } else {
                binding.btnScan.setText("Start Scan")
                binding.btnScan.setOnClickListener {
                    glucoseMeterBLE.startScan()
                }
//                binding.btnStopScan.visibility = View.GONE
            }
            binding.tvStatus.text = status
        }


        // Observe Mac address
        glucoseViewModel.macAddress.observe(this) { text ->
            binding.tvMacAddress.text = text
        }


        glucoseViewModel.glucoseRawData.observe(this) { text ->
            binding.tvRawData.text = text
        }


        // Observe Data
        glucoseViewModel.glucoseData.observe(this) { glucoseData ->
            binding.tvData.text = glucoseData
            binding.btnFetchData.visibility = View.VISIBLE
            LocalBroadcastManager.getInstance(this@GlucoseActivity)
                .sendBroadcast(Intent("GLUCOSE_DATA").putExtra("glucose", glucoseData).putExtra("glucoseDevice",binding.tvMacAddress.text))
        }

        // Observe Battery Level
        glucoseViewModel.batteryData.observe(this) { status ->
            binding.tvBatteryLevel.text = status
        }

        // Observe Device Name
        glucoseViewModel.deviceName.observe(this) { status ->
            binding.tvDeviceName.text = status

        }


        binding.btnFetchData.setOnClickListener {


            finish()
        }

    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Glucose Device"

        toolbar.setNavigationIcon(R.drawable.icon_arrowback)
        toolbar.setNavigationOnClickListener { finish() }
    }


    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all { it.value }
            if (granted) {
                Log.d("Permissions", "All Granted")
            } else {
                Log.d("Permissions", "Some Denied")
            }
        }

    // Call this when needed
    fun requestPermissions() {
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        )
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(pairingRequestReceiver)

    }
}