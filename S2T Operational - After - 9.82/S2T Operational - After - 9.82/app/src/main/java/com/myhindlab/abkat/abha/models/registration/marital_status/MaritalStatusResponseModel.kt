package com.myhindlab.abkat.model.registration.marital_status

import com.google.gson.annotations.SerializedName


data class MaritalStatusResponseModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)