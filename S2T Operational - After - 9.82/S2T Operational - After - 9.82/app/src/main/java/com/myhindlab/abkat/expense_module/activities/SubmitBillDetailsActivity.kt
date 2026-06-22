package com.myhindlab.abkat.expense_module.activities

import android.Manifest.permission
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView.Guidelines
import com.myhindlab.abkat.R
import com.myhindlab.abkat.databinding.ActivitySubmitBillDetailsBinding
import com.myhindlab.abkat.expense_module.adapters.ExpenseFileListAdapter
import com.myhindlab.abkat.expense_module.models.ExpenseFileListModel
import com.myhindlab.abkat.expense_module.models.expense_head.ExpenseHeadListModel
import com.myhindlab.abkat.expense_module.models.expense_head.Output
import com.myhindlab.abkat.expense_module.models.expense_head.sub_expense.SubExpenseHeadResponseModel
import com.myhindlab.abkat.models.GetInitiatedByListForCampModel
import com.myhindlab.abkat.rest.ApiClient
import com.myhindlab.abkat.rest.ApiInterface
import com.myhindlab.abkat.utilities.FileUtils
import com.myhindlab.abkat.utilities.PermissionUtil
import com.myhindlab.abkat.utilities.UserSessionManager
import com.myhindlab.abkat.utilities.Utilities
import com.myhindlab.abkat.utilities.Utilities.GALLERY_REQUEST
import com.myhindlab.abkat.utilities.Utilities.compressImage

import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.Calendar
import java.util.Date
import com.myhindlab.abkat.expense_module.models.advance_detail_response.Output as AdvanceDetails
import com.myhindlab.abkat.expense_module.models.expense_head.sub_expense.Output as SubExpenseOutput

class SubmitBillDetailsActivity : AppCompatActivity(),
    ExpenseFileListAdapter.ExpenseFileListEvents {
    lateinit var binding: ActivitySubmitBillDetailsBinding
    lateinit var uploadInterface: ApiInterface
    lateinit var apiInterface: ApiInterface
    lateinit var sessionManager: UserSessionManager
    var selectedExpenseHead: Int = 0
    var selectedSubExpenseHead: Int = 0
    var selectedSubExpenseHeadNew: Int = 0
    var organizedById: Int = 0
    var campType: Int = 0
    var isBillRequired: Boolean = true
    var isProofOfPermission: Int = 0
    var imageType: Int = 0
    var letterPicBm: Bitmap? = null
    val TAG: String = SubmitBillDetailsActivity::class.java.simpleName
    lateinit var progressDialog: ProgressDialog
    private var fileIndex = 0
    private val CAMERA_REQUEST = 100
    private var fileUri: Uri? = null
    private var picsFolder: File? = null
    private var file: File? = null
    private var filePath: String? = null
    private var filename: String? = null
    private var expenseFileListModels: ArrayList<ExpenseFileListModel>? = null
    private var isFileUploading = false
    private lateinit var advanceDetails: AdvanceDetails
    var total: Int = 0
    private lateinit var selectedSubExpense: SubExpenseOutput



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitBillDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        setupToolbar()
        eventListener()

    }

    fun init() {

        advanceDetails = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(
                "advanceDetails"
            ) as AdvanceDetails
        } else {
            intent.getSerializableExtra(
                "advanceDetails",
            ) as AdvanceDetails

        }


        organizedById = advanceDetails.InitiatedBy!!
        campType = advanceDetails.CampType!!


        if (organizedById == 1){
            binding.edtOrganizedBy.setText("Internal Team")
        }else if (organizedById == 2){
            binding.edtOrganizedBy.setText("Government")

        }else if (organizedById == 3){
            binding.edtOrganizedBy.setText("NGO")

        }else if (organizedById == 4){
            binding.edtOrganizedBy.setText("Labor Contractor")

        }else if (organizedById == 5){
            binding.edtOrganizedBy.setText("Kit Vendor")

        }else if (organizedById == 6){
            binding.edtOrganizedBy.setText("ACL Office")

        }else if (organizedById == 7){
            binding.edtOrganizedBy.setText("vendor/union Leader")

        }else if (organizedById == 8){
            binding.edtOrganizedBy.setText("Flexi camp")

        }else if (organizedById == 9){
            binding.edtOrganizedBy.setText("GramPanchyat")

        }else if (organizedById == 10){
            binding.edtOrganizedBy.setText("Self-organized")
        }

        binding.edtOrganizedBy.isEnabled = false;

        val campCal = Calendar.getInstance()
        campCal.time = Utilities.dfDate.parse(
            advanceDetails.campdate

        )


        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val crrMonth = cal.get(Calendar.MONTH)

        val campMonth = campCal.get(Calendar.MONTH)

        if (day == 5) {
            if (campMonth < crrMonth) {
                binding.btnSave.isEnabled = false
            }
        }

        campCal.add(Calendar.DAY_OF_MONTH, 1)
        val diff: Long =
            Utilities.dfDate.parse(Utilities.dfDate.format(Date(System.currentTimeMillis()).time)).time - Utilities.dfDate.parse(
                advanceDetails.campdate
            ).time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24


        if (days.toInt()  > 365) {
            binding.btnSave.visibility = View.GONE
            binding.tvMsg.visibility = View.VISIBLE
        } else {
            binding.btnSave.visibility = View.VISIBLE
            binding.tvMsg.visibility = View.GONE
        }


        if (advanceDetails.Registeredbeneficiarycount == null) {
            binding.CWCountEditText.text = "0"

        } else {
            binding.CWCountEditText.setText(advanceDetails.Registeredbeneficiarycount!!.toString());
        }
        apiInterface = ApiClient.getD2DClient().create(ApiInterface::class.java)
        uploadInterface = ApiClient.getUploadClient().create(ApiInterface::class.java)
        progressDialog = ProgressDialog(this@SubmitBillDetailsActivity)
        expenseFileListModels = ArrayList<ExpenseFileListModel>()

        PermissionUtil.askPermissions(this)
        sessionManager = UserSessionManager(this@SubmitBillDetailsActivity)

        if (VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            picsFolder = externalCacheDir
        } else {
            picsFolder = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/CSC Healthcare/" + "/Post Camp Photos/"
            )
            if (!picsFolder!!.exists()) picsFolder!!.mkdirs()
        }


