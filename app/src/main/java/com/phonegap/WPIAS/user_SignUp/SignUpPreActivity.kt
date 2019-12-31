package com.phonegap.WPIAS.user_SignUp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.CheckBox
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import kotlinx.android.synthetic.main.activity_sign_up_pre.*
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.in_sign_up_pre_1.*
import kotlinx.android.synthetic.main.in_sign_up_pre_2.*
import kotlinx.android.synthetic.main.in_sign_up_pre_3.*
import kotlinx.android.synthetic.main.title_bar_darkblue.*

class SignUpPreActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_pre)

        SetTransparentBar()
        titleSetting()
        termsDetail()
        checkVal()
        allChecked()
    }

    fun titleSetting() {
        txt_title.text = "약관동의"
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    // 약관보기 & 닫기
    fun termsDetail() {

        btn_sign_up_pre_open_1.setOnClickListener {
            in_sign_up_pre_1.visibility = View.VISIBLE
            in_sign_up_pre_1.setOnClickListener { }
        }
        btn_sign_up_pre_close_1.setOnClickListener { in_sign_up_pre_1.visibility = View.GONE }


        btn_sign_up_pre_open_2.setOnClickListener {
            in_sign_up_pre_2.visibility = View.VISIBLE
            in_sign_up_pre_2.setOnClickListener { }
        }
        btn_sign_up_pre_close_2.setOnClickListener { in_sign_up_pre_2.visibility = View.GONE }


        btn_sign_up_pre_open_3.setOnClickListener {
            in_sign_up_pre_3.visibility = View.VISIBLE
            in_sign_up_pre_3.setOnClickListener { }
        }
        btn_sign_up_pre_close_3.setOnClickListener { in_sign_up_pre_3.visibility = View.GONE }

    }

    // 가입하기
    fun allChecked(){

        btn_sign_up_pre.setOnClickListener {
            if (chk_sign_up_pre_all.isChecked) {
                startActivity(
                    Intent(this@SignUpPreActivity, SignUpActivity::class.java).setFlags(
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
                    )
                )
            } else {
                notCheckedAlert()
            }
        }

    }

    override fun onBackPressed() {

        if (in_sign_up_pre_1.visibility == View.VISIBLE || in_sign_up_pre_2.visibility == View.VISIBLE || in_sign_up_pre_3.visibility == View.VISIBLE) {
            in_sign_up_pre_1.visibility = View.GONE
            in_sign_up_pre_2.visibility = View.GONE
            in_sign_up_pre_3.visibility = View.GONE
        } else {
            super.onBackPressed()
        }

    }

    // 모든 약관 체크시 전체 동의 체크
    fun checkVal() {
        var chkarr = arrayListOf<CheckBox>(chk_sign_up_pre_1, chk_sign_up_pre_2, chk_sign_up_pre_3)

        chk_sign_up_pre_all.setOnClickListener {
            for (item in chkarr) {
                item.isChecked = chk_sign_up_pre_all.isChecked
            }
        }

        chk_sign_up_pre_1.setOnClickListener(res(chkarr))
        chk_sign_up_pre_2.setOnClickListener(res(chkarr))
        chk_sign_up_pre_3.setOnClickListener(res(chkarr))

    }

    fun res(chkarr: ArrayList<CheckBox>): View.OnClickListener {
        return View.OnClickListener {
            chk_sign_up_pre_all.isChecked =
                chkarr[0].isChecked && chkarr[1].isChecked && chkarr[2].isChecked
        }
    }


    // 약관미동의 alert
    fun notCheckedAlert() {

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

        title.text = "약관동의"
        sub.text = "약관을 동의해주세요."

        btn_left.visibility = View.GONE

        btn_right.text = "OK"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        btn_right.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }
}
