package com.phonegap.WPIAS.doctor_AnsweredCheck

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.dataClass.MyAnswerCaseInfo
import com.phonegap.WPIAS.dataClass.MyAnswerInfo
import com.phonegap.WPIAS.dataClass.NewCaseInfo
import com.phonegap.WPIAS.publicObject.PubVariable
import com.phonegap.WPIAS.user_Main.MainActivity
import kotlinx.android.synthetic.main.activity_doctor_answered_check.*
import kotlinx.android.synthetic.main.title_bar_darkblue.*

class DoctorAnsweredCheckActivity : RootActivity() {

    lateinit var patientCase : MyAnswerCaseInfo

    lateinit var answerInfo: MyAnswerInfo

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_answered_check)

        SetTransparentBar()

        initActivity()

    }

    //액티비티 시작 설정
    fun initActivity(){

        btn_back.setOnClickListener { onBackPressed() }

        if(intent.hasExtra("patientCase")){

            patientCase = intent.getSerializableExtra("patientCase") as MyAnswerCaseInfo

        }else{
            finish()
            startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        if(intent.hasExtra("answerInfo")){
            answerInfo = intent.getSerializableExtra("answerInfo") as MyAnswerInfo
        }

        txt_title.text = answerInfo.title

        Glide.with(this)
            .load(patientCase.imageurl1)
            .into(burnedImage)

        closeImage.setOnClickListener {

            Glide.with(this)
                .load(patientCase.imageurl1)
                .into(burnedImage)

            closeImage.setBackgroundResource(R.color.windows_blue)
            closeImage.setTextColor(ContextCompat.getColor(this, R.color.white))
            overviewImage.setBackgroundResource(R.drawable.btn_stroke)
            overviewImage.setTextColor(ContextCompat.getColor(this, R.color.windows_blue))

        }

        overviewImage.setOnClickListener {

            Glide.with(this)
                .load(patientCase.imageurl2)
                .into(burnedImage)

            overviewImage.setBackgroundResource(R.color.windows_blue)
            overviewImage.setTextColor(ContextCompat.getColor(this, R.color.white))
            closeImage.setBackgroundResource(R.drawable.btn_stroke)
            closeImage.setTextColor(ContextCompat.getColor(this, R.color.windows_blue))

        }

        patientQuestionContents.text = patientCase.contents

        doctorName.text = PubVariable.userInfo.nickname

        doctorDept.text = PubVariable.userInfo.remark.replace('_', ' ')

        //의사답변 어떻게?
        doctorAnswer.text = patientCase.answercontents

        //리뷰확인 어떻게?
        if(patientCase.feedbacktime.isEmpty()){
            patientReview.text = "사용자가 리뷰를 작성하지 않았습니다"
        }else{
            patientReview.text = patientCase.feedbacktext
        }

    }

}