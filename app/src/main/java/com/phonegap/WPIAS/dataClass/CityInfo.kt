package com.phonegap.WPIAS.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CityInfo(@SerializedName("CITYCD") @Expose var citycd : String,
                   @SerializedName("CITYNM") @Expose var citynm : String)