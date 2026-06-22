package com.myhindlab.abkat.expense_module.models.expense_head.sub_expense

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("subExpenseID"   ) var subExpenseID   : Int?     = null,
  @SerializedName("SubexpenseName" ) var SubexpenseName : String?  = null,
  @SerializedName("Unit"           ) var Unit           : String?  = null,
  @SerializedName("isbillrequired" ) var isbillrequired : Boolean? = null,
  @SerializedName("MaxAllowedAmt"  ) var MaxAllowedAmt  : Int?     = null,
  @SerializedName("subExpenseIDPk"  ) var subExpenseIDPk  : Int?     = null

)