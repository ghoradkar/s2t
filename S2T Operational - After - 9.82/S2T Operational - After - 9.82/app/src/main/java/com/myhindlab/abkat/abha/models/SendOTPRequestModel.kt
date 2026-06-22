package com.myhindlab.abkat.abha.models

import com.google.gson.annotations.SerializedName


data class SendOTPRequestModel (

  @SerializedName("txnId"     ) var txnId     : String?           = null,
  @SerializedName("scope"     ) var scope     : ArrayList<String> = arrayListOf(),
  @SerializedName("loginHint" ) var loginHint : String?           = null,
  @SerializedName("loginId"   ) var loginId   : String?           = null,
  @SerializedName("otpSystem" ) var otpSystem : String?           = null

)