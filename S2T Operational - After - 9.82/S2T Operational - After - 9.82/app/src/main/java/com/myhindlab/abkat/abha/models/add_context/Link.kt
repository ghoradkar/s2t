package com.myhindlab.abkat.abha.models.add_context
import com.google.gson.annotations.SerializedName


data class Link (

  @SerializedName("accessToken" ) var accessToken : String?  = null,
  @SerializedName("patient"     ) var patient     : Patient? = Patient()

)