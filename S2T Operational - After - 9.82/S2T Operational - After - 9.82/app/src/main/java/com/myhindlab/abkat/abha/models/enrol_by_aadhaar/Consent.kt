package com.myhindlab.abkat.abha.models.enrol_by_aadhaar

import com.google.gson.annotations.SerializedName


data class Consent (

  @SerializedName("code"    ) var code    : String? = null,
  @SerializedName("version" ) var version : String? = null

)