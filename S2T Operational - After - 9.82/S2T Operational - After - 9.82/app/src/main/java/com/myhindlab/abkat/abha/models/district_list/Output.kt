package com.myhindlab.abkat.abha.models.district_list

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("DistrictID"           ) var DistrictID          : String? = null,
  @SerializedName("StateLGDCode"         ) var StateLGDCode        : String? = null,
  @SerializedName("StateName"            ) var StateName           : String? = null,
  @SerializedName("DistrictLGDCode"      ) var DistrictLGDCode     : String? = null,
  @SerializedName("DistrictName_English" ) var DistrictNameEnglish : String? = null,
  @SerializedName("DistrictName_Local"   ) var DistrictNameLocal   : String? = null,
  @SerializedName("Hierarchy"            ) var Hierarchy           : String? = null,
  @SerializedName("ShortNameOfDistrict"  ) var ShortNameOfDistrict : String? = null,
  @SerializedName("Census2001Code"       ) var Census2001Code      : String? = null,
  @SerializedName("Census2011Code"       ) var Census2011Code      : String? = null,
  @SerializedName("PesaStatus"           ) var PesaStatus          : String? = null

)