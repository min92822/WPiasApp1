package fineinsight.app.service.wpias

import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import fineinsight.app.service.wpias.adapters.ExpandableListAdapter
import fineinsight.app.service.wpias.adapters.MyQuestionRecordAdapter
import fineinsight.app.service.wpias.dataClass.MycaseInfo
import fineinsight.app.service.wpias.dataClass.QuestionInfo
import fineinsight.app.service.wpias.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_my_question_detail.*
import kotlinx.android.synthetic.main.title_bar_skyblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyQuestionDetailActivity : RootActivity() {

    var m_questionInfo: QuestionInfo? = null
    var expandableListView : ExpandableListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_question_detail)

        m_questionInfo = intent.getSerializableExtra("myQuestion") as QuestionInfo

        SetTransparentBar()

        myDetailSetting()

        addRecord()
    }

    fun myDetailSetting(){

        txt_title.text = "상담내역 보기"
        btn_back.setOnClickListener {
            onBackPressed()
        }

        txt_my_question_detail_title.text = m_questionInfo!!.title
        txt_my_question_detail_date.text = "화상입은 날짜 : ${m_questionInfo!!.burndate}"

        txt_my_question_detail_age.text = m_questionInfo!!.age
        txt_my_question_detail_burn.text = "${m_questionInfo!!.burnnm}화상"
        txt_my_question_detail_body.text = m_questionInfo!!.detailnm


        // ExpandableListView Setting
        expandableListView = findViewById(R.id.listView)
        if(expandableListView != null){
            val listData = expandableListSetting()
            var titleList = ArrayList(listData.keys)
            var adapter = ExpandableListAdapter(this, titleList, listData)

            expandableListView!!.setAdapter(adapter)

            setListViewHeight(expandableListView!!, 4)

            expandableListView!!.setOnGroupClickListener(object : ExpandableListView.OnGroupClickListener{
                override fun onGroupClick(parent: ExpandableListView?, v: View?, groupPosition: Int, id: Long): Boolean {
                    setListViewHeight(parent!!, groupPosition)
                    return false
                }
            })
        }

        // 상처 경과기록
        detailQuestion()

    }

    // ExpandableList Setting
    fun expandableListSetting() : HashMap<String, List<String>> {

        var listDetail = HashMap<String, List<String>>()

        var daecher = ArrayList<String>()
        daecher.add("무슨화상의 대처방법")

        var teukjing = ArrayList<String>()
        teukjing.add("무슨화상의 특징")

        var bui = ArrayList<String>()
        bui.add("부위의 특징")

        var gwanri = ArrayList<String>()
        gwanri.add("화상상처 물집 관리방법")

        listDetail.put("무슨화상의 대처방법", daecher)
        listDetail.put("무슨화상의 특징", teukjing)
        listDetail.put("부위의 특징", bui)
        listDetail.put("화상상처 물집 관리방법", gwanri)

        return listDetail
    }

    // ExpandableListView height based on children
    fun setListViewHeight(listView: ExpandableListView, group: Int) {

        val listAdapter = listView.expandableListAdapter as android.widget.ExpandableListAdapter
        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(
            listView.width,
            View.MeasureSpec.EXACTLY
        )
        for (i in 0 until listAdapter.groupCount) {
            val groupItem = listAdapter.getGroupView(i, false, null, listView)
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += groupItem.measuredHeight
            if (listView.isGroupExpanded(i) && i != group
                || !listView.isGroupExpanded(i) && i == group
            ) {
                for (j in 0 until listAdapter.getChildrenCount(i)) {
                    val listItem = listAdapter.getChildView(
                        i, j, false, null,
                        listView
                    )
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
                    totalHeight += listItem.measuredHeight
                }
            }
        }
        val params = listView.layoutParams
        var height = (totalHeight
                + listView.dividerHeight * (listAdapter.groupCount - 1))
        if (height < 10) height = 200
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }

    // 경과 추가하기
    fun addRecord(){

        btn_my_question_detail_add.setOnClickListener {

            // expandableListView 모두 닫기
            for (i in 0..expandableListView!!.adapter.count){
                expandableListView!!.collapseGroup(i)
            }
            setListViewHeight(expandableListView!!, 4)

            wrap_add_record.visibility = View.VISIBLE
            wrap_my_question_info.visibility = View.GONE

        }

    }


    // 경과기록
    fun detailQuestion(){

        var map = HashMap<String, String>()
        map["CKEY"] = m_questionInfo!!.qkey

        ApiUtill().getSELECT_MYCASE().select_mycase(map).enqueue(object : Callback<ArrayList<MycaseInfo>>{
            override fun onFailure(call: Call<ArrayList<MycaseInfo>>, t: Throwable) {
                Toast.makeText(this@MyQuestionDetailActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ArrayList<MycaseInfo>>, response: Response<ArrayList<MycaseInfo>>) {

                if(response.isSuccessful){

                    var arr = response.body() as ArrayList<MycaseInfo>


                    var linearLayoutManager = LinearLayoutManager(this@MyQuestionDetailActivity)
                    linearLayoutManager.reverseLayout = true
                    linearLayoutManager.stackFromEnd = true
                    recycler_my_question_detail.layoutManager = linearLayoutManager
                    recycler_my_question_detail.adapter = MyQuestionRecordAdapter(this@MyQuestionDetailActivity, arr)

                } else {

                    Toast.makeText(this@MyQuestionDetailActivity, "상세 상담 불러오기 실패", Toast.LENGTH_SHORT).show()

                }

            }
        })
    }


}
