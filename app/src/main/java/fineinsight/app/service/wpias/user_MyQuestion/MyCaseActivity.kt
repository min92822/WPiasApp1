package fineinsight.app.service.wpias.user_MyQuestion

import android.os.Bundle
import fineinsight.app.service.wpias.R
import fineinsight.app.service.wpias.RootActivity
import kotlinx.android.synthetic.main.title_bar_skyblue.*

class MyCaseActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_case)

        SetTransparentBar()

        txt_title.text = "상담내역 보기"

    }
}
