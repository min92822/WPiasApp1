package com.phonegap.WPIAS.user_BurnInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.user_BurnInfo.burnInfoFragments.BurnFragment1
import com.phonegap.WPIAS.user_BurnInfo.burnInfoFragments.BurnFragment2
import kotlinx.android.synthetic.main.activity_burn_info.*
import kotlinx.android.synthetic.main.title_bar_skyblue.*

class BurnInfoActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_burn_info)

        SetTransparentBar()

        burnViewPager()
    }

    private fun burnViewPager() {

        txt_title.text = "화상정보"
        btn_back.setOnClickListener {
            onBackPressed()
        }

        burn_viewPager.adapter =
            BurnViewPagerSetting(
                supportFragmentManager
            )

        burn_tabLayout.addTab(burn_tabLayout.newTab().setText("화상종류"))
        burn_tabLayout.addTab(burn_tabLayout.newTab().setText("화상응급처치"))
//        burn_tabLayout.addTab(burn_tabLayout.newTab().setText("화상통계"))

        burn_tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                burn_viewPager.currentItem = p0!!.position

                if(p0.position == 0) {
                    img_burn_info_top.setImageResource(R.drawable.info_top_1)
                } else if(p0.position == 1){
                    img_burn_info_top.setImageResource(R.drawable.info_top_2)
                }
            }
        })

        burn_viewPager.addOnPageChangeListener(object : TabLayout.TabLayoutOnPageChangeListener(burn_tabLayout){})
    }

    class BurnViewPagerSetting(fm:FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

        var fragment = arrayOf(
            BurnFragment1(),
            BurnFragment2()
        ) //, BurnFragment3())

        override fun getItem(position: Int): Fragment {
            return fragment[position]
        }

        override fun getCount(): Int {
            return fragment.size
        }

    }
}
