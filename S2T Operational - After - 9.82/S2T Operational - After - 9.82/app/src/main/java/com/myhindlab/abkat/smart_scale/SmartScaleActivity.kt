package com.myhindlab.abkat.smart_scale

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bioland.bledemo.smart_scale.utils.BodyCompositionInput
import com.bioland.bledemo.smart_scale.utils.calculateBodyComposition
import com.myhindlab.abkat.R
import com.myhindlab.abkat.databinding.ActivitySmartScaleBinding

class SmartScaleActivity : AppCompatActivity() {
    lateinit var binding: ActivitySmartScaleBinding
    private lateinit var viewModel: BluetoothScaleViewModel
    private lateinit var bluetoothScanner: BluetoothScaleScanner
    private var beneficiaryAge: Int? = 0
    private var beneficiaryHeight: Int? = 0
    private var beneficiarySex: Int? = 0
    private var gender: String? = "0"
    private var name: String? = ""
    private var number: String? = ""

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivitySmartScaleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this).get(BluetoothScaleViewModel::class.java)
        bluetoothScanner = BluetoothScaleScanner(viewModel, this@SmartScaleActivity)

        requestPermissions()
        setupToolbar()
        eventListener()

        if (intent != null) {
            beneficiaryAge = intent.getStringExtra("age")?.toIntOrNull()
            beneficiaryHeight = intent.getStringExtra("height")?.toIntOrNull()
            gender = intent.getStringExtra("gender")
            name = intent.getStringExtra("name")
            number = intent.getStringExtra("beneficiaryNumber")

            binding.edtBeneficiaryname.setText(name)
            binding.edtBeneficiaryno.setText(number)




            if (gender.equals("Male")) {
                beneficiarySex = 1
            } else if (gender.equals("Female")) {
                beneficiarySex = 0
            }


        }


        viewModel.scale.observe(this) { device ->
            device.let { it -> binding.tvDeviceName.text = it!!.name
            binding.tvMacAddressName.text = it!!.address


            }
        }


