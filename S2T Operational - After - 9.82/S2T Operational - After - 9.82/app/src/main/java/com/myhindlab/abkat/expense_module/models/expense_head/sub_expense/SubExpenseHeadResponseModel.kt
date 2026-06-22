package com.myhindlab.abkat.expense_module.models.expense_head.sub_expense

import com.google.gson.annotations.SerializedName
import com.myhindlab.abkat.expense_module.models.expense_head.sub_expense.Output


data class SubExpenseHeadResponseModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)