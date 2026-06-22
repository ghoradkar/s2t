package com.myhindlab.abkat.abha.models.confirm_auth

import com.google.gson.annotations.SerializedName

data class Identifier(
    @SerializedName("type"        ) var type        : String? = null,
    @SerializedName("value"      ) var value      : String? = null,
)