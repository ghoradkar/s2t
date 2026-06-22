package com.myhindlab.abkat.abha.models.abha_error

import com.google.gson.annotations.SerializedName


data class ABHAErrorResponseModel (

  @SerializedName("error" ) var error : Error? = Error()

)