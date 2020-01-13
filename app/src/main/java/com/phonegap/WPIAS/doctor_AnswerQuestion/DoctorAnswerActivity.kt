package com.phonegap.WPIAS.doctor_AnswerQuestion

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.internal.LinkedTreeMap
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.dataClass.NewCaseInfo
import com.phonegap.WPIAS.dataClass.NewQuestionInfo
import com.phonegap.WPIAS.dataClass.pushinfo
import com.phonegap.WPIAS.doctor_Question.DoctorNewQuestionActivity
import com.phonegap.WPIAS.publicObject.PubVariable
import com.phonegap.WPIAS.public_function.FCM
import com.phonegap.WPIAS.restApi.ApiUtill
import com.phonegap.WPIAS.user_Main.MainActivity
import kotlinx.android.synthetic.main.activity_doctor_answer.*
import kotlinx.android.synthetic.main.activity_doctor_answer.doctorName
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.title_bar_darkblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class DoctorAnswerActivity : RootActivity() {

    lateinit var patientCase : NewCaseInfo

    lateinit var questionInfo: NewQuestionInfo

    var count = 0

    var popup = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_answer)

        SetTransparentBar()

        getCount()

        setDescendentViews(window.decorView.rootView)

        initActivity()

    }

    //액티비티 시작 설정
    @SuppressLint("SetTextI18n")
    fun initActivity(){

        btn_back.setOnClickListener { onBackPressed() }

        txt_title.text = "신규질문"

        if(intent.hasExtra("patientCase")){

            patientCase = intent.getSerializableExtra("patientCase") as NewCaseInfo

        }else{
            finish()
            startActivity(Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        if(intent.hasExtra("newQuestionInfo")){
            questionInfo = intent.getSerializableExtra("newQuestionInfo") as NewQuestionInfo
        }

        Glide.with(this)
            .load(patientCase.imageurl1)
            .into(burnedImage)

        closeImage.setOnClickListener {

            Glide.with(this)
                .load(patientCase.imageurl1)
                .into(burnedImage)

            closeImage.setBackgroundResource(R.color.windows_blue)
            closeImage.setTextColor(ContextCompat.getColor(this, R.color.white))
            overviewImage.setBackgroundResource(R.drawable.btn_stroke)
            overviewImage.setTextColor(ContextCompat.getColor(this, R.color.windows_blue))

        }

        overviewImage.setOnClickListener {

            Glide.with(this)
                .load(patientCase.imageurl2)
                .into(burnedImage)

            overviewImage.setBackgroundResource(R.color.windows_blue)
            overviewImage.setTextColor(ContextCompat.getColor(this, R.color.white))
            closeImage.setBackgroundResource(R.drawable.btn_stroke)
            closeImage.setTextColor(ContextCompat.getColor(this, R.color.windows_blue))

        }

        patientQuestionContents.movementMethod = ScrollingMovementMethod()

        patientQuestionContents.setOnTouchListener { v, event ->

            if(v.isVerticalScrollBarEnabled) {

                answerScrollView.requestDisallowInterceptTouchEvent(true)

            }else{

                answerScrollView.requestDisallowInterceptTouchEvent(false)

            }

            return@setOnTouchListener false

        }

        patientQuestionContents.text = patientCase.contents

        doctorName.text = PubVariable.userInfo.nickname

        doctorDept.text = PubVariable.userInfo.remark.replace('_', ' ')

        doctorAnswerInput.movementMethod = ScrollingMovementMethod()

        answerScrollView.setOnTouchListener { v, event ->

            doctorAnswerInput.parent.requestDisallowInterceptTouchEvent(false)
            patientQuestionContents.parent.requestDisallowInterceptTouchEvent(false)

            return@setOnTouchListener false

        }

        doctorAnswerInput.setOnTouchListener { v, event ->

            if(v.isVerticalScrollBarEnabled) {

                answerScrollView.requestDisallowInterceptTouchEvent(true)

            }else{

                answerScrollView.requestDisallowInterceptTouchEvent(false)

            }

            return@setOnTouchListener false

        }

        doctorAnswerSubmit.setOnClickListener {

            if(doctorAnswerInput.text.isNotEmpty()){
                insertAnswer()
            }else{
                Toast.makeText(this, "답변 내용을 입력해주세요", Toast.LENGTH_LONG).show()
            }

        }

    }

    //의사 답변수 조회 펑션
    fun getCount(){

        var map = HashMap<String, String>()

        map["IDKEY"] = PubVariable.userInfo.idkey

        ApiUtill().getSELECT_MYANSWERCOUNT().select_myanswercount(map).enqueue(object :
            Callback<Any> {

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Any>, response: Response<Any>) {

                if(response.isSuccessful){
                    count = (((response.body()!! as ArrayList<*>)[0] as LinkedTreeMap<*, *>).values.toList()[0] as Double).toInt()
                    doctorAnswerCount.text = "| 답변수 : $count"
                }else{
                    println()
                }

            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                println(t.toString())
            }

        })

    }

    //에딧 텍스트 아닌 부분 클릭시 키보드 사라지는 펑션
    fun hideKeyboard(){

        var imm = (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)

        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

        window?.decorView?.clearFocus()

    }

    //최상위 뷰 태그 및 하위 뷰 태그에 hideKeboard를 적용하는 펑션
    fun setDescendentViews(view : View){

        if(view !is EditText) {
            view.setOnTouchListener { v, event ->

                hideKeyboard()
                return@setOnTouchListener false

            }
        }

        if(view is RecyclerView){

            view.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                    hideKeyboard()
                }

                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    hideKeyboard()
                    return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                }
            })

        }

        if(view is ViewGroup){
            for(innerview in view) {
                setDescendentViews(innerview)
            }
        }

    }

    //질문 답변등록 하는 펑션
    @SuppressLint("SimpleDateFormat")
    fun insertAnswer(){

        Loading(ProgressBar, ProgressBg, true)

        var map = HashMap<String, String>()

        map["QKEY"] = patientCase.ckey
        map["UUID"] = PubVariable.uid
        map["CNUMBER"] = patientCase.cnumber
        map["ANSWERCONTENTS"] = doctorAnswerInput.text.toString()
        map["ANSWERDATE"] = SimpleDateFormat("yyyyMMddkkmmss").format(System.currentTimeMillis())

        ApiUtill().getUPDATE_FIRSTANSWER().update_firstanswer(map).enqueue(object : Callback<String>{

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if(response.isSuccessful){
                    when(response.body()){
                        "S" -> checkPushInfo()
                        "F" -> failAlert()
                        "R" -> otherDoctorAlert()
                    }
                }else{
                    println(response.message())
                    println(response.code())
                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println(t.toString())
            }

        })

    }

    //해당 환자의 푸시알림 동의 여부를 확인하는 펑션
    fun checkPushInfo(){

        var map = HashMap<String, String>()

        map["IDKEY"] = questionInfo.uuid

        ApiUtill().getSELECT_CHECKAGREE().select_checkagree(map).enqueue(object : Callback<ArrayList<pushinfo>>{

            override fun onResponse(call: Call<ArrayList<pushinfo>>,response: Response<ArrayList<pushinfo>>) {

                Loading(ProgressBar, ProgressBg, false)

                if(response.isSuccessful){

                    if(response.body()!!.size > 0){

                        var pushinfo : pushinfo = response.body()!![0]

                        if(pushinfo.SWITCH2 == "On"){

                            FCM.function.SendMsgToTarget(pushinfo.TOKEN, "${PubVariable.userInfo.nickname} 선생님이 답변을 등록되었습니다.")

                        }else{

                            println("동의 OFF")

                        }

                        successAlert()

                    }else{

                        successAlert()

                    }

                }else{

                    println(response.message())
                    println(response.code())

                }

            }

            override fun onFailure(call: Call<ArrayList<pushinfo>>, t: Throwable) {
                Loading(ProgressBar, ProgressBg, false)
                println(t.toString())
            }

        })

    }

    //환자 푸시알림 동의 및 답변 등록 성공시 알럿
    fun successAlert(){

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.setCancelable(false)

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_ck)

        title.text = "완료"
        sub.text = "답변이 정상적으로 등록되었습니다."

        btn_left.visibility = View.GONE

        btn_right.text = "확인"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        popup = true

        dialog.setOnDismissListener {
            popup = false
            dialog = Dialog(this)
        }

        btn_right.setOnClickListener {
            startActivity(Intent(this, DoctorNewQuestionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            finish()
            dialog.dismiss()
        }

        dialog.show()

    }

    //답변 등록 실패시 알럿
    fun failAlert(){

        Loading(ProgressBar, ProgressBg, false)

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_ck_x)

        title.text = "실패"
        sub.text = "답변 등록에 실패했습니다 다시 시도해주세요."

        btn_left.visibility = View.GONE

        btn_right.text = "확인"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        popup = true

        dialog.setOnDismissListener {
            popup = false
            dialog = Dialog(this)
        }

        btn_right.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

    //다른 선생님이 먼저 등록했을시 알럿
    fun otherDoctorAlert(){

        Loading(ProgressBar, ProgressBg, false)

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_ck_x)

        title.text = "실패"
        sub.text = "다른 선생님이 답변을 등록했습니다."

        btn_left.visibility = View.GONE

        btn_right.text = "확인"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        popup = true

        dialog.setOnDismissListener {
            popup = false
            dialog = Dialog(this)
        }

        btn_right.setOnClickListener {
            startActivity(Intent(this, DoctorNewQuestionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            finish()
            dialog.dismiss()
        }

        dialog.show()

    }
}