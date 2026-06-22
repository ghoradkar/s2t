package com.myhindlab.abkat.abha.activities

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.myhindlab.abkat.BuildConfig
import com.myhindlab.abkat.HealthCheckup
import com.myhindlab.abkat.R
import com.myhindlab.abkat.abha.models.ABHASessionModel
import com.myhindlab.abkat.abha.models.confirm_auth.Demographic
import com.myhindlab.abkat.abha.models.fetch_auth_modes.FetchAuthRequestModel
import com.myhindlab.abkat.abha.models.init_auth.InitAuthRequestModel
import com.myhindlab.abkat.abha.models.init_auth.Query
import com.myhindlab.abkat.abha.models.init_auth.Requester
import com.myhindlab.abkat.abha.models.sms_notify.Hip
import com.myhindlab.abkat.utilities.ApplicationConstants
import com.myhindlab.abkat.utilities.ConstantData
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import java.util.UUID

class CareContextLinkingActivity : AppCompatActivity() {
    lateinit var progressDialog: ProgressDialog
    lateinit var accessToken: String
    val TAG: String = CareContextLinkingActivity::class.java.simpleName
    private var constantData: ConstantData? = null
    lateinit var edtFullName: TextInputEditText;
    lateinit var edtABHANumber: TextInputEditText;
    lateinit var edtABHAAddress: TextInputEditText;
    lateinit var edtEmail: TextInputEditText;
    lateinit var edtMobile: TextInputEditText;
    lateinit var edtDOB: TextInputEditText;
    lateinit var edtGender: TextInputEditText;
    lateinit var edtOTP1: TextInputEditText;
    lateinit var edtOTP2: TextInputEditText;
    lateinit var edtOTP3: TextInputEditText;
    lateinit var edtOTP4: TextInputEditText;
    lateinit var edtOTP5: TextInputEditText;
    lateinit var edtOTP6: TextInputEditText;
    lateinit var llOTP: LinearLayoutCompat;
    lateinit var llOTPTimer: LinearLayoutCompat;
    lateinit var llAuthModes: LinearLayoutCompat;
    lateinit var llPatientInfo: LinearLayoutCompat;
    lateinit var btnGetOTP: Button;
    lateinit var btnVerify: Button;
    lateinit var btnResend: Button;
    lateinit var sessionManager: UserSessionManager
    private var authTransId: String? = null
    private var authToken: String? = null
    lateinit var tvTimer: TextView
    private var isTimerFinished: Boolean = false
    lateinit var OTPView: ConstraintLayout
    lateinit var authMode: String
    lateinit var rbMobileOTP: RadioButton
    lateinit var rbAadhaarOTP: RadioButton
    lateinit var rbDemographic: RadioButton
    lateinit var rgAuthMode: RadioGroup
    lateinit var btnSendOTP: Button
    private var authModesList: ArrayList<String>? = null
    lateinit var imvRefresh: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_care_context_linking)
        setUpToolbar()
        init()
        eventListener()
        setDefault()
    }

    private fun setUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false);
        supportActionBar?.title = "Care Context Linking"
        toolbar.title = "Care Context Linking"
        imvRefresh = toolbar.findViewById(R.id.imvRefresh)
//        imvRefresh.setOnClickListener {
//            if (constantData?.abhaAddress == null || constantData?.abhaAddress!!.isEmpty()) {
//                createABHASessionForSMSNotify(null);
//                llPatientInfo.visibility = View.GONE
//            } else {
//                llPatientInfo.visibility = View.VISIBLE
//                createABHASession(null)
//            }
//        }


