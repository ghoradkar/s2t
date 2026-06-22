package com.erp.hllconnect.abha.models.abha_creation_response_via_aadhaar_demo_auth

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class JwtResponse(

    @SerializedName("token") var token: String? = null,
    @SerializedName("expiresIn") var expiresIn: Int? = null,
    @SerializedName("refreshToken") var refreshToken: String? = null,
    @SerializedName("refreshExpiresIn") var refreshExpiresIn: Int? = null

) : Serializable