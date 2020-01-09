package com.phonegap.WPIAS.user_SignUp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.iterator
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.phonegap.WPIAS.LoginActivity
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.publicObject.UserToken
import com.phonegap.WPIAS.restApi.ApiUtill
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

        setDescendentViews(window.decorView.rootView)

    }

    fun viewSetting(){

        txt_title.text = "회원가입"
        btn_back.setOnClickListener {
            onBackPressed()
        }

        m_insertUserMap["OS"] = "Android"
        m_insertUserMap["SWITCH1"] = "On"
        m_insertUserMap["SWITCH2"] = "On"
        m_insertUserMap["TOKEN"] = UserToken.token
        m_insertUserMap["REMARK"] = "기타"



        // date picker 보이기 & 날짜 선택
        txt_sign_up_birth.setOnClickListener {
            wrap_datepicker.visibility = View.VISIBLE

            var today = Calendar.getInstance()
            var datePicker = findViewById(R.id.sign_up_datepicker) as DatePicker

            datePicker.maxDate = today.timeInMillis

            datePicker.init(0, 0, 0){ datePicker, year, month, day ->

                btn_sign_up_date.setOnClickListener {
                    txt_sign_up_birth.text = Editable.Factory.getInstance().newEditable("$year-${String.format("%02d", month+1)}-${String.format("%02d", day)}")
                    wrap_datepicker.visibility = View.GONE
                }
            }

            datePicker.updateDate(today.get(Calendar.YEAR), 0,1)
        }

        wrap_datepicker.setOnClickListener(null)


        // 회원가입 버튼
        btn_sign_up.setOnClickListener {
            Loading(ProgressBar, ProgressBg, true)
            emailVal()
        }


    }

    fun emailVal(){

        var email = txt_sign_up_email.text.toString()

        // 이메일 형식
        if(email.isNullOrEmpty()){
            // alert
            valAlert("이메일", "유효한 이메일을 입력해주세요!")

        } else {
                m_insertUserMap["EMAIL"] = email

                pwVal()
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

        Loading(ProgressBar, ProgressBg, false)

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

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
            .addOnSuccessListener {

                if(it != null){

                    m_insertUserMap["IDKEY"] = FirebaseAuth.getInstance().currentUser!!.uid

                    println("*****************"+m_insertUserMap)
                    azureSignUp()

                } else {
//                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }

                it.user!!.uid
            }
            .addOnFailureListener {e ->

                when (e.localizedMessage) {
                    "The email address is badly formatted." -> {
                        valAlert("이메일", "유효한 이메일을 입력해주세요!")

                    }
                    "The email address is already in use by another account." -> {
                        valAlert("이메일", "이미 사용중인 이메일 입니다!")

                    }
                    else -> {
                        valAlert("실패", "회원 가입에 실패하였습니다.")
                    }
                }

            }

    }

    fun azureSignUp(){

        ApiUtill().getINSERT_USER().insert_user(m_insertUserMap).enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if(response.body() == "S"){

                    // alert
                    successAlert()

                } else {
                    Toast.makeText(this@SignUpActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }


    // 회원가입 성공 alert - 로그인 화면 이동
    fun successAlert(){

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_ck)

        title.text = "성공"
        sub.text = "회원가입이 완료되었습니다."

        btn_left.visibility = View.GONE

        btn_right.text = "확인"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        btn_right.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
            finish()
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            Loading(ProgressBar, ProgressBg, false)
            dialog.dismiss()
        }

        dialog.show()

    }

    // 뒤로가기 시 날짜 선택창 닫기
    override fun onBackPressed() {

        if(wrap_datepicker.visibility == View.VISIBLE){
            wrap_datepicker.visibility = View.GONE

        } else {
            super.onBackPressed()

        }
    }

    //에딧 텍스트 아닌 부분 클릭시 키보드 사라지는 펑션
    fun hideKeyboard(){

        var imm = (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)

        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

        window?.decorView?.clearFocus()

    }

    //최상위 뷰 태그 및 하위 뷰 태그에 hideKeboard를 적용하는 펑션
    fun setDescendentViews(view : View){

        if(view !is EditText) {
            view.setOnTouchListener { v, event ->

                hideKeyboard()
                return@setOnTouchListener false

            }
        }

        if(view is RecyclerView){

            view.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                    hideKeyboard()
                }

                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    hideKeyboard()
                    return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                }
            })

        }

        if(view is ViewGroup){
            for(innerview in view) {
                setDescendentViews(innerview)
            }
        }

    }

}
