package com.myhindlab.abkat.abha.models.add_context

import com.google.gson.annotations.SerializedName


data class CareContexts (

  @SerializedName("referenceNumber" ) var referenceNumber : String? = null,
  @SerializedName("display"         ) var display         : String? = null

)