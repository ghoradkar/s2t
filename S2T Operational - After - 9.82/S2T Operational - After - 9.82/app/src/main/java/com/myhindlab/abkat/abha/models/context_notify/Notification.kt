package com.myhindlab.abkat.abha.models.context_notify

import com.google.gson.annotations.SerializedName


data class Notification (

    @SerializedName("patient"     ) var patient     : Patient?          = Patient(),
    @SerializedName("careContext" ) var careContext : CareContext?      = CareContext(),
    @SerializedName("hiTypes"     ) var hiTypes     : ArrayList<String> = arrayListOf(),
    @SerializedName("date"        ) var date        : String?           = null,
    @SerializedName("hip"         ) var hip         : Hip?              = Hip()

)