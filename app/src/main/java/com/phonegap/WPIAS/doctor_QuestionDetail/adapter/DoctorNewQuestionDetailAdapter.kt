package com.phonegap.WPIAS.doctor_QuestionDetail.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.dataClass.NewCaseInfo
import com.phonegap.WPIAS.dataClass.NewQuestionInfo
import com.phonegap.WPIAS.doctor_AnswerQuestion.DoctorAnswerActivity
import com.phonegap.WPIAS.doctor_QuestionDetail.DoctorNewQuestionDetailActivity
import kotlinx.android.synthetic.main.activity_doctor_answer.*
import kotlinx.android.synthetic.main.doctor_question_detail.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class DoctorNewQuestionDetailAdapter(var arr : ArrayList<NewCaseInfo>, var questionInfo : NewQuestionInfo) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        context = parent.context

        var view = LayoutInflater.from(parent.context).inflate(R.layout.doctor_question_detail, parent, false)

        return DoctorNewQuestionDetailViewHolder(view)

    }

    override fun getItemCount(): Int {
        return arr.size
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as DoctorNewQuestionDetailViewHolder

        when(position){

            0 -> {
                if(arr.size == 1) holder.topLine.visibility = View.INVISIBLE
                holder.bottomLine.visibility = View.INVISIBLE
            }

            arr.size -1 -> holder.bottomLine.visibility = View.INVISIBLE

        }

        var caseDate = SimpleDateFormat("yyyy년 MM월 dd일").format(Date(SimpleDateFormat("yyyyMMddkkmmss").parse(arr[position].casedate)!!.time))

        holder.doctorQuestionInsertDate.text = "작성일 : $caseDate"

        holder.questionHistroy.text = "${toDate(questionInfo.burndate, arr[position].casedate)} 일차"

        holder.questionTitle.text = arr[position].contents

        Glide.with(context!!)
            .load(arr[position].imageurl1)
            .into(holder.questionImage)

        when (arr[position].casestatus) {
            "Q" -> {
                holder.questionAnswerStatus.text = "답변요청"
                holder.questionAnswerStatus.setTextColor(ContextCompat.getColor(context!!, R.color.warm_grey))
                holder.questionAnswerStatus.setBackgroundResource(R.drawable.answer_stroke)
            }
            "P" -> {
                holder.questionAnswerStatus.text = "답변미요청"
                holder.questionAnswerStatus.setTextColor(ContextCompat.getColor(context!!, R.color.warm_grey))
                holder.questionAnswerStatus.setBackgroundResource(R.drawable.answer_stroke)
            }
            "A" -> {

            }
        }

        holder.questionDetailWrapper.setOnClickListener {
            context!!.startActivity(
                Intent(context!!, DoctorAnswerActivity::class.java)
                    .putExtra("patientCase", arr[position])
                    .putExtra("newQuestionInfo", questionInfo)
                    .setFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        (context as DoctorNewQuestionDetailActivity).Loading((context as DoctorNewQuestionDetailActivity).ProgressBar, (context as DoctorNewQuestionDetailActivity).ProgressBg, false)

    }

    // 날짜 차이 계산
    @SuppressLint("SimpleDateFormat")
    fun toDate(date1: String, date2: String) : Long {

        var FirstDate = SimpleDateFormat("yyyy-MM-dd").parse(date1)
        var SecondDate = SimpleDateFormat("yyyyMMddkkmmss").parse(date2)

        var calDate = FirstDate!!.time - SecondDate!!.time

        var calDateDays = calDate / (24 * 60 * 60 * 1000)

        calDateDays = abs(calDateDays)

        return calDateDays
    }

    class DoctorNewQuestionDetailViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var topLine = view.topLine
        var bottomLine = view.bottomLine
        var doctorQuestionInsertDate = view.doctorQuestionInsertDate
        var questionHistroy = view.questionHistory
        var questionTitle = view.questionTitle
        var questionImage = view.questionImage
        var questionAnswerStatus = view.questionAnswerStatus
        var questionDetailWrapper = view.questionDetailWrapper
    }

}