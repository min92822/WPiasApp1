package fineinsight.app.service.wpias

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ExpandableListView
import android.widget.ScrollView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import fineinsight.app.service.wpias.adapters.ExpandableListAdapter
import fineinsight.app.service.wpias.adapters.MyQuestionRecordAdapter
import fineinsight.app.service.wpias.dataClass.MycaseInfo
import fineinsight.app.service.wpias.dataClass.QuestionInfo
import fineinsight.app.service.wpias.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_my_question_detail.*
import kotlinx.android.synthetic.main.in_my_question_detail_add_record.*
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

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

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

        imgSetting()

        // ExpandableListView Setting
        expandableListView = findViewById(R.id.listView)
        if(expandableListView != null){
            val listData = expandableListSetting()
            var titleList = ArrayList<String>()

            for(item in listData){
                titleList.add(listData[item.key]!!.keys.elementAt(0))
            }

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

    fun imgSetting(){

        img_my_detail_gender.setImageResource(
            if (m_questionInfo!!.gender == "M"){
                R.drawable.s_male
            } else {
                R.drawable.s_female
            }
        )

        img_my_detail_burn.setImageResource(
            when(m_questionInfo!!.burnnm){
                "열탕" -> {R.drawable.yultang_1}
                "화염" -> {R.drawable.hwayum_1}
                "전기" -> {R.drawable.jungi_1}
                "접촉" -> {R.drawable.jubchok_1}
                "저온" -> {R.drawable.juon_1}
                "화학" -> {R.drawable.hwahag_1}
                "증기" -> {R.drawable.junggi_1}
                "마찰" -> {R.drawable.machar_1}
                "햇빛" -> {R.drawable.hatbit_1}
                else -> {R.drawable.heubib}
            }
        )

        img_my_detail_bui.setImageResource(
            when(m_questionInfo!!.detailnm){
                "머리" -> {R.drawable.s_mori_f}
                "어깨" -> {R.drawable.s_akke_f}
                "가슴" -> {R.drawable.s_gasum_f}
                "등" -> {R.drawable.s_gasum_b}
                "배" -> {R.drawable.s_bae_f}
                "허리" -> {R.drawable.s_bae_b}
                "팔" -> {R.drawable.s_8_f}
                "손" -> {R.drawable.s_sun_f}
                "음부" -> {R.drawable.s_umbu}
                "엉덩이" -> {R.drawable.s_bae_b}
                "다리" -> {R.drawable.s_dari_f}
                "발" -> {R.drawable.s_bar_f}
                else -> {R.drawable.s_gasum_f}
            }
        )

    }


    // ExpandableList Setting
    fun expandableListSetting() : HashMap<Int, HashMap<String, String>> {

        var listDetail = HashMap<Int, HashMap<String, String>>()
        var index = 0

        var daecher = HashMap<String, String>()
        daecher["${m_questionInfo!!.burnnm}화상의 대처방법"] = newlineText(m_questionInfo!!.burnsolution)

        var teukjing = HashMap<String, String>()
        teukjing["${m_questionInfo!!.burnnm}화상의 특징"] = newlineText(m_questionInfo!!.burnpoint)

        var bui = HashMap<String, String>()
        bui["${m_questionInfo!!.detailnm} 부위의 특징"] = newlineText(m_questionInfo!!.bodysolution)

        var gwanri = HashMap<String, String>()
        gwanri["화상상처 물집 관리방법"] = "· 화상사고로 생겨난 화상물집은 터뜨려지지 않도록" +
                " 깨끗한 수건이나 거즈를 통해 보호해 주는 것이 좋습니다.\n\n" +
                "· 물집이 터진 경우 물을 포함한 어떠한 물질도 유입되지 않도록 깨끗한 거즈로 상처를 보호," +
                " 차단하고 빠르게 화상전문치료병원을 방문하여 감염여부 진단과 치료계획을 세워 후유증," +
                " 감염을 막기 위한 치료를 하여야 합니다."

        listDetail.put(index, daecher)
        listDetail.put(++index, teukjing)
        if(m_questionInfo!!.detailnm != "음부" || m_questionInfo!!.detailnm != "엉덩이" || m_questionInfo!!.detailnm != "호흡기"){
            listDetail.put(++index, bui)
        }
        listDetail.put(++index, gwanri)

        return listDetail
    }

    // 언더바 new line 으로 변경
    fun newlineText(str:String):String {
        return str.replace("_", "\n\n")
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

        btn_add_record_cancle.setOnClickListener {
            wrap_add_record.visibility = View.GONE
            wrap_my_question_info.visibility = View.VISIBLE
        }

//        txt_record_content_add.setOnTouchListener { view, motionEvent ->
//            my_question_detail_scrollview.smoothScrollTo(0, txt_record_content_add.top)
//            my_question_detail_scrollview.post { txt_record_content_add.requestFocus()
//                showKeyboard()}
//
//        }


    }

    fun showKeyboard(){
        var inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
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

                    var qArr = ArrayList<QuestionInfo>()
                    qArr.add(m_questionInfo!!)

                    var linearLayoutManager = LinearLayoutManager(this@MyQuestionDetailActivity)
                    linearLayoutManager.reverseLayout = true
                    linearLayoutManager.stackFromEnd = true
                    recycler_my_question_detail.layoutManager = linearLayoutManager
                    recycler_my_question_detail.adapter = MyQuestionRecordAdapter(this@MyQuestionDetailActivity, arr, qArr)

                } else {

                    Toast.makeText(this@MyQuestionDetailActivity, "상세 상담 불러오기 실패", Toast.LENGTH_SHORT).show()

                }

            }
        })
    }


}
