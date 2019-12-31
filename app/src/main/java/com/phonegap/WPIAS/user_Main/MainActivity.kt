package com.phonegap.WPIAS.user_Main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.user_Main.mainFragments.MainFragment1
import com.phonegap.WPIAS.user_Main.mainFragments.MainFragment2
import com.phonegap.WPIAS.user_Main.mainFragments.MainFragment3
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

@SuppressLint("StaticFieldLeak")
class MainActivity : RootActivity() {

    companion object {
        var viewPager : ViewPager? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPermissions()
        mainViewPager()
        SetTransparentBar()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        CloseAlert(this)
    }

    fun mainViewPager() {

        viewPager = mainViewPager

        mainViewPager.adapter =
            MainViewPagerAdapter(
                supportFragmentManager
            )
        mainViewPager.currentItem = 1
        mainViewPager.setPageTransformer(false) { page, position ->

            if(position <= -1.0F || position >= 1.0F){
                page.alpha = 0.0F
                page.translationX = 0.0F
            } else if(position == 0.0F) {
                page.alpha = 1.0F
                page.translationX = 1.0F
            } else {
                page.alpha = 1.0F - abs(position)
                page.translationX = page.width * -position
            }
        }

//        indicator.setViewPager(mainViewPager)


    }

    //퍼미션 체크
    fun setupPermissions() {

        val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(this, permissions, 0)

    }

    class MainViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        var fragment = arrayOf(
            MainFragment1(),
            MainFragment2(),
            MainFragment3()
        )

        override fun getItem(position: Int): Fragment {
            return fragment[position]
        }

        override fun getCount(): Int {
            return fragment.size
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }

    }
}






