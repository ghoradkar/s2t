package com.myhindlab.abkat.abha.activities

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.chaos.view.PinView
import com.myhindlab.abkat.BuildConfig
import com.myhindlab.abkat.HealthCheckup
import com.myhindlab.abkat.R
import com.myhindlab.abkat.abha.models.ABHASessionModel
import com.myhindlab.abkat.abha.models.SendOTPRequestModel
import com.myhindlab.abkat.abha.models.abha_creation_response_model.ABHACreationResponseModel
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar.AuthData
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar.Consent
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar.EnrolByAadhaarRequestModel
import com.myhindlab.abkat.abha.models.enrol_by_aadhaar.Otp
import com.myhindlab.abkat.abha.models.verify_mobile_otp.VerifyMobileOTPRequestModel
import com.myhindlab.abkat.abha.utilities.RSAUtil
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.myhindlab.abkat.abha.models.account_profile.AccountProfileResponseModel
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Base64
import java.util.UUID


class ABHACreationActivity : AppCompatActivity() {


    val TAG: String = ABHACreationActivity::class.java.simpleName
    lateinit var cbAadhaarConsent: AppCompatCheckBox;
    lateinit var cbABHANumberConsent: AppCompatCheckBox;
    lateinit var cbABHANumberLinkConsent: AppCompatCheckBox;
    lateinit var cbHealthRecordSharingConsent: AppCompatCheckBox;
    lateinit var cbAnonymizationConsent: AppCompatCheckBox;
    lateinit var cbAnonymization1Consent: AppCompatCheckBox;
    lateinit var cbAnonymization2Consent: AppCompatCheckBox;
    lateinit var tvOTPMsg: TextView;
    lateinit var tvMsg: TextView;
    lateinit var edtAadharNumber: TextInputEditText;
    lateinit var ilAadharNumber: TextInputLayout;
    lateinit var edtAadharMobileNumber: TextInputEditText;
    lateinit var ilAadharMobileNumber: TextInputLayout;

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
    var creationResponse: ABHACreationResponseModel? = null
    lateinit var tvTimer: TextView
    private var isTimerFinished: Boolean = false
    lateinit var llOTPTimer: LinearLayoutCompat;
    lateinit var cdt: CountDownTimer

    var isAadhaarLinked = false;
    var whichOTP = 0;
    var resendOTPCount = 0;
    var resendOTPCountForMobileLink = 0;
    lateinit var tvOTPAttempts:TextView


