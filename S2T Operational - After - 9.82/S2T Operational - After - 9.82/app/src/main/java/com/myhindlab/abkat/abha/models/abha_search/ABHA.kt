package com.tbocwwb.csc_healthcare.abha.models.abha_search

import com.google.gson.annotations.SerializedName


data class ABHA (

  @SerializedName("index"       ) var index       : Int?    = null,
  @SerializedName("ABHANumber"  ) var ABHANumber  : String? = null,
  @SerializedName("name"        ) var name        : String? = null,
  @SerializedName("gender"      ) var gender      : String? = null,
  @SerializedName("kycVerified" ) var kycVerified : String? = null

)