//        toolbar.setNavigationOnClickListener {
//            finish()
//        }
    }


    fun init() {
        progressDialog = ProgressDialog(this@CareContextLinkingActivity)
        constantData = ConstantData.getInstance()
        sessionManager = UserSessionManager(this@CareContextLinkingActivity)

        title = "Care Context Linking"

//        btnGetOTP = findViewById(R.id.btnGetOTP)
        btnVerify = findViewById(R.id.btnVerify)
        btnResend = findViewById(R.id.btnResend)
        btnSendOTP = findViewById(R.id.btnSendOTP)
        authModesList = ArrayList()
        edtOTP1 = findViewById(R.id.edtOTP1)
        edtOTP2 = findViewById(R.id.edtOTP2)
        edtOTP3 = findViewById(R.id.edtOTP3)
        edtOTP4 = findViewById(R.id.edtOTP4)
        edtOTP5 = findViewById(R.id.edtOTP5)
        edtOTP6 = findViewById(R.id.edtOTP6)
        edtMobile = findViewById(R.id.edtMobile)
        edtEmail = findViewById(R.id.edtEmail)
        edtFullName = findViewById(R.id.edtFullName)
        edtABHANumber = findViewById(R.id.edtABHANumber)
        edtABHAAddress = findViewById(R.id.edtABHAAddress)
        edtDOB = findViewById(R.id.edtDOB)
        edtGender = findViewById(R.id.edtGender)
        rbAadhaarOTP = findViewById(R.id.rbAadhaarOTP)
        rbMobileOTP = findViewById(R.id.rbMobileOTP)
        rbDemographic = findViewById(R.id.rbDemographic)
        llAuthModes = findViewById(R.id.llAuthModes)
        rgAuthMode = findViewById(R.id.rgAuthModes)
        OTPView = findViewById(R.id.OTPView)
        llOTP = findViewById(R.id.llOTP)
        llOTPTimer = findViewById(R.id.llOTPTimer)
        tvTimer = findViewById(R.id.tvTimer)
        llPatientInfo = findViewById(R.id.llPatientInfo)
    }

    fun setDefault() {

//        if (constantData?.abhaAddress == null || constantData?.abhaAddress!!.isEmpty()) {
//            createABHASessionForSMSNotify(null);
//            llPatientInfo.visibility = View.GONE
//        } else {
//            llPatientInfo.visibility = View.VISIBLE
//            createABHASession(null)
//
//
//        }
//        edtABHANumber.setText(constantData?.abhaNumber);
//        edtABHAAddress.setText(constantData?.abhaAddress);
//        edtFullName.setText("${constantData?.fname} ${constantData?.mname} ${constantData?.lname}")
//        edtMobile.setText(constantData?.mobno)
//        edtDOB.setText(constantData?.dob)
//
//        var gender = if (constantData?.selectedgenderId.equals("M", ignoreCase = true)) {
//            "Male";
//        } else if (constantData?.selectedgenderId.equals("O", true)) {
//            "Other"
//        } else {
//            "Female"
//        }
//
//        edtGender.setText(gender)


    }

    fun eventListener() {


//        rgAuthMode.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
//            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
//                when (checkedId) {
//                    R.id.rbAadhaarOTP -> {
//                        authMode = "AADHAAR_OTP"
//                        btnSendOTP.setText("Send OTP")
//                    }
//
//                    R.id.rbMobileOTP -> {
//                        authMode = "MOBILE_OTP"
//                        btnSendOTP.setText("Send OTP")
//                    }
//
//                    R.id.rbDemographic -> {
//                        authMode = "DEMOGRAPHICS"
//                        btnSendOTP.setText("Validate")
//                    }
//
//                }
//            }
//
//        })


        rgAuthMode.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
                when (checkedId) {
                    R.id.rbAadhaarOTP -> {
                        authMode = "AADHAAR_OTP"
                        btnSendOTP.text = "Send OTP"
                    }

                    R.id.rbMobileOTP -> {
                        authMode = "MOBILE_OTP"
                        btnSendOTP.text = "Send OTP"
                    }

                    R.id.rbDemographic -> {
                        authMode = "DEMOGRAPHICS"
                        btnSendOTP.text = "Validate"
                    }
                }
            }
        })


        btnSendOTP.setOnClickListener {
            if (accessToken == null) {
                createABHASession(btnSendOTP)
            } else {
                if (authMode != null) {
                    initAuth()
                } else {
                    Utilities.showMessageString(
                        "Please select auth mode first",
                        this@CareContextLinkingActivity
                    )
                }
            }
        }

        btnResend.setOnClickListener {
//            createABHASession(null)
            initAuth()
        }
        edtOTP1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) {
                    edtOTP2.requestFocus();
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        edtOTP2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) {
                    edtOTP3.requestFocus();
                } else if (count == 0) {
                    edtOTP1.requestFocus(View.FOCUS_UP);

                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        edtOTP3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) {
                    edtOTP4.requestFocus();
                } else if (count == 0) {
                    edtOTP2.requestFocus(View.FOCUS_UP);

                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        edtOTP4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) {
                    edtOTP5.requestFocus();
                } else if (count == 0) {
                    edtOTP3.requestFocus(View.FOCUS_UP);

                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        edtOTP5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) {
                    edtOTP6.requestFocus();
                } else if (count == 0) {
                    edtOTP4.requestFocus(View.FOCUS_UP);

                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        edtOTP6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1) {
                    edtOTP6.clearFocus()
                } else if (count == 0) {
                    edtOTP5.requestFocus(View.FOCUS_UP);
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })


        btnVerify.setOnClickListener {
            if (authTransId == null) {
                Utilities.showMessageString("Transaction ID null", this@CareContextLinkingActivity)
                return@setOnClickListener
            }

            val otp =
                edtOTP1.text.toString() + edtOTP2.text.toString() + edtOTP3.text.toString() + edtOTP4.text.toString() + edtOTP5.text.toString() + edtOTP6.text.toString()
            if (otp.length != 6) {
                Utilities.showMessageString("Please enter OTP", this@CareContextLinkingActivity)
                return@setOnClickListener
            }


            confirmAuth()

        }

    }

