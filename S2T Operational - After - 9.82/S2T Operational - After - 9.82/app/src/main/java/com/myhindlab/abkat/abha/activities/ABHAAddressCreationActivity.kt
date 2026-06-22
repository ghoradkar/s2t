package com.myhindlab.abkat.abha.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.myhindlab.abkat.BuildConfig
import com.myhindlab.abkat.HealthCheckup
import com.myhindlab.abkat.R
import com.myhindlab.abkat.abha.adapters.PHRSuggestionsListAdapter
import com.myhindlab.abkat.abha.models.ABHASessionModel
import com.myhindlab.abkat.abha.models.abha_creation_response_model.ABHACreationResponseModel
import com.myhindlab.abkat.utilities.RecyclerItemClickListener
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class ABHAAddressCreationActivity : AppCompatActivity() {
    lateinit var tvABHAAddressValidationMsg: TextView;
    lateinit var tvABHADomain: TextView;
    lateinit var edtABHAAddress: TextInputEditText;
    lateinit var ilABHAAddress: TextInputLayout;
    lateinit var sessionManager: UserSessionManager
    var txnId: String? = null
    var accessToken: String? = null
    var authToken: String? = null
    var createHealthIdAuthToken: String? = null
    var mobile: Long? = null
    lateinit var imvAddressValidation: ImageButton
    lateinit var progressDialog: ProgressDialog
    val TAG: String = ABHAAddressCreationActivity::class.java.simpleName
    lateinit var rvPHRAddress: RecyclerView

    lateinit var btnCheckAvailability: Button
    lateinit var btnCreateABHAAddress: Button
    lateinit var btnContinueWithExisting: Button
    lateinit var phrSuggestionsList: ArrayList<String>
    lateinit var tvExistingAddress: TextView
    lateinit var tvExistingName: TextView
    lateinit var tvExistingNumber: TextView
    lateinit var llExistingDetails: LinearLayoutCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abhaaddress_creation)
        setUpToolbar()
        init()