    lateinit var progressDialog: ProgressDialog

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abha_creation)
        setUpToolbar()
        init()
        eventListener()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun init() {

        sessionManager = UserSessionManager(this@ABHACreationActivity)
        progressDialog = ProgressDialog(this@ABHACreationActivity)
        edtAadharNumber = findViewById(R.id.edtAadharNumber)
        ilAadharNumber = findViewById(R.id.ilAadharNumber)
        edtAadharMobileNumber = findViewById(R.id.edtAadharMobileNumber)
        ilAadharMobileNumber = findViewById(R.id.ilAadharMobileNumber)



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

        otpView = findViewById(R.id.otpView)

        tvOTPMsg = findViewById(R.id.tvOTPMsg)
        tvMsg = findViewById(R.id.tvMsg)
        llOTP = findViewById(R.id.llOTP)
        svOTPMsg = findViewById(R.id.svOTPMsg)
        llTermsAndCondition = findViewById(R.id.llTermsAndCondition)

        llOTPTimer = findViewById(R.id.llOTPTimer)
        tvTimer = findViewById(R.id.tvTimer)
        tvOTPAttempts = findViewById(R.id.tvOTPAttempts)
        llOTP.visibility = View.GONE

        val anonymization1Consent =
            "I, <b>${sessionManager.userDetailsJson.name}</b>, confirm that I have duly informed and explained the beneficiary of the\n" +
                    "contents of consent for aforementioned purposes"
        cbAnonymization1Consent.setText(Html.fromHtml(anonymization1Consent))

        createABHASession()
    }

    fun setUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "ABHA address creation"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }


    private fun createABHASession() {

        progressDialog.setMessage("Creating session..")
        progressDialog.setCancelable(false)
        progressDialog.show();
        val createSession =
            ABHASessionModel(BuildConfig.ClientID, BuildConfig.SecretId, "client_credentials")
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
//                                Utilities.showMessageString(
//                                    "Got Token",
//                                    this@AadharVerificationActivity
//                                )
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            btnGetOTP.isEnabled = false
                            Utilities.showMessageString(
                                "Unable to create session",
                                this@ABHACreationActivity
                            )

                        }


                    } else {
                        btnGetOTP.isEnabled = false

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d(TAG, "createABHASession: ${t.message}")
                    btnGetOTP.isEnabled = false
                    Utilities.showMessageString(
                        "Unable to create session",
                        this@ABHACreationActivity
                    )


                }

            })

    }

    fun eventListener() {
//        cbAadhaarConsent.setOnCheckedChangeListener { buttonView, isChecked ->
//            btnGetOTP.isEnabled = isChecked
//        }

        edtAadharNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (edtAadharNumber.text?.isEmpty() == true) {
                    ilAadharNumber.error = "Please enter aadhaar number"
                    return

                } else {
                    ilAadharNumber.error = null
                }

                if (edtAadharNumber.text?.length != 12) {
                    ilAadharNumber.error = "Please enter 12 digit valid aadhaar number"
                    return

                } else {
                    ilAadharNumber.error = null
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
                    ilAadharMobileNumber.error = "Please enter mobile number linked with aadhaar"
                    return

                } else {
                    ilAadharMobileNumber.error = null
                }

                if (!Utilities.isValidMobileno(edtAadharMobileNumber.text.toString())) {
                    ilAadharMobileNumber.error =
                        "Please enter valid mobile number linked with aadhaar"
                    return

                } else {
                    ilAadharMobileNumber.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        btnGetOTP.setOnClickListener {

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
                    ilAadharMobileNumber.error = "Please enter mobile number linked with aadhaar"
                    return@setOnClickListener

                } else {
                    ilAadharMobileNumber.error = null
                }

                if (edtAadharMobileNumber.text.toString().isNotEmpty()) {
                    if (!Utilities.isValidMobileno(edtAadharMobileNumber.text.toString())) {
                        ilAadharMobileNumber.error =
                            "Please enter valid mobile number linked with aadhaar"
                        return@setOnClickListener

                    } else {
                        ilAadharMobileNumber.error = null
                    }
                }
            }


            if (!cbAadhaarConsent.isChecked || !cbAnonymizationConsent.isChecked || !cbAnonymization1Consent.isChecked || !cbAnonymization2Consent.isChecked || !cbHealthRecordSharingConsent.isChecked || !cbABHANumberLinkConsent.isChecked) {
                Utilities.showMessageString(
                    "Please accept terms and conditions to proceed!",
                    this@ABHACreationActivity
                )
                return@setOnClickListener
            }
            if (svOTPMsg.visibility == View.VISIBLE) {
                svOTPMsg.visibility = View.GONE
            }
            val aadhaarNumber = edtAadharNumber.text.toString()

            if (ilAadharNumber.visibility == View.VISIBLE) {


                publicKey?.let { it1 -> generateOTP(aadhaarNumber, it1) };
            } else {
//                generateMobileOTP(edtAadharMobileNumber.text.toString())
//                if (creationResponse != null) {
//                    if (creationResponse?.ABHAProfile?.mobile == null || creationResponse?.ABHAProfile?.mobile!!.isEmpty()) {
                checkAndGenerateMobileOTP()
//                    } else {
//                        creationResponse?.let { saveABHADetailsToOurServer(it) };
//                    }
//                }
            }
        }

        btnVerify.setOnClickListener {

//            creationResponse?.let { it1 -> saveABHADetailsToOurServer(it1) }

            if (edtAadharMobileNumber.text.toString().isEmpty() || !Utilities.isValidMobileno(
                    edtAadharMobileNumber.text.toString()
                )
            ) {
                ilAadharMobileNumber.error = "Please enter Mobile Number"
                Utilities.showMessageString("Please enter Mobile Number", this@ABHACreationActivity)
                return@setOnClickListener
            }
            val otp =
                otpView.text.toString()
            if (otp.length != 6) {
                Utilities.showMessageString("Please enter OTP", this@ABHACreationActivity)
                return@setOnClickListener
            }


            try {
//                val mobile = edtAadharMobileNumber.text.toString().toLong();
                if (ilAadharNumber.visibility == View.VISIBLE) {
//                    verifyAadhaarOTP(otp.toInt())
                    enrolByAadhaarOTP(otp.toInt())
                } else {
                    verifyMobileOTP()
                }
//                createHealthId(otp.toInt(), mobile);
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
                    ilAadharMobileNumber.error = "Please enter mobile number linked with aadhaar"
                    return@setOnClickListener

                } else {
                    ilAadharMobileNumber.error = null
                }

                if (edtAadharMobileNumber.text.toString().isNotEmpty()) {
                    if (!Utilities.isValidMobileno(edtAadharMobileNumber.text.toString())) {
                        ilAadharMobileNumber.error =
                            "Please enter valid mobile number linked with aadhaar"
                        return@setOnClickListener

                    } else {
                        ilAadharMobileNumber.error = null
                    }
                }
            }


            if (!cbAadhaarConsent.isChecked || !cbAnonymizationConsent.isChecked || !cbAnonymization1Consent.isChecked || !cbAnonymization2Consent.isChecked || !cbHealthRecordSharingConsent.isChecked || !cbABHANumberLinkConsent.isChecked) {
                Utilities.showMessageString(
                    "Please accept terms and conditions to proceed!",
                    this@ABHACreationActivity
                )
                return@setOnClickListener
            }
            if (svOTPMsg.visibility == View.VISIBLE) {
                svOTPMsg.visibility = View.GONE
            }
            val aadhaarNumber = edtAadharNumber.text.toString()

            if (ilAadharNumber.visibility == View.VISIBLE) {
                if (resendOTPCount == 3) {
                    Utilities.showAlertDialog(
                        this@ABHACreationActivity,
                        "Restricted",
                        "You have reached OTP resend limit",
                        false
                    )
                    return@setOnClickListener
                }
//                generateOTP(aadhaarNumber)
                publicKey?.let { it1 -> generateOTP(aadhaarNumber, it1) };
            } else {
                if (resendOTPCountForMobileLink == 3) {
                    Utilities.showAlertDialog(
                        this@ABHACreationActivity,
                        "Restricted",
                        "You have reached OTP resend limit",
                        false
                    )

                    return@setOnClickListener
                }
                checkAndGenerateMobileOTP()
            }
        }

    }

    private fun generateOTP(aadhaarNumber: String, publicKey: String) {
        progressDialog.setMessage("Generating OTP..")
        progressDialog.setCancelable(false)
        progressDialog.show();


        var encryptedString: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encryptedString = Base64.getEncoder()
                .encodeToString(RSAUtil.encrypt(aadhaarNumber, publicKey))
        }
        println(encryptedString)
