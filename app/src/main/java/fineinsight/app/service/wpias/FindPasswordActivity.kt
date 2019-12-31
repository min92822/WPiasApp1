package fineinsight.app.service.wpias

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_find_password.*
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.title_bar_grey.*

class FindPasswordActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_password)

        SetDarkBar()
        titleSetting()
        findPassword()
    }

    fun titleSetting(){

        txt_title.text = "비밀번호 초기화"
        btn_back.setOnClickListener { onBackPressed() }

    }

    fun findPassword(){

        btn_find_password.setOnClickListener {

            if (txt_find_pw_email.text.isNullOrEmpty()) {
                failureAlert("실패", "이메일을 입력해주세요!")

            } else {

                Loading(ProgressBar, ProgressBg, true)

                FirebaseAuth.getInstance().sendPasswordResetEmail(txt_find_pw_email.text.toString())
                    .addOnSuccessListener {
                        Loading(ProgressBar, ProgressBg, false)
                        successAlert()

                    }
                    .addOnFailureListener { e ->
                        Loading(ProgressBar, ProgressBg, false)
                        when (e.localizedMessage) {
                            "The email address is badly formatted." -> {
                                failureAlert("실패", "유효한 이메일을 입력해주세요!")

                            }
                            "There is no user record corresponding to this identifier. The user may have been deleted." -> {
                                failureAlert("실패", "존재하지 않는 계정입니다.")

                            }
                            else -> {
                                failureAlert("실패", "메일 전송을 실패하였습니다.")
                            }
                        }
                    }

            }
        }
    }

    fun successAlert(){
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_ck)

        title.text = "성공"
        sub.text = "비밀번호 초기화 메일을 보냈습니다."

        btn_left.visibility = View.GONE

        btn_right.text = "확인"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        btn_right.setOnClickListener {
            startActivity(Intent(this@FindPasswordActivity, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            dialog.dismiss()
        }

        dialog.show()
    }

    fun failureAlert(titleText:String, subText:String){
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

        title.text = titleText
        sub.text = subText

        btn_left.visibility = View.GONE

        btn_right.text = "확인"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        btn_right.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // 화면 터치시 키보드 내리기
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

        return true
    }
}
