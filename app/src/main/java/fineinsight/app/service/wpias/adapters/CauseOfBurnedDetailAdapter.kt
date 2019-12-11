package fineinsight.app.service.wpias.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fineinsight.app.service.wpias.R
import kotlinx.android.synthetic.main.cause_of_burned_detail.view.*

class CauseOfBurnedDetailAdapter(category : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var viewArr = ArrayList<View>()

    var arr = ArrayList<String>()

    var boiling = arrayListOf("뜨거운 차, 커피", "정수기, 온수꼭지", "라면 등의 국물", "프라이팬 기름", "커피포트, 주전자", "끓는 국, 곰탕, 찌개", "국자 속 국물", "기타")
    var flame = arrayListOf("기름으로 인한 화염", "가스배너로 인한 화염", "시너 등으로 인한 화염", "성냥으로 인한 화염", "기타")
    var electric = arrayListOf("전기 콘센트", "기타")
    var touched = arrayListOf("구이용 석쇠", "다리미", "난로 등 전열 기구", "조리기구", "기타")
    var lowTemperature = arrayListOf("핫팩", "전기장판", "휴대폰", "기타")
    var chemical = arrayListOf("빙초산 등 강산성", "강알칼리성 물질", "락스", "기타")
    var steam = arrayListOf("가열식 가습기", "밥솥", "기타")
    var friction = arrayListOf("런닝머신", "미끄럼틀", "기타")
    var sunburned = arrayListOf("햇빛", "가타")
    var smoke = arrayListOf("유독가스", "기타")

    init {
        when(category){
            "열탕" -> arr = boiling
            "화염" -> arr = flame
            "전기" -> arr = electric
            "접촉" -> arr = touched
            "저온" -> arr = lowTemperature
            "화학" -> arr = chemical
            "증기" -> arr = steam
            "마찰" -> arr = friction
            "햇빛" -> arr = sunburned
            "흡입" -> arr = smoke
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.cause_of_burned_detail, parent, false)

        return CauseOfBurnedDetailViewHolder(view)

    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as CauseOfBurnedDetailViewHolder

        holder.causeDetail.text = arr[position]

        holder.causeOfBurnedDetailImage.setImageResource(R.drawable.yultang_1)

    }

    inner class CauseOfBurnedDetailViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var causeOfBurnedDetailWrapper = view.causeOfBurnedDetailWrapper
        var causeOfBurnedDetailCheckWrapper = view.causeOfBurnedDetailCheckWrapper
        var causeOfBurnedDetailImage = view.causeOfBurnedDetailImage
        var causeDetail = view.causeDetail
        var appCompatCheckBox = view.appCompatCheckBox

        init {

            viewArr.add(view)

        }

    }

}