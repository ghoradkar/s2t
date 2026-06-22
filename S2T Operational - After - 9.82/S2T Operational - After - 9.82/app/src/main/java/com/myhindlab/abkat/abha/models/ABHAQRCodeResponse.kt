package com.myhindlab.abkat.abha.models

import com.google.gson.annotations.SerializedName


data class ABHAQRCodeResponse (

  @SerializedName("hidn"     )     var hidn     :     String? = null,    
  @SerializedName("phr"      )     var phr      :     String? = null,    
  @SerializedName("name"     )     var name     :     String? = null,    
  @SerializedName("gender"   )     var gender   :     String? = null,    
  @SerializedName("statelgd" )     var statelgd :     String? = null,    
  @SerializedName("distlgd"  )     var distlgd  :     String? = null,    
  @SerializedName("dob"      )     var dob      :     String? = null,    
  @SerializedName("address"  )     var address  :     String? = null,    
  @SerializedName("state name" )   var      statename    : String? = null,
  @SerializedName("dist name" )   var      distname    : String? = null,
  @SerializedName("mobile"   )     var mobile   :     String? = null     

)