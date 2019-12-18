package fineinsight.app.service.wpias

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.widget.Toast
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.StorageException
import com.microsoft.azure.storage.blob.BlobRequestOptions
import com.microsoft.azure.storage.blob.CloudBlobContainer
import fineinsight.app.service.wpias.publicObject.PubVariable
import java.io.InputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AzureAsyncTask(var inputStreamArr : ArrayList<InputStream>, var imageLengthArr : ArrayList<Int>) : AsyncTask<String, Void, Boolean?>() {

    lateinit var container : CloudBlobContainer

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

        for((i, inputStream) in inputStreamArr.withIndex()){

            if (!container.createIfNotExists()) {

                var imageBlob = container.getBlockBlobReference(
                    SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().time)
                            + PubVariable.uid + "_${i+1}.jpg"
                )

                try {

                    imageBlob.upload(inputStream, imageLengthArr[i].toLong())
                    count++
                }catch (e : StorageException){


                    println("질문 등록중 오류가 발생하였습니다. 다시 시도해주세요.")
                    result = false

                }

                if(count == 2){



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

    //등록하기전 app 내 validation 체크
    fun validationConsulting() : Boolean{

        return ConsultingActivity.burnDateV.isNotEmpty() && ConsultingActivity.ageV.isNotEmpty() && ConsultingActivity.genderV.isNotEmpty() && (ConsultingActivity.bodyDetailV.isNotEmpty() || ConsultingActivity.bodyGitaV.isNotEmpty())
                && ConsultingActivity.burnStyleV.isNotEmpty() && (ConsultingActivity.burnDetailV.isNotEmpty() || ConsultingActivity.burnGitaV.isNotEmpty())
                && ConsultingActivity.scarStyleV.isNotEmpty() && ConsultingActivity.proStatusV.isNotEmpty() && ConsultingActivity.directionV.isNotEmpty()
                && ConsultingActivity.imageUrl1V.isNotEmpty() && ConsultingActivity.imageUrl2V.isNotEmpty() && ConsultingActivity.contentsV.isNotEmpty()

    }

    //restApi insert
    fun insert_question(){

        var map = HashMap<String, String>()

        map

    }

}