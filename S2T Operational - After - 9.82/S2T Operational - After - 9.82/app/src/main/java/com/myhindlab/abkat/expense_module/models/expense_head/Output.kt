package com.myhindlab.abkat.expense_module.models.expense_head

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("ExpenseHead"   ) var ExpenseID   : Int?    = null,
  @SerializedName("ExpenseHeadName" ) var ExpenseName : String? = null

)