package fineinsight.app.service.wpias.user_Consulting.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import fineinsight.app.service.wpias.user_Consulting.ConsultingActivity
import fineinsight.app.service.wpias.R
import fineinsight.app.service.wpias.publicObject.Validation
import kotlinx.android.synthetic.main.activity_consulting.*
import kotlinx.android.synthetic.main.body_part_detail.view.*

class PartDetailAdapter(fb : String, category : String, var context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var chkBoxArr = ArrayList<View>()
    var arr = ArrayList<String>()
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

    init {

        selectArr(fb, category)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.body_part_detail, parent, false)
        return PartDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as PartDetailViewHolder
        holder.detailPart.text = arr[position]

        (chkBoxArr[position].appCompatCheckBox as AppCompatCheckBox).setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){

                for(chkBox in chkBoxArr){

                    if(chkBox != chkBoxArr[position]){
                        (chkBox.appCompatCheckBox as AppCompatCheckBox).isChecked = false
                    }

                }

                buttonView.isClickable = false

                Validation.vali.bodyDetailV = (position + 1).toString().padStart(3, '0')

                if(chkBoxArr[position].detailPart.text == "기타"){

                    Validation.vali.bodyGitaV = chkBoxArr[position].detailPart.text.toString()

                }else{

                    Validation.vali.bodyGitaV = ""

                }

            }else{

                buttonView.isClickable = true

            }

        }

    }

    //화상 입은 부위에 따른 arr 정리 하는 기능
    //부위 선택시마다 이미지가 바뀌게 한다
    fun selectArr(fb : String, part : String){
        when(fb){

            "front" -> {

                (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.f)

                when(part){
                    "머리" -> {
                        arr = fHead
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.mori)
                    }
                    "어깨" -> {
                        arr = fShoulder
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.akke)
                    }
                    "가슴" -> {
                        arr = chest
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.gasem)
                    }
                    "배" -> {
                        arr = belly
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.bae)
                    }
                    "팔" -> {
                        arr = fArms
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.arm)
                    }
                    "손" -> {
                        arr = fHands
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.sun)
                    }
                    "음부" -> {
                        arr = privatePart
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.umbu_f)
                    }
                    "다리" -> {
                        arr = fLegs
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.dari_f)
                    }
                    "발" -> {
                        arr = fFoot
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.bar)
                    }
                    "호흡기" -> {
                        arr = respiratory
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.hohm)
                    }
                }
            }

            "back" -> {

                (context as ConsultingActivity).bodyImage.setImageResource(R.drawable.b)

                when(part){
                    "머리" -> {
                        arr = bHead
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.mori)
                    }
                    "어깨" -> {
                        arr = bShoulder
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.akke)
                    }
                    "등" -> {
                        arr = back
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.gasem)
                    }
                    "허리" -> {
                        arr = waist
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.bae)
                    }
                    "팔" -> {
                        arr = bArms
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.arm)
                    }
                    "손" -> {
                        arr = bHands
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.sun)
                    }
                    "엉덩이" -> {
                        arr = hip
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.umbu_b)
                    }
                    "다리" -> {
                        arr = bLegs
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.dari_b)
                    }
                    "발" -> {
                        arr = bFoot
                        (context as ConsultingActivity).bodyImage.setBackgroundResource(R.drawable.bar)
                    }
                }
            }
        }
    }

    inner class PartDetailViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var detailPart = view.detailPart
        var detailPartWrapper = view.detailPartWrapper
        var appCompatCheckBox = view.appCompatCheckBox

        init {

            chkBoxArr.add(view)

            detailPartWrapper.setOnClickListener {

                appCompatCheckBox.isChecked = true

            }

        }

    }

}