package com.phonegap.WPIAS

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.Serializable

class FcmService : FirebaseMessagingService() {

    var USERTYPE = ""
    var PUSHTYPE = ""

    override fun onMessageReceived(p0: RemoteMessage?) {
        p0?.data?.isNotEmpty().let{
            USERTYPE = p0!!.data!!["USERTYPE"]!!
            PUSHTYPE = p0!!.data!!["PUSHTYPE"]!!

            startActivity(Intent(this, LaunchActivity::class.java)
                .putExtra("USERTYPE", USERTYPE)
                .putExtra("PUSHTYPE", PUSHTYPE)
                .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }
    }

}