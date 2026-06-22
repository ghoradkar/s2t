package com.myhindlab.abkat.expense_module.models.expense_head

import com.google.gson.annotations.SerializedName


data class ExpenseHeadListModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)