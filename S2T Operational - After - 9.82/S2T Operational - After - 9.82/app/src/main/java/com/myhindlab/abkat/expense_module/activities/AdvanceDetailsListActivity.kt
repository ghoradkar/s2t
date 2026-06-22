package com.myhindlab.abkat.expense_module.activities

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.myhindlab.abkat.R
import com.myhindlab.abkat.databinding.ActivityAdvanceDetailsListBinding
import com.myhindlab.abkat.models.DistrictList_Model
import com.myhindlab.abkat.pojos.DistrictList_Pojo
import com.myhindlab.abkat.rest.ApiClient
import com.myhindlab.abkat.rest.ApiInterface
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities
import com.myhindlab.abkat.expense_module.adapters.AdvanceRequestListAdapter
import com.myhindlab.abkat.expense_module.models.advance_detail_response.AdvanceDetailsResponseModel
import com.myhindlab.abkat.expense_module.models.advance_detail_response.Output
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AdvanceDetailsListActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdvanceDetailsListBinding
    private var mYear = 0
    private var mMonth: kotlin.Int = 0
    private var mDay: kotlin.Int = 0
    private var fromDate: String? =
        null
    private var toDate: kotlin.String? = null
    private var districtId: kotlin.String? = "0"
    lateinit var apiInterface: ApiInterface

    val TAG: String = AdvanceDetailsListActivity::class.java.simpleName
    lateinit var progressDialog: ProgressDialog

    lateinit var localBroadcastManager: LocalBroadcastManager
    lateinit var sessionManager: UserSessionManager
    lateinit var advRes: ArrayList<Output>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvanceDetailsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        init()
        eventListener()
        getAdvanceDetails(fromDate, toDate)

    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "onReceive: ${intent.getStringExtra("fromDate")}")
            getAdvanceDetailsRefresh(
                intent.getStringExtra("fromDate"),
                intent.getStringExtra("toDate")
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
    }


    fun init() {
        progressDialog = ProgressDialog(this@AdvanceDetailsListActivity)
        apiInterface = ApiClient.getD2DClient().create(ApiInterface::class.java)
        sessionManager = UserSessionManager(this@AdvanceDetailsListActivity)
        binding.edtSelectdistrict.setText(sessionManager.userDetailsJson.district)
        binding.edtSelectdistrict.isEnabled =
            sessionManager.userDetailsJson.desgid == 84 || sessionManager.userDetailsJson.desgid == 102
        districtId = sessionManager.userDetailsJson.distlgdcode.toString()
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]

        localBroadcastManager = LocalBroadcastManager.getInstance(this@AdvanceDetailsListActivity)
        val intentFilter = IntentFilter("RefreshAdvanceList")
        localBroadcastManager.registerReceiver(broadcastReceiver, intentFilter)

        fromDate =
            mYear.toString() + "-" + DecimalFormat("00").format((mMonth + 1).toLong()) + "-" + DecimalFormat(
                "00"
            ).format(
                mDay.toLong()
            )
        toDate =
            mYear.toString() + "-" + DecimalFormat("00").format((mMonth + 1).toLong()) + "-" + DecimalFormat(
                "00"
            ).format(
                mDay.toLong()
            )
        binding.edtFromDate?.setText(fromDate)
        binding.edtToDate?.setText(toDate)

    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Bill Submission"

        toolbar.setNavigationIcon(R.drawable.icon_arrowback)
        toolbar.setNavigationOnClickListener { finish() }
    }

    fun eventListener() {
        binding.edtSelectdistrict.setOnClickListener(View.OnClickListener {
            if (Utilities.isNetworkAvailable(this@AdvanceDetailsListActivity)) {
                getDistrict()
            } else {
                Utilities.showToastMessage(
                    R.string.msgt_nointernetconnection,
                    this@AdvanceDetailsListActivity,
                    false
                )
            }
        })


//        binding.rvAdvanceList.addOnItemTouchListener(object :
//            RecyclerItemClickListener(this@AdvanceDetailsListActivity,
//                object : OnItemClickListener {
//                    override fun onItemClick(view: View?, position: Int) {
//                        val item = advRes[position]
//                        if (item.ActualExpenseStatus != null && item.ActualExpenseStatus.equals(
//                                "Approved",
//                                ignoreCase = true
//                            )
//                        ) {
//                            Utilities.showAlertDialog(
//                                this@AdvanceDetailsListActivity,
//                                "Warning",
//                                "This bill already approved",
//                                false
//                            )
//
//                        } else {
//                            this@AdvanceDetailsListActivity.startActivity(
//                                Intent(
//                                    this@AdvanceDetailsListActivity,
//                                    SubmitBillDetailsActivity::class.java
//                                ).putExtra(
//                                    "advanceDetails",
//                                    item
//                                ).putExtra("fromDate", fromDate).putExtra("toDate", toDate)
//                            )
//
//                        }
//                    }
//                }) {
//
//        })
        binding.edtFromDate!!.setOnClickListener {
            val dpd = DatePickerDialog(
                this@AdvanceDetailsListActivity,
                { view, year, monthOfYear, dayOfMonth ->
                    fromDate = ("$year-"
                            + DecimalFormat("00")
                        .format((monthOfYear + 1).toLong())
                            + "-" + DecimalFormat("00")
                        .format(dayOfMonth.toLong()))
                    binding.edtFromDate!!.setText(fromDate)

                    toDate = ""
                    binding.edtToDate!!.setText(toDate)
                }, mYear, mMonth, mDay
            )
            dpd.datePicker.calendarViewShown = false
            dpd.datePicker.maxDate = System.currentTimeMillis()
            dpd.show()
        }


        binding.edtToDate!!.setOnClickListener {
            val f = SimpleDateFormat("yyyy-MM-dd")
            var milliseconds = System.currentTimeMillis()
            if (!fromDate!!.isEmpty()) {
                try {
                    val d = f.parse(fromDate)
                    milliseconds = d.time
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
            val dpd = DatePickerDialog(
                this@AdvanceDetailsListActivity,
                { view, year, monthOfYear, dayOfMonth ->
                    toDate = (year.toString() + "-"
                            + DecimalFormat("00").format((monthOfYear + 1).toLong())
                            + "-" + DecimalFormat("00").format(dayOfMonth.toLong()))
                    binding.edtToDate!!.setText(toDate)
                    getAdvanceDetails(fromDate, toDate)
                }, mYear, mMonth, mDay
            )
            dpd.datePicker.minDate = milliseconds
            dpd.datePicker.calendarViewShown = false
            dpd.show()
        }

    }

    fun getDistrict() {
        apiInterface.district.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    var districtList = ArrayList<DistrictList_Model?>()
                    val pojoDetails: DistrictList_Pojo = Gson().fromJson<DistrictList_Pojo>(
                        response.body()?.string(),
                        DistrictList_Pojo::class.java
                    )
                    val type = pojoDetails.status
                    val message = pojoDetails.message
                    if (type.equals("success", ignoreCase = true)) {
                        districtList = pojoDetails.output
                        if (districtList.size > 0) {
                            districtList.add(0, DistrictList_Model("0", "All"))
                            showDistrictListDialog(districtList)
                        } else {
                            Utilities.showToastMessage(
                                R.string.msgt_emptylist,
                                this@AdvanceDetailsListActivity,
                                false
                            )
                        }
                    } else {
                        Utilities.showAlertDialog(
                            this@AdvanceDetailsListActivity,
                            "Fail",
                            message,
                            false
                        )
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Utilities.showAlertDialog(
                    this@AdvanceDetailsListActivity,
                    "Fail",
                    t.message,
                    false
                )
            }

        })
    }

    private fun showDistrictListDialog(districtList: ArrayList<DistrictList_Model?>) {
        val builderSingle =
            AlertDialog.Builder(this@AdvanceDetailsListActivity, R.style.CustomDialogTheme)
        builderSingle.setTitle("Select District")
        builderSingle.setCancelable(false)
        val arrayAdapter = ArrayAdapter<String>(this@AdvanceDetailsListActivity, R.layout.list_row)
        for (i in districtList.indices) {
            arrayAdapter.add(districtList[i]?.distname.toString())
        }
        builderSingle.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.dismiss() }
        builderSingle.setAdapter(
            arrayAdapter
        ) { dialog, which ->
            binding.edtSelectdistrict.setText(districtList[which]?.distname ?: "")
            districtId = districtList[which]?.distlgdcode
            getAdvanceDetails(fromDate, toDate)

        }
        builderSingle.show()
    }

    private fun getAdvanceDetails(fromDate: String?, toDate: String?) {
        progressDialog.setMessage("Getting Requested Advance")
        progressDialog.setCancelable(false)
        progressDialog.show()
        binding.tvError.visibility = View.GONE

        apiInterface.getAdvanceDetails(fromDate, toDate, districtId!!.toInt(),sessionManager.userDetailsJson.empCode)
            .enqueue(object : Callback<AdvanceDetailsResponseModel> {
                override fun onResponse(
                    call: Call<AdvanceDetailsResponseModel>,
                    response: Response<AdvanceDetailsResponseModel>
                ) {

                    progressDialog.dismiss()

                    try {
                        if (response.isSuccessful) {
                            val res = response.body()

                            if (res?.status.equals("success", true)) {
                                if (res!!.output.isNotEmpty()) {
                                    advRes = res.output
                                    binding.tvError.visibility = View.GONE

                                    binding.rvAdvanceList.setHasFixedSize(true)
                                    binding.rvAdvanceList.layoutManager =
                                        LinearLayoutManager(this@AdvanceDetailsListActivity)
                                    binding.rvAdvanceList.adapter =
                                        AdvanceRequestListAdapter(res.output, fromDate!!, toDate!!)
                                }
                            } else {
                                binding.tvError.visibility = View.VISIBLE
                                binding.rvAdvanceList.adapter =
                                    AdvanceRequestListAdapter(ArrayList(), fromDate!!, toDate!!)
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<AdvanceDetailsResponseModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.e(TAG, "onFailure: ${t.message}")
                    binding.tvError.visibility = View.VISIBLE

                }
            })
    }

    private fun getAdvanceDetailsRefresh(fromDate: String?, toDate: String?) {

        binding.tvError.visibility = View.GONE

        apiInterface.getAdvanceDetails(fromDate, toDate, sessionManager.userDetailsJson.distlgdcode,sessionManager.userDetailsJson.empCode)
            .enqueue(object : Callback<AdvanceDetailsResponseModel> {
                override fun onResponse(
                    call: Call<AdvanceDetailsResponseModel>,
                    response: Response<AdvanceDetailsResponseModel>
                ) {


                    try {
                        if (response.isSuccessful) {
                            val res = response.body()

                            if (res?.status.equals("success", true)) {
                                if (res!!.output.isNotEmpty()) {
                                    advRes = res.output
                                    binding.tvError.visibility = View.GONE

                                    binding.rvAdvanceList.setHasFixedSize(true)
                                    binding.rvAdvanceList.layoutManager =
                                        LinearLayoutManager(this@AdvanceDetailsListActivity)
                                    binding.rvAdvanceList.adapter =
                                        AdvanceRequestListAdapter(res.output, fromDate!!, toDate!!)
                                }
                            } else {
                                binding.tvError.visibility = View.VISIBLE
                                binding.rvAdvanceList.adapter =
                                    AdvanceRequestListAdapter(ArrayList(), fromDate!!, toDate!!)
                            }


                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }

                }

                override fun onFailure(call: Call<AdvanceDetailsResponseModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                    binding.tvError.visibility = View.VISIBLE

                }
            })
    }


}