//        if (advanceDetails.IsBillUploaded.equals("Yes")){
//            binding.btnSave.visibility = View.GONE
//        }else{
//            binding.btnSave.visibility = View.VISIBLE
//
//        }


        if (advanceDetails.IsSecondLevelApproval.equals("Yes")){
            binding.btnSave.visibility = View.GONE
        }else{
            binding.btnSave.visibility = View.VISIBLE
        }
    }


    private fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Bill Submission"

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
                        if (total >= selectedSubExpense.MaxAllowedAmt!!) {

                            when (selectedSubExpense.subExpenseID) {
                                1 -> {

                                    if (advanceDetails.CampHall != null && advanceDetails.CampHall!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.CampHall!!
                                    }
                                }

                                2 -> {

                                    if (advanceDetails.Chairs != null && advanceDetails.Chairs!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.Chairs!!
                                    }

                                }

                                3 -> {

                                    if (advanceDetails.Tables != null && advanceDetails.Tables!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.Tables!!
                                    }


                                }

                                4 -> {

                                    if (advanceDetails.CleaningCharges != null && advanceDetails.CleaningCharges!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.CleaningCharges!!
                                    }

                                }

                                5 -> {

                                    if (advanceDetails.DrinkingWater != null && advanceDetails.DrinkingWater!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.DrinkingWater!!
                                    }

                                }

                                6 -> {

                                    if (advanceDetails.BeneficiaryRefreshment != null && advanceDetails.BeneficiaryRefreshment!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.BeneficiaryRefreshment!!
                                    }

                                }

                                7 -> {

                                    if (advanceDetails.SampleMovementToLabTSRTCOrAnyOtherCargo != null && advanceDetails.SampleMovementToLabTSRTCOrAnyOtherCargo!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.SampleMovementToLabTSRTCOrAnyOtherCargo!!
                                    }

                                }

                                8 -> {

                                    if (advanceDetails.SampleMovementToLabRunnerBoy != null && advanceDetails.SampleMovementToLabRunnerBoy!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.SampleMovementToLabRunnerBoy!!
                                    }

                                }

                                9 -> {

                                    if (advanceDetails.CampAwarenessUsingBhopu != null && advanceDetails.CampAwarenessUsingBhopu!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.CampAwarenessUsingBhopu!!
                                    }

                                }

                                10 -> {

                                    if (advanceDetails.TransportationOfStaffTAAllowanceToStaffIndividuals != null && advanceDetails.TransportationOfStaffTAAllowanceToStaffIndividuals!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.TransportationOfStaffTAAllowanceToStaffIndividuals!!
                                    }

                                }

                                11 -> {

                                    if (advanceDetails.TransportationOfStaffTAAllowanceToStaffGroupTransportationLikeAutoTumTum != null && advanceDetails.TransportationOfStaffTAAllowanceToStaffGroupTransportationLikeAutoTumTum!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.TransportationOfStaffTAAllowanceToStaffGroupTransportationLikeAutoTumTum!!
                                    }

                                }

                                12 -> {

                                    if (advanceDetails.FoodToStaffTAAllowance != null && advanceDetails.FoodToStaffTAAllowance!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.FoodToStaffTAAllowance!!
                                    }

                                }

                                13 -> {

                                    if (advanceDetails.PostCampExpense != null && advanceDetails.PostCampExpense!! > 0) {
                                        total =
                                            selectedSubExpense.MaxAllowedAmt!! - advanceDetails.PostCampExpense!!
                                    }

                                }
                            }

                        }

                        if (total > selectedSubExpense.MaxAllowedAmt!!) {
//                            binding.CWTotalInputLayout.error =
//                                "Total Amount should not be grater than Max Allowed Amount"
                            Utilities.showAlertDialog(
                                this@SubmitBillDetailsActivity,
                                "Note",
                                "Only one file is allowed to upload as a proof of permission",
                                false
                            )

                            binding.CWTotalInputLayout.isErrorEnabled = true
//                            binding.amountPerUnitInputLayout.error =
//                                "Total Amount should not be grater than Max Allowed Amount"
                            binding.amountPerUnitInputLayout.isErrorEnabled = true
                            binding.btnSave.isEnabled = false

                            binding.btnBrowse.visibility = View.VISIBLE;
                            binding.tvFileName.visibility = View.VISIBLE

                            binding.imvPatient.visibility = View.VISIBLE
                            binding.tvFileName.visibility = View.VISIBLE


                            isProofOfPermission = 1


                        } else {
                            binding.CWTotalInputLayout.isErrorEnabled = false
                            binding.CWTotalInputLayout.error = null
                            binding.amountPerUnitInputLayout.isErrorEnabled = false
                            binding.amountPerUnitInputLayout.error = null
                            binding.btnSave.isEnabled = true

                            binding.btnBrowse.visibility = View.GONE;
                            binding.tvFileName.visibility = View.GONE

                            binding.imvPatient.visibility = View.GONE

                            binding.tvFileName.visibility = View.GONE



                            isProofOfPermission = 0

                            filePath = null
                            binding.tvFileName.text = ""




//                            if (advanceDetails.Registeredbeneficiarycount!! < 35) {
//                                binding.btnSave.isEnabled = selectedSubExpense.subExpenseID == 6
//                            } else {
//                                binding.btnSave.isEnabled = true
//                            }

                            /** Disable Previous Month Camps On 5th day of next month */
                            val campCal = Calendar.getInstance()
                            campCal.time = Utilities.dfDate.parse(
                                advanceDetails.campdate
                            )

                            val cal = Calendar.getInstance()
                            val day = cal.get(Calendar.DAY_OF_MONTH)
                            val crrMonth = cal.get(Calendar.MONTH)

                            val campMonth = campCal.get(Calendar.MONTH)

                            if (day == 5) {
                                if (campMonth < crrMonth) {
//                                    binding.btnSave.isEnabled = false
                                }
                            }
                            /** end */


                        }
                        binding.CWTotalEditText.setText(total.toString().replace("-", ""))
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
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
//        binding.CWAmountEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (binding.CWCountEditText.text.toString()
//                        .isNotEmpty() && binding.CWAmountEditText.text.toString().isNotEmpty()
//                ) {
//                    binding.CWAmountInputLayout.isErrorEnabled = false
//                    val count = binding.CWCountEditText.text.toString().toInt()
//                    total = binding.CWAmountEditText.text.toString().toInt() * count
//                    binding.CWTotalEditText.setText(total.toString())
//                }
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//
//            }
//
//        })
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

        binding.edtOrganizedBy.setOnClickListener {
//            if (selectedExpenseHead == 0) {
//                binding.selExpenseHeadInput.error = "Please select Expense Head"
//                return@setOnClickListener
//            }
//         //   getOrganizedBy()
        }

        binding.btnAdd.setOnClickListener {
            if (filePath != null) {
                expenseFileListModels!!.add(ExpenseFileListModel(filename, filePath))
                setupRecyclerView()
                filename = "filename"
                filePath = null
                binding.tvFileName.text = "Capture Image"
            } else {
                Utilities.showToastMessage(
                    "Please capture file",
                    this@SubmitBillDetailsActivity,
                    false
                )
            }
        }


        binding.btnBrowse.setOnClickListener(View.OnClickListener {
//            if (ActivityCompat.checkSelfPermission(
//                    this@SubmitBillDetailsActivity,
//                    permission.CAMERA
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    (this@SubmitBillDetailsActivity as Activity?)!!,
//                    arrayOf<String>(permission.CAMERA),
//                    2
//                )
//                return@OnClickListener
//            }
//            val randomEndtNo = (Math.random() * 999999 + 1).toInt()
//            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//            if (VERSION.SDK_INT > Build.VERSION_CODES.Q) {
//                val resolver: ContentResolver = this@SubmitBillDetailsActivity.contentResolver
//                val contentValues = ContentValues()
//                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, randomEndtNo)
//                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
//                contentValues.put(
//                    MediaStore.MediaColumns.RELATIVE_PATH,
//                    Environment.DIRECTORY_PICTURES
//                )
//                fileUri =
//                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
//                startActivityForResult(intent, CAMERA_REQUEST);
////                cameraCaptureResult.launch(intent)
//            } else {
//                val patientImageFile = File(picsFolder, "$randomEndtNo.png")
//                fileUri = Uri.fromFile(patientImageFile)
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
//                startActivityForResult(intent, CAMERA_REQUEST);
////                cameraCaptureResult.launch(intent)
//            }



            imageType = 0
            if (VERSION.SDK_INT < Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(
                        this@SubmitBillDetailsActivity,
                        permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                        this@SubmitBillDetailsActivity,
                        permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                        this@SubmitBillDetailsActivity,
                        permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        (this@SubmitBillDetailsActivity as Activity?)!!,
                        arrayOf<String>(
                            permission.CAMERA,
                            permission.WRITE_EXTERNAL_STORAGE,
                            permission.READ_EXTERNAL_STORAGE
                        ),
                        2
                    )
                    return@OnClickListener
                }
            } else {
                if (ActivityCompat.checkSelfPermission(
                        this@SubmitBillDetailsActivity,
                        permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        (this@SubmitBillDetailsActivity as Activity?)!!,
                        arrayOf<String>(permission.CAMERA),
                        2
                    )
                    return@OnClickListener
                }
            }


            selectImage()




        })

        binding.btnSave.setOnClickListener(View.OnClickListener {
            if (binding.edtSelExpenseHead.text.toString().isEmpty()) {
                binding.edtSelExpenseHead.setError("Please choose Expense Head")
                return@OnClickListener
            }
            if (binding.edtSelSubExpenseHead.text.toString().isEmpty()) {
                binding.edtSelSubExpenseHead.setError("Please choose Sub Expense Head")
                return@OnClickListener
            }
            if (binding.edtOrganizedBy.text.toString().isEmpty()) {
                binding.edtOrganizedBy.setError("Please Choose Organized By")
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


//            if (total > selectedSubExpense.MaxAllowedAmt!!) {
//
//                binding.CWTotalInputLayout.error =
//                    "Total Amount should not be grater than Max Allowed Amount"
//                binding.CWTotalInputLayout.isErrorEnabled
//                return@OnClickListener
//            }


            if (isProofOfPermission == 1){
                if (binding.CWDescriptionEditText.text.toString().isEmpty()){
                    binding.CWDescriptionEditText.error = "Please enter remark"
                    binding.CWDescriptionInputLayout.isErrorEnabled
                    return@OnClickListener
                }

            }

            if (isProofOfPermission == 1){

                if (isBillRequired) {
                    if (filePath == null) {
                        Utilities.showToastMessage(
                            "Choose files to upload",
                            this@SubmitBillDetailsActivity,
                            false
                        )
                        return@OnClickListener
                    }
                    if (filePath!!.isEmpty()) {
                        Utilities.showToastMessage(
                            "Choose files to upload",
                            this@SubmitBillDetailsActivity,
                            false
                        )
                        return@OnClickListener
                    }
                }


            }

            saveBillDetails()
        })
    }

