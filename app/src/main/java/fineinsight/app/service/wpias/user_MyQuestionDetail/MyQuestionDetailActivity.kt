package fineinsight.app.service.wpias.user_MyQuestionDetail

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import fineinsight.app.service.wpias.R
import fineinsight.app.service.wpias.RootActivity
import fineinsight.app.service.wpias.dataClass.MycaseInfo
import fineinsight.app.service.wpias.dataClass.QuestionInfo
import fineinsight.app.service.wpias.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_my_question_detail.*
import kotlinx.android.synthetic.main.in_my_question_detail_add_record.*
import kotlinx.android.synthetic.main.shot_distance_popup.*
import kotlinx.android.synthetic.main.title_bar_skyblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MyQuestionDetailActivity : RootActivity() {

    var m_questionInfo: QuestionInfo? = null
    var m_caseInfo: MycaseInfo? = null
    var expandableListView : ExpandableListView? = null

    //촬영 모드 구분용도
    var cameraMode = ""

    //on Activity Result Request Code 상수
    val REQUEST_TAKE_PHOTO_10 = 1
    val REQUEST_TAKE_PHOTO_20 = 2
    val GET_IMAGE_FROM_GALLERY_10 = 3
    val GET_IMAGE_FROM_GALLERY_20 = 4

    //사진 원본파일 이미지 저장용 path
    var currentPhotoPath = ""

    //AzureAsyncTask에 들어가는 image uri 및 input stream 배열
    lateinit var imageUri : Uri
    lateinit var imageUri2 : Uri
    var inputStreamArr = ArrayList<InputStream>()
    var imageLengthArr = ArrayList<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_question_detail)

        m_questionInfo = intent.getSerializableExtra("myQuestion") as QuestionInfo

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        SetTransparentBar()

        myDetailSetting()

    }

    override fun onBackPressed() {
        if(wrap_add_record.visibility == View.VISIBLE){
            wrap_add_record.visibility = View.GONE
            wrap_my_question_info.visibility = View.VISIBLE

        } else {
            super.onBackPressed()
        }
    }

    fun myDetailSetting(){

        txt_title.text = "상담내역 보기"
        btn_back.setOnClickListener {
            super.onBackPressed()
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

            var adapter =
                ExpandableListAdapter(
                    this,
                    titleList,
                    listData
                )

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
                "열탕" -> {
                    R.drawable.yultang_1
                }
                "화염" -> {
                    R.drawable.hwayum_1
                }
                "전기" -> {
                    R.drawable.jungi_1
                }
                "접촉" -> {
                    R.drawable.jubchok_1
                }
                "저온" -> {
                    R.drawable.juon_1
                }
                "화학" -> {
                    R.drawable.hwahag_1
                }
                "증기" -> {
                    R.drawable.junggi_1
                }
                "마찰" -> {
                    R.drawable.machar_1
                }
                "햇빛" -> {
                    R.drawable.hatbit_1
                }
                else -> {
                    R.drawable.heubib
                }
            }
        )

        img_my_detail_bui.setImageResource(
            when(m_questionInfo!!.bodystyle){
                "001" -> {
                    R.drawable.s_mori_f
                }
                "002" -> {
                    R.drawable.s_akke_f
                }
                "003" -> {
                    R.drawable.s_gasum_f
                }
                "004" -> {
                    R.drawable.s_gasum_b
                }
                "005" -> {
                    R.drawable.s_bae_f
                }
                "006" -> {
                    R.drawable.s_bae_b
                }
                "007" -> {
                    R.drawable.s_8_f
                }
                "008" -> {
                    R.drawable.s_sun_f
                }
                "009" -> {
                    R.drawable.s_umbu
                }
                "010" -> {
                    R.drawable.s_bae_b
                }
                "011" -> {
                    R.drawable.s_dari_f
                }
                "012" -> {
                    R.drawable.s_bar_f
                }
                else -> {
                    R.drawable.s_gasum_f
                }
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


        listDetail[index] = daecher

        listDetail[++index] = teukjing

        if(m_questionInfo!!.detailnm != "음부" || m_questionInfo!!.detailnm != "엉덩이" || m_questionInfo!!.detailnm != "호흡기"){
            listDetail[++index] = bui
        }

        listDetail[++index] = gwanri

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

        // 경과추가하기 버튼
        btn_my_question_detail_add.setOnClickListener {
            // expandableListView 모두 닫기
            for (i in 0..expandableListView!!.adapter.count){
                expandableListView!!.collapseGroup(i)
            }
            setListViewHeight(expandableListView!!, 4)

            wrap_add_record.visibility = View.VISIBLE
            wrap_my_question_info.visibility = View.GONE

        }

        // 경과추가하기 취소버튼
        btn_add_record_cancle.setOnClickListener {
            wrap_add_record.visibility = View.GONE
            wrap_my_question_info.visibility = View.VISIBLE
        }


        if(m_caseInfo!!.casestatus == "A"){
            chk_dr.text = "${m_caseInfo!!.answerdocnm} 의사 선생님에게 추가 답변요청을 합니다."
            chk_dr.visibility = View.VISIBLE
            chk_dr.isChecked = true
            wrap_direction.visibility = View.VISIBLE

        } else {
            chk_dr.visibility = View.GONE
            chk_add.isChecked = true
            wrap_direction.visibility = View.GONE
        }

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if(chk_dr.isChecked){
                wrap_direction.visibility = View.VISIBLE
            } else {
                wrap_direction.visibility = View.GONE
            }

        }

        photoGraphingAlert()

    }

    //부위 촬영 클릭시 팝업 이벤트
    fun photoGraphingAlert(){
        btn_photo_close.setOnClickListener { setupPermissions() }


        btn_photo_over.setOnClickListener { longDistancePopup() }

    }

    //부위 촬영 팝업(10cm)
    fun shortDistancePopup(){

        cameraMode = "short"

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.shot_distance_popup)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.bg_alert2)

        dialog.takeShotWrapper.setOnClickListener {

            dispatchTakePictureIntent()

        }

        dialog.chooseFromGalleryWrapper.setOnClickListener {

            fromAlbum()

        }

        dialog.cancelWrapper.setOnClickListener {

            dialog.dismiss()

        }

        dialog.show()

    }

    //부위 촬영 팝업(20cm)
    @SuppressLint("SetTextI18n")
    fun longDistancePopup(){

        cameraMode = "long"

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.shot_distance_popup)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.bg_alert2)

        dialog.img_mycase.setImageResource(R.drawable.pic_20_cm)
        dialog.textView31.text = "2단계 상세 촬영"
        dialog.textView32.text = "상처 부위로부터 약 20cm 가량 위에서 촬영해주세요."

        dialog.takeShotWrapper.setOnClickListener {

            dispatchTakePictureIntent()

        }

        dialog.chooseFromGalleryWrapper.setOnClickListener {

            fromAlbum()

        }

        dialog.cancelWrapper.setOnClickListener {

            dialog.dismiss()

        }

        dialog.show()

    }

    //핸드폰에 내장된 카메라 관련 어플들을 불러오는 펑션
    //촬영 모드 구분
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "fineinsight.app.service.wpias.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    if(cameraMode == "short") {
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_10)
                    }else{
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_20)
                    }
                }
            }
        }
    }

    //카메라 어플로 사진 촬영 직후 바로 파일로 만듬
    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    //앨범에서 이미지 가져오는 펑션
    fun fromAlbum(){

        if(cameraMode == "short") {
            startActivityForResult(
                Intent(Intent.ACTION_PICK).setType(MediaStore.Images.Media.CONTENT_TYPE),
                GET_IMAGE_FROM_GALLERY_10
            )
        }else{
            startActivityForResult(
                Intent(Intent.ACTION_PICK).setType(MediaStore.Images.Media.CONTENT_TYPE),
                GET_IMAGE_FROM_GALLERY_20
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        //사진 촬영으로 이미지 가져옴
        if(resultCode == Activity.RESULT_OK){

            var bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            bitmapToFile(bitmap)
            var file = File(currentPhotoPath)

            when(requestCode) {

                REQUEST_TAKE_PHOTO_10 -> {
                    imageUri = Uri.fromFile(file)
                    if(imageUri.toString().isNotEmpty()){

                        // 이미지 uri 처리
                        Glide.with(this)
                            .load(imageUri)
                            .into(btn_photo_close)


                    }
                }
                REQUEST_TAKE_PHOTO_20 -> {
                    imageUri2 = Uri.fromFile(file)
                    if(imageUri2.toString().isNotEmpty()){
//                        Validation.vali.imageUrl2V = imageUri2.toString()
                    }
                }

            }

        }

        //앨범에서 이미지 가져옴
        if(resultCode == Activity.RESULT_OK){

            when(requestCode) {

                GET_IMAGE_FROM_GALLERY_10 -> {
                    imageUri = data?.data!!
                    if(imageUri.toString().isNotEmpty()){

                        Glide.with(this)
                            .load(imageUri)
                            .into(btn_photo_close)


                    }
                }
                GET_IMAGE_FROM_GALLERY_20 -> {
                    imageUri2 = data?.data!!
                    if(imageUri2.toString().isNotEmpty()){
//                        Validation.vali.imageUrl2V = imageUri2.toString()
                    }
                }

            }

        }

    }


    //사진 세로로 세우는 펑션
    fun imageRotate(bitmap : Bitmap) : Bitmap?{

        var matrix = Matrix()

        matrix.postRotate(90f)

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    }

    //비트맵을 다시 파일로 바꾸는 펑션
    fun bitmapToFile(bitmap: Bitmap){

        var file = File(currentPhotoPath)
        var out = FileOutputStream(file)

        try{

            file.createNewFile()

            imageRotate(bitmap)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, out)

        }catch (e : Exception){
            e.printStackTrace()
        }finally {
            try {
                out.close()
            }catch (e : IOException){
                e.printStackTrace()
            }
        }

    }


    // 상처 경과기록
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
                    arr.sortBy { mycaseInfo ->
                        mycaseInfo.casedate
                    }

                    m_caseInfo = arr[0]
                    addRecord()

                    var qArr = ArrayList<QuestionInfo>()
                    qArr.add(m_questionInfo!!)

                    var linearLayoutManager = LinearLayoutManager(this@MyQuestionDetailActivity)
                    linearLayoutManager.reverseLayout = true
                    linearLayoutManager.stackFromEnd = true
                    recycler_my_question_detail.layoutManager = linearLayoutManager
                    recycler_my_question_detail.adapter =
                        MyQuestionRecordAdapter(
                            this@MyQuestionDetailActivity,
                            arr,
                            qArr
                        )

                } else {

                    Toast.makeText(this@MyQuestionDetailActivity, "상세 상담 불러오기 실패", Toast.LENGTH_SHORT).show()

                }

            }
        })
    }

    // 퍼미션 체크
    fun setupPermissions() {

        val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(this, permissions, 0)

        } else {

            shortDistancePopup()

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


    }

}
