package com.myhindlab.abkat.abha.models

import com.google.gson.annotations.SerializedName


data class CreateHealthIdErrResponseModel(

    @SerializedName("code") var code: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("details") var details: ArrayList<Details> = arrayListOf()

)