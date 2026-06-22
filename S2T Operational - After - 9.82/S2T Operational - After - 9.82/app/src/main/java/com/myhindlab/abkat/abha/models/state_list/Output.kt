package com.myhindlab.abkat.abha.models.state_list

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("StateID"           ) var StateID          : String? = null,
  @SerializedName("StateLGDCode"      ) var StateLGDCode     : String? = null,
  @SerializedName("StateName_English" ) var StateNameEnglish : String? = null,
  @SerializedName("StateName_Local"   ) var StateNameLocal   : String? = null,
  @SerializedName("StateOrUT"         ) var StateOrUT        : String? = null,
  @SerializedName("Census2001Code"    ) var Census2001Code   : String? = null,
  @SerializedName("Census2011Code"    ) var Census2011Code   : String? = null

)