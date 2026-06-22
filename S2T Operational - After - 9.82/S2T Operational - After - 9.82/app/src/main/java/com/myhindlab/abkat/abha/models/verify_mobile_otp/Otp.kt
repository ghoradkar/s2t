package com.myhindlab.abkat.abha.models.verify_mobile_otp

import com.google.gson.annotations.SerializedName


data class Otp (

  @SerializedName("timeStamp" ) var timeStamp : String? = null,
  @SerializedName("txnId"     ) var txnId     : String? = null,
  @SerializedName("otpValue"  ) var otpValue  : String? = null

)