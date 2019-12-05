package fineinsight.app.service.wpias.publicObject

import fineinsight.app.service.wpias.dataClass.UserInfo

object PubVariable {

    var uid = ""
    var userInfo = UserInfo("", "", "", "", "", "", "", "", "", "", "")

    fun init(){
        uid = ""
        userInfo = UserInfo("", "", "", "", "", "", "", "", "", "", "")
    }

}