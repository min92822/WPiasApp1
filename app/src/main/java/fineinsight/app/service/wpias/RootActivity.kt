package fineinsight.app.service.wpias

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

open class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    fun setTransparentBar(){
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.statusBarColor(Color.argb())
    }

    fun Loading(progress : ProgressBar, progressBg : ConstraintLayout, boolean: Boolean){

        if(boolean){

            progress.visibility = View.VISIBLE
            progressBg.visibility = View.VISIBLE

        }else{

            progress.visibility = View.GONE
            progressBg.visibility = View.GONE

        }

    }

}