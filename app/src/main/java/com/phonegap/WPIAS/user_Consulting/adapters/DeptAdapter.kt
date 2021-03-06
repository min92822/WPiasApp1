package com.phonegap.WPIAS.user_Consulting.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.publicObject.Validation
import com.phonegap.WPIAS.user_Consulting.ConsultingActivity
import kotlinx.android.synthetic.main.activity_consulting.*
import kotlinx.android.synthetic.main.visit_dept.view.*
import kotlinx.android.synthetic.main.visit_dept.view.appCompatCheckBox

class DeptAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var context : Context? = null

    var arr = arrayListOf("해당없음", "외과", "성형외과", "피부과", "응급실", "화상병원", "기타사항")
    var viewArr = ArrayList<View>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        var view = LayoutInflater.from(parent.context).inflate(R.layout.visit_dept, parent, false)
        return DeptViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as DeptViewHolder

        holder.dept.text = arr[position]

        (viewArr[position].appCompatCheckBox as AppCompatCheckBox).setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){

                for(deptListView in viewArr){

                    if(deptListView != viewArr[position]){
                        (deptListView.appCompatCheckBox as AppCompatCheckBox).isChecked = false
                    }

                }

                Validation.vali.careStyleV = (position + 1).toString().padStart(3, '0')

                if(viewArr[position].dept.text == "기타사항"){

                    Validation.vali.careGitaV = viewArr[position].dept.text.toString()

                }else{

                    Validation.vali.careGitaV = ""

                }

                if(position == 0){
                    (context as ConsultingActivity).city.setSelection(0)
                    (context as ConsultingActivity).district.setSelection(0)
                    (context as ConsultingActivity).detailLocation.setText("")
                    (context as ConsultingActivity).locationSelect.isEnabled =false
                    (context as ConsultingActivity).locationMySelf.isEnabled =false
                    (context as ConsultingActivity).locationSelect.isChecked =false
                    (context as ConsultingActivity).locationMySelf.isChecked =false
                    (context as ConsultingActivity).city.isEnabled = false
                    (context as ConsultingActivity).district.isEnabled = false
                    (context as ConsultingActivity).detailLocation.isEnabled = false
                    Validation.vali.locationV = " "
                }else{
                    (context as ConsultingActivity).locationSelect.isEnabled =true
                    (context as ConsultingActivity).locationMySelf.isEnabled =true
                    (context as ConsultingActivity).locationSelect.isChecked =true
                    (context as ConsultingActivity).city.isEnabled = true
                    (context as ConsultingActivity).district.isEnabled = true
                    (context as ConsultingActivity).detailLocation.isEnabled = true
                }

                buttonView.isClickable = false

            }else{

                buttonView.isClickable = true

            }

        }

        viewArr[0].appCompatCheckBox.isChecked = true

    }

    inner class DeptViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var deptWrapper = view.deptWrapper
        var dept = view.dept

        init {

            viewArr.add(view)

            deptWrapper.setOnClickListener {

                viewArr[adapterPosition].appCompatCheckBox.isChecked = true

            }

        }

    }

}