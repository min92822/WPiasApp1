package com.phonegap.WPIAS.doctor_Question.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.recyclerview.widget.RecyclerView
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.dataClass.LockInfo
import com.phonegap.WPIAS.dataClass.NewQuestionInfo
import com.phonegap.WPIAS.doctor_Question.DoctorNewQuestionActivity
import com.phonegap.WPIAS.doctor_QuestionDetail.DoctorNewQuestionDetailActivity
import com.phonegap.WPIAS.publicObject.PubVariable
import com.phonegap.WPIAS.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_doctor_new_question.*
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.patient_new_question.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NewQuestionAdapter(var arr : ArrayList<NewQuestionInfo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var context : Context? = null
    var popup = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        context = parent.context

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

    //해당 질문 Lock Check 및 해당 질문 액티비티로 이동하는 펑션
    @SuppressLint("SimpleDateFormat")
    fun lockCheck(position : Int){

        (context as DoctorNewQuestionActivity).Loading((context as DoctorNewQuestionActivity).ProgressBar, (context as DoctorNewQuestionActivity).ProgressBg, true)

        var map = HashMap<String, String>()
        map["QKEY"] = arr[position].qkey

        ApiUtill().getSELECT_LOCKCHECK().select_lockcheck(map).enqueue(object : Callback<ArrayList<LockInfo>>{

            override fun onResponse(call: Call<ArrayList<LockInfo>>, response: Response<ArrayList<LockInfo>>) {
                if(response.isSuccessful){

                    var lockInfo = response.body()!![0]

                    if(lockInfo.islock.toBoolean()){

                        var lockTime = with(SimpleDateFormat("yyyy-MM-dd-kk-mm-ss")) { parse(lockInfo.locktime) }

                        var timeDiff = System.currentTimeMillis() - lockTime.time

                        //lock을 건 uid와 현재 사용자 uid가 일치하면 updateLock을 실행한다
                        if(PubVariable.uid == lockInfo.lockuser){

                            updateLock(lockInfo, position)

                        }else{

                            //10분 초과 경과했으면 updateLock을 실행한다
                            if(timeDiff/60000 > 10){
                                updateLock(lockInfo, position)
                            //경과 시간이 10분 이하일 경우 알럿을띄운다
                            }else{
                                (context as DoctorNewQuestionActivity).Loading((context as DoctorNewQuestionActivity).ProgressBar, (context as DoctorNewQuestionActivity).ProgressBg, false)
                                lockPopup(timeDiff)
                            }

                        }

                    }else{

                        updateLock(lockInfo, position)

                    }

                }else{
                    (context as DoctorNewQuestionActivity).Loading((context as DoctorNewQuestionActivity).ProgressBar, (context as DoctorNewQuestionActivity).ProgressBg, false)
                    println(response.code())
                    println(response.message())
                }

            }

            override fun onFailure(call: Call<ArrayList<LockInfo>>, t: Throwable) {
                (context as DoctorNewQuestionActivity).Loading((context as DoctorNewQuestionActivity).ProgressBar, (context as DoctorNewQuestionActivity).ProgressBg, false)
                println(t.toString())
            }

        })

    }

    // lock 상태 업데이트 하는 펑션
    @SuppressLint("SimpleDateFormat")
    fun updateLock(lockInfo : LockInfo, position : Int){

        var map = HashMap<String, String>()

        map["LOCKUSER"] = PubVariable.uid
        map["LOCKTIME"] = SimpleDateFormat("yyyy-MM-dd-kk-mm-ss").format(Date(System.currentTimeMillis()).time)
        map["QKEY"] = arr[position].qkey

        ApiUtill().getUPDATE_LOCK().update_lock(map).enqueue(object : Callback<String>{

            override fun onResponse(call: Call<String>, response: Response<String>) {

                (context as DoctorNewQuestionActivity).Loading((context as DoctorNewQuestionActivity).ProgressBar, (context as DoctorNewQuestionActivity).ProgressBg, false)

                if(response.isSuccessful){

                    if(response.body() == "S"){
                        context?.startActivity(Intent(context, DoctorNewQuestionDetailActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).putExtra("newQuestionInfo", arr[position]))
                    }else{
                        context?.startActivity(Intent(context, DoctorNewQuestionDetailActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).putExtra("newQuestionInfo", arr[position]))
                    }

                }else{
                    println(response.code())
                    println(response.message())
                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                (context as DoctorNewQuestionActivity).Loading((context as DoctorNewQuestionActivity).ProgressBar, (context as DoctorNewQuestionActivity).ProgressBg, false)
                println(t.message)
            }

        })

    }

    // 해당 질문 lock 상태를 알려주는 팝업
    @SuppressLint("SetTextI18n")
    fun lockPopup(timeDiff : Long){

        var dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_ck_x)

        title.text = "선점된 질문입니다"
        sub.text = "선점 해제까지 ${calculateTime(timeDiff)} 초 남았습니다."

        btn_left.visibility = View.GONE

        btn_right.text = "확인"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        popup = true

        dialog.setOnDismissListener {
            popup = false
            dialog = Dialog(context!!)
        }

        btn_right.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    //시간 계산 펑션
    fun calculateTime(timeDiff : Long) : String{

        var minute = 0
        var second = 0

        var result = ""

        //분 계산
        minute = TimeUnit.MILLISECONDS.toMinutes(timeDiff).toInt()

        //초 계산
        second = TimeUnit.MILLISECONDS.toSeconds(timeDiff).toInt()

        when {
            second >= 540 -> second -= 540
            second >= 480 -> second -= 480
            second >= 420 -> second -= 420
            second >= 360 -> second -= 360
            second >= 300 -> second -= 300
            second >= 240 -> second -= 240
            second >= 180 -> second -= 180
            second >= 120 -> second -= 120
            second >= 60 -> second -= 60
        }

        result = "${(9 - minute)}:${(60 - second).toString().padStart(2, '0')}"

        return result

    }

    inner class NewQuestionViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var causeOfBurned = view.causeOfBurned
        var questionTitle = view.questionTitle
        var questionContents = view.questionContents
        var caseBurnedColor = view.caseBurnedColor
        var insertDate = view.insertDate
        var caseImage = view.caseImage
        var patientNewQuestion = view.patientNewQuestion

        init {

            patientNewQuestion.setOnClickListener {

                if(adapterPosition != RecyclerView.NO_POSITION){

                    lockCheck(adapterPosition)

                }

            }

        }

    }

}