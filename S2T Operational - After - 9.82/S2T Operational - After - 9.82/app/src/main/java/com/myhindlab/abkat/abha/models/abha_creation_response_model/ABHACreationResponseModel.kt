package com.myhindlab.abkat.abha.models.abha_creation_response_model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ABHACreationResponseModel(

    @SerializedName("message") var message: String? = null,
    @SerializedName("txnId") var txnId: String? = null,
    @SerializedName("tokens") var tokens: Tokens? = Tokens(),
    @SerializedName("ABHAProfile") var ABHAProfile: ABHAProfile? = ABHAProfile(),
    @SerializedName("isNew") var isNew: Boolean? = null,
    @SerializedName("createdBy") var createdBy: Int? = null,
    @SerializedName("campId") var campId: Int? = null,
    @SerializedName("existingABHAAddress") var existingABHAAddress: String? = null,

    ) : Serializable