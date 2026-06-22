package com.myhindlab.abkat.abha.models

import com.google.gson.annotations.SerializedName

data class ABHASessionModel(
    @SerializedName("clientId") val clientId: String,
    @SerializedName("clientSecret") val clientSecret: String,
    @SerializedName("grantType") val grantType: String
)