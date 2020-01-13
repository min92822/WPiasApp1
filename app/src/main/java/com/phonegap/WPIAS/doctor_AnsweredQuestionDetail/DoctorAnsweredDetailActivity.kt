package com.phonegap.WPIAS.doctor_AnsweredQuestionDetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.dataClass.MyAnswerCaseInfo
import com.phonegap.WPIAS.dataClass.MyAnswerInfo
import com.phonegap.WPIAS.doctor_QuestionDetail.adapter.DoctorAnsweredDetailAdapter
import com.phonegap.WPIAS.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_doctor_answered_detail.*
import kotlinx.android.synthetic.main.title_bar_darkblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class DoctorAnsweredDetailActivity : RootActivity(){

    lateinit var questionInfo : MyAnswerInfo

    lateinit var myAnswerCaseInfo : ArrayList<MyAnswerCaseInfo>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_answered_detail)

        SetTransparentBar()

        initActivity()

    }

    //액티비티 시작 설정
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun initActivity(){

        txt_title.text = "내가 한 답변"

        btn_back.setOnClickListener { onBackPressed() }

        if(intent.hasExtra("questionInfo")){

            questionInfo = intent.getSerializableExtra("questionInfo") as MyAnswerInfo

            doctorAnswerDetailTitle.text = questionInfo.title

            var d = SimpleDateFormat("yyyy-MM-dd").parse(questionInfo.burndate)!!

            doctorAnswerDetailDate.text = "상처발생일 ${SimpleDateFormat("yyyy-MM-dd").format(d)}"

            if(questionInfo.gender == "M") {

                doctorAnswerDetailGender.setImageResource(R.drawable.s_male)

            }else{

                doctorAnswerDetailGender.setImageResource(R.drawable.s_female)

            }

            doctorAnswerDetailAge.text = questionInfo.age

            caseImage(questionInfo.burnstyle, questionInfo.burndetailcd)

            doctorAnswerDetailBurnTxt.text = "${questionInfo.burnnm}화상"

            bodyPartsImage(questionInfo.bodystyle)

            doctorNewQuestionDetailBody.text = questionInfo.bodydetailnm

            loadPatientCase()

        }

    }

    //burn style별 이미지 적용하는 펑션
    fun caseImage(burnStyle : String, burnDetail : String) {

        when (burnStyle) {

            "001" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.yultang_1)
                    }
                    "002" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.yultang_2)
                    }
                    "003" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.yultang_3)
                    }
                    "004" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.yultang_4)
                    }
                    "005" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.yultang_6)
                    }
                    "006" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.yultang_7_2)
                    }
                    "007" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.yultang_7)
                    }
                    "999" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "002" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.hwayum_1)
                    }
                    "002" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.hwayum_2)
                    }
                    "003" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.hwayum_3)
                    }
                    "004" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.hwayum_4)
                    }
                    "999" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "003" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.jungi_1)
                    }
                    "999" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "004" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.jubchok_1)
                    }
                    "002" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.jubchok_2)
                    }
                    "003" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.jubchok_3)
                    }
                    "004" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.jubchok_4)
                    }
                    "999" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "005" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.juon_1)
                    }
                    "002" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.juon_2)
                    }
                    "003" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.juon_3)
                    }
                    "999" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "006" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.hwahag_1)
                    }
                    "002" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.hwahag_2)
                    }
                    "003" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.hwahag_3)
                    }
                    "999" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "007" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.junggi_1)
                    }
                    "002" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.junggi_2)
                    }
                    "999" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "008" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.machar_1)
                    }
                    "002" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.machar_2)
                    }
                    "999" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "009" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.hatbit_1)
                    }
                    "999" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "010" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.heubib_1)
                    }
                    "999" -> {
                        doctorAnswerDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

        }

    }

    //상처 부위별 이미지 적용하는 펑션
    fun bodyPartsImage(bodyStyle: String) {

        when (bodyStyle) {
            "001" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_mori_f)
            "002" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_akke_f)
            "003" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_gasum_f)
            "004" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_gasum_b)
            "005" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_bae_f)
            "006" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_bae_b)
            "007" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_8_f)
            "008" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_sun_f)
            "009" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_umbu)
            "010" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_umbu)
            "011" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_dari_f)
            "012" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_bar_f)
            "013" -> doctorAnswerDetailBui.setImageResource(R.drawable.s_gasum_f)
        }

    }

    //질문 케이스 조회하는 펑션
    fun loadPatientCase(){

        var map = HashMap<String, String>()

        map["CKEY"] = questionInfo.qkey

        Loading(ProgressBar, ProgressBg, true)

        ApiUtill().getSELECT_MYANSWERCASE().select_myanswercase(map).enqueue(object :
            Callback<ArrayList<MyAnswerCaseInfo>> {

            override fun onResponse(call: Call<ArrayList<MyAnswerCaseInfo>>, response: Response<ArrayList<MyAnswerCaseInfo>>) {

                if(response.isSuccessful){
                    doctorAnswerDetailWrapper.visibility = View.VISIBLE
                    myAnswerCaseInfo = response.body()!!
                    recyclerDoctorAnswerDetail.layoutManager = LinearLayoutManager(this@DoctorAnsweredDetailActivity)
                    recyclerDoctorAnswerDetail.adapter = DoctorAnsweredDetailAdapter(myAnswerCaseInfo, questionInfo)
                }else{

                    doctorAnswerDetailWrapper.visibility = View.GONE
                    println(response.message())
                    println(response.code())
                }

            }

            override fun onFailure(call: Call<ArrayList<MyAnswerCaseInfo>>, t: Throwable) {
                doctorAnswerDetailWrapper.visibility = View.GONE
                println(t.toString())
            }

        })

    }

}