// Observe scanner status
        viewModel.scannerStatus.observe(this) { status ->
            if (status.contains("Scanning for devices", ignoreCase = true)) {
//                binding.btnStopScan.visibility = View.VISIBLE
                binding.btnScan.isEnabled = true
                binding.btnScan.setText("Stop Scan")
                binding.btnScan.setOnClickListener {
                    bluetoothScanner.stopScan()
                }
            } else if (status.contains("Device Found")) {
                binding.btnScan.isEnabled = false
            } else {
                binding.btnScan.isEnabled = true

                binding.btnScan.setText("Start Scan")
                binding.btnScan.setOnClickListener {
                    bluetoothScanner.startScan()
                }
//                binding.btnStopScan.visibility = View.GONE
            }
            binding.tvStatus.text = status
        }


        viewModel.rawData.observe(this) { data ->
            binding.tvData.text = data

            Log.d("raw data", data)
        }

        viewModel.scaleData.observe(this) { data ->
//            findViewById<TextView>(R.id.textView).text = data
//            val height: Float = (beneficiaryHeight ?: 0) as Float
            val height: Float = (beneficiaryHeight ?: 0).toFloat()
            val age: Int = beneficiaryAge ?: 0
            val weight: Double = data
            val r: Float = 500F
            var sex: Int = beneficiaryAge ?: 0
            val input = BodyCompositionInput(
                height = beneficiaryHeight ?: 0,
                weight = data,
                impedance = 4280,
                sex = beneficiarySex ?: 0,
                age = beneficiaryAge ?: 0
            )
            val result = calculateBodyComposition(input)

            println("Body Fat Percentage (BFP): ${result.bfp}%")
            println("Skeletal Lean Mass (SLM): ${result.slm} kg")
            println("Basal Metabolic Rate (BMR): ${result.bmr} kcal/day")
            println("Body Mass Index (BMI): ${result.bmi}")
            println("Metabolic Age: ${result.metabolicAge} years")
            println("Health Score: ${result.score}/100")
//            try {

//                val cSBiasV235Resp: CSBiasAPI.CSBiasV235Resp = CSBiasAPI.cs_bias_v235(
//                    0, sex, age,
//                    height.toInt(), (weight * 10).toInt(), (r * 10).toInt(), 2018
//                )
//
            val sb = StringBuilder()
//
//                if (cSBiasV235Resp.result === CSBiasAPI.CSBIAS_OK) {
//                    //计算
            try {
                sb.append("enter\r\n")
                sb.append("Gender:$sex Height:$height Age:$age Resistance:$r Weight:$weight\r\n")
                sb.append("**************************************\r\n")
                sb.append("**************************************\r\n")
                sb.append(("Body Fat Percentage (BFP): ${result.bfp}%") + "\r\n")
                sb.append(("Muscle weight kg:" + result.slm).toString() + "\r\n")
                sb.append(("Water content%:${result.bwp} years") + "\r\n")
                sb.append(("Bone salt content: ${result.bmc}") + "\r\n")
                sb.append(("Visceral fat level: ${result.vfr}") + "\r\n")
                sb.append(("protein%: ${result.pp}") + "\r\n")
                sb.append(("Skeletal Lean Mass (SLM): ${result.slm} kg") + "\r\n")
                sb.append(("Basal Metabolic Rate (BMR): ${result.bmr} kcal/day") + "\r\n")
                sb.append(("Body Mass Index (BMI): ${result.bmi}") + "\r\n")
                sb.append(("Standard weight kg:${result.sbw}") + "\r\n")
                sb.append(("Muscle Control:${result.mc}") + "\r\n")
                sb.append(("Weight Control:${result.wc}") + "\r\n")
                sb.append(("Fat Control: + ${result.fc}") + "\r\n")
                sb.append(("Metabolic Age: ${result.metabolicAge} years") + "\r\n")
                sb.append(("Health Score: ${result.score}/100") + "\r\n")

                binding.tvWeight.text = weight.toString()
                binding.tvHeight.text = height.toString()

                val bmi = result.bmi.toString()
//                val formattedBmi = String.format("%.2f", bmi)
                val formattedBmi = String.format("%.2f", bmi.toDouble())
                binding.tvBMI.text = formattedBmi


                //            sb.append("脂肪重:" + builderEx.getFM()  +"\r\n");
//            sb.append("瘦体重kg:" + builderEx.getLBM()  + "\r\n");
//            sb.append("水重kg:" + builderEx.getTF() + "\r\n");
//            sb.append("肥胖度:" + builderEx.getOD() + "\r\n");
                sb.append("**************************************\r\n")

                //           sb.append("反查电阻:" + CsAlgoBuilderEx.getResistance(100f,(byte)0,38.0f,18,20.2f));
            } catch (ex: Exception) {
                sb.append("Input error, error code：" + ex.localizedMessage)
            }
//                } else {
//                    sb.append("Input error, error code：" + cSBiasV235Resp.result)
//                }
//            binding.tvData.setText(sb.toString())



            binding.btnFetchWeightData.visibility = View.VISIBLE

            LocalBroadcastManager.getInstance(this@SmartScaleActivity)
                .sendBroadcast(
                    Intent("WEIGHT_DATA").putExtra("scaleData", weight.toString())
                        .putExtra("bmi", binding.tvBMI.text.toString())
                        .putExtra("weightMachineName",binding.tvMacAddressName.text)
                )


//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
        }
    }

    fun eventListener() {
        binding.btnScan.setOnClickListener {
            bluetoothScanner.startScan()
        }
        binding.btnFetchWeightData.setOnClickListener {
            finish()
        }

        binding.btnStopScan.setOnClickListener {
            bluetoothScanner.stopScan()

        }
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

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "SmartScale Device"

        toolbar.setNavigationIcon(R.drawable.icon_arrowback)
        toolbar.setNavigationOnClickListener { finish() }
    }

//    override fun onDataReceived(weight: Double, resistance: Int, productId: Int, macAddress: String) {
//        runOnUiThread {
//            Log.d("MainActivity", "Weight: $weight kg, Resistance: $resistance, Product ID: $productId, MAC: $macAddress")
//            findViewById<TextView>(R.id.textView).text = "Weight: $weight kg"
//        }
//    }

}