package com.phonegap.WPIAS.doctor_Main

import android.os.Bundle
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import kotlinx.android.synthetic.main.activity_doctor_main.*

class DoctorMainActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_main)

        SetTransparentBar()

        initEvent()

    }

    //뷰 이벤트 리스너 등록
    fun initEvent(){

        btnPatientQuestion.setOnClickListener {



        }

        btn_setting.setOnClickListener {



        }

        btnNewQuestion.setOnClickListener {



        }

    }

}