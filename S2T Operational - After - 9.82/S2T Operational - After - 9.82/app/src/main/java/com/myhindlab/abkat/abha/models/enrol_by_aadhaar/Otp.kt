package com.myhindlab.abkat.abha.models.enrol_by_aadhaar

import com.google.gson.annotations.SerializedName


data class Otp(
    @SerializedName("timeStamp") var timeStamp: String? = null,
    @SerializedName("txnId") var txnId: String? = null,
    @SerializedName("otpValue") var otpValue: String? = null,
    @SerializedName("mobile") var mobile: String? = null

)