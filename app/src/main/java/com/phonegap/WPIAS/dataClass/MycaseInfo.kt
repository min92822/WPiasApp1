package com.phonegap.WPIAS.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MycaseInfo(@SerializedName("CKEY") @Expose var ckey : String,
                      @SerializedName("CNUMBER") @Expose var cnumber : String,
                      @SerializedName("CASEDATE") @Expose var casedate : String,
                      @SerializedName("DIRECTION") @Expose var direction : String,
                      @SerializedName("IMAGEURL1") @Expose var imageurl1 : String,
                      @SerializedName("IMAGEURL2") @Expose var imageurl2 : String,
                      @SerializedName("CONTENTS") @Expose var contents : String,
                      @SerializedName("ISLOCK") @Expose var islock : String,
                      @SerializedName("LOCKTIME") @Expose var locktime : String,
                      @SerializedName("LOCKUSER") @Expose var lockuser : String,
                      @SerializedName("CASESTATUS") @Expose var casestatus : String,
                      @SerializedName("VISIBLE") @Expose var visible : String,
                      @SerializedName("FEEDBACKSTAR") @Expose var feedbackstar : String,
                      @SerializedName("FEEDBACKTEXT") @Expose var feedbacktext : String,
                      @SerializedName("FEEDBACKTIME") @Expose var feedbacktime : String,
                      @SerializedName("ANSWERCONTENTS") @Expose var answercontents : String,
                      @SerializedName("ANSWERDATE") @Expose var answerdate : String,
                      @SerializedName("ANSWERDOC") @Expose var answerdoc : String,
                      @SerializedName("ANSWERDOCNM") @Expose var answerdocnm : String,
                      @SerializedName("ANSWERDOCCOUNT") @Expose var answerdoccount : Int) : Serializable

/*
    CKEY		    질문 KEY
    CNUMBER		    케이스 KEY
    CASEDATE		작성시간 (yyyyMMddhhmmss)
    DIRECTION	    궁금한점
    IMAGEURL1		이미지1 URL
    IMAGEURL2		이미지1 URL
    CONTENTS		질문내용
    ISLOCK	    	락 여부
    LOCKTIME		락 시간
    LOCKUSER		락 유저 UIUID
    CASESTATUS		CASE 상태
    VISIBLE		    공개여부
    FEEDBACKSTAR	별점
    FEEDBACKTEXT	후기 내용
    FEEDBACKTIME	후기 등록 시간
    ANSWERCONTENTS	의사 답변
    ANSWERDATE		의사 답변 시간 (yyyyMMddhhmmss)
    ANSWERDOC		의사 UUID
    ANSWERDOCNM     의사 이름
    ANSWERDOCCOUNT	의사 답변 건수
 */