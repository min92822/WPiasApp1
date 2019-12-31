package com.phonegap.WPIAS.user_Setting

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.phonegap.WPIAS.LoginActivity
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import kotlinx.android.synthetic.main.activity_change_pw.*
import kotlinx.android.synthetic.main.set_password_alert.*
import kotlinx.android.synthetic.main.title_bar_grey.*

class ChangePasswordActivity : RootActivity(){

    var popup = false

    var p1 = false
    var p2 = false
    var p3 = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pw)

        SetTransparentBar()

        init()

    }

    fun init(){

        txt_title.text = "비밀번호 변경"

        currentPwEvent()

        newPw()

        newPwCheck()

        pwChangeSubmitEvent()

        btn_back.setOnClickListener { onBackPressed() }

    }

    //비밀번호 변경 확인버튼 이벤트
    fun pwChangeSubmitEvent(){

        pwChangeSubmit.setOnClickListener {

            //1차로 각 에딧 텍스트가 비밀번호 조건에 맞는지 확인한다
            if(p1 && p2 && p3){
                setPassword()
            }else if(!p1){
                Toast.makeText(this, "현재 비밀번호가 6자리 미만입니다.", Toast.LENGTH_LONG).show()
            }else if(!p2){
                Toast.makeText(this, "새 비밀번호가 6자리 미만입니다.", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "새 비밀번호 확인이 6자리 미만입니다.", Toast.LENGTH_LONG).show()
            }

        }

    }

    //각 edittext들을 누를때마다 팝업 뜸
    fun pwAlert(view : View){

        popup = true

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.set_password_alert)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.bg_alert2)

        when(view.id){

            R.id.currentPw -> dialog.setPasswordAlertNotice.text = "현재 비밀번호를 입력해주세요!"
            R.id.toChangePw -> dialog.setPasswordAlertNotice.text = "바꾸실 비밀번호로 입력해주세요!"
            R.id.toChangePwCheck -> dialog.setPasswordAlertNotice.text = "바꾼 비밀번호를 다시 입력해주세요!"

        }

        dialog.setOnDismissListener {

            dialog = Dialog(this)
            popup = false

        }

//        if((view as EditText).text.isNotEmpty()){
//
//            dialog.setPasswordEditText.text = view.text
//
//        }

        dialog.submitWrapper.setOnClickListener {

            when(view.id){

                R.id.currentPw -> currentPw.text = dialog.setPasswordEditText.text
                R.id.toChangePw -> toChangePw.text = dialog.setPasswordEditText.text
                R.id.toChangePwCheck -> toChangePwCheck.text = dialog.setPasswordEditText.text

            }

            dialog.dismiss()

        }

        dialog.cancelWrapper.setOnClickListener {


            dialog.dismiss()

        }

        dialog.show()

    }

    fun currentPwEvent(){

        currentPw.setOnClickListener {

            pwAlert(it)

        }

        currentPw.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.length < 6){
                    p1 = false
                }else{
                    warnChangePw.visibility = View.GONE
                    p1 = true
                }
            }

        })

    }

    fun newPw(){

        toChangePw.setOnClickListener {

            pwAlert(it)

        }

        toChangePw.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.length < 6){
                    warnChangePw.visibility = View.VISIBLE
                    warnChangePw.text = "6자리 이상 입력해주세요."
                    p2 = false
                }else{
                    warnChangePw.visibility = View.GONE
                    p2 = true
                }
            }

        })

    }

    fun newPwCheck(){

        toChangePwCheck.setOnClickListener {

            pwAlert(it)

        }

        toChangePwCheck.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString() != toChangePw.text.toString()){
                    warnChangePwCheck.visibility = View.VISIBLE
                    warnChangePwCheck.text = "비밀번호가 일치하지 않습니다."
                    p3 = false
                }else{
                    warnChangePwCheck.visibility = View.GONE
                    p3 = true
                }
            }

        })

    }

    //파이어베이스 auth 비밀번호 재 설정 펑션
    fun setPassword(){

        var userEmail = FirebaseAuth.getInstance().currentUser!!.email

        var credential = EmailAuthProvider.getCredential(userEmail.toString(), currentPw.text.toString())

        FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential)
            .addOnCompleteListener{

                    it ->

                if(it.isSuccessful) {

                    if (toChangePw.text.toString() == toChangePwCheck.text.toString()) {
                        changePassword(toChangePw.text.toString())
                    } else {
                        Toast.makeText(this, "새 비밀번호가 일치하는지 확인해 주세요", Toast.LENGTH_LONG).show()
                    }

                } else {

                    Toast.makeText(this, "비밀번호를 다시 확인해주세요", Toast.LENGTH_LONG).show()

                }

            }

    }

    fun changePassword(password:String){

        FirebaseAuth.getInstance().currentUser!!.updatePassword(password).addOnCompleteListener {
                task ->
            if(task.isSuccessful){
                println("성공")
                FirebaseAuth.getInstance().signOut()
                startActivity(
                    Intent(this, LoginActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                finish()
            }else{
                println("실패")
            }
        }
    }

}