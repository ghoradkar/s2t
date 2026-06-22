package com.tbocwwb.csc_healthcare.abha.models.abha_search

import com.google.gson.annotations.SerializedName
import com.tbocwwb.csc_healthcare.abha.models.abha_search.ABHA


data class ABHASearchResponse (

  @SerializedName("txnId" ) var txnId : String?         = null,
  @SerializedName("ABHA"  ) var ABHA  : ArrayList<ABHA> = arrayListOf()

)