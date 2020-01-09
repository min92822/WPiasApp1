package com.phonegap.WPIAS.doctor_AnsweredQuestionDetail

import android.os.Bundle
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import kotlinx.android.synthetic.main.title_bar_darkblue.*

class DoctorAnsweredDetailActivity : RootActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_answered_detail)

        SetTransparentBar()

        initActivity()

    }

    //액티비티 시작 설정
    fun initActivity(){

        txt_title.text = "내가 한 답변"

        btn_back.setOnClickListener { onBackPressed() }

    }

}