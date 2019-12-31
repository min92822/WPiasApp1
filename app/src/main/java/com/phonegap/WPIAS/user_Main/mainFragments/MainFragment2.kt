package com.phonegap.WPIAS.user_Main.mainFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phonegap.WPIAS.user_Consulting.ConsultingActivity
import com.phonegap.WPIAS.user_MyQuestion.MyQuestionActivity
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.user_Main.MainActivity
import com.phonegap.WPIAS.user_Setting.SettingActivity
import kotlinx.android.synthetic.main.main_fragment_2.view.*

class MainFragment2 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.main_fragment_2, container, false)

        buttonSetting(view)

        return view
    }

    private fun buttonSetting(view:View) {

        view.btn_consulting.setOnClickListener {
            startActivity(Intent(activity, ConsultingActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        view.btn_myquestion.setOnClickListener {
            startActivity(Intent(activity, MyQuestionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        view.btn_setting.setOnClickListener {
            startActivity(Intent(activity, SettingActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        }

        view.imageView4.setOnClickListener {
            MainActivity.viewPager?.currentItem = MainActivity.viewPager?.currentItem?.minus(1)!!
        }

        view.textView34.setOnClickListener {
            MainActivity.viewPager?.currentItem = MainActivity.viewPager?.currentItem?.minus(1)!!
        }

        view.imageView2.setOnClickListener {
            MainActivity.viewPager?.currentItem = MainActivity.viewPager?.currentItem?.minus(1)!!
        }

        view.textView4.setOnClickListener {
            MainActivity.viewPager?.currentItem = MainActivity.viewPager?.currentItem?.plus(1)!!
        }

        view.imageView5.setOnClickListener {
            MainActivity.viewPager?.currentItem = MainActivity.viewPager?.currentItem?.plus(1)!!
        }

        view.imageView3.setOnClickListener {
            MainActivity.viewPager?.currentItem = MainActivity.viewPager?.currentItem?.plus(1)!!
        }

    }

}