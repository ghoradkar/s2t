package com.myhindlab.abkat.abha.models.enrol_by_aadhaar_via_demo_auth

import com.google.gson.annotations.SerializedName


data class DemoAuth (

  @SerializedName("aadhaarNumber" ) var aadhaarNumber : String? = null,
  @SerializedName("districtCode"  ) var districtCode  : String? = null,
  @SerializedName("stateCode"     ) var stateCode     : String? = null,
  @SerializedName("dateOfBirth"   ) var dateOfBirth   : String? = null,
  @SerializedName("gender"        ) var gender        : String? = null,
  @SerializedName("name"          ) var name          : String? = null,
  @SerializedName("mobile"        ) var mobile        : String? = null,
  @SerializedName("pinCode"       ) var pinCode       : String? = null,
  @SerializedName("address"       ) var address       : String? = null

)