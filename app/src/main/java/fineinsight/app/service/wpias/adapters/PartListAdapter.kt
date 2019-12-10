package fineinsight.app.service.wpias.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fineinsight.app.service.wpias.ConsultingActivity
import fineinsight.app.service.wpias.R
import kotlinx.android.synthetic.main.activity_consulting.*
import kotlinx.android.synthetic.main.body_part_list.view.*

class PartListAdapter(var arr : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    lateinit var context : Context

    var arr2 = ArrayList<String>()
    var fHead = arrayListOf("이마", "눈", "코", "입", "귀", "볼", "기타")
    var bHead = arrayListOf("목", "뒤통수", "목덜미", "기타")
    var fShoulder = arrayListOf("좌측 어깨", "우측 어깨", "양쪽 어깨", "기타")
    var bShoulder = arrayListOf("좌측 어깨", "우측 어깨", "양쪽 어깨", "기타")
    var chest = arrayListOf("좌측 가슴", "우측 가슴", "가슴 전체", "기타")
    var back = arrayListOf("좌측 등", "우측 등", "등 전체", "기타")
    var belly = arrayListOf("좌측 배", "우측 배", "배 전체", "기타")
    var waist = arrayListOf("좌측 허리", "우측 허리", "허리 전체", "기타")
    var fArms = arrayListOf("좌측 팔뚝", "우측 팔뚝", "팔뚝 전체", "좌측 하박", "우측 하박", "하박 전체", "기타")
    var bArms = arrayListOf("좌측 팔뚝", "우측 팔뚝", "팔뚝 전체", "좌측 하박", "우측 하박", "하박 전체", "기타")
    var fHands = arrayListOf("좌측 손바닥", "우측 손바닥", "양쪽 손바닥", "좌측 손등", "우측 손등", "양쪽 손등", "좌측 손가락", "우측 손가락", "양쪽 손가락", "기타")
    var bHands = arrayListOf("좌측 손바닥", "우측 손바닥", "양쪽 손바닥", "좌측 손등", "우측 손등", "양쪽 손등", "좌측 손가락", "우측 손가락", "양쪽 손가락", "기타")
    var privatePart = arrayListOf("음부", "기타")
    var hip = arrayListOf("좌측 엉덩이", "우측 엉덩이", "엉덩이 전체", "기타")
    var fLegs = arrayListOf("좌측 허벅지", "우측 허벅지", "양쪽 허벅지", "좌측 종아리", "우측 종아리", "양쪽 종아리", "기타")
    var bLegs = arrayListOf("좌측 허벅지", "우측 허벅지", "양쪽 허벅지", "좌측 종아리", "우측 종아리", "양쪽 종아리", "기타")
    var fFoot = arrayListOf("좌측 발등", "우측 발등", "양쪽 발등", "좌측 발가락", "우측 발가락", "양쪽 발가락", "좌측 발바닥", "우측 발바닥", "양쪽 발바닥", "좌측 뒤꿈치", "우측 뒤꿈치", "양쪽 뒤꿈치", "기타")
    var bFoot = arrayListOf("좌측 발등", "우측 발등", "양쪽 발등", "좌측 발가락", "우측 발가락", "양쪽 발가락", "좌측 발바닥", "우측 발바닥", "양쪽 발바닥", "좌측 뒤꿈치", "우측 뒤꿈치", "양쪽 뒤꿈치", "기타")
    var respiratory = arrayListOf("호흡기", "기타")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        context = parent.context

        var view = LayoutInflater.from(parent.context).inflate(R.layout.body_part_list, parent, false)

        return partListViewHolder(view)

    }

    override fun getItemCount(): Int { return arr.size }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as partListViewHolder

        holder.bodyPart.text = arr[position]

        //context가 컨설팅 액티비티일때
        if (context is ConsultingActivity) {

            holder.bodyPartWrapper.setOnClickListener {

                whenLoad(position)

            }

        }

    }

    //리싸이클러 뷰 실행시 이미지 및 부위 설정을 설정하는 펑션
    fun whenLoad(position: Int){
        var fb = ""

        //라디오버튼 체크 유무를 확인해 front, back을 구분한다
        fb = if ((context as ConsultingActivity).bodyFront.isChecked) {
            "front"
        } else {
            "back"
        }

        selectArr(fb, arr[position])

        //부위 클릭시 상세 부위 리싸이클러 뷰를 실행시킨다
        (context as ConsultingActivity).bodyPartDetailRecyclerView.layoutManager =
            GridLayoutManager(context, 3)
        (context as ConsultingActivity).bodyPartDetailRecyclerView.adapter =
            PartDetailAdapter(arr2)
    }

    //화상 입은 부위에 따른 arr 정리 하는 기능
    //부위 선택시마다 이미지가 바뀌게 한다
    fun selectArr(fb : String, part : String){
        when(fb){

            "front" -> {
                when(part){
                    "머리" -> {
                        arr2 = fHead
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.mori_f)
                    }
                    "어깨" -> {
                        arr2 = fShoulder
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.akke_f)
                    }
                    "가슴" -> {
                        arr2 = chest
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.gasem_f)
                    }
                    "배" -> {
                        arr2 = belly
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.bae_f)
                    }
                    "팔" -> {
                        arr2 = fArms
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.arm_f)
                    }
                    "손" -> {
                        arr2 = fHands
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.sun_f)
                    }
                    "음부" -> {
                        arr2 = privatePart
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.umbu)
                    }
                    "다리" -> {
                        arr2 = fLegs
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.dari_f)
                    }
                    "발" -> {
                        arr2 = fFoot
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.bar_f)
                    }
                    "호흡기" -> {
                        arr2 = respiratory
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.hohm)
                    }
                }
            }

            "back" -> {
                when(part){
                    "머리" -> {
                        arr2 = bHead
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.mori_b)
                    }
                    "어깨" -> {
                        arr2 = bShoulder
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.akke_b)
                    }
                    "등" -> {
                        arr2 = back
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.gasem_b)
                    }
                    "허리" -> {
                        arr2 = waist
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.bae_b)
                    }
                    "팔" -> {
                        arr2 = bArms
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.arm_b)
                    }
                    "손" -> {
                        arr2 = bHands
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.sun_b)
                    }
                    "엉덩이" -> {
                        arr2 = hip
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.umbu_b)
                    }
                    "다리" -> {
                        arr2 = bLegs
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.dari_b)
                    }
                    "발" -> {
                        arr2 = bFoot
                        (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.bar_b)
                    }
                }
            }
        }
    }

    class partListViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var bodyPart = view.bodyPart
        var bodyPartWrapper = view.bodyPartWrapper
    }

}