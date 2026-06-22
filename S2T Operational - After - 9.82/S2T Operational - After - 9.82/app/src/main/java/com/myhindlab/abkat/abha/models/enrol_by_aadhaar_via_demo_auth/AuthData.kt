package com.myhindlab.abkat.abha.models.enrol_by_aadhaar_via_demo_auth

import com.google.gson.annotations.SerializedName


data class AuthData (

  @SerializedName("authMethods" ) var authMethods : ArrayList<String> = arrayListOf(),
  @SerializedName("demo_auth"   ) var demoAuth    : DemoAuth?         = DemoAuth()

)