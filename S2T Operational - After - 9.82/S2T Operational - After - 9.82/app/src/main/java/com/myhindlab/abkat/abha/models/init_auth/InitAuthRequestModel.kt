package com.myhindlab.abkat.abha.models.init_auth

import com.google.gson.annotations.SerializedName


data class InitAuthRequestModel (

  @SerializedName("requestId" ) var requestId : String? = null,
  @SerializedName("timestamp" ) var timestamp : String? = null,
  @SerializedName("query"     ) var query     : Query?  = Query()

)