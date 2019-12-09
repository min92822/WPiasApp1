package fineinsight.app.service.wpias

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_my_question.*
import kotlinx.android.synthetic.main.title_bar_skyblue.*

class MyQuestionActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_question)

        SetTransparentBar()
        textSetting()
        myViewSetting()
    }

    fun textSetting(){

        txt_title.text = "상담내역 보기"

    }

    fun myViewSetting(){

        recycler_my_question.layoutManager = LinearLayoutManager(this)
        recycler_my_question.adapter = MyQuestionAdater()

    }
}
