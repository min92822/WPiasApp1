package com.phonegap.WPIAS.user_Consulting

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.iterator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phonegap.WPIAS.R
import com.phonegap.WPIAS.RootActivity
import com.phonegap.WPIAS.camera2Api.CameraActivity
import com.phonegap.WPIAS.dataClass.CityInfo
import com.phonegap.WPIAS.dataClass.DistrictInfo
import com.phonegap.WPIAS.publicObject.Location
import com.phonegap.WPIAS.user_Consulting.adapters.CauseOfBurnedAdapter
import com.phonegap.WPIAS.user_Consulting.adapters.DeptAdapter
import com.phonegap.WPIAS.user_Consulting.adapters.PartListAdapter
import com.phonegap.WPIAS.user_Consulting.adapters.QuestionAdapter
import com.phonegap.WPIAS.publicObject.PubVariable
import com.phonegap.WPIAS.publicObject.Validation
import com.phonegap.WPIAS.restApi.ApiUtill
import kotlinx.android.synthetic.main.activity_consulting.*
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.shot_distance_popup.*
import kotlinx.android.synthetic.main.title_bar_darkblue.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ConsultingActivity : RootActivity() {

    //on Activity Result Request Code 상수
    val REQUEST_TAKE_PHOTO_10 = 1
    val REQUEST_TAKE_PHOTO_20 = 2
    val GET_IMAGE_FROM_GALLERY_10 = 3
    val GET_IMAGE_FROM_GALLERY_20 = 4

    //storage 문자열
    val storageConnectionString = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

    //AzureAsyncTask에 들어가는 image uri 및 input stream 배열
    lateinit var imageUri: Uri
    lateinit var imageUri2: Uri
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

    var bodyPartFront = arrayListOf("머리", "어깨", "가슴", "배", "팔", "손", "음부", "다리", "발", "호흡기")
    var bodyPartBack = arrayListOf("머리", "어깨", "등", "허리", "팔", "손", "엉덩이", "다리", "발")
    var causeCategory = arrayListOf("열탕", "화염", "전기", "접촉", "저온", "화학", "증기", "마찰", "햇빛", "흡입")

    //액티비티 초기화될때 validation 전역 변수들을 초기화 시켜준다
    init {
        Validation.valiInit()
        Location.init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulting)
        SetTransparentBar()
        setTitle()
        getCity()
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
        setDescendentViews(window.decorView.rootView)
        eventInit()
    }

    //신체 부위 리싸이클러뷰 활성화 및 부위 버튼 활성화
    //validation을 위한 이벤트 이 펑션에 등록
    //액티비티 시작될때 초기화될 설정들 정리
    @SuppressLint("SimpleDateFormat")
    fun eventInit() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        residentCity.isEnabled = false
        residentDistrict.isEnabled = false
        residentDetailLocation.isEnabled = false
        whenBurned.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Validation.vali.burnDateV = s.toString()
            }
        })

        consultingTitle.setOnTouchListener { v, event ->
            if (v.id == R.id.consultingTitle) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            return@setOnTouchListener false
        }

        consultingTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Validation.vali.consultingTitleV = s.toString()
            }
        })

        consultingContents.setOnTouchListener { v, event ->
            if (v.id == R.id.consultingContents) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            return@setOnTouchListener false
        }

        consultingContents.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Validation.vali.contentsV = s.toString()
            }
        })

        locationMySelf.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.isClickable = false
                locationSelect.isChecked = false
                city.isEnabled = false
                district.isEnabled = false
                detailLocation.isEnabled = true
                city.setSelection(0)
                district.setSelection(0)
            } else {
                buttonView.isClickable = true
            }
        }

        locationSelect.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.isClickable = false
                locationMySelf.isChecked = false
                city.isEnabled = true
                detailLocation.isEnabled = false
                detailLocation.setText("")
            } else {
                buttonView.isClickable = true
            }
        }

        residentMySelf.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.isClickable = false
                residentSelect.isChecked = false
                residentCity.isEnabled = false
                residentDistrict.isEnabled = false
                residentDetailLocation.isEnabled = true
                residentCity.setSelection(0)
                residentDistrict.setSelection(0)
            } else {
                buttonView.isClickable = true
            }
        }

        residentSelect.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.isClickable = false
                residentMySelf.isChecked = false
                residentCity.isEnabled = true
                residentDetailLocation.isEnabled = false
                residentDetailLocation.setText("")
            } else {
                buttonView.isClickable = true
            }
        }
        bodyFront.isChecked = true
        male.isChecked = true
        checkIdentify.isChecked = true
    }

    //앞 뒤 구분 버튼 체크 이벤트
    //버튼 및 버튼 글자 색 변환이 이벤트에 걸려있음
    fun bodyPartCheck() {

        bodyFront.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                bodyBack.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.ocean_blue
                    )
                )
                frontActivated()
            } else {
                buttonView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.ocean_blue
                    )
                )
                bodyBack.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
            }
        }

        bodyBack.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                bodyFront.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.ocean_blue
                    )
                )
                backActivated()
            } else {
                buttonView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.ocean_blue
                    )
                )
                bodyFront.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
            }
        }
    }

    //앞 버튼 터치시 실행되는 리싸이클러 뷰
    fun frontActivated() {
        var partListAdapter =
            PartListAdapter(bodyPartFront)

        bodyPartRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        bodyPartRecyclerView.adapter = partListAdapter
    }

    //뒤 버튼 터치시 실행되는 리싸이클러 뷰
    fun backActivated() {
        var partListAdapter =
            PartListAdapter(bodyPartBack)

        bodyPartRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        bodyPartRecyclerView.adapter = partListAdapter
    }

    //성별 버튼 터치 펑션
    fun genderTouch() {
        male.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                female.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.ocean_blue
                    )
                )
                Validation.vali.genderV = "M"
            } else {
                buttonView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.ocean_blue
                    )
                )
                female.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
            }
        }

        female.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
                male.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.ocean_blue
                    )
                )
                Validation.vali.genderV = "F"
            } else {
                buttonView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.ocean_blue
                    )
                )
                male.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.white
                    )
                )
            }
        }
    }

    //연령 spinner 펑션
    fun ageSpinnerPopup() {
        var arrayAdapter: ArrayAdapter<String>? = null
        var generation = arrayListOf(
            "",
            "0~3세",
            "4~6세",
            "7~15세",
            "16~20세",
            "21~30세",
            "31~40세",
            "41~50세",
            "51~60세",
            "61세이상"
        )

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, generation)
        age.adapter = arrayAdapter
        age.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Validation.vali.ageV = if (position == 0) {
                    ""
                } else {
                    age.selectedItem.toString()
                }
            }
        }
    }

    //본인 눌렀을때 펑션
    //spinner age에 본인 연령대에 맞는 아이템으로 셀렉션을 한다.
    fun identifySetSpinnerAndGender() {

        checkIdentify.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                when {
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

                when (PubVariable.userInfo.gender) {
                    "M" -> male.isChecked = true
                    "F" -> female.isChecked = true
                }

            } else {
                age.setSelection(0)
            }
        }
    }

    //나이 구하는 펑션
    fun getAge(birthYear: String): Int {
        var current = Calendar.getInstance()
        var currentYear = current.get(Calendar.YEAR)
        var age = currentYear - birthYear.substring(0, 4).toInt()

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
    fun deptRecyclerViewActivated() {
        recentlyVisitRecyclerView.layoutManager = GridLayoutManager(this, 3)
        recentlyVisitRecyclerView.adapter =
            DeptAdapter()
    }

    //질문 사항 체크박스
    fun questionRecyclerViewActivated() {
        questionRecyclerView.layoutManager = GridLayoutManager(this, 3)
        questionRecyclerView.adapter =
            QuestionAdapter()
    }

    //화상입은 시기 캘린더 팝업 이벤트
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun popupCalendar() {
        var now = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            now().toString()
        } else {
            SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time)
        }

        var year = now.split("-")[0]
        var month = (now.split("-")[1]).padStart(2, '0')
        var day = now.split("-")[2].padStart(2, '0')

        MYyear = year.toInt()
        MYmonth = month.toInt()
        MYday = day.toInt()

        whenBurned.setOnClickListener {
            val dp = DatePickerDialog(
                this,
                android.app.AlertDialog.THEME_HOLO_DARK,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    whenBurned.setText(
                        "${year}-${(month + 1).toString().padStart(
                            2,
                            '0'
                        )}-${dayOfMonth.toString().padStart(2, '0')}"
                    )
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
    fun burnedHistory() {
        recentlyBurnedWrapper.setOnClickListener {
            recentlyBurned.isChecked = true
        }
        pastBurnedWrapper.setOnClickListener {
            pastBurned.isChecked = true
        }

        recentlyBurned.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                pastBurned.isChecked = false
                whenBurned.setText(
                    "$MYyear-${MYmonth.toString().padStart(
                        2,
                        '0'
                    )}-${MYday.toString().padStart(2, '0')}"
                )
                Validation.vali.burnDateV = whenBurned.text.toString()
                Validation.vali.scarStyleV = "burn"
            }
        }

        pastBurned.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                recentlyBurned.isChecked = false
                whenBurned.setText("")
                whenBurned.hint = "화상을 입은 날짜를 입력해주세요."
                Validation.vali.scarStyleV = "scar"
            }
        }
        recentlyBurned.isChecked = true
    }

    //본인 체크박스 이벤트
    fun identifyCheckBox() {
        checkIdentifyWrapper.setOnClickListener {
            checkIdentify.isChecked = !checkIdentify.isChecked
        }
    }

    //부위 촬영 클릭시 팝업 이벤트
    fun photoGraphingAlert() {
        shortDistanceShot.setOnClickListener { shortDistancePopup() }
        longDistanceShot.setOnClickListener { longDistancePopup() }
    }

    //부위 촬영 팝업(10cm)
    fun shortDistancePopup() {
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
    @SuppressLint("SetTextI18n")
    fun longDistancePopup() {
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
        if (cameraMode == "short") {
            startActivityForResult(Intent(this, CameraActivity::class.java), REQUEST_TAKE_PHOTO_10)
        } else {
            startActivityForResult(Intent(this, CameraActivity::class.java), REQUEST_TAKE_PHOTO_20)
        }
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(packageManager)?.also {
//                // Create the File where the photo should go
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    // Error occurred while creating the File
//                    null
//                }
//                // Continue only if the File was successfully created
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        this,
//                        "com.phonegap.WPIAS.fileprovider",
//                        it
//                    )
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    if(cameraMode == "short") {
//                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_10)
//                    }else{
//                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_20)
//                    }
//                }
//            }
//        }
    }

    //앨범에서 이미지 가져오는 펑션
    fun fromAlbum() {
        if (cameraMode == "short") {
            startActivityForResult(
                Intent(Intent.ACTION_PICK).setType(MediaStore.Images.Media.CONTENT_TYPE),
                GET_IMAGE_FROM_GALLERY_10
            )
        } else {
            startActivityForResult(
                Intent(Intent.ACTION_PICK).setType(MediaStore.Images.Media.CONTENT_TYPE),
                GET_IMAGE_FROM_GALLERY_20
            )
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        var bitmap: Bitmap? = null

        //사진 촬영으로 이미지 가져옴
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_TAKE_PHOTO_10 -> {
                    var path = data?.getStringExtra("path")
                    bitmap = BitmapFactory.decodeFile(path)
                    imageUri = Uri.fromFile(File(path))

                    if (imageUri.toString().isNotEmpty()) {
                        Validation.vali.imageUrl1V = imageUri.toString()
                    }

                    shortDistanceShot.setImageBitmap(bitmap)
//                    bitmap = BitmapFactory.decodeFile(currentPhotoPath)
//                    var file = File(currentPhotoPath)
//                    imageUri = Uri.fromFile(file)
//
//                    if(imageUri.toString().isNotEmpty()){
//                        Validation.vali.imageUrl1V = imageUri.toString()
//                    }
//
//                    imageUri = getImageUriFromBitmap(this, imageResizing(bitmap)!!)
//                    shortDistanceShot.setImageBitmap(imageResizing(bitmap))
                }
                REQUEST_TAKE_PHOTO_20 -> {
                    var path = data?.getStringExtra("path")
                    bitmap = BitmapFactory.decodeFile(path)
                    imageUri2 = Uri.fromFile(File(path))

                    if (imageUri2.toString().isNotEmpty()) {
                        Validation.vali.imageUrl2V = imageUri2.toString()
                    }

                    longDistanceShot.setImageBitmap(bitmap)
//                    bitmap = BitmapFactory.decodeFile(currentPhotoPath)
//                    var file = File(currentPhotoPath)
//                    imageUri2 = Uri.fromFile(file)
//
//                    if(imageUri2.toString().isNotEmpty()){
//                        Validation.vali.imageUrl2V = imageUri2.toString()
//                    }
//
//                    imageUri2 = getImageUriFromBitmap(this, imageResizing(bitmap)!!)
//                    longDistanceShot.setImageBitmap(imageResizing(bitmap))
                }
            }
        }

        //앨범에서 이미지 가져옴
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GET_IMAGE_FROM_GALLERY_10 -> {
                    imageUri = data?.data!!

                    if (imageUri.toString().isNotEmpty()) {
                        Validation.vali.imageUrl1V = imageUri.toString()
                    }

                    bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                contentResolver,
                                imageUri
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    }
                    shortDistanceShot.setImageBitmap(imageResizing(bitmap!!))
                }
                GET_IMAGE_FROM_GALLERY_20 -> {
                    imageUri2 = data?.data!!

                    if (imageUri2.toString().isNotEmpty()) {
                        Validation.vali.imageUrl2V = imageUri2.toString()
                    }

                    bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                contentResolver,
                                imageUri2
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(contentResolver, imageUri2)
                    }
                    longDistanceShot.setImageBitmap(imageResizing(bitmap!!))
                }
            }
        }
    }

    //최종적으로 글 올리는 펑션
    //inputStream 배열에 imageUri를 다 넣은다음 imageuri와 imageurlv를 초기화 한다
    @SuppressLint("SimpleDateFormat")
    fun submitBurnConsulting() {

        consultingSubmit.setOnClickListener {

            if (validationConsulting()) {
                inputStreamArr.add(contentResolver.openInputStream(imageUri)!!)
                inputStreamArr.add(contentResolver.openInputStream(imageUri2)!!)

                imageUri = Uri.EMPTY
                imageUri2 = Uri.EMPTY
                Validation.vali.imageUrl1V = ""
                Validation.vali.imageUrl2V = ""

                for (inputStream in inputStreamArr) {
                    imageLengthArr.add(inputStream.available())
                }

                if (locationSelect.isChecked) {
                    var locationParameter = ""

                    if (city.selectedItemPosition == 0 || city.selectedItemPosition == -1) {
                        locationParameter = " "
                    } else {
                        locationParameter += city.selectedItem
                    }

                    locationParameter += if (district.selectedItemPosition == 0 || district.selectedItemPosition == -1) {
                        ""
                    } else {
                        " ${district.selectedItem}"
                    }

                    Validation.vali.locationV = locationParameter
                } else {
                    Validation.vali.locationV = "${detailLocation.text}"
                }

                if (residentSelect.isChecked) {

                    var homeAreaParameter = ""

                    if (residentCity.selectedItemPosition == 0 || residentCity.selectedItemPosition == -1) {
                        homeAreaParameter = " "
                    } else {
                        homeAreaParameter += residentCity.selectedItem
                    }

                    homeAreaParameter += if (residentDistrict.selectedItemPosition == 0 || residentDistrict.selectedItemPosition == -1) {
                        ""
                    } else {
                        " ${residentDistrict.selectedItem}"
                    }

                    Validation.vali.homeAreaV = homeAreaParameter
                } else {
                    Validation.vali.homeAreaV = "${residentDetailLocation.text}"
                }

                AzureAsyncTask(
                    this,
                    inputStreamArr,
                    imageLengthArr
                ).execute(storageConnectionString)

            } else {
                when {
                    Validation.vali.imageUrl1V.isEmpty() -> failAlert("상세사진 촬영을 진행해주세요")
                    Validation.vali.imageUrl2V.isEmpty() -> failAlert("전체사진 촬영을 진행해주세요")
                    Validation.vali.scarStyleV.isEmpty() -> failAlert("화상 시기를 확인해주세요")
                    Validation.vali.burnDateV.isEmpty() -> failAlert("화상입은 날짜를 확인해주세요")
                    Validation.vali.bodyStyleV.isEmpty() -> failAlert("신체부위를 확인해주세요")
                    Validation.vali.bodyDetailV.isEmpty() -> failAlert("상세 촬영부위를 확인해주세요")
//                    Validation.vali.bodyGitaV.isEmpty() -> Toast.makeText(this, "신체부위 기타를 확인해주세요", Toast.LENGTH_LONG).show()
                    Validation.vali.burnStyleV.isEmpty() -> failAlert("화상원인을 선택해주세요")
                    Validation.vali.burnDetailV.isEmpty() -> failAlert("자세한 화상원인을 선택해주세요")
//                    Validation.vali.burnGitaV.isEmpty() -> Toast.makeText(this, "화상 기타를 확인해주세요", Toast.LENGTH_LONG).show()
                    Validation.vali.careStyleV.isEmpty() -> failAlert("최근에 치료받은곳을 선택해주세요")
                    Validation.vali.genderV.isEmpty() -> failAlert("성별을 확인해주세요")
                    Validation.vali.ageV.isEmpty() -> failAlert("연령을 확인해주세요")
                    Validation.vali.consultingTitleV.isEmpty() -> failAlert("상담 제목을 입력해주세요")
                    Validation.vali.contentsV.isEmpty() -> failAlert("상담 내용을 입력해주세요")
                }
            }
        }
    }

    //등록하기전 app 내 validation 체크
    fun validationConsulting(): Boolean {
        return Validation.vali.consultingTitleV.isNotEmpty() && Validation.vali.burnDateV.isNotEmpty() && Validation.vali.ageV.isNotEmpty()
                && Validation.vali.genderV.isNotEmpty() && (Validation.vali.bodyDetailV.isNotEmpty() || Validation.vali.bodyGitaV.isNotEmpty())
                && Validation.vali.burnStyleV.isNotEmpty() && (Validation.vali.burnDetailV.isNotEmpty() || Validation.vali.burnGitaV.isNotEmpty())
                && (Validation.vali.careStyleV.isNotEmpty() || Validation.vali.careGitaV.isNotEmpty()) && Validation.vali.scarStyleV.isNotEmpty()
                && Validation.vali.proStatusV.isNotEmpty() && Validation.vali.imageUrl1V.isNotEmpty() && Validation.vali.imageUrl2V.isNotEmpty()
                && Validation.vali.contentsV.isNotEmpty()
    }

    //업로드 실패 알럿
    fun failAlert(ment: String) {
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
        val timeStamp: String = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
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

    //사진 1:1비율로 만드는 펑션
    fun imageResizing(bitmap: Bitmap): Bitmap? {
        var matrix = Matrix()

        println("bitmap.width: ${bitmap.width}")
        println("bitmap.height: ${bitmap.height}")

        return if (bitmap.width > bitmap.height) {
            //가로가 짧은 사진이 들어오는 곳
            println("ddd")
            matrix.postRotate(90f)
            Bitmap.createScaledBitmap(
                Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                ), 1200, 1200, true
            )
        } else {
            //가로가 긴 사진이 들어와야하는데 안들어옴
            println("bbb")
            Bitmap.createScaledBitmap(
                Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                ), 1200, 1200, true
            )
        }
    }

    //타이틀바 set Text 펑션
    fun setTitle() {
        txt_title.text = "상처상담하기"
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }

    //에딧 텍스트 아닌 부분 클릭시 키보드 사라지는 펑션
    fun hideKeyboard() {
        var imm = (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)

        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        window?.decorView?.clearFocus()
    }

    //최상위 뷰 태그 및 하위 뷰 태그에 hideKeboard를 적용하는 펑션
    fun setDescendentViews(view: View) {

        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideKeyboard()
                return@setOnTouchListener false
            }
        }

        if (view is RecyclerView) {
            view.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
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

        if (view is ViewGroup) {
            for (innerview in view) {
                setDescendentViews(innerview)
            }
        }
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    //시,도 정보 받아오는 펑션
    fun getCity() {

        Loading(ProgressBar, ProgressBg, true)

        ApiUtill().getSELECT_CITY().select_city().enqueue(object : Callback<ArrayList<CityInfo>> {
            override fun onResponse(
                call: Call<ArrayList<CityInfo>>,
                response: Response<ArrayList<CityInfo>>
            ) {
                //else, Failure 부분 Toast나 알럿 띄우기
                if (response.isSuccessful) {
                    Loading(ProgressBar, ProgressBg, false)
                    Location.cityInfo = response.body()!!
                    citySpinnerPopUp()
                } else {
                    Loading(ProgressBar, ProgressBg, false)
                }
            }

            override fun onFailure(call: Call<ArrayList<CityInfo>>, t: Throwable) {
                Loading(ProgressBar, ProgressBg, false)
            }
        })
    }

    //시,도 보여주는 spinner
    fun citySpinnerPopUp() {

        var cityNmArr = ArrayList<String>()

        cityNmArr.add("지역선택")

        for (city in Location.cityInfo) {
            cityNmArr.add(city.citynm)
        }

        var arrayAdapter: ArrayAdapter<String>? = null

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, cityNmArr)
        city.adapter = arrayAdapter
        city.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    district.isEnabled = true
                    for (detailCity in Location.cityInfo) {
                        if (detailCity.citynm == city.selectedItem.toString()) {
                            getDistrict(detailCity.citycd, city)
                        }
                    }
                } else {
                    district.isEnabled = false
                    district.setSelection(0)
                }
            }
        }

        residentCity.adapter = arrayAdapter

        residentCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    residentDistrict.isEnabled = true
                    for (detailCity in Location.cityInfo) {
                        if (detailCity.citynm == residentCity.selectedItem.toString()) {
                            getDistrict(detailCity.citycd, residentCity)
                        }
                    }
                } else {
                    residentDistrict.isEnabled = false
                    residentDistrict.setSelection(0)
                }
            }
        }
    }

    //지역, 구 조회
    fun getDistrict(cityCd: String, view: View) {

        Loading(ProgressBar, ProgressBg, true)

        var map = HashMap<String, String>()
        map["CITYCD"] = cityCd

        ApiUtill().getSELECT_DISTRICT().select_district(map)
            .enqueue(object : Callback<ArrayList<DistrictInfo>> {

                override fun onResponse(
                    call: Call<ArrayList<DistrictInfo>>,
                    response: Response<ArrayList<DistrictInfo>>
                ) {
                    Loading(ProgressBar, ProgressBg, false)

                    //else, Failure 부분 Toast나 알럿 띄우기
                    if (response.isSuccessful) {
                        Location.districtInfo = response.body()!!
                        districtSpinnerPopup(view)
                    }
                }

                override fun onFailure(call: Call<ArrayList<DistrictInfo>>, t: Throwable) {
                    Loading(ProgressBar, ProgressBg, false)
                }
            })
    }

    // 지역, 구 보여주는 spinner
    fun districtSpinnerPopup(view: View) {

        var districtNmArr = ArrayList<String>()

        districtNmArr.add("시/군/구 선택")

        for (district in Location.districtInfo) {
            districtNmArr.add(district.districtnm)
        }

        var arrayAdapter: ArrayAdapter<String>? = null

        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, districtNmArr)

        when (view.id) {
            R.id.city -> district.adapter = arrayAdapter
            R.id.residentCity -> residentDistrict.adapter = arrayAdapter
        }
    }
}