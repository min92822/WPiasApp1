package fineinsight.app.service.wpias.adapters

import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fineinsight.app.service.wpias.ConsultingActivity
import fineinsight.app.service.wpias.R
import kotlinx.android.synthetic.main.activity_consulting.*
import kotlinx.android.synthetic.main.cause_of_burned.view.*

class CauseOfBurnedAdapter(var arr : ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context : Context

    var viewArr = ArrayList<View>()

    var isFirst = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        context = parent.context

        var view = LayoutInflater.from(parent.context).inflate(R.layout.cause_of_burned, parent, false)

        return CauseOfBurnedViewHolder(view)

    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder as CauseOfBurnedViewHolder

        holder.cause.text = arr[position]

    }

    //화상 원인 클릭시 메서드
    fun whenLoad(position: Int){

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

        (context as ConsultingActivity).causeOfBurnedDetailRecyclerView.layoutManager = GridLayoutManager(context, 3)
        (context as ConsultingActivity).causeOfBurnedDetailRecyclerView.adapter = CauseOfBurnedDetailAdapter(viewArr[position].cause.text.toString())

    }

    inner class CauseOfBurnedViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var cause = view.cause
        var causeWrapper = view.causeWrapper

        init {

            viewArr.add(view)

            //화상 원인 버튼 클릭 이벤트
            //클릭시 버튼 변화 화상 원인 상세 사항 체크박스 나열
            causeWrapper.setOnClickListener {

                for((i, causeView) in viewArr.withIndex()){

                    if(i == adapterPosition) {

                        causeView.causeWrapper.setBackgroundResource(
                            R.drawable.btn_cause_focused
                        )

                        causeView.cause.setTextColor(ContextCompat.getColor(
                            context,
                            R.color.white
                        ))

                    }else{

                        causeView.causeWrapper.setBackgroundResource(
                            R.drawable.btn_cause_unfocused
                        )

                        causeView.cause.setTextColor(ContextCompat.getColor(
                            context,
                            R.color.warm_grey_three
                        ))

                    }

                }

                ConsultingActivity.burnStyleV = (adapterPosition + 1).toString().padStart(3, '0')
                whenLoad(adapterPosition)

            }

            whenLoad(0)

        }

    }

}