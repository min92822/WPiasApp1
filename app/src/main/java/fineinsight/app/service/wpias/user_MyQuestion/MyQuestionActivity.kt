package fineinsight.app.service.wpias.user_MyQuestion

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import fineinsight.app.service.wpias.R
import fineinsight.app.service.wpias.RootActivity
import fineinsight.app.service.wpias.dataClass.QuestionInfo
import fineinsight.app.service.wpias.publicObject.PubVariable
import fineinsight.app.service.wpias.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_my_question.*
import kotlinx.android.synthetic.main.title_bar_skyblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyQuestionActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_question)

        SetTransparentBar()

    }

    fun myViewSetting(){

        ProgressAction(true)

        txt_title.text = "상담내역 보기"
        btn_back.setOnClickListener {
            onBackPressed()
        }
        txt_my_name.text = PubVariable.userInfo.nickname

        var myGender : String

        if (PubVariable.userInfo.gender == "F") {
            myGender = "여성"
            img_my_profile.setImageResource(R.drawable.female)
        } else {
            myGender = "남성"
            img_my_profile.setImageResource(R.drawable.male)
        }

        var map = HashMap<String,String>()

        map["UUID"] = FirebaseAuth.getInstance().currentUser?.uid!!

        ApiUtill().getSELECT_MYQUESTION().select_myquestion(map).enqueue(object : Callback<ArrayList<QuestionInfo>>{
            override fun onFailure(call: Call<ArrayList<QuestionInfo>>, t: Throwable) {
                Toast.makeText(this@MyQuestionActivity, "${t.message}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ArrayList<QuestionInfo>>, response: Response<ArrayList<QuestionInfo>>) {

                if (response.isSuccessful){

                    var arr = response.body() as ArrayList<QuestionInfo>

                    txt_my_gender_question.text = myGender + " | 질문수 : " + arr.size
                    layout_my_question.visibility = View.VISIBLE

                    if(arr.size == 0){
                        ProgressAction(false)

                        recycler_my_question.visibility = View.GONE

                    } else {
                        ProgressAction(false)
                        recycler_my_question.layoutManager = LinearLayoutManager(this@MyQuestionActivity)
                        recycler_my_question.adapter =
                            MyQuestionAdapter(
                                arr,
                                this@MyQuestionActivity
                            )
                    }


                } else {
                    ProgressAction(false)
                    Toast.makeText(this@MyQuestionActivity, "질문이 없습니다.", Toast.LENGTH_SHORT).show()
                }

            }
        })

    }

    fun ProgressAction(isShow:Boolean)
    {
        if(isShow)
        {

            Progress_circle.visibility = View.VISIBLE
            Progress_bg.visibility = View.VISIBLE
            this.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        else
        {
            Progress_circle.visibility = View.GONE
            Progress_bg.visibility = View.GONE
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    override fun onResume() {
        super.onResume()
        myViewSetting()
    }

    override fun onBackPressed() {
        finish()
    }


}
