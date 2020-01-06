package com.phonegap.WPIAS.doctor_Question.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.dataClass.NewQuestionInfo
import kotlinx.android.synthetic.main.patient_new_question.view.*
import java.text.SimpleDateFormat

class NewQuestionAdapter(var arr : ArrayList<NewQuestionInfo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.patient_new_question, parent, false)

        return NewQuestionViewHolder(view)

    }

    override fun getItemCount(): Int {
        return arr.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as NewQuestionViewHolder

        caseClassfication(holder, position)

    }

    // burn style로 구분하는 펑션
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun caseClassfication(holder : NewQuestionViewHolder, position : Int){

        holder.causeOfBurned.text = "${arr[position].burnnm}화상"
        holder.questionTitle.text = arr[position].title
        holder.questionContents.text = arr[position].nickname
        holder.insertDate.text = SimpleDateFormat("yyyy년 MM월 dd일").format(SimpleDateFormat("yyyyMMddkkmmss").parse(arr[position].insertdate)!!)

        caseImage(holder, arr[position].burnstyle, arr[position].burndetailcd)

    }

    //burn style별 이미지 적용하는 펑션
    fun caseImage(holder : NewQuestionViewHolder, burnStyle : String, burnDetail : String){

        when(burnStyle){

            "001" -> {
                holder.caseBurnedColor.setBackgroundColor(Color.parseColor("#FF7373"))
                when(burnDetail){
                    "001" -> holder.caseImage.setImageResource(R.drawable.yultang_1)
                    "002" -> holder.caseImage.setImageResource(R.drawable.yultang_2)
                    "003" -> holder.caseImage.setImageResource(R.drawable.yultang_3)
                    "004" -> holder.caseImage.setImageResource(R.drawable.yultang_4)
                    "005" -> holder.caseImage.setImageResource(R.drawable.yultang_6)
                    "006" -> holder.caseImage.setImageResource(R.drawable.yultang_7_2)
                    "007" -> holder.caseImage.setImageResource(R.drawable.yultang_7)
                    "999" -> holder.caseImage.setImageResource(R.drawable.and)
                }
            }

            "002" -> {
                holder.caseBurnedColor.setBackgroundColor(Color.parseColor("#FFB873"))
                when(burnDetail){
                    "001" -> holder.caseImage.setImageResource(R.drawable.hwayum_1)
                    "002" -> holder.caseImage.setImageResource(R.drawable.hwayum_2)
                    "003" -> holder.caseImage.setImageResource(R.drawable.hwayum_3)
                    "004" -> holder.caseImage.setImageResource(R.drawable.hwayum_4)
                    "999" -> holder.caseImage.setImageResource(R.drawable.and)
                }
            }

            "003" -> {
                holder.caseBurnedColor.setBackgroundColor(Color.parseColor("#1D1DC0"))
                when(burnDetail){
                    "001" -> holder.caseImage.setImageResource(R.drawable.jungi_1)
                    "999" -> holder.caseImage.setImageResource(R.drawable.and)
                }
            }

            "004" -> {
                holder.caseBurnedColor.setBackgroundColor(Color.parseColor("#FF73CF"))
                when(burnDetail){
                    "001" -> holder.caseImage.setImageResource(R.drawable.jubchok_1)
                    "002" -> holder.caseImage.setImageResource(R.drawable.jubchok_2)
                    "003" -> holder.caseImage.setImageResource(R.drawable.jubchok_3)
                    "004" -> holder.caseImage.setImageResource(R.drawable.jubchok_4)
                    "999" -> holder.caseImage.setImageResource(R.drawable.and)
                }
            }

            "005" -> {
                holder.caseBurnedColor.setBackgroundColor(Color.parseColor("#73D2FF"))
                when(burnDetail){
                    "001" -> holder.caseImage.setImageResource(R.drawable.juon_1)
                    "002" -> holder.caseImage.setImageResource(R.drawable.juon_2)
                    "003" -> holder.caseImage.setImageResource(R.drawable.juon_3)
                    "999" -> holder.caseImage.setImageResource(R.drawable.and)
                }
            }

            "006" -> {
                holder.caseBurnedColor.setBackgroundColor(Color.parseColor("#7D4DFF"))
                when(burnDetail){
                    "001" -> holder.caseImage.setImageResource(R.drawable.hwahag_1)
                    "002" -> holder.caseImage.setImageResource(R.drawable.hwahag_2)
                    "003" -> holder.caseImage.setImageResource(R.drawable.hwahag_3)
                    "999" -> holder.caseImage.setImageResource(R.drawable.and)
                }
            }

            "007" -> {
                holder.caseBurnedColor.setBackgroundColor(Color.parseColor("#00E78D"))
                when(burnDetail){
                    "001" -> holder.caseImage.setImageResource(R.drawable.junggi_1)
                    "002" -> holder.caseImage.setImageResource(R.drawable.junggi_2)
                    "999" -> holder.caseImage.setImageResource(R.drawable.and)
                }
            }

            "008" -> {
                holder.caseBurnedColor.setBackgroundColor(Color.parseColor("#C573FF"))
                when(burnDetail){
                    "001" -> holder.caseImage.setImageResource(R.drawable.machar_1)
                    "002" -> holder.caseImage.setImageResource(R.drawable.machar_2)
                    "999" -> holder.caseImage.setImageResource(R.drawable.and)
                }
            }

            "009" -> {
                holder.caseBurnedColor.setBackgroundColor(Color.parseColor("#D2E911"))
                when(burnDetail){
                    "001" -> holder.caseImage.setImageResource(R.drawable.hatbit_1)
                    "999" -> holder.caseImage.setImageResource(R.drawable.and)
                }
            }

            "010" -> {
                holder.caseBurnedColor.setBackgroundColor(Color.parseColor("#0088A1"))
                when(burnDetail){
                    "001" -> holder.caseImage.setImageResource(R.drawable.heubib_1)
                    "999" -> holder.caseImage.setImageResource(R.drawable.and)
                }
            }

        }

    }

    class NewQuestionViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var causeOfBurned = view.causeOfBurned
        var questionTitle = view.questionTitle
        var questionContents = view.questionContents
        var caseBurnedColor = view.caseBurnedColor
        var insertDate = view.insertDate
        var caseImage = view.caseImage
        var patientNewQuestion = view.patientNewQuestion

    }

}