//    fun validateABHANo(healthId: String?) {
//        authModes.clear()
//        selectedAuthMode = ""
//        progressDialog.setMessage("Validating ABHA Number")
//        progressDialog.setCancelable(false)
//        progressDialog.show()
//        val sessionModel = ABHASessionModel(BuildConfig.ClientID, BuildConfig.SecretId)
//        HLL_Connect.abhaClient.createAbhaSession(sessionModel)
//            .enqueue(object : Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//                    if (response.isSuccessful) {
//                        try {
//                            val jsonObject = JSONObject(response.body()!!.string())
//                            accessToken = jsonObject.getString("accessToken")
//                            accessToken = "Bearer $accessToken"
//                            if (!accessToken.isEmpty()) {
//                                val searchHealthIDRequestModel = SearchHealthIDRequestModel(
//                                    healthId!!
//                                )
//                                Log.d(
//                                    TAG,
//                                    "searchABHA req: " + Gson().toJson(searchHealthIDRequestModel)
//                                )
//                                HLL_Connect.ABDMClient.searchABHA(
//                                    accessToken,
//                                    searchHealthIDRequestModel
//                                ).enqueue(object : Callback<ResponseBody> {
//                                    override fun onResponse(
//                                        call: Call<ResponseBody>,
//                                        response: Response<ResponseBody>
//                                    ) {
//                                        progressDialog.dismiss()
//                                        try {
//                                            if (response.isSuccessful) {
//                                                val res = response.body()!!.string()
//                                                val (healthId1, healthIdNumber, name, status, authMethods) = Gson().fromJson(
//                                                    res,
//                                                    SearchHealthIdResponseModel::class.java
//                                                )
//                                                if (status != null && status.equals(
//                                                        "ACTIVE",
//                                                        ignoreCase = true
//                                                    )
//                                                ) {
//                                                    llAuthModes.setVisibility(View.VISIBLE)
//                                                    btnSendOTP.setVisibility(View.VISIBLE)
//                                                    btnValidateABHA.setVisibility(View.GONE)
//                                                    edtABHANumber.setEnabled(false)
//                                                    if (healthId1 != null && !healthId1.isEmpty()) {
//                                                        edtABHAAddress.setText(healthId1)
//                                                        edtABHAAddress.isEnabled = false
//                                                    }
//                                                    if (healthIdNumber != null && !healthIdNumber.isEmpty()) {
//                                                        edtABHANumber.setText(
//                                                            healthIdNumber.replace(
//                                                                "-",
//                                                                ""
//                                                            )
//                                                        )
//                                                        edtABHANumber.setEnabled(false)
//                                                    }
//
//
////                                                isABHANoValid = true;
////                                                Utilities.showAlertDialog(context, "ABHA number is valid", "Enter ABHA number is valid", true);
//                                                    if (name != null && !name.isEmpty()) {
//                                                        val nameArr = name.split(" ".toRegex())
//                                                            .dropLastWhile { it.isEmpty() }
//                                                            .toTypedArray()
//                                                        if (nameArr.size == 3) {
//                                                            edt_fname.setText(nameArr[0])
//                                                            edt_mname.setText(nameArr[1])
//                                                            edt_lname.setText(nameArr[2])
//                                                        } else if (nameArr.size == 2) {
//                                                            edt_fname.setText(nameArr[0])
//                                                            edt_lname.setText(nameArr[1])
//                                                        }
//                                                    }
//                                                    for (s in authMethods) {
//                                                        if (s.contains("OTP")) {
//                                                            authModes.add(s)
//                                                        }
//                                                    }
//                                                    rvAuthModes.setAdapter(AuthModesListAdapter(
//                                                        authModes
//                                                    ) { mode: String? ->
//                                                        this@PatientRegistrationActivity.onModeSelect(
//                                                            mode
//                                                        )
//                                                    })
//
////                                                btn_register.setEnabled(true);
//                                                } else {
//                                                    llAuthModes.setVisibility(View.GONE)
//                                                    btnSendOTP.setVisibility(View.GONE)
//                                                    btnValidateABHA.setVisibility(View.VISIBLE)
//                                                    edtABHANumber.setEnabled(true)
//                                                    edtABHAAddress.isEnabled = true
//                                                    isABHANoValid = false
//                                                    Utilities.showAlertDialog(
//                                                        this@CareContextLinkingActivity,
//
//                                                        "ABHA number is invalid",
//                                                        "Enter ABHA number is invalid",
//                                                        false
//                                                    )
//                                                }
//                                            } else {
//                                                llAuthModes.setVisibility(View.GONE)
//                                                btnSendOTP.setVisibility(View.GONE)
//                                                btnValidateABHA.setVisibility(View.VISIBLE)
//                                                edtABHANumber.setEnabled(true)
//                                                edtABHAAddress.isEnabled = true
//
//
////                                            btn_register.setEnabled(false);
//                                                isABHANoValid = false
//                                                val errRes = response.errorBody()!!.string()
//                                                val (_, message) = Gson().fromJson(
//                                                    errRes,
//                                                    CreateHealthIdErrResponseModel::class.java
//                                                )
//                                                Utilities.showAlertDialog(
//                                                    this@CareContextLinkingActivity,
//                                                    "ABHA number is invalid",
//                                                    message, false
//                                                )
//                                            }
//                                        } catch (e: java.lang.Exception) {
//                                            e.printStackTrace()
//                                            //                                        btn_register.setEnabled(false);
//                                            btnSendOTP.setVisibility(View.GONE)
//                                            llAuthModes.setVisibility(View.GONE)
//                                            btnValidateABHA.setVisibility(View.VISIBLE)
//                                            edtABHANumber.setEnabled(true)
//                                            edtABHAAddress.isEnabled = true
//                                            isABHANoValid = false
//                                        }
//                                    }
//
//                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
////                                    btn_register.setEnabled(false);
//                                        isABHANoValid = false
//                                        llAuthModes.setVisibility(View.GONE)
//                                        btnSendOTP.setVisibility(View.GONE)
//                                        btnValidateABHA.setVisibility(View.VISIBLE)
//                                        edtABHANumber.setEnabled(true)
//                                        edtABHAAddress.isEnabled = true
//                                        progressDialog.dismiss()
//                                        Utilities.showAlertDialog(
//                                            this@CareContextLinkingActivity,
//                                            "Unable to check ID status",
//                                            t.message,
//                                            false
//                                        )
//                                    }
//                                })
//                            }
//                        } catch (e: java.lang.Exception) {
//                            e.printStackTrace()
//                            //                        btn_register.setEnabled(false);
//                            isABHANoValid = false
//                            llAuthModes.setVisibility(View.GONE)
//                            btnSendOTP.setVisibility(View.GONE)
//                            btnValidateABHA.setVisibility(View.VISIBLE)
//                            edtABHANumber.setEnabled(true)
//                            edtABHAAddress.isEnabled = true
//                        }
//                    } else {
////                    btn_register.setEnabled(false);
//                        isABHANoValid = false
//                        llAuthModes.setVisibility(View.GONE)
//                        btnSendOTP.setVisibility(View.GONE)
//                        btnValidateABHA.setVisibility(View.VISIBLE)
//                        edtABHANumber.setEnabled(true)
//                        edtABHAAddress.isEnabled = true
//                        progressDialog.dismiss()
//                        Utilities.showAlertDialog(
//                            this@CareContextLinkingActivity,
//                            "Unable to create ABHA session",
//                            "something went wrong",
//                            false
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    progressDialog.dismiss()
//                    //                btn_register.setEnabled(false);
//                    isABHANoValid = false
//                    llAuthModes.setVisibility(View.GONE)
//                    btnSendOTP.setVisibility(View.GONE)
//                    btnValidateABHA.setVisibility(View.VISIBLE)
//                    edtABHANumber.setEnabled(true)
//                    edtABHAAddress.isEnabled = true
//                    Utilities.showAlertDialog(
//                        this@CareContextLinkingActivity,
//                        "Unable to create ABHA session",
//                        t.message,
//                        false
//                    )
//                }
//            })
//    }


    private fun createABHASessionForSMSNotify(v: View?) {

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
                            val json = response.body()?.string()?.let { JSONObject(it) }
                            if (json != null) {
                                accessToken = json.getString("accessToken")
                                smsNotify()


                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            Utilities.showMessageString(
                                "Unable to create session",
                                this@CareContextLinkingActivity
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
                        this@CareContextLinkingActivity
                    )


                }

            })

    }

    private fun createABHASession(v: View?) {
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
                            val json = response.body()?.string()?.let { JSONObject(it) }
                            if (json != null) {
                                accessToken = json.getString("accessToken")
                                fetchAuthModes()

                                if (v != null) {
                                    if (v.id == R.id.btnSendOTP || v.id == R.id.btnResend) {
                                        initAuth()
                                    }
                                }


                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            Utilities.showAlertDialogMandatory(
                                this@CareContextLinkingActivity,
                                "Error",
                                "Unable to create session", false,
                                "Retry",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        createABHASession(null)
                                    }

                                }


                            )

                        }


                    } else {
                        Utilities.showAlertDialogMandatory(
                            this@CareContextLinkingActivity,
                            "Error",
                            "Unable to create session",
                            false,
                            "Retry",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    createABHASession(null)
                                }

                            }


                        )

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d(TAG, "createABHASession: ${t.message}")
                    Utilities.showAlertDialogMandatory(
                        this@CareContextLinkingActivity,
                        "Error",
                        "Unable to create session",
                        false,
                        "Retry",
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                createABHASession(null)
                            }

                        }


                    )


                }

            })

    }


    private fun initAuth() {
        progressDialog.setMessage("Init Auth..")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1 // Month is 0-based, so add 1

        val day = calendar[Calendar.DAY_OF_MONTH]
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]


        val utcTime =
            String.format(
                "%04d-%02d-%02dT%02d:%02d:%02d.342Z",
                year,
                month,
                day,
                hour,
                minute,
                second
            )
        var query: Query? = null;
        val requestId = UUID.randomUUID().toString()
