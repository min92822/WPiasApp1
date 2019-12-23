package fineinsight.app.service.wpias.user_Main.mainFragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import fineinsight.app.service.wpias.user_MyQuestion.MyQuestionActivity
import fineinsight.app.service.wpias.R
import fineinsight.app.service.wpias.SettingActivity
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.main_fragment_3.view.*

class MainFragment3 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.main_fragment_3, container, false)

        buttonSetting(view)

        return view
    }

    private fun buttonSetting(view:View) {

        view.btn_compare.setOnClickListener {
//            startActivity(Intent(activity, ㅇㅅㅇ::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            getReadyUpdateAlert(view)
        }

        view.btn_myquestion.setOnClickListener {
            startActivity(Intent(activity, MyQuestionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        view.btn_setting.setOnClickListener {
            startActivity(Intent(activity, SettingActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

    }

    fun getReadyUpdateAlert(view:View){
        var dialog = Dialog(view.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_end)

        title.text = "준비중"
        sub.text = "업데이트 준비중입니다."

        btn_left.visibility = View.GONE

        btn_right.text = "확인"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        btn_right.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

}