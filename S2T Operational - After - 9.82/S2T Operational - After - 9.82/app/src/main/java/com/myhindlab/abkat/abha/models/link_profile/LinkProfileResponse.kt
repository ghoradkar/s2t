package com.myhindlab.abkat.abha.models.link_profile

import com.google.gson.annotations.SerializedName


data class LinkProfileResponse (

  @SerializedName("name"          ) var name          : String? = null,
  @SerializedName("yearOfBirth"   ) var yearOfBirth   : String? = null,
  @SerializedName("dayOfBirth"    ) var dayOfBirth    : String? = null,
  @SerializedName("monthOfBirth"  ) var monthOfBirth  : String? = null,
  @SerializedName("gender"        ) var gender        : String? = null,
  @SerializedName("address"       ) var address       : String? = null,
  @SerializedName("stateName"     ) var stateName     : String? = null,
  @SerializedName("districtName"  ) var districtName  : String? = null,
  @SerializedName("transactionId" ) var transactionId : String? = null

)