package com.myhindlab.abkat.abha.models

import com.google.gson.annotations.SerializedName

data class GenerateOTP(
    @SerializedName("aadhaar") val aadhaar: String,
    @SerializedName("consent_approve") val boolean: Boolean
)
