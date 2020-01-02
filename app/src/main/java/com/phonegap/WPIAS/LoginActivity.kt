package com.phonegap.WPIAS

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.phonegap.WPIAS.dataClass.UserInfo
import com.phonegap.WPIAS.publicObject.PubVariable
import com.phonegap.WPIAS.publicObject.UserToken
import com.phonegap.WPIAS.public_function.FCM
import com.phonegap.WPIAS.restApi.ApiUtill
import com.phonegap.WPIAS.user_Main.MainActivity
import com.phonegap.WPIAS.user_SignUp.SignUpPreActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set

class LoginActivity : RootActivity(){

//    var authStateListener : FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupPermissions()

        PubVariable.init()

        SetDarkBar()

        loginSubmit()

        signUp()

        findPassword()

    }

    //로그인
    fun login(id : String, pw : String){

        Loading(ProgressBar, ProgressBg, true)
        
        FirebaseAuth.getInstance().signInWithEmailAndPassword(id, pw).addOnSuccessListener {
            
            PubVariable.uid = it.user?.uid!!

            getUserinfo(it.user?.uid!!)

        }.addOnFailureListener {

            Loading(ProgressBar, ProgressBg, false)
            Toast.makeText(this, "계정정보를 확인해주세요", Toast.LENGTH_LONG).show()

        }

    }

    //로그인 버튼 동작
    fun loginSubmit(){

        loginSubmit.setOnClickListener {

            if(loginId.text.isNotEmpty() && loginPw.text.isNotEmpty()){

                login(loginId.text.toString(), loginPw.text.toString())

            }else{

                Toast.makeText(this, "계정정보를 확인해주세요", Toast.LENGTH_LONG).show()

            }

        }

    }

    //로그인 후 파이어베이스에서 가져온 uuid를 파라미터로 azure 서버로 보내 회원 정보를 가져온다
    fun getUserinfo(UUID:String){

        var map = HashMap<String,String>()

        println("USERTOKEN: ${UserToken.token}")

        map["IDKEY"] = UUID
        map["TOKEN"] = UserToken.token
        map["OS"] = "Android"

        ApiUtill().getSELECT_USERWITHTOKENOS().select_userwithtokenos(map).enqueue(object : Callback<ArrayList<UserInfo>>{

            override fun onResponse(
                call: Call<ArrayList<UserInfo>>,
                response: Response<ArrayList<UserInfo>>
            ) {

                if(response.isSuccessful){

                    Loading(ProgressBar, ProgressBg, false)
                    
                    if(response.body()?.size != 0){

                        FCM.function.TopicSetting(response.body()!![0].usertype, response.body()!![0].switch1, response.body()!![0].switch2)

                        PubVariable.userInfo = response.body()!![0]

                        //                        if(PubVariable.userInfo.usertype == "USER") {

                        startActivity(
                            Intent(
                                this@LoginActivity,
                                MainActivity::class.java
                            ).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        )

//                        }else{
//
//                            startActivity(
//                                Intent(
//                                    this@LaunchActivity,
//                                    DoctorMainActivity::class.java
//                                ).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                            )
//
//                        }

                    }else{

                        Toast.makeText(this@LoginActivity, "해당하는 계정이 없습니다.", Toast.LENGTH_LONG).show()

                    }

                }

            }

            override fun onFailure(call: Call<ArrayList<UserInfo>>, t: Throwable) {
                Loading(ProgressBar, ProgressBg, false)
                Toast.makeText(this@LoginActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }

        })

    }

    fun signUp(){

        btn_sign_up.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpPreActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

    }

    fun findPassword(){

        btn_find_pw.setOnClickListener {
            startActivity(Intent(this@LoginActivity, FindPasswordActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

    }

    //퍼미션 체크
    fun setupPermissions() {

        val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(this, permissions, 0)

    }

    override fun onBackPressed() {
//        super.onBackPressed()
        CloseAlert(this)

    }

}