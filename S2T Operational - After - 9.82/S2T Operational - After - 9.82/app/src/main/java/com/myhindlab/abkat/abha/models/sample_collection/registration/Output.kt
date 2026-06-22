package com.myhindlab.abkat.model.sample_collection.registration

import com.google.gson.annotations.SerializedName


data class Output(

    @SerializedName("Facilityname") var Facilityname: String? = "",
    @SerializedName("FacilityCode") var FacilityCode: Int? = null,
    @SerializedName("TRF_P") var TRFP: Int? = null,
    @SerializedName("Tubecount_P") var TubecountP: Int? = null,
    @SerializedName("TRF_R") var TRFR: Int? = null,
    @SerializedName("Tubecount_R") var TubecountR: Int? = null,
    @SerializedName("Tubecount_L") var TubecountL: Int? = null,
    @SerializedName("TotalRegsitrationPatient") var TotalRegsitrationPatient: Int? = null,
    @SerializedName("TRFcount_L")
    var TRFcountL: Int? = null,
    var isChecked: Boolean = false

)