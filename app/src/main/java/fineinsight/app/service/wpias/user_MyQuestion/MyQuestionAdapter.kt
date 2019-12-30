package fineinsight.app.service.wpias.user_MyQuestion

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fineinsight.app.service.wpias.R
import fineinsight.app.service.wpias.dataClass.QuestionInfo
import fineinsight.app.service.wpias.user_MyQuestionDetail.MyQuestionDetailActivity
import kotlinx.android.synthetic.main.recycler_my_question.view.*

class MyQuestionAdapter(var arr:ArrayList<QuestionInfo>, var activity: Activity):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_my_question, parent, false)

        return viewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as viewHolder

        var writeDate = "${arr[position].insertdate.substring(0, 4)}년 ${arr[position].insertdate.substring(4, 6)}월 ${arr[position].insertdate.substring(6, 8)}일"

        holder.title.text = arr[position].title
        holder.case_date.text = "${arr[position].burnnm}화상 | $writeDate"
        holder.content.text = arr[position].contents

        Glide.with(activity)
            .load(arr[position].imageurl1)
            .into(holder.imgleft)

        Glide.with(activity)
            .load(arr[position].imageurl2)
            .into(holder.imgright)

        holder.wrapper.setOnClickListener {

            activity.startActivity(Intent(activity, MyQuestionDetailActivity::class.java).putExtra("myQuestion", arr[position]).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

        }

    }

    class viewHolder(view:View) : RecyclerView.ViewHolder(view){
        var title = view.txt_my_title
        var case_date = view.txt_my_case_date
        var content = view.txt_my_content
        var imgleft = view.img_my_left
        var imgright = view.img_my_right
        var wrapper = view.wrap_my_question
    }
}