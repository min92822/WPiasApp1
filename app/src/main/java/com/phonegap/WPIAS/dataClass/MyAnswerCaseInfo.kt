package com.phonegap.WPIAS.dataClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MyAnswerCaseInfo(@SerializedName("CKEY") @Expose var ckey : String,
                            @SerializedName("CNUMBER") @Expose var cnumber : String,
                            @SerializedName("CASEDATE") @Expose var casedate : String,
                            @SerializedName("DIRECTION") @Expose var direction : String,
                            @SerializedName("IMAGEURL1") @Expose var imageurl1 : String,
                            @SerializedName("IMAGEURL2") @Expose var imageurl2 : String,
                            @SerializedName("CONTENTS") @Expose var contents : String,
                            @SerializedName("CASESTATUS") @Expose var casestatus : String,
                            @SerializedName("FEEDBACKSTAR") @Expose var feedbackstar : String,
                            @SerializedName("FEEDBACKTEXT") @Expose var feedbacktext : String,
                            @SerializedName("FEEDBACKTIME") @Expose var feedbacktime : String,
                            @SerializedName("FEEDBACKREPLY") @Expose var feedbackreply : String,
                            @SerializedName("FEEDBACKREPLYTIME") @Expose var feedbackreplytime : String,
                            @SerializedName("ANSWERCONTENTS") @Expose var answercontents : String,
                            @SerializedName("ANSWERDATE") @Expose var answerdate : String) : Serializable

/*
CKEY	String	질문 CKEY
CNUMBER	String	경과번호
CASEDATE	String	경과 등록 날짜 yyyyMMddhhmmss
DIRECTION	String	궁금한점
IMAGEURL1	String	imgurl1
IMAGEURL2	String	imgurl1
CONTENTS	String	질문 내용
CASESTATUS	String	질문 상태 (Q: 답변요청 A: 답변완료 P:답변미요청)
FEEDBACKSTAR	String	별점
FEEDBACKTEXT	String	리뷰내용
FEEDBACKTIME	String	리뷰시간 (값이 ""이면 리뷰 등록 x) yyyyMMddhhmmss
FEEDBACKREPLY	String	리뷰답글내용
FEEDBACKREPLYTIME	String	리뷰답글시간 (값이 ""이면 답글 등록 x) yyyyMMddhhmmss
ANSWERCONTENTS	String	답변내용
ANSWERDATE	String	yyyyMMddhhmmss
*/
