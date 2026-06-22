package com.myhindlab.abkat.abha.models.context_notify

import com.google.gson.annotations.SerializedName


data class CareContext (

  @SerializedName("patientReference"     ) var patientReference     : String? = null,
  @SerializedName("careContextReference" ) var careContextReference : String? = null

)