//        setDefault()
        eventListener()
    }

    fun setDefault() {
//        val healthCard: CreateHealthIdResponseModel =
//            intent.getSerializableExtra("healthCard") as CreateHealthIdResponseModel

        try {


            val healthCard: ABHACreationResponseModel =
                intent.getSerializableExtra("healthCard") as ABHACreationResponseModel

            Log.i(TAG, "setDefault: ${Gson().toJson(healthCard)}")
            Log.i(TAG, "setDefault: ${healthCard.ABHAProfile!!.phrAddress[0]}")
            val isNew = intent.getBooleanExtra("isNew", false)

            if (!isNew) {
                val existingABHAAddress = intent.getStringExtra("existingABHAAddress")
                tvExistingAddress.text = existingABHAAddress
                tvExistingName.text =
                    healthCard.ABHAProfile!!.firstName + " " + healthCard.ABHAProfile!!.lastName
                tvExistingNumber.text = healthCard.ABHAProfile!!.ABHANumber
                llExistingDetails.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onResume() {
        super.onResume()
        setDefault()
    }

    private fun setUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "ABHA address creation"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun init() {
        sessionManager = UserSessionManager(this@ABHAAddressCreationActivity)
        progressDialog = ProgressDialog(this@ABHAAddressCreationActivity)
        phrSuggestionsList = ArrayList()
        edtABHAAddress = findViewById(R.id.edtABHAAddress)
        tvABHAAddressValidationMsg = findViewById(R.id.tvABHAAddressValidationMsg)
        imvAddressValidation = findViewById(R.id.imvAddressValidation)
        btnCreateABHAAddress = findViewById(R.id.btnCreateABHAAddress)
        btnCheckAvailability = findViewById(R.id.btnCheckAvailability)
        tvABHADomain = findViewById(R.id.tvABHADomain)
        tvExistingAddress = findViewById(R.id.tvExistingAddress)
        llExistingDetails = findViewById(R.id.llExistingDetails)
        btnContinueWithExisting = findViewById(R.id.btnContinueWithExisting)
        tvExistingNumber = findViewById(R.id.tvExistingNumber)
        tvExistingName = findViewById(R.id.tvExistingName)

        tvABHADomain.setText(BuildConfig.CMID);


        rvPHRAddress = findViewById(R.id.rvPHRAddress)
        rvPHRAddress.hasFixedSize()
        rvPHRAddress.layoutManager = LinearLayoutManager(this@ABHAAddressCreationActivity)

        if (intent != null) {
            accessToken = intent.getStringExtra("accessToken")
            txnId = intent.getStringExtra("txnId")
            authToken = intent.getStringExtra("authToken")
            createHealthIdAuthToken = intent.getStringExtra("authToken")
            createABHASession()
        }


    }

    private fun toggleAddressValidationMsg() {
        if (tvABHAAddressValidationMsg.visibility == View.VISIBLE) {
            tvABHAAddressValidationMsg.visibility = View.GONE
        } else {
            tvABHAAddressValidationMsg.visibility = View.VISIBLE
        }
    }


    private fun eventListener() {
        btnContinueWithExisting.setOnClickListener {
            try {

                val healthCard: ABHACreationResponseModel =
                    intent.getSerializableExtra("healthCard") as ABHACreationResponseModel

//                Log.i(TAG, "setDefault: ${healthCard.ABHAProfile!!.phrAddress[0]}")
//                tvExistingAddress.setText(healthCard.ABHAProfile!!.phrAddress[0])


                startActivity(
                    Intent(
                        this@ABHAAddressCreationActivity,
                        ABHAHealthIDActivity::class.java
                    ).putExtra("healthCard", healthCard)
                        .putExtra("verificationCard", 0)
                        .putExtra("mobile", mobile)
                        .putExtra("accessToken", accessToken)
                        .putExtra("txnId", txnId)
                        .putExtra("authToken", authToken)
                        .putExtra("createHealthIdAuthToken", createHealthIdAuthToken)
                        .putExtra("campId", getIntent().getStringExtra("campId"))
                        .putExtra("district", getIntent().getStringExtra("district"))
                        .putExtra("distlgdcode", getIntent().getStringExtra("distlgdcode"))
                        .putExtra("siteId", getIntent().getStringExtra("siteId"))
                        .putExtra("Latitude", getIntent().getStringExtra("Latitude"))
                        .putExtra("Longitude", getIntent().getStringExtra("Longitude"))
                        .putExtra("campType", getIntent().getStringExtra("campType"))
                )
                finish()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        edtABHAAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnCheckAvailability.visibility = View.VISIBLE
                btnCreateABHAAddress.visibility = View.GONE
//                toggleAddressValidationMsg()

                if (edtABHAAddress.text?.isEmpty() == true) {
                    edtABHAAddress.error = "Please enter ABHA address (username)"
                    return

                } else {
                    edtABHAAddress.error = null
                }
                if (!Utilities.isValidABHAAddress(edtABHAAddress.text.toString())) {
                    edtABHAAddress.error = "Enter valid ABHA address"
                    edtABHAAddress.setTextColor(resources.getColor(R.color.red))
                    return
                } else {
                    edtABHAAddress.error = null
                    edtABHAAddress.setTextColor(resources.getColor(R.color.green))
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        imvAddressValidation.setOnClickListener {
            toggleAddressValidationMsg()

            if (edtABHAAddress.text?.isEmpty() == true) {
                edtABHAAddress.error = "Please enter ABHA address (username)"
                return@setOnClickListener

            } else {
                edtABHAAddress.error = null
            }
            if (!Utilities.isValidABHAAddress(edtABHAAddress.text.toString())) {
                edtABHAAddress.error = "Enter valid ABHA address"
                return@setOnClickListener
            } else {
                edtABHAAddress.error = null
            }
        }


        edtABHAAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


//                if (!Utilities.isValidABHAAddress(edtABHAAddress.text.toString())) {
//                    edtABHAAddress.error = "Enter valid ABHA address"
//                    tvABHAAddressValidationMsg.visibility = View.VISIBLE
//                } else {
//                    edtABHAAddress.error = null
//                    tvABHAAddressValidationMsg.visibility = View.GONE
//
//                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        rvPHRAddress.addOnItemTouchListener(RecyclerItemClickListener(
            this@ABHAAddressCreationActivity
        ) { view, position ->
//            checkPHRIsExists(phrSuggestionsList[position])
//            createPHRAddress(phrSuggestionsList[position])
            edtABHAAddress.setText(phrSuggestionsList[position])
        })

        btnCreateABHAAddress.setOnClickListener {
            if (!Utilities.isValidABHAAddress(edtABHAAddress.text.toString())) {
                Utilities.showAlertDialog(
                    this@ABHAAddressCreationActivity,
                    "Invalid abha address",
                    "Please enter abha address as per mentioned rules",
                    false
                )
                return@setOnClickListener
            }
            createPHRAddress(edtABHAAddress.text.toString())
        }
        btnCheckAvailability.setOnClickListener {
            if (!Utilities.isValidABHAAddress(edtABHAAddress.text.toString())) {
                Utilities.showAlertDialog(
                    this@ABHAAddressCreationActivity,
                    "Invalid abha address",
                    "Please enter abha address as per mentioned rules",
                    false
                )
                return@setOnClickListener
            }
            createPHRAddress(edtABHAAddress.text.toString())
        }

    }

    private fun createABHASession() {

        progressDialog.setMessage("Creating session..")
        progressDialog.setCancelable(false)
        progressDialog.show();
        val createSession = ABHASessionModel(
            BuildConfig.ClientID,
            BuildConfig.SecretId,
            grantType = "client_credentials"
        )
        HealthCheckup.abhaClient.createAbhaSession(
            createSession,
            Utilities.getCurrentTimeStamp(),
            UUID.randomUUID().toString(),
            BuildConfig.CMID.replace("@", "")
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            val json = response.body()?.string()?.let { JSONObject(it) }
                            if (json != null) {
                                accessToken = json.getString("accessToken")
                                getPHRSuggestions()

                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            Utilities.showMessageString(
                                "Unable to create session",
                                this@ABHAAddressCreationActivity
                            )

                        }


                    } else {

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d(TAG, "createABHASession: ${t.message}")
                    Utilities.showMessageString(
                        "Unable to create session",
                        this@ABHAAddressCreationActivity
                    )


                }

            })

    }

    private fun getPHRSuggestions() {
        progressDialog.setMessage("Getting PHR Address Suggestions..")
        progressDialog.setCancelable(false)
        progressDialog.show();

//        val payload = JsonObject()
//        payload.addProperty("transactionId", txnId)
        HealthCheckup.ABDMClient.getPHRSuggestions(
            "Bearer $accessToken",
            Utilities.getCurrentTimeStamp(),
            UUID.randomUUID().toString(), txnId
        )
            .enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        progressDialog.dismiss()
                        try {
                            if (response.isSuccessful) {

                                val res = response.body()?.string()
                                val jsonObject = JSONObject(res)
                                val jsonArr: JSONArray =
                                    jsonObject.getJSONArray("abhaAddressList")
//{"txnId":"8fc807a9-5a37-48b6-a21b-a1df290fa4d3","abhaAddressList":["kadamdishank1992","kadamdishank101992","kadamdishank1001","kadamdishank0110","kadam_1992199210","kadam_1992199201","kadam_19.92","kadam_011001","kadam_01011992","kadam_010110"]}

                                for (i in 0 until jsonArr.length()) {
                                    phrSuggestionsList.add(jsonArr.getString(i))
                                }

                                rvPHRAddress.adapter = PHRSuggestionsListAdapter(phrSuggestionsList)
                            } else {
                                rvPHRAddress.adapter = PHRSuggestionsListAdapter(ArrayList())

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        progressDialog.dismiss()
                        rvPHRAddress.adapter = PHRSuggestionsListAdapter(ArrayList())

                        Utilities.showAlertDialog(
                            this@ABHAAddressCreationActivity,
                            "Error",
                            t.message,
                            false
                        )
                    }

                })
    }

//    private fun checkPHRIsExists(address: String) {
//        progressDialog.setMessage("Checking is address already used..")
//        progressDialog.setCancelable(false)
//        progressDialog.show();
//
//
//        HLL_Connect.PHRSBXClient.phrISExists("Bearer $accessToken", address).enqueue(
//            object : Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//
//                    progressDialog.dismiss()
//                    if (response.isSuccessful) {
//                        val res = response.body()?.string()
//                        Log.i(TAG, "onResponse: $res")
//                        if (!res.toBoolean()) {
//                            edtABHAAddress.setText(address)
//                            btnCheckAvailability.visibility = View.GONE
//                            btnCreateABHAAddress.visibility = View.VISIBLE
//                            Utilities.showAlertDialogMandatory(
//                                this@ABHAAddressCreationActivity,
//                                "Success",
//                                "Address is available to use",
//                                true,
//                                "Proceed",
//                                object : DialogInterface.OnClickListener {
//                                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                                        createPHRAddress()
//
//                                    }
//
//                                }, "Cancel", object : DialogInterface.OnClickListener {
//                                    override fun onClick(p0: DialogInterface?, p1: Int) {
//
//                                    }
//
//                                }
//                            )
//
//                        } else {
//                            btnCheckAvailability.visibility = View.VISIBLE
//                            btnCreateABHAAddress.visibility = View.GONE
//                            edtABHAAddress.setText("")
//
//                            Utilities.showAlertDialog(
//                                this@ABHAAddressCreationActivity,
//                                "Warning",
//                                "Address is already used",
//                                false
//                            );
//                        }
//                    } else {
//                        Utilities.showAlertDialog(
//                            this@ABHAAddressCreationActivity,
//                            "Error",
//                            response.message(),
//                            false
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    progressDialog.dismiss()
//                    btnCheckAvailability.visibility = View.VISIBLE
//                    btnCreateABHAAddress.visibility = View.GONE
//                    edtABHAAddress.setText("")
//
//                    Utilities.showAlertDialog(
//                        this@ABHAAddressCreationActivity,
//                        "Error",
//                        t.message,
//                        false
//                    )
//                }
//
//            })
//    }

    private fun createPHRAddress(address: String) {
        progressDialog.setMessage("Creating ABHA Address..")
        progressDialog.setCancelable(false)
        progressDialog.show();
        val timeStamp = Utilities.getCurrentTimeStamp();
        val requestId = UUID.randomUUID().toString();
        val jsonObject = JsonObject()
        jsonObject.addProperty("abhaAddress", address)
        jsonObject.addProperty("txnId", txnId)
        jsonObject.addProperty("preferred", 1)
        HealthCheckup.ABDMClient.createPHRAddress(
            "Bearer $accessToken",
            requestId,
            timeStamp,
            jsonObject
        )
            .enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    try {


                        if (response.isSuccessful) {
                            val res = response.body()?.string();
                            val jRes = JSONObject(res)
//                        authToken = jRes.getString("token")
                            Utilities.showAlertDialog(
                                this@ABHAAddressCreationActivity,
                                "Success",
                                "ABHA Address created successfully",
                                true
                            )
//                        startActivity(
//                            Intent(
//                                this@ABHAAddressCreationActivity,
//                                ABHAHealthIDActivity::class.java
//                            ).putExtra("accessToken", accessToken)
//                                .putExtra("txnId", txnId)
//                                .putExtra("authToken", authToken)
//                        )
                            val healthCard: ABHACreationResponseModel =
                                intent.getSerializableExtra("healthCard") as ABHACreationResponseModel
                            val mobile: Long? = intent.getLongExtra("mobile", 0)

                            startActivity(
                                Intent(
                                    this@ABHAAddressCreationActivity,
                                    ABHAHealthIDActivity::class.java
                                ).putExtra("healthCard", healthCard)
                                    .putExtra("verificationCard", 0)
                                    .putExtra("mobile", mobile)
                                    .putExtra("accessToken", accessToken)
                                    .putExtra("txnId", txnId)
                                    .putExtra("authToken", authToken)
                                    .putExtra("isNew", healthCard.isNew)
                                    .putExtra("createHealthIdAuthToken", createHealthIdAuthToken)
                                    .putExtra("campId", getIntent().getStringExtra("campId"))
                                    .putExtra("district", getIntent().getStringExtra("district"))
                                    .putExtra(
                                        "distlgdcode",
                                        getIntent().getStringExtra("distlgdcode")
                                    )
                                    .putExtra("siteId", getIntent().getStringExtra("siteId"))
                                    .putExtra("Latitude", getIntent().getStringExtra("Latitude"))
                                    .putExtra("Longitude", getIntent().getStringExtra("Longitude"))
                                    .putExtra("campType", getIntent().getStringExtra("campType"))
                            )
                            finish()
                        } else {
                            val errRes = response.errorBody()!!.string()
                            val jsonObject = JSONObject(errRes)
                            if (jsonObject.has("error")) {
                                val errJ = jsonObject.getJSONObject("error")
                                Utilities.showAlertDialog(
                                    this@ABHAAddressCreationActivity,
                                    "Unable to create ABHA address",
                                    errJ.getString("message"),
                                    false
                                )
                            } else if (errRes.contains("Invalid LoginId")) {
                                Utilities.showAlertDialog(
                                    this@ABHAAddressCreationActivity,
                                    "Unable to create ABHA address",
                                    "Aadhaar number is incorrect, Resident shall use correct Aadhaar",
                                    false
                                )
                            } else if (errRes.contains("Invalid ABHA Address")) {
                                Utilities.showAlertDialog(
                                    this@ABHAAddressCreationActivity,
                                    "Unable to create ABHA address",
                                    "Invalid ABHA Address",
                                    false
                                )
                            } else {
                                Utilities.showAlertDialog(
                                    this@ABHAAddressCreationActivity,
                                    "Unable to create ABHA address",
                                    errRes,
                                    false
                                )
                            }
                        }
                    } catch (e: Exception) {
                        Utilities.showAlertDialog(
                            this@ABHAAddressCreationActivity,
                            "Unable to create ABHA address",
                            e.message,
                            false
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Utilities.showAlertDialog(
                        this@ABHAAddressCreationActivity,
                        "onFailure",
                        t.message,
                        false
                    )
                }

            })
    }
}