package fineinsight.app.service.wpias.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fineinsight.app.service.wpias.R
import kotlinx.android.synthetic.main.body_part_detail.view.*

class PartDetailAdapter(var arr : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.body_part_detail, parent, false)
        return partDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as partDetailViewHolder
        holder.detailPart.text = arr[position]
    }

    class partDetailViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var detailPart = view.detailPart
    }

}