//        val timeStamp = Utilities.dfTimestamp.format(Date(System.currentTimeMillis()))
        val requester = Requester("HIP", ApplicationConstants.ABDMFacilityId)
//        query = if (constantData?.abhaAddress?.isEmpty() == true) {
//            Query(constantData?.abhaNumber, "KYC_AND_LINK", authMode, requester)
//        } else if (constantData?.abhaNumber?.isEmpty() == true) {
//            Query(constantData?.abhaAddress, "KYC_AND_LINK", authMode, requester)
//        } else {
//            Query(constantData?.abhaNumber, "KYC_AND_LINK", authMode, requester)
//        }

//        query = Query(constantData?.abhaAddress, "KYC_AND_LINK", authMode, requester)
        val initAuthRequestModel: InitAuthRequestModel =
            InitAuthRequestModel(requestId, utcTime, query)

        Log.d(TAG, "initAuth: ${Gson().toJson(initAuthRequestModel)}")
        HealthCheckup.abhaClient.initAuth(
            "Bearer $accessToken",
            BuildConfig.CMID.replace("@",""),
            initAuthRequestModel
        )
            .enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d(TAG, "onResponse: ${response.body()?.string()}")
                    progressDialog.dismiss()
                    if (response.code() == 202) {
                        Utilities.showMessageString(
                            "Init Auth,Successful",
                            this@CareContextLinkingActivity
                        )
                        progressDialog.setMessage("Getting Request Details..")
                        progressDialog.setCancelable(false)
                        progressDialog.show()
                        Handler().postDelayed({
                            progressDialog.dismiss()
                            getPreRequestDetails(requestId, 0)
                        }, 5000)

                    } else {
                        Utilities.showMessageString(
                            "Init Auth,UnSuccessful",
                            this@CareContextLinkingActivity
                        )

                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Utilities.showAlertDialog(
                        this@CareContextLinkingActivity,
                        "Server error",
                        t.message,
                        false
                    )

                }

            })
    }

    private fun getPreRequestDetails(requestId: String, flag: Int) {
        progressDialog.setMessage("Getting Request Details..")
        progressDialog.setCancelable(false)
        progressDialog.show()


//        HealthCheckup.MAHAHINDClient.getPreReqDetails(requestId)
//            .enqueue(object : Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//
//                    try {
//
//
//                        val res = response.body()?.string()
//                        Log.d(TAG, "onResponse: ${res}")
//
//                        progressDialog.dismiss()
//                        if (response.isSuccessful) {
//
//                            val parsedOutput = JSONObject(res)
//                            val status = parsedOutput.getString("Status")
//                            val msg = parsedOutput.getString("Message")
//                            if (status.equals("success", ignoreCase = true)) {
//                                val outputArr = parsedOutput.getJSONArray("output")
//                                val outputJson = outputArr.getJSONObject(0)
//                                if (outputJson.has("ErrorMessage")) {
//                                    if (outputJson.getString("ErrorMessage").isNotEmpty()) {
//                                        Utilities.showAlertDialog(
//                                            this@CareContextLinkingActivity,
//                                            "Error",
//                                            outputJson.getString("ErrorMessage"),
//                                            false
//                                        )
////                                        createABHASession()
//                                        return
//
//                                    }
//                                }
//
//                                if (flag == 3) { // For AuthModes
////                                llPatientInfo.setVisibility(View.VISIBLE);
//                                    val responseObj = JSONObject(outputJson.getString("response"))
//                                    val authObj = responseObj.getJSONObject("auth")
//                                    val modeArr = authObj.getJSONArray("modes")
//                                    llAuthModes.setVisibility(View.VISIBLE)
//                                    authModesList?.clear()
////                                    if (VRFY_ABHA_FLAG == 1) {
////                                        for (i in 0 until modeArr.length()) {
////                                            if (!modeArr.getString(i)
////                                                    .equals("password", ignoreCase = true)
////                                            ) {
////                                                authModesList?.add(modeArr.getString(i))
////                                            }
////                                        }
////                                    } else {
//                                    for (i in 0 until modeArr.length()) {
//                                        if (!modeArr.getString(i)
//                                                .equals("password", ignoreCase = true)
//                                        ) {
//                                            authModesList?.add(modeArr.getString(i))
//                                        }
//                                    }
////                                    }
////                                    rvAuthModes.setAdapter(AuthModesListAdapter(authModes) { mode: String? ->
////                                        this@PatientRegistrationActivity.onModeSelect(
////                                            mode
////                                        )
////                                    })
//
////                                initAuth(2);
//                                }
//
//
//                                if (flag == 0) {
//                                    authTransId = outputJson.getString("transactionId")
//                                    authToken = outputJson.getString("authtoken")
//                                    if (btnSendOTP.text.equals("Validate")) {
//                                        confirmAuth()
//                                    } else {
//                                        Utilities.showMessageString(
//                                            "OTP sent successfully",
//                                            this@CareContextLinkingActivity
//                                        )
//
//                                        OTPView.visibility = View.VISIBLE
//
//                                        var cdt = object : CountDownTimer(90000, 1000) {
//                                            override fun onTick(millisUntilFinished: Long) {
//                                                isTimerFinished = false
//                                                tvTimer.setText("" + millisUntilFinished / 1000)
//                                                btnResend.isEnabled = isTimerFinished
//
//                                            }
//
//                                            override fun onFinish() {
////                                        tvTimer.setText("00:00");
//                                                isTimerFinished = true
//                                                btnResend.isEnabled = isTimerFinished
//
//                                            }
//                                        }.start()
//                                    }
//
//                                }
//                                if (flag == 1) {
////                                    authTransId = outputJson.getString("transactionId")
//                                    authToken = outputJson.getString("authtoken")
//
////                                    if (outputJson.has("ErrorMessage")) {
////                                        if (outputJson.getString("ErrorMessage").isNotEmpty()) {
////                                            Utilities.showAlertDialog(
////                                                this@CareContextLinkingActivity,
////                                                "Error",
////                                                outputJson.getString("ErrorMessage"),
////                                                false
////                                            )
////                                            return
////                                        }
////                                    }
//
//                                    addCareContext(authToken);
//
//                                }
//
//                            } else {
////                                createABHASession()
//
//                                Utilities.showAlertDialogMandatory(
//                                    this@CareContextLinkingActivity,
//                                    status,
//                                    msg,
//                                    false,
//                                    "Re-Try",
//                                    object : DialogInterface.OnClickListener {
//                                        override fun onClick(dialog: DialogInterface?, which: Int) {
//                                            createABHASession(null)
//                                        }
//
//                                    }
//                                )
//                            }
//
////                        if (response.body()?.Status.equals("Success")) {
////                            authTransId = response.body()!!.output[0].transactionId!!
////                        }
//                        } else {
//                            Utilities.showMessageString(
//                                "Unable to get Request Details",
//                                this@CareContextLinkingActivity
//                            )
//
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        Utilities.showMessageString(
//                            "Unable to get Request Details ${e.message}",
//                            this@CareContextLinkingActivity
//                        )
//
//                    }
//
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    progressDialog.dismiss()
//                    Utilities.showAlertDialog(
//                        this@CareContextLinkingActivity,
//                        "Server error",
//                        t.message,
//                        false
//                    )
//
//                }
//
//            })


    }

    private fun confirmAuth() {
        progressDialog.setMessage("Confirm Auth..")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val requestId = UUID.randomUUID().toString()
        val transId = authTransId

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1 // Month is 0-based, so add 1

        val day = calendar[Calendar.DAY_OF_MONTH]
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]


        val timeStamp =
            String.format(
                "%04d-%02d-%02dT%02d:%02d:%02d.342Z",
                year,
                month,
                day,
                hour,
                minute,
                second
            )
//        val timeStamp = Utilities.dfTimestamp.format(Date(System.currentTimeMillis()))
        val otp =
            edtOTP1.text.toString() + edtOTP2.text.toString() + edtOTP3.text.toString() + edtOTP4.text.toString() + edtOTP5.text.toString() + edtOTP6.text.toString()
        var demographic: Demographic? = null
//        try {
//            val identifier = Identifier("MOBILE", constantData?.mobno)
//            demographic = if (constantData?.dob!!.contains("-")) {
//                Demographic(
//                    edtFullName.text.toString(),
//                    constantData?.selectedgenderId,
//                    Utilities.dfDate.format(
//                        Utilities.dfDDMMYYYYDash.parse(constantData?.dob)
//                    ),
//                    identifier
//                )
//            } else {
//                Demographic(
//                    edtFullName.text.toString(),
//                    constantData?.selectedgenderId,
//                    Utilities.dfDate.format(
//                        Utilities.dfDate2.parse(constantData?.dob)
//                    ),
//                    identifier
//                )
//            }
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//
//        val credential = Credential(
//            if (authMode.equals(
//                    authMode,
//                    ignoreCase = true
//                )
//            ) demographic else null,
//            if (authMode.equals("otp", ignoreCase = true)) null else otp
//        )
//
//
//        val confirmAuthRequestModel =
//            ConfirmAuthRequestModel(requestId, timeStamp, transId, credential)
//        HealthCheckup.abhaClient.confirmAuth(
//            "Bearer $accessToken",
//            BuildConfig.CMID.replace("@",""),
//            confirmAuthRequestModel
//        )
//            .enqueue(object :
//                Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//                    progressDialog.dismiss()
//                    if (response.code() == 202) {
//                        Utilities.showMessageString(
//                            "Confirm Auth,Successful",
//                            this@CareContextLinkingActivity
//                        )
//                        progressDialog.setMessage("Getting Request Details..")
//                        progressDialog.setCancelable(false)
//                        progressDialog.show()
//                        Handler().postDelayed({
//                            progressDialog.dismiss()
//                            getPreRequestDetails(requestId, 1)
//                        }, 5000)
//                    } else {
//                        Utilities.showMessageString(
//                            "Confirm Auth,UnSuccessful",
//                            this@CareContextLinkingActivity
//                        )
//
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    progressDialog.dismiss()
//                    Utilities.showAlertDialog(
//                        this@CareContextLinkingActivity,
//                        "Server error",
//                        t.message,
//                        false
//                    )
//
//                }
//
//            })

    }

