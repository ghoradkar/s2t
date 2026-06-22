package com.myhindlab.abkat.abha.models.add_context

import com.google.gson.annotations.SerializedName


data class CareContextLinkingRequestModel (

  @SerializedName("abhaNumber"  ) var abhaNumber  : Long?               = null,
  @SerializedName("abhaAddress" ) var abhaAddress : String?            = null,
  @SerializedName("patient"     ) var patient     : ArrayList<Patient> = arrayListOf()

)