//    private var cameraCaptureResult: ActivityResultLauncher<Intent> =
//        registerForActivityResult<Intent, ActivityResult>(
//            ActivityResultContracts.StartActivityForResult(),
//            ActivityResultCallback<ActivityResult> { result ->
//                if (result.resultCode == RESULT_OK) {
//                    // There are no request codes
//                    val data = result.data
//                    Log.d(TAG, "onActivityResult: $data")
//                    CropImage.activity(fileUri).setGuidelines(CropImageView.Guidelines.ON)
//                        .start(this@SubmitBillDetailsActivity)
//
//                }
//            })




    private val cropImageLauncher =
        registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                val uriContent = result.uriContent
                val filePath = result.getUriFilePath(this@SubmitBillDetailsActivity, true)
                uriContent?.let { saveImageFile(it) }
                Log.i("cropImageLauncher", "Cropped path: $filePath")
            } else {
                result.error?.printStackTrace()
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            if (requestCode == GALLERY_REQUEST) {
                val imageUri = data?.data
//                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
//                    .start(this@SubmitBillDetailsActivity)

//                val cropImageOptions = CropImageOptions()
//                cropImageOptions.guidelines = Guidelines.ON
//                cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG
//
//                val options = CropImageContractOptions(imageUri, cropImageOptions)
//                cropImageLauncher.launch(options)

                imageUri?.let { saveImageFile(it) };



            }

            // There are no request codes
            if (requestCode == CAMERA_REQUEST) {
                Log.d(TAG, "onActivityResult: $data")
//                CropImage.activity(fileUri).setGuidelines(CropImageView.Guidelines.ON)
//                    .start(this@SubmitBillDetailsActivity)


                val cropImageOptions = CropImageOptions()
                cropImageOptions.guidelines = Guidelines.ON
//                cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.PNG

                val options = CropImageContractOptions(fileUri, cropImageOptions)
                cropImageLauncher.launch(options)

            }


            if (requestCode == 12) {
                val pdfUri = data!!.data
                Log.d("ActivityResult", pdfUri!!.path!!)
                savePdfFile(pdfUri)
                if (filePath != null) {

                    //                    tvFileName.setText("Capture Image");
                } else {
//                    Utilities.showToastMessage("Please capture file", context, false)
                }
            }



        }


