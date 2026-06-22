package com.myhindlab.abkat.abha.models.fetch_auth_modes

import com.google.gson.annotations.SerializedName


data class Requester (

  @SerializedName("type" ) var type : String? = null,
  @SerializedName("id"   ) var id   : String? = null

)