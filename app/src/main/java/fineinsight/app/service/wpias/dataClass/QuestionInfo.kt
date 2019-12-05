package fineinsight.app.service.wpias.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QuestionInfo (@SerializedName("QKEY") @Expose var qkey : String,
                         @SerializedName("TITLE") @Expose var title : String,
                         @SerializedName("UUID") @Expose var uuid : String,
                         @SerializedName("INSERTDATE") @Expose var insertdate : String,
                         @SerializedName("BURNDATE") @Expose var burndate : String,
                         @SerializedName("AGE") @Expose var age : String,
                         @SerializedName("GENDER") @Expose var gender : String,
                         @SerializedName("BODYSTYLE") @Expose var bodystyle : String,
                         @SerializedName("BODYDETAIL") @Expose var bodydetail : String,
                         @SerializedName("BODYGITA") @Expose var bodygita : String,
                         @SerializedName("BURNSTYLE") @Expose var burnstyle : String,
                         @SerializedName("BURNDETAIL") @Expose var burndetail : String,
                         @SerializedName("BURNGITA") @Expose var burngita : String,
                         @SerializedName("SCARSTYLE") @Expose var scarstyle : String,
                         @SerializedName("ANSWERDOC") @Expose var answerdoc : String,
                         @SerializedName("PROSTATUS") @Expose var prostatus : String,
                         @SerializedName("BURNNM") @Expose var burnnm : String,
                         @SerializedName("IMAGEURL1") @Expose var imageurl1 : String,
                         @SerializedName("IMAGEURL2") @Expose var imageurl2 : String,
                         @SerializedName("DETAILNM") @Expose var detailnm : String)

/*
    QKEY		질문 KEY
    TITLE		제목
    UUID		작성자 UUID
    INSERTDATE	작성시간 (yyyyMMddhhmmss)
    BURNDATE	다친날짜
    AGE	    	나이
    GENDER		성별
    BODYSTYLE	상처부위 코드
    BODYDETAIL	상처부위 디테일 코드
    BODYGITA	상처부위 기타내용
    BURNSTYLE	상처원인 코드
    BURNDETAIL	상처원인 디테일 코드
    BURNGITA	상처원인 기타내용
    SCARSTYLE	화상, 흉터 종류
    ANSWERDOC	답변의사 UUID
    PROSTATUS	답변상태
    BURNNM
    IMAGEURL1
    IMAGEURL2
    DETAILNM

 */