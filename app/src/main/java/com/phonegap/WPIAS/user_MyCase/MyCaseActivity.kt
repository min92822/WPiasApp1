package com.phonegap.WPIAS.user_MyCase

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.dataClass.MycaseInfo
import com.phonegap.WPIAS.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_my_case.*
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.title_bar_skyblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MyCaseActivity : RootActivity() {

    var m_myCaseInfo : MycaseInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_case)

        m_myCaseInfo = intent.getSerializableExtra("myCase") as MycaseInfo

        SetTransparentBar()

        myCaseSetting()

    }

    fun myCaseSetting(){
        txt_title.text = "상담내역 보기"
        btn_back.setOnClickListener {
            super.onBackPressed()
        }

        //기본 사진
        Glide.with(this)
            .load(m_myCaseInfo!!.imageurl1)
            .into(img_mycase)

        // 버튼 선택시 사진
        btn_mycase_close.setOnClickListener {
            Glide.with(this)
                .load(m_myCaseInfo!!.imageurl1)
                .into(img_mycase)
            btn_mycase_close.setBackgroundResource(R.color.windows_blue)
            btn_mycase_close.setTextColor(ContextCompat.getColor(this, R.color.white))
            btn_mycase_over.setBackgroundResource(R.drawable.btn_stroke)
            btn_mycase_over.setTextColor(ContextCompat.getColor(this, R.color.windows_blue))
        }

        btn_mycase_over.setOnClickListener {
            Glide.with(this)
                .load(m_myCaseInfo!!.imageurl2)
                .into(img_mycase)
            btn_mycase_close.setBackgroundResource(R.drawable.btn_stroke)
            btn_mycase_close.setTextColor(ContextCompat.getColor(this, R.color.windows_blue))
            btn_mycase_over.setBackgroundResource(R.color.windows_blue)
            btn_mycase_over.setTextColor(ContextCompat.getColor(this, R.color.white))
        }

        // 상담 내용
        txt_mycase_content.text = m_myCaseInfo!!.contents


        // 답변 상태에 따른 visibility
        when (m_myCaseInfo!!.casestatus) {
            "Q" -> {
                lbl_no_answer.text = "답변을 기다리는 중입니다."
                lbl_no_answer.visibility = View.VISIBLE

            }
            "A" -> {

                wrap_mycase_answer.visibility = View.VISIBLE
                lbl_no_answer.visibility = View.GONE

                txt_mycase_dr.text = "Dr.${m_myCaseInfo!!.answerdocnm}"
                txt_mycase_dr_detail.text = "화상외과 | 답변수 : ${m_myCaseInfo!!.answerdoccount}"

                txt_mycase_answer.text = m_myCaseInfo!!.answercontents



                //피드백이 등록되어 있지 않을 때
                if(m_myCaseInfo!!.feedbacktime.isNullOrEmpty())
                {

                    rating_mycase.setOnRatingBarChangeListener { ratingBar, fl, b ->
                        if(ratingBar.rating < 1F){
                            ratingBar.rating = 1F
                        }
                    }

                    btn_mycase_review_submit.setOnClickListener {

                        ratingAlert()
                    }
                }
                else//피드백이 등록되어 있을 때
                {
                    rating_mycase.rating = m_myCaseInfo!!.feedbackstar.toFloat()
                    rating_mycase.setIsIndicator(true)
                    btn_mycase_review_submit.visibility = View.GONE

                    if(m_myCaseInfo!!.feedbacktext.isNullOrEmpty()){
                        txt_mycase_review.hint = ""
                    }

                    txt_mycase_review.setText(m_myCaseInfo!!.feedbacktext)

                    txt_mycase_review.isEnabled = false
                    textView38.setText("소중한 리뷰 감사합니다.")
                }



            }
            "P" -> {
                lbl_no_answer.text = "답변을 요청하지 않은 경과입니다."
                lbl_no_answer.visibility = View.VISIBLE
            }
        }

    }

    // 답변 리뷰
    fun feedbackSubmit(){

        ProgressAction(true)

        var map = HashMap<String, String>()

        map["FEEDBACKSTAR"] = rating_mycase.rating.toString()
        map["FEEDBACKTEXT"] = txt_mycase_review.text.toString()
        map["FEEDBACKTIME"] = SimpleDateFormat("yyyyMMddkkmmss").format(Calendar.getInstance().time)
        map["CKEY"] = m_myCaseInfo!!.ckey
        map["CNUMBER"] = m_myCaseInfo!!.cnumber

        ApiUtill().getUPDATE_FEEDBACK().update_feedback(map).enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                ProgressAction(false)
                Toast.makeText(this@MyCaseActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if(response.body() == "S"){
                    ProgressAction(false)

                    rating_mycase.setIsIndicator(true)
                    btn_mycase_review_submit.visibility = View.GONE
                    txt_mycase_review.isEnabled = false
                    txt_mycase_review.hint = ""
                    textView38.setText("소중한 리뷰 감사합니다.")

                    successAlert()
                }
            }
        })
    }

    fun ratingAlert(){
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_ck)

        title.text = "리뷰"
        sub.text = "리뷰를 등록하시겠습니까?"

        btn_left.text = "취소"
        btn_left.setBackgroundResource(R.drawable.btn_gray)

        btn_right.text = "등록"
        btn_right.setBackgroundResource(R.drawable.btn_blue)


        btn_left.setOnClickListener {
            dialog.dismiss()
        }

        btn_right.setOnClickListener {
            feedbackSubmit()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun successAlert(){
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_ck)

        title.text = "완료"
        sub.text = "소중한 평가 감사합니다."

        btn_left.visibility = View.GONE

        btn_right.text = "확인"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        btn_right.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun ProgressAction(isShow:Boolean)
    {
        if(isShow)
        {

            this.Progress_circle.visibility = View.VISIBLE
            this!!.Progress_bg.visibility = View.VISIBLE
            this!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        else
        {
            this!!.Progress_circle.visibility = View.GONE
            this!!.Progress_bg.visibility = View.GONE
            this!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }



}
