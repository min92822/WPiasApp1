package com.phonegap.WPIAS.user_Setting

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.phonegap.WPIAS.LoginActivity
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.publicObject.PubVariable
import com.phonegap.WPIAS.public_function.FCM
import com.phonegap.WPIAS.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.title_bar_grey.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        SetDarkBar()

        init()

        settingInfo()

    }

    //이벤트 및 타이틀 바 텍스트 설정
    fun init(){

        txt_title.text = "설정페이지"

        btnLogoutWrapper.setOnClickListener {

            LOGOUT(PubVariable.uid)
        }

        btnChangePwWrapper.setOnClickListener {

            startActivity(Intent(this@SettingActivity, ChangePasswordActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

        }

        withDrawal.setOnClickListener {

            startActivity(Intent(this@SettingActivity, WithdrawalActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

        }

        notice.setOnCheckedChangeListener { buttonView, isChecked ->

            if(buttonView.isPressed){

                updateSwitch1(isChecked)

            }

        }

        reply.setOnCheckedChangeListener { buttonView, isChecked ->

            if(buttonView.isPressed){

                updateSwitch2(isChecked)

            }

        }

        btn_back.setOnClickListener {

            onBackPressed()

        }

    }

    //사용자 정보 세팅
    fun settingInfo(){

        userNickName.text = PubVariable.userInfo.nickname
        userEmail.text = PubVariable.userInfo.email
        userBirth.text = PubVariable.userInfo.birthday
        if(PubVariable.userInfo.gender == "M"){
            userGender.text = "남자"
        }else{
            userGender.text = "여자"
        }

        notice.isChecked = PubVariable.userInfo.switch1 == "On"
        reply.isChecked = PubVariable.userInfo.switch2 == "On"

        //사용자 계정
        if (PubVariable.userInfo.usertype=="USER")
        {
            lblSwitch1.text = "공지사항"
            lblSwitch2.text = "답변알림"
        }
        else//의사 계정
        {
            lblSwitch1.text = "공지사항"
            lblSwitch2.text = "질문등록"
        }

    }

    //공지알림 스위치 업데이트
    fun updateSwitch1(isChecked : Boolean){

        var map = HashMap<String, String>()

        if(isChecked) { map["SWITCH1"] = "On" }
        else{ map["SWITCH1"] = "Off" }
        map["IDKEY"] = PubVariable.uid

        ApiUtill().getUPDATE_SWITCH1().update_switch1(map).enqueue(object : Callback<String>{

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful && response.body()!! == "S"){
                    PubVariable.userInfo.switch1 = map["SWITCH1"].toString()
                    FCM.function.TopicSetting(PubVariable.userInfo.usertype, PubVariable.userInfo.switch1, PubVariable.userInfo.switch2)
                }else{
                    println("알림거부")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("오류")
            }

        })

    }

    //답글알림 스위치 업데이트
    fun updateSwitch2(isChecked : Boolean){

        var map = HashMap<String, String>()

        if(isChecked) { map["SWITCH2"] = "On" }
        else{ map["SWITCH2"] = "Off" }
        map["IDKEY"] = PubVariable.uid


        ApiUtill().getUPDATE_SWITCH2().update_switch2(map).enqueue(object : Callback<String>{

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful && response.body()!! == "S"){
                    PubVariable.userInfo.switch2 = map["SWITCH2"].toString()
                    FCM.function.TopicSetting(PubVariable.userInfo.usertype, PubVariable.userInfo.switch1, PubVariable.userInfo.switch2)
                }else{
                    println("알림거부")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("오류")
            }

        })

    }

    fun LOGOUT(UUID:String)
    {
        var map = HashMap<String, String>()


        map["IDKEY"] = UUID


        ApiUtill().getUPDATE_TOKENLOGOUT().update_tokenlogout(map).enqueue(object : Callback<String>{

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful && response.body()!! == "S"){

                    PubVariable.init()
                    FCM.function.TopicSetting("Doctor", "Off", "Off")
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this@SettingActivity, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                    finish()

                }else{
                    PubVariable.init()
                    FCM.function.TopicSetting("Doctor", "Off", "Off")
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this@SettingActivity, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                    finish()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                PubVariable.init()
                FCM.function.TopicSetting("Doctor", "Off", "Off")
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@SettingActivity, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                finish()
            }

        })
    }

}
