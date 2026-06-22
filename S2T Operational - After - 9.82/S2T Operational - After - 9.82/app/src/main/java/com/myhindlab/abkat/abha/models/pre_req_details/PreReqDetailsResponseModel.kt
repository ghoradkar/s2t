package com.myhindlab.abkat.abha.models.pre_req_details

import com.google.gson.annotations.SerializedName


data class PreReqDetailsResponseModel (

  @SerializedName("Status"  ) var Status  : String?           = null,
  @SerializedName("Message" ) var Message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)