package fineinsight.app.service.wpias

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.custom_alert.*

open class RootActivity : AppCompatActivity() {

    // 투명 상태바
    @SuppressLint("ObsoleteSdkInt")
    fun SetTransparentBar(){

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        // 하단 네비게이션 바까지 투명해짐
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    // 반투명 상태바
    fun SetDarkBar(){
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.argb(50,0,0,0)
        }
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


    // 종료 alert
    @SuppressLint("SetTextI18n")
    fun CloseAlert(context:Context){

        var dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_end)

        title.text = "종료"
        sub.text = "WPIAS를 종료하시겠습니까?"

        btn_left.text = "종료하기"
        btn_left.setBackgroundResource(R.drawable.btn_red)

        btn_right.text = "아니요"
        btn_right.setBackgroundResource(R.drawable.btn_blue)


        btn_left.setOnClickListener {
            finishAffinity()
        }

        btn_right.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}