package com.myhindlab.abkat.abha.models.sms_notify

import com.google.gson.annotations.SerializedName


data class SMSNotifyRequestModel(

    @SerializedName("requestId") var requestId: String? = null,
    @SerializedName("timestamp") var timestamp: String? = null,
    @SerializedName("notification") var notification: Notification? = Notification()

)