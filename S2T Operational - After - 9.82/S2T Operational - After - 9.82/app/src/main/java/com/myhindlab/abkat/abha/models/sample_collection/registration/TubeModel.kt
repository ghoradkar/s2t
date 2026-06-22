package com.myhindlab.abkat.abha.models.sample_collection.registration

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TubeModel(
    @SerializedName("Orderid") var Orderid: String,
    @SerializedName("TubeCount") var TubeCount: Int,
    @Expose @SerializedName("TubeContent") var TubeContent: String,
    @SerializedName("Tubeid") var TubeId: Int,
    @SerializedName("Orderdate") var OrderDate: String,
    @SerializedName("Facilitycode") var Facilitycode: Int,
    @SerializedName("Labcode") var Labcode: Int,
    @SerializedName("CreatedBy") var CreatedBy: Int,
) : Serializable