//    private fun addCareContext(authToken: String?) {
//        progressDialog.setMessage("Adding Care Context..")
//        progressDialog.setCancelable(false)
//        progressDialog.show()
//        val requestId = UUID.randomUUID().toString()
//        val transId = UUID.randomUUID().toString()
//        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
//        val year = calendar[Calendar.YEAR]
//        val month = calendar[Calendar.MONTH] + 1 // Month is 0-based, so add 1
//
//        val day = calendar[Calendar.DAY_OF_MONTH]
//        val hour = calendar[Calendar.HOUR_OF_DAY]
//        val minute = calendar[Calendar.MINUTE]
//        val second = calendar[Calendar.SECOND]
//
//
//        val timeStamp =
//            String.format(
//                "%04d-%02d-%02dT%02d:%02d:%02d.342Z",
//                year,
//                month,
//                day,
//                hour,
//                minute,
//                second
//            )
//        val otp =
//            edtOTP1.text.toString() + edtOTP2.text.toString() + edtOTP3.text.toString() + edtOTP4.text.toString() + edtOTP5.text.toString() + edtOTP6.text.toString()
//        var testNameArr: String = ""
//        constantData?.labTestArrays?.forEach {
//            testNameArr += "${it.testName},"
//        }
//        val careContext = CareContexts(
//            constantData?.barcode,
//            "$testNameArr Conducted on ${Utilities.dfDate2.format(Date())}"
//        )
//        val arrCareContexts = arrayListOf<CareContexts>()
//        arrCareContexts.add(careContext)
//        val pat = Patient(
//            constantData?.barcode,
//            constantData?.fname + " " + constantData?.lname,
//            arrCareContexts
//        )
//        val link = Link(authToken, pat)
//        val addContextRequestModel = AddContextRequestModel(requestId, timeStamp, link)
//        Log.d(TAG, "addCareContext: ${Gson().toJson(addContextRequestModel)}")
//        HLL_Connect.abhaClient.addContext(
//            "Bearer $accessToken",
//            BuildConfig.CMID.replace("@",""),
//            addContextRequestModel
//        )
//            .enqueue(object :
//                Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//                    progressDialog.dismiss()
//                    Log.d(TAG, "onResponse: ${response.code()}")
//                    if (response.code() == 202) {
//                        Utilities.showMessageString(
//                            "Add Care Context,Successful",
//                            this@CareContextLinkingActivity
//                        )
//                        notifyContext();
//
//                    } else {
//                        Utilities.showMessageString(
//                            "Add Care Context,UnSuccessful",
//                            this@CareContextLinkingActivity
//                        )
//
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    progressDialog.dismiss()
//                    Utilities.showAlertDialog(
//                        this@CareContextLinkingActivity,
//                        "Server error",
//                        t.message,
//                        false
//                    )
//
//                }
//
//            })
//
//    }

    private fun smsNotify() {
        progressDialog.setMessage("Adding Care Context..")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val requestId = UUID.randomUUID().toString()
        val transId = UUID.randomUUID().toString()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1 // Month is 0-based, so add 1

        val day = calendar[Calendar.DAY_OF_MONTH]
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]


        val timeStamp =
            String.format(
                "%04d-%02d-%02dT%02d:%02d:%02d.342Z",
                year,
                month,
                day,
                hour,
                minute,
                second
            )
        val otp =
            edtOTP1.text.toString() + edtOTP2.text.toString() + edtOTP3.text.toString() + edtOTP4.text.toString() + edtOTP5.text.toString() + edtOTP6.text.toString()

        val hip = Hip(ApplicationConstants.ABDMFacilityId, ApplicationConstants.ABDMFacilityName)
