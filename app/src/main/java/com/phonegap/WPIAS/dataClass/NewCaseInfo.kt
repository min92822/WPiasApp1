package com.phonegap.WPIAS.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewCaseInfo(
    @SerializedName("CKEY") @Expose var ckey: String,
    @SerializedName("CNUMBER") @Expose var cnumber: String,
    @SerializedName("CASEDATE") @Expose var casedate: String,
    @SerializedName("DIRECTION") @Expose var direction: String,
    @SerializedName("IMAGEURL1") @Expose var imageurl1: String,
    @SerializedName("IMAGEURL2") @Expose var imageurl2: String,
    @SerializedName("CONTENTS") @Expose var contents: String,
    @SerializedName("CASESTATUS") @Expose var casestatus: String
) : Serializable