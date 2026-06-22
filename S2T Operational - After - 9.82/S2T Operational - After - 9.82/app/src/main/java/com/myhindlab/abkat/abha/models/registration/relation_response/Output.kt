package com.myhindlab.abkat.model.registration.relation_response

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("Relation"   ) var Relation   : String? = null,
  @SerializedName("DispOrder"  ) var DispOrder  : Int?    = null,
  @SerializedName("Remark"     ) var Remark     : String? = null,
  @SerializedName("ActiveFlag" ) var ActiveFlag : String? = null

)