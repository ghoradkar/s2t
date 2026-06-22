package com.myhindlab.abkat.abha.models.enrol_by_aadhaar

import com.google.gson.annotations.SerializedName


data class EnrolByAadhaarRequestModel (

  @SerializedName("authData" ) var authData : AuthData? = AuthData(),
  @SerializedName("consent"  ) var consent  : Consent?  = Consent(),

)