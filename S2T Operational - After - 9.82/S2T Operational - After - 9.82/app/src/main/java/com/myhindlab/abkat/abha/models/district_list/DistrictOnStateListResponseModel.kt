package com.myhindlab.abkat.abha.models.district_list

import com.google.gson.annotations.SerializedName


data class DistrictOnStateListResponseModel (

  @SerializedName("Status"  ) var Status  : String?           = null,
  @SerializedName("Message" ) var Message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)