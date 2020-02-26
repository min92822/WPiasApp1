package com.phonegap.WPIAS.public_function

import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.phonegap.WPIAS.restApi.ApiUtill
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FCM {

//    data class FCMInteraction(@SerializedName("to") @Expose var to : String,
//                              @SerializedName("notification") @Expose var notification : Notification
//    )
    data class FCMInteraction(@SerializedName("to") @Expose var to : String,
                              @SerializedName("notification") @Expose var notification : Notification,
                              @SerializedName("data") @Expose var data : Data
    )
    data class Notification(var title : String, var body : String)
    data class Data(var USERTYPE : String, var PUSHTYPE : String/*, var param : PARAM*/)
    /*data class PARAM(var idkey : String)*/

    object TOPIC{
        val WpiasAll = "WpiasAll"
        val NewQuestion = "NewQuestion"
    }

    object UserType{
        val USER = "USER"
        val DOCTOR = "DOCTOR"
    }

    object PushType{
        val USER_MYQUESTION = "USER_MYQUESTION"
        val USER_FEEDBACKREPLY = "USER_FEEDBACKREPLY"
        val DOCTOR_NEWQUESTION = "DOCTOR_NEWQUESTION"
        val DOCTOR_REQUESTION = "DOCTOR_REQUESTION"
    }

    object function
    {
        fun SendMsgToTarget(TARGET:String, MSG:String, USERTYPE: String, PUSHTYPE : String)
        {
            var NOTIFICTION = Notification("WPIAS", MSG)
            var FCM_BODY =
                FCMInteraction(TARGET, NOTIFICTION, Data(USERTYPE, PUSHTYPE))

            ApiUtill().sendFCM().sendFCM(FCM_BODY).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("onFailure")
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    println("onResponse")

                    if(response.isSuccessful){
                        println("success")
                    }
                    else
                    {
                        println("fail")
                    }
                }
            })
        }


        fun SendMsgToTopic(TOPIC:String, MSG:String, USERTYPE: String, PUSHTYPE: String)
        {
            var NOTIFICTION = Notification("WPIAS", MSG)
            var FCM_BODY = FCMInteraction(
                "/topics/${TOPIC}",
                NOTIFICTION,
                Data(USERTYPE, PUSHTYPE)
            )

            ApiUtill().sendFCM().sendFCM(FCM_BODY).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("onFailure")
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    println("onResponse")

                    if(response.isSuccessful){
                        println("success")
                    }
                    else
                    {
                        println("fail")
                    }
                }
            })
        }

        fun TopicSetting(USERTYPE:String, SWITCH1:String, SWITCH2:String)
        {
            if(SWITCH1 == "On")
            {
                FirebaseMessaging.getInstance().subscribeToTopic(TOPIC.WpiasAll)
                    .addOnCompleteListener { task ->
                        println("${TOPIC.WpiasAll} 구독성공")
                    }
            }
            else
            {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC.WpiasAll)
                    .addOnCompleteListener { task ->
                        println("${TOPIC.WpiasAll} 구독 해제")
                    }
            }

            if(USERTYPE == "DOCTOR")
            {
                if(SWITCH2 == "On")
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(TOPIC.NewQuestion)
                        .addOnCompleteListener { task ->
                            println("${TOPIC.NewQuestion} 구독성공")
                        }
                }
                else
                {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC.NewQuestion)
                        .addOnCompleteListener { task ->
                            println("${TOPIC.NewQuestion} 구독 해제")
                        }
                }
            }
        }
    }




}