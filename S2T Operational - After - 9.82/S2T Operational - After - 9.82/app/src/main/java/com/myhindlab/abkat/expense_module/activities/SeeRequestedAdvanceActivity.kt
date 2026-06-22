package com.myhindlab.abkat.expense_module.activities

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myhindlab.abkat.R
import com.myhindlab.abkat.databinding.ActivitySeeRequestedAdvanceBinding
import com.myhindlab.abkat.expense_module.models.see_requested_advance_detail.SeeAdvanceRequestResponseModel
import com.myhindlab.abkat.rest.ApiClient
import com.myhindlab.abkat.rest.ApiInterface
import com.myhindlab.abkat.utilities.Utilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SeeRequestedAdvanceActivity : AppCompatActivity() {
    lateinit var binding: ActivitySeeRequestedAdvanceBinding
    lateinit var apiInterface: ApiInterface
    lateinit var progressDialog: ProgressDialog
    var screenType: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeRequestedAdvanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setupToolbar()
    }


    fun init() {
        apiInterface = ApiClient.getD2DClient().create(ApiInterface::class.java)
        progressDialog = ProgressDialog(this@SeeRequestedAdvanceActivity)

        screenType = intent.getIntExtra("screenType", 1)

        if (screenType == 1) {
            binding.edtTotalBillSubmittedAmount.hint = "Total advance submitted"
            if (intent.getStringExtra("campId") != null) {
                getRequestedAdvance(intent.getStringExtra("campId")!!)

            }

        } else {
            if (intent.getStringExtra("campId") != null) {
                getBillSubmitdetailsShow(intent.getStringExtra("campId")!!)

            }

        }


    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        if (screenType == 1) {
            supportActionBar!!.title = "Requested Advance Details"
        } else {
            supportActionBar!!.title = "Requested Bill Details"

        }
        toolbar.setNavigationIcon(R.drawable.icon_arrowback)
        toolbar.setNavigationOnClickListener { finish() }
    }


    fun getRequestedAdvance(campId: String) {
        progressDialog.setMessage("Getting Details...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        apiInterface.getAdvaDemandeddetailsShow(campId)
            .enqueue(object : Callback<SeeAdvanceRequestResponseModel> {
                override fun onResponse(
                    call: Call<SeeAdvanceRequestResponseModel>,
                    response: Response<SeeAdvanceRequestResponseModel>
                ) {

                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        if (response.body()?.status.equals("success", ignoreCase = true)) {
                            setData(response.body())

                        } else {
                            Utilities.showAlertDialogRequired(
                                this@SeeRequestedAdvanceActivity,
                                "Failed",
                                "Details not available",
                                false, "Okay", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        finish()
                                    }
                                }
                            )
                        }
                    }

                }

                override fun onFailure(call: Call<SeeAdvanceRequestResponseModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Utilities.showAlertDialog(
                        this@SeeRequestedAdvanceActivity,
                        "Error",
                        "Unable to get Advance details",
                        false
                    )
                }

            })
    }

    fun getBillSubmitdetailsShow(campId: String) {
        progressDialog.setMessage("Getting Details...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        apiInterface.getBillSubmitdetailsShow(campId)
            .enqueue(object : Callback<SeeAdvanceRequestResponseModel> {
                override fun onResponse(
                    call: Call<SeeAdvanceRequestResponseModel>,
                    response: Response<SeeAdvanceRequestResponseModel>
                ) {

                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        if (response.body()?.status.equals("success", ignoreCase = true)) {
                            setData(response.body())

                        } else {
                            Utilities.showAlertDialogRequired(
                                this@SeeRequestedAdvanceActivity,
                                "Failed",
                                "Details not available",
                                false, "Okay", object : DialogInterface.OnClickListener {
                                    override fun onClick(p0: DialogInterface?, p1: Int) {
                                        finish()
                                    }
                                }
                            )
                        }
                    }

                }

                override fun onFailure(call: Call<SeeAdvanceRequestResponseModel>, t: Throwable) {
                    progressDialog.dismiss()
                    Utilities.showAlertDialog(
                        this@SeeRequestedAdvanceActivity,
                        "Error",
                        "Unable to get Advance details",
                        false
                    )
                }

            })
    }

    fun setData(data: SeeAdvanceRequestResponseModel?) {

        if (data != null) {
            binding.edtCampId.setText(data.output[0].campid!!.toString())
            binding.edtDistrict.setText(data.output[0].DISTNAME!!)
            binding.edtCampDate.setText(data.output[0].CampDate!!)
            binding.edtFundRequestedBy.setText(data.output[0].FundRequestedBy!!)
            data.output[0].Expectedbeneficiarycount?.let { binding.edtExpectedBeneficiary.setText(it.toString()) }
            data.output[0].TotalAdvanceTaken?.let { binding.edtAdvTaken.setText("Rs. $it") }
            data.output[0].Beneficiaryrefreshment?.let {
                binding.edtBeneficiaryRefreshment.setText(
                    "Rs. $it"
                )
            }
            data.output[0].CampAwarenessusingBhopu?.let { binding.edtCampAwarenessBhopu.setText("Rs. $it") }
            data.output[0].CampHallGramPanchayatSchoolGovtOfficeTent?.let {
                binding.edtCampHall.setText(
                    "Rs. $it"
                )
            }
            data.output[0].Chairs?.let { binding.edtChairs.setText("Rs. $it") }
            data.output[0].CleaningCharges?.let { binding.edtCleaningCharges.setText("Rs. $it") }
            data.output[0].DrinkingWater?.let { binding.edtDrinkingWater.setText("Rs. $it") }
            data.output[0].FoodtoStaffTAAllowance?.let { binding.edtFoodToStaffAllowance.setText("Rs. $it") }
            data.output[0].PostCampExpense?.let { binding.edtPostCampExpense.setText("Rs. $it") }
            data.output[0].SamplemovementtolabRunnerboy?.let {
                binding.edtSampleMovementToLabRunnerBoy.setText(
                    "Rs. $it"
                )
            }
            data.output[0].SamplemovementtolabTSRTCoranyotherCargo?.let {
                binding.edtSampleMovementToLabTSRTC.setText(
                    "Rs. $it"
                )
            }
            data.output[0].Tables?.let { binding.edtTable.setText("Rs. $it") }
            data.output[0].TransportationofStaffTAIndividual?.let {
                binding.edtTransportOfStaffIndividual.setText(
                    "Rs. $it"
                )
            }
            data.output[0].TransportationofStaffTAGroupofTransport?.let {
                binding.edtTransportationOfStaffGroupOfTransport.setText(
                    "Rs. $it"
                )
            }
            if (screenType == 1) {
                data.output[0].TotalRequestedExpense?.let {
                    binding.edtTotalBillSubmittedAmount.setText(
                        "Rs. $it"
                    )
                }
            } else {
                data.output[0].TotalBillSubmittedAmt?.let {
                    binding.edtTotalBillSubmittedAmount.setText(
                        "Rs. $it"
                    )
                }

            }
            binding.edtApprovalStatus.setText(data.output[0].ApprovalStatus!!)
        }
    }
}