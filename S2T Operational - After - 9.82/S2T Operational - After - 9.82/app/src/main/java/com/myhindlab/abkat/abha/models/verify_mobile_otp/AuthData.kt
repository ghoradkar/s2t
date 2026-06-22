package com.myhindlab.abkat.abha.models.verify_mobile_otp

import com.google.gson.annotations.SerializedName


data class AuthData (

  @SerializedName("authMethods" ) var authMethods : ArrayList<String> = arrayListOf(),
  @SerializedName("otp"         ) var otp         : Otp?              = Otp()

)