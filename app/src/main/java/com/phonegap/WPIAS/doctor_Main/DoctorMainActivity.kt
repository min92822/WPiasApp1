package com.phonegap.WPIAS.doctor_Main

import android.content.Intent
import android.os.Bundle
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.doctor_AnsweredQuestion.DoctorAnsweredActivity
import com.phonegap.WPIAS.doctor_Question.DoctorNewQuestionActivity
import com.phonegap.WPIAS.user_Setting.SettingActivity
import kotlinx.android.synthetic.main.activity_doctor_main.*

class DoctorMainActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_main)

        SetTransparentBar()

        initActivity()

    }

    //액티비티가 시작될때
    fun initActivity(){

        btnAnsweredQuestionByCurrentDoctor.setOnClickListener {

            startActivity(Intent(this, DoctorAnsweredActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

        }

        btn_setting.setOnClickListener {

            startActivity(Intent(this, SettingActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

        }

        btnNewQuestion.setOnClickListener {

            startActivity(Intent(this, DoctorNewQuestionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

        }

    }

}