//        val notification = Notification(constantData?.mobno, hip)
//        val smsNotify = SMSNotifyRequestModel(requestId, timeStamp, notification)
//        Log.d(TAG, "smsNotify: ${Gson().toJson(smsNotify)}")
//        HLL_Connect.abhaClient.smsNotify("Bearer $accessToken", BuildConfig.CMID.replace("@",""), smsNotify)
//            .enqueue(object :
//                Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//                    progressDialog.dismiss()
//                    if (response.code() == 202) {
//                        Utilities.showMessageString(
//                            "SMS notification,Successful",
//                            this@CareContextLinkingActivity
//                        )
//                        PatientRegistrationActivity.fa1.finish();
////                        if (getIntent().getIntExtra("isMantralaya", 0) == 0) {
////                            AssingTestAccordionActivity.fa2.finish();
////                        }
//                        PatientSampleCollectionActivity.fa3.finish();
//                        clearConstantData();
//                        startActivity(
//                            Intent(
//                                this@CareContextLinkingActivity,
//                                PatientRegistrationActivity::class.java
//                            ).putExtra(
//                                "testList",
//                                constantData?.getLabTestArrays()
//                            )
//                        );
//                        finish();
//                    } else {
//                        Utilities.showMessageString(
//                            "SMS notification,UnSuccessful",
//                            this@CareContextLinkingActivity
//                        )
//
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    progressDialog.dismiss()
//                    Utilities.showAlertDialog(
//                        this@CareContextLinkingActivity,
//                        "Server error",
//                        t.message,
//                        false
//                    )
//
//                }
//
//            })

    }

    private fun clearConstantData() {
//        constantData!!.selectedtitleId = ""
//        constantData!!.fname = ""
//        constantData!!.mname = ""
//        constantData!!.lname = ""
//        constantData!!.aadharno = ""
//        constantData!!.selectedgenderId = ""
//        constantData!!.femaleCategoryId = ""
//        constantData!!.dob = ""
//        constantData!!.age = ""
//        constantData!!.ageType = ""
//        constantData!!.mobno = ""
//        constantData!!.address = ""
//        constantData!!.selectedcenterId = ""
//        constantData!!.selectedfacilityId = ""
//        constantData!!.selectedcityId = ""
//        constantData!!.selectedtalukaId = ""
//        constantData!!.selecteddistrictId = ""
//        constantData!!.selectedstateId = ""
//        constantData!!.selectedcountryId = ""
//        constantData!!.pincode = ""
//        constantData!!.currdate = ""
//        constantData!!.phlebouserid = ""
//        constantData!!.collectedOn = ""
//        constantData!!.billAmount = ""
//        constantData!!.dateOfEntry = ""
//        constantData!!.patient_id = ""
//        constantData!!.treatmentId = ""
//        constantData!!.timeOfEntry = ""
//        constantData!!.collectedDate = ""
//        constantData!!.collectedTime = ""
//        constantData!!.hllBarcode = ""
//        constantData!!.totalSampleCnt = ""
//        constantData!!.refDoctor = ""
//        constantData!!.abhaNumber = ""
//        constantData!!.abhaAddress = ""
//        constantData!!.sampleType = ""
//        constantData!!.gestationalWeek = ""
//        constantData!!.birthWeight = ""
//        constantData!!.birthTime = ""
//        val list = ArrayList<LabTestArray>()
//        if (intent.getIntExtra("isMantralaya", 0) == 0) {
//            constantData!!.labTestArrays = list
//        }
//        constantData!!.testArray1 = list
//        constantData!!.testArray2 = list
//        constantData!!.testArray3 = list
//        constantData!!.testArray4 = list
//        constantData!!.testArray5 = list
//        constantData!!.testArray6 = list
//        constantData!!.testArray7 = list
//        constantData!!.testArray8 = list
//        constantData!!.testArray9 = list
//        constantData!!.colldate = ""
//        constantData!!.colltime = ""
//        constantData!!.barcode = ""
//        constantData!!.sugarBarcode = ""
//        constantData!!.samplecount = ""
//        constantData!!.doctor = ""
//        constantData!!.refDoctorCode = ""
//        constantData!!.test_result_master_id = ""
    }

