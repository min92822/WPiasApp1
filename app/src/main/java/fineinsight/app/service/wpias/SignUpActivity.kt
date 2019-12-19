package fineinsight.app.service.wpias

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.Window
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import fineinsight.app.service.wpias.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.title_bar_darkblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class SignUpActivity : RootActivity() {

    /*
    IDKEY       FirebaseAuth UUID
    EMAIL       가입 이메일
    NICKNAME    이름
    GENDER      남자 -> "M"    여자 -> "F"
    BIRTHDAY    생년월일 yyyy-MM-dd
    OS          "Android"
    SWITCH1     On
    SWITCH2     On
    TOKEN       FCM TOKEN
    REMARK      기타
    */

    var m_insertUserMap = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        SetTransparentBar()

        viewSetting()

    }

    fun viewSetting(){

        txt_title.text = "회원가입"
        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_sign_up.setOnClickListener {
            emailVal()
        }

        txt_sign_up_birth.setOnClickListener {
            wrap_datepicker.visibility = View.VISIBLE
        }

        sign_up_datepicker.init(Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
                datePicker, year, month, day ->

            var month = month +1

            btn_sign_up_date.setOnClickListener {
                txt_sign_up_birth.text = Editable.Factory.getInstance().newEditable("$year-${String.format("%02d", month)}-$day")
                wrap_datepicker.visibility = View.GONE
            }

        }


        m_insertUserMap["OS"] = "Android"
        m_insertUserMap["SWITCH1"] = "On"
        m_insertUserMap["SWITCH2"] = "On"
        m_insertUserMap["TOKEN"] = ""
        m_insertUserMap["REMARK"] = "기타"

    }

    fun emailVal(){

        var email = txt_sign_up_email.text.toString()
        var emailPattern = Regex("[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}")

        // 이메일 형식
        if(email.isNullOrEmpty()){
            // alert
            valAlert("이메일", "유효한 이메일을 입력해주세요!")

        } else {

            if (!email.matches(emailPattern)){
                // alert
                valAlert("이메일", "유효한 이메일을 입력해주세요!")

            } else {
                m_insertUserMap["EMAIL"] = email

                pwVal()
            }
        }
    }

    fun pwVal(){
        var pw = txt_sign_up_pw.text.toString()
        var pw2= txt_sign_up_pw_comfirm.text.toString()

        if (pw != pw2){
            // alert
            valAlert("비밀번호", "비밀번호가 일치하지 않습니다.")

        } else if(pw.length < 6 || pw.length < 6) {
            // alert
            valAlert("비밀번호", "비밀번호는 6글자 이상으로 설정해주세요!")

        } else if(pw == pw2 && pw.length >= 6) {
            nameVal()

        }
    }

    fun nameVal(){

        var name = txt_sign_up_name.text.toString()

        if(name.isNullOrEmpty() || name.length <= 2 || name.length > 8){
            // alert
            valAlert("닉네임", "닉네임은 2글자 이상 8글자 이하로 설정해주세요!")
        } else {
            m_insertUserMap["NICKNAME"] = name

            birthVal()

        }

    }

    fun birthVal(){

        var birth = txt_sign_up_birth.text.toString()

        if(birth.isNullOrEmpty()){
            // alert
            valAlert("생년월일", "생년월일을 선택해주세요!")

        } else {
            m_insertUserMap["BIRTHDAY"] = birth

            genderVal()
        }
    }

    fun genderVal(){

        if(chk_sign_up_male.isChecked){
            m_insertUserMap["GENDER"] = "M"
        } else if(chk_sign_up_female.isChecked){
            m_insertUserMap["GENDER"] = "F"
        }

        signUpEmailId()

    }

    fun valAlert(strTitle:String, strSub:String){
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_ck_x)

        title.text = strTitle
        sub.text = strSub

        btn_left.visibility = View.GONE

        btn_right.text = "확인"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        btn_right.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    fun signUpEmailId(){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(txt_sign_up_email.text.toString(), txt_sign_up_pw.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    m_insertUserMap["IDKEY"] = FirebaseAuth.getInstance().currentUser!!.uid

                    println(m_insertUserMap)
                    //azureSignUp()

                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

    }

    fun azureSignUp(){

        ApiUtill().getINSERT_USER().insert_user(m_insertUserMap).enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if(response.message() == "S"){

                    // alert

                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))


                } else {
                    Toast.makeText(this@SignUpActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}
