package fineinsight.app.service.wpias.burnInfoFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fineinsight.app.service.wpias.R

class BurnFragment1 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.burn_fragment_1, container, false)


        return view
    }
}