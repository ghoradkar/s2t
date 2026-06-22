package com.myhindlab.abkat.abha.models.confirm_auth

import com.google.gson.annotations.SerializedName


data class Credential (

  @SerializedName("demographic" ) var demographic : Demographic? = Demographic(),
  @SerializedName("authCode"    ) var authCode    : String?      = null

)