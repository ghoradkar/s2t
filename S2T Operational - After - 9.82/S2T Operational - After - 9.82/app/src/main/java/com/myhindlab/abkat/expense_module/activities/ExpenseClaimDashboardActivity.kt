package com.myhindlab.abkat.expense_module.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myhindlab.abkat.R
import com.myhindlab.abkat.databinding.ActivityExpenseClaimDashboardBinding

class ExpenseClaimDashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityExpenseClaimDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseClaimDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        eventListener()

    }

    private fun eventListener() {
        binding.cvAdvanceRequest.setOnClickListener {
//            val modalBottomSheet = ExpenseClaimMenu()
//            modalBottomSheet.show(supportFragmentManager, ExpenseClaimMenu.TAG)
            startActivity(
                Intent(
                    this@ExpenseClaimDashboardActivity,
                    CampCalendarForExpense_Activity::class.java
                )
            )
        }
        binding.cvBillSubmission.setOnClickListener {
            startActivity(
                Intent(
                    this@ExpenseClaimDashboardActivity,
                    AdvanceDetailsListActivity::class.java
                )
            )
        }

        binding.cvBillUpload.setOnClickListener {
            startActivity(
                Intent(
                    this@ExpenseClaimDashboardActivity,
                    BillUploadActivity::class.java
                )
            )
        }
    }

    fun setupToolbar() {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Expense/Claim"

        toolbar.setNavigationIcon(R.drawable.icon_arrowback)
        toolbar.setNavigationOnClickListener { finish() }
    }
}

