package com.myhindlab.abkat.abha.models.abha_error

import com.google.gson.annotations.SerializedName


data class Error (

  @SerializedName("code"    ) var code    : String? = null,
  @SerializedName("message" ) var message : String? = null

)