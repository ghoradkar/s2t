package com.myhindlab.abkat.model.sample_collection.get_runnerboy_mapped_visited

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("USERID"          ) var USERID          : Int?    = null,
  @SerializedName("USERNAME"        ) var USERNAME        : String? = null,
  @SerializedName("Name"            ) var Name            : String? = null,
  @SerializedName("MOBNO"           ) var MOBNO           : String? = null,
  @SerializedName("EMAILID"         ) var EMAILID         : String? = null,
  @SerializedName("DesgId"          ) var DesgId          : Int?    = null,
  @SerializedName("DesgName"        ) var DesgName        : String? = null,
  @SerializedName("MappedFacility"  ) var MappedFacility  : Int?    = null,
  @SerializedName("VisitedFacility" ) var VisitedFacility : Int?    = null

)