package fineinsight.app.service.wpias.user_MyQuestionDetail

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
import fineinsight.app.service.wpias.R
import fineinsight.app.service.wpias.publicObject.PubVariable
import fineinsight.app.service.wpias.public_function.FCM
import fineinsight.app.service.wpias.restApi.ApiUtill
import fineinsight.app.service.wpias.user_MyQuestion.MyQuestionActivity
import kotlinx.android.synthetic.main.activity_my_question_detail.*
import kotlinx.android.synthetic.main.custom_alert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StaticFieldLeak")
class AddCase_AzureAsyncTask(var context : Context, var inputStreamArr : ArrayList<InputStream>, var imageLengthArr : ArrayList<Int>, var map:HashMap<String,String>) : AsyncTask<String, Void, Boolean?>() {

    lateinit var container : CloudBlobContainer

    var popup = false

    var m_imageUrl_1 = ""
    var m_imageUrl_2 = ""

    override fun onPreExecute() {

        (context as MyQuestionDetailActivity).Loading((context as MyQuestionDetailActivity).Progress_circle, (context as MyQuestionDetailActivity).Progress_bg, true)

        super.onPreExecute()

    }

    override fun doInBackground(vararg params: String?): Boolean? {

        container = getContainer(params[0]!!)

        return uploadContainerImage(inputStreamArr, imageLengthArr)

    }

    override fun onPostExecute(result: Boolean?) {

        (context as MyQuestionDetailActivity).Loading((context as MyQuestionDetailActivity).Progress_circle, (context as MyQuestionDetailActivity).Progress_bg, false)

        super.onPostExecute(result)

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
        var time = SimpleDateFormat("yyyyMMddkkmmss").format(Calendar.getInstance().time)

        for((i, inputStream) in inputStreamArr.withIndex()){

            if (!container.createIfNotExists()) {

                var imageBlob = container.getBlockBlobReference(
                    time + PubVariable.uid + "_${i+1}.jpg"
                )

                try {

                    imageBlob.upload(inputStream, imageLengthArr[i].toLong())
                    count++

                }catch (e : StorageException){


                    println("경과 추가중 오류가 발생하였습니다. 다시 시도해주세요.")
                    result = false

                }

                //업로드 완료 시
                //container안 내가 업로드한 파일을 확인해서 url을 반환 받음
                //반환 받은 url을 각 imageurl1v imageurl2v에 넣고 insert_question을 실행한다
                if(count == 2){

                    m_imageUrl_1 =
                        "https://storagewpias.blob.core.windows.net/container-wpias-question/${time}${PubVariable.uid}_1.jpg"
                    m_imageUrl_2 =
                        "https://storagewpias.blob.core.windows.net/container-wpias-question/${time}${PubVariable.uid}_2.jpg"
                    insert_question()

                }

            }else{

                println("경과 추가중 오류가 발생하였습니다. 다시 시도해주세요.")
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

        map["IMAGEURL1"] = m_imageUrl_1
        map["IMAGEURL2"] = m_imageUrl_2

        println(map)

        ApiUtill().getINSERT_CASE().insert_case(map).enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {

                failAlert()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {

                if(response.isSuccessful && response.body() == "S"){

                    successAlert()

                }

            }
        })


    }

    //업로드 성공 알럿
    fun successAlert(){

        FCM.function.SendMsgToTopic(FCM.TOPIC.NewQuestion, "신규 질문이 등록되었습니다.")

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

        title.text = "성공"
        sub.text = "글이 성공적으로 등록되었습니다."

        btn_right.visibility = View.GONE

        btn_left.text = "확인"
        btn_left.setBackgroundResource(R.drawable.btn_blue)

        popup = true

        dialog.setOnDismissListener {
            popup = false
            dialog = Dialog(context)
        }

        btn_left.setOnClickListener {

            context.startActivity(Intent(context, MyQuestionActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
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