//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            val result = CropImage.getActivityResult(data)
//            if (resultCode == Activity.RESULT_OK) {
//                val resultUri = result.uri
//                try {
////                    createPdf(getThumbnail(resultUri))
//                    saveImageFile(resultUri);
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                //                saveImageFile(resultUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                val error = result.error
//            }
//        }

    }


//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == GALLERY_REQUEST) {
//                val imageUri = data.data
//                CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
//                    .start(this@SubmitBillDetailsActivity)
//            }
//            if (resultCode == Activity.RESULT_OK) {
//                if (requestCode == CAMERA_REQUEST) {
//                    CropImage.activity(fileUri).setGuidelines(CropImageView.Guidelines.ON)
//                        .start(this@SubmitBillDetailsActivity)
//                }
//            }
//
////            if (requestCode == ACK_CAMERA_REQUEST) {
////                CropImage.activity(ackUri).setGuidelines(CropImageView.Guidelines.ON).start(BeneficiaryVerificationActivityNew.this);
////            }
//        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            val result = CropImage.getActivityResult(data)
//            if (resultCode == Activity.RESULT_OK) {
//                val resultUri = result.uri
//                if (imageType == 0) {
//                    savefile(resultUri)
//                } else if (imageType == 1) {
////                    saveAckfile(resultUri);
//                }
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                val error = result.error
//            }
//        }
//
//
////        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
////            CropImage.ActivityResult result = CropImage.getActivityResult(data);
////            if (resultCode == RESULT_OK) {
////                Uri resultUri = result.getUri();
////                savefile(resultUri);
////            }
//////            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//////                Exception error = result.getError();
//////            }
////        }
//    }

    fun savePdfFile(sourceuri: Uri) {
        Log.i("sourceuri1", "" + sourceuri)
        val path = FileUtils.getPathFromURI(this@SubmitBillDetailsActivity, sourceuri)
        var destinationFilename = ""
        filename =  "POP"+"-"+selectedSubExpenseHeadNew+"-"+advanceDetails.campid+"-"+ System.currentTimeMillis().toString() + ".pdf"
        destinationFilename = externalCacheDir.toString() + "/" + filename
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            bis = BufferedInputStream(FileInputStream(path))
            bos = BufferedOutputStream(FileOutputStream(destinationFilename, false))
            val buf = ByteArray(1024)
            bis.read(buf)
            do {
                bos.write(buf)
            } while (bis.read(buf) != -1)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                bos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

//        letterPicBm = BitmapFactory.decodeFile(destinationFilename);
//        destinationFilename = compressImage(destinationFilename);
//        letterPicBm = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false);
//        imv_patient.setImageBitmap(letterPicBm);
        filePath = destinationFilename
        val file1 = File(filePath)
        val sizeInKB = file1.length() / 1024

        if (sizeInKB > 2000) {
            Utilities.showAlertDialog(
                this@SubmitBillDetailsActivity,
                "Alert",
                "File Size should be under 2MB please check file size",
                false
            )
            return
        }


        binding.tvFileName.text = filename

        binding.btnBrowse.visibility = View.VISIBLE
        binding.btnSave.isEnabled = true


//        new BeneficiaryVerificationActivity.ApproveBeneficiary().execute(filePath,"1");
    }


//    fun savePdfFile(sourceuri: Uri) {
//        Log.i("sourceuri1", "" + sourceuri)
//        val path = FileUtils.getPathFromURI(this@SubmitBillDetailsActivity, sourceuri)
//        var destinationFilename = ""
//        filename =  "POP"+"-"+selectedSubExpenseHeadNew+"-"+advanceDetails.campid+"-"+ System.currentTimeMillis().toString() + ".pdf"
//        destinationFilename = externalCacheDir.toString() + "/" + filename
//        val sourceFilename = sourceuri.path
//        var bis: BufferedInputStream? = null
//        var bos: BufferedOutputStream? = null
//        try {
//            bis = BufferedInputStream(FileInputStream(sourceFilename))
//            bos = BufferedOutputStream(FileOutputStream(destinationFilename, false))
//            val buf = ByteArray(1024)
//            bis.read(buf)
//            do {
//                bos.write(buf)
//            } while (bis.read(buf) != -1)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            try {
//                bis?.close()
//                bos?.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//
////        letterPicBm = BitmapFactory.decodeFile(destinationFilename);
////        destinationFilename = compressImage(destinationFilename);
////        letterPicBm = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false);
////        imv_patient.setImageBitmap(letterPicBm);
//        filePath = destinationFilename
//        val file1 = File(filePath)
//        val sizeInKB = file1.length() / 1024
//
//        binding.tvFileName.text = filename
//
//        binding.btnBrowse.visibility = View.VISIBLE
//
////        new BeneficiaryVerificationActivity.ApproveBeneficiary().execute(filePath,"1");
//    }

