package com.myhindlab.abkat.expense_module.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.myhindlab.abkat.databinding.ExpenseClaimBottomSheetBinding
import com.myhindlab.abkat.expense_module.activities.CampCalendarForExpense_Activity
class ExpenseClaimMenu : BottomSheetDialogFragment() {

    lateinit var binding: ExpenseClaimBottomSheetBinding

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ExpenseClaimBottomSheetBinding.inflate(layoutInflater, container, false)


        eventListener()

        return binding.root
    }


    fun eventListener() {
        binding.cvAdvanceFundRequest.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    CampCalendarForExpense_Activity::class.java
                )
            )
        }
    }

}