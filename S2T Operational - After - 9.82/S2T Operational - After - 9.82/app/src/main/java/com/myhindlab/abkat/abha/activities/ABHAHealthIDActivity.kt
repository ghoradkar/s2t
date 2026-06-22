package com.myhindlab.abkat.abha.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.myhindlab.abkat.BuildConfig
import com.myhindlab.abkat.HealthCheckup
import com.myhindlab.abkat.R
import com.myhindlab.abkat.abha.models.ABHASessionModel
import com.myhindlab.abkat.abha.models.abha_creation_response_model.ABHACreationResponseModel
import com.myhindlab.abkat.abha.models.account_profile.AccountProfileResponseModel
import com.myhindlab.abkat.utilities.ApplicationConstants
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream
import java.util.UUID


class ABHAHealthIDActivity : AppCompatActivity() {
    lateinit var tvName: TextView
    lateinit var tvDOB: TextView
    lateinit var tvGender: TextView
    lateinit var tvABHANo: TextView
    lateinit var imvProfile: ImageView
    lateinit var imvCard: ImageView
    lateinit var btnGotoReg: Button
    lateinit var btnDownload: Button
    var txnId: String? = null
    var accessToken: String? = null
    var authToken: String? = null
    var createHealthIdAuthToken: String? = null
    lateinit var progressDialog: ProgressDialog
    val TAG: String = ABHAHealthIDActivity::class.java.simpleName
    lateinit var mContext: Context
    var healthCard: ABHACreationResponseModel? = null
    lateinit var sessionManager: UserSessionManager
    var campId = 0
    var savedABHACard: File? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abhahealth_idactivity)

        setUpToolbar()
        init()
        setDefault()
        clickEvents()

    }

    private fun clickEvents() {
        btnDownload.setOnClickListener {
            if (savedABHACard != null) {
                if (Utilities.copyFileToDownloads(
                        savedABHACard,
                        savedABHACard?.name,
                        this@ABHAHealthIDActivity
                    )
                ) {
                    Utilities.showAlertDialog(
                        this@ABHAHealthIDActivity,
                        "Success",
                        "File Saved Successfully!",
                        true
                    )

                } else {
                    Utilities.showAlertDialog(
                        this@ABHAHealthIDActivity,
                        "Failed",
                        "Unable To Save File.",
                        false
                    )
                }
            } else {
                Utilities.showAlertDialog(
                    this@ABHAHealthIDActivity,
                    "Error",
                    "ABHA Card Not Saved",
                    false
                )
            }

        }
        btnGotoReg.setOnClickListener {
            if (intent.getIntExtra("verificationCard", 0) == 0) {
                LocalBroadcastManager.getInstance(this@ABHAHealthIDActivity)
                    .sendBroadcast(
                        Intent("set_abha_data").putExtra(
                            "authToken",
                            authToken
                        )
                            .putExtra(
                                "mobile",
                                intent.getLongExtra("mobile", 0)
                            )
                    )
            }
//            startActivity(
//                Intent(this@ABHAHealthIDActivity, D2DPatientRegistration_Activity::class.java)
//                    .putExtra("campId", intent.getStringExtra("campId"))
//                    .putExtra("district", intent.getStringExtra("district"))
//                    .putExtra("distlgdcode", intent.getStringExtra("distlgdcode"))
//                    .putExtra("siteId", intent.getStringExtra("siteId"))
//                    .putExtra("Latitude", intent.getStringExtra("Latitude"))
//                    .putExtra("Longitude", intent.getStringExtra("Longitude"))
//                    .putExtra("campType", intent.getStringExtra("campType"))
//                    .putExtra("authToken", authToken)
//            )
            finish()


        }
    }

    fun getABHCard() {
        progressDialog.setMessage("Downloading Card..")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val timeStamp = Utilities.getCurrentTimeStamp()
        val requestId = UUID.randomUUID().toString()
        HealthCheckup.ABDMClient.getPngCard(
            "Bearer $accessToken",
            timeStamp,
            requestId,
            "Bearer $authToken"
        )
            .enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        val res = response.body()
                        Log.i(TAG, "onResponse: $res")
                        try {
                            val fileName = System.currentTimeMillis().toString() + ".png"
                            savedABHACard = Utilities.saveFileFromResponse(
                                res,
                                fileName,
                                this@ABHAHealthIDActivity
                            )
                            if (savedABHACard != null) {
                                Glide.with(mContext).load(savedABHACard).into(imvCard)
                                Log.i(TAG, "Image saved successfully: $savedABHACard")
                            } else {
                                Log.e(TAG, "Failed to save file.")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e(TAG, "Failed to save file.", e)
                        }
                    } else {
                        Utilities.showAlertDialog(
                            this@ABHAHealthIDActivity,
                            "Error",
                            response.errorBody()?.string(),
                            false
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Utilities.showAlertDialog(
                        this@ABHAHealthIDActivity,
                        "Error",
                        t.message,
                        false
                    )
                }

            })
    }


    private fun getABHACardABHAAddress() {
        progressDialog.setMessage("Downloading Card..")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val timeStamp = Utilities.getCurrentTimeStamp()
        val requestId = UUID.randomUUID().toString()

        /// if beta use ABDMClient for live PHRSBXClient

        HealthCheckup.PHRSBXClient.getPngCardPHR(
            "Bearer $accessToken",
            timeStamp,
            requestId,
            "Bearer $authToken"
        )
            .enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        val res = response.body()
                        Log.i(TAG, "onResponse: $res")
                        try {
                            val fileName = System.currentTimeMillis().toString() + ".png"
                            savedABHACard = Utilities.saveFileFromResponse(
                                res,
                                fileName,
                                this@ABHAHealthIDActivity
                            )
                            if (savedABHACard != null) {
                                Glide.with(mContext).load(savedABHACard).into(imvCard)
                                Log.i(TAG, "Image saved successfully: $savedABHACard")
                            } else {
                                Log.e(TAG, "Failed to save file.")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e(TAG, "Failed to save file.", e)
                        }
                    } else {
                        Utilities.showAlertDialog(
                            this@ABHAHealthIDActivity,
                            "Error",
                            response.errorBody()?.string(),
                            false
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Utilities.showAlertDialog(
                        this@ABHAHealthIDActivity,
                        "Error",
                        t.message,
                        false
                    )
                }

            })
    }


    fun File.copyInputStreamToFile(inputStream: InputStream) {
        this.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
    }

    fun init() {
        mContext = this@ABHAHealthIDActivity
        progressDialog = ProgressDialog(this@ABHAHealthIDActivity)
        sessionManager = UserSessionManager(mContext)
        tvABHANo = findViewById(R.id.tvABHANo)
        tvDOB = findViewById(R.id.tvDOB)
        tvName = findViewById(R.id.tvName)
        tvGender = findViewById(R.id.tvGender)
        imvProfile = findViewById(R.id.imvProfile)
        btnGotoReg = findViewById(R.id.btnGotoReg)
        imvCard = findViewById(R.id.imvCard)
        btnDownload = findViewById(R.id.btnDownload)

        if (intent != null) {
            accessToken = intent.getStringExtra("accessToken")
            txnId = intent.getStringExtra("txnId")
            authToken = intent.getStringExtra("authToken")
            createHealthIdAuthToken = intent.getStringExtra("createHealthIdAuthToken")
            createABHASession()
        }


    }

    fun setDefault() {
        try {
            val healthCard: ABHACreationResponseModel =
                intent.getSerializableExtra("healthCard") as ABHACreationResponseModel
            tvABHANo.text = healthCard.ABHAProfile?.ABHANumber
            tvName.text =
                healthCard.ABHAProfile?.firstName + " " + healthCard.ABHAProfile?.middleName + " " + healthCard.ABHAProfile?.lastName;
            tvDOB.text = "${healthCard.ABHAProfile?.dob}"
            tvGender.text = healthCard.ABHAProfile?.gender
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun setUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.title = "ABHA address creation"

//        supportActionBar?.setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener {
//            finish()
//        }
    }

    override fun onBackPressed() {

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
//                                getPNGCard()
                                val whichCard = intent.getIntExtra("verificationCard", 0)
                                if (whichCard == 0 || whichCard == 1) {
                                    getABHCard()
                                } else {
                                    getABHACardABHAAddress()
                                }

                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            Utilities.showMessageString(
                                "Unable to create session",
                                this@ABHAHealthIDActivity
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
                        this@ABHAHealthIDActivity
                    )


                }

            })

    }


    private fun getPNGCard() {
        progressDialog.setMessage("Getting Card..")
        progressDialog.setCancelable(false)
        progressDialog.show()
//        val client = OkHttpClient.Builder()
//            .addInterceptor { chain ->
//                val newRequest: Request = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer $accessToken")
//                    .addHeader("X-TOKEN", "Bearer $authToken")
//                    .build()
//                chain.proceed(newRequest)
//            }
//            .build()
//
//        val picasso = Picasso.Builder(this@ABHAHealthIDActivity)
//            .downloader(OkHttp3Downloader(client))
//            .build()
//
//        picasso.isLoggingEnabled = true
//        picasso.load(BuildConfig.PHRSBX + ApplicationConstants.getPngCard).into(imvCard)

//        CSCHealthCheckup.ABDMClient.getPngCard("Bearer $accessToken", "Bearer $authToken")
//            .enqueue(object :
//                Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//                    progressDialog.dismiss()
//                    if (response.isSuccessful) {
//                        val res = response.body()?.string()
//                        Log.i(TAG, "onResponse: $res")
//                    } else {
//                        Utilities.showAlertDialog(
//                            this@ABHAHealthIDActivity,
//                            "Error",
//                            response.errorBody()?.string(),
//                            false
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    progressDialog.dismiss()
//                    Utilities.showAlertDialog(this@ABHAHealthIDActivity, "Error", t.message, false)
//                }
//
//            })


        val url = GlideUrl(
            BuildConfig.HealthIDSBX + ApplicationConstants.getPngCard, LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer $accessToken")
                .addHeader("X-Token", "Bearer $authToken")
                .build()
        )


        val options: RequestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        Glide.with(this@ABHAHealthIDActivity).load(url)
            .listener(object : RequestListener<Drawable> {
                //                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    progressDialog.dismiss()
//                    return false
//                }
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    progressDialog.dismiss()
                    return false
                }

//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    progressDialog.dismiss()
//
//                    return false
//                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    progressDialog.dismiss()
                    return false
                }

            })
            .transition(withCrossFade())
            .thumbnail(0.5f)
            .apply(options)
            .into(imvCard);

    }

    private fun getAccountProfile(flag: Int?) {
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

                        LocalBroadcastManager.getInstance(this@ABHAHealthIDActivity)
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
                        finish()
                    } else {
                        Utilities.showAlertDialog(
                            this@ABHAHealthIDActivity,
                            "Error",
                            response.message()?.let { it -> it },
                            false
                        )
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Utilities.showAlertDialog(mContext, "Error", t.message, false)
                }
            })
    }


}