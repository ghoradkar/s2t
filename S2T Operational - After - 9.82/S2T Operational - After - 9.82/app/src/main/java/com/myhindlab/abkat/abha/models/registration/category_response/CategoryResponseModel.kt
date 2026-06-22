package com.myhindlab.abkat.model.registration.category_response

import com.myhindlab.abkat.model.registration.category_response.Output
import com.google.gson.annotations.SerializedName


data class CategoryResponseModel (

  @SerializedName("status"  ) var status  : String?           = null,
  @SerializedName("message" ) var message : String?           = null,
  @SerializedName("output"  ) var output  : ArrayList<Output> = arrayListOf()

)