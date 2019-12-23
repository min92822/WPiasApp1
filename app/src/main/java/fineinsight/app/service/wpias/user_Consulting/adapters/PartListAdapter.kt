package fineinsight.app.service.wpias.user_Consulting.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fineinsight.app.service.wpias.user_Consulting.ConsultingActivity
import fineinsight.app.service.wpias.R
import kotlinx.android.synthetic.main.activity_consulting.*
import kotlinx.android.synthetic.main.body_part_list.view.*
import android.os.SystemClock
import fineinsight.app.service.wpias.publicObject.Validation

class PartListAdapter(var arr : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    lateinit var context : Context

    var viewArr = ArrayList<View>()

    var isFirst = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        context = parent.context

        var view = LayoutInflater.from(parent.context).inflate(R.layout.body_part_list, parent, false)

        return PartListViewHolder(view)

    }

    override fun getItemCount(): Int { return arr.size }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as PartListViewHolder

        holder.bodyPart.text = arr[position]

    }

    //리싸이클러 뷰 실행시 이미지 및 부위 설정을 설정하는 펑션
    fun whenLoad(position: Int){

        //강제로 터치 이벤트 발생시킴
        //무한 반복 boolean으로 막음
        if(position == 0 && isFirst){

            viewArr[0].dispatchTouchEvent(
                MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(),
                    MotionEvent.ACTION_DOWN,
                    viewArr[0].x,
                    viewArr[0].y,
                    0
                )
            )

            viewArr[0].dispatchTouchEvent(
                MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis() + 100,
                    MotionEvent.ACTION_UP,
                    viewArr[0].x,
                    viewArr[0].y,
                    0
                )
            )

            isFirst = false

        }

        var fb = ""

        //라디오버튼 체크 유무를 확인해 front, back을 구분한다
        fb = if ((context as ConsultingActivity).bodyFront.isChecked) {
            "front"
        } else {
            "back"
        }

        //부위 클릭시 상세 부위 리싸이클러 뷰를 실행시킨다
        (context as ConsultingActivity).bodyPartDetailRecyclerView.layoutManager =
            GridLayoutManager(context, 3)
        (context as ConsultingActivity).bodyPartDetailRecyclerView.adapter =
            PartDetailAdapter(
                fb,
                arr[position],
                context
            )

    }

    inner class PartListViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var bodyPart = view.bodyPart
        var bodyPartWrapper = view.bodyPartWrapper

        init {

            //뷰홀더 뷰들을 리스트에 집어넣음
            viewArr.add(view)

            bodyPartWrapper.setOnClickListener {

                //온 클릭 이벤트에 viewArr 반복문을 실행하고
                //index 위치와 adapter position을 비교해 같으면 색을 띄운다
                for((i, bodyPartView) in viewArr.withIndex()){

                    if(i == adapterPosition){
                        bodyPartView.bodyPart.setTextColor(ContextCompat.getColor(context, R.color.ocean_blue))
                    }else{
                        bodyPartView.bodyPart.setTextColor(ContextCompat.getColor(context, R.color.black))
                    }

                }

                whenLoad(adapterPosition)
                Validation.vali.bodyStyleV = (adapterPosition + 1).toString().padStart(3, '0')

            }

            whenLoad(0)

        }

    }

}