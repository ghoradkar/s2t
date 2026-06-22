package com.myhindlab.abkat.model.sample_collection.get_runnerboy_mapped_visited

import com.google.gson.annotations.SerializedName


data class GetRunnerboyListWithMappedFacilityCountResponseModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)