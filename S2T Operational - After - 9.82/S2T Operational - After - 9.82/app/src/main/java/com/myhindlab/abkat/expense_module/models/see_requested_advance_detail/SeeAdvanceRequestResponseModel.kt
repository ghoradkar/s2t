package com.myhindlab.abkat.expense_module.models.see_requested_advance_detail

import com.google.gson.annotations.SerializedName
import com.myhindlab.abkat.expense_module.models.see_requested_advance_detail.Output


data class SeeAdvanceRequestResponseModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)