//
//    fun saveImageFile(sourceuri: Uri) {
//        Log.i("sourceuri1", "" + sourceuri)
//        var destinationFilename = ""
//        filename =  "POP"+"-"+selectedSubExpenseHeadNew+"-"+advanceDetails.campid+"-"+ System.currentTimeMillis().toString() + ".jpeg"
//        destinationFilename = externalCacheDir.toString() + "/" + filename
//        val sourceFilename = sourceuri.path
//        var bis: BufferedInputStream? = null
//        var bos: BufferedOutputStream? = null
//        try {
//            bis = BufferedInputStream(FileInputStream(sourceFilename))
//            bos = BufferedOutputStream(FileOutputStream(destinationFilename, false))
//            val buf = ByteArray(1024)
//            bis.read(buf)
//            do {
//                bos.write(buf)
//            } while (bis.read(buf) != -1)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            try {
//                bis?.close()
//                bos?.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//
//        letterPicBm = BitmapFactory.decodeFile(destinationFilename)
//        destinationFilename = Utilities.compressImage(destinationFilename)
////        letterPicBm = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false)
//        binding.imvPatient.setImageBitmap(letterPicBm)
//
//        filePath = destinationFilename
//        binding.tvFileName.text = filename
//
////        uploadBillsNew("1");
//
//        binding.btnSave.isEnabled = true
//
//    }


    fun saveImageFile(sourceUri: Uri) {
        progressDialog.setMessage("Saving Image,\n Please wait...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        Thread {
            try {
                Log.i("sourceuri1", "$sourceUri")

                // Build filename using same pattern you had originally
                val generatedName = "POP-${selectedSubExpenseHeadNew}-${advanceDetails.campid}-${System.currentTimeMillis()}.jpeg"
                // save to external cache with that name
                val destinationFile = File(externalCacheDir, generatedName)

                // Open InputStream from the content URI (works for camera, gallery, etc.)
                val inputStream = contentResolver.openInputStream(sourceUri)
                    ?: throw IOException("Unable to open source Uri: $sourceUri")

                // Copy streams (safe buffered copy)
                BufferedInputStream(inputStream).use { bis ->
                    BufferedOutputStream(FileOutputStream(destinationFile)).use { bos ->
                        val buffer = ByteArray(8 * 1024)
                        var bytesRead: Int
                        while (bis.read(buffer).also { bytesRead = it } != -1) {
                            bos.write(buffer, 0, bytesRead)
                        }
                        bos.flush()
                    }
                }

                // Assign to the class-level filename property (as your original code did)
                filename = generatedName

                // Call your existing compress logic
                val compressedPath = try {
                    Utilities.compressImage(destinationFile.absolutePath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    // fallback to original file path if compress fails
                    destinationFile.absolutePath
                }

                val finalPath = if (compressedPath.isNullOrBlank()) destinationFile.absolutePath else compressedPath

                // Decode bitmap from final file and update UI
                val bitmap = try {
                    BitmapFactory.decodeFile(finalPath)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                runOnUiThread {
                    progressDialog.dismiss()

                    // set filePath to the final path (used elsewhere)
                    filePath = finalPath

                    // Keep the same UI updates as original snippet
                    if (bitmap != null) {
                        letterPicBm = bitmap
                        binding.imvPatient.setImageBitmap(letterPicBm)
                    }

                    // show the generated filename in UI (matches original behavior)
                    binding.tvFileName.text = filename

                    binding.btnSave.isEnabled = true
                }

            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread { progressDialog.dismiss() }
            }
        }.start()
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

//    fun savefile(sourceuri: Uri) {
//        Log.i("sourceuri1", "" + sourceuri)
//        var destinationFilename = ""
//        val returnCursor = contentResolver.query(sourceuri, null, null, null, null)
//        /*
//         * Get the column indexes of the data in the Cursor,
//         * move to the first row in the Cursor, get the data,
//         * and display it.
//         */
//        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
//        returnCursor.moveToFirst()
//        filename = returnCursor.getString(nameIndex)
//        destinationFilename = externalCacheDir.toString() + "/" + filename
//        var bis: InputStream? = null
//        try {
//            bis = contentResolver.openInputStream(sourceuri)
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//        var bos: BufferedOutputStream? = null
//        try {
//            bos = BufferedOutputStream(FileOutputStream(destinationFilename, false))
//            val buf = ByteArray(1024)
//            bis!!.read(buf)
//            do {
//                bos.write(buf)
//            } while (bis.read(buf) != -1)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            try {
//                bis?.close()
//                bos?.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        filePath = destinationFilename
//        binding.tvFileName.setText(filename)
//        Log.d(TAG, "savefile: $filePath")
//    }


    fun savefile(sourceuri: Uri) {
        Log.i("sourceuri1", "" + sourceuri)
        var destinationFilename = ""
        filename = (Math.random() * 99999 + 1).toInt().toString() + "_PR.png"
        destinationFilename = picsFolder.toString() + filename
        val sourceFilename = sourceuri.path
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        try {
            bis = BufferedInputStream(FileInputStream(sourceFilename))
            bos = BufferedOutputStream(FileOutputStream(destinationFilename, false))
            val buf = ByteArray(1024)
            bis.read(buf)
            do {
                bos.write(buf)
            } while (bis.read(buf) != -1)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bis?.close()
                bos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
//        letterPicBm = BitmapFactory.decodeFile(destinationFilename)
//        destinationFilename = Utilities.compressImage(destinationFilename)
//        letterPicBm = Bitmap.createScaledBitmap(letterPicBm, 150, 150, false)
//        imv_patient.setImageBitmap(letterPicBm)
        filePath = destinationFilename
        binding.tvFileName.text = filename


//        new BeneficiaryVerificationActivity.ApproveBeneficiary().execute(letterImagePath,"1");
    }


    fun setupRecyclerView() {
        binding.rvFileList.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE
        binding.rvFileList.setHasFixedSize(false)
        binding.rvFileList.layoutManager = LinearLayoutManager(this@SubmitBillDetailsActivity)
        binding.rvFileList.adapter =
            ExpenseFileListAdapter(expenseFileListModels, this@SubmitBillDetailsActivity)
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

        advanceDetails.campid?.let {
            advanceDetails.Registeredbeneficiarycount?.let { it1 ->
                apiInterface.getSubExpensesMasterDataNew(
                    selectedExpenseHead,
                    organizedById,
                    campType
                )
                    .enqueue(object : Callback<SubExpenseHeadResponseModel> {
                        override fun onResponse(
                            call: Call<SubExpenseHeadResponseModel>,
                            response: Response<SubExpenseHeadResponseModel>
                        ) {
                            progressDialog.dismiss()
                            try {
                                if (response.isSuccessful) {
                                    var subExpenseHeadResponseModel = response.body()!!
                                    if (subExpenseHeadResponseModel.status.equals(
                                            "success",
                                            ignoreCase = true
                                        )
                                    ) {
                                        showSubExpenseListDialog(subExpenseHeadResponseModel.output)
                                    }else{

                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onFailure(
                            call: Call<SubExpenseHeadResponseModel>,
                            t: Throwable
                        ) {
                            Log.d(TAG, "onFailure: ${t.message}")
                            progressDialog.dismiss()
                        }
                    })
            }
        }
    }
    private fun getOrganizedBy() {
        val progressDialog = ProgressDialog(this@SubmitBillDetailsActivity)
        progressDialog.setMessage("Please wait . . . ")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val apiService = ApiClient.getD2DClient().create(ApiInterface::class.java)
        val call = apiService.getOrganizedBy()
        call.enqueue(object : Callback<GetInitiatedByListForCampModel> {
            override fun onResponse(
                call: Call<GetInitiatedByListForCampModel>,
                response: Response<GetInitiatedByListForCampModel>
            ) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    val status = response.body()!!.status
                    val message = response.body()!!.message
                    if (status.equals("Success", ignoreCase = true)) {
                        val outputItems = response.body()!!
                            .output
                        if (outputItems.size > 0) {
                            for (o in outputItems) {
                            }
                            showTrenchListDialog(outputItems)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<GetInitiatedByListForCampModel>, t: Throwable) {
                progressDialog.dismiss()
                t.localizedMessage
            }
        })
    }


    private fun showTrenchListDialog(trenchList: MutableList<GetInitiatedByListForCampModel.Output>) {
        val builderSingle = AlertDialog.Builder(this@SubmitBillDetailsActivity)
        builderSingle.setTitle("Select Organized By")
        builderSingle.setCancelable(false)
        val arrayAdapter =
            ArrayAdapter<String>(this@SubmitBillDetailsActivity, R.layout.list_row)
        for (i in trenchList.indices) {
            arrayAdapter.add(trenchList[i].initiatedBy)
        }
        builderSingle.setAdapter(
            arrayAdapter
        ) { dialog, which ->
            binding.edtOrganizedBy.setText(trenchList[which].initiatedBy)
            organizedById = trenchList[which].id
          //  binding.inputLayoutCallStatus.setErrorEnabled(false)

        }
        val alertDialog = builderSingle.create()
        alertDialog.setButton(
            DialogInterface.BUTTON_NEGATIVE, "Cancel"
        ) { dialogInterface, i -> dialogInterface.dismiss() }
        alertDialog.show()
    }


    private fun showListDialog(expenseHeadListModel: ArrayList<Output>) {
        val builderSingle =
            AlertDialog.Builder(this@SubmitBillDetailsActivity, R.style.CustomDialogTheme)
        builderSingle.setTitle("Select Expense Head")
        builderSingle.setCancelable(false)
        val arrayAdapter = ArrayAdapter<String>(this@SubmitBillDetailsActivity, R.layout.list_row)
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
            resetView();

//            if (selectedExpenseHead == 1) {
//                advanceDetails.ApprovedRefreshment?.let {
//                    binding.tvExpenseApprovedAmount.text = "Approved Amount:- $it"
//                }
//
//            } else if (selectedExpenseHead == 2) {
//                advanceDetails.ApprovedCourier?.let {
//                    binding.tvExpenseApprovedAmount.text = "Approved Amount:- $it"
//                }
//
//            } else if (selectedExpenseHead == 3) {
//                advanceDetails.ApprovedTransport?.let {
//                    binding.tvExpenseApprovedAmount.text = "Approved Amount:- $it"
//                }
//
//            } else if (selectedExpenseHead == 4) {
//                advanceDetails.ApprovedMIS?.let {
//                    binding.tvExpenseApprovedAmount.text = "Approved Amount:- $it"
//                }
//
//            }

        }
        builderSingle.show()
    }
    private fun showListOrganizedDialog(getInitiatedByListForCampModel: ArrayList<Output>) {
        val builderSingle =
            AlertDialog.Builder(this@SubmitBillDetailsActivity, R.style.CustomDialogTheme)
        builderSingle.setTitle("Select Organized By")
        builderSingle.setCancelable(false)
        val arrayAdapter = ArrayAdapter<String>(this@SubmitBillDetailsActivity, R.layout.list_row)
        for (heads in getInitiatedByListForCampModel) {
            arrayAdapter.add(heads.ExpenseName)
        }
        builderSingle.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, _: Int -> dialog.dismiss() }

        builderSingle.setAdapter(arrayAdapter) { dialog: DialogInterface?, which: Int ->
            selectedExpenseHead = getInitiatedByListForCampModel[which].ExpenseID!!
            binding.edtSelExpenseHead.setText(getInitiatedByListForCampModel[which].ExpenseName)
            binding.tvExpenseTitle.setText(getInitiatedByListForCampModel[which].ExpenseName)
            binding.edtSelExpenseHead.setError(null);
            binding.selExpenseHeadInput.error = null
            resetView();

//            if (selectedExpenseHead == 1) {
//                advanceDetails.ApprovedRefreshment?.let {
//                    binding.tvExpenseApprovedAmount.text = "Approved Amount:- $it"
//                }
//
//            } else if (selectedExpenseHead == 2) {
//                advanceDetails.ApprovedCourier?.let {
//                    binding.tvExpenseApprovedAmount.text = "Approved Amount:- $it"
//                }
//
//            } else if (selectedExpenseHead == 3) {
//                advanceDetails.ApprovedTransport?.let {
//                    binding.tvExpenseApprovedAmount.text = "Approved Amount:- $it"
//                }
//
//            } else if (selectedExpenseHead == 4) {
//                advanceDetails.ApprovedMIS?.let {
//                    binding.tvExpenseApprovedAmount.text = "Approved Amount:- $it"
//                }
//
//            }

        }
        builderSingle.show()
    }

    fun resetView() {
        binding.unitEditText.setText("")
        binding.amountPerUnitEditText.setText("")
        binding.CWTotalEditText.setText("")
        binding.edtSelSubExpenseHead.setText("")
        binding.tvExpenseApprovedAmount.setText("")
        binding.tvMaxAllowedAmount.setText("")
        binding.tvUnit.setText("")
        binding.CWDescriptionEditText.setText("")
        binding.tvFileName.setText("Capture Image")
        filePath = null
    }

    private fun showSubExpenseListDialog(expenseHeadListModel: ArrayList<SubExpenseOutput>) {
        val builderSingle =
            AlertDialog.Builder(this@SubmitBillDetailsActivity, R.style.CustomDialogTheme)
        builderSingle.setTitle("Select Sub Expense Head")
        builderSingle.setCancelable(false)
        val arrayAdapter = ArrayAdapter<String>(this@SubmitBillDetailsActivity, R.layout.list_row)
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
            binding.edtSelSubExpenseHead.error = null;
            binding.selExpenseHeadInput.error = null;

            selectedSubExpenseHeadNew = expenseHeadListModel[which].subExpenseIDPk!!

            expenseHeadListModel[which].isbillrequired?.let {
                isBillRequired = it
            }

//            if (isBillRequired) {
//                binding.btnBrowse.visibility = View.VISIBLE
//                binding.tvFileName.visibility = View.VISIBLE
//            } else {
//                binding.btnBrowse.visibility = View.GONE
//                binding.tvFileName.visibility = View.GONE
//            }
//            resetView()
            binding.unitEditText.setText("")
            binding.amountPerUnitEditText.setText("")
            binding.CWTotalEditText.setText("")
            binding.CWDescriptionEditText.setText("")
            binding.tvUnit.text = expenseHeadListModel[which].Unit
            binding.tvMaxAllowedAmount.text = expenseHeadListModel[which].MaxAllowedAmt.toString()

//            if (advanceDetails.Registeredbeneficiarycount!! < 35) {
//                binding.btnSave.isEnabled = selectedSubExpense.subExpenseID == 6
//            }
            binding.btnSave.isEnabled = true

            when (selectedSubExpense.subExpenseID) {
                1 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.CampHall}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(1))
                }

                2 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.Chairs}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                3 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.Tables}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                4 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.CleaningCharges}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                5 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.DrinkingWater}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                6 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.BeneficiaryRefreshment}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(3))
                }

                7 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.SampleMovementToLabTSRTCOrAnyOtherCargo}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                8 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.SampleMovementToLabRunnerBoy}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                9 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.CampAwarenessUsingBhopu}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                10 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.TransportationOfStaffTAAllowanceToStaffIndividuals}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))
                }

                11 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.TransportationOfStaffTAAllowanceToStaffGroupTransportationLikeAutoTumTum}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))
                }

                12 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.FoodToStaffTAAllowance}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))

                }

                13 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.PostCampExpense}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))
                }
                14 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.Doctorreportscreeningexpense}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))
                }
                15 -> {
                    binding.tvExpenseApprovedAmount.setText("Rs. ${advanceDetails.Doctorphysicalscreeningexpense}")
                    binding.unitEditText.filters = arrayOf(InputFilter.LengthFilter(2))
                }
            }

        }
        builderSingle.show()
    }


    private fun saveBillDetails() {

        progressDialog.setMessage("Submitting bill details")
        progressDialog.setCancelable(false)
        progressDialog.show()
        advanceDetails.campid?.let {
            apiInterface.insertAdvancesRequestNewChangesNew(
                it.toInt(),
                2,
                selectedSubExpenseHeadNew,
                binding.CWTotalEditText.text.toString().toInt(),
                1,
                sessionManager.userDetailsJson.empCode,
                0,
                binding.CWDescriptionEditText.text.toString(),
                binding.unitEditText.text.toString().toInt(),
                binding.amountPerUnitEditText.text.toString(),
                organizedById.toString()
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

                                if (isProofOfPermission == 1){

                                    uploadBillsNew(msg);


//                                    if (selectedSubExpense.isbillrequired!!) {
//
//                                        uploadBillsNew(msg);
//                                    }
                                } else {
                                    LocalBroadcastManager.getInstance(this@SubmitBillDetailsActivity)
                                        .sendBroadcast(
                                            Intent("RefreshAdvanceList").putExtra(
                                                "fromDate",
                                                intent.getStringExtra("fromDate")
                                            ).putExtra("toDate", intent.getStringExtra("toDate"))
                                        )
                                    resetView()

                                    Utilities.showAlertDialog(
                                        this@SubmitBillDetailsActivity,
                                        status,
                                        "Bill Submitted Successfully.",
                                        true,
                                        "Okay",
                                        object : DialogInterface.OnClickListener {
                                            override fun onClick(p0: DialogInterface, p1: Int) {
//                                    finish()
                                                p0.dismiss()
                                            }
                                        })
                                }
                            }else{
                                if (status.equals("fail")){
                                    Utilities.showAlertDialog(
                                        this@SubmitBillDetailsActivity,
                                        status,
                                        "Bill has been uploaded for selected sub-expense.",
                                        false,
                                        "Okay",
                                        object : DialogInterface.OnClickListener {
                                            override fun onClick(p0: DialogInterface, p1: Int) {
//                                    finish()
                                                p0.dismiss()
                                            }
                                        })
                                }
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

    fun uploadBills(msg: String) {
        isFileUploading = true
        progressDialog.setMessage("Uploading File..")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val uploadFile = File(filePath)

        uploadInterface.uploadBills(
            MultipartBody.Part.createFormData("Billid", msg),
            MultipartBody.Part.createFormData(
                "createdBy",
                sessionManager.userDetailsJson.empCode.toString()
            ),
            MultipartBody.Part.createFormData("ExpenseHead", selectedExpenseHead.toString()),
            MultipartBody.Part
                .createFormData(
                    name = "FileName",
                    filename = uploadFile.name,
                    body = uploadFile.asRequestBody()
                )
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressDialog.dismiss()
                try {
                    val res = response.body()?.string()
                    val json = JSONObject(res)
                    val status = json.getString("status")
                    val msg = json.getString("message")
                    Log.d(TAG, "onResponse: ${res}")
                    isFileUploading = false
                    if (status.equals("success", ignoreCase = true)) {
                        expenseFileListModels?.clear()
                        binding.rvFileList.adapter = ExpenseFileListAdapter(
                            expenseFileListModels,
                            this@SubmitBillDetailsActivity
                        )

                        resetView()

                        Utilities.showAlertDialog(
                            this@SubmitBillDetailsActivity,
                            status,
                            "Bill Submitted Successfully",
                            true,
                            "Okay",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface, p1: Int) {
//                                    finish()
                                    p0.dismiss()
                                }

                            })
                        LocalBroadcastManager.getInstance(this@SubmitBillDetailsActivity)
                            .sendBroadcast(
                                Intent("RefreshAdvanceList").putExtra(
                                    "fromDate",
                                    intent.getStringExtra("fromDate")
                                ).putExtra("toDate", intent.getStringExtra("toDate"))
                            )

                    } else {
                        Utilities.showAlertDialog(
                            this@SubmitBillDetailsActivity,
                            status,
                            msg,
                            false
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onResponse: ${e.message}")
                    Utilities.showAlertDialog(
                        this@SubmitBillDetailsActivity,
                        "Fail",
                        e.message,
                        false
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog.dismiss()
                Log.e(TAG, "onFailure: ${t.message}")
                Utilities.showAlertDialog(
                    this@SubmitBillDetailsActivity,
                    "Fail",
                    t.message,
                    false
                )
            }
        })
    }

    fun uploadBillsNew(msg: String) {
        isFileUploading = true
        progressDialog.setMessage("Uploading File..")
        progressDialog.setCancelable(false)
        progressDialog.show()
        val uploadFile = File(filePath)

        uploadInterface.uploadProofOfPermission(
            MultipartBody.Part.createFormData("RequestID", msg),
            MultipartBody.Part.createFormData(
                "createdBy",
                sessionManager.userDetailsJson.empCode.toString()
            ),
            MultipartBody.Part
                .createFormData(
                    name = "AttachementProof",
                    filename = uploadFile.name,
                    body = uploadFile.asRequestBody()
                )
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressDialog.dismiss()
                try {
                    val res = response.body()?.string()
                    val json = JSONObject(res)
                    val status = json.getString("status")
                    val msg = json.getString("message")
                    Log.d(TAG, "onResponse: ${res}")
                    isFileUploading = false
                    if (status.equals("success", ignoreCase = true)) {
                        expenseFileListModels?.clear()
                        binding.rvFileList.adapter = ExpenseFileListAdapter(
                            expenseFileListModels,
                            this@SubmitBillDetailsActivity
                        )

                        resetView()

                        Utilities.showAlertDialog(
                            this@SubmitBillDetailsActivity,
                            status,
                            "Bill Submitted Successfully",
                            true,
                            "Okay",
                            object : DialogInterface.OnClickListener {
                                override fun onClick(p0: DialogInterface, p1: Int) {
                                    filePath = null
                                    letterPicBm = null
                                    binding.imvPatient.setImageBitmap(letterPicBm)

                                    binding.imvPatient.visibility = View.GONE
                                    binding.btnBrowse.visibility = View.GONE
                                    binding.tvFileName.visibility = View.GONE
//                                    binding.tvFileName.text = filename


//                                    finish()
                                    p0.dismiss()
                                }

                            })
                        LocalBroadcastManager.getInstance(this@SubmitBillDetailsActivity)
                            .sendBroadcast(
                                Intent("RefreshAdvanceList").putExtra(
                                    "fromDate",
                                    intent.getStringExtra("fromDate")
                                ).putExtra("toDate", intent.getStringExtra("toDate"))
                            )

                    } else {
                        Utilities.showAlertDialog(
                            this@SubmitBillDetailsActivity,
                            status,
                            msg,
                            false
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onResponse: ${e.message}")
                    Utilities.showAlertDialog(
                        this@SubmitBillDetailsActivity,
                        "Fail",
                        e.message,
                        false
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog.dismiss()
                Log.e(TAG, "onFailure: ${t.message}")
                Utilities.showAlertDialog(
                    this@SubmitBillDetailsActivity,
                    "Fail",
                    t.message,
                    false
                )
            }
        })
    }


//    internal class UploadFile :
//        AsyncTask<String?, Void?, String?>() {
//        override fun onPreExecute() {
//            super.onPreExecute()
//            isFileUploading = true
//            progressDialog.setMessage("Uploading " + postCampFileListModels.size + " files..")
//            progressDialog.setCancelable(false)
//            progressDialog.show()
//        }
//
//        protected override fun onPostExecute(response: String) {
//            super.onPostExecute(response)
//            Log.d(TAG, "onPostExecute: $response")
//            isFileUploading = false
//            if (response != null) {
//                try {
//                    val jsonObject = JSONObject(response)
//                    val status = jsonObject.getString("status")
//                    val msg = jsonObject.getString("message")
//                    if (status.equals("success", ignoreCase = true)) {
////                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent("AttendanceMarkedPatients_Activity"));
////                        postCampFileListModels.remove(fileIndex);
//                        fileIndex = fileIndex + 1
//                        if (fileIndex == postCampFileListModels.size) {
//                            progressDialog.dismiss()
//                            Utilities.showAlertDialog(
//                                mContext,
//                                "Uploaded Successfully",
//                                "File uploaded sccessfully",
//                                true,
//                                "Okay"
//                            ) { dialogInterface, i -> finish() }
//                        }
//                    } else {
//                        progressDialog.dismiss()
//                        LocalBroadcastManager.getInstance(mContext)
//                            .sendBroadcast(Intent("AttendanceMarkedPatients_Activity"))
//                        Utilities.showAlertDialog(
//                            mContext, "Failed", msg, false, "Okay"
//                        ) { dialogInterface, i ->
//                            dialogInterface.dismiss()
//                            finish()
//                        }
//                    }
//                } catch (e: JSONException) {
//                    progressDialog.dismiss()
//                    e.printStackTrace()
//                    Utilities.showToastMessage(response, mContext, false)
//                }
//            }
//        }
//
//        protected override fun doInBackground(vararg strings: String): String {
//            val uploadFile: File = File(filePath)
//            val client = OkHttpClient().newBuilder()
//                .build()
//            val mediaType: MediaType = parse.parse("text/plain")
//            val body: RequestBody = Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("CampId", campDetails.getCAMPID())
//                .addFormDataPart(
//                    "CreatedBy",
//                    UserSessionManager(mContext).userDetailsJson.empCode.toString()
//                )
//                .addFormDataPart(
//                    "file", uploadFile.name,
//                    RequestBody.create(
//                        parse.parse("application/octet-stream"),
//                        uploadFile
//                    )
//                )
//                .build()
//            val request: Request = Builder()
//                .url(ApplicationConstants.Upload_Post_Camp_File_Handler)
//                .method("POST", body)
//                .build()
//            var response: okhttp3.Response? = null
//            return try {
//                response = client.newCall(request).execute()
//                response.body!!.string()
//            } catch (e: IOException) {
//                e.printStackTrace()
//                e.message!!
//            }
//        }
//    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take a Photo", "Choose from Gallery","PDF")
        val builder = android.app.AlertDialog.Builder(this@SubmitBillDetailsActivity)
        builder.setTitle("Select Photo")
        builder.setCancelable(false)
        builder.setItems(
            options
        ) { dialog: DialogInterface?, item: Int ->
            if (options[item] == "Take a Photo") {
                file = File(picsFolder, "_PR.png")
                fileUri = Uri.fromFile(file)
                //                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                val randomEndtNo = (Math.random() * 99999 + 1).toInt()
                if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val resolver: ContentResolver = this@SubmitBillDetailsActivity.getContentResolver()
                    val contentValues =
                        ContentValues()
                    contentValues.put(
                        MediaStore.MediaColumns.DISPLAY_NAME,
                        randomEndtNo.toString() + "_PR.png"
                    )
                    contentValues.put(
                        MediaStore.MediaColumns.MIME_TYPE,
                        "image/png"
                    )
                    contentValues.put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES
                    )
                    fileUri = resolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )
                    val intent =
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                    startActivityForResult(intent, CAMERA_REQUEST)
                } else {
                    val patientImageFile: File =
                        File(picsFolder, randomEndtNo.toString() + "_PR.png")
                    fileUri = Uri.fromFile(patientImageFile)
                    val intent =
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
                    startActivityForResult(intent, CAMERA_REQUEST)
                }
            } else if (options[item] == "Choose from Gallery") {
                val intent =
                    Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, GALLERY_REQUEST)
            } else if (options[item] == "PDF") {
                val intent = Intent()
                intent.type = "application/pdf"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 12)
            }
        }
        builder.setPositiveButton(
            "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        builder.show()
    }




}