package com.myhindlab.abkat.abha.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myhindlab.abkat.abha.models.patient_queue.GetQueueResponseModel
import com.google.gson.Gson
import com.myhindlab.abkat.HealthCheckup
import com.myhindlab.abkat.R
import com.myhindlab.abkat.abha.adapters.PatientQueueAdapter
import com.myhindlab.abkat.utilities.Utilities
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ABHAPatientQueueActivity : AppCompatActivity() {
    private lateinit var rvPatientQueue: RecyclerView
    private lateinit var imvNoData: ImageView
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mContext: Context
    val TAG: String = ABHAPatientQueueActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abhapatient_queue)
        init()
        setupToolbar()
        intent.getStringExtra("campId")?.let { getRegistrationQueue(it) }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Patient Queue"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    fun init() {
        mContext = this@ABHAPatientQueueActivity
        rvPatientQueue = findViewById(R.id.rvPatientQueueList)
        imvNoData = findViewById(R.id.imvNoData)
        progressDialog = ProgressDialog(mContext)
        val campId = intent.getStringExtra("campId");

        if (campId != null) {
            getRegistrationQueue(campId)
        }

    }

    private fun getRegistrationQueue(campId: String) {
        progressDialog.setMessage("Getting patients in queue...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        HealthCheckup.getD2DClient.getPatientQueue(campId?.toLong())
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        try {
                            val strResp = response.body()?.string()
                            val status = JSONObject(strResp).getString("status")
                            if (status.equals("success", true)) {
                                imvNoData.visibility = View.GONE

                                val patientQueueResponseModel: GetQueueResponseModel =
                                    Gson().fromJson(strResp, GetQueueResponseModel::class.java)

                                rvPatientQueue.hasFixedSize()
                                rvPatientQueue.layoutManager = LinearLayoutManager(mContext)
                                rvPatientQueue.adapter =
                                    PatientQueueAdapter(patientQueueResponseModel.output)
                            } else {
                                imvNoData.visibility = View.GONE

                                Utilities.showAlertDialogMandatory(
                                    mContext,
                                    "Queue is empty",
                                    "No patient in queue",
                                    false,
                                    "Okay",
                                    object : DialogInterface.OnClickListener {
                                        override fun onClick(dialog: DialogInterface?, which: Int) {
                                            finish()
                                        }

                                    })
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            imvNoData.visibility = View.GONE
                            Utilities.showAlertDialogMandatory(
                                mContext,
                                "Queue is empty",
                                "No patient in queue",
                                false,
                                "Okay",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        finish()
                                    }

                                })

                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.d(TAG, "onFailure: ${t.message}")
                    imvNoData.visibility = View.GONE

                }

            })
    }
}