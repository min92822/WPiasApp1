package com.phonegap.WPIAS.user_Setting

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.phonegap.WPIAS.LoginActivity
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import kotlinx.android.synthetic.main.activity_withdrawal.*
import kotlinx.android.synthetic.main.set_password_alert.*
import kotlinx.android.synthetic.main.title_bar_grey.*

class WithdrawalActivity : RootActivity() {

    var p1 = false
    var popup = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdrawal)

        SetTransparentBar()

        init()

    }

    fun init(){

        txt_title.text = "회원탈퇴"

        btn_back.setOnClickListener { onBackPressed() }

        withDrawalPasswordEvent()

        submitWithDrawal()

    }

    //회원탈퇴 버튼 클릭 시 이벤트
    fun submitWithDrawal(){

        withDrawalSubmit.setOnClickListener {

            if(p1){

                beforeWithDrawal()

            }else{

                Toast.makeText(this, "비밀번호가 6자리 미만입니다.", Toast.LENGTH_LONG).show()

            }

        }

    }

    //에딧 텍스트가 비밀번호 조건에 맞는지 1차로 확인하는 펑션
    fun withDrawalPasswordEvent(){

        withDrawalPassword.setOnClickListener {

            pwAlert()

        }

        withDrawalPassword.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                p1 = s!!.length >= 6

            }

        })

    }

    //edittext들을 누를때 팝업
    fun pwAlert(){

        popup = true

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.set_password_alert)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.bg_alert2)

        dialog.setOnDismissListener {

            dialog = Dialog(this)
            popup = false

        }

        dialog.submitWrapper.setOnClickListener {

            withDrawalPassword.text = dialog.setPasswordEditText.text
            dialog.dismiss()

        }

        dialog.cancelWrapper.setOnClickListener {


            dialog.dismiss()

        }

        dialog.show()

    }

    //회원 탈퇴 전 회원여부 확인하는 펑션
    fun beforeWithDrawal(){

        var credential = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().currentUser!!.email!!, withDrawalPassword.text.toString())

        FirebaseAuth.getInstance().currentUser!!.reauthenticate(credential).addOnCompleteListener {

            if(it.isSuccessful){

                withDrawal()

            }else{

                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()

            }

        }

    }

    //회원 탈퇴하는 펑션
    fun withDrawal(){

        FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener {

            if(it.isSuccessful){

                Toast.makeText(this,"아이디 삭제가 완료되었습니다.", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                startActivity(
                    Intent(this, LoginActivity::class.java)
                        .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

            }else{

                Toast.makeText(this, "삭제를 다시 시도해주세요", Toast.LENGTH_LONG).show()

            }

        }

    }

}