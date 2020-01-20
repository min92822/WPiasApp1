package com.phonegap.WPIAS.user_MyQuestionDetail

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ExpandableListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.iterator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.dataClass.MycaseInfo
import com.phonegap.WPIAS.dataClass.QuestionInfo
import com.phonegap.WPIAS.publicObject.Validation
import com.phonegap.WPIAS.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_my_question_detail.*
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.in_my_question_detail_add_record.*
import kotlinx.android.synthetic.main.shot_distance_popup.*
import kotlinx.android.synthetic.main.title_bar_skyblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MyQuestionDetailActivity : RootActivity() {

    var m_questionInfo: QuestionInfo? = null
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
    var imageUri : Uri? = null
    var imageUri2 : Uri? = null
    var inputStreamArr = ArrayList<InputStream>()
    var imageLengthArr = ArrayList<Int>()

    //storage 문자열
    val storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=storagewpias;AccountKey=G+ZyYwRLvxTFebMpLqsSeNI/V+1ALImJqs1MAG1rD315BN1TRO7Q8CpcKv0KOmRB9hasKF4pJqZkTEJ3TEAlPw=="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_question_detail)

        m_questionInfo = intent.getSerializableExtra("myQuestion") as QuestionInfo

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        SetTransparentBar()

        myDetailSetting()

        setDescendentViews(window.decorView.rootView)

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

    // 경과 추가하기 setting
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


        // 추가 질문여부
        if(m_questionInfo!!.prostatus == "A"){
            chk_dr.text = "${m_questionInfo!!.answerdocnm} 의사 선생님에게 추가 답변요청을 합니다."
            chk_dr.visibility = View.VISIBLE
            chk_dr.isChecked = true

        } else {
            chk_dr.visibility = View.GONE
            chk_add.isChecked = true
        }

        // 사진 촬영
        photoGraphingAlert()


        // 경과 추가하기
        btn_add_record_add.setOnClickListener {


            if(imageUri == null || imageUri2 == null){

                failAlert("이미지를 등록해주세요.")

            } else {

                if(txt_record_content_add.text.isNullOrEmpty()){

                    failAlert("내용을 입력해주세요.")

                } else {

                    inputStreamArr.add(contentResolver.openInputStream(imageUri!!)!!)
                    inputStreamArr.add(contentResolver.openInputStream(imageUri2!!)!!)

                    for (inputStream in inputStreamArr) {
                        imageLengthArr.add(inputStream.available())
                    }

                    // 경과추가 - 답변요청
                    if (chk_dr.isChecked) {

                        var map = HashMap<String, String>()
                        map["QKEY"] = m_questionInfo!!.qkey
                        map["CASEDATE"] = SimpleDateFormat("yyyyMMddkkmmss").format(Calendar.getInstance().time)
                        map["CONTENTS"] = txt_record_content_add.text.toString()
                        map["DIRECTION"] = direction()


                        AddCaseRequest_AzureAsyncTask(
                            m_questionInfo!!.answerdoc,
                            this,
                            inputStreamArr,
                            imageLengthArr,
                            map
                        ).execute(storageConnectionString)

                        println(map)

                    } else {
                        // 경과추가 - 답변미요청
                        var map = HashMap<String, String>()
                        map["QKEY"] = m_questionInfo!!.qkey
                        map["CASEDATE"] = SimpleDateFormat("yyyyMMddkkmmss").format(Calendar.getInstance().time)
                        map["CONTENTS"] = txt_record_content_add.text.toString()

                        println(map)

                        AddCase_AzureAsyncTask(
                            this,
                            inputStreamArr,
                            imageLengthArr,
                            map
                        ).execute(storageConnectionString)

                    }
                }
            }

        }

    }


    // 궁금한점 체크 -> String (ex. 1-2-4-7)
    fun direction():String{
        var direction = ""
        var directionArr = arrayOf(chk_direction_1, chk_direction_2, chk_direction_3, chk_direction_4, chk_direction_5, chk_direction_6, chk_direction_7)


        for (i in 0..directionArr.size-1){
            if(directionArr[i].isChecked){
                direction += "${i+1}-"
            }
        }

        if (direction.length == 0){
            return direction
        } else {
            return direction.substring(0, direction.length-1)
        }



    }


    //부위 촬영 클릭시 팝업 이벤트
    fun photoGraphingAlert(){
        btn_photo_close.setOnClickListener { setupPermissions("short") }


        btn_photo_over.setOnClickListener { setupPermissions("long") }

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
            dialog.dismiss()
        }

        dialog.chooseFromGalleryWrapper.setOnClickListener {

            fromAlbum()
            dialog.dismiss()
        }

        dialog.cancelWrapper.setOnClickListener {

            dialog.dismiss()

        }

        dialog.show()

    }

    //부위 촬영 팝업(20cm)
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
            dialog.dismiss()

        }

        dialog.chooseFromGalleryWrapper.setOnClickListener {

            fromAlbum()
            dialog.dismiss()

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
                        "com.phonegap.WPIAS.fileprovider",
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


    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        var bitmap : Bitmap? = null

        //사진 촬영으로 이미지 가져옴
        if(resultCode == Activity.RESULT_OK){

            when(requestCode) {

                REQUEST_TAKE_PHOTO_10 -> {
                    bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    var file = File(currentPhotoPath)
                    imageUri = Uri.fromFile(file)
                    if(imageUri.toString().isNotEmpty()){
                        Validation.vali.imageUrl1V = imageUri.toString()
                    }

                    imageUri = getImageUriFromBitmap(this, imageRotate(bitmap)!!)
                    btn_photo_close.setImageBitmap(imageRotate(bitmap))

                }
                REQUEST_TAKE_PHOTO_20 -> {
                    bitmap = BitmapFactory.decodeFile(currentPhotoPath)
                    var file = File(currentPhotoPath)
                    imageUri2 = Uri.fromFile(file)
                    if(imageUri2.toString().isNotEmpty()){
                        Validation.vali.imageUrl2V = imageUri2.toString()
                    }

                    imageUri2 = getImageUriFromBitmap(this, imageRotate(bitmap)!!)
                    btn_photo_over.setImageBitmap(imageRotate(bitmap))

                }

            }

        }

        //앨범에서 이미지 가져옴
        if(resultCode == Activity.RESULT_OK){

            when(requestCode) {

                GET_IMAGE_FROM_GALLERY_10 -> {

                    imageUri = data?.data!!
                    if(imageUri.toString().isNotEmpty()){
//                        Validation.vali.imageUrl1V = imageUri.toString()
                    }

                    bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imageUri!!))
                    }else{
                        MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    }

                    btn_photo_close.setImageBitmap(imageRotate(bitmap!!))

                }
                GET_IMAGE_FROM_GALLERY_20 -> {
                    imageUri2 = data?.data!!

                    if(imageUri2.toString().isNotEmpty()){
//                        Validation.vali.imageUrl2V = imageUri2.toString()
                    }

                    bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imageUri2!!))
                    }else{
                        MediaStore.Images.Media.getBitmap(contentResolver, imageUri2)
                    }

                    btn_photo_over.setImageBitmap(imageRotate(bitmap!!))

                }

            }

        }

    }


    //사진 세로로 세우는 펑션
    fun imageRotate(bitmap : Bitmap) : Bitmap?{

        var matrix = Matrix()

        if(bitmap.width >= bitmap.height){

            matrix.postRotate(90f)

        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    }


    // 상처 경과기록
    fun detailQuestion(){

        ProgressAction(true)

        var map = HashMap<String, String>()
        map["CKEY"] = m_questionInfo!!.qkey

        ApiUtill()
            .getSELECT_MYCASE().select_mycase(map).enqueue(object : Callback<ArrayList<MycaseInfo>>{
            override fun onFailure(call: Call<ArrayList<MycaseInfo>>, t: Throwable) {
                Toast.makeText(this@MyQuestionDetailActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ArrayList<MycaseInfo>>, response: Response<ArrayList<MycaseInfo>>) {

                if(response.isSuccessful){

                    ProgressAction(false)

                    var arr = response.body() as ArrayList<MycaseInfo>
                    arr.sortBy { mycaseInfo ->
                        mycaseInfo.cnumber
                    }

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
                            m_questionInfo!!
                        )

                    wrap_my_question_record.visibility = View.VISIBLE

                } else {

                    ProgressAction(false)
                    Toast.makeText(this@MyQuestionDetailActivity, "상세 상담 불러오기 실패", Toast.LENGTH_SHORT).show()

                }

            }
        })
    }

    // 퍼미션 체크
    fun setupPermissions(str: String) {

        val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(this, permissions, 0)

        } else {

            if(str == "short"){

                shortDistancePopup()

            } else if (str == "long"){

                longDistancePopup()

            }

        }

    }

    //업로드 실패 알럿
    fun failAlert(str: String){

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_end)

        title.text = "실패"
        sub.text = str

        btn_left.visibility = View.GONE

        btn_right.text = "OK"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        btn_right.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }


    fun ProgressAction(isShow:Boolean)
    {
        if(isShow)
        {

            Progress_circle.visibility = View.VISIBLE
            Progress_bg.visibility = View.VISIBLE
            this.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
        else
        {
            Progress_circle.visibility = View.GONE
            Progress_bg.visibility = View.GONE
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }


    //에딧 텍스트 아닌 부분 클릭시 키보드 사라지는 펑션
    fun hideKeyboard(){

        var imm = (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)

        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

        currentFocus?.clearFocus()

    }

    //최상위 뷰 태그 및 하위 뷰 태그에 hideKeboard를 적용하는 펑션
    fun setDescendentViews(view : View){

        if(view !is EditText) {
            view.setOnTouchListener { v, event ->

                hideKeyboard()

                return@setOnTouchListener false

            }
        }

        if(view is RecyclerView){

            view.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener{
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                    hideKeyboard()
                }

                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    hideKeyboard()
                    return false
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                }
            })

        }

        if(view is ViewGroup){
            for(innerview in view) {
                setDescendentViews(innerview)
            }
        }

    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    override fun onResume() {
        super.onResume()
        detailQuestion()
    }
}
