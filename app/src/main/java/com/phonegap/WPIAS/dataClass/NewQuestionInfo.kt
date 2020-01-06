package com.phonegap.WPIAS.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewQuestionInfo(@SerializedName("QKEY") @Expose var qkey : String,
            @SerializedName("UUID") @Expose var uuid : String,
            @SerializedName("TITLE") @Expose var title : String,
            @SerializedName("INSERTDATE") @Expose var insertdate : String,
            @SerializedName("BURNDATE") @Expose var burndate : String,
            @SerializedName("NICKNAME") @Expose var nickname : String,
            @SerializedName("AGE") @Expose var age : String,
            @SerializedName("GENDER") @Expose var gender : String,
            @SerializedName("BURNSTYLE") @Expose var burnstyle : String,
            @SerializedName("BURNNM") @Expose var burnnm : String,
            @SerializedName("BURNDETAILCD") @Expose var burndetailcd : String,
            @SerializedName("BURNDETAILNM") @Expose var burndetailnm : String,
            @SerializedName("BURNGITA") @Expose var burngita : String,
            @SerializedName("BODYSTYLE") @Expose var bodystyle : String,
            @SerializedName("BODYNM") @Expose var bodynm : String,
            @SerializedName("BODYDETAILCD") @Expose var bodydetailcd : String,
            @SerializedName("BODYDETAILNM") @Expose var bodydetailnm : String,
            @SerializedName("BODYGITA") @Expose var bodygita : String,
            @SerializedName("ISLOCK") @Expose var islock : String,
            @SerializedName("LOCKUSER") @Expose var lockuser : String,
            @SerializedName("LOCKTIME") @Expose var locktime : String,
            @SerializedName("SCARSTYLE") @Expose var scarstyle : String,
            @SerializedName("CARESTYLE") @Expose var carestyle : String,
            @SerializedName("CAREAREA") @Expose var carearea : String) : Serializable

/*
QKEY	String	질문 KEY
UUID	String	질문 작성자 UUID
TITLE	String	질문 제목
INSERTDATE	String	작성시간 (yyyyMMddhhmmss)
BURNDATE	String	상처발생일 (yyyy-MM-dd)
NICKNAME	String	작성자 이름
AGE	String	화상환자 나이
GENDER	String	성별 (M/F)
BURNSTYLE	String	화상원인 코드
BURNNM	String	화상원인 이름
BURNDETAILCD	String	화상원인 디테일 코드
BURNDETAILNM	String	화상원인 디테일 이름
BURNGITA	String	화상 기타 원인
BODYSTYLE	String	화상부위 코드
BODYNM	String	화상부위 이름
BODYDETAILCD	String	화상부위 디테일 코드
BODYDETAILNM	String	화상부위 디테일 이름
BODYGITA	String	화상 기타  부위
ISLOCK	String	락 여부
LOCKUSER	String	락 유저
LOCKTIME	String	락 시간
SCARSTYLE	String	화상 스타일 (scar: 흉터, burn: 상처)
CARESTYLE	String	최근에 치료 받은곳 코드
CAREAREA	String	최근에 치료 받은지역 텍스트
COUNT	Int	접속 의사 총 답변 수
*/
