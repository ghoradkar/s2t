package com.myhindlab.abkat.model.registration.solitair_test_mandatory

import com.google.gson.annotations.SerializedName


data class Output(

    @SerializedName("RateRelatedId") var RateRelatedId: Int? = null,
    @SerializedName("CityCode") var CityCode: Int? = null,
    @SerializedName("IsSpecialRateBillable") var IsSpecialRateBillable: Boolean? = null,
    @SerializedName("IsActive") var IsActive: Boolean? = null,
    @SerializedName("ValidFrom") var ValidFrom: String? = null,
    @SerializedName("ValidTo") var ValidTo: String? = null,
    @SerializedName("servicecode") var servicecode: Int? = null

)