//            val decryptedString = RSAUtil.decrypt(encryptedString, RSAUtil.privateKey)
//            println(decryptedString)

        val payload = SendOTPRequestModel(
            txnId = "",
            scope = arrayListOf("abha-enrol"),
            loginHint = "aadhaar",
            loginId = encryptedString,
            otpSystem = "aadhaar"
        )

        Log.i(TAG, "generateOTP: ${Gson().toJson(payload)}")
//https://abhasbx.abdm.gov.in/abha/api/v3/enrollment/request/otp
        HealthCheckup.ABDMClient.generateOtp(
            "Bearer ${accessToken}", Utilities.getCurrentTimeStamp(),
            UUID.randomUUID().toString(), payload
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            Log.d(TAG, "genOTP: ${response.body()}")
                            val jsonObject = JSONObject(response.body()!!.string())
                            txnId = jsonObject.getString("txnId");
                            if (txnId != null) {
                                val message = jsonObject.getString("message");
                                Utilities.showMessageString(
                                    "OTP sent on registered mobile number",
                                    this@ABHACreationActivity
                                )

                                resendOTPCount += 1;
                                tvOTPAttempts.setText((resendOTPCount - 1).toString() + "/2")

                                cdt = object : CountDownTimer(60000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        isTimerFinished = false
                                        tvTimer.setText("${millisUntilFinished / 1000} Sec")
                                        btnResend.isEnabled = isTimerFinished
                                    }

                                    override fun onFinish() {
//                                        tvTimer.setText("00:00");
                                        isTimerFinished = true
                                        btnResend.isEnabled = isTimerFinished

                                    }
                                }.start()
                                ilAadharMobileNumber.visibility = View.VISIBLE
                                cbAadhaarConsent.isEnabled = false
                                cbABHANumberConsent.isEnabled = false
                                cbAnonymizationConsent.isEnabled = false
                                cbAnonymization1Consent.isEnabled = false
                                cbAnonymization2Consent.isEnabled = false
                                cbHealthRecordSharingConsent.isEnabled = false
                                cbABHANumberLinkConsent.isEnabled = false
                                svOTPMsg.visibility = View.VISIBLE

                                tvOTPMsg.text = message
                                edtAadharNumber.isEnabled = false

                                llOTPTimer.visibility = View.VISIBLE

                                llOTP.visibility = View.VISIBLE
                                btnGetOTP.visibility = View.GONE
                                btnResend.visibility = View.VISIBLE
                                btnVerify.visibility = View.VISIBLE
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    } else {
//                        val errBody=response.body()?.string();
//                        val errObject=JSONObject(errBody)
//
//                        if(errObject.has("error")){
//                            val codeObject=
//                        }
                        Utilities.showAlertDialog(
                            this@ABHACreationActivity,
                            "Unable to send OTP",
                            "Aadhaar number is incorrect, Resident shall use correct Aadhaar",
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
                            this@ABHACreationActivity,
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

    private fun enrolByAadhaarOTP(otp: Int) {
        progressDialog.setMessage("Verifying Aadhaar OTP..")
        progressDialog.setCancelable(false)
        progressDialog.show()

        var encryptedString: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encryptedString = Base64.getEncoder()
                .encodeToString(RSAUtil.encrypt(otp.toString(), publicKey))
        }

        var encryptedMobile: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encryptedMobile = Base64.getEncoder()
                .encodeToString(RSAUtil.encrypt(edtAadharMobileNumber.text.toString(), publicKey))
        }
        val authData =
            AuthData(
                authMethods = arrayListOf("otp"),
                otp = Otp(
                    txnId = txnId,
                    otpValue = encryptedString,
                    mobile = edtAadharMobileNumber.text.toString(),
                    timeStamp = Utilities.getCurrentTimeStamp()
                )

            )
        val payload = EnrolByAadhaarRequestModel(
            authData = authData,
            consent = Consent(code = "abha-enrollment", version = "1.4")
        )

        Log.i(TAG, "enrolByAadhaarOTP: ${Gson().toJson(payload)}")

        HealthCheckup.ABDMClient.enrollByAAdhaar(
            "Bearer $accessToken", Utilities.getCurrentTimeStamp(),
            UUID.randomUUID().toString(), payload
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
                            txnId = jsonObject.getString("txnId");
                            val tokenObj = jsonObject.getJSONObject("tokens");
                            authToken = tokenObj.getString("token")

                            if (txnId != null) {
                                creationResponse = Gson().fromJson<ABHACreationResponseModel>(
                                    res,
                                    ABHACreationResponseModel::class.java
                                )


                                cdt.cancel()

                                if (creationResponse != null) {
                                    if (creationResponse?.ABHAProfile?.mobile == null || creationResponse?.ABHAProfile?.mobile!!.isEmpty() || (creationResponse?.ABHAProfile?.mobile != null && !creationResponse?.ABHAProfile?.mobile.equals(
                                            edtAadharMobileNumber.text.toString(),
                                            true
                                        ))
                                    ) {
                                        ilAadharMobileNumber.visibility = View.VISIBLE
                                        ilAadharNumber.visibility = View.GONE
                                        llTermsAndCondition.visibility = View.GONE
                                        llOTP.visibility = View.GONE
                                        llOTPTimer.visibility = View.GONE
                                        btnGetOTP.visibility = View.VISIBLE
                                        btnResend.visibility = View.GONE
                                        btnVerify.visibility = View.GONE
                                        svOTPMsg.visibility = View.GONE
                                        tvMsg.text =
                                            "Entered mobile no is not linked with aadhaar, \nPlease enter otp to verify"
                                        otpView.text?.clear()
                                        checkAndGenerateMobileOTP()

                                    } else {
                                        creationResponse?.let { saveABHADetailsToOurServer(it) };
//                                        getAccountProfile()
                                    }
                                }

                            } else {
                                Utilities.showAlertDialog(
                                    this@ABHACreationActivity,
                                    "Unable to verify OTP",
                                    "",
                                    false
                                )
                            }
                        } catch (e: java.lang.Exception) {

                            e.printStackTrace()
                            Utilities.showAlertDialog(
                                this@ABHACreationActivity,
                                "Unable to verify OTP",
                                e.message,
                                false
                            )
                        }
                    } else {
                        try {
                            val errRes = response?.errorBody()?.string()
                            if (errRes != null) {
                                if (errRes.contains("ABDM-1204")) {
                                    Utilities.showAlertDialog(
                                        this@ABHACreationActivity,
                                        "Unable to verify OTP",
                                        "OTP validation failed",
                                        false
                                    )
                                } else {
                                    Utilities.showAlertDialog(
                                        this@ABHACreationActivity,
                                        "Unable to verify OTP",
                                        errRes,
                                        false
                                    )
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Utilities.showAlertDialog(
                                this@ABHACreationActivity,
                                "Unable to verify OTP",
                                e.message,
                                false
                            )
                        }

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    progressDialog.dismiss()
                    Log.d(TAG, "onResponse: ${t.message}")
                    Utilities.showAlertDialog(
                        this@ABHACreationActivity,
                        "Unable to verify OTP",
                        t.message,
                        false
                    )
                }

            })

    }

    private fun checkAndGenerateMobileOTP() {
        progressDialog.setMessage("Checking And Generating Mobile OTP..")
        progressDialog.setCancelable(false)
        progressDialog.show();
        isAadhaarLinked = false
        whichOTP = 1

        var encryptedMobile: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encryptedMobile = Base64.getEncoder()
                .encodeToString(RSAUtil.encrypt(edtAadharMobileNumber.text.toString(), publicKey))
        }

        val payload = SendOTPRequestModel(
            txnId = txnId,
            scope = arrayListOf("abha-enrol", "mobile-verify"),
            loginHint = "mobile",
            loginId = encryptedMobile,
            otpSystem = "abdm"
        )
        HealthCheckup.ABDMClient.sendMobileOTP(
            "Bearer ${accessToken}",
            Utilities.getCurrentTimeStamp(),
            UUID.randomUUID().toString(),
            payload
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            Log.d(TAG, "checkAndGenerateMobileOTP: ${response.body()}")
                            val jsonObject = JSONObject(response.body()!!.string())
                            txnId = jsonObject.getString("txnId");
                            if (txnId != null) {
                                val message = jsonObject.getString("message");

                                Utilities.showMessageString(
                                    "OTP sent on entered mobile number",
                                    this@ABHACreationActivity
                                )
                                resetOTPUI()
                                tvOTPMsg.text = message

                                resendOTPCountForMobileLink += 1;
                                llOTPTimer.visibility = View.VISIBLE
                                tvOTPAttempts.setText((resendOTPCountForMobileLink - 1).toString() + "/2")
                                cdt = object : CountDownTimer(60000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        isTimerFinished = false
                                        tvTimer.setText("${millisUntilFinished / 1000} Sec")
                                        btnResend.isEnabled = isTimerFinished
                                    }

                                    override fun onFinish() {
                                        isTimerFinished = true
                                        btnResend.isEnabled = isTimerFinished

                                    }
                                }.start()

                                llOTP.visibility = View.VISIBLE
                                btnGetOTP.visibility = View.GONE
                                btnResend.visibility = View.VISIBLE
                                btnVerify.visibility = View.VISIBLE

                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            Utilities.showAlertDialog(
                                this@ABHACreationActivity,
                                "Unable to send OTP",
                                e.message,
                                false
                            )
                        }
                    } else {
                        val errRes = response.errorBody()?.string()
                        Utilities.showAlertDialog(
                            this@ABHACreationActivity,
                            "Unable to send OTP",
                            errRes,
                            false
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d(TAG, "onResponse: ${t.message}")

                    Utilities.showAlertDialog(
                        this@ABHACreationActivity,
                        "Unable to send OTP",
                        t.message,
                        false
                    )
                }

            })

    }

    fun resetOTPUI() {
        otpView.text?.clear()

        svOTPMsg.visibility = View.VISIBLE
        tvTimer.setText("00:00")

        llOTPTimer.visibility = View.VISIBLE

    }

    private fun verifyMobileOTP() {
        progressDialog.setMessage("Verifying Mobile OTP..")
        progressDialog.setCancelable(false)
        progressDialog.show();
        val otp =
            otpView.text.toString()

//        val payload = JsonObject()
//        payload.addProperty("otp", otp)
//        payload.addProperty("txnId", txnId)
        val crrTimeStamp = Utilities.getCurrentTimeStamp();
        val requestId = UUID.randomUUID().toString()
        var encryptedOTP: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            encryptedOTP = Base64.getEncoder()
                .encodeToString(RSAUtil.encrypt(otp, publicKey))
        }

        val payload = VerifyMobileOTPRequestModel(
            scope = arrayListOf(
                "abha-enrol",
                "mobile-verify"
            ),
            authData = com.myhindlab.abkat.abha.models.verify_mobile_otp.AuthData(
                authMethods = arrayListOf("otp"),
                otp = com.myhindlab.abkat.abha.models.verify_mobile_otp.Otp(
                    timeStamp = crrTimeStamp,
                    txnId = txnId,
                    otpValue = encryptedOTP
                )
            )
        )
        HealthCheckup.ABDMClient.verifyMobileOTPABDM(
            "Bearer $accessToken",
            crrTimeStamp,
            requestId,
            payload
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            Log.d(TAG, "verifyMobileOTP: ${response.body()}")
                            val res = response.body()!!.string();
                            val jsonObject = JSONObject(res)
                            txnId = jsonObject.getString("txnId");
                            val authResult = jsonObject.getString("authResult");
                            if (txnId != null && authResult.equals("success", ignoreCase = true)) {

                                creationResponse?.let { saveABHADetailsToOurServer(it) };

//                                startActivity(
//                                    Intent(
//                                        this@ABHACreationActivity,
//                                        ABHAAddressCreationActivity::class.java
//                                    ).putExtra("accessToken", accessToken)
//                                        .putExtra("txnId", txnId)
//                                        .putExtra("authToken", authToken)
//                                        .putExtra("healthCard", creationResponse)
//                                        .putExtra(
//                                            "mobile",
//                                            edtAadharMobileNumber.text.toString()
//                                        )
//                                )
//
//                                finish()

                            } else {
                                val msg = jsonObject.getString("message");

                                Utilities.showAlertDialog(
                                    this@ABHACreationActivity,
                                    "Unable to verify OTP",
                                    msg,
                                    false
                                )
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            Utilities.showAlertDialog(
                                this@ABHACreationActivity,
                                "Unable to verify OTP",
                                e.message,
                                false
                            )

                        }
                    } else {
                        Utilities.showAlertDialog(
                            this@ABHACreationActivity,
                            "Unable to verify OTP",
                            response.errorBody()?.string(),
                            false
                        )


                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d(TAG, "onResponse: ${t.message}")
                    Utilities.showAlertDialog(
                        this@ABHACreationActivity,
                        "Unable to verify OTP",
                        t.message,
                        false
                    )
                }

            })

    }


    fun saveABHADetailsToOurServer(
        accountProfileRequestModel: ABHACreationResponseModel,

        ) {
        accountProfileRequestModel.createdBy =
            sessionManager.userDetailsJson.empCode
        val campId = intent.getStringExtra("campId");
        if (campId == null) {
            accountProfileRequestModel.campId = 0
        } else {
            accountProfileRequestModel.campId = campId.toInt();
        }

        progressDialog.setMessage("Storing Profile Data...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val payload = Gson().toJson(accountProfileRequestModel);
        Log.i(TAG, "saveABHADetailsToOurServer: " + payload)
        HealthCheckup.getD2DClient.insertAbhaRegistration(payload)
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
                                val status = js.getString("status");
                                if (status.equals("success", ignoreCase = true)) {
                                    LocalBroadcastManager.getInstance(this@ABHACreationActivity)
                                        .sendBroadcast(
                                            Intent("set_abha_data").putExtra(
                                                "healthCard",
                                                accountProfileRequestModel
                                            )
                                                .putExtra(
                                                    "mobile",
                                                    intent.getLongExtra("mobile", 0)
                                                )
                                        )
//                                    if(accountProfileRequestModel.isNew == true){
//                                        Utilities.showAlertDialog(this@ABHACreationActivity,"ABHA already exists","Do you want to continue creation? If yes then click on continue else click on use existing details.",true,"Continue",
//                                            object : DialogInterface.OnClickListener {
//                                                override fun onClick(
//                                                    p0: DialogInterface?,
//                                                    p1: Int
//                                                ) {
//
//                                                }
//                                            },"Use existing",
//                                            object : DialogInterface.OnClickListener {
//                                                override fun onClick(
//                                                    p0: DialogInterface?,
//                                                    p1: Int
//                                                ) {
//
//                                                }
//
//                                            })
//                                    }
                                    startActivity(
                                        Intent(
                                            this@ABHACreationActivity,
                                            ABHAAddressCreationActivity::class.java
                                        ).putExtra("accessToken", accessToken)
                                            .putExtra("txnId", txnId)
                                            .putExtra("authToken", authToken)
                                            .putExtra("healthCard", creationResponse)
                                            .putExtra("isNew", accountProfileRequestModel.isNew)
                                            .putExtra(
                                                "existingABHAAddress",
                                                accountProfileRequestModel.ABHAProfile!!.phrAddress[0]
                                            )
                                            .putExtra(
                                                "mobile",
                                                edtAadharMobileNumber.text.toString()
                                            ).putExtra(
                                                "campId",
                                                getIntent().getStringExtra("campId")
                                            )
                                            .putExtra(
                                                "district",
                                                getIntent().getStringExtra("district")
                                            )
                                            .putExtra(
                                                "distlgdcode",
                                                getIntent().getStringExtra("distlgdcode")
                                            )
                                            .putExtra(
                                                "siteId",
                                                getIntent().getStringExtra("siteId")
                                            )
                                            .putExtra(
                                                "Latitude",
                                                getIntent().getStringExtra("Latitude")
                                            )
                                            .putExtra(
                                                "Longitude",
                                                getIntent().getStringExtra("Longitude")
                                            )
                                            .putExtra(
                                                "campType",
                                                getIntent().getStringExtra("campType")
                                            )
                                    )

                                    finish()


                                } else {
                                    Log.e(TAG, "onResponse: ${stringResp}")

                                    Utilities.showAlertDialogMandatory(
                                        this@ABHACreationActivity,
                                        "Error",
                                        stringResp,
                                        false,
                                        "Re-Try",
                                        object : DialogInterface.OnClickListener {
                                            override fun onClick(
                                                dialog: DialogInterface?,
                                                which: Int
                                            ) {
                                                saveABHADetailsToOurServer(
                                                    accountProfileRequestModel
                                                )
                                            }

                                        })

                                }

                            } else {
                                Log.e(TAG, "onResponse: ${response.body()?.string()}")
                                Utilities.showAlertDialogMandatory(
                                    this@ABHACreationActivity,
                                    "Unable to save ABHA details",
                                    "Unable to save ABHA details",
                                    false,
                                    "Re-Try",
                                    object : DialogInterface.OnClickListener {
                                        override fun onClick(
                                            dialog: DialogInterface?,
                                            which: Int
                                        ) {
                                            saveABHADetailsToOurServer(
                                                accountProfileRequestModel
                                            )
                                        }

                                    })
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            Utilities.showAlertDialogMandatory(
                                this@ABHACreationActivity,
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
                                            accountProfileRequestModel
                                        )
                                    }

                                })
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        progressDialog.dismiss()
                        Log.e(TAG, "onFailure: ", t)
                        Utilities.showAlertDialogMandatory(
                            this@ABHACreationActivity,
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
                                        accountProfileRequestModel
                                    )
                                }

                            })

                    }

                })

    }


    private fun getAccountProfile() {
        progressDialog.setMessage("Getting Profile...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val timeStamp = Utilities.getCurrentTimeStamp();
        val requestId = UUID.randomUUID().toString()
        HealthCheckup.ABDMClient.accountProfile(
            "Bearer $accessToken",
            timeStamp,
            requestId,
            "Bearer $authToken"
        )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        val res = response.body()!!.string()
                        Log.i(TAG, "accountProfile onResponse: $res")

                        val accountProfileRequestModel: AccountProfileResponseModel =
                            Gson().fromJson(
                                res,
                                AccountProfileResponseModel::class.java
                            )

//                        saveABHADetailsToOurServer(accountProfileRequestModel)
//                        LocalBroadcastManager.getInstance(this@ABHACreationActivity)
//                            .sendBroadcast(
//                                Intent("set_abha_data").putExtra(
//                                    "healthCard",
//                                    accountProfileRequestModel
//                                )
//                                    .putExtra(
//                                        "mobile",
//                                        intent.getLongExtra("mobile", 0)
//                                    )
//                            )
//                        finish()
                    } else {
                        Utilities.showAlertDialog(
                            this@ABHACreationActivity,
                            "Error",
                            response.message()?.let { it -> it },
                            false
                        )
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Utilities.showAlertDialog(this@ABHACreationActivity, "Error", t.message, false)
                }
            })
    }


}