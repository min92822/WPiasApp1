package fineinsight.app.service.wpias.burnInfoFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fineinsight.app.service.wpias.BurnInfoDetailActivity
import fineinsight.app.service.wpias.R
import kotlinx.android.synthetic.main.burn_fragment_1.view.*

// 화상정보
class BurnFragment1 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.burn_fragment_1, container, false)

        btnSetting(view)

        return view
    }

    fun btnSetting(v:View){

        v.wrap_yultang.setOnClickListener{startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_burn_info_yultang))}
        v.wrap_hwayum.setOnClickListener{startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_burn_info_hwayum))}
        v.wrap_jungi.setOnClickListener{startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_burn_info_jungi))}
        v.wrap_jubchok.setOnClickListener{startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_burn_info_jubchok))}
        v.wrap_juon.setOnClickListener{startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_burn_info_juon))}
        v.wrap_hwahag.setOnClickListener{startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_burn_info_hwahag))}
        v.wrap_junggi.setOnClickListener{startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_burn_info_junggi))}
        v.wrap_machar.setOnClickListener{startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_burn_info_machar))}
        v.wrap_hatbit.setOnClickListener{startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_burn_info_hatbit))}
        v.wrap_heubib.setOnClickListener{startActivity(Intent(activity, BurnInfoDetailActivity::class.java).putExtra("xml", R.layout.in_burn_info_heubib))}


    }
}