package com.myhindlab.abkat.model.sample_collection.registration

import com.google.gson.annotations.SerializedName


data class RegistrationSummaryResponseModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)