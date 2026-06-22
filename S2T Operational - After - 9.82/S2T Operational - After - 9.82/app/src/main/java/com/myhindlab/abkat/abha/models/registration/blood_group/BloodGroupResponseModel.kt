package com.myhindlab.abkat.model.registration.blood_group

import com.myhindlab.abkat.model.registration.blood_group.Output
import com.google.gson.annotations.SerializedName


data class BloodGroupResponseModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)