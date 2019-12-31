package com.phonegap.WPIAS.user_Consulting.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.publicObject.Validation
import kotlinx.android.synthetic.main.question.view.*

class QuestionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewArr = ArrayList<View>()

    var arr = arrayListOf("병원방문", "물집관리", "응급처치", "사후관리", "상처상태", "흉터관리", "기타사항")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun getItemCount(): Int {

        return arr.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as QuestionViewHolder

        holder.question.text = arr[position]

        viewArr[position].appCompatCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){

                Validation.vali.directionV.add((position + 1).toString())

            }else{

                Validation.vali.directionV.remove((position + 1).toString())

            }

        }

    }

    inner class QuestionViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var question = view.question
        var questionWrapper = view.questionWrapper

        init {

            viewArr.add(view)

            questionWrapper.setOnClickListener {

                viewArr[adapterPosition].appCompatCheckBox.isChecked = !viewArr[adapterPosition].appCompatCheckBox.isChecked

            }

        }

    }
}