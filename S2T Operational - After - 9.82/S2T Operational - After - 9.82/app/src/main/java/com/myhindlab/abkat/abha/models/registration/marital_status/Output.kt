package com.myhindlab.abkat.model.registration.marital_status

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("MARITALSTATUSID" ) var MARITALSTATUSID : Int?    = null,
  @SerializedName("MARITALSTATUS"   ) var MARITALSTATUS   : String? = null

)