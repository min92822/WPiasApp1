package fineinsight.app.service.wpias.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fineinsight.app.service.wpias.ConsultingActivity
import fineinsight.app.service.wpias.R
import kotlinx.android.synthetic.main.activity_consulting.*
import kotlinx.android.synthetic.main.cause_of_burned.view.*

class CauseOfBurnedAdapter(var arr : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        context = parent.context

        var view = LayoutInflater.from(parent.context).inflate(R.layout.cause_of_burned, parent, false)

        return CauseOfBurnedViewHolder(view)

    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as CauseOfBurnedViewHolder

        holder.cause.text = arr[position]

        holder.cause.setOnClickListener {

            (context as ConsultingActivity).causeOfBurnedDetailRecyclerView.layoutManager = GridLayoutManager(context, 3)
            (context as ConsultingActivity).causeOfBurnedDetailRecyclerView.adapter = CauseOfBurnedDetailAdapter(holder.cause.text.toString())

        }

    }

    class CauseOfBurnedViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var cause = view.cause

    }

}