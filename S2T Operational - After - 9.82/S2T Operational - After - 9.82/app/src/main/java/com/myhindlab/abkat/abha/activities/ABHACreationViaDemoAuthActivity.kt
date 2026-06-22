package com.myhindlab.abkat.abha.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import com.chaos.view.PinView
import com.myhindlab.abkat.BuildConfig
import com.myhindlab.abkat.R
import com.myhindlab.abkat.abha.models.ABHASessionModel
import com.myhindlab.abkat.abha.models.abha_creation_response_via_aadhaar_demo_auth.EnrollByAadhaarViaDemoAuthCreationResponseModel
import com.myhindlab.abkat.abha.models.district_list.DistrictOnStateListResponseModel
import com.myhindlab.abkat.abha.models.district_list.Output
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar_via_demo_auth.AuthData
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar_via_demo_auth.Consent
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar_via_demo_auth.DemoAuth
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar_via_demo_auth.EnrollByAadhaarViaDemoAuth
import com.myhindlab.abkat.abha.models.state_list.StateListResponseModel
import com.myhindlab.abkat.abha.utilities.RSAUtil
import com.myhindlab.abkat.rest.ApiClient
import com.myhindlab.abkat.rest.ApiInterface
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.myhindlab.abkat.HealthCheckup
import com.myhindlab.abkat.utilities.ApplicationConstants
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities
import com.myhindlab.abkat.utilities.utils.GenericSelectionDialog
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Base64
import java.util.Calendar
import java.util.UUID

class ABHACreationViaDemoAuthActivity : AppCompatActivity() {

    val TAG: String = ABHACreationViaDemoAuthActivity::class.java.simpleName
    private lateinit var districtId: String
    private lateinit var stateId: String
    lateinit var cbAadhaarConsent: AppCompatCheckBox;
    lateinit var cbABHANumberConsent: AppCompatCheckBox;
    lateinit var cbABHANumberLinkConsent: AppCompatCheckBox;
    lateinit var cbHealthRecordSharingConsent: AppCompatCheckBox;
    lateinit var cbAnonymizationConsent: AppCompatCheckBox;
    lateinit var cbAnonymization1Consent: AppCompatCheckBox;
    lateinit var cbAnonymization2Consent: AppCompatCheckBox;
    lateinit var cbAcceptAll: AppCompatCheckBox;
    lateinit var tvOTPMsg: TextView;
    lateinit var tvMsg: TextView;
    lateinit var edtAadharNumber: TextInputEditText;
    lateinit var ilAadharNumber: TextInputLayout;
    lateinit var edtAadharMobileNumber: TextInputEditText;
    lateinit var ilAadharMobileNumber: TextInputLayout;
    lateinit var edtDistrictCode: TextInputEditText;
    lateinit var edtStateCode: TextInputEditText;
    lateinit var edtAddress: TextInputEditText;
    lateinit var edtPincode: TextInputEditText;
    lateinit var edtDOB: TextInputEditText;
    lateinit var edtName: TextInputEditText;
    lateinit var ilDOB: TextInputLayout;
    lateinit var ilDistrictCode: TextInputLayout;
    lateinit var ilStateCode: TextInputLayout;
    lateinit var ilAddress: TextInputLayout;
    lateinit var ilPincode: TextInputLayout;
    lateinit var ilPersonName: TextInputLayout;

    private lateinit var rgGender: RadioGroup
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var rbOtherGender: RadioButton

    lateinit var svOTPMsg: LinearLayoutCompat;
    lateinit var llTermsAndCondition: LinearLayoutCompat;
    lateinit var otpView: PinView
    lateinit var llOTP: LinearLayoutCompat;
    lateinit var btnGetOTP: Button;
    lateinit var btnVerify: Button;
    lateinit var btnResend: Button;
    lateinit var sessionManager: UserSessionManager
    var otp: String? = null
    var txnId: String? = null
    var accessToken: String? = null
    var authToken: String? = null
    var publicKey: String? = null
    var creationResponse: EnrollByAadhaarViaDemoAuthCreationResponseModel? = null
    lateinit var tvTimer: TextView
    private var isTimerFinished: Boolean = false
    lateinit var llOTPTimer: LinearLayoutCompat;
    lateinit var cdt: CountDownTimer

