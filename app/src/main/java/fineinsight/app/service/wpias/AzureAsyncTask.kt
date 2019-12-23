package fineinsight.app.service.wpias

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.view.View
import android.view.Window
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.StorageException
import com.microsoft.azure.storage.blob.CloudBlobContainer
import fineinsight.app.service.wpias.publicObject.PubVariable
import fineinsight.app.service.wpias.restApi.ApiUtill
import fineinsight.app.service.wpias.user_Consulting.ConsultingActivity
import fineinsight.app.service.wpias.user_Main.MainActivity
import kotlinx.android.synthetic.main.custom_alert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@SuppressLint("StaticFieldLeak")
class AzureAsyncTask(var context : Context, var inputStreamArr : ArrayList<InputStream>, var imageLengthArr : ArrayList<Int>) : AsyncTask<String, Void, Boolean?>() {

    lateinit var container : CloudBlobContainer

    var popup = false

    override fun doInBackground(vararg params: String?): Boolean? {

        container = getContainer(params[0]!!)

        return uploadContainerImage(inputStreamArr, imageLengthArr)

    }

    //Azure 컨테이너 불러오는 펑션
    fun getContainer(storageConnectionString : String) : CloudBlobContainer {

        var blobClient = CloudStorageAccount.parse(storageConnectionString).createCloudBlobClient()

        var container =  blobClient.getContainerReference("container-wpias-question")

        return container

    }

    //Azure 컨테이너에 이미지 업로드 하는 펑션
    @SuppressLint("SimpleDateFormat")
    fun uploadContainerImage(inputStreamArr : ArrayList<InputStream>, imageLengthArr : ArrayList<Int>) : Boolean {

        var result = true
        var count = 0
        var time = SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().time)

        for((i, inputStream) in inputStreamArr.withIndex()){

            if (!container.createIfNotExists()) {

                var imageBlob = container.getBlockBlobReference(
                    time + PubVariable.uid + "_${i+1}.jpg"
                )

                try {

                    imageBlob.upload(inputStream, imageLengthArr[i].toLong())
                    count++

                }catch (e : StorageException){


                    println("질문 등록중 오류가 발생하였습니다. 다시 시도해주세요.")
                    result = false

                }

                //업로드 완료 시
                //container안 내가 업로드한 파일을 확인해서 url을 반환 받음
                //반환 받은 url을 각 imageurl1v imageurl2v에 넣고 insert_question을 실행한다
                if(count == 2){

                    ConsultingActivity.imageUrl1V =
                        "https://storagewpias.blob.core.windows.net/container-wpias-question/${time}${PubVariable.uid}_1.jpg"
                    ConsultingActivity.imageUrl2V =
                        "https://storagewpias.blob.core.windows.net/container-wpias-question/${time}${PubVariable.uid}_2.jpg"
                    insert_question()

                }

            }else{

                println("질문 등록중 오류가 발생하였습니다. 다시 시도해주세요.")
                result = false

            }

        }

        //사진 다시 찍게 하기 위함
        inputStreamArr.clear()
        imageLengthArr.clear()

        return result

    }

    //restApi insert
    @SuppressLint("SimpleDateFormat")
    fun insert_question(){

        var map = HashMap<String, String>()

        ConsultingActivity.directionV.sort()

        map["QKEY"] = SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().time) + "_" + PubVariable.uid
        map["TITLE"] = ConsultingActivity.consultingTitleV
        map["UUID"] = PubVariable.uid
        map["INSERTDATE"] = SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().time)
        map["BURNDATE"] = ConsultingActivity.burnDateV
        map["AGE"] = ConsultingActivity.ageV
        map["GENDER"] = ConsultingActivity.genderV
        map["BODYSTYLE"] = ConsultingActivity.bodyStyleV
        map["BODYDETAIL"] = ConsultingActivity.bodyDetailV
        map["BODYGITA"] = ConsultingActivity.bodyGitaV
        map["BURNSTYLE"] = ConsultingActivity.burnStyleV
        map["BURNDETAIL"] = ConsultingActivity.burnDetailV
        map["BURNGITA"] = ConsultingActivity.burnGitaV
        map["CARESTYLE"] = ConsultingActivity.careStyleV
        map["CAREGITA"] = ConsultingActivity.careGitaV
        map["SCARSTYLE"] = ConsultingActivity.scarStyleV
        map["PROSTATUS"] = ConsultingActivity.proStatusV
        map["DIRECTION"] = ConsultingActivity.directionV.joinToString("-")
        map["IMAGEURL1"] = ConsultingActivity.imageUrl1V
        map["IMAGEURL2"] = ConsultingActivity.imageUrl2V
        map["CONTENTS"] = ConsultingActivity.contentsV

        ApiUtill().getINSERT_QUESTION().insert_question(map).enqueue(object : Callback<String>{

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if(response.isSuccessful && response.body()!! == "S"){

                    successAlert()

                }else{

                    failAlert()

                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                failAlert()

            }

        })

    }

    //업로드 성공 알럿
    fun successAlert(){

        var dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_alert)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var img = dialog.img_alert
        var title = dialog.txt_alert_title
        var sub = dialog.txt_alert_sub
        var btn_left = dialog.btn_alert_left
        var btn_right = dialog.btn_alert_right

        img.setImageResource(R.drawable.alert_ck)

        title.text = "등록"
        sub.text = "글이 성공적으로 등록되었습니다."

        btn_left.text = "확인"
        btn_left.setBackgroundResource(R.drawable.btn_blue)

        btn_right.text = "내 글 보기"
        btn_right.setBackgroundResource(R.drawable.btn_purple)

        popup = true

        dialog.setOnDismissListener {
            popup = false
            dialog = Dialog(context)
        }

        btn_left.setOnClickListener {

            context.startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            dialog.dismiss()

        }

        btn_right.setOnClickListener {
            context.startActivity(Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
            dialog.dismiss()
        }

        dialog.show()

    }

    //업로드 실패 알럿
    fun failAlert(){

        var dialog = Dialog(context)
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
        sub.text = "업로드에 실패하였습니다."

        btn_left.visibility = View.GONE

        btn_right.text = "OK"
        btn_right.setBackgroundResource(R.drawable.btn_blue)

        popup = true

        dialog.setOnDismissListener {
            popup = false
            dialog = Dialog(context)
        }

        btn_right.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }

}