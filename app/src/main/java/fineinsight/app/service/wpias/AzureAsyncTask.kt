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

class AzureAsyncTask(var inputStream : InputStream, var imageLength : Int) : AsyncTask<String, Void, String?>() {

    lateinit var container : CloudBlobContainer

    override fun doInBackground(vararg params: String?): String? {

        container = getContainer(params[0]!!)

        uploadContainerImage(inputStream, imageLength)



        return ""

    }

    //Azure 컨테이너 불러오는 펑션
    fun getContainer(storageConnectionString : String) : CloudBlobContainer {

        var blobClient = CloudStorageAccount.parse(storageConnectionString).createCloudBlobClient()

        var container =  blobClient.getContainerReference("container-wpias-question")

        return container

    }

    //Azure 컨테이너에 이미지 업로드 하는 펑션
    @SuppressLint("SimpleDateFormat")
    fun uploadContainerImage(inputStream : InputStream, imageLength : Int) {

        if (!container.createIfNotExists()) {
            var imageBlob = container.getBlockBlobReference(
                SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().time)
                        + PubVariable.uid + "_1.jpg"
            )

            imageBlob.upload(inputStream, imageLength.toLong())

//            var imageBlob2 = container.getBlockBlobReference(
//                SimpleDateFormat("yyyyMMddhhmmss").format(Calendar.getInstance().time)
//                        + PubVariable.uid + "_2.jpg"
//            )
//
//            imageBlob2.upload()

        }else{

            println("질문 등록중 오류가 발생하였습니다. 다시 시도해주세요.")

        }

    }

}