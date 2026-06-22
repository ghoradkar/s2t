package com.myhindlab.abkat.abha.models.confirm_auth

import com.google.gson.annotations.SerializedName


data class ConfirmAuthRequestModel (

  @SerializedName("requestId"     ) var requestId     : String?     = null,
  @SerializedName("timestamp"     ) var timestamp     : String?     = null,
  @SerializedName("transactionId" ) var transactionId : String?     = null,
  @SerializedName("credential"    ) var credential    : Credential? = Credential()

)