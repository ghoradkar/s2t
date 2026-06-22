package com.myhindlab.abkat.abha.models.add_context

import com.google.gson.annotations.SerializedName
import com.myhindlab.abkat.abha.models.add_context.CareContexts


data class Patient (

    @SerializedName("referenceNumber" ) var referenceNumber : String?                 = null,
    @SerializedName("display"         ) var display         : String?                 = null,
    @SerializedName("careContexts"    ) var careContexts    : ArrayList<CareContexts> = arrayListOf(),
    @SerializedName("hiType"          ) var hiType          : String?                 = null,
    @SerializedName("count"           ) var count           : Int?                    = null

)