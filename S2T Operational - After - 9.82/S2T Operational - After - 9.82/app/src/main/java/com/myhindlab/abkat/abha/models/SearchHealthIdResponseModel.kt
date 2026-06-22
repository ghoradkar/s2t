package com.myhindlab.abkat.abha.models

import com.google.gson.annotations.SerializedName


data class SearchHealthIdResponseModel(

    @SerializedName("healthId") var healthId: String? = null,
    @SerializedName("healthIdNumber") var healthIdNumber: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("authMethods") var authMethods: ArrayList<String> = arrayListOf(),
    @SerializedName("tags") var tags: String? = null

)