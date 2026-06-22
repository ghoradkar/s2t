package com.myhindlab.abkat.abha.models.confirm_auth

import com.google.gson.annotations.SerializedName


data class Demographic(

    @SerializedName("name") var name: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("dateOfBirth") var dateOfBirth: String? = null,
    @SerializedName("identifier") var identifier: Identifier? = null

)