package com.myhindlab.abkat.abha.models.enrol_by_aadhaar

import com.google.gson.annotations.SerializedName


data class AuthData (

  @SerializedName("authMethods" ) var authMethods : ArrayList<String> = arrayListOf(),
  @SerializedName("otp"         ) var otp         : Otp?              = Otp()

)