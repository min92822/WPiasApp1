package com.phonegap.WPIAS.doctor_AnsweredQuestion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.internal.LinkedTreeMap
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.dataClass.MyAnswerInfo
import com.phonegap.WPIAS.doctor_AnsweredQuestion.adapter.AnswerAdapter
import com.phonegap.WPIAS.doctor_Main.DoctorMainActivity
import com.phonegap.WPIAS.publicObject.PubVariable
import com.phonegap.WPIAS.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_doctor_answered.*
import kotlinx.android.synthetic.main.title_bar_darkblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorAnsweredActivity : RootActivity() {

    var myAnswerInfo = ArrayList<MyAnswerInfo>()
    var notAnswered = ArrayList<MyAnswerInfo>()
    var answered = ArrayList<MyAnswerInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_answered)

        SetTransparentBar()

        getCount()

        initActivity()

    }

    //액티비티 시작 설정
    fun initActivity(){

        txt_title.text = "내가 한 답변"

        reLoad.visibility = View.VISIBLE

        doctorName.text = PubVariable.userInfo.nickname

        dept.text = PubVariable.userInfo.remark.replace('_', ' ')

        btn_back.setOnClickListener { onBackPressed() }

        reLoad.setOnClickListener {
            getCount()
            loadQuestion()
        }

        loadQuestion()

        rdAnswerSelect()

        rdNotAnswered.isChecked = true

    }

    //라디오 버튼 이벤트 세팅
    fun rdAnswerSelect(){

        rdAnswered.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(
                    ContextCompat.getColor(this,
                        R.color.white
                    ))
                rdNotAnswered.setTextColor(
                    ContextCompat.getColor(this,
                        R.color.windows_blue
                    ))

                questionRecyclerView.layoutManager = LinearLayoutManager(this)
                questionRecyclerView.adapter = AnswerAdapter(answered)

            }else{
                buttonView.setTextColor(
                    ContextCompat.getColor(this,
                        R.color.windows_blue
                    ))
                rdNotAnswered.setTextColor(
                    ContextCompat.getColor(this,
                        R.color.white
                    ))
            }

        }

        rdNotAnswered.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(
                    ContextCompat.getColor(this,
                        R.color.white
                    ))
                rdAnswered.setTextColor(
                    ContextCompat.getColor(this,
                        R.color.windows_blue
                    ))

                questionRecyclerView.layoutManager = LinearLayoutManager(this)
                questionRecyclerView.adapter = AnswerAdapter(notAnswered)

            }else{
                buttonView.setTextColor(
                    ContextCompat.getColor(this,
                        R.color.windows_blue
                    ))
                rdAnswered.setTextColor(
                    ContextCompat.getColor(this,
                        R.color.white
                    ))
            }

        }

    }

    //의사 답변수 조회 펑션
    fun getCount(){

        viewVisibleControl(false)

        var map = HashMap<String, String>()

        map["IDKEY"] = PubVariable.userInfo.idkey

        ApiUtill().getSELECT_MYANSWERCOUNT().select_myanswercount(map).enqueue(object :
            Callback<Any> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Any>, response: Response<Any>) {

                if(response.isSuccessful){
                    var count = ((response.body()!! as ArrayList<*>)[0] as LinkedTreeMap<*, *>).values.toList()[0]
                    answerCount.text = "답변 수 : ${(count as Double).toInt()}"
                }else{
                    println(response.message())
                    println(response.code())
                }

            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                println(t.toString())
            }

        })

    }

    //서버 통신 완료되기까지 view visible을 컨트롤 한다
    fun viewVisibleControl(result : Boolean){
        if(result) {
            constraintLayout20.visibility = View.VISIBLE
            constraintLayout11.visibility = View.VISIBLE
            questionRecyclerView.visibility = View.VISIBLE
            Loading(ProgressBar, ProgressBg, !result)
        }else{
            constraintLayout20.visibility = View.GONE
            constraintLayout11.visibility = View.GONE
            questionRecyclerView.visibility = View.GONE
            Loading(ProgressBar, ProgressBg, !result)
        }
    }

    // 진행중인 질문 및 현재 의사가 작성완료한 질문을 불러오는 펑션
    fun loadQuestion(){

        answered.clear()
        notAnswered.clear()

        var map = HashMap<String, String>()

        map["ANSWERDOC"] = PubVariable.uid

        ApiUtill().getSELECT_MYANSWER200106().select_myanswer200106(map).enqueue(object : Callback<ArrayList<MyAnswerInfo>>{

            override fun onResponse(call: Call<ArrayList<MyAnswerInfo>>,response: Response<ArrayList<MyAnswerInfo>>) {

                viewVisibleControl(true)

                if(response.isSuccessful){

                    myAnswerInfo = response.body()!!

                    for(info in myAnswerInfo){

                        if(info.prostatus == "A"){
                            answered.add(info)
                        }else{
                            notAnswered.add(info)
                        }

                    }

                }else{

                    println(response.code())
                    println(response.message())

                }

            }

            override fun onFailure(call: Call<ArrayList<MyAnswerInfo>>, t: Throwable) {
                viewVisibleControl(true)
                println(t.toString())
            }

        })

    }

    override fun onBackPressed() {
        startActivity(Intent(this, DoctorMainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
    }

    override fun onResume() {
        getCount()
        loadQuestion()
        super.onResume()
    }

}