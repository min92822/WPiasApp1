package com.phonegap.WPIAS.doctor_Main

import android.os.Bundle
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity

class DoctorMainActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_main)

        SetTransparentBar()

    }

}