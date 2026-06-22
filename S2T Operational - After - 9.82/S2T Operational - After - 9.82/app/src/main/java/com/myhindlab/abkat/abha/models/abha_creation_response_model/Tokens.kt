package com.myhindlab.abkat.abha.models.abha_creation_response_model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Tokens(

    @SerializedName("token") var token: String? = null,
    @SerializedName("expiresIn") var expiresIn: Int? = null,
    @SerializedName("refreshToken") var refreshToken: String? = null,
    @SerializedName("refreshExpiresIn") var refreshExpiresIn: Int? = null

) : Serializable