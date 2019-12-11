package fineinsight.app.service.wpias

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fineinsight.app.service.wpias.adapters.CauseOfBurnedAdapter
import fineinsight.app.service.wpias.adapters.PartListAdapter
import kotlinx.android.synthetic.main.activity_consulting.*

class ConsultingActivity : RootActivity(){

    var bodyPartFront = arrayListOf("머리", "어깨", "가슴", "배", "팔", "손", "음부", "다리", "발", "호흡기")
    var bodyPartBack = arrayListOf("머리", "어깨", "등", "허리", "팔", "손", "엉덩이", "다리", "발")
    var causeCategory = arrayListOf("열탕", "화염", "전기", "접촉", "저온", "화학", "증기", "마찰", "햇빛", "흡입")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulting)

        SetTransparentBar()

        bodyPartCheck()

        genderTouch()

        causeRecyclerViewActivated()

        eventInit()

    }

    //신체 부위 리싸이클러뷰 활성화 및 부위 버튼 활성화
    fun eventInit(){

        bodyFront.isChecked = true

        male.isChecked = true

    }

    //앞 뒤 구분 버튼 체크 이벤트
    //버튼 및 버튼 글자 색 변환이 이벤트에 걸려있음
    fun bodyPartCheck(){

        bodyFront.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.white))
                bodyBack.setTextColor(ContextCompat.getColor(this, R.color.dark_sky_blue_four))
                frontActivated()
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.dark_sky_blue_four))
                bodyBack.setTextColor(ContextCompat.getColor(this, R.color.white))
            }

        }

        bodyBack.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.white))
                bodyFront.setTextColor(ContextCompat.getColor(this, R.color.dark_sky_blue_four))
                backActivated()
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.dark_sky_blue_four))
                bodyFront.setTextColor(ContextCompat.getColor(this, R.color.white))
            }

        }

    }

    //앞 버튼 터치시 실행되는 리싸이클러 뷰
    fun frontActivated(){

        var partListAdapter = PartListAdapter(bodyPartFront)

        bodyPartRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        bodyPartRecyclerView.adapter = partListAdapter

    }

    //뒤 버튼 터치시 실행되는 리싸이클러 뷰
    fun backActivated(){

        var partListAdapter = PartListAdapter(bodyPartBack)

        bodyPartRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        bodyPartRecyclerView.adapter = partListAdapter

    }

    //성별 버튼 터치 펑션
    fun genderTouch(){

        male.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.white))
                female.setTextColor(ContextCompat.getColor(this, R.color.dark_sky_blue_four))
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.dark_sky_blue_four))
                female.setTextColor(ContextCompat.getColor(this, R.color.white))
            }

        }

        female.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.white))
                male.setTextColor(ContextCompat.getColor(this, R.color.dark_sky_blue_four))
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.dark_sky_blue_four))
                male.setTextColor(ContextCompat.getColor(this, R.color.white))
            }

        }

    }

    //화상 원인 버튼 카테고리 뿌려주는 펑션
    fun causeRecyclerViewActivated() {

        causeOfBurnedRecyclerView.layoutManager = GridLayoutManager(this, 4)
        causeOfBurnedRecyclerView.adapter = CauseOfBurnedAdapter(causeCategory)

    }

}