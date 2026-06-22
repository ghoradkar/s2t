package com.myhindlab.abkat.model.registration.blood_group

import com.google.gson.annotations.SerializedName


data class Output (

  @SerializedName("ID"   )      var ID  :     String? = null,    
  @SerializedName("Blood Group" )   var BloodGroup   : String? = null

)