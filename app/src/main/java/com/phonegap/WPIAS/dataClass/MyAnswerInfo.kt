package com.phonegap.WPIAS.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyAnswerInfo(@SerializedName("QKEY") @Expose var qkey : String,
                        @SerializedName("UUID") @Expose var uuid : String,
                        @SerializedName("TITLE") @Expose var title : String,
                        @SerializedName("INSERTDATE") @Expose var insertdate : String,
                        @SerializedName("BURNDATE") @Expose var burndate : String,
                        @SerializedName("NICKNAME") @Expose var nickname : String,
                        @SerializedName("AGE") @Expose var age : String,
                        @SerializedName("GENDER") @Expose var gender : String,
                        @SerializedName("EMAIL") @Expose var email : String,
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
                        @SerializedName("PROSTATUS") @Expose var prostatus : String,
                        @SerializedName("SCARSTYLE") @Expose var scarstyle : String,
                        @SerializedName("ANSWERCOUNT") @Expose var answercount : Double,
                        @SerializedName("TOTALCOUNT") @Expose var totalcount : Double) : Serializable

/*
QKEY	String	질문 KEY
UUID	String	사용자 UUID
TITLE	String	질문 제목
INSERTDATE	String	질문 등록일 yyyyMMddHHmmss
BURNDATE	String	상처 발생일 yyyy-MM-dd
NICKNAME	String	사용자 이름
AGE	String	나이
GENDER	String	성별 (M/F)
EMAIL	String	작성자 이메일
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
PROSTATUS	String	질문 현재 상태 (A: 답변완료, R:추가질문등록)
SCARSTYLE	String	화상 스타일 (scar: 흉터, burn: 상처)
ANSWERCOUNT	Int	현재 질문 케이스 중 답변 수
TOTALCOUNT	Int	현재 질문 총 케이스 수
*/
