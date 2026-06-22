package com.myhindlab.abkat.abha.models.fetch_auth_modes

import com.google.gson.annotations.SerializedName


data class FetchAuthRequestModel (

  @SerializedName("requestId" ) var requestId : String? = null,
  @SerializedName("timestamp" ) var timestamp : String? = null,
  @SerializedName("query"     ) var query     : Query?  = Query()

)