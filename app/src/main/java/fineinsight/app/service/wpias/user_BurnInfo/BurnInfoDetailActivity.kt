package fineinsight.app.service.wpias.user_BurnInfo

import android.os.Bundle
import fineinsight.app.service.wpias.R
import fineinsight.app.service.wpias.RootActivity
import kotlinx.android.synthetic.main.in_burn_info_hatbit.*
import kotlinx.android.synthetic.main.in_burn_info_heubib.*
import kotlinx.android.synthetic.main.in_burn_info_hwahag.*
import kotlinx.android.synthetic.main.in_burn_info_hwayum.*
import kotlinx.android.synthetic.main.in_burn_info_jubchok.*
import kotlinx.android.synthetic.main.in_burn_info_junggi.*
import kotlinx.android.synthetic.main.in_burn_info_jungi.*
import kotlinx.android.synthetic.main.in_burn_info_juon.*
import kotlinx.android.synthetic.main.in_burn_info_machar.*
import kotlinx.android.synthetic.main.in_burn_info_yultang.*
import kotlinx.android.synthetic.main.title_bar_skyblue.*

class BurnInfoDetailActivity : RootActivity() {

    var m_xmlID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        m_xmlID = intent.getIntExtra("xml", 0)
        setContentView(m_xmlID)

        SetTransparentBar()
        imgClick()

    }

    fun imgClick() {

        btn_back.setOnClickListener {
            onBackPressed()
        }

        when (m_xmlID) {

            R.layout.in_burn_info_yultang -> {
                txt_title.text = "열탕화상"
                img_info_yultang.setOnClickListener {
                    img_info_yultang.setImageResource(R.drawable.burn_img_yultang)
                }
            }
            R.layout.in_burn_info_hwayum -> {
                txt_title.text = "화염화상"
                img_info_hwayum.setOnClickListener {
                    img_info_hwayum.setImageResource(R.drawable.burn_img_hwayum)
                }
            }
            R.layout.in_burn_info_jungi -> {
                txt_title.text = "전기화상"
                img_info_jungi.setOnClickListener {
                    img_info_jungi.setImageResource(R.drawable.burn_img_jungi)
                }
            }
            R.layout.in_burn_info_jubchok -> {
                txt_title.text = "접촉화상"
                img_info_jubchok.setOnClickListener {
                    img_info_jubchok.setImageResource(R.drawable.burn_img_jubchok)
                }
            }
            R.layout.in_burn_info_juon -> {
                txt_title.text = "저온화상"
                img_info_juon.setOnClickListener {
                    img_info_juon.setImageResource(R.drawable.burn_img_juon)
                }
            }
            R.layout.in_burn_info_hwahag -> {
                txt_title.text = "화학화상"
                img_info_hwahag.setOnClickListener {
                    img_info_hwahag.setImageResource(R.drawable.burn_img_hwahag)
                }
            }
            R.layout.in_burn_info_junggi -> {
                txt_title.text = "증기화상"
                img_info_junggi.setOnClickListener {
                    img_info_junggi.setImageResource(R.drawable.burn_img_junggi)
                }
            }
            R.layout.in_burn_info_machar -> {
                txt_title.text = "마찰화상"
                img_info_machar.setOnClickListener {
                    img_info_machar.setImageResource(R.drawable.burn_img_machar)
                }
            }
            R.layout.in_burn_info_hatbit -> {
                txt_title.text = "햇빛화상"
                img_info_hatbit.setOnClickListener {
                    img_info_hatbit.setImageResource(R.drawable.burn_img_hatbit)
                }
            }
            R.layout.in_burn_info_heubib -> {
                txt_title.text = "흡입화상"
                img_info_heubib.setOnClickListener {
                    img_info_heubib.setImageResource(R.drawable.burn_img_heubib)
                }
            }

            R.layout.in_aid_info_1 -> {
                txt_title.text = "응급처치 방법"
            }

            R.layout.in_aid_info_2 -> {
                txt_title.text = "응급처치 주의"
            }

            R.layout.in_aid_info_3 -> {
                txt_title.text = "소아화상 예방"
            }
        }
    }
}
