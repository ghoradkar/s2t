package com.myhindlab.abkat.abha.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.myhindlab.abkat.R
import com.myhindlab.abkat.utilities.Utilities

class ViewQueuePatientActivity : AppCompatActivity() {
    private lateinit var tvFullname: EditText
    private lateinit var tvABHAAddress: EditText
    private lateinit var tvABHANumber: EditText
    private lateinit var tvMobile: EditText
    private lateinit var edtGender: EditText
    private lateinit var edtAddress: EditText
    private lateinit var edtDOB: EditText
    private lateinit var edtAge: EditText
    private lateinit var edtPermAddress: EditText
    private lateinit var edtTokenNo: EditText
    private lateinit var trTokenNumber: TableRow
    private lateinit var trPermanentAddress: TableRow
    lateinit var mContext: Context
    private lateinit var btnGotoReg: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_queue_patient)
        init()
        setEventListener()
        setDefault()
    }

    fun setEventListener() {
        btnGotoReg.setOnClickListener {
            val token = intent.getStringExtra("token")
            if (token != null) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(
                    Intent("set_token").putExtra(
                        "token",
                        intent.getStringExtra("token")
                    )

                        .putExtra("identityID", intent.getIntExtra("identityID", 0))
                        .putExtra("response", intent.getStringExtra("response"))
                )
                finish()
            } else {
                Utilities.showAlertDialog(
                    this@ViewQueuePatientActivity,
                    "Error",
                    "Unable to get token",
                    false
                )
            }
        }
    }

    fun init() {
        mContext = this@ViewQueuePatientActivity
        tvFullname = findViewById(R.id.edtFullName)
        tvABHAAddress = findViewById(R.id.edtABHAAddress)
        tvABHANumber = findViewById(R.id.edtABHANumber)
        tvMobile = findViewById(R.id.edt_mobno)
        edtGender = findViewById(R.id.edtGender)
        edtAddress = findViewById(R.id.edt_address)
        edtDOB = findViewById(R.id.edt_dob)
        edtAge = findViewById(R.id.edt_age)
        btnGotoReg = findViewById(R.id.btnGotoReg)
        edtTokenNo = findViewById(R.id.edtTokenNo)
        trTokenNumber = findViewById(R.id.trTokenNumber)
        edtPermAddress = findViewById(R.id.edtPermAddress)
        trPermanentAddress = findViewById(R.id.trPermanentAddress)

        if (intent.getStringExtra("type") != null && intent.getStringExtra("type")
                .equals("ReadyOnly")
        ) {
            btnGotoReg.visibility = View.GONE
            edtTokenNo.visibility = View.GONE
            trTokenNumber.visibility = View.GONE
            trPermanentAddress.visibility = View.VISIBLE

        } else {
            trPermanentAddress.visibility = View.GONE

        }
    }

    fun setDefault() {
        val name = intent.getStringExtra("name")
        val abhaNumber = intent.getStringExtra("abhaNumber")
        val abhaAddress = intent.getStringExtra("abhaAddress")
        val gender = intent.getStringExtra("gender")
        val dob = intent.getStringExtra("dob")
        val ageInYears = intent.getStringExtra("ageInYears")
        val addressLine = intent.getStringExtra("addressLine")
        val permAddress = intent.getStringExtra("permAddress")
        val mobileNum = intent.getStringExtra("mobileNum")
        val tokenNo = intent.getIntExtra("identityID", 0)

        tvFullname.setText(name)
        tvABHAAddress.setText(abhaAddress)
        tvABHANumber.setText(abhaNumber)
        edtGender.setText(gender)
        edtDOB.setText(dob)
        edtAge.setText(ageInYears.toString())
        edtAddress.setText(addressLine)
        tvMobile.setText(mobileNum)
        edtTokenNo.setText(tokenNo.toString())
        if (permAddress != null) {
            edtPermAddress.setText(permAddress)
        }

    }


}