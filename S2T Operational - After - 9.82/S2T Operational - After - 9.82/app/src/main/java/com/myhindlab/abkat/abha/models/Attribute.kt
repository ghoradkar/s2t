package com.myhindlab.abkat.abha.models

import com.google.gson.annotations.SerializedName

data class Attribute(
    @SerializedName("key") val key: String,
    @SerializedName("value") val value: String,
)