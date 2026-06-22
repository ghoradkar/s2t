package com.myhindlab.abkat.abha.models.init_auth

import com.google.gson.annotations.SerializedName


data class Query (

  @SerializedName("id"        ) var id        : String?    = null,
  @SerializedName("purpose"   ) var purpose   : String?    = null,
  @SerializedName("authMode"  ) var authMode  : String?    = null,
  @SerializedName("requester" ) var requester : Requester? = Requester()

)