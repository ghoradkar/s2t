package com.myhindlab.abkat.abha.models.state_list

import com.google.gson.annotations.SerializedName
import com.myhindlab.abkat.abha.models.state_list.Output


data class StateListResponseModel (

  @SerializedName("Status"  ) var Status  : String?           = null,
  @SerializedName("Message" ) var Message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)