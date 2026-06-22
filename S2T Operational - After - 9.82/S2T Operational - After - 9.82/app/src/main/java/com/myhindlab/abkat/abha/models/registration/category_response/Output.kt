package com.myhindlab.abkat.model.registration.category_response

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("CastId"   ) var CastId   : Int?    = null,
  @SerializedName("CastName" ) var CastName : String? = null

)