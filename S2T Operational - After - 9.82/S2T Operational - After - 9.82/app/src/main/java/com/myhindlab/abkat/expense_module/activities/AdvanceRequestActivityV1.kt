package com.myhindlab.abkat.expense_module.activities

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.myhindlab.abkat.R
import com.myhindlab.abkat.databinding.ActivityAdvanceRequestBinding
import com.myhindlab.abkat.expense_module.adapters.ExpenseFileListAdapter
import com.myhindlab.abkat.expense_module.models.ExpenseFileListModel
import com.myhindlab.abkat.rest.ApiClient
import com.myhindlab.abkat.rest.ApiInterface
import com.myhindlab.abkat.utilities.PermissionUtil
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities


import com.myhindlab.abkat.expense_module.models.expense_head.ExpenseHeadListModel
import com.myhindlab.abkat.expense_module.models.expense_head.Output
import com.myhindlab.abkat.expense_module.models.expense_head.sub_expense.SubExpenseHeadResponseModel
import com.myhindlab.abkat.models.CampCalendarModel
import okhttp3.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.Date
import com.myhindlab.abkat.expense_module.models.expense_head.sub_expense.Output as SubExpenseOutput

class AdvanceRequestActivityV1 : AppCompatActivity(), ExpenseFileListAdapter.ExpenseFileListEvents {
    lateinit var binding: ActivityAdvanceRequestBinding
    lateinit var uploadInterface: ApiInterface
    lateinit var apiInterface: ApiInterface
    lateinit var sessionManager: UserSessionManager
    var selectedExpenseHead: Int = 0
    var selectedSubExpenseHead: Int = 0
    val TAG: String = AdvanceRequestActivityV1::class.java.simpleName
    lateinit var progressDialog: ProgressDialog
    private var fileIndex = 0
    private val CAMERA_REQUEST = 100
    private var fileUri: Uri? = null
    private var picsFolder: File? = null
    private var filePath: String? = null
    private var filename: String? = null
    private var expenseFileListModels: ArrayList<ExpenseFileListModel>? = null
    private var isFileUploading = false
    var total: Int = 0
    lateinit var campDetails: CampCalendarModel.OutputBean
    private lateinit var selectedSubExpense: SubExpenseOutput


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvanceRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setupToolbar()
        eventListener()
    }

    fun init() {
        if (intent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                campDetails =
                    (intent.getSerializableExtra(
                        "campDetails"
                    ) as CampCalendarModel.OutputBean?)!!
            } else {
                campDetails =
                    intent.getSerializableExtra("campDetails") as CampCalendarModel.OutputBean
            }
        }

        val diff: Long =
            Utilities.dfDate.parse(Utilities.dfDate.format(Date(System.currentTimeMillis()).time)).time - Utilities.dfDate.parse(
                campDetails.campDate
            ).time

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        if (days.toInt() < 0 && days.toInt() <= -10) {
            binding.btnSave.visibility = View.VISIBLE
            binding.tvMsg.visibility = View.GONE
        } else {
            binding.btnSave.visibility = View.GONE
            binding.tvMsg.visibility = View.VISIBLE
        }

        if (campDetails.expectedbeneficiarycount == null) {
            binding.CWCountEditText.setText("0")

        } else {
            binding.CWCountEditText.setText(campDetails.expectedbeneficiarycount!!.toString());

        }
        apiInterface = ApiClient.getD2DClient().create(ApiInterface::class.java)
      //  uploadInterface = ApiClient.getD2DClient().create(ApiInterface::class.java)
        uploadInterface = ApiClient.getUploadClient().create(ApiInterface::class.java)
        progressDialog = ProgressDialog(this@AdvanceRequestActivityV1)
        expenseFileListModels = ArrayList<ExpenseFileListModel>()

        PermissionUtil.askPermissions(this)
        sessionManager = UserSessionManager(this@AdvanceRequestActivityV1)

        if (VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            picsFolder = externalCacheDir
        } else {
            picsFolder = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/CSC Healthcare/" + "/Post Camp Photos/"
            )
            if (!picsFolder!!.exists()) picsFolder!!.mkdirs()
        }


    }


    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Request Advance"

        toolbar.setNavigationIcon(R.drawable.icon_arrowback)
        toolbar.setNavigationOnClickListener { finish() }
    }

    private fun eventListener() {
        binding.amountPerUnitEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.edtSelSubExpenseHead.text?.isEmpty() == true) {
                    binding.edtSelSubExpenseHead.error = "Please select Sub Expense Head"
                    return
                }
                if (binding.unitEditText.text.toString()
                        .isNotEmpty()
                ) {
                    if (binding.amountPerUnitEditText.text.toString().isNotEmpty()) {
                        binding.amountPerUnitInputLayout.isErrorEnabled = false
                        val count = binding.unitEditText.text.toString().toInt()
                        total = binding.amountPerUnitEditText.text.toString().toInt() * count
                        if (total > selectedSubExpense.MaxAllowedAmt!!) {
                            binding.CWTotalInputLayout.error =
                                "Total Amount should not be grater than Max Allowed Amount"
                            binding.CWTotalInputLayout.isErrorEnabled = true
                            binding.btnSave.isEnabled = false
                        } else {
                            binding.CWTotalInputLayout.isErrorEnabled = false
                            binding.CWTotalInputLayout.error = null
                            binding.btnSave.isEnabled = true
                        }
                        binding.CWTotalEditText.setText(total.toString())
                    } else {
                        binding.CWTotalEditText.setText("")
                        binding.CWTotalInputLayout.isErrorEnabled = false
                        binding.CWTotalInputLayout.error = null
                        binding.btnSave.isEnabled = false
                    }
                } else {
                    binding.CWTotalEditText.setText("")
                    binding.CWTotalInputLayout.isErrorEnabled = false
                    binding.CWTotalInputLayout.error = null
                    binding.btnSave.isEnabled = false

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
//        binding.CWTotalEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (binding.CWTotalEditText.text.toString().isNotEmpty()
//                ) {
//                    binding.CWTotalInputLayout.isErrorEnabled = false
//                    binding.CWTotalInputLayout.error = null
//                }
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//
//            }
//
//        })
        binding.unitEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.unitEditText.text.toString().isNotEmpty()
                ) {
                    binding.unitInputLayout.isErrorEnabled = false
                    binding.unitInputLayout.error = null
                    binding.amountPerUnitEditText.setText("")
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.edtSelExpenseHead.setOnClickListener {
            getExpenseHeads()
        }
        binding.edtSelSubExpenseHead.setOnClickListener {
            if (selectedExpenseHead == 0) {
                binding.selExpenseHeadInput.error = "Please select Expense Head"
                return@setOnClickListener
            }
            getSubExpenseHeads()
        }

        binding.btnSave.setOnClickListener(View.OnClickListener {
            if (binding.edtSelExpenseHead.text.toString().isEmpty()) {
                binding.edtSelExpenseHead.setError("Please choose Expense Head")
                return@OnClickListener
            }

            if (binding.edtSelSubExpenseHead.text.toString().isEmpty()) {
                binding.edtSelSubExpenseHead.setError("Please choose Sub Expense Head")
                return@OnClickListener
            }
            if (binding.unitEditText.text.toString().isEmpty()) {
                binding.unitInputLayout.error = "Please enter No Of Units Required"
                binding.unitInputLayout.isErrorEnabled
                return@OnClickListener
            }
            if (binding.amountPerUnitEditText.text.toString().isEmpty()) {
                binding.amountPerUnitInputLayout.error = "Please enter Amount Per Unit"
                binding.amountPerUnitInputLayout.isErrorEnabled
                return@OnClickListener
            }

            if (binding.CWTotalEditText.text.toString().isEmpty()) {
                binding.CWTotalInputLayout.error = "Please enter Total Amount"
                binding.CWTotalInputLayout.isErrorEnabled
                return@OnClickListener
            }
            if (total > selectedSubExpense.MaxAllowedAmt!!) {

                binding.CWTotalInputLayout.error =
                    "Total Amount should not be grater than Max Allowed Amount"
                binding.CWTotalInputLayout.isErrorEnabled
                return@OnClickListener
            }



            saveBillDetails()
        })

    }


    @Throws(FileNotFoundException::class, IOException::class)
    fun getThumbnail(uri: Uri?): Bitmap? {
        var input = contentResolver.openInputStream(uri!!)
        val onlyBoundsOptions = BitmapFactory.Options()
        onlyBoundsOptions.inJustDecodeBounds = true
        onlyBoundsOptions.inDither = true //optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
        input!!.close()
        if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
            return null
        }
        val originalSize =
            if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) onlyBoundsOptions.outHeight else onlyBoundsOptions.outWidth
        val ratio = if (originalSize > 1080) (originalSize / 1080).toDouble() else 1.0
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inSampleSize =
            getPowerOfTwoForSampleRatio(ratio)
        bitmapOptions.inDither = true //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //
        input = this.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
        input!!.close()
        return bitmap
    }

    private fun getPowerOfTwoForSampleRatio(ratio: Double): Int {
        val k = Integer.highestOneBit(Math.floor(ratio).toInt())
        return if (k == 0) 1 else k
    }


    fun setupRecyclerView() {
        binding.rvFileList.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.rvFileList.setHasFixedSize(false)
        binding.rvFileList.layoutManager = LinearLayoutManager(this@AdvanceRequestActivityV1)
        binding.rvFileList.adapter =
            ExpenseFileListAdapter(expenseFileListModels, this@AdvanceRequestActivityV1)
    }

    override fun onDelete(postCampFileListModel: ExpenseFileListModel?, pos: Int) {
        expenseFileListModels?.remove(postCampFileListModel)
        if (expenseFileListModels?.isEmpty() == true) {
            binding.rvFileList.visibility = View.GONE
            binding.tvError.visibility = View.VISIBLE
        }
    }

    private fun getExpenseHeads() {
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        apiInterface.expensesMasterData.enqueue(object : Callback<ExpenseHeadListModel> {
            override fun onResponse(
                call: Call<ExpenseHeadListModel>,
                response: Response<ExpenseHeadListModel>
            ) {
                progressDialog.dismiss()
                try {
                    if (response.isSuccessful) {
                        showListDialog(response.body()!!.output)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ExpenseHeadListModel>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                progressDialog.dismiss()
            }
        })
    }

    private fun getSubExpenseHeads() {
        progressDialog.setMessage("Please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        apiInterface.getSubExpensesMasterData(
            selectedExpenseHead,
            campDetails.campId.toInt(),
            campDetails.registerworkers.toInt()
        )
            .enqueue(object : Callback<SubExpenseHeadResponseModel> {
                override fun onResponse(
                    call: Call<SubExpenseHeadResponseModel>,
                    response: Response<SubExpenseHeadResponseModel>
                ) {
                    progressDialog.dismiss()
                    try {
                        if (response.isSuccessful) {
                            showSubExpenseListDialog(response.body()!!.output)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<SubExpenseHeadResponseModel>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                    progressDialog.dismiss()
                }
            })
    }


    private fun showListDialog(expenseHeadListModel: ArrayList<Output>) {
        val builderSingle =
            AlertDialog.Builder(this@AdvanceRequestActivityV1, R.style.CustomDialogTheme)
        builderSingle.setTitle("Select Expense Head")
        builderSingle.setCancelable(false)
        val arrayAdapter = ArrayAdapter<String>(this@AdvanceRequestActivityV1, R.layout.list_row)
        for (heads in expenseHeadListModel) {
            arrayAdapter.add(heads.ExpenseName)
        }
        builderSingle.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }

        builderSingle.setAdapter(arrayAdapter) { dialog: DialogInterface?, which: Int ->
            selectedExpenseHead = expenseHeadListModel[which].ExpenseID!!
            binding.edtSelExpenseHead.setText(expenseHeadListModel[which].ExpenseName)
            binding.tvExpenseTitle.setText(expenseHeadListModel[which].ExpenseName)
            binding.edtSelExpenseHead.setError(null);
            binding.selExpenseHeadInput.error = null
            resetView()

        }
        builderSingle.show()
    }

    private fun showSubExpenseListDialog(expenseHeadListModel: ArrayList<SubExpenseOutput>) {
        val builderSingle =
            AlertDialog.Builder(this@AdvanceRequestActivityV1, R.style.CustomDialogTheme)
        builderSingle.setTitle("Select Sub Expense Head")
        builderSingle.setCancelable(false)
        val arrayAdapter = ArrayAdapter<String>(this@AdvanceRequestActivityV1, R.layout.list_row)
        for (heads in expenseHeadListModel) {
            arrayAdapter.add(heads.SubexpenseName)
        }
        builderSingle.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }

        builderSingle.setAdapter(arrayAdapter) { dialog: DialogInterface?, which: Int ->
            selectedSubExpenseHead = expenseHeadListModel[which].subExpenseID!!
            selectedSubExpense = expenseHeadListModel[which]
            binding.edtSelSubExpenseHead.setText(expenseHeadListModel[which].SubexpenseName)
            binding.selExpenseHeadInput.setError(null);
            binding.edtSelSubExpenseHead.setError(null);

            binding.tvUnit.setText(expenseHeadListModel[which].Unit)
            binding.tvMaxAllowedAmount.setText(expenseHeadListModel[which].MaxAllowedAmt.toString())

            when (selectedSubExpense.subExpenseID) {
                1 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(1))
                }

                2 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                3 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                4 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                5 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                6 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(3))
                }

                7 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                8 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                9 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                10 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                11 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                12 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                13 -> {
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }
            }


            resetViewAfterSubHeadSelection()


        }
        builderSingle.show()
    }

    private fun saveBillDetails() {

        progressDialog.setMessage("Submitting advance details")
        progressDialog.setCancelable(false)
        progressDialog.show()
        campDetails.campId?.let {
            apiInterface.insertAdvancesRequestNewChanges(
                it.toInt(),
                selectedSubExpenseHead,
                binding.CWTotalEditText.text.toString().toInt(),
                sessionManager.userDetailsJson.empCode,
                1,
                0,
                1,
                binding.CWDescriptionEditText.text.toString(),
                binding.unitEditText.text.toString().toInt(),
                binding.amountPerUnitEditText.text.toString()
            ).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    progressDialog.dismiss()
                    try {
                        if (response.isSuccessful) {
                            val res = response.body()?.string()
                            val json = JSONObject(res)
                            val status = json.getString("status")
                            val msg = json.getString("message")
                            if (status.equals("success", ignoreCase = true)) {
                                LocalBroadcastManager.getInstance(this@AdvanceRequestActivityV1)
                                    .sendBroadcast(
                                        Intent("refreshCampList")
                                    )
                                resetView()
                                Utilities.showAlertDialog(
                                    this@AdvanceRequestActivityV1,
                                    status,
                                    "Advance Request submitted successfully",
                                    true,
                                    "Okay",
                                    object : DialogInterface.OnClickListener {
                                        override fun onClick(p0: DialogInterface, p1: Int) {
//                                            finish()
                                            p0.dismiss()
                                        }

                                    })

                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "onFailure: ${e.message}")

                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    progressDialog.dismiss()

                    Log.e(TAG, "onFailure: ${t.message}")
                }

            })
        }
    }

    private fun resetView() {
        binding.unitEditText.setText("")
        binding.amountPerUnitEditText.setText("")
        binding.CWTotalEditText.setText("")
        binding.edtSelSubExpenseHead.setText("")
        binding.tvExpenseApprovedAmount.setText("")
        binding.tvMaxAllowedAmount.setText("")
        binding.tvUnit.setText("")
        binding.CWDescriptionEditText.setText("")

    }

    private fun resetViewAfterSubHeadSelection() {
        binding.unitEditText.setText("")
        binding.amountPerUnitEditText.setText("")
        binding.CWTotalEditText.setText("")
        binding.tvExpenseApprovedAmount.setText("")
        binding.CWDescriptionEditText.setText("")

    }

}