package com.phonegap.WPIAS.doctor_Question

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.internal.LinkedTreeMap
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.dataClass.NewQuestionInfo
import com.phonegap.WPIAS.doctor_Main.DoctorMainActivity
import com.phonegap.WPIAS.doctor_Question.adapters.NewQuestionAdapter
import com.phonegap.WPIAS.publicObject.PubVariable
import com.phonegap.WPIAS.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_doctor_new_question.*
import kotlinx.android.synthetic.main.activity_doctor_new_question.ProgressBar
import kotlinx.android.synthetic.main.activity_doctor_new_question.ProgressBg
import kotlinx.android.synthetic.main.activity_doctor_new_question.questionRecyclerView
import kotlinx.android.synthetic.main.title_bar_darkblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DoctorNewQuestionActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_new_question)

        SetTransparentBar()

        getCount()

        getNewQuestion()

        initActivity()

    }

    //액티비티가 시작될때 기본 설정
    fun initActivity(){

        btn_back.setOnClickListener { onBackPressed() }

        txt_title.text = "신규 질문"

        reLoad.visibility = View.VISIBLE

        doctorName.text = PubVariable.userInfo.nickname

        dept.text = PubVariable.userInfo.remark.replace('_', ' ')

        reLoad.setOnClickListener {
            getCount()
            getNewQuestion()
        }

    }

    //신규질문 조회 펑션
    fun getNewQuestion(){

        ApiUtill().getSELECT_NEWQUESTION().select_newquestion().enqueue(object : Callback<ArrayList<NewQuestionInfo>>{

            override fun onResponse(call: Call<ArrayList<NewQuestionInfo>>, response: Response<ArrayList<NewQuestionInfo>>) {
                if(response.isSuccessful){
                    questionRecyclerView.layoutManager = LinearLayoutManager(this@DoctorNewQuestionActivity)
                    questionRecyclerView.adapter = NewQuestionAdapter(response.body()!!)
                    viewVisibleControl(true)
                }else{
                    println()
                }
            }

            override fun onFailure(call: Call<ArrayList<NewQuestionInfo>>, t: Throwable) {
                println(t.toString())
            }

        })

    }

    //의사 답변수 조회 펑션
    fun getCount(){

        viewVisibleControl(false)

        var map = HashMap<String, String>()

        map["IDKEY"] = PubVariable.userInfo.idkey

        ApiUtill().getSELECT_MYANSWERCOUNT().select_myanswercount(map).enqueue(object : Callback<Any>{

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
            questionRecyclerView.visibility = View.VISIBLE
            Loading(ProgressBar, ProgressBg, !result)
        }else{
            questionRecyclerView.visibility = View.GONE
            Loading(ProgressBar, ProgressBg, !result)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, DoctorMainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
    }

    override fun onResume() {
        getCount()
        getNewQuestion()
        super.onResume()
    }

}