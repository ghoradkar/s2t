package com.myhindlab.abkat.abha.models.context_notify

import com.google.gson.annotations.SerializedName


data class CareContextNotifyRequestModel (

  @SerializedName("notification" ) var notification : Notification? = Notification()

)