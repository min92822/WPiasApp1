package com.phonegap.WPIAS.publicObject

import com.phonegap.WPIAS.dataClass.UserInfo

object PubVariable {

    var uid = ""
    var userInfo = UserInfo("", "", "", "", "", "", "", "", "", "", "")

    fun init(){
        uid = ""
        userInfo =
            UserInfo("", "", "", "", "", "", "", "", "", "", "")
    }

}