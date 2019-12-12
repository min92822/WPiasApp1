package fineinsight.app.service.wpias

import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import fineinsight.app.service.wpias.adapters.ExpandableListAdapter
import kotlinx.android.synthetic.main.title_bar_skyblue.*


class MyQuestionDetailActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_question_detail)

        SetTransparentBar()

        myDetailSetting()

    }

    fun myDetailSetting(){

        txt_title.text = "상담내역 보기"
        btn_back.setOnClickListener {
            onBackPressed()
        }

        // ExpandableListView Setting
        var expandableListView = findViewById<ExpandableListView>(R.id.listView)
        if(expandableListView != null){
            val listData = expandableListSetting()
            var titleList = ArrayList(listData.keys)
            var adapter = ExpandableListAdapter(this, titleList, listData)

            expandableListView.setAdapter(adapter)

            setListViewHeight(expandableListView, 4)

            expandableListView.setOnGroupClickListener(object : ExpandableListView.OnGroupClickListener{
                override fun onGroupClick(parent: ExpandableListView?, v: View?, groupPosition: Int, id: Long): Boolean {
                    setListViewHeight(parent!!, groupPosition)
                    return false
                }
            })

        }

        // 상처 경과기록


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


}
