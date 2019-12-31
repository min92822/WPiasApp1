package com.phonegap.WPIAS.user_BurnInfo.burnInfoFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.phonegap.WPIAS.user_BurnInfo.BurnInfoDetailActivity
import com.phonegap.WPIAS.R
import kotlinx.android.synthetic.main.burn_fragment_2.view.*


// 화상응급처치
class BurnFragment2 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.burn_fragment_2, container, false)

        btnSetting(view)

        return view
    }

    fun btnSetting(v:View){

        v.wrap_aid_1.setOnClickListener {startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_aid_info_1))}
        v.wrap_aid_2.setOnClickListener {startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_aid_info_2))}
        v.wrap_aid_3.setOnClickListener {startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_aid_info_3))}

    }
}