    var isAadhaarLinked = false;
    var whichOTP = 0;

    var resendOTPCount = 0;
    var resendOTPCountForMobileLink = 0;
    lateinit var progressDialog: ProgressDialog
    private var cayear = 0;
    private var camonth: Int = 0;
    private var caday: Int = 0;
    private var genderId: String? = null
    private var selectedStateCode: Int? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abhacreation_via_demo_auth)
        setUpToolbar()
        init()
        eventListener()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun init() {

        sessionManager = UserSessionManager(this@ABHACreationViaDemoAuthActivity)
        progressDialog = ProgressDialog(this@ABHACreationViaDemoAuthActivity)
        edtAadharNumber = findViewById(R.id.edtAadharNumber)
        ilAadharNumber = findViewById(R.id.ilAadharNumber)
        edtAadharMobileNumber = findViewById(R.id.edtAadharMobileNumber)
        ilAadharMobileNumber = findViewById(R.id.ilAadharMobileNumber)

        edtStateCode = findViewById(R.id.edtStateCode)
        ilStateCode = findViewById(R.id.ilStateCode)

        edtAddress = findViewById(R.id.edtAddress)
        ilAddress = findViewById(R.id.ilAddress)

        edtPincode = findViewById(R.id.edtPincode)
        ilPincode = findViewById(R.id.ilPincode)



        btnGetOTP = findViewById(R.id.btnGetOTP)
        btnVerify = findViewById(R.id.btnVerify)
        btnResend = findViewById(R.id.btnResend)
        cbAadhaarConsent = findViewById(R.id.cbAadhaarConsent)
        cbABHANumberLinkConsent = findViewById(R.id.cbABHANumberLinkConsent)
        cbHealthRecordSharingConsent = findViewById(R.id.cbHealthRecordSharingConsent)
        cbABHANumberConsent = findViewById(R.id.cbABHANumberConsent)
        cbAnonymizationConsent = findViewById(R.id.cbAnonymizationConsent)
        cbAnonymization1Consent = findViewById(R.id.cbAnonymization1Consent)
        cbAnonymization2Consent = findViewById(R.id.cbAnonymization2Consent)
        cbAcceptAll = findViewById(R.id.cbAcceptAll)

        otpView = findViewById(R.id.otpView)
        tvOTPMsg = findViewById(R.id.tvOTPMsg)
        tvMsg = findViewById(R.id.tvMsg)
        llOTP = findViewById(R.id.llOTP)
        svOTPMsg = findViewById(R.id.svOTPMsg)
        llTermsAndCondition = findViewById(R.id.llTermsAndCondition)
        edtDistrictCode = findViewById(R.id.edtDistrictCode)
        edtDOB = findViewById(R.id.edtDOB)
        edtName = findViewById(R.id.edtPersonName)
        rgGender = findViewById(R.id.rgGender)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        rbOtherGender = findViewById(R.id.rbOtherGender)

        ilDOB = findViewById(R.id.ilDOB)
        ilDistrictCode = findViewById(R.id.ilDistrictCode)
        ilPersonName = findViewById(R.id.ilPersonName)

        llOTPTimer = findViewById(R.id.llOTPTimer)
        tvTimer = findViewById(R.id.tvTimer)
        llOTP.visibility = View.GONE

        val anonymization1Consent =
            "I, <b>${sessionManager.userDetailsJson.name}</b>, confirm that I have duly informed and explained the beneficiary of the\n" +
                    "contents of consent for aforementioned purposes"
        cbAnonymization1Consent.setText(Html.fromHtml(anonymization1Consent))
        val ca = Calendar.getInstance()
        cayear = ca[Calendar.YEAR]
        camonth = ca[Calendar.MONTH]
        caday = ca[Calendar.DAY_OF_MONTH]
        createABHASession()
    }

    fun setUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "ABHA creation"

        toolbar.setNavigationOnClickListener {
            finish()
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
//                            Log.d(TAG, "createABHASession: ${response.body()?.string()}")
                            val json = response.body()?.string()?.let { JSONObject(it) }
                            if (json != null) {
                                accessToken = json.getString("accessToken")
                                btnGetOTP.isEnabled = true
                                getPublicCertificate()
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            btnGetOTP.isEnabled = false
                            Utilities.showMessageString(
                                "Unable to create session",
                                this@ABHACreationViaDemoAuthActivity
                            )

                        }


                    } else {
                        Utilities.showAlertDialog(
                            this@ABHACreationViaDemoAuthActivity,
                            "Unable to create session",
                            response.errorBody()?.string(), false
                        )
                        btnGetOTP.isEnabled = false

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d(TAG, "createABHASession: ${t.message}")
                    btnGetOTP.isEnabled = false
                    Utilities.showMessageString(
                        "Unable to create session",
                        this@ABHACreationViaDemoAuthActivity
                    )


                }

            })

    }

    private fun eventListener() {
//        cbAadhaarConsent.setOnCheckedChangeListener { buttonView, isChecked ->
//            btnGetOTP.isEnabled = isChecked
//        }

        rgGender.setOnCheckedChangeListener { radioGroup, i ->

            if (rbMale.isChecked) {
                genderId = "M"
            }

            if (rbFemale.isChecked) {
                genderId = "F"
            }

            if (rbOtherGender.isChecked) {
                genderId = "O";
            }
        }
        edtDOB.setOnClickListener {
            val dpd = DatePickerDialog(
                this@ABHACreationViaDemoAuthActivity,
                { view, year, monthOfYear, dayOfMonth ->
                    var m = ""
                    val month = monthOfYear + 1
                    m = if (month < 10) "0$month"
                    else month.toString()

                    var d = ""
                    d = if (dayOfMonth < 10) "0$dayOfMonth"
                    else dayOfMonth.toString()

                    try {
                        val parsedDate: String? =
                            Utilities.dfDDMMYYYYDash.format(Utilities.dfDDMMYYYYDash.parse("$d-$m-$year"));
                        edtDOB.setText(parsedDate)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }, cayear, camonth, caday
            )
            try {
                val c = Calendar.getInstance()
                c.add(Calendar.DAY_OF_MONTH, -1)
                dpd.datePicker.calendarViewShown = false
                dpd.datePicker.maxDate = c.timeInMillis
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            dpd.show()
        }
        edtDistrictCode.setOnClickListener {
            if (selectedStateCode == null) {
                Utilities.showMessageString(
                    "Please select state first",
                    this@ABHACreationViaDemoAuthActivity
                )
                return@setOnClickListener
            }
            getDistrictListApi(edtDistrictCode, selectedStateCode!!)
        }
        edtStateCode.setOnClickListener {
            getStateListApi(edtStateCode)
        }
        edtAadharNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtAadharNumber.text?.isEmpty() == true) {
                    ilAadharNumber.error = "Please enter aadhaar number"
                    return

                } else {
                    ilAadharNumber.error = null
                    ilAadharNumber.isErrorEnabled = false
                }

                if (edtAadharNumber.text?.length != 12) {
                    ilAadharNumber.error = "Please enter 12 digit valid aadhaar number"
                    return

                } else {
                    ilAadharNumber.error = null
                    ilAadharNumber.isErrorEnabled = false

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        edtAadharMobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtAadharMobileNumber.text?.isEmpty() == true) {
                    ilAadharMobileNumber.error = "Please enter mobile number"
                    return
                } else {
                    ilAadharMobileNumber.error = null
                    ilAadharMobileNumber.isErrorEnabled = false
                }

                if (!Utilities.isValidMobileno(edtAadharMobileNumber.text.toString())) {
                    ilAadharMobileNumber.error =
                        "Please enter valid mobile number"

                    return

                } else {
                    ilAadharMobileNumber.error = null
                    ilAadharMobileNumber.isErrorEnabled = false

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        edtDOB.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!edtDOB.text.toString().isEmpty()) {
                    ilDOB.error = null
                    ilDOB.isErrorEnabled = false

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        edtDistrictCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!edtDistrictCode.text.toString().isEmpty()) {
                    ilDistrictCode.error = null
                    ilDistrictCode.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!edtName.text.toString().isEmpty()) {
                    ilPersonName.error = null
                    ilPersonName.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        btnGetOTP.setOnClickListener {
            Log.i(TAG, "eventListener: ")

            if (ilAadharNumber.visibility == View.VISIBLE) {
                if (edtAadharNumber.text?.isEmpty() == true) {
                    ilAadharNumber.error = "Please enter aadhaar number"
                    return@setOnClickListener

                } else {
                    ilAadharNumber.error = null
                }

                if (edtAadharNumber.text?.length != 12) {
                    ilAadharNumber.error = "Please enter 12 digit valid aadhaar number"
                    return@setOnClickListener

                } else {
                    ilAadharNumber.error = null
                }
            }

            val aadhaarNumber = edtAadharNumber.text.toString()

            if (edtName.text.toString().isEmpty()) {
                ilPersonName.error = "Please enter Name as per Aadhaar."
                return@setOnClickListener
            }

            if (edtDOB.text.toString().isEmpty()) {
                ilDOB.error = "Please select date of birth"
                return@setOnClickListener
            }
            if (genderId == null) {
                Utilities.showMessageString(
                    "Please select gender",
                    this@ABHACreationViaDemoAuthActivity
                )
                return@setOnClickListener
            }


            if (edtStateCode.text.toString().isEmpty()) {
                ilStateCode.error = "Please select State"
                return@setOnClickListener
            }

            if (edtDistrictCode.text.toString().isEmpty()) {
                ilDistrictCode.error = "Please select District"
                return@setOnClickListener
            }

            if (ilAadharMobileNumber.visibility == View.VISIBLE) {
                if (edtAadharMobileNumber.text?.isEmpty() == true) {
                    ilAadharMobileNumber.error = "Please enter mobile number"
                    return@setOnClickListener

                } else {
                    ilAadharMobileNumber.error = null
                }

                if (edtAadharMobileNumber.text.toString().isNotEmpty()) {
                    if (!Utilities.isValidMobileno(edtAadharMobileNumber.text.toString())) {
                        ilAadharMobileNumber.error =
                            "Please enter valid mobile number"
                        return@setOnClickListener

                    } else {
                        ilAadharMobileNumber.error = null
                    }
                }
            }


            if (!cbAadhaarConsent.isChecked || !cbAnonymizationConsent.isChecked || !cbAnonymization1Consent.isChecked || !cbAnonymization2Consent.isChecked || !cbHealthRecordSharingConsent.isChecked || !cbABHANumberLinkConsent.isChecked) {
                Utilities.showMessageString(
                    "Please accept terms and conditions to proceed!",
                    this@ABHACreationViaDemoAuthActivity
                )
                return@setOnClickListener
            }
            if (svOTPMsg.visibility == View.VISIBLE) {
                svOTPMsg.visibility = View.GONE
            }
            enrolByAadhaarDemoAuth()


        }

        btnVerify.setOnClickListener {

            try {

                enrolByAadhaarDemoAuth()

            } catch (ex: NumberFormatException) {
                ex.printStackTrace()

            }

        }

        btnResend.setOnClickListener {

            otpView.text?.clear()

            if (ilAadharNumber.visibility == View.VISIBLE) {
                if (edtAadharNumber.text?.isEmpty() == true) {
                    ilAadharNumber.error = "Please enter aadhaar number"
                    return@setOnClickListener

                } else {
                    ilAadharNumber.error = null
                }

                if (edtAadharNumber.text?.length != 12) {
                    ilAadharNumber.error = "Please enter 12 digit valid aadhaar number"
                    return@setOnClickListener

                } else {
                    ilAadharNumber.error = null
                }
            }
            if (ilAadharMobileNumber.visibility == View.VISIBLE) {
                if (edtAadharMobileNumber.text?.isEmpty() == true) {
                    ilAadharMobileNumber.error = "Please enter mobile number"
                    return@setOnClickListener

                } else {
                    ilAadharMobileNumber.error = null
                }

                if (edtAadharMobileNumber.text.toString().isNotEmpty()) {
                    if (!Utilities.isValidMobileno(edtAadharMobileNumber.text.toString())) {
                        ilAadharMobileNumber.error =
                            "Please enter valid mobile number"
                        return@setOnClickListener

                    } else {
                        ilAadharMobileNumber.error = null
                    }
                }
            }


            if (!cbAadhaarConsent.isChecked || !cbAnonymizationConsent.isChecked || !cbAnonymization1Consent.isChecked || !cbAnonymization2Consent.isChecked || !cbHealthRecordSharingConsent.isChecked || !cbABHANumberLinkConsent.isChecked) {
                Utilities.showMessageString(
                    "Please accept terms and conditions to proceed!",
                    this@ABHACreationViaDemoAuthActivity
                )
                return@setOnClickListener
            }
            if (svOTPMsg.visibility == View.VISIBLE) {
                svOTPMsg.visibility = View.GONE
            }
            val aadhaarNumber = edtAadharNumber.text.toString()

            if (resendOTPCount == 2) {
                Utilities.showAlertDialog(
                    this@ABHACreationViaDemoAuthActivity,
                    "Restricted",
                    "You have reached OTP resend limit",
                    false
                );

                return@setOnClickListener
            }


        }

//        cbAcceptAll.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
//            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
//                cbAadhaarConsent.isChecked = cbAcceptAll.isChecked
//                cbABHANumberConsent.isChecked = cbAcceptAll.isChecked
//                cbAnonymizationConsent.isChecked = cbAcceptAll.isChecked
//                cbABHANumberLinkConsent.isChecked = cbAcceptAll.isChecked
//                cbAnonymization1Consent.isChecked = cbAcceptAll.isChecked
//                cbAnonymization2Consent.isChecked = cbAcceptAll.isChecked
//                cbHealthRecordSharingConsent.isChecked = cbAcceptAll.isChecked
//
//            }
//
//        })


        cbAcceptAll.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                cbAadhaarConsent.isChecked = isChecked
                cbABHANumberConsent.isChecked = isChecked
                cbAnonymizationConsent.isChecked = isChecked
                cbABHANumberLinkConsent.isChecked = isChecked
                cbAnonymization1Consent.isChecked = isChecked
                cbAnonymization2Consent.isChecked = isChecked
                cbHealthRecordSharingConsent.isChecked = isChecked
            }
        })


    }


    private fun getPublicCertificate() {
        progressDialog.setMessage("Loading..")
        progressDialog.setCancelable(false)
        progressDialog.show();

        HealthCheckup.ABDMClient.getPublicCertificate(
            "Bearer ${accessToken}",
            Utilities.getCurrentTimeStamp(),
            UUID.randomUUID().toString()
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    var res = response.body()?.string()
                    Log.i(TAG, "onResponse: ${res}")
                    if (response.isSuccessful) {
                        val parsed = res?.let { JSONObject(it) }
                        var key = parsed?.getString("publicKey")
                        publicKey = key

                    } else {
                        Utilities.showAlertDialog(
                            this@ABHACreationViaDemoAuthActivity,
                            "Unable to generate public certificate",
                            response.errorBody()?.string(),
                            false
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d(TAG, "onResponse: ${t.message}")
                }

            })

    }


    private fun enrolByAadhaarDemoAuth() {
        progressDialog.setMessage("Verifying Aadhaar details..")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var encryptedAadhaar: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encryptedAadhaar = Base64.getEncoder()
                .encodeToString(RSAUtil.encrypt(edtAadharNumber.text.toString(), publicKey))
        }
        val authData =
            AuthData(
                authMethods = arrayListOf("demo_auth"),
                demoAuth = DemoAuth(
                    aadhaarNumber = encryptedAadhaar,
                    districtCode = districtId,
                    mobile = edtAadharMobileNumber.text.toString(),
                    stateCode = "27",
                    dateOfBirth = edtDOB.text.toString(),
                    gender = genderId,
                    name = edtName.text.toString(),
                    pinCode = edtPincode.text.toString(),
                    address = edtAddress.text.toString()
                )

            )
        val payload = EnrollByAadhaarViaDemoAuth(
            authData = authData,
            consent = Consent(code = "abha-enrollment", version = "1.4")
        )

        Log.i(TAG, "enrolByAadhaarOTP: ${Gson().toJson(payload)}")

        HealthCheckup.ABDMClient.enrollByAAdhaarViaDemoAuth(
            "Bearer $accessToken", Utilities.getCurrentTimeStamp(),
            UUID.randomUUID().toString(), BuildConfig.BENEFIT_NAME, payload
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            val res = response.body()?.string()
                            Log.d(TAG, "verifyOTP: $res")
                            val jsonObject = JSONObject(res)
//                            txnId = jsonObject.getString("txnId");
                            val tokenObj = jsonObject.getJSONObject("jwtResponse");

                            authToken = tokenObj.getString("token")

//                            if (txnId != null) {
                            creationResponse =
                                Gson().fromJson<EnrollByAadhaarViaDemoAuthCreationResponseModel>(
                                    res,
                                    EnrollByAadhaarViaDemoAuthCreationResponseModel::class.java
                                )
                            val isNew = creationResponse?.new ?: false
//                            startActivity(
//                                    Intent(
//                                        this@ABHACreationViaDemoAuthActivity,
//                                        ABHAAddressCreationActivity::class.java
//                                    ).putExtra("accessToken", accessToken)
//                                        .putExtra("txnId", txnId)
//                                        .putExtra("authToken", authToken)
//                                        .putExtra("demoAuthHealthCard", creationResponse)
//                                        .putExtra("isNew",isNew )
//                                        .putExtra(
//                                            "mobile",
//                                            edtAadharMobileNumber.text.toString()
//                                        )
//                                )
                            startActivity(
                                Intent(
                                    this@ABHACreationViaDemoAuthActivity,
                                    ABHAHealthIDActivity::class.java
                                )
                                    .putExtra("verificationCard", 0)
                                    .putExtra("mobile", intent.getLongExtra("mobile", 0))
                                    .putExtra("accessToken", accessToken)
                                    .putExtra("txnId", txnId)
                                    .putExtra("authToken", authToken)
                            );
                            finish()
//                                ilAadharMobileNumber.visibility = View.VISIBLE
//                                ilAadharNumber.visibility = View.GONE
//                                llTermsAndCondition.visibility = View.GONE
//                                llOTP.visibility = View.GONE
//                                llOTPTimer.visibility = View.GONE
//                                btnGetOTP.visibility = View.VISIBLE
//                                btnResend.visibility = View.GONE
//                                btnVerify.visibility = View.GONE
//                                svOTPMsg.visibility = View.GONE
//                                tvMsg.text = "Enter communication mobile number"
//                                otpView.text?.clear()

//                                cdt.cancel()

//                            }
                        } catch (e: java.lang.Exception) {

                            e.printStackTrace()
                            Utilities.showAlertDialog(
                                this@ABHACreationViaDemoAuthActivity,
                                "Error",
                                e.message,
                                false
                            )
                        }
                    } else {
                        try {
                            val errRes = response.errorBody()!!.string()
                            val jsonObject = JSONObject(errRes)
                            if (jsonObject.has("error")) {
                                val errJ = jsonObject.getJSONObject("error")
                                Utilities.showAlertDialog(
                                    this@ABHACreationViaDemoAuthActivity,
                                    "Unable to create ABHA",
                                    "The provided Personal Identity information didn't match. Please re-enter the details correctly.",
                                    false
                                )
                            } else if (errRes.contains("Invalid Aadhaar")) {
                                Utilities.showAlertDialog(
                                    this@ABHACreationViaDemoAuthActivity,
                                    "Unable to create ABHA",
                                    "Invalid Aadhaar Number",
                                    false
                                )
                            } else {
                                Utilities.showAlertDialog(
                                    this@ABHACreationViaDemoAuthActivity,
                                    "Unable to create ABHA",
                                    "The provided Personal Identity information didn't match. Please re-enter the details correctly.",
                                    false
                                )
                            }

//{"error":{"code":"ABDM-1204","message":"UIDAI Error code : 100 : The provided Personal Identity information didn't match. Please re-enter the details correctly."}}

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    progressDialog.dismiss()
                    Log.d(TAG, "onResponse: ${t.message}")
                }

            })

    }


    fun saveABHADetailsToOurServer(
        accountProfileRequestModel: EnrollByAadhaarViaDemoAuthCreationResponseModel,
        flag: Int?
    ) {
        accountProfileRequestModel.createdBy =
            sessionManager.userDetailsJson.empCode
        accountProfileRequestModel.facilityCode =
            sessionManager.userDetailsJson.facilityID
        accountProfileRequestModel.districtLGDCode =
            sessionManager.userDetailsJson.distlgdcode
        accountProfileRequestModel.districtID =
            sessionManager.userDetailsJson.hlldistrictid
        progressDialog.setMessage("Storing Profile Data...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        HealthCheckup.myhindlabClient.saveAbhaDetails(accountProfileRequestModel)
            .enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        progressDialog.dismiss()
                        try {

                            if (response.isSuccessful) {
                                var stringResp = response.body()?.string()
                                Log.d(TAG, "onResponse: ${stringResp}")
                                val js = JSONObject(stringResp)
                                val status = js.getString("Status");
                                if (status.equals("Success")) {
                                    if (flag == 1) {
                                        startActivity(
                                            Intent(
                                                this@ABHACreationViaDemoAuthActivity,
                                                ABHAAddressCreationActivity::class.java
                                            ).putExtra("accessToken", accessToken)
                                                .putExtra("txnId", txnId)
                                                .putExtra("authToken", authToken)
//                                                .putExtra("healthCard", creationResponse)
                                                .putExtra(
                                                    "mobile",
                                                    edtAadharMobileNumber.text.toString()
                                                )
                                        )

                                        finish()
                                    }

                                } else {
                                    Utilities.showAlertDialogMandatory(
                                        this@ABHACreationViaDemoAuthActivity,
                                        "Error",
                                        "Unable to save ABHA details",
                                        false,
                                        "Re-Try",
                                        object : DialogInterface.OnClickListener {
                                            override fun onClick(
                                                dialog: DialogInterface?,
                                                which: Int
                                            ) {
                                                saveABHADetailsToOurServer(
                                                    accountProfileRequestModel, flag
                                                )
                                            }

                                        })

                                }

                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Utilities.showAlertDialogMandatory(
                                this@ABHACreationViaDemoAuthActivity,
                                "Error",
                                "Unable to save ABHA details",
                                false,
                                "Re-Try",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(
                                        dialog: DialogInterface?,
                                        which: Int
                                    ) {
                                        saveABHADetailsToOurServer(
                                            accountProfileRequestModel, flag
                                        )
                                    }

                                })
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        progressDialog.dismiss()
                        Log.e(TAG, "onFailure: ", t)
                        Utilities.showAlertDialogMandatory(
                            this@ABHACreationViaDemoAuthActivity,
                            "Error",
                            "Unable to save ABHA details",
                            false,
                            "Re-Try",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(
                                    dialog: DialogInterface?,
                                    which: Int
                                ) {
                                    saveABHADetailsToOurServer(
                                        accountProfileRequestModel, flag
                                    )
                                }

                            })

                    }

                })

    }


    private fun getDistrictListApi(edt_select_district: TextInputEditText, selectedState: Int) {
        progressDialog.setMessage("Please Wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call = HealthCheckup.myhindlabClient.getDistrictsOnState(selectedState)
        call.enqueue(object : Callback<DistrictOnStateListResponseModel> {
            override fun onResponse(
                call: Call<DistrictOnStateListResponseModel>,
                response: Response<DistrictOnStateListResponseModel>
            ) {
                progressDialog.dismiss()
                if (response.code() == ApplicationConstants.OKSUCCESS) {
                    if (response.body()!!.Status.equals("Success", ignoreCase = true)) {
                        if (response.body()!!.output.size != 0) {
                            val dialog =
                                GenericSelectionDialog<Output>(
                                    "Select District",
                                    response.body()!!.output,
                                    { pat: Output -> pat.DistrictNameEnglish!! },
                                    { selectedItem: Output ->
                                        try {
                                            districtId = selectedItem.DistrictLGDCode!!
                                            edtDistrictCode.setText(selectedItem.DistrictNameEnglish!!)
                                        } catch (e: java.lang.Exception) {
                                            e.printStackTrace()
                                        }
                                        Unit
                                    }
                                )

                            dialog.show(supportFragmentManager, "GetDistrictDialog")
                        }
                    } else {
                        Utilities.showAlertDialog(
                            this@ABHACreationViaDemoAuthActivity, "Alert", response.body()!!
                                .Message, false
                        )
                    }
                } else {
                    Utilities.showAlertDialog(
                        this@ABHACreationViaDemoAuthActivity,
                        "Alert",
                        "Server Not Responding",
                        false
                    )
                }
            }

            override fun onFailure(call: Call<DistrictOnStateListResponseModel>, t: Throwable) {
                progressDialog.dismiss()
                t.printStackTrace()
                Utilities.showAlertDialog(
                    this@ABHACreationViaDemoAuthActivity,
                    "Alert",
                    "Server Not Responding",
                    false
                )
            }
        })
    }

    private fun showDistrictListDialog(
        output: List<Output>,
        edt_select_district: EditText
    ) {

        val builder = AlertDialog.Builder(this@ABHACreationViaDemoAuthActivity)
        builder.setTitle("Select District")
        builder.setCancelable(false)

        val arrayAdapter =
            ArrayAdapter<String>(this@ABHACreationViaDemoAuthActivity, R.layout.list_row_city_state)

        for (outputBean in output) {
            arrayAdapter.add(outputBean.DistrictNameEnglish)
        }

        builder.setAdapter(arrayAdapter) { dialog, which ->
            districtId = output[which].DistrictLGDCode.toString()
            edt_select_district.setText(output[which].DistrictNameEnglish)
        }

        builder.setNegativeButton(
            "cancel"
        ) { dialog, which -> }

        builder.create().show()
    }

    private fun getStateListApi(edt: TextInputEditText) {
        progressDialog.setMessage("Please Wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call = HealthCheckup.myhindlabClient.state
        call.enqueue(object : Callback<StateListResponseModel> {
            override fun onResponse(
                call: Call<StateListResponseModel>,
                response: Response<StateListResponseModel>
            ) {
                progressDialog.dismiss()
                if (response.code() == ApplicationConstants.OKSUCCESS) {
                    if (response.body()!!.Status.equals("Success", ignoreCase = true)) {
                        if (response.body()!!.output.size != 0) {
                            val dialog =
                                GenericSelectionDialog<com.myhindlab.abkat.abha.models.state_list.Output>(
                                    "Select State",
                                    response.body()!!.output,
                                    { pat: com.myhindlab.abkat.abha.models.state_list.Output -> pat.StateNameEnglish!! },
                                    { selectedItem: com.myhindlab.abkat.abha.models.state_list.Output ->
                                        try {
                                            selectedStateCode = selectedItem.StateLGDCode!!.toInt()
                                            edtStateCode.setText(selectedItem.StateNameEnglish!!)
                                            ilStateCode.isErrorEnabled=false
                                            ilStateCode.error=null
                                        } catch (e: java.lang.Exception) {
                                            e.printStackTrace()
                                        }
                                        Unit
                                    }
                                )
                            dialog.show(supportFragmentManager, "GetStateDialog")
                        }
                    } else {
                        Utilities.showAlertDialog(
                            this@ABHACreationViaDemoAuthActivity,
                            "Alert",
                            response.body()!!.Message,
                            false
                        )
                    }
                } else {
                    Utilities.showAlertDialog(
                        this@ABHACreationViaDemoAuthActivity,
                        "Alert",
                        "Server Not Responding",
                        false
                    )
                }
            }

            override fun onFailure(call: Call<StateListResponseModel>, t: Throwable) {
                progressDialog.dismiss()
                t.printStackTrace()
                Utilities.showAlertDialog(
                    this@ABHACreationViaDemoAuthActivity,
                    "Alert",
                    "Server Not Responding",
                    false
                )
            }
        })
    }

    private fun showStateListDialog(
        output: List<com.myhindlab.abkat.abha.models.state_list.Output>,
        edt: EditText
    ) {

        val builder = AlertDialog.Builder(this@ABHACreationViaDemoAuthActivity)
        builder.setTitle("Select State")
        builder.setCancelable(false)

        val arrayAdapter =
            ArrayAdapter<String>(this@ABHACreationViaDemoAuthActivity, R.layout.list_row_city_state)

        for (outputBean in output) {
            arrayAdapter.add(outputBean.StateNameEnglish)
        }

        builder.setAdapter(arrayAdapter) { dialog, which ->
            districtId = output[which].StateLGDCode.toString()
            edt.setText(output[which].StateNameEnglish)
        }

        builder.setNegativeButton(
            "cancel"
        ) { dialog, which -> }

        builder.create().show()
    }
}