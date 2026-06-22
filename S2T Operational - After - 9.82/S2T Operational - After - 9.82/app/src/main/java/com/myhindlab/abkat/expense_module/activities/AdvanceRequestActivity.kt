package com.myhindlab.abkat.expense_module.activities

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.myhindlab.abkat.R
import com.myhindlab.abkat.databinding.ActivityClaimBinding
import com.myhindlab.abkat.models.CampCalendarModel
import com.myhindlab.abkat.rest.ApiClient
import com.myhindlab.abkat.rest.ApiInterface
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities
import com.myhindlab.abkat.models.CampCalendarModel.OutputBean
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class AdvanceRequestActivity : AppCompatActivity() {
    lateinit var binding: ActivityClaimBinding
    lateinit var campDetails: CampCalendarModel.OutputBean
    lateinit var sessionManager: UserSessionManager
    lateinit var apiInterface: ApiInterface
    lateinit var progressDialog: ProgressDialog
    private var finalTotal = 0
    var cwRefTotal = "0"
    var courierTotal = "0"
    var teamTransportTotal = "0"
    var otherMiscTotal = "0"
    val TAG: String = AdvanceRequestActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setupToolbar()
        setDefault()
        eventListener()
    }

    fun init() {
        sessionManager = UserSessionManager(this@AdvanceRequestActivity)
        progressDialog = ProgressDialog(this@AdvanceRequestActivity)
        apiInterface = ApiClient.getD2DClient().create(ApiInterface::class.java)
    }

    private fun eventListener() {
        binding.CWRefCountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {

                if (chars.toString().isNotEmpty()) {
                    binding.CWRefCountInputLayout.isErrorEnabled = false
                } else {
                    binding.CWRefCountInputLayout.error = "Please enter CW Count"
                    binding.CWRefCountInputLayout.isErrorEnabled
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.CWRefAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {

                if (chars.toString().isNotEmpty()) {
                    binding.CWRefAmountInputLayout.isErrorEnabled = false
                } else {
                    binding.CWRefAmountInputLayout.error = "Please enter CW Amount"
                    binding.CWRefAmountInputLayout.isErrorEnabled
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.CWRefTotalEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {

                if (chars.toString().isNotEmpty()) {
                    binding.CWRefTotalInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.CourierCountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {

                if (chars.toString().isNotEmpty()) {
                    binding.CourierCountInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.CourierAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {

                if (chars.toString().isNotEmpty()) {
                    binding.CourierAmountInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.CourierTotalEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {

                if (chars.toString().isNotEmpty()) {
                    binding.CourierTotalInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.TeamTransportCountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {

                if (chars.toString().isNotEmpty()) {
                    binding.TeamTransportCountInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.TeamTransportAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {

                if (chars.toString().isNotEmpty()) {
                    binding.TeamTransportAmountInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.TeamTransportTotalEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {

                if (chars.toString().isNotEmpty()) {
                    binding.TeamTransportTotalInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.CWRefTotalEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                finalTotal = if (p0.toString().isNotEmpty()) {
                    cwRefTotal = p0.toString()
                    cwRefTotal.toInt() + courierTotal.toInt() + teamTransportTotal.toInt() + otherMiscTotal.toInt();
                } else {
                    cwRefTotal = "0"
                    cwRefTotal.toInt() + courierTotal.toInt() + teamTransportTotal.toInt() + otherMiscTotal.toInt();

                }

                binding.FinalTotalEditText.setText(finalTotal.toString())


            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.CourierTotalEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {
                finalTotal = if (chars.toString().isNotEmpty()) {
                    courierTotal = chars.toString()

                    cwRefTotal.toInt() + courierTotal.toInt() + teamTransportTotal.toInt() + otherMiscTotal.toInt();
                } else {
                    courierTotal = "0"

                    cwRefTotal.toInt() + courierTotal.toInt() + teamTransportTotal.toInt() + otherMiscTotal.toInt();

                }
                binding.FinalTotalEditText.setText(finalTotal.toString())

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.TeamTransportTotalEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {
                finalTotal = if (chars.toString().isNotEmpty()) {
                    teamTransportTotal = chars.toString()

                    cwRefTotal.toInt() + courierTotal.toInt() + teamTransportTotal.toInt() + otherMiscTotal.toInt();
                } else {
                    teamTransportTotal = "0"
                    cwRefTotal.toInt() + courierTotal.toInt() + teamTransportTotal.toInt() + otherMiscTotal.toInt();

                }

                binding.FinalTotalEditText.setText(finalTotal.toString())

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        binding.OtherMiscTotalEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(chars: CharSequence?, start: Int, before: Int, count: Int) {
                finalTotal = if (chars.toString().isNotEmpty()) {
                    otherMiscTotal = chars.toString()

                    cwRefTotal.toInt() + courierTotal.toInt() + teamTransportTotal.toInt() + otherMiscTotal.toInt();
                } else {
                    otherMiscTotal = "0"

                    cwRefTotal.toInt() + courierTotal.toInt() + teamTransportTotal.toInt() + otherMiscTotal.toInt()

                }
                binding.FinalTotalEditText.setText(finalTotal.toString())


            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.btnSave.setOnClickListener {
            insertAdvancesRequest()
        }
    }

    fun setDefault() {

        if (intent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                campDetails =
                    (intent.getSerializableExtra(
                        "campDetails"
                    ) as OutputBean?)!!
            } else {
                campDetails =
                    intent.getSerializableExtra("campDetails") as OutputBean
            }
        }

        binding.edtCampOrganizedBy.setText(campDetails.organizedby)
        binding.edtDistrict.setText(campDetails.distname)
        binding.edtCampName.setText(campDetails.campName)
        binding.edtCampAddress.setText(campDetails.campLocation)
        binding.edtCampDate.setText(campDetails.campDate)
        binding.edtExpectedBeneficiary.setText(campDetails.expectedbeneficiarycount)
        binding.CWRefCountEditText.setText(campDetails.expectedbeneficiarycount)
        binding.CourierCountEditText.setText(campDetails.expectedbeneficiarycount)
//        binding.TeamTransportCountEditText.setText(campDetails.expectedbeneficiarycount)
        val postCampDate = Utilities.dfDate.parse(campDetails.campDate)
        val cal = Calendar.getInstance()
        if (postCampDate != null) {
            cal.time = postCampDate
        }
        cal.add(Calendar.DAY_OF_MONTH, 7)
        val postCamp = Utilities.dfDate.format(cal.time)
        binding.edtPostCampDate.setText(postCamp)
    }

    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Advance Request"

        toolbar.setNavigationIcon(R.drawable.icon_arrowback)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun calculateTotal() {
//        if (binding.CWRefCountEditText.text.toString().isEmpty()) {
//            binding.CWRefCountInputLayout.error = "Please enter CW Count"
//            binding.CWRefCountInputLayout.isErrorEnabled
//            return
//        }
//        if (binding.CWRefCountEditText.text.toString().isEmpty()) {
//            binding.CWRefCountInputLayout.error = "Please enter CW Count"
//            binding.CWRefCountInputLayout.isErrorEnabled
//            return
//        }


    }

    private fun validate(): Boolean {
        if (binding.CWRefCountEditText.text.toString().isEmpty()) {
            binding.CWRefCountInputLayout.error = "Please enter CW Count"
            binding.CWRefCountInputLayout.isErrorEnabled
            return false
        }
//        if (binding.CWRefAmountEditText.text.toString().isEmpty()) {
//            binding.CWRefAmountInputLayout.error = "Please enter Refreshment Amount"
//            binding.CWRefAmountInputLayout.isErrorEnabled
//            return false
//        }
        if (binding.CWRefTotalEditText.text.toString().isEmpty()) {
            binding.CWRefTotalInputLayout.error = "Please enter Refreshment Total"
            binding.CWRefTotalInputLayout.isErrorEnabled
            return false
        }

        if (binding.CourierCountEditText.text.toString().isEmpty()) {
            binding.CourierCountInputLayout.error = "Please enter Courier Count"
            binding.CourierCountInputLayout.isErrorEnabled
            return false
        }
//        if (binding.CourierAmountEditText.text.toString().isEmpty()) {
//            binding.CourierAmountInputLayout.error = "Please enter Courier Amount"
//            binding.CourierAmountInputLayout.isErrorEnabled
//            return false
//        }
        if (binding.CourierTotalEditText.text.toString().isEmpty()) {
            binding.CourierTotalInputLayout.error = "Please enter Courier Total"
            binding.CourierTotalInputLayout.isErrorEnabled
            return false
        }

        if (binding.TeamTransportCountEditText.text.toString().isEmpty()) {
            binding.TeamTransportCountInputLayout.error = "Please enter Team Transport Count"
            binding.TeamTransportCountInputLayout.isErrorEnabled
            return false
        }
//        if (binding.TeamTransportAmountEditText.text.toString().isEmpty()) {
//            binding.TeamTransportAmountInputLayout.error = "Please enter Team Transport Amount"
//            binding.TeamTransportAmountInputLayout.isErrorEnabled
//            return false
//        }
        if (binding.TeamTransportTotalEditText.text.toString().isEmpty()) {
            binding.TeamTransportTotalInputLayout.error = "Please enter Team Transport Total"
            binding.TeamTransportTotalInputLayout.isErrorEnabled
            return false
        }

        return true
    }

    private fun insertAdvancesRequest() {
        if (!validate()) {
            return
        }

        val cwRefCount = binding.CWRefCountEditText.text.toString()
        val cwRefAmount = binding.CWRefAmountEditText.text.toString()
        cwRefTotal = binding.CWRefTotalEditText.text.toString()
        val cwRefDescriptions = binding.CWDescriptionEditText.text.toString()

        if (cwRefTotal.isEmpty()) {
            cwRefTotal = "0";
        }

        val courierCount = binding.CourierCountEditText.text.toString()
        val courierAmount = binding.CourierAmountEditText.text.toString()
        courierTotal = binding.CourierTotalEditText.text.toString()
        val courierDescription = binding.CourierDescriptionEditText.text.toString()

        if (courierTotal.isEmpty()) {
            courierTotal = "0";
        }

        val teamTransportCount = binding.TeamTransportCountEditText.text.toString()
        val teamTransportAmount = binding.TeamTransportAmountEditText.text.toString()
        teamTransportTotal = binding.TeamTransportTotalEditText.text.toString()
        val teamTransportDescription = binding.TeamTransportDescriptionEditText.text.toString()
        if (teamTransportTotal.isEmpty()) {
            teamTransportTotal = "0";
        }


        var otherMiscCount = binding.OtherMiscCountEditText.text.toString()
        var otherMiscAmount = binding.OtherMiscAmountEditText.text.toString()
        otherMiscTotal = binding.OtherMiscTotalEditText.text.toString()
        val otherMiscDescription = binding.OtherMiscDescriptionEditText.text.toString()

        if (otherMiscAmount.isEmpty()) {
            otherMiscAmount = "0";
        }
        if (otherMiscCount.isEmpty()) {
            otherMiscCount = "0";
        }
        if (otherMiscTotal.isEmpty()) {
            otherMiscTotal = "0";
        }

        finalTotal =
            cwRefTotal.toInt() + courierTotal.toInt() + teamTransportTotal.toInt() + otherMiscTotal.toInt();


        progressDialog.setMessage("Please wait..")
        progressDialog.setCancelable(false)
        progressDialog.show()

        apiInterface.insertAdvancesRequest(
            campDetails.campId.toInt(),
            cwRefCount.toInt(),
            0,
            cwRefTotal.toInt(),
            cwRefDescriptions,
            courierCount.toInt(),
            0,
            courierTotal.toInt(),
            courierDescription,
            teamTransportCount.toInt(),
            0,
            teamTransportTotal.toInt(),
            teamTransportDescription,
            otherMiscCount.toInt(),
            0,
            otherMiscTotal.toInt(),
            otherMiscDescription,
            finalTotal,
            sessionManager.userDetailsJson.empCode
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressDialog.dismiss()
                Log.d(TAG, "onResponse:")
                val res = response.body()?.string();
                val json = JSONObject(res)
                val status = json.getString("status")
                val msg = json.getString("message")

                if (status.equals("success", true)) {
                    Utilities.showAlertDialog(
                        this@AdvanceRequestActivity,
                        "Success",
                        "Advance Submitted Successfully",
                        true, "Okay", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                finish()
                            }

                        }
                    )
                } else {
                    Utilities.showAlertDialog(
                        this@AdvanceRequestActivity,
                        "Failed",
                        "Unable to Submit Advance",
                        false
                    )

                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog.dismiss()
            }

        })
    }
}