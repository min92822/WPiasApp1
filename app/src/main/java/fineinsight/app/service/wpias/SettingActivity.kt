package fineinsight.app.service.wpias

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import fineinsight.app.service.wpias.publicObject.PubVariable
import fineinsight.app.service.wpias.restApi.ApiUtill
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

            PubVariable.init()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@SettingActivity, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            finish()
        }

        btnChangePwWrapper.setOnClickListener {



        }

        memberOut.setOnClickListener {



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
                }else{
                    println("알림거부")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("오류")
            }

        })

    }

}
