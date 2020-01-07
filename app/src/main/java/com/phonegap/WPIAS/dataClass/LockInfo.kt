package com.phonegap.WPIAS.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LockInfo(@SerializedName("ISLOCK") @Expose var islock : String,
                    @SerializedName("LOCKTIME") @Expose var locktime : String,
                    @SerializedName("LOCKUSER") @Expose var lockuser : String)