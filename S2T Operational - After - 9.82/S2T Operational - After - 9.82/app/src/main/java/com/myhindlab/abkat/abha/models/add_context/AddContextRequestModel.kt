package com.myhindlab.abkat.abha.models.add_context
import com.google.gson.annotations.SerializedName


data class AddContextRequestModel (

  @SerializedName("requestId" ) var requestId : String? = null,
  @SerializedName("timestamp" ) var timestamp : String? = null,
  @SerializedName("link"      ) var link      : Link?   = Link()

)