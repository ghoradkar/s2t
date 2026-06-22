package com.myhindlab.abkat.expense_module.models.advance_detail_response

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class AdvanceDetailsResponseModel(

    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("output") var output: ArrayList<Output> = arrayListOf()

) : Serializable