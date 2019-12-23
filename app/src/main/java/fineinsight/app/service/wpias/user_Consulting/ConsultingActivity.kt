package fineinsight.app.service.wpias.user_Consulting

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fineinsight.app.service.wpias.R
import fineinsight.app.service.wpias.RootActivity
import fineinsight.app.service.wpias.user_Consulting.adapters.CauseOfBurnedAdapter
import fineinsight.app.service.wpias.user_Consulting.adapters.DeptAdapter
import fineinsight.app.service.wpias.user_Consulting.adapters.PartListAdapter
import fineinsight.app.service.wpias.user_Consulting.adapters.QuestionAdapter
import fineinsight.app.service.wpias.publicObject.PubVariable
import kotlinx.android.synthetic.main.activity_consulting.*
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.shot_distance_popup.*
import kotlinx.android.synthetic.main.title_bar_darkblue.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class ConsultingActivity : RootActivity(){

    //on Activity Result Request Code 상수
    val REQUEST_TAKE_PHOTO_10 = 1
    val REQUEST_TAKE_PHOTO_20 = 2
    val GET_IMAGE_FROM_GALLERY_10 = 3
    val GET_IMAGE_FROM_GALLERY_20 = 4

    //storage 문자열
    val storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=storagewpias;AccountKey=G+ZyYwRLvxTFebMpLqsSeNI/V+1ALImJqs1MAG1rD315BN1TRO7Q8CpcKv0KOmRB9hasKF4pJqZkTEJ3TEAlPw=="

    //AzureAsyncTask에 들어가는 image uri 및 input stream 배열
    lateinit var imageUri : Uri
    lateinit var imageUri2 : Uri
    var inputStreamArr = ArrayList<InputStream>()
    var imageLengthArr = ArrayList<Int>()

    //사진 원본파일 이미지 저장용 path
    var currentPhotoPath = ""

    //촬영 모드 구분용도
    var cameraMode = ""

    var MYyear = 0
    var MYmonth = 0
    var MYday = 0

    var popup = false

    //validation 용도
    //일부 validation이 adapter에 checkBox들의 값에 의하기 때문에
    companion object {

        var consultingTitleV = ""
        var burnDateV = ""
        var ageV = ""
        var genderV = ""
        var bodyStyleV = ""
        var bodyDetailV = ""
        var bodyGitaV = ""
        var burnStyleV = ""
        var burnDetailV = ""
        var burnGitaV = ""
        var careStyleV = ""
        var careGitaV = ""
        var scarStyleV = ""
        var proStatusV = "Q"
        var directionV = ArrayList<String>()
        var imageUrl1V = ""
        var imageUrl2V = ""
        var contentsV = ""

    }

    var bodyPartFront = arrayListOf("머리", "어깨", "가슴", "배", "팔", "손", "음부", "다리", "발", "호흡기")
    var bodyPartBack = arrayListOf("머리", "어깨", "등", "허리", "팔", "손", "엉덩이", "다리", "발")
    var causeCategory = arrayListOf("열탕", "화염", "전기", "접촉", "저온", "화학", "증기", "마찰", "햇빛", "흡입")

    init {

        consultingTitleV = ""
        burnDateV = ""
        ageV = ""
        genderV = ""
        bodyStyleV = ""
        bodyDetailV = ""
        bodyGitaV = ""
        burnStyleV = ""
        burnDetailV = ""
        burnGitaV = ""
        scarStyleV = ""
        proStatusV = "Q"
        directionV = ArrayList<String>()
        imageUrl1V = ""
        imageUrl2V = ""
        contentsV = ""

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulting)

        setupPermissions()

        SetTransparentBar()

        setTitle()

        popupCalendar()

        burnedHistory()

        bodyPartCheck()

        genderTouch()

        identifySetSpinnerAndGender()

        ageSpinnerPopup()

        causeRecyclerViewActivated()

        identifyCheckBox()

        deptRecyclerViewActivated()

        questionRecyclerViewActivated()

        photoGraphingAlert()

        submitBurnConsulting()

        eventInit()

    }

    //신체 부위 리싸이클러뷰 활성화 및 부위 버튼 활성화
    //validation을 위한 이벤트 이 펑션에 등록
    @SuppressLint("SimpleDateFormat")
    fun eventInit(){

        bodyFront.isChecked = true

        male.isChecked = true

        whenBurned.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                burnDateV = s.toString()

            }

        })

        consultingTitle.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                 consultingTitleV = s.toString()

            }

        })

        consultingContents.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                contentsV = s.toString()

            }

        })

    }

    //앞 뒤 구분 버튼 체크 이벤트
    //버튼 및 버튼 글자 색 변환이 이벤트에 걸려있음
    fun bodyPartCheck(){

        bodyFront.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this,
                    R.color.white
                ))
                bodyBack.setTextColor(ContextCompat.getColor(this,
                    R.color.ocean_blue
                ))
                frontActivated()
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this,
                    R.color.ocean_blue
                ))
                bodyBack.setTextColor(ContextCompat.getColor(this,
                    R.color.white
                ))
            }

        }

        bodyBack.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this,
                    R.color.white
                ))
                bodyFront.setTextColor(ContextCompat.getColor(this,
                    R.color.ocean_blue
                ))
                backActivated()
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this,
                    R.color.ocean_blue
                ))
                bodyFront.setTextColor(ContextCompat.getColor(this,
                    R.color.white
                ))
            }

        }

    }

    //앞 버튼 터치시 실행되는 리싸이클러 뷰
    fun frontActivated(){

        var partListAdapter =
            PartListAdapter(bodyPartFront)

        bodyPartRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        bodyPartRecyclerView.adapter = partListAdapter

    }

    //뒤 버튼 터치시 실행되는 리싸이클러 뷰
    fun backActivated(){

        var partListAdapter =
            PartListAdapter(bodyPartBack)

        bodyPartRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        bodyPartRecyclerView.adapter = partListAdapter

    }

    //성별 버튼 터치 펑션
    fun genderTouch(){

        male.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this,
                    R.color.white
                ))
                female.setTextColor(ContextCompat.getColor(this,
                    R.color.ocean_blue
                ))
                genderV = "M"
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this,
                    R.color.ocean_blue
                ))
                female.setTextColor(ContextCompat.getColor(this,
                    R.color.white
                ))
            }

        }

        female.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this,
                    R.color.white
                ))
                male.setTextColor(ContextCompat.getColor(this,
                    R.color.ocean_blue
                ))
                genderV = "F"
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this,
                    R.color.ocean_blue
                ))
                male.setTextColor(ContextCompat.getColor(this,
                    R.color.white
                ))
            }

        }

    }

    //연령 spinner 펑션
    fun ageSpinnerPopup(){

        var arrayAdapter : ArrayAdapter<String>? = null

        var generation = arrayListOf("", "0~3세", "4~6세", "7~15세", "16~20세", "21~30세", "31~40세", "41~50세", "51~60세", "61세이상")

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, generation)

        age.adapter = arrayAdapter

        age.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                ageV = if(position == 0){

                    ""

                }else{

                    age.selectedItem.toString()

                }
            }
        }


    }

    //본인 눌렀을때 펑션
    //spinner age에 본인 연령대에 맞는 아이템으로 셀렉션을 한다.
    fun identifySetSpinnerAndGender(){

        checkIdentify.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){

                when{

                    getAge(PubVariable.userInfo.birthday) > 60 -> age.setSelection(9)
                    getAge(PubVariable.userInfo.birthday) > 50 -> age.setSelection(8)
                    getAge(PubVariable.userInfo.birthday) > 40 -> age.setSelection(7)
                    getAge(PubVariable.userInfo.birthday) > 30 -> age.setSelection(6)
                    getAge(PubVariable.userInfo.birthday) > 20 -> age.setSelection(5)
                    getAge(PubVariable.userInfo.birthday) > 16 -> age.setSelection(4)
                    getAge(PubVariable.userInfo.birthday) > 6 -> age.setSelection(3)
                    getAge(PubVariable.userInfo.birthday) > 3 -> age.setSelection(2)
                    getAge(PubVariable.userInfo.birthday) >= 0 -> age.setSelection(1)

                }

                when(PubVariable.userInfo.gender){
                    "M" -> male.isChecked = true
                    "F" -> female.isChecked = true
                }

            }else{

                age.setSelection(0)

            }

        }

    }

    //나이 구하는 펑션
    fun getAge(birthYear : String) : Int{

        var current = Calendar.getInstance()
        var currentYear  = current.get(Calendar.YEAR)

        var age = currentYear - birthYear.substring(0,4).toInt()

        return age
    }

    //화상 원인 버튼 카테고리 뿌려주는 펑션
    fun causeRecyclerViewActivated() {

        causeOfBurnedRecyclerView.layoutManager = GridLayoutManager(this, 4)
        causeOfBurnedRecyclerView.adapter =
            CauseOfBurnedAdapter(
                causeCategory
            )

    }

    //최근 치료받은 진료과 뿌려주는 펑션
    fun deptRecyclerViewActivated(){

        recentlyVisitRecyclerView.layoutManager = GridLayoutManager(this, 3)
        recentlyVisitRecyclerView.adapter =
            DeptAdapter()

    }

    //질문 사항 체크박스
    fun questionRecyclerViewActivated(){

        questionRecyclerView.layoutManager = GridLayoutManager(this, 3)
        questionRecyclerView.adapter =
            QuestionAdapter()

    }

    //화상입은 시기 캘린더 팝업 이벤트
    @SuppressLint("SetTextI18n")
    fun popupCalendar(){

        var now = LocalDate.now().toString()

        var year = now.split("-")[0]
        var month = (now.split("-")[1]).padStart(2, '0')
        var day = now.split("-")[2].padStart(2, '0')

        MYyear = year.toInt()
        MYmonth = month.toInt()
        MYday = day.toInt()

        whenBurned.setOnClickListener{

            val dp = DatePickerDialog(
                this,
                android.app.AlertDialog.THEME_HOLO_DARK,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    whenBurned.setText("${year}-${(month + 1).toString().padStart(2,'0')}-${dayOfMonth.toString().padStart(2, '0')}")
                    burnDateV = "$year + ${month + 1} + $dayOfMonth"
                },
                MYyear,
                MYmonth,
                MYday

            )

            dp.setOnCancelListener {

            }

            dp.datePicker.maxDate = Calendar.getInstance().timeInMillis

            dp.show()

        }

    }

    //화상입은 시기 체크박스 이벤트
    @SuppressLint("SetTextI18n")
    fun burnedHistory(){

        recentlyBurnedWrapper.setOnClickListener {

            recentlyBurned.isChecked = true

        }

        pastBurnedWrapper.setOnClickListener {

            pastBurned.isChecked = true

        }

        recentlyBurned.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){

                pastBurned.isChecked = false
                whenBurned.setText("$MYyear-$MYmonth-$MYday")
                whenBurned.isEnabled = false

                burnDateV = whenBurned.text.toString()
                scarStyleV = "burn"

            }

        }

        pastBurned.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){

                recentlyBurned.isChecked = false
                whenBurned.setText("")
                whenBurned.hint = "화상을 입은 날짜를 입력해주세요."
                whenBurned.isEnabled = true
                scarStyleV = "scar"

            }

        }

        recentlyBurned.isChecked =true

    }

    //본인 체크박스 이벤트
    fun identifyCheckBox(){

        checkIdentifyWrapper.setOnClickListener {

            checkIdentify.isChecked = !checkIdentify.isChecked

        }

    }

    //부위 촬영 클릭시 팝업 이벤트
    fun photoGraphingAlert(){

        shortDistanceShot.setOnClickListener { shortDistancePopup() }

        longDistanceShot.setOnClickListener { longDistancePopup() }

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

        dialog.imageView.setImageResource(R.drawable.pic_20_cm)
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

        //사진 촬영으로 이미지 가져옴
        if(resultCode == Activity.RESULT_OK){

            var bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            bitmapToFile(bitmap)
            var file = File(currentPhotoPath)

            when(requestCode) {

                REQUEST_TAKE_PHOTO_10 -> {
                    imageUri = Uri.fromFile(file)
                    if(imageUri.toString().isNotEmpty()){
                        imageUrl1V = imageUri.toString()
                    }
                }
                REQUEST_TAKE_PHOTO_20 -> {
                    imageUri2 = Uri.fromFile(file)
                    if(imageUri2.toString().isNotEmpty()){
                        imageUrl2V = imageUri2.toString()
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
                        imageUrl1V = imageUri.toString()
                    }
                }
                GET_IMAGE_FROM_GALLERY_20 -> {
                    imageUri2 = data?.data!!
                    if(imageUri2.toString().isNotEmpty()){
                        imageUrl2V = imageUri2.toString()
                    }
                }

            }

        }

    }

    //최종적으로 글 올리는 펑션
    //inputStream 배열에 imageUri를 다 넣은다음 imageuri와 imageurlv를 초기화 한다
    @SuppressLint("SimpleDateFormat")
    fun submitBurnConsulting(){

        consultingSubmit.setOnClickListener {

            if(validationConsulting()) {

                inputStreamArr.add(contentResolver.openInputStream(imageUri)!!)
                inputStreamArr.add(contentResolver.openInputStream(imageUri2)!!)

                imageUri = Uri.EMPTY
                imageUri2 = Uri.EMPTY
                imageUrl1V = ""
                imageUrl2V = ""

                for (inputStream in inputStreamArr) {
                    imageLengthArr.add(inputStream.available())
                }

                AzureAsyncTask(
                    this,
                    inputStreamArr,
                    imageLengthArr
                ).execute(storageConnectionString)

            }else{

                when{

                    imageUrl1V.isEmpty() -> failAlert("10cm 사진 촬영을 진행해주세요")
                    imageUrl2V.isEmpty() -> failAlert("20cm 사진 촬영을 진행해주세요")
                    scarStyleV.isEmpty() -> failAlert("화상 시기를 확인해주세요")
                    burnDateV.isEmpty() -> failAlert("화상입은 날짜를 확인해주세요")
                    bodyStyleV.isEmpty() -> failAlert("신체부위를 확인해주세요")
                    bodyDetailV.isEmpty() -> failAlert("상세 촬영부위를 확인해주세요")
//                    bodyGitaV.isEmpty() -> Toast.makeText(this, "신체부위 기타를 확인해주세요", Toast.LENGTH_LONG).show()
                    burnStyleV.isEmpty() -> failAlert("화상원인을 선택해주세요")
                    burnDetailV.isEmpty() -> failAlert("자세한 화상원인을 선택해주세요")
//                    burnGitaV.isEmpty() -> Toast.makeText(this, "화상 기타를 확인해주세요", Toast.LENGTH_LONG).show()
                    careStyleV.isEmpty() -> failAlert("최근에 치료받은곳을 선택해주세요")
                    genderV.isEmpty() -> failAlert("성별을 확인해주세요")
                    ageV.isEmpty() -> failAlert("연령을 확인해주세요")
                    consultingTitleV.isEmpty() -> failAlert("상담 제목을 입력해주세요")
                    contentsV.isEmpty() -> failAlert("상담 내용을 입력해주세요")

                }

            }

        }

    }

    //등록하기전 app 내 validation 체크
    fun validationConsulting() : Boolean{

        return consultingTitleV.isNotEmpty() && burnDateV.isNotEmpty() && ageV.isNotEmpty() && genderV.isNotEmpty()
                && (bodyDetailV.isNotEmpty() || bodyGitaV.isNotEmpty())
                && burnStyleV.isNotEmpty() && (burnDetailV.isNotEmpty() || burnGitaV.isNotEmpty())
                && (careStyleV.isNotEmpty() || careGitaV.isNotEmpty())
                && scarStyleV.isNotEmpty() && proStatusV.isNotEmpty()
                && imageUrl1V.isNotEmpty() && imageUrl2V.isNotEmpty() && contentsV.isNotEmpty()

    }

    //업로드 실패 알럿
    fun failAlert(ment : String){

        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_ck_x)

        title.text = "실패"
        sub.text = ment

        btn_left.visibility = View.GONE

        btn_right.text = "OK"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        popup = true

        dialog.setOnDismissListener {
            popup = false
            dialog = Dialog(this)
        }

        btn_right.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

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

    //퍼미션 체크
    fun setupPermissions() {

        val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(this, permissions, 0)

    }

    //타이틀바 set Text 펑션
    fun setTitle(){
        txt_title.text = "상처상담하기"
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

}