package fineinsight.app.service.wpias

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import fineinsight.app.service.wpias.mainFragments.MainFragment1
import fineinsight.app.service.wpias.mainFragments.MainFragment2
import fineinsight.app.service.wpias.mainFragments.MainFragment3
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager()
        setTransparentBar()
    }

    fun viewPager() {

        mainViewPager.adapter = mainViewPagerSetting(supportFragmentManager)
//        mainViewPager.setPageTransformer(false, ViewPager.PageTransformer { page, position ->
//
//            page.translationX = page.width * -position
//
//            if(position <= -1.0F || position >= 1.0F){
//                page.alpha = 0.0F
//            } else if(position == 0.0F) {
//                page.alpha = 1.0F
//            } else {
//                page.alpha = 1.0F - Math.abs(position)
//            }
//        })
    }

    fun setTransparentBar(){
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.statusBarColor(Color.argb())
    }


    class mainViewPagerSetting(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        var fragment = arrayOf(MainFragment1(), MainFragment2(), MainFragment3())

        override fun getItem(position: Int): Fragment {
            return fragment[position]
        }

        override fun getCount(): Int {
            return fragment.size
        }

    }
}






