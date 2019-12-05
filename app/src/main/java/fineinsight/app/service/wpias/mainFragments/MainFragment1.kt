package fineinsight.app.service.wpias.mainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fineinsight.app.service.wpias.R
import kotlinx.android.synthetic.main.main_fragment_1.*

class MainFragment1 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.main_fragment_1, container, false)

        buttonSetting()

        return view
    }

    private fun buttonSetting() {

//        activity!!.btn_burn_info.setOnClickListener {
//
//        }

    }

}