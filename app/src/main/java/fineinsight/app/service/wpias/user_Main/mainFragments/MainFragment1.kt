package fineinsight.app.service.wpias.user_Main.mainFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import fineinsight.app.service.wpias.user_BurnInfo.BurnInfoActivity
import fineinsight.app.service.wpias.user_MyQuestion.MyQuestionActivity
import fineinsight.app.service.wpias.R
import fineinsight.app.service.wpias.user_Main.MainActivity
import fineinsight.app.service.wpias.user_Setting.SettingActivity
import kotlinx.android.synthetic.main.main_fragment_1.view.*

class MainFragment1 : Fragment() {

    var v : View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.main_fragment_1, container, false)
        v = view
        buttonSetting(view)

        return view
    }

    private fun buttonSetting(view:View) {

        view.btn_burn_info.setOnClickListener {
            startActivity(Intent(activity, BurnInfoActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        view.btn_myquestion.setOnClickListener {
            startActivity(Intent(activity, MyQuestionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        view.btn_setting.setOnClickListener {
            startActivity(Intent(activity, SettingActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        view.textView4.setOnClickListener {
            MainActivity.viewPager?.currentItem = MainActivity.viewPager?.currentItem?.plus(1)!!
        }

        view.imageView.setOnClickListener {
            MainActivity.viewPager?.currentItem = MainActivity.viewPager?.currentItem?.plus(1)!!
        }

        view.imageView3.setOnClickListener {
            MainActivity.viewPager?.currentItem = MainActivity.viewPager?.currentItem?.plus(1)!!
        }

    }


}