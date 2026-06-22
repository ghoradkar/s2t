package com.myhindlab.abkat.model.registration.solitair_test_mandatory

import com.google.gson.annotations.SerializedName


data class SolitierTestMandateORNotResponseModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)