//    fun notifyContext() {
//        progressDialog.setMessage("Notifying context..")
//        progressDialog.setCancelable(false)
//        progressDialog.show()
//
//        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
//        val year = calendar[Calendar.YEAR]
//        val month = calendar[Calendar.MONTH] + 1 // Month is 0-based, so add 1
//
//        val day = calendar[Calendar.DAY_OF_MONTH]
//        val hour = calendar[Calendar.HOUR_OF_DAY]
//        val minute = calendar[Calendar.MINUTE]
//        val second = calendar[Calendar.SECOND]
//
//
//        val utcTime =
//            String.format(
//                "%04d-%02d-%02dT%02d:%02d:%02d.342Z",
//                year,
//                month,
//                day,
//                hour,
//                minute,
//                second
//            )
//        val requestId = UUID.randomUUID().toString()
//        val patient =
//            constantData?.let { com.myhindlab.abkat.abha.models.context_notify.Patient(it.abhaAddress) }
//        val careContext =
//            constantData?.let {
//                CareContext(
//                    patientReference = it.barcode,
//                    careContextReference = it.barcode
//                )
//            }
//        val hipType = arrayListOf<String>("HealthDocumentRecord")
//        val hip =
//            com.myhindlab.abkat.abha.models.context_notify.Hip(ApplicationConstants.ABDMFacilityId)
//        val notification = com.myhindlab.abkat.abha.models.context_notify.Notification(
//            patient!!,
//            careContext!!,
//            hipType,
//            utcTime,
//            hip
//        )
//        val notifyContextModel = ContextNotifyRequestModel(requestId, utcTime, notification)
//
//        Log.d(TAG, "notifyContext: ${Gson().toJson(notifyContextModel)}")
//
//        HLL_Connect.abhaClient.contextNotify(
//            "Bearer $accessToken",
//            BuildConfig.CMID.replace("@",""),
//            notifyContextModel
//        )
//            .enqueue(
//                object : Callback<ResponseBody> {
//                    override fun onResponse(
//                        call: Call<ResponseBody>,
//                        response: Response<ResponseBody>
//                    ) {
//                        progressDialog.dismiss()
//                        Log.d(TAG, "onResponse: ${response.code()}")
//                        if (response.code() == 202) {
////                            Utilities.showMessageString(
////                                "Context notify,Successful",
////                                this@CareContextLinkingActivity
////                            )
////                            smsNotify()
//                            PatientRegistrationActivity.fa1.finish();
////                            if (intent.getIntExtra("isMantralaya", 0) == 0) {
////                                AssingTestAccordionActivity.fa2.finish();
////                            }
//                            PatientSampleCollectionActivity.fa3.finish();
//                            clearConstantData();
//                            startActivity(
//                                Intent(
//                                    this@CareContextLinkingActivity,
//                                    PatientRegistrationActivity::class.java
//                                ).putExtra(
//                                    "testList",
//                                    constantData?.getLabTestArrays()
//                                )
//                            );
//                            finish();
//
//                        } else {
////                            Utilities.showMessageString(
////                                "Context notify,UnSuccessful",
////                                this@CareContextLinkingActivity
////                            )
//
//                        }
//                    }
//
//                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
////                        Utilities.showMessageString(
////                            "Context notify,UnSuccessful",
////                            this@CareContextLinkingActivity
////                        )
//                    }
//
//                })
//    }

    private fun fetchAuthModes() {
        progressDialog.setMessage("Fetching auth modes..")
        progressDialog.setCancelable(false)
        progressDialog.show()
//        authMode = ""
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH] + 1 // Month is 0-based, so add 1
        val day = calendar[Calendar.DAY_OF_MONTH]
        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]
        val utcTime = String.format(
            "%04d-%02d-%02dT%02d:%02d:%02d.342Z",
            year,
            month,
            day,
            hour,
            minute,
            second
        )
        val requestId = UUID.randomUUID().toString()
        val timeStamp = Utilities.dfTimestamp.format(Date(System.currentTimeMillis()))
        val requester = com.myhindlab.abkat.abha.models.fetch_auth_modes.Requester(
            "HIP",
            ApplicationConstants.ABDMFacilityId
        )
        val query = com.myhindlab.abkat.abha.models.fetch_auth_modes.Query(
            if (edtABHAAddress.text.toString()
                    .isEmpty()
            ) edtABHANumber.text.toString() else edtABHAAddress.text.toString(),
            "LINK",
            requester
        )
        val fetchAuthModesModel = FetchAuthRequestModel(requestId, utcTime, query)
        Log.d(TAG, "fetchAuthModes: \${Gson().toJson(initAuthRequestModel)}")
        HealthCheckup.abhaClient.fetchModes(
            "Bearer $accessToken",
            BuildConfig.CMID.replace("@",""),
            fetchAuthModesModel
        )
            .enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>,
                    response: Response<ResponseBody?>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        if (response.code() == 202) {
                            progressDialog.setMessage("Getting Request Details..")
                            progressDialog.setCancelable(false)
                            progressDialog.show()
                            Handler().postDelayed({ getPreRequestDetails(requestId, 3) }, 3000)
                        }
                    } else {
                        try {
                            val errRes = response.errorBody()!!.string()
                            Utilities.showAlertDialog(
                                this@CareContextLinkingActivity,
                                "Error",
                                errRes,
                                false
                            )
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    progressDialog.dismiss()
                    Utilities.showAlertDialog(
                        this@CareContextLinkingActivity,
                        "Error",
                        t.message,
                        false
                    )
                }
            })
    }



}