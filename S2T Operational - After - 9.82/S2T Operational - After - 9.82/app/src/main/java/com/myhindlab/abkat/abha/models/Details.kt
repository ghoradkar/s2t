package com.myhindlab.abkat.abha.models

import com.google.gson.annotations.SerializedName


data class Details(

    @SerializedName("message") var message: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("attribute") var attribute: Attribute? = null

)