package fineinsight.app.service.wpias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.title_bar_skyblue.*

class MyCaseActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_case)

        SetTransparentBar()

        txt_title.text = "상담내역 보기"

    }
}
