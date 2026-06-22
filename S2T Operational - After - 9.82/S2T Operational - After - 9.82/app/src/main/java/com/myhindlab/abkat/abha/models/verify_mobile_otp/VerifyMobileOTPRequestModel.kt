package com.myhindlab.abkat.abha.models.verify_mobile_otp

import com.google.gson.annotations.SerializedName
import com.myhindlab.abkat.abha.models.verify_mobile_otp.AuthData


data class VerifyMobileOTPRequestModel (

  @SerializedName("scope"    ) var scope    : ArrayList<String> = arrayListOf(),
  @SerializedName("authData" ) var authData : AuthData?         = AuthData()

)