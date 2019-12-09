package fineinsight.app.service.wpias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import fineinsight.app.service.wpias.publicObject.PubVariable
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        SetDarkBar()
        btnSetting()
    }

    fun btnSetting(){

        btn_logout.setOnClickListener {

            PubVariable.init()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@SettingActivity, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))

        }

    }
}
