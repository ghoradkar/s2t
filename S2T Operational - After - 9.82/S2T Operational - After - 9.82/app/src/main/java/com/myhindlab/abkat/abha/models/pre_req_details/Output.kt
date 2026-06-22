package com.myhindlab.abkat.abha.models.pre_req_details

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("requestId"     ) var requestId     : String? = null,
  @SerializedName("transactionId" ) var transactionId : String? = null,
  @SerializedName("time_stamp"    ) var timeStamp     : String? = null,
  @SerializedName("authtoken"     ) var authtoken     : String? = null,
  @SerializedName("urllink"       ) var urllink       : String? = null,
  @SerializedName("response"      ) var response      : String? = null,
  @SerializedName("CreatedOn"     ) var CreatedOn     : String? = null

)