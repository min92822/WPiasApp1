package com.phonegap.WPIAS

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.phonegap.WPIAS.dataClass.UserInfo
import com.phonegap.WPIAS.doctor_AnsweredQuestion.DoctorAnsweredActivity
import com.phonegap.WPIAS.doctor_Main.DoctorMainActivity
import com.phonegap.WPIAS.doctor_Question.DoctorNewQuestionActivity
import com.phonegap.WPIAS.publicObject.PubVariable
import com.phonegap.WPIAS.publicObject.UserToken
import com.phonegap.WPIAS.public_function.FCM
import com.phonegap.WPIAS.restApi.ApiUtill
import com.phonegap.WPIAS.user_Main.MainActivity
import com.phonegap.WPIAS.user_MyQuestion.MyQuestionActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class LaunchActivity : RootActivity() {

    var authStateListener : FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        GetTOKEN()
        loginStateCheck()

    }

    fun GetTOKEN()
    {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    println("TokenAccessFail")
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                UserToken.token = token
            })
    }

    //로그인 상태 체크 앱 실행시 메인으로 바로 진행할지 여부를 결정하는 펑션
    fun loginStateCheck(){

        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            var user = firebaseAuth.currentUser

            if(user != null) {
                getUserinfo(user.uid)
            }else{
                startActivity(
                    Intent(this@LaunchActivity, LoginActivity::class.java).setFlags(
                        Intent.FLAG_ACTIVITY_SINGLE_TOP))
            }

        }

    }

    override fun onResume() {
        super.onResume()
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener!!)
    }

    override fun onPause() {
        super.onPause()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener!!)
    }

    //로그인 후 파이어베이스에서 가져온 uuid를 파라미터로 azure 서버로 보내 회원 정보를 가져온다
    fun getUserinfo(UUID:String){

        PubVariable.uid = UUID

        var map = HashMap<String,String>()

        map["IDKEY"] = UUID
        map["TOKEN"] = UserToken.token
        map["OS"] = "Android"

        val USERTYPE : String
        val PUSHTYPE : String

        if(intent.hasExtra("USERTYPE") && intent.hasExtra("PUSHTYPE")) {
            USERTYPE = intent.getStringExtra("USERTYPE")!!
            PUSHTYPE = intent.getStringExtra("PUSHTYPE")!!
        }else{
            USERTYPE = ""
            PUSHTYPE = ""
        }

        ApiUtill().getSELECT_USERWITHTOKENOS().select_userwithtokenos(map).enqueue(object :
            Callback<ArrayList<UserInfo>> {

            override fun onResponse(
                call: Call<ArrayList<UserInfo>>,
                response: Response<ArrayList<UserInfo>>
            ) {
                if(response.isSuccessful){
                    if(response.body()?.size != 0){
                        PubVariable.userInfo = response.body()!![0]

                        FCM.function.TopicSetting(
                            response.body()!![0].usertype,
                            response.body()!![0].switch1,
                            response.body()!![0].switch2
                        )
                        //유저 로그인 상태인 경우
                        if(PubVariable.userInfo.usertype == "USER") {
                            //푸시알림이 있을경우
                            //else to MainActivity
                            when(PUSHTYPE){
                                FCM.PushType.USER_MYQUESTION -> {
                                    startActivity(
                                        Intent(
                                            this@LaunchActivity,
                                            MyQuestionActivity::class.java
                                        ).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                    )
                                }
                                FCM.PushType.USER_FEEDBACKREPLY -> {
                                    startActivity(
                                        Intent(
                                            this@LaunchActivity,
                                            MyQuestionActivity::class.java
                                        ).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                    )
                                }
                                else -> {
                                    startActivity(
                                        Intent(
                                            this@LaunchActivity,
                                            MainActivity::class.java
                                        ).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                    )
                                }
                            }
                        //의사 로그인 상태인 경우
                        }else{
                            //푸시알림이 있을경우
                            //else to DoctorMainActivity
                            when(PUSHTYPE){
                                FCM.PushType.DOCTOR_NEWQUESTION -> {
                                    startActivity(
                                        Intent(
                                            this@LaunchActivity,
                                            DoctorNewQuestionActivity::class.java
                                        ).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                    )
                                }
                                FCM.PushType.DOCTOR_REQUESTION -> {
                                    startActivity(
                                        Intent(
                                            this@LaunchActivity,
                                            DoctorAnsweredActivity::class.java
                                        ).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                    )
                                }
                                else -> {
                                    startActivity(
                                        Intent(
                                            this@LaunchActivity,
                                            DoctorMainActivity::class.java
                                        ).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                    )
                                }
                            }
                        }
                    }else{
                        startActivity(
                            Intent(this@LaunchActivity, LoginActivity::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_SINGLE_TOP))
                        Toast.makeText(this@LaunchActivity, "해당하는 계정이 없습니다.", Toast.LENGTH_LONG).show()
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<UserInfo>>, t: Throwable) {
                startActivity(
                    Intent(this@LaunchActivity, LoginActivity::class.java).setFlags(
                        Intent.FLAG_ACTIVITY_SINGLE_TOP))
                Toast.makeText(this@LaunchActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}