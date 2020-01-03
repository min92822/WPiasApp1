package com.phonegap.WPIAS.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DistrictInfo(@SerializedName("DISTRICTCD") @Expose var districtcd : String,
                        @SerializedName("DISTRICTNM") @Expose var districtnm : String)