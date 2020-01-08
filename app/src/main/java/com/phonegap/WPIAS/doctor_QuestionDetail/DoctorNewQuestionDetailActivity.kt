package com.phonegap.WPIAS.doctor_QuestionDetail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.dataClass.MycaseInfo
import com.phonegap.WPIAS.dataClass.NewCaseInfo
import com.phonegap.WPIAS.dataClass.NewQuestionInfo
import com.phonegap.WPIAS.doctor_QuestionDetail.adapter.DoctorNewQuestionDetailAdapter
import com.phonegap.WPIAS.restApi.ApiUtill
import com.phonegap.WPIAS.user_Main.MainActivity
import kotlinx.android.synthetic.main.activity_doctor_new_question_detail.*
import kotlinx.android.synthetic.main.title_bar_darkblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class DoctorNewQuestionDetailActivity : RootActivity() {

    lateinit var newQuestionInfo : NewQuestionInfo

    lateinit var newCaseInfo : ArrayList<NewCaseInfo>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_new_question_detail)

        SetTransparentBar()

        initActivity()

    }

    //액티비티 시작 기본 설정
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun initActivity(){

        btn_back.setOnClickListener { onBackPressed() }

        txt_title.text = "신규 질문"

        if(intent.hasExtra("newQuestionInfo")){

            newQuestionInfo = intent.getSerializableExtra("newQuestionInfo") as NewQuestionInfo

            doctorNewQuestionDetailTitle.text = newQuestionInfo.title

            var d = SimpleDateFormat("yyyy-MM-dd").parse(newQuestionInfo.burndate)!!

            doctorNewQuestionDetailDate.text = "상처발생일 ${SimpleDateFormat("yyyy-MM-dd").format(d)}"

            if(newQuestionInfo.gender == "M") {

                doctorNewQuestionDetailGender.setImageResource(R.drawable.s_male)

            }else{

                doctorNewQuestionDetailGender.setImageResource(R.drawable.s_female)

            }

            doctorNewQuestionDetailAge.text = newQuestionInfo.age

            caseImage(newQuestionInfo.burnstyle, newQuestionInfo.burndetailcd)

            doctorNewQuestionDetailBurnTxt.text = "${newQuestionInfo.burnnm}화상"

            bodyPartsImage(newQuestionInfo.bodystyle)

            doctorNewQuestionDetailBody.text = newQuestionInfo.bodydetailnm

            loadPatientCase()

        }else{

            finish()
            startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

        }

    }

    //burn style별 이미지 적용하는 펑션
    fun caseImage(burnStyle : String, burnDetail : String) {

        when (burnStyle) {

            "001" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.yultang_1)
                    }
                    "002" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.yultang_2)
                    }
                    "003" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.yultang_3)
                    }
                    "004" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.yultang_4)
                    }
                    "005" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.yultang_6)
                    }
                    "006" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.yultang_7_2)
                    }
                    "007" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.yultang_7)
                    }
                    "999" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "002" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.hwayum_1)
                    }
                    "002" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.hwayum_2)
                    }
                    "003" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.hwayum_3)
                    }
                    "004" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.hwayum_4)
                    }
                    "999" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "003" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.jungi_1)
                    }
                    "999" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "004" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.jubchok_1)
                    }
                    "002" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.jubchok_2)
                    }
                    "003" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.jubchok_3)
                    }
                    "004" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.jubchok_4)
                    }
                    "999" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "005" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.juon_1)
                    }
                    "002" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.juon_2)
                    }
                    "003" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.juon_3)
                    }
                    "999" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "006" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.hwahag_1)
                    }
                    "002" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.hwahag_2)
                    }
                    "003" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.hwahag_3)
                    }
                    "999" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "007" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.junggi_1)
                    }
                    "002" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.junggi_2)
                    }
                    "999" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "008" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.machar_1)
                    }
                    "002" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.machar_2)
                    }
                    "999" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "009" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.hatbit_1)
                    }
                    "999" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

            "010" -> {
                when (burnDetail) {
                    "001" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.heubib_1)
                    }
                    "999" -> {
                        doctorNewQuestionDetailBurn.setImageResource(R.drawable.and)
                    }
                }
            }

        }

    }

    //상처 부위별 이미지 적용하는 펑션
    fun bodyPartsImage(bodyStyle: String) {

        when (bodyStyle) {
            "001" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_mori_f)
            "002" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_akke_f)
            "003" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_gasum_f)
            "004" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_gasum_b)
            "005" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_bae_f)
            "006" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_bae_b)
            "007" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_8_f)
            "008" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_sun_f)
            "009" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_umbu)
            "010" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_umbu)
            "011" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_dari_f)
            "012" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_bar_f)
            "013" -> doctorNewQuestionDetailBui.setImageResource(R.drawable.s_gasum_f)
        }

    }

    //질문 케이스 조회하는 펑션
    fun loadPatientCase(){

        var map = HashMap<String, String>()

        map["CKEY"] = newQuestionInfo.qkey

        Loading(ProgressBar, ProgressBg, true)

        ApiUtill().getSELECT_NEWCASE().select_newcase(map).enqueue(object : Callback<ArrayList<NewCaseInfo>>{

            override fun onResponse(call: Call<ArrayList<NewCaseInfo>>, response: Response<ArrayList<NewCaseInfo>>) {

                if(response.isSuccessful){
                    doctorNewQuestionDetailWrapper.visibility = View.VISIBLE
                    newCaseInfo = response.body()!!
                    recyclerDoctorNewQuestionDetail.layoutManager = LinearLayoutManager(this@DoctorNewQuestionDetailActivity)
                    recyclerDoctorNewQuestionDetail.adapter = DoctorNewQuestionDetailAdapter(newCaseInfo, newQuestionInfo)
                }else{

                    doctorNewQuestionDetailWrapper.visibility = View.GONE
                    println(response.message())
                    println(response.code())
                }

            }

            override fun onFailure(call: Call<ArrayList<NewCaseInfo>>, t: Throwable) {
                doctorNewQuestionDetailWrapper.visibility = View.GONE
                println(t.toString())
            }

        })

    }

}