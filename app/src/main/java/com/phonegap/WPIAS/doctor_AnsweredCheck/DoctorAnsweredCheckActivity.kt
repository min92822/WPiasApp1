package com.phonegap.WPIAS.doctor_AnsweredCheck

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.dataClass.MyAnswerCaseInfo
import com.phonegap.WPIAS.dataClass.MyAnswerInfo
import com.phonegap.WPIAS.dataClass.pushinfo
import com.phonegap.WPIAS.doctor_AnsweredQuestion.DoctorAnsweredActivity
import com.phonegap.WPIAS.publicObject.PubVariable
import com.phonegap.WPIAS.public_function.FCM
import com.phonegap.WPIAS.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_doctor_answered_check.*
import kotlinx.android.synthetic.main.activity_doctor_answered_check.ProgressBar
import kotlinx.android.synthetic.main.activity_doctor_answered_check.ProgressBg
import kotlinx.android.synthetic.main.activity_doctor_answered_check.burnedImage
import kotlinx.android.synthetic.main.activity_doctor_answered_check.closeImage
import kotlinx.android.synthetic.main.activity_doctor_answered_check.doctorAnswerInput
import kotlinx.android.synthetic.main.activity_doctor_answered_check.doctorAnswerSubmit
import kotlinx.android.synthetic.main.activity_doctor_answered_check.doctorDept
import kotlinx.android.synthetic.main.activity_doctor_answered_check.doctorName
import kotlinx.android.synthetic.main.activity_doctor_answered_check.overviewImage
import kotlinx.android.synthetic.main.activity_doctor_answered_check.patientQuestionContents
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.title_bar_darkblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class DoctorAnsweredCheckActivity : RootActivity() {

    lateinit var patientCase : MyAnswerCaseInfo

    lateinit var answerInfo: MyAnswerInfo

    var popup = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doctor_answered_check)

        SetTransparentBar()

        setDescendentViews(window.decorView.rootView)

        initActivity()

    }

    //액티비티 시작 설정
    fun initActivity(){

        if(intent.hasExtra("patientCase") && intent.hasExtra("answerInfo")) {

            patientCase = intent.getSerializableExtra("patientCase") as MyAnswerCaseInfo
            answerInfo = intent.getSerializableExtra("answerInfo") as MyAnswerInfo

        }

        burnedImage.maxScale = 6.0f

        btn_back.setOnClickListener { onBackPressed() }

        Glide.with(this)
            .asBitmap()
            .load(patientCase.imageurl1)
            .into(object : CustomTarget<Bitmap>(){

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    burnedImage.setImage(ImageSource.bitmap(resource))
                }

            })

        Glide.with(this)
            .asBitmap()
            .load(patientCase.imageurl2)
            .into(object : CustomTarget<Bitmap>(){

                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    burnedImage2.setImage(ImageSource.bitmap(resource))
                }

            })

        closeImage.setOnClickListener {
            burnedImage.visibility = View.VISIBLE
            burnedImage2.visibility = View.INVISIBLE
            closeImage.setBackgroundResource(R.color.windows_blue)
            closeImage.setTextColor(ContextCompat.getColor(this, R.color.white))
            overviewImage.setBackgroundResource(R.drawable.btn_stroke)
            overviewImage.setTextColor(ContextCompat.getColor(this, R.color.windows_blue))
        }

        overviewImage.setOnClickListener {
            burnedImage.visibility = View.INVISIBLE
            burnedImage2.visibility = View.VISIBLE
            overviewImage.setBackgroundResource(R.color.windows_blue)
            overviewImage.setTextColor(ContextCompat.getColor(this, R.color.white))
            closeImage.setBackgroundResource(R.drawable.btn_stroke)
            closeImage.setTextColor(ContextCompat.getColor(this, R.color.windows_blue))
        }

        txt_title.text = answerInfo.title
        patientQuestionContents.text = patientCase.contents

        answeredScrollView.setOnTouchListener { v, event ->
            doctorAnswerInput.parent.requestDisallowInterceptTouchEvent(false)
            return@setOnTouchListener false
        }

        doctorName.text = "Dr.${PubVariable.userInfo.nickname}"
        doctorDept.text = PubVariable.userInfo.remark.replace('_', ' ')

        when (patientCase.casestatus) {
            // 답변미요청
            "P" -> {
                wrapDoctorAnswerRequest.visibility = View.GONE
                wrapDoctorAnswerNotRequest.visibility = View.VISIBLE

                imgDoctorAnswerNotRequest.setImageResource(R.drawable.qa_chat_1)
                lblDoctorAnswerNotRequest.text = "사용자가 답변요청을 하지 않은 경과 입니다."
            }

            // 답변 완료
            "A" -> {
                wrapDoctorAnswerNotRequest.visibility = View.GONE
                wrapDoctorAnswerRequest.visibility = View.VISIBLE
                wrapDoctorAnswerAnswered.visibility = View.VISIBLE

                ratedByPatient.visibility = View.GONE
                wrapDoctorAnswerSubmit.visibility = View.GONE

                doctorAnswer.text = patientCase.answercontents

                if (patientCase.feedbacktime.isNullOrBlank()) {
                    reviewExist.text = "사용자가 리뷰를 작성하지 않았습니다."

                } else {
                    // 사용자가 리뷰를 등록했을 때
                    ratedByPatient.rating = patientCase.feedbackstar.toFloat()
                    ratedByPatient.setIsIndicator(true)
                    ratedByPatient.visibility = View.VISIBLE
                    reviewExist.text = "사용자가 리뷰를 작성했습니다."

                    wrapDoctorAnswerReview.visibility = View.VISIBLE
                    wrapDoctorAnswerPatientReview.visibility = View.VISIBLE

                    lblDoctorAnswerPatientReviewName.text = answerInfo.nickname
                    lblDoctorAnswerPatientReviewContent.text = patientCase.feedbacktext

                    if (answerInfo.gender == "F") {
                        imgDoctorAnswerPatientReview.setImageResource(R.drawable.female)
                    } else {
                        imgDoctorAnswerPatientReview.setImageResource(R.drawable.male)
                    }

                    var year = patientCase!!.answerdate.subSequence(0, 4)
                    var month = patientCase!!.answerdate.subSequence(4, 6)
                    var day = patientCase!!.answerdate.subSequence(6, 8)
                    lblDoctorAnswerPatientReviewTime.text = "답변일자 ${year}년 ${month}월 ${day}일"

                    if(patientCase.feedbackreplytime.isNullOrBlank()){
                        // 리뷰에 의사 댓글 없을 때
                        wrapDoctorAnswerFeedbackAdd.visibility = View.VISIBLE
                        wrapDoctorAnswerFeedback.visibility = View.GONE

                        btnDoctorAnswerFeedbackSubmit.setOnClickListener {
                            insertReply()
                        }

                    } else {
                        // 리뷰에 의사 댓글 있을 때
                        wrapDoctorAnswerFeedbackAdd.visibility = View.GONE
                        wrapDoctorAnswerFeedback.visibility = View.VISIBLE

                        imgDoctorAnswerFeedback.setImageResource(R.drawable.doctor_m)
                        lblDoctorAnswerFeedbackName.text = "Dr.${PubVariable.userInfo.nickname}"
                        lblDoctorAnswerFeedbackContent.text = patientCase.feedbackreply
                        lblDoctorAnswerFeedbackTime.text =
                            "답변일자 ${patientCase.feedbackreplytime.subSequence(0, 4)}년" +
                                    " ${patientCase.feedbackreplytime.subSequence(4, 6)}월" +
                                    " ${patientCase.feedbackreplytime.subSequence(6, 8)}일"

                    }
                }
            }
            // 답변 미작성
            "Q" -> {
                wrapDoctorAnswerNotRequest.visibility = View.GONE
                wrapDoctorAnswerRequest.visibility = View.VISIBLE
                wrapDoctorAnswerSubmit.visibility = View.VISIBLE

                doctorAnswerSubmit.setOnClickListener {
                    insertAnswer()
                }
            }
        }

    }

    //경과추가 답변 등록하는 펑션
    //질문 답변등록 하는 펑션
    @SuppressLint("SimpleDateFormat")
    fun insertAnswer(){

        Loading(ProgressBar, ProgressBg, true)

        var map = HashMap<String, String>()

        map["QKEY"] = patientCase.ckey
        map["UUID"] = PubVariable.uid
        map["CNUMBER"] = patientCase.cnumber
        map["ANSWERCONTENTS"] = doctorAnswerInput.text.toString()
        map["ANSWERDATE"] = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())

        ApiUtill().getUPDATE_FIRSTANSWER().update_firstanswer(map).enqueue(object :
            Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if(response.isSuccessful){
                    when(response.body()){
                        "S" -> checkPushInfo()
                        "F" -> failAlert()
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

    fun insertReply(){

        if(txtDoctorAnswerFeedback.text.isNullOrBlank()){

            Toast.makeText(this@DoctorAnsweredCheckActivity, "글을 입력해주세요.", Toast.LENGTH_SHORT).show()

        } else {

            var map = HashMap<String, String>()
            map["FEEDBACKREPLY"] = txtDoctorAnswerFeedback.text.toString()
            map["FEEDBACKREPLYTIME"] = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
            map["CKEY"] = patientCase.ckey
            map["CNUMBER"] = patientCase.cnumber

            ApiUtill().getUPDATE_REPLY().update_reply(map).enqueue(object : Callback<String>{
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@DoctorAnsweredCheckActivity, "리뷰에 대한 답글을 등록하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {

                    if(response.isSuccessful){

                        if(response.body() == "S") {

                            // 성공
                            Toast.makeText(this@DoctorAnsweredCheckActivity, "답글등록성공", Toast.LENGTH_SHORT).show()

                            checkPushInfo2()

                            wrapDoctorAnswerFeedbackAdd.visibility = View.GONE
                            wrapDoctorAnswerFeedback.visibility = View.VISIBLE

                            imgDoctorAnswerFeedback.setImageResource(R.drawable.doctor_m)
                            lblDoctorAnswerFeedbackName.text = "Dr.${PubVariable.userInfo.nickname}"
                            lblDoctorAnswerFeedbackContent.text = map["FEEDBACKREPLY"]
                            lblDoctorAnswerFeedbackTime.text =
                                "답변일자" +
                                " ${map["FEEDBACKREPLYTIME"].toString().subSequence(0, 4)}년" +
                                " ${map["FEEDBACKREPLYTIME"].toString() .subSequence(4, 6)}월" +
                                " ${map["FEEDBACKREPLYTIME"].toString() .subSequence(6, 8)}일"

                        } else {
                            Toast.makeText(this@DoctorAnsweredCheckActivity, "리뷰에 대한 답글을 등록하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        Toast.makeText(this@DoctorAnsweredCheckActivity, "리뷰에 대한 답글을 등록하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })



        }


    }


    //해당 환자의 푸시알림 동의 여부를 확인하는 펑션
    fun checkPushInfo(){

        var map = HashMap<String, String>()

        map["IDKEY"] = answerInfo.uuid

        ApiUtill().getSELECT_CHECKAGREE().select_checkagree(map).enqueue(object :
            Callback<ArrayList<pushinfo>> {

            override fun onResponse(call: Call<ArrayList<pushinfo>>, response: Response<ArrayList<pushinfo>>) {

                Loading(ProgressBar, ProgressBg, false)

                if(response.isSuccessful){

                    if(response.body()!!.size > 0){

                        var pushinfo : pushinfo = response.body()!![0]

                        if(pushinfo.SWITCH2 == "On"){

                            FCM.function.SendMsgToTarget(pushinfo.TOKEN, "${PubVariable.userInfo.nickname} 선생님이 답변을 등록하였습니다.", FCM.UserType.USER, FCM.PushType.USER_MYQUESTION)

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

    //해당 환자의 푸시알림 동의 여부를 확인하는 펑션
    fun checkPushInfo2(){

        var map = HashMap<String, String>()

        map["IDKEY"] = answerInfo.uuid

        ApiUtill().getSELECT_CHECKAGREE().select_checkagree(map).enqueue(object :
            Callback<ArrayList<pushinfo>> {

            override fun onResponse(call: Call<ArrayList<pushinfo>>, response: Response<ArrayList<pushinfo>>) {

                Loading(ProgressBar, ProgressBg, false)

                if(response.isSuccessful){

                    if(response.body()!!.size > 0){

                        var pushinfo : pushinfo = response.body()!![0]

                        if(pushinfo.SWITCH2 == "On"){

                            FCM.function.SendMsgToTarget(pushinfo.TOKEN, "${PubVariable.userInfo.nickname} 선생님이 ${answerInfo.title}질문의 리뷰에 답변을 등록하였습니다.", FCM.UserType.USER, FCM.PushType.USER_FEEDBACKREPLY)

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
            startActivity(Intent(this, DoctorAnsweredActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
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

}