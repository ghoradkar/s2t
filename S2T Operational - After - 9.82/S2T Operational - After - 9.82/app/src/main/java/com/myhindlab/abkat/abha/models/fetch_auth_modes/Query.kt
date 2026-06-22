package com.myhindlab.abkat.abha.models.fetch_auth_modes

import com.google.gson.annotations.SerializedName


data class Query (

  @SerializedName("id"        ) var id        : String?    = null,
  @SerializedName("purpose"   ) var purpose   : String?    = null,
  @SerializedName("requester" ) var requester : Requester? = Requester()

)