package com.myhindlab.abkat.abha.models.patient_queue

import com.google.gson.annotations.SerializedName
import com.myhindlab.abkat.abha.models.patient_queue.Output


data class GetQueueResponseModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)