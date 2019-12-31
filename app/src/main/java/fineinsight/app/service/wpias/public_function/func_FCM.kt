package fineinsight.app.service.wpias.public_function

import android.widget.Toast
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import fineinsight.app.service.wpias.restApi.ApiUtill
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FCM {

    data class FCMInteraction(@SerializedName("to") @Expose var to : String,
                              @SerializedName("notification") @Expose var notification : Notification)

    data class Notification(var title : String, var body : String)

    object TOPIC{
        val WpiasAll = "WpiasAll"
        val NewQuestion = "NewQuestion"
    }


    object function
    {
        fun SendMsgToTarget(TARGET:String, MSG:String)
        {
            var NOTIFICTION = Notification("WPIAS", MSG)
            var FCM_BODY = FCMInteraction(TARGET, NOTIFICTION)

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


        fun SendMsgToTopic(TOPIC:String, MSG:String)
        {
            var NOTIFICTION = Notification("WPIAS", MSG)
            var FCM_BODY = FCMInteraction("/topics/${TOPIC}", NOTIFICTION)

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
    }




}