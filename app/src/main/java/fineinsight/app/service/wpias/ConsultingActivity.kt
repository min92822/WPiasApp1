package fineinsight.app.service.wpias

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Window
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.blob.CloudBlobContainer
import fineinsight.app.service.wpias.adapters.CauseOfBurnedAdapter
import fineinsight.app.service.wpias.adapters.DeptAdapter
import fineinsight.app.service.wpias.adapters.PartListAdapter
import fineinsight.app.service.wpias.adapters.QuestionAdapter
import fineinsight.app.service.wpias.dataClass.UserInfo
import fineinsight.app.service.wpias.publicObject.PubVariable
import kotlinx.android.synthetic.main.activity_consulting.*
import kotlinx.android.synthetic.main.shot_distance_popup.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ConsultingActivity : RootActivity(){

    val REQUEST_TAKE_PHOTO = 1
    val GET_IMAGE_FROM_GALLERY = 2

    val storageConnectionString = "DefaultEndpointsProtocol=https;AccountName=storagewpias;AccountKey=G+ZyYwRLvxTFebMpLqsSeNI/V+1ALImJqs1MAG1rD315BN1TRO7Q8CpcKv0KOmRB9hasKF4pJqZkTEJ3TEAlPw=="

    lateinit var imageUri : Uri
    lateinit var inputStream : InputStream
    var imageLength = 0
    lateinit var imageUri2 : Uri
    lateinit var inputStream2 : InputStream
    var imageLength2 = 0

    var currentPhotoPath = ""

    var bodyPartFront = arrayListOf("머리", "어깨", "가슴", "배", "팔", "손", "음부", "다리", "발", "호흡기")
    var bodyPartBack = arrayListOf("머리", "어깨", "등", "허리", "팔", "손", "엉덩이", "다리", "발")
    var causeCategory = arrayListOf("열탕", "화염", "전기", "접촉", "저온", "화학", "증기", "마찰", "햇빛", "흡입")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulting)

        setupPermissions()

        SetTransparentBar()

        burnedHistory()

        bodyPartCheck()

        genderTouch()

        causeRecyclerViewActivated()

        identifyCheckBox()

        deptRecyclerViewActivated()

        questionRecyclerViewActivated()

        photoGraphingAlert()

        submitBurnConsulting()

        eventInit()

    }

    //신체 부위 리싸이클러뷰 활성화 및 부위 버튼 활성화
    fun eventInit(){

        bodyFront.isChecked = true

        male.isChecked = true

    }

    //앞 뒤 구분 버튼 체크 이벤트
    //버튼 및 버튼 글자 색 변환이 이벤트에 걸려있음
    fun bodyPartCheck(){

        bodyFront.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.white))
                bodyBack.setTextColor(ContextCompat.getColor(this, R.color.ocean_blue))
                frontActivated()
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.ocean_blue))
                bodyBack.setTextColor(ContextCompat.getColor(this, R.color.white))
            }

        }

        bodyBack.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.white))
                bodyFront.setTextColor(ContextCompat.getColor(this, R.color.ocean_blue))
                backActivated()
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.ocean_blue))
                bodyFront.setTextColor(ContextCompat.getColor(this, R.color.white))
            }

        }

    }

    //앞 버튼 터치시 실행되는 리싸이클러 뷰
    fun frontActivated(){

        var partListAdapter = PartListAdapter(bodyPartFront)

        bodyPartRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        bodyPartRecyclerView.adapter = partListAdapter

    }

    //뒤 버튼 터치시 실행되는 리싸이클러 뷰
    fun backActivated(){

        var partListAdapter = PartListAdapter(bodyPartBack)

        bodyPartRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        bodyPartRecyclerView.adapter = partListAdapter

    }

    //성별 버튼 터치 펑션
    fun genderTouch(){

        male.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.white))
                female.setTextColor(ContextCompat.getColor(this, R.color.ocean_blue))
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.ocean_blue))
                female.setTextColor(ContextCompat.getColor(this, R.color.white))
            }

        }

        female.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.white))
                male.setTextColor(ContextCompat.getColor(this, R.color.ocean_blue))
            }else{
                buttonView.setTextColor(ContextCompat.getColor(this, R.color.ocean_blue))
                male.setTextColor(ContextCompat.getColor(this, R.color.white))
            }

        }

    }

    //화상 원인 버튼 카테고리 뿌려주는 펑션
    fun causeRecyclerViewActivated() {

        causeOfBurnedRecyclerView.layoutManager = GridLayoutManager(this, 4)
        causeOfBurnedRecyclerView.adapter = CauseOfBurnedAdapter(causeCategory)

    }

    //최근 치료받은 진료과 뿌려주는 펑션
    fun deptRecyclerViewActivated(){

        recentlyVisitRecyclerView.layoutManager = GridLayoutManager(this, 3)
        recentlyVisitRecyclerView.adapter = DeptAdapter()

    }

    //질문 사항 체크박스
    fun questionRecyclerViewActivated(){

        questionRecyclerView.layoutManager = GridLayoutManager(this, 3)
        questionRecyclerView.adapter = QuestionAdapter()

    }

    //화상입은 시기 체크박스 이벤트
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

            }

        }

        pastBurned.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){

                recentlyBurned.isChecked = false

            }

        }

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
    fun longDistancePopup(){



    }

    //핸드폰에 내장된 카메라 관련 어플들을 불러오는 펑션
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
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    //앨범에서 이미지 가져오는 펑션
    fun fromAlbum(){

        startActivityForResult(Intent(Intent.ACTION_PICK).setType(MediaStore.Images.Media.CONTENT_TYPE), GET_IMAGE_FROM_GALLERY)

    }

    @SuppressLint("SimpleDateFormat")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        //사진 촬영으로 이미지 가져옴
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){

            var file = File(currentPhotoPath)

            imageUri = Uri.fromFile(file)

        }

        //앨범에서 이미지 가져옴
        if(requestCode == GET_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK){

            imageUri = data?.data!!

        }

    }

    //최종적으로 글 올리는 펑션
    //1차 이미지 업로드만
    @SuppressLint("SimpleDateFormat")
    fun submitBurnConsulting(){

        consultingSubmit.setOnClickListener {

            inputStream = contentResolver.openInputStream(imageUri)!!
            imageLength = inputStream.available()

            AzureAsyncTask(inputStream, imageLength).execute(storageConnectionString)

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

    //사진 세로로 세우는 펑션
    fun imageRotate(bitmap : Bitmap) : Bitmap?{

        var matrix = Matrix()

        matrix.postRotate(90f)

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    }

    //퍼미션 체크
    fun setupPermissions() {

        val permissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
        ActivityCompat.requestPermissions(this, permissions, 0)

    }

}