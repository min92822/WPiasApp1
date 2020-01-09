package com.phonegap.WPIAS.doctor_AnsweredQuestion.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.dataClass.MyAnswerInfo
import com.phonegap.WPIAS.doctor_AnsweredQuestionDetail.DoctorAnsweredDetailActivity
import kotlinx.android.synthetic.main.answer_question.view.*
import java.text.SimpleDateFormat

class AnswerAdapter(var arr : ArrayList<MyAnswerInfo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        context = parent.context

        var view = LayoutInflater.from(parent.context).inflate(R.layout.answer_question, parent, false)

        return AnswerViewHolder(view)

    }

    override fun getItemCount(): Int {
        return arr.size
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as AnswerViewHolder

        caseImage(holder, arr[position].burnstyle, arr[position].burndetailcd)

        holder.burnStyleNm.setTextColor(ContextCompat.getColor(context!!, R.color.windows_blue))

        holder.burnStyleNm.text = "${arr[position].burnnm}화상"

        holder.questionInsertDate.setTextColor(ContextCompat.getColor(context!!, R.color.warm_grey_three))

        holder.questionInsertDate.text = "최초 작성일 : ${SimpleDateFormat("yyyy년 MM월 dd일").format(SimpleDateFormat("yyyyMMddkkmmss").parse(arr[position].insertdate)?.time)}"

        holder.patientName.text = arr[position].nickname

        holder.questionTitle.text = arr[position].title

        if(arr[position].prostatus == "A"){
            holder.stateAnswer.text = "답변 완료"
        }else{
            holder.stateAnswer.text = "답변 대기"
        }

        holder.answerCount.text = "${arr[position].answercount.toInt()}/${arr[position].totalcount.toInt()}"

    }

    //burn style별 이미지 적용하는 펑션
    fun caseImage(holder : AnswerViewHolder, burnStyle : String, burnDetail : String){

        when(burnStyle){

            "001" -> {
                when(burnDetail){
                    "001" -> holder.burnStyle.setImageResource(R.drawable.yultang_1)
                    "002" -> holder.burnStyle.setImageResource(R.drawable.yultang_2)
                    "003" -> holder.burnStyle.setImageResource(R.drawable.yultang_3)
                    "004" -> holder.burnStyle.setImageResource(R.drawable.yultang_4)
                    "005" -> holder.burnStyle.setImageResource(R.drawable.yultang_6)
                    "006" -> holder.burnStyle.setImageResource(R.drawable.yultang_7_2)
                    "007" -> holder.burnStyle.setImageResource(R.drawable.yultang_7)
                    "999" -> holder.burnStyle.setImageResource(R.drawable.and)
                }
            }

            "002" -> {
                when(burnDetail){
                    "001" -> holder.burnStyle.setImageResource(R.drawable.hwayum_1)
                    "002" -> holder.burnStyle.setImageResource(R.drawable.hwayum_2)
                    "003" -> holder.burnStyle.setImageResource(R.drawable.hwayum_3)
                    "004" -> holder.burnStyle.setImageResource(R.drawable.hwayum_4)
                    "999" -> holder.burnStyle.setImageResource(R.drawable.and)
                }
            }

            "003" -> {
                when(burnDetail){
                    "001" -> holder.burnStyle.setImageResource(R.drawable.jungi_1)
                    "999" -> holder.burnStyle.setImageResource(R.drawable.and)
                }
            }

            "004" -> {
                when(burnDetail){
                    "001" -> holder.burnStyle.setImageResource(R.drawable.jubchok_1)
                    "002" -> holder.burnStyle.setImageResource(R.drawable.jubchok_2)
                    "003" -> holder.burnStyle.setImageResource(R.drawable.jubchok_3)
                    "004" -> holder.burnStyle.setImageResource(R.drawable.jubchok_4)
                    "999" -> holder.burnStyle.setImageResource(R.drawable.and)
                }
            }

            "005" -> {
                when(burnDetail){
                    "001" -> holder.burnStyle.setImageResource(R.drawable.juon_1)
                    "002" -> holder.burnStyle.setImageResource(R.drawable.juon_2)
                    "003" -> holder.burnStyle.setImageResource(R.drawable.juon_3)
                    "999" -> holder.burnStyle.setImageResource(R.drawable.and)
                }
            }

            "006" -> {
                when(burnDetail){
                    "001" -> holder.burnStyle.setImageResource(R.drawable.hwahag_1)
                    "002" -> holder.burnStyle.setImageResource(R.drawable.hwahag_2)
                    "003" -> holder.burnStyle.setImageResource(R.drawable.hwahag_3)
                    "999" -> holder.burnStyle.setImageResource(R.drawable.and)
                }
            }

            "007" -> {
                when(burnDetail){
                    "001" -> holder.burnStyle.setImageResource(R.drawable.junggi_1)
                    "002" -> holder.burnStyle.setImageResource(R.drawable.junggi_2)
                    "999" -> holder.burnStyle.setImageResource(R.drawable.and)
                }
            }

            "008" -> {
                when(burnDetail){
                    "001" -> holder.burnStyle.setImageResource(R.drawable.machar_1)
                    "002" -> holder.burnStyle.setImageResource(R.drawable.machar_2)
                    "999" -> holder.burnStyle.setImageResource(R.drawable.and)
                }
            }

            "009" -> {
                when(burnDetail){
                    "001" -> holder.burnStyle.setImageResource(R.drawable.hatbit_1)
                    "999" -> holder.burnStyle.setImageResource(R.drawable.and)
                }
            }

            "010" -> {
                when(burnDetail){
                    "001" -> holder.burnStyle.setImageResource(R.drawable.heubib_1)
                    "999" -> holder.burnStyle.setImageResource(R.drawable.and)
                }
            }

        }

    }

    inner class AnswerViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var answerQuestionWrapper = view.answerQuestionWrapper
        var burnStyle = view.burnStyle
        var burnStyleNm = view.burnStyleNm
        var questionInsertDate = view.questionInsertDate
        var patientName = view.patientName
        var questionTitle = view.questionTitle
        var stateAnswer = view.stateAnswer
        var answerCount = view.answerCount

        init {

            answerQuestionWrapper.setOnClickListener {

                context?.startActivity(Intent(context, DoctorAnsweredDetailActivity::class.java)
                    .putExtra("questionInfo", arr[adapterPosition])
                    .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

            }

        }

    }

}