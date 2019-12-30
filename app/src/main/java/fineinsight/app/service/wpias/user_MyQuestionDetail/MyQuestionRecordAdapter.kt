package fineinsight.app.service.wpias.user_MyQuestionDetail

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fineinsight.app.service.wpias.R
import fineinsight.app.service.wpias.dataClass.MycaseInfo
import fineinsight.app.service.wpias.dataClass.QuestionInfo
import fineinsight.app.service.wpias.user_MyCase.MyCaseActivity
import kotlinx.android.synthetic.main.recycler_my_question_detail_record.view.*
import java.text.SimpleDateFormat
import kotlin.math.abs

class MyQuestionRecordAdapter(var activity: Activity, var arr: ArrayList<MycaseInfo>, var qArr: ArrayList<QuestionInfo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_my_question_detail_record, parent, false)

        return viewHolder(
            view
        )

    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as viewHolder

        // 처음 끝 선 안보이게
        when (position) {
            0 -> {

                if (arr.size == 1) {
                    holder.top_line.visibility = View.INVISIBLE
                }
                holder.bottom_line.visibility = View.INVISIBLE
            }
            arr.size - 1 -> {
                holder.top_line.visibility = View.INVISIBLE
            }
        }

        var date = arr[position].casedate
        holder.date.text = "작성일 : ${date.substring(0, 4)}년 ${date.substring(4, 6)}월 ${date.substring(6, 8)}일"


        var days = toDate(qArr[0].burndate,"${date.substring(0, 4)}-${date.substring(4, 6)}-${date.substring(6, 8)}")
        holder.days.text = "${days} 일차"

        holder.record_title.text = arr[position].contents

        Glide.with(activity)
            .load(arr[position].imageurl1)
            .into(holder.img)

        when (arr[position].casestatus) {
            "Q" -> {
                holder.answer.text = "답변대기"
                holder.answer.setTextColor(ContextCompat.getColor(activity, R.color.warm_grey))
                holder.answer.setBackgroundResource(R.drawable.answer_stroke)
            }
            "P" -> {
                holder.answer.text = "답변미요청"
                holder.answer.setTextColor(ContextCompat.getColor(activity, R.color.warm_grey))
                holder.answer.setBackgroundResource(R.drawable.answer_stroke)
            }
            "A" -> {

            }
        }



        holder.wrap.setOnClickListener {
            activity.startActivity(Intent(activity, MyCaseActivity::class.java).putExtra("myCase", arr[position]).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

    }

    // 날짜 차이 계산
    fun toDate(date1: String, date2: String) : Long {

        var format = SimpleDateFormat("yyyy-MM-dd")
        var FirstDate = format.parse(date1)
        var SecondDate = format.parse(date2)

        var calDate = FirstDate.time - SecondDate.time

        var calDateDays = calDate / (24 * 60 * 60 * 1000)

        calDateDays = abs(calDateDays)

        println("두 날짜의 날짜 차이: " + calDateDays)
        return calDateDays
    }

    class viewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var top_line = v.top_line
        var bottom_line = v.bottom_line

        var date = v.txt_detail_record_date
        var days = v.txt_detail_record_days
        var record_title = v.txt_detail_record_title
        var img = v.img_detail_record
        var answer = v.txt_answer_status

        var wrap = v.wrap_record

    }
}