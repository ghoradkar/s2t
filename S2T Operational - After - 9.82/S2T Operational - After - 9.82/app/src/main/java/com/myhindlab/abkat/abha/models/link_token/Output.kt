package com.myhindlab.abkat.abha.models.link_token

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("abhaAddress" ) var abhaAddress : String? = null,
  @SerializedName("linkToken"   ) var linkToken   : String? = null,
  @SerializedName("requestId"   ) var requestId   : String? = null,
  @SerializedName("CreatedOn"   ) var CreatedOn   : String? = null

)