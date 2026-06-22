package com.myhindlab.abkat.abha.models.link_token

import com.google.gson.annotations.SerializedName


data class GetLinkTokenCallbackResponseModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)