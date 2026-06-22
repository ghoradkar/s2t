package com.myhindlab.abkat.model.registration.relation_response

import com.google.gson.annotations.SerializedName


data class RelationResponseModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)