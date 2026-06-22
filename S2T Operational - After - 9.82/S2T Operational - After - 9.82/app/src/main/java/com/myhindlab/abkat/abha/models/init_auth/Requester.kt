package com.myhindlab.abkat.abha.models.init_auth

import com.google.gson.annotations.SerializedName


data class Requester(

    @SerializedName("type") var type: String? = null,
    @SerializedName("id") var id: String? = null

)